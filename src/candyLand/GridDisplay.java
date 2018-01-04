package candyLand;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.jgroups.tests.rt.transports.JGroupsTransport;

import shape.Hunter;
import shape.Sweet;

public class GridDisplay extends JFrame implements KeyListener {


	private int cellSize;
	private int gridSize;

	Map<Integer,int[]> moveTable = new HashMap<Integer,int[]>() ;
	Container myContainer ;

	ArrayList<Sweet> sweet_array;
	ArrayList<Hunter> hunter_array;
	
	public GridDisplay(int cellSize, int gridSize, int nbSweets) {
		super();
		this.sweet_array = new ArrayList<Sweet>();
		this.hunter_array = new ArrayList<Hunter>();
		this.cellSize = cellSize;
		this.gridSize = gridSize;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(600, 50);
		myContainer = getContentPane();
		myContainer.setPreferredSize(new Dimension(cellSize * (gridSize + 1), cellSize * (gridSize + 1) ));
		pack();
		setVisible(true);
		
		//Grid initialization
		initializeSweets(nbSweets);
	}

	public int getCellSize() {
		return cellSize;
	}
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	public int getGridSize() {
		return gridSize;
	}
	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}
	
	private void initializeSweets(int nbSweets){
		while (this.sweet_array.size() < nbSweets){
			this.sweet_array.add(new Sweet(new LocationOnGrid((int) (Math.random()*gridSize), (int) (Math.random()*gridSize)), this));
		}		
	}
	
	public void addHunter(Long id_player) {
		this.hunter_array.add(new Hunter(this.computeFreeSpot(), this, id_player));
	}
	
	private LocationOnGrid computeFreeSpot() {
		boolean posOk = false;
		int x = 0;
		int y = 0;
		while(!posOk) {
			x = (int) (Math.random() * gridSize);
			y = (int) (Math.random() * gridSize);
			posOk = isAvailable(x, y);
			System.out.println(posOk);
		}
		
		return new LocationOnGrid(x, y);
		
	}
	
	private boolean isAvailable(int x, int y) {
		System.out.println("new Pos = " + x + " , " + y);
		for(Hunter hunter : hunter_array) {
			if(hunter.getPos().getX() == x && hunter.getPos().getY() == y) {
				return false;
			}
		}
		return true;
	}

//	@Override
//	public void keyPressed (KeyEvent ke){ 
//		switch (ke.getKeyCode()) { 
//		case KeyEvent.VK_DOWN : 
//			if()
//			break;
//		case KeyEvent.VK_UP :
//			
//			break;
//		case KeyEvent.VK_LEFT :
//			
//			break;
//		case KeyEvent.VK_RIGHT :
//			
//			break;
//		default: 
//			break;
//		}
//	
//		
//		 int keyCode = ke.getKeyCode();
//		 if (!moveTable.containsKey(keyCode)) return ;
//		 
//		 
//		 myRectangle.moveRect(moveTable.get(keyCode));
//		 if (gameMap[myRectangle.x][myRectangle.y]!=null) {
//		   Circle c = gameMap[myRectangle.x][myRectangle.y];
//		   myContainer.remove(c);
//		   pack();
//		   gameMap[myRectangle.x][myRectangle.y]=null;
//		   numberOfSweets--;
//		   if (numberOfSweets==0) {
//		     System.out.println("You've won. Congratulations!");
//		     System.exit(0);
//		   }
//		   System.out.println("Only "+numberOfSweets+" sweet(s) remaining...");
//		 }
//		 repaint();
//		} // EndMethod keyPressed

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
