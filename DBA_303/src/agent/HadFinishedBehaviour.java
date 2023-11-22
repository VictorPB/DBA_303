/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent;

import jade.core.behaviours.Behaviour;

/**
 * @brief Class that makes the behaviour to check if an agent 
 * has reached the target.
 * 
 * @author carlos
 */
public class HadFinishedBehaviour extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final ScoutAgent myAgent;
    
    public HadFinishedBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        // Sensor checks if the agent position is equal to the target position
        myAgent.targetReached = Sensor.getInstance().targetReached();
        System.out.println("");
        
        // If agent is on the target, we remove all the behaviours form queue
        // and call doDelete
        if(myAgent.targetReached){
            for(Behaviour b : myAgent.activeBehaviours){
                this.myAgent.removeBehaviour(b);
            }
            
            myAgent.doDelete();
        }
    }

    @Override
    public boolean done() {
        return myAgent.targetReached;
    }
}
