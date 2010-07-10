package app.ArgumentsStartUp.core;

/**
 *
 * @author wara
 */
public abstract class AbstractParameter implements ParameterCore{

    private String symbol;

    public AbstractParameter(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String getOption() {
        return symbol;
    }

    @Override
    public void handleOption(String[] optionValues){
            int nOptions = optionValues != null? optionValues.length: 0;
            if (nOptions != getOptionValuesLength()){
                throw new IllegalArgumentException();
            }

            safeOption(optionValues);
        }

    public abstract boolean isExit();
    public abstract void safeOption(String[] optionValues);
}
