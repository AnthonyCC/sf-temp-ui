package com.freshdirect.fdstore;

import java.util.Calendar;
import java.util.Date;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

public class OncePerRequestDateCache {
	
	private static ThreadLocal availabilityHorizon = new ThreadLocal();
	private static ThreadLocal today = new ThreadLocal();
	
	private OncePerRequestDateCache(){
		
	}
	
	public static void init(){
		Calendar startCal = DateUtil.truncate(Calendar.getInstance());
		today.set(startCal.getTime());
		
		Calendar endCal = (Calendar) startCal.clone();
		
		startCal.add(Calendar.DATE, 1);
		endCal.add(Calendar.DATE, ErpServicesProperties.getHorizonDays());
		
		availabilityHorizon.set(new DateRange(startCal.getTime(), endCal.getTime()));
	}
	
	public static Date getToday() {
		return (Date)today.get();
	}
	
	public static DateRange getAvailabilityHorizon() {
		return (DateRange) availabilityHorizon.get();
	}
	
	public static void clear(){
		today.set(null);
		availabilityHorizon.set(null);
	}

}
