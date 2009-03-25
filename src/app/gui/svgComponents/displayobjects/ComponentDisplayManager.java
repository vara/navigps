/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGCanvasLayers;
import app.gui.svgComponents.SynchronizedSVGLayer;
import app.utils.GraphicsUtilities;
import app.utils.MyLogger;
import app.utils.NaviPoint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.Element;

/**
 *
 * @author wara
 */
public class ComponentDisplayManager extends AbstractDisplayManager{

    private Vector <ObjectToDisplayService> services = new Vector<ObjectToDisplayService>(100);

    public ComponentDisplayManager(Canvas can) {
        super(can);
    }

    @Override
    public void putObject(Object object, SVGOMPoint point) {
        
    }

    @Override
    public void putObject(Vector<Element> object) {

        for (Element e : object) {
            String groupName = "";
            String serviceName = "";
            String serviceDesc = "";

            groupName = e.getAttribute("groupName");
            serviceName = e.getAttribute("serviceName");
            serviceDesc = e.getAttribute("serviceDesc");
            String x = e.getAttribute("x");
            String y = e.getAttribute("y");
            String w = e.getAttribute("width");
            String h = e.getAttribute("height");
            String href = e.getAttributeNS("http://www.w3.org/1999/xlink", "href");

            float fx = Float.parseFloat(x);
            float fy = Float.parseFloat(y);
            int iw = Integer.parseInt(w);
            NaviPoint np = new NaviPoint(fx, fy);
            ObjectService ob = null;
            try {

                BufferedImage bi = GraphicsUtilities.loadCompatibleImage(new URL(href));
                BufferedImage thumbImg = GraphicsUtilities.createThumbnail(bi, iw);
                ob = new ObjectService(thumbImg, serviceDesc,groupName,serviceName, np);               

            } catch (IOException ex) {
                ob = new ObjectService(null, serviceDesc,groupName,serviceName, np);
                String msg = ex + "[ for group name "+groupName+" ]";
                MyLogger.log.log(Level.WARNING, msg);
            }
            getDisplayLayer().add(ob);
        }
        getDisplayLayer().updateComponent();
    }

    @Override
    public void removeLastServices() {
        services.removeAllElements();
        getDisplayLayer().removeAll();
        getDisplayLayer().repaint();
    }

    private SynchronizedSVGLayer getDisplayLayer(){
        SVGCanvasLayers svgcl = (SVGCanvasLayers)can.getParent();
        return (SynchronizedSVGLayer)svgcl.getServicesContainer();
    }
}
