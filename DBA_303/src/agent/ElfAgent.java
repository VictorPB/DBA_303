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
        
        Map map = new Map("mapWithDiagonalWall.txt");
        System.out.println("Generating new Environment...");
        
        Environment.getInstance().setParameters(map);
        
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
        boolean allReindeerFound = false;
        
        String secretCode;
        Position rudolphPos;
        String reindeerName;
        Position reindeerPos;
        
        ACLMessage lastMsgSanta;
        ACLMessage lastMsgRudolph;
        
        @Override
        public void action(){
            
            ACLMessage msg;
            
            switch (state) {
                
                
                //SANTA
                
                case 0:
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
                        System.out.println("Elf: Santa me aceptó");
                        System.out.println("       msg: "+santaValidationContent+"\n");
                                             
                        //Decode Message Get Secret Code and Rudolf Pos
                        decodeMSG(santaValidationContent);
                        
                        System.out.println("Elf: Tengo el codigo para hablar con Rudolf:"+this.secretCode);
                        System.out.println("Elf: Lo encontrare en la posicion:"+this.rudolphPos.toString()+"\n");
                        
                        //TODO: desplazarse hacia Rudolf
                    }
                    else{
                        System.out.println("Elf: Santa me rechazó\n");
                        this.finish = true;
                    }
                    
                    this.state = 2;
                    break;
                    
                //RUDOLF
                    
                case 3:
                    msg = new ACLMessage(ACLMessage.PROPOSE);
                    msg.addReceiver(new AID(CommManager.AID_RUDOLPH,AID.ISLOCALNAME));
                    msg.setConversationId(secretCode);
                    msg.setContent("Elf: Hola Rudolf, este es el codigo secreto:"+this.secretCode);               
                    System.out.println(msg.getContent());
                    System.out.println("Elf: Santa me ha aceptado mi proposicion para encontrar los renos perdidos.");
                    myAgent.send(msg);                  
                    
                    this.state = 4;
                    break;
                    
                case 4:
                    this.lastMsgRudolph = myAgent.blockingReceive();
                    if(this.lastMsgRudolph.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                        String reindeerPosContent = this.lastMsgRudolph.getContent();
                        System.out.println("Elf: Rudolf me habla!");
                        System.out.println("       msg: "+reindeerPosContent+"\n");
                        
                        //Decode Message Get Secret Code and Rudolf Pos
                        decodeMSG(reindeerPosContent);
                        
                        System.out.println("Elf: Encontrare a "+this.reindeerName+" en la posicion:"+this.reindeerPos.toString()+"\n");
                        
                        //Decode Message Get first lost reindeer
                        decodeMSG(reindeerPosContent);
                        
                        //TODO: desplazarse hacia reno perdido
                        
                    }
                    else{
                        System.out.println("Elf: Rudolf no habla conmigo.\n");
                        
                        this.finish = true;
                    }
                    
                    this.state = 5;
                    
                    break;
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
         * Position toString pasa esto (5,9)   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * Además rudolf devuelve primero PENDING o FINISH
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
            }
            
            
        }
        
    }
}
