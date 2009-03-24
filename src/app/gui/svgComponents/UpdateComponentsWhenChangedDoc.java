package app.gui.svgComponents;

/**
 *
 * @author vara
 */

public interface UpdateComponentsWhenChangedDoc{
    //when document rendering building etc.       
    /**
     *
     */
    void documentPrepareToModification();
    /**
     *
     */
    void documentClosed();
}
