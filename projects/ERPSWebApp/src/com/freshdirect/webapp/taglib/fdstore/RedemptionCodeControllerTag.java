package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import sun.security.x509.CertAndKeyGen;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.PromotionContextAdapter;
import com.freshdirect.fdstore.giftcard.FDGiftCardI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.fdstore.promotion.CustomerStrategy;
import com.freshdirect.fdstore.promotion.DlvZoneStrategy;
import com.freshdirect.fdstore.promotion.EnumOrderType;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionApplicatorI;
import com.freshdirect.fdstore.promotion.PromotionContextI;
import com.freshdirect.fdstore.promotion.PromotionErrorType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionHelper;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.PromotionStrategyI;
import com.freshdirect.fdstore.promotion.WaiveChargeApplicator;
import com.freshdirect.fdstore.promotion.management.FDPromotionManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class RedemptionCodeControllerTag extends AbstractControllerTag {

	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String action = request.getParameter("action");
		try {
			if ("removeCode".equalsIgnoreCase(action)) {
				HttpSession session = (HttpSession) pageContext.getSession();
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				
				PromotionI promo = user.getRedeemedPromotion();
				if(promo != null){
					//MNT - 144 Bug fix.
					PromotionApplicatorI applicator = ((Promotion)promo).getApplicator();
					if(applicator instanceof WaiveChargeApplicator){
						WaiveChargeApplicator waiveChargeApplicator = (WaiveChargeApplicator)applicator;
						if(waiveChargeApplicator.getChargeType() == EnumChargeType.DELIVERY){
							/*
							 * Then its a delivery promotion. So reset the isDlvPromoApplied flag
							 * since the redemption code is removed.
							 */
							user.getShoppingCart().setDlvPromotionApplied(false);
						}
					}
					
					user.setRedeemedPromotion(null);
					
					user.updateUserState();
					session.setAttribute(SessionName.USER, user);
				}
			}
			else if ("removeGiftCard".equalsIgnoreCase(action)) {
				HttpSession session = (HttpSession) pageContext.getSession();
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				
				FDGiftCardInfoList giftCards = user.getGiftCardList();
				if(giftCards != null){
					giftCards.clearAllSelection();
				}
			}
			else if ("applyGiftCard".equalsIgnoreCase(action)) {
				String certNum = request.getParameter("certNum");
				String value = request.getParameter("value");
				HttpSession session = (HttpSession) pageContext.getSession();
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				FDGiftCardInfoList giftCards = user.getGiftCardList();
				if(certNum != null && certNum.length() > 0){
					giftCards.setSelected(certNum, new Boolean(value).booleanValue());
				}
			}
			else if ("deleteGiftCard".equalsIgnoreCase(action)) {
				String certNum = request.getParameter("certNum");
				String value = request.getParameter("value");
				HttpSession session = (HttpSession) pageContext.getSession();
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				FDGiftCardInfoList giftCards = user.getGiftCardList();
				FDGiftCardInfoList giftCards1 = FDCustomerManager.getGiftCards(user.getIdentity());
				if(certNum != null && certNum.length() > 0){
					
					FDGiftCardModel model = (FDGiftCardModel)giftCards1.getGiftCard(certNum);
					FDCustomerManager.removePaymentMethod(AccountActivityUtil
							.getActionInfo(pageContext.getSession()), model.getGiftCardModel());
					//Remove it from user cache.
					giftCards.remove(certNum);
				}
		}
		}catch(FDResourceException fre){
			throw new JspException(fre);
		}
		return true;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if ("redeemCode".equals(this.getActionName())) {
			try{
				HttpSession session = (HttpSession) pageContext.getSession();
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				request.setAttribute("isEligible", false);
				String redemptionCode = NVL.apply(request.getParameter("redemptionCode"), "").trim();
				if ("".equals(redemptionCode)) {
					actionResult.addError(true, "redemption_error", "Promotion Code is required");
					return true;
				}
	
				//String customerId = user.getIdentity() == null ? null : user.getIdentity().getErpCustomerPK();
				
				//PromotionI promotion = FDPromotionFactory.getInstance().getRedemptionPromotion(redemptionCode, customerId);
				
				//Get the Promotion Id for the given redemption code before getting it from the cache.
				String promoId = FDPromotionNewManager.getRedemptionPromotionId(redemptionCode);	
				if (promoId == null) {
					Object[] params = new Object[] { redemptionCode };
					actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
					return true;
				}
				//Get the redemption Promotion from the cache.	
				Promotion promotion = (Promotion)PromotionFactory.getInstance().getRedemptionPromotion(promoId);
				if (promotion == null) {//This check is required for incomplete promotions in DB.
					Object[] params = new Object[] { redemptionCode };
					actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
					return true;
				}
				user.setRedeemedPromotion(promotion);
				//Get the header discount if any applied to the cart before the updateUserState call.
				FDCartI cart = user.getShoppingCart();
				List prevdiscounts = new ArrayList(cart.getDiscounts());
				user.updateUserState();
				String promoCode = promotion.getPromotionCode();
				boolean eligible = user.getPromotionEligibility().isEligible(promoCode);
				boolean isApplied = user.getPromotionEligibility().isApplied(promoCode);
				int errorCode = user.getPromoErrorCode(promoCode);
//				request.setAttribute("isEligible", eligible);
				Object[] params = new Object[] { redemptionCode };
				if (!eligible) {
					if(user.isFraudulent()&& promotion.isFraudCheckRequired()){						
						actionResult.addError(true,"signup_warning",MessageFormat.format(
								SystemMessageList.MSG_PROMO_NOT_UNIQUE_INFO,
								new Object[] { user
										.getCustomerServiceContact() }));
						actionResult.addError(true, "redemption_error",  MessageFormat.format(SystemMessageList.MSG_REDEMPTION_NOT_ELIGIBLE,params));
						user.setRedeemedPromotion(null);
					}
					else if (promotion.getExpirationDate() != null && new Date().after(promotion.getExpirationDate())) {
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_REDEMPTION_HAS_EXPIRED,params));
						user.setRedeemedPromotion(null);
					} else if(errorCode == PromotionErrorType.ERROR_REDEMPTION_EXCEEDED.getErrorCode()){
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_CART_REDEMPTION_EXCEEDED);
						user.setRedeemedPromotion(null);
					} else {
						AddressModel shippingAddress = cart.getDeliveryAddress();
						ErpPaymentMethodI paymentMethod = cart.getPaymentMethod();
						if(shippingAddress == null || !PromotionHelper.checkPromoEligibilityForAddress(user, shippingAddress)){
							actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_DLV_ADDRESS);
							request.setAttribute("promoError", "true");
						} else if(paymentMethod == null || !PromotionHelper.checkPromoEligibilityForPayment(user, paymentMethod)){
							actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_PAYMENT);
							request.setAttribute("promoError", "true");
						}else if (!isApplied) {
							actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
						} else { 
							actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_ALREADY_USED);
						}
					} 
				} else if (!isApplied) {
					request.setAttribute("isEligible", eligible);
					if(user.isFraudulent()&& promotion.isFraudCheckRequired()){
						user.setRedeemedPromotion(null);
						actionResult.addError(true,"signup_warning",MessageFormat.format(
								SystemMessageList.MSG_PROMO_NOT_UNIQUE_INFO,
								new Object[] { user
										.getCustomerServiceContact() }));
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_REDEMPTION_NOT_ELIGIBLE,params));
					}else if (user.getShoppingCart().getSubTotal() < promotion.getMinSubtotal()) {
						Object[] params1 = new Object[] { new Double(promotion.getMinSubtotal())};
						actionResult.addError(
							true,
							"redemption_error",
							MessageFormat.format(SystemMessageList.MSG_REDEMPTION_MIN_NOT_MET, params1));
	
					} else if (promotion.isLineItemDiscount()) {
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NO_ELIGIBLE_CARTLINES);
					} else if (promotion.isSampleItem()) {
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_PRODUCT_UNAVAILABLE);
					} else{
						FDReservation reservation = cart.getDeliveryReservation();
							if(reservation == null || !PromotionHelper.checkPromoEligibilityForTimeslot(user, reservation.getTimeslotId()))
								actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_TIMESLOT);
					}
				} else {
					/*
					 * The redemption promotion is applied. Check if there is any previously
					 * applied header discount-the automatice ones. If yes then throw an error
					 * message to the user.
					 */
					if(prevdiscounts.size() > 0 && promotion.isHeaderDiscount()){
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_OVERRIDE_AUTOMATIC);
						request.setAttribute("redeem_override_msg", SystemMessageList.MSG_REDEMPTION_OVERRIDE_AUTOMATIC);
					}
					request.setAttribute("isEligible", eligible);
										
					
				}
				session.setAttribute(SessionName.USER, user);
			}catch(FDResourceException fre){
				throw new JspException(fre);
			}
		}
		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
