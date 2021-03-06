package gameDisplay;

import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JList;

import org.jgroups.Address;

import candyLand.LocationOnGrid;
import gameShapes.Hunter;
import gameShapes.Sweet;

public class GridDisplay extends JFrame{


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
	}

	public Container getMyContainer() {
		return myContainer;
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
	
	public void initializeSweets(int nbSweets){
		while (this.sweet_array.size() < nbSweets){
			this.sweet_array.add(new Sweet(this.computeFreeSpot(), this));

		}		
	}
	
	public LocationOnGrid addHunter(Address id_player) {
		Hunter newHunter = new Hunter(this.computeFreeSpot(), this, id_player);
		this.hunter_array.add(newHunter);
		return newHunter.getPos();
	}
	
	private LocationOnGrid computeFreeSpot() {
		boolean posOk = false;
		boolean posOkSweet = false;
		int x = 0;
		int y = 0;
		while(!(posOk && posOkSweet)) {
			x = (int) (Math.random() * gridSize);
			y = (int) (Math.random() * gridSize);
			posOk = isAvailable(x, y);
			posOkSweet = isAvailableSweet(x, y);
		}
		
		return new LocationOnGrid(x, y);
		
	}
	
	private boolean isAvailable(int x, int y) {
		for(Hunter hunter : hunter_array) {
			if(hunter.getPos().getX() == x && hunter.getPos().getY() == y) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isAvailableSweet(int x, int y) {
		for(Sweet sweet : sweet_array) {
			if(sweet.getPos().getX() == x && sweet.getPos().getY() == y) {
				return false;
			}
		}
		return true;
	}



	
	public ArrayList<Sweet> getSweet_array() {
		return sweet_array;
	}

	public void setSweet_array(ArrayList<Sweet> sweet_array) {
		this.sweet_array = sweet_array;
	}

	public ArrayList<Hunter> getHunter_array() {
		return hunter_array;
	}

	public void setHunter_array(ArrayList<Hunter> hunter_array) {
		this.hunter_array = hunter_array;
	}
	

}
