/*
 * @file
 * @author
 * @version
 */
package agent;

import java.util.Random;

/**
 *
 * @author JorgeBG
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
     * Enum all Elf communication states
     */
    public enum ElfCommStates {
        INIT_MISSION,
        RECEIVE_ACCEPT_SANTA,
        PROPOSE_RUDOLPH,
        RECEIVE_ACCEPT_RUDOLPH,
        FOUND_AND_NEXT_REINDEER,
        RECEIVE_NEW_REINDEER_POS,
        REQUEST_SANTA_POS,
        RECEIVE_SANTA_POS,
        INFORM_SANTA_REACHED,
        RECEIVE_SANTA_CONGRATS;
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


