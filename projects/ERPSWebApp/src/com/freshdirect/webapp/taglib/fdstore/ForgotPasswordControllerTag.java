package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.mail.EmailUtil;

public class ForgotPasswordControllerTag extends BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ForgotPasswordControllerTag.class);

	private final static String URI_LINK_EXPIRED = "/login/link_expired.jsp";
	private final static String URI_HOME = "/index.jsp";

	private final static String MSG_INVALID_EMAIL =
		"Invalid or missing email address. If you need assistance please call us at 1-866-283-7374.";
	private final static String MSG_EMAIL_NOT_EXPIRED = "An email was already sent. Please try again later.";
	private final static String MSG_INVALID_PASSWORD = "Invalid or missing Password.";

	private String results;
	private String successPage;
	private String password;

	private String hint;
	private String altEmail;

	private String email;
	private String newPassword;
	private String confirmNewPassword;

	public void setSuccessPage(String sp) {
		this.successPage = sp;
	}

	public void setResults(String resultName) {
		this.results = resultName;
	}

	public void setPassword(String ps) {
		this.password = ps;
	}

	public int doStartTag() throws JspException {
		//
		// perform any actions requested by the user if the request was a POST
		//
		ActionResult result = new ActionResult();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		HttpSession session = pageContext.getSession();

		String passStep = request.getParameter("passStep");

		this.email = request.getParameter("email");
		String link = request.getParameter("link");

		LOGGER.debug("email= " + email + " link= " + link + " passStep= " + passStep);

		if (!"POST".equalsIgnoreCase(request.getMethod())) {

			if (link != null) {
				LOGGER.debug("Attempting to do isLinkExpired");
				if (this.isLinkExpired(email, link)) {
					this.doRedirect(URI_LINK_EXPIRED);
				}
			}

		} else {

			if (passStep.equals("sendUrl")) {
				//
				// Email link to user
				//
				this.performSendUrl(result, request);

			} else if (passStep.equals("checkHint")) {
				//
				// Check if hint answer is correct
				//
				if (isLinkExpired(email, link)) {
					this.doRedirect(URI_LINK_EXPIRED);
				} else {
					performCheckHint(result, request);
				}

			} else if (passStep.equals("changePassword")) {
				//
				// changes user password
				//
				this.performChangePassword(result, request, response, session);
			}
		}

		// place the result as a scripting variable in the page
		pageContext.setAttribute(this.results, result);
		return EVAL_BODY_BUFFERED;
	}

	private void performCheckHint(ActionResult result, HttpServletRequest request) {
		getHintData(request);
		validateHintInput(result);
		LOGGER.debug("Validating: " + hint + " for " + email);
		if (result.isSuccess()) {
			try {
				if (FDCustomerManager.isCorrectPasswordHint(email, hint)) {
					pageContext.setAttribute(this.password, "true");

				} else {
					LOGGER.debug("Hint not Valid");
					result.addError(new ActionError("invalid_hint", SystemMessageList.MSG_INVALID_HINT));
				}

			} catch (FDResourceException ex) {
				result.addError(new ActionError("invalid_hint", SystemMessageList.MSG_INVALID_HINT));
				LOGGER.warn("Failed to locate customer", ex);

			} catch (ErpFraudException ex) {
				//
				// Number of unsuccessful guesses > 5
				//
				result.addError(new ActionError("numberOfAttempts", 
	            		MessageFormat.format(SystemMessageList.MSG_NUMBER_OF_ATTEMPTS, 
	            		new Object[] { UserUtil.getCustomerServiceContact(request)})));
				LOGGER.warn("Exceeded allowed number of guesses", ex);
			}
		}
	}

	private void performChangePassword(
		ActionResult result,
		HttpServletRequest request,
		HttpServletResponse response,
		HttpSession session) {

		this.getNewPasswordData(request);
		this.validateNewPasswordData(result);
		LOGGER.debug("Checked");

		if (result.isSuccess()) {
			try {
				FDCustomerManager.changePassword(AccountActivityUtil.getActionInfo(session), email, newPassword);
				LOGGER.debug("Password has been changed");

				FDIdentity identity = FDCustomerManager.login(email, newPassword);

				FDUser user = FDCustomerManager.recognize(identity);

				UserUtil.createSessionUser(request, response, user);

				doRedirect(URI_HOME);

			} catch (ErpInvalidPasswordException ex) {
				LOGGER.warn("new password too short", ex);
				result.addError(new ActionError("password", SystemMessageList.MSG_PASSWORD_LENGTH));
				pageContext.setAttribute(this.password, "true");

			} catch (FDResourceException ex) {
				result.addError(new ActionError("passwordError", SystemMessageList.MSG_SYSTEM_ERROR));
				LOGGER.warn("Failed to Change Password", ex);

			} catch (FDAuthenticationException ex) {
				result.addError(new ActionError("passwordError", SystemMessageList.MSG_SYSTEM_ERROR));
				LOGGER.warn("Failed to Login", ex);
			}

		} else {
			pageContext.setAttribute(this.password, "true");
		}
	}

	private void performSendUrl(ActionResult result, HttpServletRequest request) {
		getFormData(request);
		validateInput(result);
		if (result.isSuccess()) {
			try {

				LOGGER.debug("Email is going to: " + email);
				FDCustomerManager.sendPasswordEmail(email, altEmail != null);
				LOGGER.debug("Success, redirecting to: " + successPage);
				this.doRedirect(this.successPage);

			} catch (FDResourceException ex) {
				result.addError(new ActionError("invalid_email", MSG_INVALID_EMAIL));
				LOGGER.warn("Failed to locate customer", ex);

			} catch (PasswordNotExpiredException pe) {
				result.addError(new ActionError("email_not_expired", MSG_EMAIL_NOT_EXPIRED));
				LOGGER.warn("Link has not Expired yet", pe);
			}

		}
	}

	private void getFormData(HttpServletRequest request) {
		this.email = request.getParameter("email");
		this.altEmail = request.getParameter("altEmail");
	}

	private void validateInput(ActionResult result) {
		if (email == null || email.length() < 1) {
			result.addError(new ActionError("email", MSG_INVALID_EMAIL));
		}

		if (!EmailUtil.isValidEmailAddress(email)) {
			result.addError(new ActionError("email", MSG_INVALID_EMAIL));
		}

	}

	private void getHintData(HttpServletRequest request) {
		this.email = request.getParameter("email");
		this.hint = request.getParameter("hint");
	}

	private void validateHintInput(ActionResult result) {
		if (hint == null || hint.length() < 1) {
			result.addError(new ActionError("hint", SystemMessageList.MSG_INVALID_HINT));
		}
	}

	private void getNewPasswordData(HttpServletRequest request) {
		this.email = request.getParameter("email");
		this.newPassword = request.getParameter("newPassword");
		this.confirmNewPassword = request.getParameter("confirmNewPassword");
	}

	private void validateNewPasswordData(ActionResult result) {
		if (newPassword == null || newPassword.length() < 1) {
			result.addError(new ActionError("newPassword", MSG_INVALID_PASSWORD));
		}
		if (!newPassword.equals(confirmNewPassword)) {
			result.addError(new ActionError("passwordMissmatch", SystemMessageList.MSG_PASSWORD_REPEAT));
		}
	}

	private boolean isLinkExpired(String email, String link) {
		try {
			return FDCustomerManager.isPasswordRequestExpired(email, link);
		} catch (FDResourceException fdre) {
			LOGGER.warn("Failed on Expiration Portion", fdre);
			return true;
		}
	}

	private void doRedirect(String url) {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			response.sendRedirect(response.encodeRedirectURL(url));
			JspWriter writer = pageContext.getOut();
			writer.close();
		} catch (IOException ioe) {
			//   throw new JspException(ioe.getMessage());
		}
	}

}
