/*
 * JXLayerContentPane.java
 *
 * Created on 2009-04-07, 15:35:05
 */

package app.navigps.gui.detailspanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;
import org.jdesktop.jxlayer.plaf.LayerUI;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class JXLayerContentPane extends ContentPaneForRoundWindow{

    private JXLayer<AlphaJPanel> jxlayer;

    private AlphaJPanel contentPane;

    public JXLayerContentPane(){

        contentPane = new AlphaJPanel();
        contentPane.setOpaque(false);

        setLayout(new BorderLayout());
        add(new JXLayer<AlphaJPanel>(),BorderLayout.CENTER);

        jxlayer.setView(contentPane);
        //jxlayer.addPropertyChangeListener();
    }

    public void setJXLayerUI(LayerUI ui){
        ((AbstractLayerUI)ui).
                addPropertyChangeListener("locked",new JXLayerLockedListener());
        jxlayer.setUI(ui);
    }

    public LayerUI getLayerUI(){
        return jxlayer.getUI();
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if(comp instanceof JXLayer){
            super.remove(comp);
            super.addImpl(comp, constraints, 0);
            jxlayer = (JXLayer)comp;
        }else{
            contentPane.add(comp, constraints);
            
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        contentPane.paintComponent(g);
    }

    @Override
    public boolean setAlpha(float alpha) {
        super.setAlpha(alpha);
        return contentPane.setAlpha(alpha);
    }

    @Override
    public float getAlpha() {
        return contentPane.getAlpha();
    }
/*
    @Override
    public void removeAll() {
        contentPane.removeAll();
    }

    @Override
    public void remove(int index) {
        contentPane.remove(index);
    }

    @Override
    public Component getComponent(int n) {
        return contentPane.getComponent(n);
    }

    @Override
    public Component[] getComponents() {
        return contentPane.getComponents();
    }
*/
    private class JXLayerLockedListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if( !((Boolean)evt.getNewValue()) ){
                System.err.println("Locked down !");
                //contentPane.repaint();
                //jxlayer.repaint();
            }
        }
    }
}
