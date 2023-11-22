/*
 * @file
 * @author
 * @version
 */
package gui;

import agent.ScoutAgent;
import agent.Sensor;
import components.Action;
import components.Map;
import components.Position;
import components.Tile;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultCaret;
import launcher.Launcher;

/**
 *
 * @author JorgeBG
 */
public class MainWindow extends javax.swing.JFrame {

    Map map;
    String mapStringName;
    List<List<JPanel>> mainMapTilePanelArr;
    int MapTileWidth;
    
    
    
    /**
     * Creates new form MainWindow with a Map as argument
     */
    public MainWindow(Map map, String mapName) {
        initComponents();
        
        // set the internal map and the name to the title
        this.map = map;
        this.mapName.setText(mapName);
        
        // init the main map tile array
        int rows = this.map.getNumRows();
        int cols = this.map.getNumCols();
        
        // calculate the tile dimension to be used to draw the agent
        this.MapTileWidth = (int)(this.mainMap.getWidth()/cols);
        
        // creates the tile array for be used to draw the agent path and draw
        createMapView();
               
        updateAgent();
        
        this.mainMap.updateUI();
        
        this.closeBtn.addActionListener(((e) -> {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }));

    }

    
    /**
     * Create the map view in the Main Map Frame
     */
    private void createMapView(){
        int rows = this.map.getNumRows();
        int cols = this.map.getNumCols();
        
        // initialize the array
        GridLayout gridLayout = new GridLayout(rows, cols,1,1);
        this.mainMap.setLayout(gridLayout);
        this.mainMapTilePanelArr = new ArrayList<>();
        for(int i = 0; i< rows; i++){
            this.mainMapTilePanelArr.add(new ArrayList<>());
            for(int j=0; j<cols; j++){
                JPanel p = AssetManager.getTilePanel(map.getTile(i, j).getType());
                this.mainMapTilePanelArr.get(i).add( p );
                this.mainMap.add(p);
            }
        }

        this.mainMap.updateUI();
    }
    
    public void updateInternalMapView(){
        Map mindMap = ((ScoutAgent)Launcher.getAgent()).getExploredArea();
        int rows = mindMap.getNumRows();
        int cols = mindMap.getNumCols();
        int size = Math.max(rows, cols);
        int tileSize = 250/Math.max(rows, cols)-1;
        
        this.agentMentalMap.removeAll();
        
        //Create new grid
        GridLayout gridLayout = new GridLayout(size,size, 1,1);
        this.agentMentalMap.setLayout(gridLayout);
        
        // Complete a squared map
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(i<rows && j<cols){
                    ScoutAgent agent = ((ScoutAgent)Launcher.getAgent());
                    Position talePos = new Position(j, i);
                    JPanel miniTale = AssetManager.getMentalTilePanel(mindMap.getTile(talePos).getType());

                    if(talePos.equals(agent.getAgentRelPos())){
                        miniTale.add( new JLabel(AssetManager.getAgentCurrentIcon(tileSize)));
                    }
                    else if(talePos.equals(agent.getTargetRelPos())){
                        miniTale.add( new JLabel(AssetManager.getTargetIcon(tileSize, true)));
                    }
                    this.agentMentalMap.add(miniTale);
                }
                else{
                    JPanel minitale = AssetManager.getMentalTilePanel(Tile.Type.OTHER);
                    this.agentMentalMap.add(minitale);
                }
            }
        }
        this.agentMentalMap.updateUI();
        
    }
    
    /**
     * Updates the agent position 
     */
    public void updateAgent(){
        clearAgentPath();
        
        ArrayList<Sensor.ActionPair> path = Sensor.getInstance().getAgentVisitedPath();
        
        for(int i=0; i<path.size(); i++){
            Position p = path.get(i).getPos();
            JPanel panel = this.mainMapTilePanelArr.get(p.getY()).get(p.getX());
            if(path.get(i).getAct() == Action.IDLE)
                panel.add(new JLabel(AssetManager.getAgentCurrentIcon(this.MapTileWidth)));
            else
                panel.add(new JLabel (AssetManager.getAgentPastIcon(this.MapTileWidth)));
        }
        
        this.mainMap.updateUI();
        
    }
    
    private void clearAgentPath(){
        for(List<JPanel> row : this.mainMapTilePanelArr)
            for(JPanel p: row)
                p.removeAll();
    }
    
    
    /**************************************************************************/
    /*** update the agent in the main map *************************************/
    // this is a temporal implementation
    public void updateAgentWithoutPath(){
        clearAgentPath();
        
        // temporal -> it access directly to sensors
        Position agentPos = Sensor.getInstance().getAgentPosition();
        Position targetPos = Sensor.getInstance().getTargetPosition();
        
        JPanel agentPanel = this.mainMapTilePanelArr.get(agentPos.getY()).get(agentPos.getX());
        JPanel targetPanel = this.mainMapTilePanelArr.get(targetPos.getY()).get(targetPos.getX());
        
        agentPanel.add(new JLabel(AssetManager.getAgentCurrentIcon(this.MapTileWidth)));
        targetPanel.add(new JLabel(AssetManager.getTargetIcon(this.MapTileWidth, true)));
        
        this.mainMap.updateUI();
    }
    
    public void updateActionList(){
        
        Position origin = Sensor.getInstance().getOriginPosition();
        Position target = Sensor.getInstance().getTargetPosition();
        
        String log = "Configurado Agente:\n"
                + "   - PosInicial: "+origin+"\n"
                + "   - PosDestino: "+target+"\n\n";
        for(Sensor.ActionPair ap: Sensor.getInstance().getAgentVisitedPath()){
            log += ap.getPos();
            if(ap.getAct() == Action.END)
                log += "   ENDS";
            else if(ap.getAct()!= Action.IDLE)
                log += "   act: " + ap.getAct().name();
            log += "\n";
        }
        if(Sensor.getInstance().targetReached()){
            log+="\nOBJETIVO ALCANZADO!!!\n";
            log += "   en "+Sensor.getInstance().getAgentVisitedPath().size()+" movimientos\n";
                    
        }
        this.logTextArea.setText(log);
        this.logTextArea.updateUI();
        this.logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    }
    
    
    /**************************************************************************/
    /**************************************************************************/
    /**************************************************************************/
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainMap = new javax.swing.JPanel();
        agentMentalMap = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        closeBtn = new javax.swing.JButton();
        mainMapTitle = new javax.swing.JLabel();
        mentalMapTitle = new javax.swing.JLabel();
        logTextTitle = new javax.swing.JLabel();
        mapName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DBA 2023/24 - P2 - Grupo 303");
        setMaximumSize(new java.awt.Dimension(830, 630));
        setMinimumSize(new java.awt.Dimension(830, 630));
        setPreferredSize(new java.awt.Dimension(830, 630));
        setResizable(false);
        setSize(new java.awt.Dimension(830, 630));

        mainMap.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout mainMapLayout = new javax.swing.GroupLayout(mainMap);
        mainMap.setLayout(mainMapLayout);
        mainMapLayout.setHorizontalGroup(
            mainMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        mainMapLayout.setVerticalGroup(
            mainMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        agentMentalMap.setBackground(new java.awt.Color(43, 43, 43));
        agentMentalMap.setMaximumSize(new java.awt.Dimension(250, 250));
        agentMentalMap.setMinimumSize(new java.awt.Dimension(250, 250));
        agentMentalMap.setPreferredSize(new java.awt.Dimension(250, 250));

        javax.swing.GroupLayout agentMentalMapLayout = new javax.swing.GroupLayout(agentMentalMap);
        agentMentalMap.setLayout(agentMentalMapLayout);
        agentMentalMapLayout.setHorizontalGroup(
            agentMentalMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        agentMentalMapLayout.setVerticalGroup(
            agentMentalMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setRowHeaderView(null);
        jScrollPane1.setViewportView(null);

        logTextArea.setEditable(false);
        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        jScrollPane1.setViewportView(logTextArea);

        closeBtn.setText("Cerrar");

        mainMapTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        mainMapTitle.setText("Mapa principal");

        mentalMapTitle.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        mentalMapTitle.setText("Mapa interno mental del agente:");

        logTextTitle.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        logTextTitle.setText("Log acciones");

        mapName.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        mapName.setText("nombremapa");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mainMapTitle)
                        .addGap(27, 27, 27)
                        .addComponent(mapName)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addComponent(closeBtn)
                        .addComponent(agentMentalMap, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(mentalMapTitle)
                    .addComponent(logTextTitle))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mentalMapTitle)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mainMapTitle)
                        .addComponent(mapName)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(agentMentalMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logTextTitle)
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mainMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeBtn)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // TODO - detele 
//        Map myMap = new Map("mapWithDiagonalWall.txt");
//        Sensor.getInstance().setParameters( "mapWithDiagonalWall.txt", new Position(0,0), new Position(3,6));
//        Sensor.getInstance().setAgentPosition(new Position(0,0));
//        Sensor.getInstance().updatePosition(Action.DOWN);
//        Sensor.getInstance().updatePosition(Action.DOWN_RIGHT);
//        Sensor.getInstance().updatePosition(Action.DOWN_RIGHT);
//        Sensor.getInstance().updatePosition(Action.RIGHT);
        
        
        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new MainWindow(myMap).setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel agentMentalMap;
    private javax.swing.JButton closeBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JLabel logTextTitle;
    private javax.swing.JPanel mainMap;
    private javax.swing.JLabel mainMapTitle;
    private javax.swing.JLabel mapName;
    private javax.swing.JLabel mentalMapTitle;
    // End of variables declaration//GEN-END:variables
}
