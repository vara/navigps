package app.navigps.utils;

import app.config.GUIConfiguration;
import app.config.MainConfiguration;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public class BridgeForVerboseMode extends OutputVerboseStreamAdapter{

    /**
     * @return the instance
     */
    public static BridgeForVerboseMode getInstance() {
        return instance;
    }

    private final LinkedList<OutputVerboseStream> updateComponents =
			new LinkedList<OutputVerboseStream>();
    
    private PrintWriter out = new PrintWriter(new MyOutputStream(false),false);
    private PrintWriter err = new PrintWriter(new MyOutputStream(true),false);
    private PrintStream sout ;
    private PrintStream serr ;

    public static final Console console =
                        new Console();

    private static BridgeForVerboseMode instance = new BridgeForVerboseMode();
    /**
     *
     */
    public BridgeForVerboseMode(){
        try {
            String encode = GUIConfiguration.getDefaultGuiCharsEncoding();
            sout = new PrintStream(new MyOutputStream(false), false, encode);
            serr = new PrintStream(new MyOutputStream(true),false,encode);
        } catch (UnsupportedEncodingException ex) {}

        if(MainConfiguration.getMode())
            addComponentsWithOutputStream(console);
    }
    
    /**
     *
     * @param text
     */
    @Override
    public void outputVerboseStream(String text){
        synchronized(updateComponents){
            for (OutputVerboseStream ucomp : updateComponents) {
                ucomp.outputVerboseStream(text);
            }
        }
    }
    public void addComponentsWithOutputStream(OutputVerboseStream l){
        if(l!=null){
            synchronized(updateComponents){
                updateComponents.add(l);
            }
        }
    }
    public boolean removeComponentFromOutputStream(OutputVerboseStream l){
        if(l!=null){
            synchronized(updateComponents){
                return updateComponents.remove(l);
            }
        }return false;
    }
    /**
     *
     * @param text
     */
    @Override
    public void outputErrorVerboseStream(String text) {
        synchronized(updateComponents){
            for (OutputVerboseStream ucomp : updateComponents) {
                ucomp.outputErrorVerboseStream(text);
            }
        }        
    }

    /**
     *
     * @return
     */
    @Override
    public PrintWriter getOutputWriter() {
        return out;
    }

    /**
     *
     * @return
     */
    @Override
    public PrintWriter getErrOutputWriter() {
        return err;
    }

    /**
     *
     * @return
     */
    @Override
    public PrintStream getOutputStream() {
        return sout;
    }

    /**
     *
     * @return
     */
    @Override
    public PrintStream getErrOutputStream() {
        return serr;
    }

    private class MyOutputStream extends OutputStream {
        
        protected char buf[] = new char[512];
        protected int count=0;
        //private int lsep;
        private boolean err;

        MyOutputStream(boolean err){
            //lsep = System.getProperty("line.separator").getBytes()[0];
            this.err = err;
        }
        
        @Override
        public void write(int b) throws IOException {           
            if(b!=10 && b!=13){ //FIXED !!! bug. with '\n' 10 Linux 13 Winshit
                addToBuffer(b);
                //Console.out.print(Integer.toHexString(b)+" ");
            }
            else
                flush();            
        }

        @Override
        public void flush(){
            if(count!=0){
                if (err) {
                    outputErrorVerboseStream(new String(buf, 0, count));
                } else {
                    outputVerboseStream(new String(buf, 0, count));
                }
                reset();          
            }
        }

        @Override
        public void close() throws IOException {            
            reset();
        }

        protected void addToBuffer(int val){            
            buf[count++]=(char) val;
            if(count==buf.length){
                resizeArray(-1);
            }
        }

        private void resizeArray(int expand){
            if(expand==-1)
                buf = Utils.resizeArray(buf, buf.length);
            else
                buf = Utils.resizeArray(buf, expand);
        }

        public void reset(){
            count = 0;
        }
    }
}
