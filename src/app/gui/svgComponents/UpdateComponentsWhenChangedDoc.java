package app.gui.svgComponents;

/**
 * When document rendering,building etc.
 * @author vara
 */

public interface UpdateComponentsWhenChangedDoc{          
    /**
     *
     */
    void documentPrepareToModification();
    /**
     *
     */
    void documentClosed();
}
