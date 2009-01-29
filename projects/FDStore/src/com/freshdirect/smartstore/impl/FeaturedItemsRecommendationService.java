/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;

/**
 * TODO : think about, that the current RecommendationService contract states, that the recommend method should return a list of ContentKey-s. 
 *  
 * 
 * @author zsombor
 *
 */
public class FeaturedItemsRecommendationService extends AbstractRecommendationService {

    /**
     * @param variant
     */
    public FeaturedItemsRecommendationService(Variant variant) {
        super(variant);
    }

    /**
     * 
     * @param input
     * @return a List<{@link ContentNodeModel}> of recommendations
     *         
     */
    public List recommendNodes(SessionInput input) {
        List featuredNodes = Collections.EMPTY_LIST; 
        if (input.getCurrentNode()!=null) {
            ContentNodeModel model = input.getCurrentNode();

            if(model instanceof DepartmentModel) {
                featuredNodes = ((DepartmentModel) model).getFeaturedProducts();
            } else if (model instanceof CategoryModel) {
                featuredNodes = ((CategoryModel) model).getFeaturedProducts();
            } else {
                Object value = model.getAttribute("FEATURED_PRODUCTS", null);
                if (value instanceof List) {
                    featuredNodes = (List) value;
                }
            }
            featuredNodes = sampleContentNodeModels(input, featuredNodes);
        }

        return featuredNodes;
    }

}
