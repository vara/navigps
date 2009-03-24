/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils.coordinates;

/**
 *
 * @author vara
 */
public class Ellipsoid implements EllipsedMethods{
 
    /* 
     * Ellipsoid's name
     */
    private String name;
    
    /*
     * Big half axis (semi-major axis)
     */
    private double a;
    
    /*
     * Small half axis (semi-minor axis)
     */
    private double b;
    
    /*
     * Pin flattening elipsoidy (Reciprocal of flattening)
     * (pl) Biegunowe spłaszczenie elipsoidy
     */
    private double f;
    
    /*
     * first eccentricity 
     * (pl)(mimosród)
     */    
    private double e1;
    
    /*
     * second eccentricity
     */
    private double e2;
    
    /**
     *
     * @param name
     * @param a
     * @param b
     */
    public Ellipsoid(String name,double a,double b)  {
	this.name = name;
	this.a = a;
	this.b = b;
	this.f = (a-b)/a;	
	
	double a2 = Math.pow(a,2);
	double b2 = Math.pow(b,2);
	
	e1 = Math.sqrt(a2-b2)/a;
	e2 = Math.sqrt(a2-b2)/b;
	
	/*
	
	double e11 = (1-Math.pow((b/a),2));
	
	System.out.println("Name "+getName());
	System.out.println("1. FirstEccentricity "+e1);
	System.out.println("----------");	
	System.out.println("1. SquareFirstEccentricity "+getSquareFirstEccentricity());		
	System.out.println("2. SquareFirstEccentricity "+e11);//1−(b/a)2
	System.out.println("----------");
	
	System.out.println("Name "+getName()+"\nf = "+f+"\n1/f = "+getInvF()+"\ne1 = "+e1+"\ne2 = "+e2+"\ne1^2 = "+getSquareFirstEccentricity());
	*/
    }

    /**
     *
     * @return
     */
    public String getName() {
	return name;
    }
    
    /**
     *
     * @return
     */
    public double getInvF() {
	return 1/getPinFlatteningElipsoidy();
    }

    /**
     *
     * @return
     */
    public double getSemiMinorAxis() {
	return b;
    }
    
    /**
     *
     * @return
     */
    public double getSemiMajorAxis() {
	return a;
    }

    /**
     *
     * @return
     */
    public double getPinFlatteningElipsoidy() {
	return f;
    }

    /**
     *
     * @return
     */
    public double getFirstEccentricity() {
	return e1;
    }

    /**
     *
     * @return
     */
    public double getSecondEccentricity() {
	return e2;
    }

    /**
     *
     * @return
     */
    public double getSquareFirstEccentricity() {
	return ( (getPinFlatteningElipsoidy()*2) - Math.pow(getPinFlatteningElipsoidy(),2) );
    }

    /**
     *
     * @return
     */
    public double getSquareSecondEccentricity() {
	return ( Math.pow(getSecondEccentricity(),2) / (1-Math.pow(getSecondEccentricity(),2)) );
    }

}
