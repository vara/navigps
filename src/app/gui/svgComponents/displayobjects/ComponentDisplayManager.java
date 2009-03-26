/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.MainWindowIWD;
import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGCanvasLayers;
import app.gui.svgComponents.SynchronizedSVGLayer;
import app.utils.GraphicsUtilities;
import app.utils.MyLogger;
import app.utils.NaviPoint;
import config.SVGConfiguration;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import odb.core.ServiceCore;
import org.apache.batik.dom.svg.SVGOMPoint;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putObject(Vector object) {
        for (Object e : object) {
            if(e instanceof ObjectService)
                getDisplayLayer().add((ObjectService)e);
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

    @Override
    public void updateService(Object element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object createObject(ServiceCore sc) {

        String groupName = sc.getServiceDescription().getCategory().getName();
        String serviceName = sc.getServiceDescription().getServiceName();
        String serviceDesc = sc.getServiceDescription().getAdditionaInfo();

        String serviceStreet = sc.getServiceDescription().getServiceStreet();
        String serviceNumber = sc.getServiceDescription().getServiceNumber();
        serviceDesc+="<br>Street :<b>"+serviceStreet+" "+serviceNumber+"</b>";
        ObjectService ob = null;
        NaviPoint np = new NaviPoint(sc.getServiceAttributes().getX(),
                sc.getServiceAttributes().getY());
        try {

            URL url = MainWindowIWD.createNavigationIconPath("test/"+groupName,"png");
            BufferedImage bi = GraphicsUtilities.loadCompatibleImage(url);
            BufferedImage thumbImg = GraphicsUtilities.createThumbnail(bi, 
                    SVGConfiguration.getInformationIconSize());
            ob = new ObjectService(thumbImg, serviceDesc,groupName,serviceName, np);

        } catch (IOException ex) {
            ob = new ObjectService(null, serviceDesc,groupName,serviceName, np);
            String msg = ex + "[ for group name "+groupName+" ]";
            MyLogger.log.log(Level.WARNING, msg);
        }

        return ob;
    }

    @Override
    public Vector createObject(Vector<ServiceCore> vsc) {

        Vector <ObjectService> vos = new Vector<ObjectService>();
        for (ServiceCore os : vsc) {
            vos.add((ObjectService)createObject(os));
        }
        return vos;
    }
}
