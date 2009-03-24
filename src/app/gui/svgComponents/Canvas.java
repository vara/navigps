package app.gui.svgComponents;

import app.gui.MainWindowIWD;
import app.gui.searchServices.SearchServices;
import app.gui.svgComponents.displayobjects.AbstractDisplayManager;
import app.gui.svgComponents.displayobjects.ComponentDisplayManager;
import app.gui.svgComponents.displayobjects.XMLDOMDisplayManager;
import config.SVGConfiguration;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGLocatable;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.svg.SVGPoint;

/**
 *
 * @author vara
 */

/* important "Rendering Process"
 * 
 *  The rendering process can be broken down into five phases. 
 *  Not all of those steps are required - depending on the method used to specify 
 *  the SVG document to display, but basically the steps in the rendering process are:
 *
 *  --Building a DOM tree 
 *	If the loadSVGDocument(String) method is used, the SVG file is parsed 
 *	and an SVG DOM Tree is built.
 * 
 *  --Building a GVT tree 
 *	Once an SVGDocument is created (using the step 1 or if the setSVGDocument(SVGDocument) 
 *	method has been used) - a GVT tree is constructed. 
 *	The GVT tree is the data structure used internally to render an SVG document. 
 *	see the org.apache.batik.gvt package.
 * 
 *  --Executing the SVGLoad event handlers 
 *	If the document is dynamic, the scripts are initialized and the SVGLoad 
 *	event is dispatched before the initial rendering. 
 * 
 *  --Rendering the GVT tree 
 *	Then the GVT tree is rendered. see the org.apache.batik.gvt.renderer package.
 * 
 *  --Running the document 
 *	If the document is dynamic, the update threads are started.
 */

public class Canvas extends JSVGCanvas{

    private SVGConfiguration svgConfig = new SVGConfiguration();
    
    private SVGBridgeComponents listeners = MainWindowIWD.
                        getBridgeInformationPipe();

    private MouseGestures mouseIteraction;
    
    //not used yet
    //private SVGUserAgentGUIAdapter agent;

    private SearchServices search;
    private AbstractDisplayManager dm;

    private boolean isFocus = false;

    private LinkedList<TransformAdapter> tranformListener =
            new LinkedList<TransformAdapter>();

    MouseListener requestFocus = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            if(!isFocus)
                requestFocusInWindow();             
        }
    };
    
    public Canvas(){
		super(null,false,false);
        //setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        setDocumentState(Canvas.ALWAYS_STATIC);
        setDoubleBuffered(true);
        //setDoubleBufferedRendering(true);

        mouseIteraction = new  MouseGestures();
        setLayout(new BorderLayout());
        search = new SearchServices(this);
        //add(search,BorderLayout.CENTER);
        
        //dm = new XMLDOMDisplayManager(this);
        dm = new ComponentDisplayManager(this);
        

        //setEnableZoomInteractor(false);
        //setEnableImageZoomInteractor(false);

        setRequestFocusOnWindow(true);
        addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                isFocus = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocus = false;
            }
        });

    }

    public AbstractDisplayManager getDisplayManager(){
        return dm;
    }

    /**
     *
     * @param pt
     */
    public void addTranformListener(TransformAdapter ta){
        tranformListener.add(ta);
    }

    @Override
    public void setPaintingTransform(AffineTransform at) {        
        super.setPaintingTransform(at);
        if(at!=null){
            final AffineTransform tmpAt = new AffineTransform(at);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Object o : tranformListener) {
                        ((PaintingTransformIterface)o).setPaintingTransform(tmpAt);
                    }
                }
            }).start();
        }
    }

    @Override
    public void setRenderingTransform(AffineTransform at) {
        final AffineTransform tmpAt = new AffineTransform(at);   
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Object o : tranformListener) {
                    ((RenderingTransformInterface)o).setRenderingTransform(tmpAt);
                }
            }
        }).start();        
        
        super.setRenderingTransform(at);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    public SVGPoint[] getWindowPoints() {
        final SVGOMPoint upperLeft = new SVGOMPoint(0, 0);
        final SVGOMPoint upperRight = new SVGOMPoint(this.getWidth(), 0);
        final SVGOMPoint lowerLeft = new SVGOMPoint(0, this.getHeight());
        return new SVGPoint[] {upperLeft, upperRight, lowerLeft};
    }

    /**
     * Maps the points of the corners of this JSVGCanvas to SVG document
     * coordinates.
     * @return An SVGPoint array of points representing document coordinates.
     */
    public SVGPoint[] getDocumentPoints() {
        if (this.getSVGDocument() != null) {
            final Node node = this.getSVGDocument().getDocumentElement();
            SVGMatrix matrix = ((SVGLocatable) node).getScreenCTM();
            if(matrix!=null){
                matrix = matrix.inverse();

                SVGPoint[] windowPoints = this.getWindowPoints();
                final SVGPoint derivedUpperLeft = windowPoints[0].matrixTransform(matrix);
                final SVGPoint derivedUpperRight = windowPoints[1].matrixTransform(matrix);
                final SVGPoint derivedLowerLeft = windowPoints[2].matrixTransform(matrix);
                /*
                System.out.println("Upper Left: " + windowPoints[0].getX() + ", " + windowPoints[0].getY());
                System.out.println("Upper Right: " + windowPoints[1].getX() + ", " + windowPoints[1].getY());
                System.out.println("Lower Left: " + windowPoints[2].getX() + ", " + windowPoints[2].getY());
                System.out.println("Document Upper Left: " + derivedUpperLeft.getX() + ", " + derivedUpperLeft.getY());
                System.out.println("Document Upper Right: " + derivedUpperRight.getX() + ", " + derivedUpperRight.getY());
                System.out.println("Document Lower Left: " + derivedLowerLeft.getX() + ", " + derivedLowerLeft.getY());
                */
                return new SVGPoint[] {derivedUpperLeft, derivedUpperRight, derivedLowerLeft};
            }
        }
        return null;
        
    }
    /**
     *
     * @param val
     */
    public void setRequestFocusOnWindow(boolean val){
        if(val){
            addMouseListener(requestFocus);
        }else{
            removeMouseListener(requestFocus);
        }
    }

    public SearchServices getSearchServices(){
        return search;
    }

    /**
     *
     * @return
     */
    public boolean isDocumentSet(){
        return (getSVGDocument() != null);
    }
    
    /**
     *
     * @param zoomIn
     */
    public void zoomFromCenterDocumnet(boolean zoomIn){
        mouseIteraction.zoomFromCenterDocumnet(zoomIn);
        mouseIteraction.setMode(MouseGestures.ZOOM_ACTION);
    }

    /**
     *
     * @param setZoom
     */
    public void zoomFromMouseCoordinationEnable(boolean setZoom){

        if(setZoom){
            addMouseListener(mouseIteraction);
            addMouseMotionListener(mouseIteraction);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else{
            setCursor(Cursor.getDefaultCursor());
            removeMouseListener(mouseIteraction);
            removeMouseMotionListener(mouseIteraction);
        }
        mouseIteraction.setMode(MouseGestures.ZOOM_ACTION);
    }

    private class MouseGestures extends MouseAdapter{

        private int xStart;
        private int yStart;
        private boolean drag=false;

        public static final byte ZOOM_ACTION   =0;
        public static final byte PAN_ACTION    =1;
        public static final byte ROTATE_ACTION =2;

        //none mode
        private byte mode = -1;

        @Override
        public void mouseClicked(MouseEvent evt) {            
            Point2D p2d = new Point2D.Double(evt.getX(),evt.getY());
            int button = evt.getButton();
            if(button==MouseEvent.BUTTON1){
                renderingZoom(p2d,svgConfig.getZoomInRate());
                System.out.println("Zoom In\n\tPoint "+p2d+
                                "\n\tRate ["+svgConfig.getZoomInRateX()+","+svgConfig.getZoomInRateY()+"]");
            }
            else if(button == MouseEvent.BUTTON3){
                renderingZoom(p2d,svgConfig.getZoomOutRate());
                System.out.println("Zoom Out\n\tPoint "+p2d+
                                "\n\tRate ["+svgConfig.getZoomOutRateX()+","+svgConfig.getZoomOutRateY()+"]");
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            xStart = e.getX();
            yStart = e.getY();
            if(e.getButton() == MouseEvent.BUTTON1 && !e.isShiftDown()){
                drag=true;                
            }                
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(drag){
                drag=false;
                AffineTransform pt = getPaintingTransform();
                if (pt != null) {
                    AffineTransform rt = (AffineTransform)getRenderingTransform();
                    pt.concatenate(rt);
                    setRenderingTransform(pt);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {            
            if(drag && getMode()==MouseGestures.ZOOM_ACTION){
                int dy = e.getY() - yStart;
                double s;
                if (dy < 0) {
                    dy -= 10;
                    s = (dy > -15) ? 1.0 : -15.0/dy;
                } else {
                    dy += 10;
                    s = (dy <  15) ? 1.0 : dy/15.0;
                }
                paintingZoom(new Point(xStart, yStart), new Point2D.Double(s, s));
            }
        }

        public void paintingZoom(Point2D translate,Point2D scale){
            AffineTransform at = getZoomTransform(translate, scale);            
            setPaintingTransform(at);
        }

        public void renderingZoom(Point2D translate,Point2D scale){

            AffineTransform rat = getRenderingTransform();
            if(rat!=null){
                AffineTransform at = getZoomTransform(translate, scale);
                at.concatenate(rat);
                setRenderingTransform(at);
            }
        }

        private AffineTransform getZoomTransform(Point2D translate,Point2D scale){            
            AffineTransform t = AffineTransform.getTranslateInstance(translate.getX(),translate.getY());
            t.concatenate(AffineTransform.getScaleInstance(scale.getX(),scale.getY()));                
            t.translate(-translate.getX(),-translate.getY());            
            return t;
        }

        public void zoomFromCenterDocumnet(boolean zoomIn){
            Point2D translate = new Point(getSize().width>>1,getSize().height>>1);
            Point2D scale;
            if(zoomIn) scale= svgConfig.getZoomInRate();
            else scale= svgConfig.getZoomOutRate();
            renderingZoom(translate, scale);
        }

        /**
         * @return the mode
         */
        public byte getMode() {
            return mode;
        }

        /**
         * @param mode the mode to set
         */
        public void setMode(byte mode) {
            this.mode = mode;
        }
    }//class MouseGestures

    /**
     *
     */
    protected class ThumbnailAction extends AbstractAction{
        /**
         *
         * @param mnemonic
         */
        public ThumbnailAction(Integer mnemonic) {
            super();

            putValue(AbstractAction.MNEMONIC_KEY, mnemonic);
            putValue(AbstractAction.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke(mnemonic,InputEvent.SHIFT_DOWN_MASK|InputEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
    }
}
