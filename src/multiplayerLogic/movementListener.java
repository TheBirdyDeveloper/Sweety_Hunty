package multiplayerLogic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.peer.WindowPeer;

import javax.swing.JFrame;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;

import gameDisplay.GridDisplay;
import gameShapes.Hunter;
import gameShapes.Sweet;

public class movementListener implements KeyListener {

	Address playerId;
	JFrame window;
	JChannel channel;

	public  movementListener(Address playId, JFrame windows, JChannel channel) {
		this.playerId = playId;
		this.window = windows;
		this.channel = channel;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) { 

		case KeyEvent.VK_DOWN :
			for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
				if(hunter.getId_player().equals(playerId)) {
					if(hunter.getPos().getY() < 29 && isAvailable(hunter.getPos().getX(), (hunter.getPos().getY()+1))) {
						hunter.getPos().setY(hunter.getPos().getY()+1);
						isSweet(hunter);

						Message newMsg=new Message(null, new GameMessageContent(1, playerId.toString(), hunter.getPos().getX(), hunter.getPos().getY(), 0));
						try {
							channel.send(newMsg);
						} catch (Exception exception) {
							// TODO Auto-generated catch block
							exception.printStackTrace();
						}
					}

				}
			}
			break;
		case KeyEvent.VK_UP :
			for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
				if(hunter.getId_player().equals(playerId)) {
					if(hunter.getPos().getY() > 0 && isAvailable(hunter.getPos().getX(), (hunter.getPos().getY()-1))) {
						hunter.getPos().setY(hunter.getPos().getY()-1);
						isSweet(hunter);

						Message newMsg=new Message(null, new GameMessageContent(1, playerId.toString(), hunter.getPos().getX(), hunter.getPos().getY(), 0));
						try {
							channel.send(newMsg);
						} catch (Exception exception) {
							// TODO Auto-generated catch block
							exception.printStackTrace();
						}
					}

				}
			}
			break;
		case KeyEvent.VK_LEFT :
			for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
				if(hunter.getId_player().equals(playerId)) {
					if(hunter.getPos().getX() > 0 && isAvailable(hunter.getPos().getX()-1, (hunter.getPos().getY()))) {
						hunter.getPos().setX(hunter.getPos().getX()-1);
						isSweet(hunter);

						Message newMsg=new Message(null, new GameMessageContent(1, playerId.toString(), hunter.getPos().getX(), hunter.getPos().getY(), 0));
						try {
							channel.send(newMsg);
						} catch (Exception exception) {
							// TODO Auto-generated catch block
							exception.printStackTrace();
						}
					}

				}
			}
			break;
		case KeyEvent.VK_RIGHT :
			for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
				if(hunter.getId_player().equals(playerId)) {
					if(hunter.getPos().getX() < 29 && isAvailable(hunter.getPos().getX()+1, (hunter.getPos().getY()))) {
						hunter.getPos().setX(hunter.getPos().getX()+1);
						isSweet(hunter);

						Message newMsg=new Message(null, new GameMessageContent(1, playerId.toString(), hunter.getPos().getX(), hunter.getPos().getY(), 0));
						try {
							channel.send(newMsg);
						} catch (Exception exception) {
							// TODO Auto-generated catch block
							exception.printStackTrace();
						}
					}

				}
			}
			break;
		default: 
			break;
		}

		window.revalidate();
		window.repaint();
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	private boolean isAvailable(int x, int y) {
		for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
			if(hunter.getPos().getX() == x && hunter.getPos().getY() == y) {
				return false;
			}
		}
		return true;
	}

	private void isSweet(Hunter hunter) {
		int x =hunter.getPos().getX();
		int y = hunter.getPos().getY();

		for(int i = 0; i < ((GridDisplay)this.window).getSweet_array().size(); i++){
			Sweet sweet = ((GridDisplay)this.window).getSweet_array().get(i);
			if(sweet.getPos().getX() == x && sweet.getPos().getY() == y) {


				Message newMsg=new Message(null, new GameMessageContent(4, playerId.toString(), x, y, 0));
				try {
					channel.send(newMsg);
					hunter.eatSweet();
					Message newMsgEat=new Message(null, new GameMessageContent(5, playerId.toString(), -1, -1, hunter.getScore()));
					
					try {channel.send(newMsgEat);}
					catch(Exception exceptionScore){
						exceptionScore.printStackTrace();
					}

					((GridDisplay)this.window).getSweet_array().remove(i);
					((GridDisplay)this.window).getMyContainer().remove(sweet);
					((GridDisplay)this.window).pack();
				} catch (Exception exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}

			}

		}

	}


}
