/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintStream;

/**
 *
 * @author vara
 */
public class MainConfiguration {
    
    public static final String NAVIGPS_VERSION = "0.5.1";

    private static boolean modeVerbose = false;
    private static boolean modeVerboseGui = false;
    public static final String OS_WINDOWS = "WINDOWS";
    public static final String OS_LINUX = "LINUX";
    public static final String OS_OTHER = "OTHER";
    
    private static String pathToFile = null;
    private static String pathToConfigurationFile = null;
    private static boolean showDocumentProperties = false;
    
    
    public MainConfiguration()
    {}

    public static Dimension getScreenSize()
    {
	return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    public static String getOSName(){
        String osname = System.getProperty("os.name");
        if (osname.toUpperCase().indexOf(MainConfiguration.OS_WINDOWS) != -1) {
            return MainConfiguration.OS_WINDOWS;
        }else if(osname.toUpperCase().indexOf(MainConfiguration.OS_LINUX) != -1){
            return MainConfiguration.OS_LINUX;
        }else
            return MainConfiguration.OS_OTHER;
        }
    
    public static void setMode(boolean verbose){	
        modeVerbose = verbose;
    }
    
    public static synchronized boolean getMode(){
        return modeVerbose;
    }
    
    public static boolean isModeVerboseGui() {
        return modeVerboseGui;
    }

    public static void setModeVerboseGui(boolean aModeVerboseGui) {
        modeVerboseGui = aModeVerboseGui;
    }
    
    public static void setPathChartToFile(String pathToFile) {
        MainConfiguration.pathToFile = pathToFile;
    }

    public static String getPathToConfigurationFile() {
        return pathToConfigurationFile;
    }
    public static String getPathToChartFile() {
        return pathToFile;
    }

    public static void setPathToConfigurationFile(String pathToConfigurationFile) {
        MainConfiguration.pathToConfigurationFile = pathToConfigurationFile;
    }

    public static boolean isShowDocumentProperties() {
        return showDocumentProperties;
    }

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

    public static void printVersion(String str,PrintStream out){
        if(out==null)
            out = System.out;
        out.println(str+MainConfiguration.NAVIGPS_VERSION);
    }
}
