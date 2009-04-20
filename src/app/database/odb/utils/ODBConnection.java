/*
 * ODBConnection.java
 *
 * Created on 2009-03-31, 19:09:34
 */

package app.database.odb.utils;

import app.config.DataBaseConfig;
import app.navigps.utils.NaviLogger;
import app.navigps.utils.StringUtils;
import java.io.File;
import java.util.LinkedList;
import java.util.logging.Level;
import org.neodatis.odb.Configuration;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class ODBConnection {

    public static final String PREFIX = "ODB: ";
    public static final String CONNECTING_INFO = PREFIX+"connecting ...";
    public static final String CONNECTED_INFO = PREFIX+"connected ! ";

    public static final String DISCONNECTING_INFO = PREFIX+"disconnecting ... ";
    public static final String DISCONNECTED_INFO = PREFIX+"disconnected ! ";

    public static final String ERROR_CONNECT = PREFIX+"can not connect ";
    public static final String ERROR_DISCONNECT = PREFIX+"not connected ";

    public static final String FILE_NOT_EXIST = PREFIX+"file doesn't exist ";
    public static final String READ_FILE_PROBLEM = PREFIX+"file doesn't exist ";
    public static final String ERROR_PATH = PREFIX+"problem with path to file";

    private LinkedList<ConnectionListener> listenerConnetion =
            new LinkedList<ConnectionListener>();

    private static ODBConnection instance = new ODBConnection();
    
    /*
     * @return String containing message of connection
     */
    public static String connect(String databasePath, String fileName) {

        String status = "";
        
        if (!checkFile(databasePath, fileName)) {
            fileName =  DataBaseConfig.getDatabaseFilename();
            databasePath = DataBaseConfig.getDefaultDatabasePath();
        }

        if(isConnected())
            disconnect();

        String msg = "[ DB name '"+fileName+"']";
        try {

            System.out.println(CONNECTING_INFO);
            NaviLogger.logger.log(Level.FINE, CONNECTING_INFO);

            //for test set default chars encoding
            Configuration.setDatabaseCharacterEncoding(
                    DataBaseConfig.getDefaultDatabaseCharsEncoding());            

            ODB odb = ODBFactory.open(databasePath+fileName);
            Constants.setDbConnection(odb);
            //notify all object
            getInstance().notifyAllObjOfConnection();

            status = CONNECTED_INFO+msg;
            NaviLogger.logger.log(Level.FINE,status);
            
        } catch (Exception ex) {
            status = ERROR_CONNECT+msg;
            NaviLogger.logger.log(Level.WARNING,status,ex);
        }
        return status;
    }

    public static String disconnect(){

        NaviLogger.logger.log(Level.FINE,DISCONNECTING_INFO);

        if(!isConnected()){
            NaviLogger.logger.log(Level.FINE,ERROR_DISCONNECT);
            return ERROR_DISCONNECT;
        }

        Constants.getDbConnection().close();
        //important ! dispose instance in bridge connetion
        Constants.setDbConnection(null);
        //notify all object
        getInstance().notifyAllObjOfDisconnection();


        NaviLogger.logger.log(Level.FINE,DISCONNECTED_INFO);
        return DISCONNECTED_INFO;
    }

    public static boolean isConnected(){
        try{
            Constants.getDbConnection();
        }catch(NullPointerException e){
            return false;
        }
        return true;
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

    public static ODBConnection getInstance(){
        return instance;
    }

    private void notifyAllObjOfConnection(){
        for (ConnectionListener cl : listenerConnetion) {
            cl.connectionOpened();
        }
    }

    private void notifyAllObjOfDisconnection(){
        for (ConnectionListener cl : listenerConnetion) {
            cl.connectionClosed();
        }
    }

    public void addConnectionListener(ConnectionListener cl){
        listenerConnetion.add(cl);
    }

    public void removeConnectionListener(ConnectionListener cl){
        listenerConnetion.remove(cl);
    }

    public void removeAlllisteners(){
        listenerConnetion.clear();
    }
}
