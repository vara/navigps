/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.ArgumentsStartUp;

/**
 *
 * @author wara
 */
public abstract class IntegerValueParameter extends SingleValueParameter{

    public IntegerValueParameter(String symbol) {
        super(symbol);
    }

    @Override
    public void handleOption(String optionValue) {
        try{
            handleOption(Integer.parseInt(optionValue));
        } catch(NumberFormatException e){
            throw new IllegalArgumentException();
        }
    }
    
    public abstract void handleOption(int optionValue);
}
