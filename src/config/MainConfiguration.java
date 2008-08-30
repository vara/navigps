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
public class MainConfiguration {
    private static boolean modeVerbose = false;
    public static final String OS_WINDOWS = "WINDOWS";
    public static final String OS_LINUX = "LINUX";
    public static final String OS_OTHER = "OTHER";
    
    public MainConfiguration()
    {}

    public static Dimension getScreenSize()
    {
	return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    public static String getOSName()
    {
	String osname = System.getProperty("os.name");
	if (osname.toUpperCase().indexOf(MainConfiguration.OS_WINDOWS) != -1) {	
	    return MainConfiguration.OS_WINDOWS;
	}else if(osname.toUpperCase().indexOf(MainConfiguration.OS_LINUX) != -1){
	    return MainConfiguration.OS_LINUX;
	}else
	    return MainConfiguration.OS_OTHER;
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
