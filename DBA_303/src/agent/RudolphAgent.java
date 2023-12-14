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
import jade.util.leap.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
    
    // Array of lost reindeers
    private ArrayList<Reindeer> reindeers;
        
    /**
     * Constructor that initialize the lost reindeers array
     */
    public RudolphAgent() {
        super();
        
        List<Reindeer> aux = new LinkedList<Reindeer>();
        this.reindeers = new ArrayList<>();
        
        int numReindeers = Environment.getInstance().getNumberReindeers();
        
        for (int i = 0; i < numReindeers; i++ ) {
            Reindeer reindeer = Environment.getInstance().getReindeer(i);
            aux.add(reindeer);
        }
        
        // Desordenar lista
        Collections.shuffle(aux);
        
        for (int i = 0; i < aux.size(); i++) {
            this.reindeers.add(aux.get(i));
        }
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
        ACLMessage lastMsg;
        
        @Override
        public void action(){
            ACLMessage resp;
            
            switch (state) {
                case 0:
                    this.lastMsg = myAgent.blockingReceive();
                    if(this.lastMsg.getConversationId().equals(CommManager.CONV_ID_RUDOLF)){
                        System.out.println("Rudolph: Recibido un propose con código correcto!!");
                        resp = this.lastMsg.createReply(ACLMessage.ACCEPT_PROPOSAL);
                        System.out.println(Environment.getInstance().getReindeer(foundReindeers).toString());
                        Reindeer first = Environment.getInstance().getReindeer(foundReindeers);
                        resp.setContent(first.toString());
                        this.myAgent.send(resp);
                        state = 1;
                    }
                    else{
                        resp = this.lastMsg.createReply(ACLMessage.REJECT_PROPOSAL);
                        System.out.println("Rudolph: NO TE ENTIENDO (código incorrecto)\n");
                        this.myAgent.send(resp);
                    }
                    break;
                    
                case 1:
                    this.lastMsg = myAgent.blockingReceive();
                    if(this.lastMsg.getPerformative() == ACLMessage.REQUEST) {
                        System.out.println("Rudolph: Request recibida\n");
                        if (foundReindeers < Environment.getInstance().getNumberReindeers()){
                            System.out.println("Rudolph: Te envio las coordenadas del siguiente reno\n\n");
                            foundReindeers++;
                            resp = this.lastMsg.createReply(ACLMessage.INFORM);
                            // Añadir a la respuesta un reno
                            Reindeer next = Environment.getInstance().getReindeer(foundReindeers);
                            resp.setContent("PENDING " + next.getName() + " " + next.getPosition());
                            myAgent.send(resp);
                        }
                        else {
                            System.out.println("Rudolph: No quedan renos perdidos\n\n");
                            resp = this.lastMsg.createReply(ACLMessage.INFORM);
                            // Añadir a la respuesta un reno
                            resp.setContent("FINISH");
                            myAgent.send(resp);
                            finish = true;
                        }
                    }
                    else{
                        resp = this.lastMsg.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                        System.out.println("Rudoplh: Recibido mensaje inesperado\n");
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
