package app.navigps.gui.searchServices;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class Version
{
    private static final String MAJOR_VALUE = "0";
    private static final String MINOR_VALUE = "0";
    private static final String DOT_VALUE = "2";
    private static final String versionNumber = MAJOR_VALUE + "." + MINOR_VALUE + "." + DOT_VALUE;
    private static final String versionName = "Search Services";

    /**
     *
     * @return
     */
    public static String getVersion()
    {
        return versionName + " " + versionNumber;
    }

    /**
     *
     * @return
     */
    public static String getVersionName()
    {
        return versionName;
    }

    /**
     *
     * @return
     */
    public static String getVersionNumber()
    {
        return versionNumber;
    }

    /**
     *
     * @return
     */
    public static String getVersionMajorNumber()
    {
        return MAJOR_VALUE;
    }

    /**
     *
     * @return
     */
    public static String getVersionMinorNumber()
    {
        return MINOR_VALUE;
    }

    /**
     *
     * @return
     */
    public static String getVersionDotNumber()
    {
        return DOT_VALUE;
    }
}
