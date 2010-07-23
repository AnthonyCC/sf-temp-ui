package com.freshdirect.framework.util;

/**
 * 
 * @author ksriram
 *
 */
public class NumberUtil {

	public static boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	public static boolean isDouble(String i) {
		try {
			Double.parseDouble(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}
