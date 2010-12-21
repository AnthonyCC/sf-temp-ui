package com.freshdirect.fdstore.standingorders;

import java.util.Calendar;
import java.util.Date;

import com.freshdirect.fdstore.FDTimeslot;


public class DeliveryInterval {
	
	protected Calendar	start			= null;
	protected Calendar	end				= null;
	protected Calendar	dayStart		= null;
	protected Calendar	dayEnd			= null;
	protected Calendar	nextDelivery	= null;
	protected Calendar	windowStart		= null;
	
	public DeliveryInterval( FDStandingOrder so ) throws IllegalArgumentException, NullPointerException {
		this( so.getNextDeliveryDate(), so.getStartTime(), so.getEndTime() );
	}
	
	public DeliveryInterval( Date nextDeliveryDate, Date startTime, Date endTime ) throws IllegalArgumentException {
		
		if ( nextDeliveryDate == null || startTime == null || endTime == null ) {
			throw new IllegalArgumentException( "Some of the input dates are null." );
		}
		
		try {
			nextDelivery = Calendar.getInstance();
			nextDelivery.setTime( nextDeliveryDate );
			
			start = Calendar.getInstance();
			start.setTime( startTime );
			start.set( Calendar.YEAR, nextDelivery.get( Calendar.YEAR ) );
			start.set( Calendar.MONTH, nextDelivery.get( Calendar.MONTH ) );
			start.set( Calendar.DATE, nextDelivery.get( Calendar.DATE ) );
			
			end = Calendar.getInstance();
			end.setTime( endTime );
			end.set( Calendar.YEAR, nextDelivery.get( Calendar.YEAR ) );
			end.set( Calendar.MONTH, nextDelivery.get( Calendar.MONTH ) );		
			end.set( Calendar.DATE, nextDelivery.get( Calendar.DATE ) );
			
			dayStart = Calendar.getInstance();
			dayStart.setTime( nextDeliveryDate );
			dayStart.set( Calendar.HOUR_OF_DAY, 0 );
			dayStart.set( Calendar.MINUTE, 0 );
			dayStart.set( Calendar.SECOND, 0 );
			dayStart.set( Calendar.MILLISECOND, 0 );
			
			dayEnd = Calendar.getInstance();
			dayEnd.setTime( nextDeliveryDate );
			dayEnd.add( Calendar.DAY_OF_YEAR, 1 );
			dayEnd.set( Calendar.HOUR_OF_DAY, 0 );
			dayEnd.set( Calendar.MINUTE, 0 );
			dayEnd.set( Calendar.SECOND, 0 );
			dayEnd.set( Calendar.MILLISECOND, 0 );
			
			windowStart = Calendar.getInstance();
			windowStart.setTime( dayStart.getTime() );
			windowStart.add( Calendar.DATE, -7 );
			
		} catch ( ArrayIndexOutOfBoundsException ex ) {
			throw new IllegalArgumentException( "Some of the input dates are invalid.", ex );
		}
	}
	
	public Date getStart() {
		if ( start == null ) return null;
		return start.getTime();
	}
	public Date getEnd() {
		if ( end == null ) return null;
		return end.getTime();
	}
	public Date getDayStart() {
		if ( dayStart == null ) return null;
		return dayStart.getTime();
	}
	public Date getDayEnd() {
		if ( dayEnd == null ) return null;
		return dayEnd.getTime();
	}
	public Date getNextDelivery() {
		if ( nextDelivery == null ) return null;
		return nextDelivery.getTime();
	}
	public Date getWindowStart() {
		if ( windowStart == null ) return null;
		return windowStart.getTime();
	}	
	
	public boolean isWithinDeliveryWindow() {
		if ( windowStart == null ) return false;
		return windowStart.getTime().before( new Date() );
	}
	
	public static boolean isWithinDeliveryWindow( Date nextDate ) {
		Calendar window = Calendar.getInstance();
		window.setTime( nextDate );
		window.set( Calendar.HOUR_OF_DAY, 0 );
		window.set( Calendar.MINUTE, 0 );
		window.set( Calendar.SECOND, 0 );
		window.set( Calendar.MILLISECOND, 0 );
		window.add( Calendar.DATE, -7 );		
		return window.getTime().before( new Date() );
	}
	
	public boolean checkTimeslot( FDTimeslot timeslot ) {
		if ( timeslot == null || start == null || end == null ) 
			return false;
		
		Date reqStart = start.getTime();
		Date reqEnd = end.getTime();
		
		Date tsStart = timeslot.getBegDateTime();
		Date tsEnd = timeslot.getEndDateTime();
		Date cutoffTime = timeslot.getCutoffDateTime();

		return ( reqStart.equals( tsStart ) || reqStart.before( tsStart ) ) && ( reqEnd.equals( tsEnd ) || reqEnd.after( tsEnd ) ) && cutoffTime.after( new Date() );
	}
	
	@Override
	public String toString() {
		return "DeliveryInterval[ " + nextDelivery.getTime().toString() + " / " + start.getTime().toString() + " / " +  end.getTime().toString() + " ]";
	}
}
