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
public class Tile {
    
    /// internal representation
    private TypeTile type;
    /// times visited
    private int timesVisited;

    /**
     * Tile Constructor with the representation value
     * @param value 
     */
    public Tile(int value) {
        this.type = TypeTile.fromValue(value);
        this.timesVisited = 0;
    }
    
    public Tile(TypeTile type) {
        this.type = type;
        this.timesVisited = 0;
    }

    
    /**
     * Tile value getter
     * @return the typeValue representation
     */
    public int getVal() { return this.type.typeValue; }
    
    public TypeTile getType() { return this.type; }
    
    /**
     * Tile times visited getter
     * @return Times the tile is visited
     */
    public int getTimesVisited() { return this.timesVisited; }
    
    
    public void newVisit(){
        this.timesVisited++;
    }
    
    public boolean isType(TypeTile type){
        return type == this.type;
    }
    
    public boolean isReacheable(){
        return this.type != TypeTile.UNREACHABLE;
    }
    
    public enum TypeTile {
        
    // ENUM VALUES
        EMPTY(0), 
        UNREACHABLE(-1),
        UNKNOWN(-2);
        
        private final int typeValue;

        private TypeTile(int value) {
            this.typeValue = value;
        }
        
        public static TypeTile fromValue(int value) {
            TypeTile res = null;
            if(value == 0)          res = TypeTile.EMPTY;
            else if(value == -1)    res = TypeTile.UNREACHABLE;
            else                    res = TypeTile.UNKNOWN;
            return res;
        }
    }
}
