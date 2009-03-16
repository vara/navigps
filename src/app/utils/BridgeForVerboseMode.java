/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import config.MainConfiguration;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 *
 * @author vara
 */
public class BridgeForVerboseMode extends OutputVerboseStreamAdapter{

    private LinkedList<OutputVerboseStream> updateComponents =
			new LinkedList<OutputVerboseStream>();
    
    private PrintWriter out = new PrintWriter(new MyOutputStream(false),false);
    private PrintWriter err = new PrintWriter(new MyOutputStream(true),false);
    private PrintStream sout = new PrintStream(new MyOutputStream(false),false);
    private PrintStream serr = new PrintStream(new MyOutputStream(true),false);

    public static final Console console =
                        new Console();

    /**
     *
     */
    public BridgeForVerboseMode(){
        if(MainConfiguration.getMode())
            addComponentsWithOutputStream(console);
    }
    
    /**
     *
     * @param text
     */
    @Override
    public void outputVerboseStream(String text){	
        for (OutputVerboseStream ucomp : updateComponents) {            
            ucomp.outputVerboseStream(text);
        }
    }
    public void addComponentsWithOutputStream(OutputVerboseStream l){	
        if(l!=null)
            updateComponents.add(l);
    }
    public boolean removeComponentFromOutputStream(OutputVerboseStream l){
        if(l!=null){
            return updateComponents.remove(l);
        }return false;
    }
    /**
     *
     * @param text
     */
    @Override
    public void outputErrorVerboseStream(String text) {
        for (OutputVerboseStream ucomp : updateComponents) {            
            ucomp.outputErrorVerboseStream(text);
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
            if(b!=10 && b!=13) //FIXED !!! bug. with '\n' 10 Linux 13 Winshit
                addToBuffer(b);
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
