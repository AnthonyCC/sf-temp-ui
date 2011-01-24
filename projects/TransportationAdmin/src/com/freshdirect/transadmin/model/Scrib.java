package com.freshdirect.transadmin.model;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.freshdirect.transadmin.util.TransStringUtil;

public class Scrib implements java.io.Serializable, IWaveInstanceSource
{
	
	private String scribId;
	private Zone zone;
	private Region region;
	private Date scribDate;
	private Date startTime;
	private Date firstDeliveryTime;
	private Date lastDeliveryTime;
	private Date maxReturnTime;
	private int routeLength;
	private int count;
	private int resources;
	private String supervisorCode;
	private String supervisorName;
	
	private String shiftType;
	private double shiftDuration;
	private String scribLabel;
	private String firstDeliveryTimeModified;
	private String zoneModified;
	
	private Date cutOffTime;
	
	public Date getFirstDeliveryTime() {
		return firstDeliveryTime;
	}

	public Date getLastDeliveryTime() {
		return lastDeliveryTime;
	}

	public void setFirstDeliveryTime(Date firstDeliveryTime) {
		this.firstDeliveryTime = firstDeliveryTime;
	}

	public void setLastDeliveryTime(Date lastDeliveryTime) {
		this.lastDeliveryTime = lastDeliveryTime;
	}

	public String getZoneModified() {
		return zoneModified;
	}

	public void setZoneModified(String zoneModified) {
		this.zoneModified = zoneModified;
	}

	public String getFirstDeliveryTimeModified() {
		return firstDeliveryTimeModified;
	}

	public void setFirstDeliveryTimeModified(String firstDeliveryTimeModified) {
		this.firstDeliveryTimeModified = firstDeliveryTimeModified;
	}
	
	public String getScribLabel() {
		return scribLabel;
	}
	public void setScribLabel(String scribLabel) {
		this.scribLabel = scribLabel;
	}
	public String getShiftType() {
		return shiftType;
	}
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
	public double getShiftDuration() {
		return shiftDuration;
	}
	public void setShiftDuration(double shiftDuration) {
		this.shiftDuration = shiftDuration;
	}
	public int getCount() {
		return count;
	}
	public String getCount1() {
		return resources==0?null:""+resources;
	}
	public void setCount(int count) {
		this.count = count;
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
		if(lastDeliveryTime==null)return null;
		Calendar c=Calendar.getInstance();
		c.setTime(lastDeliveryTime);
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
		c.setTime(firstDeliveryTime);
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
		if(zone.getStemFromTime()!=null)
		{	time=zone.getStemFromTime().intValue()*60*1000;		
			return new Date(time-TimeZone.getDefault().getRawOffset());
		}
		return null;
	}
	
	public String getEndDlvTimeS() 
	{
		try {
			if(lastDeliveryTime!=null)return TransStringUtil.getServerTime(lastDeliveryTime);
		} catch (ParseException e) 
		{
			
		}
		return null;
		
	}
	public void setEndDlvTimeS(String endDlvTimeS) 
	{
		try 
		{
			if(endDlvTimeS!=null&&endDlvTimeS.length()>0)lastDeliveryTime=TransStringUtil.getServerTime(endDlvTimeS);
			else lastDeliveryTime=null;
		} catch (ParseException e) 
		{
			
		}
	}
	public String getFirstDlvTimeS() 
	{
		try {
			if(firstDeliveryTime!=null)return TransStringUtil.getServerTime(firstDeliveryTime);
		} catch (ParseException e) 
		{
			
		}
		return null;
	}
	public void setFirstDlvTimeS(String firstDlvTimeS) 
	{
		try 
		{
			if(firstDlvTimeS!=null&&firstDlvTimeS.length()>0)firstDeliveryTime=TransStringUtil.getServerTime(firstDlvTimeS);
			else firstDeliveryTime=null;
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

	public Date getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(Date cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	
	public String getCutOffTimeS() 
	{
		try {
			if(cutOffTime!=null)return TransStringUtil.getServerTime(cutOffTime);
		} catch (ParseException e) 
		{
			
		}
		return null;
		
	}
	public void setCutOffTimeS(String cutOffTimeS) 
	{
		try 
		{
			if(cutOffTimeS!=null&&cutOffTimeS.length()>0)cutOffTime=TransStringUtil.getServerTime(cutOffTimeS);
			else cutOffTime=null;
		} catch (ParseException e) 
		{
			
		}
	}

	@Override
	public Date getDeliveryDate() {
		// TODO Auto-generated method stub
		return this.getScribDate();
	}

	@Override
	public int getNoOfResources() {
		// TODO Auto-generated method stub
		return this.getZone() != null && this.getZone().getArea() != null 
						&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot()) ? this.getResources() : this.getCount() ;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return this.getDeliveryDate() != null && this.getStartTime()!= null 
					&& this.getFirstDeliveryTime() != null && this.getLastDeliveryTime() != null 
					&& this.getCutOffTime() != null && this.getZone() != null;
	}

	@Override
	public boolean needsConsolidation() {
		// For now consolidation will happen transparent to transapp it will be consolidated when sending it to UPS.
		// It can be turned on anytime based on future business requirements
		return false;
		/*return this.getZone() != null && this.getZone().getArea() != null 
				&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot());*/
	}

	@Override
	public void setNoOfResources(int value) {
		// TODO Auto-generated method stub
		if(this.getZone() != null && this.getZone().getArea() != null 
				&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot())) {
			this.setResources(value);
		} else {
			this.setCount(value);
		}
	}
	
}
