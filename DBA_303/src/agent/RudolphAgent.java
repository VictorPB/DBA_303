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
import java.util.Random;


/**
 * @brief   This class models the Rudolph Agent that gives to elf the
 * lost reindeers position if he has the secret code
 */
public class RudolphAgent extends Agent{
    

    // List of behaviours added to the agent
    Behaviour[] activeBehaviours;
    
    // Number of found reindeers
    int foundReindeers;
    
    /**
     * Constructor
     */
    public RudolphAgent() {
        super();
        
        foundReindeers = 0;
    }
    
    // TODO refill with getters, etc
    
    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup() {
        System.out.println("Hello! I'm the Rudolph Agent.\n");
        
        this.addBehaviour(new RudolphComunicationBeh());
        
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has found all reindeers. Terminating Rudolph...\n");
    }
    
    /**************************************************************************/
    
    class RudolphComunicationBeh extends Behaviour{
         
        int state = 0;
        boolean finish = false;
        
        @Override
        public void action(){
            switch (state) {
                case 0:
                    ACLMessage msg = myAgent.blockingReceive();
                    if(msg.getConversationId().equals(CommManager.CONV_ID_RUDOLF)){
                        System.out.println("Rudolph: Recibido un propose con código correcto!!");
                        ACLMessage resp = msg.createReply(ACLMessage.ACCEPT_PROPOSAL);
                        // Añadir a la respuesta un reno
                        //resp.setContent(Environment.getInstance().getReindeer(foundReindeers));
                        myAgent.send(resp);
                        state++;
                    }
                    else{
                        ACLMessage resp = msg.createReply(ACLMessage.REJECT_PROPOSAL);
                        System.out.println("Rudolph: NO TE ENTIENDO (código incorrecto)\n");
                        this.myAgent.send(resp);
                    }
                    break;
                    
                case 1:
                    msg = myAgent.blockingReceive();
                    if(msg.getConversationId().equals(CommManager.CONV_ID_RUDOLF) 
                     /*&& foundReindeers < Environment.getInstance().getNumberReindeers*/){
                        System.out.println("Rudolph: Recibido un nuevo reno!!");
                        foundReindeers++;
                        ACLMessage resp = msg.createReply(ACLMessage.ACCEPT_PROPOSAL);
                        // Añadir a la respuesta un reno
                        //resp.setContent(Environment.getInstance().getReindeer(foundReindeers));
                        myAgent.send(resp);
                    }
                    else{
                        ACLMessage resp = msg.createReply(ACLMessage.REJECT_PROPOSAL);
                        System.out.println("Rudolph: NO QUEDAN RENOS POR BUSCAR\n");
                        this.myAgent.send(resp);
                        finish = true;
                    }
                    break;
            }
        }
        
        @Override
        public boolean done(){
            return finish; 
        }
        
    }
}
