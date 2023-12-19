/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent.behaviours;

import agent.ElfAgent;
import jade.core.behaviours.Behaviour;

/**
 * @brief Class that makes the behaviour to check if an agent 
 * has reached the target.
 * 
 * @author carlos
 */
public class TargetReachedBehaviour extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final ElfAgent myAgent;
    
    public TargetReachedBehaviour(ElfAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        if(myAgent.getElfState() != ElfAgent.State.COMMUNICATING){
            //If agent is next to targetPos
            System.out.println("REL AGENT: "+myAgent.getAgentRelPos() + " TARGET: "+myAgent.getTargetRelPos());
            if(myAgent.getAgentRelPos().getChebyshovTo(myAgent.getTargetRelPos()) < 2){
                System.out.println("SE SUPONE QUE HA LLEGAU!");
                myAgent.setTargetReached(true);
                myAgent.setElfState(ElfAgent.State.COMMUNICATING);
            }
        }
    }

    @Override
    public boolean done() {
        return myAgent.finished;
    }
}
