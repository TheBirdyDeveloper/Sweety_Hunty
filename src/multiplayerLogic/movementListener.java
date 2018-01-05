package multiplayerLogic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.peer.WindowPeer;

import javax.swing.JFrame;

import org.jgroups.Address;

import gameDisplay.GridDisplay;
import gameShapes.Hunter;
import gameShapes.Sweet;

public class movementListener implements KeyListener {

	Address playerId;
	JFrame window;

	public  movementListener(Address playId, JFrame windows) {
		this.playerId = playId;
		this.window = windows;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) { 

		case KeyEvent.VK_DOWN :
			for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
				if(hunter.getId_player().equals(playerId)) {
					if(hunter.getPos().getY() < 29 && isAvailable(hunter.getPos().getX(), (hunter.getPos().getY()+1))) {
						hunter.getPos().setY(hunter.getPos().getY()+1);
						isSweet(hunter.getPos().getX(), hunter.getPos().getY());
					}
					
				}
			}
			break;
		case KeyEvent.VK_UP :
			for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
				if(hunter.getId_player().equals(playerId)) {
					if(hunter.getPos().getY() > 0 && isAvailable(hunter.getPos().getX(), (hunter.getPos().getY()-1))) {
						hunter.getPos().setY(hunter.getPos().getY()-1);
						isSweet(hunter.getPos().getX(), hunter.getPos().getY());
					}
					
				}
			}
			break;
		case KeyEvent.VK_LEFT :
			for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
				if(hunter.getId_player().equals(playerId)) {
					if(hunter.getPos().getX() > 0 && isAvailable(hunter.getPos().getX()-1, (hunter.getPos().getY()))) {
						hunter.getPos().setX(hunter.getPos().getX()-1);
						isSweet(hunter.getPos().getX(), hunter.getPos().getY());
					}
					
				}
			}
			break;
		case KeyEvent.VK_RIGHT :
			for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
				if(hunter.getId_player().equals(playerId)) {
					if(hunter.getPos().getX() < 29 && isAvailable(hunter.getPos().getX()+1, (hunter.getPos().getY()))) {
						hunter.getPos().setX(hunter.getPos().getX()+1);
						isSweet(hunter.getPos().getX(), hunter.getPos().getY());
					}
					
				}
			}
			break;
		default: 
			break;
		}
		
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
		System.out.println("new Pos = " + x + " , " + y);
		for(Hunter hunter : ((GridDisplay)this.window).getHunter_array()) {
			if(hunter.getPos().getX() == x && hunter.getPos().getY() == y) {
				return false;
			}
		}
		return true;
	}
	
	private void isSweet(int x, int y) {
		for(int i = 0; i < ((GridDisplay)this.window).getSweet_array().size(); i++){
			Sweet sweet = ((GridDisplay)this.window).getSweet_array().get(i);
			if(sweet.getPos().getX() == x && sweet.getPos().getY() == y) {
				((GridDisplay)this.window).getSweet_array().remove(i);
				}
		}
		System.out.println(((GridDisplay)this.window).getSweet_array().size());
		
	}
	

}
