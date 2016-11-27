package com.freshdirect.framework.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

/**
 * Unmutable set of days-of-week. 
 */
public class DayOfWeekSet implements Serializable {

	public final static DayOfWeekSet EMPTY = new DayOfWeekSet();

	private final static String[] DAY_CODES = { "1", "2", "3", "4", "5", "6", "7" };
	private final static String[] NAMES = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
	private final static String[] PLURALS = { "Sundays", "Mondays", "Tuesdays", "Wednesdays", "Thursdays", "Fridays", "Saturdays" };

	/** SortedSet of Integer */
	private final SortedSet daysOfWeek;

	private DayOfWeekSet() {
		this(new TreeSet());
	}

	private DayOfWeekSet(SortedSet daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public DayOfWeekSet(int[] daysOfWeek) {
		this();
		for (int i = 0; i < daysOfWeek.length; i++) {
			this.add(daysOfWeek[i]);
		}
	}

	public int size() {
		return this.daysOfWeek.size();
	}

	public boolean isEmpty() {
		return this.daysOfWeek.isEmpty();
	}

	public boolean equals(Object o) {
		if (o instanceof DayOfWeekSet) {
			return this.daysOfWeek.equals(((DayOfWeekSet) o).daysOfWeek);
		}
		return false;
	}

	public DayOfWeekSet union(DayOfWeekSet other) {
		if (other.isEmpty()) {
			return this;
		}
		if (this.isEmpty()) {
			return other;
		}
		SortedSet s = new TreeSet(this.daysOfWeek);
		s.addAll(other.daysOfWeek);
		return new DayOfWeekSet(s);
	}

	public List translate(Object[] lookupTable) {
		if (lookupTable == null || lookupTable.length != 7) {
			throw new IllegalArgumentException();
		}
		if (this.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List l = new ArrayList(this.size());
		for (Iterator i = this.daysOfWeek.iterator(); i.hasNext();) {
			Integer dow = (Integer) i.next();
			l.add(lookupTable[dow.intValue() - 1]);
		}
		return l;
	}

	public DayOfWeekSet inverted() {
		DayOfWeekSet dows = new DayOfWeekSet();
		for (int i = 1; i < 8; i++) {
			if (!this.daysOfWeek.contains(new Integer(i))) {
				dows.add(i);
			}
		}
		return dows;
	}

	public String format(boolean plural) {
		return StringUtils.join(this.translate(plural ? PLURALS : NAMES).iterator(), ", ");
	}

	private void add(int dayOfWeek) {
		if (dayOfWeek < 1 || dayOfWeek > 7) {
			throw new IllegalArgumentException("Invalid day of week " + dayOfWeek);
		}
		this.daysOfWeek.add(new Integer(dayOfWeek));
	}

	/**
	 * @return String representation such as "12345". 
	 */
	public String encode() {
		if (this.isEmpty()) {
			return "";
		}
		return StringUtils.join(this.translate(DAY_CODES).iterator(), "");
	}

	/**
	 * Decode a string such as "12345" as DayOfWeekSet.
	 * 
	 * @return DayOfWeekSet.EMPTY if empty
	 * @throws IllegalArgumentException if contains non-numeric characters
	 */
	public static DayOfWeekSet decode(String string) {
		if (string == null || string.trim().length() == 0) {
			return DayOfWeekSet.EMPTY;
		}
		char[] chars = string.trim().toCharArray();
		int[] days = new int[chars.length];
		for (int i = 0; i < chars.length; i++) {
			days[i] = Character.getNumericValue(chars[i]);
		}
		return new DayOfWeekSet(days);
	}

	public String toString() {
		return "DayOfWeekSet" + this.daysOfWeek;
	}

}
