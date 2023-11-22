/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import components.*;
import java.util.ArrayList;
import components.Map;

/**
 *
 * @author vipeba
 */
public class ScoutAgent extends Agent{
    
    Map exploredArea;
    
    //Posicion del agente en según mapa interno
    Position agentPos;
    
    //Posición del destino segun mapa interno
    Position targetPos;
    
    boolean targetReached;
    
    ArrayList<Tile> vision;
    
    Action nextAction;
    
    Behaviour[] comportamientosActivos;

    
    
    public ScoutAgent() {}
    
    /**
     * Method to get the internal map 
     * @return the map that contains the explored area
     */
    public Map getExploredArea(){
        return exploredArea;
    }
    
    /**
     * Method to initialize the internal map
     * It also calls the UpdateVision for the first time
     * @param targetRespectAgent 
     */
    public void setMission(Position targetRespectAgent){
        // +2 -> una unidad por que al trabajar con la pos relativa, se empieza en 0
        //      y otra unidad para la vision del agente
        int rows = Math.abs(targetRespectAgent.getY()) + 2;
        int cols = Math.abs(targetRespectAgent.getX()) + 2;
        
        if(rows < 3){
            rows = 3;
        }
        
        if(cols < 3){
            cols = 3;
        }
        
        exploredArea = new Map(cols, rows);
        
        
        //Set agent and target position 
        if(targetRespectAgent.getX() > 0){        
            
            if(targetRespectAgent.getY() > 0){
                agentPos = new Position(1,1);
                targetPos = new Position(cols-1,rows-1);
            }
            
            if(targetRespectAgent.getY() < 0){
                agentPos = new Position(1,rows-2);
                targetPos = new Position(cols-1,0);
            }
            
            if(targetRespectAgent.getY() == 0){
                agentPos = new Position(1,1);
                targetPos = new Position(cols-1,1);
            }
        }
        
        
        if(targetRespectAgent.getX() < 0){        
            
            if(targetRespectAgent.getY() > 0){
                agentPos = new Position(1, rows-2 );
                targetPos = new Position(cols-1, 0);
            }
            
            if(targetRespectAgent.getY() < 0){
                agentPos = new Position(cols-2,rows-2);
                targetPos = new Position(0,0);
            }
            
            if(targetRespectAgent.getY() == 0){
                agentPos = new Position(1, rows-2);
                targetPos = new Position(1, 0);
            }
        }
        
        
        if(targetRespectAgent.getX() == 0){        
            
            if(targetRespectAgent.getY() > 0){
                agentPos = new Position(1,1);
                targetPos = new Position(cols-1,1);
            }
            
            if(targetRespectAgent.getY() < 0){
                agentPos = new Position(cols-2,1);
                targetPos = new Position(0,1);
            }
            
            if(targetRespectAgent.getY() == 0){
                agentPos = new Position(1,1);
                targetPos = new Position(1,1);
            }
        }
        
        exploredArea.getTile(agentPos).newVisit();
        for(int i=0; i<exploredArea.getNumRows(); i++){
            for(int j=0; j<exploredArea.getNumCols(); j++)
                System.out.print(exploredArea.getTile(i, j).getTimesVisited());
            System.out.println("");
        }
        updateVision();
    }
    
    /**
     * Method to set the values of the agent's adjacent tiles
     * It calls the sensor to take the value tiles
     */
    void updateVision(){
        
        vision = Sensor.getInstance().reveal();
              
        int indexVision = 0;
        
        for(int i = agentPos.getY()-1; i <= agentPos.getY()+1; i++){
            for(int j = agentPos.getX()-1; j <= agentPos.getX()+1; j++){
                exploredArea.setTile(j, i, vision.get(indexVision));
                indexVision++;
            }         
        }
    }
    
    /**
     * Check if Agent Map (exploredArea) needs to be resized because
     * being in the border of the map.
     */
    void updateResizeMap(){

        if(agentPos.getX() == 0){
            exploredArea.addColToBeggining();
            agentPos  = agentPos.update(Action.RIGHT);
            targetPos = targetPos.update(Action.RIGHT);
        }
        else if(agentPos.getX() == exploredArea.getNumCols()-1){         //En ultima columna
            exploredArea.addColToEnd();
        }
        
        if( agentPos.getY() == 0){
            exploredArea.addRowToBeggining();
            agentPos  = agentPos.update(Action.DOWN);
            targetPos = targetPos.update(Action.DOWN);
        }
        else if(agentPos.getY() == exploredArea.getNumRows()-1){
            exploredArea.addRowToEnd();
        }

    }
    
 
    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup(){
        System.out.println("Hello! I'm ScoutAgent.\n");
  
        Sensor.getInstance().setParameters("mapWithDiagonalWall.txt", new Position(8,2), new Position(9,3));
        
        //Para iniciar el agente solo necesitamos que sensores nos indique la posicion relativa al objetivo
        
        setMission(Sensor.getInstance().getTargetRespectAgent());
        
        comportamientosActivos = new Behaviour[]{
            new ThinkObstacleBehaviour(this),
            new UpdatePositionBehaviour(this)
        };

   
        this.addBehaviour(new HadFinishedBehaviour(this));
        
        for(Behaviour b : comportamientosActivos){
            this.addBehaviour(b);
        }   
        
    }
    
    
    @Override
    protected void takeDown(){
        System.out.println("Agent may have reached the target. Terminating ScoutAgent...\n");
    }
}
