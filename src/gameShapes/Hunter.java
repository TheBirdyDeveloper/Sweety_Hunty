package gameShapes;

import java.awt.Color;
import java.awt.Graphics;

import org.jgroups.Address;

import candyLand.LocationOnGrid;
import gameDisplay.GridDisplay;

public class Hunter extends AbstractShape{

	private int score = 0;
	private Address id_player;

	public Hunter(LocationOnGrid loc, GridDisplay display, Address id_player) {
		super(loc, display);
		this.id_player = id_player;
	}

	public void eatSweet() {
		score++;
	}

	public Address getId_player() {
		return id_player;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public void drawShape(Graphics g, int x, int y, int w, int h) {	
		g.setColor(Color.BLUE);
		g.fillRect(x,y,w,h);
	}


}
