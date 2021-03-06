/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.svgComponents;

import app.navigps.gui.svgComponents.Thumbnail.ThumbnailPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 *
 * @author wara
 */
public class SVGLayerScrollPane extends SVGScrollPane{

    /**
     *
     * @param svgCanvasL
     */
    public SVGLayerScrollPane(SVGCanvasLayers svgCanvasL){
        
        this.canvas = svgCanvasL.getSvgCanvas();
        canvas.setRecenterOnResize(false);

        // create components
        vertical   = new JScrollBar(JScrollBar.VERTICAL,   0, 0, 0, 0);
        horizontal = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 0);

        // create a spacer next to the horizontal bar
        horizontalPanel = new JPanel(new BorderLayout());
        horizontalPanel.add(horizontal, BorderLayout.CENTER);
        cornerBox = Box.createRigidArea
            (new Dimension(vertical.getPreferredSize().width,
                           horizontal.getPreferredSize().height));
        horizontalPanel.add(cornerBox, BorderLayout.EAST);

        // listeners
        hsbListener = createScrollBarListener(false);
        horizontal.getModel().addChangeListener(hsbListener);

        vsbListener = createScrollBarListener(true);
        vertical.getModel().addChangeListener(vsbListener);

        // by default, scrollbars are not needed
        updateScrollbarState(false, false);

        addMouseWheelListener(new WheelListener());

        // layout
        setLayout(new BorderLayout());
        add(svgCanvasL, BorderLayout.CENTER);
        add(vertical, BorderLayout.EAST);
        add(horizontalPanel, BorderLayout.SOUTH);

        // inform of ZOOM events (to print sizes, such as in a status bar)
        canvas.addSVGDocumentLoaderListener(createLoadListener());

        // canvas listeners
        ScrollListener xlistener = createScrollListener();
        addComponentListener(xlistener);
        canvas.addGVTTreeRendererListener(xlistener);
        canvas.addJGVTComponentListener  (xlistener);
        canvas.addGVTTreeBuilderListener (xlistener);
        canvas.addUpdateManagerListener  (xlistener);
    }

    /**
     *
     * @param scale
     */
    @Override
    public void scaleChange(float scale) {
        super.scaleChange(scale);
        SVGCanvasLayers parent = (SVGCanvasLayers)canvas.getParent();
        Component[] comp = parent.getThumbnailContainer().getComponents();
        for (int i = 0; i < comp.length; i++) {
            Component component = comp[i];
            if(component instanceof ThumbnailPanel){
                ThumbnailPanel tmp = (ThumbnailPanel)component;
                tmp.getThumbnail().updateThumbnailRenderingTransform();
            }
        }
    }
}
