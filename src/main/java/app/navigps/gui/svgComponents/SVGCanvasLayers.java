package app.navigps.gui.svgComponents;

import app.navigps.gui.detailspanel.AlphaJPanel;
import app.navigps.gui.detailspanel.SimpleBusyPanel;
import app.navigps.gui.svgComponents.Thumbnail.Thumbnail;
import app.navigps.gui.svgComponents.Thumbnail.ThumbnailPanel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;

/**
 *
 * @author Gzregorz (vara) Warywoda
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

    private AlphaJPanel componentContainer;
    private AlphaJPanel modalContainer;
    private AlphaJPanel thumbnailContainer;
    private AlphaJPanel servicesContainer;
    private SimpleBusyPanel glassPane;

    /**
     *
     */
    public SVGCanvasLayers(){

        setLayout(new OverlayLayout(this));

        setSVGCanvas(createSVGCanvas(), JLayeredPane.FRAME_CONTENT_LAYER);
        createDefaultContainers();
    }

    private void createGlasspane(){
        setGlassPane(new CanvasBusyPanel());
        getSvgCanvas().addSVGDocumentLoaderListener((CanvasBusyPanel)getGlassPane());
        getSvgCanvas().addGVTTreeRendererListener((CanvasBusyPanel)getGlassPane());
        getSvgCanvas().addGVTTreeRendererListener((CanvasBusyPanel)getGlassPane());
        add(getGlassPane(),POPUP_LAYER);
    }

    private void createDefaultContainers(){
        modalContainer = new AlphaJPanel(null);
        getModalContainer().setOpaque(false);        

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

        createGlasspane();
    }

    public void updateSynchronizedLayers(){
        Component [] comps = getComponents();
        //for test
        int countSynchLayers=0;        
        for (Component comp : comps) {
            if(comp instanceof SynchronizedSVGLayer){
                ((SynchronizedSVGLayer)comp).updateComponent();
                countSynchLayers++;
            }
        }
        String msg = "***Update synchronized layers.\nFinished !\nStatus:\n\tAll components: "+
                     comps.length+" including Synchronized layers: "+
                     countSynchLayers+"\n****";

        System.out.println(msg);
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
     * @return the glassPane
     */
    public SimpleBusyPanel getGlassPane() {
        return glassPane;
    }

    /**
     * @param glassPane the glassPane to set
     */
    public void setGlassPane(SimpleBusyPanel glassPane) {
        this.glassPane = glassPane;
    }
}
