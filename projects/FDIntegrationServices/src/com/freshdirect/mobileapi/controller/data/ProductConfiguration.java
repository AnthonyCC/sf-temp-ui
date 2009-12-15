package com.freshdirect.mobileapi.controller.data;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.mobileapi.model.ProductSelection;

public class ProductConfiguration {

    private Map<String, String> passback = new HashMap<String, String>();
    private String passbackString;

    public void setPassback(String passbackString) {
    	this.passbackString = passbackString;
    }
    
    public String getPassback() {
        String passbackString = null;
        if (!passback.isEmpty()) {
            StringBuilder passbackBuffer = new StringBuilder();
            for (String key : passback.keySet()) {
                if (passbackBuffer.length() == 0) {
                    passbackBuffer.append("?");
                } else {
                    passbackBuffer.append("&");
                }
                passbackBuffer.append(key).append("=").append(passback.get(key));
            }
            passbackString = passbackBuffer.toString();
        } else {
        	passbackString = this.passbackString;
        }
        return passbackString;
    }

    private ProductSearchResult product;

    private String configurationDescription;

    private Map<String, String> options;

    private SalesUnit salesUnit;

    private String skuCode;

    private Sku sku;

    private float quantity;

    public String getConfigurationDescription() {
        return configurationDescription;
    }

    public void setConfigurationDescription(String configurationDescription) {
        this.configurationDescription = configurationDescription;
    }

    public ProductSearchResult getProduct() {
        return product;
    }

    public void setProduct(ProductSearchResult product) {
        this.product = product;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
        if (null != sku) {
            this.skuCode = sku.getCode();
        }
    }

    public void populateProductWithModel(com.freshdirect.mobileapi.model.Product product, String sku) {
        this.product = ProductSearchResult.wrap(product);
        this.skuCode = sku;
    }

    public void populateProductWithModel(com.freshdirect.mobileapi.model.Product product, Sku sku) {
        //        Product newProduct = new Product();
        //        newProduct.setId(product.getProductId());
        //        newProduct.setFullName(product.getProductTitle());
        //        newProduct.setQuantityMinimum(product.getQuantityMinimum());
        //        newProduct.setQuantityMaximum(product.getQuantityMaximum());
        //        newProduct.setQuantityIncrement(product.getQuantityIncrement());
        //        newProduct.disableMessageMetaData();
        this.product = ProductSearchResult.wrap(product);
        this.product.setSku(sku);
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public SalesUnit getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(SalesUnit salesUnit) {
        this.salesUnit = salesUnit;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getSkuCode() {
        String skuCode = null;
        if (this.skuCode == null) {
            skuCode = product.getSku().getCode();
        } else {
            skuCode = this.skuCode;

        }
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getCategoryId() {
        return product.getCategoryId();
    }

    public String getProductId() {
        return product.getId();
    }

    public void setFromProductSelection(ProductSelection productSelection) {
        setQuantity(productSelection.getQuantity());
        setOptions(productSelection.getOptions());
        setConfigurationDescription(productSelection.getConfigurationDesc());
        setSalesUnit(new SalesUnit(productSelection.getSalesUnit()));
        setSkuCode(productSelection.getSkuCode());
    }

    public void addPassbackParam(String key, String value) {
        this.passback.put(key, value);
    }

}
