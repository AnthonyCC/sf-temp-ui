package com.freshdirect.transadmin.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.freshdirect.transadmin.service.DomainManagerI;


public class UPSDataCacheManager 
{
	private Collection datas=null;	
	private static UPSDataCacheManager instance=null;
	public static Calendar clientCalendar = Calendar.getInstance();
	private long expired=-1;
	
	private UPSDataCacheManager()
	{
		
	}

	public static UPSDataCacheManager getInstance()
	{
		if(instance==null)
		{
			instance=new UPSDataCacheManager();
		}
		return instance;
	}
	
	public Collection getData(DomainManagerI mgr)
	{		
		long currentTime=System.currentTimeMillis();
		if(expired<currentTime)
		{
			loadData(mgr,currentTime);
		}		
		return datas;
	}
	public void loadData(DomainManagerI mgr,long currentTime)
	{
		if(isToday(new Date(currentTime)))
		{
			try {
				datas=	mgr.getUPSRouteInfo(TransStringUtil.getServerDate(new Date(currentTime)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				expired=-1;
			}
		}
	}
	public  boolean isToday(Date date) 
	{		
		try {
			clientCalendar.setTimeInMillis(System.currentTimeMillis());
			clientCalendar.set(Calendar.HOUR,5);
			clientCalendar.set(Calendar.MINUTE, 0);
			clientCalendar.set(Calendar.SECOND, 0);
			clientCalendar.set(Calendar.MILLISECOND, 0);
			clientCalendar.set(Calendar.AM_PM, Calendar.AM);
			long todayStartTime=clientCalendar.getTimeInMillis();
			clientCalendar.set(Calendar.HOUR,3);
			clientCalendar.set(Calendar.MINUTE, 0);
			clientCalendar.set(Calendar.SECOND, 0);
			clientCalendar.set(Calendar.MILLISECOND, 0);
			clientCalendar.set(Calendar.AM_PM, Calendar.AM);
			clientCalendar.add(Calendar.DATE, 1);
			clientCalendar.add(Calendar.MILLISECOND, -1);
			long todayEndTime=clientCalendar.getTimeInMillis();			
			long toCalculate=date.getTime();
			
			if(todayStartTime<=toCalculate&&toCalculate<=todayEndTime)
			{
				expired=todayEndTime;
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
