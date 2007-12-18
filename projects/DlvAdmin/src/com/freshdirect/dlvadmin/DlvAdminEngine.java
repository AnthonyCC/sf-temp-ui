package com.freshdirect.dlvadmin;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.log4j.Category;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.BaseEngine;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvTemplateManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.planning.Week;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.core.PrimaryKey;

import weblogic.security.Security;
import weblogic.security.principal.WLSUserImpl;
import java.security.Principal;
import javax.security.auth.Subject;

public class DlvAdminEngine extends BaseEngine {

	private final static Category LOGGER = LoggerFactory.getInstance(DlvAdminEngine.class);

	private transient IPropertySelectionModel regionSelectionModel = null;
	private transient Map regionCache = new HashMap();
	private transient Map dailyTimeslotsCache = new HashMap();
	
	public Object getUserName() {
		Principal principal = getCurrentPrincipal();
		String name = principal == null ? System.getProperty("user.name") : principal.getName();
		return name;
	}
	
	private Principal getCurrentPrincipal() {
		Subject subject = Security.getCurrentSubject();
		if (subject == null) {
			return null;
		}
		Set principals = subject.getPrincipals(WLSUserImpl.class);
		if (principals.isEmpty()) {
			return null;
		}
		return (Principal) principals.iterator().next();
	}

	protected void cleanCache() {
		this.regionSelectionModel = null; 
		this.regionCache = new HashMap();
		this.dailyTimeslotsCache = new HashMap();
	}

	public IPropertySelectionModel getRegionSelectionModel() {
		if (this.regionSelectionModel==null) {
			this.regionSelectionModel = this.buildRegionSelectionModel();	
		}
		return this.regionSelectionModel;
	}

	private IPropertySelectionModel buildRegionSelectionModel() {
		Collection regions;
		try {
			regions = DlvTemplateManager.getInstance().getRegions();
		} catch (DlvResourceException e) {
			throw new ApplicationRuntimeException(e);
		}
		ObjectSelectionModel sm = new ObjectSelectionModel();
		sm.add(null, "");
		for (Iterator i=regions.iterator(); i.hasNext(); ) {
			Object[] result = (Object[])i.next();
			DlvRegionModel r = new DlvRegionModel(new PrimaryKey((String)result[0]),(String)result[1]);

			sm.add(r, r.getName());
		}
		return sm;
	}

	public DlvRegionModel getRegion(String regionName) {
		return this.getRegion(regionName, new Date());
	}
	
	public DlvRegionModel getRegion(String regionName, Date startDate){
		DlvRegionModel region = (DlvRegionModel) this.regionCache.get(regionName + QuickDateFormat.SHORT_DATE_FORMATTER.format(startDate));
		if (region==null) {
			try{
				LOGGER.debug("getRegion "+regionName+" "+startDate+" start");
				region = DlvTemplateManager.getInstance().getRegion(regionName, startDate);
				LOGGER.debug("getRegion "+regionName+" "+startDate+" end");
				this.regionCache.put(regionName + QuickDateFormat.SHORT_DATE_FORMATTER.format(startDate), region);
			}catch(DlvResourceException e){
				throw new ApplicationRuntimeException(e);
			}
		}
		return region;
	}
	
	public IPropertySelectionModel getZoneSelectionModel(DlvRegionModel region, Week week) {
		List zones = this.getRegion(region.getName(), week.getWeekEnd()).getZones();
		ObjectSelectionModel sm = new ObjectSelectionModel();
		sm.add(null, "");
		for (Iterator i=zones.iterator(); i.hasNext(); ) {
			DlvZoneModel z = (DlvZoneModel)i.next();
			sm.add(z.getZoneCode(), z.getDisplayName());
		}
		return sm;
	}

	public IPropertySelectionModel getZoneSelectionModel(DlvRegionModel region) {
		return this.getZoneSelectionModel(region, new Week());
	}

	public List getZones(DlvRegionModel region, Week week) {
		List zones = this.getRegion(region.getName(), week.getWeekEnd()).getZones();
		return zones;
	}

	public List getZones(DlvRegionModel region) {
		return this.getZones(region, new Week());
	}

	
	public IPropertySelectionModel getDaySelectionModel(){
		ObjectSelectionModel sm = new ObjectSelectionModel();
		sm.add(null, "ALL");
		sm.add(new Integer(Calendar.MONDAY), "Monday");
		sm.add(new Integer(Calendar.TUESDAY), "Tuesday");
		sm.add(new Integer(Calendar.WEDNESDAY), "Wednesday");
		sm.add(new Integer(Calendar.THURSDAY), "Thursday");
		sm.add(new Integer(Calendar.FRIDAY), "Friday");
		sm.add(new Integer(Calendar.SATURDAY), "Saturday");
		sm.add(new Integer(Calendar.SUNDAY), "Sunday");
		return sm;
	}

	public IPropertySelectionModel getViewSelectionModel(){
		ObjectSelectionModel sm = new ObjectSelectionModel();
		sm.add(null, "Standard");
		sm.add("All", "All");
		sm.add("CT", "Chef's Table");
		return sm;
	}

	public Map getDailyTimeslots(String zoneCode){
		return this.getDailyTimeslots(zoneCode, new Date());
	}


	/**
	 * @return Map of day String - timeslot Set
	 */
	public Map getDailyTimeslots(String zoneCode, Date startDate) {
		if (zoneCode==null) {
			return Collections.EMPTY_MAP;
		}

		Map days = (Map)this.dailyTimeslotsCache.get(zoneCode);
	
		if (days==null) {
			List timeslots = this.getTimeslots(zoneCode);
			days = new TreeMap();
			Calendar beg = Calendar.getInstance();
			beg.setTime(startDate);
			Calendar end = Calendar.getInstance();
			end.setTime(startDate);
			end.add(Calendar.DATE, 7);
			while(beg.before(end)){
				Date day = beg.getTime();
				String begDate = QuickDateFormat.SHORT_DATE_FORMATTER.format(day);
				days.put(day, new TreeSet(
					new Comparator() {
						public int compare(Object o1, Object o2) {
							DlvTimeslotModel t1 = ((DlvTimeslotModel)o1);
							DlvTimeslotModel t2 = ((DlvTimeslotModel)o2);
							return t1.getStartTimeAsDate().compareTo( t2.getStartTimeAsDate() );
						}
					}
				) );

				for(int i = 0, size = timeslots.size(); i < size; i++){
					
					DlvTimeslotModel t = (DlvTimeslotModel)timeslots.get(i);
					String timeslotDate = QuickDateFormat.SHORT_DATE_FORMATTER.format(t.getBaseDate());
					if(timeslotDate.equals(begDate)){
						((Set)days.get(day)).add(t);
					}
				}
				beg.add(Calendar.DATE, 1);
			}
			
			this.dailyTimeslotsCache.put(zoneCode, days);
		}
	
		return days;
	}
	
	private List getTimeslots(String zoneCode) {
		
		Calendar  startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		
		endCal.add(Calendar.DATE, 8);
		endCal.set(Calendar.HOUR, 12);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.AM_PM, Calendar.AM);
		
		startCal.set(Calendar.HOUR_OF_DAY,0);
		startCal.set(Calendar.MINUTE,1);
		
		return getTimeslots(zoneCode, startCal.getTime(), endCal.getTime());
	}
	
	public List getTimeslots(String zoneCode, Date startDate, Date endDate){
		try{
			return DlvTemplateManager.getInstance().getTimeslotForDateRangeAndZone(startDate, endDate, new Date(), zoneCode);
		}catch(DlvResourceException de){
			throw new ApplicationRuntimeException(de);
		}
	}
}

