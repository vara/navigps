package app.navigps;

/**
 *
 * @author wara
 */
public class Version
{
    private static final String MAJOR_VALUE = "0";
    private static final String MINOR_VALUE = "5";
    private static final String DOT_VALUE = "3";
    private static final String versionNumber = MAJOR_VALUE + "." + MINOR_VALUE + "." + DOT_VALUE;
    private static final String versionName = "NaviGPS infant";

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
