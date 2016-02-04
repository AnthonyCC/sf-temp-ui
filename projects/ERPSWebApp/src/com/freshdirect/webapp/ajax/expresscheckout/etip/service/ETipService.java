package com.freshdirect.webapp.ajax.expresscheckout.etip.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public class ETipService {

    public static final String ETIP_FIELD_CODE_ID = "tipAmount";
    private static final String CUSTOM_TIP_NAME = "Other Amount";
    private static final String CUSTOM_TIP_AMOUNT_ID = "otherTipAmount";
    private static final double CUSTOM_TIP_RATIO = 0.32;

    private static final ETipService INSTANCE = new ETipService();

    private ETipService() {
    }

    public static ETipService defaultService() {
        return INSTANCE;
    }

    public List<ValidationError> applyETip(FormDataRequest eTipRequestData, FDUserI user, HttpSession session) {
        List<ValidationError> result = new ArrayList<ValidationError>();

        String tipAmount = FormDataService.defaultService().get(eTipRequestData, ETIP_FIELD_CODE_ID);
        boolean isCustomTip = false;
        if (tipAmount == null || tipAmount.equalsIgnoreCase(CUSTOM_TIP_NAME)) {
            tipAmount = FormDataService.defaultService().get(eTipRequestData, CUSTOM_TIP_AMOUNT_ID);
            isCustomTip = true;
        }

        if (tipAmount.startsWith("$")) {
            tipAmount = tipAmount.substring(1);
        }

        if (tipAmount.startsWith(".")) { // allow change without leading zero
            tipAmount = "0" + tipAmount;
        }
        if (tipAmount.endsWith(".")) { // allow no change like "3."
            tipAmount += "0";
        }

        if (tipAmount.length() < 1) {
            tipAmount += "0.00";
        }

        try {
            double parsedTipAmount = Double.parseDouble(tipAmount);
            double subTotal = user.getShoppingCart().getSubTotal();
            double maximumTipAllowed = subTotal * CUSTOM_TIP_RATIO;
            double roundedMaximumTipAllowed = (double) Math.round(maximumTipAllowed*100)/100;
            if (roundedMaximumTipAllowed < parsedTipAmount) {
                result.add(new ValidationError(ETIP_FIELD_CODE_ID, MessageFormat.format("As of now, we cap all electronic tips at 32% of the subtotal, making the highest allowed tip to be ${0} for this order.", roundedMaximumTipAllowed)));
            }
        } catch (NumberFormatException e) {
            result.add(new ValidationError(ETIP_FIELD_CODE_ID, "Not a valid tip amount , " + tipAmount));
        }

        if (result.isEmpty()) {
            FDCartModel cart = user.getShoppingCart();
            cart.setTip(Double.parseDouble(tipAmount));
            cart.setCustomTip(isCustomTip);
        }

        return result;
    }

}
