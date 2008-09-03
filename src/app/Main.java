/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import app.gui.MainWindowIWD;
import app.utils.MyLogger;
import config.MainConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author vara
 */
public class Main {
 
    
    public Main(){	
	MyLogger mlogger = new MyLogger();
	Logger log = mlogger.getLogger();
	log.log(Level.FINE,"test log");
	
    }
    
    public static void main(String[] args) {
        
	MainConfiguration.setMode(true);
	Main.initGui(new Main());
    }

    public static void initGui(final Main m)
    {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {			
		new MainWindowIWD(m);
	    }
	});
    }
    
    public void reload()
    {	
	System.out.println("Reload application ...");
	initGui(this);
    }
}
