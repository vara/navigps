/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.navigps.gui.svgComponents;

import app.navigps.gui.detailspanel.AlphaJPanel;
import app.navigps.gui.detailspanel.RoundWindow;
import app.navigps.gui.detailspanel.RoundWindowUtils;
import app.navigps.gui.svgComponents.Thumbnail.Thumbnail;
import app.navigps.gui.svgComponents.Thumbnail.ThumbnailPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;

/**
 *
 * @author wara
 */
public class SVGCanvasLayers extends JLayeredPane{

    /**
     *
     */
    public static final String SVG_CANVAS_CHANGED = "svgcanvas.changed";
    /**
     *
     */
    public static final Integer DISPLAY_SERVICES_LAYER = new Integer(JLayeredPane.FRAME_CONTENT_LAYER+50);
    /**
     *
     */
    public static final Integer SEARCH_SERVICES_LAYER = new Integer(JLayeredPane.FRAME_CONTENT_LAYER+100);
    /**
     *
     */
    public static final Integer THUMBNAIL_LAYER = new Integer(JLayeredPane.DEFAULT_LAYER+10);

    private Canvas svgCanvas;
    private RoundWindow detailsPane;

    private AlphaJPanel componentContainer;
    private AlphaJPanel modalContainer;
    private AlphaJPanel thumbnailContainer;
    private AlphaJPanel servicesContainer;

    /**
     *
     */
    public SVGCanvasLayers(){

        setLayout(new OverlayLayout(this));

        setSVGCanvas(createSVGCanvas(), JLayeredPane.FRAME_CONTENT_LAYER);
        createDefaultContainers();
    }

    private void createDefaultContainers(){
        modalContainer = new AlphaJPanel(null);
        getModalContainer().setOpaque(false);
        detailsPane = createRoundWindowProperties();
        getModalContainer().add(getDetailsPane());
        getModalContainer().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Container cont = RoundWindowUtils.getRoundWindowFromContainer((Container)e.getSource());
                if(cont != null){
                    ((RoundWindow)cont).updateMyUI();
                }
            }
        });

        componentContainer = new AlphaJPanel(null);
        getComponentContainer().setOpaque(false);

        thumbnailContainer = new AlphaJPanel(null);
        getThumbnailContainer().setOpaque(false);

        setServicesContainer(new ServicesContainer(getSvgCanvas()));
        getServicesContainer().setAlpha(0.6f);

        add(getModalContainer(),MODAL_LAYER);
        add(getComponentContainer(),DEFAULT_LAYER);
        add(getThumbnailContainer(),THUMBNAIL_LAYER);
        add(getServicesContainer(),DISPLAY_SERVICES_LAYER);

        createThumbnails();
    }

    private RoundWindow createRoundWindowProperties(){
        RoundWindow rw = new RoundWindow();
        rw.setDynamicRevalidate(true);
        rw.setUpperThresholdAlpha(0.6f);
        rw.setAlpha(0.0f);
        rw.getContentPane().setUpperThresholdAlpha(0.75f);
        rw.setVisible(false);
        return rw;
    }

    private void createThumbnails(){
        Thumbnail thumb = new Thumbnail(svgCanvas);
        thumb.setAlpha(0.0f);
        thumb.setUpperThresholdAlpha(0.65f);
        ThumbnailPanel tmp = new ThumbnailPanel(thumb);
        tmp.setUpperThresholdAlpha(0.65f);
        tmp.setAlpha(0.0f);
        tmp.setBackground(Color.BLACK);
        tmp.setBounds(50, 50, 200, 100);        
        getThumbnailContainer().add(tmp);
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
        /*if(comp instanceof JComponent){
            ((JComponent)comp).setOpaque(false);
        }
         */
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

    /**
     * @return the thumbnailContainer
     */
    public AlphaJPanel getThumbnailContainer() {
        return thumbnailContainer;
    }

    /**
     * @return the servicesContainer
     */
    public AlphaJPanel getServicesContainer() {
        return servicesContainer;
    }

    /**
     * @param servicesContainer the servicesContainer to set
     */
    public void setServicesContainer(AlphaJPanel servicesContainer) {
        this.servicesContainer = servicesContainer;
    }

    /**
     * @return the detailsPane
     */
    public RoundWindow getDetailsPane() {
        return detailsPane;
    }
}
