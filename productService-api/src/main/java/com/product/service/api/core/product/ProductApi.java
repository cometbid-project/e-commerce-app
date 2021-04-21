package com.product.service.api.core.product;

/**
 *
 * @author Adedayo
 */
public class ProductApi {
    
    private int productId; 
    private String name;
    private int weight; 
    private String description;
    private String serviceAddress;
    
    
    public ProductApi(){
        productId = 0;
        name = null;
        weight = 0;
        description = null;
        serviceAddress = null;
    }
    
    public ProductApi(int productId, String name, int weight, String description, String serviceAddress ) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.description = description;
        this.serviceAddress = serviceAddress;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

   public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
     public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
    
}
