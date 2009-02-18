package app;

/**
 *
 * @author wara
 */
public class Version
{
    private static final String MAJOR_VALUE = "0";
    private static final String MINOR_VALUE = "5";
    private static final String DOT_VALUE = "1";
    private static final String versionNumber = MAJOR_VALUE + "." + MINOR_VALUE + "." + DOT_VALUE;
    private static final String versionName = "NaviGPS infant";

    public static String getVersion()
    {
        return versionName + " " + versionNumber;
    }

    public static String getVersionName()
    {
        return versionName;
    }

    public static String getVersionNumber()
    {
        return versionNumber;
    }

    public static String getVersionMajorNumber()
    {
        return MAJOR_VALUE;
    }

    public static String getVersionMinorNumber()
    {
        return MINOR_VALUE;
    }

    public static String getVersionDotNumber()
    {
        return DOT_VALUE;
    }
}
