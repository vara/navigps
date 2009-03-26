/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.MainWindowIWD;
import app.gui.svgComponents.Canvas;
import app.gui.svgComponents.SVGCanvasLayers;
import app.gui.svgComponents.SynchronizedSVGLayer;
import app.utils.MyLogger;
import app.utils.NaviPoint;
import java.awt.Component;
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
    public void updateService(final Object element) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(element instanceof ServiceCore){
                    ServiceCore sc = (ServiceCore)element;
                    Component[] comps = getDisplayLayer().getComponents();
                    for (Component c : comps) {
                        ObjectService os = (ObjectService)c;
                        if(os.getOID().equals(sc.getOID())){
                            os.updateService(sc);
                            String msg = "Service \'"+os.getServiceName()+"\' has been changed";
                            MainWindowIWD.getBridgeInformationPipe().currentStatusChanged(msg);
                            System.out.println(msg);
                            return;
                        }
                    }
                }else{
                    MyLogger.log.log(Level.WARNING, "Method UpdateService \"argument is not instance class ServiceCore\"");
                }
            }
        }).start();
    }

    @Override
    public Object createObject(ServiceCore sc) {

        String groupName = sc.getServiceDescription().getCategory().getName();
        String serviceName = sc.getServiceDescription().getServiceName();
        String serviceDesc = sc.getServiceDescription().getAdditionaInfo();

        String serviceStreet = sc.getServiceDescription().getServiceStreet();
        String serviceNumber = sc.getServiceDescription().getServiceNumber();
        serviceDesc+="<br>Street :<b>"+serviceStreet+" "+serviceNumber+"</b>";        
        NaviPoint np = new NaviPoint(sc.getServiceAttributes().getX(),
                sc.getServiceAttributes().getY());
        return new ObjectService(serviceDesc,groupName,serviceName,sc.getOID(), np);
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
