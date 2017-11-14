package com.freshdirect.webapp.ajax.holidaymealbundle.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ComponentGroupModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.EnumLayoutType;
import com.freshdirect.storeapi.content.EnumProductLayout;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.holidaymealbundle.data.HolidayMealBundleContainer;
import com.freshdirect.webapp.ajax.holidaymealbundle.data.HolidayMealBundleIncludeMealData;
import com.freshdirect.webapp.ajax.holidaymealbundle.data.HolidayMealBundleIncludeMealProductData;
import com.freshdirect.webapp.ajax.holidaymealbundle.data.HolidayMealBundleOptionalProductData;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.util.MediaUtils;

public class HolidayMealBundleService {

    private static final HolidayMealBundleService INSTANCE = new HolidayMealBundleService();

    private HolidayMealBundleService() {
    }

    public static HolidayMealBundleService defaultService() {
        return INSTANCE;
    }

    public String populateHolidayMealCategoryMedia(NavigationModel navigationModel) {
        String categoryMedia = null;
        if (navigationModel != null && navigationModel.getSelectedContentNodeModel() != null && isCategoryModelLayoutTypeHolidayMealBundle(navigationModel.getSelectedContentNodeModel())) {
            categoryMedia = MediaUtils.renderHtmlToString(navigationModel.getSelectedContentNodeModel().getEditorial(), null);
        }
        return categoryMedia;
    }

    public HolidayMealBundleContainer populateHolidayMealBundleData(SkuModel skuModel, FDUserI user) throws FDSkuNotFoundException, FDResourceException, HttpErrorResponse {
        HolidayMealBundleContainer container = new HolidayMealBundleContainer();
        if(skuModel==null) return container;
        ProductModel productModel = skuModel.getProductModel();
        if (productModel != null && productModel.getParentNode() != null && isProductModelLayoutTypeHolidayMealBundle(productModel)) {
            container.setMealIncludeDatas(populateIncludeMealDatas(skuModel));
            container.setOptionalProducts(populateAvailableOptionalProducts(productModel, user));
        }
        return container;
    }

    private List<HolidayMealBundleIncludeMealData> populateIncludeMealDatas(SkuModel skuModel) throws FDSkuNotFoundException, FDResourceException {
        List<HolidayMealBundleIncludeMealData> mealIncludes = new ArrayList<HolidayMealBundleIncludeMealData>();
        if (skuModel != null) {
            List<ComponentGroupModel> componentGroups = skuModel.getProductModel().getComponentGroups();
            if (componentGroups != null) {
                for (ComponentGroupModel componentGroup : componentGroups) {
                    if (componentGroup.getOptionalProducts().isEmpty()) {
                        mealIncludes.add(createIncludeMealData(componentGroup, populateIncludeMealProducts(componentGroup, skuModel.getProduct())));
                    }
                }
            }
        }
        return mealIncludes;
    }

    private List<HolidayMealBundleIncludeMealProductData> populateIncludeMealProducts(ComponentGroupModel componentGroup, FDProduct product) throws FDSkuNotFoundException {
        List<HolidayMealBundleIncludeMealProductData> includeMealProducts = new ArrayList<HolidayMealBundleIncludeMealProductData>();
        Map<String, FDVariationOption[]> variationOptions = new HashMap<String, FDVariationOption[]>();
        for (FDVariation variation : product.getVariations()) {
            variationOptions.put(variation.getName(), variation.getVariationOptions());
        }
        for (String characteristicName : componentGroup.getCharacteristicNames()) {
            if (variationOptions.containsKey(characteristicName)) {
                for (FDVariationOption variationOption : variationOptions.get(characteristicName)) {
                    ProductModel sideBoxProductModel = ContentFactory.getInstance().getProduct(variationOption.getSkuCode());
                    List<ProductModel> includeSideBoxProductModels = sideBoxProductModel.getIncludeProducts();
                    if (includeSideBoxProductModels.isEmpty()) {
                        includeMealProducts.add(createIncludeMealProductData(sideBoxProductModel));
                    } else {
                        for (ProductModel includeSideBoxProductModel : includeSideBoxProductModels) {
                            includeMealProducts.add(createIncludeMealProductData(includeSideBoxProductModel));
                        }
                    }
                }
            }
        }
        return includeMealProducts;
    }

    private List<HolidayMealBundleOptionalProductData> populateAvailableOptionalProducts(ProductModel productModel, FDUserI user)
            throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse {
        List<HolidayMealBundleOptionalProductData> includeMealOptionalProducts = new ArrayList<HolidayMealBundleOptionalProductData>();
        List<ComponentGroupModel> componentGroups = productModel.getComponentGroups();
        if (componentGroups != null) {
            for (ComponentGroupModel componentGroup : componentGroups) {
                List<ProductModel> availableOptionalProducts = componentGroup.getAvailableOptionalProducts();
                if (!availableOptionalProducts.isEmpty()) {
                    List<ProductData> optionalProducts = new ArrayList<ProductData>();
                    for (ProductModel availableOptionalProduct : availableOptionalProducts) {
                        optionalProducts.add(ProductDetailPopulator.createProductData(user, availableOptionalProduct));
                    }
                    includeMealOptionalProducts.add(createOptionalData(componentGroup, optionalProducts));
                }
            }
        }
        return includeMealOptionalProducts;
    }

    private HolidayMealBundleOptionalProductData createOptionalData(ComponentGroupModel componentGroup, List<ProductData> availableOptionalProductDatas) {
        HolidayMealBundleOptionalProductData optionalProductData = new HolidayMealBundleOptionalProductData();
        optionalProductData.setFullName(componentGroup.getFullName());
        optionalProductData.setEditorial(MediaUtils.renderHtmlToString(componentGroup.getEditorial(), null));
        optionalProductData.setOptionalProducts(availableOptionalProductDatas);
        return optionalProductData;
    }

    private HolidayMealBundleIncludeMealData createIncludeMealData(ComponentGroupModel componentGroup, List<HolidayMealBundleIncludeMealProductData> includeMealProducts)
            throws FDSkuNotFoundException {
        HolidayMealBundleIncludeMealData mealIncludeData = new HolidayMealBundleIncludeMealData();
        mealIncludeData.setComponentGroupId(componentGroup.getContentKey().getId());
        mealIncludeData.setComponentGroupName(componentGroup.getFullName());
        mealIncludeData.setIncludeMealProducts(includeMealProducts);
        return mealIncludeData;
    }

    private HolidayMealBundleIncludeMealProductData createIncludeMealProductData(ProductModel productModel) {
        HolidayMealBundleIncludeMealProductData mealIncludeData = new HolidayMealBundleIncludeMealProductData();
        mealIncludeData.setProductId(productModel.getContentKey().getId());
        mealIncludeData.setCategoryId(productModel.getCategory().getContentKey().getId());
        mealIncludeData.setLabel(productModel.getFullName());
        mealIncludeData.setDescription(MediaUtils.renderHtmlToString(productModel.getProductDescription(), null));
        Image sideImage = productModel.getFeatureImage();
        if (sideImage != null) {
            mealIncludeData.setImagePath(sideImage.getPath());
        }
        return mealIncludeData;
    }

    private boolean isCategoryModelLayoutTypeHolidayMealBundle(ContentNodeModel contentNodeModel) {
        return contentNodeModel instanceof CategoryModel && (EnumLayoutType.HOLIDAY_MEAL_BUNDLE_CATEGORY.equals(((CategoryModel) contentNodeModel).getSpecialLayout()));
    }

    public boolean isProductModelLayoutTypeHolidayMealBundle(ContentNodeModel contentNodeModel) {
        return contentNodeModel instanceof ProductModel && (EnumProductLayout.HOLIDAY_MEAL_BUNDLE_PRODUCT.equals(((ProductModel) contentNodeModel).getSpecialLayout()));
    }
}
