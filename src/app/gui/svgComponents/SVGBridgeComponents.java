/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import java.util.LinkedList;
import javax.swing.JLabel;

/**
 *
 * @author vara
 */
public class SVGBridgeComponents implements LetComponentsOfChangedDoc{

    private JLabel labCurrentStatus = new JLabel("Choose Documnet");
    private JLabel mousePosOnSvgComponent = new JLabel("no documnet",JLabel.CENTER);
    private JLabel pointInSvgDoc = new JLabel("no document",JLabel.CENTER);
    private String strCurrentStatus ="";
    private boolean rederingStatus = true;
    
    private LinkedList<UpdateComponentsWhenChangedDoc> updateComponets = 
			new LinkedList<UpdateComponentsWhenChangedDoc>();
    
    private String absoluteFilePath="";    
    

    public boolean isRendering() {
	return rederingStatus;
    }
    
    protected void documentBuildCompleted(){
	
	Thread t = new Thread(new Runnable() {

	    public void run() {
		for (UpdateComponentsWhenChangedDoc ucomp : updateComponets) {
		    ucomp.documentPrepareToModification();
		}
	    }
	});
	t.start();
    }
    
    protected void setRederingStatus(boolean rederingStatus) {
	this.rederingStatus = rederingStatus;
    }
    
    protected void setTextToCurrentStatus(String text){
	labCurrentStatus.setText(text);
	strCurrentStatus = text;
	for (UpdateComponentsWhenChangedDoc ucomp : updateComponets) {
	    ucomp.currentStatusChanged(text);
	}
    }

    public JLabel getLabelWithPosOnSvgComponent() {
	return mousePosOnSvgComponent;
    }

    public JLabel getLabelWithPointInSvgDoc() {
	return pointInSvgDoc;
    }
    
    public void setLabelInformationPosytion(String pos){
	
	String [] str = pos.split(";");
	mousePosOnSvgComponent.setText(str[1]);
	pointInSvgDoc.setText(str[2]);
    }

    public String getStringWithCurrentStatus() {
	return strCurrentStatus;
    }
    
    public JLabel getLabelWithCurrentStatus() {
	return labCurrentStatus;
    }
    
    public void setAbsoluteFilePath(String path){
	absoluteFilePath = path;
    }
    
    public final String getAbsoluteFilePath(){
	return absoluteFilePath;
    }
    
    public void addUpdateComponents(UpdateComponentsWhenChangedDoc l){
	
	updateComponets.add(l);
	System.out.println("Update components size "+updateComponets.size());
    }
}
