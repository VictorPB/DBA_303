/*
 * @file
 * @author
 * @version
 */
package gui;

import components.Tile;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JPanel;

public class AssetManager {
    
    static final Color COL_FREE = Color.decode("#68BBE3");
    static final Color COL_WALL = Color.decode("#003060");
    static final Color COL_UNKNOWN = Color.decode("#CAD1D9");
    
    static private final ImageIcon originValid = new ImageIcon("assets/icon_home_valid.png");
    static private final ImageIcon originNotValid = new ImageIcon("assets/icon_home_not_valid.png");
    static private final ImageIcon targetValid = new ImageIcon("assets/icon_target_valid.png");
    static private final ImageIcon targetNotValid = new ImageIcon("assets/icon_target_not_valid.png");
    
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
    
    static JPanel getPreviewTilePanel(Tile tile){
        JPanel panelTile = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        switch (tile.getType()) {
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
}
