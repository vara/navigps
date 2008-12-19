/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import config.MainConfiguration;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 *
 * @author vara
 */
public class BridgeForVerboseMode implements OutputVerboseStream{

    private LinkedList<OutputVerboseStream> updateComponets = 
			new LinkedList<OutputVerboseStream>();
    
    private PrintWriter out = new PrintWriter(new MyOutputStram(false),true);
    private PrintWriter err = new PrintWriter(new MyOutputStram(true),true);

    public BridgeForVerboseMode(){
        if(MainConfiguration.getMode())
            addComponentsWithOutputStream(new OutputVerboseStreamToConsole());
    }
    
    @Override
    public void outputVerboseStream(String text){	
        for (OutputVerboseStream ucomp : updateComponets) {
            ucomp.outputVerboseStream(text);
        }
    }
    public void addComponentsWithOutputStream(OutputVerboseStream l){	
        if(l!=null)
            updateComponets.add(l);
    }
    public boolean removeComponentFromOutputStream(OutputVerboseStream l){
        if(l!=null){
            return updateComponets.remove(l);
        }return false;
    }
    @Override
    public void outputErrorVerboseStream(String text) {
        for (OutputVerboseStream ucomp : updateComponets) {
            ucomp.outputErrorVerboseStream(text);
        }
    }

    @Override
    public PrintWriter getOutputStream() {
        return out;
    }

    @Override
    public PrintWriter getErrOutputStream() {
        return err;
    }

    public class OutputVerboseStreamToConsole extends OutputVerboseStreamAdapter{
	
        @Override
        public void outputVerboseStream(String text){
            System.out.println(text);
        }
        @Override
        public void outputErrorVerboseStream(String text) {
            System.err.println(text);
        }
    }

    class MyOutputStram extends OutputStream {
        
        protected char buf[] = new char[512];
        protected int count=0;
        private int lsep;
        private boolean err;
        MyOutputStram(boolean err){
            lsep = System.getProperty("line.separator").getBytes()[0];
            this.err = err;
        }
        @Override
        public void write(int b) throws IOException {
            if(b!=lsep)
                addToBuffer(b);
        }
        @Override
        public void flush(){
            if(err)
                outputErrorVerboseStream(new String(buf,0, count));
            else
                outputVerboseStream(new String(buf,0, count));
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
