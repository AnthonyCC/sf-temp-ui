package com.freshdirect.transadmin.web.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.framework.util.TimeOfDay;

public class CustomTimeOfDay extends TimeOfDay {
	
	private static SimpleDateFormat	FORMATTER			= new SimpleDateFormat( "hh:mm a" );
	
	public CustomTimeOfDay() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomTimeOfDay(Date date) {
		super(date);
		// TODO Auto-generated constructor stub
	}

	public CustomTimeOfDay(String time, String format) {
		super(time, format);
		// TODO Auto-generated constructor stub
	}

	public CustomTimeOfDay(String time) {
		super(time);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof TimeOfDay) {
			return this.getNormalDate().equals(((TimeOfDay) o).getNormalDate());
		}
		return false;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public int hashCode(Object o) {
		if (o instanceof TimeOfDay) {
			return this.getNormalDate().hashCode();
		}
		return 1;
	}
	
	public String getTimeString() {
		return FORMATTER.format(this.getNormalDate());
	}
}
