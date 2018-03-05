package com.freshdirect.webapp.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;

/*
 * @author: Nakkeeran Annamalai
 */
public class CaptchaUtil {

	private static final Logger LOGGER = Logger.getLogger(CaptchaUtil.class);
	
	private static final HttpClient HTTP_CLIENT = new HttpClient();
	public static boolean validateCaptcha(final ServletRequest request) {
		boolean isCaptchaSuccess = false;
		final PostMethod postMethod = new PostMethod();
		try {
			if (StringUtils.equals(request.getParameter("captchaVersion"), "1.0")) {
				isCaptchaSuccess = validateCaptchaV1(request);
			} else {
				isCaptchaSuccess = validateCaptchaV2(StringUtils.defaultString(request.getParameter("g-recaptcha-response")), request.getRemoteAddr());
			}
		} finally {
			try {
				postMethod.releaseConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isCaptchaSuccess;
	}

	public static boolean validateCaptchaV2(String captchaToken, String remoteIp){
		boolean isCaptchaSuccess = false;
		PostMethod postMethod = new PostMethod("https://www.google.com/recaptcha/api/siteverify");
		postMethod.addParameter("secret", FDStoreProperties.getRecaptchaPrivateKey());
		postMethod.addParameter("response", captchaToken);
		postMethod.addParameter("remoteip", remoteIp);
		try {
			int status = HTTP_CLIENT.executeMethod(postMethod);
			
			if (status == HttpServletResponse.SC_OK) {
				String response = postMethod.getResponseBodyAsString();
				isCaptchaSuccess= StringUtils.contains(response, "true");
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

	public static boolean validateCaptchaV1(final ServletRequest request) {
		boolean isCaptchaSuccess = false;
		PostMethod postMethod = new PostMethod("http://api-verify.recaptcha.net/verify");
		postMethod.addParameter("privatekey", FDStoreProperties.getRecaptchaPrivateKey());
		postMethod.addParameter("response", StringUtils.defaultString(request.getParameter("recaptcha_response_field")));
		postMethod.addParameter("challenge", StringUtils.defaultString(request.getParameter("recaptcha_challenge_field")));
		postMethod.addParameter("remoteip", request.getRemoteAddr());
		try {
			HTTP_CLIENT.executeMethod(postMethod);
			String response = postMethod.getResponseBodyAsString();
			String[] a = response.split("\r?\n");
			if (a.length < 1) {
				isCaptchaSuccess = false;
			}
			isCaptchaSuccess = "true".equals(a[0]);
		} catch (Exception e) {
			LOGGER.error("Error connecting to google captcha", e);
		} finally {
			postMethod.releaseConnection();
		}
		return isCaptchaSuccess;
	}
}