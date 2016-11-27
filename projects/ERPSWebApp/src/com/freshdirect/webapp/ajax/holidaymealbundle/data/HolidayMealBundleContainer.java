package com.freshdirect.webapp.ajax.holidaymealbundle.data;

import java.util.List;

public class HolidayMealBundleContainer {

    private List<HolidayMealBundleIncludeMealData> mealIncludeDatas;
    private List<HolidayMealBundleOptionalProductData> optionalProducts;

    public List<HolidayMealBundleIncludeMealData> getMealIncludeDatas() {
        return mealIncludeDatas;
    }

    public void setMealIncludeDatas(List<HolidayMealBundleIncludeMealData> mealIncludeDatas) {
        this.mealIncludeDatas = mealIncludeDatas;
    }

    public List<HolidayMealBundleOptionalProductData> getOptionalProducts() {
        return optionalProducts;
    }

    public void setOptionalProducts(List<HolidayMealBundleOptionalProductData> optionalProducts) {
        this.optionalProducts = optionalProducts;
    }

}
