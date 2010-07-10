package app.config;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class GUIConfiguration {   
    
    private static Dimension defaultWindowSize = Toolkit.getDefaultToolkit().getScreenSize();

    private static String defaultGuiCharsEncoding = System.getProperty("file.encoding");

    /**
     *
     */
    public static final byte FULL_SCREEN = 0;
    /**
     *
     */
    public static final byte FRAME_SCREEN = 1;

    private static byte modeScreen = FRAME_SCREEN;

    /**
     * @return the defaultGuiCharsEncoding
     */
    public static String getDefaultGuiCharsEncoding() {
        return defaultGuiCharsEncoding;
    }

    /**
     * @param aDefaultGuiCharsEncoding the defaultGuiCharsEncoding to set
     */
    public static void setDefaultGuiCharsEncoding(String aDefaultGuiCharsEncoding) throws UnsupportedEncodingException {
        "test".getBytes(aDefaultGuiCharsEncoding);
        defaultGuiCharsEncoding = aDefaultGuiCharsEncoding;
    }

    /**
     *
     */
    public GUIConfiguration(){
    }

    /**
     * @return the modeScreen
     */
    public static byte getModeScreen() {
        return modeScreen;
    }

    /**
     * @param aModeScreen the modeScreen to set
     */
    public static void setModeScreen(byte aModeScreen) {
        modeScreen = aModeScreen;
    }
    /**
     * @return the windowSize
     */
    public static Dimension getWindowSize() {
        return defaultWindowSize;
    }

    /**
     * @param winSize
     */
    public static void setWindowSize(Dimension winSize) {        
        if(winSize.getWidth()!=0 && winSize.getHeight()!=0){
            if(winSize.getWidth()<=getWindowSize().getWidth() &&
               winSize.getHeight()<=getWindowSize().getHeight() ){

                GUIConfiguration.defaultWindowSize = winSize;
                return;
            }
            else System.out.println("The root window can not be greater than screen resolution !");

        }else System.out.println("The root window size must be greater than zero !");

        System.out.println("Default window size "+
                (int)getWindowSize().getWidth()+","+(int)getWindowSize().getHeight());
    }

    /**
     *
     * @return
     */
    public static GraphicsDevice getGraphicDevice(){
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }
}
