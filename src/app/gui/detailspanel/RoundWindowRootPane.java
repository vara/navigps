package app.gui.detailspanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.JLayeredPane;

/**
 *
 * @author wara
 */
public class RoundWindowRootPane extends RoundJPanel{

    private RoundJPanel glassPane;
    private ContentPaneForRoundWindow contentPane;
    private JLayeredPane layeredPane;

    /**
     *
     */
    public RoundWindowRootPane(){
        getRoundBorder().setBorderInsets(new Insets(0,0,0,0));
        setGlassPane(createGlassPane());
        setLayeredPane(createLayeredPane());
        setContentPane(createContentPane());
        setLayout(createRootLayout());
        updateUI();
    }
    
    /*
     *  Methods to created all Root Pane
     *
     */

    /**
     *
     * @return
     */
    protected ContentPaneForRoundWindow createContentPane(){
        ContentPaneForRoundWindow cp = new ContentPaneForRoundWindow(){

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        cp.setName(this.getName()+".contentPane");        
        return cp;
    }

    /**
     *
     * @return
     */
    protected RoundJPanel createGlassPane() {
        RoundJPanel gp = new RoundJPanel();
        gp.setName(this.getName()+".glassPane");
        gp.setVisible(false);
        gp.setOpaque(false);
        return gp;
    }

    /**
     *
     * @return
     */
    protected JLayeredPane createLayeredPane() {
        JLayeredPane p = new JLayeredPane();
        p.setName(this.getName()+".layeredPane");
        return p;
    }

    /**
     *
     * @return
     */
    protected LayoutManager createRootLayout() {
        return new RootLayout();
    }
    
    /************************************************
     * @return
     */

    public JLayeredPane getLayeredPane(){
        return layeredPane;
    }

    /**
     *
     * @param layered
     */
    public void setLayeredPane(JLayeredPane layered) {
        if(layered == null)
            throw new IllegalComponentStateException("layeredPane cannot be set to null.");
        if(layeredPane != null && layeredPane.getParent() == this)
            this.remove(layeredPane);
        layeredPane = layered;

        this.add(layeredPane, -1);
    }

    /**
     * @return the glassPane
     */
    public RoundJPanel getGlassPane() {
        return glassPane;
    }

    /**
     * @param glass
     */
    public void setGlassPane(RoundJPanel glass) {
        if (glass == null) {
            throw new NullPointerException("glassPane cannot be set to null.");
        }

        boolean visible = false;
        if (glassPane != null && glassPane.getParent() == this) {
            this.remove(glassPane);
            visible = glassPane.isVisible();
        }
        glass.setVisible(visible);
        glassPane = glass;
        this.add(glassPane, 0);
        if (visible) {
            repaint();
        }
    }

    /**
     * @return the contentPane
     */
    public ContentPaneForRoundWindow getContentPane() {
        return contentPane;
    }

    /**
     * @param content
     */
    public void setContentPane(ContentPaneForRoundWindow content) {
        if(content == null)
            throw new IllegalComponentStateException("contentPane cannot be set to null.");
        if(contentPane != null && contentPane.getParent() == layeredPane)
            layeredPane.remove(contentPane);
        contentPane = content;

        layeredPane.add(contentPane, JLayeredPane.FRAME_CONTENT_LAYER);
    }

    /* check this "SystemEventQueueUtilities.addRunnableCanvas(this) !!!!!!!"
    @Override
    public void addNotify() {
	SystemEventQueueUtilities.addRunnableCanvas(this);
        super.addNotify();
        enableEvents(AWTEvent.KEY_EVENT_MASK);
    }
     */

    /**
     *
     * @param comp
     * @param constraints
     * @param index
     */
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        /// We are making sure the glassPane is on top.
        if(glassPane != null
            && glassPane.getParent() == this
            && getComponent(0) != glassPane) {
            add(glassPane, 0);
        }
    }

    /**
     *
     */
    protected class RootLayout implements LayoutManager2, Serializable
    {
        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param parent the Container for which this layout manager
         * is being used
         * @return a Dimension object containing the layout's preferred size
         */
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension rd, mbd;
            Insets i = getInsets();

            if(contentPane != null) {
                rd = contentPane.getPreferredSize();
            } else {
                rd = parent.getSize();
            }
            /*
            if(menuBar != null && menuBar.isVisible()) {
                mbd = menuBar.getPreferredSize();
            } else {
                mbd = new Dimension(0, 0);
            }
             */
            mbd = new Dimension(0, 0);
            return new Dimension(Math.max(rd.width, mbd.width) + i.left + i.right,
                                        rd.height + mbd.height + i.top + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param parent the Container for which this layout manager
         * is being used
         * @return a Dimension object containing the layout's minimum size
         */
        @Override
        public Dimension minimumLayoutSize(Container parent) {
            Dimension rd, mbd;
            Insets i = getInsets();
            if(contentPane != null) {
                rd = contentPane.getMinimumSize();
            } else {
                rd = parent.getSize();
            }
            /*
            if(menuBar != null && menuBar.isVisible()) {
                mbd = menuBar.getMinimumSize();
            } else {
                mbd = new Dimension(0, 0);
            }
             */
            mbd = new Dimension(0, 0);
            return new Dimension(Math.max(rd.width, mbd.width) + i.left + i.right,
                        rd.height + mbd.height + i.top + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param target the Container for which this layout manager
         * is being used
         * @return a Dimension object containing the layout's maximum size
         */
        @Override
        public Dimension maximumLayoutSize(Container target) {
            Dimension rd, mbd;
            Insets i = getInsets();
            /*
            if(menuBar != null && menuBar.isVisible()) {
                mbd = menuBar.getMaximumSize();
            } else {
                mbd = new Dimension(0, 0);
            }
             */
            mbd = new Dimension(0, 0);
            if(contentPane != null) {
                rd = contentPane.getMaximumSize();
            } else {
                // This is silly, but should stop an overflow error
                rd = new Dimension(Integer.MAX_VALUE,
                        Integer.MAX_VALUE - i.top - i.bottom - mbd.height - 1);
            }
            return new Dimension(Math.min(rd.width, mbd.width) + i.left + i.right,
                                         rd.height + mbd.height + i.top + i.bottom);
        }

        /**
         * Instructs the layout manager to perform the layout for the specified
         * container.
         *
         * @param parent the Container for which this layout manager
         * is being used
         */
        @Override
        public void layoutContainer(Container parent) {
            Rectangle b = parent.getBounds();
            Insets i = getInsets();
            int contentY = 0;
            int w = b.width - i.right - i.left;
            int h = b.height - i.top - i.bottom;

            if(layeredPane != null) {
                layeredPane.setBounds(i.left, i.top, w, h);
            }
            if(glassPane != null) {
                glassPane.setBounds(i.left, i.top, w, h);
            }
            // Note: This is laying out the children in the layeredPane,
            // technically, these are not our children.
            /*
            if(menuBar != null && menuBar.isVisible()) {
                Dimension mbd = menuBar.getPreferredSize();
                menuBar.setBounds(0, 0, w, mbd.height);
                contentY += mbd.height;
            }
             */
            if(contentPane != null) {
                contentPane.setBounds(0, contentY, w, h - contentY);
            }
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {}
        @Override
        public void removeLayoutComponent(Component comp) {}
        @Override
        public void addLayoutComponent(Component comp, Object constraints) {}
        @Override
        public float getLayoutAlignmentX(Container target) { return 0.0f; }
        @Override
        public float getLayoutAlignmentY(Container target) { return 0.0f; }
        @Override
        public void invalidateLayout(Container target) {}
    }
}
