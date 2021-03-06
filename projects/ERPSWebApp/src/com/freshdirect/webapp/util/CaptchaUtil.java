package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.enums.CaptchaType;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CaptchaUtil {

	private static final Logger LOGGER = Logger.getLogger(CaptchaUtil.class);
	
	private static final HttpClient HTTP_CLIENT = new HttpClient();

	public static boolean isExcessiveAttempt(int maxAttemptAllowed, HttpSession session, String sessionName) {
		int currentAttempt = session.getAttribute(sessionName) != null ? (Integer) session.getAttribute(sessionName)
				: Integer.valueOf(0);
		return maxAttemptAllowed != 0 && currentAttempt >= maxAttemptAllowed;
	}

	public static boolean validateCaptcha(String captchaToken, String remoteIp, CaptchaType captchaType, HttpSession session, String sessionName, int maxAttemptAllowed) {
		if (!isExcessiveAttempt(maxAttemptAllowed, session, sessionName)) {
			return true;
		}
		// captcha widget is not loaded or the validation is not needed
		if (captchaToken == null) {
			EnumTransactionSource source = null;
			if (session.getAttribute(SessionName.APPLICATION) != null) {
				source = EnumTransactionSource.getTransactionSource(session.getAttribute(SessionName.APPLICATION).toString());
			} else if (session.getAttribute(SessionName.USER) != null) {
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				source = user.getApplication();
			}
			
			boolean isWeb = source == EnumTransactionSource.WEBSITE || source == EnumTransactionSource.FOODKICK_WEBSITE;
			return !FDStoreProperties.isCaptchaMandatory(captchaType, isWeb);
		}
		// captcha widget is loaded but user did not respond to the captcha
		if ( captchaToken.isEmpty()) {
			return false;
		}

		boolean isCaptchaSuccess = false;
		PostMethod postMethod = new PostMethod("https://www.google.com/recaptcha/api/siteverify");
		postMethod.addParameter("secret", FDStoreProperties.getRecaptchaPrivateKey(captchaType));
		postMethod.addParameter("response", captchaToken);
		postMethod.addParameter("remoteip", remoteIp);
		try {
			int status = HTTP_CLIENT.executeMethod(postMethod);

			if (status == HttpServletResponse.SC_OK) {
				String response = postMethod.getResponseBodyAsString();
				isCaptchaSuccess = StringUtils.contains(response, "true");
			}

		} catch (Exception e) {
			LOGGER.error("Error connecting to google captcha", e);
			// if service is down, do not block the workflow
			isCaptchaSuccess = true;
			
		} finally {
			postMethod.releaseConnection();
		}
		return isCaptchaSuccess;
	}
	
	public static int increaseAttempt(HttpServletRequest request, String name) {
		return increaseAttempt(request.getSession(), name);
	}
	
	public static int increaseAttempt(HttpSession session, String name) {
		Integer fdLoginAttempt = session.getAttribute(name) != null ? (Integer) session.getAttribute(name) : Integer.valueOf(0);
		fdLoginAttempt++;
		session.setAttribute(name, fdLoginAttempt);
		return fdLoginAttempt;
	}
	public static void resetAttempt(HttpServletRequest request, String name) {
		resetAttempt(request.getSession(), name);
	}
	
	public static void resetAttempt(HttpSession session, String name) {
		session.setAttribute(name, 0);
	}
}