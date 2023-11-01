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

    /**
     * Tile Constructor with the representation value
     * @param value 
     */
    private Tile(int value) {
        this.typeValue = value;
    }
    
    /**
     * Tile value getter
     * @return the typeValue representation
     */
    public int getVal() { return this.typeValue; }
    
}
