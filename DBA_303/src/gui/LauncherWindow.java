/*
 * @file
 * @author
 * @version
 */
package gui;

import java.awt.GridLayout;
import java.io.File;
import java.util.stream.Stream;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import components.Map;
import components.Position;
import components.Tile;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import launcher.Launcher;

/**
 *
 * @author JorgeBG
 */
public class LauncherWindow extends javax.swing.JFrame {

    static public String mapName;
    static public Map selectedMap;
    static private List<JPanel> panelTileList;
        
    static public Position originPos = new Position(0,0);
    static public Position targetPos = new Position(0,0);
            
    
    /**
     * Creates new form LauncherWindow
     */
    public LauncherWindow() {
        initComponents();
        this.numPract_ComboBox.setSelectedIndex(1);
        notifyPChange();
        updateMapList();
        initializeListeners();
        this.warningLabel.setVisible(false);
        
    }

    /**
     * Function that reads all files into the map folder and fills the launcher
     * menu
     */
    private void updateMapList(){
        String path = new File("maps/").getAbsolutePath();
        String [] mapList = Stream.of(new File(path).listFiles())
            .filter(file -> !file.isDirectory())
            .filter(file -> file.getName().endsWith(".txt"))
            .map(file -> file.getName())
            .toArray(String[]::new);
        String [] filteredMapList;
        String [] p3List = Stream.of(mapList)
                .filter(name -> name.startsWith("P3"))
                .toArray(String[]::new);
        
        if(this.numPract_ComboBox.getSelectedIndex() == 0){
            filteredMapList = Stream.of(mapList)
                .filter(name -> !name.startsWith("P3"))
                .toArray(String[]::new);
        }
        else{
            filteredMapList = Stream.of(mapList)
                .filter(name -> name.startsWith("P3"))
                .toArray(String[]::new);
        }
        
        this.mapSelection_List.setListData(filteredMapList);
    }
    
    
    /**
     * Private method that checks if its the P3 selected
     */
    private boolean isP2selected(){
        return this.numPract_ComboBox.getSelectedIndex()==0;
    }
    
    /**
     * Private method that control the interaction between the GUI instances
     */
    private void notifyPChange(){
        if(isP2selected()){
            this.positionPanel.setVisible(true);
        }
        else{   // P3
            this.positionPanel.setVisible(false);
        }
        this.mapSelection_List.setSelectedIndex(-1);
        this.updateMapList();
        this.acceptButton.setEnabled(false);
    }
    
    /**
     * Method to update the Preview map
     */
    private void updateMapPreview(){
        // first clean the panel
        this.mapPreview.removeAll();
        // array to retrieve the tile by row and col
        panelTileList = new ArrayList<JPanel>();
        
        // Open the new map
        if(this.numPract_ComboBox.getSelectedIndex()>-1){
            selectedMap = new Map(LauncherWindow.mapName);
            int rows = selectedMap.getNumRows();
            int cols = selectedMap.getNumCols();

            // Create the grid and assign to the mapPreview Panel
            GridLayout gridLayout = new GridLayout(rows, cols,1,1);
            this.mapPreview.setLayout(gridLayout);
            for(int i=0; i<rows; i++){
                for(int j=0; j<cols; j++){
                    Tile tile = selectedMap.getTile(i,j);
                    // add the tile to the panel
                    JPanel panelTile = AssetManager.getTilePanel(tile.getType());
                    panelTileList.add(panelTile);
                    this.mapPreview.add(panelTile);
                }
            }
        
            // set the limit to the spinner widgets
            ((SpinnerNumberModel)this.originRowSpinner.getModel()).setMaximum(rows-1);
            ((SpinnerNumberModel)this.originColSpinner.getModel()).setMaximum(cols-1);
            ((SpinnerNumberModel)this.targetRowSpinner.getModel()).setMaximum(rows-1);
            ((SpinnerNumberModel)this.targetColSpinner.getModel()).setMaximum(cols-1);
        }
        
        // Update the position icons only if it is in P2
        if(isP2selected()){
            this.updatePositionIcons();
        }
        this.mapPreview.updateUI();
    }

    /**
     * Method that updates the origin and target icons for the mapPreview
     */
    private void updatePositionIcons(){
        
        clearPositionIcons();
        int rows = selectedMap.getNumRows();
        
        // Add the origin icon
        panelTileList.get(originPos.getY() * rows + originPos.getX())
                .add(
                    new JLabel(AssetManager.getOriginIcon(16, 
                                !selectedMap.getTile(originPos).isType(Tile.Type.UNREACHABLE)))
                );

        // Add the target icon only if its different from the origin position
        if( !targetPos.equals(originPos)){
            panelTileList.get(targetPos.getY() * rows + targetPos.getX())
                .add( 
                    new JLabel(AssetManager.getTargetIcon(16, 
                                !selectedMap.getTile(targetPos).isType(Tile.Type.UNREACHABLE)))
                );
        }
    }
    
    /**
     * Method that clears the origin and target icons from the map preview
     */
    private void clearPositionIcons(){
        for(JPanel p: panelTileList){
            p.removeAll();
        }
    }
    
    /**
     * Method to initialize all listener for the gui usability
     */
    private void initializeListeners(){
        // Add listener to the map list
        this.mapSelection_List.addListSelectionListener((ListSelectionEvent e) -> {
            if( !e.getValueIsAdjusting()){
                LauncherWindow.mapName = mapSelection_List.getSelectedValue();
                updateMapPreview();
                // enable the button if it is the p3 selection
                if(numPract_ComboBox.getSelectedIndex() == 1){
                    this.acceptButton.setEnabled(true);
                }
            }
        });
        
        // Add listener to the Spinners
        ChangeListener spinnerChListener = (ChangeEvent e) -> {
            // set the new position. Be careful, position is x,y
            originPos = new Position((int)originColSpinner.getValue(),(int)originRowSpinner.getValue());
            targetPos = new Position((int)targetColSpinner.getValue(), (int)targetRowSpinner.getValue());
            if( selectedMap != null &&
                    !originPos.equals(targetPos) && 
                    selectedMap.getTile(originPos).isReacheable() &&
                    selectedMap.getTile(targetPos).isReacheable()){
                this.acceptButton.setEnabled(true);
                this.warningLabel.setVisible(false);
            }
            else{
                this.acceptButton.setEnabled(false);
                this.warningLabel.setVisible(true);
            }
            updateMapPreview();
        };
        this.originRowSpinner.addChangeListener(spinnerChListener);
        this.originColSpinner.addChangeListener(spinnerChListener);
        this.targetRowSpinner.addChangeListener(spinnerChListener);
        this.targetColSpinner.addChangeListener(spinnerChListener);
        
        this.numPract_ComboBox.addActionListener((e) -> notifyPChange());
        
        this.acceptButton.addActionListener((e) -> openCorrespondingMainWindow());
    }
    
    /**
     * Private method to use as callback in the accept button
     * Its select the corresponding main window to be opened
     */
    private void openCorrespondingMainWindow(){
        if(isP2selected())  Launcher.runP2();
        else                Launcher.runP3();
    }
        
    private boolean valid(){
        if(isP2selected() && selectedMap != null){
            boolean res = !targetPos.equals(originPos);
            res &= selectedMap.getTile(originPos).isReacheable();
            res &= selectedMap.getTile(targetPos).isReacheable();
            return res;
        }
        else if( !isP2selected() && selectedMap != null){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**************************************************************************/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DBATitle_Label = new java.awt.Label();
        numPract_ComboBox = new javax.swing.JComboBox<>();
        mapSelection_Frame = new javax.swing.JPanel();
        mapPreview = new javax.swing.JPanel();
        mapSelection_Scroll = new javax.swing.JScrollPane();
        mapSelection_List = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        acceptButton = new javax.swing.JButton();
        positionPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        originColSpinner = new javax.swing.JSpinner();
        originRowSpinner = new javax.swing.JSpinner();
        targetRowSpinner = new javax.swing.JSpinner();
        targetColSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        warningLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DBA 2023/24 - P2 - Grupo 303");
        setResizable(false);

        DBATitle_Label.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        DBATitle_Label.setForeground(new java.awt.Color(51, 51, 51));
        DBATitle_Label.setText("Pantalla de Carga - DBA");

        numPract_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "P2 - Busqueda Objetivo", "P3 - Busqueda otro Agente" }));

        mapSelection_Frame.setBackground(new java.awt.Color(102, 102, 102));
        mapSelection_Frame.setPreferredSize(new java.awt.Dimension(500, 220));

        mapPreview.setBackground(new java.awt.Color(204, 204, 204));
        mapPreview.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout mapPreviewLayout = new javax.swing.GroupLayout(mapPreview);
        mapPreview.setLayout(mapPreviewLayout);
        mapPreviewLayout.setHorizontalGroup(
            mapPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        mapPreviewLayout.setVerticalGroup(
            mapPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        mapSelection_List.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        mapSelection_Scroll.setViewportView(mapSelection_List);

        jLabel1.setText("Selección de Mapa:");

        javax.swing.GroupLayout mapSelection_FrameLayout = new javax.swing.GroupLayout(mapSelection_Frame);
        mapSelection_Frame.setLayout(mapSelection_FrameLayout);
        mapSelection_FrameLayout.setHorizontalGroup(
            mapSelection_FrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapSelection_FrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mapSelection_FrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mapSelection_FrameLayout.createSequentialGroup()
                        .addComponent(mapSelection_Scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mapPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84))
                    .addGroup(mapSelection_FrameLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        mapSelection_FrameLayout.setVerticalGroup(
            mapSelection_FrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mapSelection_FrameLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mapSelection_FrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mapSelection_Scroll)
                    .addComponent(mapPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        acceptButton.setText("Iniciar");
        acceptButton.setEnabled(false);

        jLabel2.setText("Selección posiciones:");

        jLabel3.setText("Origen");

        jLabel4.setText("Destino");

        originColSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));

        originRowSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));

        targetRowSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));

        targetColSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));

        jLabel5.setText("Fila:");

        jLabel6.setText("Col:");

        jLabel7.setText("Col:");

        jLabel8.setText("Fila:");

        warningLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        warningLabel.setForeground(new java.awt.Color(153, 0, 0));
        warningLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        warningLabel.setText("<html><center> Compruebe las posiciones de inicio y final.</br> No deben ser iguales ni estar sobre un muro. </center></html>");

        javax.swing.GroupLayout positionPanelLayout = new javax.swing.GroupLayout(positionPanel);
        positionPanel.setLayout(positionPanelLayout);
        positionPanelLayout.setHorizontalGroup(
            positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(positionPanelLayout.createSequentialGroup()
                .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(warningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(positionPanelLayout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(positionPanelLayout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(positionPanelLayout.createSequentialGroup()
                                    .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(originRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(targetRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(originColSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(targetColSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(14, 14, 14))
        );
        positionPanelLayout.setVerticalGroup(
            positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(positionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(originRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(originColSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(targetRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(targetColSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(warningLabel)
                .addContainerGap())
        );

        jLabel5.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(acceptButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(numPract_ComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(DBATitle_Label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mapSelection_Frame, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(positionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DBATitle_Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numPract_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(positionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mapSelection_Frame, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(acceptButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DBATitle_Label.getAccessibleContext().setAccessibleName("launcherTitle");

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
            java.util.logging.Logger.getLogger(LauncherWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LauncherWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LauncherWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LauncherWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LauncherWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label DBATitle_Label;
    private javax.swing.JButton acceptButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel mapPreview;
    private javax.swing.JPanel mapSelection_Frame;
    private javax.swing.JList<String> mapSelection_List;
    private javax.swing.JScrollPane mapSelection_Scroll;
    private javax.swing.JComboBox<String> numPract_ComboBox;
    private javax.swing.JSpinner originColSpinner;
    private javax.swing.JSpinner originRowSpinner;
    private javax.swing.JPanel positionPanel;
    private javax.swing.JSpinner targetColSpinner;
    private javax.swing.JSpinner targetRowSpinner;
    private javax.swing.JLabel warningLabel;
    // End of variables declaration//GEN-END:variables
}
