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
public class Configuration {
    private static boolean modeVerbose = false;
    public static final String OS_WINDOWS = "WINDOWS";
    public static final String OS_LINUX = "LINUX";
    public static final String OS_OTHER = "OTHER";
    
    public Configuration()
    {}

    public static Dimension getScreenSize()
    {
	return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    public static String getOSName()
    {
	String osname = System.getProperty("os.name");
	if (osname.toUpperCase().indexOf(Configuration.OS_WINDOWS) != -1) {	
	    return Configuration.OS_WINDOWS;
	}else if(osname.toUpperCase().indexOf(Configuration.OS_LINUX) != -1){
	    return Configuration.OS_LINUX;
	}else
	    return Configuration.OS_OTHER;
    }
    
    public static void setMode(boolean verbose)
    {
	if(verbose)
	   System.out.println("Mode Set loudly ! aaaaaa");
	else
	    System.out.println("set to silent mode");
	
	modeVerbose = verbose;
    }
    public static synchronized boolean getMode()
    {
	return modeVerbose;
    }
}
