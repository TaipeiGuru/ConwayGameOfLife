/*
 * This Java Project is a simulation of Conway's Life. Conway's Life involves a grid of cells, each of which has a certain number of neighbors.
 * Each cell will live or die in the next round depending on how many neighbors it has. 
 * Programmer: Jason Randolph
 * Last Modified: 11/20/21
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;

 // Adding implements to be used later on in the code (event listeners and runnable)
public class ConwayLife implements MouseListener, ActionListener, Runnable {

	// Creating the cell boolean and JFrame/components
	boolean[][] cells = new boolean[25][25];
	JFrame frame = new JFrame("Conway's Life Simulation");
	ConwayLifePanel panel = new ConwayLifePanel(cells);
	Container south = new Container();
	JButton step = new JButton("Step");
	JButton start = new JButton("Start");
	JButton stop = new JButton("Stop");
	boolean running = false;
	
	/* This method calls the frame and adds each component, as well as the event listeners. The bulk of the code 
	is found in the event listener methods below. */
	public ConwayLife() {
		frame.setSize(600,600);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		panel.addMouseListener(this);
		south.setLayout(new GridLayout(1,3));
		south.add(step);
		step.addActionListener(this);
		south.add(start);
		start.addActionListener(this);
		south.add(stop);
		stop.addActionListener(this);
		frame.add(south, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	// Main method 
	public static void main(String[] args) {
		new ConwayLife();
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
	}

	@Override
	public void mousePressed(MouseEvent e) {		
	}

	// This is the one mouse listener that's used in the code. It changes the color of the cell (and thus its status from "alive" to "dead") 
	@Override
	public void mouseReleased(MouseEvent e) {
		
		/* 
		 * When the mouse is released, the panel width is divided by the number of cells to get the width of each
		 * column. This is repeated with the panel height and the number of rows. Then,
		 * the column integer is assigned to either the number of columns or the X coordinate of the 
		 * point at which the mouse was released. The X coordinate is divided by the width to get an integer and 
		 * not a decimal. This also holds true for the row integer. Lastly, the two integers are assigned to the 
		 * corresponding cell in the array, the color of that cell is reversed, and then the frame is repainted 
		 * so that these changes can become visible. 
		 */
		double width = (panel.getWidth() / cells[0].length);
		double height = (double)(panel.getHeight() / cells.length);
		int column = Math.min(cells[0].length - 1, (int)Math.floor(e.getX() / width));
		int row = Math.min(cells.length - 1, (int)Math.floor(e.getY() / height));
		cells[row][column] = !cells[row][column];
		frame.repaint(); 
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}

	@Override
	
	// This actionPerformed event listener manages the three buttons at the bottom of the panel.
	public void actionPerformed(ActionEvent e) {
		
		// If the step button is pressed, the step method is called. More about this below.
		if (e.getSource().equals(step)) {
			step();
		} 
		
		/* 
		 * If the start button is pressed, the running boolean will be set to true, which
		 * starts a loop. Then, a runnable thread is started, which contains the repeated running
		 * code.
		 * */
		if (e.getSource().equals(start)) {
			if(running == false) {
				running = true;
				Thread t = new Thread(this);
				t.start();
			}
		} 
		
		// If the stop button is pressed, the running boolean is set to false, which stops the loop.
		if (e.getSource().equals(stop)){
			running = false;
		}
	}
	
	@Override
	public void run() {
		while(running == true) {
			/* 
			 * This is the thread that runs when the "start" button is pressed. This is needed because otherwise 
			 * the new neighbors would be calculated prior to refreshing the panel, which would affect how the game 
			 * is played. Using a run method allows for neighbors to be checked for each step method, thus preventing cells
			 * from being changed prematurely. 
			 * */
			step();
			
			// Try/catch exception to prevent errors.
			try {
				
				// Telling the thread to sleep for 500ms so that the changes don't happen as quickly and are easier to view by the player.
				Thread.sleep(500);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	// Here's the most important part of the code: the step method. 
	public void step() {
		
		// A new boolean array is created; it'll serve as the "board" for the next round. 
		boolean[][] nextCells = new boolean[cells.length][cells[0].length]; 
		
		/* 
		 * For each row and column (basically each cell), these loops check for neighbors in adjacent cells. 
		 * In each 3x3 grid around the cell, if any of those adjacent cells are true (alive), the neighbor count 
		 * will be increased by 1.
		 * */
		
		for (int row = 0; row < cells.length; row++) {
			for (int column = 0; column < cells[0].length; column++) {
				
				// Resetting neighborCount to 0 for each cell to prevent overlap.
				int neighborCount = 0;
				
				/* 
				 * Different parameters are specified in these if statements. This is done because some cells (like the ones 
				 * in orders or on the edges of the grid) don't have neighboring cells in certain locations. To avoid this error,
				 * the code requires that certain cells must have certain requirements (such as the previous column not being 
				 * checked if the selected cell is in column 0). 
				 * */
				if(row > 0 && column > 0 && cells[row-1][column-1] == true) {
					neighborCount++;
				}
				if(row > 0 && cells[row-1][column] == true) {
					neighborCount++;
				}
				if(row > 0 && column < cells[0].length-1 && cells[row-1][column+1] == true) {
					neighborCount++;
				}
				if(column > 0 && cells[row][column-1] == true) {
					neighborCount++;
				}
				if(column < cells[0].length-1 && cells[row][column+1] == true) {
					neighborCount++;
				}
				if(row < cells.length-1 && column > 0 && cells[row+1][column-1] == true) {
					neighborCount++;
				}
				if(row < cells.length-1 && cells[row+1][column] == true) {
					neighborCount++;
				}
				if(row < cells.length-1 && column < cells[0].length-1 && cells[row+1][column+1] == true) {
					neighborCount++;
				}
				
				/* If the selected cell is alive and there are 2 or 3 neighbors, the cell will stay alive in the next round (by keeping the boolean
				 * true). If not, it'll be set to false. 
				 */
				if(cells[row][column] == true) {
					if(neighborCount == 2 || neighborCount == 3) {
						nextCells[row][column] = true;
					} else {
						nextCells[row][column] = false;
					}
				
				/* 
				 * If the cell is dead (boolean = false) but there are 3 neighbors, the cell will be alive (boolean = true)
				 * in the next round. Otherwise, it stays dead.
				 * */
				} else {
					if(neighborCount == 3) {
						nextCells[row][column] = true;
					} else {
						nextCells[row][column] = false;
					}
				}
			}
		}
		// The current boolean array is updated to the new array that was calculated above. This is updated in the panel and then the frame is repainted.
		cells = nextCells;
		panel.setCells(nextCells);
		frame.repaint();
	}
}
