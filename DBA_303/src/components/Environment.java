/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;
import java.util.ArrayList;


/**
 * @brief Class that initializes the position of all the agents and the 
 * reindeers on the map generated with the map class

 * 
 * @author carlos
 */
public class Environment {
    /// The lost reindeers array
    private ArrayList<Reindeer> reindeers;
    
    private Map theMap;
    
    private final int numReindeers = 8;
    
    /**
     * Constructor with the map
     * 
     * It initialize Reindeers array
     */
    public Environment(Map map){
        this.theMap = map;
        
        this.reindeers = new ArrayList<>();
        
        for(int i=0; i<numReindeers; i++){
            reindeers.add(new Reindeer(i));
        }
        
        generateLostReindeers();
    }
    
    /** GETTERS ***************************************************************/

    /**
     * Gets a reindeer by his position on array
     * @param pos the position in array
     */
    public Reindeer getReindeer(int pos) {
        return reindeers.get(pos);
    };
    
    /** SETTERS ***************************************************************/

    /**
     * Sets a reindeer by his position in the array
     * @param index the position on the array
     * @param row
     * @param tile
     */
    public void setReindeer(int index, Position pos, Reindeer.State state) { 
        setReindeerPos(index, pos);
        setReindeerState(index, state);
    };
    
    /**
     * Sets a reindeer position by his position on array
     * @param index the position in the array
     * @param  pos the new position in the map
     */
    public void setReindeerPos (int index, Position pos) {
        getReindeer(index).setPosition(pos);
    };
    
    /**
     * Sets a reindeer state by his position on array
     * @param index the position in the array
     * @param  state the new state of the reindeer
     */
    public void setReindeerState (int index, Reindeer.State state) {
        getReindeer(index).setState(state);
    };
    
    /** PRIVATE UTIL METHODS **************************************************/

    /**
     * Method that initialize reindeers array with a random position
     * for each reindeer
     */
    private void generateLostReindeers () {
        int randomX, randomY;
        int n = this.theMap.getNumCols();
        int m = this.theMap.getNumRows();
        
        Position pos;
        
        for (int i = 0; i < numReindeers; i++) {
            do {
                pos = new Position ((int) (Math.random() * n) +1,(int) (Math.random() * m) +1);
            } while (!legalPos(pos));
            
            getReindeer(i).setPosition(pos);
        }
    }
    
    /**
     * Method that checks if a position is reacheable and "agents free",
     * it means, the tile in map is empty, there's no agent there
     */
    private boolean legalPos (Position pos) {
        return this.theMap.getTile(pos).isReacheable(); // Comprobar tambien que no esta santa, rudolph u otro reno 
                                                          // ( ¿la casa de santa será directamente unreacheable? )
    }
    
    
    /**************************************************************************/

    @Override
    public String toString() {
        String res = "";
        for(Reindeer reindeer : this.reindeers){
            
        }
        return res;
    }
}
