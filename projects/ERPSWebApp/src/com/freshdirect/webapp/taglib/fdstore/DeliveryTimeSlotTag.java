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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdlogistics.model.FDDeliveryTimeslots;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdlogistics.model.FDTimeslotList;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.PromotionHelper;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.DlvTimeslotStats;
import com.freshdirect.fdstore.util.FDTimeslotUtil;
import com.freshdirect.fdstore.util.RestrictionUtil;
import com.freshdirect.fdstore.util.TimeslotContext;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.logistics.delivery.model.EnumOrderAction;
import com.freshdirect.logistics.delivery.model.EnumOrderType;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.util.StandingOrderHelper;


/**
 *
 * @version $Revision:11$
 * @author $Author:Viktor Szathmary$
 */
public class DeliveryTimeSlotTag extends AbstractGetterTag<Result> {
	private static final long serialVersionUID = 4638621907476643671L;
	private final static Logger LOGGER = LoggerFactory.getInstance(DeliveryTimeSlotTag.class);



	private ErpAddressModel address = null;
	private boolean deliveryInfo = false;
	private boolean returnSameDaySlots = true;
	
	private TimeslotContext timeSlotContext = null;
	
	// selected timeslot ID
	private String timeSlotId = "";

	private boolean forceOrder = false;


	/**
	 * Flag that indicates generic timeslots will be returned
	 */
	private boolean generic = false;
	
	public void setAddress(ErpAddressModel address) {
		this.address = address;
	}

	public void setDeliveryInfo(boolean deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}	

	public void setTimeSlotContext(TimeslotContext timeSlotContext) {
		this.timeSlotContext = timeSlotContext;
	}

	public void setTimeSlotId(String timeSlotId) {
		this.timeSlotId = timeSlotId;
	}
	
	
	public void setReturnSameDaySlots(boolean returnSameDaySlots) {
		this.returnSameDaySlots = returnSameDaySlots;
	}

	/**
	 * Used in CRM interface
	 * 
	 * @param forceOrder
	 */
	public void setForceOrder(boolean forceOrder) {
		this.forceOrder = forceOrder;
	}


	/**
	 * Tag setter method
	 * 
	 * @param generic
	 */
	public void setGeneric(boolean generic) {
		this.generic = generic;
	}
	
	
	
	private DateRange createDateRange(int begin, int end) {
		Calendar begCal = Calendar.getInstance();
		begCal.add(Calendar.DATE, begin);
		begCal = DateUtil.truncate(begCal);

		Calendar endCal = Calendar.getInstance();
		endCal.add(Calendar.DATE, end);
		endCal = DateUtil.truncate(endCal);

		return new DateRange(begCal.getTime(), endCal.getTime());
	}

	@Override
	protected Result getResult() throws FDResourceException {
		if (address == null) {
			return null;
		}
		
		// [APPDEV-2149] go a different way
		if (generic) {
			return getGenericTimeslots();
		}
					
		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		FDDeliveryTimeslotModel deliveryModel = new FDDeliveryTimeslotModel();

		final FDCartModel cart = user.getShoppingCart();
		Result result = new Result();
		//check if preReservedSlotId exits
		checkForPreReservedSlotId(deliveryModel, cart, user, timeSlotId);
		
		//getRestriction reasons
		getRestrictionReason(cart, deliveryModel);
		
		boolean showPremiumSlots = false;
		
		DateRange baseRange = createDateRange(1, 8);
		
		/*if(EnumEStoreId.FDX.name().equals(user.getUserContext().getStoreContext().getEStoreId())){
			baseRange = createDateRange(0, 7);
		}*/
		
		
		DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();

		// set standing order
		if (user.getCheckoutMode().isModifyStandingOrder()){
			deliveryModel.setCurrentStandingOrder(user.getCurrentStandingOrder());
		}
	


		if(user != null && user.getIdentity() != null 
									&& !StringUtil.isEmpty(user.getIdentity().getErpCustomerPK())) {
			address.setCustomerId(user.getIdentity().getErpCustomerPK());
		}
				
		TimeslotEvent event = new TimeslotEvent((user.getApplication()!=null)?user.getApplication().getCode():"",
				cart.isDlvPassApplied(),cart.getDeliverySurcharge(), cart.isDeliveryChargeWaived(),
				(cart.getZoneInfo()!=null)?cart.getZoneInfo().isCtActive():false, user.getPrimaryKey(), EnumCompanyCode.fd.name());
		
		event.setLogged(false);
		if("GET".equalsIgnoreCase(request.getMethod()) ||
		"Y".equals(request.getParameter("addressChange"))){
			event.setLogged(true);
		}
		
		EnumDlvRestrictionReason specialHoliday = getNextHoliday(restrictions, baseRange, FDStoreProperties
				.getHolidayLookaheadDays());

		LOGGER.debug("specialHoliday :"+specialHoliday);
			
		final boolean containsSpecialHoliday = cart.getApplicableRestrictions().contains(specialHoliday);
		final boolean containsAdvanceOrderItem = cart.hasAdvanceOrderItem();

		LOGGER.debug("containsSpecialHoliday :"+containsSpecialHoliday+" :containsAdvanceOrderItem:"+containsAdvanceOrderItem);
		
		List<DateRange> dateRanges = new ArrayList<DateRange>();
		
		dateRanges = getDateRanges(baseRange,
				(containsSpecialHoliday && !deliveryInfo), restrictions,specialHoliday, containsAdvanceOrderItem);
		
		Collections.sort(dateRanges, new DateRangeComparator());
		
		/*Holiday & specialItems restrictions*/
		getHolidayRestrictions(restrictions, dateRanges, deliveryModel);
		
		DlvTimeslotStats stats = new DlvTimeslotStats();
		
		// saving the reservation in the timeslot event 
		
		try{
			if (deliveryModel.getRsv()!=null && deliveryModel.isPreReserved())
				event.setReservationId(deliveryModel.getRsv().getId());
			
			if (cart instanceof FDModifyCartModel
					&& deliveryModel.getRsv() != null
					&& (address.getPK() != null
							&& address.getPK().getId() != null && address
							.getPK().getId()
							.equals(deliveryModel.getRsv().getAddressId())))
				event.setReservationId(deliveryModel.getRsv().getId());
		}catch(Exception e){
			//eat it
			LOGGER.info("reservation capture in timeslot event failed");
		}

		
		List<FDTimeslotUtil> timeslotList = getFDTimeslotListForDateRange(restrictions, dateRanges,
				result, address, user, deliveryModel, event, stats);
		
		if(!returnSameDaySlots || (timeSlotContext!=null && !timeSlotContext.equals(TimeslotContext.CHECKOUT_TIMESLOTS))) {		
			TimeslotLogic.purgeSDSlots(timeslotList);
		}
				
		showPremiumSlots =TimeslotLogic.hasPremiumSlots(timeslotList);
				
		deliveryModel.setShowPremiumSlots(showPremiumSlots);
				
		// list of timeslots that must be shown regardless of capacity
		Set<String> retainTimeslotIds = new HashSet<String>();
		if (user.getReservation() != null) {
			// make sure current reservation is shown
			retainTimeslotIds.add(user.getReservation().getTimeslotId());
		}
		if (cart instanceof FDModifyCartModel) {
			// make sure original reservation still shown
			String tsId = ((FDModifyCartModel)cart).getOriginalReservationId();
			retainTimeslotIds.add(tsId);
		}
					
		List<RestrictionI> r = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY,getAlcoholRestrictionReasons(cart),baseRange);
		//Filter Alcohol restrictions by current State and county.
		final String county = FDDeliveryManager.getInstance().getCounty(address);
		List<RestrictionI> alcoholRestrictions = RestrictionUtil.filterAlcoholRestrictionsForStateCounty(address.getState(), county, r);

		LOGGER.debug("AlcoholRestrictions :"+alcoholRestrictions.size());

		/*setDiscounts & apply Geo-restrictions to timeslots*/
		
		TimeslotLogic.filterDeliveryTimeSlots(user, restrictions, timeslotList, retainTimeslotIds,
				deliveryModel, alcoholRestrictions,
				forceOrder, address, generic, stats);

		// TimeSlot event specific block
		{
			event.setSameDay(deliveryModel.isShowPremiumSlots()?"X":"");
			// build session event
			if (FDStoreProperties.isSessionLoggingEnabled() && result.isSuccess() &&
					(
						"GET".equalsIgnoreCase(request.getMethod()) ||
						"Y".equals(request.getParameter("addressChange")))
					)
			{
				TimeslotLogic.logTimeslotSessionInfo(user, address, deliveryInfo, timeslotList, event);
			}
			
		}


		
		// Post-op: remove unnecessary timeslots
		TimeslotLogic.purge(timeslotList);
		
		// unflag variable min order timeslots for deliveryinfo timeslots
		if(timeSlotContext!=null && !timeSlotContext.equals(TimeslotContext.CHECKOUT_TIMESLOTS)){
			TimeslotLogic.clearVariableMinimum(user, timeslotList);
			deliveryModel.setMinOrderReqd(false);
		}

		deliveryModel.setTimeslotList(timeslotList);
		
		stats.apply(deliveryModel);
		// update chefs table stats in user
		stats.apply(user);
		
		user.applyOrderMinimum();
		
		deliveryModel.setZoneId(cart.getDeliveryZone());		
		
		//get zone Promotion amount
		deliveryModel.setZonePromoAmount(PromotionHelper.getDiscount(user, deliveryModel.getZoneId()));
		//set cart to model
		deliveryModel.setShoppingCart(cart);
		
		deliveryModel.setSameDayCutoff(stats.getSameDayCutoff());
		deliveryModel.setSameDayCutoffUTC(stats.getSameDayCutoffUTC());
		
		result.setDeliveryTimeslotModel(deliveryModel);
		
		return result;
	}
	
	
	
	private Result getGenericTimeslots() throws FDResourceException {
		final FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		final FDCartModel cart = user.getShoppingCart();
		final FDStandingOrder so = user.getCurrentStandingOrder();


		final Result result = new Result();
		final FDDeliveryTimeslotModel deliveryModel = new FDDeliveryTimeslotModel();
	
		DateRange range = createDateRange(1,8);
		List<DateRange> ranges = new ArrayList<DateRange>();
		ranges.add(range);
		
		// collect all restrictions
		DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();
		// narrow down restrictions
		LOGGER.debug("All Restrictions (before OTR filter): "  + restrictions.size());
		restrictions.keepRestrictionsForGenericTimeslots();
		LOGGER.debug("All Restrictions: " + restrictions.size());
		
		
		FDTimeslotUtil tsu; // we want only a single set of time slots (see: multiple sets in case of advance orders)
		List<FDTimeslotUtil> singleTSset = new ArrayList<FDTimeslotUtil>();
		FDDeliveryTimeslots deliveryTimeslots = null;
		{

			TimeslotEvent event = new TimeslotEvent((user.getApplication()!=null)?user.getApplication().getCode():"",
					cart.isDlvPassApplied(),cart.getDeliverySurcharge(), cart.isDeliveryChargeWaived(),
					(cart.getZoneInfo()!=null)?cart.getZoneInfo().isCtActive():false, user.getPrimaryKey(), EnumCompanyCode.fd.name());

			event.setLogged(false);
			if("GET".equalsIgnoreCase(request.getMethod()) ||
			"Y".equals(request.getParameter("addressChange"))){
				event.setLogged(true);
			}
			
			// Fetch time slots
			deliveryTimeslots = FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(
					ranges, event, TimeslotLogic.encodeCustomer(address, user), 
					TimeslotLogic.getOrderContext(EnumOrderAction.CREATE, user.getIdentity().getErpCustomerPK(), EnumOrderType.SO_TEMPLATE));
			
			FDTimeslotList tsList = deliveryTimeslots.getTimeslotList().get(0);
			
			tsu = new FDTimeslotUtil(tsList.getTimeslots(), range.getStartDate(), range.getEndDate(), restrictions, tsList.getResponseTime(),range.isAdvanced(), null);
			singleTSset.add( tsu );
		}


		List<RestrictionI> alcoholRestrictions;
		{
			List<RestrictionI> r = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, getAlcoholRestrictionReasons(cart), range);
			//Filter Alcohol restrictions by current State and county.
			final String county = FDDeliveryManager.getInstance().getCounty(address);
			alcoholRestrictions = RestrictionUtil.filterAlcoholRestrictionsForStateCounty(address.getState(), county, r);

			LOGGER.debug("Alcohol restrictions" + alcoholRestrictions);
		}
	
		DlvTimeslotStats stats = new DlvTimeslotStats();
		
		stats.apply(deliveryTimeslots);
		
		TimeslotLogic.filterDeliveryTimeSlots(user, restrictions, singleTSset,
				Collections.<String>emptySet(), deliveryModel, alcoholRestrictions,
				false, address,
				true, stats);
		// Post-op: remove unnecessary timeslots
		TimeslotLogic.purge(singleTSset);

		deliveryModel.setTimeslotList(singleTSset);
		stats.apply(deliveryModel);
		// update chefs table stats in user
		stats.apply(user);


		// unflag variable min order timeslots for deliveryinfo timeslots
		// TimeslotLogic.clearVariableMinimum(user, singleTSset);
		// deliveryModel.setMinOrderReqd(false);
		
		// fill in delivery model
		deliveryModel.setShoppingCart(cart);
		deliveryModel.setCurrentStandingOrder(user.getCurrentStandingOrder()); // <== ??? does it matter here?
		deliveryModel.setZoneId(cart.getDeliveryZone());		
		deliveryModel.setZonePromoAmount(PromotionHelper.getDiscount(user, deliveryModel.getZoneId()));
		
		// set selected timeslot ID according to next delivery date of standing order template
		deliveryModel.setTimeSlotId( StandingOrderHelper.findMatchingSlot(so, tsu));
		deliveryModel.setPreReserved( false );
		deliveryModel.setPreReserveSlotId( null );
		
		// cheat the capacity
		deliveryModel.setHasCapacity( true );
		
		result.setDeliveryTimeslotModel(deliveryModel);
		return result;
	}

	private List<FDTimeslotUtil> getFDTimeslotListForDateRange(DlvRestrictionsList restrictions, List<DateRange> dateRanges, ActionResult result,
			ErpAddressModel timeslotAddress,FDUserI user, FDDeliveryTimeslotModel deliveryModel, TimeslotEvent event, DlvTimeslotStats stats) throws FDResourceException {
		
		List<FDTimeslotUtil> tsuList = new ArrayList<FDTimeslotUtil>();
		int responseTime;
		
		FDDeliveryTimeslots deliveryTimeslots = FDDeliveryManager.getInstance().
					getTimeslotsForDateRangeAndZone(dateRanges, event, TimeslotLogic.encodeCustomer(address, user), forceOrder, deliveryInfo, 
							TimeslotLogic.getOrderContext(user));
			
		for(FDTimeslotList timeslotList : deliveryTimeslots.getTimeslotList()) {	
			
			if(timeslotList == null || timeslotList.getError() != null) {
					result.addError(new ActionError("deliveryTime", "We are sorry. Our system is temporarily experiencing a problem " +
							"displaying the available timeslots. Please try to refresh this page in about three minutes. " +
							"If you continue to experience difficulties loading this page, " +
							"please call our customer service department"+
							(user != null ? " at " + user.getCustomerServiceContact() : "")));				
			} 
			
			List<FDTimeslot> timeslots = timeslotList.getTimeslots();
			responseTime = timeslotList.getResponseTime();
				
			tsuList.add(new FDTimeslotUtil(timeslots, timeslotList.getRange().getStartDate(),
					timeslotList.getRange().getEndDate(), restrictions, responseTime, 
					timeslotList.isAdvanced(), timeslotList.getEventPk()));
			
		}
		
		stats.apply(deliveryTimeslots);
		deliveryModel.apply(deliveryTimeslots);
		return tsuList;
	}


	
	private void checkForPreReservedSlotId(FDDeliveryTimeslotModel deliverymodel,FDCartModel cart, FDUserI user, String timeSlotId) throws FDResourceException{
		FDReservation rsv = null;
		boolean hasPreReserved = false;
		String preReserveSlotId = "";
		ErpDepotAddressModel depotAddress = null;		
		
		EnumCheckoutMode checkOutMode = user.getCheckoutMode();
		if ( EnumCheckoutMode.NORMAL == checkOutMode || EnumCheckoutMode.CREATE_SO == checkOutMode || EnumCheckoutMode.MODIFY_SO_MSOI == checkOutMode ) {
			rsv = user.getReservation();
			if(rsv != null){
				preReserveSlotId = rsv.getTimeslotId();
				hasPreReserved = address.getPK()!=null && address.getPK().getId().equals(rsv.getAddressId());
			}
			if (cart.getDeliveryReservation() != null) {
				rsv = cart.getDeliveryReservation();
			}
			if (timeSlotId == null) {
				if(address != null && address instanceof ErpDepotAddressModel){
					depotAddress = (ErpDepotAddressModel) address;
					if(depotAddress.isPickup() && rsv != null && cart.getDeliveryReservation() != null)
						timeSlotId = rsv.getTimeslotId();						
					else
						timeSlotId = "";											
				}else{
					if(rsv != null && (address != null && address.getPK() != null && address.getPK().getId() != null 
							&& address.getPK().getId().equals(rsv.getAddressId())) && cart.getDeliveryReservation() != null){
						timeSlotId = rsv.getTimeslotId();
					}else{
						timeSlotId = "";
					}
				}
			}
		
			if(cart instanceof FDModifyCartModel && rsv != null && !EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType()) && "".equals(preReserveSlotId) && !hasPreReserved){
				preReserveSlotId = rsv.getTimeslotId();
				hasPreReserved = address.getPK()!=null && address.getPK().getId().equals(rsv.getAddressId());
			}
			
		} else {
			timeSlotId = "";
		}
		
		deliverymodel.setTimeSlotId(timeSlotId); // this is the ID of selected timeslot		
		deliverymodel.setPreReserved(hasPreReserved);
		deliverymodel.setPreReserveSlotId(preReserveSlotId);
		deliverymodel.setRsv(rsv);
	}

	protected static List<DateRange> getDateRanges (DateRange period, boolean lookahead, DlvRestrictionsList restrictions, EnumDlvRestrictionReason specialRestriction) {
	    return getDateRanges(period,lookahead,restrictions, specialRestriction,false) ;
	}
	
	@SuppressWarnings("unchecked")
	protected static List<DateRange> getDateRanges(
		DateRange period,
		boolean lookahead,
		DlvRestrictionsList restrictions,
		EnumDlvRestrictionReason specialRestriction, boolean useAdvanceOrderDates) {
	    int daysInAdvance = FDStoreProperties.getHolidayLookaheadDays();

		Calendar restrictionEndCal = Calendar.getInstance();
		restrictionEndCal.setTime(period.getStartDate());
		restrictionEndCal.add(Calendar.DATE, daysInAdvance);

		List<RestrictionI> specialRestrictions = Collections.EMPTY_LIST;
		if (specialRestriction != null) {
			specialRestrictions = restrictions.getRestrictions(
				EnumDlvRestrictionCriterion.DELIVERY,
				specialRestriction,
				null,
				new DateRange(period.getStartDate(), restrictionEndCal.getTime()));

		}

		DateRange restrictionRange=null;
		for (Iterator<RestrictionI> i = specialRestrictions.iterator(); i.hasNext();) {
			RestrictionI o = i.next();
			if (o instanceof OneTimeRestriction) {
			    restrictionRange = ((OneTimeRestriction) o).getDateRange();
			} else if (o instanceof OneTimeReverseRestriction && useAdvanceOrderDates) {
			    restrictionRange = ((OneTimeReverseRestriction) o).getDateRange();
			}
		}
	
		DateRange advOrdDateRange = FDStoreProperties.getAdvanceOrderRange();
		boolean isAdvOrderGap = FDStoreProperties.IsAdvanceOrderGap();
		DateRange advOrdNewDateRange = FDStoreProperties.getAdvanceOrderNewRange();
		DateRange restrictionRangeNew=null;
		
		LOGGER.debug("advOrdDateRange :"+advOrdDateRange);
		LOGGER.debug("advOrdNewDateRange :"+advOrdNewDateRange);
		
		if (useAdvanceOrderDates && advOrdDateRange.overlaps(new DateRange(period.getStartDate(),DateUtil.addDays(period.getStartDate(),daysInAdvance))) ) {
			// get the advanced Dlv date range and factor them into the date range

			// adjust the end and start range 
		    if (restrictionRange==null) {
		        restrictionRange=new DateRange(advOrdDateRange.getStartDate(),advOrdDateRange.getEndDate(), true);
		    } else {
				if ( advOrdDateRange.getEndDate().after(restrictionRange.getEndDate()) ) {
				    restrictionRange = new DateRange(restrictionRange.getStartDate(), advOrdDateRange.getEndDate(), true);
				} else if (daysInAdvance  < (restrictionRange.getStartDate().getTime()-period.getStartDate().getTime()) / DateUtil.DAY){
				    // if the holiday starts after advance range and holiday is not in the lookahead range then use advance order end date
					if (advOrdDateRange.getStartDate().before(restrictionRange.getStartDate()) ) {
					    restrictionRange = new DateRange(advOrdDateRange.getStartDate(), advOrdDateRange.getEndDate(), true);
					} else {
				        restrictionRange = new DateRange(restrictionRange.getStartDate(), advOrdDateRange.getEndDate(), true);
					}
				}
	
				if (advOrdDateRange.getStartDate().before(restrictionRange.getStartDate()) ) {
				    restrictionRange = new DateRange(advOrdDateRange.getStartDate(),restrictionRange.getEndDate(), true);
				}
		    }
		}
		
		//Get the second advance order range for gaps
		if (useAdvanceOrderDates && advOrdNewDateRange.overlaps(new DateRange(period.getStartDate(),DateUtil.addDays(period.getStartDate(),daysInAdvance))) ) {
			// adjust the end and start range 
	    	restrictionRangeNew=new DateRange(advOrdNewDateRange.getStartDate(),advOrdNewDateRange.getEndDate(), true);
		}//Advance order range for gaps
		
		// shrink the date range if it is more than 7
		if (restrictionRange != null && !isAdvOrderGap &&  (restrictionRange.getEndDate().getTime()- restrictionRange.getStartDate().getTime()) / DateUtil.DAY > ErpServicesProperties.getHorizonDays()) {
		    restrictionRange = new DateRange(restrictionRange.getStartDate(),DateUtil.addDays(restrictionRange.getStartDate(),ErpServicesProperties.getHorizonDays()), true);
		}else if((restrictionRange != null && isAdvOrderGap && (restrictionRange.getEndDate().getTime()- restrictionRange.getStartDate().getTime()) / DateUtil.DAY >= ErpServicesProperties.getHorizonDays()) ||
				restrictionRangeNew != null && isAdvOrderGap && (restrictionRangeNew.getEndDate().getTime()- restrictionRangeNew.getStartDate().getTime()) / DateUtil.DAY >= ErpServicesProperties.getHorizonDays()){
			// if there is advance order timeslots with a gap then read the second date range from properties
			restrictionRange = new DateRange(advOrdDateRange.getStartDate(), advOrdDateRange.getEndDate(), true);
			restrictionRangeNew = new DateRange(advOrdNewDateRange.getStartDate(),advOrdNewDateRange.getEndDate(), true);
			
		}
		
		LOGGER.debug("restrictionRange :"+restrictionRange);
		LOGGER.debug("restrictionRangeNew  :"+restrictionRangeNew);
		
		List<DateRange> lst = new ArrayList<DateRange>();
		int dayDiff = 0;
		if (restrictionRange != null) {
			dayDiff = (int) ((restrictionRange.getStartDate().getTime()- period.getStartDate().getTime()) / DateUtil.DAY);
		}
		
		int remainingDays  = restrictionRange==null ? 0 : (int) ((restrictionRange.getEndDate().getTime() - period.getEndDate().getTime()) / DateUtil.DAY);
		
		// If there is advance order with gap read the second date range along with first range
		if(isAdvOrderGap){
			int dayDiffNew = 0;
			if (restrictionRangeNew != null) {
				dayDiffNew = (int) ((restrictionRangeNew.getStartDate().getTime()- period.getStartDate().getTime()) / DateUtil.DAY);
			}
			int remainingDaysNew  = restrictionRangeNew==null ? 0 : (int) ((restrictionRangeNew.getEndDate().getTime() - period.getEndDate().getTime()) / DateUtil.DAY);
			
			//read the first advance order date range
			if(dayDiff > daysInAdvance   
				    || dayDiff < 6 || (!lookahead && !useAdvanceOrderDates)){
				if (dayDiff < 6 && remainingDays > 0) {
			        lst.add (new DateRange(period.getEndDate(),restrictionRange.getEndDate(),true));
			    }else{
			    	
			    }
				
			}else if (dayDiff > 6) {
				lst.add(restrictionRange);
			} else{
				restrictionRange = new DateRange(DateUtil.addDays(restrictionRange.getStartDate(),1),restrictionRange.getEndDate(),true);
				lst.add(restrictionRange);
			}
			
			//read the second advance order date range
			if(dayDiffNew > daysInAdvance   
				    || dayDiffNew < 6 || (!lookahead && !useAdvanceOrderDates)){
				if (dayDiffNew < 6 && remainingDaysNew > 0) {
			        lst.add (new DateRange(period.getEndDate(),restrictionRangeNew.getEndDate(),true));
			    }
				
			}else if (dayDiffNew > 6) {
				lst.add(restrictionRangeNew);
			} else{
				restrictionRangeNew = new DateRange(DateUtil.addDays(restrictionRangeNew.getStartDate(),1),restrictionRangeNew.getEndDate(),true);
				lst.add(restrictionRangeNew);
			}
			lst.add(period);
		}else if ( dayDiff > daysInAdvance   
		    || dayDiff < 6 || (!lookahead && !useAdvanceOrderDates) ) {
		    if (dayDiff < 6 && remainingDays > 0) {
		        lst.add (new DateRange(period.getEndDate(),restrictionRange.getEndDate(),true));
		    }
			lst.add(period);
		} else if (dayDiff > 6) {
			lst.add(restrictionRange);
			lst.add(period);
		} else {
			restrictionRange = new DateRange(DateUtil.addDays(restrictionRange.getStartDate(),1),restrictionRange.getEndDate(),true);
			lst.add(restrictionRange);
			lst.add(period);
		}

		return lst;

	}

	protected static EnumDlvRestrictionReason getNextHoliday(DlvRestrictionsList restrictions, DateRange baseRange, int lookahead) {
		Set<EnumDlvRestrictionReason> holidays = new HashSet<EnumDlvRestrictionReason>();
		for (@SuppressWarnings("unchecked")
		Iterator<EnumDlvRestrictionReason> i = EnumDlvRestrictionReason.iterator(); i.hasNext();) {
			EnumDlvRestrictionReason reason = (EnumDlvRestrictionReason) i.next();
			if (reason.isSpecialHoliday()) {
				holidays.add(reason);
			}
		}

		DateRange holidayRange = new DateRange(baseRange.getStartDate(), DateUtil.addDays(baseRange.getEndDate(), lookahead));
		for (Iterator<RestrictionI> i = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, holidays).iterator(); i.hasNext();) {
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



	/**
	 * Set restrictions based on various cart items
	 * 
	 * @param cart
	 * @param deliveryModel
	 */
	private void getRestrictionReason(FDCartModel cart, FDDeliveryTimeslotModel deliveryModel){
		for(Iterator<EnumDlvRestrictionReason> i = cart.getApplicableRestrictions().iterator(); i.hasNext(); ){
			EnumDlvRestrictionReason reason = i.next();
			if(EnumDlvRestrictionReason.THANKSGIVING.equals(reason)){
				deliveryModel.setThxgivingRestriction(true);
				continue;
			}
	        if(EnumDlvRestrictionReason.THANKSGIVING_MEALS.equals(reason)){
	           deliveryModel.setThxgiving_meal_Restriction(true);
	           continue;
	        }
	        if(EnumDlvRestrictionReason.EASTER.equals(reason)){
	           deliveryModel.setEasterRestriction(true);
	           continue;
	        }
			if(EnumDlvRestrictionReason.EASTER_MEALS.equals(reason)){
	           deliveryModel.setEasterMealRestriction(true);
	           continue;
	        }
			if(EnumDlvRestrictionReason.ALCOHOL.equals(reason)){
				deliveryModel.setAlcoholRestriction(true);
				continue;
			}
			if(EnumDlvRestrictionReason.KOSHER.equals(reason)){
				deliveryModel.setKosherRestriction(true);
				continue;
			}
			if(EnumDlvRestrictionReason.VALENTINES.equals(reason)){
				deliveryModel.setValentineRestriction(true);
				continue;
			}
		}
	}
	
	public List<RestrictionI> getHolidayRestrictions(DlvRestrictionsList restrictions, List<DateRange> dateRanges, FDDeliveryTimeslotModel deliveryModel){
		List<RestrictionI> holidayRes = new ArrayList<RestrictionI>();
		for (Iterator<DateRange> i = dateRanges.iterator(); i.hasNext();) {
			DateRange validRange = i.next();
		
		//DateRange validRange = new DateRange(baseRange.getStartDate(), baseRange.getEndDate());
		
			for (Iterator<RestrictionI> itr = restrictions.getRestrictions(EnumDlvRestrictionReason.CLOSED, validRange).iterator(); itr.hasNext();) {
				RestrictionI r = itr.next();
				holidayRes.add(r);			
			}
		}
		deliveryModel.setHolidayRestrictions(holidayRes);
		return holidayRes;
	}
	

	
	@SuppressWarnings("unchecked")
	protected Set<EnumDlvRestrictionReason> getAlcoholRestrictionReasons(FDCartModel cart){
		Set<EnumDlvRestrictionReason> alcoholReasons = new HashSet<EnumDlvRestrictionReason>();
		for (Iterator<EnumDlvRestrictionReason> i = EnumDlvRestrictionReason.iterator(); i.hasNext();) {
			EnumDlvRestrictionReason reason = i.next();
			if (("WIN".equals(reason.getName())||"BER".equals(reason.getName())
										||"ACL".equals(reason.getName())) && (cart !=null && cart.getApplicableRestrictions().contains(reason))) {
				alcoholReasons.add(reason);
			}
		}
		return alcoholReasons;
	}
	
	protected static boolean isTimeslotAlcoholRestricted(List<RestrictionI> alcoholRestrictions, FDTimeslot slot) {
		if(alcoholRestrictions.size()>0 && slot != null){
			DateRange slotRange = new DateRange(slot.getStartDateTime(),slot.getEndDateTime());		
			for (Iterator<RestrictionI> i = alcoholRestrictions.iterator(); i.hasNext();) {
				RestrictionI r = i.next();

				if (r instanceof AlcoholRestriction) {
					AlcoholRestriction ar = (AlcoholRestriction) r;
					if (ar.overlaps(slotRange)) return true;
				}
			}
		}
		return false;
	}


	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return Result.class.getName();
		}
	}

	private class DateRangeComparator implements Comparator<DateRange> {

		public int compare(DateRange obj1, DateRange obj2) {
			if(obj1.getStartDate() != null &&  obj2.getStartDate() != null) {
				return -(obj2.getStartDate().compareTo(obj1.getStartDate()));
			}
			return 0;
		}
	}
}

