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
    /// Number of reindeers. It corresponds to the number of enum
    /// constant the Reindeer.Name enum has
    private final int numReindeers = 8;
    
    private Map theMap;
    
    private Position elfPosition;
    
    private Position santaPosition;
    
    private Position rudolphPosition;
    
    /**
     * Constructor with the map
     * 
     * It initializes the environment, the reindeers array and the agents position
     */
    public Environment(){
        this.reindeers = new ArrayList<>();
        
        for(int i=0; i<numReindeers; i++){
            reindeers.add(new Reindeer(i));
        }
        
        generateLostReindeers();
    }
    
    /** GETTERS ***************************************************************/

    /**
     * Gets a reindeer by his index on array
     * @param index the position in array
     */
    public Reindeer getReindeer(int index) {
        return reindeers.get(index);
    };
    
    /**
     * Gets the santa agent position
     */
    public Position getSantaPosition() {
        return this.santaPosition;
    };
    
    /**
     * Gets the rudolph agent position
     */
    public Position getRudolphPosition() {
        return this.rudolphPosition;
    };
    
    /**
     * Gets the elf agent position
     */
    public Position getElfPosition() {
        return this.elfPosition;
    };
    
    /** SETTERS ***************************************************************/

    /**
     * Sets the map and agents position
     * @param map the map
     * @param rudolphPosition the rudolph agent position
     */
    public void setParameters (Map map, Position rudolphPosition) {
        this.theMap = map;
        
        this.rudolphPosition = rudolphPosition;
        this.santaPosition = new Position(2,1);
        this.elfPosition = new Position(3,1);
    }
    
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
        return this.theMap.getTile(pos).isReacheable() && 
                !isReindeerInTile(pos) && !isSantaInTile(pos) 
                && !isRudolphInTile(pos);
    }
    
    /**
     * Method that checks if a position has a reindeer
     * @param pos The postion to check
     */
    private boolean isReindeerInTile (Position pos) {
        boolean isIn = false;
        
        for (int i = 0; i < this.reindeers.size() && !isIn; i++) {
            if (getReindeer( i).getPosition() == pos) {
                isIn = true;
            }
        }
        
        return isIn;
    }
    
    /**
     * Method that checks if Santa agent is in the position
     * @param pos The postion to check
     */
    private boolean isSantaInTile (Position pos) {        
        return this.santaPosition == pos;
    }
    
    /**
     * Method that checks if Santa agent is in the position
     * @param pos The postion to check
     */
    private boolean isRudolphInTile (Position pos) {        
        return this.rudolphPosition == pos;
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
