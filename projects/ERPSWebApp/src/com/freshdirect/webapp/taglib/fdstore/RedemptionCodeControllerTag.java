package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import sun.security.x509.CertAndKeyGen;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.giftcard.FDGiftCardI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.fdstore.giftcard.FDGiftCardModel;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionApplicatorI;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.WaiveChargeApplicator;
import com.freshdirect.fdstore.promotion.management.FDPromotionManager;
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
				if(certNum != null && certNum.length() > 0){
					
					FDGiftCardModel model = (FDGiftCardModel)giftCards.getGiftCard(certNum);
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
	
				String redemptionCode = NVL.apply(request.getParameter("redemptionCode"), "").trim();
				if ("".equals(redemptionCode)) {
					actionResult.addError(true, "redemption_error", "Promotion Code is required");
					return true;
				}
	
				//String customerId = user.getIdentity() == null ? null : user.getIdentity().getErpCustomerPK();
				
				//PromotionI promotion = FDPromotionFactory.getInstance().getRedemptionPromotion(redemptionCode, customerId);
				
				//Get the Promotion Id for the given redemption code before getting it from the cache.
				String promoId = FDPromotionManager.getRedemptionPromotionId(redemptionCode);	
				if (promoId == null) {
					Object[] params = new Object[] { redemptionCode };
					actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
					return true;
				}
				//Get the redemption Promotion from the cache.	
				PromotionI promotion = PromotionFactory.getInstance().getRedemptionPromotion(promoId);
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
				boolean eligible = user.getPromotionEligibility().isEligible(promotion.getPromotionCode());
				boolean isApplied = user.getPromotionEligibility().isApplied(promotion.getPromotionCode());
				if (!eligible) {
					user.setRedeemedPromotion(null);
					if (promotion.getExpirationDate() != null && new Date().after(promotion.getExpirationDate())) {
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_HAS_EXPIRED);
					} else if (!isApplied) {
						Object[] params = new Object[] { redemptionCode };
							actionResult.addError(true, "redemption_error", MessageFormat.format(SystemMessageList.MSG_INVALID_CODE, params));
					} else {
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_ALREADY_USED);
					}
	
				} else if (!isApplied) {
	
					if (user.getShoppingCart().getSubTotal() < promotion.getMinSubtotal()) {
						Object[] params = new Object[] { new Double(promotion.getMinSubtotal())};
						actionResult.addError(
							true,
							"redemption_error",
							MessageFormat.format(SystemMessageList.MSG_REDEMPTION_MIN_NOT_MET, params));
	
					} else if (promotion.isCategoryDiscount()) {
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_NO_ELIGIBLE_CARTLINES);
					} else if (promotion.isSampleItem()) {
						actionResult.addError(true, "redemption_error", SystemMessageList.MSG_REDEMPTION_PRODUCT_UNAVAILABLE);

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
