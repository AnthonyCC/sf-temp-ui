package com.freshdirect.transadmin.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserUtil {
	
	private static DateFormat MONTH_DATE_YEAR_FORMATER = new SimpleDateFormat("MM/dd/yyyy");
	private static DateFormat YEAR_MONTH_DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd");
	
	private String removeLeadingZeros(String str) {
		return str != null ? str.replaceFirst("^0+(?!$)", "") : null;
	}

	private static Date parseDate(String dateStr) {
		if (!isEmpty(dateStr)) {
			try {
				return MONTH_DATE_YEAR_FORMATER.parse(dateStr);
			} catch (ParseException px) {
				try {
					return YEAR_MONTH_DATE_FORMATER.parse(dateStr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static boolean isEmpty(String str) {
		return str == null || (str != null && str.length() == 0)
				|| "null".equalsIgnoreCase(str);
	}

	public static String trim(String str) {
		return str != null ? str.trim() : null;
	}
	
	public static double parseDouble(String str) {
		return str != null && !isEmpty(str) ? Double.parseDouble(str
				.replaceAll("[^\\d-]+", "")) : 0.0;
	}

	public static int parseInt(String str) {
		return str != null && !isEmpty(str) ? Integer.parseInt(str.replaceAll(
				"[^\\d-]+", "")) : 0;
	}

}
