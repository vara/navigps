package app.database.odb.core;

/**
 *
 * @author ACME
 */
public class ServiceDescription {

    private String serviceNumber = "no service number";;
    private String serviceName = "no service name";
    private String serviceStreet = "no service street";
    private String serviceCity = "no city";
    private String serviceAdditionaInfo = "no additional info";

    private Category category ;
    private Subcategory serviceSubCategory;
    
    private ServiceCore sc;
    

    public ServiceDescription(String number, String name, String street, Category category, Subcategory subcategory, String additionalInfo,String city) {
        setServiceNumber(number);
        setServiceName(name);
        setServiceStreet(street);
        setCategory(category);
        setServiceSubCategory(subcategory);
        setAdditionaInfo(additionalInfo);
        setCity(city);
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
        if(serviceNumber != null)
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
        if(serviceName!=null)
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
        if(serviceStreet!=null)
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
        if(serviceSubCategory != null)
            this.serviceSubCategory = serviceSubCategory;
    }

    /**
     * @return the additionaInfo
     */
    public String getAdditionaInfo() {
        return serviceAdditionaInfo;
    }

    /**
     * @param additionaInfo the additionaInfo to set
     */
    public void setAdditionaInfo(String additionaInfo) {
        if(additionaInfo != null)
            this.serviceAdditionaInfo = additionaInfo;
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
        if(category != null)
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
        if(sc != null)
            this.sc = sc;
    }

    @Override
    public String toString() {     

        String msg = "Description:\nName: " + getServiceName() +
                     "\nCity: "+getCity()+
                     "\nStreet: " + getServiceStreet() +
                     "\nNumber: " + getServiceNumber() +
                     "\n" + getCategory() +
                     "\nSubcategory: " + 
                     (getServiceSubCategory() == null ? "no subcategory":getServiceSubCategory() )+
                     "\nAdditional: " + getAdditionaInfo();
        
        return msg;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return serviceCity;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        if(city != null)
            this.serviceCity = city;
    }
}