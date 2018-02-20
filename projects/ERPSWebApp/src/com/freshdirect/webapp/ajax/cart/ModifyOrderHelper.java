package com.freshdirect.webapp.ajax.cart;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.giftcard.FDGiftCardI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.ExtendDeliveryPassApplicator;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionApplicatorI;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.RedemptionCodeStrategy;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class ModifyOrderHelper {

	private static Category LOGGER = LoggerFactory.getInstance(ModifyOrderHelper.class);

	public static void handleDeliveryPassPromotion(FDSessionUser currentUser, FDStandingOrder currentStandingOrder, EnumCheckoutMode checkOutMode,
			FDOrderAdapter order, FDModifyCartModel cart) throws FDResourceException, FDInvalidConfigurationException {
		Set<String> usedPromoCodes = order.getSale().getUsedPromotionCodes();
		for(Iterator<String> it = usedPromoCodes.iterator(); it.hasNext();){
			PromotionI promo  = PromotionFactory.getInstance().getPromotion(it.next());
			if(promo != null && promo.getOfferType() != null && promo.getOfferType().equals(EnumOfferType.DP_EXTN)){
				//APPDEV-2850 - combinable offers
				ExtendDeliveryPassApplicator app = null;
				for (Iterator<PromotionApplicatorI> i = ((Promotion)promo).getApplicatorList().iterator(); i.hasNext();) {
					PromotionApplicatorI _applicator = i.next();
					if (_applicator instanceof ExtendDeliveryPassApplicator) {
						app = (ExtendDeliveryPassApplicator)_applicator;
					}
				}

				if(app != null) {
					cart.setCurrentDlvPassExtendDays(app.getExtendDays());
					break;
				}
				/*
				ExtendDeliveryPassApplicator app = (ExtendDeliveryPassApplicator)((Promotion)promo).getApplicator();
				cart.setCurrentDlvPassExtendDays(app.getExtendDays());
				break;
				*/
			}
		}

		cart.refreshAll(true);
		currentUser.setCurrentStandingOrder(currentStandingOrder);
		currentUser.setCheckoutMode(checkOutMode);
		currentUser.setShoppingCart( cart );
	}

	public static void handleReservation(FDOrderAdapter order, FDModifyCartModel cart) throws FDResourceException {
		LOGGER.info("Get Reservation By Id: "+ order.getDeliveryReservationId() + " Sale ID: "+ order.getSale().getId());
		FDReservation reservation = FDDeliveryManager.getInstance().getReservation( order.getDeliveryReservationId(), order.getSale().getId() );
		cart.setDeliveryReservation(reservation);
	}

	public static void handleRedemptionPromotions(FDSessionUser currentUser, FDOrderAdapter order) {
		for ( String promoCode : order.getUsedPromotionCodes() ) {
			Promotion promo = (Promotion) PromotionFactory.getInstance().getPromotion(promoCode);
			RedemptionCodeStrategy redemption = (RedemptionCodeStrategy) promo.getStrategy(RedemptionCodeStrategy.class);
			if (redemption != null) {
				currentUser.setRedeemedPromotion(PromotionFactory.getInstance().getPromotion(promoCode));
			}
		}

		// recalculate promotion
		currentUser.invalidateCache();
		currentUser.updateUserState( );
	}

	public static void handleCoupons(HttpSession session) {
		FDCustomerCouponUtil.getCustomerCoupons(session);
		FDCustomerCouponUtil.evaluateCartAndCoupons(session);
	}

	public static void updateSession(FDSessionUser currentUser, HttpSession session) {
		session.setAttribute( SessionName.USER, currentUser );
        //The previous recommendations of the current user need to be removed.
        session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
	}

	public static void handleDlvPass(FDModifyCartModel cart, FDUserI fdUser) {
		if(fdUser.getShoppingCart().getDeliveryPassCount()>0 || fdUser.isDlvPassActive() || ( fdUser.applyFreeTrailOptinBasedDP() )){
			cart.setDlvPassApplied(true);
		}
	}

	public static void loadGiftCardsIntoCart(FDUserI user, FDOrderI originalOrder) {
		FDGiftCardInfoList gcList = user.getGiftCardList();
		//Clear any hold amounts.
		if(null !=gcList){
			gcList.clearAllHoldAmount();
	    	List<ErpAppliedGiftCardModel> appliedGiftCards = originalOrder.getAppliedGiftCards();
	    	if(appliedGiftCards != null && appliedGiftCards.size() > 0) {
		    	for( ErpAppliedGiftCardModel agcmodel : appliedGiftCards ) {
		    		String certNum = agcmodel.getCertificateNum();
		    		FDGiftCardI fg = gcList.getGiftCard(certNum);
		    		if(fg != null) {
		    			//Found. Gift card already validated. set hold amount = amount applied on this order.
		    			fg.setHoldAmount(originalOrder.getAppliedAmount(certNum));
		    		}
		    	}
	    	}
		}
	}

	public static void handleModificationCutoff(FDOrderAdapter order, FDSessionUser currentUser, HttpSession session, ActionResult results) {
		if(EnumEStoreId.FDX.name().equalsIgnoreCase(order.getEStoreId().name())){
			if (EnumSaleStatus.INPROCESS.equals(order.getSaleStatus()) || new Date().after(order.getDeliveryInfo().getDeliveryCutoffTime())) {
				results.addError(true, "invalid_reservation", MessageFormat.format(
					SystemMessageList.MSG_CHECKOUT_PAST_CUTOFF_MODIFY,
					new Object[] {order.getDeliveryInfo().getDeliveryCutoffTime()}) );
			}else{
				// Give minimum MOD_Y min for order modification and max till the timeslot cutoff. if sysdate + MOD_Y is past cutoff then give till sysdate + MOD_Y to modify the order. if less then
				// give till cutoff time.
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MINUTE, (int)order.getDeliveryInfo().getMinDurationForModification());
				if(cal.getTime().after(order.getDeliveryInfo().getDeliveryCutoffTime())){
					order.getDeliveryInfo().setDeliveryCutoffTime(cal.getTime());
					order.getDeliveryReservation().getTimeslot().setCutoffDateTime(cal.getTime());
					order.getDeliveryReservation().getTimeslot().setCutoffTime(new TimeOfDay(cal.getTime()));

				}else{
					order.getDeliveryInfo().setDeliveryCutoffTime(order.getDeliveryInfo().getDeliveryCutoffTime());
					order.getDeliveryReservation().getTimeslot().setCutoffDateTime(order.getDeliveryInfo().getDeliveryCutoffTime());
					order.getDeliveryReservation().getTimeslot().setCutoffTime(new TimeOfDay(order.getDeliveryInfo().getDeliveryCutoffTime()));
				}
			}

			EnumTransactionSource transactionSource = (session.getAttribute(SessionName.CUSTOMER_SERVICE_REP)!=null
					|| CrmSession.getCurrentAgent(session)!=null
					|| currentUser.getMasqueradeContext()!=null) ? EnumTransactionSource.CUSTOMER_REP : EnumTransactionSource.WEBSITE;


			if((EnumTransactionSource.CUSTOMER_REP.equals(transactionSource)) ||
						(order.getDeliveryInfo().getDeliveryCutoffTime()!=null &&
						order.getDeliveryInfo().getOriginalCutoffTime()!=null &&
						order.getDeliveryInfo().getDeliveryCutoffTime().after(order.getDeliveryInfo().getOriginalCutoffTime()))){
				order.getDeliveryInfo().setDeliveryCutoffTime(order.getDeliveryInfo().getOriginalCutoffTime());
				order.getDeliveryReservation().getTimeslot().setCutoffDateTime(order.getDeliveryInfo().getOriginalCutoffTime());
				order.getDeliveryReservation().getTimeslot().setCutoffTime(new TimeOfDay(order.getDeliveryInfo().getOriginalCutoffTime()));
			}

		}

	}

}
