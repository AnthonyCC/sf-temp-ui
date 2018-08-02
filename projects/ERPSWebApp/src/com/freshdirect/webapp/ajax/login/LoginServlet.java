package com.freshdirect.webapp.ajax.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.enums.CaptchaType;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.service.RedirectService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.CaptchaUtil;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.ValidationUtils;

public class LoginServlet extends BaseJsonServlet {

    private static final String MERGE_CART_PAGE_URI = "/login/merge_cart.jsp";
    private static final long serialVersionUID = 133867689987220127L;
    private static final Logger LOGGER = LoggerFactory.getInstance(LoginServlet.class);

    /**
     * This is an API to do the log-in based on the existing login logic(@LoginControllerTag.java).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user1) throws HttpErrorResponse {
        HttpSession session = request.getSession();
        LoginResponse loginResponse = new LoginResponse();
        LoginRequest loginRequest = BaseJsonServlet.parseRequestData(request, LoginRequest.class);
        String successPage = NVL.apply(loginRequest.getSuccessPage(), FDURLUtil.LANDING_PAGE);

        ActionResult actionResult = new ActionResult();

        // validate captcha if it's enabled
        boolean isCaptchaSuccess = CaptchaUtil.validateCaptcha(loginRequest.getCaptchaToken(), request.getRemoteAddr(), CaptchaType.SIGN_IN, session, SessionName.LOGIN_ATTEMPT,
                FDStoreProperties.getMaxInvalidLoginAttempt());
        if (!isCaptchaSuccess) {
            actionResult.addError(!isCaptchaSuccess, "captcha", SystemMessageList.MSG_INVALID_CAPTCHA);
        }

        ValidationUtils.validateEmailAddress(actionResult, "userId", loginRequest.getUserId());

        if (actionResult.isSuccess()) {
            String updatedSuccessPage = UserUtil.loginUser(session, request, response, actionResult, loginRequest.getUserId(), loginRequest.getPassword(), MERGE_CART_PAGE_URI,
                    successPage, false);
            loginResponse.setSuccessPage(RedirectService.defaultService().replacedRedirectUrl(updatedSuccessPage));
        }

        if (actionResult.isSuccess()) {
            // check T&C only if login success
            FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
            if (user != null && !user.getTcAcknowledge()) {
                loginResponse.setMessage("TcAgreeFail");
                session.setAttribute("fdTcAgree", false);
            }
            loginResponse.setSuccess(true);
            CaptchaUtil.resetAttempt(request, SessionName.LOGIN_ATTEMPT);
        } else {
            for (ActionError error : actionResult.getErrors()) {
                LOGGER.debug(error.getDescription());
                if (error.getDescription().equals(SystemMessageList.MSG_VOUCHER_REDEMPTION_FDX_NOT_ALLOWED)) {
                    loginResponse.setMessage("voucherredemption");
                    loginResponse.addError("voucherredemption",
                            "<div class=\"header\">This email is not valid for FoodKick orders.</div>Please register a new account to place a FoodKick order.");
                } else {
                    loginResponse.addError(error.getType(), error.getDescription());
                }
            }

            CaptchaUtil.increaseAttempt(request, SessionName.LOGIN_ATTEMPT);
            if (CaptchaUtil.isExcessiveAttempt(FDStoreProperties.getMaxInvalidLoginAttempt(), session, SessionName.LOGIN_ATTEMPT)) {
                // Should be the redirecting key to login page.
                loginResponse.setMessage("CaptchaRedirect");
            }
        }

        writeResponseData(response, loginResponse);
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
