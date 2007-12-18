/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap;

/**
 * Utility class to deal with conversion between indexes and SAP posex representation. 
 *
 * @version $Revision$
 * @author $Author$
 */
public class PosexUtil {

	private final static int INCREMENT = 100;

	private PosexUtil() {
	}

	public static int getPosexInt(int i) {
		return (i + 1) * INCREMENT;
	}

	public static String getPosex(int i) {
		return zeroPad(6, Integer.toString((i + 1) * INCREMENT));
	}

	public static String getLongPosex(int i) {
		return zeroPad(8, Integer.toString((i + 1) * INCREMENT));
	}

	public static int getIndexFromPosex(String posex) throws NumberFormatException {
		int raw = Integer.parseInt(posex);
		if (raw % INCREMENT != 0) {
			throw new NumberFormatException("Posex " + posex + " not a multiple of increment " + INCREMENT);
		}
		return (raw / INCREMENT) - 1;
	}

	private static String zeroPad(int len, String str) {
		StringBuffer buf = new StringBuffer(len);
		int max = len - str.length();
		if (max < 0) {
			throw new IllegalArgumentException("String is longer than pad length");
		}
		for (int i = 0; i < max; i++) {
			buf.append('0');
		}
		buf.append(str);
		return buf.toString();
	}

}