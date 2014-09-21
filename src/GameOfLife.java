import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Peter Mösenthin.
 */
public class GameOfLife {
	

	// These arrays hold the information about the
	// current and the next grid
	int[][] currentGrid;
	int[][] nextGrid;
	
	// Make teh logger availiable
	private static Logger logger = Logger.getLogger("GameOfLife");
	
	
	/**
	 * Constructor for a 'Game of life'
	 * Pass the seed/inital grid  
	 * @param initialGrid
	 */
	public GameOfLife(int[][] initialGrid){
	this.currentGrid = initialGrid;	
	nextGrid = new int[currentGrid.length][currentGrid[0].length];
	logger.log(Level.INFO, "New GameOfLife Instance initialized");
	}
	
	
	/**
	 * Calculates next population state
	 * 
	 * A cell dies with less than two or more than three neighbours.
	 * A cell stays alive if it has exactly two or three neighbours.
	 * A dead cell comes to life if it has three neighbours. 
	 * 
	 * @return next population 
	 */
	public int[][] getNextGrid(){
	resetGrid(nextGrid);
	int neighbours;
	for(int i=0; i< currentGrid.length;i++ ){
		for(int j=0; j< currentGrid[0].length; j++){
			neighbours = countNeighbours(i,j);

            //Cell dies
			if(neighbours < 2 && isLivingCell(currentGrid[i][j])){
				nextGrid[i][j] = Seed.CELL_DYING;
            // Still lives
			} else if(((neighbours == 2) || (neighbours == 3)) && isLivingCell(currentGrid[i][j])){
				nextGrid[i][j] = Seed.CELL_LIVING;

            //Cell dies
			} else if(neighbours > 3 && isLivingCell(currentGrid[i][j])){
				nextGrid[i][j] = Seed.CELL_DYING;

            // Cell is born
			} else if(neighbours == 3 && isDeadCell(currentGrid[i][j])){
				nextGrid[i][j] = Seed.CELL_NEW;
			}
		}
	}
	currentGrid = nextGrid;
	logger.log(Level.FINEST, "Next population calculated");
	return nextGrid;
	}


    private boolean isDeadCell(int cell){
        if(cell == Seed.CELL_DYING || cell == 0){
            return true;
        }else{
            return false;
        }
    }

    private boolean isLivingCell(int cell){
        if(cell == Seed.CELL_LIVING || cell == Seed.CELL_NEW){
            return true;
        }else {
            return false;
        }
    }


	
	/**
	 * Resets the grid for the next population
	 * @param grid
	 */
	private void resetGrid(int[][] grid){
		nextGrid = new int[currentGrid.length][currentGrid[0].length];
	}



	/**
	 * Counts the neighbours to a gridposition 
	 * Note: Steps over to the beginning when index is out of bounds
	 * @param x
	 * @param y
	 * @return
	 */
	private int countNeighbours(int x, int y){
		int neighbours = 0;
		if (isLivingCell(currentGrid[getX(x+1)][getY(y-1)])){
			neighbours += 1;
		}
		if (isLivingCell(currentGrid[getX(x+1)][getY(y)])){
			neighbours += 1;
		}
		if (isLivingCell(currentGrid[getX(x+1)][getY(y+1)])){
			neighbours += 1;
		}
		if (isLivingCell(currentGrid[getX(x)][getY(y+1)])){
			neighbours += 1;
		}
		if (isLivingCell(currentGrid[getX(x-1)][getY(y+1)])){
			neighbours += 1;
		}
		if (isLivingCell(currentGrid[getX(x-1)][getY(y)])){
			neighbours += 1;
		}
		if (isLivingCell(currentGrid[getX(x-1)][getY(y-1)])){
			neighbours += 1;
		}
		if (isLivingCell(currentGrid[getX(x)][getY(y-1)])){
			neighbours += 1;
		}
		return neighbours;
	}
	
	
	/**
	 * Returns corrected value of the x-coordinate when an index 
	 * is out of bounds
	 * @param x
	 * @return corrected value
	 */
	private int getX(int x){
		if (x == currentGrid.length){
			x = 0;
		} else if (x == -1){
			x = currentGrid.length-1;
		}
		return x;
	}
	
	/**
	 * Returns corrected value of the y-coordinate when an index 
	 * is out of bounds
	 * @param y 
 	 * @return corrected value
	 */
	private int getY(int y){
		if (y == currentGrid[0].length){
			y = 0;
		} else if (y == -1){
			y = currentGrid[0].length-1;
		}
		return y;
	}
}
