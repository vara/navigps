/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WindowInitialListener.java
 *
 * Created on 2009-04-05, 16:34:39
 */

package app.navigps;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public interface WindowInitialListener {

    void initialPrepare(WindowInitialEvent e);
    void initialCompleted(WindowInitialEvent e);

    public static class Stub implements WindowInitialListener{

        @Override
        public void initialPrepare(WindowInitialEvent e) {
        }

        @Override
        public void initialCompleted(WindowInitialEvent e) {
        }

    }
}
