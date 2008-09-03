/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import javax.swing.JLabel;

/**
 *
 * @author vara
 */
public class SVGBridgeComponents implements LetComponentsOfChangedDoc{

    private JLabel currentStatus = new JLabel("Choose Documnet");
    private JLabel mousePosOnSvgComponent = new JLabel("no documnet",JLabel.CENTER);
    private JLabel pointInSvgDoc = new JLabel("no document",JLabel.CENTER);
    private boolean rederingStatus = true;
    
    public JLabel getLabelWithCurrentStatus() {
	return currentStatus;
    }

    public boolean isRedering() {
	return rederingStatus;
    }

    protected void setRederingStatus(boolean rederingStatus) {
	this.rederingStatus = rederingStatus;
    }
    protected void setTextToCurrentStatus(String text){
	currentStatus.setText(text);
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
    
}
