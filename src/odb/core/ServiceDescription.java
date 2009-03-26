package odb.core;

/**
 *
 * @author ACME
 */
public class ServiceDescription {

    private String serviceNumber;
    private String serviceName;
    private String serviceStreet;
    private Category category;
    private Subcategory serviceSubCategory;
    private String additionaInfo;
    private ServiceCore sc;
    private String city;

    public ServiceDescription(String number, String name, String street, Category category, Subcategory subcategory, String additionalInfo,String city) {
        this.serviceNumber = number;
        this.serviceName = name;
        this.serviceStreet = street;
        this.category = category;
        this.serviceSubCategory = subcategory;
        this.additionaInfo = additionalInfo;
        this.city = city;
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

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the sc
     */
    public ServiceCore getServiceCore() {
        return sc;
    }

    /**
     * @param sc the sc to set
     */
    public void setServiceCore(ServiceCore sc) {
        this.sc = sc;
    }

    @Override
    public String toString() {
        String msg = getClass().getCanonicalName()+
                " [ Category : "+getCategory()+" [ Number :"+getServiceNumber()+
                " Name : "+getServiceName()+" Streat : "+getServiceStreet()+" ]]";
        return msg;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }


}