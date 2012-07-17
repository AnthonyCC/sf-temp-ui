/**
 * 
 */
package com.freshdirect.fdstore.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.TimeslotCapacityContext;
import com.freshdirect.delivery.TimeslotCapacityWrapper;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.FDZoneNotFoundException;
import com.freshdirect.fdstore.customer.FDDeliveryTimeslotModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.PromotionHelper;
import com.freshdirect.fdstore.rules.FDRulesContextImpl;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;

/**
 * Utility to calculate available capacity in given circumstances.
 */
public class TimeslotLogic {
	private final static Logger LOGGER = LoggerFactory.getInstance(TimeslotLogic.class);

	/** normal page (regular cust or CT cust on normal reservation) */
	public static final int PAGE_NORMAL = 0;

	/** chefstable page (CT cust only) */
	public static final int PAGE_CHEFSTABLE = 1;

	/**
	 * Calculate the available capacity for a page.
	 * 
	 * @param timeslotModel
	 *            the timeslot that should be checked
	 * @param currentTime
	 *            the date of the request
	 * @param page
	 *            one of {@link PAGE_NORMAL}, {@link PAGE_CHEFSTABLE}
	 * @param autoRelease
	 *            the number of minutes the auto-release occurs before the
	 *            cutoffTime
	 * @return the number of available orders.
	 */
	public static boolean getAvailableCapacity(DlvTimeslotModel timeslotModel,
			Date currentTime, int page, int autoRelease) {
		
		TimeslotCapacityContext context = new TimeslotCapacityContext();
		context.setChefsTable(PAGE_CHEFSTABLE == page);
		context.setCurrentTime(currentTime);
		
		TimeslotCapacityWrapper capacityProvider = new TimeslotCapacityWrapper(timeslotModel, context);
		return capacityProvider.isAvailable();	
	}
	
		
	public static IDeliveryWindowMetrics recalculateCapacity(DlvTimeslotModel timeslotModel) {
		
		IDeliveryWindowMetrics _metrics = null;
		if(timeslotModel != null ) {
			
			if(timeslotModel.getRoutingSlot() != null 
										&& timeslotModel.getRoutingSlot().getDeliveryMetrics() != null ) {
				TimeslotCapacityContext context = new TimeslotCapacityContext();
								
				TimeslotCapacityWrapper capacityProvider = new TimeslotCapacityWrapper(timeslotModel, context);
				
				_metrics = timeslotModel.getRoutingSlot().getDeliveryMetrics();
				
				_metrics.setOrderCapacity(capacityProvider.getCalculatedDynamicCapacity());
				_metrics.setOrderCtCapacity(capacityProvider.getCalculatedDynamicCTCapacity());
				_metrics.setOrderPremiumCapacity(capacityProvider.getCalculatedDynamicPremiumCapacity());
				_metrics.setOrderPremiumCtCapacity(capacityProvider.getCalculatedDynamicPremiumCTCapacity());
			}
		}
		return _metrics;
	}



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
			final DateRange geoRestrictionRange,
			final DlvRestrictionsList restrictions,
			final List<FDTimeslotUtil> timeslotList,
			final Set<String> retainTimeslotIds,
			final List<GeographyRestriction> geographicRestrictions,
			final FDDeliveryTimeslotModel deliveryModel,
			final List<RestrictionI> alcoholRestrictions, final boolean forceorder,
			final ErpAddressModel address, 
			final boolean genericTimeslots)
			throws FDResourceException {


		final DlvTimeslotStats stats = new DlvTimeslotStats();
		HashMap<String, DlvZoneModel> _zonesMap = new HashMap<String, DlvZoneModel>();
		final double premiumFee = user.getShoppingCart().getPremiumFee(new FDRulesContextImpl(user));
		final boolean isAlcoholDlv = FDDeliveryManager.getInstance()
				.checkForAlcoholDelivery(address);
		stats.setAlcoholDelivery(isAlcoholDlv);
		for (FDTimeslotUtil list : timeslotList) {
			for (Collection<FDTimeslot> col : list.getTimeslots()) {
				for (FDTimeslot timeslot : col) {
					// holiday restricted timeslot
					if (!genericTimeslots && list.getHolidays().contains(timeslot.getBaseDate())) {
						timeslot.setHolidayRestricted(true);
						timeslot.setStoreFrontAvailable("R");
						if(timeslot.getDlvTimeslot().isPremiumSlot()) deliveryModel.setShowPremiumSlots(false);
						continue;
					} else {
						// ... available for store front
						timeslot.setStoreFrontAvailable("A");
					}

					final DlvTimeslotModel _ts = timeslot.getDlvTimeslot();
					boolean _remove = false;
					

					double steeringDiscount = 0;
					if (!genericTimeslots) {
						// Calculate steering discount and apply to the current timeslot
						if(_ts.isPremiumSlot())
						{
							if(user.getShoppingCart()!=null && user.getShoppingCart().getDeliveryPremium()>0)
									_ts.setPremiumAmount(0);
							else
									_ts.setPremiumAmount(premiumFee);
							stats.setSameDayCutoffUTC(DateUtil.getUTCDate(_ts.getCutoffTimeAsDate()));
							stats.setSameDayCutoff(_ts.getCutoffTimeAsDate());
						}
						else if(!_ts.isPremiumSlot() || (_ts.isPremiumSlot() && FDStoreProperties.allowDiscountsOnPremiumSlots()))
						{
							steeringDiscount = PromotionHelper.getDiscount(
								user, timeslot);
						_ts.setSteeringDiscount(steeringDiscount);
						}
					}
					
					// Apply geo restrictions and
					//   mark timeslot for removal if restricted
					{
						boolean geoRestricted = GeographyRestriction
								.isTimeSlotGeoRestricted(
										geographicRestrictions, timeslot,
										stats.getMessages(), geoRestrictionRange, stats.getComments());
						timeslot.setGeoRestricted(geoRestricted);
						if ((_ts.getCapacity() <= 0 || geoRestricted)
								&& !retainTimeslotIds.contains(_ts.getId())) {
							LOGGER.debug("Timeslot Removed By Tag :" + _ts);
							timeslot.setStoreFrontAvailable("H");
							timeslot.setTimeslotRemoved(true);
							_remove = true;
						}
					}
					
					Calendar cal = Calendar.getInstance();
					Date now = cal.getTime();
					//Remove the same day slots that are passed cutoff
					if(timeslot.getDlvTimeslot().isPremiumSlot() && timeslot.getCutoffDateTime().before(now))
					{
						timeslot.setUnavailable(true);
						_remove = true;
					}
					


					// Build zone map
					{
						String zoneCode = timeslot.getZoneId();
						if (!_zonesMap.containsKey(zoneCode)) {
							try {
								FDDeliveryManager deliveryManager = FDDeliveryManager
										.getInstance();
								DlvZoneModel zoneModel = deliveryManager
										.findZoneById(zoneCode);
								if (zoneModel.isCtActive()) {
									stats.setCtActive(true);
								}
								_zonesMap.put(zoneCode, zoneModel);
							} catch (FDZoneNotFoundException e) {
								LOGGER.error("Referenced zone not found, database error. Zone:"
										+ zoneCode
										+ " timeslot id:"
										+ timeslot.getTimeslotId());
							}
						}
					}


					if (_remove)
						continue;

					stats.setMaximumDiscount(steeringDiscount);


					// Check timeslot capacity
					checkTimeslotCapacity(user, _zonesMap, timeslot);


					/* Update various slot counters */
					if (isAlcoholDlv
							&& isTimeslotAlcoholRestricted(
									alcoholRestrictions, timeslot)) {
						timeslot.setAlcoholRestricted(true);
						stats.incrementAlcoholSlots();
					}

					// -- Chef's table-- //
					if (!timeslot.hasNormalAvailCapacity()
							&& timeslot.hasAvailCTCapacity())
						stats.incrementCtSlots();

					if (!timeslot.isDepot() && timeslot.isEcoFriendly())
						stats.incrementEcoFriendlySlots();

					if (!genericTimeslots && timeslot.isDepot() && timeslot.isEcoFriendly())
						stats.incrementNeighbourhoodSlots();


					if (!genericTimeslots) {
						// -- Sold-Out -- //
						final IDeliverySlot routingSlot = _ts != null ? _ts.getRoutingSlot() : null;
						final boolean isTSPreserved =
								timeslot.getTimeslotId().equals(deliveryModel.getTimeSlotId())
								||
								(	timeslot.getTimeslotId().equals(deliveryModel.getPreReserveSlotId())
									&&
									deliveryModel.isPreReserved()
								);
						if (!timeslot.hasAvailCTCapacity()
								&& !isTSPreserved
								&&
								(	!forceorder
									||
									(
										routingSlot != null
										&&
										(	routingSlot.isManuallyClosed()
											||
											!routingSlot.isDynamicActive()
										)
									)
								)
						) {
							timeslot.setStoreFrontAvailable("S");
							stats.incrementSoldOutSlots();
						}
					}

					stats.incrementTotalSlots();
				}
			}


			stats.updateKosherSlotAvailable(list.isKosherSlotAvailable(restrictions));
			if (!genericTimeslots)
				stats.updateHasCapacity(list.hasCapacity());
		}

		if (genericTimeslots)
			stats.updateHasCapacity(true);
		stats.setZonesMap(_zonesMap);

		return stats;
	}



	private static boolean isTimeslotAlcoholRestricted(List<RestrictionI> alcoholRestrictions, FDTimeslot slot) {
		if(alcoholRestrictions.size()>0 && slot != null){
			DateRange slotRange = new DateRange(slot.getBegDateTime(),slot.getEndDateTime());
			for (RestrictionI r : alcoholRestrictions) {
				if (r instanceof AlcoholRestriction) {
					AlcoholRestriction ar = (AlcoholRestriction) r;
					if (ar.overlaps(slotRange)) return true;
				}
			}
		}
		return false;
	}



	private static void checkTimeslotCapacity(FDUserI user, Map<String, DlvZoneModel> zonesMap,
			FDTimeslot timeslot) {
		DlvZoneModel tempZoneModel = null;
		try {
			int pageType_ct = user.isChefsTable() ? TimeslotLogic.PAGE_CHEFSTABLE
					: TimeslotLogic.PAGE_NORMAL;
			// Check the timeslot capacity
			tempZoneModel = zonesMap.get(timeslot.getZoneId());

			boolean isCTCapacity = false;
			if (CTDeliveryCapacityLogic.isEligible(user, timeslot) != null) {
				pageType_ct = TimeslotLogic.PAGE_CHEFSTABLE;
				isCTCapacity = true;
			}

			boolean availCapacity = getAvailableCapacity(
					timeslot.getDlvTimeslot(), new Date(), pageType_ct,
					timeslot.getDlvTimeslot().isPremiumSlot()?tempZoneModel.getPremiumCtReleaseTime():tempZoneModel.getCtReleaseTime());
			boolean normalAvailCapacity = availCapacity;

			if (TimeslotLogic.PAGE_CHEFSTABLE == pageType_ct && !isCTCapacity) {
				normalAvailCapacity = getAvailableCapacity(
						timeslot.getDlvTimeslot(), new Date(),
						TimeslotLogic.PAGE_NORMAL,
						timeslot.getDlvTimeslot().isPremiumSlot()?tempZoneModel.getPremiumCtReleaseTime():tempZoneModel.getCtReleaseTime());
			}

			timeslot.setNormalAvailCapacity(normalAvailCapacity);
			timeslot.setAvailCTCapacity(availCapacity);
		} catch (Exception ex) {
			LOGGER.error(ex);
		}
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
	

	/**
	 * Utility method to purge SameDay items from timeslot list
	 * 
	 * @param timeslotList
	 */
	public static void purgeSDSlots(Collection<FDTimeslotUtil> timeslotList) {
		for (FDTimeslotUtil list : timeslotList) {
			for (Collection<FDTimeslot> col : list.getTimeslots()) {
				for (Iterator<FDTimeslot> k = col.iterator(); k.hasNext(); ) {
					FDTimeslot timeslot = k.next();
					if (timeslot.getDlvTimeslot().isPremiumSlot())
						k.remove();
				}
			}
		}
	}
	public static boolean hasPremiumSlots(List<FDTimeslotUtil> timeslotList, Date startDate, Date endDate)
	{
		Iterator<FDTimeslotUtil> it = timeslotList.iterator();
		FDTimeslotUtil fdTsu;List<FDTimeslot> fdTsList;
		for(;it.hasNext();)
		{
			fdTsu = it.next();
			fdTsList = fdTsu.getTimeslotsForDate(startDate);
			if(fdTsList==null || fdTsList.size()==0 )
			{
				fdTsu.removeTimeslots(startDate);
				startDate = DateUtil.addDays(startDate, 1);
				fdTsList = fdTsu.getTimeslotsForDate(startDate);
				return hasPremiumSlot(fdTsList);
			}
			else if(fdTsList!=null && fdTsList.size()>0 )
			{
				fdTsu.removeTimeslots(endDate);
				return hasPremiumSlot(fdTsList);
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
		return fdT.getDlvTimeslot().isPremiumSlot();
	}
}
