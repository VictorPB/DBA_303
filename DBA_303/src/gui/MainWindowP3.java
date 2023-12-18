/*
 * @file
 * @author
 * @version
 */
package gui;

import components.Environment;
import components.Map;
import components.Position;
import components.Reindeer;
import components.Tile;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultCaret;
import launcher.Launcher;

/**
 *
 * @author JorgeBG
 */
public class MainWindowP3 extends javax.swing.JFrame {

    Map map;
    String mapStringName;
    List<List<JPanel>> mainMapTilePanelArr;
    int MapTileWidth;
    
    JPanel rudolphConvPanel, santaConvPanel;
    
    
    
    /**
     * Creates new form MainWindow with a Map as argument
     */
    public MainWindowP3(Map map) {
        initComponents();
        
        // set the environment
        Environment.getInstance().setParameters(map);
        
        this.map = map;
        this.mainMap.setBackground(AssetManager.CONVERSATION_HEADER_BG);
        
        this.santaConvPanel = new ConversationPanel("SANTA");
        this.santaConvPanel.setMaximumSize(new Dimension(360,360));
        this.santaConvContainer.add(this.santaConvPanel);
        this.rudolphConvPanel = new ConversationPanel("RUDOLF");
        this.rudolphConvPanel.setMaximumSize(new Dimension(360,360));
        this.rudolphConvContainer.add(this.rudolphConvPanel);
        this.MapTileWidth = (int)(this.mainMap.getWidth()/map.getNumCols());
        
        createMapView();
        
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
        System.out.println("MAP: "+rows +"x"+cols);
        
        // initialize the array
        GridLayout gridLayout = new GridLayout(rows, cols,1,1);
        this.mainMap.setLayout(gridLayout);
        this.mainMapTilePanelArr = new ArrayList<>();
        for(int i = 0; i< rows; i++){
            this.mainMapTilePanelArr.add(new ArrayList<>());
            for(int j=0; j<cols; j++){
                JPanel p = AssetManager.getTileP3Panel(map.getTile(i, j).getType());
                this.mainMapTilePanelArr.get(i).add( p );
                this.mainMap.add(p);
            }
        }

        this.updateIcons();
    }
    
    private void updateIcons(){
        clearMap();
        Environment theEnvironment = Environment.getInstance();
        Position elf = theEnvironment.getElfPosition();
        this.mainMapTilePanelArr.get(elf.getY()).get(elf.getX()).add(
            new JLabel(new ImageIcon(AssetManager.getElfIcon(this.MapTileWidth))));
        Position santa = theEnvironment.getSantaPosition();
        this.mainMapTilePanelArr.get(santa.getY()).get(santa.getX()).add(
            new JLabel(new ImageIcon(AssetManager.getSantaAvatar(this.MapTileWidth))));
        Position rudolph = theEnvironment.getRudolphPosition();
        this.mainMapTilePanelArr.get(rudolph.getY()).get(rudolph.getX()).add(
            new JLabel(new ImageIcon(AssetManager.getRudolfAvatar(this.MapTileWidth))));
        
        for(Reindeer r: theEnvironment.getReindeers()){
            Position pos = r.getPosition();
            JLabel icon;
            switch (r.getState()) {
                case UNKNOWN:
                    icon = new JLabel(new ImageIcon(AssetManager.getReindeer_Soft(this.MapTileWidth)));
                    break;
                case KNOWN:
                    icon = new JLabel(new ImageIcon(AssetManager.getReindeer_Line(this.MapTileWidth)));
                    break;
                case CURRENT:
                    icon = new JLabel(new ImageIcon(AssetManager.getReindeer_Target(this.MapTileWidth)));
                    break;
                default:
                    icon = new JLabel("");
            }
            this.mainMapTilePanelArr.get(pos.getY()).get(pos.getX()).add(icon);
            this.mainMap.updateUI();
        }
    }
    
    /**
     * Updates the agent position 
     */
    public void updateAgent(){
//        clearAgentPath();
//        
//        ArrayList<Sensor.ActionPair> path = Sensor.getInstance().getAgentVisitedPath();
//        
//        for(int i=0; i<path.size(); i++){
//            Position p = path.get(i).getPos();
//            JPanel panel = this.mainMapTilePanelArr.get(p.getY()).get(p.getX());
//            if(path.get(i).getAct() == Action.IDLE)
//                panel.add(new JLabel(AssetManager.getAgentCurrentIcon(this.MapTileWidth)));
//            else
//                panel.add(new JLabel (AssetManager.getAgentPastIcon(this.MapTileWidth)));
//        }
//        
//        this.mainMap.updateUI();
//        
    }
    
    private void clearMap(){
        for(List<JPanel> row : this.mainMapTilePanelArr)
            for(JPanel p: row)
                p.removeAll();
    }
    
    
    /**************************************************************************/
    /*** update the agent in the main map *************************************/
    // this is a temporal implementation
    public void updateAgentWithoutPath(){
//        clearAgentPath();
//        
//        // temporal -> it access directly to sensors
//        Position agentPos = Sensor.getInstance().getAgentPosition();
//        Position targetPos = Sensor.getInstance().getTargetPosition();
//        
//        JPanel agentPanel = this.mainMapTilePanelArr.get(agentPos.getY()).get(agentPos.getX());
//        JPanel targetPanel = this.mainMapTilePanelArr.get(targetPos.getY()).get(targetPos.getX());
//        
//        agentPanel.add(new JLabel(AssetManager.getAgentCurrentIcon(this.MapTileWidth)));
//        targetPanel.add(new JLabel(AssetManager.getTargetIcon(this.MapTileWidth, true)));
//        
//        this.mainMap.updateUI();
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
        closeBtn = new javax.swing.JButton();
        mainMapTitle = new javax.swing.JLabel();
        santaConvContainer = new javax.swing.JPanel();
        rudolphConvContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DBA 2023/24 - P2 - Grupo 303");
        setSize(new java.awt.Dimension(830, 630));

        mainMap.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout mainMapLayout = new javax.swing.GroupLayout(mainMap);
        mainMap.setLayout(mainMapLayout);
        mainMapLayout.setHorizontalGroup(
            mainMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 730, Short.MAX_VALUE)
        );
        mainMapLayout.setVerticalGroup(
            mainMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 730, Short.MAX_VALUE)
        );

        closeBtn.setText("Cerrar");

        mainMapTitle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        mainMapTitle.setText("Mapa");

        santaConvContainer.setMaximumSize(new java.awt.Dimension(360, 360));
        santaConvContainer.setMinimumSize(new java.awt.Dimension(360, 360));
        santaConvContainer.setPreferredSize(new java.awt.Dimension(360, 360));
        santaConvContainer.setLayout(new javax.swing.BoxLayout(santaConvContainer, javax.swing.BoxLayout.LINE_AXIS));

        rudolphConvContainer.setMaximumSize(new java.awt.Dimension(360, 360));
        rudolphConvContainer.setMinimumSize(new java.awt.Dimension(360, 360));
        rudolphConvContainer.setPreferredSize(new java.awt.Dimension(360, 360));
        rudolphConvContainer.setLayout(new javax.swing.BoxLayout(rudolphConvContainer, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(closeBtn, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mainMapTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mainMap, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(santaConvContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rudolphConvContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(mainMapTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(santaConvContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(rudolphConvContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mainMap, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeBtn)
                .addContainerGap(9, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(MainWindowP3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindowP3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindowP3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindowP3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Create the example map
        Map myMap = new Map("P3map40.txt");
        System.out.println(myMap);
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindowP3 mw = new MainWindowP3(myMap);
                mw.setVisible(true);
                
                ((ConversationPanel)mw.santaConvPanel).addElfMessage("Yepaaaa que pacha!!!");
                ((ConversationPanel)mw.santaConvPanel).addReceiverMsg("Chupame un huevo");
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeBtn;
    private javax.swing.JPanel mainMap;
    private javax.swing.JLabel mainMapTitle;
    private javax.swing.JPanel rudolphConvContainer;
    private javax.swing.JPanel santaConvContainer;
    // End of variables declaration//GEN-END:variables
}
