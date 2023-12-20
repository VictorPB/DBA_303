/*
 * DBA PR3 - Files for the resolution of the Pr3, Agent communication.
 * @file    ConversationPanel.java
 * @author  DBA_303. JorgeBG
 */
package gui;

import components.Reindeer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * @brief   Class that models a Panel where diferent agents shows its
 *          conversation.
 */
public class ConversationPanel extends javax.swing.JPanel {

    static final int ICON_SIZE = 40;
    static final int CONV_PAN_WIDTH = 360;
    static final Font nameFont = new Font("Arial", Font.BOLD, 16);
    // COLORS
    static final Color AVATAR_BG = Color.decode("#c7cfd4");
    static final Color MSG_PANEL_BG = Color.decode("#e9f1f5");
    
    /// Destionation avatar icon
    Image destIcon;
    /// Destionation agent name
    String destName;
    /// internal panel for a tasklist to be showed
    JLayeredPane taskListPanel;
    
    /**
     * Creates new form ConversationPanel
     */
    public ConversationPanel(String receiver) {
        initComponents();
        this.headerPanel.setBackground(AssetManager.CONVERSATION_HEADER_BG);
        
        
        // Init the 
        if(receiver=="SANTA"){
            destName = "SantaClaus";
            destIcon = AssetManager.getSantaAvatar(ICON_SIZE);
        }
        else if(receiver=="RUDOLF"){
            destName = "Rudolf";
            destIcon = AssetManager.getRudolfAvatar(ICON_SIZE);
        }
        
        createHeader();
        addSeparatorToConversation();
    }
    
    /**
     * Private method to generate the content of the Conversation header
     */
    private void createHeader(){
        // añado icono
        JLabel icon = new JLabel(new ImageIcon(destIcon));
        icon.setBounds(5,5,ICON_SIZE,ICON_SIZE);
        this.headerPanel.add(icon);
        // añado Circulo
        JPanel iconBg = new CirclePanel(40, Color.black);
        iconBg.setBounds(5,5,ICON_SIZE,ICON_SIZE);
        this.headerPanel.add(iconBg);
        // añado el nombre
        JLabel name = new JLabel(this.destName);
        name.setBounds(55,5,300,40);
        name.setFont(nameFont);
        name.setAlignmentX(LEFT_ALIGNMENT);
        name.setAlignmentY(CENTER_ALIGNMENT);
        this.headerPanel.add(name);
    }
    
    
    /**
     * Adds an elf message
     * @param msg the message
     */
    public void addElfMessage(String msg){
        // add a previous separator
        //addSeparatorToConversation();
        // create and add the entry panel to the conversation
        this.ConvPanel.add(new MessageEntryPanel(Sender.ME, msg));
        this.updateUI();
    }

    /**
     * Adds a message from the other agent (Rudolf or Santa)
     * @param msg the message
     */
    public void addReceiverMsg(String msg){
        // add a previous separator
        //addSeparatorToConversation();
        // create and add the entry panel to the conversation
        this.ConvPanel.add(new MessageEntryPanel(Sender.TARGET, msg));
        this.updateUI();
    }    

    /**
     * Private method to add a Separator to the conversation Panel
     */
    private void addSeparatorToConversation(){
        this.ConvPanel.add(
                new Box.Filler(
                        new Dimension(0, 5), 
                        new Dimension(0, 5), 
                        new Dimension(32767, 5))
        );
        this.updateUI();
    }
    
    /**  USEFUL INNER CLASSES  *************************************************/
    
    /**
     * @brief   A class that creates a panel with a circled backgroud for the
     *          header of the conversation panel
     */
    class CirclePanel extends JPanel{
        
        private Dimension size;
        private Color color;
        
        public CirclePanel(int dim, Color color){
            this.size = new Dimension(dim,dim);
            this.color = color;
            this.setSize(this.size);
            this.setPreferredSize(this.size);
            this.setMaximumSize(this.size);
            this.setMinimumSize(this.size);
        }
        
        @Override
        protected void paintComponent(Graphics gr){
            Graphics2D g2d = (Graphics2D) gr.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(this.color);
            g2d.fillOval(0,0,this.size.width,this.size.height);
            g2d.dispose();
        }
    }
    
    
    /**
     * Enum to difference between the message sender
     */
    enum Sender{ ME, TARGET }

    /**
     * @brief Inner class to draw the message background
     */
    class MessageBgPanel extends JPanel {
        
        Sender sender;
        Area msgBg;
        static final int PAN_WIDTH = 280;
        static final int STEP = 10;
        
        MessageBgPanel(Sender sender, int height){
            this.sender = sender;
            
            Dimension d = new Dimension(PAN_WIDTH,height);
            this.setSize(d);
            this.setMinimumSize(d);
            this.setMaximumSize(d);
            
            this.msgBg = new Area(
                    new RoundRectangle2D.Float(10,0,260,height,10,10));
            Path2D mark = new Path2D.Float();
            if(this.sender.equals(Sender.ME)){
                mark.moveTo(0, 0);
                mark.lineTo(2*STEP,0);
                mark.lineTo(STEP,STEP);
                mark.closePath();
            }
            else{
                mark.moveTo(PAN_WIDTH-2*STEP, 0);
                mark.lineTo(PAN_WIDTH, 0);
                mark.lineTo(PAN_WIDTH-STEP, STEP);
                mark.closePath();
            }

            this.msgBg.add(new Area((Shape)mark));
        }
 

        @Override
        public void paintComponent(Graphics g) {
          Graphics2D g2d = (Graphics2D) g;
          g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

          g2d.setColor(MSG_PANEL_BG);
          g2d.fill(this.msgBg);
        }
    }

    /**
     * @brief   Class to generate the whole entry panel for the main
     *          panel of the conversation
     */
    class MessageEntryPanel extends JLayeredPane {
        ConversationPanel.Sender sender;
        String msg;
        
        static final int LINE_H = 14;
        static final int VMARGIN = 5;
        static final int MIN_H = LINE_H + 4*VMARGIN;

        MessageEntryPanel(ConversationPanel.Sender sender, String msg) {
            this.sender = sender;
            this.msg = msg;
            
            // Split lines and count
            String newMsg = msg;
            ArrayList<String> lines = new ArrayList<>();
            while(newMsg.length()>40){
                int ind = newMsg.indexOf(" ",0);
                while(newMsg.indexOf(" ",ind+1)<40 && newMsg.indexOf(" ",ind+1)!= -1)
                    ind = newMsg.indexOf(" ",ind+1);
                lines.add(newMsg.substring(0,ind));
                newMsg = newMsg.substring(ind).trim();
            }
            lines.add(newMsg);

            // calculate the dimension of the pannel
            int height = lines.size()*LINE_H + 4*VMARGIN;
            if(height < MIN_H) height = MIN_H;
            int bgHeight = height - 2*VMARGIN;
            
            /******************************************************************/
            
            // Create the label(s)
            for(int i=0; i<lines.size(); i++){
                JLabel l = new JLabel(lines.get(i));
                l.setBounds(55,2*VMARGIN+i*LINE_H,250,LINE_H);
                this.add(l);
            }

            //dpending on the sender, place icon, msg bg (and circle?)
            JLabel avatar;
            JPanel avatarBg = new ConversationPanel.CirclePanel(30, AVATAR_BG);

            if(sender==ConversationPanel.Sender.ME){
                avatar = new JLabel(new ImageIcon(AssetManager.getElfAvatar(30)));
                avatar.setBounds(10,VMARGIN, 30,30);
                avatarBg.setBounds(10,VMARGIN,30,30);
            }
            else{
                avatar = new JLabel(new ImageIcon(destIcon.getScaledInstance(30, 30, 0)));
                avatar.setBounds(320,VMARGIN,30,30);
                avatarBg.setBounds(320,VMARGIN,30,30);
            }

            JPanel msgBg = new ConversationPanel.MessageBgPanel(sender, bgHeight);
            msgBg.setBounds(40,VMARGIN,280,bgHeight);

            this.add(avatar);
            this.add(avatarBg);
            this.add(msgBg);
            
            // resize the panel
            Dimension d = new Dimension(360 ,height);
            this.setSize(d);
            this.setMaximumSize(d);
            this.setMinimumSize(d);
        }
    }
    
    /**
     * @brief   Class that models a task (mission) list
     */
    class missionList extends JLayeredPane {
        Map<Reindeer.Name, Boolean> misions;
        public Map<Reindeer.Name, JLabel> missionsCheckedPanels;
        JLayeredPane misionListPane;
        
        missionList(){
            misions = new HashMap<>();
            missionsCheckedPanels = new HashMap<>();
            for(Reindeer.Name r: Reindeer.Name.values()){
                misions.put(r, false);
                missionsCheckedPanels.put(r, 
                    new JLabel( new ImageIcon(AssetManager.getTick_Empty(20))));
            }
            
            Dimension d = new Dimension(360, 125);
            this.setSize(d);
            this.setMaximumSize(d);
            this.setMinimumSize(d);
            
            // Main taks list panel
            misionListPane = new JLayeredPane();
            misionListPane.setBounds(70, 0, 220,125);
            this.add(misionListPane);
                       
            
            // Add main label
            JLabel panelTitle = new JLabel("LISTA DE TAREAS:");
            panelTitle.setBounds(0,0,220,20);
            panelTitle.setHorizontalAlignment(SwingConstants.CENTER);
            panelTitle.setFont(new Font("Arial", Font.BOLD, 14));
            misionListPane.add(panelTitle);

            // Add each panel tile
            for(int i = 0; i<misions.size(); i++){
                int x = 15 + (int)(i/4)*100;
                int y = 25 + (i%4)*25;
                Reindeer.Name theReindeer = Reindeer.Name.values()[i];
                JLabel tick = missionsCheckedPanels.get(theReindeer);
                tick.setBounds(x, y, 20,20);
                misionListPane.add(tick);
                JLabel name = new JLabel(theReindeer.name());
                name.setBounds(x+25, y, 65, 20);
                name.setVerticalAlignment(SwingConstants.CENTER);
                misionListPane.add(name);
            }
            
            // Add the bg to the list
            JPanel misionListBgPanel = new JPanel();
            misionListBgPanel.setBounds(0,0,220,125);
            misionListBgPanel.setBackground(AssetManager.CONVERSATION_HEADER_BG);
            misionListPane.add(misionListBgPanel);
        }
        
        void checkFoundReindeer(Reindeer.Name reindeer){
            JLabel thickPane = this.missionsCheckedPanels.get(reindeer);
            thickPane.setIcon(new ImageIcon(AssetManager.getTick_Checked(20)));
            this.updateUI();
        }
    }

    /**
     * Method that adds the task list (lost reindeers) panel to the conversation
     */
    public void addTaskListPanel(){
         // add a previous separator
        addSeparatorToConversation();
        // create and add the entry panel to the conversation
        this.taskListPanel = new missionList();
        this.ConvPanel.add(this.taskListPanel);
        this.updateUI();
    }
    
    /**
     * Method that sets one lost reindeer as found (checked) in the mission list
     * @pre   The task list must be created before this method is called
     * @param reindeer the found reindeer
     */
    public void addCheckFoundReindeer(Reindeer.Name reindeer){
        ((missionList)this.taskListPanel).checkFoundReindeer(reindeer);
        this.updateUI();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JLayeredPane();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        ConvPanel = new javax.swing.JPanel();

        setMaximumSize(new java.awt.Dimension(360, 360));
        setMinimumSize(new java.awt.Dimension(360, 360));
        setPreferredSize(new java.awt.Dimension(360, 360));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        headerPanel.setBackground(new java.awt.Color(30, 30, 30));
        headerPanel.setAlignmentY(0.0F);
        headerPanel.setMaximumSize(new java.awt.Dimension(360, 50));
        headerPanel.setMinimumSize(new java.awt.Dimension(360, 50));
        headerPanel.setOpaque(true);
        headerPanel.setPreferredSize(new java.awt.Dimension(360, 50));

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        add(headerPanel);
        add(filler1);

        ConvPanel.setBackground(new java.awt.Color(80, 80, 80));
        ConvPanel.setToolTipText("");
        ConvPanel.setAlignmentX(0.5F);
        ConvPanel.setAlignmentY(0.0F);
        ConvPanel.setMaximumSize(new java.awt.Dimension(360, 305));
        ConvPanel.setMinimumSize(new java.awt.Dimension(360, 305));
        ConvPanel.setPreferredSize(new java.awt.Dimension(360, 305));
        ConvPanel.setLayout(new javax.swing.BoxLayout(ConvPanel, javax.swing.BoxLayout.Y_AXIS));
        add(ConvPanel);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]){
        
        JFrame mainW = new JFrame("Ventana Conversaciones");
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainW.setSize(1100,500);
        mainW.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mainW.add(mainPanel);
                
        ConversationPanel SantaPanel = new ConversationPanel("SANTA");
        SantaPanel.setMaximumSize(new Dimension(400,400));
        
        ConversationPanel RudolfPanel = new ConversationPanel("RUDOLF");
        RudolfPanel.setMaximumSize(new Dimension(400,400));
        
        mainPanel.add(SantaPanel);
        mainPanel.add(new Box.Filler(new Dimension(20,0), new Dimension(20,0), new Dimension(20,0)));
        mainPanel.add(RudolfPanel);
        
        SantaPanel.addElfMessage("Hola Santa! he venío pa YATUSABEH!");
        SantaPanel.addReceiverMsg("Ok, pos ya sabes, busca al rudolf...");
        SantaPanel.addTaskListPanel();
        
        RudolfPanel.addElfMessage("Hey Rude! quiero buscar un reno...");
        RudolfPanel.addReceiverMsg("Busca a VIXEN (30,22)");
        RudolfPanel.addElfMessage("Aquí lo tienes");
        RudolfPanel.addReceiverMsg("Busca a BLITZEN (12,39)");
        
        SantaPanel.addCheckFoundReindeer(Reindeer.Name.DANCER);
        SantaPanel.addCheckFoundReindeer(Reindeer.Name.PRANCER);
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainW.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ConvPanel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLayeredPane headerPanel;
    // End of variables declaration//GEN-END:variables
}
