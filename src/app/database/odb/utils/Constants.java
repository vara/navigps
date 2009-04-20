package app.database.odb.utils;

import org.neodatis.odb.ODB;

/**
 *
 * @author praise & vara
 */
public class Constants {

    private final static String PREFIX = "ODB: ";
    public final static String DATABASE_NOT_INITIALIZED = PREFIX+"Data base not initialized !";

    private static ODB dbConnection;

    /**
     * @return the dbConnection
     */
    public static ODB getDbConnection() throws NullPointerException{
        if(dbConnection == null){
            throw new NullPointerException(DATABASE_NOT_INITIALIZED);
        }
        return dbConnection;
    }

    /**
     * @param aDbConnection the dbConnection to set
     */
    public static void setDbConnection(ODB aDbConnection) {
        dbConnection = aDbConnection;
    }
}
