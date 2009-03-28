/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author vara
 */
public class MyLogger{

    /**
     *
     */
    public static Logger log = Logger.getLogger("navigps");
    private FileHandler fh;
    
    /**
     *
     */
    public MyLogger(){
	
	try {
	    
	    fh = new FileHandler("./navigps.log", true);	   
	    log.addHandler(fh);
	    log.setLevel(Level.ALL);
	    SimpleFormatter formatter = new SimpleFormatter();	    
	    fh.setFormatter(formatter);
	    
	} catch (IOException ex) {
	    ex.printStackTrace();
	} catch (SecurityException ex) {
	    ex.printStackTrace();
	}
	
    }    
}