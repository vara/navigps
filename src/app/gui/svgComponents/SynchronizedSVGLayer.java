package app.gui.svgComponents;

import app.gui.detailspanel.AlphaJPanel;
import app.utils.Utils;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.svg.SVGPoint;

/**
 *
 * @author wara
 */

public abstract class SynchronizedSVGLayer extends AlphaJPanel{

    protected Canvas svgCanvas;
    private TransformCanvasListener txListener = new TransformCanvasListener();

    /*
     * Var. needUpdate seted true when only svg document transform changed
     */
    protected boolean needUpdate = false;

    public SynchronizedSVGLayer(Canvas can){
        super(null);
        this.svgCanvas = can;
        setOpaque(false);
        can.addTranformListener(txListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform pt = svgCanvas.getPaintingTransform();
        if(pt!=null){
            AffineTransform orgT = g2.getTransform();
            orgT.concatenate(pt);
            g2.setTransform(orgT);
        }else{
            if(needUpdate) updateComponent();

        }
    }

    public SVGPoint[] getPointsRelativeToWindow() {
        final SVGOMPoint upperLeft = new SVGOMPoint(0, 0);
        final SVGOMPoint upperRight = new SVGOMPoint(this.getWidth(), 0);
        final SVGOMPoint lowerLeft = new SVGOMPoint(0, this.getHeight());
        return new SVGPoint[] {upperLeft, upperRight, lowerLeft};
    }

    public AffineTransform getTransform(){

        AffineTransform derivedTransform = new AffineTransform();
        SVGPoint[] docPoints = svgCanvas.getDocumentPoints();
        if(docPoints != null){
            final Point2D[] src = Utils.svgPointToPoint2D(docPoints);
            final Point2D[] dest = Utils.svgPointToPoint2D(getPointsRelativeToWindow());
            try {
                derivedTransform = Utils.deriveTransform(src, dest);
            } catch (NoninvertibleTransformException ex) {}
        }
        return derivedTransform;
    }

    public abstract void updateComponent();

    public class TransformCanvasListener extends TransformAdapter{
        @Override
        public void setRenderingTransform(AffineTransform rt) {
            needUpdate = true;
        }
    }
}
