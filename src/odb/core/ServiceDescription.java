package odb.core;

public class ServiceDescription {

    private String serviceNumber;
    private String serviceName;
    private String serviceStreet;
    private Subcategory serviceSubCategory;
    private String additionaInfo;

    public ServiceDescription(String number, String name, String street, Subcategory subcategory, String additionalInfo) {
        this.serviceNumber = number;
        this.serviceName = name;
        this.serviceStreet = street;
        this.serviceSubCategory = subcategory;
        this.additionaInfo = additionalInfo;
    }

    /**
     * @return the serviceNumber
     */
    public String getServiceNumber() {
        return serviceNumber;
    }

    /**
     * @param serviceNumber the serviceNumber to set
     */
    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the serviceStreet
     */
    public String getServiceStreet() {
        return serviceStreet;
    }

    /**
     * @param serviceStreet the serviceStreet to set
     */
    public void setServiceStreet(String serviceStreet) {
        this.serviceStreet = serviceStreet;
    }

    /**
     * @return the serviceSubCategory
     */
    public Subcategory getServiceSubCategory() {
        return serviceSubCategory;
    }

    /**
     * @param serviceSubCategory the serviceSubCategory to set
     */
    public void setServiceSubCategory(Subcategory serviceSubCategory) {
        this.serviceSubCategory = serviceSubCategory;
    }

    /**
     * @return the additionaInfo
     */
    public String getAdditionaInfo() {
        return additionaInfo;
    }

    /**
     * @param additionaInfo the additionaInfo to set
     */
    public void setAdditionaInfo(String additionaInfo) {
        this.additionaInfo = additionaInfo;
    }
}