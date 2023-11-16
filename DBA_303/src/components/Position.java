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
    
    /**
     * Method to create a new position from the actual one with an action  
     * @param   int The number of the tile in the array os the adjacents
     * @return  The new position
     */
    public Position update (int i) {
        Position res = new Position(this);
        if (i==0){
            res.y-=1;
            res.x -= 1;
        } else if (i==1) {
            res.y-=1;
        } else if (i==2) {
            res.y-=1;
        } else if (i==3) {
            res.y-=1;
        } else if (i==4) {
            res.y-=1;
        } else if (i==5) {
            res.y-=1;
        } else if (i==6) {
            res.y-=1;
        } else if (i==7) {
            res.y-=1;
        } else if (i==8) {
            res.y-=1;
        } else {
            res.x = -1;
            res.y = -1;
        } 
        return res;
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
    
}
