/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModelImpl;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;

/**
 * @author zsombor
 *
 */
public class ManualOverrideRecommendationService extends CandidateProductRecommendationService {

    /**
     * @param variant
     */
    public ManualOverrideRecommendationService(Variant variant) {
        super(variant);
    }
    
    public List recommendNodes(SessionInput input) {
        if (input.getCurrentNode() != null) {
            ContentNodeModel model = input.getCurrentNode();
            if (model instanceof CategoryModel) {
                CategoryModel category = (CategoryModel) model;
                int slots = category.getManualSelectionSlots();
                
                List result = new ArrayList(100);
                fillManualSlots(input, category, slots, result);
                
                //result = sampleContentNodeModels(input, collectNodes(category, result));
                List randomChildProducts = sampleRankedContents(input, collectNodes(category), result);
                
                result.addAll(randomChildProducts);
                return result;
            }
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Collects product models into the result parameter.
     * 
     * @param input
     * @param category
     * @param slots
     * @param result
     */
    protected void fillManualSlots(SessionInput input, CategoryModel category, int slots, List result) {
        List fprods = category.getFeaturedProducts();
        Random rnd = new Random();
        while (result.size()<slots && fprods.size()>0) {
            int pos = input.isNoShuffle() ? 0 : rnd.nextInt(fprods.size());
            Object product = fprods.remove(pos);
            
            if (((ProductModelImpl)product).isDisplayable()) {
                result.add(product);
            }
        }
    }

}
