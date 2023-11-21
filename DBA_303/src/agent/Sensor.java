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

    /// Agent historic path
    private ArrayList<ActionPair> visitedPath;
    
    /**
     * Constructor for the sensor
     */
    private Sensor() {
        this.visitedPath = new ArrayList<>();
    }
    
    public static Sensor getInstance(){
        return instance;
    }
    
    /** GETTERS ***************************************************************/
    
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
    
    public ArrayList<ActionPair> getAgentVisitedPath(){
        return this.visitedPath;
    }
    
    public void setAgentPosition (Position newAgentPosition) {
        this.agentPosition = newAgentPosition;
        this.visitedPath.add(new ActionPair(agentPosition, Action.IDLE));
    }
    
    public void setParameters (String map, Position origin, Position target) {
        this.theMap = new Map (map);
        this.originPosition = origin;
        this.targetPosition = target;
    }
    
    /** OTHER USEFUL FUNCTIONS ************************************************/
    
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
                    result.add(Tile.UNREACHABLE);
                }
                else{
                    result.add(theMap.getTile(i, j));
                }
            }
        }
        return result;
    }
    
    // TODO review the public visibility
    /**
     * Updates the agent position given an Action (if possible)
     * @return @true if its updated, @false if not
     */
    public boolean updatePosition(Action action){
        Position newPosition = this.agentPosition.update(action);
        if(theMap.getTile(newPosition.getY(),newPosition.getX()) == Tile.EMPTY){
            this.agentPosition = newPosition;
            this.visitedPath.get(this.visitedPath.size()-1).a = action;
            this.visitedPath.add(new ActionPair(newPosition, Action.IDLE));
            return true;
        }
        else{
            return false;
        }
    }
    
    
    /**
     * Method to check if the agent has reached the goal
     */
    boolean targetReached(){
        return agentPosition.equals(targetPosition);
    }
    
    /**
     * A Class that models a visited node in the map
     */
    public class ActionPair {
        Position p;
        Action a;
        
        public ActionPair(Position p, Action a){
            this.p = p;
            this.a = a;
        }
        
        public Position getPos(){return this.p;}
        public Action getAct(){ return this.a;}
    }
}
