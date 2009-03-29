/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.ArgumentsStartUp;

import app.ArgumentsStartUp.core.AbstractParameter;

/**
 *
 * @author wara
 */
public abstract class SingleValueParameter extends AbstractParameter{

    public SingleValueParameter(String symbol) {
        super(symbol);
    }

    @Override
    public void safeOption(String[] optionValues) {
        handleOption(optionValues[0]);
    }

    @Override
    public int getOptionValuesLength() {
        return 1;
    }

    @Override
    public boolean isExit() {
        return false;
    }

    public abstract void handleOption(String optionValue);
}
