/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils.coordinates;

import java.awt.Point;

/**
 *
 * @author vara
 */
public class MappingEllipsoid {
    
    public final static double DEGREE = 1.74532925199e-2;
    public final static double R0 = 6367449.145771;
    public final static Ellipsoid [] ellipsoids = new Ellipsoid[]{
				    new Ellipsoid("WGS-84",6378137.0,6356752.3142)    
				};
    
    public MappingEllipsoid(){
    }
    protected static PointXY mappingLagrange(double b,double l){
	
	//convert to radians
	double phi = b * MappingEllipsoid.DEGREE;
	//double lambda = l * MappingEllipsoid.DEGREE;
	
	double e = ellipsoids[0].getFirstEccentricity();
	
	double u = 1-(e*Math.sin(phi));
	double v = 1+(e*Math.sin(phi));
	double k = Math.pow(u/v,e/2);
	double c = k * Math.tan( ((phi/2)+Math.PI/4) );
	phi = 2*Math.atan(c) - Math.PI/2;
	double delta = PointXY.Conversion.convertDMSToDecimalDegree(15,0,0) + (l-PointXY.Conversion.convertDMSToDecimalDegree(15,0,0));
	return new PointXY(phi,delta);	
	/*
	Params : Latitude 54.19 Longitude 16.1825
Lagrange : Latitude : 0.8322110555269999 Longitude : 31.1825
Marcator : Latitude : 108102.00309768399 Longitude : 3649870.714536103
Gauss Kruger : Latitude : 1889.9027949587519 Longitude : 63809.1792214115
	 * 
Lagrange : Latitude : 0.9426057930109599 Longitude : 31.1825
Marcator : Latitude : 122441.09387753019 Longitude : 3649736.3121762136
Gauss Kruger : Latitude : 2140.587212999138 Longitude : 63806.82952110365
	 */
		
    }
    protected static PointXY mappingMercator(double phi,double lambda){
	
	phi*= MappingEllipsoid.DEGREE;
	lambda*= MappingEllipsoid.DEGREE;
	
	double p = Math.sin(phi);
	double q = Math.cos(phi)*Math.cos(lambda);
	double r = 1 + Math.cos(phi) * Math.sin(lambda);
	double s = 1 - Math.cos(phi) * Math.sin(lambda);
	
	//promien sfery lagrange dla GRS-80 ???? 
		
	double xmerc = MappingEllipsoid.R0 * Math.atan(p/q);
	double ymerc = 0.5 * MappingEllipsoid.R0 * Math.log(r/s);
	
	return new PointXY(xmerc,ymerc);
    }
    protected static PointXY mappingGaussKruger(double xmerc,double ymerc){
	
	xmerc*= MappingEllipsoid.DEGREE;
	ymerc*= MappingEllipsoid.DEGREE;
	
	xmerc/=MappingEllipsoid.R0;
	ymerc/=MappingEllipsoid.R0;
	
	double a2 = 0.8377318247344e-3;
	double a4 = 0.7608527788826e-6;
	double a6 = 0.1197638019173e-8;
	double a8 = 0.2443376242510e-11;
	
	double xgc = MappingEllipsoid.R0 *( xmerc+a2*Math.sin(2*xmerc)+a4*Math.sin(4*xmerc)+a6*Math.sin(6*xmerc)+a8*Math.sin(8*xmerc)  );
	double ygc = MappingEllipsoid.R0 *( ymerc+a2*Math.sin(2*ymerc)+a4*Math.sin(4*ymerc)+a6*Math.sin(6*ymerc)+a8*Math.sin(8*ymerc)  );
	return new PointXY(xgc,ygc);
    }
    
    public static PointXY getCoordinateFromGK(double latitude,double longitude){
	PointXY lag = MappingEllipsoid.mappingLagrange(latitude,longitude);
	PointXY marc = MappingEllipsoid.mappingMercator(lag.getLatitude(), lag.getLongitude());
	PointXY krug = MappingEllipsoid.mappingGaussKruger(marc.getLatitude(), marc.getLongitude());
	
	System.out.println("Params : Latitude "+latitude+" Longitude "+longitude);
	System.out.println("Lagrange : "+lag);
	System.out.println("Marcator : "+marc);
	System.out.println("Gauss Kruger : "+krug);
	
	return krug;
    }
}
