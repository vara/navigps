package app.database.odb.core;

/**
 *
 * @author ACME
 */
public class ServiceAttributes {

    private float x;
    private float y;
    private ServiceCore sc;

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the sc
     */
    public ServiceCore getServiceCore() {
        return sc;
    }

    /**
     * @param sc the sc to set
     */
    public void setServiceCore(ServiceCore sc) {
        this.sc = sc;
    }

    @Override
    public String toString() {
        String msg = getClass().getCanonicalName()+
                " [ x : "+getX()+" y : "+getY()+
                " ServiceCore : "+getServiceCore()+" ]";
        return msg;
    }
}
