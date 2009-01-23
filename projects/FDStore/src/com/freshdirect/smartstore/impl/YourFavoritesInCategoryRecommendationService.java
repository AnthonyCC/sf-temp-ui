/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ProductModelImpl;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.ProductStatisticsProvider;

/**
 * @author zsombor
 *
 */
public class YourFavoritesInCategoryRecommendationService extends ManualOverrideRecommendationService {

    /**
     * @param variant
     */
    public YourFavoritesInCategoryRecommendationService(Variant variant) {
        super(variant);
    }

    protected void fillManualSlots(SessionInput input, CategoryModel category, int slots, List result) {
        if (slots>0) {
            String customerId = input.getCustomerId();
            if (customerId!=null) {
                Map userProductScores = ProductStatisticsProvider.getInstance().getUserProductScores(customerId);
                if (userProductScores!=null && !userProductScores.isEmpty()) {
                    ProductModelImpl pm = findMyMostFavoriteProduct(category, userProductScores, 0);
                    if (pm!=null) {
                        result.add(pm);
                    }
                }
            }
            
            super.fillManualSlots(input, category, slots, result);
        }
    }

    ProductModelImpl findMyMostFavoriteProduct(CategoryModel category, Map userProductScores, float maxScore) {
        ProductModelImpl maxProd = null;

        List subcategories = category.getSubcategories();
        for (int i = 0; i < subcategories.size(); i++) {
            CategoryModel cm = (CategoryModel) subcategories.get(i);
            ProductModelImpl pi = findMyMostFavoriteProduct(cm, userProductScores, maxScore);
            if (pi!=null) {
                Float score = (Float) userProductScores.get(pi.getContentKey());
                maxScore = score.floatValue();
                maxProd = pi;
            }
        }
        
        List products = category.getProducts();
        for (int i = 0; i < products.size(); i++) {
            ProductModelImpl p = (ProductModelImpl) products.get(i);
            Float score = (Float) userProductScores.get(p.getContentKey());
            if (score != null && score.floatValue() > maxScore) {
                if (p.isDisplayable()) {
                    maxScore = score.floatValue();
                    maxProd = p;
                }
            }
        }
        return maxProd;
    }
}
