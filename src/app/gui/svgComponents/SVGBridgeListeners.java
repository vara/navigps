/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderListener;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;

/**
 *
 * @author vara
 */
public class SVGBridgeListeners extends SVGBridgeComponents implements 
                    SVGDocumentLoaderListener,
                    GVTTreeBuilderListener,
                    GVTTreeRendererListener	{
            
    
    @Override
    public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
        setRederingStatus(true);
        setTextToCurrentStatus("Document Loading Started ...");
    }
    @Override
    public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {	
        setTextToCurrentStatus("Document Loading Completed");
        setAbsoluteFilePath(e.getSVGDocument().getDocumentURI());
    }
    @Override
    public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
        setTextToCurrentStatus("Document Loading Cancelled !");
    }
    @Override
    public void documentLoadingFailed(SVGDocumentLoaderEvent e) {	
        setTextToCurrentStatus("Document Loading Failed !");
    }
    
    
    @Override
    public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
        documentBuildCompleted();
        setTextToCurrentStatus("Document Build Completed");

        getVerboseStream().outputVerboseStream("getBounds \t"+e.getGVTRoot().getBounds());
        getVerboseStream().outputVerboseStream("getClip \t"+e.getGVTRoot().getClip());
        getVerboseStream().outputVerboseStream("getcomposite \t"+e.getGVTRoot().getComposite());
        getVerboseStream().outputVerboseStream("getGeomatryBounds \t"+e.getGVTRoot().getGeometryBounds());
        getVerboseStream().outputVerboseStream("getGlobalTransform \t"+e.getGVTRoot().getGlobalTransform());
        getVerboseStream().outputVerboseStream("getInverstranform \t"+e.getGVTRoot().getInverseTransform());
        getVerboseStream().outputVerboseStream("getMask \t"+e.getGVTRoot().getMask());
        getVerboseStream().outputVerboseStream("getOutline \t"+e.getGVTRoot().getOutline());
        getVerboseStream().outputVerboseStream("getBounds \t"+e.getGVTRoot().getBounds());
    }
    @Override
    public void gvtBuildCancelled(GVTTreeBuilderEvent e) {
        setTextToCurrentStatus("Document Build Cancelled !");
    }
    @Override
    public void gvtBuildFailed(GVTTreeBuilderEvent e) {
        setTextToCurrentStatus("Document Build Failed !");
    }
    @Override
    public void gvtBuildStarted(GVTTreeBuilderEvent e) {        
        setTextToCurrentStatus("Documnet Build Started");
    }
    
    
    @Override
    public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
        setTextToCurrentStatus("Document Rendering Prepare ...");
    }
    @Override
    public void gvtRenderingStarted(GVTTreeRendererEvent e) {
        setTextToCurrentStatus("Document Rendering Started ...");
        setRederingStatus(true);
    }
    @Override
    public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
        setTextToCurrentStatus("Document Rendering Completed");
        setRederingStatus(false);
    }
    @Override
    public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
        setTextToCurrentStatus("Documnet Rendering Cancelled !");
    }
    @Override
    public void gvtRenderingFailed(GVTTreeRendererEvent e) {
        setTextToCurrentStatus("Documnet Rendering Failed !");
    }           
}
