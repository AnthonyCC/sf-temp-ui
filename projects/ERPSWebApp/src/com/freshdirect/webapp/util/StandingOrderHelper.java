package com.freshdirect.webapp.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.standingorders.DeliveryInterval;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.FDTimeslotUtil;

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
					if(ts.isTimeslotRestricted()) ts.setTimeslotRestricted(false);
					break;
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
					if(ts.isTimeslotRestricted()) ts.setTimeslotRestricted(false);
					break;
				}
			}
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
	
}
