/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import java.io.File;
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
    
    public SVGBridgeListeners(){
    }

    @Override
    public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
        setRederingStatus(true);
        currentStatusChanged("Document Loading Started ...");
    }
    @Override
    public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {	
        currentStatusChanged("Document Loading Completed");
        setSvgFileObject(new File(e.getSVGDocument().getDocumentURI()));
    }
    @Override
    public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
        currentStatusChanged("Document Loading Cancelled !");
    }
    @Override
    public void documentLoadingFailed(SVGDocumentLoaderEvent e) {	
        currentStatusChanged("Document Loading Failed !");
    }
    
    
    @Override
    public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
        documentPrepareToModification();
        currentStatusChanged("Document Build Completed");
        String info = "getBounds \t"+e.getGVTRoot().getBounds()+
                      "\ngetClip \t"+e.getGVTRoot().getClip()+
                      "\ngetcomposite \t"+e.getGVTRoot().getComposite()+
                      "\ngetGeomatryBounds \t"+e.getGVTRoot().getGeometryBounds()+
                      "\ngetGlobalTransform \t"+e.getGVTRoot().getGlobalTransform()+
                      "\ngetInverstranform \t"+e.getGVTRoot().getInverseTransform()+
                      "\ngetMask \t"+e.getGVTRoot().getMask()+
                      "\ngetOutline \t"+e.getGVTRoot().getOutline()+
                      "\ngetBounds \t"+e.getGVTRoot().getBounds();
        System.out.println(info);
    }
    @Override
    public void gvtBuildCancelled(GVTTreeBuilderEvent e) {
        currentStatusChanged("Document Build Cancelled !");
    }
    @Override
    public void gvtBuildFailed(GVTTreeBuilderEvent e) {
        currentStatusChanged("Document Build Failed !");
    }
    @Override
    public void gvtBuildStarted(GVTTreeBuilderEvent e) {        
        currentStatusChanged("Documnet Build Started");
    }    
    
    @Override
    public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
        currentStatusChanged("Document Rendering Prepare ...");
    }
    @Override
    public void gvtRenderingStarted(GVTTreeRendererEvent e) {
        currentStatusChanged("Document Rendering Started ...");
        setRederingStatus(true);
    }
    @Override
    public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
        currentStatusChanged("Document Rendering Completed");
        setRederingStatus(false);
    }
    @Override
    public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
        currentStatusChanged("Documnet Rendering Cancelled !");
    }
    @Override
    public void gvtRenderingFailed(GVTTreeRendererEvent e) {
        currentStatusChanged("Documnet Rendering Failed !");
    }           
}
