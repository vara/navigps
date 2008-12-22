/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author vara
 */
public class GUIConfiguration {   
    private static Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();

    public GUIConfiguration(){
    }

    /**
     * @return the windowSize
     */
    public static Dimension getWindowSize() {
        return windowSize;
    }

    /**
     * @param windowSize the windowSize to set
     */
    public static void setWindowSize(Dimension winSize) {        
        if(winSize.getWidth()!=0 && winSize.getHeight()!=0){
            if(winSize.getWidth()<=getWindowSize().getWidth() &&
               winSize.getHeight()<=getWindowSize().getHeight() ){

                GUIConfiguration.windowSize = winSize;
                return;
            }
            else System.out.println("The root window can not be greater than screen resolution !");

        }else System.out.println("The root window size must be greater than zero !");

        System.out.println("Default window size "+
                (int)getWindowSize().getWidth()+","+(int)getWindowSize().getHeight());
    }
}
