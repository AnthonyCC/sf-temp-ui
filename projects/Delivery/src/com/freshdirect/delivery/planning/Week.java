package com.freshdirect.delivery.planning;


import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.Serializable;

import com.freshdirect.framework.util.QuickDateFormat;

public class Week implements Serializable {
	
	private final SimpleDateFormat SF = new SimpleDateFormat("MMM dd");
	private Date weekStart;
	private Date weekEnd;
	
	public Week() {
		this(new Date());
	}

	public Week(Date weekStart){
		this.weekStart = weekStart;
		Calendar cal = Calendar.getInstance();
		cal.setTime(weekStart);
		cal.add(Calendar.DATE, 6);
		this.weekEnd = cal.getTime();
	}
	
	public Date getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(Date weekStart) {
		this.weekStart = weekStart;
	}

	public Date getWeekEnd() {
		return weekEnd;
	}

	public void setWeekEnd(Date weekEnd) {
		this.weekEnd = weekEnd;
	}
	
	public Date[] getDays(){
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(weekStart);
		
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		startCal.set(Calendar.MILLISECOND, 0);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(weekEnd);
		
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);
		Date[] days = new Date[7];
		int idx = 0;
		while(startCal.before(endCal) || startCal.equals(endCal)){
			days[idx++] = startCal.getTime();
			startCal.add(Calendar.DATE, 1);
		}
		
		return days;
	}
	
	public String toString(){
		return SF.format(weekStart) +" - "+SF.format(weekEnd);
	}
	
	public boolean equals(Object o){
		if(!(o instanceof Week)){
			return false;
		}
		QuickDateFormat qf = new QuickDateFormat(QuickDateFormat.FORMAT_SHORT_DATE);
		return (qf.format(((Week)o).weekStart)).equals(qf.format(this.weekStart));
	}

}