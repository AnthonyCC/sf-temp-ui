package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility for classifying non-cookied users where no Cohort information is
 * available. Generates a hash based on User-Agent. Same hash code is generated
 * for the same user. Ideal for supporting (non Cohort-based) partial roll out
 * of features.
 * 
 * @author tgelesz
 * 
 */
public class RequestClassifier {

	private String userAgent;
	private int hashPercent;

	public RequestClassifier(HttpServletRequest request) {
		userAgent = request.getHeader("User-Agent");
		hashPercent = userAgent.hashCode() % 100;
		if (hashPercent < 0) {
			hashPercent += 100;
		}
	}

	/**
	 * Function for supporting partial roll out.
	 * 
	 * @param enablePercent percentage of users whom the feature is rolled out
	 */
	public boolean isInHashRange(int enablePercent) {
		return hashPercent < enablePercent;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public int getHashPercent() {
		return hashPercent;
	}

}