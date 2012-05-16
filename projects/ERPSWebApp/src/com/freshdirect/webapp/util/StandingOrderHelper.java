package com.freshdirect.webapp.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDTimeslot;
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

			c.setTime( ts.getBaseDate() );
			this.day = c.get(Calendar.DAY_OF_WEEK);
			
			c.setTime( ts.getBegDateTime());
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
			
			buf.append(StandingOrderHelper.formatTime(startHour, endHour));
			buf.append(", ");
			buf.append( DAY_NAMES[ day ] );
			
			return buf.toString();
		}
		
		public String getDayName() {
			return StandingOrderHelper.DAY_NAMES[day];
		}
		
		public String formatTime() {
			return StandingOrderHelper.formatTime(startHour, endHour);
		}
	}


	
	public static String formatTime(Date s, Date e) {
		int sh = dateToCalendar(s).get(Calendar.HOUR_OF_DAY);
		int eh = dateToCalendar(e).get(Calendar.HOUR_OF_DAY);
		
		return formatTime(sh, eh);
	}
	
	public static String formatTime(int sh, int eh) {
		StringBuilder sb = new StringBuilder();
		
		if ( sh < 12 ) {
			sb.append( sh );
			sb.append( AM );
		} else if ( sh == 12 ) {
			sb.append( NOON );			
		} else {
			sb.append( sh-12 );
			sb.append( PM );
		}
		
		sb.append( SEPARATOR );
		
		if ( eh < 12 ) {
			sb.append( eh );
			sb.append( AM );
		} else if ( eh == 12 ) {
			sb.append( NOON );			
		} else {
			sb.append( eh-12 );
			sb.append( PM );
		}

		return sb.toString();
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
			
			final Date __ds = so.getStartTime();
			final Date __de = so.getEndTime();


			for (FDTimeslot ts : l) {
				if (ts.isMatching(selDate, __ds, __de)) {
					// exact timeslot match!
					LOGGER.debug(" +--> " + ts + " (exact match)");
					__so_timeslotId = ts.getTimeslotId();
					break;
				} else if (ts.isWithinRange(selDate, __ds) /* || ts.isWithinRange(selDate, __de) */) {
					// starting bound falls within timeslot
					LOGGER.debug(" ===> " + ts+ " (overlap)");
					__so_timeslotId = ts.getTimeslotId();
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
	 * @return
	 */
	public static List<List<Date>> getAllDeliveryDateCandidates(FDStandingOrder so) {
		List<List<Date>> cMat = new ArrayList<List<Date>>(7);

		// prepare calendar
		final Calendar c = Calendar.getInstance();
		// c.setTime(so.getNextDeliveryDate());

		
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
			
			for (int k = 0; k<so.getFrequency(); k++) {
				// leap ahead with a week
				c.add(Calendar.WEEK_OF_YEAR, 1);
				// this ensures that a newly chosen delivery date will fall
				// at least in the next week or later 

				candidates.add(c.getTime());
			}

			cMat.add(candidates);
		}
		
		return cMat;
	}
}
