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
    
    @Override
    public void currentStatusChanged(String str){}
    @Override
    public void documentPrepareToModification(){}
    @Override
    public void documentClosed(){}
}
