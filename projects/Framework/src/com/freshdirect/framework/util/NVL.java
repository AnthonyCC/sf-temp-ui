/*
 * $Workfile: NVL.java$
 *
 * $Date: 11/8/2001 12:45:03 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

/**
 * Utility class for treating null values, just like the NVL function in SQL.
 *
 * @version $Revision: 1$
 * @author $Author: Viktor Szathmary$
 */
public class NVL<X> {

	private X defaultObj;

	public NVL(X defaultObj) {
		this.defaultObj = defaultObj;
	}
	
	public X apply(X obj) {
		return obj == null ? this.defaultObj : obj;
	}

	public static boolean nullEquals(Object a, Object b) {
		return (a==null && b==null) || (a!=null && a.equals(b));
	}

	public static <X> X apply(X obj, X defaultObj) {
		return obj==null ? defaultObj : obj;
	}
	
	public static String apply(String obj, String defaultObj) {
		return obj==null ? defaultObj : obj;
	}

	public static Integer apply(Integer obj, Integer defaultObj) {
		return obj==null ? defaultObj : obj;
	}

	public static Double apply(Double obj, Double defaultObj) {
		return obj==null ? defaultObj : obj;
	}

}