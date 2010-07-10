package app.ArgumentsStartUp;

import app.ArgumentsStartUp.core.AbstractParameter;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public abstract class NoValueParameter extends AbstractParameter{

    /* This class represents a parameter which performs
     * the action defined and the program ends.
     */
    public NoValueParameter(String symbol){
        super(symbol);
    }

    @Override
    public void safeOption(String[] optionValues) {
        handleOption();
    }

    @Override
    public int getOptionValuesLength() {
        return 0;
    }

    @Override
    public boolean isExit() {
        return false;
    }

    public abstract void handleOption();
}
