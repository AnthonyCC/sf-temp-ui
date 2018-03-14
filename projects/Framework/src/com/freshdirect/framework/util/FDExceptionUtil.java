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
}
