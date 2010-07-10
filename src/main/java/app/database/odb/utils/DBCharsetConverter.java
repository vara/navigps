package app.database.odb.utils;

import app.database.odb.core.Category;
import app.database.odb.core.ServiceCore;
import app.database.odb.core.ServiceDescription;
import app.navigps.utils.StringUtils;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class DBCharsetConverter {


    private static Category convertToDefaultOSCharset(Category cat){
		cat.setName(StringUtils.decode(cat.getName()));
		return cat;
    }
    private static ServiceDescription convertToDefaultOSCharset(ServiceDescription sd){
		sd.setServiceName(StringUtils.decode(sd.getServiceName()));
		sd.setServiceStreet(StringUtils.decode(sd.getServiceStreet()));
		sd.setCity(StringUtils.decode(sd.getCity()));
		sd.setAdditionaInfo(StringUtils.decode(sd.getAdditionaInfo()));
		convertToDefaultOSCharset(sd.getCategory());
		return sd;
    }    
    
    private static ServiceCore convertToDefaultOSCharset(ServiceCore sc){
		convertToDefaultOSCharset(sc.getServiceDescription());
		return sc;
    }

	public static Object convertToDefaultOSChar(Object obj){
		if(obj instanceof ServiceCore){
			System.out.println("Convert to "+StringUtils.CURRENT_ENCODING+" object ServiceCore");
			return convertToDefaultOSCharset((ServiceCore)obj);
		}else if(obj instanceof ServiceDescription){
			System.out.println("Convert to "+StringUtils.CURRENT_ENCODING+" object ServiceDescription");
			return convertToDefaultOSCharset((ServiceDescription)obj);
		}else if(obj instanceof Category){
			System.out.println("Convert to "+StringUtils.CURRENT_ENCODING+" object Category");
			return convertToDefaultOSCharset((Category)obj);
		}else{
			System.out.println("** Not recognized object '"+obj+"'**");
		}
		return obj;
	}

    //convert objects to utf-8
    
    private static Category convertToUTF8(Category cat){	
		cat.setName(StringUtils.encode(cat.getName()));
		return cat;
    }   
    
    private static ServiceDescription convertToUTF8(ServiceDescription sd){	
		sd.setServiceName(StringUtils.encode(sd.getServiceName()));
		sd.setServiceStreet(StringUtils.encode(sd.getServiceStreet()));
		sd.setCity(StringUtils.encode(sd.getCity()));
		sd.setAdditionaInfo(StringUtils.encode(sd.getAdditionaInfo()));
		sd.setServiceNumber(StringUtils.encode(sd.getServiceNumber()));
		convertToUTF8(sd.getCategory());
		return sd;
    }    
    
    private static ServiceCore convertToUTF8(ServiceCore sc){
		convertToUTF8(sc.getServiceDescription());
		return sc;
    }
    
    public static Object convertToUTF8(Object obj){
		if(obj instanceof ServiceCore){
			System.out.println("Convert to UTF-8 object ServiceCore");
			return convertToUTF8((ServiceCore)obj);
		}else if(obj instanceof ServiceDescription){
			System.out.println("Convert to UTF-8 object ServiceDescription");
			return convertToUTF8((ServiceDescription)obj);
		}else if(obj instanceof Category){
			System.out.println("Convert to UTF-8 object Category");
			return convertToUTF8((Category)obj);
		}else{
			System.out.println("** Not recognized object '"+obj+"'**");
		}
		return obj;
    }
}
