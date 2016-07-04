package com.freshdirect.webapp.ajax.mealkit.service;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.util.MediaUtils;

public class MealkitService {

    private static final MealkitService INSTANCE = new MealkitService();

    private MealkitService() {
    }

    public static MealkitService defaultService() {
        return INSTANCE;
    }

    public String populateMealkitCategoryMedia(NavigationModel navigationModel) {
        String categoryMedia = null;
        if (navigationModel != null && navigationModel.getSelectedContentNodeModel() != null && isCategoryModelLayoutTypeMealkit(navigationModel.getSelectedContentNodeModel())) {
            categoryMedia = MediaUtils.renderHtmlToString(navigationModel.getSelectedContentNodeModel().getEditorial(), null);
        }
        return categoryMedia;
    }

    public void populateData(ProductData data, ProductModel productModel) {
        // product quality note
        String productQualityNote = null;
        if (productModel != null && productModel.getProductQualityNote() != null) {
            productQualityNote = MediaUtils.renderHtmlToString(productModel.getProductQualityNote(), null);
        }
        data.setProductQualityNote(productQualityNote);

        // time to complete field
        final int value = productModel.getTimeToComplete();
        data.setTimeToComplete( value > 0 ? value : 0);
    }

    public boolean isProductModelLayoutTypeMealkit(ProductModel productModel){
        return (null != productModel) && (EnumProductLayout.RECIPE_MEALKIT_PRODUCT.equals(productModel.getSpecialLayout()));
    }
    
    private boolean isCategoryModelLayoutTypeMealkit(ContentNodeModel contentNodeModel){
        return ((contentNodeModel instanceof CategoryModel) && (EnumLayoutType.RECIPE_MEALKIT_CATEGORY.equals(((CategoryModel) contentNodeModel).getSpecialLayout())));
    }

}
