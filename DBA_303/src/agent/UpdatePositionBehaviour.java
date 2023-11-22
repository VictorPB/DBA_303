/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent;

import components.Position;
import components.Tile;
import jade.core.behaviours.Behaviour;

/**
 * @brief Class that makes the behaviour to make the next move of an agent.
 * 
 * @author carlos
 */
public class UpdatePositionBehaviour extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final ScoutAgent myAgent;
    
    public UpdatePositionBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
        public void action() {
            // The agent add a visit to the tile he is.
            myAgent.exploredArea.getTile(myAgent.agentPos).newVisit();
            
            // The agent actualizes his position on his internal map
            myAgent.agentPos = myAgent.agentPos.update(myAgent.nextAction);
            
            // The sensor actualize the position of the agent
            Sensor.getInstance().updatePosition(myAgent.nextAction);
            
            // The agent update what is surrounding him
            myAgent.updateVision();
            
            //If needs it, resize AgentMap
            myAgent.updateResizeMap();        
            
            System.out.println("-------------------\n");
            
            // this may show in console what the agent know about the map
            // it position and the target
            for(int i=0; i<myAgent.exploredArea.getNumRows(); i++){
                for(int j=0; j<myAgent.exploredArea.getNumCols(); j++){
                    Position at = new Position(j,i);
                    Tile t = myAgent.exploredArea.getTile(i, j);
                    if(at.equals(myAgent.agentPos))                          System.out.print("A");
                    else if (at.equals(myAgent.targetPos))                   System.out.print("X");
                    else if (t.isType(Tile.Type.EMPTY))         System.out.print("▯");
                    else if (t.isType(Tile.Type.UNREACHABLE))   System.out.print("▮");
                    else                                                System.out.print("?");
                }
                System.out.println("");
            }
                       
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            
        }
        
        @Override
        public boolean done() {
            return myAgent.targetReached;
        }
}
