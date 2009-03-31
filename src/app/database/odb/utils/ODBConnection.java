/*
 * ODBConnection.java
 *
 * Created on 2009-03-31, 19:09:34
 */

package app.database.odb.utils;

import app.config.DataBaseConfig;
import app.navigps.utils.NaviLogger;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class ODBConnection {

    public static final String PREFIX = "ODB:";
    public static final String CONNECTING_INFO = PREFIX+" connecting ...";
    public static final String CONNECTED_INFO = PREFIX+" connected ! ";

    public static final String DISCONNECTING_INFO = PREFIX+" disconnecting ... ";
    public static final String DISCONNECTED_INFO = PREFIX+" disconnected ! ";

    public static final String ERROR_CONNECT = PREFIX+" can not connect ";
    public static final String ERROR_DISCONNECT = PREFIX+" not connected ";

    public static final String FILE_NOT_EXIST = PREFIX+" file doesn't exist ";
    public static final String READ_FILE_PROBLEM = PREFIX+" file doesn't exist ";
    public static final String ERROR_PATH = PREFIX+" problem with path to file";

    /*
     * @return String containing message of connection
     */
    public static String connect(String databasePath, String fileName) {

        String status = "";
        
        if (!checkFile(databasePath, fileName)) {
            fileName =  DataBaseConfig.getDatabaseFilename();
            databasePath = DataBaseConfig.getDefaultDatabasePath();
        }

        String msg = "[ DB name '"+fileName+"']";
        try {

            System.out.println(CONNECTING_INFO);
            NaviLogger.log.log(Level.FINE, CONNECTING_INFO);

            ODB odb = ODBFactory.open(databasePath+fileName);
            Constants.setDbConnection(odb);
            status = CONNECTED_INFO+msg;
            NaviLogger.log.log(Level.FINE,status);
            
        } catch (Exception ex) {
            status = ERROR_CONNECT+msg;
            NaviLogger.log.log(Level.WARNING,status,ex);            
        }
        return status;
    }

    public static String disconnect(){

        NaviLogger.log.log(Level.FINE,DISCONNECTING_INFO);

        if(!isConnected()){
            NaviLogger.log.log(Level.FINE,ERROR_DISCONNECT);
            return ERROR_DISCONNECT;
        }

        Constants.getDbConnection().close();

        NaviLogger.log.log(Level.FINE,DISCONNECTED_INFO);
        return DISCONNECTED_INFO;
    }

    public static boolean isConnected(){
        return Constants.getDbConnection() != null ? true : false;
    }

    protected static boolean checkFile(String databasePath, String fileName){
        //if data base will be to operate file from user (map) space then
        //can uncomment verbose info
        if ((databasePath == null && databasePath.equalsIgnoreCase(""))&&
                (fileName == null && fileName.equalsIgnoreCase(""))) {
            //System.err.println(ERROR_PATH+fileName);
            //NaviLogger.log.log(Level.WARNING,ERROR_PATH+fileName);
            return false;
        }

        File file = new File(databasePath+fileName);
        if(!file.exists()){
            //System.err.println(FILE_NOT_EXIST+fileName);
            //NaviLogger.log.log(Level.WARNING,FILE_NOT_EXIST+fileName);
            return false;
        }
        if(!file.canRead()){
            //System.err.println(READ_FILE_PROBLEM+fileName);
            //NaviLogger.log.log(Level.WARNING,READ_FILE_PROBLEM+fileName);
            return false;
        }
        return true;
    }
}
