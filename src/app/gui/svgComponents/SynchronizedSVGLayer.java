package app.gui.svgComponents;

import app.gui.detailspanel.AlphaJPanel;
import app.gui.svgComponents.displayobjects.ObjectService;
import app.utils.Utils;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.svg.SVGPoint;

/**
 *
 * @author wara
 */

public class SynchronizedSVGLayer extends AlphaJPanel{

    protected Canvas can;
    private TransformCanvasListener txListener = new TransformCanvasListener();

    public SynchronizedSVGLayer(Canvas can){
        super(null);
        this.can = can;
        setOpaque(false);
        can.addTranformListener(txListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public SVGPoint[] getWindowPoints() {
        final SVGOMPoint upperLeft = new SVGOMPoint(0, 0);
        final SVGOMPoint upperRight = new SVGOMPoint(this.getWidth(), 0);
        final SVGOMPoint lowerLeft = new SVGOMPoint(0, this.getHeight());
        return new SVGPoint[] {upperLeft, upperRight, lowerLeft};
    }

    public AffineTransform getTransform(){

        AffineTransform derivedTransform = new AffineTransform();
        SVGPoint[] docPoints = can.getDocumentPoints();
        if(docPoints != null){
            final Point2D[] src = Utils.svgPointToPoint2D(docPoints);
            final Point2D[] dest = Utils.svgPointToPoint2D(getWindowPoints());
            try {
                derivedTransform = Utils.deriveTransform(src, dest);
            } catch (NoninvertibleTransformException ex) {}
        }
        return derivedTransform;
    }

    public void updateComponentsCoordinates(){

        AffineTransform at = getTransform();
        if( !(at.equals(new AffineTransform())) ){

            Component [] comps = getComponents();
            for (Component c : comps) {
                ((ObjectService)c).transformCoordinate(at);
            }
        }
    }

    public class TransformCanvasListener extends TransformAdapter{
        @Override
        public void setRenderingTransform(AffineTransform rt) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateComponentsCoordinates();
                }
            });
        }

        @Override
        public void setPaintingTransform(AffineTransform pt) {
        }
    }
}
