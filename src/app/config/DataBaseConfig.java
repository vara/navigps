/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.config;

/**
 *
 * @author wara
 */
public class DataBaseConfig {
    private static String defaultDatabasePath = "./resources/odb/";
    private static String databaseFilename = "neodatis.odb";
    private static String iconPath = "resources/graphics/icons/";

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

    /**
     * @return the iconPath
     */
    public static String getIconPath() {
        return iconPath;
    }

    /**
     * @param aIconPath the iconPath to set
     */
    public static void setIconPath(String aIconPath) {
        iconPath = aIconPath;
    }
}
