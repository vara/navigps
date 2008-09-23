/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import app.gui.MainWindowIWD;
import app.utils.MyLogger;
import app.utils.Utils;
import config.MainConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author vara
 */
public class Main {
    
    public final MyLogger logger = new MyLogger();
    public Main(){
	
	MyLogger.log.log(Level.FINE,"start application");	
    }
    
    public static void main(String[] args) {
        
	MainConfiguration.setMode(true);
	//MainConfiguration.setMode(false);
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
	initGui(this);
    }
}
