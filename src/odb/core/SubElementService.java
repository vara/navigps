package odb.core;

import org.apache.batik.dom.svg.SVGOMPoint;

public class SubElementService extends RootElementStreet {
    
    private String serviceNumber;
    private String serviceName;
    private Subcategory serviceSubCategory;
    private String additionalAttribute1;
    private RootElementStreet serviceStreet;
    private SVGOMPoint svg;
    private double localx;
    private double localy;
    
    
    public SubElementService(RootElementStreet street, String num, String name, Subcategory subcategory,SVGOMPoint x,double localx,double localy, String add) {
        this.serviceStreet = street;
        this.serviceNumber = num;
        this.serviceName = name;
        this.serviceSubCategory = subcategory;
        this.additionalAttribute1 = add;
        this.svg = x;
        this.localx = localx;
        this.localy = localy;
        
        //serviceStreet.addService(this);
        
    }
    
    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAdditionalAttribute1() {
        return additionalAttribute1;
    }

    public void setAdditionalAttribute1(String additionalAttribute1) {
        this.additionalAttribute1 = additionalAttribute1;
    }

    public String getServiceStreet() {
        return serviceStreet.getName();
    }

    public void setServiceStreet(RootElementStreet serviceStreet) {
        this.serviceStreet = serviceStreet;
    }

    public Subcategory getServiceSubCategory() {
        return serviceSubCategory;
    }

    public void setServiceSubCategory(Subcategory serviceSubCategory) {
        this.serviceSubCategory = serviceSubCategory;
    }

    public SVGOMPoint getSvg() {
        return svg;
    }

    public void setSvg(SVGOMPoint svg) {
        this.svg = svg;
    }
    
}
