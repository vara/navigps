package app.database.odb.core;

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
/*
    @Override
    public boolean equals(Object obj) {

        if(obj instanceof ServiceCore){
            return hashCode() == obj.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.serviceAttributes != null ? this.serviceAttributes.hashCode() : 0);
        hash = 67 * hash + (this.serviceDescription != null ? this.serviceDescription.hashCode() : 0);
        hash = 67 * hash + (this.oid != null ? (int) (this.oid.getObjectId() ^ (this.oid.getObjectId()) >>> 32) : 0);
        return hash;
    }
*/
}
