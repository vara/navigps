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
import javax.swing.JToolBar;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

public class NaviToolBarLayout implements LayoutManager2,java.io.Serializable {

    Vector<JToolBar> arrayToolbars = new Vector<JToolBar>(5);

    private int hgap;
    private int vgap;

    public NaviToolBarLayout(){
        this.hgap = 0;
        this.vgap = 0;
    }

    public NaviToolBarLayout(int hgap,int vgap){
        this.hgap = hgap;
        this.vgap = vgap;
    }

    @Override
    public void addLayoutComponent(Component c, Object con) {
        synchronized (c.getTreeLock()) {
            c.setVisible(true);
            arrayToolbars.add((JToolBar)c);
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

            placeComponents(target, arrayToolbars, left, top, right - left,
                      bottom);
        }
    }

    // Returns the ideal width for a vertically oriented toolbar
    // and the ideal height for a horizontally oriented tollbar:
    private Dimension getPreferredDimension(Vector comps) {
        int w = 0, h = 0;
        for (int i = 0; i < comps.size(); i++) {
            Component c = (Component) (comps.get(i));
            Dimension d = c.getPreferredSize();
            w +=  d.width+hgap;
            h = Math.max(h, d.height);
        }
        return new Dimension(w, h);
    }

    private void placeComponents(Container target, Vector comps,
                               int x, int y, int w, int h) {
        //System.out.println("x: "+x+" y: "+y+" w: "+w+" h: "+h);
        //if(animatorCount != 0) return;
        Component c = null;
        
        int offset = x;
        int totalWidth = 0;
        int compWidth  = 0;
        int numOfComps=comps.size();

        for (int i = 0; i < numOfComps; i++) {
            c = (Component) (comps.get(i));            
            //System.err.println("Component name "+c.getName());
            int widthSwap=totalWidth;
            int compWidthSwap=compWidth;

            compWidth = c.getPreferredSize().width;
            totalWidth += compWidth;

            if (w < totalWidth && i != 0) {
                Component previousComp =(Component)(comps.get(i-1));
                //System.out.println("previousComp "+previousComp);
                Rectangle rec= previousComp.getBounds();
                Rectangle newBounds = new Rectangle(rec.x,rec.y,w-widthSwap+compWidthSwap,rec.height);                
                previousComp.setBounds(newBounds);
                offset = x;
                y += h;
                totalWidth = compWidth;
            }
            //last component
            if(i+1==numOfComps){
                //System.out.println("cx: "+(x + offset)+" cy: "+y+" cw: "+(w-totalWidth+compWidth)+" ch: "+h);
                
                Rectangle newRec = new Rectangle(x + offset, y, w-totalWidth+compWidth, h);
                c.setBounds(newRec);
                
            }else{
                Rectangle newRec = new Rectangle(x + offset, y, compWidth, h);
                c.setBounds(newRec);
                offset += compWidth+hgap;
                totalWidth+=hgap;
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
        
        System.out.println(getClass().getName()+" 'invalidateLayout'");
        arrayToolbars.clear();
        Component [] comps = target.getComponents();
        Component c;
        for (int i=0;i<comps.length;i++) {
            c = comps[i];
            if(c.isVisible()){
                arrayToolbars.add((JToolBar)c);
            }
        }
        //layoutContainer(target);
        
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