/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file          ElfAgent.java
 * @author    DBA_303. Jorge, Victor
 */
package agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import components.*;
import java.util.ArrayList;
import agent.behaviours.ElfComunicationBeh;



/**
 * @brief   This class models the Elf Agent that helps Santa and Rudolf to
 *          find the lost reindeers.
 */
public class ElfAgent extends Agent{
    
    // Internal map (only explored area)
    Map exploredArea;

    // Agent position relative to the internal map
    Position agentPos;

    // Target position relative to the internal map
    Position targetPos;
    
    // Boolean value that controls if the agent had reached the target
    boolean targetReached;

    // Internal storage of the last vision of the agent
    ArrayList<Tile> vision;

    // Next action to be done
    Action nextAction;

    // List of behaviours added to the agent
    Behaviour[] activeBehaviours;
    
    // Indicates whether the agent has to move or is moving so as not to engage in communication behavior
    boolean isMoving = false;
    
    /**
     * Constructor
     */
    public ElfAgent() {
        this.exploredArea = new Map(3,3);
        this.agentPos = new Position(1,1);
    }
    
    // TODO refill with getters, etc
    
    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup() {
        System.out.println("Hello! I'm the Elf Agent.\n");
        
        Map map = new Map("mapWithDiagonalWall.txt");        
        Environment.getInstance().setParameters(map);
        
        this.addBehaviour(new ElfComunicationBeh());
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has reached the target. Terminating ElfAgent...\n");
    }
}
