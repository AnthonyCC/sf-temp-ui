package com.freshdirect.fdstore.promotion;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter;

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
		
	
		public static boolean checkPromoEligibilityForAddress(FDUserI user, AddressModel shippingAddress) throws FDResourceException{
			Promotion promotion = (Promotion)user.getRedeemedPromotion();
			//This block is executed only if current eligibility is true. So default is set to true.
			boolean isEligible = true;
			if(null != promotion && null != shippingAddress){
				FDIdentity identity = user.getIdentity();
				PromotionContextI promotionContext = new PromotionContextAdapter(user);
				EnumOrderType orderType = promotionContext.getOrderType(shippingAddress);
				Calendar date = new GregorianCalendar();
				date.add( Calendar.DATE, 7 );
				try {
					DlvZoneInfoModel zoneInfo =  FDDeliveryManager.getInstance().getZoneInfo(shippingAddress, date.getTime());
					if(null != zoneInfo){
						for (Iterator<PromotionStrategyI> i = promotion.getStrategies().iterator(); i.hasNext();) {
							PromotionStrategyI strategy = i.next();
							if (strategy instanceof DlvZoneStrategy) {
								DlvZoneStrategy dlvZoneStrategy = (DlvZoneStrategy) strategy;
								isEligible = dlvZoneStrategy.isZonePresent(zoneInfo.getZoneCode());
							} else if (strategy instanceof GeographyStrategy) {
								GeographyStrategy geoGraphyStrategy = (GeographyStrategy)strategy;
								String depotCode = promotionContext.getDepotCode(shippingAddress);
								isEligible = geoGraphyStrategy.evaluate(promotion.getPromotionCode(), orderType, shippingAddress.getZipCode(), depotCode);												
							} else if (strategy instanceof CustomerStrategy) {
								CustomerStrategy custStrategy = (CustomerStrategy)strategy;
								isEligible = custStrategy.evaluateOrderType(orderType);
							}
							if(!isEligible) break;
						}
					}
				} catch (FDInvalidAddressException e) {
					throw new FDResourceException(e);
				}
			}
			return isEligible;		
		}
		
		public static boolean checkPromoEligibilityForTimeslot(FDUserI user, String timeSlotId) throws FDResourceException{
			//This block is executed only if current eligibility is true. So default is set to true.
			boolean isEligible = true;
			Promotion promotion = (Promotion)user.getRedeemedPromotion();
			if(null != promotion){
				if(null != timeSlotId && !"".equals(timeSlotId)){					
					if(timeSlotId.startsWith("f_")) {
						timeSlotId = timeSlotId.replaceAll("f_", "");
					}
					FDTimeslot timeSlot = FDDeliveryManager.getInstance().getTimeslotsById(timeSlotId);
					if(null != timeSlot){
						PromotionApplicatorI app = promotion.getApplicator();
						if (app != null) {
							DlvZoneStrategy dlvZoneStrategy = app.getDlvZoneStrategy();
							isEligible = dlvZoneStrategy != null ? dlvZoneStrategy.isTimeSlotEligible(timeSlot) : isEligible;								
						}
					}
					
				}
				
			}
			return isEligible;
		}
		
		public static boolean checkPromoEligibilityForPayment(FDUserI user, String paymentId)throws FDResourceException{
			//This block is executed only if current eligibility is true. So default is set to true.
			boolean isEligible = true;
			Promotion promotion = (Promotion)user.getRedeemedPromotion();
			if(null != promotion){
				FDIdentity identity = user.getIdentity();
				PromotionContextI promotionContext = new PromotionContextAdapter(user);
				Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods( identity );
				ErpPaymentMethodI paymentMethod = null;
	
				for ( ErpPaymentMethodI item : paymentMethods ) {
					if ( item.getPK().getId().equals( paymentId ) ) {
						paymentMethod = item;
						break;
					}
				}
				if(null != paymentMethod){
					for (Iterator<PromotionStrategyI> i = promotion.getStrategies().iterator(); i.hasNext();) {
						PromotionStrategyI strategy = i.next();
						if (strategy instanceof CustomerStrategy) {
							CustomerStrategy customerStrategy = (CustomerStrategy) strategy;							
							isEligible = customerStrategy.evaluateByPaymentCardType(paymentMethod.getCardType(), promotionContext);				
							break;
						}
					}
				}
			}
			return isEligible;
		}
}
