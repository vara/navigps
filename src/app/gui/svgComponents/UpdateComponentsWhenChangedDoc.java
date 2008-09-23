/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

/**
 *
 * @author vara
 */

public interface UpdateComponentsWhenChangedDoc {
    
    //when document rendering building etc. status changed
    void currentStatusChanged(String str);   
    void documentPrepareToModification();
    void documentClosed();
}
