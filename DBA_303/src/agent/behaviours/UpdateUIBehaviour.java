/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent.behaviours;

import agent.ScoutAgent;
import jade.core.behaviours.Behaviour;
import launcher.Launcher;

/**
 * Behaviour that updates the UI
 * @author carlos
 * 
 */
public class UpdateUIBehaviour extends Behaviour {

    // Private atribute to access the agent that uses the behaviour
    private final ScoutAgent myAgent;
    
    public UpdateUIBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        Launcher.getMainWindow().updateAgentWithoutPath();
        Launcher.getMainWindow().updateInternalMapView();
        Launcher.getMainWindow().updateActionList();
    }

    @Override
    public boolean done() {
        return myAgent.isTargetReached();
    }

}
