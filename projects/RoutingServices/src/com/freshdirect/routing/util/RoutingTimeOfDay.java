package com.freshdirect.routing.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.framework.util.TimeOfDay;

public final class RoutingTimeOfDay extends TimeOfDay {
	
	private static SimpleDateFormat	FORMATTER			= new SimpleDateFormat( "hh:mm a" );
		
	public RoutingTimeOfDay() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoutingTimeOfDay(Date date) {
		super(date);
		// TODO Auto-generated constructor stub
	}

	public RoutingTimeOfDay(String time, String format) {
		super(time, format);
		// TODO Auto-generated constructor stub
	}

	public RoutingTimeOfDay(String time) {
		super(time);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object o) {		
		if (o instanceof RoutingTimeOfDay) {
			return this.getNormalDate().equals(((RoutingTimeOfDay) o).getNormalDate());
		} else {
			throw new RuntimeException();
		}
		//return false;
	}
	
	@Override
	public int hashCode() {
		return this.getNormalDate().hashCode();
	}
	
	public String getTimeString() {
		return FORMATTER.format(this.getNormalDate());
	}
}
