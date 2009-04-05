/*
 * ConnectionListener.java
 *
 * Created on 2009-04-02, 15:11:13
 */

package app.database.odb.utils;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public interface ConnectionListener {
    void connectionOpened();
    void connectionClosed();
}
