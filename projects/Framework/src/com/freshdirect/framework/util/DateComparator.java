/*
 * $Workfile: DateComparator.java$
 *
 * $Date: 9/10/2001 6:32:43 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util;

import java.util.Date;
import java.util.Comparator;

/**
 * Comparator for Dates, with variable precision.
 * <B>Warning:</B> this class tells if the two dates are within a certain range (eg 24 hours), not that they are on the same day!
 *
 * @version $Revision: 3$
 * @author $Author: Viktor Szathmary$
 */
public class DateComparator implements Comparator {
	
	public final static int PRECISION_SECOND = 0;
	public final static int PRECISION_MINUTE = 1;
	public final static int PRECISION_HOUR = 2;
	public final static int PRECISION_DAY = 3;
	
	private final static long SECOND = 1000;
	private final static long MINUTE = 60 * SECOND;
	private final static long HOUR = 60 * MINUTE;
	private final static long DAY = 24 * HOUR;
	
	private final long range;
	
	public DateComparator(int precision) {
		this.range = DateComparator.getRange(precision);
	}
	
	/**
	 * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer
	 * as the first argument is less than, equal to, or greater than the second.
	 */
	public int compare(Object o1, Object o2) {
		return DateComparator.compare( this.range, ((Date)o1).getTime(), ((Date)o2).getTime() );
	}

	public static int compare(int precision, Date d1, Date d2) {
		return DateComparator.compare(
			DateComparator.getRange(precision),
			d1.getTime(),
			d2.getTime() );
	}

	private static int compare(long range, long t1, long t2) {
		long diff = t2 - t1;
		
		if (diff==0) {
			return 0;
		}
		
		if (diff < range) {
			if ( diff > -range ) {
				return 0;
			}
			return 1;
		} else {
			if ( diff < -range ) {
				return 0;	
			}
			return -1;
		}
	}

	private static long getRange(int precision) {
		switch (precision) {
			case PRECISION_SECOND:
				return SECOND;
			case PRECISION_MINUTE:
				return MINUTE;
			case PRECISION_HOUR:
				return HOUR;
			case PRECISION_DAY:
				return DAY;
			default:
				throw new IllegalArgumentException();
		}
	}


	
	
}
