/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file    ElfComunicationBeh.java
 * @author  DBA_303. Carlos, Jorge, Victor
 */
package agent.behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import components.*;
import agent.CommManager;
import agent.ElfAgent;

import launcher.Launcher;

/**
 * @brief Class that do the comunication behaviour Rudolph-Elf-Santa
 * 
 */
public class ElfComunicationBeh extends Behaviour{
    
    // Private atribute to access the agent that uses the behaviour
    private final ElfAgent myAgent;

    CommManager.ElfCommStates state = CommManager.ElfCommStates.PROPOSE_SANTA_MISSION;
    boolean finish = false;
    boolean allReindeerFound = false;

    String secretCode;
    Position rudolphPos;
    String reindeerName;
    Position reindeerPos;
    Position santaPos;

    ACLMessage lastMsgSanta;
    ACLMessage lastMsgRudolph;
    
    public ElfComunicationBeh(ElfAgent agent) {
        this.myAgent = agent;
    }

    @Override
    public void action(){

        ACLMessage msg;
        ACLMessage replySanta;
        ACLMessage replyRudolph;

        System.out.println(state);
        
        if(this.myAgent.getElfState() == ElfAgent.State.COMMUNICATING){
            switch (state) {
                //SANTA

                case PROPOSE_SANTA_MISSION:

                    msg = new ACLMessage(ACLMessage.PROPOSE);
                    msg.addReceiver(new AID(CommManager.AID_SANTA,AID.ISLOCALNAME));
                    msg.setConversationId(CommManager.CONV_ID_SANTA);
                    msg.setContent("PROPOSE MISION");
                    myAgent.send(msg);

                    // Console log and UI update
                    System.out.println("ELF ---> SANTA --------------- PROPOSE mission");
                    Launcher.getMainWindow()
                            .getSantaConversation()
                            .addElfMessage("Hola Santa! Me propongo voluntario para la misión");

                    this.state = CommManager.ElfCommStates.SANTA_PROPOSAL_RESPONSE;
                    break;

                case SANTA_PROPOSAL_RESPONSE:
                    this.lastMsgSanta = myAgent.blockingReceive();
                    if(this.lastMsgSanta.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                        String santaValidationContent = this.lastMsgSanta.getContent();
                        System.out.println("ELF <--- SANTA  ---------------  ACCEPT code + rudoplhPos");
                        System.out.println("       msg: " + santaValidationContent+"\n");

                        //Decode Message Get Secret Code and Rudolf Pos
                        decodeMSG(santaValidationContent);

                        System.out.println("       code: "+this.secretCode + " ---- rudolphPos: " +this.rudolphPos.toString()+"\n\n");

                        // Update the Santa conversation with the mision list
                        Launcher.getMainWindow().getSantaConversation().addTaskListPanel();

                        //TODO: desplazarse hacia Rudolf
                        myAgent.setMission(Environment.getInstance().getRudolphRespectAgent());
                        myAgent.setElfState(ElfAgent.State.GOING_TO_RUDOLPH);
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
                    myAgent.send(msg);                  

                    // Console log and UI update
                    System.out.println("ELF ---> RUDOLPH  ---------------  PROPOSE code " + this.secretCode);
                    Launcher.getMainWindow()
                            .getRudolphConversation()
                            .addElfMessage("Hola Rudolph! Código secreto: "+this.secretCode);

                    this.state = CommManager.ElfCommStates.RUDOLPH_PROPOSAL_RESPONSE;
                    break;

                case RUDOLPH_PROPOSAL_RESPONSE:
                    this.lastMsgRudolph = myAgent.blockingReceive();
                    if(this.lastMsgRudolph.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                        System.out.println("ELF <--- RUDOLPH  ---------------  ACCEPT");
                        this.state = CommManager.ElfCommStates.REQUEST_REINDEER; 
                    }
                    else{
                        System.out.println("ELF <--- RUDOLPH  ---------------  REJECT");
                        this.finish = true;
                    }                               
                    break;

                case REQUEST_REINDEER:
                    replyRudolph = this.lastMsgRudolph.createReply(ACLMessage.REQUEST);
                    replyRudolph.setContent("Next Reindeer");
                    this.myAgent.send(replyRudolph);

                    System.out.println("ELF ---> RUDOLPH  ---------------  REQUEST nextReindeer");
                    Launcher.getMainWindow()
                            .getRudolphConversation()
                            .addElfMessage("Dame más coordenadas");

                    this.state = CommManager.ElfCommStates.RECEIVE_REINDEER_OR_FINISH;
                    break;

                case RECEIVE_REINDEER_OR_FINISH:
                    this.lastMsgRudolph = myAgent.blockingReceive();
                    if(this.lastMsgRudolph.getPerformative() == ACLMessage.INFORM){
                        String reindeerPosContent = this.lastMsgRudolph.getContent();


                        //Decode Message Get first lost reindeer
                        decodeMSG(reindeerPosContent);

                        if(!this.allReindeerFound){

                            System.out.println("ELF <--- RUDOLPH  ---------------  "+this.reindeerName+" "+this.reindeerPos.toString());

                            myAgent.setMission(Environment.getInstance().getRudolphRespectAgent());
                            myAgent.setElfState(ElfAgent.State.GOING_TO_LOST_REINDEER);
                            
                            this.state = CommManager.ElfCommStates.INFORM_FOUND_REINDEER;

                        }else{
                            System.out.println("ELF <--- RUDOLPH  ---------------  INFORM FINISH");
                            this.state = CommManager.ElfCommStates.REQUEST_SANTA_POS;
                        }
                    }else{
                        System.out.println("Elf: Rudolf no mando un INFORM.\n");
                        this.finish = true;
                    }
                    break;

                case INFORM_FOUND_REINDEER:         //Informamos reno encontrado y pedimos siguiente reno
                    // Inform to rudolph of the found reindeer
                    replyRudolph = this.lastMsgRudolph.createReply(ACLMessage.INFORM);
                    replyRudolph.setContent(this.reindeerName);
                    this.myAgent.send(replyRudolph);

                    // Inform to santa of the found reindeer
                    replySanta = this.lastMsgSanta.createReply(ACLMessage.INFORM);
                    replySanta.setContent(this.reindeerName);
                    System.out.println(this.reindeerName);
                    this.myAgent.send(replySanta);

                    System.out.println("ELF ---> SANTA  ---------------  INFORM found reindeer " + this.reindeerName);
                    Launcher.getMainWindow()
                            .getRudolphConversation()
                            .addElfMessage("Encontrado "+this.reindeerName);
                    System.out.println("ELF ---> RUDOLPH  ---------------  INFORM found reindeer " + this.reindeerName);
                    
                    myAgent.setMission(Environment.getInstance().getRudolphRespectAgent());
                    myAgent.setElfState(ElfAgent.State.GOING_TO_RUDOLPH);

                    this.state = CommManager.ElfCommStates.REQUEST_REINDEER;
                    break;

                case REQUEST_SANTA_POS:         //Request Santa position
                    replySanta = this.lastMsgSanta.createReply(ACLMessage.REQUEST);
                    this.myAgent.send(replySanta);

                    System.out.println("ELF ---> SANTA --------------- REQUEST SantaPos");
                    Launcher.getMainWindow()
                            .getSantaConversation()
                            .addElfMessage("Santa, dime tu posición");

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

                        //Moving to Santa
                        myAgent.setMission(Environment.getInstance().getSantaRespectAgent());
                        myAgent.setElfState(ElfAgent.State.GOING_TO_SANTA);

                    }else{
                        System.out.println("Elf: Mi pana el santa no me mando bien la ubi\n");
                        this.finish = true;
                    }
                    this.state = CommManager.ElfCommStates.INFORM_SANTA_REACHED;
                    break;

                case INFORM_SANTA_REACHED:
                    replySanta = this.lastMsgSanta.createReply(ACLMessage.INFORM);                
                    this.myAgent.send(replySanta);

                    System.out.println("ELF ---> SANTA --------------- INFORM");
                    Launcher.getMainWindow()
                            .getSantaConversation()
                            .addElfMessage("Ya estoy aquí. Misión Completada!");

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
        System.out.println("ElfDecodeMSG: " + encodedMessage);
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