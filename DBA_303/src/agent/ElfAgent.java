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
    boolean isTalking;
    
    /**
     * Constructor
     */
    public ElfAgent() {
        this.exploredArea = new Map(3,3);
        this.agentPos = new Position(1,1);
        
        this.isTalking = false;
    }
    
    // TODO refill with getters, etc
    
    /** GETTERS ***************************************************************/
    /**
     * Get the attribute isMoving
     * 
     */
    public boolean getTalking() { return this.isTalking; }
    
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
    public void setTalking(boolean state) { 
        this.isTalking = state; 
    }
    
    public void setTargetReached(boolean reached) {
        this.targetReached = reached;
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
        
        Behaviour thinker = new ThinkObstacleBehaviour(this);
        Behaviour updater = new UpdatePositionBehaviour(this);
        Behaviour comm = new ElfComunicationBeh(); 
        
        this.addBehaviour(comm);
        this.addBehaviour(new TargetReachedBehaviour(this));
        this.addBehaviour(thinker);
        this.addBehaviour(updater);
       
        movementBehaviours = new Behaviour[] { thinker, updater };

    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has completed the mision. Terminating ElfAgent...\n");
    }
    
    
    /**************************************************************************/
    
    class ElfComunicationBeh extends Behaviour{
         
        int state = 0;
        boolean finish = false;
        boolean allReindeerFound = false;
        
        String secretCode;
        Position rudolphPos;
        String reindeerName;
        Position reindeerPos;
        Position santaPos;
        
        ACLMessage lastMsgSanta;
        ACLMessage lastMsgRudolph;
        
        @Override
        public void action(){
            if (getTalking()) {
            
                ACLMessage msg;
                ACLMessage replySanta;
                ACLMessage replyRudolph;

                switch (state) {
                    //SANTA

                    case 0:
                        System.out.println("ELF ---> SANTA --------------- PROPOSE mission");
                        msg = new ACLMessage(ACLMessage.PROPOSE);
                        msg.addReceiver(new AID(CommManager.AID_SANTA,AID.ISLOCALNAME));
                        msg.setConversationId(CommManager.CONV_ID_SANTA);
                        msg.setContent("Elf: Me propongo voluntario para buscar los renos perdidos");
                        myAgent.send(msg);

                        this.state = 1;
                        break;

                    case 1:
                        this.lastMsgSanta = myAgent.blockingReceive();
                        if(this.lastMsgSanta.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                            String santaValidationContent = this.lastMsgSanta.getContent();
                            System.out.println("ELF <--- SANTA  ---------------  ACCEPT code + rudoplhPos");
                            System.out.println("       msg: " + santaValidationContent+"\n");

                            //Decode Message Get Secret Code and Rudolf Pos
                            decodeMSG(santaValidationContent);

                            System.out.println("       code: "+this.secretCode + " ---- rudolphPos: " +this.rudolphPos.toString()+"\n\n");

                            //TODO: desplazarse hacia Rudolf
                            setTalking(false);
                            setMission(rudolphPos);
                        }
                        else{
                            System.out.println("ELF <--- SANTA  ---------------  REJECT");
                            this.finish = true;
                        }

                        this.state = 2;
                        break;

                    //RUDOLF

                    case 2:
                        msg = new ACLMessage(ACLMessage.PROPOSE);
                        msg.addReceiver(new AID(CommManager.AID_RUDOLPH,AID.ISLOCALNAME));
                        msg.setConversationId(secretCode);
                        msg.setContent("Elf: Hola Rudolf, este es el codigo secreto:"+this.secretCode);               
                        System.out.println("ELF ---> RUDOLPH  ---------------  PROPOSE code " + this.secretCode);
                        myAgent.send(msg);                  

                        this.state = 3;
                        break;

                    case 3:
                        this.lastMsgRudolph = myAgent.blockingReceive();
                        if(this.lastMsgRudolph.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                            String reindeerPosContent = this.lastMsgRudolph.getContent();
                            System.out.println("ELF <--- RUDOLPH  ---------------  ACCEPT ReindeerName + ReindeerPos");

                            //Decode Message Get first lost reindeer
                            decodeMSG(reindeerPosContent);

                            System.out.println("       ReindeerName: "+this.reindeerName + " ---- ReindeerPos: " +this.reindeerPos.toString()+"\n\n");

                            //TODO: desplazarse hacia primer reno perdido

                            //TODO: volver a Rudolf
                        }
                        else{
                            System.out.println("ELF <--- RUDOLPH  ---------------  REJECT");

                            this.finish = true;
                        }

                        this.state = 4;                 
                        break;

                    case 4:         //Informamos reno encontrado y pedimos siguiente reno
                        // Inform to rudolph of the found reindeer
                        replyRudolph = this.lastMsgRudolph.createReply(ACLMessage.INFORM);
                        replyRudolph.setContent(this.reindeerName);
                        System.out.println("ELF ---> RUDOLPH  ---------------  INFORM found reindeer " + this.reindeerName);

                        this.myAgent.send(replyRudolph);

                        // Inform to santa of the found reindeer
                        replySanta = this.lastMsgSanta.createReply(ACLMessage.INFORM);
                        replySanta.setContent(this.reindeerName);
                        System.out.println("ELF ---> SANTA  ---------------  INFORM found reindeer " + this.reindeerName);

                        this.myAgent.send(replySanta);

                        replyRudolph = this.lastMsgRudolph.createReply(ACLMessage.REQUEST);
                        System.out.println("ELF ---> RUDOLPH  ---------------  REQUEST nextReindeer");
                        this.myAgent.send(replyRudolph);

                        this.state = 5;
                        break;

                    case 5:         //Obtenemos coordenadas
                        this.lastMsgRudolph = myAgent.blockingReceive();

                        if(this.lastMsgRudolph.getPerformative() == ACLMessage.INFORM){
                            String reindeerPosContent = this.lastMsgRudolph.getContent();
                            System.out.println("ELF <--- RUDOLPH  ---------------  INFORM ReindeerName + ReindeerPos");

                            //Decode Message Get first lost reindeer
                            decodeMSG(reindeerPosContent);

                            if(!this.allReindeerFound){
                                System.out.println("       ReindeerName: "+this.reindeerName + " ---- ReindeerPos: " +this.reindeerPos.toString()+"\n\n");

                                //TODO: desplazarse hacia siguiente reno perdido

                                //TODO: volver a Rudolf

                                this.state = 4;
                            }else{
                                System.out.println("ELF <--- RUDOLPH  ---------------  INFORM FINISH");
                                this.state = 6;
                            }
                        }else{
                            System.out.println("Elf: Rudolf no mando un INFORM.\n");

                            this.finish = true;
                        }

                        break;

                    case 6:         //Request Santa position
                        replySanta = this.lastMsgSanta.createReply(ACLMessage.REQUEST);
                        System.out.println("ELF ---> SANTA --------------- REQUEST SantaPos");
                        this.myAgent.send(replySanta);

                        this.state = 7;
                        break;

                    case 7:
                        this.lastMsgSanta = myAgent.blockingReceive();
                        if(this.lastMsgSanta.getPerformative() == ACLMessage.INFORM){
                            String santaLocationContent = this.lastMsgSanta.getContent();
                            System.out.println("ELF <--- SANTA  ---------------  INFORM SantaPos");

                            //Decode Message Get Santa Pos
                            decodeMSG(santaLocationContent);

                                System.out.println("       SantaPos: "+this.santaPos.toString()+"\n\n");

                            //TODO: desplazarse hacia Santa

                        }else{
                            System.out.println("Elf: Mi pana el santa no me mando bien la ubi\n");
                            this.finish = true;
                        }
                        this.state = 8;
                        break;

                    case 8:
                        replySanta = this.lastMsgSanta.createReply(ACLMessage.INFORM);
                        System.out.println("Elf: Hola santa, ya estoy aqui. Mision Completada");
                        System.out.println("ELF ---> SANTA --------------- INFORM");
                        this.myAgent.send(replySanta);

                        this.state = 9;
                        break; 

                    case 9:
                        this.lastMsgSanta = myAgent.blockingReceive();
                        if(this.lastMsgSanta.getPerformative() == ACLMessage.INFORM){
                            String santaThanksContent = this.lastMsgSanta.getContent();
                            System.out.println("Elf: Santa me agradecio la labor.");
                            this.finish = true;
                        }else{
                            System.out.println("Elf: Santa manda performativa final mal\n");
                            this.finish = true;
                        }
                        break;
                }
            }
        }
        
        @Override
        public boolean done(){
            return finish;
        }
        
        
        /**
         * Decodes messages containing a string and a position and stores the 
         * information in the output parameters.
         * 
         * Used to obtain Rudolf's secret code and position. 
         * Used to obtain name and position of lost reindeer.
         * 
         * The message encoding is preset and depends on the programmer,
         * using SEPARATOR to delimit the fields in the encodedMessage.
         * 
         * Example encode message: "Rudolf;xAeJtxC;12;26"  "PENDING ;BLITZEN;28;17"
         * 
         * @param encodedMessage
         * @param outputString
         * @param outputPosition 
         */
        void decodeMSG(String encodedMessage){
            
            String[] parts = encodedMessage.split(CommManager.SEPARATOR);
            
            if("SecretCode".equals(parts[0])){
                this.secretCode = parts[1];
                this.rudolphPos = new Position(Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
            }else if("PENDING".equals(parts[0])){
                reindeerName = parts[1];
                reindeerPos = new Position(Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
            }else if("FINISH".equals(parts[0])){
                allReindeerFound = true;
            }else if("SANTA".equals(parts[0])){
                santaPos = new Position(Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));     
            }else {
                System.err.println("----- ERROR EN DECODICIFACION DE MENSAJE -----");
            }
            
        }
        
    }
}
