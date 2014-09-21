
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Peter Mösenthin.
 */
public class SeedManager {

    private SeedData seedData;
    private int[][] currentPopulation;
    private int[][] lastPopulation;
    private boolean repetitionDetected = false;
    private int seedPlaylistPosition = 0;
    private Seed currentSeed;



    // Make teh logger availiable
    private static Logger logger = Logger.getLogger("GameOfLife");


    public SeedManager(){
        seedData = new SeedData();
        seedData.loadSeeds();
    }


    /**
     * Loads and returns the next Seed in the SeedList. Repeats the loaded seeds infinitely.
     * @return
     */
    public Seed loadNext(){
        this.repetitionDetected = false;
        if(seedPlaylistPosition == 0){
            logger.log(Level.INFO,"Starting seed playlist");
        }else {
            logger.log(Level.INFO,"Switching to next Seed");
        }
        Seed nextSeed;
        if(seedPlaylistPosition < seedData.getSeedList().size()){
            currentSeed = nextSeed = seedData.getSeedList().get(seedPlaylistPosition);
        }else{
            seedPlaylistPosition = 0;
            currentSeed = nextSeed = seedData.getSeedList().get(seedPlaylistPosition);
        }

        seedPlaylistPosition++;
        return nextSeed;
    }

    /**
     * Updates the state of the current population.
     * Checks for repetition and sets a flag if the seed is repeating.
     * @param currentPopulation
     */
    public void setCurrentPopulation(int[][] currentPopulation){
        if(lastPopulation != null){
        lastPopulation = this.currentPopulation;
        }else {
            lastPopulation =
                    new int[GameOfLifeApplet.GRID_X][GameOfLifeApplet.GRID_Y];
        }
        this.currentPopulation = currentPopulation;
        if(!populationsDiffer(lastPopulation,currentPopulation)){
            this.repetitionDetected = true;
        }
    }

    /**
     * Checks if two arrays differ and returns true on first detected change
     * @param grid1
     * @param grid2
     * @return
     */
    private boolean populationsDiffer(int[][] grid1, int[][] grid2) {
        for (int i = 0; i < GameOfLifeApplet.GRID_Y; i++) {
            for (int j = 0; j < GameOfLifeApplet.GRID_X; j++) {
                if(grid1[j][i] != grid2[j][i]){
                    logger.log(Level.FINE,"Repetition detected");
                    return true;
                }
            }
        }
        return false;
    }

    public Seed getCurrentSeed(){
        return this.currentSeed;
    }

    public boolean repetitionDetected(){
        return repetitionDetected;
    }

}
