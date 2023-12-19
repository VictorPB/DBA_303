/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file    SantaAgent.java
 * @author  DBA_303. Jorge
 */
package agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import components.*;
import java.util.ArrayList;
import java.util.Random;
import agent.behaviours.SantaComunicationBeh;


/**
 * @brief   This class models the Santa Agent that checks if the volunteer elf
 *          deserves to do the mision and comunicates the rudolf secret code
 */
public class SantaAgent extends Agent{
    

    // List of behaviours added to the agent
    Behaviour[] activeBehaviours;
    
    /**
     * Constructor
     */
    public SantaAgent() {
        super();
    }
    
    // TODO refill with getters, etc
    
    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup() {
        System.out.println("Hello! I'm the Santa Agent.\n");
        
        this.addBehaviour(new SantaComunicationBeh(this));
        
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has reached the target. Terminating Santa...\n");
    }
    
    /**
     * Method that randomly checks if an elf deserves to do the mission.
     * @return true 80% and false 20%
     */
    public boolean radomElfAprove(){
        return (new Random().nextInt(5)) >0 ;
    }
}
