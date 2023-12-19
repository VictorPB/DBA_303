/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent.behaviours;

import agent.ElfAgent;
import components.Environment;
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
    private final ElfAgent myAgent;
    
    public UpdatePositionBehaviour(ElfAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
        public void action() {
            // The agent add a visit to the tile he is.
            myAgent.getExploredArea().getTile(myAgent.getAgentPos()).newVisit();
            
            // The agent actualizes his position on his internal map
            myAgent.setAgentPos(myAgent.getAgentPos().update(myAgent.getNextAction()));
            
            // The Environment actualize the position of the agent
            Environment.getInstance().updatePosition(myAgent.getNextAction());
            
            //If needs it, resize AgentMap
            myAgent.updateResizeMap(); 
            
            // The agent update what is surrounding him
            myAgent.updateVision();
            
            // this may show in console what the agent know about the map
            // it position and the target
            for(int i=0; i<myAgent.getExploredArea().getNumRows(); i++){
                for(int j=0; j<myAgent.getExploredArea().getNumCols(); j++){
                    Position at = new Position(j,i);
                    Tile t = myAgent.getExploredArea().getTile(i, j);
                    if(at.equals(myAgent.getAgentPos()))                          System.out.print("A");
                    else if (at.equals(myAgent.getTargetPos()))                   System.out.print("X");
                    else if (t.isType(Tile.Type.EMPTY))         System.out.print("▯");
                    else if (t.isType(Tile.Type.UNREACHABLE))   System.out.print("▮");
                    else                                                System.out.print("?");
                }
                System.out.println("");
            }

                        
            System.out.println("---------------------------------");
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            
        }
        
        @Override
        public boolean done() {
            return myAgent.isTargetReached();
        }
}
