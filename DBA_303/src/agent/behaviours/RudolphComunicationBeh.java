/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file    RudolphComunicationBeh.java
 * @author  DBA_303. Carlos
 */
package agent.behaviours;

import agent.CommManager;
import agent.RudolphAgent;
import components.Environment;
import components.Reindeer;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import launcher.Launcher;

/**
 * @brief Class that do the comunication behaviour Rudolph-Elf
 * 
 */
public class RudolphComunicationBeh extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final RudolphAgent rudolphAgent;
    
    public RudolphComunicationBeh(RudolphAgent agent) {
        this.rudolphAgent = agent;
    }
         
        CommManager.RudolphCommStates state = CommManager.RudolphCommStates.WAIT_FOR_PROPOSAL;
        boolean finish = false;
        ACLMessage lastMsg;
        
        @Override
        public void action(){
            ACLMessage resp;
            
            switch (state) {
                case WAIT_FOR_PROPOSAL:
                    this.lastMsg = myAgent.blockingReceive();

                    if(this.lastMsg.getPerformative() == ACLMessage.PROPOSE){
                        System.out.println("RUDOLPH <--- ELF  ---------------  PROPOSE");
                        if(this.lastMsg.getConversationId().equals(CommManager.CONV_ID_RUDOLPH)){
                            resp = this.lastMsg.createReply(ACLMessage.ACCEPT_PROPOSAL);
                            this.myAgent.send(resp);
                            Launcher.getMainWindow()
                                    .getRudolphConversation()
                                    .addReceiverMsg("Codigo secreto Correcto!");
                            System.out.println("RUDOLPH ---> ELF --------------- ACCEPT ReindeerName + ReindeerPos\n");
                            state = CommManager.RudolphCommStates.WAIT_REQUESTS_OR_INFORMS;
                        }
                        else{
                            resp = this.lastMsg.createReply(ACLMessage.REJECT_PROPOSAL);
                            System.out.println("RUDOLPH ---> ELF --------------- REJECT\n");
                            this.myAgent.send(resp);
                            Launcher.getMainWindow()
                                    .getRudolphConversation()
                                    .addReceiverMsg("Codigo INCORRECTO!");
                            finish = true;
                        }
                    }else{
                        resp = this.lastMsg.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                            System.out.println("RUDOLPH ---> ELF --------------- UNKNOWN\n");
                        finish = true;
                    }
                    break;
                    
                case WAIT_REQUESTS_OR_INFORMS:
                    this.lastMsg = myAgent.blockingReceive();
                    if(this.lastMsg.getPerformative() == ACLMessage.REQUEST) {
                        System.out.println("RUDOLPH <--- ELF  ---------------  REQUEST nextReindeer");
                        if (Environment.getInstance().getNumberReindeers() > 0){
                            resp = this.lastMsg.createReply(ACLMessage.INFORM);
                            // Añadir a la respuesta un reno
                            Reindeer next = this.rudolphAgent.getNextReindeer();
                            resp.setContent("PENDING" + CommManager.SEPARATOR + 
                                            next.getName().name() + CommManager.SEPARATOR +              // TODO: getName devuelve numero del ENUM- pasar a String
                                            next.getPosition().toString(CommManager.SEPARATOR));
                            myAgent.send(resp);
                            System.out.println("RUDOLPH ---> ELF --------------- INFORM ReindeerName + ReindeerPos\n");
                            Launcher.getMainWindow()
                                    .getRudolphConversation()
                                    .addReceiverMsg(next.getName().toString() + " " + next.getPosition());
                        }
                        else {
                            resp = this.lastMsg.createReply(ACLMessage.INFORM);
                            // Añadir a la respuesta un reno
                            resp.setContent("FINISH");
                            myAgent.send(resp);
                            System.out.println("RUDOLPH ---> ELF --------------- INFORM FINISH\n");
                            Launcher.getMainWindow()
                                    .getRudolphConversation()
                                    .addReceiverMsg("Has terminado!");
                            finish = true;
                        }
                    }
                    else if (this.lastMsg.getPerformative() == ACLMessage.INFORM) {
                        System.out.println("RUDOLPH <--- ELF  ---------------  INFORM found");
                        Reindeer.Name reindeer = Reindeer.Name.fromName(this.lastMsg.getContent());
                        this.rudolphAgent.foundReindeer(reindeer);

                    }
                    else {
                        resp = this.lastMsg.createReply(ACLMessage.UNKNOWN);
                        this.myAgent.send(resp);
                        System.out.println("RUDOLPH ---> ELF --------------- UNKNOWN\n");
                        finish = true;
                    }
                    break;
            }
                    try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        }
        
        @Override
        public boolean done(){
            return finish; 
        }
        
    }
