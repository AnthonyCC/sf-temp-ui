package com.freshdirect.fdstore.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDUserI;

public class PromotionHelper {
	
	/**
	 * This method returns a set of eligible windows steering promotion codes for the given zone.
	 * @param zoneId
	 * @param user
	 * @return
	 */
	public static Set<String> getEligiblePromoCodes(FDUserI user, String zoneId) {
		Set<String> zonePromoCodes = new HashSet<String>();
		Set<String> eligiblePromoCodes = user.getPromotionEligibility().getEligiblePromotionCodes();
		for(Iterator<String> it=eligiblePromoCodes.iterator(); it.hasNext();){
			String promoId = it.next();
			Promotion p = (Promotion) PromotionFactory.getInstance().getPromotion(promoId);
			if(p !=  null && p.getOfferType() != null && p.getOfferType().equals(EnumOfferType.WINDOW_STEERING)){
				PromotionApplicatorI app = p.getApplicator();
				DlvZoneStrategy zoneStrategy = app != null ? app.getDlvZoneStrategy() : null;
				if(zoneStrategy != null && zoneStrategy.isZonePresent(zoneId))
					zonePromoCodes.add(promoId);
			}
		}
		return zonePromoCodes;
	}
	
	 public static double getDiscount(FDUserI user, FDTimeslot timeSlot) {
		 Set<String> eligiblePromoCodes = user.getPromotionEligibility().getEligiblePromotionCodes();
		 if(null == eligiblePromoCodes || eligiblePromoCodes.isEmpty()) return 0.0;
		 Discount applied = null;
		 for(Iterator<String> it=eligiblePromoCodes.iterator(); it.hasNext();){
				String promoId = it.next();
				Promotion p = (Promotion) PromotionFactory.getInstance().getPromotion(promoId);
				if(p !=  null && p.getOfferType() != null && p.getOfferType().equals(EnumOfferType.WINDOW_STEERING)){
					PromotionApplicatorI app = p.getApplicator();
					DlvZoneStrategy zoneStrategy = app != null ? app.getDlvZoneStrategy() : null;
					if(zoneStrategy != null && zoneStrategy.isZonePresent(timeSlot.getZoneCode()) 
							&& zoneStrategy.isTimeSlotEligible(timeSlot)){
						double promoAmt = p.getHeaderDiscountTotal();
						if(isMaxDiscountAmount(promoAmt, p.getPriority(), applied)){
							applied = new Discount(promoId, EnumDiscountType.DOLLAR_OFF, promoAmt);
							
						}
					}
				}
		 }
		 return applied != null ? applied.getAmount() :  0.0;
	 }
	 
		private static boolean isMaxDiscountAmount(double promotionAmt, int priority, Discount applied) {
			if(applied == null) return true;
			boolean flag = false;
			String appliedCode = applied.getPromotionCode();
			PromotionI appliedPromo = PromotionFactory.getInstance().getPromotion(appliedCode);
			if((priority < appliedPromo.getPriority()) ||
					(priority == appliedPromo.getPriority() &&
							promotionAmt > applied.getAmount())){
				//The applied promo priority is less than the one that is being applied.
				//or the applied promo amount is less than the one that is being applied.
				flag = true;
			}
			return flag;
		}
}
