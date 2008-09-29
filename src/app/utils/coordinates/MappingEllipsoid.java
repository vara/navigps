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
    
    final static double DEGREE = 1.74532925199e-2;
    
    Ellipsoid [] ellipsoids = new Ellipsoid[]{
				    new Ellipsoid("WGS-84",6378137.0,6356752.3142)    
				};
    
    public MappingEllipsoid(){
    }
    protected static Point.Double MappingLagrange(double b,double l){
	Point.Double retPoint = new Point.Double(0.0,0.0);
	
	
	return retPoint;
    }
}
