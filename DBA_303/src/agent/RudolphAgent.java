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
        System.out.println("Hello! I'm the Rudolph Agent. \"¡Hiaa, hiaa!\\n");
        
        this.addBehaviour(new RudolphComunicationBeh());
        
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has found all reindeers. Terminating Rudolph...\n");
    }
    
    /**
     * Get the first element of array Reindeers (Environment class)
     * @return the first reindeer in Enviroment class array reindeers
     */
    public Reindeer getNextReindeer() {
        return (Reindeer) Environment.getInstance().getReindeers().get(0);
    }
    
    /**
     * Pops the element of array Reindeers (Environment class) with name name
     * @param name the name of the reindeer to pop
     * @return true if it deletes the reindeer, false otherwhise
     */
    public boolean foundReindeer(Reindeer.Name name) {
        Reindeer reindeer = (Reindeer) Environment.getInstance().getReindeers().stream().filter(r -> r.getName().equals(name)).findAny().orElse(null);
            
        return Environment.getInstance().getReindeers().remove(reindeer);
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

                    if(this.lastMsg.getPerformative() == ACLMessage.PROPOSE){
                        System.out.println("RUDOLPH <--- ELF  ---------------  PROPOSE");
                        if(this.lastMsg.getConversationId().equals(CommManager.CONV_ID_RUDOLPH)){
                            resp = this.lastMsg.createReply(ACLMessage.ACCEPT_PROPOSAL);
                            Reindeer ini = getNextReindeer();
                            resp.setContent("PENDING" + CommManager.SEPARATOR + 
                                            ini.getName().toString() + CommManager.SEPARATOR +              // TODO: getName devuelve numero del ENUM- pasar a String
                                            ini.getPosition().toString(CommManager.SEPARATOR));
                            this.myAgent.send(resp);
                            System.out.println("RUDOLPH ---> ELF --------------- ACCEPT ReindeerName + ReindeerPos\n");
                            state = 1;
                        }
                        else{
                            resp = this.lastMsg.createReply(ACLMessage.REJECT_PROPOSAL);
                            System.out.println("RUDOLPH ---> ELF --------------- REJECT\n");
                            this.myAgent.send(resp);
                        }
                    }else{
                        resp = this.lastMsg.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                            System.out.println("RUDOLPH ---> ELF --------------- UNKNOWN\n");
                        finish = true;
                    }
                    break;
                    
                case 1:
                    this.lastMsg = myAgent.blockingReceive();
                    if(this.lastMsg.getPerformative() == ACLMessage.REQUEST) {
                        System.out.println("RUDOLPH <--- ELF  ---------------  REQUEST nextReindeer");
                        if (foundReindeers < Environment.getInstance().getNumberReindeers()){
                            System.out.println("RUDOLPH ---> ELF --------------- INFORM ReindeerName + ReindeerPos\n");
                            
                            resp = this.lastMsg.createReply(ACLMessage.INFORM);
                            // Añadir a la respuesta un reno
                            Reindeer next = getNextReindeer();
                            resp.setContent("PENDING" + CommManager.SEPARATOR + 
                                            next.getName().toString() + CommManager.SEPARATOR +              // TODO: getName devuelve numero del ENUM- pasar a String
                                            next.getPosition().toString(CommManager.SEPARATOR));
                            myAgent.send(resp);
                        }
                        else {
                            System.out.println("RUDOLPH ---> ELF --------------- INFORM FINISH\n");
                            resp = this.lastMsg.createReply(ACLMessage.INFORM);
                            // Añadir a la respuesta un reno
                            resp.setContent("FINISH");
                            myAgent.send(resp);
                            finish = true;
                        }
                    }
                    else if (this.lastMsg.getPerformative() == ACLMessage.INFORM) {
                        System.out.println("RUDOLPH <--- ELF  ---------------  INFORM found");
                        Reindeer reindeer = new Reindeer(Integer.parseInt(this.lastMsg.getContent()));
                        foundReindeer(reindeer.getName());

                    }
                    else {
                        resp = this.lastMsg.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                        System.out.println("RUDOLPH ---> ELF --------------- UNKNOWN\n");
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
