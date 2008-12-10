/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import app.gui.MainWindowIWD;
import app.utils.MyLogger;
import java.util.logging.Level;
import javax.swing.SwingUtilities;

/**
 *
 * @author vara
 */
public class Main {
    
    public final MyLogger logger = new MyLogger();
   
    
    public Main(String [] args){
	
	MyLogger.log.log(Level.FINE,"Start main application");
	new ArgumentsStartUp(args);		
	//MainConfiguration.setModeVerboseGui(true);
    }
    
    public static void main(String[] args) {
        args = new String[]{"-vg","-v","-f","/home/vara/MapWorld.svg"};
	Main app = new Main(args);	
	Main.initGui(app);
    }

    public static void initGui(final Main m)
    {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
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
