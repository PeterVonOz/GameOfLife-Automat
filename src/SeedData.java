import processing.core.PImage;

import java.awt.*;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by Peter Mösenthin.
 */
public class SeedData {

    private LinkedList<Seed> seedList = new LinkedList<Seed>();

    // Make the logger availiable
    private static Logger logger = Logger.getLogger("GameOfLife");

    public SeedData(){
    }


    /**
     * Currently all Seeds are instantiated in this method.
     * Usage:
     * 1.Create a new seed object and pass an array indicating the first population.
     * 2.Set every color individually or set all colors at once
     * 3.Add the seed to the seedlist
     */
    public void loadSeeds(){
        // Sample colors for testing
        // create color "pallets" lie this to set all colors at once
        Color[] sampleColors = new Color[3];
        sampleColors[Seed.CELL_COLOR_NEW] = new Color(0,255,0);
        sampleColors[Seed.CELL_COLOR_LIVING] = new Color(0,0,255);
        sampleColors[Seed.CELL_COLOR_DYING] = new Color(255,0,0);


        // Empty grid
        // Copy and paste this if you want to create a seed manually
        // new int[][]{
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        // };

        // Octagon
        Seed octagon = new Seed(new int[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, });
        octagon.setColor(Seed.CELL_COLOR_LIVING,new Color(51,181,229));
        octagon.setColor(Seed.CELL_COLOR_NEW,new Color(255,187,51));
        octagon.setColor(Seed.CELL_COLOR_DYING,new Color(170,102,204));
        seedList.add(octagon);

        // P3 Oscillator
        Seed p3Oscilltor = new Seed(new int[][]{
                { 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        });
        p3Oscilltor.setColors(sampleColors);
        seedList.add(p3Oscilltor);

        // Stable ship
        Seed stableShip = new Seed();
        int[][] stableShipSeed =
                new int[GameOfLifeApplet.GRID_X][GameOfLifeApplet.GRID_Y];
        stableShipSeed[3][3] = 1;
        stableShipSeed[3][4] = 1;
        stableShipSeed[4][3] = 1;
        stableShipSeed[5][4] = 1;
        stableShipSeed[5][5] = 1;
        stableShipSeed[4][8] = 1;
        stableShip.setSeed(stableShipSeed);
        stableShip.setColors(sampleColors);
        seedList.add(stableShip);


        // Testseed
        Seed testSeed = new Seed(new int[][]{
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        });
        testSeed.setColors(sampleColors);
        seedList.add(testSeed);

        // Blinker
        Seed blinker = new Seed();
        int[][] blinkerSeed =
                new int[GameOfLifeApplet.GRID_X][GameOfLifeApplet.GRID_Y];
        blinkerSeed[3][2] = 1;
        blinkerSeed[4][2] = 1;
        blinkerSeed[5][2] = 1;
        blinker.setSeed(blinkerSeed);
        blinker.setColors(sampleColors);
        seedList.add(blinker);

    }



    public LinkedList<Seed> getSeedList(){
        return seedList;
    }
}
