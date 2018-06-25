package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionApplicatorI;
import com.freshdirect.fdstore.promotion.PromotionErrorType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.WaiveChargeApplicator;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class RedemptionCodeControllerTag extends AbstractControllerTag {
	
	private final int GC_RETRY_COUNT = 4;
	private final int GC_RETRY_WARNING_COUNT = 3;
	private boolean dlvPassCart;

	public boolean isDlvPassCart() {
		return dlvPassCart;
	}

	public void setDlvPassCart(boolean dlvPassCart) {
		this.dlvPassCart = dlvPassCart;
	}

	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String action = request.getParameter("action");
		try {
			if ("removeCode".equalsIgnoreCase(action)) {
				HttpSession session = (HttpSession) pageContext.getSession();
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				
				PromotionI promo = user.getRedeemedPromotion();
				if(promo != null){
					//MNT - 144 Bug fix.
					//APPDEV-2850 - applicator are now combinable and come as a list					
					for (Iterator<PromotionApplicatorI> i = ((Promotion)promo).getApplicatorList().iterator(); i.hasNext();) {
						PromotionApplicatorI _applicator = i.next();
						if (_applicator instanceof WaiveChargeApplicator) {
							WaiveChargeApplicator waiveChargeApplicator = (WaiveChargeApplicator)_applicator;
							if(waiveChargeApplicator.getChargeType() == EnumChargeType.DELIVERY){
								/*
								 * Then its a delivery promotion. So reset the isDlvPromoApplied flag
								 * since the redemption code is removed.
								 */
								FDCartModel cart = UserUtil.getCart(user, "", isDlvPassCart());
								cart.setDlvPromotionApplied(false);
							}
						}
					}
					
					/*
					PromotionApplicatorI applicator = ((Promotion)promo).getApplicator();
					if(applicator instanceof WaiveChargeApplicator){
						WaiveChargeApplicator waiveChargeApplicator = (WaiveChargeApplicator)applicator;
						if(waiveChargeApplicator.getChargeType() == EnumChargeType.DELIVERY){
							/*
							 * Then its a delivery promotion. So reset the isDlvPromoApplied flag
							 * since the redemption code is removed.
							 */
							/*user.getShoppingCart().setDlvPromotionApplied(false);
						}
					}
			*/
					
					user.setRedeemedPromotion(null);
					
					user.updateUserState();
					user.setCouponEvaluationRequired(true);
					session.setAttribute(SessionName.USER, user);
				}
				if(session.getAttribute(SessionName.APC_PROMO) != null) {
					session.removeAttribute(SessionName.APC_PROMO);
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
					giftCards.setSelected(certNum, Boolean.valueOf(value).booleanValue());
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
							.getActionInfo(pageContext.getSession()), model.getGiftCardModel(), FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user));
					//Remove it from user cache.
					giftCards.remove(certNum);
				}
		} else {
			/*APPDEV-2024*/		
			HttpSession session = (HttpSession) pageContext.getSession();
			String apcpromo = null;
			if(session.getAttribute(SessionName.APC_PROMO) != null) {			
				apcpromo = (String) session.getAttribute(SessionName.APC_PROMO);
			}
			if(apcpromo != null) {
				applyApcPromo(request, actionResult, apcpromo);
			}
		}
		}catch(FDResourceException fre){
			throw new JspException(fre);
		}
		return true;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		/*APPDEV-2024*/		
		HttpSession session = (HttpSession) pageContext.getSession();
		FDActionInfo info = AccountActivityUtil.getActionInfo(session);
		String apcpromo = null;
		if(session.getAttribute(SessionName.APC_PROMO) != null) {			
			apcpromo = (String) session.getAttribute(SessionName.APC_PROMO);
		}
		
		String redemptionCode = NVL.apply(request.getParameter("redemptionCode"), "").trim();
		Object[] params = new Object[] { redemptionCode };
				
		if ("redeemCode".equals(this.getActionName()) || apcpromo != null) {
			try{				
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				request.setAttribute("isEligible", false);
				
				if ("".equals(redemptionCode)) {
					if(apcpromo != null) {
						return applyApcPromo(request, actionResult, apcpromo);
					} else {
						actionResult.addError(true, "redemption_error", "Promotion Code is required");
						return true;
					}
				}
	
				//String customerId = user.getIdentity() == null ? null : user.getIdentity().getErpCustomerPK();
				
				//PromotionI promotion = FDPromotionFactory.getInstance().getRedemptionPromotion(redemptionCode, customerId);
				
				//Get the Promotion Id for the given redemption code before getting it from the cache.
				String promoId = FDPromotionNewManager.getRedemptionPromotionId(redemptionCode);	
				if (promoId == null) {
					//Object[] params = new Object[] { redemptionCode };
					actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
					return true;
				}
				//Get the redemption Promotion from the cache.	
				Promotion promotion = (Promotion)PromotionFactory.getInstance().getRedemptionPromotion(promoId);
				if (promotion == null) {//This check is required for incomplete promotions in DB.
					//Object[] params = new Object[] { redemptionCode };
					actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
					return true;
				}
				user.setRedeemedPromotion(promotion);
				//Get the header discount if any applied to the cart before the updateUserState call.
				FDCartI cart = UserUtil.getCart(user, "", isDlvPassCart());
				List prevdiscounts = new ArrayList(cart.getDiscounts());
				user.updateUserState();
				String promoCode = promotion.getPromotionCode();
				boolean eligible = user.getPromotionEligibility().isEligible(promoCode);
				boolean isApplied = user.getPromotionEligibility().isApplied(promoCode);
				int errorCode = user.getPromoErrorCode(promoCode);
//				request.setAttribute("isEligible", eligible);
				//Object[] params = new Object[] { redemptionCode };
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
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_CART_REDEMPTION_EXCEEDED,params));
						user.setRedeemedPromotion(null);
					} else if(errorCode == PromotionErrorType.ERROR_USAGE_LIMIT_ONE_EXCEEDED.getErrorCode()){
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_CART_USAGE_LIMIT_ONE_EXCEEDED,params));
						user.setRedeemedPromotion(null);						
					} else if(errorCode == PromotionErrorType.ERROR_USAGE_LIMIT_MORE_EXCEEDED.getErrorCode()){
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_CART_USAGE_LIMIT_MORE_EXCEEDED,params));
						user.setRedeemedPromotion(null);
					} else {
						if(errorCode == PromotionErrorType.NO_ELIGIBLE_ADDRESS_SELECTED.getErrorCode() 
								|| errorCode == PromotionErrorType.NO_DELIVERY_ADDRESS_SELECTED.getErrorCode()){
							actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_DLV_ADDRESS);
							request.setAttribute("promoError", "true");
						} else if(errorCode == PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode() 
								|| errorCode == PromotionErrorType.NO_PAYMENT_METHOD_SELECTED.getErrorCode()){
							actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_PAYMENT);
							request.setAttribute("promoError", "true");
						}else if (!isApplied) {
							actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
							user.setRedeemedPromotion(null);
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
					}else if (UserUtil.getCart(user, "", isDlvPassCart()).getSubTotal() < promotion.getMinSubtotal()) {
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
						if(errorCode == PromotionErrorType.NO_ELIGIBLE_TIMESLOT_SELECTED.getErrorCode()) 
								actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_TIMESLOT);
					}
				} else {
					/*
					 * The redemption promotion is applied. Check if there is any previously
					 * applied header discount-the automatice ones. If yes then throw an error
					 * message to the user.
					 */
					if(prevdiscounts.size() > 0 && promotion.isHeaderDiscount()&& !promotion.isCombineOffer()){
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_OVERRIDE_AUTOMATIC);
						request.setAttribute("redeem_override_msg", SystemMessageList.MSG_REDEMPTION_OVERRIDE_AUTOMATIC);
					}
					user.setCouponEvaluationRequired(true);
					request.setAttribute("isEligible", eligible);
										
					
				}
				session.setAttribute(SessionName.USER, user);
				
				}catch(FDResourceException fre){
				throw new JspException(fre);
			}
		} else if("applyCode".equals(this.getActionName())){
			try {
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

				if (!user.getFDCustomer().getProfile().allowApplyGC()) {
					actionResult.addError(true, "account_locked", SystemMessageList.ACCOUNT_LOCKED_FOR_GC);
					return true;
				}
				String givexNum = NVL.apply(request.getParameter("redemptionCode"), "").trim();
				if (givexNum != null && givexNum.trim().length() > 0) {
					try {
						ErpGiftCardModel gcModel = FDCustomerManager.applyGiftCard(user.getIdentity(), givexNum.trim(), info);
						if (gcModel.getBalance() == 0) {
							actionResult.addError(true, "card_zero_balance", SystemMessageList.APPLY_GC_WITH_ZERO_BALANCE);
						}
						user.getGiftCardList().addGiftCard(new FDGiftCardModel(gcModel));
						user.resetGCRetryCount();
					} catch (ServiceUnavailableException se) {
						actionResult.addError(true, "service_unavailable", SystemMessageList.MSG_GC_SERVICE_UNAVAILABLE);
					} catch (InvalidCardException e) {
						user.incrementGCRetryCount();
						if (user.getGCRetryCount() >= GC_RETRY_COUNT) {
							user.getFDCustomer().getProfile().setAttribute("allow_apply_gc", "false");
							FDCustomerManager.setProfileAttribute(user.getIdentity(), "allow_apply_gc", "false", info);
							actionResult.addError(true, "account_locked", SystemMessageList.ACCOUNT_LOCKED_FOR_GC);
							return true;
						}
						if (user.getGCRetryCount() >= GC_RETRY_WARNING_COUNT) {
							actionResult.addError(true, "apply_gc_warning", SystemMessageList.APPLY_GC_WARNING);
						}
						actionResult.addError(new ActionError("redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE_OR_GIFTCARD, params)));
					} catch (CardOnHoldException e) {
						actionResult.addError(true, "card_on_hold", SystemMessageList.MSG_GC_ON_HOLD);
					} catch (CardInUseException ce) {
						if (ce.getCardOwner().equals(user.getUserId())) {
							actionResult.addError(true, "card_in_use",  SystemMessageList.MSG_GC_ALREADY_ADDED);
							request.setAttribute("giftCardError", "true");
						} else {
							actionResult.addError(true, "card_in_use", SystemMessageList.MSG_GC_IN_USE);
							request.setAttribute("giftCardError", "true");
						}
					}
				} else {
					actionResult.addError(new ActionError("redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE_OR_GIFTCARD,params)));
					request.setAttribute("giftCardError", "true");
				}
				if (actionResult.isSuccess()) {
					request.setAttribute("giftCardError", "false");
				}
			}catch(FDResourceException fre){
				if(fre.getNestedException()!=null && fre.getNestedException().getMessage()!=null && fre.getNestedException().getMessage().contains("com.freshdirect.payment.GivexException")){
					actionResult.addError(true, "service_unavailable", SystemMessageList.MSG_GC_SERVICE_UNAVAILABLE);
				} else {
				throw new JspException(fre);
				}
			}
		}
		return true;
	}
	
	protected boolean applyApcPromo(HttpServletRequest request, ActionResult actionResult, String tsaPromoCode) throws JspException {
			try{				
				HttpSession session = (HttpSession) pageContext.getSession();
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				request.setAttribute("isEligible", false);
				
				String redemptionCode = FDPromotionNewManager.getRedemptionCode(tsaPromoCode);
	
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
				FDCartI cart = UserUtil.getCart(user, "", isDlvPassCart());
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
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_CART_REDEMPTION_EXCEEDED,params));
						user.setRedeemedPromotion(null);
					} else if(errorCode == PromotionErrorType.ERROR_USAGE_LIMIT_ONE_EXCEEDED.getErrorCode()){
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_CART_USAGE_LIMIT_ONE_EXCEEDED,params));
						user.setRedeemedPromotion(null);						
					} else if(errorCode == PromotionErrorType.ERROR_USAGE_LIMIT_MORE_EXCEEDED.getErrorCode()){
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_CART_USAGE_LIMIT_MORE_EXCEEDED,params));
						user.setRedeemedPromotion(null);
					} else {
						if(errorCode == PromotionErrorType.NO_ELIGIBLE_ADDRESS_SELECTED.getErrorCode() 
								|| errorCode == PromotionErrorType.NO_DELIVERY_ADDRESS_SELECTED.getErrorCode()){
							actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_DLV_ADDRESS);
							request.setAttribute("promoError", "true");
						} else if(errorCode == PromotionErrorType.NO_ELIGIBLE_PAYMENT_SELECTED.getErrorCode() 
								|| errorCode == PromotionErrorType.NO_PAYMENT_METHOD_SELECTED.getErrorCode()){
							actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_PAYMENT);
							request.setAttribute("promoError", "true");
						}else if (!isApplied) {
							actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
							user.setRedeemedPromotion(null);
						} else { 
							actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_ALREADY_USED);
						}
					} 
					session.removeAttribute(SessionName.APC_PROMO);
				} else if (!isApplied) {
					request.setAttribute("isEligible", eligible);
					if(user.isFraudulent()&& promotion.isFraudCheckRequired()){
						user.setRedeemedPromotion(null);
						actionResult.addError(true,"signup_warning",MessageFormat.format(
								SystemMessageList.MSG_PROMO_NOT_UNIQUE_INFO,
								new Object[] { user
										.getCustomerServiceContact() }));
						actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_REDEMPTION_NOT_ELIGIBLE,params));
					}else if (UserUtil.getCart(user, "", isDlvPassCart()).getSubTotal() < promotion.getMinSubtotal()) {
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
						if(errorCode == PromotionErrorType.NO_ELIGIBLE_TIMESLOT_SELECTED.getErrorCode()) 
								actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NOTE_TIMESLOT);
					}
				} else {
					/*
					 * The redemption promotion is applied. Check if there is any previously
					 * applied header discount-the automatice ones. If yes then throw an error
					 * message to the user.
					 */
					if(prevdiscounts.size() > 0 && promotion.isHeaderDiscount()&& !promotion.isCombineOffer()) {
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_OVERRIDE_AUTOMATIC);
						request.setAttribute("redeem_override_msg", SystemMessageList.MSG_REDEMPTION_OVERRIDE_AUTOMATIC);
					} else if(prevdiscounts.size() > 0 && promotion.isCombineOffer()) {
						actionResult.addError(true, "redemption_error", "Your promotions have been added to your order");
						request.setAttribute("redeem_override_msg", "Your promotions have been added to your order");
					} 
					user.setCouponEvaluationRequired(true);
					request.setAttribute("isEligible", eligible);
										
					
				}
				session.setAttribute(SessionName.USER, user);
			}catch(FDResourceException fre){
				throw new JspException(fre);
			}
		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}
