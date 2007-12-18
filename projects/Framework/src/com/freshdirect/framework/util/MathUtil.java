package com.freshdirect.framework.util;

public class MathUtil {

	private MathUtil() {
	}

	/** Round a double to two decimals */
	public static double roundDecimal(double value) {
		return (double) (Math.round(value * 100)) / 100;
	}

}
