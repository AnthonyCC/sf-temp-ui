package com.freshdirect.analytics.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

public class OrderRateUtil {
	private static final Category LOGGER = Logger.getLogger(OrderRateUtil.class);


	public static Date addTime(Date time)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MINUTE, 30);
		return cal.getTime();
		
	}
	
	public static Date getDate(Date date, int lookback)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, lookback);
		return cal.getTime();
	}
	public static float roundValue(double value)
	{
		float val = 0;
		try
		{
		DecimalFormat df = new DecimalFormat("#.##");
		val =  Float.valueOf(df.format(value)).floatValue();
		}
		catch(NumberFormatException nfe)
		{
			LOGGER.info("Exception while rounding the value: "+value);
		}
		return val;
	}
	
	public static Date getSample(Calendar cal, Map holidayMap)
	{
		cal.add(Calendar.DATE, -7);
		while(holidayMap!= null && holidayMap.get(cal.getTime()) != null)
		{
			cal.add(Calendar.DATE, -7);
		}
		return cal.getTime();
		
	}
	public static Date getSample(Calendar cal,  List<Date> holidays)
	{
		cal.add(Calendar.DATE, -7);
		while(holidays.indexOf(cal.getTime())!=-1)
		{
			cal.add(Calendar.DATE, -7);
		}	
		return cal.getTime();
	}
	
}
