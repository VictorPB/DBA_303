/*
 * @file    ElfAgent.java
 * @author 
 * @version
 */
package agent;

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

    // Internal storage of the last vision of the agent
    ArrayList<Tile> vision;

    // Next action to be done
    Action nextAction;

    // List of behaviours added to the agent
    Behaviour[] activeBehaviours;
    
    /**
     * Constructor
     */
    public ElfAgent() {
        this.exploredArea = new Map(3,3);
        this.agentPos = new Position(1,1);
    }
    
    // TODO refill with getters, etc
    
    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup() {
        System.out.println("Hello! I'm the Elf Agent.\n");
        
        this.addBehaviour(new ElfComunicationBeh());
        
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has reached the target. Terminating ElfAgent...\n");
    }
    
    
    /**************************************************************************/
    
    class ElfComunicationBeh extends Behaviour{
         
        int state = 0;
        boolean finish = false;
        
        String secretCode;
        Position rudolfPos;
        
        
        @Override
        public void action(){
            switch (state) {
                
                //SANTA
                
                case 0:
                    ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
                    msg.addReceiver(new AID(CommManager.AID_SANTA,AID.ISLOCALNAME));
                    msg.setConversationId(CommManager.CONV_ID_SANTA);
                    msg.setContent("Me propongo voluntario para buscar los renos perdidos");
                    myAgent.send(msg);
                    
                    this.state = 1;
                    break;
                
                case 1:
                    ACLMessage santaValidation = myAgent.blockingReceive();
                    if(santaValidation.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                        String santaValidationContext = santaValidation.getContent();
                        System.out.println("Elf: Santa me aceptó");
                        System.out.println("       msg: "+santaValidationContext+"\n");
                        
                        //Decode Message Get Secret Code and Rudolf Pos
                        decodeMSG(santaValidationContext, secretCode, rudolfPos);
                    }
                    else{
                        System.out.println("Elf: Santa me rechazó\n");
                    }
                    this.finish = true;
                    
                    break;
                    
                //RUDOLF    
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
         * Example encode message: "xAeJtxC;12;26"  "reno;28;17"
         * 
         * @param encodedMessage
         * @param outputString
         * @param outputPosition 
         */
        void decodeMSG(String encodedMessage, String outputString, Position outputPosition){
            final char SEPARATOR = ';';
            
            String[] parts = encodedMessage.split(";");
            
            outputString = parts[0];
            
            outputPosition = new Position(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
        }
        
    }
}
