import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MapDataDrawer
{
	/** the 2D array containing the elevations */
	private int[][] grid;

	/** constructor, parses input from the file into grid */
	public MapDataDrawer(String fileName) throws IOException
	{
		Scanner read = new Scanner(new File(fileName));
		int rows = read.nextInt();
		int cols = read.nextInt();
		grid = new int[rows][cols];

		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				grid[r][c] = read.nextInt();
			}
		}
	}

	/** @return the min value in the entire grid */
	public int findMin()
	{
		int min = grid[0][0];

		for(int r = 0; r < grid.length; r++) {
			for(int c = 0; c < grid[r].length; c++) {
				if(grid[r][c] < min) {
					min = grid[r][c];
				}
			}
		}

		return min;
	}

	/** @return the max value in the entire grid */
	public int findMax()
	{
		int max = grid[0][0];

		for(int r = 0; r < grid.length; r++) {
			for(int c = 0; c < grid[r].length; c++) {
				if(grid[r][c] > max) {
					max = grid[r][c];
				}
			}
		}

		return max;
	}

	/**
	 * Draws the grid using the given Graphics obrect.
	 * Colors should be grayscale values 0-255, scaled based on min/max values in grid
	 */
	public void drawMap(Graphics g)
	{
		int range = findMax() - findMin();
		double scale = 256.0 / range;
		double color = 0;

		for(int r = 0; r < grid.length; r++)
		{
			for(int c = 0; c < grid[r].length; c++)
			{
				color = scale * (grid[r][c] - findMin() - 1);
				g.setColor(new Color((int) color, (int) color, (int) color));
				g.fillRect(c, r, 1, 1);
			}
		}
	}

	/**
	 * Find a path from West-to-East starting at given row.
	 * Choose a forward step out of 3 possible forward locations, using greedy method described in assignment.
	 * @return the total change in elevation traveled from West-to-East
	 */
	public int drawLowestElevPath(Graphics g, int row)
	{
		g.setColor(new Color(255,0, 0));
		int e1 = 0;
		int e2 = 0;
		int e3 = 0;
		int count = 0;

		for(int r = 0; r < grid[0].length - 1; r++)
		{
			if(row >= 1 && row < grid.length - 1)
			{
				e1 = Math.abs(grid[row - 1][r + 1] - grid[row][r]);
				e2 = Math.abs(grid[row][r + 1] - grid[row][r]);
				e3 = Math.abs(grid[row + 1][r + 1] - grid[row][r]);
			}
			else if(row < 1)
			{
				e2 = Math.abs(grid[row][r + 1] - grid[row][r]);
				e3 = Math.abs(grid[row + 1][r + 1] - grid[row][r]);
				e1 = e2 + 1;
			}
			else
			{
				e1 = Math.abs(grid[row - 1][r + 1] - grid[row][r]);
				e2 = Math.abs(grid[row][r + 1] - grid[row][r]);
				e3 = e1 + 1;
			}

			if(Math.min(Math.min(e1, e2), e3) == e1)
			{
				row--;
				count += e1;
				g.fillRect(r, row, 1, 1);
			}
			else if(Math.min(Math.min(e1, e2), e3) == e2)
			{
				count += e2;
				g.fillRect(r, row, 1, 1);
			}
			else
			{
				row++;
				count += e3;
				g.fillRect(r, row, 1, 1);
			}
		}

		return count;
	}

	/** @return the index of the starting row for the lowest-elevation-change path in the entire grid. */
	public int indexOfLowestElevPath(Graphics g)
	{
		int index = 0;
		int a = 0;
		int path = drawLowestElevPath(g, index);

		for(int r = 1; r < grid.length; r++)
		{
			a = drawLowestElevPath(g, r);
			if(drawLowestElevPath(g, index) > a)
			{
				path = a;
				index = r;
			}
		}

		return index;
	}
	
	public int getRows()
	{
		return grid.length;
	}
	
	public int getCols()
	{
		return grid[0].length;
	}
}
