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

    
    
    public ScoutAgent() {
    
    }
    
    public Map getExploredArea(){
        return exploredArea;
    }
    
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
    
 
    @Override
    public void setup(){
        System.out.println("Hello! I'm ScoutAgent.\n");
  
        Sensor.getInstance().setParameters("mapWithDiagonalWall.txt", new Position(8,2), new Position(9,3));
        
        //Para iniciar el agente solo necesitamos que sensores nos indique la posicion relativa al objetivo
        
        setMission(Sensor.getInstance().getTargetRespectAgent());
        
        comportamientosActivos = new Behaviour[]{
            new think_obstacle(),
            new update_position()
        };

   
        this.addBehaviour(new had_finished());
        
        for(Behaviour b : comportamientosActivos){
            this.addBehaviour(b);
        }   
        
    }
    
    
    @Override
    protected void takeDown(){
        System.out.println("Agent may have reached the target. Terminating ScoutAgent...\n");
    }
    

    
    /**
     *  Agente que piensa a dónde debe ir. 
     */
    class think_obstacle extends Behaviour{
       
        @Override
        public void action() {
            if(!targetReached){
                System.out.print("Evaluating next action in think_obstacle.\n");

                System.out.println("Sensor: Actual agent " + Sensor.getInstance().getAgentPosition());
                System.out.println("Sensor: Actual target " + Sensor.getInstance().getTargetPosition());
                System.out.println("Agent: Actual agent " + agentPos);
                System.out.println("Agent: Actual target " + targetPos);

                // Obtiene las casillas adyacentes con el sensor
                vision = Sensor.getInstance().reveal();


                // Evalua las casillas adyacentes y elige la mejor opción
                Action bestAction = null;
                double bestScore = Integer.MAX_VALUE;
                boolean isAccesible;

                for (int i=0; i < vision.size(); i++) {

                    if (i!= 4) {
                        isAccesible = true;
                        Tile tile = vision.get(i);

                        // Obtiene la posicion del array i
                        Position nextPos = agentPos.update(i);

                        // Comprueba que la casilla no sea un obstáculo
                        if(tile.isReacheable()) {
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
                                double score = calculateScore(agentPos, nextPos);

                                if (score < bestScore) {
                                    bestScore = score;
                                    bestAction = Action.values()[i];
                                }
                            }
                        }
                    }
                }

                System.out.println("He salido del bucle \n\n");

                if (bestAction != null) {
                    nextAction = bestAction;
                    System.out.println("nextAction  " + nextAction);
                } else {
                    System.out.print("Ninguna de las opciones posibles es la mejor.");
                }
            
            }
        }
        
        // Métodos para comprobar si las esquinas son alcanzables
        // Esquina superior izquierda
        private boolean upLeftIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(1).isType(Tile.TypeTile.UNREACHABLE) && 
                    adjacentTiles.get(3).isType(Tile.TypeTile.UNREACHABLE);
        }
        
        // Esquina superior derecha
        private boolean upRightIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(1).isType(Tile.TypeTile.UNREACHABLE) &&
                    adjacentTiles.get(5).isType(Tile.TypeTile.UNREACHABLE);
        }
        
        // Esquina inferior izquierda
        private boolean downLeftIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(3).isType(Tile.TypeTile.UNREACHABLE) &&
                    adjacentTiles.get(7).isType(Tile.TypeTile.UNREACHABLE);
        }
        
        // Esquina inferior derecha
        private boolean downRightIsUnreachable (ArrayList<Tile> adjacentTiles) {
            return adjacentTiles.get(5).isType(Tile.TypeTile.UNREACHABLE) &&
                    adjacentTiles.get(7).isType(Tile.TypeTile.UNREACHABLE);
        }
        
        private double calculateScore (Position currentPos, Position nextPos) {
           // TODO: Considerar la distancia al objetivo y el número de visitas 
           int deltaX = nextPos.getX() - targetPos.getX();
           int deltaY = nextPos.getY() - targetPos.getY();
           
           double distanceToTarget = nextPos.getEuclideTo(targetPos);
           int visitCount = exploredArea.getTile(nextPos.getY(), nextPos.getX()).getTimesVisited();
           double score = distanceToTarget*100 + visitCount*200;
           
           System.out.println("- at["+agentPos.getX()+","+agentPos.getY()+"]" + 
                   " -> ["+nextPos.getX()+","+nextPos.getY()+"]*("+visitCount+")"+
                   "  --  d("+deltaX+","+deltaY+") = "+distanceToTarget+"  -  SCORE:"+score);
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
            
            exploredArea.getTile(agentPos).newVisit();  //informamos de paso por casilla
            
            //Actualizamos la posicion del agente en su mapa interno
            agentPos = agentPos.update(nextAction);
            
            
            //Informamos de la accion a sensores
            Sensor.getInstance().setAgentPosition(Sensor.getInstance().getAgentPosition().update(nextAction));            
            
            updateVision();
            
            //If need resize AgentMap
            updateResizeMap();        
            
            System.out.println("-------------------\n");
            
            // this may show in console what the agent know about the map
            // it position and the target
            for(int i=0; i<exploredArea.getNumRows(); i++){
                for(int j=0; j<exploredArea.getNumCols(); j++){
                    Position at = new Position(j,i);
                    Tile t = exploredArea.getTile(i, j);
                    if(at.equals(agentPos))                          System.out.print("A");
                    else if (at.equals(targetPos))                   System.out.print("X");
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
            return targetReached;
        }
    }
    
    /**
     *  Behaviour that verify if the agent had reached the target
     */
    class had_finished extends Behaviour {

        @Override
        public void action() {
            targetReached = Sensor.getInstance().getAgentPosition().equals(Sensor.getInstance().getTargetPosition());
            System.out.println("");
            if(targetReached){
                for(Behaviour b :comportamientosActivos){
                    this.myAgent.removeBehaviour(b);
                }
            }
        }

        @Override
        public boolean done() {
            return targetReached;
        }
        
    }  
}
