/*
 * Created on Aug 1, 2005
 */
package com.freshdirect.webapp.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SiteFeatureUtils {

	public static boolean isEnabled(EnumSiteFeature feature, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return false;
		}

		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		return feature.isEnabled(user);
	}
	
}
