/*
 * NaviUtilities.java
 *
 * Created on 2009-04-08, 15:35:17
 */

package app.navigps.utils;

import app.navigps.gui.svgComponents.SVGCanvasLayers;
import java.awt.Container;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class NaviUtilities {


    public static SVGCanvasLayers getSVGCanvasLayers(Container parent){
        SVGCanvasLayers svgCanvasLayer = null;
        if(parent != null){
            while((parent = parent.getParent())!=null){
                if(parent instanceof SVGCanvasLayers){
                    System.err.println("Found SVGCanvaaslayer");
                    return (SVGCanvasLayers)parent;
                }
            }
        }
        return svgCanvasLayer;
    }
}
