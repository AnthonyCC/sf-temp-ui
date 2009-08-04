/*
 * $Workfile:DeliveryTimeSlotTag.java$
 *
 * $Date:4/15/03 12:51:23 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

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
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.FDTimeslotList;
import com.freshdirect.fdstore.FDZoneNotFoundException;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision:11$
 * @author $Author:Viktor Szathmary$
 */
public class DeliveryTimeSlotTag extends AbstractGetterTag {

	
	private final static Category LOGGER = Category.getInstance(DeliveryTimeSlotTag.class);
	
	private ErpAddressModel address = null;
	private boolean deliveryInfo = false;
	
	private boolean containsAdvanceOrderItem = false;
	
	public void setAddress(ErpAddressModel address) {
		this.address = address;
	}

	public void setDeliveryInfo(boolean deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	private DateRange getBaseRange() {
		Calendar begCal = Calendar.getInstance();
		begCal.add(Calendar.DATE, 1);
		begCal = DateUtil.truncate(begCal);

		Calendar endCal = Calendar.getInstance();
		endCal.add(Calendar.DATE, 8);
		endCal = DateUtil.truncate(endCal);

		return new DateRange(begCal.getTime(), endCal.getTime());
	}
	
	private DateRange getStandardRange() {
		Calendar begCal = Calendar.getInstance();
		begCal.add(Calendar.DATE, 1);
		begCal = DateUtil.truncate(begCal);

		Calendar endCal = Calendar.getInstance();
		endCal.add(Calendar.DATE, 7);
		endCal = DateUtil.truncate(endCal);

		return new DateRange(begCal.getTime(), endCal.getTime());
	}

	protected Object getResult() throws FDResourceException {

		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

		DateRange baseRange = getBaseRange();
		DateRange geoRestrictionRange = getStandardRange();
		
		DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();

		EnumDlvRestrictionReason specialHoliday = getNextHoliday(restrictions, baseRange, FDStoreProperties
			.getHolidayLookaheadDays());

		LOGGER.debug("specialHoliday :"+specialHoliday);
		
		boolean containsSpecialHoliday = user.getShoppingCart().getApplicableRestrictions().contains(specialHoliday);
		containsAdvanceOrderItem = user.getShoppingCart().hasAdvanceOrderItem();

		LOGGER.debug("containsSpecialHoliday :"+containsSpecialHoliday+" :containsAdvanceOrderItem:"+containsAdvanceOrderItem);
		
		
		List dateRanges = getDateRanges(baseRange, (containsSpecialHoliday && !deliveryInfo), restrictions, specialHoliday, containsAdvanceOrderItem);

		List timeslotList = new ArrayList();
		for (Iterator i = dateRanges.iterator(); i.hasNext();) {
			DateRange range = (DateRange) i.next();
			List timeslots = this.getTimeslots(
				address,
				range.getStartDate(),
				range.getEndDate());
			timeslotList.add(new FDTimeslotList(timeslots, DateUtil.toCalendar(range.getStartDate()), DateUtil.toCalendar(range
				.getEndDate()), restrictions));
		}
		boolean ctActive = false;
		HashMap zonesMap = new HashMap();
		
		// list of timeslots that must be shown regardless of capacity
		Set retainTimeslotIds = new HashSet();
		if (user.getReservation() != null) {
			// make sure current reservation is shown
			retainTimeslotIds.add(user.getReservation().getTimeslotId());
		}
		if (user.getShoppingCart() instanceof FDModifyCartModel) {
			// make sure original reservation still shown
			String tsId = ((FDModifyCartModel)user.getShoppingCart()).getOriginalReservationId();
			retainTimeslotIds.add(tsId);
		}
		
		List geographicRestrictions = FDDeliveryManager.getInstance().getGeographicDlvRestrictions(address);
		List messages = new ArrayList();
		for (Iterator i = timeslotList.iterator(); i.hasNext();) {
			FDTimeslotList list = (FDTimeslotList) i.next();
			for (Iterator j = list.getTimeslots().iterator(); j.hasNext();) {
				Collection col = (Collection) j.next();
				for(Iterator k = col.iterator(); k.hasNext();){
					FDTimeslot timeslot = (FDTimeslot) k.next();
					DlvTimeslotModel ts = timeslot.getDlvTimeslot();
					if ((ts.getCapacity() <= 0 ||  
							GeographyRestriction.isTimeSlotGeoRestricted(geographicRestrictions, timeslot, messages, geoRestrictionRange)) 
								&& !retainTimeslotIds.contains(ts.getId())) {
						// filter off empty timeslots (unless they must be retained)
						k.remove();
					}
					String zoneCode = timeslot.getZoneId();
					if (!zonesMap.containsKey(zoneCode)) {
						try {
							FDDeliveryManager deliveryManager = FDDeliveryManager.getInstance();
							DlvZoneModel zoneModel = deliveryManager.findZoneById(zoneCode);
							if(zoneModel.isCtActive()) {
								ctActive = true;
							}
							zonesMap.put(zoneCode, zoneModel);
						} catch (FDZoneNotFoundException e) {
							LOGGER.error("Referenced zone not found, database error. Zone:"+zoneCode+" timeslot id:"+timeslot.getTimeslotId());
						}
					}
				}
			}
		}  
		
		return new Result(timeslotList, zonesMap, ctActive, messages);
	}
	
		
	private List getTimeslots(ErpAddressModel address, Date startDate, Date endDate) throws FDResourceException {

		if (address instanceof ErpDepotAddressModel) {
			ErpDepotAddressModel depotAddress = (ErpDepotAddressModel) address;
			return FDDeliveryManager.getInstance().getTimeslotsForDepot(
				startDate,
				endDate,
				depotAddress.getRegionId(),
				depotAddress.getZoneCode());
		} else {
			return FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(startDate, endDate, this.address);
		}
		//Removing Depot Option - Sivachandar
		//return FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(startDate, endDate, this.address);
	}

	protected static List getDateRanges (DateRange period, boolean lookahead, DlvRestrictionsList restrictions, EnumDlvRestrictionReason specialRestriction) {
	    return getDateRanges(period,lookahead,restrictions, specialRestriction,false) ;
	}
	
	protected static List getDateRanges(
		DateRange period,
		boolean lookahead,
		DlvRestrictionsList restrictions,
		EnumDlvRestrictionReason specialRestriction, boolean useAdvanceOrderDates) {
	    int daysInAdvance = FDStoreProperties.getHolidayLookaheadDays();
		
		Calendar restrictionEndCal = Calendar.getInstance();
		restrictionEndCal.setTime(period.getStartDate());
		restrictionEndCal.add(Calendar.DATE, daysInAdvance);

		List specialRestrictions = Collections.EMPTY_LIST;
		if (specialRestriction != null) {
			specialRestrictions = restrictions.getRestrictions(
				EnumDlvRestrictionCriterion.DELIVERY,
				specialRestriction,
				null,
				new DateRange(period.getStartDate(), restrictionEndCal.getTime()));

		}

		DateRange restrictionRange=null;
		Calendar tmpCalendar=null;
		for (Iterator i = specialRestrictions.iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof OneTimeRestriction) {
			    restrictionRange = ((OneTimeRestriction) o).getDateRange();
			} else if (o instanceof OneTimeReverseRestriction && useAdvanceOrderDates) {
			    restrictionRange = ((OneTimeReverseRestriction) o).getDateRange();
			}
		}
	
		DateRange advOrdDateRange = FDStoreProperties.getAdvanceOrderRange();
		
		LOGGER.debug("advOrdDateRange :"+advOrdDateRange);
		
		if (useAdvanceOrderDates && advOrdDateRange.overlaps(new DateRange(period.getStartDate(),DateUtil.addDays(period.getStartDate(),daysInAdvance))) ) {
			// get the advanced Dlv date range and factor them into the date range

			// adjust the end and start range 
		    if (restrictionRange==null) {
		        restrictionRange=new DateRange(advOrdDateRange.getStartDate(),advOrdDateRange.getEndDate());
		    } else {
				if ( advOrdDateRange.getEndDate().after(restrictionRange.getEndDate()) ) {
				    restrictionRange = new DateRange(restrictionRange.getStartDate(), advOrdDateRange.getEndDate());
				} else if (daysInAdvance  < (restrictionRange.getStartDate().getTime()-period.getStartDate().getTime()) / DateUtil.DAY){
				    // if the holiday starts after advance range and holiday is not in the lookahead range then use advance order end date
					if (advOrdDateRange.getStartDate().before(restrictionRange.getStartDate()) ) {
					    restrictionRange = new DateRange(advOrdDateRange.getStartDate(), advOrdDateRange.getEndDate());
					} else {
				        restrictionRange = new DateRange(restrictionRange.getStartDate(), advOrdDateRange.getEndDate());
					}
				}
	
				if (advOrdDateRange.getStartDate().before(restrictionRange.getStartDate()) ) {
				    restrictionRange = new DateRange(advOrdDateRange.getStartDate(),restrictionRange.getEndDate());
				}
		    }
		}
				
		// shrink the date range if it is more than 7
		if (restrictionRange != null &&  (restrictionRange.getEndDate().getTime()- restrictionRange.getStartDate().getTime()) / DateUtil.DAY > ErpServicesProperties.getHorizonDays()) {
		    restrictionRange = new DateRange(restrictionRange.getStartDate(),DateUtil.addDays(restrictionRange.getStartDate(),ErpServicesProperties.getHorizonDays()));
		}
		
		LOGGER.debug("restrictionRange :"+restrictionRange);
		
		List lst = new ArrayList();
		int dayDiff = 0;
		if (restrictionRange != null) {
			dayDiff = (int) ((restrictionRange.getStartDate().getTime()- period.getStartDate().getTime()) / DateUtil.DAY);
		}
		
		int remainingDays  = restrictionRange==null ? 0 : (int) ((restrictionRange.getEndDate().getTime() - period.getEndDate().getTime()) / DateUtil.DAY);
		if ( dayDiff > daysInAdvance   
		    || dayDiff < 6 || (!lookahead && !useAdvanceOrderDates) ) {
		    if (dayDiff < 6 && remainingDays > 0) {
		        lst.add (new DateRange(period.getEndDate(),restrictionRange.getEndDate()));
		    }
			lst.add(period);
		} else if (dayDiff > 6) {
			lst.add(restrictionRange);
			lst.add(period);
		} else {
			restrictionRange = new DateRange(DateUtil.addDays(restrictionRange.getStartDate(),1),restrictionRange.getEndDate());
			lst.add(restrictionRange);
			lst.add(period);
		}
		return lst;

	}

	protected static EnumDlvRestrictionReason getNextHoliday(DlvRestrictionsList restrictions, DateRange baseRange, int lookahead) {
		Set holidays = new HashSet();
		for (Iterator i = EnumDlvRestrictionReason.iterator(); i.hasNext();) {
			EnumDlvRestrictionReason reason = (EnumDlvRestrictionReason) i.next();
			if (reason.isSpecialHoliday()) {
				holidays.add(reason);
			}
		}

		DateRange holidayRange = new DateRange(baseRange.getStartDate(), DateUtil.addDays(baseRange.getEndDate(), lookahead));
		for (Iterator i = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, holidays).iterator(); i.hasNext();) {
			RestrictionI r = (RestrictionI) i.next();
			if (r instanceof OneTimeReverseRestriction) {
				OneTimeReverseRestriction rev = (OneTimeReverseRestriction) r;
				if (holidayRange.contains(rev.getDateRange().getStartDate())) {
					return r.getReason();
				}
			}
		}
		return null;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return Result.class.getName();
		}
	}
}