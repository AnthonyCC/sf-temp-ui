package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDPromotionEligibility;
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
		LOGGER.info("Promotion eligibility:after resolve conflicts " + eligibilities);
		applyPromotions(context, eligibilities);
		LOGGER.info("Promotion eligibility: after apply " + eligibilities);
		
		LOGGER.info("Apply Promotions - END TIME ");
		long endTime = System.currentTimeMillis();
		LOGGER.info("Apply Promotions - TOTAL EXECUTION TIME "+(endTime - startTime)+" milliseconds");
		return eligibilities;
	}

	/**
	 * @return Map of promotionCode String -> EnumPromotionEligibility
	 */
	/*private static FDPromotionEligibility evaluatePromotions(PromotionContextI context) {
		FDPromotionEligibility eligibilities = new FDPromotionEligibility();

		Collection promotions = FDPromotionFactory.getInstance().getPromotions();
		for (Iterator i = promotions.iterator(); i.hasNext();) {
			PromotionI promotion = (PromotionI) i.next();

			boolean e = promotion.evaluate(context);

			eligibilities.setEligibility(promotion.getPromotionCode(), e);
		}

		return eligibilities;
	}*/

	private static FDPromotionEligibility evaluatePromotions(PromotionContextI context) {
		long startTime = System.currentTimeMillis();
		FDPromotionEligibility eligibilities = new FDPromotionEligibility();
		//Get All Automatic Promo codes.  Evaluate them.
		Collection promotions = PromotionFactory.getInstance().getAllAutomaticPromotions();
		for (Iterator i = promotions.iterator(); i.hasNext();) {
			PromotionI autopromotion  = (PromotionI) i.next();
			boolean e = autopromotion.evaluate(context);
			eligibilities.setEligibility(autopromotion.getPromotionCode(), e);
		}
		//Get the redemption promotion if user redeemed one and evaluate it.
		PromotionI redeemedPromotion = context.getRedeemedPromotion();
		if(redeemedPromotion != null){
			boolean e = redeemedPromotion.evaluate(context);
			eligibilities.setEligibility(redeemedPromotion.getPromotionCode(), e);
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

	private static void applyPromotions(PromotionContextI context, FDPromotionEligibility eligibilities) {
		String headerPromoCode = "";
		for (Iterator i = eligibilities.getEligiblePromotionCodes().iterator(); i.hasNext();) {
			String promoCode = (String) i.next();
			PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
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
