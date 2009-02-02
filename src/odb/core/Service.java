/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package odb.core;

/**
 *
 * @author ACME
 */
public class Service {
    private ServiceAttributes serviceAttributes;
    private ServiceDescription serviceDescription;

    public Service(ServiceAttributes serviceAttributes, ServiceDescription serviceDescription) {
        this.serviceAttributes = serviceAttributes;
        this.serviceDescription = serviceDescription;
    }

    /**
     * @return the serviceAttributes
     */
    public ServiceAttributes getServiceAttributes() {
        return serviceAttributes;
    }

    /**
     * @param serviceAttributes the serviceAttributes to set
     */
    public void setServiceAttributes(ServiceAttributes serviceAttributes) {
        this.serviceAttributes = serviceAttributes;
    }

    /**
     * @return the serviceDescription
     */
    public ServiceDescription getServiceDescription() {
        return serviceDescription;
    }

    /**
     * @param serviceDescription the serviceDescription to set
     */
    public void setServiceDescription(ServiceDescription serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
}
