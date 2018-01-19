package com.freshdirect.webapp.ajax.expresscheckout.cart.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartSubTotalFieldData;
import com.freshdirect.webapp.ajax.expresscheckout.service.FDCartModelService;
import com.freshdirect.webapp.util.JspMethods;

public class CartSubTotalBoxService {

    private static final CartSubTotalBoxService INSTANCE = new CartSubTotalBoxService();

    private static final String GIFT_CARD_BALANCE_NAME = "Gift Card Balance";
    private static final String GIFT_CARD_BALANCE_ID = "giftcardbalance";
    private static final String REMOVE_TEXT = "Remove";
    private static final String REMOVE_CODE_KEY = "removeCode";
    private static final String YOU_SAVED = "youSaved";
    private static final String YOU_SAVED_TEXT = "You've Saved";
    private static final String REDEMPTION_PROMO_ID = "redemptionpromo";
    private static final String ORDER_TOTAL_ID = "ssOrderTotal";
    private static final String AVALARA_ORDER_TOTAL_ID = "ssOrderSubTotal";
    private static final String ORDER_TOTAL_TEXT = "Order Total";
    private static final String ORDER_ESTIMATED_TOTAL_TEXT = "Estimated Total";
    private static final String CREDITS_TEXT = "Credit Applied";
    private static final String CREDITS_ID = "credits";
    private static final String FREE_FOOD_TEXT = "Free Food";
    private static final String FREE_FOOD_ID = "freefood";
    private static final String PROMO_CODE_KEY = "promoCode";
    private static final String MARK_KEY = "mark";
    private static final String TAXABLE_ITEM_MARK = "T";
    private static final String ESTIMATED_PRICE_MARK = "*";
    private static final String SUBTOTAL_NAME_ETIP = "Subtotal";
    private static final String SUBTOTAL_NAME = "Order Subtotal";
    private static final String SUBTOTAL_ID = "subtotal";
    private static final String FUEL_SURCHARGE_NAME = "Fuel Surcharge";
    private static final String FUEL_SURCHARGE_ID = "fuelsurcharge";
    private static final String FUEL_SURCHARGE_WAIVED_NAME = "Fuel Surcharge (waived)";
    private static final String TOTAL_TAX_WAIVED_NAME = "Total Tax (waived)";
    private static final String DELIVERY_FEE_NAME = "Delivery Fee";
    private static final String DELIVERY_CHARGE_WAIVED_NAME = "Delivery Charge (waived)";
    private static final String DELIVERY_FEE_ID = "deliveryfee";
    private static final String DEPOSIT_NAME = "State Bottle Deposit";
    private static final String DEPOSIT_ID = "statebottledeposit";
    private static final String TOTAL_TAX_NAME = "Total Tax";
    private static final String TOTAL_TAX_NAME_ETIP = "Sales Tax";
    private static final String TOTALTAX_ID = "totaltax";
    private static final String AVAL_TOTALTAX_ID = "totalAvalaratax";
    private static final String ZERO_POINT_ZERO_ZERO_VALUE = "$0.00";
    private static final String VIEW_CART_TAX_AVALARA = "Added during Checkout";
    private static final String TIP = "Tip";
    
    private static final String DELIVERY_CHARGE_ID = "deliverycharge";
    private static final String DELIVERY_PREMIUM_HAMPTONS_WAIVED_NAME = "Delivery Premium (Hamptons) (waived)";
    private static final String DELIVERY_PREMIUM_HAMPTONS_NAME = "Delivery Premium (Hamptons)";

    private CartSubTotalBoxService() {
    }

    public static CartSubTotalBoxService defaultService() {
        return INSTANCE;
    }

    public void populateSubTotalToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
        double subTotalValue = cart.getSubTotal();
        CartSubTotalFieldData data = new CartSubTotalFieldData();
        data.setId(SUBTOTAL_ID);
        if(FDStoreProperties.isETippingEnabled()){
        	data.setText(SUBTOTAL_NAME_ETIP);
        }else{
        	data.setText(SUBTOTAL_NAME);
        }
        data.setValue(JspMethods.formatPrice(subTotalValue));
        if (cart.isEstimatedPrice()) {
            data.getOther().put(MARK_KEY, ESTIMATED_PRICE_MARK);
        }
        subTotalBox.add(data);
    }

    public void populateTaxToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart, boolean hasMultipleStores) {
        CartSubTotalFieldData data = new CartSubTotalFieldData();
        data.setId(TOTALTAX_ID);
        if (FDCartModelService.defaultService().isEbtPaymentForCart(cart)) {
            data.setText(TOTAL_TAX_WAIVED_NAME);
            data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
            subTotalBox.add(data);
        } else {
            double taxValue = cart.getTaxValue();
            if (0 < taxValue) {
            	if(FDStoreProperties.isETippingEnabled() && !hasMultipleStores){
            		data.setText(TOTAL_TAX_NAME_ETIP );
            	}else{
            		data.setText(TOTAL_TAX_NAME );
            	}
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
        final double epsilon = 0.0000001;
        
        boolean isDlvPassApplied = false;
        boolean isDlvPromotionApplied = false;
        if (cart instanceof FDCartModel) {
            isDlvPassApplied = ((FDCartModel) cart).isDlvPassApplied();
            isDlvPromotionApplied = ((FDCartModel) cart).isDlvPromotionApplied();
        } else if (cart instanceof FDOrderAdapter) {
            isDlvPassApplied = ((FDOrderAdapter) cart).isDlvPassApplied();
        }

        boolean deliveryPassPopupNeeded = false;
        String deliveryPassName = DELIVERY_FEE_NAME;
        String deliveryPassValue = ZERO_POINT_ZERO_ZERO_VALUE;
        if (isDlvPromotionApplied) {
            deliveryPassName = DELIVERY_CHARGE_WAIVED_NAME;
            deliveryPassPopupNeeded = true; // FIXME not sure ...
        } else if (isDlvPassApplied) {
            deliveryPassValue = DeliveryPassUtil.getDlvPassAppliedMessage(user);
        } else if (cart.getChargeAmount(EnumChargeType.DELIVERY) > 0) {
            if (cart.isChargeWaived(EnumChargeType.DELIVERY)) {
                deliveryPassName = DELIVERY_CHARGE_WAIVED_NAME;
                deliveryPassPopupNeeded = true;
            } else {
                deliveryPassValue = JspMethods.formatPrice(cart.getChargeAmount(EnumChargeType.DELIVERY));
                deliveryPassPopupNeeded = true;
            }
        }
        
        if (cart.getChargeAmount(EnumChargeType.DLVPREMIUM)> 0) {
            if (cart.isChargeWaived(EnumChargeType.DLVPREMIUM)) {
                CartSubTotalFieldData data = new CartSubTotalFieldData();
                data.setId(DELIVERY_CHARGE_ID);
                data.setText(DELIVERY_PREMIUM_HAMPTONS_WAIVED_NAME);
                data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
                subTotalBox.add(data);
            } else {
                CartSubTotalFieldData data = new CartSubTotalFieldData();
                data.setId(DELIVERY_CHARGE_ID);
                data.setText(DELIVERY_PREMIUM_HAMPTONS_NAME);
                data.setValue(JspMethods.formatPrice(cart.getChargeAmount(EnumChargeType.DLVPREMIUM)));
                if (cart.isChargeTaxable(EnumChargeType.DLVPREMIUM)) {
                    data.getOther().put(MARK_KEY, TAXABLE_ITEM_MARK);
                }
                subTotalBox.add(data);
            }
        }
        
        if(null!=cart.getEStoreId() && "FDX".equalsIgnoreCase(cart.getEStoreId().getContentId())){
        	deliveryPassPopupNeeded=false;
        }
        
        boolean isTaxableItemInCart = false;
        for (FDCartLineI lineItem : cart.getOrderLines()) {
            if (lineItem.getTaxRate() > epsilon) {
                isTaxableItemInCart = true;
                break;
            }
        }

        CartSubTotalFieldData data = new CartSubTotalFieldData();
        cartData.setDeliveryCharge(deliveryPassValue);
        data.setId(DELIVERY_FEE_ID);
        data.setText(deliveryPassName);
        data.setValue(deliveryPassValue);
        data.getOther().put("deliveryPassPopupNeeded", deliveryPassPopupNeeded);
        if (isTaxableItemInCart) {
            data.getOther().put(MARK_KEY, TAXABLE_ITEM_MARK);
        }

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
        cartData.setPromotionApplied(isRedemptionApplied);

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
                other.put(REMOVE_CODE_KEY, REMOVE_TEXT);
                redemptionPromoData.setOther(other);
                subTotalBox.add(redemptionPromoData);
                cartData.setPromotionApplied(true);
            } else { // Its a automatic header discount
                PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
                CartSubTotalFieldData automaticHeaderDiscount = new CartSubTotalFieldData();
                automaticHeaderDiscount.setId("automaticheaderdiscount");
                Map<String, Object> other = automaticHeaderDiscount.getOther();
                other.put(PROMO_CODE_KEY, promotion.getPromotionCode());
                automaticHeaderDiscount.setOther(other);
                automaticHeaderDiscount.setText(promotion.getDescription());
                automaticHeaderDiscount.setValue(JspMethods.formatPriceWithNegativeSign(discount.getAmount()));
                subTotalBox.add(automaticHeaderDiscount);
            }
        }

        if (isRedemptionApplied && !removeLinkDisplayed) {
            if (redemptionPromo.isSampleItem()) {
                CartSubTotalFieldData sampleRedemptionPromo = new CartSubTotalFieldData();
                sampleRedemptionPromo.setId(REDEMPTION_PROMO_ID);
                sampleRedemptionPromo.setText(redemptionPromo.getDescription());
                Map<String, Object> other = sampleRedemptionPromo.getOther();
                other.put(PROMO_CODE_KEY, promoCode);
                sampleRedemptionPromo.setValue("FREE!");
                other.put(REMOVE_CODE_KEY, REMOVE_TEXT);
                sampleRedemptionPromo.setOther(other);
                subTotalBox.add(sampleRedemptionPromo);
            } else if (redemptionPromo.isWaiveCharge() && !removeLinkDisplayed) {
                CartSubTotalFieldData waivedRedemptionPromo = new CartSubTotalFieldData();
                waivedRedemptionPromo.setId(REDEMPTION_PROMO_ID);
                waivedRedemptionPromo.setText(redemptionPromo.getDescription());
                Map<String, Object> other = waivedRedemptionPromo.getOther();
                other.put(PROMO_CODE_KEY, promoCode);
                waivedRedemptionPromo.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
                other.put(REMOVE_CODE_KEY, REMOVE_TEXT);
                waivedRedemptionPromo.setOther(other);
                subTotalBox.add(waivedRedemptionPromo);
            } else if (redemptionPromo.isExtendDeliveryPass()) {
                CartSubTotalFieldData extendedDeliveryPassRedemptionPromo = new CartSubTotalFieldData();
                extendedDeliveryPassRedemptionPromo.setId(REDEMPTION_PROMO_ID);
                extendedDeliveryPassRedemptionPromo.setText(redemptionPromo.getDescription());
                Map<String, Object> other = extendedDeliveryPassRedemptionPromo.getOther();
                other.put(PROMO_CODE_KEY, promoCode);
                extendedDeliveryPassRedemptionPromo.setValue("Pass extension");
                other.put(REMOVE_CODE_KEY, REMOVE_TEXT);
                extendedDeliveryPassRedemptionPromo.setOther(other);
                subTotalBox.add(extendedDeliveryPassRedemptionPromo);
            }
        }

        if (redemptionPromo != null && redemptionPromo.isLineItemDiscount() && cart.isDiscountInCart(promoCode) && !removeLinkDisplayed) {
            CartSubTotalFieldData lineItemRedemptionPromo = new CartSubTotalFieldData();
            lineItemRedemptionPromo.setId(REDEMPTION_PROMO_ID);
            lineItemRedemptionPromo.setText(redemptionPromo.getDescription());
            Map<String, Object> other = lineItemRedemptionPromo.getOther();
            other.put(PROMO_CODE_KEY, promoCode);
            lineItemRedemptionPromo.setValue(JspMethods.formatPriceWithNegativeSign(cart.getLineItemDiscountAmount(redemptionPromo.getPromotionCode())));
            other.put(REMOVE_CODE_KEY, REMOVE_TEXT);
            lineItemRedemptionPromo.setOther(other);
            subTotalBox.add(lineItemRedemptionPromo);
            cartData.setPromotionApplied(true);
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
        	if(FDStoreProperties.getAvalaraTaxEnabled()){
        		data.setText(SUBTOTAL_NAME);
        	}
        	else{
        		data.setText(ORDER_TOTAL_TEXT);
        	}
        }

        	data.setValue(JspMethods.formatPrice(cart.getTotal()));
        subTotalBox.add(data);
    }

    public void populateGiftBalanceToBox(List<CartSubTotalFieldData> subTotalBox, FDUserI user) {
        if (user.getGiftcardBalance() > 0) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(GIFT_CARD_BALANCE_ID);
            data.setText(GIFT_CARD_BALANCE_NAME);
            data.setValue(JspMethods.formatPriceWithNegativeSign(user.getGiftcardBalance()));
            subTotalBox.add(data);
        }
    }

    public void populateGiftCardsTotalBalanceToBox(List<CartSubTotalFieldData> subTotalBox, FDUserI user, FDCartI cart) throws FDResourceException {
        double perishableBufferAmount = 0;
        double gcSelectedBalance = user.getGiftcardBalance() - cart.getTotalAppliedGCAmount();
        double gcBufferAmount = 0;
        if (cart instanceof FDCartModel) {
            perishableBufferAmount = FDCustomerManager.getPerishableBufferAmount((FDCartModel) cart);
        }
        if (perishableBufferAmount > 0) {
            if (cart.getTotalAppliedGCAmount() > 0) {
                ErpPaymentMethodI paymentMethod = cart.getPaymentMethod();
                if (!EnumPaymentMethodType.GIFTCARD.equals(paymentMethod.getPaymentMethodType())) {
                    gcBufferAmount = gcSelectedBalance;
                } else {
                    gcBufferAmount = perishableBufferAmount;
                }
            }
        }

        CartSubTotalFieldData data = new CartSubTotalFieldData();
        data.setId(GIFT_CARD_BALANCE_ID);
        data.setText(GIFT_CARD_BALANCE_NAME);
        double giftCardsBalance = 0;
        if (cart.getTotalAppliedGCAmount() > 0) {
            giftCardsBalance = user.getGiftcardsTotalBalance();
            if (cart instanceof FDCartModel) {
                giftCardsBalance = giftCardsBalance - cart.getTotalAppliedGCAmount() - gcBufferAmount;
            }
            data.setValue(JspMethods.formatPriceWithNegativeSign(user.getGiftcardBalance()));
            subTotalBox.add(data);
        } else {
            giftCardsBalance = user.getGiftcardsTotalBalance();
        }
        data.setValue(JspMethods.formatPriceWithNegativeSign(giftCardsBalance));
        subTotalBox.add(data);
    }

    public CartSubTotalFieldData createSavingToBox(FDCartI cart) {
        double saving = cart.getSaveAmount(true);

        if (0 < saving) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(YOU_SAVED);
            data.setText(YOU_SAVED_TEXT);
            data.setValue(JspMethods.formatPrice(saving));
            return data;
        }
        return null;
    }

    public void populateTipToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
        if (EnumEStoreId.FDX.equals(cart.getEStoreId()) && 0.0 < cart.getTip()) {
                CartSubTotalFieldData data = new CartSubTotalFieldData();
                data.setId(TIP);
                data.setText(TIP);
                data.setValue(JspMethods.formatPrice(cart.getTip()));
                subTotalBox.add(data);
        }
    }

	public void populateAvalaraTaxToBox(
			List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
		 CartSubTotalFieldData data = new CartSubTotalFieldData();
	        data.setId(AVAL_TOTALTAX_ID);
	        if (FDCartModelService.defaultService().isEbtPaymentForCart(cart)) {
	            data.setText(TOTAL_TAX_WAIVED_NAME);
	            data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
	            subTotalBox.add(data);
	        } else {
	            	data.setText(TOTAL_TAX_NAME );
	            	data.setValue(VIEW_CART_TAX_AVALARA);
	                subTotalBox.add(data);
	            }
	        
	}
	
}
