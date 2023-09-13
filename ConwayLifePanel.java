/* 
 * This is the panel setup for Conway's life.
 * Programmer: Jason Randolph
 * Last Modified: 11/20/21 
 */
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ConwayLifePanel extends JPanel {
	
	// Defining boolean array, width and height. 
	boolean[][] cells;
	double width;
	double height;
	
	// "Importing" the array from the ConwayLife class so that it can be drawn here.
	public ConwayLifePanel(boolean[][] in) {
		cells = in;
	}
	
	// Setting the cell values using the setCells method (which is required in the step function in the ConwayLife class).
	public void setCells(boolean[][] newcells) {
		cells = newcells;
	}
	
	// Graphics generator (making the rows and columns).
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Setting width to the width of the panel divided by the number of columns (and same for the rows). 
		width = (double)this.getWidth() / cells[0].length;
		height = (double)this.getHeight() / cells.length;
		
		// Setting the color of the cells. 
		g.setColor(new Color(88, 164, 176));
		
		/* The fillRect function actually fills each rectangle with the specified color. For each cell, if it's alive (boolean
		 * = true), a rectangle with the corners at the four parameters is colored. The Math.round function is used to prevent 
		 * odd white spaces from appearing that might otherwise be the result of imprecise math when taking the coordinates of
		 * the cell. 
		 * */
		for (int row = 0; row < cells.length; row++) {
			for (int column = 0; column < cells[0].length; column++) {
				if(cells[row][column] == true) {
					g.fillRect((int)Math.round(column*width),(int)Math.round(row*height), (int)width+1, (int)height+1);
					
				}
			}
		}
		
		// Setting the color of the bordering lines.
		g.setColor(new Color(55, 63, 81));
		
		/* 
		 * A for loop is used to create each line. The line is drawn from the width multiplied by the value
		 * of x in the for the loop (and y = 0) to the same x coordinate with y being the height of the panel.
		 * This process is repeated for the horizontal lines but with the height and width instead. 
		 * */
		for (int x = 0; x < cells[0].length + 1; x++) {
			g.drawLine((int)Math.round(x*width), 0, (int)Math.round(x*width), this.getHeight());
		}
		for (int y = 0; y < cells[0].length + 1; y++) {
			g.drawLine(0, (int)Math.round(y*height), this.getWidth(), (int)Math.round(y*height));
		}
	}
}
