package app.utils;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;

/**
 *
 * @author vara
 */
public class Utils {        
    
    public static final Runtime getRuntime(){
        return Runtime.getRuntime();
    }
    
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
    public static Border createInsiderBorder(int top,int left,int bottom,int right,Color colFrame){     
        return BorderFactory.createCompoundBorder(
			    BorderFactory.createEmptyBorder(
				top,   // number of points from the top
				left,   // number of points along the left side
				bottom,    // number of points from the bottom
				right),  // number of points on the right side				
			    BorderFactory.createLineBorder(colFrame));
    }
    
    public static SVGOMPoint getLocalPointFromDomElement(Element element, int x, int y){	    
	    SVGMatrix mat = ((SVGLocatable) element).getScreenCTM();
	    SVGMatrix imat = mat.inverse(); // screen -> elem
	    SVGOMPoint pt = new SVGOMPoint(x, y);
	    return (SVGOMPoint) pt.matrixTransform(imat);
    }

    public static String roundsValue(double val,int fraction,int mul){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(fraction);
        return nf.format(val/mul);
    }

    public static String roundsValue(double val,int fraction){
        return roundsValue(val,fraction,1);
    }

    public static Font createFitFont(Font f,double scale){
        return f.deriveFont( (float)(f.getSize()*scale) );
    }

    public static char[] resizeArray(char[] tab,int expand){
        char [] newArray = new char [tab.length+expand];
        for (int i = 0; i < tab.length; i++) {
            newArray[i] = tab[i];
        }
        return newArray;
    }
    
    public static Color colorAlpha(int r,int g,int b,float alpha){
        return new Color(r,g,b,(int)(alpha*255));
    }

    public static Color colorAlpha(Color oldCol,float alpha){
        return new Color(oldCol.getRed(),
                oldCol.getGreen(),oldCol.getBlue(),(int)(alpha*255));
    }
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
    public static Color checkColor(int r,int g,int b){
        return checkColor(r, g, b, 255);
    }
}
