/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (variantId.equals(orderLine.getSavingsId()) || variantId.equals(orderLine.getVariantId())) {
                cartSuggestions.add(orderLine.lookupProduct());
            }
        }
        if (cartSuggestions.size() < input.getMaxRecommendations()) {
            List internalRec = internal.recommendNodes(input);
            // we have to filter out cart items from the internally recommended item list.
            // segabor: why?
            internalRec = FDStoreRecommender.getInstance().filterProducts(internalRec, input.getCartContents(), true);            
            cartSuggestions.addAll(internalRec);
        }
        if (cartSuggestions.size() > input.getMaxRecommendations())
        	cartSuggestions = cartSuggestions.subList(0, input.getMaxRecommendations());
        if (prevMap == null) {
            prevMap = new HashMap();
            input.setPreviousRecommendations(prevMap);
        }
        prevMap.put(variantId, SmartStoreUtil.toContentKeysFromModels(cartSuggestions));
        return cartSuggestions;
    }

	private boolean isEligibleForSavings(SessionInput sessionInput, String variantId) {
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
	
	public boolean isSmartSavings() {
		return true;
	}
	
	public boolean isRefreshable() {
		return false;
	}
}
