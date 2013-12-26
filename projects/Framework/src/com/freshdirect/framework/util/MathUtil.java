package com.freshdirect.framework.util;

import java.math.BigDecimal;

public class MathUtil {

	private MathUtil() {
	}

	/** Round a double to two decimals */
	public static double roundDecimal(double value) {
		return (double) (Math.round(value * 100)) / 100;
	}

	public static double roundDecimalCeiling(double value) {
		BigDecimal bd =new BigDecimal(value);
		bd =bd.setScale(2,BigDecimal.ROUND_CEILING);
		return bd.doubleValue();
	}
}
