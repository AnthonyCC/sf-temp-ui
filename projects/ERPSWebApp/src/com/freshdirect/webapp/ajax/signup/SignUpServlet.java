package com.freshdirect.webapp.ajax.signup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.enums.CaptchaType;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ActionWarning;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.service.RedirectService;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.CaptchaUtil;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.RequestUtil;

public class SignUpServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -6022478595013059984L;

    private static final Category LOGGER = LoggerFactory.getInstance(SignUpServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        HttpSession session = request.getSession();
        SignUpRequest signUpRequest = BaseJsonServlet.parseRequestData(request, SignUpRequest.class);
        boolean skipPopup = false;
        EnumServiceType serviceType = NVL.apply(EnumServiceType.getEnum(signUpRequest.getServiceType()), EnumServiceType.HOME);
        String successPage = FDURLUtil.extendsUrlWithServiceType(NVL.apply(signUpRequest.getSuccessPage(), FDURLUtil.LANDING_PAGE), serviceType);

        try {
            SignUpResponse signUpResponse = new SignUpResponse();
            SignUpService registrationService = selectSignUpService(serviceType);

            ActionResult result = registrationService.validate(signUpRequest);

            boolean isCaptchaSuccess = CaptchaUtil.validateCaptcha(signUpRequest.getCaptchaToken(), request.getRemoteAddr(), CaptchaType.SIGN_UP, session,
                    SessionName.SIGNUP_ATTEMPT, FDStoreProperties.getMaxInvalidSignUpAttempt());
            result.addError(!isCaptchaSuccess, "captcha", SystemMessageList.MSG_INVALID_CAPTCHA);

            if (result.isSuccess()) {

                if (user.getLevel() < FDUserI.RECOGNIZED) {
                    String zipCode = signUpRequest.getZipCode();
                    FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode);
                    AddressModel address = new AddressModel();
                    address.setZipCode(zipCode);
                    serviceType = checkServiceStatus( serviceType, result, serviceResult);
                    FDSessionUser signedInUser = UserUtil.createSessionUser(serviceType, serviceResult.getAvailableServices(), session, response, address);
                    try {
                        FDActionInfo actionInfo = AccountActivityUtil.getActionInfo(session);
                        FDCustomerModel fdCustomer = registrationService.createFdCustomer(signedInUser, signUpRequest);
                        ErpCustomerModel erpCustomer = registrationService.createErpCustomer(signedInUser, signUpRequest);
                        RegisterService.getInstance().register(signedInUser, actionInfo, erpCustomer, fdCustomer, serviceType, signUpRequest.isTcAgree());
                    } catch (ErpDuplicateUserIdException de) {
                        LOGGER.warn("User registration failed due to duplicate id", de);
                        result.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_UNIQUE_USERNAME));
                    } catch (ErpFraudException fe) {
                        LOGGER.warn("User registration failed due to ", fe);
                        result.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), fe.getFraudReason().getDescription()));
                    }
                } else {
                    AddressModel address = null;
                    FDDeliveryServiceSelectionResult serviceResult = null;
                    serviceType = user.getSelectedServiceType();
                    //Address will not be null when user signs up for a Partial Delivery address
                    if(user.getAddress() != null && user.getAddress().getAddress1() != null && user.getAddress().getAddress1().length() > 0) {
                        // VALIDATE DELIVERY ADDRESS to see if service restricted
                        address = user.getAddress();
                        address.setServiceType(serviceType);
                        serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByAddress(address);
                    } else {
                        //Directly from Zip Check page
                        String zipCode = user.getZipCode();
                        serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode);
                        address = new AddressModel();
                        address.setZipCode(zipCode);
                    }
                    skipPopup = true;
                    serviceType = checkServiceStatus( serviceType, result, serviceResult);
                    UserUtil.reclassifyUser(serviceType, serviceResult.getAvailableServices(), session, response, address);
                }
            }

            if (result.isSuccess()) {
                CaptchaUtil.resetAttempt(session, SessionName.SIGNUP_ATTEMPT);
                response.addCookie(RequestUtil.createCookie("hasJustSignedUp", "true", session.getMaxInactiveInterval()));
            } else {
                CaptchaUtil.increaseAttempt(session, SessionName.SIGNUP_ATTEMPT);
                if (CaptchaUtil.isExcessiveAttempt(FDStoreProperties.getMaxInvalidSignUpAttempt(), session, SessionName.SIGNUP_ATTEMPT)) {
                    signUpResponse.setMessage("CaptchaRedirect");
                }
            }

            if (result.isSuccess()) {
                String preSuccessPageFromSession = (String) session.getAttribute(SessionName.PREV_SUCCESS_PAGE);
                if (preSuccessPageFromSession != null) {
                    session.removeAttribute(SessionName.PREV_SUCCESS_PAGE);
                    successPage = preSuccessPageFromSession;
                    skipPopup = true;
                }
            }

            session.setAttribute(SessionName.SIGNUP_SUCCESS, result.isSuccess());

            signUpResponse.setSuccess(result.isSuccess());
            signUpResponse.setEmail(signUpRequest.getEmail());
            signUpResponse.setServiceType(serviceType.getName());
            signUpResponse.setSuccessPage(RedirectService.defaultService().replacedRedirectUrl(successPage));
            signUpResponse.setSkipPopup(skipPopup);
            for (ActionError error : result.getErrors()) {
                signUpResponse.addErrorMessage(error.getType(), error.getDescription());
            }

            writeResponseData(response, signUpResponse);
        } catch (FDResourceException e) {
            CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
            session.setAttribute(SessionName.SIGNUP_SUCCESS, false);
            BaseJsonServlet.returnHttpError(500, "Failed to register customer with email: " + signUpRequest.getEmail(), e);
        } catch (FDInvalidAddressException e) {
            CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
            session.setAttribute(SessionName.SIGNUP_SUCCESS, false);
            BaseJsonServlet.returnHttpError(500, "Failed to register customer with email: " + signUpRequest.getEmail(), e);
        }
    }

    private EnumServiceType checkServiceStatus(EnumServiceType serviceType, ActionResult result, FDDeliveryServiceSelectionResult serviceResult) {
        EnumDeliveryStatus status = serviceResult.getServiceStatus(serviceType);
        if (!EnumDeliveryStatus.DELIVER.equals(status) && !EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status) && !EnumDeliveryStatus.COS_ENABLED.equals(status)) {
            result.addWarning(new ActionWarning("serviceType", "Selected service type is not available for current area, go with pickup type"));
            serviceType = EnumServiceType.PICKUP;
        }
        return serviceType;
    }

    private SignUpService selectSignUpService(EnumServiceType serviceType) {
        SignUpService registrationService;
        switch (serviceType) {
            case CORPORATE:
                registrationService = CorporateSignUpService.getInstance();
                break;

            case HOME:
            case PICKUP:
                registrationService = HomeSignUpService.getInstance();
                break;

            default:
                registrationService = HomeSignUpService.getInstance();
                break;
        }
        return registrationService;
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

}
