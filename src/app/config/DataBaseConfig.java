package app.config;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class DataBaseConfig {
    private static String defaultDatabasePath = "./resources/odb/";
    private static String databaseFilename = "neodatis.odb";
    private static String iconPath = "resources/graphics/icons/";

    private static String defaultDatabaseCharsEncoding = "UTF-8";

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

    /**
     * @return the defaultDatabaseCharsEncoding
     */
    public static String getDefaultDatabaseCharsEncoding() {
        return defaultDatabaseCharsEncoding;
    }

    /**
     * @param aDefaultDatabaseCharsEncoding the defaultDatabaseCharsEncoding to set
     */
    public static void setDefaultDatabaseCharsEncoding(String aDefaultDatabaseCharsEncoding) throws UnsupportedEncodingException {
        "test".getBytes(aDefaultDatabaseCharsEncoding);
        defaultDatabaseCharsEncoding = aDefaultDatabaseCharsEncoding;
    }
}
