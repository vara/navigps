package app.navigps.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class NaviLogger{

    static{
        new NaviLogger();
    }
    public static Logger log;
    
    public NaviLogger(){	
        try {
            String pathToFile = System.getProperty("user.dir");
            System.out.println(""+pathToFile);
            FileHandler fh = new FileHandler(pathToFile+"/navigps.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            log = Logger.getLogger("app.navigps");            
            log.addHandler(fh);
            log.setLevel(Level.ALL);


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }    
}