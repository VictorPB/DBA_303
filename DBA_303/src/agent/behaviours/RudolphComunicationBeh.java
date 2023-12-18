/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent.behaviours;

import agent.CommManager;
import agent.RudolphAgent;
import components.Environment;
import components.Reindeer;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author carlos
 */
public class RudolphComunicationBeh extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final RudolphAgent rudolphAgent;
    
    public RudolphComunicationBeh(RudolphAgent agent) {
        this.rudolphAgent = agent;
    }
         
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
                            Reindeer ini = this.rudolphAgent.getNextReindeer();
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
                        if (Environment.getInstance().getNumberReindeers() > 0){
                            System.out.println("RUDOLPH ---> ELF --------------- INFORM ReindeerName + ReindeerPos\n");
                            
                            resp = this.lastMsg.createReply(ACLMessage.INFORM);
                            // Añadir a la respuesta un reno
                            Reindeer next = this.rudolphAgent.getNextReindeer();
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
                        this.rudolphAgent.foundReindeer(reindeer.getName());

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
