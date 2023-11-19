/*
 * @file
 * @author
 * @version
 */
package gui;

import javax.swing.ImageIcon;
import java.awt.Image;

public class AssetManager {
    
    static private ImageIcon originValid = new ImageIcon("assets/icon_home_valid.png");
    static private ImageIcon originNotValid = new ImageIcon("assets/icon_home_not_valid.png");
    static private ImageIcon targetValid = new ImageIcon("assets/icon_target_valid.png");
    static private ImageIcon targetNotValid = new ImageIcon("assets/icon_target_not_valid.png");
    
    static public ImageIcon getOriginIcon(int dim, boolean valid){
        ImageIcon sourceIcon = originValid;
        if(!valid)      sourceIcon = originNotValid;
        
        return new ImageIcon(sourceIcon.getImage().getScaledInstance(dim,dim, Image.SCALE_DEFAULT ));
    }
    
    static public ImageIcon getTargetIcon(int dim, boolean valid){
        ImageIcon sourceIcon = targetValid;
        if(!valid)      sourceIcon = targetNotValid;
        
        return new ImageIcon(sourceIcon.getImage().getScaledInstance(dim,dim, Image.SCALE_DEFAULT ));
    }
}
