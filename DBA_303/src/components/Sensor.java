/*
 * @file    
 * @author  
 * @version 
 */
package components;

import java.util.ArrayList;


/**
 *
 * @author JorgeBG
 */
public class Sensor {
    
    /// The real map to be consluted
    final Map theMap;
    
    /// The origin position of the agent in absolute coordinates
    final Position originPosition;
    
    /// The target position in absolute coordinates
    final Position targetPosition;
    
    /// Teh current position of the agent in absolute coordinates
    Position agentPosition;

    
    /**
     * Constructor for the sensor
     * @param map
     * @param origin
     * @param target
     */
    public Sensor(Map map, Position origin, Position target) {
        this.theMap = map;
        this.originPosition = this.agentPosition = origin;
        this.targetPosition = target;
    }
    
    
    /**
     * Method that evaluates the agent environment and return the array of tiles
     * @return The ordered tile array
     */
    ArrayList<Tile> reveal(){
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
    
    /**
     * Updates the agent position given an Action
     */
    // Debe devolver un booleano si lo consigue? como metodo de proteccion?
    boolean updatePosition(Action action){
        // implementamos un metodo en action que parta de una posicion y devuleva
        // la nueva tras ser ejecutada?
        return false;
    }
}
