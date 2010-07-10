/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.searchServices.swing;

import app.navigps.utils.GraphicsUtilities;
import app.navigps.gui.NaviRootWindow;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author wara
 */
public class ImageListForServices {

    private static HashMap <String,ImageIcon> icons = new HashMap<String,ImageIcon>();

    public static ImageIcon getIcon(String name,boolean verb){

        ImageIcon img = icons.get(name);
        if(img == null){
            BufferedImage buffIco = loadThumbnailIcon(name, 16,verb);
            if(buffIco != null){
                ImageIcon imIco = new ImageIcon(buffIco);
                icons.put(name, imIco);
                return imIco;
            }
            return null;
        }
        return img;
    }

    protected static BufferedImage loadThumbnailIcon(String name,int size,boolean verb){
        try {
            
            URL href = NaviRootWindow.createNavigationIconPath("services/"+name,"png",verb);
            BufferedImage bi = GraphicsUtilities.loadCompatibleImage(href);
            return GraphicsUtilities.createThumbnail(bi,size);

        } catch (Exception ex) {}
        return null;
    }
}
