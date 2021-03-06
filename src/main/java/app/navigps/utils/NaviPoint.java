/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
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

    public NaviPoint matrixTransform(AffineTransform matrix){
        Point.Float p = new Point.Float(this.x,this.y);
        matrix.transform(p, p);
        return new NaviPoint(p.x, p.y);
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

    /**
     * Create area square to surround this point.
     * Radius is a half the length of the side of the square.
     *
     * @param float - half the length of the side of the square
     * @return Rectangle.Float - Square to surround this point
     */
    public Rectangle.Float createAreaSquareF(float radius){
        return new Rectangle2D.Float(x-radius, y-radius, x+radius, y+radius);
    }

    /**
     * Create area square to surround this point.
     * Radius is a half the length of the side of the square.
     *
     * @param float - half the length of the side of the square
     * @return Rectangle.Integer - Square to surround this point
     */
    public Rectangle createAreaSquareI(float radius){
        return new Rectangle((int)(x-radius), (int)(y-radius),(int) (radius*2), (int)(radius*2));
    }
}
