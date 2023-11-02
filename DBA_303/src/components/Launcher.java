
package components;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/**
 *
 * @author carlos
 */
public class Launcher {
    public static void main(String[] args) throws StaleProxyException {
        String host = "localhost";
        String mainContName = "DBA_303";
        String agentContName = "DBA_agent";
        String agentName = "explorador";

        // Instance of runtime
        jade.core.Runtime runtime = jade.core.Runtime.instance();

        // main Container Profile
        Profile mainProfile = new ProfileImpl();

        // mainProfile parameters
        // Includes: 
        //      * mainProfile host
        //      * mainContainer name
        mainProfile.setParameter(Profile.MAIN_HOST, host);
        mainProfile.setParameter(Profile.CONTAINER_NAME, mainContName);
        
        
        // agent Profile
        Profile agentProfile = new ProfileImpl();

        // agent Profile parameters
        // Includes:
        //      * agent Profile host
        //      * agentContainer name
        agentProfile.setParameter(Profile.MAIN_HOST, host);
        agentProfile.setParameter(Profile.CONTAINER_NAME, agentContName);
        
        // main Container controller
        ContainerController mainContController = runtime.createMainContainer(mainProfile);
        
        // agent container controller
        ContainerController agentContController = runtime.createAgentContainer(agentProfile);
        
        // Creation of agent container controller
        AgentController agentController = agentContController.createNewAgent(agentName,
        /*BasicHelloWorld.class.getCanonicalName()*/, null);

        // Start of agent
        agentController.start();
    }
}
