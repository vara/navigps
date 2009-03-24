/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.svgComponents.Canvas;
import java.util.Vector;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.svg.SVGDocument;

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
     * @param point
     */
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
