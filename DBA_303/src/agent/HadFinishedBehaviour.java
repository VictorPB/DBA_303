/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent;

import jade.core.behaviours.Behaviour;

/**
 *
 * @author carlos
 */
public class HadFinishedBehaviour extends Behaviour{
    private final ScoutAgent myAgent;
    
    public HadFinishedBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        myAgent.targetReached = Sensor.getInstance().getAgentPosition().equals(Sensor.getInstance().getTargetPosition());
        System.out.println("");
        if(myAgent.targetReached){
            for(Behaviour b :myAgent.comportamientosActivos){
                this.myAgent.removeBehaviour(b);
            }
        }
    }

    @Override
    public boolean done() {
        return myAgent.targetReached;
    }
}
