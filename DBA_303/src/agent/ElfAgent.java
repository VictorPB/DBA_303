/*
 * @file    ElfAgent.java
 * @author 
 * @version
 */
package agent;

import agent.behaviours.HadFinishedBehaviour;
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
public class ElfAgent extends ScoutAgent{
    
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
    
    /** SETTERS ***************************************************************/

    /**
     * Set the attribute isMoving
     * @param state boolean to set
     * 
     */
    public void setTalking(boolean state) { 
        this.isTalking = state; 
    }
    
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
        this.addBehaviour(new HadFinishedBehaviour(this));
        this.addBehaviour(thinker);
        this.addBehaviour(updater);
        
        activeBehaviours = new Behaviour[] { comm, thinker, updater };

    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has reached the target. Terminating ElfAgent...\n");
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
