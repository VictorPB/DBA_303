/*
 * @file    Tile.java
 * @author  JorgeBG
 * @version 0.0.1
 */
package components;

/**
 * @brief This class models each tile inside the Map.
 */
public class Tile {
    
    /// internal representation
    private final Type type;
    
    /// times visited
    private int timesVisited;

    /**
     * Tile Constructor with the representation value
     * @param value 
     */
    public Tile(int value) {
        this.type = Type.fromValue(value);
        this.timesVisited = 0;
    }
    
    /**
     * Tile Contructor with the tile type
     * @param type The tile type
     */
    public Tile(Type type) {
        this.type = type;
        this.timesVisited = 0;
    }

    /**
     * Tile value getter
     * @return the typeValue representation
     */
    public int getVal() { return this.type.typeValue; }
    
    /**
     * Gets the tile type 
     * @return The TyleTipe enum
     */
    public Type getType() { return this.type; }
    
    /**
     * Tile times visited getter
     * @return Times the tile is visited
     */
    public int getTimesVisited() { return this.timesVisited; }
    
    /**
     * Increments the visits counter
     */
    public void newVisit(){
        this.timesVisited++;
    }
    
    /**
     * Checks if a Tile is of a specific type
     * @param type TileType to check about
     * @return 
     */
    public boolean isType(Type type){
        return type == this.type;
    }
    
    /**
     * Checks if the tile is reacheable by an agent
     * @return 
     */
    public boolean isReacheable(){
        return this.type != Type.UNREACHABLE;
    }
    
    /**
     * Enum type to model the tile type
     */
    public enum Type {
        
        EMPTY(0), 
        UNREACHABLE(-1),
        UNKNOWN(-2);
        
        private final int typeValue;

        private Type(int value) {
            this.typeValue = value;
        }
        
        public static Type fromValue(int value) {
            Type res = null;
            if(value == 0)          res = Type.EMPTY;
            else if(value == -1)    res = Type.UNREACHABLE;
            else                    res = Type.UNKNOWN;
            return res;
        }
    }
}
