/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import app.gui.MainWindowIWD;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author wara
 */
public class ImageList {
    private static HashMap <String,ImageIcon> icons = new HashMap<String,ImageIcon>();

    public static ImageIcon getIcon(String name,boolean verb){

        ImageIcon img = icons.get(name);
        if(img == null){
            ImageIcon tmpIco = loadThumbnailIcon(name, 16,verb);
            if(tmpIco != null){
                icons.put(name, tmpIco);
                return tmpIco;
            }
            return null;
        }
        return img;
    }

    protected static ImageIcon loadThumbnailIcon(String name,int size,boolean verb){
        try {
            
            URL href = MainWindowIWD.createNavigationIconPath("test/"+name,"png",verb);
            BufferedImage bi = GraphicsUtilities.loadCompatibleImage(href);
            return new ImageIcon(GraphicsUtilities.createThumbnail(bi,size));

        } catch (Exception ex) {}
        return null;
    }
}
