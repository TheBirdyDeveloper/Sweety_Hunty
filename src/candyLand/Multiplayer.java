package candyLand;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.tools.DocumentationTool.Location;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.View;
import org.jgroups.stack.Configurator;

import shape.Hunter;
import shape.Sweet;

public class Multiplayer extends org.jgroups.ReceiverAdapter {

JChannel channel;
String user_name=System.getProperty("user.name", "n/a");

//attributs pour le jeu 

int cellSize = 30;
int gridSize = 30;
JFrame window = new GridDisplay(cellSize,gridSize, 15);

	private void startSession() throws Exception {
		channel = new JChannel();
		channel.setReceiver(this);
		channel.connect("Candy_Land");
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
	
	public void viewAccepted(View new_view) {
	    //Add a hunter to the array of Hunters.
		addHunter(new_view.getViewId().getId());
	}
	
	public void addHunter(Long id_hunter) {
		((GridDisplay)this.window).addHunter(id_hunter);
	}
}
