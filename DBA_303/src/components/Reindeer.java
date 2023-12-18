/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file          Reindeer.java
 * @author    DBA_303. Carlos
 */
package components;


/**
 * @brief This class stores the name, state and position of each reindeer in
 * environment.
 */
public class Reindeer {
    
    /// Name of the reindeer
    private final Name name;
    
    /// State of the reindeer
    private State state;
    
    /// Position of the reindeer in the map
    private Position pos;
    
    
    /**
     * Reindeer Constructor by the representation value
     * @param value
     */
    public Reindeer(int value) {
        this.name = Name.fromValue(value);
        this.state = State.UNKNOWN;
        this.pos = new Position(1,1);
    }
    
    /**
     * Reindeer Constructor by the reindeer name
     * @param name The reindeer name
     */
    public Reindeer(Name name) {
        this.name = name;
        this.state = State.UNKNOWN;
        this.pos = new Position(1,1);
    }
    
    
    /** GETTERS ***************************************************************/

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
    
    
    /** SETTERS ***************************************************************/
    
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
    
    /**************************************************************************/

     @Override
        public String toString() {
            String res = "";
            
            res += "Reno ";
            
            res += this.name;
            
            res += " con estado ";
            
            res += this.state;
            
            res += " y posición ";
            
            res += this.pos;
            
            return res;
        }
    
    
    /**************************************************************************/
    
    /**
     * Enum all Santa's lost reindeers 
     */
    public enum Name {
        BLITZEN(0),
        COMET(1),
        CUPIDO(2),
        DANCER(3),
        DASHER(4),
        DONNER(5),
        PRANCER(6),
        VIXEN(7);
        
        private final int nameValue;

        private Name(int value) {
            this.nameValue = value;
        }
        
        public static Name fromValue(int value) {
            Name res = null;
            switch (value) {
                case 0:
                    res = Name.BLITZEN;
                    break;
                case 1:
                    res = Name.COMET;
                    break;
                case 2:
                    res = Name.CUPIDO;
                    break;
                case 3:
                    res = Name.DANCER;
                    break;
                case 4:
                    res = Name.DASHER;
                    break;
                case 5:
                    res = Name.DONNER;
                    break;
                case 6:
                    res = Name.PRANCER;
                    break;
                case 7:
                    res = Name.VIXEN;
                    break;
                default:
                    throw new AssertionError();
            }
            
            return res;
        }

        @Override
        public String toString() {
            return Integer.toString(nameValue);
        }
    }
    
    /**
     * Enum reindeer states
     */
    public enum State {
        UNKNOWN(0),        // When Elf hasn't talk with Rudolph yet
        KNOWN(1),          // When Sensor doesn't know the reindeer pos
        CURRENT(2),        // When Sensor knows the reindeer pos
        FOUND(3);          // It has been found, so it dissappears from the map
        
        
        private final int stateValue;

        private State(int value) {
            this.stateValue = value;
        }
        
        public static State fromValue(int value) {
            State res = null;
            switch (value) {
                case 0:
                    res = State.UNKNOWN;
                    break;
                case 1:
                    res = State.KNOWN;
                    break;
                case 2:
                    res = State.CURRENT;
                    break;
                case 3:
                    res = State.FOUND;
                    break;
                default:
                    throw new AssertionError();
            }
            
            return res;
        }

        @Override
        public String toString() {
            return Integer.toString(stateValue);
        }
    }
}
