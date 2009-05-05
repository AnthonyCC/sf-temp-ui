/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.WrapperRecommendationService;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
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
        if(!isEligibleForSavings(input, variantId)) return Collections.EMPTY_LIST;
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
            if (!cartSuggestions.contains(orderLine.lookupProduct()) && 
            		(variantId.equals(orderLine.getSavingsId()) || variantId.equals(orderLine.getVariantId()))) {
                cartSuggestions.add(orderLine.lookupProduct());
            }
        }
        if (cartSuggestions.size() < input.getMaxRecommendations()) {
            List internalRec = internal.recommendNodes(input);
            Set filterProds = new HashSet(cartSuggestions.size());
            for (int i = 0; i < cartSuggestions.size(); i++) {
            	ContentNodeModel node = (ContentNodeModel) cartSuggestions.get(i);
            	filterProds.add(node.getContentKey());
            }
            internalRec = FDStoreRecommender.getInstance().filterProducts(internalRec, filterProds, false);            
            cartSuggestions.addAll(internalRec);
        }
        if (cartSuggestions.size() > input.getMaxRecommendations())
        	cartSuggestions = cartSuggestions.subList(0, input.getMaxRecommendations());
        //If there are no recommendations return empty list. No need set to previous recommendations map.
        if(cartSuggestions.size() == 0 ) {
        	return cartSuggestions;
        }

        if (prevMap == null) {
            prevMap = new HashMap();
            input.setPreviousRecommendations(prevMap);
        }
        prevMap.put(variantId, SmartStoreUtil.toContentKeysFromModels(cartSuggestions));
        return cartSuggestions;
    }

	private boolean isEligibleForSavings(SessionInput sessionInput, String variantId) {
		if(sessionInput.isCheckForEnoughSavingsMode()) return true; 
		
		Map prevMap = sessionInput.getPreviousRecommendations();
		if((prevMap != null && !prevMap.containsKey(variantId))) return false;
		
        if(FDStoreProperties.isFeaturePriorityEnabled() 
        		&& !(variantId.equals(getPriorityVariant(sessionInput)))) {
        	return false;
        }
		 
		Map promoVariantMap = sessionInput.getPromoVariantMap();
		if(promoVariantMap == null || promoVariantMap.size() == 0)
			return false;
		PromoVariantModel pvModel = (PromoVariantModel) promoVariantMap.get(variantId);

		// variant is marked as savings although it has no promotion
		if (pvModel == null)
			return false;
		/*
		String promoCode =pvModel.getAssignedPromotion().getPromotionCode();
		if(sessionInput.getEligiblePromotions().contains(promoCode)) {
			return true;
		}
		*/
		return true;
	}
	
	private String getPriorityVariant(SessionInput sessionInput) {
		int priority = 0;
		String priorityVariant = "";
		Map prevMap = sessionInput.getPreviousRecommendations();
		Map promoVariantMap = sessionInput.getPromoVariantMap();
		for(Iterator it=promoVariantMap.keySet().iterator(); it.hasNext();){
			String variantId = (String) it.next();
			if(!prevMap.containsKey(variantId)) continue;
			PromoVariantModel pvModel = (PromoVariantModel) promoVariantMap.get(variantId);
			if(pvModel.getVariantPriority() > priority) {
				priorityVariant = pvModel.getVariantId();
				priority = pvModel.getVariantPriority();
			}
		}
		return priorityVariant;
	}
	
	public boolean isSmartSavings() {
		return true;
	}
	
	public boolean isRefreshable() {
		return false;
	}
}
