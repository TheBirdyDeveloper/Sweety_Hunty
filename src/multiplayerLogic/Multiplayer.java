package multiplayerLogic;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.tools.DocumentationTool.Location;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.View;
import org.jgroups.stack.Configurator;

import candyLand.LocationOnGrid;
import gameDisplay.GridDisplay;
import gameShapes.Hunter;
import gameShapes.Sweet;

public class Multiplayer extends org.jgroups.ReceiverAdapter {

	String user_name=System.getProperty("user.name", "n/a");


	//attributs pour le jeu 

	int cellSize = 30;
	int gridSize = 30;
	public JFrame window = new GridDisplay(cellSize,gridSize, 15);
	int nbSweets = 15;
	Address playerId;

	JChannel channel;

	public void startSession() throws Exception {
		channel = new JChannel();
		channel.setDiscardOwnMessages(true);
		channel.setReceiver(this);
		channel.connect("Candy_Land");
		if (isFirstConnectedPerson()){
			((GridDisplay)this.window).initializeSweets(nbSweets);
		}
		eventLoop();

		channel.close();
	}

	private void eventLoop() {
		window.addKeyListener(new movementListener(this.playerId, this.window, this.channel));
		while(((GridDisplay)this.window).getSweet_array().size() > 0) {
			window.validate();
		} 
		Hunter myHunter = this.getMyHunter();
		System.out.println("Your final score is : " + myHunter.getScore());
		if (isWinner(myHunter)) {
			System.out.println("You Win !!!");
		}

	}

	public Hunter getMyHunter() {

		for (Hunter hunter : (((GridDisplay)this.window).getHunter_array())){
			if (hunter.getId_player().equals(playerId)){
				return hunter;
			}
		}

		return null;
	}

	private boolean isWinner(Hunter myHunter) {
		for (Hunter hunter : (((GridDisplay)this.window).getHunter_array())){
			if (myHunter.getScore() < hunter.getScore()){
				return false;
			}
		}

		return true;
	}


	public void startSessionTest() throws Exception {
		channel = new JChannel();
		channel.setDiscardOwnMessages(true);
		channel.setReceiver(this);
		channel.connect("Candy_Land");
		if (isFirstConnectedPerson()){
			((GridDisplay)this.window).initializeSweets(nbSweets);
		}
		eventLoopTest();
		channel.close();
	}

	private void eventLoopTest() throws Exception {
		window.addKeyListener(new movementListener(this.playerId, this.window, this.channel));
		Thread.sleep(5000);
		while(((GridDisplay)this.window).getSweet_array().size() > 0) {
			window.validate();
			Random rand = new Random();
			int direction = rand.nextInt(4);
			Robot robot = new Robot();
			switch(direction) {
			case 0:
				robot.keyPress(KeyEvent.VK_UP);
				robot.keyRelease(KeyEvent.VK_UP);
				break;
			case 1:
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyRelease(KeyEvent.VK_DOWN);
				break;
			case 2:
				robot.keyPress(KeyEvent.VK_LEFT);
				robot.keyRelease(KeyEvent.VK_LEFT);
				break;
			case 3:
				robot.keyPress(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_RIGHT);
				break;

			}
			window.repaint();
			Thread.sleep(10);

		}   
	}

	public static void main(String[] args) {
		try {

			new Multiplayer().startSession();
		} catch (Exception e) {
			System.out.println("Connexion impossible");
			e.printStackTrace();
		}
	}

	public void receive(Message msg) {
		Address addr = msg.getSrc();

		GameMessageContent messageContent = (GameMessageContent)msg.getObject();

		if (messageContent.getOpCode() < 5) {

			if (messageContent.getOpCode() == 1){
				//moves
				for (Hunter hunter : (((GridDisplay)this.window).getHunter_array())){
					if (hunter.getId_player().equals(addr)){
						hunter.getPos().setX(messageContent.getX());
						hunter.getPos().setY(messageContent.getY());
					}
				}
				((GridDisplay)this.window).getHunter_array().subList(0, ((GridDisplay)this.window).getHunter_array().size()-1);
				window.pack();
				window.revalidate();
				window.repaint();
			}

			if (messageContent.getOpCode() == 2){
				//sweet
				((GridDisplay)this.window).getSweet_array().add(new Sweet(new LocationOnGrid(messageContent.getX(), messageContent.getY()), (GridDisplay)this.window));
			}

			if (messageContent.getOpCode() == 3){
				//hunter
				((GridDisplay)this.window).getHunter_array().add(new Hunter(new LocationOnGrid(messageContent.getX(), messageContent.getY()), (GridDisplay)this.window, addr));
			}

			if (messageContent.getOpCode() == 4){
				for(int i = 0; i < ((GridDisplay)this.window).getSweet_array().size(); i++){
					Sweet sweet = ((GridDisplay)this.window).getSweet_array().get(i);
					if(sweet.getPos().getX() == messageContent.getX() && sweet.getPos().getY() == messageContent.getY()) {
						((GridDisplay)this.window).getSweet_array().remove(i);
						((GridDisplay)this.window).getMyContainer().remove(sweet);
						((GridDisplay)this.window).pack();
					}
				}
			}
		}

		// Score Message
		else if (messageContent.getOpCode() == 5) {

			for (Hunter hunter : (((GridDisplay)this.window).getHunter_array())){
				if (hunter.getId_player().equals(addr)){
					hunter.setScore(messageContent.getScore());;
				}
			}
		}

	}


	private boolean isFirstConnectedPerson(){
		return channel.getView().getMembers().get(0) == playerId;
	}

	private boolean isLastConnectedPerson(){
		return channel.getView().getMembers().get(channel.getView().getMembers().size()-1).equals(playerId);
	}


	public void viewAccepted(View new_view) {
		playerId = channel.address();
		//Send all information if first in the List of Hunters
		Address last = new_view.getMembers().get(new_view.getMembers().size()-1);

		if (isFirstConnectedPerson()){
			//send all messages
			for (Sweet sweet : (((GridDisplay)this.window).getSweet_array())){
				Message msg=new Message(last, new GameMessageContent(2, null, sweet.getPos().getX(), sweet.getPos().getY(), 0));
				try {
					channel.send(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		//If new player, create new Hunter and send it to all
		if (isLastConnectedPerson()){
			LocationOnGrid posNewHunter = addHunter(playerId);
			Message newMsg=new Message(null, new GameMessageContent(3, playerId.toString(), posNewHunter.getX(), posNewHunter.getY(), 0));

			try {
				channel.send(newMsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//If already in the game, send it to all
		else {
			int myPosX = -1;
			int myPosY = -1;

			for (Hunter hunter : (((GridDisplay)this.window).getHunter_array())){
				if (hunter.getId_player() == playerId){
					myPosX = hunter.getPos().getX();
					myPosY = hunter.getPos().getY();
				}
			}
			Message newMsg=new Message(last, new GameMessageContent(3, playerId.toString(), myPosX, myPosY, 0));

			try {
				channel.send(newMsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public LocationOnGrid addHunter(Address id_hunter) {
		return ((GridDisplay)this.window).addHunter(id_hunter);
	}


}