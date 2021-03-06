package app.config;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class MainConfiguration {

    private static boolean modeVerbose = false;
    private static boolean modeVerboseGui = false;
    /**
     *
     */
    public static final String OS_WINDOWS = "WINDOWS";
    /**
     *
     */
    public static final String OS_LINUX = "LINUX";
    /**
     *
     */
    public static final String OS_OTHER = "OTHER";
    
    private static String pathToFile = null;
    private static String pathToConfigurationFile = null;
    private static boolean showDocumentProperties = false;
    
    
    /**
     *
     */
    public MainConfiguration()
    {}

    /**
     *
     * @return
     */
    public static Dimension getScreenSize()
    {
	return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    /**
     *
     * @return
     */
    public static String getOSName(){
        String osname = System.getProperty("os.name");
        if (osname.toUpperCase().indexOf(MainConfiguration.OS_WINDOWS) != -1) {
            return MainConfiguration.OS_WINDOWS;
        }else if(osname.toUpperCase().indexOf(MainConfiguration.OS_LINUX) != -1){
            return MainConfiguration.OS_LINUX;
        }else
            return MainConfiguration.OS_OTHER;
        }
    
    /**
     *
     * @param verbose
     */
    public static void setModeVerboseConsole(boolean verbose){
        modeVerbose = verbose;
    }
    
    /**
     *
     * @return
     */
    public static synchronized boolean getMode(){
        return modeVerbose;
    }
    
    /**
     *
     * @return
     */
    public static boolean isModeVerboseGui() {
        return modeVerboseGui;
    }

    /**
     *
     * @param aModeVerboseGui
     */
    public static void setModeVerboseGui(boolean aModeVerboseGui) {
        modeVerboseGui = aModeVerboseGui;
    }
    
    /**
     *
     * @param pathToFile
     */
    public static void setPathChartToFile(String pathToFile) {
        MainConfiguration.pathToFile = pathToFile;
    }

    /**
     *
     * @return
     */
    public static String getPathToConfigurationFile() {
        return pathToConfigurationFile;
    }
    /**
     *
     * @return
     */
    public static String getPathToChartFile() {
        return pathToFile;
    }

    /**
     *
     * @param pathToConfigurationFile
     */
    public static void setPathToConfigurationFile(String pathToConfigurationFile) {
        MainConfiguration.pathToConfigurationFile = pathToConfigurationFile;
    }

    /**
     *
     * @return
     */
    public static boolean isShowDocumentProperties() {
        return showDocumentProperties;
    }

    /**
     *
     * @param showDocumentProperties
     */
    public static void setShowDocumentProperties(boolean showDocumentProperties) {
        MainConfiguration.showDocumentProperties = showDocumentProperties;
    }
    
    @Override
    public String toString(){
        String ret="";

        if(MainConfiguration.getMode())
           ret = "Mode set loud ! aaaaaa";
        else
            ret = "Set to silent mode";
        ret+="\nSystem Operation "+getOSName();
        if(pathToFile != null);
            ret+="\nset Path to svg document "+pathToFile;
        if(pathToConfigurationFile!=null)
            ret+="\nset Path to configuration file "+pathToConfigurationFile;
        ret+="\nShow Document Properties "+showDocumentProperties;

        return ret;
    }
}
