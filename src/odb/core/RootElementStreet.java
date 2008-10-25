package odb.core;

import java.util.Vector;
import org.apache.batik.dom.svg.SVGOMPoint;

public class RootElementStreet {
    
    private String name;
    private String additionalAttributeA;
    private String additionalAttributeB;
    private Vector services;
    private SVGOMPoint svgPoint;
    private double localx;
    private double localy;
    

    public RootElementStreet(String s,String attribA,String attribB,SVGOMPoint svgPoint,double localx,double localy) {
        this.name = s;
        this.additionalAttributeA = attribA;
        this.additionalAttributeB = attribB;
        this.services = null;
        this.svgPoint = svgPoint;
        this.localx = localx;
        this.localy = localy;
    }
    
    public RootElementStreet() {
        
    }

    public String getName() {
        return name;
    }

    public String getAdditionalAttributeA() { 
        return additionalAttributeA;
    }

    public String getAdditionalAttributeB() {
        return additionalAttributeB;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdditionalAttributeA(String additionalAttributeA) {
        this.additionalAttributeA = additionalAttributeA;
    }

    public void setAdditionalAttributeB(String additionalAttributeB) {
        this.additionalAttributeB = additionalAttributeB;
    }

    public Vector getServices() {
        return services;
    }

    public void setServices(Vector services) {
        this.services = services;
    }
    
    public void addService(SubElementService service) {
        services.add(service);
    }

    public double getLocalx() {
        return localx;
    }

    public void setLocalx(double localx) {
        this.localx = localx;
    }

    public double getLocaly() {
        return localy;
    }

    public void setLocaly(double localy) {
        this.localy = localy;
    }

    public SVGOMPoint getSvgPoint() {
        return svgPoint;
    }

    public void setSvgPoint(SVGOMPoint svgPoint) {
        this.svgPoint = svgPoint;
    }
}