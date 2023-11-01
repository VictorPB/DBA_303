/*
 * @file    Position.java
 * @author  JorgeBG
 * @version 0.0.1
 */
package components;

/**
 * @brief Class that models a position indexed inside the Map.
 * It allows to easily store and query about the Agent position.
 */
public class Position {
    
    /// X position
    private int x;
    
    /// Y position
    private int y;

    /**
     * Constructor with x and y parameters
     * @param x The x position
     * @param y The y position
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Copy constructor
     * @param position 
     */
    public Position(Position position){
        this.x = position.x;
        this.y = position.y;
    }
    
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    
}
