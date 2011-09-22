package com.freshdirect.analytics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.drools.lang.DRLParser.synchronized_key_return;

public class EventHelper {

	private List<Date> cutoffs = new ArrayList();
	public static boolean isFutureDay(Date date, int range)
	{
		Calendar nextDay = Calendar.getInstance();
		Calendar deliveryDay = Calendar.getInstance();
		deliveryDay.setTime(date);
		nextDay.add(Calendar.DATE, range);
		if(nextDay.get(Calendar.DATE) == deliveryDay.get(Calendar.DATE)
				&& nextDay.get(Calendar.MONTH) == deliveryDay.get(Calendar.MONTH)
				&& nextDay.get(Calendar.YEAR) == deliveryDay.get(Calendar.YEAR))
			return true;
		else
			return false;
		
	}
	public boolean beforeCutoff(Date eventDtm, String time)
	{
		Calendar eventDate = Calendar.getInstance();
		eventDate.setTimeInMillis(eventDtm.getTime());
		int hr =  Integer.parseInt(time.substring(0, 2));
		return eventDate.get(Calendar.HOUR_OF_DAY) < hr;	
	}
	
	public boolean beforeMaxCutoff(TimeslotEventModel event)
	{
		List<TimeslotEventDetailModel> list = event.getDetail();
		Iterator<TimeslotEventDetailModel> it = list.iterator();
		while(it.hasNext())
		{
			TimeslotEventDetailModel eventD = (TimeslotEventDetailModel)it.next();
			if(isFutureDay(eventD.getDeliveryDate(),1))
			{
				cutoffs.add(eventD.getCutOff());
			}
		}
		if(cutoffs.size()==0)
			return false;
		return	(event.getEventDate().compareTo(Collections.max(cutoffs))<0)?true:false;
	}

	public Date getMaxCutoff()
	{
		return Collections.max(cutoffs);
	}
	public Date getMinCutoff()
	{
		return Collections.min(cutoffs);
	}
	
}
