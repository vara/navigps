package odb.utils;

import odb.gui.DatabaseManager;
import org.neodatis.odb.ODB;

/**
 *
 * @author praise
 */
public class Constants {

    private static ODB dbConnection;
    private static DatabaseManager managerWindow;

    /**
     * @return the dbConnection
     */
    public static ODB getDbConnection() {
        return dbConnection;
    }

    /**
     * @param aDbConnection the dbConnection to set
     */
    public static void setDbConnection(ODB aDbConnection) {
        dbConnection = aDbConnection;
    }

    /**
     * @return the managerWindow
     */
    public static DatabaseManager getManagerWindow() {
        return managerWindow;
    }

    /**
     * @param aManagerWindow the managerWindow to set
     */
    public static void setManagerWindow(DatabaseManager aManagerWindow) {
        managerWindow = aManagerWindow;
    }
}
