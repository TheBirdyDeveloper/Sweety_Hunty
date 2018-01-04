package shape;

import java.awt.Color;
import java.awt.Graphics;

import candyLand.GridDisplay;
import candyLand.LocationOnGrid;

//This class defines the properties of the sweets.
public class Sweet extends AbstractShape{

	public Sweet(LocationOnGrid loc, GridDisplay display) {
		super(loc, display);
	}

	@Override
	public void drawShape(Graphics g, int x, int y, int w, int h) {
		g.setColor(Color.RED);
		g.fillOval(x,y,w,h);
	}


}
