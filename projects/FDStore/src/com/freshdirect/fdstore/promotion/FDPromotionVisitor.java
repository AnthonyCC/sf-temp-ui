package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.ZonePromoDiscount;
import com.freshdirect.customer.ErpDiscountLineModel;
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
		Set<String> combinableOffers = applyPromotions(context, eligibilities);
		
        //Add applied line item discounts to the applied list.
        Set<String> appliedSet =  context.getLineItemDiscountCodes();
        for (Iterator<String> i = appliedSet.iterator(); i.hasNext();) {
        	String code = i.next();
        	if(eligibilities.isEligible(code)) 
        		eligibilities.setApplied(code);
        }
        
		LOGGER.info("Promotion eligibility: after apply " + eligibilities);
		LOGGER.info("Promotion eligibility:context.isPostPromoConflictEnabled() " + context.isPostPromoConflictEnabled());
		
		if(context.isPostPromoConflictEnabled()){
			// post resolve conflict
			boolean e = postResolveConflicts(context,eligibilities);
			if(e)
				context.getUser().setPromoConflictResolutionApplied(true);
			LOGGER.info("Promotion eligibility: after postResolveConflicts() " + eligibilities);
			context.getUser().setPostPromoConflictEnabled(false);
			
		}		
		//Now Apply redemption promo if any.
		
        //Get the redemption promotion if user redeemed one.
		double redemptionValue = 0.0;
		String redeemCode = "";
        PromotionI redeemedPromotion = context.getRedeemedPromotion();
        if(redeemedPromotion != null){
        	redeemCode = redeemedPromotion.getPromotionCode();
        	if(eligibilities.isEligible(redeemCode)) {
        		boolean e = redeemedPromotion.apply(context);
        		if(e){
        			eligibilities.setApplied(redeemedPromotion.getPromotionCode());
        			if(redeemedPromotion.isDollarValueDiscount())
        				redemptionValue = context.getShoppingCart().getDiscountValue(redeemCode);
        		}
        	}
        }
        

        
        
        //Reconcile the discounts to make sure total header discounts does not exceed pre-deduction total(subtotal + dlv charge + tax).
        reconcileDiscounts(context, eligibilities, combinableOffers, redemptionValue);
       
		LOGGER.info("Promotion eligibility: after redemption apply " + eligibilities);
		//applyZonePromotion(context, eligibilities);
		LOGGER.info("Apply Promotions - END TIME ");
		long endTime = System.currentTimeMillis();
		LOGGER.info("Apply Promotions - TOTAL EXECUTION TIME "+(endTime - startTime)+" milliseconds");
		//LOGGER.info("Promotion eligibility: after applyZonePromotion() " + eligibilities);
		
		return eligibilities;
	}




	private static void reconcileDiscounts(PromotionContextI context,
			FDPromotionEligibility eligibilities, Set<String> combinableOffers,
			double redemptionValue) {
		double remainingBalance = context.getShoppingCart().getPreDeductionTotal() - redemptionValue;
        for (Iterator<String> i = combinableOffers.iterator(); i.hasNext();) {
        	String promoCode = i.next();
        	if(remainingBalance <= 0) {
        		context.getShoppingCart().removeDiscount(promoCode);
        	} else {
	        	double oldDiscountAmt = context.getShoppingCart().getDiscountValue(promoCode);
	        	double newDiscountAmt = Math.min(remainingBalance, oldDiscountAmt);
	        	if(oldDiscountAmt != newDiscountAmt) {
		        	context.getShoppingCart().removeDiscount(promoCode);
		        	context.getShoppingCart().addDiscount(new Discount(promoCode, EnumDiscountType.DOLLAR_OFF, newDiscountAmt));
	        	}
	        	//Discount can be applied now.
	        	eligibilities.setApplied(promoCode);
	        	remainingBalance -= newDiscountAmt;
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
               if(promoCode.equals("CD_1278620655837")){
            	   System.out.println();
               }
               boolean e = autopromotion.evaluate(context);
               eligibilities.setEligibility(promoCode, e);
               if(e && autopromotion.isFavoritesOnly()) eligibilities.addRecommendedPromo(promoCode); 
        }
         
         //Get the redemption promotion if user redeemed one and evaluate it.
         PromotionI redeemedPromotion = context.getRedeemedPromotion();
         if(redeemedPromotion != null){
               boolean e = redeemedPromotion.evaluate(context);
               String promoCode = redeemedPromotion.getPromotionCode();
               eligibilities.setEligibility(promoCode, e);
               if(e && redeemedPromotion.isFavoritesOnly()) eligibilities.addRecommendedPromo(promoCode);
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
							(!allowMultipleHeader && (p.isHeaderDiscount() || p.isLineItemDiscount())&& p.isRedemption()) 
							|| 
							p.isSignupDiscount())
						  ){
				found = true;
			}else{ 
				if (found && !p.isCombineOffer()) {
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

		if (promos.size() <= promoCodes.size()) {
			Set<String> actualPromoCodes = new LinkedHashSet<String>(promos.size());
			for (Iterator<Promotion> i = promos.iterator(); i.hasNext();) {
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
		
		// check if line item discount exists along with non combinable header promo.
		PromotionI nonCombinableHeaderPromo = context.getNonCombinableHeaderPromotion();
		double lineItemDiscAmount = context.getTotalLineItemDiscount();
		if(nonCombinableHeaderPromo == null || lineItemDiscAmount <= 0) return false;
		
		// also check if the allow header promotion flag is off 		
		boolean isLineItemCombinable = eligibilities.isLineItemCombinable();
		String headerPromoCode=nonCombinableHeaderPromo.getPromotionCode();
		if(isLineItemCombinable){
			//CLear the automatic non combinable header discount. Keep the line item.
			context.getShoppingCart().removeDiscount(headerPromoCode);
			eligibilities.removeAppliedPromo(headerPromoCode);
		}else{
			//Check which one has a higher value. Give the higher value promotion.
			Discount headerDiscount = context.getShoppingCart().getDiscount(headerPromoCode);
			if(lineItemDiscAmount >=  headerDiscount.getAmount()){
				//CLear the automatic header discount. Keep the line item discount(s).
				context.getShoppingCart().removeDiscount(headerPromoCode);
				eligibilities.removeAppliedPromo(headerPromoCode);
			} else {
				//CLear the line item discount(s). Keep the automatic header discount.
				context.clearLineItemDiscounts();
				eligibilities.removeAppliedLineItemPromotions();
			}
		}
		return true;
	}			

	private static Set applyPromotions(PromotionContextI context, FDPromotionEligibility eligibilities) {
        String headerPromoCode = "";
      //Step 1: Process all sample, delivery promo, extend DP promo, automatic non-combinable header and line item offers.
        for (Iterator i = eligibilities.getEligiblePromotionCodes().iterator(); i.hasNext();) {
              String promoCode = (String) i.next();
              PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
              if(!promo.isDollarValueDiscount() || (!promo.isRedemption() && !promo.isCombineOffer())) {
            	  	
                    boolean applied = promo.apply(context);
                    if (applied) {
                          if(promo.isHeaderDiscount()){
                                //This logic has been added to filter the max discount promocode
                                //when there are more than one. Currently this happens only in the
                                //case of automatic header discounts.
                                headerPromoCode = promoCode;
                          } else if(!promo.isLineItemDiscount()){
                                //Add any non-line item/header promos to the applied list. 
                                eligibilities.setApplied(promoCode);      
                          }
                    }
              }
        }
        boolean isCombinableOfferApplied = false;
        //Step 2: Process all automatic combinable header and line item offers.
        Set<String> combinableOffers = new HashSet<String>();
        for (Iterator<String> i = eligibilities.getEligiblePromotionCodes().iterator(); i.hasNext();) {
            String promoCode = (String) i.next();
            PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
            if(promo.isDollarValueDiscount() && !promo.isRedemption() && promo.isCombineOffer()) {
          	  	//Process all automatic combinable header and line item offers.
                  boolean applied = promo.apply(context);
                  if (applied ) {
                	  if(promo.isHeaderDiscount()){
                		  isCombinableOfferApplied = true;  
                		  combinableOffers.add(promoCode);
                	  } 
                	  /*
                	  if(!promo.isLineItemDiscount()){
	                      //Add applied  promos to the applied list. 
	                      eligibilities.setApplied(promoCode);
                  	  }*/
                  }
            }
      }
        //Add the final header promo code to the applied list from Step 1 if isCombinableOfferApplied is false. 
        if(headerPromoCode.length() > 0 && eligibilities.isEligible(headerPromoCode)){
        	if(!isCombinableOfferApplied)
              eligibilities.setApplied(headerPromoCode);
        	else{
        		//remove non combinable header promo.
        		String code = context.getNonCombinableHeaderPromotion().getPromotionCode();
        		context.getShoppingCart().removeDiscount(code);
        	}
        }
        return combinableOffers;

  }

}
