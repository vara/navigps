/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents.displayobjects;

import app.gui.MainWindowIWD;
import app.gui.MyPopupMenu;
import app.gui.detailspanel.AlphaJPanel;
import app.gui.svgComponents.ServicesContainer;
import app.utils.GraphicsUtilities;
import app.utils.NaviPoint;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.event.MouseInputAdapter;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.ModernBalloonStyle;
import net.java.balloontip.utils.ToolTipUtils;

/**
 *
 * @author wara
 */
public class ObjectService extends AlphaJPanel implements ObjectToDisplayService{

    /**
     * @return the iconinfo
     */
    public static ImageIcon getIconinfo() {
        return iconinfo;
    }

    private ImageIcon icon = null;
    private static ImageIcon iconinfo = null;

    private String description = "";
    private String category = "";
    private NaviPoint xy = null;
    private String serviceName = "";

    private int iconGap = 0;   

    private ObjectMouseListener ml = new ObjectMouseListener();

    private String toolTipString;    

    public ObjectService(BufferedImage img,String desc,String group,String servicesName,NaviPoint point){
        super(null);
        setOpaque(false);
        icon = new ImageIcon(img);
        description = desc;
        category = group;
        this.serviceName = servicesName;
        xy = point;

        defaultInstall();
    }

    private void defaultInstall(){

        if(getIconinfo() == null){
            try {
                String href = MainWindowIWD.createNavigationIconPath("test/GlobalInfo","png").toURI().toString();
                BufferedImage bi = GraphicsUtilities.loadCompatibleImage(new URL(href));
                iconinfo = new ImageIcon(GraphicsUtilities.createThumbnail(bi, 28));

            } catch (Exception ex) {
                System.err.println(""+ex);
            }
        }

        updateObject();
        installMouseListener();

        toolTipString = "<html><table>" +
                        "<tr><td>"+getServiceName()+"</td></tr>"+
                        "<tr><td>"+getDescription()+"</td></tr>"+
                        "</table></html>";
        
    }

    public void installMouseListener(){
        uninstallMouseListener();
        addMouseListener(ml);
        addMouseMotionListener(ml);
    }

    public void uninstallMouseListener(){
        removeMouseListener(ml);
        removeMouseMotionListener(ml);
    }

    @Override
    protected void paintComponent(Graphics g) {        
        g.drawImage(getIconinfo().getImage(), iconGap,iconGap, null);
    }

    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public NaviPoint getCoordinate() {
        return xy;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public void setToolTip(final String text) {
        BalloonTipStyle style = new ModernBalloonStyle(15,15,new Color(248,249,211),new Color(249,239,184),new Color(73,158,236));
        BalloonTip balloon = new BalloonTip(this, toolTipString, style, BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.ALIGNED, 15, 10, false);
        balloon.setIcon(getIcon());
        balloon.enableClickToHide(true);
        ToolTipUtils.balloonToToolTip(balloon,0, 3000);
	}


    @Override
    public String toString() {
        String msg = getClass().getCanonicalName()+" [ Service Name : "+getServiceName()+
                " ; Description : "+getDescription()+" ; Group Name : "+getCategory()+
                " ; icon size : "+getIcon().getIconWidth()+","+getIcon().getIconHeight()+
                " ; Component bounds : "+getBounds()+" ]";
        return msg;
    }

    @Override
    public void transformCoordinate(AffineTransform at) {
        NaviPoint np = xy.matrixTransform(at);
        float offsetx = getWidth()/2.0f;
        float offsety = getHeight();
        setLocation((int)(np.getX()-offsetx), (int)(np.getY()-offsety));
    }

    private void updateObject(){

        int width = getIconinfo().getIconWidth();
        int height = getIconinfo().getIconHeight();
        int allGap = iconGap*2;
        setBounds((int)getCoordinate().getX(), (int)getCoordinate().getY(), width+allGap, height+ allGap);

    }

    private static Container getServicesContainer(JComponent child){
        Container parent = child.getParent();
        if(parent instanceof ServicesContainer){
            return parent;
        }
        return null;
    }

    private class ObjectMouseListener extends MouseInputAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            //System.err.println(ObjectService.this.toString());
            setToolTip(toolTipString);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3){
                MyPopupMenu popup = new MyPopupMenu();
                JMenuItem miRemoveThis= new JMenuItem("Remove this service");
                JMenuItem miRemoveAll= new JMenuItem("Remove all services");
                miRemoveThis.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Container parent = ObjectService.getServicesContainer(ObjectService.this);                        
                        if(parent != null){
                            parent.remove(ObjectService.this);
                            parent.repaint();
                            String msg = "Removed service \""+ObjectService.this.getServiceName()+"\"";
                            MainWindowIWD.getBridgeInformationPipe().
                                    currentStatusChanged(msg);
                            System.out.println(msg);
                        }
                    }
                });
                miRemoveAll.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {                        
                        Container parent = ObjectService.getServicesContainer(ObjectService.this);
                        if(parent != null){
                            int count = parent.getComponentCount();
                            parent.removeAll();
                            parent.repaint();
                            String msg = "Removed "+count+" services";
                            MainWindowIWD.getBridgeInformationPipe().
                                    currentStatusChanged(msg);
                            System.out.println(msg);
                        }
                    }
                });
                popup.add(miRemoveThis);
                popup.add(miRemoveAll);
                popup.show(ObjectService.this, e.getX(), e.getY());
            }
        }
    }
}
