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
                    if(this.lastMsg.getConversationId().equals(CommManager.CONV_ID_RUDOLF)){
                        System.out.println("Rudolph: Recibido un propose con código correcto!!");
                        resp = this.lastMsg.createReply(ACLMessage.ACCEPT_PROPOSAL);
                        Reindeer ini = getNextReindeer();
                        resp.setContent("PENDING " + ini.getName() + " " + ini.getPosition());
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
                            
                            resp = this.lastMsg.createReply(ACLMessage.INFORM);
                            // Añadir a la respuesta un reno
                            Reindeer next = getNextReindeer();
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
                    else if (this.lastMsg.getPerformative() == ACLMessage.INFORM) {
                        System.out.println("Rudolph: Inform recibido (reno encontrado)\n");
                        /*
                        Obtener el name del mensaje y llamar a foundReindeer para eliminar el reno del array
                        Reindeer reindeer = new Reindeer(this.lastMsg.getContent());
                        foundReindeer(name);
                        */
                    }
                    else {
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