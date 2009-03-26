/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.MainWindowIWD;
import app.gui.svgComponents.Canvas;
import config.SVGConfiguration;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Vector;
import odb.core.ServiceCore;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.events.DOMMouseEvent;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;

/**
 *
 * @author vara
 */
public class XMLDOMDisplayManager extends AbstractDisplayManager{
    
    private UpdateManager updateManager;
    private SVGDocument doc;

    public static final String SERVICES_NAME = "GroupServices";

    public XMLDOMDisplayManager(Canvas c) {
        super(c);
        c.addSVGDocumentLoaderListener(getRenderingTreeListener());
    }

    private void putElement(Element e){

        Element gServ = getGroupServices();
        //if doc == null then gServ == null
        if(gServ!=null){
            gServ.appendChild(e);
        }
    }
    private void putText(String txt,SVGOMPoint pointOnSVGMap){
		
		Element gServ = getGroupServices();
        //if doc == null then gServ == null
        if(gServ!=null){
            String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
            Element textGraph = doc.createElementNS(svgNS, "text");
            textGraph.setAttributeNS(null, "x", Double.toString(pointOnSVGMap.getX()));
            textGraph.setAttributeNS(null, "y", Double.toString(pointOnSVGMap.getY()));
            textGraph.setAttributeNS(null, "font-size", "4");
            Text nodeText = doc.createTextNode(txt);
            textGraph.appendChild(nodeText);

            gServ.appendChild(textGraph);
        }
	}
    /**
     *
     * @return
     */
    public Element getGroupServices(){
        Element gServ = null;
        if(doc!=null){
            gServ = doc.getElementById(SERVICES_NAME);
            if(gServ==null){
                return createGroupServices();
            }
        }
        return gServ;
    }

    private Element createGroupServices(){
        Element servGroup = null;
        if(doc != null){
            Element svgRoot = doc.getRootElement();
            String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
            servGroup = doc.createElementNS(svgNS, "g");
            servGroup.setAttributeNS(null, "id", SERVICES_NAME);
            svgRoot.appendChild(servGroup);
        }
        return servGroup;
    }

    
    @Override
    public void removeLastServices(){
        if(checkDisplaManager()){
            updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                @Override
                public void run(){
                    if(doc!=null){
                        Element gTekst = doc.getElementById(SERVICES_NAME);
                        if(gTekst!=null){                            
                            Node nl = doc.getRootElement().removeChild(gTekst);
                            System.err.println("Removed "+nl.getChildNodes().getLength()+" last services");
          
                        }else{
                            System.err.println("Group "+SERVICES_NAME+" does not exist !");
                        }
                    }
                }
            });
        }
    }

    /**
     *
     * @return
     */
    public SVGDocumentLoaderListener getRenderingTreeListener(){
        return new LoaderTreeListener();
    }

    /**
     *
     * @param object
     * @param point declarate only when object is String
     */
    @Override
    public void putObject(final Object object, final SVGOMPoint point) {
        if(checkDisplaManager()){
            updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                @Override
                public void run(){

                    if(object instanceof String){
                        putText((String)object, point);
                    }else if(object instanceof Element){
                        putElement((Element)object);
                    }
                }
            });
        }
    }

    /**
     *
     * @return
     */
    public boolean checkDisplaManager(){
        if(updateManager==null){
            updateManager = can.getUpdateManager();
        }
        return updateManager!=null;
    }

    @Override
    public void putObject(final Vector object) {
        if(checkDisplaManager()){
            updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                @Override
                public void run(){
                    for (Object e : object) {
                        if(e instanceof Element)
                            putElement((Element)e);
                    }
                }
            });
        }
    }

    @Override
    public void updateService(Object element) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object createObject(ServiceCore sc) {

        String xlinkNS = SVGConstants.XLINK_NAMESPACE_URI;
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

        String groupName = sc.getServiceDescription().getCategory().getName();
        String serviceName = sc.getServiceDescription().getServiceName();
        String serviceDesc = sc.getServiceDescription().getAdditionaInfo();
        String serviceStreet = sc.getServiceDescription().getServiceStreet();
        String serviceNumber = sc.getServiceDescription().getServiceNumber();
        serviceDesc+="<br>Street :<b>"+serviceStreet+" "+serviceNumber+"</b>";

        Element service = MainWindowIWD.getSVGCanvas().getSVGDocument().createElementNS(svgNS, "image");
        service.setAttributeNS(null, "x", String.valueOf(sc.getServiceAttributes().getX()));
        service.setAttributeNS(null, "y", String.valueOf(sc.getServiceAttributes().getY()));
        service.setAttributeNS(null, "width", ""+SVGConfiguration.getInformationIconSize());
        service.setAttributeNS(null, "height",""+SVGConfiguration.getInformationIconSize());
        service.setAttributeNS(null, "groupName", groupName);
        service.setAttributeNS(null, "serviceName", serviceName);
        service.setAttributeNS(null, "serviceDesc", serviceDesc);

        service.setAttributeNS(null, "xmlns:xlink", "http://www.w3.org/1999/xlink");
        String path = "";

        try {
            URL url = MainWindowIWD.createNavigationIconPath("test/"+groupName,"png");
            if(url != null){
                path = url.toURI().toString();
            }
        } catch (URISyntaxException ex) {}

        service.setAttributeNS(xlinkNS, SVGConstants.XLINK_HREF_QNAME,path);

        EventTarget evT = (EventTarget)service;
        evT.addEventListener("click", new EventListener() {

            @Override
            public void handleEvent(Event evt) {
                SVGElement element = (SVGElement) evt.getTarget();
                DOMMouseEvent elEvt = (DOMMouseEvent)evt;
                int xxx = elEvt.getClientX();
                int yyy = elEvt.getClientY();
                System.out.println("x: "+xxx+" y: "+yyy+" Element: "+element.getNodeName());
            }
        }, false);

        return service;
    }

    @Override
    public Vector createObject(Vector<ServiceCore> vsc) {
        Vector<Element> vele = new Vector<Element>();
        for (ServiceCore sc : vsc) {
            vele.add((Element)createObject(sc));
        }
        return vele;
    }

    protected class LoaderTreeListener implements SVGDocumentLoaderListener{

        @Override
        public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
        }

        @Override
        public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
            doc = e.getSVGDocument();
        }
        @Override
        public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
        }

        @Override
        public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
        }
    }
}
