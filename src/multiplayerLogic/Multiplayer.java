package multiplayerLogic;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

	JChannel channel;
	String user_name=System.getProperty("user.name", "n/a");


	//attributs pour le jeu 

	int cellSize = 30;
	int gridSize = 30;
	JFrame window = new GridDisplay(cellSize,gridSize, 15);
	int nbSweets = 15;
	Address playerId;


	private void startSession() throws Exception {
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
		window.addKeyListener(new movementListener(this.playerId, this.window));
		 BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		    while(true) {
		        try {
		            System.out.print("> "); System.out.flush();
		            String line=in.readLine().toLowerCase();
		            if(line.startsWith("quit") || line.startsWith("exit"))
		                break;
		            line="[" + user_name + "] " + line;
		            Message msg=new Message(null, line);
		            channel.send(msg);
		        }
		        catch(Exception e) {
		        }
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
		//System.out.println(msg.getSrc() + ": " + msg.getObject());
		Address addr = msg.getSrc();
		GameMessageContent messageContent = (GameMessageContent)msg.getObject();
		System.out.println(msg.getSrc() + ": " + messageContent.getOpCode() + " " + messageContent.getId() +" "+ messageContent.getX() +" "+ messageContent.getY());


		if (messageContent.getOpCode() == 1){
			//moves
		}

		if (messageContent.getOpCode() == 2){
			((GridDisplay)this.window).getSweet_array().add(new Sweet(new LocationOnGrid(messageContent.getX(), messageContent.getY()), (GridDisplay)this.window));
		}

		if (messageContent.getOpCode() == 3){
			((GridDisplay)this.window).getHunter_array().add(new Hunter(new LocationOnGrid(messageContent.getX(), messageContent.getY()), (GridDisplay)this.window, addr));
		}
		/*
		 * decode avec un switch sur l'OP code
		 */
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
				Message msg=new Message(last, new GameMessageContent(2, null, sweet.getPos().getX(), sweet.getPos().getY()));
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
			Message newMsg=new Message(null, new GameMessageContent(3, playerId.toString(), posNewHunter.getX(), posNewHunter.getY()));

			try {
				channel.send(newMsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//If new player, create new Hunter and send it to all
		if (isLastConnectedPerson() && !isFirstConnectedPerson()){
			LocationOnGrid posNewHunter = addHunter(playerId);
			Message newMsg=new Message(null, new GameMessageContent(3, playerId.toString(), posNewHunter.getX(), posNewHunter.getY()));

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
			Message newMsg=new Message(last, new GameMessageContent(3, playerId.toString(), myPosX, myPosY));

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