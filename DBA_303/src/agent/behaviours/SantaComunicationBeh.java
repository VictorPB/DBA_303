/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file          SantaComunicationBeh.java
 * @author    DBA_303. Maria, Victor
 */
package agent.behaviours;

import agent.CommManager;
import agent.SantaAgent;
import components.Environment;
import components.Position;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

/**
 * @brief Class that do the comunication behaviour Santa-Elf
 * 
 */
public class SantaComunicationBeh extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final SantaAgent santaAgent;
    
    public SantaComunicationBeh(SantaAgent agent) {
        this.santaAgent = agent;
    }
         
        int state = 0;
        boolean finish = false;
        ACLMessage lastMsg;
        
        @Override
        public void action(){
            ACLMessage resp;
            
            this.lastMsg = myAgent.blockingReceive();

            switch (state) {
                case 0:
                    if(this.lastMsg.getConversationId().equals(CommManager.CONV_ID_SANTA)){
                        System.out.println("SANTA <--- ELF  --------------- PROPOSE mission");
                        if(this.santaAgent.radomElfAprove()){
                            resp = this.lastMsg.createReply(ACLMessage.ACCEPT_PROPOSAL);                          
                            resp.setContent("SecretCode" + 
                                            CommManager.SEPARATOR +
                                            CommManager.CONV_ID_RUDOLPH + 
                                            CommManager.SEPARATOR +
                                            Environment.getInstance().getRudolphPosition().toString(CommManager.SEPARATOR));
                            myAgent.send(resp);
                            System.out.println("SANTA ---> ELF --------------- ACCEPT code + rudolphPos\n");
                            this.state = 1;
                        }
                        else{
                            resp = this.lastMsg.createReply(ACLMessage.REJECT_PROPOSAL);
                            System.out.println("SANTA ---> ELF --------------- REJECT\n");
                            this.myAgent.send(resp);
                            this.finish = true;
                        }
                    }
                    else{
                        resp = this.lastMsg.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                        System.out.println("Santa: Recibido mensaje inesperado\n");
                        this.finish = true;
                    }
                    break;
                
                case 1:
                    if(this.lastMsg.getConversationId().equals(CommManager.CONV_ID_SANTA)){
                        if (this.lastMsg.getPerformative() == ACLMessage.INFORM) {
                            System.out.println("SANTA <--- ELF  --------------- INFORM found");
                            
                            // TODO: Añadir interfaz
                        }
                        else if (this.lastMsg.getPerformative() == ACLMessage.REQUEST) {
                            System.out.println("SANTA <--- ELF  --------------- REQUEST SantaPos");

                            // TODO: Obtener ubicación de Santa
                            Position santaPos = Environment.getInstance().getSantaPosition();

                            ACLMessage posResponse = this.lastMsg.createReply();

                            posResponse.setPerformative(ACLMessage.INFORM);
                            posResponse.setContent("SANTA" + CommManager.SEPARATOR 
                                            + " POS " + CommManager.SEPARATOR 
                                            + santaPos.toString(CommManager.SEPARATOR));

                            myAgent.send(posResponse);

                            System.out.println("SANTA ---> ELF --------------- INFORM SantaPos\n");
                            this.state = 2;
                        }
                        else{
                            resp = this.lastMsg.createReply(ACLMessage.UNKNOWN);
                            this.myAgent.send(resp);
                            System.out.println("SANTA ---> ELF --------------- UNKNOWN\n");
                            this.finish = true;
                        }
                    }
                    else{
                        resp = this.lastMsg.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                            System.out.println("SANTA ---> ELF --------------- UNKNOWN\n");
                        this.finish = true;
                    }
                    break;
                case 2:
                    if (this.lastMsg.getConversationId().equals(CommManager.CONV_ID_SANTA)) {
                        System.out.println("SANTA <--- ELF  --------------- INFORM final");
                        
                        ACLMessage hoHoHoResponse = this.lastMsg.createReply();
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
