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
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

    public DisplayManager(Canvas c) {        
        can = c;
    }

    synchronized private void putText(String txt,SVGOMPoint pointOnSVGMap){

		Element svgRoot = doc.getRootElement();
		//SVGOMPoint pointOnSVGMap = Utils.getLocalPointFromDomElement(svgRoot, (int)p.getX(), (int)p.getY());
        //System.out.println("X "+pointOnSVGMap.getX()+" Y "+pointOnSVGMap.getY());
		//FontMetrics fm = doc.getFontMetrics(doc.getFont());
		int strWidth = 40 ;//fm.stringWidth(txt);

		Element gTekst = doc.getElementById("grupaTekst");
		if(gTekst==null)
		{
		    String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		    Element grupaTekst = doc.createElementNS(svgNS, "g");
		    grupaTekst.setAttributeNS(null, "id", "grupaServices");

		    Element rectangle = doc.createElementNS(svgNS, "rect");
		    rectangle.setAttributeNS(null, "x", "" + (pointOnSVGMap.getX()) );
		    rectangle.setAttributeNS(null, "y", "" + (pointOnSVGMap.getY()) );
		    rectangle.setAttributeNS(null, "width", ""+(strWidth+10));
		    rectangle.setAttributeNS(null, "height", "15");
		    rectangle.setAttributeNS(null, "style", "fill:red");

		    Element textGraph = doc.createElementNS(svgNS, "text");
		    textGraph.setAttributeNS(null, "x", Double.toString(pointOnSVGMap.getX()));
		    textGraph.setAttributeNS(null, "y", Double.toString(pointOnSVGMap.getY()));
		    textGraph.setAttributeNS(null, "font-size", "22");
		    Text nodeText = doc.createTextNode(txt);

		    textGraph.appendChild(nodeText);
		    //grupaTekst.appendChild(rectangle);
		    grupaTekst.appendChild(textGraph);
		    svgRoot.appendChild(grupaTekst);

		}else
		{
		    Element textGraph = (Element)gTekst.getFirstChild();

		    textGraph.setAttributeNS(null, "x", Double.toString(pointOnSVGMap.getX()));
		    textGraph.setAttributeNS(null, "y", Double.toString(pointOnSVGMap.getY()));
		    Node oldNodeText = textGraph.getFirstChild();
		    Text nodeText = doc.createTextNode(txt);
		    textGraph.replaceChild(nodeText,oldNodeText);
		}
	}

    public GVTTreeRendererListener getRenderingTreeListener(){
        return new RenderingTreeListener();
    }

    @Override
    public void putObject(final Object object, final SVGOMPoint point) {
        if(updateManager != null){
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


    public void putObject(final Vector<Element> object) {
        if(updateManager != null){
            updateManager.getUpdateRunnableQueue().invokeLater(new Runnable() {
                @Override
                public void run(){

                }
            });
        }
    }

    protected class RenderingTreeListener implements GVTTreeRendererListener{
        @Override
        public void gvtRenderingPrepare(GVTTreeRendererEvent e) {

        }
        @Override
        public void gvtRenderingStarted(GVTTreeRendererEvent e) {

        }
        @Override
        public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
            updateManager = can.getUpdateManager();
            doc = can.getSVGDocument();
            putObject("Test !!!", new SVGOMPoint(520, 400));
        }
        @Override
        public void gvtRenderingCancelled(GVTTreeRendererEvent e) {

        }
        @Override
        public void gvtRenderingFailed(GVTTreeRendererEvent e) {

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
