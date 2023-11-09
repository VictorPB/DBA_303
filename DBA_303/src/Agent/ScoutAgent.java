/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import components.*;
import java.util.ArrayList;

/**
 *
 * @author vipeba
 */
public class ScoutAgent extends Agent{
    
    Map exploredArea;
    
    Position agentPos;
    
    Position targetPos;
    
    ArrayList<Tile> vision;
    
    Action nextAction;
    
    
    
    public void setMission(Position targetRespectAgent, ArrayList<Tile> vision){
        
        
        // +2 -> una unidad por que al trabajar con la pos relativa, se empieza en 0
        //      y otra unidad para la vision del agente
        int rows = Math.abs(targetRespectAgent.getX()) + 2;
        int cols = Math.abs(targetRespectAgent.getY()) + 2;
        
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
                targetPos = new Position(rows-1,cols-1);
            }
            
            if(targetRespectAgent.getY() < 0){
                agentPos = new Position(1,cols-2);
                targetPos = new Position(rows-1,0);
            }
            
            if(targetRespectAgent.getY() == 0){
                agentPos = new Position(1,1);
                targetPos = new Position(rows-1,1);
            }
        }
        
        
        if(targetRespectAgent.getX() < 0){        
            
            if(targetRespectAgent.getY() > 0){
                agentPos = new Position(rows-2,1);
                targetPos = new Position(0,cols-1);
            }
            
            if(targetRespectAgent.getY() < 0){
                agentPos = new Position(rows-2,cols-2);
                targetPos = new Position(0,0);
            }
            
            if(targetRespectAgent.getY() == 0){
                agentPos = new Position(rows-2,1);
                targetPos = new Position(0,1);
            }
        }
        
        
        if(targetRespectAgent.getX() == 0){        
            
            if(targetRespectAgent.getY() > 0){
                agentPos = new Position(1,1);
                targetPos = new Position(1,cols-1);
            }
            
            if(targetRespectAgent.getY() < 0){
                agentPos = new Position(1,cols-2);
                targetPos = new Position(1,0);
            }
            
            if(targetRespectAgent.getY() == 0){
                agentPos = new Position(1,1);
                targetPos = new Position(1,1);
            }
        }
        
    }
    
    
 
    @Override
    public void setup(){
        System.out.println("Hello! I'm ScoutAgent.\n");
        //Get from sensors:
        //pos target respect pos agent
        //agent vision
       
        //setMission(tagetRespectAgent, Vision);
        
        this.addBehaviour(new think_Manhattan());
        
        
        
    }
    
    
    @Override
    protected void takeDown(){
        System.out.println("Terminating ScoutAgent...\n");
    }
    
    /**
     * 
     */
    class think_Manhattan extends Behaviour{
        
        @Override
        public void action() {

            System.out.print("Evaluating next action.\n");
            
            
            if(targetPos.getX() > agentPos.getX()){       //target right
                
                if(targetPos.getY() > agentPos.getY()){   //target bottom right
                    
                    nextAction = Action.DOWN_RIGHT;
                    
                }
                
                if(targetPos.getY() < agentPos.getY()){    //target top right
                    
                    nextAction = Action.UP_RIGHT;
            
                }
                
                if(targetPos.getY() == agentPos.getY()){    //target same row right
                    
                    nextAction = Action.RIGHT;
                    
                }
                
            }
            
            if(targetPos.getX() < agentPos.getX()){       //target left
                
                if(targetPos.getY() > agentPos.getY()){   //target bottom left
                    
                    nextAction = Action.DOWN_LEFT;
                    
                }
                
                if(targetPos.getY() < agentPos.getY()){    //target top left
                    
                    nextAction = Action.UP_LEFT;
            
                }
                
                if(targetPos.getY() == agentPos.getY()){    //target same row left
                    
                    nextAction = Action.LEFT;
                    
                }
                
            }
            
            if(targetPos.getX() == agentPos.getX()){            //target same column
                
                if(targetPos.getY() > agentPos.getY()){   //target down
                    
                    nextAction = Action.DOWN;
                    
                }
                
                if(targetPos.getY() < agentPos.getY()){    //target top
                    
                    nextAction = Action.UP;
            
                }
                
            }
            
            
             
            doDelete();
            
        }
        
        @Override
        public boolean done(){
           return true; 
        }
        
    }  
    
}
