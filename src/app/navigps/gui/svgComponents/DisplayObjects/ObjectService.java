package app.navigps.gui.svgComponents.DisplayObjects;

import app.navigps.gui.NaviRootWindow;
import app.navigps.gui.MyPopupMenu;
import app.navigps.gui.detailspanel.AlphaJPanel;
import app.navigps.gui.svgComponents.ServicesContainer;
import app.navigps.utils.GraphicsUtilities;
import app.navigps.utils.NaviLogger;
import app.navigps.utils.NaviPoint;
import app.config.SVGConfiguration;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.event.MouseInputAdapter;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.ModernBalloonStyle;
import net.java.balloontip.utils.ToolTipUtils;
import app.database.odb.core.ServiceCore;
import org.neodatis.odb.OID;

/**
 *
 * @author wara
 */
public class ObjectService extends AlphaJPanel implements ObjectToDisplayService{

    private static ImageIcon iconinfo = null;
    /**
     * @return the iconinfo
     */
    public static ImageIcon getIconinfo() {
        return iconinfo;
    }

    private ImageIcon icon = null;

    private String description = "";
    private String category = "";
    private NaviPoint xy = null;
    private String serviceName = "";

    private int iconGap = 0;   

    private ObjectMouseListener ml = new ObjectMouseListener();

    private OID objectId;

    private BalloonTip balloon;

    public ObjectService(String desc,String group,String servicesName,OID id,NaviPoint point){
        super(null);
        setOpaque(false);

        description = desc;
        category = group;
        this.serviceName = servicesName;
        xy = point;
        objectId = id;


        defaultInstall();
    }

    private void defaultInstall(){

        if(getIconinfo() == null){
            iconinfo = loadThumbnailIcon("GlobalInfo");
        }
        updateIcon();
        updateObject();
        installMouseListener();        
        
        createTooltip(createToolTipString());
    }

    public String createToolTipString(){

        return "<html><table>" +
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
        if(getIconinfo() != null)
            g.drawImage(getIconinfo().getImage(), iconGap,iconGap, null);
        else
            g.drawRect(0, 0,getWidth()-1, getHeight()-1);
    }

    public void updateIcon(){        
        icon = ObjectService.loadThumbnailIcon(getCategory());
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

    private void createTooltip(final String text) {
        if(getBalloonTip() == null){
            BalloonTipStyle style = new ModernBalloonStyle(15,15,new Color(248,249,211),new Color(249,239,184),new Color(73,158,236));
            ((ModernBalloonStyle)style).enableAntiAliasing(true);
            ((ModernBalloonStyle)style).setBorderThickness(2);
            balloon = new BalloonTip(this, text, style, BalloonTip.Orientation.LEFT_ABOVE, BalloonTip.AttachLocation.ALIGNED, 15, 10, false);
            getBalloonTip().setUpperThresholdAlpha(0.85f);
            getBalloonTip().setAnimationDuration(350);
            getBalloonTip().setIcon(getIcon());
            getBalloonTip().enableClickToHide(true);
            ToolTipUtils.balloonToToolTip(getBalloonTip(),0, 5000);
        }
	}


    @Override
    public String toString() {
        String msg = getClass().getCanonicalName()+" [ Service Name : "+getServiceName()+
                " ; Description : "+getDescription()+" ; Group Name : "+getCategory()+
                " ; icon : "+getIcon()+
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
        int width = 28;
        int height = 28;
        if(getIconinfo() != null){
            width = getIconinfo().getIconWidth();
            height = getIconinfo().getIconHeight();
        }
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

    public static ImageIcon loadThumbnailIcon(String name){
        try {
            URL href = NaviRootWindow.createNavigationIconPath("services/"+name,"png");
            BufferedImage bi = GraphicsUtilities.loadCompatibleImage(href);
            return new ImageIcon(GraphicsUtilities.createThumbnail(bi, SVGConfiguration.getInformationIconSize()));

        } catch (Exception ex) {
            String msg = ex + "[ for group name "+name+" ]";
            NaviLogger.log.log(Level.WARNING, msg);
        }
        return null;
    }

    @Override
    public OID getOID() {
        return objectId;
    }

    @Override
    public void updateService(ServiceCore sc) {
        String gName = sc.getServiceDescription().getCategory().getName();
        String sName = sc.getServiceDescription().getServiceName();
        String sDesc = sc.getServiceDescription().getAdditionaInfo();
        String sStreet = sc.getServiceDescription().getServiceStreet();
        String sNumber = sc.getServiceDescription().getServiceNumber();
        String sCity = sc.getServiceDescription().getCity();

        sDesc+="<br>Street: <b>"+sStreet+" "+sNumber+"</b><br>City: "+sCity;

        boolean needUpdateTooltipText = false;

        if(!getCategory().equals(gName)){            
            category = gName;
            updateIcon();
            getBalloonTip().setIcon(getIcon());
        }

        if(!getServiceName().equals(sName)){            
            serviceName = sName;
            needUpdateTooltipText = true;
        }

        if(!getDescription().equals(sDesc)){            
            description = sDesc;
            needUpdateTooltipText = true;
        }

        if(needUpdateTooltipText){
            getBalloonTip().setText(createToolTipString());
        }
        NaviPoint np = new NaviPoint(sc.getServiceAttributes().getX(),
                sc.getServiceAttributes().getY());
        if(!xy.equals(np)){
            xy = np;
            ServicesContainer container = (ServicesContainer)getParent();
            transformCoordinate(container.getTransform());
        }
    }

    /**
     * @return the balloon
     */
    public BalloonTip getBalloonTip() {
        return balloon;
    }

    public void dispose(){

        getBalloonTip().closeBalloon();
        uninstallMouseListener();
        removeAll();

        //balloon = null;
        //icon = null;
        //objectId = null;
        //ml=null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println(getClass().getCanonicalName()+" method 'Finalize'");
    }

    private class ObjectMouseListener extends MouseInputAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            //System.err.println(ObjectService.this.toString());
            //setToolTip(toolTipString);
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
                            String msg = "Removed service \""+ObjectService.this.getServiceName()+"\"";
                            parent.remove(ObjectService.this);                            
                            parent.repaint();
                            
                            NaviRootWindow.getBridgeInformationPipe().
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
                            NaviRootWindow.getBridgeInformationPipe().
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
