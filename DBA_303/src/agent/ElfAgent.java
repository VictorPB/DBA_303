/*
 * @file    ElfAgent.java
 * @author 
 * @version
 */
package agent;

import agent.behaviours.TargetReachedBehaviour;
import agent.behaviours.ThinkObstacleBehaviour;
import agent.behaviours.UpdatePositionBehaviour;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import components.*;
import java.util.ArrayList;
import agent.behaviours.ElfComunicationBeh;



/**
 * @brief   This class models the Elf Agent that helps Santa and Rudolf to
 *          find the lost reindeers.
 */
public class ElfAgent extends Agent{
    
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
    
    // List of behaviours for movement
    Behaviour[] movementBehaviours;
    
    // Indicates whether the agent has to move or is moving so as not to engage in communication behavior
    boolean isMoving;
    
    /**
     * Constructor
     */
    public ElfAgent() {
        this.exploredArea = new Map(3,3);
        this.agentPos = new Position(1,1);
        
        this.isMoving = false;
    }
    
    // TODO refill with getters, etc
    
    /** GETTERS ***************************************************************/
    /**
     * Get the attribute isMoving
     * 
     */
    public boolean getIsMoving() { return this.isMoving; }
    
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
    
    public boolean isTargetReached() {
        return this.targetReached;
    }
    
    /** SETTERS ***************************************************************/

    /**
     * Set the attribute isMoving
     * @param state boolean to set
     * 
     */
    public void setIsMoving(boolean state) { 
        this.isMoving = state; 
    }
    
    public void setTargetReached(boolean reached) {
        this.targetReached = reached;
        this.isMoving = false;
    }
    
    public Position getAgentPos(){
        return agentPos;
    }
    
    public Position getTargetPos(){
        return targetPos;
    }
    
    public Action getNextAction(){
        return nextAction;
    }
    
    public void setNextAction(Action act){
        this.nextAction = act;
    }
    
    public void setAgentPos(Position pos){
        agentPos = pos;
    }
    
    public ArrayList<Tile> getVision(){
        return vision;
    }
    
    public void setVision(ArrayList<Tile> vis){
        vision = vis;
    }
    
    public Behaviour[] getMovementBehaviours(){
        return movementBehaviours;
    }
    
    /********************************************************/
    //Funciones movimiento
    /**
     * Method to initialize the internal map
     * It also calls the UpdateVision for the first time
     * 
     * @param targetRespectAgent
     */
    public void setMission(Position targetRespectAgent) {

        Position targetRelative = new Position(
                this.agentPos.getX() + targetRespectAgent.getX(),
                this.agentPos.getY() + targetRespectAgent.getY());
        
        // Resize columns
        // to the left (if necesary)
        while(targetRelative.getX() <0){
            this.exploredArea.addColToBeggining();
            targetRelative = targetRelative.update(Action.RIGHT);
            this.agentPos = this.agentPos.update(Action.RIGHT);
        }
        // or to the right (if necesary)
        while(targetRelative.getX() > this.exploredArea.getNumCols() -1 ){
            this.exploredArea.addColToEnd();
        }
        
        // Resize rows
        // above
        while(targetRelative.getY() <0){
            this.exploredArea.addRowToBeggining();
            targetRelative = targetRelative.update(Action.DOWN);
            this.agentPos = this.agentPos.update(Action.DOWN);
        }
        // below
        while(targetRelative.getY() > this.exploredArea.getNumRows()-1){
            this.exploredArea.addRowToEnd();
        }
        
        this.targetPos = targetRelative;
        updateVision();
        
        this.addBehaviour(new TargetReachedBehaviour(this));
        this.addBehaviour(new ThinkObstacleBehaviour(this));
        this.addBehaviour(new UpdatePositionBehaviour(this));
        
        this.setIsMoving(true);
        
        System.out.println("CONFIGURATION FINISHES");
        System.out.println("Agent"+this.agentPos);
        System.out.println("Target: "+this.targetPos);
    }

    /**
     * Private method to prin the internal map in the console
     * for debugging purposes
     */
    private void printInternalMap(){
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
    }
    
    /**
     * Method to set the values of the agent's adjacent tiles
     * It calls the sensor to take the value tiles
     */
    public void updateVision() {

        vision = Sensor.getInstance().reveal();
        int indexVision = 0;

        for (int i = agentPos.getY() - 1; i <= agentPos.getY() + 1; i++) {
            for (int j = agentPos.getX() - 1; j <= agentPos.getX() + 1; j++) {
                exploredArea.setTile(i, j, vision.get(indexVision));
                indexVision++;
            }
        }
    }

    /**
     * Check if Agent Map (exploredArea) needs to be resized because
     * being in the border of the map.
     */
    public void updateResizeMap() {

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
    
    
    /********************************************************/
    
    
    
    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup() {
        System.out.println("Hello! I'm the Elf Agent.\n");
        
        Map map = new Map("mapWithDiagonalWall.txt");        
        Environment.getInstance().setParameters(map);
        
//        Behaviour thinker = new ThinkObstacleBehaviour(this);
//        Behaviour updater = new UpdatePositionBehaviour(this);
        Behaviour comm = new ElfComunicationBeh(this); 
        
        this.addBehaviour(comm);
//        this.addBehaviour(new TargetReachedBehaviour(this));
//        this.addBehaviour(thinker);
//        this.addBehaviour(updater);
       
        //movementBehaviours = new Behaviour[] { thinker, updater };

    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has completed the mision. Terminating ElfAgent...\n");
    }
}