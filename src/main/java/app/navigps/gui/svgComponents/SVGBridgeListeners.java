package app.navigps.gui.svgComponents;

import app.database.odb.utils.ODBConnection;
import app.navigps.utils.NaviLogger;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderListener;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class SVGBridgeListeners extends SVGBridgeComponents implements 
                    SVGDocumentLoaderListener,
                    GVTTreeBuilderListener,
                    GVTTreeRendererListener	{
    
    /**
     *
     */
    public SVGBridgeListeners(){
    }

    /**
     *
     * @param e
     */
    @Override
    public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
        setRederingStatus(true);
        currentStatusChanged("Document Loading Started ...");
        NaviLogger.logger.log(Level.FINE,"Loading documnet Started ...",e);
    }
    /**
     *
     * @param e
     */
    @Override
    public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {	
        currentStatusChanged("Document Loading Completed");
        setSvgFileObject(new File(e.getSVGDocument().getDocumentURI()));
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //maybe this place to init DB is not good idea ? hmm
        String msg = ODBConnection.connect("", "");
        System.out.println(msg);
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        NaviLogger.logger.log(Level.FINE,"Loading documnet completed ",e);        
    }
    /**
     *
     * @param e
     */
    @Override
    public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
        NaviLogger.logger.log(Level.WARNING,"Loading documnet canceled !",e);
        currentStatusChanged("Document Loading Cancelled !");
    }
    /**
     *
     * @param e
     */
    @Override
    public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
        NaviLogger.logger.log(Level.WARNING,"Loading documnet failed !",e);
        currentStatusChanged("Document Loading Failed !");
    }
    
    
    /**
     *
     * @param e
     */
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
        NaviLogger.logger.log(Level.FINE,"GVT tree build completed !",e);
    }
    /**
     *
     * @param e
     */
    @Override
    public void gvtBuildCancelled(GVTTreeBuilderEvent e) {
        currentStatusChanged("Document Build Cancelled !");
        NaviLogger.logger.log(Level.WARNING,"GVT tree build cancelled !",e);
    }
    /**
     *
     * @param e
     */
    @Override
    public void gvtBuildFailed(GVTTreeBuilderEvent e) {
        currentStatusChanged("Document Build Failed !");
        NaviLogger.logger.log(Level.WARNING,"GVT tree build failed !",e);
    }
    /**
     *
     * @param e
     */
    @Override
    public void gvtBuildStarted(GVTTreeBuilderEvent e) {        
        currentStatusChanged("Documnet Build Started");
        NaviLogger.logger.log(Level.FINE,"GVT tree build started !",e);
    }    
    
    /**
     *
     * @param e
     */
    @Override
    public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
        currentStatusChanged("Document Rendering Prepare ...");
    }
    /**
     *
     * @param e
     */
    @Override
    public void gvtRenderingStarted(GVTTreeRendererEvent e) {
        currentStatusChanged("Document Rendering Started ...");
        setRederingStatus(true);
    }
    /**
     *
     * @param e
     */
    @Override
    public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
        currentStatusChanged("Document Rendering Completed");
        documentLoadingCompleted();
        setRederingStatus(false);
    }
    /**
     *
     * @param e
     */
    @Override
    public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
        currentStatusChanged("Documnet Rendering Cancelled !");
    }
    /**
     *
     * @param e
     */
    @Override
    public void gvtRenderingFailed(GVTTreeRendererEvent e) {
        currentStatusChanged("Documnet Rendering Failed !");
    }           
}
