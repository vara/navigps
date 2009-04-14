package app.navigps.gui.detailspanel;

import app.navigps.utils.GraphicsUtilities;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class RoundWindowUtils {

    /**
     *
     * @param container
     * @return
     */
    public static Container getRoundWindowFromContainer(Container container){
        SearchRoundWindow.search(container);
        return SearchRoundWindow.getRoundWindow();
    }
    
    private static class SearchRoundWindow{
        private static Container roundWindow = null;
        private static boolean done = false;

        private static void search(Container container){
            Component [] child = container.getComponents();
            for (Component component : child) {
                if(done)
                    break;
                if(component instanceof Container){
                    if(component instanceof RoundWindow){                        
                        setRoundWindow((Container)component);
                        done = true;
                        break;
                    }
                    search((Container)component);
                }
            }
        }
        private static void setRoundWindow(Container cont){
            roundWindow = cont;
        }
        private static Container getRoundWindow(){
            Container retVal = roundWindow;
            roundWindow = null;
            done = false;
            return retVal;
        }
    }

    public static ImageIcon getIcons(String name,int size){
        BufferedImage bi = loadThumbnailIcon("resources/graphics/icons/", name, size, true);
        if(bi != null){
            return new ImageIcon(bi);
        }
        return null;
    }

    protected static BufferedImage loadThumbnailIcon(String path,String name,int size,boolean verb){
        try {

            URL href = RoundWindow.class.getResource(path+name);
            BufferedImage bi = GraphicsUtilities.loadCompatibleImage(href);
            return GraphicsUtilities.createThumbnail(bi,size);

        } catch (Exception ex) {
            if(verb){
                System.err.println(""+ex);
            }
        }
        return null;
    }
}
