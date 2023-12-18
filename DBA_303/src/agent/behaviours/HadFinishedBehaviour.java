/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent.behaviours;

import agent.ElfAgent;
import agent.Sensor;
import jade.core.behaviours.Behaviour;

/**
 * @brief Class that makes the behaviour to check if an agent 
 * has reached the target.
 * 
 * @author carlos
 */
public class HadFinishedBehaviour extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final ElfAgent myAgent;
    
    public HadFinishedBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        // Sensor checks if the agent position is equal to the target position
        myAgent.setTargetReached(Sensor.getInstance().targetReached());
        
        // If agent is on the target, we remove all the behaviours form queue
        // and call doDelete
        if(myAgent.isTargetReached()){
            for(Behaviour b : myAgent.getActiveBehaviours()){
                this.myAgent.removeBehaviour(b);
            }
            myAgent.doDelete();
        }
    }

    @Override
    public boolean done() {
        return myAgent.isTargetReached();
    }
}
