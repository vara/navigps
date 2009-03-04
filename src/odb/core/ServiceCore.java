package odb.core;

/**
 *
 * @author ACME
 */
public class ServiceCore {
    private ServiceAttributes serviceAttributes;
    private ServiceDescription serviceDescription;

    public ServiceCore(ServiceAttributes serviceAttributes, ServiceDescription serviceDescription) {
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
    public ServiceAttributes getServiceAttributes() {
        return serviceAttributes;
    }

    /**
     * @param serviceAttributes the serviceAttributes to set
     */
    public void setServiceAttributes(ServiceAttributes serviceAttributes) {
        this.serviceAttributes = serviceAttributes;
    }
}
