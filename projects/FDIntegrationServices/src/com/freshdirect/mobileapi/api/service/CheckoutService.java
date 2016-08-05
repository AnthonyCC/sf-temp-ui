package com.freshdirect.mobileapi.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;

@Component
public class CheckoutService {

    public List<ErpPaymentMethodI> getPaymentMethods(SessionUser user) {
        // Discard Masterpass Wallet Cards from list -- For Standard Checkout Feature
        List<ErpPaymentMethodI> paymentMethods = discardEwalletCards(user.getPaymentMethods(), "MP");
        paymentMethods = discardEwalletCards(paymentMethods, "PP");
        return paymentMethods;
    }

    public ActionResult setPaymentMethod(SessionUser user, String paymentMethodId, String billingRef) throws FDException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.setPaymentMethodEx(paymentMethodId, billingRef);
        return resultBundle.getActionResult();
    }

    private List<ErpPaymentMethodI> discardEwalletCards(List<ErpPaymentMethodI> paymentMethods, String walletType) {
        List<ErpPaymentMethodI> updatedPaymentMethodList = new ArrayList<ErpPaymentMethodI>();
        if (paymentMethods != null && !paymentMethods.isEmpty()) {
            for (ErpPaymentMethodI paymentMethod : paymentMethods) {
                if (paymentMethod.geteWalletID() != null) {
                    int ewalletId = Integer.parseInt(paymentMethod.geteWalletID());
                    if (ewalletId == EnumEwalletType.getEnum(walletType).getValue()) {
                        continue;
                    } else {
                        updatedPaymentMethodList.add(paymentMethod);
                    }
                } else {
                    updatedPaymentMethodList.add(paymentMethod);
                }
            }
        }
        return updatedPaymentMethodList;
    }

    public String getSelectedPaymentId(SessionUser user) {
        Checkout checkout = new Checkout(user);
        return checkout.getPreselectedPaymethodMethodId();
    }
}
