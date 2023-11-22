/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import components.*;
import java.util.ArrayList;

import launcher.Launcher;

/**
 *
 * @author vipeba
 */
public class ScoutAgent extends Agent {

    // Internal map (only explored area)
    Map exploredArea;

    // Agent position relative to the internal map
    Position agentPos;

    // Target position relative to the internal map
    Position targetPos;

    // Boolean value that controls if the agent had reached the target
    boolean targetReached;

    // Internal storage of the last vision of the agent
    ArrayList<Tile> vision;

    // Next action to be done
    Action nextAction;

    // List of behaviours added to the agent
    Behaviour[] activeBehaviours;

    public ScoutAgent() {
    }

    /**
     * Method to get the internal map
     * 
     * @return the map that contains the explored area
     */
    public Map getExploredArea() {
        return exploredArea;
    }

    public Position getAgentRelPos() {
        return this.agentPos;
    }

    public Position getTargetRelPos() {
        return this.targetPos;
    }

    /**
     * Method to initialize the internal map
     * It also calls the UpdateVision for the first time
     * 
     * @param targetRespectAgent
     */
    public void setMission(Position targetRespectAgent) {
        // +2 -> una unidad por que al trabajar con la pos relativa, se empieza en 0
        // y otra unidad para la vision del agente
        int rows = Math.abs(targetRespectAgent.getY()) + 2;
        int cols = Math.abs(targetRespectAgent.getX()) + 2;

        if (rows < 3) {
            rows = 3;
        }

        if (cols < 3) {
            cols = 3;
        }

        exploredArea = new Map(cols, rows);

        System.out.println("TARGET_RESPECT_AGENT: " + targetRespectAgent);

        // Set agent and target position
        if (targetRespectAgent.getX() > 0) {

            if (targetRespectAgent.getY() > 0) {
                agentPos = new Position(1, 1);
                targetPos = new Position(cols - 1, rows - 1);
            }

            if (targetRespectAgent.getY() < 0) {
                agentPos = new Position(1, rows - 2);
                targetPos = new Position(cols - 1, 0);
            }

            if (targetRespectAgent.getY() == 0) {
                agentPos = new Position(1, 1);
                targetPos = new Position(cols - 1, 1);
            }
        }

        if (targetRespectAgent.getX() < 0) {

            if (targetRespectAgent.getY() > 0) {
                agentPos = new Position(1, rows - 2);
                targetPos = new Position(cols - 1, 0);
            }

            if (targetRespectAgent.getY() < 0) {
                agentPos = new Position(cols - 2, rows - 2);
                targetPos = new Position(0, 0);
            }

            if (targetRespectAgent.getY() == 0) {
                agentPos = new Position(1, rows - 2);
                targetPos = new Position(1, 0);
            }
        }

        if (targetRespectAgent.getX() == 0) {

            if (targetRespectAgent.getY() > 0) {
                agentPos = new Position(1, 1);
                targetPos = new Position(1, rows - 1);
            }

            if (targetRespectAgent.getY() < 0) {
                agentPos = new Position(1, rows - 2);
                targetPos = new Position(1, 0);
            }

            if (targetRespectAgent.getY() == 0) {
                agentPos = new Position(1, 1);
                targetPos = new Position(1, 1);
            }
        }

        exploredArea.getTile(agentPos).newVisit();
        for (int i = 0; i < exploredArea.getNumRows(); i++) {
            for (int j = 0; j < exploredArea.getNumCols(); j++) {
                Position p = new Position(j, i);
                if (p.equals(agentPos))
                    System.out.print("A");
                else if (p.equals(targetPos))
                    System.out.print("X");
                else
                    System.out.print("0");
            }
            System.out.println("");
        }
        updateVision();
    }

    /**
     * Method to set the values of the agent's adjacent tiles
     * It calls the sensor to take the value tiles
     */
    void updateVision() {

        vision = Sensor.getInstance().reveal();

        int indexVision = 0;

        for (int i = agentPos.getY() - 1; i <= agentPos.getY() + 1; i++) {
            for (int j = agentPos.getX() - 1; j <= agentPos.getX() + 1; j++) {
                exploredArea.setTile(j, i, vision.get(indexVision));
                indexVision++;
            }
        }
    }

    /**
     * Check if Agent Map (exploredArea) needs to be resized because
     * being in the border of the map.
     */
    void updateResizeMap() {

        if (agentPos.getX() == 0) {
            exploredArea.addColToBeggining();
            agentPos = agentPos.update(Action.RIGHT);
            targetPos = targetPos.update(Action.RIGHT);
        } else if (agentPos.getX() == exploredArea.getNumCols() - 1) { // En ultima columna
            exploredArea.addColToEnd();
        }

        if (agentPos.getY() == 0) {
            exploredArea.addRowToBeggining();
            agentPos = agentPos.update(Action.DOWN);
            targetPos = targetPos.update(Action.DOWN);
        } else if (agentPos.getY() == exploredArea.getNumRows() - 1) {
            exploredArea.addRowToEnd();
        }

    }

    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup() {
        System.out.println("Hello! I'm ScoutAgent.\n");

        setMission(Sensor.getInstance().getTargetRespectAgent());

        Behaviour UIupdater = new updateUIBehaviour(this);
        Behaviour thinker = new ThinkObstacleBehaviour(this);
        Behaviour updater = new UpdatePositionBehaviour(this);

        this.addBehaviour(UIupdater); // TODO cambiar esto
        this.addBehaviour(new HadFinishedBehaviour(this));
        this.addBehaviour(thinker);
        this.addBehaviour(updater);

        activeBehaviours = new Behaviour[] {
                UIupdater, thinker, updater
        };

    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has reached the target. Terminating ScoutAgent...\n");
    }
}
