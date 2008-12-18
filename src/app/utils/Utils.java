/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

    public static String roundsValue(double val,int n,int mul){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(n);
        return nf.format(val/mul);
    }

    public static String roundsValue(double val,int n){
        return roundsValue(val,n,1);
    }

    public static Font createFitFont(Font f,double scale){
        int newSize = (int)(f.getSize()*scale);
        return new Font(f.getName(),f.getStyle(), newSize);
    }
}
