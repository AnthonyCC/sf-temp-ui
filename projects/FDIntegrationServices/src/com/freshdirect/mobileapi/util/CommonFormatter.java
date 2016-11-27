package com.freshdirect.mobileapi.util;

import com.freshdirect.webapp.util.JspMethods;

public class CommonFormatter {

	public static String formatCurrency(double amount) {
		return JspMethods.formatPrice(amount);
	}
}
