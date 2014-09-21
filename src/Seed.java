import java.awt.*;

/**
 * Created by Peter Mösenthin.
 */
public class Seed {


    // Access constants
    public static final int CELL_COLOR_NEW = 0;
    public static final int CELL_COLOR_DYING = 1;
    public static final int CELL_COLOR_LIVING = 2;

    public static final int CELL_LIVING = 1;
    public static final int CELL_NEW = 2;
    public static final int CELL_DYING = 3;

    /**
     * Stores colors for cells
     * <code>
     *     [0] = new cell
     *     [1] = dying cell
     *     [2] = living cell
     * </code>
     */
    private Color[] cellColors = new Color[3];

    private int[][] seed;

    public Seed(int[][] seed){
       setSeed(seed);
    }

    public Seed(){
    }

    /**
     * Set a seed with an integer array
     * @param seed
     */
    public void setSeed(int[][] seed){
        this.seed = seed;
    }

    /**
     *
     * @param type
     * Use
     * <code>
     *     Seed.CELL_COLOR_NEW
     *     Seed.CELL_COLOR_DYING
     *     Seed.CELL_COLOR_LIVING
     * </code>
     * @param color
     */
    public void setColor(int type,Color color){
        if(type< cellColors.length){
            cellColors[type] = color;
        }
    }

    /**
     *
     * @param type
     * Use
     * <code>
     *     Seed.CELL_COLOR_NEW
     *     Seed.CELL_COLOR_DYING
     *     Seed.CELL_COLOR_LIVING
     * </code>
     */
    public Color getColor(int type){
        if(type< cellColors.length){
            return cellColors[type];
        }else {
            return null;
        }
    }

    /**
     * Sets all colors at once
     *
     * @param colors
     */
    public void setColors(Color[] colors){
        if(colors.length == cellColors.length){
        this.cellColors = colors;
        }
    }

    public int[][] getSeedGrid(){
        return seed;
    }

}
