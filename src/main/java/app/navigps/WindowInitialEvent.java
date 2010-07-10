/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WindowInitialEvent.java
 *
 * Created on 2009-04-05, 16:36:20
 */

package app.navigps;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public class WindowInitialEvent {

    private final Object source;
    private final long time;

    public WindowInitialEvent(Object source,long time){
        this.source = source;
        this.time = time;
    }

    /**
     * @return the source
     */
    public final Object getSource() {
        return source;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return new Long(time);
    }
}
