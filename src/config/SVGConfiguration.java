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

    
    /**
     *
     */
    public SVGConfiguration(){
    }
    
    /**
     *
     * @return
     */
    public double getZoomInRateX() {
        return zoomInRateX;
    }

    /**
     *
     * @param zoomInRateX
     */
    public void setZoomInRateX(double zoomInRateX) {
        this.zoomInRateX = zoomInRateX;
    }

    /**
     *
     * @return
     */
    public double getZoomInRateY() {
        return zoomInRateY;
    }

    /**
     *
     * @param zoomInRateY
     */
    public void setZoomInRateY(double zoomInRateY) {
        this.zoomInRateY = zoomInRateY;
    }

    /**
     *
     * @return
     */
    public double getZoomOutRateX() {
        return zoomOutRateX;
    }

    /**
     *
     * @param zoomOutRateX
     */
    public void setZoomOutRateX(double zoomOutRateX) {
        this.zoomOutRateX = zoomOutRateX;
    }

    /**
     *
     * @return
     */
    public double getZoomOutRateY() {
        return zoomOutRateY;
    }

    /**
     *
     * @param zoomOutRateY
     */
    public void setZoomOutRateY(double zoomOutRateY) {
        this.zoomOutRateY = zoomOutRateY;
    }
    /**
     *
     * @return
     */
    public Point2D getZoomOutRate(){
        return new Point2D.Double(zoomOutRateX, zoomOutRateY);
    }
    /**
     *
     * @return
     */
    public Point2D getZoomInRate(){
        return new Point2D.Double(zoomInRateX, zoomInRateY);
    }
}
