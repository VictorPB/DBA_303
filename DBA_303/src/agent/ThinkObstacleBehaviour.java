/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent;

import components.Action;
import components.Position;
import components.Tile;
import jade.core.behaviours.Behaviour;
import java.util.ArrayList;

/**
 *
 * @author carlos
 */
public class ThinkObstacleBehaviour extends Behaviour {

    private final ScoutAgent myAgent;
    
    public ThinkObstacleBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        if(!myAgent.targetReached){
            System.out.print("Evaluating next action in think_obstacle.\n");
            System.out.println("Sensor: Actual agent " + Sensor.getInstance().getAgentPosition());
            System.out.println("Sensor: Actual target " + Sensor.getInstance().getTargetPosition());
            System.out.println("Agent: Actual agent " + myAgent.agentPos);
            System.out.println("Agent: Actual target " + myAgent.targetPos);

            // Obtiene las casillas adyacentes con el sensor
            myAgent.vision = Sensor.getInstance().reveal();


            // Evalua las casillas adyacentes y elige la mejor opción
            Action bestAction = null;
            double bestScore = Integer.MAX_VALUE;
            boolean isAccesible;

            for (int i=0; i < myAgent.vision.size(); i++) {

                if (i!= 4) {
                    isAccesible = true;
                    Tile tile = myAgent.vision.get(i);

                    // Obtiene la posicion del array i
                    Position nextPos = myAgent.agentPos.update(i);

                    // Comprueba que la casilla no sea un obstáculo
                    if(tile.isReacheable()) {
                        // Comprobamos si las esquinas son accesibles
                        if (i == 0 && upLeftIsUnreachable(myAgent.vision)) {
                            isAccesible = false;
                        } else if (i == 2 && upRightIsUnreachable(myAgent.vision)) {
                            isAccesible = false;
                        } else if (i == 6 && downLeftIsUnreachable(myAgent.vision)) {
                            isAccesible = false;
                        } else if (i == 8 && downRightIsUnreachable(myAgent.vision)) {
                            isAccesible = false;
                        }

                        if (isAccesible) {
                            double score = calculateScore(myAgent.agentPos, nextPos);

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
                myAgent.nextAction = bestAction;
                System.out.println("nextAction  " + myAgent.nextAction);
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
       int deltaX = nextPos.getX() - myAgent.targetPos.getX();
       int deltaY = nextPos.getY() - myAgent.targetPos.getY();

       double distanceToTarget = nextPos.getEuclideTo(myAgent.targetPos);
       int visitCount = myAgent.exploredArea.getTile(nextPos.getY(), nextPos.getX()).getTimesVisited();
       double score = distanceToTarget*100 + visitCount*200;

       System.out.println("- at["+myAgent.agentPos.getX()+","+myAgent.agentPos.getY()+"]" + 
               " -> ["+nextPos.getX()+","+nextPos.getY()+"]*("+visitCount+")"+
               "  --  d("+deltaX+","+deltaY+") = "+distanceToTarget+"  -  SCORE:"+score);
       return score; // Ajustar parámetros
    }

    @Override
    public boolean done() {
        return myAgent.targetReached;
    }
}
