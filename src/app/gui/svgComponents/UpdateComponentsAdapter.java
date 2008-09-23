/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

/**
 *
 * @author vara
 */
public abstract class UpdateComponentsAdapter implements UpdateComponentsWhenChangedDoc{
    
    public void currentStatusChanged(String str){}
    public void documentPrepareToModification(){}
    public void documentClosed(){}
}
