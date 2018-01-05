package multiplayerLogic;
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
		//playerId = channel.address(); //own addr
		if (isFirstConnectedPerson()){
			((GridDisplay)this.window).initializeSweets(nbSweets);
		}
		eventLoop();
		channel.close();
	}
	
	private void eventLoop() {
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
		 System.out.println(msg.getSrc() + ": " + msg.getObject());
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
		if (isFirstConnectedPerson()){
			Address last = new_view.getMembers().get(new_view.getMembers().size()-1);
			//send all messages
			Message msg=new Message(last, "15 sweets left");
			
			try {
				channel.send(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//If new player, create new Hunter and send it to all
		if (isLastConnectedPerson()){
			addHunter(new_view.getViewId().getId());
			Message newMsg=new Message(null, "I am newbie");
			
			try {
				channel.send(newMsg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addHunter(Long id_hunter) {
		((GridDisplay)this.window).addHunter(id_hunter);
	}
}