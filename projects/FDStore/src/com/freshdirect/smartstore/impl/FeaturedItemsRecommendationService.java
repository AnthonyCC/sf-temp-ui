/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.Collections;
import java.util.List;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.scoring.HelperFunctions;

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
    public FeaturedItemsRecommendationService(Variant variant, ImpressionSampler sampler,
    		boolean catAggr, boolean includeCartItems) {
        super(variant, sampler, catAggr, includeCartItems);
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

            featuredNodes = getFeaturedItems(model);
            featuredNodes = sampleContentNodeModels(input, featuredNodes);
        }

        return featuredNodes;
    }

    /**
     * Return a list of featured items.
     * 
     * @param model
     * @return
     */
    public static List getFeaturedItems(ContentNodeModel model) {
        if(model instanceof DepartmentModel) {
            return ((DepartmentModel) model).getFeaturedProducts();
        } else if (model instanceof CategoryModel) {
            return ((CategoryModel) model).getFeaturedProducts();
        } else if (model instanceof ProductModel) {
        	return ((CategoryModel) HelperFunctions.getToplevelCategory(model)).getFeaturedProducts();
        } else {
            Object value = model.getAttribute("FEATURED_PRODUCTS", null);
            if (value instanceof List) {
                return (List) value;
            }
        }
        return Collections.EMPTY_LIST;
    }

}
