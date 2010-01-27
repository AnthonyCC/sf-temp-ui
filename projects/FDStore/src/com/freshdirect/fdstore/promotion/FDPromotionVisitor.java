package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.ZonePromoDiscount;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.PromoVariantHelper;
import com.freshdirect.framework.util.log.LoggerFactory;


public class FDPromotionVisitor {

	private static Category LOGGER = LoggerFactory.getInstance(FDPromotionVisitor.class);

	public static FDPromotionEligibility applyPromotions(PromotionContextI context) {
		long startTime = System.currentTimeMillis();
		LOGGER.info("Apply Promotions - START TIME ");
		List ruleBasedPromotions = FDPromotionRulesEngine.getEligiblePromotions(context);
		context.setRulePromoCode(ruleBasedPromotions);
		FDPromotionEligibility eligibilities = evaluatePromotions(context);
		LOGGER.info("Promotion eligibility:after evaluate " + eligibilities);
		resolveConflicts(eligibilities);
		resolveLineItemConflicts(context, eligibilities);
		LOGGER.info("Promotion eligibility:after resolve conflicts " + eligibilities);					
		applyPromotions(context, eligibilities);
		LOGGER.info("Promotion eligibility: after apply " + eligibilities);
		
		if(context.isPostPromoConflictEnabled()){
			// post resolve conflict
			boolean e = postResolveConflicts(context,eligibilities);
			if(e)
				context.getUser().setPromoConflictResolutionApplied(true);
			LOGGER.info("Promotion eligibility: after Post resolveconflict apply " + eligibilities);
			context.getUser().setPostPromoConflictEnabled(false);
			
		}		
		LOGGER.info("Apply Promotions - END TIME ");
		long endTime = System.currentTimeMillis();
		LOGGER.info("Apply Promotions - TOTAL EXECUTION TIME "+(endTime - startTime)+" milliseconds");
		
		applyZonePromotion(context, eligibilities);
		//removeDuplicates(context,eligibilities);
		return eligibilities;
	}

	
    private static void removeDuplicates(PromotionContextI context,
			FDPromotionEligibility eligibilities) {
            
		
	}


	private static void applyZonePromotion(PromotionContextI context, FDPromotionEligibility eligibilities) 
    {    
    	
    	if(FDPromotionZoneRulesEngine.isEligible(context))//iPhone check
    	{
			String promoCode=FDPromotionZoneRulesEngine.getPromoCode(context);
			//if(promoCode!=null&&eligibilities.isEligible(promoCode))
			if(promoCode !=null && eligibilities.isEligible(promoCode) )
			{
				PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
				if(promo.apply(context)) {
					Discount discount = new ZonePromoDiscount(promoCode, EnumDiscountType.DOLLAR_OFF,promo.getHeaderDiscountTotal() );
					context.addDiscount(discount);
				}
			} 
    	}
    }
	private static void resolveLineItemConflicts(PromotionContextI context, FDPromotionEligibility eligibilities) {
		//Reload the promo variant map based on new promotion eligibilities.	
		Map pvMap = PromoVariantHelper.getPromoVariantMap(context.getUser(), eligibilities);
		context.getUser().setPromoVariantMap(pvMap);
	}

	
	 private static FDPromotionEligibility evaluatePromotions(PromotionContextI context) {
         long startTime = System.currentTimeMillis();
         FDPromotionEligibility eligibilities = new FDPromotionEligibility();
         int counter = 0;
         //Get All Automatic Promo codes.  Evaluate them.
         Collection promotions = PromotionFactory.getInstance().getAllAutomaticPromotions(); 
         for (Iterator i = promotions.iterator(); i.hasNext();) {
               PromotionI autopromotion  = (PromotionI) i.next(); 
               String promoCode = autopromotion.getPromotionCode();
               boolean e = autopromotion.evaluate(context);
               eligibilities.setEligibility(promoCode, e);
               if(e && autopromotion.isRecommendedItemsOnly()) eligibilities.addRecommendedPromo(promoCode); 
        }
         
         //Get the redemption promotion if user redeemed one and evaluate it.
         PromotionI redeemedPromotion = context.getRedeemedPromotion();
         if(redeemedPromotion != null){
               boolean e = redeemedPromotion.evaluate(context);
               String promoCode = redeemedPromotion.getPromotionCode();
               eligibilities.setEligibility(promoCode, e);
               if(e && redeemedPromotion.isRecommendedItemsOnly()) eligibilities.addRecommendedPromo(promoCode);
         }
         long endTime = System.currentTimeMillis();
         return eligibilities;
   }

	protected static List resolveConflicts(boolean allowMultipleHeader, List promotions) {
		if (promotions.isEmpty() || promotions.size() == 1) {
			return promotions;
		}

		List l = new ArrayList(promotions);
		Collections.sort(l, new Comparator() {
			public int compare(Object o1, Object o2) {
				Promotion p1 = (Promotion) o1;
				Promotion p2 = (Promotion) o2;
				return p1.getPriority() - p2.getPriority();
			}
		});
		/*
		 * The following block of code eliminates all other automatic
		 * header discounts if a signup or a redemption header discount
		 * is present. Otherwise it restores all the automatic header discounts.
		 */
		boolean found = false;
		for (Iterator i = l.iterator(); i.hasNext();) {
			Promotion p = (Promotion) i.next();
			if (!found && (
							(!allowMultipleHeader && p.isHeaderDiscount()&& p.isRedemption()) 
							|| 
							p.isSignupDiscount())
						  ){
				found = true;
			}else{ 
				if (found) {
					//Any promotion after this point can be removed.
					i.remove();
				}
			}
		}
		return l;
	}
	
	
	/**
	 * Resolve potential conflicts b/w promotions (by altering eligibilities).
	 */
	private static void resolveConflicts(FDPromotionEligibility eligibilities) {
		Set promoCodes = eligibilities.getEligiblePromotionCodes();
		List promos = new ArrayList(promoCodes.size());
		for (Iterator i = promoCodes.iterator(); i.hasNext();) {
			String promoCode = (String) i.next();
			PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
			promos.add(promo);
		}

		promos = resolveConflicts(FDStoreProperties.useMultiplePromotions(), promos);

		if (promos.size() < promoCodes.size()) {
			Set actualPromoCodes = new HashSet(promos.size());
			for (Iterator i = promos.iterator(); i.hasNext();) {
				Promotion promo = (Promotion) i.next();
				actualPromoCodes.add(promo.getPromotionCode());
			}

			LOGGER.warn("Promotion conflict resolution from " + promoCodes + " retained " + actualPromoCodes);

			eligibilities.setEligiblity(promoCodes, false);
			eligibilities.setEligiblity(actualPromoCodes, true);
		}

	}	
	
	
	/**
	 * Resolve potential conflicts b/w promotions (by altering eligibilities).
	 */
	private static boolean postResolveConflicts(PromotionContextI context, FDPromotionEligibility eligibilities) {
		
		// check if line item discount exists
		// check if header discount allowed
		double headerDiscAmount=0;		 
		double lineItemDiscAmount=0;
				
		if(context.getHeaderDiscount()==null || context.getTotalLineItemDiscount()<=0) return false;
		
		// also check if the allow header promotion flag is off 		
		
		String linePromoCode=eligibilities.getAppliedLineItemPromoCode();
		if(linePromoCode!=null){
			PromotionI lineItemPromo = PromotionFactory.getInstance().getPromotion(linePromoCode);
			if(lineItemPromo.isAllowHeaderDiscount())
			   return false; 
		}
		String headerPromoCode=context.getHeaderDiscount().getPromotionCode();
		String lineItemPromoCode=eligibilities.getAppliedLineItemPromoCode();
		// clear the both discounts
		context.clearHeaderDiscounts();
		context.clearLineItemDiscounts();
		eligibilities.removeAppliedPromo(lineItemPromoCode);
		eligibilities.removeAppliedPromo(headerPromoCode);
		context.getUser().setRedeemedPromotion(null);
		
		PromotionI headerPromo = PromotionFactory.getInstance().getPromotion(headerPromoCode);
		if(headerPromo.apply(context)){		
			Discount headerDiscount=context.getHeaderDiscount();		
			if(headerDiscount!=null) headerDiscAmount=headerDiscount.getAmount();
		}
		
		if(headerPromo.isSignupDiscount() || headerPromo.isRedemption()){
			//Applied header discount
			eligibilities.setApplied(headerPromoCode);			
			if(headerPromo.isRedemption())
				context.getUser().setRedeemedPromotion(headerPromo);
			return true;
		}
						
		PromotionI lineItemPromo = PromotionFactory.getInstance().getPromotion(lineItemPromoCode);
		if(lineItemPromo.apply(context)){
			lineItemDiscAmount=context.getTotalLineItemDiscount();
		}
		if((headerDiscAmount!=0 && headerDiscAmount>=lineItemDiscAmount)) {
			//Applied header discount
			eligibilities.setApplied(headerPromoCode);			
			if(headerPromo.isRedemption())
				context.getUser().setRedeemedPromotion(headerPromo);
			context.clearLineItemDiscounts();
			
		}else if(lineItemDiscAmount!=0 && lineItemDiscAmount>headerDiscAmount) {
			// Applied line item discount
			eligibilities.setApplied(lineItemPromoCode);
			context.clearHeaderDiscounts();
			
		}	
		return true;
	}

	private static void applyPromotions(PromotionContextI context, FDPromotionEligibility eligibilities) {
		String headerPromoCode = "";
		for (Iterator i = eligibilities.getEligiblePromotionCodes().iterator(); i.hasNext();) {
			String promoCode = (String) i.next();
			PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
			if(!EnumPromotionType.WINDOW_STEERING.equals(promo.getPromotionType())) {
				boolean applied = promo.apply(context);
				if (applied) {
					if(promo.isHeaderDiscount()){
						//This logic has been added to filter the max discount promocode
						//when there are more than one. Currently this happens only in the
						//case of automatic header discounts.
						headerPromoCode = promoCode;
					} else {
						//Add any non-header promos to the applied list. 
						eligibilities.setApplied(promoCode);	
					}
				}
			}
			//Add the final header promo code to the applied list. 
			if(headerPromoCode.length() > 0 && eligibilities.isEligible(headerPromoCode)){
				eligibilities.setApplied(headerPromoCode);
			}
		}
	}


}
