package odb.core;

import org.apache.batik.dom.svg.SVGOMPoint;

public class SubElementService extends RootElementStreet {
    
    private String serviceNumber;
    private String serviceName;
    private String serviceCategory;
    private String additionalAttribute1;
    private RootElementStreet serviceStreet;
    private SVGOMPoint svgx;
    private SVGOMPoint svgy;
    private double localx;
    private double localy;
    
    
    public SubElementService(RootElementStreet street, String num, String name, String category,SVGOMPoint x,SVGOMPoint y,double localx,double localy) {
        this.serviceStreet = street;
        this.serviceNumber = num;
        this.serviceName = name;
        this.serviceCategory = category;
        this.svgx = x;
        this.svgy = y;
        this.localx = localx;
        this.localy = localy;
        
        serviceStreet.addService(this);
        
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

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
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
}
