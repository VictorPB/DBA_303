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
        BLITZEN("BLITZEN"),
        COMET("COMET"),
        CUPIDO("CUPIDO"),
        DANCER("DANCER"),
        DASHER("DASHER"),
        DONNER("DONNER"),
        PRANCER("PRANCER"),
        VIXEN("VIXEN");
        
        private final String nameValue;

        private Name(String nameValue) {
            this.nameValue = nameValue;
        }
        
        public static Name fromName(String name) {
            Name res = null;
            switch (name) {
                case "BLITZEN":
                    res = Name.BLITZEN;
                    break;
                case "COMET":
                    res = Name.COMET;
                    break;
                case "CUPIDO":
                    res = Name.CUPIDO;
                    break;
                case "DANCER":
                    res = Name.DANCER;
                    break;
                case "DASHER":
                    res = Name.DASHER;
                    break;
                case "DONNER":
                    res = Name.DONNER;
                    break;
                case "PRANCER":
                    res = Name.PRANCER;
                    break;
                case "VIXEN":
                    res = Name.VIXEN;
                    break;
                default:
                    throw new AssertionError();
            }
            
            return res;
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
