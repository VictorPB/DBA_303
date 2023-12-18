/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent.behaviours;

import agent.CommManager;
import agent.SantaAgent;
import components.Environment;
import components.Position;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author carlos
 */
public class SantaComunicationBeh extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final SantaAgent santaAgent;
    
    public SantaComunicationBeh(SantaAgent agent) {
        this.santaAgent = agent;
    }
         
        CommManager.SantaCommStates state = CommManager.SantaCommStates.RECEIVE_MISSION;
        boolean finish = false;
        ACLMessage lastMsg;
        
        @Override
        public void action(){
            ACLMessage resp;
            
            this.lastMsg = myAgent.blockingReceive();

            switch (state) {
                case RECEIVE_MISSION:
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
                            this.state = CommManager.SantaCommStates.RECEIVE_FOUND_OR_FINISH;
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
                
                case RECEIVE_FOUND_OR_FINISH:
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
                            this.state = CommManager.SantaCommStates.HOHOHO;
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
                case HOHOHO:
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
