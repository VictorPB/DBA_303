/*
 * @file    Sensor.java
 * @author  
 * @version 
 */
package agent;

import components.Action;
import components.Map;
import components.Position;
import components.Tile;
import java.util.ArrayList;


/**
 *
 * @author JorgeBG
 */
public final class Sensor {
    
    // Static variable that save the only instance of the Singleton
    private static Sensor instance = new Sensor();
    
    // The real map to be consluted
    private Map theMap;
    
    /// The origin position of the agent in absolute coordinates
    private Position originPosition;
    
    /// The target position in absolute coordinates
    private Position targetPosition;
    
    /// The current position of the agent in absolute coordinates
    private Position agentPosition;

    
    /**
     * Constructor for the sensor
     * @param mapFile
     * @param origin
     * @param target
     */
    private Sensor() {}
    
    public static Sensor getInstance(){
        return instance;
    }
    
    // Getters and setters
    public Map getMap () {
        return theMap;
    }
    
    public Position getOriginPosition () {
        return originPosition;
    }
    
    public Position getTargetPosition () {
        return targetPosition;
    }
    
    public Position getAgentPosition () {
        return agentPosition;
    }
    
    public void setAgentPosition (Position newAgentPosition) {
        this.agentPosition = newAgentPosition;
    }
    
    public void setParameters (String map, Position origin, Position target) {
        this.theMap = new Map (map);
        this.originPosition = origin;
        this.agentPosition = origin;
        this.targetPosition = target;
    }
    
    /**
     * Method that evaluates the agent environment and return the array of tiles
     * @return The ordered tile array
     */
    public ArrayList<Tile> reveal(){
        ArrayList<Tile> result = new ArrayList();
        int row = agentPosition.getY();
        int col = agentPosition.getX();
        for( int i=row-1; i<=row+1; i++){
            for(int j=col-1; j<=col+1; j++){
                // if the position is out of the map bounds, it adds an
                // unreachable tile. otherwise it adds the source tile.
                if(i<0 || i>=theMap.getNumRows() || j<0 || j>=theMap.getNumCols()){
                    result.add(new Tile(Tile.TypeTile.UNREACHABLE));
                }
                else{
                    result.add(theMap.getTile(i, j));
                }
            }
        }
        return result;
    }
    
    
    /**
     * Updates the agent position given an Action (if possible)
     * @return @true if its updated, @false if not
     */
    boolean updatePosition(Action action){
        Position newPosition = this.agentPosition.update(action);
        if(theMap.getTile(newPosition.getY(),newPosition.getX()).isType(Tile.TypeTile.EMPTY)){
            this.agentPosition = newPosition;
            return true;
        }
        else{
            return false;
        }
    }
    
    
    Position getTargetRespectAgent(){
        return new Position(
            targetPosition.getX() - originPosition.getX(),
            targetPosition.getY() - originPosition.getY()
        );
    }
    
    /**
     * Method to check if the agent has reached the goal
     */
    boolean targetReached(){
        return agentPosition.equals(targetPosition);
    }
}
