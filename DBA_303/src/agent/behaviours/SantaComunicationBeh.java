/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file    SantaComunicationBeh.java
 * @author  DBA_303. Maria, Victor
 */
package agent.behaviours;

import agent.CommManager;
import agent.SantaAgent;
import components.Environment;
import components.Position;
import components.Reindeer;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import launcher.Launcher;

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
         
        CommManager.SantaCommStates state = CommManager.SantaCommStates.WAIT_FOR_PROPOSAL;
        boolean finish = false;
        ACLMessage lastMsg;
        
        @Override
        public void action(){
            ACLMessage resp;
            
            this.lastMsg = myAgent.blockingReceive();

            switch (state) {
                case WAIT_FOR_PROPOSAL:
                    if(this.lastMsg.getConversationId().equals(CommManager.CONV_ID_SANTA)){
                        System.out.println("SANTA <--- ELF  --------------- PROPOSE mission");
                        if(this.santaAgent.radomElfAprove()){
                            Launcher.getMainWindow()
                                    .getSantaConversation()
                                    .addReceiverMsg("Eres un elfo válido");
                            Launcher.getMainWindow()
                                    .getSantaConversation()
                                    .addReceiverMsg("Este es el código: "+CommManager.CONV_ID_RUDOLPH);
                            System.out.println("SANTA ---> ELF --------------- ACCEPT code + rudolphPos\n");
                            
                            resp = this.lastMsg.createReply(ACLMessage.ACCEPT_PROPOSAL);                          
                            resp.setContent("SecretCode" + 
                                            CommManager.SEPARATOR +
                                            CommManager.CONV_ID_RUDOLPH + 
                                            CommManager.SEPARATOR +
                                            Environment.getInstance().getRudolphPosition().toString(CommManager.SEPARATOR));
                            myAgent.send(resp);
                                                        
                            this.state = CommManager.SantaCommStates.RECEIVE_FOUND_OR_FINISH;
                        }
                        else{
                            Launcher.getMainWindow()
                                    .getSantaConversation()
                                    .addReceiverMsg("No creo que valgas para esto...");
                            System.out.println("SANTA ---> ELF --------------- REJECT\n");
                            
                            resp = this.lastMsg.createReply(ACLMessage.REJECT_PROPOSAL);
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
                            // content = reinderName
                            String reinderName = this.lastMsg.getContent();
                            System.out.println("SANTA <--- ELF  --------------- INFORM found "+reinderName);
                            Launcher.getMainWindow()
                                    .getSantaConversation()
                                    .addCheckFoundReindeer(Reindeer.Name.fromName(reinderName));
                        }
                        else if (this.lastMsg.getPerformative() == ACLMessage.REQUEST) {
                            System.out.println("SANTA <--- ELF  --------------- REQUEST SantaPos");

                            Position santaPos = Environment.getInstance().getSantaPosition();
                            
                            Launcher.getMainWindow()
                                    .getSantaConversation()
                                    .addReceiverMsg("Estoy en "+santaPos);
                            System.out.println("SANTA ---> ELF --------------- INFORM SantaPos\n");

                            resp = this.lastMsg.createReply();
                            resp.setPerformative(ACLMessage.INFORM);
                            resp.setContent("SANTA" + CommManager.SEPARATOR 
                                            + " POS " + CommManager.SEPARATOR 
                                            + santaPos.toString(CommManager.SEPARATOR));

                            myAgent.send(resp);
                            
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
                        System.out.println("Santa: ¡HoHoHo! - Elf llegó a la ubicación.");
                        Launcher.getMainWindow()
                                .getSantaConversation()
                                .addReceiverMsg("Gracias, HoHoHo!!!");
                        
                        resp = this.lastMsg.createReply();
                        resp.setPerformative(ACLMessage.INFORM);
                        resp.setContent("HoHoHo!");
                        myAgent.send(resp);
                        
                    } else {
                        System.out.println("Santa: Mensaje inesperado o elfo no llegó.");
                    }
                    this.finish = true;
                    break;
                }
                
                // Force delay
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }

            @Override
            public boolean done(){
                return finish; 
            }

        }
