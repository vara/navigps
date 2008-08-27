/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import app.gui.MainWindow;
import javax.swing.SwingUtilities;

/**
 *
 * @author vara
 */
public class Main {
 
    
    public Main(){	
	
    }
    
    public static void main(String[] args) {
        
	
	Main.initGui(new Main());
    }

    public static void initGui(final Main m)
    {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {		
		new MainWindow(m);		
	    }
	});
    }
    
    public void reload()
    {	
	System.out.println("Reload application ...");
	initGui(this);
    }
}
