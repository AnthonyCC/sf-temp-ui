package com.freshdirect.delivery.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.EnumDayCode;
import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.EnumTruckType;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.planning.DlvPlanModel;
import com.freshdirect.delivery.planning.DlvResourceModel;
import com.freshdirect.delivery.planning.DlvShiftModel;
import com.freshdirect.delivery.planning.DlvShiftTimeslotModel;
import com.freshdirect.delivery.planning.DlvTruckAllocation;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.TimeOfDay;

public class DlvAdminManagerMock implements DlvAdminI {
	
	private DlvPlanModel planModel;
	
	public DlvAdminManagerMock(){
		super();
		initialize();
		
	}

	private void initialize() {
		this.planModel = new DlvPlanModel(new PrimaryKey("12345"));
		this.planModel.setName("Standard Plan");
		this.getShifts(planModel);
	}
	
	public void getShifts(DlvPlanModel planModel){
		DlvShiftModel shift = null;
		int count = 1;
		for(Iterator i = EnumDayCode.iterator(); i.hasNext(); ){
			
			EnumDayCode day = (EnumDayCode)i.next();
			
			if(EnumDayCode.SATURDAY.equals(day) || EnumDayCode.SUNDAY.equals(day)){
				shift = new DlvShiftModel();
				shift.setDayOfWeek(day);
				shift.setName(day.getName()+" AM Shift");
				shift.setStart( new TimeOfDay("08:00 AM") );
				shift.setEnd( new TimeOfDay("04:00 PM") );
				shift.setTimeslots(getTimeslots("weekend"));

				planModel.addShift(shift);
				count++;
			}
			shift = new DlvShiftModel();
			shift.setDayOfWeek(day);
			shift.setName(day.getName()+" PM Shift");
			shift.setStart( new TimeOfDay("04:00 PM") );
			shift.setEnd( new TimeOfDay("12:00 PM") );
			shift.setTimeslots(getTimeslots("regular"));

			planModel.addShift(shift);
			
			count++;
			
		}
	}
	
	public List getTimeslots(String shift){
		List timeslots = new ArrayList();
		
		if("regular".equalsIgnoreCase(shift)){
			
			DlvShiftTimeslotModel timeslot = new DlvShiftTimeslotModel();
			timeslot.setStartTime(new TimeOfDay("04:00 PM"));
			timeslot.setEndTime(new TimeOfDay("06:00 PM"));
			timeslot.setCutoffTime(new TimeOfDay("11:59 PM"));
			timeslot.setStatus(EnumTimeslotStatus.STANDARD);
			timeslots.add(timeslot);
			
			timeslot = new DlvShiftTimeslotModel();
			timeslot.setStartTime(new TimeOfDay("06:00 PM"));
			timeslot.setEndTime(new TimeOfDay("08:00 PM"));
			timeslot.setCutoffTime(new TimeOfDay("11:59 PM"));
			timeslot.setStatus(EnumTimeslotStatus.STANDARD);
			timeslots.add(timeslot);
			
			timeslot = new DlvShiftTimeslotModel();
			timeslot.setStartTime(new TimeOfDay("08:00 PM"));
			timeslot.setEndTime(new TimeOfDay("10:00 PM"));
			timeslot.setCutoffTime(new TimeOfDay("11:59 PM"));
			timeslot.setStatus(EnumTimeslotStatus.STANDARD);
			timeslots.add(timeslot);
		}else if("weekend".equals(shift)){
			DlvShiftTimeslotModel timeslot = new DlvShiftTimeslotModel();
			timeslot.setStartTime(new TimeOfDay("10:00 AM"));
			timeslot.setEndTime(new TimeOfDay("12:00 PM"));
			timeslot.setCutoffTime(new TimeOfDay("11:59 PM"));
			timeslot.setStatus(EnumTimeslotStatus.STANDARD);
			timeslots.add(timeslot);

			timeslot = new DlvShiftTimeslotModel();
			timeslot.setStartTime(new TimeOfDay("12:00 PM"));
			timeslot.setEndTime(new TimeOfDay("02:00 PM"));
			timeslot.setCutoffTime(new TimeOfDay("11:59 PM"));
			timeslot.setStatus(EnumTimeslotStatus.STANDARD);
			timeslots.add(timeslot);

			timeslot = new DlvShiftTimeslotModel();
			timeslot.setStartTime(new TimeOfDay("02:00 PM"));
			timeslot.setEndTime(new TimeOfDay("04:00 PM"));
			timeslot.setCutoffTime(new TimeOfDay("11:59 PM"));
			timeslot.setStatus(EnumTimeslotStatus.STANDARD);
			timeslots.add(timeslot);
		}
		
		return timeslots;
	}
	
	public List getPlansForRegion(String regionId) throws DlvResourceException{
		
		List plans = new ArrayList();
		this.planModel.setRegionId(regionId);
		
		plans.add(this.planModel);
		
		return plans;
	}
	
	public DlvPlanModel getPlan(String planId) throws DlvResourceException {
		return this.planModel;
	}
	
	public List getResourcesForDate(String regionName, String zoneCode, Date day) throws DlvResourceException {
		
		DlvPlanModel plan = this.getPlanForZone(regionName, zoneCode);
		List dlvResources = new ArrayList();
		Calendar dayCal = Calendar.getInstance();
		dayCal.setTime(day);
		for(Iterator i = plan.getShiftsForDay(EnumDayCode.getEnum(dayCal.get(Calendar.DAY_OF_WEEK))).iterator(); i.hasNext(); ){
			DlvShiftModel shift = (DlvShiftModel)i.next();
			List dlvTimeslots = new ArrayList();
			DlvResourceModel resource = new DlvResourceModel();
			resource.setName(shift.getName());
			resource.setZoneCode(zoneCode);
			resource.setStart(shift.getStart());
			resource.setEnd(shift.getEnd());
			resource.setDeliveryRate(6);
			resource.setDay(day);
			resource.setPeople(5);
			
			DlvTruckAllocation truck = new DlvTruckAllocation(EnumTruckType.TYPE_A);
			truck.setCapacity(75);
			truck.setCount(2);
			
			resource.setTruckAllocation(truck);
			
			for(Iterator j = shift.getTimeslots().iterator(); j.hasNext();){
				DlvShiftTimeslotModel shiftTimeslot = (DlvShiftTimeslotModel)j.next();
				DlvTimeslotModel dlvTimeslot = new DlvTimeslotModel(shiftTimeslot);
				dlvTimeslot.setBaseDate(day);
				dlvTimeslot.setTrafficFactor(80);
				
				dlvTimeslots.add(dlvTimeslot);
			}
			resource.setTimeslots(dlvTimeslots);
			resource.calculateCapacities();
			dlvResources.add(resource);
		}
		
		return dlvResources;
	}
	
	public DlvPlanModel getPlanForZone(String regionName, String zoneCode){
		
		return this.planModel;

	}
	
	public void addPlan(DlvPlanModel planModel) throws DlvResourceException {
		
	}
	
	public void updatePlan(DlvPlanModel planModel) throws DlvResourceException {
	}
	
	public PrimaryKey updatePlanningResource(String regionName, String zoneCode, DlvResourceModel resourceModel) throws DlvResourceException{
		return new PrimaryKey("12345");
	}

	public void updateZones(String regionName, List zones, Date startDate) throws DlvResourceException {
	}

	public void updateChefsTableZone(String zoneCode, boolean ctActive, int ctReleaseTime) throws DlvResourceException {
	}
	
	public void deletePlan(String planId) throws DlvResourceException {
	}

	public void updateRegionData(String regionDataId, Date startDate, double dlvCharge) throws DlvResourceException {
	}
	
	public List getResourcesForDateRange(String regionName, String zoneCode, Date[] days) throws DlvResourceException {
		return null;
	}
	
	public DlvResourceModel getResourceById(String resourceId) throws DlvResourceException {
		return null;
	}
	
	public List getMapLayersForRegion() throws DlvResourceException {
		return null;
	}
	
	public Map getResourcesForRegionAndDateRange(String regionId, Date[] days) throws DlvResourceException {
		return null;
	}
	
	public List getEarlyWarningData(Date day) throws DlvResourceException {
		return Collections.EMPTY_LIST;
	}

	public void updateZoneUnattendedDeliveryStatus(String zoneCode, boolean unattended) throws DlvResourceException {
		// TODO Auto-generated method stub
		
	}

}
