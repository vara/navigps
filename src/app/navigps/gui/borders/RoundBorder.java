package app.navigps.gui.borders;

import java.awt.Insets;

/**
 *
 * @author wara
 */
public abstract class RoundBorder extends AlphaBorder{

    private float recw=0;
    private float rech=0;
    
    /**
     *
     */
    public RoundBorder(){
    }

    /**
     *
     * @param recw
     * @param rech
     */
    public RoundBorder(float recw, float rech) {
         this.recw=recw;
         this.rech=rech;
    }

    /**
     *
     * @param ins
     */
    public abstract void setBorderInsets(Insets ins);

    /**
     * @return the recw
     */
    public float getRecW() {
        return recw;
    }

    /**
     * @param recw the recw to set
     */
    public void setRecW(float recw) {
        this.recw = recw;
    }

    /**
     * @return the rech
     */
    public float getRecH() {
        return rech;
    }

    /**
     * @param rech the rech to set
     */
    public void setRecH(float rech) {
        this.rech = rech;
    }

    /**
     *
     * @param recw
     * @param rech
     */
    public void setRound(float recw, float rech){
        setRecH(rech);
        setRecW(recw);
    }
}
