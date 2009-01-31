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
}
