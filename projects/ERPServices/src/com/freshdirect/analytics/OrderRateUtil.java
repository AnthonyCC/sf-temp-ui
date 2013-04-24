package com.freshdirect.analytics;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import weblogic.auddi.util.Logger;

public class OrderRateUtil {

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
			Logger.info("Exception while rounding the value: "+value);
		}
		return val;
	}
	
	public static Date getSample(Calendar cal, Set<Date> exceptions)
	{
		cal.add(Calendar.DATE, -7);
		while(exceptions!= null && exceptions.contains(cal.getTime()))
		{
			cal.add(Calendar.DATE, -7);
		}
		return cal.getTime();
	}
	
}
