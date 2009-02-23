/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package odb.core;

import org.w3c.dom.Element;

/**
 *
 * @author ACME
 */
public class ServiceCore {
    private Element serviceAttributes;
    private ServiceDescription serviceDescription;

    public ServiceCore(Element serviceAttributes, ServiceDescription serviceDescription) {
        this.serviceAttributes = serviceAttributes;
        this.serviceDescription = serviceDescription;
    }

//    public ServiceCore(ServiceAttributes serviceAttributes, ServiceDescription serviceDescription) {
//        this.serviceAttributes = serviceAttributes;
//        this.serviceDescription = serviceDescription;
//    }

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

    /**
     * @return the serviceAttributes
     */
    public Element getServiceAttributes() {
        return serviceAttributes;
    }

    /**
     * @param serviceAttributes the serviceAttributes to set
     */
    public void setServiceAttributes(Element serviceAttributes) {
        this.serviceAttributes = serviceAttributes;
    }
}
