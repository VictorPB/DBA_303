/*
 * @file    Map.java
 * @author  JorgeBG
 * @version 0.0.1 - Map using the int values instead of the BoxValue enum
 */
package components;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;

/**
 * @brief Class that models the board map where an agent will be deployed and
 * try to find a path to reach a goal position.
 */
class Map {
    
    public enum BoxValue { EMPTY, UNAVAILABLE, UNKNOWN };
    
    /// The map matrix
    private ArrayList<ArrayList<Integer>> board;
    
    /// NumCols (x dimension)
    private int x;
    
    /// Number of rows (y dimension);
    private int y;

    /**
     * Constructor with dimensions
     * @param x Map x dimension (number of columns)
     * @param y Map y dimension (number of rows)
     */
    Map(int x, int y){
        this.x = x;
        this.y = y;
        this.board = new ArrayList<>();
        for(int i=0; i<y; i++){
            ArrayList<Integer> row = new ArrayList<>();
            for(int j=0; j<x; j++){
                row.add(0);
            }
            this.board.add(row);
        }
    }
    
    /**
     * Constructor with a map file name
     * @param mapname 
     */
    Map(String mapname){
        this.board = readMapFromFile(mapname);
        this.x = this.board.size();
        this.y = this.board.get(0).size();
    }
    
    /**
     * Private method that reads the map given the .txt file name
     * @param filename
     * @return 
     */
    private ArrayList<ArrayList<Integer>> readMapFromFile(String filename){
        // Create a new empty array
        ArrayList<ArrayList<Integer>> tempBoard = new ArrayList<>();
        // Converts the filename to abosolute path
        String filepath = new File("maps/" + filename).getAbsolutePath();
        System.out.println(filepath);
        // Create the filereader and buffered reader objects and try to read 
        // the target file
        FileReader fr = null;
        BufferedReader br = null;
        try{
            //file = new File(filename);
            fr = new FileReader(filepath);
            br = new BufferedReader(fr);
            
            int rows = Integer.parseInt( br.readLine() );
            int cols = Integer.parseInt( br.readLine() );
            String bufferLine;
            
            for( int i=0; i<rows; i++){
                bufferLine = br.readLine();
                ArrayList<Integer> row = new ArrayList<>();
                if(bufferLine!= null){
                    for(String c: bufferLine.split("\t")){
                        row.add(Integer.parseInt(c));
                    }
                    tempBoard.add(row);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(fr!=null){
                    fr.close();
                }
            }
            catch(Exception e2){
                e2.printStackTrace();
            }
        }
        
        return tempBoard;
    }


    @Override
    public String toString() {
        String res = "";
        for(ArrayList<Integer> row : this.board){
            for(Integer c: row){
                if(c==0)        res+="▯";
                else if(c==-1)  res+="▮";
            }
            res +="\n";
        }
        return res;
    }
     
    
    /**************************************************************************/
    
    /**
     * Testing the Map class...
     * This function may open a map file, instanciates and print it in console
     * @param args 
     */
    public static void main(String[] args) {
        // testeamos la lectura del archivo:
        String file = "mapWithComplexObstacle1.txt";
        
        Map mapa = new Map(file);
        
        System.out.println(mapa);
    }
}
