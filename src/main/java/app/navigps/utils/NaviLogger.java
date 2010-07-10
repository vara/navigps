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

    public static final String LOGGER_NAME = "app.navigps";

    static{
        new NaviLogger();
    }
    public static Logger logger;
    
    public NaviLogger(){	
        try {

            String pathToFile = System.getProperty("user.dir");
            //System.out.println(""+pathToFile);
            FileHandler fh = new FileHandler(pathToFile+"/navigps.log", false);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger = Logger.getLogger(LOGGER_NAME);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }    
}