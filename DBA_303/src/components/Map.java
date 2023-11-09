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
public class Map {
  
    /// The map matrix
    private ArrayList<ArrayList<Tile>> board;
    
    /// NumCols (cols dimension)
    private int cols;
    
    /// Number of rows (rows dimension);
    private int rows;

    /**
     * Constructor with dimensions
     * @param cols Map cols dimension (number of columns)
     * @param rows Map rows dimension (number of rows)
     */
    public Map(int cols, int rows){
        this.cols = cols;
        this.rows = rows;
        this.board = new ArrayList<>();
        for(int i=0; i<rows; i++){
            ArrayList<Tile> row = new ArrayList<>();
            for(int j=0; j<cols; j++){
                row.add(Tile.UNKNOWN);
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
        this.cols = this.board.size();
        this.rows = this.board.get(0).size();
    }
    
    /** PRIVATE UTIL METHODS **************************************************/
    /**
     * Private method that reads the map given the .txt file name
     * @param filename
     * @return 
     */
    private ArrayList<ArrayList<Tile>> readMapFromFile(String filename){
        // Create a new empty array
        ArrayList<ArrayList<Tile>> tempBoard = new ArrayList<>();
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
                ArrayList<Tile> row = new ArrayList<>();
                if(bufferLine!= null){
                    for(String c: bufferLine.split("\t")){
                        row.add(Tile.fromValue(Integer.parseInt(c)));
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

    
    /** GETTERS ***************************************************************/
    
    /**
     * Gets the number of rows of the map
     */
    public int getNumRows() { return rows; };
    
    
    /**
     * Gets the number of columns of the map
     */
    public int getNumCols() { return cols; };
    
    
    /**
     * Gets the Tile at a position
     * @param r Row index
     * @param c Column index
     * @return The tile
     */
    public Tile getTile(int r, int c){
        return this.board.get(r).get(c);
    }
    
    
    /**************************************************************************/

    @Override
    public String toString() {
        String res = "";
        for(ArrayList<Tile> row : this.board){
            for(Tile c: row){
                if(c == Tile.EMPTY)             res+="▯";
                else if(c== Tile.UNREACHABLE)   res+="▮";
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
     * */
    public static void main(String[] args) {
        // testeamos la lectura del archivo:
        String file = "mapWithComplexObstacle1.txt";
        
        Map mapa = new Map(file);
        
        System.out.println(mapa);
    }
    
}
