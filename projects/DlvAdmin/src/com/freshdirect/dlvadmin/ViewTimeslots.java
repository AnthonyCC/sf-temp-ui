package com.freshdirect.dlvadmin;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvTemplateManager;
import com.freshdirect.delivery.admin.DlvHistoricTimeslotData;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.framework.util.DateRange;

public abstract class ViewTimeslots extends DlvPage implements PageDetachListener {
	
	private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE'<br>'MMMM d, yyyy");

	private DlvHistoricTimeslotData timeslotData;
	private TimeslotIndex timeslotsByWindow;
	private ZoneIndex timeslotsByZone;
	
	
	public void pageDetached(PageEvent event) {
		this.timeslotData = null;
		this.timeslotsByWindow = null;
		this.timeslotsByZone = null;
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
	
	public abstract String getSelectedDetailLevel();
	
	public String getTimeWindowString(DateRange range) {
		StringBuffer buffer = new StringBuffer(30);
		buffer.
			append(timeFormatter.format(range.getStartDate())).
			append(" - ").
			append(timeFormatter.format(range.getEndDate()));
		return buffer.toString();
	}
	
	
	/**
	 * An cached object that indexes the timeslots according to
	 * the page layout.
	 * 
	 * The share (constrained to) the data source.
	 * @author istvan
	 *
	 */
	protected abstract class Index {
		
		/**
		 * Iterator method.
		 * 
		 * This method will be repeatedly invoked to prepare the inex.
		 * @param zone delivery zone
		 * @param timeslot a timeslot in that zone
		 * @param date date (day) corresponding to that zone
		 * @see ViewTimeslots#index(com.freshdirect.dlvadmin.ViewTimeslots.Index)
		 */
		public abstract void addTimeSlot(DlvZoneModel zone, DlvTimeslotModel timeslot, Date date);
	}
	
	/**
	 * Zone detail layout index.
	 * 
	 * The index follows the page layout:
	 * <code>
	 * - For each zone selected
	 * 	 - For each day selected
	 *     - For each timeslot in {day and zone}
	 * </code>
	 * Java type: Map<zone:{@link DlvZoneModel},Map<day:{@link Date},Set<{@link DlvTimeslotModel}>>>.
	 * @author istvan
	 *
	 */
	protected class ZoneIndex extends Index {
		// Zone -> Map<Date,Set<TimeSlot>>
		private Map zoneIndex = 
			new TreeMap(
					
				new Comparator() {
					private String getZoneCode(Object o) {
						return o instanceof DlvZoneModel ? ((DlvZoneModel)o).getZoneCode() : o.toString();
					}

					public int compare(Object o1, Object o2) {
						
						return getZoneCode(o1).compareTo(getZoneCode(o2));
					}
					
				}
			);
		
		/** compare timeslots by start time so they are sorted
		 * 
		 * @return set
		 */
		private Set makeTimeSlotSet() {
			
			
			return new TreeSet( 
				new Comparator() {
					public int compare(Object o1, Object o2) {
						return ((DlvTimeslotModel)o1).getStartTimeAsDate().compareTo(((DlvTimeslotModel)o2).getStartTimeAsDate());
					}
				}
			);
		}
		
		/**
		 * Index callback method.
		 */
		public void addTimeSlot(DlvZoneModel zone, DlvTimeslotModel timeslot, Date date) {
			if (!zoneIndex.containsKey(zone)) zoneIndex.put(zone, new TreeMap());
			Map zoneTimeslots = (Map)zoneIndex.get(zone);
			if (!zoneTimeslots.containsKey(date)) zoneTimeslots.put(date, makeTimeSlotSet());
			Set timeslots = (Set)zoneTimeslots.get(date);
			timeslots.add(timeslot);
		}
		
		/** 
		 * All zones sorted by zone code.
		 *@return Set<{@link DlvZoneModel>}
		 */
		public Collection getZones() {
			return zoneIndex.keySet();
		}
		 
		/**
		 * Timeslots in chosen zone code corresponding to chosen date.
		 * @return Set<{@link DlvTimeslot}>
		 */
		public Collection getTimeslots(String zoneCode, Date date) {
			Map zoneTimeslots = (Map)zoneIndex.get(zoneCode);
			if (zoneTimeslots == null) return Collections.EMPTY_SET;
			Set res = (Set)zoneTimeslots.get(date);
			return res == null ? Collections.EMPTY_SET : res;
		}
		
		/**
		 * All timeslots in zone code on all chosen dates.
		 * @return List<{@link DlvTimeslot}> (not sorted).
		 */
		public Collection getTimeslots(String zoneCode) {
			List allTimeslots = new ArrayList();
			Map zoneTimeslots = (Map)zoneIndex.get(zoneCode);
			if (zoneTimeslots == null) return allTimeslots;
			for(Iterator i = zoneTimeslots.values().iterator(); i.hasNext();) {
				allTimeslots.addAll((Set)i.next());
			}
			return allTimeslots;
		}
	}
	
	/**
	 * Timeslot detail layout.
	 * 
	 * The index follows the page layout:
	 * <code>
	 * - For each day selected
	 * 	   - For each window (start and end time)
	 *       - For each cutoff time (which could differ even within the same window! -- due to proximity to FD depot)
	 *       	- For each zone - timeslot in {day,window,cutoff} 
	 * </code>
	 * Java type: Map<{@link Date},Map<{@link DateRange},Map<day:{@link Date},Map<{@link DlvZoneModel},{@link DlvTimeslotModel}>>>>.
	 * @author istvan
	 *
	 */
	protected class TimeslotIndex extends Index {
		
		// date -> Map<Window,Map<cutoffTime:Date,Map<DlvZoneModel,Timeslot>>>
		private Map timeslotIndex = new TreeMap();
		
		/**
		 * Time window map.
		 * 
		 * Compares first by start date and then by end date.
		 * @return map.
		 */
		private Map makeWindowMap() {
			return new TreeMap(
				new Comparator() {
					public int compare(Object arg0, Object arg1) {
						DateRange z1 = (DateRange)arg0;
						DateRange z2 = (DateRange)arg1;
						
						int ds = z1.getStartDate().compareTo(z2.getStartDate());
						if (ds < 0) return -1;
						else if (ds > 0) return +1;
						
						return z1.getEndDate().compareTo(z2.getEndDate());
					}
				}
			);
		}
		
		/**
		 * Map that can find timeslots by zone code or DlvZoneModel.
		 * 
		 * @return map
		 */
		private Map makeZoneMap() {
			return new TreeMap(
				new Comparator() {
					private String getZoneCode(Object o) {
						return o instanceof DlvTimeslotModel ? ((DlvZoneModel)o).getZoneCode() : o.toString();
					}
					
					public int compare(Object arg0, Object arg1) {
						
						String zcode1 = getZoneCode(arg0);
						String zcode2 = getZoneCode(arg1);
						
						return zcode1.compareTo(zcode2);
					}
				}
			);
		}
		
		/**
		 * Index callback method.
		 */
		public void addTimeSlot(DlvZoneModel zone, DlvTimeslotModel timeslot, Date date) {
			if (!timeslotIndex.containsKey(date)) timeslotIndex.put(date, makeWindowMap());
			Map timeslotsByWindow = (Map)timeslotIndex.get(date);
			DateRange window = new DateRange(timeslot.getStartTimeAsDate(),timeslot.getEndTimeAsDate());
			if (!timeslotsByWindow.containsKey(window)) timeslotsByWindow.put(window, new TreeMap());
			Map timeslotsByCutoff = (Map)timeslotsByWindow.get(window);
			Date cutoff = timeslot.getCutoffTimeAsDate();
			if (!timeslotsByCutoff.containsKey(cutoff)) timeslotsByCutoff.put(cutoff,makeZoneMap());
			
			Map timeslotsByZoneCode = (Map)timeslotsByCutoff.get(cutoff);
			timeslotsByZoneCode.put(zone,timeslot);
		}
		
		/**
		 * Get "the" timeslot corresponding to the chosen date, time window, cutoff and zone code.
		 * 
		 * @param date
		 * @param window
		 * @param cutoff
		 * @param zoneCode
		 * @return "the" timeslot as a Map.Entry<{@link DlvZoneModel},{@link DlvTimeslotModel}>
		 */
		public Map.Entry getTimeslot(Date date, DateRange window, Date cutoff, String zoneCode) {
			Map timeslotsByWindow = (Map)timeslotIndex.get(date);
			Map timeslotsByCutoff = (Map)timeslotsByWindow.get(window);
			Map timeslotsByZoneCode = (Map)timeslotsByCutoff.get(cutoff);
			
			// this ugly line gets both the DlvZoneModel and the DlvTimeSlotMode without
			// crating spurios (proxy or wrapper) objects
			return (Map.Entry)(((SortedMap)timeslotsByZoneCode).tailMap(zoneCode)).values().iterator().next();
		}
		
		/**
		 * Get the distinct cutoff times for a particular day and time window.
		 * @return Set<{@link Date}> (sorted by date)
		 */
		public Collection getCutoffTimes(Date date, DateRange window) {
			Map timeslotsByWindow = (Map)timeslotIndex.get(date);
			Map timeslotsByCutoff = (Map)timeslotsByWindow.get(window);
			return timeslotsByCutoff.keySet();
		}
		
		/**
		 * Get the time windows on a particular day.
		 * @return Set<{@link DateRange}> (sorted by start date)
		 */
		public Collection getWindows(Date date) {
			Map timeslotsByWindow = (Map)timeslotIndex.get(date);
			return timeslotsByWindow == null ? Collections.EMPTY_SET : timeslotsByWindow.keySet();
		}
		
		/**
		 * 
		 * @return Collection<{@link Map.Entry}> (sorted by zone code)
		 */
		public Collection getZoneTimeslots(Date date, DateRange window, Date cutoff) {
			Map timeslotsByWindow = (Map)timeslotIndex.get(date);
			Map timeslotsByCutoff = (Map)timeslotsByWindow.get(window);
			Map timeslotsByZoneCode = (Map)timeslotsByCutoff.get(cutoff);
			
			return timeslotsByZoneCode.entrySet();
		}	
		
	};
	
	/**
	 * Iterate through the date to prepare an index.
	 */
	private void index(Index index) {
		List dates = getDays();
		List zones = getZones();
		for(Iterator i = dates.iterator(); i.hasNext();) {
			Date date = (Date)i.next();
			for(Iterator j= zones.iterator(); j.hasNext(); ) {
				DlvZoneModel zone = (DlvZoneModel)j.next();
				Collection timeslots = obtainHistoricalTimeslots(zone.getZoneCode(), date);
				for(Iterator k = timeslots.iterator(); k.hasNext();) {
					DlvTimeslotModel timeslot = (DlvTimeslotModel)k.next();
					index.addTimeSlot(zone, timeslot, date);
				}
			}
		}
	}
	
	/**
	 * Lazy timeslot view index.
	 * @return timeslot view index.
	 */
	public TimeslotIndex getTimeslotIndex() {
		if (timeslotsByWindow == null) {
			timeslotsByWindow = new TimeslotIndex();
			index(timeslotsByWindow);
		}
		return timeslotsByWindow;
	}
	
	/**
	 * Lazy zone view index.
	 * @return zone index
	 */
	public ZoneIndex getZoneIndex() {
		if (timeslotsByZone == null) {
			timeslotsByZone = new ZoneIndex();
			index(timeslotsByZone);
		}
		return timeslotsByZone;
	}
		
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
	
	/**
	 * 
	 *	@return Collection<ZoneTimeslot>
	 */
	public Collection getZoneTimeslots(Date date, DateRange window, Date cutoff) {
		return getTimeslotIndex().getZoneTimeslots(date, window, cutoff);
	}
	
	// for view detail selection control
	public IPropertySelectionModel getDetailLevelSelectionModel() {
		ObjectSelectionModel sm = new ObjectSelectionModel();
		sm.add("byZone","Zone");
		sm.add("byTimeslot","Timeslot");
		return sm;
	}
	
	/**
	 * Get selected zones.
	 * @return zones
	 */
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
	
	/**
	 * Get selected days.
	 * @return days
	 */
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
		
	/**
	 * Get {@link DlvTimeslotModel}s in the selected zone corresponding to the selected date.
	 * (Links to previous code that fetches persistent data).
	 * @param zoneCode
	 * @param date
	 * @return timeslots
	 */
	public Collection obtainHistoricalTimeslots(String zoneCode, Date date){
		return getTimeslotData().getTimeslots(zoneCode, date);
	}
	
	//// ALL METHODS BELOW SIMPLY RETRIEVE AND CALCULATE FOR THE DISPLAY FROM THE INDEXES BY INVOKING
	//// THE CORRESPONDING DlvHistoricalTimeslotData METHOD WITH THE APPROPRIATE TIMESLOT COLLECTION
	
	
	public Collection getWindows(Date date) {
		return getTimeslotIndex().getWindows(date);
	}
	
	public Collection getTimeslots(String zoneCode, Date date) {
		return getZoneIndex().getTimeslots(zoneCode,date);
	}
	
	public Collection getCutoffTimes(Date date, DateRange window) {
		return getTimeslotIndex().getCutoffTimes(date, window);
	}
	
	public Map.Entry getTimeslot(Date date, DateRange window, Date cutoff, String zoneCode) {
		return getTimeslotIndex().getTimeslot(date, window, cutoff, zoneCode);
	}
	
	public int getCapacityTotal(Date date, DateRange window, Date cutoff) {
		return DlvHistoricTimeslotData.getCapacityTotal(getTimeslotIndex().getZoneTimeslots(date, window, cutoff));
	}
	
	public int getCapacityTotal(String zoneCode, Date date) {
		return DlvHistoricTimeslotData.getCapacityTotal(getZoneIndex().getTimeslots(zoneCode, date));
	}
	
	public int getAllocationTotal(Date date, DateRange window, Date cutoff) {
		return DlvHistoricTimeslotData.getAllocationTotal(getTimeslotIndex().getZoneTimeslots(date, window, cutoff),getCurrentDate());
	}
	
	public int getAllocationTotal(String zoneCode, Date date) {
		return DlvHistoricTimeslotData.getAllocationTotal(getZoneIndex().getTimeslots(zoneCode,date),getCurrentDate());
	}
	
	public int getBaseCapacityTotal(Date date, DateRange window, Date cutoff) {
		return DlvHistoricTimeslotData.getBaseCapacityTotal(getTimeslotIndex().getZoneTimeslots(date, window, cutoff));
	}
	
	public int getBaseCapacityTotal(String zoneCode, Date date) {
		return DlvHistoricTimeslotData.getBaseCapacityTotal(getZoneIndex().getTimeslots(zoneCode,date));
	}
	
	public int getBaseAllocationTotal(Date date, DateRange window, Date cutoff) {
		return DlvHistoricTimeslotData.getBaseAllocationTotal(getTimeslotIndex().getZoneTimeslots(date, window, cutoff));
	}
	
	public int getBaseAllocationTotal(String zoneCode, Date date) {
		return DlvHistoricTimeslotData.getBaseAllocationTotal(getZoneIndex().getTimeslots(zoneCode, date));
	}
	
	public int getCTCapacityTotal(Date date, DateRange window, Date cutoff) {
		return DlvHistoricTimeslotData.getCTCapacityTotal(getTimeslotIndex().getZoneTimeslots(date, window, cutoff));
	}
	
	public int getCTCapacityTotal(String zoneCode, Date date) {
		return DlvHistoricTimeslotData.getCTCapacityTotal(getZoneIndex().getTimeslots(zoneCode, date));
	}
	
	public int getCTAllocationTotal(Date date, DateRange window, Date cutoff) {
		return DlvHistoricTimeslotData.getCTAllocationTotal(getTimeslotIndex().getZoneTimeslots(date, window, cutoff));
	}
	
	public int getCTAllocationTotal(String zoneCode, Date date) {
		return DlvHistoricTimeslotData.getCTAllocationTotal(getZoneIndex().getTimeslots(zoneCode, date));
	}
	
	public double getPercentage(DlvTimeslotModel currentTimeslot) {
		int currentAllocation = currentTimeslot.calculateCurrentAllocation(getCurrentDate());
		return 
			currentAllocation == 0 ? 0 :
			((double)currentTimeslot.getChefsTableAllocation())/currentAllocation;
	}

	public double getPercentageTotal(Date date, DateRange window, Date cutoff) {
		return DlvHistoricTimeslotData.getPercentageTotal(getTimeslotIndex().getZoneTimeslots(date, window, cutoff),getCurrentDate());
	}
	
	public double getPercentageTotal(String zoneCode, Date date) {
		return DlvHistoricTimeslotData.getPercentageTotal(getZoneIndex().getTimeslots(zoneCode, date),getCurrentDate());
	}
	
	
	public int getZoneCapacityTotal(String zoneCode) {
		return DlvHistoricTimeslotData.getCapacityTotal(getZoneIndex().getTimeslots(zoneCode));
	}
	
	public int getZoneAllocationTotal(String zoneCode) {
		return DlvHistoricTimeslotData.getAllocationTotal(getZoneIndex().getTimeslots(zoneCode), getCurrentDate());
	}

}
