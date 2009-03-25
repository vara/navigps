/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGCanvasLayers;
import app.gui.svgComponents.SynchronizedSVGLayer;
import app.utils.GraphicsUtilities;
import app.utils.NaviPoint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.util.SVGConstants;
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

            System.out.println("href "+href);
            try {

                BufferedImage bi = GraphicsUtilities.loadCompatibleImage(new URL(href));
                BufferedImage thumbImg = GraphicsUtilities.createThumbnail(bi, iw);
                ObjectService ob = new ObjectService(thumbImg, serviceDesc,groupName,serviceName, np);
                getDisplayLayer().add(ob);

            } catch (IOException ex) {
                System.err.println(""+ex);
            }
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
