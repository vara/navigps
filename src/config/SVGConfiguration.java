/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config;

import java.awt.geom.Point2D;

/**
 *
 * @author vara
 */
public class SVGConfiguration {

    private double zoomInRateX = 1.25;
    private double zoomInRateY = 1.25;
    private double zoomOutRateX = 0.75;
    private double zoomOutRateY = 0.75;

    
    public SVGConfiguration(){
    }
    
    public double getZoomInRateX() {
        return zoomInRateX;
    }

    public void setZoomInRateX(double zoomInRateX) {
        this.zoomInRateX = zoomInRateX;
    }

    public double getZoomInRateY() {
        return zoomInRateY;
    }

    public void setZoomInRateY(double zoomInRateY) {
        this.zoomInRateY = zoomInRateY;
    }

    public double getZoomOutRateX() {
        return zoomOutRateX;
    }

    public void setZoomOutRateX(double zoomOutRateX) {
        this.zoomOutRateX = zoomOutRateX;
    }

    public double getZoomOutRateY() {
        return zoomOutRateY;
    }

    public void setZoomOutRateY(double zoomOutRateY) {
        this.zoomOutRateY = zoomOutRateY;
    }
    public Point2D getZoomOutRate(){
        return new Point2D.Double(zoomOutRateX, zoomOutRateY);
    }
    public Point2D getZoomInRate(){
        return new Point2D.Double(zoomInRateX, zoomInRateY);
    }
}
