/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import app.gui.detailspanel.AlphaJPanel;
import app.gui.detailspanel.RoundWindow;
import app.gui.detailspanel.RoundWindowUtils;
import app.gui.svgComponents.thumbnail.Thumbnail;
import app.gui.svgComponents.thumbnail.ThumbnailPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;

/**
 *
 * @author wara
 */
public class SVGCanvasLayers extends JLayeredPane{

    public static final String SVG_CANVAS_CHANGED = "svgcanvas.changed";

    private Canvas svgCanvas;

    private AlphaJPanel componentContainer;
    private AlphaJPanel modalContainer;

    public SVGCanvasLayers(){

        setLayout(new OverlayLayout(this));

        setSVGCanvas(createSVGCanvas(), JLayeredPane.FRAME_CONTENT_LAYER);
        createDefaultContainers();
    }

    private void createDefaultContainers(){
        modalContainer = new AlphaJPanel(null);
        getModalContainer().setOpaque(false);
        createRoundWindowProperties();

        componentContainer = new AlphaJPanel(null);
        getComponentContainer().setOpaque(false);
        createThumbnails();

        add(getModalContainer(),MODAL_LAYER);
        add(getComponentContainer(),DEFAULT_LAYER);
    }

    private void createRoundWindowProperties(){
        RoundWindow detailsPane = new RoundWindow();
        detailsPane.setDynamicRevalidate(true);
        detailsPane.setUpperThresholdAlpha(0.6f);
        detailsPane.setAlpha(0.0f);
        detailsPane.getContentPane().setUpperThresholdAlpha(0.75f);
        detailsPane.setVisible(false);
        getModalContainer().add(detailsPane);
        getModalContainer().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Container cont = RoundWindowUtils.getRoundWindowFromContainer((Container)e.getSource());
                if(cont != null){
                    ((RoundWindow)cont).updateMyUI();
                }
            }
        });
    }

    private void createThumbnails(){
        Thumbnail thumb = new Thumbnail(svgCanvas);
        thumb.setAlpha(0.0f);
        thumb.setUpperThresholdAlpha(0.77f);
        ThumbnailPanel tmp = new ThumbnailPanel(thumb);
        tmp.setUpperThresholdAlpha(0.4f);
        tmp.setAlpha(0.0f);
        tmp.setBackground(Color.BLACK);
        tmp.setBounds(50, 50, 200, 100);
        getComponentContainer().add(tmp);
        tmp.displayThumbnail(true);
    }

    private Canvas createSVGCanvas(){
        Canvas can = new Canvas();
        return can;
    }

    private void setSVGCanvas(Canvas can,Integer layer){
        this.svgCanvas = can;
        add(can,layer);
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if(comp instanceof JComponent){
            ((JComponent)comp).setOpaque(false);
        }
        super.addImpl(comp, constraints, index);
    }

    /**
     * @return the svgCanvas
     */
    public Canvas getSvgCanvas() {        
        return svgCanvas;
    }

    /**
     * @param svgCanvas the svgCanvas to set
     */
    public void setSvgCanvas(Canvas svgCanvas) {
        Canvas oldVal = this.svgCanvas;
        this.svgCanvas = svgCanvas;
        firePropertyChange(SVG_CANVAS_CHANGED, oldVal, this.svgCanvas);
    }

    /**
     * @return the componentContainer
     */
    public AlphaJPanel getComponentContainer() {
        return componentContainer;
    }

    /**
     * @return the modalContainer
     */
    public AlphaJPanel getModalContainer() {
        return modalContainer;
    }
}
