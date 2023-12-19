
package launcher;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import agent.ElfAgent;

import agent.Sensor;
import components.Map;
import components.Position;
import gui.LauncherWindow;
import gui.MainWindowP2;
import jade.core.Agent;

// P3
import agent.ElfAgent;
import agent.SantaAgent;
import agent.RudolphAgent;
import agent.CommManager;

import gui.MainWindowP3;
import javax.swing.JFrame;


/**
 *
 * @author carlos
 * 
 * @brief Class that implements the main function that loads the 
 * containers and agents.
 */
public class Launcher {

    private static String host = "localhost";
    private static String mainContName = "DBA_303";
    private static String agentContName = "DBA_agent";
    private static String agentName = "explorador";
    
    private static ContainerController mainContController;
    private static Profile mainProfile = new ProfileImpl();
    private static ContainerController agentContController;
    private static Profile agentProfile = new ProfileImpl();
    
    private static jade.core.Runtime runtime = jade.core.Runtime.instance();
    
    private static Agent scoutAgent; 
    private static AgentController agentController;
    
    /// P3
    private static Agent elfAgent, santaAgent, rudolphAgent;
    private static AgentController elfController, santaController, rudolphController;
    
    /// windows
    private static LauncherWindow launcherW = new LauncherWindow();
    private static JFrame mainW;
    
    /**
     * Initialize containers profiles
     */
    private static void initProfiles(){
        // mainProfile parameters
        mainProfile.setParameter(Profile.MAIN_HOST, host);
        mainProfile.setParameter(Profile.CONTAINER_NAME, mainContName);

        // agent Profile parameters
        agentProfile.setParameter(Profile.MAIN_HOST, host);
        agentProfile.setParameter(Profile.CONTAINER_NAME, agentContName);
    }
    
    /**
     * Public method to create Scouter Agent
     * for being called from the launcher window
     */
    public static void createScoutAgent(){
        scoutAgent = new ElfAgent();
        try{
            agentController = agentContController.acceptNewAgent("scoutter", scoutAgent);
        }
        catch(StaleProxyException e){
            System.out.println(e);
        }
    }
    
    /**
     * Public metho to start the Scouter agent
     * for being called from the launcher window
     */
    public static void startScoutAgent(){
        try{
            agentController.start();
        }
        catch(StaleProxyException e){
            System.out.println(e);
        }
    }
    
    /**
     * Public method to create agents for P3
     * for being called from the launcher window
     */
    public static void createP3Agents(){
        elfAgent = new ElfAgent();
        santaAgent = new SantaAgent();
        rudolphAgent = new RudolphAgent();
        
        try{
            santaController = agentContController.acceptNewAgent(CommManager.AID_SANTA, santaAgent);
            rudolphController = agentContController.acceptNewAgent(CommManager.AID_RUDOLPH, rudolphAgent );
            elfController = agentContController.acceptNewAgent(CommManager.AID_ELF, elfAgent);
        }
        catch(StaleProxyException e){
            System.out.println(e);
        }
    }
    
    /**
     * Public metho to start the agents for the P3
     * for being called from the launcher window
     */
    public static void startP3Agents(){
        try{
            santaController.start();
            rudolphController.start();
            elfController.start();
        }
        catch(StaleProxyException e){
            System.out.println(e);
        }
    }
    
    /**
     * Method to configure the agent for the P2 launch
     * @param map
     * @param origin
     * @param target 
     */
    public static void configureAgent(Map map, Position origin, Position target){
        Sensor.getInstance().setParameters(map, origin, target);
    }
    
    /**
     * Method to open the main Window for the P2
     * @param map
     * @param mapName 
     */
    public static void openP2Window(Map map, String mapName){
        mainW = new MainWindowP2(map, mapName);
        mainW.setVisible(true);
    }
    
    public static void openP3Window(Map map){
        mainW = new MainWindowP3(map);
        mainW.setVisible(true);
    }
    
    public static JFrame getMainWindow(){ return mainW; }
    
    public static Agent getAgent(){ return scoutAgent; }
    
    /* MAIN RUN METHODS FOR EACH PR.      *************************************/
    
    /**
     * Method that intilizes the agent for the P2 and open the corresponding window
     */
    public static void runP2(){
        // Creates and launch the agent
        createScoutAgent();

        // set initial mision
        configureAgent(launcherW.selectedMap, launcherW.originPos, launcherW.targetPos);

        // call to the main window
        openP2Window(launcherW.selectedMap, launcherW.mapName);

        // start the agent
        startScoutAgent();

        // close the window
        launcherW.setVisible(false);
    }
    
    /**
     * Method that initializes agents for the P3 and open the corresponding window
     */
    public static void runP3(){
        // Create agents (first those that have to hear others)
        createP3Agents();
               
        // configure?
        // open the P3 window
        openP3Window(launcherW.selectedMap);
        
        //start agents (first those that have to hear others)
        startP3Agents();
        //close the launcher window
        launcherW.setVisible(false);
    }
    
    /**************************************************************************/
    /**
     * Main
     */
    public static void main(String[] args) throws StaleProxyException {

        // Instance of runtime
        jade.core.Runtime runtime = jade.core.Runtime.instance();

        // initialize main container and agent profiles
        initProfiles();
        
        // Create the containers
        mainContController = runtime.createMainContainer(mainProfile);
        agentContController = runtime.createAgentContainer(agentProfile);
        
        // Show the launcher window
        launcherW.setVisible(true);

        // TODO change with the UI
        // previous steps for the P3
//        Agent elfAgent = new ElfAgent();
//        Agent santaAgent = new SantaAgent();
//        try{
//            AgentController elfController = agentContController.acceptNewAgent("ELF", elfAgent);
//            AgentController santaController = agentContController.acceptNewAgent("SANTA", santaAgent);
//            santaController.start();
//            elfController.start();
//        }
//        catch(StaleProxyException e){
//            System.out.println(e);
//        }
//        Agent elfAgent = new ElfAgent();
//        Agent santaAgent = new SantaAgent();
//        Agent rudolphAgent = new RudolphAgent();
//        try{
//            AgentController elfController = agentContController.acceptNewAgent("ELF", elfAgent);
//            AgentController santaController = agentContController.acceptNewAgent("SANTA", santaAgent);
//            AgentController rudolphController = agentContController.acceptNewAgent("RUDOLPH", rudolphAgent);
//            santaController.start();
//            elfController.start();
//            rudolphController.start();
//        }
//        catch(StaleProxyException e){
//            System.out.println(e);
//        }

    }
                
}
