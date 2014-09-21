import java.awt.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.core.PImage;
import hsrw.illumination.client.intern.APIClientIntern;


/**
 * Created by Peter Mösenthin.
 */
@SuppressWarnings("serial")
public class GameOfLifeApplet extends PApplet {

	// #############################################
	// SETTINGS
	// #############################################
	
	// Bounds of the grid/display
	public static int GRID_X = 9;
	public static int GRID_Y = 14;
	
	// Scaling factor for the processing window
	public static final float SCALE_FACTOR = 3.0f;
	// Sets the framerate
	public static final int FRAMERATE = 3;
	
	// These values are used to display the debugging grid
	// and won't affect the actual animation
	public static final int COLUMN_GAP = 5;
	public static final int GRID_OFFSET = 10;

	// Setup for the server
	APIClientIntern client;
	PImage serverImage;
	// If this is true a picture will be sent every frame to the server
	// This is used to prevent Exceptions for testing if server is unreachable
	public boolean serverAPI = true;

    private static final String SERVER_IP = "192.168.0.120";
    //private static final String SERVER_IP = "testserver.kamp-lintfort-leuchtet.de";
    private static final int SERVER_PORT = 3001;
    private static final String SERVER_LOGIN = "PeterVonOz";
    private static final String SERVER_PASSWIORD = "12345678";
    private static final String SCHEDULE_NAME = "GameOfLife";

    /**
     * Timeout if a seed is repeating in seconds
     */
    public static final float SEED_REPEATING_TIMEOUT = 3f;
    /**
     * Timeout if a seed is not repeating in seconds
     */
    public static final float SEED_TIMEOUT = 6f;

    private float seedTimer = 0;

	// Game instance
	public GameOfLife gameOfLifeInstance;

	// Logger and log level
	// Note: Most of the logs are set to FINE or in some cases to FINEST
	// INFO is used for... information
	public static final Level LOG_LEVEL = Level.INFO;
	private static Logger logger = Logger.getLogger("GameOfLife");
	// Counters
	public int frameCount = 0;
	public int stepCount = 0;

	int[][] golGrid;
    SeedManager seedManager = new SeedManager();




    // #############################################
	// PROCESSING SPECIFIC METHODS
	// #############################################

	public void setup() {
		// Set Log-level
		System.out.println("LOGGING LEVEL: " + LOG_LEVEL);
		LogManager.getLogManager().getLogger("GameOfLife").setLevel(LOG_LEVEL);
		
		// Window size
		size(GRID_X * (int) (15 * SCALE_FACTOR), GRID_Y
				* (int) (12 * SCALE_FACTOR));
		logger.log(Level.INFO, "GOL started on a "+GRID_X+"x"+GRID_Y+" grid");
		
		// Create a golGrid for debugging purposes
		golGrid = new int[GRID_X][GRID_Y];
		
		// Set framerate for processing engine
		frameRate(FRAMERATE);
		
		// Disable AA to prevent weird fading
		noSmooth();
		
		// This image will be sent to the server
		serverImage = createImage(GRID_X, GRID_Y, RGB);

		// Log into the server
		if (serverAPI) {
            client = new APIClientIntern(SERVER_IP,
                    SERVER_PORT,
                    SERVER_LOGIN,
                    SERVER_PASSWIORD);
            GRID_X = client.getViewportWidth();
            GRID_Y = client.getViewportHeight();
            client.scheduleAddApplication(SCHEDULE_NAME);
            client.createCanvas(GRID_X, GRID_Y);
		}

		// Create a new GameOfLife with the 
		// initial grid that should be set by now
		gameOfLifeInstance =
                new GameOfLife(seedManager.loadNext().getSeedGrid());
	}

	/**
	 * Called every frame to update and display everything
	 */
	public void draw() {
		logger.log(Level.FINEST, "Frame " + frameCount);
		
		// static output
		background(150);
		scale(SCALE_FACTOR);
		translate(GRID_OFFSET, GRID_OFFSET);
		stroke(0);
		fill(150);
		drawGridLines();


		// Reset the image
		serverImage = createImage(GRID_X, GRID_Y, RGB);

		// Handle game of life seeds
		logger.log(Level.FINE, "Requesting next population");
		golGrid = gameOfLifeInstance.getNextGrid();
		stepCount++;
        seedManager.setCurrentPopulation(golGrid);
        // Check for timeouts
        if(seedTimer < SEED_TIMEOUT){
            if(seedManager.repetitionDetected()){
               if(seedTimer >= SEED_REPEATING_TIMEOUT){
                   logger.log(Level.INFO,"Repeating seed timed out ");
                   gameOfLifeInstance.currentGrid = seedManager.loadNext().getSeedGrid();
                   seedTimer = 0;
               }
            }
            seedTimer += 1/ (float)FRAMERATE;
        }else {
            logger.log(Level.INFO,"Seed timed out ");
            gameOfLifeInstance.currentGrid = seedManager.loadNext().getSeedGrid();
            seedTimer = 0;
        }


		
		// Draw the current population to the debug grid and the server image
		drawGrid(golGrid);

        drawOutputImage(golGrid);
		image(serverImage, GRID_X * 12, GRID_Y);

		// Send frame to the server
		if (serverAPI) {
			client.drawCanvas(serverImage);
		}

		// Count frames
		frameCount++;
	}

	// ##########################################
	// # METHODS
	// ##########################################


	/**
	 * Draws lines to display a grid in which the grid pixel can be displayed
	 * 
	 * A single "pixel" is 10px wide 
	 * Scaling is done through the engine
	 */
	private void drawGridLines() {
        stroke(200,200,200);
		int gap = 0;
		for (int i = 0; i < GRID_X; i++) {
			if (i % 3 == 0) {
				gap += COLUMN_GAP;
			}
			for (int j = 0; j < GRID_Y; j++) {
				rect(i * 10 + gap, j * 10, 10, 10);
			}
		}
	}

    /**
     * Draws the image that will be sent to server
     * Image size is set by GRID_X and GRID_Y
     *
     * @param grid
     */
    private void drawOutputImage(int[][] grid) {
        int count = 0;
        for (int i = 0; i < GRID_Y; i++) {
            for (int j = 0; j < GRID_X; j++) {
                if (grid[j][i] == 0) {
                    serverImage.pixels[count] = color(0, 0, 0);
                }else if (grid[j][i] == Seed.CELL_LIVING) {
                    Color c = seedManager.getCurrentSeed()
                            .getColor(Seed.CELL_COLOR_LIVING);
                    serverImage.pixels[count] = color(
                            c.getRed(),
                            c.getGreen(),
                            c.getBlue());
                }else if(grid[j][i] == Seed.CELL_NEW){
                    Color c = seedManager.getCurrentSeed()
                            .getColor(Seed.CELL_COLOR_NEW);
                    serverImage.pixels[count] = color(
                            c.getRed(),
                            c.getGreen(),
                            c.getBlue());
                }else if(grid[j][i] == Seed.CELL_DYING){
                    Color c = seedManager.getCurrentSeed()
                            .getColor(Seed.CELL_COLOR_DYING);
                    serverImage.pixels[count] = color(
                            c.getRed(),
                            c.getGreen(),
                            c.getBlue());
                }
                count++;
            }
        }
    }
	

	/**
	 * Draws the current seed to the debug grid and the serverimage
	 */
	private void drawGrid(int[][] frame) {
		int gap = 0;
		for (int i = 0; i < frame.length; i++) {
			if (i % 3 == 0) {
				gap += COLUMN_GAP;
			}
			for (int j = 0; j < frame[0].length; j++) {
                if(frame[i][j] == 0){
                drawGridPosition(i,j,gap,new Color(0,0,0));
                }else if (frame[i][j] == Seed.CELL_LIVING) {
				drawGridPosition(i,j,gap,
                        seedManager.getCurrentSeed()
                                .getColor(Seed.CELL_COLOR_LIVING));
                }else if(frame[i][j] == Seed.CELL_NEW){
                drawGridPosition(i,j,gap,
                        seedManager.getCurrentSeed()
                                .getColor(Seed.CELL_COLOR_NEW));
                }else if(frame[i][j] == Seed.CELL_DYING){
                drawGridPosition(i,j,gap,
                        seedManager.getCurrentSeed()
                                .getColor(Seed.CELL_COLOR_DYING));
                }
			}
		}

	}

    private void drawGridPosition(int x, int y, int gap,Color c){
            color(c.getRed(),c.getGreen(),c.getBlue());
            fill(c.getRed(),c.getGreen(),c.getBlue());
            stroke(c.getRed(),c.getGreen(),c.getBlue());
            rect(x * 10 + 1 + gap, y * 10 + 1, 8, 8);
    }

}
