package com.freshdirect.transadmin.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.freshdirect.transadmin.model.UPSRouteInfo;
import com.freshdirect.transadmin.service.DispatchManagerI;

public class UPSDataCacheManager 
{
	private List<UPSRouteInfo> routeData = null;
	private static UPSDataCacheManager instance=null;
	public static Calendar clientCalendar = Calendar.getInstance();
	private long expired = -1;
	
	private UPSDataCacheManager() {

	}

	public static UPSDataCacheManager getInstance() {
		if (instance == null) {
			instance = new UPSDataCacheManager();
		}
		return instance;
	}
	
	public List<UPSRouteInfo> getData(DispatchManagerI mgr) {
		long currentTime = System.currentTimeMillis();
		if (expired < currentTime) {
			loadData(mgr, currentTime);
		}
		return routeData;
	}
	public void loadData(DispatchManagerI mgr, long currentTime) {
		if (isToday(new Date(currentTime))) {
			try {
				routeData = mgr.getUPSRouteInfo(new Date(currentTime));
			} catch (Exception e) {
				e.printStackTrace();
				expired = -1;
			}
		}
	}
	public  boolean isToday(Date date){
		try {
			clientCalendar.setTimeInMillis(System.currentTimeMillis());
			clientCalendar.set(Calendar.HOUR, 5);
			clientCalendar.set(Calendar.MINUTE, 0);
			clientCalendar.set(Calendar.SECOND, 0);
			clientCalendar.set(Calendar.MILLISECOND, 0);
			clientCalendar.set(Calendar.AM_PM, Calendar.AM);
			long todayStartTime = clientCalendar.getTimeInMillis();
			clientCalendar.set(Calendar.HOUR, 3);
			clientCalendar.set(Calendar.MINUTE, 0);
			clientCalendar.set(Calendar.SECOND, 0);
			clientCalendar.set(Calendar.MILLISECOND, 0);
			clientCalendar.set(Calendar.AM_PM, Calendar.AM);
			clientCalendar.add(Calendar.DATE, 1);
			clientCalendar.add(Calendar.MILLISECOND, -1);
			long todayEndTime = clientCalendar.getTimeInMillis();
			long toCalculate = date.getTime();

			if (todayStartTime <= toCalculate && toCalculate <= todayEndTime) {
				expired = todayEndTime;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
