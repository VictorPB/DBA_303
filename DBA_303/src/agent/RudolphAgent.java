/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file    RudolphAgent.java
 * @author  DBA_303. Jorge, Carlos
 */
package agent;

import agent.behaviours.RudolphComunicationBeh;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import components.*;
import java.util.ArrayList;
import java.util.Random;


/**
 * @brief   This class models the Rudolph Agent that gives to elf the
 * lost reindeers position if he has the secret code
 */
public class RudolphAgent extends Agent{
    

    // List of behaviours added to the agent
    Behaviour[] activeBehaviours;
    
    // Number of found reindeers
    int foundReindeers;
        
    /**
     * Constructor
     */
    public RudolphAgent() {
        super();
        
        foundReindeers = 0;
    }
    
    // TODO refill with getters, etc
    
    /**
     * Get the number of found reindeers
     * @return the number of found reindeers
     */
    public int getNumFoundReindeers () { return this.foundReindeers; }
    
    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup() {
        System.out.println("Hello! I'm the Rudolph Agent. \"¡Hiaa, hiaa!\\n");
        
        this.addBehaviour(new RudolphComunicationBeh(this));
        
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has found all reindeers. Terminating Rudolph...\n");
    }
    
    /**
     * Get the first element of array Reindeers (Environment class)
     * @return the first reindeer in Enviroment class array reindeers
     */
    public Reindeer getNextReindeer() {
        return (Reindeer) Environment.getInstance().getReindeers().get(0);
    }
    
    /**
     * Pops the element of array Reindeers (Environment class) with name name
     * @param name the name of the reindeer to pop
     * @return true if it deletes the reindeer, false otherwhise
     */
    public boolean foundReindeer(Reindeer.Name name) {
        Reindeer reindeer = (Reindeer) Environment.getInstance().getReindeers().stream().filter(r -> r.getName().equals(name)).findAny().orElse(null);
            
        return Environment.getInstance().getReindeers().remove(reindeer);
    }
}
