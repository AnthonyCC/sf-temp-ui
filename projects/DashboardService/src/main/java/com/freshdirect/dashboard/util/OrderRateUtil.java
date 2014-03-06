package com.freshdirect.dashboard.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderRateUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderRateUtil.class);
	
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
	
	public static Date getSample(Calendar cal, Set exceptions)
	{
		cal.add(Calendar.DATE, -7);
		while(exceptions!= null && exceptions.contains(cal.getTime()))
		{
			cal.add(Calendar.DATE, -7);
		}
		return cal.getTime();
	}
	
}
