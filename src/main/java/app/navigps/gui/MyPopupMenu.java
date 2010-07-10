package app.navigps.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class MyPopupMenu extends JPopupMenu{

    private Timer closeTimer;
    private int closeDelay = 5000;

    private PopupMouseListener mouseListenr = new PopupMouseListener();

    public MyPopupMenu(){
        this(null);
    }
    
    public MyPopupMenu(String title){
        super(title);
        setLightWeightPopupEnabled(true);
        setAlignmentX(100);

        init();
    }

    private void init(){
        initialTimer();
        installListeners();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        uninstallMaouseListener();
        closeTimer = null;
        System.err.println("MyPopupMenu "+getName()+" finalize !!!");
    }

    private void initialTimer(){

        closeTimer = new Timer(closeDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isVisible())
                    setVisible(false);
            }
        });

        closeTimer.setRepeats(false);
    }   


    private void installListeners(){

        addMouseListener(mouseListenr);
        addMouseMotionListener(mouseListenr);
    }

    private void uninstallMaouseListener(){

        removeMouseListener(mouseListenr);
        removeMouseMotionListener(mouseListenr);
    }

    public void setTimeToClose(int closeInit){
        closeDelay = closeInit;
        closeTimer.setDelay(closeDelay);
    }

    public void addToPopup(JMenuItem [] item){
        if(item.length>0){
            for(int i=0;i<item.length;i++)
                add(item[i]);
        }
    }

    @Override
    public void show(Component invoker, int x, int y) {
        super.show(invoker, x, y);        
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        //System.out.println("***"+e);
    }

    @Override
    public void processMouseEvent(MouseEvent event, MenuElement[] path, MenuSelectionManager manager) {
        super.processMouseEvent(event, path, manager);
        System.out.println("***"+event);
    }



    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //final Graphics2D g2 = (Graphics2D) g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        //GradientPaint gradient1 = new GradientPaint(0.0f, (float) getHeight(),new Color(174,201,255),
        //					    0.0f, 8.5f, Color.white);
        //Rectangle rec1 = new Rectangle(0, 0, getWidth(), getHeight());
        //g2.setPaint(gradient1);
        //g2.fill(rec1);
    }
    public static JMenuItem menuItem(String str, Icon icon, int mnemo, String skrot, ActionListener al){
        JMenuItem mi = new JMenuItem(str, icon);
        mi.setMnemonic(mnemo);
        mi.setAccelerator(KeyStroke.getKeyStroke(skrot));
        mi.addActionListener(al);
        return mi;
    }

    private class PopupMouseListener extends MouseInputAdapter{

        @Override
        public void mouseExited(MouseEvent e) {            
            //System.err.println("MouseExited close timer start");
            //Rectangle rec = getBounds();
            //System.out.println(""+rec);
            closeTimer.start();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //System.err.println("MouseEntered close timer start");
            if(closeTimer.isRunning()){
                closeTimer.stop();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            //System.out.println(""+e);
        }
    }
}
