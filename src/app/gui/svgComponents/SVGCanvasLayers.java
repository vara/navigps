/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

//import tests.layers.*;
import app.gui.detailspanel.AlphaJPanel;
import app.gui.svgComponents.Canvas;
import java.awt.Component;
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

        componentContainer = new AlphaJPanel(null);
        getComponentContainer().setOpaque(false);

        add(getModalContainer(),MODAL_LAYER);
        add(getComponentContainer(),DEFAULT_LAYER);
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
