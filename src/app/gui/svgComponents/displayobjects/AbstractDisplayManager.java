/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import java.util.Vector;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;

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
    public abstract void putObject(final Vector<Element> object);
}
