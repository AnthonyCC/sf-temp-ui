/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.WrapperRecommendationService;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;

/**
 * @author zsombor
 * 
 */
public class SmartSavingRecommendationService extends WrapperRecommendationService {

    /**
     * @param variant
     */
    public SmartSavingRecommendationService(RecommendationService internal) {
        super(internal);
    }

    public List recommendNodes(SessionInput input) {
        String variantId = getVariant().getId();
        Map prevMap = input.getPreviousRecommendations();
        if (prevMap != null) {
            List prevRecommendations = (List) prevMap.get(variantId);
            if (prevRecommendations != null) {
                return SmartStoreUtil.toContentNodesFromKeys(prevRecommendations);
            }
        }
        FDCartModel cartModel = input.getCartModel();
        List cartSuggestions = new ArrayList();
        for (int i = 0; i < cartModel.numberOfOrderLines(); i++) {
            FDCartLineI orderLine = cartModel.getOrderLine(i);
            if (variantId.equals(orderLine.getVariantId())) {
                cartSuggestions.add(orderLine.lookupProduct());
            }
        }
        if (input.getMaxRecommendations() < cartSuggestions.size()) {
            List internalRec = internal.recommendNodes(input);
            cartSuggestions.addAll(internalRec);
        }
        if (prevMap == null) {
            prevMap = new HashMap();
            input.setPreviousRecommendations(prevMap);
        }
        prevMap.put(variantId, SmartStoreUtil.toContentKeysFromModels(cartSuggestions));
        return cartSuggestions;
    }

}
