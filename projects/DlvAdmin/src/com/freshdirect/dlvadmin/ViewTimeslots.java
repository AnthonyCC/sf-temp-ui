package com.freshdirect.dlvadmin;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvTemplateManager;
import com.freshdirect.delivery.admin.DlvHistoricTimeslotData;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;

public abstract class ViewTimeslots extends DlvPage implements PageDetachListener {
	
	private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE'<br>'MMMM d, yyyy");

	private DlvHistoricTimeslotData timeslotData;
		
	public void pageDetached(PageEvent event) {
		this.timeslotData = null;
	}

	private DlvHistoricTimeslotData getTimeslotData() {
		try {
			if (this.timeslotData == null) {
				String regionId = getSelectedRegion().getPK().getId();
				this.timeslotData = DlvTemplateManager.getInstance()
						.getTimeslotForDateRangeAndRegion(getStartDate(),
								getEndDate(), regionId);
			}
			return this.timeslotData;
		} catch (DlvResourceException e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
	public NumberFormat getPercentageFormatter(){
		return NumberFormat.getPercentInstance();
	}
	
	public abstract Date getStartDate();
	
	public abstract Date getEndDate();
	
	public abstract DlvRegionModel getSelectedRegion();

	public abstract String getSelectedZone();
	
	public Date calculateDate(int delta) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, delta);
		return cal.getTime();
	}


	public SimpleDateFormat getTimeFormatter() {
		return timeFormatter;
	}

	public SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}
		
	public abstract Integer getSelectedDay();
	
	public List getZones() {
		DlvAdminEngine engine = (DlvAdminEngine) this.getEngine();
		DlvRegionModel region = engine.getRegion(getSelectedRegion().getName(),
				getEndDate());
		if (this.getSelectedZone() == null) {
			return region.getZones();
		} else {
			DlvZoneModel zone = region.getZone(this.getSelectedZone());
			if (zone == null) {
				return region.getZones();
			}
			List zones = new ArrayList();
			zones.add(zone);
			return zones;
		}
	}
	
	public List getDays(){
		
		ArrayList days = new ArrayList();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(getStartDate());
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(getEndDate());
		while(startCal.before(endCal)){
			if(getSelectedDay() != null){
				if(startCal.get(Calendar.DAY_OF_WEEK) == this.getSelectedDay().intValue()){
					days.add(startCal.getTime());
				}
			}else{
				days.add(startCal.getTime());
			}
			
			startCal.add(Calendar.DATE, 1);
		}
		
		return days;
	}
		
	public Collection getTimeslots(String zoneCode, Date date){
		return getTimeslotData().getTimeslots(zoneCode, date);
	}
	
	public int getCapacityTotal(String zoneCode, Date date){
		return getTimeslotData().getCapacityTotal(zoneCode, date);
	}
	
	public int getAllocationTotal(String zoneCode, Date date){
		return getTimeslotData().calculateAllocationTotal(getCurrentDate(), zoneCode, date);
	}
	public int getBaseCapacityTotal(String zoneCode, Date date){
		return getTimeslotData().getBaseCapacityTotal(zoneCode, date);
	}
	
	public int getBaseAllocationTotal(String zoneCode, Date date){
		return getTimeslotData().getBaseAllocationTotal(zoneCode, date);
	}
	public int getCTCapacityTotal(String zoneCode, Date date){
		return getTimeslotData().getCTCapacityTotal(zoneCode, date);
	}
	
	public int getCTAllocationTotal(String zoneCode, Date date){
		return getTimeslotData().getCTAllocationTotal(zoneCode, date);
	}

	
	public int getZoneAllocationTotal(String zoneCode, List dates){
		return getTimeslotData().calculateZoneAllocationTotal(getCurrentDate(), zoneCode, dates);
	}
	
	public int getZoneCapacityTotal(String zoneCode, List dates){
		return getTimeslotData().getZoneCapacityTotal(zoneCode, dates);
	}
	
	public double getPercentage(DlvTimeslotModel currentTimeslot){
		if(currentTimeslot.calculateCurrentAllocation(getCurrentDate())==0)
			return 0;
		else
			return ((double)currentTimeslot.getChefsTableAllocation())/currentTimeslot.calculateCurrentAllocation(getCurrentDate());
	}
	
	public double getPercentageTotal(String zoneCode, Date currentDay){
		if(getAllocationTotal(zoneCode, currentDay) == 0)
			return 0;
		else
			return new Integer(getCTAllocationTotal(zoneCode, currentDay)).doubleValue()/new Integer(getAllocationTotal(zoneCode, currentDay)).doubleValue();
	}	

}
