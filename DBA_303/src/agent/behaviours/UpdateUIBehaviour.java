package agent.behaviours;

import agent.ScoutAgent;
import gui.MainWindowP2;
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
        ((MainWindowP2)Launcher.getMainWindow()).updateAgentWithoutPath();
        ((MainWindowP2)Launcher.getMainWindow()).updateInternalMapView();
        ((MainWindowP2)Launcher.getMainWindow()).updateActionList();
    }

    @Override
    public boolean done() {
        return myAgent.isTargetReached();
    }

}
