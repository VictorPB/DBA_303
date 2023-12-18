/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file    CommManager.java
 * @author  DBA_303. Jorge, Maria, Victor, Carlos
 */

package agent;

import java.util.Random;

/**
 *  @brief Class to manage the agent AIDs and states related with communication.
 * It also generates the secret code used by the elf agent to talk to rudolph
 * 
 */
public class CommManager {
    public static final String AID_ELF = "ELF";
    public static final String AID_SANTA = "SANTA";
    public static final String AID_RUDOLPH = "RUDOLPH";
    
    public static final String CONV_ID_SANTA = "SANTA_MISION";
    
    public static final String CONV_ID_RUDOLPH = generateSecretCode();
    
    public static final String SEPARATOR = ";";
    
    /**
     * Generates a secret code (8 length string) using only alphabetic letters
     * @return The secret code
     */
    private static String generateSecretCode() {
        Random random = new Random();
        int min = (int)'A';
        int max = (int)'z';
        int range = max-min+1;
        String result = "";
        while(result.length()<8){
            char ch = (char)(min + random.nextInt(range));
            // Ensure that the character is an uppercase or lowercase letter
            if (Character.isLetter(ch)) {
                result += ch;
            }
        }
        return result;
    }
    
    /**
    public static void main(String args[]){
        
        
        System.out.println("Generating new random code...");
        System.out.println(generateSecretCode()+"\n");
        
        System.out.println("Generating new random code...");
        System.out.println(generateSecretCode()+"\n");
        
        System.out.println("Generating new random code...");
        System.out.println(generateSecretCode()+"\n");
    }
    */
}


