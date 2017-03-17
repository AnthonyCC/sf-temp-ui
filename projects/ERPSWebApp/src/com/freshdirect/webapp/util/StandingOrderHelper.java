package com.freshdirect.webapp.util;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.standingorders.DeliveryInterval;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.util.FDTimeslotUtil;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.FormLocationData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.FormPaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;
import com.freshdirect.webapp.ajax.standingorder.StandingOrderResponseData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

/**
 * Helper class for standing orders, any static method which has no place anywhere else should be here.
 * A lot of code duplicates functionality in DeliveryTimeWindowFormatter with some minor but important changes.
 * 
 */
public class StandingOrderHelper {
	private final static Logger LOGGER = Logger.getLogger(StandingOrderHelper.class);
	
	public static final String	SEPARATOR	= " - ";
	public static final String	NOON		= "noon";
	public static final String	AM			= "am";
	public static final String	PM			= "pm";
	
	public static final String DAY_NAMES[] = new DateFormatSymbols().getWeekdays();
    private static final String DEFAULT_TIMESLOT_ID = "timeslotId";
	private static SimpleDateFormat			FORMATTER			= new SimpleDateFormat( "hh:mm a" );


/*	public static String getSODeliveryDate(FDStandingOrder so, boolean alt) {

		Calendar cl = Calendar.getInstance();
		StringBuilder buf = new StringBuilder();

		if (null != so.getNextDeliveryDate()) {
			if (alt) {
				// normal format for upcoming delivery "Tue May 6, 10pm-11pm "
			cl.setTime(so.getUpcomingDelivery().getRequestedDate());
			buf.append(new SimpleDateFormat("EEE MMM d", Locale.ENGLISH).format(cl.getTime()));
			} else {
				// format for standing order settings Tuesday, 10pm -11 pm
				cl.setTime(so.getNextDeliveryDate());
				if (so.getStartTime() != null && so.getEndTime() != null) {
					buf.append(DAY_NAMES[cl.get(Calendar.DAY_OF_WEEK)]);
					buf.append(", ");
					buf.append(formatTime(so.getStartTime(), so.getEndTime()));
				}
			}
		}
		return buf.toString();
	}
*/

	public static String getSODeliveryDate(FDStandingOrder so, boolean alt) {

		if (null != so.getNextDeliveryDate()) {
			if (alt) {
				// normal format for upcoming delivery "Feb 20"
			return DateUtil.formatMonthAndDate(so.getUpcomingDelivery().getRequestedDate());
			
			} else {
				// format for standing order settings "Feb 20"
				return DateUtil.formatMonthAndDate(so.getNextDeliveryDate());
				}
			}
		
		return null;
	}
	
	/**
	 * Returns formatted delivery date (e.g. "Tuesday, 6 - 8am")
	 * @param alt use alternative format?
	 * @return next delivery date as a formatted string
	 */
	public static String getDeliveryDate( FDStandingOrder so, boolean alt ) {

		Calendar cl = Calendar.getInstance();
		cl.setTime(so.getNextDeliveryDate());

		StringBuilder buf = new StringBuilder();
		
		if (alt) {
			// alternative format "6 - 8am, Tuesday"
			buf.append(formatTime(so.getStartTime(), so.getEndTime()));
			buf.append(", ");
			buf.append( DAY_NAMES[ cl.get(Calendar.DAY_OF_WEEK)] );
		} else {
			// normal format "Tuesday, 6 - 8am"
			buf.append( DAY_NAMES[ cl.get(Calendar.DAY_OF_WEEK)] );
			buf.append(", ");
			buf.append(formatTime(so.getStartTime(), so.getEndTime()));
		}
		
		return buf.toString();
	}
	/**
	 * Helper class to display / set SO delivery time
	 * 
	 * @author segabor
	 */
	public static class DeliveryTime {
		/* Delivery day stored in day of week format (1=SUN, 2=MON, ...) */
		int day;
		
		/* time window start in hour */
		int startHour;
		int startMinute;
		
		int endHour;
		int endMinute;

		int weekOffset;
		
		public int getDay() {
			return day;
		}
		
		public void setDay(int day) {
			this.day = day;
		}
		
		public int getStartHour() {
			return startHour;
		}
		
		public void setStartHour(int startHour) {
			this.startHour = startHour;
		}
		
		public int getStartMinute() {
			return startMinute;
		}
		
		public void setStartMinute(int startMinute) {
			this.startMinute = startMinute;
		}
		

		public int getEndHour() {
			return endHour;
		}
		
		public void setEndHour(int endHour) {
			this.endHour = endHour;
		}

		public int getEndMinute() {
			return endMinute;
		}
		
		public void setEndMinute(int endMinute) {
			this.endMinute = endMinute;
		}

		public int getWeekOffset() {
			return weekOffset;
		}
		
		public void setWeekOffset(int weekOffset) {
			this.weekOffset = weekOffset;
		}
		
		
		/**
		 * Create instance from a standing order instance
		 * @param so
		 */
		public DeliveryTime(FDStandingOrder so) {
			final Calendar c = Calendar.getInstance();

			int _curWeek = c.get(Calendar.WEEK_OF_YEAR);
			
			// day is stored in next delivery date
			c.setTime(so.getNextDeliveryDate());
			this.day = c.get(Calendar.DAY_OF_WEEK);

			c.setTime(so.getStartTime());
			this.startHour = c.get(Calendar.HOUR_OF_DAY);
			this.startMinute = c.get(Calendar.MINUTE);

			c.setTime(so.getEndTime());
			this.endHour = c.get(Calendar.HOUR_OF_DAY);
			this.endMinute = c.get(Calendar.MINUTE);

			final int woy =  c.get(Calendar.WEEK_OF_YEAR);
			this.weekOffset = woy -_curWeek;
			/* if (this.weekOffset > 0)
				this.weekOffset += 52; */
		}


		public DeliveryTime(FDTimeslot ts) {
			final Calendar c = Calendar.getInstance();

			int _curWeek = c.get(Calendar.WEEK_OF_YEAR);

			c.setTime( ts.getDeliveryDate() );
			this.day = c.get(Calendar.DAY_OF_WEEK);
			
			c.setTime( ts.getStartDateTime());
			this.startHour = c.get(Calendar.HOUR_OF_DAY);
			this.startMinute = c.get(Calendar.MINUTE);
			
			c.setTime( ts.getEndDateTime());
			this.endHour = c.get(Calendar.HOUR_OF_DAY);
			this.endMinute = c.get(Calendar.MINUTE);

			final int woy =  c.get(Calendar.WEEK_OF_YEAR);
			this.weekOffset = woy -_curWeek;
			/* if (this.weekOffset > 0)
				this.weekOffset += 52; */
		}


		/**
		 * Store delivery time in standing order
		 * @param so
		 * @return
		 */
		public boolean update(FDStandingOrder so) {
			if (startHour >= endHour)
				return false;
			
			if (day < 1 || day > 8)
				return false;
			
			final Calendar c = Calendar.getInstance();
			
			// store day in next dlv date
			// c.setTime(so.getNextDeliveryDate());
			
			// apply week offset
			c.add(Calendar.WEEK_OF_YEAR, weekOffset);
			
			// set the day
			c.set(Calendar.DAY_OF_WEEK, day);
			so.setNextDeliveryDate(c.getTime());
			
			c.set(Calendar.HOUR_OF_DAY, startHour);
			c.set(Calendar.MINUTE, startMinute);
			c.set(Calendar.SECOND, 0); // reset the rest
			c.set(Calendar.MILLISECOND, 0);
			so.setStartTime(c.getTime());
			
			c.set(Calendar.HOUR_OF_DAY, endHour);
			c.set(Calendar.MINUTE, endMinute);
			so.setEndTime(c.getTime());

			return true;
		}


		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			
			buf.append(StandingOrderHelper.formatTime(startHour, startMinute, endHour, endMinute));
			buf.append(", ");
			buf.append( DAY_NAMES[ day ] );
			
			return buf.toString();
		}
		
		public String getDayName() {
			return StandingOrderHelper.DAY_NAMES[day];
		}
		
		public String formatTime() {
			return StandingOrderHelper.formatTime(startHour, startMinute, endHour, endMinute);
		}
	}


	
	public static String formatTime(Date s, Date e) {
		return FDTimeslot.format(s, e);
	}
	
	public static String formatTime(int sh, int sm, int eh, int em) {
		Calendar c = Calendar.getInstance();
		
		Date s, e;
		
		c.set(Calendar.HOUR_OF_DAY, sh);
		c.set(Calendar.MINUTE, sm);
		c.set(Calendar.SECOND, 0); // reset the rest
		c.set(Calendar.MILLISECOND, 0);

		s = c.getTime();
		
		c.set(Calendar.HOUR_OF_DAY, eh);
		c.set(Calendar.MINUTE, em);
		c.set(Calendar.SECOND, 0); // reset the rest
		c.set(Calendar.MILLISECOND, 0);
		
		e = c.getTime();
		
		return FDTimeslot.format(s, e);
	}

	public static Calendar dateToCalendar(final Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}

	public static String getPreviousDeliveryDate(FDStandingOrder so) {
		if ( so == null || so.getPreviousDeliveryDate() == null ) 
			return "";
		return new SimpleDateFormat("EEEE, MMMM d.").format( so.getPreviousDeliveryDate() );
	}



	/**
	 * Utility method to match time slots against next delivery date.
	 * It returns the ID of the first matching or overlapping time slot.
	 * 
	 * @param so
	 * @param tsu
	 * 
	 * @return matching timeslot ID
	 */
	public static String findMatchingSlot(final FDStandingOrder so, FDTimeslotUtil tsu) {
	   String __so_timeslotId = null;
       if(null!=so.getNextDeliveryDate()&& null!=so.getStartTime()&& null!=so.getEndTime()){
		Calendar c = Calendar.getInstance();
		c.setTime( so.getNextDeliveryDate() );
		final int _dw = c.get(Calendar.DAY_OF_WEEK);
		c.setTime( tsu.getStartDate());
		final int _dw0 = c.get(Calendar.DAY_OF_WEEK);
		
		c.set(Calendar.DAY_OF_WEEK, _dw);
		if (_dw <= _dw0) {
			// shift with one week
			c.add(Calendar.DATE, 7);
		}
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0); // reset
		c.set(Calendar.SECOND, 0); // reset 
		c.set(Calendar.MILLISECOND, 0); // reset
		
		final Date selDate = c.getTime(); // <-- we want time slot matching this date

		List<FDTimeslot> l = tsu.getTimeslotsForDate(selDate);
		if (l == null) {
			LOGGER.debug("NO TIMESLOTS FOR DATE " + _dw);
		} else {
			LOGGER.debug("TIMESLOTS FOR DATE " + _dw + ": " + l);
						
			DeliveryInterval deliveryTimes = new DeliveryInterval(selDate, so.getStartTime(), so.getEndTime());
			
			FDTimeslot _tmpTimeslot = null;
			for ( FDTimeslot ts : l ) {		
				if ( deliveryTimes.checkTimeslot( ts ) ) {
					// this time slot matches to SO template window, and is within the cutoff time
					LOGGER.info( "Found matched timeslot: " + ts.toString() );
					_tmpTimeslot = ts;
					__so_timeslotId = ts.getId();
					if(ts.isTimeslotRestricted()){ 
						ts.setTimeslotRestricted(false);
						ts.setSoldOut(false);
						break;
					}
					
				}
			}
			
			//To allow time windows that do not exactly match the window specified in the standing order template. 
			//If an exact match time window is not available, but time windows exist that overlap the template's time window, 
			//then select the overlapping time window with the earliest start time 		
			if(_tmpTimeslot == null && FDStoreProperties.isStandingOrdersOverlapWindowsEnabled()) {				
				List<FDTimeslot> altTimeslots = deliveryTimes.getAltTimeslots(l);
				for ( FDTimeslot ts : altTimeslots ) {
					// starting bound falls within time slot
					LOGGER.debug(" ===> " + ts + " (overlap)");
					__so_timeslotId = ts.getId();
					if(ts.isTimeslotRestricted()){
						ts.setTimeslotRestricted(false);
						ts.setSoldOut(false);
						break;
					}
					
				}
			}
		}
       }else{
    	   __so_timeslotId="";
        }
		return __so_timeslotId;
	}
	


	/**
	 * This function calculates possible next delivery dates 
	 * based on input
	 * 
	 * @param so A standing order
	 * @param dayOfWeek
	 * 
	 * @return
	 * @throws FDException 
	 */
	public static List<Date> getNextDeliveryDateCandidates(FDStandingOrder so, int dayOfWeek) throws FDException {
		if (so == null)
			throw new FDException("Null Standing Order Instance!");

		final Calendar c = Calendar.getInstance();
		c.setTime(so.getNextDeliveryDate());
		
		if (dayOfWeek < Calendar.SUNDAY || dayOfWeek > Calendar.SATURDAY) {
			LOGGER.warn("Invalid day of week, fall back to next delivery day");
			dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		}

		List<Date> candidates = new ArrayList<Date>(so.getFrequency());
		
		// setup base date
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		// reset the rest
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		for (int k = 0; k<so.getFrequency(); k++) {
			// leap ahead with a week
			c.add(Calendar.WEEK_OF_YEAR, 1);

			candidates.add(c.getTime());
		}
		
		return candidates;
	}
	
	
	/**
	 * Returns a matrix holding all possible delivery date candidates.
	 * Size of matrix is [n_days,freq]
	 * 
	 * @param so
	 * @param offsetStart Must be >= 1
	 * 
	 * @return
	 */
	public static List<List<Date>> getAllDeliveryDateCandidates(FDStandingOrder so, int offsetStart) {
		List<List<Date>> cMat = new ArrayList<List<Date>>(7);

		// prepare calendar
		final Calendar c = Calendar.getInstance();
		// c.setTime(so.getNextDeliveryDate());

		if (offsetStart < 1)
			offsetStart = 1;

		
		for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
			List<Date> candidates = new ArrayList<Date>(so.getFrequency());
			
			// setup base date
			c.setTimeInMillis(System.currentTimeMillis());
			c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			// reset the rest
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

			// set starting week
			c.add(Calendar.WEEK_OF_YEAR, offsetStart);

			for (int k = 0; k<so.getFrequency(); k++) {
				// this ensures that a newly chosen delivery date will fall
				// at least in the next week or later 

				candidates.add(c.getTime());

				// leap ahead with a week
				c.add(Calendar.WEEK_OF_YEAR, 1);
			}

			cMat.add(candidates);
		}
		
		return cMat;
	}
	

	/**
	 * Calculate the starting week offset for next delivery date candidates 
	 * 
	 * @param instances
	 *
	 * @return
	 * @throws FDResourceException
	 */
	public static int getFirstAvailableWeekOffset(List<FDOrderInfoI> instances) {
		int offset = 1; // next week (0=current week)
		
		if (instances != null && instances.size() > 0) {
			final Calendar c = Calendar.getInstance();
			
			c.setTimeInMillis( System.currentTimeMillis() );
			final int curw = c.get(Calendar.WEEK_OF_YEAR);
			int k = c.get(Calendar.WEEK_OF_YEAR);
			
			// get the latest schedule
			for (FDOrderInfoI info : instances ) {
				c.setTime( info.getDeliveryStartTime() );
				k = Math.max(k, c.get(Calendar.WEEK_OF_YEAR) );
			}
			
			// update offset according to the calculations
			offset = (k-curw)+1;
		}
		
		return offset;
	}
	
	public static Collection<Map<String, Object>> convertCustomerStandingOrdersToMap(Collection<FDStandingOrder> soList) throws FDResourceException {
		Collection<Map<String, Object>> result = new ArrayList<Map<String, Object>>(); 
		for (FDStandingOrder so : soList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", so.getId());
			map.put("url", so.getLandingPage());
			map.put("name", so.getCustomerListName());
			
			// Count the available items - as in QSController and QuickCart
			// FIXME : should be refactored to some common place ....
			int productCnt = 0;
			List<FDCustomerListItem> soLines = so.getCustomerList().getLineItems();			
			if ( soLines != null ) {
				for (FDProductSelectionI orderLine: OrderLineUtil.getValidProductSelectionsFromCCLItems( soLines )) {
					ProductModel productNode = orderLine.lookupProduct();
					if(!((productNode==null || productNode.getSku(orderLine.getSkuCode()).isUnavailable() || orderLine.isInvalidConfig()))) {
							productCnt++;
					}
				}
			}			
			map.put("count", productCnt );
			
			result.add(map);
		}
		return result;
	}
	public static Collection<Map<String, Object>> convertStandingOrderToSoy(Collection<FDStandingOrder> soList,boolean isUpcomingDelivery) throws FDResourceException, PricingException, FDInvalidConfigurationException {
		Collection<Map<String, Object>> result = new ArrayList<Map<String, Object>>(); 
		for (FDStandingOrder so : soList) {
			
			
			Map<String, Object> map = convertStandingOrderToSoy(isUpcomingDelivery, so);
			
			result.add(map);

		}
		return result;

	}

	/**
	 * @param isUpcomingDelivery
	 * @param so
	 * @return
	 * @throws FDResourceException
	 * @throws FDInvalidConfigurationException
	 * @throws PricingException
	 */
	public static Map<String, Object> convertStandingOrderToSoy(boolean isUpcomingDelivery, FDStandingOrder so)
			throws FDResourceException, FDInvalidConfigurationException, PricingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", so.getId());
		map.put("url", so.getLandingPage());
		map.put("soName", so.getCustomerListName());
		map.put("listId", so.getCustomerListId());
		map.put("frequencyDesc", so.getFullFrequencyDescription());
		int productCnt = 0;
		double amount=0.0;
		//TODO : need to work on calculating total amount
		if(!isUpcomingDelivery){
				productCnt= getNoOfItemsForSoSettings(so);	
				amount=getTotalAmountForSoSettings(so);
		}else{
			productCnt= getNoOfItemsForUpcomingDelivery(so);	
			amount=so.getUpcomingDelivery().getTotal();
			map.put("orderId", so.getUpcomingDelivery().getErpSalesId());
		}
		map.put("noOfitems", productCnt );
		map.put("amount", amount);
		map.put("activated", "Y".equals(so.getActivate())?true:false);
		map.put("readyForActivation",populateResponseData(so, false).isActivate());
		map.put("errorHeader", so.getErrorHeader());
		map.put("errorDetails",so.getErrorDetail());
		if(null!=so.getLastError()){
			map.put("lastError", so.getLastError().name());
		} else {
			String lastError= isValidStandingOrder(so, false) && amount<ErpServicesProperties.getStandingOrderSoftLimit() ? "MINORDER":null;
			if("MINORDER".equals(lastError)){
				map.put("errorHeader", FDStandingOrder.ErrorCode.MINORDER.getErrorHeader());
				map.put("errorDetails",FDStandingOrder.ErrorCode.MINORDER.getErrorDetail(null));
			}
			map.put("lastError", lastError);
		}
		//map.put("dayOfWeek", so.getNextDeliveryDate()!=null?DateUtil.formatFullDayOfWk(so.getNextDeliveryDate()):null);
		map.put("dayOfWeek", so.getDayOfWeek(so, isUpcomingDelivery));
		map.put("shortDayOfWeek", so.getShortSODeliveryDayOfWeek(so, isUpcomingDelivery));
		map.put("deliveryDate", getSODeliveryDate(so, isUpcomingDelivery));
		map.put("deliveryTime",
				isUpcomingDelivery ? DateUtil.formatHourAMPMRange(so.getUpcomingDelivery()
						.getDeliveryStartTime(), so.getUpcomingDelivery()
						.getDeliveryEndTime())
						: so.getStartTime() != null ? DateUtil.formatHourAMPMRange(
								so.getStartTime(), so.getEndTime()) : "");

		//map.put("modifyDeliveryDate", isUpcomingDelivery?getModifyDeliveryDate(so.getUpcomingDelivery().getRequestedDate()):null);
		//map.put("shortDeliveryDate",so.getNextDeliveryDate()!=null? DateUtil.formatMonthAndDate(so.getNextDeliveryDate()):null );
		map.put("shortDeliveryDate", so.getShortSODeliveryDate(so, isUpcomingDelivery));
		map.put("cutOffFormattedDeliveryDate", so.getFormattedCutOffDeliveryDate());
		map.put("cutOffDeliveryTime", FDStandingOrder.cutOffDeliveryTime);
		map.put("tipAmount", so.getTipAmount());
		map.put("displayCart", isValidStandingOrder(so, true));
		map.put("currentDeliveryDate", map.get("deliveryDate"));
		map.put("currentDeliveryTime", map.get("deliveryTime"));
		map.put("currentDayOfWeek", map.get("dayOfWeek"));
		map.put("isEligibleToModify", isEligibleToModify(so));
		if(isEligibleToModify(so)){
			map.put("AddressInfo", soDeliveryAddress(so));
			map.put("paymentInfo", so.getPaymentMethod()!=null?so.getPaymentMethod().getAccountNumber():null);
		}
		return map;
	}
	
	public static double getTotalAmountForSoSettings(FDStandingOrder so) throws FDResourceException, FDInvalidConfigurationException {
		double amount = 0.0;
		List<FDCartLineI> cartLineIs = so.getStandingOrderCart().getOrderLines();
		if (null != cartLineIs) {
		 so.getStandingOrderCart().refreshAll(true);
		 amount=so.getStandingOrderCart().getTotal();
		 amount=amount+so.getTipAmount();
		}
		return amount;
	}

	private static Object getModifyDeliveryDate( Date deliveryDate ) {
		Calendar cl = Calendar.getInstance();
	    StringBuilder buf = new StringBuilder();
        
			cl.setTime(deliveryDate);

			buf.append(cl.get(Calendar.MONTH)+1);
			buf.append("/");
			buf.append(cl.get(Calendar.DATE));
		return buf.toString();
	}

	private static int getNoOfItemsForUpcomingDelivery(FDStandingOrder so) throws FDResourceException {
       
		FDOrderInfoI fdOrderInfoI=so.getUpcomingDelivery();
        
        int productCnt = 0;
        FDOrderI fdOrderI= FDCustomerManager.getOrder(fdOrderInfoI.getErpSalesId());
        productCnt=fdOrderI.getLineCnt();
        	
		return productCnt;
	}

	// Count the available items - as in QSController and QuickCart

	private static int getNoOfItemsForSoSettings(FDStandingOrder so) throws FDResourceException {
		int productCnt = 0;
		List<FDCartLineI> cartLineIs = so.getStandingOrderCart().getOrderLines();
		if (null != cartLineIs) {
			for (FDCartLineI cartLineI : cartLineIs) {
				ProductModel productNode = cartLineI.lookupProduct();
				if (!((productNode == null || productNode.getSku(cartLineI.getSkuCode()).isUnavailable() || cartLineI
						.isInvalidConfig()))) {
					productCnt++;
				}
			}
		}
		return productCnt;
	}
	
	public static FDStandingOrder populateStandingOrderDetails(FDStandingOrder so, Map<String, Object> responseDate) {
		FormLocationData addressLocationData=(FormLocationData) responseDate.get(SinglePageCheckoutFacade.ADDRESS_JSON_KEY);
		FormTimeslotData timeSlotData=(FormTimeslotData) responseDate.get(SinglePageCheckoutFacade.TIMESLOT_JSON_KEY);
		FormPaymentData paymentData=(FormPaymentData) responseDate.get(SinglePageCheckoutFacade.PAYMENT_JSON_KEY);

		if(null!=addressLocationData && null!=addressLocationData.getSelected()){
			so.setAddressId(addressLocationData.getSelected());
		}if(null!=paymentData && null!=paymentData.getSelected()){
			so.setPaymentMethodId(paymentData.getSelected());
		}if(null!=timeSlotData){
			so.setStartTime(timeSlotData.getStartDate());
			so.setEndTime(timeSlotData.getEndDate());
		}
		
	return so;
	
	}
	
	public static boolean isSO3StandingOrder(FDUserI user) {
		return  user.isNewSO3Enabled() && user.getCurrentStandingOrder() != null && user.getCurrentStandingOrder().getId() != null 
				&& user.getCurrentStandingOrder().isNewSo()? true : false;
	}
	
	public static void clearSO3Context(FDUserI user,HttpServletRequest request, String standingOrder){
		
		if(null == request.getParameter("isSO") && standingOrder==null){
        	FDSessionUser fdSessionUser=(FDSessionUser) user;
        	if(null!=user.getCurrentStandingOrder() && user.getCurrentStandingOrder().isNewSo()){
            	fdSessionUser.setCheckoutMode(EnumCheckoutMode.NORMAL);
                user.setCurrentStandingOrder(null);
        	}
        }
	}
	
	/* combined check for using SO 3.0
	 * method is needed for separate checks outside of here
	 * */
	public static boolean isEligibleForSo3_0(FDUserI user) {
		if (user.isEligibleForStandingOrders() && user.isNewSO3Enabled()) {
			return true;
		}
		return false;
	}

	/* get a single Hashmap has all data that soy files need
	 * can be used directly, returns "settingsData":{DATA} */
	public static HashMap<String,Object> getAllSoData(FDUserI user, boolean isAddtoProduct) {
		HashMap<String,Object> allSoData = new HashMap<String,Object>();
		HashMap<String,Object> soSettingsData = new HashMap<String,Object>();
		String selectedSoId=null;
		/* these are global settings */
		HashMap<String, Object> soSettings = new HashMap<String, Object>();
		soSettings.put("isEligibleForStandingOrders", isEligibleForSo3_0(user));
		soSettings.put("isContainerOpen", ((FDSessionUser)user).isSoContainerOpen()); /* replace with real value - get from fdsessionuser */
		soSettings.put("soHardLimitDisplay", StandingOrderHelper.formatDecimalPrice(ErpServicesProperties.getStandingOrderHardLimit()));
		soSettings.put("soSoftLimit", (int)(ErpServicesProperties.getStandingOrderSoftLimit()));
		allSoData.put("soSettings", soSettings);
		
		/* these are the so's themselves */
		Collection<FDStandingOrder> standingOrders = new ArrayList<FDStandingOrder>();
		Collection<Map<String, Object>> soData = new ArrayList<Map<String, Object>>();

		try {
			if(null != user.getIdentity()){
				standingOrders = isAddtoProduct?getValidSO3(user): FDStandingOrdersManager.getInstance().loadCustomerNewStandingOrders(user.getIdentity());
			    soData = StandingOrderHelper.convertStandingOrderToSoy(FDStandingOrdersManager.getInstance().getAllSOUpcomingOrders(user, standingOrders), false);
			    
			    if(null!=standingOrders && !standingOrders.isEmpty()){
					for(FDStandingOrder fdStandingOrder:standingOrders){
						if(fdStandingOrder.isDefault()){
							selectedSoId=fdStandingOrder.getId();
							break;
						}
					}
				}
			    
			    // TO Display current delivery date
			    populateCurrentDeliveryDate(user,soData);
			}
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error While Getting the valid standing Order" + e);
		} catch (FDInvalidConfigurationException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error While Getting the valid standing Order" + e);
		} catch (PricingException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error While Getting the valid standing Order" + e);
		}
		catch (FDAuthenticationException e) {
					// TODO Auto-generated catch block
				LOGGER.error("Error While Gettig the upcoming Order" + e);
		}
        

		soSettings.put("selectedSoId", selectedSoId); 

		allSoData.put("soData", soData);
		
		soSettingsData.put("settingsData", allSoData);
		
		return soSettingsData;
	}
	private static void populateCurrentDeliveryDate(FDUserI user,
			Collection<Map<String, Object>> soData) throws FDResourceException {
		FDOrderHistory h = (FDOrderHistory) user.getOrderHistory();
		for(Map<String,Object> soDataMap:soData){
			a:for (ErpSaleInfo i : h.getErpSaleInfos()) {
				if (soDataMap.get("id").toString().equalsIgnoreCase(i.getStandingOrderId())) {
					if(!i.getStatus().isCanceled() && !new Date().after(i.getRequestedDate())){
						soDataMap.put("currentDeliveryDate", DateUtil.formatMonthAndDate(i.getRequestedDate()));
						soDataMap.put("currentDeliveryTime", DateUtil.formatHourAMPMRange(i.getDeliveryStartTime(), i.getDeliveryEndTime()));
						soDataMap.put("currentDayOfWeek", DateUtil.formatFullDayOfWk(i.getRequestedDate()));
						
					}
					break a;
				}
			}
		}
	}
	public static void populateCurrentDeliveryDate(FDUserI user,Map<String, Object> soDataMap) throws FDResourceException {
		
		FDOrderHistory h = (FDOrderHistory) user.getOrderHistory();
			a:for (ErpSaleInfo i : h.getErpSaleInfos()) {
				if (soDataMap.get("id").toString().equalsIgnoreCase(i.getStandingOrderId())) {
					if(!i.getStatus().isCanceled() && !new Date().after(i.getRequestedDate())){
						soDataMap.put("currentDeliveryDate", DateUtil.formatMonthAndDate(i.getRequestedDate()));
						soDataMap.put("currentDeliveryTime", DateUtil.formatHourAMPMRange(i.getDeliveryStartTime(), i.getDeliveryEndTime()));
						soDataMap.put("currentDayOfWeek", DateUtil.formatFullDayOfWk(i.getRequestedDate()));
						
					}
					break a;
			}
		}
	}
	protected static Collection<FDStandingOrder> getValidSO3(FDUserI user) throws FDResourceException, FDInvalidConfigurationException {
		if(user.isRefreshValidSO3()){
			user.setValidSO3(FDStandingOrdersManager.getInstance().getValidStandingOrder(user.getIdentity()));
			user.setRefreshValidSO3(false);
		}

		return user.getValidSO3();
	}

	public static boolean isValidStandingOrder(FDUserI user) {
	        FDStandingOrder so=isSO3StandingOrder(user)? user.getCurrentStandingOrder():null;
	        return isValidStandingOrder(so,true);
		}

	public static boolean isValidStandingOrder(FDStandingOrder so,boolean noErronCheck) {
		
		return null!=so && null!=so.getAddressId()&& null!=so.getPaymentMethodId() && null!=so.getNextDeliveryDate() && so.getFrequency()>0 
				&& (noErronCheck ? true:so.getLastError() ==null) ? true:false;
	}

	public static StandingOrderResponseData populateResponseData(FDStandingOrder so,boolean isPdp) {
		
		StandingOrderResponseData orderResponseData = new StandingOrderResponseData();

		try {
			if (null != so) {
				orderResponseData.setName(so.getCustomerListName());
				orderResponseData.setId(so.getId());
				if (isPdp) {
					orderResponseData.setProductCount(getNoOfItemsForSoSettings(so) + " items");
					orderResponseData.setAmount(getTotalAmountForSoSettings(so));
					if (orderResponseData.getAmount() <= ErpServicesProperties.getStandingOrderSoftLimit()) {
						orderResponseData.setMessage(" Add $"
								+ StandingOrderHelper.formatDecimalPrice((ErpServicesProperties.getStandingOrderSoftLimit() - orderResponseData.getAmount()))
								+ " to meet the $" + StandingOrderHelper.formatDecimalPrice(ErpServicesProperties.getStandingOrderSoftLimit()) + " minimum");
					} else {
						orderResponseData
								.setMessage("Changes will begin with your "
										+ getModifyDeliveryDate(so.getNextDeliveryDate())
										+ " delivery.");
					}
				} else {
					if (isValidStandingOrder(so,false) && Calendar.getInstance().getTime().before(so.getCutOffDeliveryDateTime())) {
						if (getTotalAmountForSoSettings(so) >= ErpServicesProperties.getStandingOrderSoftLimit()) {
							orderResponseData.setActivate(isSOActivated(so)?false:true);
						} else {
							orderResponseData
							.setMessage("Must add items to cart and meet the $" + StandingOrderHelper.formatDecimalPrice(ErpServicesProperties.getStandingOrderSoftLimit()) + " minimum to receive a delivery");
						}
					} else {
						orderResponseData
								.setMessage("This order is incomplete. Add missing info to add your Standing Order");
					}

				}
			} else {
				orderResponseData
						.setMessage("Error while adding product to the standing order ");

			}
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error While Getting standing order response data "+e);
			orderResponseData
					.setMessage("Error while adding product to the standing order ");

		} catch (FDInvalidConfigurationException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error While Getting the standing order response data"+e);
			orderResponseData
					.setMessage("Error while adding product to the standing order ");
		}
		return orderResponseData;
	}

	/**
	 * Format method to remove trailing decimals.
	 * Used in soy template for user cart minimum alerts
	 * @param number
	 * @return
	 */
	public static String formatDecimalPrice(double number) {
		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		return decimalFormat.format(number);
	}

	public static boolean isSOActivated(FDStandingOrder so){
		
		if(null!=so && "Y".equals(so.getActivate())){
			return true;
		}
		return false;
	}
	
	/*
	 *  To clear last error details if user has fixed anyone of the following error
	 *  ADDRESS,PAYMENT ,NO_ADDRESS,MINORDER
	 */
	public static void clearSO3ErrorDetails(FDStandingOrder so,String[] userModules){
		for( String userModule:userModules){
			if(userModule.equals(so.getLastErrorCode())){
				so.clearLastError();
			}
		}

	}
	
	public static void populateSO3TimeslotDetails(FDUserI user, String deliveryTimeSlotId,
			ErpAddressModel erpAddress, String soNextDeliveryDate) throws ParseException,
			FDResourceException {
		FDTimeslot timeSlot = FDDeliveryManager.getInstance().getTimeslotsById(deliveryTimeSlotId,erpAddress.getBuildingId(), true);

		FDStandingOrder so = user.getCurrentStandingOrder();
		user.getCurrentStandingOrder().setNextDeliveryDate(DateUtil.parseMDY(soNextDeliveryDate));
		
		
/*		FDOrderHistory history = (FDOrderHistory)user.getOrderHistory();
		if (null != history.getFDOrderInfos() && !history.getFDOrderInfos().isEmpty()) {
			for (FDOrderInfoI fdOrderInfo : history.getFDOrderInfos()) {
				if (so.getId().equals(fdOrderInfo.getStandingOrderId()) && so.getFrequency()>0) {
					user.getCurrentStandingOrder().calculateNextDeliveryDate(
							so.getNextDeliveryDate());
					break;
				}
			}
		} */
		
		so.setStartTime(timeSlot.getStartTime());
		so.setEndTime(timeSlot.getEndTime());
		so.setTimeSlotId(timeSlot.getId());
		Calendar c = Calendar.getInstance();
		c.setTime(so.getNextDeliveryDate());
		so.setReservedDayOfweek(c.get(Calendar.DAY_OF_WEEK));
	}
	
	public static void loadSO3CartTimeSlot(FormTimeslotData timeslotData,FDStandingOrder so) {
        if(null!=so.getNextDeliveryDate()&& null!=so.getStartTime()&& null!=so.getEndTime()){
            Calendar startTimeCalendar = DateUtil.toCalendar(so.getNextDeliveryDate());
            String dayNames[] = new DateFormatSymbols().getWeekdays();
            timeslotData.setId(DEFAULT_TIMESLOT_ID);
            timeslotData.setYear(String.valueOf(startTimeCalendar.get(Calendar.YEAR)));
            timeslotData.setMonth(String.valueOf(startTimeCalendar.get(Calendar.MONTH) + 1));
            timeslotData.setDayOfMonth(String.valueOf(startTimeCalendar.get(Calendar.DAY_OF_MONTH)));
            timeslotData.setDayOfWeek(dayNames[startTimeCalendar.get(Calendar.DAY_OF_WEEK)]);
            timeslotData.setTimePeriod(format(DateUtil.toCalendar(so.getStartTime()), DateUtil.toCalendar(so.getEndTime())));
            timeslotData.setStartDate(so.getNextDeliveryDate());
            timeslotData.setEndDate(so.getNextDeliveryDate());
            timeslotData.setSoCutOffFormattedDeliveryDate(so.getFormattedCutOffDeliveryDate());
            timeslotData.setSoCutOffDeliveryTime(FDStandingOrder.cutOffDeliveryTime);
            timeslotData.setShortDayOfWeek(DateUtil.formatDayOfWk(so.getNextDeliveryDate()));
            timeslotData.setSoDeliveryDate(DateUtil.formatMonthAndDate(so.getNextDeliveryDate()));
        }
		timeslotData.setNewSO3(true);
        timeslotData.setSoFreq(so.getFrequency()>=1? Integer.toString(so.getFrequency()):null);
        timeslotData.setSoActivated(null!=so.getActivate() && "Y".equals(so.getActivate())?true:false);

	}
 protected static String format(Calendar startCal, Calendar endCal) {
        StringBuffer sb = new StringBuffer();

        formatCal(startCal, sb);
        sb.append(" - ");
        formatCal(endCal, sb);

        return sb.toString();
    }
 protected static void formatCal(Calendar cal, StringBuffer sb) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int marker = cal.get(Calendar.AM_PM);

        if (hour > 12) {
            sb.append(hour - 12);
        } else {
            sb.append(hour);
        }

        if (minute != 0) {
            sb.append(':').append(minute);
        }

        if (marker == Calendar.AM) {
            sb.append("AM");
        } else {
            sb.append("PM");
        }
    }
 
 public static boolean findSO3MatchTimeslot(FDTimeslot slot, FDStandingOrder so){
	 boolean isMatch=false;
     if(null!=so.getNextDeliveryDate() && null!=so.getStartTime()){
    	 
	 	Calendar calendar1 = Calendar.getInstance();
	 	Calendar calendar2 = Calendar.getInstance();
    
	 	calendar1.setTime(slot.getDeliveryDate());
	 	calendar2.setTime(so.getNextDeliveryDate());

	 	boolean sameDay = calendar1.get(Calendar.DAY_OF_WEEK) == calendar2.get(Calendar.DAY_OF_WEEK);
	 	isMatch=sameDay && convert(slot.getDlvStartTime().getAsDate()).equals(convert(so.getStartTime())) && convert(slot.getDlvEndTime().getAsDate()).equals(convert(so.getEndTime()))? true:false;
     }
	 return isMatch;
	 
 }
 
private static String convert(Date time) {
		 return FORMATTER.format(time);
	}

	public static boolean displayFirstSODelivery(FDUserI user) {
        boolean flg=true;
		if (isSO3StandingOrder(user)) {
			FDStandingOrder so = user.getCurrentStandingOrder();
			try {
				FDOrderHistory h = (FDOrderHistory) user.getOrderHistory();
				for (ErpSaleInfo i : h.getErpSaleInfos()) {
					if (so.getId().equalsIgnoreCase(i.getStandingOrderId())) {
						flg=false;
						break;
					}
				}
			} catch (FDResourceException e) {
				LOGGER.error(" Error while validating SO 3 first delivery date  :: SO Id :: "
						+ so.getId());
			}
		} else {
			flg=false;
		}

		return flg;
	}
	
	private static String soDeliveryAddress(FDStandingOrder so) {
		try {
			FDOrderInfoI fdOrderInfoI=so.getUpcomingDelivery();
	        FDReservation fDReservation=FDCustomerManager.getOrder(fdOrderInfoI.getErpSalesId()).getDeliveryReservation();
	        if(!so.getAddressId().equalsIgnoreCase(fDReservation!=null?fDReservation.getAddressId():""))
	        	return so.getDeliveryAddress()!=null?(so.getDeliveryAddress().getScrubbedStreet()+","+so.getDeliveryAddress().getZipCode()):null;	
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			LOGGER.info("while prepare the SoDeliveryAddress " +e);
		}
		return null;
	}

	private static boolean isEligibleToModify(FDStandingOrder so) {
		try {
			return so.getUpcomingDelivery().getErpSalesId()!=null?true:false;
			//return so.getAllUpcomingOrders().isEmpty()?false:true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.info("while check an Upcoming Delivery " +e);
		}
		return false;
	}
}