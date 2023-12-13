/*
 * @file
 * @author
 * @version
 */
package gui;

import components.Tile;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AssetManager {
    
    static final Color COL_FREE = Color.decode("#68BBE3");
    static final Color COL_WALL = Color.decode("#003060");
    static final Color COL_UNKNOWN = Color.decode("#CAD1D9");
    
    static final Color COL_MIND_BASE = Color.decode("#2B2B2B");
    static final Color COL_MIND_FREE = Color.decode("#EFEDF0");
    static final Color COL_MIND_WALL = Color.decode("#9c4d3d");
    static final Color COL_MIND_UNKN = Color.decode("#898989");
    
    static final Color CONVERSATION_HEADER_BG = Color.decode("#A3A3A3");
    static final Color CONVERSATION_SCREEN_BG = Color.decode("#707070");
            
    static private final ImageIcon originValid = new ImageIcon("assets/icon_home_valid.png");
    static private final ImageIcon originNotValid = new ImageIcon("assets/icon_home_not_valid.png");
    static private final ImageIcon targetValid = new ImageIcon("assets/icon_target_valid.png");
    static private final ImageIcon targetNotValid = new ImageIcon("assets/icon_target_not_valid.png");
    static private final ImageIcon pastAgent = new ImageIcon("assets/icon_agent_past.png");
    static private final ImageIcon currentAgent = new ImageIcon("assets/icon_agent_current.png");
    
    
    // ICONS FOR THE P3 UI
    static private final ImageIcon ELF_AGENT = new ImageIcon( "assets/p3-elf-agent.png");
    static private final ImageIcon ELF_AVATAR = new ImageIcon( "assets/p3-elf-agent-avatar.png");
    static private final ImageIcon RUDOLF_AVATAR = new ImageIcon( "assets/p3-rudolf.png");
    static private final ImageIcon SANTA_AVATAR = new ImageIcon( "assets/p3-santa.png");
    
    
    static ImageIcon getOriginIcon(int dim, boolean valid){
        ImageIcon sourceIcon = originValid;
        if(!valid)      sourceIcon = originNotValid;
        
        return new ImageIcon(sourceIcon.getImage().getScaledInstance(dim,dim, Image.SCALE_DEFAULT ));
    }
    
    static ImageIcon getTargetIcon(int dim, boolean valid){
        ImageIcon sourceIcon = targetValid;
        if(!valid)      sourceIcon = targetNotValid;
        
        return new ImageIcon(sourceIcon.getImage().getScaledInstance(dim,dim, Image.SCALE_DEFAULT ));
    }
    
    static ImageIcon getAgentPastIcon(int dim){
        return new ImageIcon(pastAgent.getImage().getScaledInstance(dim, dim, Image.SCALE_DEFAULT ));
    }
    
    static ImageIcon getAgentCurrentIcon(int dim){
        return new ImageIcon(currentAgent.getImage().getScaledInstance(dim, dim, Image.SCALE_DEFAULT ));
    }
    
    static JLabel getAgentPastLbl(int dim){
        JLabel lbl = new JLabel(getAgentPastIcon(dim));
        lbl.setBounds(0,0,dim,dim);
        return lbl;
    }
    
    static Image getSantaAvatar(int dim){
        return SANTA_AVATAR.getImage().getScaledInstance(dim, dim, Image.SCALE_DEFAULT);
    }
    
    static Image getRudolfAvatar(int dim){
        return RUDOLF_AVATAR.getImage().getScaledInstance(dim, dim, Image.SCALE_DEFAULT);
    }

    static Image getElfAvatar(int dim){
        return ELF_AVATAR.getImage().getScaledInstance(dim, dim, Image.SCALE_DEFAULT);
    }
    
    static JPanel getTilePanel(Tile.Type type){

        JPanel panelTile = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        switch (type) {
                    case EMPTY: 
                        panelTile.setBackground(COL_FREE);
                        break;
                    case UNREACHABLE:
                        panelTile.setBackground(COL_WALL);
                        break;
                    case UNKNOWN:
                        panelTile.setBackground(COL_UNKNOWN);
                        break;
                }
        return panelTile;
    }
    
    static JPanel getMentalTilePanel(Tile.Type type){
        JPanel panelTile = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        switch (type) {
                    case EMPTY: 
                        panelTile.setBackground(COL_MIND_FREE);
                        break;
                    case UNREACHABLE:
                        panelTile.setBackground(COL_MIND_WALL);
                        break;
                    case UNKNOWN:
                        panelTile.setBackground(COL_MIND_UNKN);
                        break;
                    default:
                        panelTile.setBackground(COL_MIND_BASE);
                        break;
                }
        return panelTile;
    }
    
}
