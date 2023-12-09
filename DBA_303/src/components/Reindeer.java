/*
 * @file
 * @author
 * @version
 */
package components;


/**
 * @brief This class models each tile inside the Map.
 */
public class Reindeer {
    
    /// Name of the reindeer
    private final Name name;
    
    /// State of the reindeer
    private State state;
    
    /// Position of the reindeer in the map
    private Position pos;
    
    /**
     * Reindeer Constructor
     * @param name The reindeer name
     */
    public Reindeer(Name name) {
        this.name = name;
        this.state = State.UNKNOWN;
        this.pos = new Position(1,1);
    }
    
    /**
     * Reindeer name getter
     * @return the reindeer name
     */
    public Name getName() { return this.name; }
    
    /**
     * Reindeer name getter
     * @return the reindeer name
     */
    public State getState() { return this.state; }
    
        /**
     * Reindeer position getter
     * @return the reindeer pos
     */
    public Position getPosition() { return this.pos; }
    
    /**
     * Reindeer state setter
     * 
     */
    public void setState(State state) { 
        this.state = state; 
    }
    
    /**
     * Reindeer position setter
     * 
     */
    public void setPosition(Position pos) { 
        this.pos = pos; 
    }
    
    
    /**
     * Enum all Santa's lost reindeers 
     */
    public enum Name {
        BLITZEN,
        COMET,
        CUPIDO,
        DANCER,
        DASHER,
        DONNER,
        PRANCER,
        VIXEN
    }
    
    /**
     * Enum reindeer states
     */
    public enum State {
        UNKNOWN,        // When Elf hasn't talk with Rudolph yet
        KNOWN,          // When Sensor doesn't know the reindeer pos
        CURRENT,        // When Sensor knows the reindeer pos
        FOUND,          // It has been found, so it dissappears from the map
    }
}
