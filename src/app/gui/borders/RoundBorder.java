package app.gui.borders;

import java.awt.Insets;

/**
 *
 * @author wara
 */
public abstract class RoundBorder extends AlphaBorder{

    private double recw=0;
    private double rech=0;
    
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
    public RoundBorder(double recw, double rech) {
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
    public double getRecW() {
        return recw;
    }

    /**
     * @param recw the recw to set
     */
    public void setRecW(double recw) {
        this.recw = recw;
    }

    /**
     * @return the rech
     */
    public double getRecH() {
        return rech;
    }

    /**
     * @param rech the rech to set
     */
    public void setRecH(double rech) {
        this.rech = rech;
    }

    /**
     *
     * @param recw
     * @param rech
     */
    public void setRound(double recw, double rech){
        setRecH(rech);
        setRecW(recw);
    }
}
