package com.freshdirect.framework.util;

import java.text.DecimalFormat;

public class FormatterUtil {
	
	private static final DecimalFormat DECIMALFORMAT_TWODECIMAL = new DecimalFormat("0.00");
	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#,##,###,####");
	
	public static String formatToTwoDecimal(double number) {
		return DECIMALFORMAT_TWODECIMAL.format(number);
	}
	
	public static String formatToGrouping(double number) {
		return DECIMALFORMAT.format(number);
	}
}
