
package launcher;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import agent.ScoutAgent;

import agent.Sensor;
import components.Map;
import components.Position;
import gui.LauncherWindow;
import gui.MainWindowP2;
import jade.core.Agent;

// P3
import agent.ElfAgent;
import agent.SantaAgent;

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
    
    /// windows
    private static LauncherWindow launcherW = new LauncherWindow();
    private static MainWindowP2 mainW;
    
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
        scoutAgent = new ScoutAgent();
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
    
    public static void configureAgent(Map map, Position origin, Position target){
        Sensor.getInstance().setParameters(map, origin, target);
        
    }
    
    public static void openMainWindow(Map map, String mapName){
        mainW = new MainWindowP2(map, mapName);
        mainW.setVisible(true);
    }
    
    public static MainWindowP2 getMainWindow(){ return mainW; }
    
    public static Agent getAgent(){ return scoutAgent; }
    
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
//        launcherW.setVisible(true);

        // TODO change with the UI
        // previous steps for the P3
        Agent elfAgent = new ElfAgent();
        Agent santaAgent = new SantaAgent();
        try{
            AgentController elfController = agentContController.acceptNewAgent("ELF", elfAgent);
            AgentController santaController = agentContController.acceptNewAgent("SANTA", santaAgent);
            santaController.start();
            elfController.start();
        }
        catch(StaleProxyException e){
            System.out.println(e);
        }

    }
                
}
