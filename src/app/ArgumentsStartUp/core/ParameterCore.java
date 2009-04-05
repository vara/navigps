package app.ArgumentsStartUp.core;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public interface ParameterCore {
    /**
     * @Return name parameter
     */
    String getOption();

    /*
     * @Return description for parameter
     */
    String getOptionDescription();

    /**
     * @return counts for paramater
     */
    int getOptionValuesLength();
    
    void handleOption(String[] values);
}
