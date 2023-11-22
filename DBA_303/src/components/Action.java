/*
 * @file    Action.java
 * @author  JorgeBG
 * @version 0.0.1
 */
package components;

/**
 * @brief   Class that models the different actions that the agent can perform
 */
public enum Action{
    UP_LEFT(0),
    UP(1), 
    UP_RIGHT(2), 
    LEFT(3), 
    CENTER(4),
    RIGHT(5), 
    DOWN_LEFT(6),
    DOWN(7),  
    DOWN_RIGHT(8),
    IDLE (-2),
    END(-1);
    
    private final int actionValue;
    
    private Action(int value) {
        this.actionValue = value;
    }
    
    public static Action fromValue(int value) {
        Action res = null;
        switch (value) {
            case 0:
                res = Action.UP_LEFT;
                break;
            case 1:
                res = Action.UP;
                break;
            case 2:
                res = Action.UP_RIGHT;
                break;
            case 3:
                res = Action.LEFT;
                break;
            case 4:
                res = Action.CENTER;
                break;
            case 5:
                res = Action.RIGHT;
                break;
            case 6:
                res = Action.DOWN_LEFT;
                break;
            case 7:
                res = Action.DOWN;
                break;
            case 8:
                res = Action.DOWN_RIGHT;
                break;
            case -1:
                res = Action.END;
                break;
            default:
                res = Action.IDLE;
                break;
        }
        
        return res;
    }
}
