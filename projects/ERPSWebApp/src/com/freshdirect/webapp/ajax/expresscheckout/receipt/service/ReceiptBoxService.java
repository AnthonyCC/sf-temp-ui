package com.freshdirect.webapp.ajax.expresscheckout.receipt.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartSubTotalFieldData;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.data.ReceiptData;
import com.freshdirect.webapp.util.JspMethods;

public class ReceiptBoxService {

    private static final String EXTENDED_DPD_DISCOUNT_PASS_EXTENSION = "Pass extension";
    private static final String EXTENDED_DPD_DISCOUNT_ID = "extendeddpddiscount";
    private static final String REDEEMED_SAMPLE_DESCRIPTION_ID = "redeemedsampledescription";
    private static final String REDEEMED_SAMPLE_FREE = "FREE!";
    private static final String GIFT_CARD_AMOUNT_TO_BE_APPLIED_NAME = "Gift Card Amount to Be Applied";
    private static final String GIFT_CARD_AMOUNT_TO_BE_APPLIED_ID = "giftcardamounttobeapplied";
    private static final String REMAINING_GIFT_CARD_BALANCE_NAME = "Remaining Gift Card Balance";
    private static final String REMAINING_GIFT_CARD_BALANCE_ID = "remaininggiftcardbalance";
    private static final String AMOUNT_TO_BE_CHARGED_TO_YOUR_ACCOUNT_NAME = "Amount to Be Charged to Your Account";
    private static final String AMOUNT_TO_BE_CHARGED_TO_YOUR_ACCOUNT_ID = "amounttobechargetoyouraccount";
    private static final String DELIVERY_CHARGE_WAIVED_NAME = "Delivery Charge (waived)";
    private static final String DELIVERY_CHARGE_ID = "deliverycharge";
    private static final String DELIVERY_CHARGE_NAME = "Delivery Charge";
    private static final String TOTALTAX_ID = "totaltax";
    private static final String TOTAL_TAX_NAME = "Total Tax";
    private static final String DEPOSIT_NAME = "State Bottle Deposit";
    private static final String DEPOSIT_ID = "statebottledeposit";
    private static final String ESTIMATED_PRICE_MARK = "*";
    private static final String SUBTOTAL_NAME = "Order Subtotal";
    private static final String SUBTOTAL_ID = "subtotal";
    private static final String MARK_KEY = "mark";
    private static final String TAXABLE_ITEM_MARK = "T";
    private static final String REMOVE_GIFT_CARD_URL_PARAMETER = "?action=removeGiftCard";
    private static final String ORDER_TOTAL_TEXT = "ORDER TOTAL";
    private static final String ORDER_ESTIMATED_TOTAL_TEXT = "ESTIMATED TOTAL";
    private static final String CREDITS_TEXT = "Credit Applied";
    private static final String CREDITS_ID = "credits";
    private static final String DISCOUNT_ID = "discount";
    private static final String REMOVE_URL_KEY = "removeUrl";
    private static final String DELIVERY_PREMIUM_HAMPTONS_WAIVED_NAME = "Delivery Premium (Hamptons) (waived)";
    private static final String DELIVERY_PREMIUM_HAMPTONS_NAME = "Delivery Premium (Hamptons)";
    private static final String FUEL_SURCHARGE_NAME = "Fuel Surcharge";
    private static final String FUEL_SURCHARGE_ID = "fuelsurcharge";
    private static final String FUEL_SURCHARGE_WAIVED_NAME = "Fuel Surcharge (waived)";
    private static final String ZERO_POINT_ZERO_ZERO_VALUE = "$0.00";
    private static final String TIP_TEXT = "Optional Tip";
    private static final String TIP_ID = "tip";
    private static final String ORDER_FREE_TRIAL_MSG_ID = "ORDER_FREE_TRIAL_MSG_ID";
    private static final String ORDER_FREE_TRIAL_MSG = "ORDER_FREE_TRIAL_MSG";

    private static final ReceiptBoxService INSTANCE = new ReceiptBoxService();

    private ReceiptBoxService() {
    }

    static ReceiptBoxService defaultService() {
        return INSTANCE;
    }

    public void populateExtendDeliveyPassDiscountToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
        String extendDPDescTop = order.getExtendDPDiscountDescription();
        if (extendDPDescTop != null && !extendDPDescTop.equals("NONE")) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(EXTENDED_DPD_DISCOUNT_ID);
            data.setText(extendDPDescTop);
            data.setValue(EXTENDED_DPD_DISCOUNT_PASS_EXTENSION);
            receiptBox.add(data);
        }
    }

    public void populateRedeemedSampleDescriptionToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
        String descSample = order.getRedeemedSampleDescription();
        if (descSample != null && !descSample.equals("NONE")) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(REDEEMED_SAMPLE_DESCRIPTION_ID);
            data.setText(descSample);
            data.setValue(REDEEMED_SAMPLE_FREE);
            receiptBox.add(data);
        }
    }
    
    public void populateDeliveryChargeToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order, FDUserI user) {
    	boolean freeTrialOptinBasedDPApplied = order.isChargeWaived(EnumChargeType.DELIVERY) &&  user.applyFreeTrailOptinBasedDP();
    	
        if (order.isDlvPassApplied() || freeTrialOptinBasedDPApplied) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(DELIVERY_CHARGE_ID);
            data.setText(DELIVERY_CHARGE_NAME);
            String deliveryPassAppliedMessage = DeliveryPassUtil.getDlvPassAppliedMessage(user);
            data.setValue(deliveryPassAppliedMessage);
            receiptBox.add(data);
            
            if(freeTrialOptinBasedDPApplied) {
            	data.getOther().put(ORDER_FREE_TRIAL_MSG, freeTrialOptinBasedDPApplied);
            }
            
        } else if (order.getChargeAmount(EnumChargeType.DELIVERY) > 0) {
            if (order.isChargeWaived(EnumChargeType.DELIVERY)) {
                CartSubTotalFieldData data = new CartSubTotalFieldData();
                data.setId(DELIVERY_CHARGE_ID);
                data.setText(DELIVERY_CHARGE_WAIVED_NAME);
                data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
                receiptBox.add(data);
            } else {
                CartSubTotalFieldData data = new CartSubTotalFieldData();
                data.setId(DELIVERY_CHARGE_ID);
                data.setText(DELIVERY_CHARGE_NAME);
                data.setValue(JspMethods.formatPrice(order.getChargeAmount(EnumChargeType.DELIVERY)));
                if (order.isChargeTaxable(EnumChargeType.DELIVERY)) {
                    data.getOther().put(MARK_KEY, TAXABLE_ITEM_MARK);
                }
                receiptBox.add(data);
            }
        }
        if (order.getChargeAmount(EnumChargeType.DLVPREMIUM) > 0) {
            if (order.isChargeWaived(EnumChargeType.DLVPREMIUM)) {
                CartSubTotalFieldData data = new CartSubTotalFieldData();
                data.setId(DELIVERY_CHARGE_ID);
                data.setText(DELIVERY_PREMIUM_HAMPTONS_WAIVED_NAME);
                data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
                receiptBox.add(data);
            } else {
                CartSubTotalFieldData data = new CartSubTotalFieldData();
                data.setId(DELIVERY_CHARGE_ID);
                data.setText(DELIVERY_PREMIUM_HAMPTONS_NAME);
                data.setValue(JspMethods.formatPrice(order.getChargeAmount(EnumChargeType.DLVPREMIUM)));
                if (order.isChargeTaxable(EnumChargeType.DLVPREMIUM)) {
                    data.getOther().put(MARK_KEY, TAXABLE_ITEM_MARK);
                }
                receiptBox.add(data);
            }
        }
    }

    public void populateGiftCardAmountToBeAppliedToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order, String requestURI) throws FDResourceException {
        double totalAppliedGiftcartAmount = order.getTotalAppliedGCAmount();
        if (totalAppliedGiftcartAmount > 0) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(GIFT_CARD_AMOUNT_TO_BE_APPLIED_ID);
            data.setText(GIFT_CARD_AMOUNT_TO_BE_APPLIED_NAME);
            data.setValue(JspMethods.formatPrice(totalAppliedGiftcartAmount));
            Map<String, Object> other = data.getOther();
            other.put(REMOVE_URL_KEY, requestURI + REMOVE_GIFT_CARD_URL_PARAMETER);
            receiptBox.add(data);
        }
    }

    public void populateRemainingGiftCardBalanceToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order, FDUserI user) throws FDResourceException {
        double totalAppliedGiftcartAmount = order.getTotalAppliedGCAmount();
        if (totalAppliedGiftcartAmount > 0) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(REMAINING_GIFT_CARD_BALANCE_ID);
            data.setText(REMAINING_GIFT_CARD_BALANCE_NAME);
            data.setValue(JspMethods.formatPrice(user.getGiftcardBalance()));
            receiptBox.add(data);
        }
    }

    public void populateAmountToBeChargedYourBalanceToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) throws FDResourceException {
        if (order.getTotalAppliedGCAmount() > 0 && !EnumPaymentMethodType.GIFTCARD.equals(order.getPaymentMethod().getPaymentMethodType())) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(AMOUNT_TO_BE_CHARGED_TO_YOUR_ACCOUNT_ID);
            data.setText(AMOUNT_TO_BE_CHARGED_TO_YOUR_ACCOUNT_NAME);
            data.setValue(JspMethods.formatPrice(order.getCCPaymentAmount()));
            receiptBox.add(data);
        }
    }

    public void populateTaxToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
        final double taxValue = order.getTaxValue();
        if (0 < taxValue) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(TOTALTAX_ID);
            data.setText(TOTAL_TAX_NAME);
            data.setValue(JspMethods.formatPrice(taxValue));
            receiptBox.add(data);
        }
    }

    public void populateDepositValueToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
        double depositValue = order.getDepositValue();
        if (depositValue != 0) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(DEPOSIT_ID);
            data.setText(DEPOSIT_NAME);
            data.setValue(JspMethods.formatPrice(depositValue));
            receiptBox.add(data);
        }
    }

    public void populateSubTotalToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
        CartSubTotalFieldData data = new CartSubTotalFieldData();
        data.setId(SUBTOTAL_ID);
        data.setText(SUBTOTAL_NAME);
        data.setValue(JspMethods.formatPrice(order.getSubTotal()));
        if (order.isEstimatedPrice()) {
            data.getOther().put(MARK_KEY, ESTIMATED_PRICE_MARK);
        }
        receiptBox.add(data);
    }
    
    public void populateSaveAmountBox(ReceiptData receiptData, FDOrderI order) {
    	double saving = order.getSaveAmount(true);
    	if (saving > 0) {
	         receiptData.setSaveAmount(JspMethods.formatPrice(saving));
    	}
    	
    }
    public void populateDiscountsToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
        if (order.getTotalDiscountValue() >= 0) {
            final List<ErpDiscountLineModel> discountLineModels = order.getDiscounts();
            for (ErpDiscountLineModel discountLineModel : discountLineModels) {
                Discount discount = discountLineModel.getDiscount();
                PromotionI promotion = PromotionFactory.getInstance().getPromotion(discount.getPromotionCode());
                String desc = EnumPromotionType.SIGNUP.equals(promotion.getPromotionType()) ? "Free Food" : promotion.getDescription();
                CartSubTotalFieldData discountLine = new CartSubTotalFieldData();
                discountLine.setId(DISCOUNT_ID);
                discountLine.setText(desc);
                discountLine.setValue(JspMethods.formatPriceWithNegativeSign(discount.getAmount()));
                receiptBox.add(discountLine);
            }
        }
    }

    public void populateFuelSurchargeToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
        if (order.getMiscellaneousCharge() > 0.0) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(FUEL_SURCHARGE_ID);
            if (order.isMiscellaneousChargeWaived()) {
                data.setText(FUEL_SURCHARGE_WAIVED_NAME);
                data.setValue(ZERO_POINT_ZERO_ZERO_VALUE);
            } else {
                data.setText(FUEL_SURCHARGE_NAME);
                data.setValue(JspMethods.formatPrice(order.getMiscellaneousCharge()));
                if (order.isMiscellaneousChargeTaxable()) {
                    data.getOther().put(MARK_KEY, TAXABLE_ITEM_MARK);
                }
            }
            receiptBox.add(data);
        }
    }

    public void populateOrderTotalToBox(ReceiptData receiptData, FDOrderI order) {
        final String totalValue = JspMethods.formatPrice(order.getTotal());

        if (order.isEstimatedPrice()) {
            receiptData.setTotalLabel(ORDER_ESTIMATED_TOTAL_TEXT);
            receiptData.setTotal(totalValue + ESTIMATED_PRICE_MARK);
        } else {
            receiptData.setTotalLabel(ORDER_TOTAL_TEXT);
            receiptData.setTotal(totalValue);
        }
    }

    public void populateCustomerCreditsToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
        double customerCreditsValue = order.getCustomerCreditsValue();
        if (0.0 < customerCreditsValue) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(CREDITS_ID);
            data.setText(CREDITS_TEXT);
            data.setValue(JspMethods.formatPriceWithNegativeSign(customerCreditsValue));
            receiptBox.add(data);
        }
    }

     public void populateOrderTipToBox(List<CartSubTotalFieldData> receiptBox, FDOrderI order) {
     
        if (FDStoreProperties.isETippingEnabled() && 0.0 <= order.getTip()) {
            CartSubTotalFieldData data = new CartSubTotalFieldData();
            data.setId(TIP_ID);
            data.setText(TIP_TEXT);
            data.setValue(JspMethods.formatPrice(order.getTip()));
            receiptBox.add(data);
        
    	 }
    }
}
