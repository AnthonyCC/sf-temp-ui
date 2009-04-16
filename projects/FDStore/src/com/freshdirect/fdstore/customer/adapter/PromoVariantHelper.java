package com.freshdirect.fdstore.customer.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.PromoVariantCache;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.fdstore.promotion.PromotionContextI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.smartstore.fdstore.VariantSelection;

public class PromoVariantHelper {

	public static Map getPromoVariantMap(FDUserI user){
		try{
			
	        VariantSelection helper = VariantSelection.getInstance();
	        List ssFeatures = EnumSiteFeature.getSmartSavingsFeatureList();
	        List eligiblePVList = new ArrayList();
	        for(Iterator it = ssFeatures.iterator(); it.hasNext();){
	            // fetch variant assignment (cohort -> variant map)
	        	EnumSiteFeature siteFeature = EnumSiteFeature.getEnum((String)it.next());
	        	if(!SmartStoreUtil.isCustomerEligible(user, siteFeature)) continue;
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
		                	//if(lineItemPromoCodes.contains(promoVariant.getAssignedPromotion().getPromotionCode())) {
		                		//promoVariantMap.put(variantId, promoVariant.getPromoCode());
		                		eligiblePVList.add(promoVariant);
		                		break;
		                	//}
		                }
		            }
	        	}
	        }
	        Map promoVariantMap = new HashMap();
	        if(eligiblePVList!= null && eligiblePVList.size() > 0) {
	        	if(eligiblePVList.size() > 1)//If the user is elgible for more than one smart savings site feature.
	        		Collections.sort(eligiblePVList, PromoVariantCache.FEATURE_PRIORITY_COMPARATOR);
		        //Get the top priority feature promo variant as the eligible promo variant.
		        PromoVariantModel eligiblePV = (PromoVariantModel)eligiblePVList.get(0);
		        promoVariantMap.put(eligiblePV.getVariantId(), eligiblePV);
		        //context.getUser().setPromoVariantMap(promoVariantMap);
	        }
	        return promoVariantMap;
		} catch(FDResourceException fe){
			throw new RuntimeException(fe);
		}
	}
}
