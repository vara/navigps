/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config;

/**
 *
 * @author wara
 */
public class DataBaseConfig {
    private static String defaultDatabasePath = "./resources/odb/";
    private static String databaseFilename = "neodatis.odb";

    /**
     * @return the defaultDatabasePath
     */
    public static String getDefaultDatabasePath() {
        return defaultDatabasePath;
    }

    /**
     * @param aDefaultDatabasePath the defaultDatabasePath to set
     */
    public static void setDefaultDatabasePath(String aDefaultDatabasePath) {
        defaultDatabasePath = aDefaultDatabasePath;
    }

    /**
     * @return the databaseFilename
     */
    public static String getDatabaseFilename() {
        return databaseFilename;
    }

    /**
     * @param aDatabaseFilename the databaseFilename to set
     */
    public static void setDatabaseFilename(String aDatabaseFilename) {
        databaseFilename = aDatabaseFilename;
    }
}
