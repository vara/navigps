package app.navigps;

import app.config.GUIConfiguration;
import app.config.MainConfiguration;
import app.navigps.gui.NaviRootWindow;
import app.ArgumentsStartUp.FileValueParameter;
import app.ArgumentsStartUp.NoValueParameter;
import app.ArgumentsStartUp.SizeValueParameter;
import app.ArgumentsStartUp.core.AbstractParameter;
import app.ArgumentsStartUp.core.ParametersContainer;
import app.navigps.utils.BridgeForVerboseMode;
import app.navigps.utils.NaviLogger;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.SwingUtilities;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class NaviGPSCore {    

    private ArrayList<String> parameters;

    public NaviGPSCore(ArrayList<String> arg){

        this.parameters = arg;
        //Intercept all output streams
        System.setOut(BridgeForVerboseMode.getInstance().getOutputStream());
        System.setErr(BridgeForVerboseMode.getInstance().getErrOutputStream());

        if(ParametersContainer.isEmpty()){
            ParametersContainer.putParameter(createParametrs());
        }        
        NaviLogger.logger.log(Level.FINE,"Start main application");

    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args){     
        
        //args = new String[]{"-Vg","-sp","-V","-fuy","./resources/maps/MapWorld.svg","-ws","800,600"};
        //System.setProperty("sun.java2d.noddraw", "true");
        //System.setProperty("swing.aatext", "true");

        ArrayList<String> arguments = new ArrayList<String>();
        for (int i=0; i<args.length; i++){
            arguments.add(args[i]);
        }
        NaviGPSCore app = new NaviGPSCore(arguments);
        try {
            app.executeParameters();
        } catch (Exception ex) {
            return;
        }
        initGui(app);
    }

    public void executeParameters() throws Exception{
        try {
            parseParameters(parameters);
        } catch (Exception ex) {
            NaviLogger.logger.log(Level.WARNING,"Parsering argument start up",ex);
            throw ex;
        }
    }

    private void parseParameters(ArrayList<String> args) throws Exception{
        int iArgs = args.size();
        for (int i = 0; i < iArgs; i++) {
            String string = args.get(i);
            //System.err.println("** '"+string+"'");
            AbstractParameter optionHandler = ParametersContainer.getParameter(string);
            if (optionHandler == null){                
                throw new Exception("Not recognizied parameter "+string+".\nTry run application with option '-h'");
            } else {
                int nOptionArgs = optionHandler.getOptionValuesLength();
                if (i + nOptionArgs >= iArgs){
                    throw new Exception("Error not enough option values for : "+ optionHandler.getOption());
                    
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
                    throw new Exception("Error: illegal argument for option "+optionHandler.getOption() +" : "+
                                         optionValuesToString(optionValues)+".\nTry run application with option '-h'");
                    
                }
                if(optionHandler.isExit()){
                    System.exit(0);
                }
            }
        }        
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
                nw.setSize(GUIConfiguration.getWindowSize());
                nw.initComponents();
                if (MainConfiguration.getPathToChartFile() != null) {
                    nw.openSVGDocument(MainConfiguration.getPathToChartFile());
                }
                nw.setTitle(Version.getVersion());
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
        
        System.out.println("<TEST> Create defined parameters. This text should be shown only once.");

        params.add(new NoValueParameter("-v") {
            @Override
            public boolean isExit() {
                return true;
            }
            @Override
            public void handleOption() {
                System.out.println("Version: "+Version.getVersion());
            }
            @Override
            public String getOptionDescription() {
                return "-v\n(version) print application version";
            }
        });

        params.add(new NoValueParameter("-V") {
            @Override
            public void handleOption() {
                MainConfiguration.setModeVerboseConsole(true);
                BridgeForVerboseMode.getInstance().
                        addComponentsWithOutputStream(BridgeForVerboseMode.console);
                //System.out.println("parameter -V");
            }

            @Override
            public String getOptionDescription() {
                return "-V\n(verbose mode) result returned by functions shown only on console";
            }
        });

        params.add(new NoValueParameter("-Vg") {

            @Override
            public void handleOption() {
                MainConfiguration.setModeVerboseGui(true);
                //System.out.println("parameter -Vg");
            }

            @Override
            public String getOptionDescription() {
                return "-Vg\n(verbose mode) like -v + create window in gui with console results";
            }
        });

        params.add(new NoValueParameter("-sp") {

            @Override
            public void handleOption() {
                MainConfiguration.setShowDocumentProperties(true);
                //System.out.println("parameter -sp");
            }

            @Override
            public String getOptionDescription() {
                return "-sp\n(Show properties) create window in app with properties of chart file";
            }
        });
/*
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
*/
        params.add(new FileValueParameter("-f") {
            @Override
            public void handleOption(File optionValue) {
                MainConfiguration.setPathChartToFile(optionValue.getAbsolutePath());
                //System.out.println("parameter -f");
            }
            @Override
            public String getOptionDescription() {
                return "-f\n(path to chart file) file format must be svg only!";
            }
        });

        params.add(new SizeValueParameter("-ws") {

            @Override
            public void handleOption(Dimension dimensionValue) {
                GUIConfiguration.setWindowSize(dimensionValue);
                //System.out.println("parameter -ws "+dimensionValue);
            }

            @Override
            public String getOptionDescription() {
                return "-ws\n(window size  eq. -ws 800,600 ) " +
                           "\nIt takes two arguments to define the size of main window." +
                           "\nIf specified arguments exceed screen resolution they will be brought down to current resolution";
            }
        });

        params.add(new NoValueParameter("-fs") {

            @Override
            public void handleOption() {
                GUIConfiguration.setModeScreen(GUIConfiguration.FULL_SCREEN);
            }

            @Override
            public String getOptionDescription() {
                return "-fs\n(Display root window in full screen mode)";
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
                return "-h\n(help -- Show help text)";
            }

            @Override
            public boolean isExit() {
                return true;
            }
        });
        return params;
    }
}
