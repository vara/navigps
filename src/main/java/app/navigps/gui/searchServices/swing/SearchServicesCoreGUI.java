/*
 * SearchServicesGUI.java
 *
 * Created on 2009-04-22, 11:43:21
 */

package app.navigps.gui.searchServices.swing;

import app.navigps.gui.NaviRootWindow;
import app.navigps.gui.detailspanel.AlphaJPanel;
import app.navigps.gui.detailspanel.RoundWindow;
import app.navigps.gui.detailspanel.RoundWindowUtils;
import app.navigps.gui.searchServices.SearchServices;
import app.navigps.gui.searchServices.SearchServicesConfig;
import app.navigps.gui.svgComponents.SVGCanvasLayers;
import app.navigps.utils.NaviUtilities;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class SearchServicesCoreGUI {

    protected RoundWindow roundWindowInstace;
    protected SearchServicesPanel guiForSearchServ;

    private SearchServices parent;

    public SearchServicesCoreGUI(SearchServices ss){

        parent = ss;

        init();
    }

    private void init(){
        roundWindowInstace = createRoundWindowProperties();
        guiForSearchServ = createSearchServicesPanel();

        Container cont = roundWindowInstace.getContentPane();
        cont.setLayout(new BorderLayout());
        cont.add(guiForSearchServ,BorderLayout.CENTER);

        installRootWindow();
    }

    public SearchServicesPanel getPanel(){
        return guiForSearchServ;
    }

    protected void installRootWindow(){
        
        SVGCanvasLayers svgClayer = NaviUtilities.getSVGCanvasLayers(parent);
        if(svgClayer != null){
            svgClayer.getModalContainer().add(roundWindowInstace);

            svgClayer.getModalContainer().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Container cont = RoundWindowUtils.getRoundWindowFromContainer((Container)e.getSource());
                    if(cont != null){
                        ((RoundWindow)cont).updatePosition();
                    }
                }
            });
            roundWindowInstace.updatePosition();
        }else{
            System.err.println(getClass().getName()+"installRootWindow SVGCanvasLayers==null !!!");
        }
        
        //svgCanvas.add(synch,BorderLayout.CENTER);
        


        //System.out.println("Mouse listeners "+svgCanvas.getMouseListeners().length);

        //svgCanvas.removeMouseMotionListener(me);
        //svgCanvas.removeMouseListener(me);
        

        //System.out.println("Mouse listeners after "+svgCanvas.getMouseListeners().length);

        
        

        System.out.println(getClass().getCanonicalName()+" [install components]");
    }

    protected PropertyChangeListener createPropertyChangeListener(){
        return new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(AlphaJPanel.ALPHA_CHANGE)) {
                    //System.out.println("new Alpa "+newAlpha);
                    if ( parent.setAlpha( (java.lang.Float) evt.getNewValue()) ) {
                            parent.repaintVisibleArea();
                    }
                }
            }
        };
    }

    public void setVisible(boolean val){
        if(val){
            guiForSearchServ.reloadCategory();
            guiForSearchServ.installTriggers();
        }
        roundWindowInstace.setEnabled(val);
    }

    protected SearchServicesPanel createSearchServicesPanel(){
        return new SearchServicesPanel();
    }

    protected RoundWindow createRoundWindowProperties(){
        RoundWindow rw = new RoundWindow();
        rw.setIcon(NaviRootWindow.createNavigationIcon("searchServices32"));
        rw.setDynamicRevalidate(true);
        rw.setUpperThresholdAlpha(0.6f);
        rw.setAlpha(0.0f);
        rw.getContentPane().setUpperThresholdAlpha(0.75f);
        rw.setTitle(SearchServicesConfig.getModuleName());
        rw.setVisible(false);
        rw.getDecoratePanel().setVisibleCloseButton(false);

        rw.addPropertyChangeListener(createPropertyChangeListener());
        rw.getWinBehavior().addEndAction(createCloseAction());
        return rw;
    }

    protected ActionListener createCloseAction(){
        return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(RoundWindow.CLOSE_WINDOW_ACTION == e.getID()){
                        //System.err.println("id: "+e.getID());
                        //setEnabledSearchServices(false);
                        //uninstall();
                        //enabled = false;
                    }
                }
            };
    }
}
