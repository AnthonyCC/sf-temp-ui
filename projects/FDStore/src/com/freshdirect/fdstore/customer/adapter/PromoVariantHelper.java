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
import com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper;
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
}
