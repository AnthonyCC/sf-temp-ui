/**
 * 
 */
package com.freshdirect.fdstore.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdlogistics.model.FDTimeslotList;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.PromotionHelper;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.fdstore.rules.OrderMinimumCalculator;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.DlvZoneModel;

/**
 * Utility to calculate available capacity in given circumstances.
 */
public class TimeslotLogic {
	private final static Logger LOGGER = LoggerFactory.getInstance(TimeslotLogic.class);
	
	private static final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	private static final DecimalFormat QUANTITY_FORMATTER = new java.text.DecimalFormat("0.##");

	/** normal page (regular cust or CT cust on normal reservation) */
	public static final int PAGE_NORMAL = 0;

	/** chefstable page (CT cust only) */
	public static final int PAGE_CHEFSTABLE = 1;

	/**
	 * Utility method to filter time slot lists according to various restriction sets.
	 * 
	 * It also updates chef's table stats in user object if specified.
	 * 
	 * @param user
	 * @param geoRestrictionRange
	 * @param restrictions Set of delivery restrictions.
	 * @param timeslotList List of fetched timeslots.
	 * @param retainTimeslotIds List of retained timeslots.
	 * @param geographicRestrictions
	 * @param deliveryModel
	 * @param alcoholRestrictions
	 * @param forceorder
	 * @param address
	 * @param genericTimeslots Timeslots are regarded as weekly recurring items.
	 * 			So one-time restrictions and events are discarded (holidays, neighbour slots).
	 * 
	 * @throws FDResourceException
	 */
	public static DlvTimeslotStats filterDeliveryTimeSlots(final FDUserI user,
			final DlvRestrictionsList restrictions,
			final List<FDTimeslotUtil> timeslotList,
			final Set<String> retainTimeslotIds,
			final FDDeliveryTimeslotModel deliveryModel,
			final List<RestrictionI> alcoholRestrictions, final boolean forceorder,
			final ErpAddressModel address, 
			final boolean genericTimeslots,
			final DlvTimeslotStats stats)
			throws FDResourceException {


		Map<String, DlvZoneModel> _zonesMap = stats.getZonesMap();
		
		final double premiumFee = user.getShoppingCart().getPremiumFee(new FDRulesContextImpl(user));
		final boolean isAlcoholDlv = FDDeliveryManager.getInstance()
				.checkForAlcoholDelivery(address);
		stats.setAlcoholDelivery(isAlcoholDlv);
		
		Map<Integer, List<FDTimeslot>> timeslotMap = new HashMap<Integer, List<FDTimeslot>>();
		for (FDTimeslotUtil list : timeslotList) {
			for (Collection<FDTimeslot> col : list.getTimeslots()) {
				for (FDTimeslot ts : col) {
					if(!timeslotMap.containsKey(ts.getDayOfWeek())){
						timeslotMap.put(ts.getDayOfWeek(), new ArrayList<FDTimeslot>());
					}
					timeslotMap.get(ts.getDayOfWeek()).add(ts);
				}
			}
		}
		
		user.setSteeringSlotIds(new HashSet<String>());
		boolean minOrderReqd = false;
		
		for (FDTimeslotUtil list : timeslotList) {
			for (Collection<FDTimeslot> col : list.getTimeslots()) {
				for (FDTimeslot _ts : col) {
					// holiday restricted timeslot
					if (!genericTimeslots && list.getHolidays().contains(_ts.getDeliveryDate())) {
						_ts.setHolidayRestricted(true);
						_ts.setStoreFrontAvailable("R");
						if(_ts.isPremiumSlot()) deliveryModel.setShowPremiumSlots(false);
						continue;
					} else {
						// ... available for store front
						_ts.setStoreFrontAvailable("A");
					}

					boolean _remove = false;
					
					if (_ts.isEarlyAM()
							&& (EnumUnattendedDeliveryFlag.OPT_OUT
									.equals(address.getUnattendedDeliveryFlag()) || 
									(!EnumUnattendedDeliveryFlag.OPT_OUT.equals(address.getUnattendedDeliveryFlag()) && 
									!_zonesMap.get(_ts.getZoneId()).getZoneDescriptor().isUnattended()))) {
						_ts.setStoreFrontAvailable("E");
						_ts.setTimeslotRemoved(true);
						_remove = true;
					}
					
					
					
					
					Calendar cal = Calendar.getInstance();
					Date now = cal.getTime();
					//Remove the same day slots that are passed cutoff
					if(_ts.isPremiumSlot() && _ts.getCutoffDateTime().before(now))
					{
						_ts.setUnavailable(true);
						_remove = true;
					}
					
					double steeringDiscount = 0;
					if (!genericTimeslots) {
						// Calculate steering discount and apply to the current timeslot
						if(_ts.isPremiumSlot())
						{
							if(user.getShoppingCart()!=null && user.getShoppingCart().getDeliveryPremium()>0)
									_ts.setPremiumAmount(0);
							else
									_ts.setPremiumAmount(premiumFee);
							stats.setSameDayCutoffUTC(DateUtil.getUTCDate(_ts.getHandoffDateTime()));
							stats.setSameDayCutoff(_ts.getHandoffDateTime());
						}
						else if(!_ts.isPremiumSlot() || (_ts.isPremiumSlot() && FDStoreProperties.allowDiscountsOnPremiumSlots()))
						{
							steeringDiscount = PromotionHelper.getDiscount(user, _ts, timeslotMap.get(_ts.getDayOfWeek()), deliveryModel, forceorder);
							_ts.setSteeringDiscount(steeringDiscount);
						}
					}

			


					if (_remove)
						continue;

					stats.setMaximumDiscount(steeringDiscount);


					/* Update various slot counters */
					if (isAlcoholDlv
							&& isTimeslotAlcoholRestricted(
									alcoholRestrictions, _ts)) {
						_ts.setAlcoholRestricted(true);
						stats.incrementAlcoholSlots();
					}

					// Early AM TimeSlot
					if(_ts.isEarlyAM()){
						stats.incrementEarlyAMSlots();
					}

					minOrderReqd = applyOrderMinimum(user, _ts) || minOrderReqd;
				}
			}
		
			deliveryModel.setMinOrderReqd(minOrderReqd);
			stats.updateKosherSlotAvailable(list.isKosherSlotAvailable(restrictions));
		}

		return stats;
	}

	public static boolean applyOrderMinimum(FDUserI user, FDTimeslot timeslot) {
		double orderMinimum  = 0;
		try{
			OrderMinimumCalculator calc = new OrderMinimumCalculator("TIMESLOT");
			FDRulesContextImpl ctx = new FDRulesContextImpl(user, timeslot);
			orderMinimum = calc.getOrderMinimum(ctx);
			
			if(orderMinimum > 0){
					timeslot.setMinOrderAmt(orderMinimum);
					timeslot.setMinOrderMsg(formatMinAmount(orderMinimum)+" Min.");
					timeslot.setMinOrderMet( (orderMinimum > user.getShoppingCart().getSubTotal())?false:true);
			}
		}catch(Exception e){
			LOGGER.error(e);
			e.printStackTrace();
		}
		return orderMinimum > 0;
	}

	public static void applyOrderMinimum(FDUserI user, FDTimeslot timeslot, double subTotal) {
		try{
			OrderMinimumCalculator calc = new OrderMinimumCalculator("TIMESLOT");
			FDRulesContextImpl ctx = new FDRulesContextImpl(user, timeslot, subTotal);
			double orderMinimum = calc.getOrderMinimum(ctx);
			if(orderMinimum > 0){
				if(orderMinimum > subTotal) {
					timeslot.setMinOrderMet(false);
				}else{
					timeslot.setMinOrderMet(true);
				}
			}
			
		}catch(Exception e){
			LOGGER.error(e);
			e.printStackTrace();
		}
	}

	public static String formatMinAmount(double minOrderAmt){
		return "$"+QUANTITY_FORMATTER.format(minOrderAmt);
	}
	private static boolean isTimeslotAlcoholRestricted(List<RestrictionI> alcoholRestrictions, FDTimeslot slot) {
		if(alcoholRestrictions.size()>0 && slot != null){
			DateRange slotRange = new DateRange(slot.getStartDateTime(),slot.getEndDateTime());
			for (RestrictionI r : alcoholRestrictions) {
				if (r instanceof AlcoholRestriction) {
					AlcoholRestriction ar = (AlcoholRestriction) r;
					if (ar.overlaps(slotRange)) return true;
				}
			}
		}
		return false;
	}

	/**
	 * Utility method to purge marked items from timeslot list
	 * 
	 * @param timeslotList
	 */
	public static void purge(Collection<FDTimeslotUtil> timeslotList) {
		for (FDTimeslotUtil list : timeslotList) {
			for (Collection<FDTimeslot> col : list.getTimeslots()) {
				for (Iterator<FDTimeslot> k = col.iterator(); k.hasNext(); ) {
					FDTimeslot timeslot = k.next();
					if (timeslot.isTimeslotRemoved() || timeslot.isHolidayRestricted())
						k.remove();
				}
			}
		}
	}
	public static void clearVariableMinimum(FDUserI user, Collection<FDTimeslotUtil> timeslotList) {
		for (FDTimeslotUtil list : timeslotList) {
			for (Collection<FDTimeslot> col : list.getTimeslots()) {
				for (Iterator<FDTimeslot> k = col.iterator(); k.hasNext(); ) {
					FDTimeslot timeslot = k.next();
						timeslot.setMinOrderAmt(0);
						timeslot.setMinOrderMet(true);
						timeslot.setMinOrderMsg("");
				}
			}
		}
		if(user!=null && user.getReservation()!=null && !user.getReservation().isMinOrderMet()){
			clearVariableMinimumTs(user.getReservation().getTimeslot());
		}
		if(user!=null && user.getShoppingCart()!=null &&  user.getShoppingCart().getDeliveryReservation()!=null && 
				!user.getShoppingCart().getDeliveryReservation().isMinOrderMet()){
			clearVariableMinimumTs(user.getShoppingCart().getDeliveryReservation().getTimeslot());
		}
	}
	public static void clearVariableMinimumTs(FDTimeslot timeslot){
		if(timeslot!=null){
			timeslot.setMinOrderAmt(0);
			timeslot.setMinOrderMet(true);
			timeslot.setMinOrderMsg("");
		}
	}
	
	public static boolean isTSPreReserved(FDReservation rsv, FDDeliveryTimeslotModel deliveryModel){
		
		return (rsv!=null && !rsv.isMinOrderMet() 
	    		&& ((deliveryModel.getTimeSlotId()!=null && deliveryModel.getTimeSlotId().equals(rsv.getTimeslotId())) || 
	    				(deliveryModel.getPreReserveSlotId()!=null && deliveryModel.getPreReserveSlotId().equals(rsv.getTimeslotId()) && deliveryModel.isPreReserved()) ));
	}
	
	public static boolean isTSMinOrderNotMet(FDTimeslot slot, FDReservation rsv, FDDeliveryTimeslotModel deliveryModel){
		
		return (!slot.isMinOrderMet() && !(rsv!=null && !rsv.isMinOrderMet() && rsv.getTimeslotId().equals(slot.getId())
				&& ((deliveryModel.getTimeSlotId()!=null && deliveryModel.getTimeSlotId().equals(rsv.getTimeslotId())) ||
						(deliveryModel.getPreReserveSlotId()!=null && deliveryModel.getPreReserveSlotId().equals(rsv.getTimeslotId()) && deliveryModel.isPreReserved())) ));
	}
	
	public static boolean isTSRsvOrderNotMet(FDTimeslot slot, FDReservation rsv, FDDeliveryTimeslotModel deliveryModel){
	
		return (rsv!=null && !rsv.isMinOrderMet() && rsv.getTimeslotId().equals(slot.getId())
			&& ((deliveryModel.getTimeSlotId()!=null && deliveryModel.getTimeSlotId().equals(rsv.getTimeslotId()))||
					(deliveryModel.getPreReserveSlotId()!=null && deliveryModel.getPreReserveSlotId().equals(rsv.getTimeslotId()) && deliveryModel.isPreReserved())));
	}

	public static boolean hasPremiumSlots(List<FDTimeslotUtil> timeslotList){

		for (FDTimeslotUtil list : timeslotList) {
			for (Collection<FDTimeslot> col : list.getTimeslots()) {
				for (Iterator<FDTimeslot> k = col.iterator(); k.hasNext(); ) {
					FDTimeslot timeslot = k.next();
					if (timeslot.isPremiumSlot())
						return true;
				}
			}
		}
		return false;
	}
	
	public static boolean hasPremiumSlot(List<FDTimeslot> fdTsList)
	{
		FDTimeslot fdT;
		if(fdTsList!=null && fdTsList.size()>0 )
		{
			Iterator<FDTimeslot> fit = fdTsList.iterator();
			for(;fit.hasNext();)
			{
				fdT = fit.next();
				return hasPremiumSlot(fdT);
				
			}
		}
		return false;
	}
	
	public static boolean hasPremiumSlot(FDTimeslot fdT)
	{
		return fdT.isPremiumSlot();
	}
	
	public static boolean isTimeslotPurged(FDTimeslot ts) {
		if (ts.isTimeslotRemoved() || ts.isHolidayRestricted()){
			return true;
		}
		return false;
	}
	
	public static boolean isTimeslotSoldout(FDTimeslot ts, FDDeliveryTimeslotModel deliveryModel, boolean isForce) {
		
		if (ts.isSoldOut()) {
				return true;
			}
		return false;
	}

	public static void purgeSDSlots(List<FDTimeslotUtil> timeslotList) {
		for (FDTimeslotUtil list : timeslotList) {
			for (Collection<FDTimeslot> col : list.getTimeslots()) {
				for (Iterator<FDTimeslot> k = col.iterator(); k.hasNext(); ) {
					FDTimeslot timeslot = k.next();
					if (timeslot.isPremiumSlot())
						k.remove();
				}
			}
		}
	}
	
}
