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
    
    boolean targetReached;
    
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
        
        this.agentPos = new Position(7,7);
        this.targetPos = new Position(5,5);
        
        if (!this.agentPos.equals(this.targetPos)) {
            targetReached = false;
        }
        
        Sensor.getInstance().setAgentPosition(agentPos);
  
        Sensor.getInstance().setParameters("mapWithComplexObstacle1.txt", agentPos, targetPos);
        
        // Inicialización del mapa que lleva la cuenta de las veces que he pasado por una casilla
        this.visitedCountMap = new ArrayList<>();  
        for (int i = 0; i < Sensor.getInstance().getMap().getNumRows(); i++){
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < Sensor.getInstance().getMap().getNumCols(); j++) {
                row.add(0);  
            }
            this.visitedCountMap.add(row);
        }
        
        //setMission(this.targetPos, this.vision);
        
        // this.addBehaviour(new think_Manhattan());
        this.addBehaviour(new had_finished());
        this.addBehaviour(new think_obstacle());
        this.addBehaviour(new update_position());
        
    }
    
    
    @Override
    protected void takeDown(){
        System.out.println("Agent may have reached the target. Terminating ScoutAgent...\n");
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
     *  Agente que piensa a dónde debe ir. 
     */
    class think_obstacle extends Behaviour{
       
        @Override
        public void action() {
            System.out.print("Evaluating next action in think_obstacle.\n");
            
            System.err.println("Actual agent " + Sensor.getInstance().getAgentPosition());
           
            Position currentPos = new Position(Sensor.getInstance().getAgentPosition());
            
            // Obtiene las casillas adyacentes con el sensor
            vision = Sensor.getInstance().reveal();
            
            
            // Evalua las casillas adyacentes y elige la mejor opción
            Action bestAction = null;
            double bestScore = -100.0;
            boolean isAccesible;
                        
            for (int i=0; i < vision.size(); i++) {
                System.out.println("Valor de i: " + i);
                
                if (i!= 4) {
                    isAccesible = true;
                    Tile tile = vision.get(i);
                
                    // Obtiene la posicion del array i
                    Position nextPos = currentPos.update(i);
                    
                    // Comprueba que la casilla no sea un obstáculo
                    if(tile != Tile.UNREACHABLE) {
                        // Comprobamos si las esquinas son accesibles
                        if (i == 0 && upLeftIsUnreachable(vision)) {
                            isAccesible = false;
                        } else if (i == 2 && upRightIsUnreachable(vision)) {
                            isAccesible = false;
                        } else if (i == 6 && downLeftIsUnreachable(vision)) {
                            isAccesible = false;
                        } else if (i == 8 && downRightIsUnreachable(vision)) {
                            isAccesible = false;
                        }
                    
                        if (isAccesible) {
                            double score = calculateScore (currentPos, nextPos);

                            if (score > bestScore) {
                                bestScore = score;
                                System.out.println("Action.values()[i] " + Action.values()[i] + " i " + i);
                                bestAction = Action.values()[i];
                            }
                        }
                    }
                }
            }
            
            System.out.println("He salido del bucle \n\n");
            
            if (bestAction != null) {
                nextAction = bestAction;
                System.err.println("nextAction  " + nextAction);
            } else {
                System.err.print("Ninguna de las opciones posibles es la mejor.");
            }
            
            // Actualizar posición del agente
            
            // Actualizar mapas de exploración
            // exploredArea.setTile(currentPos.getX(), currentPos.getY(), Tile.EMPTY);
            
            // Sumar una visita en el mapa de exploración
            System.out.println("visitCount - - - " + visitedCountMap.get(currentPos.getX()).get(currentPos.getY()));
            int visitCount = visitedCountMap.get(currentPos.getX()).get(currentPos.getY());
            visitedCountMap.get(currentPos.getX()).set(currentPos.getY(), visitCount + 1);
        }
        
        // Métodos para comprobar si las esquinas son alcanzables
        // Esquina superior izquierda
        private boolean upLeftIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(1) == Tile.UNREACHABLE && adjacentTiles.get(3) == Tile.UNREACHABLE;
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
        
        private double calculateScore (Position currentPos, Position nextPos) {
            System.out.println("En calculateScore: currentPos " + currentPos + ", nextPos " + nextPos+ "\n");
           // TODO: Considerar la distancia al objetivo y el número de visitas 
           int deltaX = nextPos.getX() - targetPos.getX();
           int deltaY = nextPos.getY() - targetPos.getY();
           
            System.out.println("\n\ndeltaX :" + deltaX + ", deltaY :" + deltaY + "\n");
    
           double distanceToTarget = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)); // Distancia Euclídea
           int visitCount = visitedCountMap.get(nextPos.getX()).get(nextPos.getY());
            System.out.println("distanceToTarget: " + distanceToTarget);
           double score = 1000 - distanceToTarget*100 - visitCount*200;
           System.out.println("score: " + score);
           return score; // Ajustar parámetros
        }
        
        @Override
        public boolean done() {
            return targetReached;
        }
    }
    
    /**
     * Behaviour that update the position of the agent
     */
    class update_position extends Behaviour{
        @Override
        public void action() {
            Position pos = Sensor.getInstance().getAgentPosition();
           
            Sensor.getInstance().setAgentPosition(pos.update(nextAction)); 
            
            System.out.println();
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            
            
        }
        @Override
        public boolean done(){
           return targetReached; 
        }
    }
    
    /**
     * Behaviour that print the position of the agent
     */ 
    class print extends Behaviour{
        @Override
        public void action() {
            // Imprimir la posición
            Position currentPosition = Sensor.getInstance().getAgentPosition();
            
            System.out.println(currentPosition);
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        @Override
        public boolean done(){
           return true; 
        }
    }
    
    /**
     *  Behaviour that verify if the agent had reached the target
     */
    class had_finished extends Behaviour {

        @Override
        public void action() {
            if (Sensor.getInstance().getAgentPosition().equals(Sensor.getInstance().getTargetPosition())) {
                targetReached = true;
            }
            else {
                targetReached = false;
            }
        }

        @Override
        public boolean done() {
            return targetReached;
        }
        
        
        
    }  
}
