/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file    Launcher.java
 * @author  DBA_303. JorgeBg / Carlos
 */
package launcher;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import agent.Sensor;
import components.Map;
import components.Position;
import gui.LauncherWindow;
import jade.core.Agent;

// P3
import agent.ElfAgent;
import agent.SantaAgent;
import agent.RudolphAgent;
import agent.CommManager;
import components.Environment;

import gui.MainWindow;


/**
 * @brief   Class that implements the main function that loads the containers
 *          and agents, such as opens the launcher window.
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
    private static MainWindow mainW;
    
    
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
     * Method that open and start the main Window
     * @param map 
     */
    public static void openMainWindow(Map map){
        mainW = new MainWindow(map);
        mainW.setVisible(true);
    }

    
    /**
     * Method that initializes agents for the P3 and open the corresponding window
     */
    public static void runP3(){
        // Create agents (first those that have to hear others)
        createP3Agents();
        
        // Set the environment and open the mainWindow
        Environment.getInstance().setParameters(launcherW.selectedMap);
        openMainWindow(launcherW.selectedMap);
        
        //start agents (first those that have to hear others)
        startP3Agents();
        //close the launcher window
        launcherW.setVisible(false);
    }
    
    /**
     * Getter for the Main Window to be updated from the differents behaviours
     */
    static public MainWindow getMainWindow() { return mainW; }    
    
    
    /**************************************************************************/
    /**
     * Main Function. Entry point of the application
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
    }
}
