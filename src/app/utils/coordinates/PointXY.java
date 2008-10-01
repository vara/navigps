/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils.coordinates;

/**
 *
 * @author vara
 */
       //ddmm.sss   dddmm.sss
//$GPGLL,3907.360,N,12102.481,W,183730,A*33

public class PointXY {

    private double latitude;
    private double longitude;
    
    public PointXY(double coordX,double coordY){
	latitude = coordX;
	longitude = coordY;
    }
    
    public static double convertDMSToDecimalDegree(int deg,int min,double sec){
	return ( ((min*60)+sec)/3600 )+deg;
    }
    
    public static PointXY convertDMSToDecimalDegree(int deg,int min,double sec,
						    int deg2,int min2,double sec2){
	
	return new PointXY(PointXY.convertDMSToDecimalDegree(deg,min,sec),
			   PointXY.convertDMSToDecimalDegree(deg2,min2,sec2));
    }
    
    public static String convertDecimalDegreeToDMS(double ddms){
	int degree = (int)ddms;	
	double tmpmin = (ddms - (int)ddms)*60;
	double sec = (tmpmin-(int)(tmpmin))*60;
	return degree+","+(int)(tmpmin)+","+sec;
    }
    
    public static String convertDecimalDegreeToDMS(double ddmsLat,double ddmsLon){
	
	return "latitude: "+PointXY.convertDecimalDegreeToDMS(ddmsLat)+" ; "+
	       "longitude: "+PointXY.convertDecimalDegreeToDMS(ddmsLon);
    }
    
    public static double convertNmeaDMSToToDecimalDegree(double ddms){
	int deg = (int)ddms/100;
	return deg + ( ddms-(deg*100) )/60;	
    }
    
    public static PointXY convertNmeaDMSToToDecimalDegree(double ddmsLat,double ddmsLon){
	return new PointXY(convertNmeaDMSToToDecimalDegree(ddmsLat),
			   convertNmeaDMSToToDecimalDegree(ddmsLon));
    }
}
