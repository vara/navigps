package app.gui.detailspanel;

import java.awt.Component;
import java.awt.Container;

/**
 *
 * @author wara
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
}
