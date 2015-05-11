package com.freshdirect.webapp.util;

import javax.servlet.ServletRequest;

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
	
	public static boolean validateCaptcha(final ServletRequest request) {
		boolean isCaptchaSuccess = false;
		final PostMethod postMethod = new PostMethod();
		postMethod.addParameter("remoteip", request.getRemoteAddr());
		if(StringUtils.equals(request.getParameter("captchaVersion"), "1.0")){
			isCaptchaSuccess = validateCaptchaV1(request,postMethod);
		} else {
			isCaptchaSuccess = validateCaptchaV2(request,postMethod);
		}
		return isCaptchaSuccess;
	}

	public static boolean validateCaptchaV2(final ServletRequest request,final PostMethod postMethod){
		boolean isCaptchaSuccess = false;
		final HttpClient client = new HttpClient();
		postMethod.setPath("https://www.google.com/recaptcha/api/siteverify");
		postMethod.addParameter("secret", FDStoreProperties.getRecaptchaPrivateKey());
		postMethod.addParameter("response", StringUtils.defaultString(request.getParameter("g-recaptcha-response")));
		try {
			client.executeMethod(postMethod);
			String response = postMethod.getResponseBodyAsString();
			if (StringUtils.contains(response, "true")) {
				isCaptchaSuccess = true;
			} else {
				LOGGER.error(response);
			}
		} catch (Exception e) {
			LOGGER.error("Error connecting to google captcha");
		}
		return isCaptchaSuccess;
	}

	public static boolean validateCaptchaV1(final ServletRequest request,final PostMethod postMethod) {
		boolean isCaptchaSuccess = false;
		postMethod.setPath("http://api-verify.recaptcha.net/verify");
		postMethod.addParameter("privatekey", FDStoreProperties.getRecaptchaPrivateKey());
		postMethod.addParameter("response", StringUtils.defaultString(request.getParameter("recaptcha_response_field")));
		postMethod.addParameter("challenge", StringUtils.defaultString(request.getParameter("recaptcha_challenge_field")));
		final HttpClient client = new HttpClient();
		try {
			client.executeMethod(postMethod);
			String response = postMethod.getResponseBodyAsString();
			String[] a = response.split("\r?\n");
			if (a.length < 1) {
				isCaptchaSuccess = false;
			}
			isCaptchaSuccess = "true".equals(a[0]);
		} catch (Exception e) {
			LOGGER.error("Error connecting to google captcha", e);
		}
		return isCaptchaSuccess;
	}
}