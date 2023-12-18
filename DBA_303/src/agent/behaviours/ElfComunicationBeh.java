/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent.behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import components.*;
import agent.CommManager;

/**
 *
 * @author carlos
 */
public class ElfComunicationBeh extends Behaviour{
         
        CommManager.ElfCommStates state = CommManager.ElfCommStates.INIT_MISSION;
        boolean finish = false;
        boolean allReindeerFound = false;
        
        String secretCode;
        Position rudolphPos;
        String reindeerName;
        Position reindeerPos;
        Position santaPos;
        
        ACLMessage lastMsgSanta;
        ACLMessage lastMsgRudolph;
        
        @Override
        public void action(){
            
            ACLMessage msg;
            ACLMessage replySanta;
            ACLMessage replyRudolph;
            
            switch (state) {
                //SANTA
                
                case INIT_MISSION:
                    System.out.println("ELF ---> SANTA --------------- PROPOSE mission");
                    msg = new ACLMessage(ACLMessage.PROPOSE);
                    msg.addReceiver(new AID(CommManager.AID_SANTA,AID.ISLOCALNAME));
                    msg.setConversationId(CommManager.CONV_ID_SANTA);
                    msg.setContent("Elf: Me propongo voluntario para buscar los renos perdidos");
                    myAgent.send(msg);
                    
                    this.state = CommManager.ElfCommStates.RECEIVE_ACCEPT_SANTA;
                    break;
                
                case RECEIVE_ACCEPT_SANTA:
                    this.lastMsgSanta = myAgent.blockingReceive();
                    if(this.lastMsgSanta.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                        String santaValidationContent = this.lastMsgSanta.getContent();
                        System.out.println("ELF <--- SANTA  ---------------  ACCEPT code + rudoplhPos");
                        System.out.println("       msg: " + santaValidationContent+"\n");
                                             
                        //Decode Message Get Secret Code and Rudolf Pos
                        decodeMSG(santaValidationContent);
                        
                        System.out.println("       code: "+this.secretCode + " ---- rudolphPos: " +this.rudolphPos.toString()+"\n\n");
                        
                        //TODO: desplazarse hacia Rudolf
                    }
                    else{
                        System.out.println("ELF <--- SANTA  ---------------  REJECT");
                        this.finish = true;
                    }
                    
                    this.state = CommManager.ElfCommStates.PROPOSE_RUDOLPH;
                    break;
                    
                //RUDOLF
                    
                case PROPOSE_RUDOLPH:
                    msg = new ACLMessage(ACLMessage.PROPOSE);
                    msg.addReceiver(new AID(CommManager.AID_RUDOLPH,AID.ISLOCALNAME));
                    msg.setConversationId(secretCode);
                    msg.setContent("Elf: Hola Rudolf, este es el codigo secreto:"+this.secretCode);               
                    System.out.println("ELF ---> RUDOLPH  ---------------  PROPOSE code " + this.secretCode);
                    myAgent.send(msg);                  
                    
                    this.state = CommManager.ElfCommStates.RECEIVE_ACCEPT_RUDOLPH;
                    break;
                    
                case RECEIVE_ACCEPT_RUDOLPH:
                    this.lastMsgRudolph = myAgent.blockingReceive();
                    if(this.lastMsgRudolph.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                        String reindeerPosContent = this.lastMsgRudolph.getContent();
                        System.out.println("ELF <--- RUDOLPH  ---------------  ACCEPT ReindeerName + ReindeerPos");
                        
                        //Decode Message Get first lost reindeer
                        decodeMSG(reindeerPosContent);
                        
                        System.out.println("       ReindeerName: "+this.reindeerName + " ---- ReindeerPos: " +this.reindeerPos.toString()+"\n\n");
                        
                        //TODO: desplazarse hacia primer reno perdido
                        
                        //TODO: volver a Rudolf
                    }
                    else{
                        System.out.println("ELF <--- RUDOLPH  ---------------  REJECT");
                        
                        this.finish = true;
                    }
                    
                    this.state = CommManager.ElfCommStates.FOUND_AND_NEXT_REINDEER;                 
                    break;
                    
                case FOUND_AND_NEXT_REINDEER:         //Informamos reno encontrado y pedimos siguiente reno
                    // Inform to rudolph of the found reindeer
                    replyRudolph = this.lastMsgRudolph.createReply(ACLMessage.INFORM);
                    replyRudolph.setContent(this.reindeerName);
                    System.out.println("ELF ---> RUDOLPH  ---------------  INFORM found reindeer " + this.reindeerName);

                    this.myAgent.send(replyRudolph);
                    
                    // Inform to santa of the found reindeer
                    replySanta = this.lastMsgSanta.createReply(ACLMessage.INFORM);
                    replySanta.setContent(this.reindeerName);
                    System.out.println("ELF ---> SANTA  ---------------  INFORM found reindeer " + this.reindeerName);

                    this.myAgent.send(replySanta);
                    
                    replyRudolph = this.lastMsgRudolph.createReply(ACLMessage.REQUEST);
                    System.out.println("ELF ---> RUDOLPH  ---------------  REQUEST nextReindeer");
                    this.myAgent.send(replyRudolph);
                    
                    this.state = CommManager.ElfCommStates.RECEIVE_NEW_REINDEER_POS;
                    break;
                    
                case RECEIVE_NEW_REINDEER_POS:         //Obtenemos coordenadas
                    this.lastMsgRudolph = myAgent.blockingReceive();
                    
                    if(this.lastMsgRudolph.getPerformative() == ACLMessage.INFORM){
                        String reindeerPosContent = this.lastMsgRudolph.getContent();
                        System.out.println("ELF <--- RUDOLPH  ---------------  INFORM ReindeerName + ReindeerPos");
                        
                        //Decode Message Get first lost reindeer
                        decodeMSG(reindeerPosContent);
                        
                        if(!this.allReindeerFound){
                            System.out.println("       ReindeerName: "+this.reindeerName + " ---- ReindeerPos: " +this.reindeerPos.toString()+"\n\n");

                            //TODO: desplazarse hacia siguiente reno perdido
                            
                            //TODO: volver a Rudolf
                            
                            this.state = CommManager.ElfCommStates.FOUND_AND_NEXT_REINDEER;
                        }else{
                            System.out.println("ELF <--- RUDOLPH  ---------------  INFORM FINISH");
                            this.state = CommManager.ElfCommStates.REQUEST_SANTA_POS;
                        }
                    }else{
                        System.out.println("Elf: Rudolf no mando un INFORM.\n");
                        
                        this.finish = true;
                    }
                    
                    break;
                    
                case REQUEST_SANTA_POS:         //Request Santa position
                    replySanta = this.lastMsgSanta.createReply(ACLMessage.REQUEST);
                    System.out.println("ELF ---> SANTA --------------- REQUEST SantaPos");
                    this.myAgent.send(replySanta);
                    
                    this.state = CommManager.ElfCommStates.RECEIVE_SANTA_POS;
                    break;
                    
                case RECEIVE_SANTA_POS:
                    this.lastMsgSanta = myAgent.blockingReceive();
                    if(this.lastMsgSanta.getPerformative() == ACLMessage.INFORM){
                        String santaLocationContent = this.lastMsgSanta.getContent();
                        System.out.println("ELF <--- SANTA  ---------------  INFORM SantaPos");
                                             
                        //Decode Message Get Santa Pos
                        decodeMSG(santaLocationContent);
                        
                            System.out.println("       SantaPos: "+this.santaPos.toString()+"\n\n");
                        
                        //TODO: desplazarse hacia Santa
                                               
                    }else{
                        System.out.println("Elf: Mi pana el santa no me mando bien la ubi\n");
                        this.finish = true;
                    }
                    this.state = CommManager.ElfCommStates.INFORM_SANTA_REACHED;
                    break;
                    
                case INFORM_SANTA_REACHED:
                    replySanta = this.lastMsgSanta.createReply(ACLMessage.INFORM);
                    System.out.println("Elf: Hola santa, ya estoy aqui. Mision Completada");
                    System.out.println("ELF ---> SANTA --------------- INFORM");
                    this.myAgent.send(replySanta);
                    
                    this.state = CommManager.ElfCommStates.RECEIVE_SANTA_CONGRATS;
                    break; 
                    
                case RECEIVE_SANTA_CONGRATS:
                    this.lastMsgSanta = myAgent.blockingReceive();
                    if(this.lastMsgSanta.getPerformative() == ACLMessage.INFORM){
                        String santaThanksContent = this.lastMsgSanta.getContent();
                        System.out.println("Elf: Santa me agradecio la labor.");
                        this.finish = true;
                    }else{
                        System.out.println("Elf: Santa manda performativa final mal\n");
                        this.finish = true;
                    }
            }
        }
        
        @Override
        public boolean done(){
            return finish;
        }
        
        
        /**
         * Decodes messages containing a string and a position and stores the 
         * information in the output parameters.
         * 
         * Used to obtain Rudolf's secret code and position. 
         * Used to obtain name and position of lost reindeer.
         * 
         * The message encoding is preset and depends on the programmer,
         * using SEPARATOR to delimit the fields in the encodedMessage.
         * 
         * Example encode message: "Rudolf;xAeJtxC;12;26"  "PENDING ;BLITZEN;28;17"
         * 
         * @param encodedMessage
         * @param outputString
         * @param outputPosition 
         */
        void decodeMSG(String encodedMessage){
            
            String[] parts = encodedMessage.split(CommManager.SEPARATOR);
            
            if("SecretCode".equals(parts[0])){
                this.secretCode = parts[1];
                this.rudolphPos = new Position(Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
            }else if("PENDING".equals(parts[0])){
                reindeerName = parts[1];
                reindeerPos = new Position(Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
            }else if("FINISH".equals(parts[0])){
                allReindeerFound = true;
            }else if("SANTA".equals(parts[0])){
                santaPos = new Position(Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));     
            }else {
                System.err.println("----- ERROR EN DECODICIFACION DE MENSAJE -----");
            }
            
        }
        
    }