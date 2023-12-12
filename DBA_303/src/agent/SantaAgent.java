/*
 * @file    SantaAgent.java
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
 * @brief   This class models the Santa Agent that checks if the volunteer elf
 *          deserves to do the mision and comunicates the rudolf secret code
 */
public class SantaAgent extends Agent{
    

    // List of behaviours added to the agent
    Behaviour[] activeBehaviours;
    
    /**
     * Constructor
     */
    public SantaAgent() {
        super();
    }
    
    // TODO refill with getters, etc
    
    /**
     * In setup we initialize the agent, we set the sensor
     * and add to queue of Behaviours the actions that the
     * agent needs
     */
    @Override
    public void setup() {
        System.out.println("Hello! I'm the Santa Agent.\n");
        
        this.addBehaviour(new SantaComunicationBeh());
        
    }

    @Override
    protected void takeDown() {
        System.out.println("Agent has reached the target. Terminating Santa...\n");
    }
    
    /**
     * Method that randomly checks if an elf deserves to do the mission.
     * @return true 80% and false 20%
     */
    boolean radomElfAprove(){
        int value = new Random().nextInt(5);
        System.out.println("   evaluating elf with a ..."+value);
        return value >0;
        //return (new Random().nextInt(5)) >0 ;
    }
    
    /**************************************************************************/
    
    class SantaComunicationBeh extends Behaviour{
         
        int state = 0;
        boolean finish = false;
        
        @Override
        public void action(){
            switch (state) {
                case 0:
                    ACLMessage msg = myAgent.blockingReceive();
                    if(msg.getConversationId().equals(CommManager.CONV_ID_SANTA)){
                        System.out.println("Santa: Recibido un propose!!");
                        if(radomElfAprove()){
                            ACLMessage resp = msg.createReply(ACLMessage.ACCEPT_PROPOSAL);
                            // TODO Añadir los mensajes con la dirección de Rudolf
                            resp.setContent(CommManager.CONV_ID_RUDOLF);
                            myAgent.send(resp);
                        }
                        else{
                            ACLMessage resp = msg.createReply(ACLMessage.REJECT_PROPOSAL);
                            System.out.println("Santa: Elf REJECTED\n");
                            this.myAgent.send(resp);
                        }
                    }
                    else{
                        ACLMessage resp = msg.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                        System.out.println("Santa: Recibido mensaje inesperado\n");
                    }
                    this.finish = true;
                    break;
                
                case 1:
                    ACLMessage locationRequest = myAgent.blockingReceive();
                    if(locationRequest.getConversationId().equals(CommManager.CONV_ID_SANTA)){
                        System.out.println("Santa: Recibido un mensaje 2!!");
                        
                        // TODO: Obtener ubicación de Santa
                        String santaLocation = "";
                        
                        ACLMessage locationResponse = locationRequest.createReply();

                        locationResponse.setPerformative(ACLMessage.INFORM);
                        locationResponse.setContent(santaLocation);

                        myAgent.send(locationResponse);

                        System.out.println("Santa: Enviando ubicación: " + santaLocation);
                    }
                    else{
                        ACLMessage resp = locationRequest.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                        System.out.println("Santa: Recibido mensaje inesperado\n");
                    }
                    this.finish = true;
                    break;
                case 2:
                    ACLMessage arrivalMessage = myAgent.blockingReceive();
                    if (arrivalMessage.getConversationId().equals(CommManager.CONV_ID_SANTA)) {
                        System.out.println("Santa: Recibido un mensaje 3!!");
                        
                        ACLMessage hoHoHoResponse = arrivalMessage.createReply();
                        hoHoHoResponse.setPerformative(ACLMessage.INFORM);
                        hoHoHoResponse.setContent("HoHoHo!");

                        myAgent.send(hoHoHoResponse);

                        System.out.println("Santa: ¡HoHoHo! - Elf llegó a la ubicación.");
                    } else {
                        System.out.println("Santa: Mensaje inesperado o elfo no llegó.");
                    }
                    this.finish = true;
                    break;
                }
            }

            @Override
            public boolean done(){
                return finish; 
            }

        }
}
