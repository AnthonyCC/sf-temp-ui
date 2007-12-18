package com.freshdirect.framework.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Represent a time of day at a one minute precision.
 */
public class TimeOfDay implements Comparable, Serializable {

	private final static GregorianCalendar EPOCH = new GregorianCalendar(1970, 0, 1, 0, 0, 0);
	public final static TimeOfDay MIDNIGHT = new TimeOfDay(0, 0);
	public final static TimeOfDay NEXT_MIDNIGHT = new TimeOfDay(24, 0);

	private static SimpleDateFormat FORMATTER = new SimpleDateFormat("hh:mm a");

	/** Normalized to 1970-01-01, only time portion relevant */
	private Date normalDate;

	public TimeOfDay(){}
	
	public TimeOfDay(String time) {
		this.normalDate = convert(time, FORMATTER);
		if (this.after(NEXT_MIDNIGHT)) {
			throw new IllegalArgumentException("More than a day");
		}
	}
	
	public TimeOfDay(String time, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		this.normalDate = convert(time, sf);
		if (this.after(NEXT_MIDNIGHT)) {
			throw new IllegalArgumentException("More than a day");
		}
	}

	private TimeOfDay(int hours, int minutes) {
		Calendar c = DateUtil.truncate(EPOCH);
		c.add(Calendar.HOUR_OF_DAY, hours);
		c.add(Calendar.MINUTE, minutes);
		this.normalDate = c.getTime();
	}

	public TimeOfDay(Date date) {
		this(convert(date));
	}
	
	public Date getNormalDate() {
		return normalDate;
	}

	public void setNormalDate(Date normalDate) {
		this.normalDate = normalDate;
	}

	public Date getAsDate() {
		return this.normalDate;
	}

	/**
	 * Get this time of day on the specified day.
	 * 
	 * @param day base date (never null)
	 * @return date/time
	 */
	public Date getAsDate(Date day) {
		Calendar timeDate = getAsCalendar(day);
		return timeDate.getTime();
	}

	/**
	 * Get this time of day on the specified day.
	 * 
	 * @param day base date (never null)
	 * @return date/time as Calendar
	 */
	public Calendar getAsCalendar(Date day) {
		Calendar requestedDate = Calendar.getInstance();
		requestedDate.setTime(day);

		Calendar timeDate = Calendar.getInstance();
		timeDate.setTime(this.normalDate);
		timeDate.set(Calendar.MONTH, requestedDate.get(Calendar.MONTH));
		timeDate.set(Calendar.DATE, requestedDate.get(Calendar.DATE));
		timeDate.set(Calendar.YEAR, requestedDate.get(Calendar.YEAR));

		if (this.equals(NEXT_MIDNIGHT)) {
			timeDate.add(Calendar.DATE, 1);
		}
		return timeDate;
	}

	public String getAsString() {
		return convert(this.normalDate);
	}

	public double getAsHours() {
		return this.normalDate.getTime() / (double) (1000 * 60 * 60);
	}

	public String toString() {
		return this.getAsString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		return this.normalDate.compareTo(((TimeOfDay) o).normalDate);
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof TimeOfDay) {
			return this.normalDate.equals(((TimeOfDay) o).normalDate);
		}
		return false;
	}

	public boolean before(TimeOfDay time) {
		return this.normalDate.before(time.normalDate);
	}

	public boolean after(TimeOfDay time) {
		return this.normalDate.after(time.normalDate);
	}

	public static double getDurationAsHours(TimeOfDay start, TimeOfDay end) {
		if (start.after(end)) {
			return 24.0 - start.getAsHours() - end.getAsHours();
		}
		return end.getAsHours() - start.getAsHours();
	}

	/*private static Date convert(String time) {
		try {
			synchronized (FORMATTER) {
				return FORMATTER.parse(time);
			}
		} catch (ParseException pe) {
			throw new IllegalArgumentException(pe.getMessage());
		}
	}*/
	
	private static Date convert(String time, SimpleDateFormat format) {
		try {
			synchronized (format) {
				return format.parse(time);
			}
		} catch(ParseException pe) {
			throw new IllegalArgumentException(pe.getMessage());
		}
	}

	private static String convert(Date time) {
		synchronized (FORMATTER) {
			return FORMATTER.format(time);
		}
	}

}