/*
 * @file    Position.java
 * @author  JorgeBG
 * @version 0.0.1
 */
package components;

import static components.Action.DOWN_LEFT;
import static components.Action.LEFT;
import static components.Action.UP_LEFT;

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
    
       
    
    /**
     * Method to create a new position from the actual one with an action  
     * @param   action The action to be done
     * @return  The new position
     */
    public Position update(Action action) {
        Position res = new Position(this);
        switch (action) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                res.y-=1;
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                res.y+=1;
                break;
        }
        
        switch (action) {
            case UP_LEFT:
            case LEFT:
            case DOWN_LEFT:
                res.x-=1;
                break;
            case UP_RIGHT:
            case RIGHT:
            case DOWN_RIGHT:
                res.x+=1;
                break;
        }
        
        return res;
    }
    
    public int getManhattanTo(Position target){
        return Math.abs(target.x-this.x) + Math.abs(target.y-this.y);
    }
    
    public double getEuclideTo(Position target){
        int dX = Math.abs(target.x-this.x);
        int dY = Math.abs(target.y-this.y);
        return Math.sqrt(dX*dX + dY*dY);
    }
    
    /** GETTERS ***************************************************************/
    
    /**
     * X position getter
     */
    public int getX() { return this.x; }
    
    /**
     * Y position getter
     */
    public int getY() { return this.y; }
    
    
    /** SETTERS ***************************************************************/

    /**
     * X position setter
     */
    public void setX(int x) { this.x = x; }
    
    /**
     * Y position setter
     */
    public void setY(int y) { this.y = y; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Position))
            return false;
        
        Position p = (Position)obj;
        return this.x==p.x && this.y==p.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y+")";
    }

    
}
