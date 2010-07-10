/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConnectionAdapter.java
 *
 * Created on 2009-04-02, 15:25:42
 */

package app.database.odb.utils;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public abstract class ConnectionAdapter implements ConnectionListener{

    @Override
    public void connectionOpened() {
    }

    @Override
    public void connectionClosed() {
    }

}
