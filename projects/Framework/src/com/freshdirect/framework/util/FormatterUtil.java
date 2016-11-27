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
	
	public static double formatDecimal(double number) {
		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		String strNumber = decimalFormat.format(number);
		strNumber = strNumber.replaceAll(",", ".");
		Double numberDouble = new Double(strNumber);
		return numberDouble.doubleValue();
	}
	public static double formatFourDecimal(double number) {
		DecimalFormat decimalFormat = new DecimalFormat("0.####");
		String strNumber = decimalFormat.format(number);
		strNumber = strNumber.replaceAll(",", ".");
		Double numberDouble = new Double(strNumber);
		return numberDouble.doubleValue();
	}
}
