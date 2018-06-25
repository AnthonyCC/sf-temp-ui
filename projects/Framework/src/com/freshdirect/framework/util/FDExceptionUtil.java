package com.freshdirect.framework.util;

import org.apache.commons.lang.exception.ExceptionUtils;

public class FDExceptionUtil {

	public static String getRootCauseStackTrace(Throwable e) {

		StringBuffer strBuf = new StringBuffer();
		if(e != null) {
			String[] traces = ExceptionUtils.getRootCauseStackTrace(e);
			if (traces != null) {
				for (final String element : ExceptionUtils.getRootCauseStackTrace(e)) {
					strBuf.append(element).append(" ");
				}
			}
		}
		return strBuf.toString();
	}

    public static boolean isConnectionResetOrBrokenPipe(String exceptionText) {
        boolean relatedException = false;

        if (exceptionText != null) {
            String upperExceptionText = exceptionText.toUpperCase();
            relatedException = (upperExceptionText.contains("JSPEXCEPTION") || upperExceptionText.contains("SOCKETEXCEPTION"))
                    && (upperExceptionText.contains("BROKEN PIPE") || upperExceptionText.contains("CONNECTION RESET"));
        }

        return relatedException;

    }

    public static boolean isCheckoutPaymentError(String exceptionText) {
        boolean relatedException = false;

        if (exceptionText != null) {
            String upperExceptionText = exceptionText.toUpperCase();
            relatedException = upperExceptionText.contains("Your payment method was declined. Please select a different payment method for this order and contact your card issuer for clarification.".toUpperCase())
                    || upperExceptionText.contains("Your timeslot reservation has expired, please go back and choose another timeslot.".toUpperCase()) 
                    || upperExceptionText.contains("Funding Instrument In The PayPal Account Was Declined By The Processor Or Bank, Or It Can't Be Used For This Payment".toUpperCase())
                    || upperExceptionText.contains("For your protection, FreshDirect will not accept orders over $1500 online".toUpperCase())
                    || upperExceptionText.contains("Your payment method appears to be expired or inactive".toUpperCase());
        }

        return relatedException;

    }

}
