/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGPoint;

/**
 *
 * @author wara
 */
public class NaviPoint extends SVGOMPoint{

    /**
     *
     * @param x
     * @param y
     */
    public NaviPoint(float x, float y){
        super(x, y);
    }
    
    /**
     *
     * @param p
     */
    public NaviPoint(SVGPoint p){
        super(p.getX(),p.getY());
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setLocation(float x, float y) {
	    this.x = x;
	    this.y = y;
	}
    
    /**
     *
     * @param p
     */
    public void setLocation(SVGPoint p) {
	    this.x = p.getX();
	    this.y = p.getY();
	}

    /**
     *
     * @param pt
     * @return
     */
    public double distance(NaviPoint pt) {
        float px = pt.getX() - this.getX();
        float py = pt.getY() - this.getY();
        return Math.sqrt(px * px + py * py);
    }

    /**
     *
     * @param px
     * @param py
     * @return
     */
    public double distance(float px, float py) {
        px -= getX();
        py -= getY();
        return Math.sqrt(px * px + py * py);
    }

    /**
     *
     * @param px
     * @param py
     * @return
     */
    public double distanceSq(float px, float py) {
        px -= getX();
        py -= getY();
        return (px * px + py * py);
    }

    /**
     *
     * @param pt
     * @return
     */
    public double distanceSq(NaviPoint pt) {
        float px = pt.getX() - this.getX();
        float py = pt.getY() - this.getY();
        return (px * px + py * py);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NaviPoint) {
            NaviPoint p2d = (NaviPoint) obj;
            return (getX() == p2d.getX()) && (getY() == p2d.getY());
        }
        return super.equals(obj);
    }

    @Override
    public NaviPoint matrixTransform(SVGMatrix matrix) {
        SVGPoint p = matrixTransform(this, matrix);
        return new NaviPoint(p.getX(), p.getY());
    }
    @Override
    public int hashCode() {
        int bits = java.lang.Float.floatToIntBits(getX());
        bits ^= java.lang.Float.floatToIntBits(getY()) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    @Override
    public String toString() {
        return "x: "+getX()+" y: "+getY();
    }
}
