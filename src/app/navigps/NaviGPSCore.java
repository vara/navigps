package app.navigps;

import app.config.GUIConfiguration;
import app.config.MainConfiguration;
import app.navigps.gui.NaviRootWindow;
import app.ArgumentsStartUp.FileValueParameter;
import app.ArgumentsStartUp.NoValueParameter;
import app.ArgumentsStartUp.SizeValueParameter;
import app.ArgumentsStartUp.core.AbstractParameter;
import app.ArgumentsStartUp.core.ParametersContainer;
import app.navigps.utils.MyLogger;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.SwingUtilities;

/**
 *
 * @author vara
 */
public class NaviGPSCore {    
    
    public NaviGPSCore(){        

        if(ParametersContainer.isEmpty()){
            ParametersContainer.putParameter(createParametrs());
        }
        
        MyLogger.log.log(Level.FINE,"Start main application");        
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        //args = new String[]{"-Vg","-sp","-V","-f","./resources/maps/MapWorld.svg","-ws","1100,1100"};
        //System.setProperty("sun.java2d.noddraw", "true");
        //System.setProperty("swing.aatext", "true");
        NaviGPSCore app = new NaviGPSCore();

        ArrayList<String> arguments = new ArrayList<String>();
        for (int i=0; i<args.length; i++){
            arguments.add(args[i]);
        }
        app.execute(arguments);        
    }

    public void execute(ArrayList<String> args){
        int iArgs = args.size();
        for (int i = 0; i < iArgs; i++) {
            String string = args.get(i);
            //System.err.println("** '"+string+"'");
            AbstractParameter optionHandler = ParametersContainer.getParameter(string);
            if (optionHandler == null){
                // Assume v is a source.
                System.err.println("Not recognizied parameter "+string);
            } else {
                int nOptionArgs = optionHandler.getOptionValuesLength();
                if (i + nOptionArgs >= iArgs){
                    System.err.println("Error not enough option values for : "+ optionHandler.getOption());
                    return;
                }

                String[] optionValues = new String[nOptionArgs];
                for (int j=0; j<nOptionArgs; j++){
                    optionValues[j] = (String)args.get(1+i+j);
                }
                i += nOptionArgs;

                try {
                    optionHandler.handleOption(optionValues);
                } catch(IllegalArgumentException e){
                    e.printStackTrace();
                    System.err.println("Error: illegal argument for option "+optionHandler.getOption() +" : "+
                                         optionValuesToString(optionValues)+".\nTry run application with option '-h'");
                    return;
                }
                if(optionHandler.isExit()){
                    System.exit(0);
                }
            }
        }

        NaviGPSCore.initGui(this);
    }

    /**
     *
     * @param m
     */
    protected static void initGui(final NaviGPSCore m){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NaviRootWindow nw = new NaviRootWindow(m);
                nw.setDisplayMode();
            }
        });
    }
    
    /**
     *
     */
    public void reload(){
        initGui(this);
    }

    protected static String optionValuesToString(String[] v){

        StringBuffer sb = new StringBuffer();
        int n = v != null ? v.length:0;
        for (int i=0; i<n; i++){
            sb.append(v[i] );
            sb.append( ' ' );
        }
        return sb.toString();
    }

    public static Vector<AbstractParameter>getAllparameters(){
        Vector<AbstractParameter> retVec = null;

        if(ParametersContainer.isEmpty()){
            ParametersContainer.putParameter(createParametrs());
        }
        retVec = ParametersContainer.getAllParameters();

        return retVec;
    }

    private static Vector<AbstractParameter>createParametrs(){

        Vector<AbstractParameter> params = new Vector<AbstractParameter>();
        
        System.out.println("<TEST> Create difined parameters. This text should be shown only once.");

        params.add(new NoValueParameter("-v") {
            @Override
            public boolean isExit() {
                return true;
            }
            @Override
            public void handleOption() {
                System.out.println("Version : "+Version.getVersion());
            }
            @Override
            public String getOptionDescription() {
                return "\t-v  \t(version) print version application";
            }
        });

        params.add(new NoValueParameter("-V") {
            @Override
            public void handleOption() {
                MainConfiguration.setModeVerboseConsole(true);
                System.out.println("parameter -V");
            }

            @Override
            public String getOptionDescription() {
                return "\t-V  \t(verbose mode) result return by function only on console";
            }
        });

        params.add(new NoValueParameter("-Vg") {

            @Override
            public void handleOption() {
                MainConfiguration.setModeVerboseGui(true);
                System.out.println("parameter -Vg");
            }

            @Override
            public String getOptionDescription() {
                return "\t-Vg \t(verbose mode) like -v + create window in gui with the same result on console";
            }
        });

        params.add(new NoValueParameter("-sp") {

            @Override
            public void handleOption() {
                MainConfiguration.setShowDocumentProperties(true);
                System.out.println("parameter -sp");
            }

            @Override
            public String getOptionDescription() {
                return "\t-sp \t(Show properties) create window in app with properties chart file";
            }
        });

        params.add(new FileValueParameter("-cf") {
            @Override
            public void handleOption(File optionValue) {
                System.out.println("parameter -cf");
            }

            @Override
            public String getOptionDescription() {
                return "\t-c  \t(path to configuration file) not suported yet !!!";
            }
        });

        params.add(new FileValueParameter("-f") {
            @Override
            public void handleOption(File optionValue) {
                MainConfiguration.setPathChartToFile(optionValue.getAbsolutePath());
                System.out.println("parameter -f");
            }
            @Override
            public String getOptionDescription() {
                return "\t-f  \t(path to chart file) file format documents must by only svg !!!";
            }
        });

        params.add(new SizeValueParameter("-ws") {

            @Override
            public void handleOption(Dimension dimensionValue) {
                GUIConfiguration.setWindowSize(dimensionValue);
                System.out.println("parameter -ws "+dimensionValue);
            }

            @Override
            public String getOptionDescription() {
                return "\t-ws \t(window size  eq. -ws 800,600 ) " +
                           "\n\t\t\tIt takes two arguments defining the size of the total number of main window." +
                           "\n\t\t\tIf there is to adopt the size of a window the size of screen resolution";
            }
        });

        params.add(new NoValueParameter("-fs") {

            @Override
            public void handleOption() {
                GUIConfiguration.setModeScreen(GUIConfiguration.FULL_SCREEN);
            }

            @Override
            public String getOptionDescription() {
                return "\t-fs \t(Display root window in full screen mode)";
            }
        });

        params.add(new NoValueParameter("-h") {

            @Override
            public void handleOption() {
                Vector<AbstractParameter> vec = ParametersContainer.getAllParameters();
                System.out.println("NaviGPS options are:");
                for (AbstractParameter ap : vec) {
                    System.out.println(ap.getOptionDescription());
                }
            }

            @Override
            public String getOptionDescription() {
                return "\t-h  \t(help -- Show this text)";
            }

            @Override
            public boolean isExit() {
                return true;
            }
        });
        return params;
    }
}
