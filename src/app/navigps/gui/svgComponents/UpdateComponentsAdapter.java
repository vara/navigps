package app.navigps.gui.svgComponents;

/**
 *
 * @author Gzregorz (vara) Warywoda
 */
public abstract class UpdateComponentsAdapter implements UpdateComponentsWhenChangedDoc{
    
    /**
     *
     */
    @Override
    public void documentPrepareToModification(){}
    /**
     *
     */
    @Override
    public void documentClosed(){}

    @Override
    public void documentLoadingCompleted() {}
}
