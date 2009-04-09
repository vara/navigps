package app.navigps.utils;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class StringUtils {

    private static String defaultEncoding = "UTF-8";

    public static final String CURRENT_ENCODING = System.getProperty("file.encoding");
    
    private static boolean doConvert = !isTheSame();
    
    private static boolean isTheSame(){
	return CURRENT_ENCODING.equals(getDefaultEncoding());
    }
	    
    public static String encode(String str){
        if(str == null){
            return ""; //I dont know is it good idea
        }
        if(doConvert){
            System.err.println("convert to "+getDefaultEncoding());
            String retStr = str;
            try {
            retStr = new String(str.getBytes(getDefaultEncoding()),CURRENT_ENCODING);
            System.out.println(""+str+" -> "+retStr);
            } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NaviLogger.LOGGER_NAME).log(Level.SEVERE, null, ex);
            System.err.println(""+ex);
            }
            return retStr;
        }else{
            System.err.println("not need convert to UTF8");
        }
        return str;
    }
    
    public static String decode(String str){
        if(str==null){
            return "";
        }
        if(doConvert){
            String retStr = str;
            System.out.println("convert to "+CURRENT_ENCODING);
            try {
            retStr = new String(str.getBytes(CURRENT_ENCODING),getDefaultEncoding());
            System.out.println(""+str+" -> "+retStr);
            } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NaviLogger.LOGGER_NAME).log(Level.SEVERE, null, ex);
            System.err.println(""+ex);
            }
            return retStr;
        }else{
            System.err.println("not need convert to "+CURRENT_ENCODING);
        }
        return str;
    }

    /**
     * @return the defaultEncoding
     */
    public static String getDefaultEncoding() {
        return new String(defaultEncoding);
    }

    /**
     * @param aDefaultEncoding the defaultEncoding to set
     */
    public static void setDefaultEncoding(String aDefaultEncoding) throws UnsupportedEncodingException {
        "test".getBytes(aDefaultEncoding);
        defaultEncoding = aDefaultEncoding;
        doConvert = !isTheSame();
    }
}
