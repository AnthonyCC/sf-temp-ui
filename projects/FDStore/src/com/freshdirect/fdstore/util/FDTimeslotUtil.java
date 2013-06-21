package com.freshdirect.fdstore.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.freshdirect.delivery.DlvZoneCutoffInfo;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;

public class FDTimeslotUtil implements Serializable {
	private static final long	serialVersionUID	= -6937768211070595636L;

	private final static Logger LOGGER = Logger.getLogger(FDTimeslotUtil.class);
	
	private final SortedMap<Date, Map<String, List<FDTimeslot>>> timeslotMap = new TreeMap<Date, Map<String, List<FDTimeslot>>>();
	private final Set<Date> holidaySet = new HashSet<Date>();
	
	//this is date[hours, media path]
	private final SortedMap<String, Map<String, String>> timeslotSpecialMsgsMap = new TreeMap<String, Map<String, String>>();
	
	public SortedMap<String, Map<String, String>> getTimeslotSpecialMsgsMap() {
		return timeslotSpecialMsgsMap;
	}

	private int responseTime; 
	private boolean advanced;
	public FDTimeslotUtil( List<FDTimeslot> timeslots, Calendar startCal, Calendar endCal, DlvRestrictionsList restrictions, int responseTime, boolean advanced ) {
		Collections.sort( timeslots );
		
		for ( FDTimeslot timeslot : timeslots ) {
			
			List<FDTimeslot> shiftTimeslotList = null;
			Map<String,List<FDTimeslot>> shiftMap = null;
			
			shiftMap = timeslotMap.get(timeslot.getBaseDate());
			if(null == shiftMap){
				shiftMap = new HashMap<String, List<FDTimeslot>>();
			}else{
				shiftTimeslotList = shiftMap.get(timeslot.getTimeslotShift());
			}
			
			if(null == shiftTimeslotList || shiftTimeslotList.isEmpty()){
				shiftTimeslotList = new ArrayList<FDTimeslot>();
			}
			shiftTimeslotList.add(timeslot);
			shiftMap.put(timeslot.getTimeslotShift(), shiftTimeslotList);
			timeslotMap.put(timeslot.getBaseDate(), shiftMap);
			
		}
		
		Map<String,List<FDTimeslot>> shiftMap = null;
		while ( startCal.before( endCal ) ) {
			shiftMap = timeslotMap.get( startCal.getTime() );
			if(shiftMap!=null){
				for(List<FDTimeslot> list : shiftMap.values()){
					if ( list == null || restrictions.isRestricted( EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.CLOSED, startCal.getTime() ) ) {
						//timeslotMap.put( startCal.getTime(), new HashMap<String,List<FDTimeslot>>());
						holidaySet.add(startCal.getTime());
					}	
				}		
			}else{
				timeslotMap.put( startCal.getTime(), new HashMap<String,List<FDTimeslot>>());
				if (restrictions.isRestricted( EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.CLOSED, startCal.getTime() ) ) {
					holidaySet.add(startCal.getTime());
				}	
			}			
			startCal.add( Calendar.DATE, 1 );
		}	
		this.setAdvanced(advanced);
		this.setResponseTime(responseTime);
		
		this.parseSpecialMsgsProp();
	}
	
	public boolean hasCapacity() {
		for(Iterator<Map<String, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<String, List<FDTimeslot>> mapCutOff  = itr.next();
			for ( List<FDTimeslot> timeslots : mapCutOff.values() ) {
				for ( FDTimeslot slot : timeslots ) {
					if ( slot.getTotalAvailable() > 0 ) {
						return true;
					}
				}
			}	
		}		
		return false;
	}
	
	public boolean isKosherSlotAvailable( DlvRestrictionsList restrictions ) {
		for ( Date day : timeslotMap.keySet() ) {
			if ( !restrictions.isRestricted( null, EnumDlvRestrictionReason.KOSHER, day ) ) {
				return false;
			}
		}
		return true;
	}
	
	public Date getStartDate() {
		return timeslotMap.firstKey();
	}
	
	public Date getEndDate() {
		return timeslotMap.lastKey();
	}
	
	public Collection<Date> getDays() {
		return timeslotMap.keySet();
	}

	public Collection<Date> getHolidays() {
		return holidaySet;
	}
	
	/**
	 * Returns list of list of FDTimeslot.
	 * @return list of list of FDTimeslot
	 */	
	public Collection<List<FDTimeslot>> getTimeslots() {
		List<List<FDTimeslot>> timeslots = new ArrayList<List<FDTimeslot>>();
		for(Iterator<Map<String, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<String, List<FDTimeslot>> tempMap = itr.next();
			timeslots.addAll(tempMap.values());
		}
		return timeslots;
	}
	
	public List<FDTimeslot> getTimeslotsFlat() {
		List<FDTimeslot> slots = new ArrayList<FDTimeslot>();
		for(Iterator<Map<String, List<FDTimeslot>>> itr = timeslotMap.values().iterator();itr.hasNext();){
			Map<String, List<FDTimeslot>> mapShift  = itr.next();
			for ( List<FDTimeslot> list : mapShift.values() ) {
				for ( FDTimeslot slot : list ) {
					slots.add( slot );
				}
			}		
		}		
		return slots;
	}
	
	public List<FDTimeslot> getTimeslotsForDate( Date day ) {
		List<FDTimeslot> slots = new ArrayList<FDTimeslot>();
		Map<String, List<FDTimeslot>> mapShift  = timeslotMap.get( day );
		if (mapShift == null) {
			LOGGER.error("Failed to request timeslots for day " + day);
			return Collections.<FDTimeslot> emptyList();
		}

		for ( List<FDTimeslot> list : mapShift.values()) {
				for ( FDTimeslot slot : list ) {
					slots.add( slot );
				}
		}
		
		if ( slots.size() > 0 ) {
			Collections.sort(slots);
			return slots;
		} else {
			return Collections.<FDTimeslot> emptyList();
		}
	}
	
	public Date getMaxCutoffForDate( String zoneCode, Date day ) {
		Date cutOff = null;
		List<DlvZoneCutoffInfo> cutoffInfo = null;
		List<Date> cTimes = new ArrayList<Date>();
		try {
			cutoffInfo = FDDeliveryManager.getInstance().getCutofftimeForZone(zoneCode, day);
			
			for(DlvZoneCutoffInfo zoneCutoff : cutoffInfo){
				TimeOfDay timeOfday = zoneCutoff.getCutoffTime();
				cTimes.add(timeOfday.getAsDate());
			}
			if(cTimes.size() > 0){
				Collections.sort(cTimes);
				for (Date _cutoff : cTimes) {
					cutOff = _cutoff;
				}
			}
			if (cutOff != null) {
				Calendar requestedDate = Calendar.getInstance();
				requestedDate.setTime(day);
				requestedDate.add(Calendar.DATE, -1);

				Calendar timeDate = Calendar.getInstance();
				timeDate.setTime(cutOff);
				timeDate.set(Calendar.MONTH, requestedDate.get(Calendar.MONTH));
				timeDate.set(Calendar.DATE, requestedDate.get(Calendar.DATE));
				timeDate.set(Calendar.YEAR, requestedDate.get(Calendar.YEAR));
				cutOff = timeDate.getTime();
			}
		} catch (FDResourceException e) {
			e.printStackTrace();
		}

		return cutOff;
	}
	public RestrictionI getHolidayRestrictionForDate( Date day ) {
		
		Calendar begCal = Calendar.getInstance();
		begCal.setTime(day);
		begCal = DateUtil.truncate(begCal);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(day); 
		endCal.add(Calendar.DATE, 1);
		endCal = DateUtil.truncate(endCal);
		try {
			DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
			List<RestrictionI> r = restrictions.getRestrictions(EnumDlvRestrictionReason.CLOSED, new DateRange(begCal.getTime(),endCal.getTime()));
			if ( r != null ) {
				return r.get(0);
			} else {
				return null;
			}
		} catch (FDResourceException e) {			
			e.printStackTrace();
		}
		return null;
	}
	public FDTimeslot getTimeslot( Date day, Date startTime, Date endTime ) {
		
		Map<String, List<FDTimeslot>> shiftMap  = timeslotMap.get( day );
		for ( List<FDTimeslot> lst : shiftMap.values()) {
			if ( lst != null ) {
				for ( FDTimeslot slot : lst ) {
					if ( slot.isMatching( day, startTime, endTime ) ) {
						return slot;
					}			
				}
			}
		}	
		return null;
	}
	
	public int size() {
		return timeslotMap.size();
	}	
	
	public List<FDTimeslot> getTimeslotsForDayAndShift( Date day, String shift ) {
		
		Map<String,List<FDTimeslot>> tempMap = timeslotMap.get(day);
		if(tempMap.isEmpty()){
			return Collections.<FDTimeslot> emptyList();
		}else{
			List<FDTimeslot> lst = tempMap.get( shift );
			if ( lst != null ) {
				return lst;
			} else {
				return Collections.<FDTimeslot> emptyList();
			}
		}
	}
	
	public int getNumDayShiftTimeslots( Date day, String shift ) {
		
		Map<String,List<FDTimeslot>> tempMap= timeslotMap.get(day);
		if(tempMap.isEmpty()){
			return 0;
		}else{
			List<FDTimeslot> lst = tempMap.get( shift );
			if ( lst != null ) {
				return lst.size();
			} else {
				return 0;
			}
		}
	}

	public int getMaxNumShiftTimeslots(String shift) {
		Collection<Date> days = this.getDays();
		int maxTimeslots = 0; 
		for (Date day : days) {
			List<FDTimeslot> timeslots = this.getTimeslotsForDayAndShift(day, shift);
			if(timeslots.size()>=maxTimeslots)
				maxTimeslots = timeslots.size();					
		}
		
		return maxTimeslots;
	}
	
	public boolean isSelectedTimeslot( Date day, String timeslotId ) {
		
		Map<String, List<FDTimeslot>> mapCutOff  = timeslotMap.get( day );
		for ( List<FDTimeslot> lst : mapCutOff.values()) {
			if ( lst != null ) {
				for ( FDTimeslot slot : lst ) {
					if ( slot.getTimeslotId().equals(timeslotId) ) {
						return true;
					}			
				}
			}
		}	
		return false;
	}
	
	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}
	
	public void removeTimeslots(Date baseDate)
	{
		timeslotMap.remove(baseDate);
	}

	public boolean isAdvanced() {
		return advanced;
	}

	public void setAdvanced(boolean advanced) {
		this.advanced = advanced;
	}
	
	//parse prop and set to timeslotSpecialMsgsMap
	public void parseSpecialMsgsProp() {
		String propStr = FDStoreProperties.getTSSpecialMessaging();
		String dateKeyQuote = "'";
	    Random randomGenerator = new Random();
		String dateKey = null;
		
		if (propStr !=null && !"".equals(propStr)) {
			//split in to configs
			for (String configStr : propStr.split(";")) {

				List<String> tempDates = new ArrayList<String>();
				List<String> tempHours = new ArrayList<String>();
				String tempMedia = "";
				
				//split in to config sections
				for (String configSectionStr : configStr.split(":")) {
					
					//split in to WHICH section
					String[] configSectionKnownStr = configSectionStr.split("=");
					if (configSectionKnownStr.length == 2) {
						if ("d".equalsIgnoreCase(configSectionKnownStr[0]) || "h".equalsIgnoreCase(configSectionKnownStr[0]) ) {
							//dates or hours

							//split in to values
							for (String configSectionValueStr : configSectionKnownStr[1].split(",")) {
								
								String[] configSectionValueRangeStr = configSectionValueStr.split("-"); //split in to a range
								if (configSectionValueRangeStr.length == 1) { //single value
									//note that this can be an invalid value, or the wildcard "*". front-end will handle that
									if ("d".equalsIgnoreCase(configSectionKnownStr[0])) { //date

										dateKey = configSectionValueRangeStr[0];
										if (timeslotSpecialMsgsMap.containsKey(dateKeyQuote+dateKey+dateKeyQuote)) {
											dateKey += "_"+randomGenerator.nextInt(10000);
											while (timeslotSpecialMsgsMap.containsKey((String)dateKeyQuote+dateKey+dateKeyQuote)) {
												dateKey += randomGenerator.nextInt(10000);
											}
										}
										tempDates.add( dateKeyQuote+dateKey+dateKeyQuote );
									} else if ("h".equalsIgnoreCase(configSectionKnownStr[0])) { //hour
										tempHours.add("'"+configSectionValueRangeStr[0]+"'");
									}
								} else if (configSectionValueRangeStr.length == 2) { //range
									Calendar startCal = Calendar.getInstance();
									Calendar endCal = Calendar.getInstance();
									
									if ("d".equalsIgnoreCase(configSectionKnownStr[0])) { //date
	
										String dateStart[] = configSectionValueRangeStr[0].split("/");
										String dateEnd[] = configSectionValueRangeStr[1].split("/");
										
										if (dateStart.length == 3 && dateEnd.length == 3) {
											try {
												int dateStartM = Integer.parseInt(dateStart[0])-1;
												int dateStartD = Integer.parseInt(dateStart[1]);
												int dateStartY = Integer.parseInt(dateStart[2]);
												int dateEndM = Integer.parseInt(dateEnd[0])-1;
												int dateEndD = Integer.parseInt(dateEnd[1]);
												int dateEndY = Integer.parseInt(dateEnd[2]);
												
												//add each date in range
												startCal.set(dateStartY, dateStartM, dateStartD);
												
												endCal.set(dateEndY, dateEndM, dateEndD);
												
												if (startCal.before(endCal)) {
													
													while (startCal.before(endCal)) {
														String dayLeadZero = "";
														String monthLeadZero = "";
														
														if ( (startCal.get(Calendar.MONTH)+1) < 10) { monthLeadZero = "0"; }
														if ( startCal.get(Calendar.DATE) < 10) { dayLeadZero = "0"; }
														
														dateKey = monthLeadZero+(startCal.get(Calendar.MONTH)+1)+"/"+dayLeadZero+startCal.get(Calendar.DATE)+"/"+startCal.get(Calendar.YEAR);
														if (timeslotSpecialMsgsMap.containsKey(dateKeyQuote+dateKey+dateKeyQuote)) {
															dateKey += "_"+randomGenerator.nextInt(10000);
															while (timeslotSpecialMsgsMap.containsKey(dateKeyQuote+dateKey+dateKeyQuote)) {
																dateKey += randomGenerator.nextInt(10000);
															}
														}
														tempDates.add( dateKeyQuote+dateKey+dateKeyQuote );
														startCal.add(Calendar.DATE, 1);
													}
													String dayLeadZero = "";
													String monthLeadZero = "";
													if ( (endCal.get(Calendar.MONTH)+1) < 10) { monthLeadZero = "0"; }
													if ( endCal.get(Calendar.DATE) < 10) { dayLeadZero = "0"; }
													
													dateKey = monthLeadZero+(endCal.get(Calendar.MONTH)+1)+"/"+dayLeadZero+endCal.get(Calendar.DATE)+"/"+endCal.get(Calendar.YEAR);
													if (timeslotSpecialMsgsMap.containsKey(dateKeyQuote+dateKey+dateKeyQuote)) {
														dateKey += "_"+randomGenerator.nextInt(10000);
														while (timeslotSpecialMsgsMap.containsKey(dateKeyQuote+dateKey+dateKeyQuote)) {
															dateKey += randomGenerator.nextInt(10000);
														}
													}
													tempDates.add( dateKeyQuote+dateKey+dateKeyQuote );
												}
											} catch (NumberFormatException nfe) {
											
											}
											
										}
									} else if ("h".equalsIgnoreCase(configSectionKnownStr[0])) { //hour
										try {
											int startHour = Integer.parseInt(configSectionValueRangeStr[0]);
											int endHour = Integer.parseInt(configSectionValueRangeStr[1]);
											

											startCal.set(Calendar.HOUR_OF_DAY, startHour);
											endCal.set(Calendar.HOUR_OF_DAY, endHour);
											
											if (startCal.before(endCal)) {
												while (startCal.before(endCal)) {
													tempHours.add( "'"+Integer.toString(startCal.get(Calendar.HOUR_OF_DAY))+"'" );
													startCal.add(Calendar.HOUR_OF_DAY, 1);
												}
												tempHours.add( "'"+Integer.toString(endCal.get(Calendar.HOUR_OF_DAY))+"'" );
											}
											
										} catch (NumberFormatException nfe) {
											
										}
									}
								
								}
							}
							
						} else if ("m".equalsIgnoreCase(configSectionKnownStr[0])) {
							//media (can only be one)
							tempMedia = "'"+configSectionKnownStr[1]+"'";
						}
					}
					
				} //end split ":"
				
				//we have all our values, put them in to the hashmap (if valid)
				if (!"".equals(tempMedia) && tempHours.size() > 0 && tempDates.size() > 0) {
					for (String curDate : tempDates) {
						HashMap<String, String> hoursMedia = new HashMap<String, String>();
						for (String curHour : tempHours) {
							hoursMedia.put(curHour, tempMedia);
						}
						timeslotSpecialMsgsMap.put(curDate, hoursMedia);
					}
				}
			} //end split ";"
		}
	}
}
