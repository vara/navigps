package odb.core;

import org.neodatis.odb.OID;

/**
 *
 * @author ACME
 */
public class ServiceCore {
    private ServiceAttributes serviceAttributes;
    private ServiceDescription serviceDescription;
    private OID oid;

    /**
     *
     * @param serviceAttributes
     * @param serviceDescription
     */
    public ServiceCore(ServiceAttributes serviceAttributes, ServiceDescription serviceDescription) {
        this.serviceAttributes = serviceAttributes;
        this.serviceDescription = serviceDescription;
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
     * @return the oid
     */
    public OID getOID() {
        return oid;
    }

    /**
     * @param oid the oid to set
     */
    public void setOID(OID oid) {
        this.oid = oid;
    }
}
