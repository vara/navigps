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
     
    
    /**
     *
     * @param coordX
     * @param coordY
     */
    public PointXY(double coordX,double coordY){
	latitude = coordX;
	longitude = coordY;
    }       
    
    /**
     *
     * @return
     */
    public double getLatitude() {
	return latitude;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(double latitude) {
	this.latitude = latitude;
    }

    /**
     *
     * @return
     */
    public double getLongitude() {
	return longitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(double longitude) {
	this.longitude = longitude;
    }
    
    @Override
    public String toString(){
	return "Latitude : "+getLatitude()+" Longitude : "+getLongitude();
    }
    
    /**
     *
     */
    public static class Conversion{
	
        /**
         *
         * @param deg
         * @param min
         * @param sec
         * @return
         */
        public static double convertDMSToDecimalDegree(int deg,int min,double sec){
	    return ( ((min*60)+sec)/3600 )+deg;
	}

    /**
     *
     * @param deg
     * @param min
     * @param sec
     * @param deg2
     * @param min2
     * @param sec2
     * @return
     */
    public static PointXY convertDMSToDecimalDegree(int deg,int min,double sec,
							int deg2,int min2,double sec2){

	    return new PointXY(PointXY.Conversion.convertDMSToDecimalDegree(deg,min,sec),
			       PointXY.Conversion.convertDMSToDecimalDegree(deg2,min2,sec2));
	}

    /**
     *
     * @param ddms
     * @return
     */
    public static String convertDecimalDegreeToDMS(double ddms){
	    int degree = (int)ddms;	
	    double tmpmin = (ddms - (int)ddms)*60;
	    double sec = (tmpmin-(int)(tmpmin))*60;
	    return degree+","+(int)(tmpmin)+","+sec;
	}

    /**
     *
     * @param ddmsLat
     * @param ddmsLon
     * @return
     */
    public static String convertDecimalDegreeToDMS(double ddmsLat,double ddmsLon){

	    return "latitude: "+PointXY.Conversion.convertDecimalDegreeToDMS(ddmsLat)+" ; "+
		   "longitude: "+PointXY.Conversion.convertDecimalDegreeToDMS(ddmsLon);
	}

    /**
     *
     * @param ddms
     * @return
     */
    public static double convertNmeaDMSToToDecimalDegree(double ddms){
	    int deg = (int)ddms/100;
	    return deg + ( ddms-(deg*100) )/60;	
	}

    /**
     *
     * @param ddmsLat
     * @param ddmsLon
     * @return
     */
    public static PointXY convertNmeaDMSToToDecimalDegree(double ddmsLat,double ddmsLon){
	    return new PointXY(PointXY.Conversion.convertNmeaDMSToToDecimalDegree(ddmsLat),
			       PointXY.Conversion.convertNmeaDMSToToDecimalDegree(ddmsLon));
	}
	
    /**
     *
     * @param ddmsLat
     * @param orientLat
     * @param ddmsLon
     * @param orientLon
     * @return
     */
    public static PointXY convertNmeaDMSToToDecimalDegree(double ddmsLat,char orientLat,
							      double ddmsLon,char orientLon){
	    return new PointXY(PointXY.Conversion.convertNmeaDMSToToDecimalDegree(ddmsLat)*PointXY.Conversion.checkOrientation(orientLat),
			       PointXY.Conversion.convertNmeaDMSToToDecimalDegree(ddmsLon)*PointXY.Conversion.checkOrientation(orientLon));
	}
	private static int checkOrientation(char orient){
	    int i=1;
	    
	    switch(orient){
		case 'S':
		    i*=-1;
		    break;
		case 'W':
		    i*=-1;
		    break;
		default:
	    }	    
	    return i;
	}
    }
    
    
}
