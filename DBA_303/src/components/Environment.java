/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package components;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * @brief Class that initializes the position of all the agents and the 
 * reindeers on the map generated with the map class

 * 
 * @author carlos
 */
public class Environment {
    
    // Static variable that save the only instance of the Singleton
    private static Environment instance = new Environment();
    
    /// The lost reindeers array
    private ArrayList<Reindeer> reindeers;
    
    private Map theMap;
    
    private Position elfPosition;
    
    private Position santaPosition;
    
    private Position rudolphPosition;
    
    /**
     * Constructor with the map
     * 
     * It initializes the environment, the reindeers array and the agents position
     */
    private Environment(){
        this.reindeers = new ArrayList<>();
        
        for(Reindeer.Name name : Reindeer.Name.values()){
            this.reindeers.add(new Reindeer(name));
        }
        
        Collections.shuffle(this.reindeers);
    }
        
    /**
     *  Gets the instance of Environment
     * @return the instance of the singleton
     */
    public static Environment getInstance(){
        return instance;
    }
    
    /** GETTERS ***************************************************************/

    /**
     * Gets the reindeer array
     * @return the ArrayList reindeer
     */
    public ArrayList<Reindeer> getReindeers() {
        return this.reindeers;
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
    
    /**
     *  Gets the number of reindeers (size) in reindeers array
     */
    public int getNumberReindeers () {
        return this.reindeers.size();
    }
    
    /** SETTERS ***************************************************************/

    /**
     * Sets the map, agents position and initialize reindeers
     * @param map the map
     * @param rudolphPosition the rudolph agent position
     */
    public void setParameters (Map map) {
        this.theMap = map;
        
        initRudolphPos();
        this.santaPosition = new Position(1,2);
        this.elfPosition = new Position(1,3);
        
        generateLostReindeers();
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
        this.reindeers.get(index).setPosition(pos);
    };
    
    /**
     * Sets a reindeer state by his position on array
     * @param index the position in the array
     * @param  state the new state of the reindeer
     */
    public void setReindeerState (int index, Reindeer.State state) {
        this.reindeers.get(index).setState(state);
    };
    
    /** PRIVATE UTIL METHODS **************************************************/

    /**
     * Method that initialize rudolph position with a random position
     * 
     * Initialize rudolph in a range where the X is a value between 
     * Map.cols/3 and (Map.cols*2)/3 and the Y is a value between
     * Map.rows/3 and /(Map.rows*2)/3
    */
    void initRudolphPos () {
        int n = this.theMap.getNumCols()/3;
        int m = this.theMap.getNumRows()/3;
        
        do {
            this.rudolphPosition = new Position ((int) (Math.random() * (n*2-n+1)+n),(int) (Math.random() * (m*2-m+1)) +m);        
        } while (!this.theMap.getTile(getRudolphPosition()).isReacheable());
    }
    
    /**
     * Method that initialize reindeers array with a random position
     * for each reindeer
     */
    private void generateLostReindeers () {
        int n = this.theMap.getNumCols();
        int m = this.theMap.getNumRows();
        
        Position pos;
        
        for (int i = 0; i < this.reindeers.size(); i++) {
            do {
                pos = new Position ((int) (Math.random() * n),(int) (Math.random() * m));
            } while (!legalPos(pos));
            
            this.reindeers.get(i).setPosition(pos);
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
            if (this.reindeers.get( i).getPosition().equals(pos)) {
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
        return this.santaPosition.equals(pos);
    }
    
    /**
     * Method that checks if Rudolph agent is in the position
     * @param pos The postion to check
     */
    private boolean isRudolphInTile (Position pos) {        
        return this.rudolphPosition.equals(pos);
    }
    
    
    /**************************************************************************/

    @Override
    public String toString() {
        String res = "";
        for(Reindeer reindeer : this.reindeers){
            /// Implements if neccesary
        }
        return res;
    }
    
    
    /**************************************************************************/
    public static void main(String args[]){
        
        Map map = new Map("mapWithDiagonalWall.txt");
        System.out.println("Generating new Environment...");
        
        Environment.getInstance().setParameters(map);
        
        // this may show in console what the agent know about the map
        // it position and the target
        for(int i=0; i<Environment.getInstance().theMap.getNumRows(); i++){
            for(int j=0; j<Environment.getInstance().theMap.getNumCols(); j++){
                Position at = new Position(j,i);
                Tile t = Environment.getInstance().theMap.getTile(i, j);
                if(at.equals(Environment.getInstance().getElfPosition()))                           System.out.print("E");
                else if (at.equals(Environment.getInstance().getRudolphPosition()))                 System.out.print("R");
                else if (at.equals(Environment.getInstance().getSantaPosition()))                   System.out.print("S");
                else if (Environment.getInstance().isReindeerInTile(at))                            System.out.print("D");
                else if (t.isType(Tile.Type.EMPTY))                                                 System.out.print("▯");
                else if (t.isType(Tile.Type.UNREACHABLE))                                           System.out.print("▮");
                else                                                                                    System.out.print("?");
            }
            System.out.println("");
        }
    }
}
