package com.freshdirect.mobileapi.api.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.api.data.request.PaymentMessageRequest;
import com.freshdirect.mobileapi.api.service.AccountService;
import com.freshdirect.mobileapi.api.service.CheckoutService;
import com.freshdirect.mobileapi.api.service.ConfigurationService;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethods;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.PaymentMethod;
import com.freshdirect.mobileapi.model.SessionUser;

@RestController
@RequestMapping(value = "/checkout")
public class CheckoutController {

    @Autowired
    private AccountService loginService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private CheckoutService checkoutService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public Message setPayment(HttpServletRequest request, HttpServletResponse response) throws FDException, JsonParseException, JsonMappingException, IOException {
        PaymentMessageRequest paymentRequest = objectMapper.readValue(request.getParameter("data"), PaymentMessageRequest.class);
        SessionUser user = loginService.checkLogin(request, response, paymentRequest.getSource());
        ActionResult paymentResult = checkoutService.setPaymentMethod(user, paymentRequest.getPaymentMethodId(), paymentRequest.getBillingRef());
        List<ErpPaymentMethodI> paymentMethods = checkoutService.getPaymentMethods(user);
        String selectedPaymentId  = checkoutService.getSelectedPaymentId(user); 
        PaymentMethods paymentResponse = getPaymentResponse(user, paymentMethods, selectedPaymentId);
        paymentResponse.setConfiguration(configurationService.getConfiguration(user.getFDSessionUser()));
        paymentResponse.addWarningMessages(paymentResult.getWarnings());
        paymentResponse.addErrorMessages(paymentResult.getErrors(), user);
        paymentResponse.setStatus(paymentResult.isSuccess() ? Message.STATUS_SUCCESS : Message.STATUS_FAILED);
        return paymentResponse;
    }

    private PaymentMethods getPaymentResponse(SessionUser user, List<ErpPaymentMethodI> paymentMethods, String selectedPaymentId) throws FDException {
        List<PaymentMethod> ewallet = user.getEwallet(paymentMethods);
        List<PaymentMethod> electronicChecks = user.getElectronicChecks(paymentMethods);
        List<PaymentMethod> creditCards = user.getCreditCards(paymentMethods);
        List<PaymentMethod> ebtCards = user.getEBTCards(paymentMethods);

        boolean isCheckEligible = user.isCheckEligible();
        boolean isEcheckRestricted = user.isEcheckRestricted();
        boolean isEbtAccepted = user.isEbtAccepted();

        PaymentMethods responseMessage = new PaymentMethods(isCheckEligible, isEcheckRestricted, isEbtAccepted, creditCards, electronicChecks, ebtCards, ewallet);

        if ((responseMessage.getCreditCards() != null && responseMessage.getCreditCards().size() == 0)
                && (responseMessage.getElectronicChecks() != null && responseMessage.getElectronicChecks().size() == 0)
                && (responseMessage.getEwallet() != null && responseMessage.getEwallet().size() == 0)) {
            responseMessage.addWarningMessage(MessageCodes.ERR_NO_PAYMENT_METHOD, MessageCodes.ERR_NO_PAYMENT_METHOD_MSG);
        } else {
            responseMessage.setSelectedId(selectedPaymentId);
        }
        responseMessage.getCheckoutHeader().setHeader(user.getShoppingCart());
        return responseMessage;
    }

}
