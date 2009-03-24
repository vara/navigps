/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.svgComponents.Canvas;
import java.util.Vector;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author vara
 */
public class DisplayManager extends AbstractDisplayManager{

    private SVGDocument doc;
    private UpdateManager updateManager;
    private Canvas can;


    public static final String SERVICES_NAME = "GroupServices";

    public DisplayManager(Canvas c) {        
        can = c;
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

    public void removeLastServices(){
        if(checkDisplaManager()){
            updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                @Override
                public void run(){
                    if(doc!=null){
                        Element gTekst = doc.getElementById(SERVICES_NAME);
                        if(gTekst!=null){
                            /*NodeList nl = gTekst.getChildNodes();

                            for (int i = 0; i < nl.getLength(); i++) {
                                Node child =nl.item(i);
                                System.out.println("node: name : "+child.getLocalName()+" value : "+child.getNodeValue());
                                gTekst.removeChild(child);

                            }
                            System.err.println("Removed "+nl.getLength()+" last services");
                             */
                            doc.getRootElement().removeChild(gTekst);
                        }else{
                            System.err.println("Group "+SERVICES_NAME+" does not exist !");
                        }
                    }
                }
            });
        }
    }

    public SVGDocumentLoaderListener getRenderingTreeListener(){
        return new LoaderTreeListener();
    }

    @Override
    public void putObject(final Object object, final SVGOMPoint point) {
        if(checkDisplaManager()){
            updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                @Override
                public void run(){

                    if(object instanceof String){
                        putText((String)object, point);
                    }
                }
            });
        }
    }

    public boolean checkDisplaManager(){
        if(updateManager==null){
            updateManager = can.getUpdateManager();
        }
        return updateManager!=null;
    }

    public void putObject(final Vector<Element> object) {
        if(checkDisplaManager()){
            updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                @Override
                public void run(){
                    for (Element element : object) {
                        putElement(element);
                    }
                }
            });
        }
    }

    protected class LoaderTreeListener implements SVGDocumentLoaderListener{

        @Override
        public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
        }

        @Override
        public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
            updateManager = null;
            doc = e.getSVGDocument();
            putObject("Test !!!", new SVGOMPoint(520, 400));
        }

        @Override
        public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
        }

        @Override
        public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
        }
    }

    protected class UpdateManagerInfoListener implements UpdateManagerListener{


        @Override
        public void managerStarted(UpdateManagerEvent e) {
            System.err.println("managerStarted ");
        }

        @Override
        public void managerSuspended(UpdateManagerEvent e) {
            System.err.println("managerSuspend ");
        }

        @Override
        public void managerResumed(UpdateManagerEvent e) {
            System.err.println("managerResumed ");
        }

        @Override
        public void managerStopped(UpdateManagerEvent e) {
            System.err.println("managerStoped ");
        }

        @Override
        public void updateStarted(UpdateManagerEvent e) {
            System.err.println("updateStarted ");
        }

        @Override
        public void updateCompleted(UpdateManagerEvent e) {
            System.err.println("updateCompleted ");
        }

        @Override
        public void updateFailed(UpdateManagerEvent e) {
            System.err.println("updateFailed ");
        }
    }
}
