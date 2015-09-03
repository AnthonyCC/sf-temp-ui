package com.freshdirect.webapp.ajax.expresscheckout.cart.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartSubTotalFieldData;
import com.freshdirect.webapp.ajax.expresscheckout.service.FDCartModelService;
import com.freshdirect.webapp.util.JspMethods;

public class CartSubTotalBoxService {

    private static final String REMOVE_TEXT = "Remove";

    private static final String REMOVE_CODE_KEY = "removeCode";

    private static final CartSubTotalBoxService INSTANCE = new CartSubTotalBoxService();

    private static final String YOU_SAVED = "youSaved";
    private static final String YOU_SAVED_TEXT = "You've Saved";
    private static final String REDEMPTION_PROMO_ID = "redemptionpromo";
    private static final String ORDER_TOTAL_ID = "ssOrderTotal";
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
    private static final String ZERO_POINT_ZERO_ZERO_VALUE = "$0.00";

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

        if (isRedemptionApplied) {
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

        if (redemptionPromo != null && redemptionPromo.isLineItemDiscount() && cart.isDiscountInCart(promoCode)) {
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

    public void populateSavingToBox(List<CartSubTotalFieldData> subTotalBox, FDCartI cart) {
        double saving = cart.getTotalDiscountValue() + cart.getCustomerCreditsValue();

        for (FDCartLineI orderLine : cart.getOrderLines()) {
            if (hasCartLineGroupDiscount(orderLine)) {
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

    private boolean hasCartLineGroupDiscount(FDCartLineI cartLine) {
        return cartLine.getDiscount() == null && cartLine.getGroupQuantity() > 0 && cartLine.getGroupScaleSavings() > 0;
    }

}
