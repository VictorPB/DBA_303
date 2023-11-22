/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent;

import components.Position;
import components.Tile;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author carlos
 */
public class UpdatePositionBehaviour extends Behaviour{
    
    private final ScoutAgent myAgent;
    
    public UpdatePositionBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
        public void action() {
            myAgent.exploredArea.getTile(myAgent.agentPos).newVisit();  //informamos de paso por casilla
            
            //Actualizamos la posicion del agente en su mapa interno
            myAgent.agentPos = myAgent.agentPos.update(myAgent.nextAction);
            
            
            //Informamos de la accion a sensores
            Sensor.getInstance().setAgentPosition(Sensor.getInstance().getAgentPosition().update(myAgent.nextAction));            
            
            myAgent.updateVision();
            
            //If need resize AgentMap
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
                    else if (t.isType(Tile.TypeTile.EMPTY))         System.out.print("▯");
                    else if (t.isType(Tile.TypeTile.UNREACHABLE))   System.out.print("▮");
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
