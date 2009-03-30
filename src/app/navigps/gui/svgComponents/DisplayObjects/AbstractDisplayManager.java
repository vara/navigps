package app.navigps.gui.svgComponents.DisplayObjects;

import app.navigps.gui.svgComponents.Canvas;
import java.util.Vector;
import app.database.odb.core.ServiceCore;
import org.apache.batik.dom.svg.SVGOMPoint;

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
    public abstract void putObject(final Vector object);
    public abstract void removeLastServices();
    public abstract void updateService(final Object element);
    public abstract Object createObject(ServiceCore sc);
    public abstract Vector createObject(Vector<ServiceCore> vsc);
}
