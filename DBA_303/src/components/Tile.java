/*
 * @file    Tile.java
 * @author  JorgeBG
 * @version 0.0.1
 */
package components;

/**
 * @biref This class models each tile inside the Map. It may include(by now) only a 
 * reference value that represents if its accessible (empty) or not.
 */
public enum Tile {
    // ENUM VALUES
    EMPTY(0), 
    UNREACHABLE(-1),
    UNKNOWN(-2);
    
    /// internal representation
    private final int typeValue;
    int times_visited;

    /**
     * Tile Constructor with the representation value
     * @param value 
     */
    private Tile(int value) {
        this.typeValue = value;
        this.times_visited = 0;
    }
    
    /**
     * Method to get the correct Tile object from the value representation
     * @param value the value representation
     * @return The corresponding Tile object
     */
    public static Tile fromValue(int value) {
        Tile res = null;
        if(value == 0)          res = Tile.EMPTY;
        else if(value == -1)    res = Tile.UNREACHABLE;
        else                    res = Tile.UNKNOWN;
        return res;
    }
    
    /**
     * Tile value getter
     * @return the typeValue representation
     */
    public int getVal() { return this.typeValue; }
    
    /**
     * Tile times visited getter
     * @return Times the tile is visited
     */
    public int getTimesVisited() { return this.times_visited; }
    
    
    public void newVisit(){
        this.times_visited++;
    }
    
}
