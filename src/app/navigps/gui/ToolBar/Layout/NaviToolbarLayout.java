/*
 * NaviToolbarLayout.java
 *
 * Created on 2009-04-15, 17:39:46
 */

package app.navigps.gui.ToolBar.Layout;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
import java.awt.*;
import java.util.*;

public class NaviToolbarLayout implements LayoutManager2,java.io.Serializable {

    private LinkedList arrayToolbars = new LinkedList();

    private int hgap;
    private int vgap;

    public NaviToolbarLayout(){
        this.hgap = 0;
        this.vgap = 0;
    }

    public NaviToolbarLayout(int hgap,int vgap){
        this.hgap = hgap;
        this.vgap = vgap;
    }

    @Override
    public void addLayoutComponent(Component c, Object con) {
        synchronized (c.getTreeLock()) {
            c.setVisible(true);
            arrayToolbars.add(c);
            c.getParent().validate();
        }
    }

    @Override
    public void removeLayoutComponent(Component c) {
        arrayToolbars.remove(c);
        c.getParent().validate();
    }

    @Override
    public void layoutContainer(Container target) {

        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int top = insets.top;
            int bottom = target.getHeight() - insets.bottom;
            int left = insets.left;
            int right = target.getWidth() - insets.right;

            //int height = getPreferredDimension(arrayToolbars).height;


            placeComponents(target, arrayToolbars, left, top, right - left,
                      bottom);
        }
    }

    // Returns the ideal width for a vertically oriented toolbar
    // and the ideal height for a horizontally oriented tollbar:
    private Dimension getPreferredDimension(LinkedList comps) {
        int w = 0, h = 0;
        for (int i = 0; i < comps.size(); i++) {
            Component c = (Component) (comps.get(i));
            Dimension d = c.getPreferredSize();
            w +=  d.width+hgap;
            h = Math.max(h, d.height);
        }
        return new Dimension(w, h);
    }

    private void placeComponents(Container target, LinkedList comps,
                               int x, int y, int w, int h) {
        //System.out.println("x: "+x+" y: "+y+" w: "+w+" h: "+h);
        int offset = 0;
        Component c = null;
        offset = x;

        int totalWidth = 0;
        int cwidth=0;
        int num=comps.size();

        for (int i = 0; i < num; i++) {
            c = (Component) (comps.get(i));
            if(c.isVisible()){
                int widthSwap=totalWidth;
                int cwidthSwap=cwidth;
                cwidth = c.getPreferredSize().width;
                totalWidth += cwidth;
                if (w < totalWidth && i != 0) {
                    Component c0=(Component)(comps.get(i-1));
                    Rectangle rec=c0.getBounds();
                    c0.setBounds(rec.x,rec.y,w-widthSwap+cwidthSwap,rec.height);
                    offset = x;
                    y += h;
                    totalWidth = cwidth;
                }
                //last component
                if(i+1==num){
                    System.out.println("cx: "+(x + offset)+" cy: "+y+" cw: "+(w-totalWidth+cwidth)+" ch: "+h);
                    c.setBounds(x + offset, y, w-totalWidth+cwidth, h);
                }else{
                    c.setBounds(x + offset, y, cwidth, h);
                    offset += cwidth;
                }
            }
        }

    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return .5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return .5f;
    }

    @Override
    public void invalidateLayout(Container target) {
        //System.out.println(getClass().getName()+" 'invalidateLayout'");
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        //System.out.println(getClass().getName()+" 'addLayoutComponent(String name, Component comp)'");
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = getPreferredDimension(arrayToolbars);
        Insets ins = parent.getInsets();
        dim.width+= (ins.left+ins.right);
        dim.height+= (ins.top+ins.bottom);
        return dim;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    /**
     * @return the hgap
     */
    public int getHgap() {
        return hgap;
    }

    /**
     * @param hgap the hgap to set
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * @return the wgap
     */
    public int getVgap() {
        return vgap;
    }

    /**
     * @param wgap the wgap to set
     */
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }
}