package com.freshdirect.transadmin.model;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.freshdirect.transadmin.util.TransStringUtil;

public class Scrib 
{
	
	private String scribId;
	private Zone zone;
	private Region region;
	private Date scribDate;
	private Date startTime;
	private Date firstDlvTime;
	private Date endDlvTime;
	private Date maxReturnTime;
	private int routeLength;
	private int count;
	private int resources;
	private String supervisorCode;
	private String supervisorName;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getEndDlvTime() {
		return endDlvTime;
	}
	public void setEndDlvTime(Date endDlvTime) {
		this.endDlvTime = endDlvTime;
	}
	public Date getFirstDlvTime() {
		return firstDlvTime;
	}
	public void setFirstDlvTime(Date firstDlvTime) {
		this.firstDlvTime = firstDlvTime;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public int getRouteLength() {
		return routeLength;
	}
	public void setRouteLength(int routeLength) {
		this.routeLength = routeLength;
	}
	public Date getScribDate() {
		return scribDate;
	}
	public void setScribDate(Date scribDate) {
		this.scribDate = scribDate;
	}
	public String getScribId() {
		return scribId;
	}
	public void setScribId(String scribId) {
		this.scribId = scribId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date scribTime) {
		this.startTime = scribTime;
	}
	
	public Zone getZone() {
		return zone;
	}
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	public Date getPrefRuturn()
	{
		if(endDlvTime==null)return null;
		Calendar c=Calendar.getInstance();
		c.setTime(endDlvTime);
		int stemTime=0;
		if(zone.getStemNotNullFromTime()!=null)stemTime=zone.getStemNotNullFromTime().intValue();
		c.add(Calendar.MINUTE, stemTime);
		return c.getTime();
	}
	public Date getMaxReturnTime() 
	{
		return maxReturnTime;
		
	}
	public Date getMaxReturnTimeDisplay() 
	{
		Date d=maxReturnTime;
		if(d==null)d=getPrefRuturn();
		return d;
	}
	public Date getMaxReturnTimeDisplay1() 
	{
		Date d=maxReturnTime;		
		return d;
	}
	public void setMaxReturnTime(Date maxReturnTime) 
	{
		this.maxReturnTime = maxReturnTime;
	}
	
	public Date getWaveStart()
	{
		Calendar c=Calendar.getInstance();
		c.setTime(firstDlvTime);
		int stemTime=0;
		if(zone.getStemToTime()!=null)stemTime=zone.getStemToTime().intValue();
		c.add(Calendar.MINUTE, -stemTime);
		return c.getTime();
	}
	public Date getPrefTime()
	{
		long time=getPrefRuturn().getTime()-getWaveStart().getTime();
		return new Date(time-TimeZone.getDefault().getRawOffset());
	}
	public Date getMaxTime()
	{
		long time=getMaxReturnTimeDisplay().getTime()-getWaveStart().getTime();
		return new Date(time-TimeZone.getDefault().getRawOffset());
	}
	public Date getMaxTime1()
	{
		if(maxReturnTime==null) return null;
		else return getMaxTime();
	}
	public Date getMaxTimeOrig()
	{
		long time=getMaxReturnTimeDisplay().getTime()-getWaveStart().getTime();
		return new Date(time);
	}
	public Date getStemToTime()
	{
		long time=0;
		if(zone.getStemToTime()!=null)time=zone.getStemToTime().intValue()*60*1000;		
		return new Date(time-TimeZone.getDefault().getRawOffset());
	}
	public Date getStemFromTime()
	{
		long time=0;
		if(zone.getStemNotNullFromTime()!=null)time=zone.getStemNotNullFromTime().intValue()*60*1000;		
		return new Date(time-TimeZone.getDefault().getRawOffset());
	}
	
	public String getEndDlvTimeS() 
	{
		try {
			if(endDlvTime!=null)return TransStringUtil.getServerTime(endDlvTime);
		} catch (ParseException e) 
		{
			
		}
		return null;
		
	}
	public void setEndDlvTimeS(String endDlvTimeS) 
	{
		try 
		{
			if(endDlvTimeS!=null&&endDlvTimeS.length()>0)endDlvTime=TransStringUtil.getServerTime(endDlvTimeS);
			else endDlvTime=null;
		} catch (ParseException e) 
		{
			
		}
	}
	public String getFirstDlvTimeS() 
	{
		try {
			if(firstDlvTime!=null)return TransStringUtil.getServerTime(firstDlvTime);
		} catch (ParseException e) 
		{
			
		}
		return null;
	}
	public void setFirstDlvTimeS(String firstDlvTimeS) 
	{
		try 
		{
			if(firstDlvTimeS!=null&&firstDlvTimeS.length()>0)firstDlvTime=TransStringUtil.getServerTime(firstDlvTimeS);
			else firstDlvTime=null;
		} catch (ParseException e) 
		{
			
		}
	}
	public String getMaxReturnTimeS() 
	{
		try {
			if(maxReturnTime!=null)return TransStringUtil.getServerTime(maxReturnTime);
			
		} catch (ParseException e) 
		{
			
		}
		return null;
	}
	public void setMaxReturnTimeS(String maxReturnTimeS) 
	{
		try 
		{
			if(maxReturnTimeS!=null&&maxReturnTimeS.length()>0)maxReturnTime=TransStringUtil.getServerTime(maxReturnTimeS);
			else maxReturnTime=null;
		} catch (ParseException e) 
		{
			
		}
	}
	public String getStartTimeS() 
	{
		try {
			if(startTime!=null)return TransStringUtil.getServerTime(startTime);
		} catch (ParseException e) 
		{
			
		}
		return null;
	}
	public void setStartTimeS(String startTimeS) 
	{
		try 
		{
			if(startTimeS!=null&&startTimeS.length()>0)startTime=TransStringUtil.getServerTime(startTimeS);
			else startTime=null;
		} catch (ParseException e) 
		{
			
		}
	}
	public String getZoneS() 
	{
		if(zone!=null)return zone.getZoneCode();
		return null;
	}
	public String getRegionS() 
	{
		if(region!=null)return region.getCode();
		return null;
	}
	public void setZoneS(String zoneS) 
	{
		if(zoneS!=null&&zoneS.length()>0){ zone=new Zone();zone.setZoneCode(zoneS);}
		else zone=null;
	}
	public int getResources() {
		return resources;
	}
	public void setResources(int resources) {
		this.resources = resources;
	}
	public String getSupervisorCode() {
		return supervisorCode;
	}
	public void setSupervisorCode(String supervisorCode) {
		this.supervisorCode = supervisorCode;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	
	
	
}
