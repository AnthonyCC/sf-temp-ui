package com.freshdirect.webapp.ajax.holidaymealbundle.data;

import java.util.List;

public class HolidayMealBundleIncludeMealData {

    private String componentGroupId;
    private String componentGroupName;
    private List<HolidayMealBundleIncludeMealProductData> includeMealProducts;

    public String getComponentGroupId() {
        return componentGroupId;
    }

    public void setComponentGroupId(String componentGroupId) {
        this.componentGroupId = componentGroupId;
    }

    public String getComponentGroupName() {
        return componentGroupName;
    }

    public void setComponentGroupName(String componentGroupName) {
        this.componentGroupName = componentGroupName;
    }

    public List<HolidayMealBundleIncludeMealProductData> getIncludeMealProducts() {
        return includeMealProducts;
    }

    public void setIncludeMealProducts(List<HolidayMealBundleIncludeMealProductData> includeMealProducts) {
        this.includeMealProducts = includeMealProducts;
    }


}
