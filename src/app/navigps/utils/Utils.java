package app.navigps.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGPoint;

/**
 *
 * @author vara
 */
public class Utils {        
    
    /**
     *
     * @return
     */
    public static final Runtime getRuntime(){
        return Runtime.getRuntime();
    }
    
    /**
     *
     * @param top
     * @param left
     * @param bottom
     * @param right
     * @param colFrame
     * @return
     */
    public static Border createOutsiderBorder(int top,int left,int bottom,int right,Color colFrame){
        return BorderFactory.createCompoundBorder(
			    BorderFactory.createLineBorder(colFrame),
			    BorderFactory.createEmptyBorder(
				top,   // number of points from the top
				left,   // number of points along the left side
				bottom,    // number of points from the bottom
				right)  // number of points on the right side
				);
    }
    /**
     *
     * @param top
     * @param left
     * @param bottom
     * @param right
     * @param colFrame
     * @return
     */
    public static Border createInsiderBorder(int top,int left,int bottom,int right,Color colFrame){
        return BorderFactory.createCompoundBorder(
			    BorderFactory.createEmptyBorder(
				top,   // number of points from the top
				left,   // number of points along the left side
				bottom,    // number of points from the bottom
				right),  // number of points on the right side				
			    BorderFactory.createLineBorder(colFrame));
    }
    
    public static NaviPoint getLocalPointFromDomElement(Element element, float x, float y){
	    SVGMatrix mat = ((SVGLocatable) element).getScreenCTM();
	    SVGMatrix imat = mat.inverse(); // screen -> elem
	    NaviPoint pt = new NaviPoint(x, y);
	    return pt.matrixTransform(imat);
    }
    
    public static NaviPoint getComponentPointRelativeToDomElement(Element element, NaviPoint p){
	    SVGMatrix mat = ((SVGLocatable) element).getScreenCTM();
	    return p.matrixTransform(mat);
    }

    public static NaviPoint[] getComponentPointRelativeToDomElement(Element element, NaviPoint []src,NaviPoint[]dest){

        if(dest==null){
            dest = new NaviPoint[src.length];
        }
        
        SVGMatrix mat = ((SVGLocatable) element).getScreenCTM();
        for (int i = 0; i < src.length; i++) {
            dest[i] = src[i].matrixTransform(mat);

        }
	    return dest;
    }
    
    public static Point2D[] svgPointToPoint2D(SVGPoint[] points) {
        final Point2D[] returnPoints = new Point2D.Double[points.length];
        for (int count = 0; count < points.length; count++) {
            returnPoints[count] =
                    new Point2D.Double(points[count].getX(),
                    points[count].getY());
        }
        return returnPoints;
    }

    /**
     * Derives the "magic" transform that can transform source points to
     * destination points.
     *
     * @param srcPoints Three points representing the source parallelogram.
     * @param destPoints Three points representing the destination
     * parallelogram.
     * 
     * Matrix is represent
     * [m00 m01 m02]
     * [m10 m11 m12]
     * [ 0   0   1 ]
     *
     * @return A transform as described above.
     *
     */
    public static AffineTransform deriveTransform(
            Point2D[] srcPoints, Point2D[] destPoints) throws NoninvertibleTransformException {
        if ((srcPoints == null) || (srcPoints.length != 3)) {
            throw new IllegalArgumentException(
                    "Source points must contain three points!");
        } else if ((destPoints == null) || (destPoints.length != 3)) {
            throw new IllegalArgumentException(
                    "Destination points must contain three points!");
        }
        AffineTransform returnTransform = null;

        //System.out.println(srcPoints[0]+","+srcPoints[1]+","+srcPoints[2]);
        final double m00 = srcPoints[1].getX() - srcPoints[0].getX();
        final double m10 = srcPoints[1].getY() - srcPoints[0].getY();
        final double m01 = srcPoints[2].getX() - srcPoints[0].getX();
        final double m11 = srcPoints[2].getY() - srcPoints[0].getY();
        final double m02 = srcPoints[0].getX();
        final double m12 = srcPoints[0].getY();

        final double n00 = destPoints[1].getX() - destPoints[0].getX();
        final double n10 = destPoints[1].getY() - destPoints[0].getY();
        final double n01 = destPoints[2].getX() - destPoints[0].getX();
        final double n11 = destPoints[2].getY() - destPoints[0].getY();
        final double n02 = destPoints[0].getX();
        final double n12 = destPoints[0].getY();

        // Build the transform based on the source points.
        final AffineTransform srcTransform =
                new AffineTransform(
                new double[] {m00, m10, m01, m11, m02, m12});
        //System.out.println("src Tran "+srcTransform);

        final AffineTransform destTransform =
                    new AffineTransform(
                    new double[] {n00, n10, n01, n11, n02, n12});
        //System.out.println("destTran "+destTransform);
       
        try {

            returnTransform = srcTransform.createInverse();
            returnTransform.preConcatenate(destTransform);

        } catch (NoninvertibleTransformException e) {
            NaviLogger.log.severe(e.getMessage());
        }
        //System.out.println("retTran "+returnTransform);
        return returnTransform;
    }
    /**
     *
     * @param val
     * @param fraction
     * @param mul
     * @return
     */
    public static String roundsValue(double val,int fraction,double mul){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(fraction);
        return nf.format(val/mul);
    }

    /**
     *
     * @param val
     * @param fraction
     * @return
     */
    public static String roundsValue(double val,int fraction){
        return roundsValue(val,fraction,1);
    }

    /**
     *
     * @param f
     * @param scale
     * @return
     */
    public static Font createFitFont(Font f,double scale){
        return f.deriveFont( (float)(f.getSize()*scale) );
    }

    /**
     *
     * @param tab
     * @param expand
     * @return
     */
    public static char[] resizeArray(char[] tab,int expand){
        char [] newArray = new char [tab.length+expand];
        for (int i = 0; i < tab.length; i++) {
            newArray[i] = tab[i];
        }
        return newArray;
    }
    
    /**
     *
     * @param r
     * @param g
     * @param b
     * @param alpha
     * @return
     */
    public static Color colorAlpha(int r,int g,int b,float alpha){
        return new Color(r,g,b,(int)(alpha*255));
    }

    /**
     *
     * @param oldCol
     * @param alpha
     * @return
     */
    public static Color colorAlpha(Color oldCol,float alpha){
        return new Color(oldCol.getRed(),
                oldCol.getGreen(),oldCol.getBlue(),(int)(alpha*255));
    }
    /**
     *
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static Color checkColor(int r,int g,int b,int a){
        if ( a < 0 || a > 255) {
            a = a < 0 ? 0 : 255;
        }
        if ( r < 0 || r > 255) {
            r = r < 0 ? 0 : 255;
        }
        if ( g < 0 || g > 255) {
            g = g < 0 ? 0 : 255;
        }
        if ( b < 0 || b > 255) {
            b = b < 0 ? 0 : 255;
        }
        return new Color(r,g,b,a);
    }
    /**
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static Color checkColor(int r,int g,int b){
        return checkColor(r, g, b, 255);
    }
}
