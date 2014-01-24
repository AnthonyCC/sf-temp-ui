package com.freshdirect.transadmin.model;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.routing.model.EquipmentType;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

public class Scrib implements java.io.Serializable, IWaveInstanceSource {
	
	private String scribId;
	private TrnFacility originFacility;
	private TrnFacility destinationFacility;
	private Zone zone;
	private Region region;
	private Date scribDate;	
	private Date dispatchGroup;
	private Date startTime;
	private Date endTime;
	private Date maxReturnTime;
	private int routeLength;
	private int truckCnt;
	private int handTruckCnt;
	private String supervisorCode;
	private String supervisorName;
	private String shiftType;
	private double shiftDuration;
	private String scribLabel;
	private Date cutOffTime;
	private String equipmentTypeS;
	private List<EquipmentType> equipmentTypes;	
	
	private String dispatchGroupModified;
	private String zoneModified;
		
	public Date getDispatchGroup() {
		return dispatchGroup;
	}

	public void setDispatchGroup(Date dispatchGroup) {
		this.dispatchGroup = dispatchGroup;
	}

	public TrnFacility getOriginFacility() {
		return originFacility;
	}

	public void setOriginFacility(TrnFacility originFacility) {
		this.originFacility = originFacility;
	}

	public TrnFacility getDestinationFacility() {
		return destinationFacility;
	}

	public void setDestinationFacility(TrnFacility destinationFacility) {
		this.destinationFacility = destinationFacility;
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
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getMaxReturnTime() {
		return maxReturnTime;
	}
	public void setMaxReturnTime(Date maxReturnTime) {
		this.maxReturnTime = maxReturnTime;
	}

	public String getZoneModified() {
		return zoneModified;
	}

	public void setZoneModified(String zoneModified) {
		this.zoneModified = zoneModified;
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
	public int getTruckCnt() {
		return truckCnt;
	}
	public void setTruckCnt(int truckCnt) {
		this.truckCnt = truckCnt;
	}
	public String getDispatchGroupModified() {
		return dispatchGroupModified;
	}
	public void setDispatchGroupModified(String dispatchGroupModified) {
		this.dispatchGroupModified = dispatchGroupModified;
	}
	public String getHandTruckCnt1() {
		return handTruckCnt == 0 ? null : "" + handTruckCnt;
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

	public Date getWaveStart() {
		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		int stemTime = 0;
		if (zone != null && zone.getPreTripTime() != null) {
			stemTime = zone.getPreTripTime().intValue();
		} else if (destinationFacility != null) {
			stemTime = destinationFacility.getLeadToTime() != null 
							? destinationFacility.getLeadToTime().intValue() 
									: destinationFacility.getLeadFromTime() != null ? destinationFacility.getLeadFromTime().intValue() : 0;
		}
		c.add(Calendar.MINUTE, stemTime);
		return c.getTime();
	}

	public Date getPrefRunTime() {
		if (endTime == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(endTime);
		int stemTime = 0;
		if (zone != null && zone.getPostTripTime() != null) {
			stemTime = zone.getPostTripTime().intValue();
		} else if (destinationFacility != null) {
			stemTime = destinationFacility.getLeadFromTime() != null 
							? destinationFacility.getLeadFromTime().intValue() 
									: destinationFacility.getLeadToTime() != null ? destinationFacility.getLeadToTime().intValue() : 0;
		}
		c.add(Calendar.MINUTE, -stemTime);

		long time =  RoutingDateUtil.calcRunTime(getWaveStart(), c.getTime()) * 1000;
		return new Date(time - TimeZone.getDefault().getRawOffset());
	}

	public Date getMaxRunTime() {

		if (maxReturnTime == null) {
			return getPrefRunTime();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(maxReturnTime);
		int stemTime = 0;
		if (zone != null && zone.getPostTripTime() != null) {
			stemTime = zone.getPostTripTime().intValue();
		} else if (destinationFacility != null) {
			stemTime = destinationFacility.getLeadFromTime() != null 
							? destinationFacility.getLeadFromTime().intValue() 
									: destinationFacility.getLeadToTime() != null ? destinationFacility.getLeadToTime().intValue() : 0;
		}
		c.add(Calendar.MINUTE, -stemTime);

		long time =  RoutingDateUtil.calcRunTime(getWaveStart(), c.getTime()) * 1000;
		return new Date(time - TimeZone.getDefault().getRawOffset());

	}

	public Date getPreTripTime() {
		long time = 0;
		if (zone != null && zone.getPreTripTime() != null)
			time = zone.getPreTripTime().intValue() * 60 * 1000;
		else if (destinationFacility != null)
			time = destinationFacility.getLeadToTime() != null 
						? destinationFacility.getLeadToTime().intValue() * 60 * 1000 
								: destinationFacility.getLeadFromTime() != null ? destinationFacility.getLeadFromTime().intValue() * 60 * 1000  : 0;			
		return new Date(time - TimeZone.getDefault().getRawOffset());
	}

	public Date getPostTripTime() {
		long time = 0;
		if(zone != null && zone.getPostTripTime() != null) {
			time = zone.getPostTripTime().intValue() * 60 * 1000;		
		} else if (destinationFacility != null) {
			time = destinationFacility.getLeadFromTime() != null 
					? destinationFacility.getLeadFromTime().intValue() * 60 * 1000 
							: destinationFacility.getLeadToTime() != null ? destinationFacility.getLeadToTime().intValue() * 60 * 1000  : 0;
		}
		return new Date(time - TimeZone.getDefault().getRawOffset());
	}
	
	public String getEndTimeS() {
		try {
			if (endTime != null)
				return TransStringUtil.getServerTime(endTime);
		} catch (ParseException e) {

		}
		return null;

	}

	public void setEndTimeS(String endTimeS) {
		try {
			if (endTimeS != null && endTimeS.length() > 0)
				endTime = TransStringUtil.getServerTime(endTimeS);
			else
				endTime = null;
		} catch (ParseException e) {

		}
	}

	public String getMaxReturnTimeS() {
		try {
			if (maxReturnTime != null)
				return TransStringUtil.getServerTime(maxReturnTime);

		} catch (ParseException e) {

		}
		return null;
	}

	public void setMaxReturnTimeS(String maxReturnTimeS) {
		try {
			if (maxReturnTimeS != null && maxReturnTimeS.length() > 0)
				maxReturnTime = TransStringUtil.getServerTime(maxReturnTimeS);
			else
				maxReturnTime = null;
		} catch (ParseException e) {

		}
	}

	public String getStartTimeS() {
		try {
			if (startTime != null)
				return TransStringUtil.getServerTime(startTime);
		} catch (ParseException e) {

		}
		return null;
	}

	public void setStartTimeS(String startTimeS) {
		try {
			if (startTimeS != null && startTimeS.length() > 0)
				startTime = TransStringUtil.getServerTime(startTimeS);
			else
				startTime = null;
		} catch (ParseException e) {

		}
	}
	
	public String getDispatchGroupS() {
		try {
			if (dispatchGroup != null)
				return TransStringUtil.getServerTime(dispatchGroup);
		} catch (ParseException e) {

		}
		return null;
	}
	public void setDispatchGroupS(String timeS) {

		try 
		{
			if (timeS != null && timeS.length() > 0)
				dispatchGroup = TransStringUtil.getServerTime(timeS);
			else dispatchGroup = null;
		} catch (ParseException e) 
		{
			
		}
	}

	public String getZoneS() 
	{
		if (zone != null)
			return zone.getZoneCode();
		return null;
	}
	public String getRegionS() 
	{
		if (region != null)
			return region.getCode();
		return null;
	}
	public void setZoneS(String zoneS) 
	{
		if (zoneS != null && zoneS.length() > 0) {
			zone = new Zone();
			zone.setZoneCode(zoneS);
		} else {
			zone = null;
		}
	}
	public void setRegionS(String regionS) 
	{
		if (regionS != null && regionS.length() > 0) {
			region = new Region();
			region.setCode(regionS);
		} else {
			region = null;
		}
	}
	public int getHandTruckCnt() {
		return handTruckCnt;
	}
	public void setHandTruckCnt(int handTruckCnt) {
		this.handTruckCnt = handTruckCnt;
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
	
	public String getCutOffTimeS() {
		try {
			if (cutOffTime != null)
				return TransStringUtil.getServerTime(cutOffTime);
		} catch (ParseException e) {

		}
		return null;
	}

	public void setCutOffTimeS(String cutOffTimeS) {
		try {
			if (cutOffTimeS != null && cutOffTimeS.length() > 0)
				cutOffTime = TransStringUtil.getServerTime(cutOffTimeS);
			else
				cutOffTime = null;
		} catch (ParseException e) {

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
						&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot()) 
						&& EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(this.getOriginFacility().getTrnFacilityType().getName()) ? this.getHandTruckCnt() : this.getTruckCnt() ;
	}

	public int getNoOfResources1() {
		// TODO Auto-generated method stub
		return this.getZone() != null && this.getZone().getArea() != null 
						&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot()) 
						 ? this.getHandTruckCnt() : this.getTruckCnt() ;
	}

	@Override
	public boolean isValidSource() {
		// TODO Auto-generated method stub
		return this.getOriginFacility() != null && this.getDeliveryDate() != null && this.getStartTime()!= null 
					&& this.getDispatchGroup() != null && this.getEndTime() != null 
					&& this.getCutOffTime() != null && this.getZone() != null;
	}

	@Override
	public boolean needsConsolidation() {
		// For now consolidation will happen in UPS.
		// It can be turned off anytime based on future business requirements
		//return false;
		return this.getZone() != null && this.getZone().getArea() != null 
				&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot())
				&& !(this.getOriginFacility()!=null && this.getOriginFacility().getTrnFacilityType()!=null && EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(this.getOriginFacility().getTrnFacilityType().getName()))
				&& !(this.getDestinationFacility()!=null && this.getDestinationFacility().getTrnFacilityType()!=null && EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(this.getDestinationFacility().getTrnFacilityType().getName()));
	}

	@Override
	public void setNoOfResources(int value) {
		// TODO Auto-generated method stub
		if(this.getZone() != null && this.getZone().getArea() != null 
				&& "X".equalsIgnoreCase(this.getZone().getArea().getIsDepot())
				&& EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equals(this.getOriginFacility().getTrnFacilityType().getName())) {
			this.setHandTruckCnt(value);
		} else {
			this.setTruckCnt(value);
		}
	}
	
	public String getFacilityInfoEx() {
		if (originFacility == null || destinationFacility == null)
			return null;

		StringBuffer buf = new StringBuffer();
		buf.append(originFacility.getName() + " - "
				+ destinationFacility.getName());

		return buf.toString();
	}

	public void setFacilityS(String facilityS) {
		if (facilityS != null && facilityS.length() > 0) {
			originFacility = new TrnFacility();
			destinationFacility = new TrnFacility();
			String[] facilityLst = TransStringUtil.splitStringForValue1(facilityS);
			if (facilityLst != null && facilityLst.length > 0) {
				originFacility.setName(facilityLst[0].trim());
				destinationFacility.setName(facilityLst[1].trim());
			}
		} else {
			originFacility = null;
			destinationFacility = null;
		}
	}	

	public String getEquipmentTypeS() {
		return equipmentTypeS;
	}

	public void setEquipmentTypeS(String equipmentTypeS) {
		this.equipmentTypeS = equipmentTypeS;
	}

	public List<EquipmentType> getEquipmentTypes() {
		return equipmentTypes;
	}

	public void setEquipmentTypes(List<EquipmentType> equipmentTypes) {
		this.equipmentTypes = equipmentTypes;
	}
	
}
