/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent;

import jade.core.behaviours.Behaviour;
import launcher.Launcher;

/**
 * Behaviour that updates the UI
 * @author carlos
 * 
 */
class updateUIBehaviour extends Behaviour {

    // Private atribute to access the agent that uses the behaviour
    private final ScoutAgent myAgent;
    
    public updateUIBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        Launcher.getMainWindow().updateAgentWithoutPath();
        Launcher.getMainWindow().updateInternalMapView();
    }

    @Override
    public boolean done() {
        return myAgent.targetReached;
    }

}
