package com.freshdirect.webapp.ajax.expresscheckout.cart.service;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.PromotionErrorType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.SignupDiscountRule;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartSubTotalFieldData;
import com.freshdirect.webapp.ajax.expresscheckout.fdcoupon.service.FDCouponService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FDCartModelService;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.JspMethods;

public class CartSubTotalBoxService {


	private static final CartSubTotalBoxService INSTANCE = new CartSubTotalBoxService();

	private static final String SIGNUP_PROMOTION_INFO_ID = "signupPromotionInfo";
	private static final String YOU_SAVED = "youSaved";
	private static final String YOU_SAVED_TEXT = "You've Saved";
	private static final String REDEMPTION_PROMO_ID = "redemptionpromo";
	private static final String REDEMPTION_ERROR_OTHER_ID = "redemptionErrorOther";
	public static final String REMOVE_GIFT_CARD_URL_PARAMETER = "?action=removeGiftCard";
	private static final String ORDER_TOTAL_ID = "ssOrderTotal";
	private static final String ORDER_TOTAL_TEXT = "Order Total";
	private static final String ORDER_ESTIMATED_TOTAL_TEXT = "Estimated Total";
	private static final String CREDITS_TEXT = "Credit Applied";
	private static final String CREDITS_ID = "credits";
	private static final String FREE_FOOD_TEXT = "Free Food";
	private static final String FREE_FOOD_ID = "freefood";
	private static final String PROMO_CODE_KEY = "promoCode";
	public static final String REMOVE_URL_KEY = "removeUrl";
	public static final String MARK_KEY = "mark";
	public static final String DELIVERY_PREMIUM_HAMPTONS_WAIVED_NAME = "Delivery Premium (Hamptons) (waived)";
	public static final String DELIVERY_PREMIUM_HAMPTONS_NAME = "Delivery Premium (Hamptons)";
	public static final String TAXABLE_ITEM_MARK = "T";
	private static final String ESTIMATED_PRICE_MARK = "*";
	private static final String SUBTOTAL_NAME = "Order Subtotal";
	private static final String SUBTOTAL_ID = "subtotal";
	private static final String FUEL_SURCHARGE_NAME = "Fuel Surcharge";
	private static final String FUEL_SURCHARGE_ID = "fuelsurcharge";
	private static final String FUEL_SURCHARGE_WAIVED_NAME = "Fuel Surcharge (waived)";
	private static final String TOTAL_TAX_WAIVED_NAME = "Total Tax (waived)";
	private static final String DELIVERY_FEE_NAME = "Delivery Fee";
	private static final String DELIVERY_FEE_ID = "deliveryfee";
	private static final String DEPOSIT_NAME = "State Bottle Deposit";
	private static final String DEPOSIT_ID = "statebottledeposit";
	private static final String TOTAL_TAX_NAME = "Total Tax";
	private static final String TOTALTAX_ID = "totaltax";
	public static final String ZERO_POINT_ZERO_ZERO_VALUE = "$0.00";
	private static final String PROMOTION_ERROR_KEY = "promotionError";
	private static final String REDEMPTION_ERROR_KEY = "redemptionError";

	private CartSubTotalBoxService() {
	}

	public static CartSubTotalBoxService defaultService() {
		return INSTANCE;
	}

	public void populateSubTotalToBox(List<CartSubTotalFieldData> subTotalBox, double subTotalValue) {
		CartSubTotalFieldData data = new CartSubTotalFieldData();
		data.setId(SUBTOTAL_ID);
		data.setText(SUBTOTAL_NAME);
		data.setValue(JspMethods.formatPrice(subTotalValue));
		data.getOther().put(MARK_KEY, ESTIMATED_PRICE_MARK);
		subTotalBox.add(data);
	}

	public void populateTaxToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
		CartSubTotalFieldData data = new CartSubTotalFieldData();
		data.setId(TOTALTAX_ID);
		if (FDCartModelService.defaultService().isEbtPaymentForCart(cart)) {
			data.setText(TOTAL_TAX_WAIVED_NAME);
			data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
			subTotalBox.add(data);
		} else {
			double taxValue = cart.getTaxValue();
			if (0 < taxValue) {
				data.setText(TOTAL_TAX_NAME);
				data.setValue(JspMethods.formatPrice(taxValue));
				subTotalBox.add(data);
			}
		}
	}

	public void populateDepositValueToBox(List<CartSubTotalFieldData> subTotalBox, double depositValue) {
		if (0 < depositValue) {
			CartSubTotalFieldData data = new CartSubTotalFieldData();
			data.setId(DEPOSIT_ID);
			data.setText(DEPOSIT_NAME);
			data.setValue(JspMethods.formatPrice(depositValue));
			subTotalBox.add(data);
		}
	}

	public void populateDeliveryFeeToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart, FDUserI user, CartData cartData) {
		boolean isDlvPassApplied = false;
		if (cart instanceof FDCartModel) {
			isDlvPassApplied = ((FDCartModel) cart).isDlvPassApplied();
		} else if (cart instanceof FDOrderAdapter) {
			isDlvPassApplied = ((FDOrderAdapter) cart).isDlvPassApplied();
		}
		

		CartSubTotalFieldData data = new CartSubTotalFieldData();
		boolean deliveryPassPopupNeeded = false;
		data.setId(DELIVERY_FEE_ID);
		data.setText(DELIVERY_FEE_NAME);
		final String deliveryPassValue;
		if (isDlvPassApplied) {
			deliveryPassValue = DeliveryPassUtil.getDlvPassAppliedMessage(user);
		} else {
			deliveryPassValue = JspMethods.formatPrice(cart.getChargeAmount(EnumChargeType.DELIVERY));
			deliveryPassPopupNeeded = true;
		}
		cartData.setDeliveryCharge(deliveryPassValue);
		data.setValue(deliveryPassValue);
		data.getOther().put("deliveryPassPopupNeeded", deliveryPassPopupNeeded);
		subTotalBox.add(data);
	}

	public void populateFuelSurchargeToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
		CartSubTotalFieldData data = new CartSubTotalFieldData();
		data.setId(FUEL_SURCHARGE_ID);
		if (FDCartModelService.defaultService().isEbtPaymentForCart(cart)) {
			data.setText(FUEL_SURCHARGE_WAIVED_NAME);
			data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
			subTotalBox.add(data);
		} else {
			if (0 < cart.getMiscellaneousCharge()) {
				if (cart.isMiscellaneousChargeWaived()) {
					data.setText(FUEL_SURCHARGE_WAIVED_NAME);
				} else {
					data.setText(FUEL_SURCHARGE_NAME);
				}
				if (cart.isMiscellaneousChargeWaived()) {
					data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
				} else {
					data.setValue(JspMethods.formatPrice(cart.getMiscellaneousCharge()));
					if (cart.isMiscellaneousChargeTaxable()) {
						data.getOther().put(MARK_KEY, TAXABLE_ITEM_MARK);
					}
				}
				subTotalBox.add(data);
			}
		}
	}

	public void populateDiscountsToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart, CartData cartData, FDUserI user) {
		PromotionI redemptionPromo = user.getRedeemedPromotion();
		String promoCode = redemptionPromo != null ? redemptionPromo.getPromotionCode() : "";
		boolean isRedemptionApplied = (redemptionPromo != null && user.getPromotionEligibility().isApplied(redemptionPromo.getPromotionCode()));
		boolean removeLinkDisplayed = false;

		List<ErpDiscountLineModel> discounts = cart.getDiscounts();
		for (Iterator<ErpDiscountLineModel> iter = discounts.iterator(); iter.hasNext();) {
			ErpDiscountLineModel discountLine = iter.next();
			Discount discount = discountLine.getDiscount();
			if (user.isEligibleForSignupPromotion() && cart.getTotalDiscountValue() >= 0.01) {
				CartSubTotalFieldData freeFood = new CartSubTotalFieldData();
				freeFood.setId(FREE_FOOD_ID);
				freeFood.setText(FREE_FOOD_TEXT);
				freeFood.setValue(JspMethods.formatPriceWithNegativeSign(discount.getAmount()));
				subTotalBox.add(freeFood);
			} else if (isRedemptionApplied && promoCode.equalsIgnoreCase(discount.getPromotionCode())) {
				removeLinkDisplayed = true;
				CartSubTotalFieldData redemptionPromoData = new CartSubTotalFieldData();
				redemptionPromoData.setId(REDEMPTION_PROMO_ID);
				redemptionPromoData.setText(redemptionPromo.getDescription());
				redemptionPromoData.setValue(JspMethods.formatPriceWithNegativeSign(discount.getAmount()));
				Map<String, Object> other = redemptionPromoData.getOther();
				other.put(PROMO_CODE_KEY, promoCode);
				other.put("removeCode", "Remove");
				subTotalBox.add(redemptionPromoData);
				cartData.setPromotionApplied(true);
			} else { // Its a automatic header discount
				PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
				CartSubTotalFieldData automaticHeaderDiscount = new CartSubTotalFieldData();
				automaticHeaderDiscount.setId("automaticheaderdiscount");
				Map<String, Object> other = automaticHeaderDiscount.getOther();
				other.put(PROMO_CODE_KEY, promotion.getPromotionCode());
				automaticHeaderDiscount.setText(promotion.getDescription());
				automaticHeaderDiscount.setValue(JspMethods.formatPriceWithNegativeSign(discount.getAmount()));
				subTotalBox.add(automaticHeaderDiscount);
			}
		}

		if (isRedemptionApplied) {
			if (redemptionPromo.isSampleItem()) {
				CartSubTotalFieldData sampleRedemptionPromo = new CartSubTotalFieldData();
				sampleRedemptionPromo.setId("sampleredemptionpromo");
				sampleRedemptionPromo.setText(redemptionPromo.getDescription());
				Map<String, Object> other = sampleRedemptionPromo.getOther();
				other.put(PROMO_CODE_KEY, promoCode);
				sampleRedemptionPromo.setValue("FREE!");
				other.put("removeCode", "Remove");
				subTotalBox.add(sampleRedemptionPromo);
				cartData.setPromotionApplied(true);
			} else if (redemptionPromo.isWaiveCharge() && !removeLinkDisplayed) {
				CartSubTotalFieldData waivedRedemptionPromo = new CartSubTotalFieldData();
				waivedRedemptionPromo.setId("waivedRedemptionPromo");
				waivedRedemptionPromo.setText(redemptionPromo.getDescription());
				Map<String, Object> other = waivedRedemptionPromo.getOther();
				other.put(PROMO_CODE_KEY, promoCode);
				waivedRedemptionPromo.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
				other.put("removeCode", "Remove");
				subTotalBox.add(waivedRedemptionPromo);
				cartData.setPromotionApplied(true);
			} else if (redemptionPromo.isExtendDeliveryPass()) {
				CartSubTotalFieldData extendedDeliveryPassRedemptionPromo = new CartSubTotalFieldData();
				extendedDeliveryPassRedemptionPromo.setId("extendedDeliveryPassRedemptionPromo");
				extendedDeliveryPassRedemptionPromo.setText(redemptionPromo.getDescription());
				Map<String, Object> other = extendedDeliveryPassRedemptionPromo.getOther();
				other.put(PROMO_CODE_KEY, promoCode);
				extendedDeliveryPassRedemptionPromo.setValue("Pass extension");
				other.put("removeCode", "Remove");
				subTotalBox.add(extendedDeliveryPassRedemptionPromo);
				cartData.setPromotionApplied(true);
			}
		}
	}

	public void populateCustomerCreditsToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
		if (0 < cart.getCustomerCreditsValue()) {
			CartSubTotalFieldData data = new CartSubTotalFieldData();
			data.setId(CREDITS_ID);
			data.setText(CREDITS_TEXT);
			data.setValue(JspMethods.formatPriceWithNegativeSign(cart.getCustomerCreditsValue()));
			subTotalBox.add(data);
		}
	}

	public void populateOrderTotalToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
		CartSubTotalFieldData data = new CartSubTotalFieldData();
		data.setId(ORDER_TOTAL_ID);
		if (cart.isEstimatedPrice()) {
			data.setText(ORDER_ESTIMATED_TOTAL_TEXT);
			Map<String, Object> other = data.getOther();
			other.put(MARK_KEY, ESTIMATED_PRICE_MARK);
		} else {
			data.setText(ORDER_TOTAL_TEXT);
		}
		data.setValue(JspMethods.formatPrice(cart.getTotal()));
		subTotalBox.add(data);
	}

	public void populateGiftBalanceToBox(List<CartSubTotalFieldData> subTotalBox, FDUserI user) {
		if (user.getGiftcardBalance() > 0) {
			CartSubTotalFieldData data = new CartSubTotalFieldData();
			data.setId("giftcardbalance");
			data.setText("Gift Card Balance");
			data.setValue(JspMethods.formatPriceWithNegativeSign(user.getGiftcardBalance()));
			subTotalBox.add(data);
		}
	}

	public void populateRedeemOverrideMessageToBox(List<CartSubTotalFieldData> subTotalBox, FDUserI user, String redeemOverrideMessage) {
		if (isRedemptionApplied(user) && getRedemptionPromo(user).isHeaderDiscount()) {
			if (redeemOverrideMessage != null) {
				CartSubTotalFieldData data = new CartSubTotalFieldData();
				data.setId("redeem_override_msg");
				data.setText(redeemOverrideMessage);
				data.getOther().put("error", true);
				subTotalBox.add(data);
			}
		}
	}

	private PromotionI getRedemptionPromo(FDUserI user) {
		return user.getRedeemedPromotion();
	}

	private boolean isRedemptionApplied(FDUserI user) {
		PromotionI redemptionPromo = getRedemptionPromo(user);
		return redemptionPromo != null && user.getPromotionEligibility().isApplied(redemptionPromo.getPromotionCode());
	}

	public void populateRedemptionErrorToBox(List<CartSubTotalFieldData> subTotalBox, FDUserI user, String promoError, ActionError redemptionError, Boolean isEligible) {
		String errorMsg1 = "";
		PromotionI redemptionPromo = getRedemptionPromo(user);
		double maxPromotion = getMaxPromotion(user);
		String promoCode = getPromoCode(redemptionPromo);
		if ((redemptionPromo == null && maxPromotion <= 0.0) || (redemptionPromo != null && !user.getPromotionEligibility().isEligible(promoCode) && promoError == null)) {
			if (redemptionError != null) {
				errorMsg1 = redemptionError.getDescription();
				CartSubTotalFieldData redemptionErrorField = new CartSubTotalFieldData();
				redemptionErrorField.setId("redemptionerror");
				redemptionErrorField.setText(errorMsg1);
				redemptionErrorField.getOther().put("error", true);
				subTotalBox.add(redemptionErrorField);
			}
		}
	}

	private String getPromoCode(PromotionI redemptionPromo) {
		return redemptionPromo != null ? redemptionPromo.getPromotionCode() : "";
	}

	@SuppressWarnings("deprecation")
	private double getMaxPromotion(FDUserI user) {
		return user.getMaxSignupPromotion();
	}

	public void populateAdditionalRedemptionErrorToBox(List<CartSubTotalFieldData> subTotalBox, FDUserI user, FDCartI cart, String requestURI, ActionError redemptionError) {
		PromotionI redemptionPromo = getRedemptionPromo(user);
		String promoCode = getPromoCode(redemptionPromo);
		if (redemptionPromo != null
				&& user.getPromotionEligibility().isEligible(promoCode)
				&& (!user.getPromotionEligibility().isApplied(promoCode)
						|| (redemptionPromo.getHeaderDiscountRules() != null && !(redemptionPromo.getHeaderDiscountTotal() == 0) && cart.getDiscountValue(promoCode) == 0) || (redemptionPromo
						.isLineItemDiscount() && redemptionPromo.getLineItemDiscountPercentage() > 0 && !cart.isDiscountInCart(promoCode)))) {
			// TODO: some of these error handlers are already written in
			// RedemptionCodeControllerTag
			// so error display duplication may occur!
			double redemptionAmt = 0;
			String warningMessage = "";
			if (cart.getSubTotal() < redemptionPromo.getMinSubtotal()) {
				redemptionAmt = redemptionPromo.getHeaderDiscountTotal();
				warningMessage = MessageFormat.format(SystemMessageList.MSG_REDEMPTION_MIN_NOT_MET, new Object[] { new Double(redemptionPromo.getMinSubtotal()) });
			} else if (redemptionPromo.isLineItemDiscount()) {
				int errorCode = user.getPromoErrorCode(promoCode);
				if (errorCode == PromotionErrorType.NO_ELIGIBLE_CART_LINES.getErrorCode())
					warningMessage = SystemMessageList.MSG_REDEMPTION_NO_ELIGIBLE_CARTLINES;
			} else if (redemptionPromo.isSampleItem()) {
				warningMessage = SystemMessageList.MSG_REDEMPTION_PRODUCT_UNAVAILABLE;
			} else if (redemptionPromo.getOfferType().equals(EnumOfferType.WINDOW_STEERING)) {
				warningMessage = SystemMessageList.MSG_REDEMPTION_NO_ELIGIBLE_TIMESLOT;
			}

			populateRedemptionPromoToBox(subTotalBox, requestURI, redemptionPromo, redemptionAmt);

			// MNT-113 Quick fix: to avoid duplicated error displays we check
			// whether the generated warning message is displayed already by
			// ErrorHandlerTag
			String redemptionErrorMessage = (redemptionError != null ? redemptionError.getDescription() : null);

			// display if message is not empty and does not match the previously
			// displayed error message (if exists)
			populateRedemptionErrorOtherToBox(subTotalBox, warningMessage, redemptionErrorMessage);
		}
	}

	private void populateRedemptionPromoToBox(List<CartSubTotalFieldData> subTotalBox, String requestURI, PromotionI redemptionPromo, double redemptionAmt) {
		CartSubTotalFieldData data = new CartSubTotalFieldData();
		data.setId(REDEMPTION_PROMO_ID);
		data.setText(redemptionPromo.getDescription());
		if (redemptionPromo.isSampleItem()) {
			data.setValue("FREE!");
		} else {
			data.setValue(JspMethods.formatPriceWithNegativeSign(redemptionAmt));
		}
		data.getOther().put("removeCode", "Remove");
		subTotalBox.add(data);
	}

	private void populateRedemptionErrorOtherToBox(List<CartSubTotalFieldData> subTotalBox, String warningMessage, String redemptionErrorMessage) {
		CartSubTotalFieldData data = new CartSubTotalFieldData();
		data.setId(REDEMPTION_ERROR_OTHER_ID);
		data.getOther().put("error", true);
		if (null != redemptionErrorMessage && !"".equals(redemptionErrorMessage)) {
			data.setText(redemptionErrorMessage);
		} else if (!"".equalsIgnoreCase(warningMessage) && !warningMessage.equalsIgnoreCase(redemptionErrorMessage)) {
			data.setText(warningMessage);
		}

		if (data.getText() != null && !data.getText().isEmpty()) {
			subTotalBox.add(data);
		}
	}

	public void populateRedemptionFailureDueToAddressAndPaymentToBox(List<CartSubTotalFieldData> subTotalBox, FDUserI user, String promoError, String requestURI, ActionError redemptionError) {
		PromotionI redemptionPromo = getRedemptionPromo(user);
		String promoCode = getPromoCode(redemptionPromo);
		if (redemptionPromo != null && !user.getPromotionEligibility().isEligible(promoCode) && promoError != null) {
			CartSubTotalFieldData data = new CartSubTotalFieldData();
			data.setId("redemptionfailure");
			data.setText(redemptionPromo.getDescription());
			if (redemptionPromo.isSampleItem()) {
				data.setValue("FREE!");
			} else {
				data.setValue(JspMethods.formatPriceWithNegativeSign(redemptionPromo.getHeaderDiscountTotal()));
			}
			Map<String, Object> other = data.getOther();
			other.put(PROMO_CODE_KEY, promoCode);
			other.put("removeCode", "Remove");
			other.put(PROMOTION_ERROR_KEY, "");
			other.put(REDEMPTION_ERROR_KEY, redemptionError.getDescription());
			subTotalBox.add(data);
		}
	}

	public void populateSavingToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
		double saving = cart.getTotalDiscountValue() + cart.getCustomerCreditsValue();
		
		for(FDCartLineI orderLine : cart.getOrderLines()){
			if(hasCartLineGroupDiscount(orderLine)){
				saving += orderLine.getGroupScaleSavings();
			}
		}

		if (0 < saving) {
			CartSubTotalFieldData data = new CartSubTotalFieldData();
			data.setId(YOU_SAVED);
			data.setText(YOU_SAVED_TEXT);
			data.setValue(JspMethods.formatPrice(saving));
			subTotalBox.add(data);
		}
	}
	
	private boolean hasCartLineGroupDiscount(FDCartLineI cartLine){
		return cartLine.getDiscount() == null && cartLine.getGroupQuantity() > 0 && cartLine.getGroupScaleSavings() > 0;
	}

	public void populateSignupPromotionInfoToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart, FDUserI user) {
		FDIdentity identity = user.getIdentity();
		if (identity == null || user.isEligibleForSignupPromotion()) {
			if (user.isEligibleForSignupPromotion()) {
				SignupDiscountRule discountRule = user.getSignupDiscountRule();
				int minSubtotal = (int) discountRule.getMinSubtotal();
				int maxAmount = (int) discountRule.getMaxAmount();
				{ // TODO: remove trash blocks
					CartSubTotalFieldData data = new CartSubTotalFieldData();
					data.setId(SIGNUP_PROMOTION_INFO_ID + "_1");
					data.setText(MessageFormat.format("Spend {0} on this order to get {1} free, fresh food.", JspMethods.formatPrice(minSubtotal), JspMethods.formatPrice(maxAmount)));
					subTotalBox.add(data);
				}
				if (cart.getSubTotal() < minSubtotal) {
					CartSubTotalFieldData data = new CartSubTotalFieldData();
					data.setId(SIGNUP_PROMOTION_INFO_ID + "_2");
					data.setText(MessageFormat.format("This order does not yet qualify for {0} offer. Subtotal must be {1} or more.", JspMethods.formatPrice(maxAmount),
							JspMethods.formatPrice(minSubtotal)));
					subTotalBox.add(data);
				}
				{
					CartSubTotalFieldData data = new CartSubTotalFieldData();
					data.setId(SIGNUP_PROMOTION_INFO_ID + "_3");
					data.setText("Applies to home delivery orders only.");
					subTotalBox.add(data);
				}
			}

			if (user.isPickupOnly()) {
				CartSubTotalFieldData data = new CartSubTotalFieldData();
				data.setId(SIGNUP_PROMOTION_INFO_ID + "_4");
				data.setText("Pickup orders are not eligible for our free food promotion.");
				data.getOther().put("error", true);
				subTotalBox.add(data);
			}
		}
	}

	public void populateCustomerCreditHistoryInfoToBox(List<CartSubTotalFieldData> subTotalBox, FDUserI user) throws FDResourceException {
		if (user != null && user.getIdentity() != null) {
			FDCustomerCreditHistoryModel customerCreditHistory = FDCustomerManager.getCreditHistory(user.getIdentity());
			if (0 < customerCreditHistory.getRemainingAmount()) {
				CartSubTotalFieldData data = new CartSubTotalFieldData();
				data.setId("customerCreditHistoryInfo");
				data.setText(MessageFormat.format("You have {0} in credits that will be applied during final check out.", JspMethods.formatPrice(customerCreditHistory.getRemainingAmount())));
				subTotalBox.add(data);
			}
		}
	}

	public void populateRecentOrderLinesToBox(List<CartSubTotalFieldData> subTotalBox, FDCartModel cart, FDUserI user, String confirmParameter) {
		if (cart.getRecentOrderLines().size() > 0 && "1".equalsIgnoreCase(confirmParameter)) {
			for (Iterator<FDCartLineI> oit = cart.getRecentOrderLines().iterator(); oit.hasNext();) {
				FDCartLineI orderLine = oit.next();
				EnumCouponContext couponContextCnT = EnumCouponContext.VIEWCART;
				FDCustomerCoupon coupon = user.getCustomerCoupon(orderLine, couponContextCnT);
				String displayQuantity = orderLine.getDisplayQuantity();
				String description = orderLine.getDescription();
				String couponContent = FDCouponService.defaultService().getContent(coupon, "fdCoupon_cartlineViewCart");
				String price = JspMethods.formatPrice(orderLine.getPrice());
				CartSubTotalFieldData data = new CartSubTotalFieldData();
				data.setId("recentOrderLines");
				Map<String, Object> other = data.getOther();
				other.put("quantity", displayQuantity);
				other.put("description", description);
				other.put("coupon", couponContent);
				other.put("price", price);
				subTotalBox.add(data);
			}
		}
	}

	public void populateRemovePromotionCode(List<CartSubTotalFieldData> additionalInfo, FDUserI user, FDCartI cart, String requestURI) {
		PromotionI redemptionPromo = getRedemptionPromo(user);
		String promoCode = getPromoCode(redemptionPromo);
		if (redemptionPromo != null && redemptionPromo.isLineItemDiscount() && cart.isDiscountInCart(promoCode)) {
			CartSubTotalFieldData data = new CartSubTotalFieldData();
			data.setId("removePromotionCode");
			Map<String, Object> other = data.getOther();
			other.put(PROMO_CODE_KEY, redemptionPromo.getRedemptionCode());
			other.put("requestURI", requestURI);
			additionalInfo.add(data);
		}
	}
	
}
