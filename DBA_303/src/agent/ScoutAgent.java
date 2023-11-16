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
    
    ArrayList<ArrayList<Integer>> visitedCountMap;
    
    Position agentPos;
    
    /* Guarda posición destino */
    Position targetPos; 
    
    ArrayList<Tile> vision;
    
    Action nextAction;

    Tile nextTile;

    
    
    public ScoutAgent() {
        exploredArea = new Map();
    }
    
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
        
        Position agentPos = new Position(3,2);
        Position targetPos = new Position(4,6);
        
        Sensor.getInstance().setParameters("mapWithComplexObstacle1.txt", agentPos, targetPos);
        
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
    
    /**
     *  
     */
    class think_obstacle extends Behaviour{
        private boolean behaviourFinished = false;
        
        @Override
        public void action() {
            System.out.print("Evaluating next action in think_obstacle.\n");
            Position currentPos = agentPos;
            
            // Obtiene las casillas adyacentes con el sensor
            ArrayList<Tile> adjacentTiles = Sensor.getInstance().reveal(); 
            
            // Evalua las casillas adyacentes y elige la mejor opción
            Action bestAction = null;
            double bestScore = Integer.MIN_VALUE;
            boolean isAccesible;
            
            // IMPORTANTE: Evitar pasar por diagonal de muro
            
            for (int i=0; i<adjacentTiles.size(); i++) {
                if (i!= 4) {
                    isAccesible = true;
                    Tile tile = adjacentTiles.get(i);
                
                    // Obtiene la posicion del array i
                    Position nextPos = currentPos.update(i);
                    
                    // Comprueba que la casilla no sea un obstáculo
                    if(tile != Tile.UNREACHABLE) {
                        // Comprobamos si las esquinas son accesibles
                        if (i == 0 && upLeftIsUnreachable(adjacentTiles)) {
                            isAccesible = false;
                        } else if (i == 2 && upRightIsUnreachable(adjacentTiles)) {
                            isAccesible = false;
                        } else if (i == 6 && downLeftIsUnreachable(adjacentTiles)) {
                            isAccesible = false;
                        } else if (i == 8 && downRightIsUnreachable(adjacentTiles)) {
                            isAccesible = false;
                        }
                    
                        if (isAccesible) {
                            double score = calculateScore (currentPos, nextPos);

                            if (score > bestScore) {
                                bestScore = score;
                                bestAction = Action.values()[i];
                            }
                        }
                    }
                }
            }
            
            if (bestAction != null) {
                nextAction = bestAction;
            } else {
                System.err.print("Ninguna de las opciones posibles es la mejor.");
            }
            
            // Actualizar mapas de exploración
            exploredArea.setTile(currentPos.getX(), currentPos.getY(), Tile.EMPTY);
            
            // Sumar una visita en el mapa de exploración
            int visitCount = visitedCountMap.get(currentPos.getX()).get(currentPos.getY());
            visitedCountMap.get(currentPos.getX()).set(currentPos.getY(), visitCount + 1);

        }
        
        // Métodos para comprobar si las esquinas son alcanzables
        // Esquina superior izquierda
        private boolean upLeftIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(1) == Tile.UNREACHABLE && adjacentTiles.get(1) == Tile.UNREACHABLE;
        }
        
        // Esquina superior derecha
        private boolean upRightIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(1) == Tile.UNREACHABLE && adjacentTiles.get(5) == Tile.UNREACHABLE;
        }
        
        // Esquina inferior izquierda
        private boolean downLeftIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(3) == Tile.UNREACHABLE && adjacentTiles.get(7) == Tile.UNREACHABLE;
        }
        
        // Esquina inferior derecha
        private boolean downRightIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(5) == Tile.UNREACHABLE && adjacentTiles.get(7) == Tile.UNREACHABLE;
        }
        
        @Override
        public boolean done() {
            return behaviourFinished;
        }
        
        private double calculateScore (Position currentPos, Position nextPos) {
           // TODO: Considerar la distancia al objetivo y el número de visitas 
           int deltaX = nextPos.getX() - targetPos.getX();
           int deltaY = nextPos.getY() - targetPos.getY();
    
           double distanceToTarget = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)); // Distancia Euclídea
           int visitCount = visitedCountMap.get(nextPos.getX()).get(nextPos.getY());
           return Integer.MAX_VALUE - distanceToTarget*100 - visitCount; // Ajustar parámetros
        }
    }
    
    /**
     * 
     */   
}
