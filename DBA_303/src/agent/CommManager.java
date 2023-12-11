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
class CommManager {
    static final String AID_ELF = "ELF";
    static final String AID_SANTA = "SANTA";
    static final String AID_RUDOLF = "RUDOLF";
    
    static final String CONV_ID_SANTA = "SANTA-MISION";
    
    private static String CONV_ID_RUDOLF;
    
    /**
     * Generates a secret code (8 length string) using only alphabetic letters
     * @return The secret code
     */
    public static String generateSecretCode() {
        if (CONV_ID_RUDOLF == null) {
            Random random = new Random();
            int min = (int)'A';
            int max = (int)'z';
            int range = max-min+1;
            String result = "";
            while(result.length()<8){
                char ch = (char)(min + random.nextInt(range));
                result += ch;
            }
            CONV_ID_RUDOLF = result.toString();
        }
        return CONV_ID_RUDOLF;
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


