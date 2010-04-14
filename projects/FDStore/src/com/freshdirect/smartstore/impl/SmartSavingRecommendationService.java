/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentNodeModel;
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

    public List<ContentNodeModel> recommendNodes(SessionInput input) {
    	if(input.isCheckForEnoughSavingsMode()) {
    		return internal.recommendNodes(input);
    	}
    		
        String variantId = getVariant().getId();
        if(!variantId.equals(input.getSavingsVariantId())) {
            return Collections.emptyList();
        }
        //if(!isEligibleForSavings(input, variantId)) return Collections.EMPTY_LIST;
        Map<String, List<ContentKey>> prevMap = input.getPreviousRecommendations();
        if (prevMap != null) {
            List<ContentKey> prevRecommendations = prevMap.get(variantId);
            if (prevRecommendations != null) {
                return SmartStoreUtil.toContentNodesFromKeys(prevRecommendations);
            }
        }
        FDCartModel cartModel = input.getCartModel();
        List<ContentNodeModel> cartSuggestions = new ArrayList<ContentNodeModel>();
        for (int i = 0; i < cartModel.numberOfOrderLines(); i++) {
            FDCartLineI orderLine = cartModel.getOrderLine(i);
            if (!cartSuggestions.contains(orderLine.lookupProduct()) && 
            		(variantId.equals(orderLine.getSavingsId()))) {
                cartSuggestions.add(orderLine.lookupProduct());
            }
        }
        if (cartSuggestions.size() < input.getMaxRecommendations()) {
            List<ContentNodeModel> internalRec = internal.recommendNodes(input);
            Set<ContentKey> filterProds = new HashSet<ContentKey>(cartSuggestions.size());
            for (int i = 0; i < cartSuggestions.size(); i++) {
            	ContentNodeModel node = cartSuggestions.get(i);
            	filterProds.add(node.getContentKey());
            }
            cartSuggestions.addAll(internalRec);
        }
        if (cartSuggestions.size() > input.getMaxRecommendations())
        	cartSuggestions = cartSuggestions.subList(0, input.getMaxRecommendations());

        if (prevMap == null) {
            prevMap = new HashMap<String, List<ContentKey>>();
            input.setPreviousRecommendations(prevMap);
        }
        prevMap.put(variantId, SmartStoreUtil.toContentKeysFromModels(cartSuggestions));
        return cartSuggestions;
    }

	
	public boolean isSmartSavings() {
		return true;
	}
	
	public boolean isRefreshable() {
		return false;
	}
}
