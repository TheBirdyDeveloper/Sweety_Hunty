package shape;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

import candyLand.GridDisplay;
import candyLand.LocationOnGrid;

public abstract class AbstractShape extends JComponent {

/* Position of the shape in the grid */
private LocationOnGrid location;

private GridDisplay grid;

public AbstractShape(LocationOnGrid loc, GridDisplay display) {
	grid = display;
	grid.add(this);
	grid.pack();
	this.location = loc;
}

public void setGridPos(int someX,int someY) {
	 this.location = new LocationOnGrid(someX, someY);
	}

public LocationOnGrid getPos() {
	return location;
}

abstract public void drawShape(Graphics g,int x,int y,int w,int h);

/* delegates drawing proper to drawShape. Transform the grid
* coordinates of the shape into pixel coordinates, using the cell
* size of the ExampleDisplay associated with the AbstractGridShape */
public void paint(Graphics g) {
 this.drawShape(g,
                grid.getCellSize()/2 + this.location.getX()*grid.getCellSize(), 
                grid.getCellSize()/2 + this.location.getY()*grid.getCellSize(), 
                grid.getCellSize(), grid.getCellSize());
}

}
