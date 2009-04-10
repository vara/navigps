/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CanvasBusyPanel.java
 *
 * Created on 2009-04-06, 03:55:48
 */

package app.navigps.gui.svgComponents;

import app.navigps.gui.detailspanel.SimpleBusyPanel;
import java.awt.Insets;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderListener;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderListener;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class CanvasBusyPanel extends SimpleBusyPanel implements
                                    SVGDocumentLoaderListener,
                                    GVTTreeBuilderListener,
                                    GVTTreeRendererListener{

    public CanvasBusyPanel(){
        setInsets(new Insets(0,0,0,0));
        setOuterCorners(0,0);
    }
    
    @Override
    public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
        setBusy(true);
    }

    @Override
    public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
    }

    @Override
    public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
        setBusy(false);
    }

    @Override
    public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
        setBusy(false);
    }

    @Override
    public void gvtBuildStarted(GVTTreeBuilderEvent e) {
    }

    @Override
    public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
    }

    @Override
    public void gvtBuildCancelled(GVTTreeBuilderEvent e) {
        //setBusy(false);
    }

    @Override
    public void gvtBuildFailed(GVTTreeBuilderEvent e) {
        setBusy(false);
    }

    @Override
    public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
    }

    @Override
    public void gvtRenderingStarted(GVTTreeRendererEvent e) {
        //setBusy(true);
    }

    @Override
    public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
        setBusy(false);
    }

    @Override
    public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
        setBusy(false);
    }

    @Override
    public void gvtRenderingFailed(GVTTreeRendererEvent e) {
        setBusy(false);
    }
}
