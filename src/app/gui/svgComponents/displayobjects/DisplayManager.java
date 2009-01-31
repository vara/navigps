/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.svgComponents.Canvas;
import app.utils.OutputVerboseStream;
import app.utils.Utils;
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

    private OutputVerboseStream vs;
    private SVGDocument doc;
    private UpdateManager updateManager;
    private Canvas can;
    public DisplayManager(Canvas c,OutputVerboseStream vs) {
        this.vs =vs;
        if(!c.isDynamic()){
            vs.setTimeEnabled(true);
            vs.outputErrorVerboseStream("Display Manager Disabled !!!\nCanvas isn't dynamic document");
        }
        can = c;
    }

    synchronized private void putText(String txt,SVGOMPoint p){

		Element svgRoot = doc.getRootElement();
		SVGOMPoint pointOnSVGMap = Utils.getLocalPointFromDomElement(svgRoot, (int)p.getX(), (int)p.getY());
        System.out.println("X "+pointOnSVGMap.getX()+" Y "+pointOnSVGMap.getY());
		//FontMetrics fm = doc.getFontMetrics(doc.getFont());
		int strWidth = 40 ;//fm.stringWidth(txt);

		Element gTekst = doc.getElementById("grupaTekst");
		if(gTekst==null)
		{
		    String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		    Element grupaTekst = doc.createElementNS(svgNS, "g");
		    grupaTekst.setAttributeNS(null, "id", "grupaServices");

		    Element rectangle = doc.createElementNS(svgNS, "rect");
		    rectangle.setAttributeNS(null, "x", "" + (pointOnSVGMap.getX()-5) );
		    rectangle.setAttributeNS(null, "y", "" + (pointOnSVGMap.getY()-5) );
		    rectangle.setAttributeNS(null, "width", ""+(strWidth+10));
		    rectangle.setAttributeNS(null, "height", "15");
		    rectangle.setAttributeNS(null, "style", "fill:red");

		    Element textGraph = doc.createElementNS(svgNS, "text");
		    textGraph.setAttributeNS(null, "x", Double.toString(pointOnSVGMap.getX()));
		    textGraph.setAttributeNS(null, "y", Double.toString(pointOnSVGMap.getY()));
		    textGraph.setAttributeNS(null, "font-size", "12");
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

    public OutputVerboseStream getVerboseStream(){
        return vs;
    }

    public GVTTreeRendererListener getRenderingTreeListener(){
        return new RenderingTreeListener();
    }

    @Override
    public void putObject(Object object, SVGOMPoint point) {
        if(updateManager != null){

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
            getVerboseStream().outputVerboseStream("managerStarted ");
        }

        @Override
        public void managerSuspended(UpdateManagerEvent e) {
            getVerboseStream().outputVerboseStream("managerSuspend ");
        }

        @Override
        public void managerResumed(UpdateManagerEvent e) {
            getVerboseStream().outputVerboseStream("managerResumed ");
        }

        @Override
        public void managerStopped(UpdateManagerEvent e) {
            getVerboseStream().outputVerboseStream("managerStoped ");
        }

        @Override
        public void updateStarted(UpdateManagerEvent e) {
            getVerboseStream().outputVerboseStream("updateStarted ");
        }

        @Override
        public void updateCompleted(UpdateManagerEvent e) {
            getVerboseStream().outputVerboseStream("updateCompleted ");
        }

        @Override
        public void updateFailed(UpdateManagerEvent e) {
            getVerboseStream().outputVerboseStream("updateFailed ");
        }
    }
}