package shape;

import java.awt.Color;
import java.awt.Graphics;

import candyLand.GridDisplay;
import candyLand.LocationOnGrid;

public class Hunter extends AbstractShape{

	private int score = 0;
	private long id_player;

	public Hunter(LocationOnGrid loc, GridDisplay display, Long id_player) {
		super(loc, display);
		this.id_player = id_player;
	}

	public void eatSweet() {
		score++;
	}

	public int getScore() {
		return score;
	}

	@Override
	public void drawShape(Graphics g, int x, int y, int w, int h) {	
		g.setColor(Color.BLUE);
		g.fillRect(x,y,w,h);
	}


}
