package odb.core;

import java.util.Vector;

public class RootElementStreet {
    
    private String name;
    private String additionalAttributeA;
    private String additionalAttributeB;
    private Vector services;

    public RootElementStreet(String s,String attribA,String attribB) {
        this.name = s;
        this.additionalAttributeA = attribA;
        this.additionalAttributeB = attribB;
        this.services = null;
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
}