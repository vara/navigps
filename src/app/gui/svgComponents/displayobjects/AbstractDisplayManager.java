/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import org.apache.batik.dom.svg.SVGOMPoint;

/**
 *
 * @author wara
 */
public abstract class AbstractDisplayManager {
    /**
     *
     * @param object
     * @param point
     */
    public abstract void putObject(Object object,SVGOMPoint point);
}
