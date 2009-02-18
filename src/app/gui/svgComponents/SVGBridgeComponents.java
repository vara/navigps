/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import app.gui.StatusChangedListener;
import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author vara
 */
public class SVGBridgeComponents implements StatusChangedListener,
                                    UpdateComponentsWhenChangedDoc{
   
    private boolean rederingStatus = true;

    private LinkedList<UpdateComponentsWhenChangedDoc> updateComponets = 
			new LinkedList<UpdateComponentsWhenChangedDoc>();

    private LinkedList<StatusChangedListener> statusChanged =
			new LinkedList<StatusChangedListener>();
    
    private File svgFile= null;
    
    public SVGBridgeComponents(){}
    
    public boolean isRendering() {
        return rederingStatus;
    }
    
    protected void setRederingStatus(boolean rederingStatus) {
        this.rederingStatus = rederingStatus;
    }
    
    public void setSvgFileObject(File path){
        svgFile = path;
    }
    
    public final String getAbsoluteFilePath(){
        if(svgFile != null)
            return svgFile.getName();
        return "";
    }
    
    public void addUpdateComponentslisteners(UpdateComponentsWhenChangedDoc l){
	
        if(l!=null)
            updateComponets.add(l);
    }
    
    public void addStatusChangedlistener(StatusChangedListener l){
        if(l!=null)
             statusChanged.add(l);
    }

    @Override
    synchronized public void currentStatusChanged(String str) {
        for (StatusChangedListener ucomp : statusChanged) {
            ucomp.currentStatusChanged(str);
        }
        //System.out.println(str);
    }

    @Override
    public void documentPrepareToModification() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (UpdateComponentsWhenChangedDoc ucomp : updateComponets)
                    ucomp.documentPrepareToModification();
            }
        });
        t.start();
    }

    @Override
    public void documentClosed() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (UpdateComponentsWhenChangedDoc ucomp : updateComponets)
                    ucomp.documentClosed();
            }
        });
        t.start();
    }
}
