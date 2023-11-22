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
    
    /**
     * Default constructor
     */
    public Map() {
        this.board = new ArrayList<>();
    }
    
    /**
     * Constructor with dimensions
     * @param cols Map cols dimension (number of columns)
     * @param rows Map rows dimension (number of rows)
     */
    public Map(int cols, int rows){

        this.board = new ArrayList<>();
        for(int i=0; i<rows; i++){
            ArrayList<Tile> row = new ArrayList<>();
            for(int j=0; j<cols; j++){
                row.add(new Tile(Tile.Type.UNKNOWN));
            }
            this.board.add(row);
        }
    }

    
    /**
     * Constructor with a map file name
     * @param mapname 
     */
    public Map(String mapname){
        this.board = readMapFromFile(mapname);
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
                        row.add(new Tile(Integer.parseInt(c)));
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
    public int getNumRows() { return this.board.size(); };
    
    
    /**
     * Gets the number of columns of the map
     */
    public int getNumCols() { 
        if(getNumRows()==0)
            return 0;
        else
            return this.board.get(0).size(); 
    };
    
    /** SETTERS ***************************************************************/
    
    /**
     * Sets the tile
     * @param col
     * @param row
     * @param tile
     */
    public void setTile(int row, int col, Tile tile) { 
        if(col >= 0 && col < board.get(0).size() && row >= 0 && row < board.size()) {
            board.get(row).set(col, tile);
        }
        else{System.out.println("at "+row+","+col+" not setted");}
    };
    
    /**
     * Gets the Tile at a position
     * @param r Row index
     * @param c Column index
     * @return The tile
     */
    public Tile getTile(int r, int c){
        return this.board.get(r).get(c);
    }
    
    /**
     * Gets the Tile at a position (with Position instance)
     * @param p The position
     * @return The tile
     */
    public Tile getTile(Position p){
        return this.board.get(p.getY()).get(p.getX());
    }
            
    /** USEFUL ****************************************************************/
    
    public void addRowToEnd(){
        ArrayList<Tile> newRow = new ArrayList<>();
        for(int i=0; i<getNumCols(); i++)  
            newRow.add(new Tile(Tile.Type.UNKNOWN));
        this.board.add(newRow);
    }
    
    public void addRowToBeggining(){
        ArrayList<Tile> newRow = new ArrayList<>();
        for(int i=0; i<getNumCols(); i++)  
            newRow.add(new Tile(Tile.Type.UNKNOWN));
        this.board.add(0,newRow);
    }
    
    public void addColToEnd(){
        for(ArrayList r : this.board)
            r.add(new Tile(Tile.Type.UNKNOWN));
    }
    
    public void addColToBeggining(){
        for(ArrayList r : this.board)
            r.add(0, new Tile(Tile.Type.UNKNOWN));
    }
    
    /**************************************************************************/

    @Override
    public String toString() {
        String res = "";
        for(ArrayList<Tile> row : this.board){
            for(Tile c: row){
                if(c.isType(Tile.Type.EMPTY))               res+="▯";
                else if(c.isType(Tile.Type.UNREACHABLE))    res+="▮";
                else if(c.isType(Tile.Type.UNKNOWN))        res+="?";
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
    /*
    public static void main(String[] args) {
        // testeamos la lectura del archivo:
        String file = "mapWithComplexObstacle1.txt";
        
        Map mapa = new Map(file);
        
        System.out.println(mapa);
    }
    */
}
