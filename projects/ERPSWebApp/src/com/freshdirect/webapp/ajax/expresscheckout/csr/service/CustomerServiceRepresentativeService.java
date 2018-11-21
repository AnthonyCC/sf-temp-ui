package com.freshdirect.webapp.ajax.expresscheckout.csr.service;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.expresscheckout.csr.data.CustomerServiceRepresentativeData;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.taglib.callcenter.ComplaintUtil;

public class CustomerServiceRepresentativeService {

    private static final Logger LOG = LoggerFactory.getInstance(CustomerServiceRepresentativeService.class);

    private static final CustomerServiceRepresentativeService INSTANCE = new CustomerServiceRepresentativeService();

    private static final String KEY_SILENT_MODE = "silentMode";
    private static final String KEY_DLV_FORCE_WAIVE = "dlvForceWaive";
    private static final String KEY_DLV_FORCE_WAIVE_PREMIUM = "dlvForceWaivePremium";
    private static final String KEY_INVOICE_MESSAGE = "customerInvoiceMessage";
    private static final String KEY_WAIVE_PHONE_HANDLING = "waivePhoneHandlingCharge";

    private CustomerServiceRepresentativeService() {
    }

    public static CustomerServiceRepresentativeService defaultService() {
        return INSTANCE;
    }

    /**
     * Pre-set CSR fields according to masquerading mode Fields are the following:
     * 
     * - delivery force waive (boolean) - delivery force waive premium (boolean) - waive phone handling charge (boolean) - silent mode (boolean) - customer service message (string)
     * 
     * @param user
     */
    public void presetCustomerServiceRepresentativeInfo(FDUserI user) {
        if (user == null || user.getMasqueradeContext() == null) {
            return;
        }

        final MasqueradeContext ctx = user.getMasqueradeContext();
        final FDCartModel cart = user.getShoppingCart();

        final OrderHistoryI history = getSafeOrderHistory(user);
        final boolean isPhoneChargeActive = history != null ? history.getPhoneOrderCount() >= 3 : false;

        /**
         * Turn on the following CSR flags for the FDX Add-On order
         */
        if (isPhoneChargeActive) {
            cart.setChargeWaived(EnumChargeType.PHONE, true, "CSR"); // setting phone charge waive "on" by default
            cart.setChargeAmount(EnumChargeType.PHONE, ErpServicesProperties.getPhoneHandlingFee());
        }
        
        if (ctx.isAddOnOrderEnabled()) {
            // waive phone charge := on / checked (if active)
            if (isPhoneChargeActive) {
                cart.setChargeWaived(EnumChargeType.PHONE, true, "CSR");
            }

            // waive delivery fee
            ctx.setCsrWaivedDeliveryCharge(true);
        }
    }

    private OrderHistoryI getSafeOrderHistory(FDUserI user) {
        try {
            return user.getOrderHistory();
        } catch (FDResourceException e) {
            return null;
        }
    }

    public void updateCustomerServiceRepresentativeInfo(FormDataRequest csrRequest, FDUserI user) {
        MasqueradeContext masqueradeContext = user.getMasqueradeContext();
        if (masqueradeContext != null) {
            FDCartModel cart = user.getShoppingCart();
            String deliveryForceWaive = FormDataService.defaultService().get(csrRequest, KEY_DLV_FORCE_WAIVE);
            String deliveryForceWaivePremium = FormDataService.defaultService().get(csrRequest, KEY_DLV_FORCE_WAIVE_PREMIUM);
            String waivedPhoneHandling = FormDataService.defaultService().get(csrRequest, KEY_WAIVE_PHONE_HANDLING);
            String silentMode = FormDataService.defaultService().get(csrRequest, KEY_SILENT_MODE);
            String invoiceMessage = FormDataService.defaultService().get(csrRequest, KEY_INVOICE_MESSAGE);
            cart.setChargeWaived(EnumChargeType.PHONE, "on".equals(waivedPhoneHandling) || "true".equals(waivedPhoneHandling), "CSR");
            cart.setCustomerServiceMessage(invoiceMessage);
            masqueradeContext.setCsrWaivedDeliveryCharge("on".equals(deliveryForceWaive) || "true".equals(deliveryForceWaive));
            masqueradeContext.setCsrWaivedDeliveryPremium("on".equals(deliveryForceWaivePremium) || "true".equals(deliveryForceWaivePremium));
            masqueradeContext.setSilentMode("on".equals(silentMode) || "true".equals(silentMode));
        }
    }

    public CustomerServiceRepresentativeData loadCustomerServiceRepresentativeInfo(FDUserI user) {
        CustomerServiceRepresentativeData csrData = null;
        MasqueradeContext masqueradeContext = user.getMasqueradeContext();
        if (masqueradeContext != null) {
            FDCartModel cart = user.getShoppingCart();
            csrData = new CustomerServiceRepresentativeData();
            csrData.setDeliveryForceWaived(masqueradeContext.isCsrWaivedDeliveryCharge());
            csrData.setPremiumDeliveryForceWaived(masqueradeContext.isCsrWaivedDeliveryPremium());
            csrData.setEmailInvoiceSendingSkipped(masqueradeContext.isSilentMode());
            csrData.setPhoneHandlingChargeWaived(cart.isPhoneChargeWaived());
            csrData.setCustomerInvoiceMessage(cart.getCustomerServiceMessage());
            csrData.setPhoneCharge(cart.getPhoneCharge());
            csrData.setDeliveryCharge(cart.getChargeAmount(EnumChargeType.DELIVERY));
            csrData.setDeliveryChargePremium(cart.getChargeAmount(EnumChargeType.DLVPREMIUM));
            try {
                if (masqueradeContext.isMakeGood()) {
                    csrData.setComplaintReasons(ComplaintUtil.getReasonsForDepartment("Makegood"));
                }
            } catch (FDResourceException e) {
                LOG.error("Failed to load make good order complaint reasons", e);
            }
        }
        return csrData;
    }
}
