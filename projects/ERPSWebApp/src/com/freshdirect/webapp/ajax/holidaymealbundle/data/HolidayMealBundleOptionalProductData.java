package com.freshdirect.webapp.ajax.holidaymealbundle.data;

import java.util.List;

import com.freshdirect.webapp.ajax.product.data.ProductData;

public class HolidayMealBundleOptionalProductData {

    private String fullName;
    private String editorial;
    private List<ProductData> optionalProducts;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public List<ProductData> getOptionalProducts() {
        return optionalProducts;
    }

    public void setOptionalProducts(List<ProductData> optionalProducts) {
        this.optionalProducts = optionalProducts;
    }
}
