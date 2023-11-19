/*
 * @file
 * @author
 * @version
 */
package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.stream.Stream;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import components.Map;
import components.Position;
import components.Tile;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
        updateMapList();
        initializeListeners();
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
        this.mapSelection_List.setListData(mapList);
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
                JPanel panelTile = AssetManager.getPreviewTilePanel(tile);
                panelTileList.add(panelTile);
                this.mapPreview.add(panelTile);
            }
        }
        
        // set the limit to the spinner widgets
        ((SpinnerNumberModel)this.originRowSpinner.getModel()).setMaximum(rows-1);
        ((SpinnerNumberModel)this.originColSpinner.getModel()).setMaximum(cols-1);
        ((SpinnerNumberModel)this.targetRowSpinner.getModel()).setMaximum(rows-1);
        ((SpinnerNumberModel)this.targetColSpinner.getModel()).setMaximum(cols-1);
        
        // Update the map preview
        this.updatePositionIcons();
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
                                !selectedMap.getTile(originPos).equals(Tile.UNREACHABLE)))
                );

        // Add the target icon only if its different from the origin position
        if( !targetPos.equals(originPos)){
            panelTileList.get(targetPos.getY() * rows + targetPos.getX())
                .add( 
                    new JLabel(AssetManager.getTargetIcon(16, 
                                !selectedMap.getTile(targetPos).equals(Tile.UNREACHABLE)))
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
            }
        });
        
        // Add listener to the Spinners
        ChangeListener spinnerChListener = (ChangeEvent e) -> {
            // set the new position. Be careful, position is x,y
            originPos = new Position((int)originColSpinner.getValue(),(int)originRowSpinner.getValue());
            targetPos = new Position((int)targetColSpinner.getValue(), (int)targetRowSpinner.getValue());
            updateMapPreview();
        };
        this.originRowSpinner.addChangeListener(spinnerChListener);
        this.originColSpinner.addChangeListener(spinnerChListener);
        this.targetRowSpinner.addChangeListener(spinnerChListener);
        this.targetColSpinner.addChangeListener(spinnerChListener);
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
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DBA 2023/24 - Grupo 303");
        setResizable(false);

        DBATitle_Label.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        DBATitle_Label.setForeground(new java.awt.Color(51, 51, 51));
        DBATitle_Label.setText("Pantalla de Carga - DBA");

        numPract_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "P2 - Busqueda Objetivo", "P3 - Busqueda otro Agente" }));
        numPract_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numPract_ComboBoxActionPerformed(evt);
            }
        });

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

        jButton1.setText("Iniciar");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(originRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(targetRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(originColSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(targetColSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(originRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(originColSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(targetRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(targetColSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );

        jLabel5.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(numPract_ComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(DBATitle_Label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mapSelection_Frame, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mapSelection_Frame, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DBATitle_Label.getAccessibleContext().setAccessibleName("launcherTitle");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void numPract_ComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numPract_ComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numPract_ComboBoxActionPerformed

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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mapPreview;
    private javax.swing.JPanel mapSelection_Frame;
    private javax.swing.JList<String> mapSelection_List;
    private javax.swing.JScrollPane mapSelection_Scroll;
    private javax.swing.JComboBox<String> numPract_ComboBox;
    private javax.swing.JSpinner originColSpinner;
    private javax.swing.JSpinner originRowSpinner;
    private javax.swing.JSpinner targetColSpinner;
    private javax.swing.JSpinner targetRowSpinner;
    // End of variables declaration//GEN-END:variables
}
