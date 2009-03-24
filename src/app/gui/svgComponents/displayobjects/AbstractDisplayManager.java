package app.gui.svgComponents.displayobjects;

import app.gui.svgComponents.Canvas;
import java.util.Vector;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;

/**
 *
 * @author wara
 */
public abstract class AbstractDisplayManager {
    
    protected Canvas can;

    public AbstractDisplayManager(Canvas can) {
        this.can = can;
    }
    
    public abstract void putObject(Object object,SVGOMPoint point);
    public abstract void putObject(final Vector<Element> object);
    public abstract void removeLastServices();
}
