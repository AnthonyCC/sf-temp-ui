package com.freshdirect.fdstore.customer.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.PromoVariantCache;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.VariantSelection;

public class PromoVariantHelper {

	public static Map getPromoVariantMap(FDUserI user, FDPromotionEligibility eligibilities){
		try{
			Set recommendedPromos = eligibilities.getRecommendedPromos();
			eligibilities.setEligiblity(recommendedPromos, false);
	        VariantSelection helper = VariantSelection.getInstance();
	        List ssFeatures = EnumSiteFeature.getSmartSavingsFeatureList();
	        Map promoVariantMap = new HashMap();
	        
	        for(Iterator it = ssFeatures.iterator(); it.hasNext();){
	            // fetch variant assignment (cohort -> variant map)
	        	EnumSiteFeature siteFeature = (EnumSiteFeature) it.next();

				// lookup overridden variant
				OverriddenVariantsHelper vhelper = new OverriddenVariantsHelper(user);
				String variantId = vhelper.getOverriddenVariant(siteFeature);
				if(variantId == null){
		            Map assignment = helper.getVariantMap(siteFeature);
		            String cohortId = user.getCohortName();
		            variantId = (String) assignment.get(cohortId);
				}
				
				if(variantId != null) {
					List promoVariants = PromoVariantCache.getInstance().getAllPromoVariants(variantId);
		            if(promoVariants != null){
		                for(Iterator iter = promoVariants.iterator(); iter.hasNext();){
		                	PromoVariantModel promoVariant = (PromoVariantModel) iter.next();
		                	String promoCode = promoVariant.getAssignedPromotion().getPromotionCode();
		                	if(recommendedPromos != null && recommendedPromos.contains(promoCode)) {
		                		promoVariantMap.put(variantId, promoVariant);
		                		eligibilities.setEligibility(promoCode, true);
		                		break;
		                	}
		                }
		            }
	        	}
	        }
	        return promoVariantMap;
		} catch(FDResourceException fe){
			throw new RuntimeException(fe);
		}
	}
	
	public static void updateSavingsVariant(FDUserI user, Map savingsLookupTable){
		Map pvMap = user.getPromoVariantMap();
		if(pvMap == null || pvMap.size() == 0) return ;
		String savVariant = null;
		Set keys = pvMap.keySet();
		if(keys.size() > 1) { //If it is more than 1 then hit recommender to check for recommendations.
			FDStoreRecommender recommender = FDStoreRecommender.getInstance();
			SessionInput input = new SessionInput(user);
			List availablePromoVariants = new ArrayList();
			int recSize = 0;
			for(Iterator it = keys.iterator(); it.hasNext();){
				String variantId = (String) it.next();
				PromoVariantModel pv = (PromoVariantModel)pvMap.get(variantId);
				if(savingsLookupTable != null && savingsLookupTable.containsKey(variantId)) {
					recSize = ((Integer)savingsLookupTable.get(variantId)).intValue();
				}else {
					try {
						input.setCheckForEnoughSavingsMode(true);
						Recommendations rec = recommender.getRecommendations(pv.getSiteFeature(), user, input, null);
						recSize = rec.getProducts().size();
						savingsLookupTable.put(variantId, new Integer(recSize));
						input.setCheckForEnoughSavingsMode(false);
					}catch (FDResourceException e) {
						//Do nothing.
	                }
				}
				  if (recSize > 0) {
					  //no recommendations found for this smart saving feature. remove it from the promo variant map.
					  availablePromoVariants.add(pv);
				  }
			}
			if(availablePromoVariants.size() == 0) {
				//no savings variant available to show.
				return;
			}
			if(availablePromoVariants.size() > 1)
				Collections.sort(availablePromoVariants, PromoVariantCache.FEATURE_PRIORITY_COMPARATOR);
			PromoVariantModel selected = (PromoVariantModel) availablePromoVariants.get(0);
			savVariant = selected.getVariantId();
		} else {//If it is only 1 variant then return that.
			savVariant = (String)keys.iterator().next();
		}
		user.setSavingsVariantId(savVariant);
	}
}
