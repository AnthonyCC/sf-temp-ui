package com.freshdirect.webapp.ajax.product.data;

import java.util.List;

public class HolidayMealBundleContainer {

    private List<HolidayMealBundleMealIncludeData> mealIncludeDatas;
    private List<HolidayMealBundleOptionalProductData> optionalProducts;

    public List<HolidayMealBundleMealIncludeData> getMealIncludeDatas() {
        return mealIncludeDatas;
    }

    public void setMealIncludeDatas(List<HolidayMealBundleMealIncludeData> mealIncludeDatas) {
        this.mealIncludeDatas = mealIncludeDatas;
    }

    public List<HolidayMealBundleOptionalProductData> getOptionalProducts() {
        return optionalProducts;
    }

    public void setOptionalProducts(List<HolidayMealBundleOptionalProductData> optionalProducts) {
        this.optionalProducts = optionalProducts;
    }

}
