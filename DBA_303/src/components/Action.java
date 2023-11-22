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
    IDLE (-1),
    END(-2);
    
    private final int actionValue;
    
    private Action(int value) {
        this.actionValue = value;
    }
}
