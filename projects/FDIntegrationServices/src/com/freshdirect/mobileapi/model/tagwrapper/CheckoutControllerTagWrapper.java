package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.CheckoutControllerTag;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CheckoutControllerTagWrapper extends ControllerTagWrapper implements RequestParamName, SessionParamName, MessageCodes {

    private static Category LOGGER = LoggerFactory.getInstance(CheckoutControllerTagWrapper.class);

    public static final String ACTION_SET_DELIVERY_ADDRESS = "setDeliveryAddress";

    public static final String ACTION_EDIT_SET_DELIVERY_ADDRESS = "editAndSetDeliveryAddress"; //Not supported

    public static final String ACTION_DELETE_DELIVERY_ADDRESS = "deleteDeliveryAddress"; //Not supported

    public static final String ACTION_RESERVE_DELIVERY_TIMESLOT = "reserveDeliveryTimeSlot";

    public static final String ACTION_SUBMIT_ORDER = "submitOrder";

    public static final String ACTION_SET_PAYMENT_METHOD = "setPaymentMethod";

    public static final String ACTION_ADD_SET_PAYMENT_METHOD = "addAndSetPaymentMethod"; //Not supported

    public static final String ACTION_ADD_PAYMENT_METHOD = "addPaymentMethod"; //Not supported

    public static final String ACTION_SET_PAYMENT_METHOD_AND_SUBMIT = "setPaymentAndSubmit"; //Not supported

    public static final String ACTION_DELETE_PAYMENT_METHOD = "deletePaymentMethod"; //Not supported

    public static final String ACTION_SET_DELIVERY_ADDRESS_AND_PAYMENT = "setDeliveryAddressAndPayment"; //Not supported

    protected CheckoutControllerTagWrapper(CheckoutControllerTag wrapTarget, SessionUser user) {
        this(wrapTarget, user.getFDSessionUser());
    }

    protected CheckoutControllerTagWrapper(CheckoutControllerTag wrapTarget, FDUserI user) {
        super(wrapTarget, user);
        ((HttpResponseWrapper) this.pageContext.getResponse()).setSuccessUrl(wrapTarget.getSuccessPage());
    }

    public CheckoutControllerTagWrapper(FDUserI user) {
        this(new CheckoutControllerTag(), user);
    }

    public CheckoutControllerTagWrapper(SessionUser sessionUser) {
        this(new CheckoutControllerTag(), sessionUser.getFDSessionUser());
    }

    public ResultBundle submitOrder() throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_PROCESSING_ORDER, SESSION_PARAM_MAKEGOOD_COMPLAINT,
                SESSION_PARAM_APPLICATION, SESSION_PARAM_MAKE_GOOD_ORDER, SESSION_PARAM_CRM_AGENT, SESSION_PARAM_CUSTOMER_SERVICE_REP,
                SESSION_PARAM_RECENT_ORDER_NUMBER }, new String[] { SESSION_PARAM_PROCESSING_ORDER, SESSION_PARAM_USER,
                SESSION_PARAM_SMART_STORE_PREV_RECOMMENDATIONS, SESSION_PARAM_AUTHORIZED_PEOPLE, SESSION_PARAM_PICKUP_AGREEMENT,
                SESSION_PARAM_RECENT_ORDER_NUMBER, SESSION_PARAM_DLV_PASS_SESSION_ID, SESSION_PARAM_REFERENCED_ORDER,
                SESSION_PARAM_MAKE_GOOD_ORDER }); //gets,sets
        addExpectedRequestValues(new String[] { SESSION_PARAM_RECENT_ORDER_NUMBER, REQ_PARAM_IGNORE_PROMO_ERRORS }, new String[] { REQ_PARAM_PAYMENT_METHOD_ID,
                REQ_PARAM_BILLING_REF });//gets,sets
        getWrapTarget().setActionName(ACTION_SUBMIT_ORDER);
        setMethodMode(true);
        ResultBundle result = new ResultBundle(executeTagLogic(), this);

        //Tag Lib set some values in session that we need. Get them. 
        result.addExtraData(SESSION_PARAM_RECENT_ORDER_NUMBER, pageContext.getSession().getAttribute(SESSION_PARAM_RECENT_ORDER_NUMBER));

        //((CheckoutControllerTag)wrapTarget).
        LOGGER.debug("Success page after execution is:" + getWrapTarget().getSuccessPage());

        String redirectUrl = ((HttpResponseWrapper) pageContext.getResponse()).getSendRedirectUrl();
        LOGGER.debug("redirectUrl is:" + redirectUrl);

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();

        //Check redirect status on credit card auth failure
        if ((result.getActionResult() == null) && (redirectUrl != null)) {
            ActionResult actionResult = new ActionResult();
            if (redirectUrl.equals(wrappedTag.getCcdProblemPage())) {
                actionResult.addError(new ActionError(ERR_CREDIT_CARD_PROBLEM, ERR_CREDIT_CARD_PROBLEM_MSG));
            } else if (redirectUrl.equals(wrappedTag.getAuthCutoffPage())) {
                actionResult.addError(new ActionError(ERR_PAYMENT_ACCOUNT_PROBLEM, ERR_PAYMENT_ACCOUNT_PROBLEM_MSG));
            } else if (redirectUrl.equals(wrappedTag.getGcAVSExceptionPage())) {
                actionResult.addError(new ActionError(ERR_GIFTCARD_AVS_EXCEPTION, ERR_GIFTCARD_AVS_EXCEPTION_MSG));
            } else {
                actionResult.addError(new ActionError(ERR_GENERIC_CHECKOUT_EXCEPTION, ERR_GENERIC_CHECKOUT_EXCEPTION_MSG));
            }
            result.setActionResult(actionResult);
        }

        return result;
    }

    public ResultBundle setPaymentMethod(String paymentMethodId, String billingReference) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT },
                new String[] { SESSION_PARAM_USER }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CHEF_TABLE, REQ_PARAM_PAYMENT_METHOD_ID, REQ_PARAM_BILLING_REF,
                REQ_PARAM_GIFT_CARD, REQ_PARAM_DONATION, REQ_PARAM_BYPASS_ACCOUNT_CHECK }, new String[] { REQ_PARAM_PAYMENT_METHOD_ID,
                REQ_PARAM_BILLING_REF });//gets,sets
        addRequestValue(REQ_PARAM_PAYMENT_METHOD_ID, paymentMethodId);
        addRequestValue(REQ_PARAM_BILLING_REF, billingReference);
        getWrapTarget().setActionName(ACTION_SET_PAYMENT_METHOD);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    public ResultBundle reserveDeliveryTimeslot(String deliveryTimeslotId) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION }, new String[] { SESSION_PARAM_USER }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CHEF_TABLE, REQ_PARAM_SLOT_ID }, new String[] { REQ_PARAM_SLOT_ID });//gets,sets
        addRequestValue(REQ_PARAM_SLOT_ID, deliveryTimeslotId);
        getWrapTarget().setActionName(ACTION_RESERVE_DELIVERY_TIMESLOT);
        setMethodMode(true);
        return new ResultBundle(executeTagLogic(), this);
    }

    /*
     * DUP: com.freshdirect.webapp.taglib.fdstore.CheckoutControllerTag
     * LAST UPDATED ON: 9/30/2009
     * LAST UPDATED WITH SVN#: 6017
     * WHY: Need to detect if tag is trying to redirect to age verification page. would like to 
     *     reference but it's a private field.
     * WHAT: constant in tag class that defines URL for page verification.
     */
    public ResultBundle setCheckoutDeliveryAddress(SessionUser user, String selectAddressId, DeliveryAddressType type) throws FDException {
        addExpectedSessionValues(new String[] { SESSION_PARAM_APPLICATION, SESSION_PARAM_CUSTOMER_SERVICE_REP, SESSION_PARAM_CRM_AGENT },
                new String[] { SESSION_PARAM_USER, SessionName.SIGNUP_WARNING }); //gets,sets
        addExpectedRequestValues(new String[] { REQ_PARAM_CHEF_TABLE, REQ_PARAM_PAYMENT_METHOD_ID, REQ_PARAM_BILLING_REF }, new String[] {
                REQ_PARAM_PAYMENT_METHOD_ID, REQ_PARAM_BILLING_REF });//gets,sets

        addRequestValue("contact_phone_" + selectAddressId, null); //TODO: move to constant file

        //Treat depot and pickup same.
        if (DeliveryAddressType.DEPOT.equals(type) || DeliveryAddressType.PICKUP.equals(type)) {
            selectAddressId = "DEPOT_" + selectAddressId;
        }
        addRequestValue("selectAddressList", selectAddressId); //TODO: move to constant file
        addRequestValue("corpDlvInstructions", null); //TODO: move to constant file
        addRequestValue(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode(), null);
        addRequestValue(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode(), null);
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode(), null);
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode(), null);
        addRequestValue(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode(), null);
        addRequestValue("makeGoodOrder", null); //TODO: move to constant file

        getWrapTarget().setActionName(ACTION_SET_DELIVERY_ADDRESS);
        setMethodMode(true);

        ActionResult actionResult = executeTagLogic();
        String successPage = ((CheckoutControllerTag) wrapTarget).getSuccessPage();

        if (actionResult == null) {
            actionResult = new ActionResult();
        }

        CheckoutControllerTag wrappedTag = (CheckoutControllerTag) this.getWrapTarget();
        if (wrappedTag.getAgeVerificationPage().equals(successPage)) {
            actionResult.addError(new ActionError(ERR_AGE_VERIFICATION, MobileApiProperties.getMediaPath()
                    + MobileApiProperties.getAlcoholAgeWarningMediaPath()));
        }
        return new ResultBundle(actionResult, this);
    }

    /*
     * DUP: com.freshdirect.webapp.taglib.fdstore.AgeVerificationControllerTag
     * LAST UPDATED ON: 10/12/2009
     * LAST UPDATED WITH SVN#: 5951
     * WHY: Address verification for alcohol was tightly integrated with age verification.  
     *     The logic is duplicated here so that could be better reused
     * WHAT: logic to check is alcohol delivery is allowed for specified address
     */
    /**
     * @param address
     * @return
     * @throws JspException
     * @throws FDResourceException
     */
    private boolean verifyAddress(ErpAddressModel address) throws FDResourceException {
        if (!FDDeliveryManager.getInstance().checkForAlcoholDelivery(address)) {
            //response.sendRedirect(response.encodeRedirectURL(blockedAddressPage));
            return false;
        }
        return true;
    }

}
