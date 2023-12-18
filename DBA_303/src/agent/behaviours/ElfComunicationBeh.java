/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agent.behaviours;


import agent.ElfAgent;
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
        
        // Private atribute to access the agent that uses the behaviour
        private final ElfAgent myAgent;
        
        int state;
        boolean finish;
        boolean allReindeerFound;
        
        String secretCode;
        Position rudolphPos;
        String reindeerName;
        Position reindeerPos;
        Position santaPos;
        
        ACLMessage lastMsgSanta;
        ACLMessage lastMsgRudolph;
        
        public ElfComunicationBeh(ElfAgent agent) {
            this.myAgent = agent;
            
            state = 0;
            finish = false;
            allReindeerFound = false;
        }
        
        @Override
        public void action(){
            
            ACLMessage msg;
            ACLMessage replySanta;
            ACLMessage replyRudolph;
            
            //If is moving, communication does nothing
            if(!myAgent.getIsMoving()){         
                switch (state) {
                    //SANTA

                    case 0:         //Greeting Santa
                        System.out.println("ELF ---> SANTA --------------- PROPOSE mission");
                        msg = new ACLMessage(ACLMessage.PROPOSE);
                        msg.addReceiver(new AID(CommManager.AID_SANTA,AID.ISLOCALNAME));
                        msg.setConversationId(CommManager.CONV_ID_SANTA);
                        msg.setContent("Elf: Me propongo voluntario para buscar los renos perdidos");
                        myAgent.send(msg);

                        this.state = 1;
                        break;

                    case 1:         //Get Secret code an Rudolph Pos
                        this.lastMsgSanta = myAgent.blockingReceive();
                        if(this.lastMsgSanta.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                            String santaValidationContent = this.lastMsgSanta.getContent();
                            System.out.println("ELF <--- SANTA  ---------------  ACCEPT code + rudoplhPos");
                            System.out.println("       msg: " + santaValidationContent+"\n");

                            //Decode Message Get Secret Code and Rudolf Pos
                            decodeMSG(santaValidationContent);

                            System.out.println("       code: "+this.secretCode + " ---- rudolphPos: " +this.rudolphPos.toString()+"\n\n");

                            //TODO: desplazarse hacia Rudolf
                            myAgent.setMission(rudolphPos);                       
                        }
                        else{
                            System.out.println("ELF <--- SANTA  ---------------  REJECT");
                            this.finish = true;
                        }

                        this.state = 2;
                        break;

                    //RUDOLF

                    case 2:         //Propose Rudolph to find lost reindeer
                        msg = new ACLMessage(ACLMessage.PROPOSE);
                        msg.addReceiver(new AID(CommManager.AID_RUDOLPH,AID.ISLOCALNAME));
                        msg.setConversationId(secretCode);
                        msg.setContent("Elf: Hola Rudolf, este es el codigo secreto:"+this.secretCode);               
                        System.out.println("ELF ---> RUDOLPH  ---------------  PROPOSE code " + this.secretCode);
                        myAgent.send(msg);                  

                        this.state = 3;
                        break;

                    case 3:         //Get First Reindeer Name and Pos
                        this.lastMsgRudolph = myAgent.blockingReceive();
                        if(this.lastMsgRudolph.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                            String reindeerPosContent = this.lastMsgRudolph.getContent();
                            System.out.println("ELF <--- RUDOLPH  ---------------  ACCEPT ReindeerName + ReindeerPos");

                            //Decode Message Get first lost reindeer
                            decodeMSG(reindeerPosContent);

                            System.out.println("       ReindeerName: "+this.reindeerName + " ---- ReindeerPos: " +this.reindeerPos.toString()+"\n\n");

                            //TODO: desplazarse hacia primer reno perdido
                            myAgent.setMission(reindeerPos);


                        }
                        else{
                            System.out.println("ELF <--- RUDOLPH  ---------------  REJECT");

                            this.finish = true;
                        }

                        //Back to Rudolph
                        this.state = 4;                 
                        break;

                    case 4:         //Back to Rudolf
                        myAgent.setMission(rudolphPos);


                        break;

                    case 5:         //Report reindeer found to Rudolph and Santa and ask for next reindeer to Rudolph
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

                        this.state = 6;
                        break;

                    case 6:         //Get next Reindeer Name and Pos
                        this.lastMsgRudolph = myAgent.blockingReceive();

                        if(this.lastMsgRudolph.getPerformative() == ACLMessage.INFORM){
                            String reindeerPosContent = this.lastMsgRudolph.getContent();
                            System.out.println("ELF <--- RUDOLPH  ---------------  INFORM ReindeerName + ReindeerPos");

                            //Decode Message Get first lost reindeer
                            decodeMSG(reindeerPosContent);

                            if(!this.allReindeerFound){
                                System.out.println("       ReindeerName: "+this.reindeerName + " ---- ReindeerPos: " +this.reindeerPos.toString()+"\n\n");

                                //Find lost reindeer
                                myAgent.setMission(reindeerPos);


                                //next state is back to Rudolph    
                                this.state = 4;
                            }else{
                                System.out.println("ELF <--- RUDOLPH  ---------------  INFORM FINISH");
                                this.state = 7;
                            }
                        }else{
                            System.out.println("Elf: Rudolf no mando un INFORM.\n");

                            this.finish = true;
                        }

                        break;

                    case 7:         //Request Santa position
                        replySanta = this.lastMsgSanta.createReply(ACLMessage.REQUEST);
                        System.out.println("ELF ---> SANTA --------------- REQUEST SantaPos");
                        this.myAgent.send(replySanta);

                        this.state = 7;
                        break;

                    case 8:
                        this.lastMsgSanta = myAgent.blockingReceive();
                        if(this.lastMsgSanta.getPerformative() == ACLMessage.INFORM){
                            String santaLocationContent = this.lastMsgSanta.getContent();
                            System.out.println("ELF <--- SANTA  ---------------  INFORM SantaPos");

                            //Decode Message Get Santa Pos
                            decodeMSG(santaLocationContent);

                                System.out.println("       SantaPos: "+this.santaPos.toString()+"\n\n");

                            //TODO: desplazarse hacia Santa
                            myAgent.setMission(santaPos);


                        }else{
                            System.out.println("Elf: Mi pana el santa no me mando bien la ubi\n");
                            this.finish = true;
                        }
                        this.state = 9;
                        break;

                    case 9:
                        replySanta = this.lastMsgSanta.createReply(ACLMessage.INFORM);
                        System.out.println("Elf: Hola santa, ya estoy aqui. Mision Completada");
                        System.out.println("ELF ---> SANTA --------------- INFORM");
                        this.myAgent.send(replySanta);

                        this.state = 9;
                        break; 

                    case 10:
                        this.lastMsgSanta = myAgent.blockingReceive();
                        if(this.lastMsgSanta.getPerformative() == ACLMessage.INFORM){
                            String santaThanksContent = this.lastMsgSanta.getContent();
                            System.out.println("Elf: Santa me agradecio la labor.");
                            this.finish = true;
                        }else{
                            System.out.println("Elf: Santa manda performativa final mal\n");
                            this.finish = true;
                        }
                        break;
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