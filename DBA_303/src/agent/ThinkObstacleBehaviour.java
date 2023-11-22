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
 * @brief Class that makes the behaviour to decide the next move of an agent.
 * 
 * @author carlos
 */
public class ThinkObstacleBehaviour extends Behaviour {

    // Private atribute to access the agent that uses the behaviour
    private final ScoutAgent myAgent;
    
    public ThinkObstacleBehaviour(ScoutAgent agent) {
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        if(!myAgent.targetReached){
            System.out.println("Evaluating...");
            System.out.println("Ag_abs" + Sensor.getInstance().getAgentPosition() + 
                    "   Target_abs"+Sensor.getInstance().getTargetPosition());

            // The agent takes the ajacents tiles with the sensor
            myAgent.vision = Sensor.getInstance().reveal();

            // The agent checks the adjacents tiles and decides the best one
            // by their values. The best one will be the tile whose score is lower.
            // The score of a tile is calculated with the euclidean distance to
            // the target plus the times the agent has visited the tile he's checking
            Action bestAction = null;
            double bestScore = Integer.MAX_VALUE;
            boolean isAccesible;

            for (int i=0; i < myAgent.vision.size(); i++) {

                if (i!= 4) {
                    isAccesible = true;
                    Tile tile = myAgent.vision.get(i);
                    Position nextPos = myAgent.agentPos.update(Action.fromValue(i));

                    // It checks that the tile is reacheable
                    if(tile.isReacheable()) {
                        // It checks if he is evaluating a corner
                        // If it is a corner, it checks if it's not reacheable
                        if (i == 0 && !upLeftIsReachable(myAgent.vision)) {
                            isAccesible = false;
                        } else if (i == 2 && !upRightIsReachable(myAgent.vision)) {
                            isAccesible = false;
                        } else if (i == 6 && !downLeftIsReachable(myAgent.vision)) {
                            isAccesible = false;
                        } else if (i == 8 && !downRightIsReachable(myAgent.vision)) {
                            isAccesible = false;
                        }

                        // Finally, if the tile is reacheable, it calculates his score
                        if (isAccesible) {
                            double score = calculateScore(myAgent.agentPos, nextPos);

                            // If the score is better, it saves it
                            if (score < bestScore) {
                                bestScore = score;
                                bestAction = Action.values()[i];
                            }
                        }
                    }
                }
            }

            if (bestAction != null) {
                myAgent.nextAction = bestAction;
                System.out.println("nextAction  " + myAgent.nextAction);
            } else {
                System.out.println("Ninguna de las opciones posibles es la mejor.");
                // TODO: Implement an exit failture
            }

        }
    }

    /**
     * Methods to check if a corner is reacheable
     * @param adjacentTiles
     * @return boolean (true = reacheable, false = unreacheable)
     */
    // Up-left corner
    private boolean upLeftIsReachable (ArrayList<Tile> adjacentTiles) {
        return adjacentTiles.get(1).isReacheable() || 
                adjacentTiles.get(3).isReacheable();
    }

    // Up-right corner
    private boolean upRightIsReachable (ArrayList<Tile> adjacentTiles) {
        return adjacentTiles.get(1).isReacheable() ||
                adjacentTiles.get(5).isReacheable();
    }

    // Down-left corner
    private boolean downLeftIsReachable (ArrayList<Tile> adjacentTiles) {
        return adjacentTiles.get(3).isReacheable() ||
                adjacentTiles.get(7).isReacheable();
    }

    // Down-right corner
    private boolean downRightIsReachable (ArrayList<Tile> adjacentTiles) {
        return adjacentTiles.get(5).isReacheable() ||
                adjacentTiles.get(7).isReacheable();
    }

    /**
     * Method to calculate the score of the nextPos tile
     * The score is the distance from the adjacent tile the agent is evaluating 
     * to target multiplied by a constant plus the times the agent has visited 
     * this adjacent tile multiplied by a constant.
     * @param currentPos, it is the position of the agent
     * @param nextPos, it is one adjacent tile to the agent
     * @return the score for a tile
     */
    private double calculateScore (Position currentPos, Position nextPos) {

       double distanceToTarget = nextPos.getEuclideTo(myAgent.targetPos);
       int visitCount = myAgent.exploredArea.getTile(nextPos.getY(), nextPos.getX()).getTimesVisited();
       double score = distanceToTarget*100 + visitCount*200;

//       //For debugging purposes
//       System.out.println("- at["+myAgent.agentPos.getX()+","+myAgent.agentPos.getY()+"]" + 
//               " -> ["+nextPos.getX()+","+nextPos.getY()+"]*("+visitCount+")"+
//               "  --  d("+deltaX+","+deltaY+") = "+distanceToTarget+"  -  SCORE:"+score);

       return score;
    }

    @Override
    public boolean done() {
        return myAgent.targetReached;
    }
}
