package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.freshdirect.delivery.EnumDayShift;
import com.freshdirect.delivery.EnumRegionServiceType;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import java.text.DecimalFormat;

/**
 * @version $Revision:3$
 * @author $Author:Kashif Nadeem$
 */

public class FDTimeslot implements Serializable, Comparable<FDTimeslot> {

	private static final long	serialVersionUID	= 4180048326412481300L;

	@SuppressWarnings( "unused" )
	private final static Logger LOGGER = LoggerFactory.getInstance( FDTimeslot.class );
	
	private final DlvTimeslotModel dlvTimeslot;
	private boolean isAlcoholRestricted;
	private boolean normalAvailCapacity;
	private boolean availCTCapacity;
	private boolean isHolidayRestricted;
	private boolean timeslotRemoved;
	private boolean geoRestricted;
	private String storeFrontAvailable;
	private boolean unavailable;
	private DecimalFormat premiumAmountFmt = new DecimalFormat("#.##");
	private String minOrderMsg = "";
	private double minOrderAmt = 0;
	private boolean minOrderMet = true;
	private boolean timeslotRestricted;
	/** Creates new FDTimeslot */
	public FDTimeslot(DlvTimeslotModel dlvTimeslot) {
		this.dlvTimeslot = dlvTimeslot;
	}

	public Date getBaseDate() {
		return dlvTimeslot.getBaseDate();
	}

	public Date getBegDateTime() {
		return dlvTimeslot.getStartTimeAsDate();
	}

	public Date getBegTime() {
		return dlvTimeslot.getStartTime().getNormalDate();
	}

	public Date getEndDateTime() {
		return dlvTimeslot.getEndTimeAsDate();
	}

	public Date getEndTime() {
		return dlvTimeslot.getEndTime().getNormalDate();
	}

	public Date getCutoffDateTime() {
		return dlvTimeslot.getCutoffTimeAsDate();
	}
	
	public Date getPremiumCutoffDateTime() {
		return dlvTimeslot.getPremiumCutoffAsDate();
	}

	public Date getCutoffNormalDateTime() {
		return dlvTimeslot.getCutoffTimeAsNormalDate();
	}

	public String getZoneId() {
		return dlvTimeslot.getZoneId();
	}

	public String getZoneCode() {		
		return dlvTimeslot.getZoneCode();
	}
	public int getTotalAvailable() {
		return dlvTimeslot.getTotalAvailable();
	}

	public int getBaseAvailable() {
		return dlvTimeslot.getBaseAvailable();
	}

	public int getChefsTableAvailable() {
		return dlvTimeslot.getChefsTableAvailable();
	}

	public int getStatusCode() {
		return dlvTimeslot.getStatus().getValue();
	}

	public String getTimeslotId() {
		return dlvTimeslot.getPK().getId();
	}

	public String getDisplayString(boolean forceAmPm) {
		return format(forceAmPm, DateUtil.toCalendar(this.getBegDateTime()), DateUtil.toCalendar(this.getEndDateTime()));
	}
	
	public String getDisplayString() {
		return getDisplayString(false);
	}
	
	public int getDayOfWeek() {
		return DateUtil.getDayOfWeek(dlvTimeslot.getBaseDate());
	}

	public static String format(Date startDate, Date endDate) {
		return format(true, DateUtil.toCalendar(startDate), DateUtil.toCalendar(endDate));
	}

	private static String format(boolean forceAmPm, Calendar startCal, Calendar endCal) {
		StringBuffer sb = new StringBuffer();

		formatCal(startCal, false, sb);
		sb.append("-");
		formatCal(endCal, true, sb);

		return sb.toString();
	}

	private static void formatCal(Calendar cal, boolean showAmPm, StringBuffer sb) {
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int marker = cal.get(Calendar.AM_PM);

		if (hour > 12) {
			sb.append(hour - 12);
		} else {
			sb.append(hour);
		}

		if (minute != 0) {
			sb.append(":").append(minute);
		}
		if (showAmPm) {
			if (marker == Calendar.AM) {
				sb.append("am");
			} else {
				sb.append("pm");
			}
		}
	}
	
	public boolean isRoutingSlotMatching(Date baseDate, Date startTime, Date endTime){
	    return this.dlvTimeslot.isRoutingSlotMatching(baseDate, startTime, endTime);
	}

	public boolean isMatching(Date baseDate, Date startTime, Date endTime){
	    return this.dlvTimeslot.isMatching(baseDate, startTime, endTime);
	}

	
	public boolean isWithinRange(Date baseDate, Date t) {
		return this.dlvTimeslot.isWithinRange(baseDate, t);
	}


	public DlvTimeslotModel getDlvTimeslot() {
		return dlvTimeslot;
	}
	
	public double getSteeringDiscount() {
		return dlvTimeslot.getSteeringDiscount();
	}

	public boolean isAlcoholRestricted() {
		return isAlcoholRestricted;
	}
	
	public void setAlcoholRestricted(boolean isAlcoholRestricted) {
		this.isAlcoholRestricted = isAlcoholRestricted;
	}
	
	public boolean hasNormalAvailCapacity() {
		return normalAvailCapacity;
	}

	public void setNormalAvailCapacity(boolean normalAvailCapacity) {
		this.normalAvailCapacity = normalAvailCapacity;
	}

	public boolean hasAvailCTCapacity() {
		return availCTCapacity;
	}

	public void setAvailCTCapacity(boolean availCTCapacity) {
		this.availCTCapacity = availCTCapacity;
	}
	
	/* Eco Friendly timeslot*/
	public boolean isEcoFriendly() {
		return dlvTimeslot.isEcoFriendly();
	}
	
	
	// Early AM timeslot
	public boolean isEarlyAM() {
		if (this.getBegTime() != null
				&& this.getBegTime().before(new TimeOfDay(getEarlyAMTime()).getNormalDate())) {
			return true;
		}
		return false;
	}

	private Date getEarlyAMTime() {
		Calendar _earlyWindowCalInstance = Calendar.getInstance();
		_earlyWindowCalInstance.set(Calendar.HOUR_OF_DAY, FDStoreProperties.getEarlyAMWindowHour());
		_earlyWindowCalInstance.set(Calendar.MINUTE, FDStoreProperties.getEarlyAMWindowMinute());
		_earlyWindowCalInstance.set(Calendar.SECOND, 0);
		_earlyWindowCalInstance.set(Calendar.MILLISECOND, 0);
		return _earlyWindowCalInstance.getTime();
	}
	
	/* is_Depot timeslot*/
	public boolean isDepot() {
		return dlvTimeslot.isDepot();
	}
	
	public String getTimeslotShift() {
		Calendar startTimeCal = DateUtil.toCalendar(this.getBegDateTime());
		int startHour = startTimeCal.get(Calendar.HOUR_OF_DAY);
		if(startHour > 12)
			return EnumDayShift.DAY_SHIFT_PM.getName();
		else
			return EnumDayShift.DAY_SHIFT_AM.getName();		
	}

	public String toString() {
		return dlvTimeslot.toString();
	}
	
	@Override
	public int compareTo(FDTimeslot t1) {
		return this.getBegTime().compareTo(t1.getBegTime());
	}
	
	public static String getDisplayString(boolean forceAmPm, Date startTime, Date endTime) {
		return format(forceAmPm, DateUtil.toCalendar(startTime), DateUtil.toCalendar(endTime));
	}

	public boolean isTimeslotRemoved() {
		return timeslotRemoved;
	}

	public void setTimeslotRemoved(boolean timeslotRemoved) {
		this.timeslotRemoved = timeslotRemoved;
	}

	public boolean isHolidayRestricted() {
		return isHolidayRestricted;
	}

	public void setHolidayRestricted(boolean isHolidayRestricted) {
		this.isHolidayRestricted = isHolidayRestricted;
	}

	public boolean isGeoRestricted() {
		return geoRestricted;
	}

	public void setGeoRestricted(boolean geoRestricted) {
		this.geoRestricted = geoRestricted;
	}

	public String getStoreFrontAvailable() {
		return storeFrontAvailable;
	}

	public void setStoreFrontAvailable(String storeFrontAvailable) {
		this.storeFrontAvailable = storeFrontAvailable;
	}

	/* Steering Radius */
	public boolean hasSteeringRadius() {
		return dlvTimeslot.hasSteeringRadius();
	}
	
	public boolean isUnavailable() {
		return unavailable;
	}

	public void setUnavailable(boolean unavailable) {
		this.unavailable = unavailable;
	}

	public double getPremiumAmount() {
		return dlvTimeslot.getPremiumAmount();
	}
	
	public String getPremiumAmountFmt() {
		return "ADD&nbsp;$"+premiumAmountFmt.format(dlvTimeslot.getPremiumAmount());
	}
	
	public int getTotalConfirmed() {
		return dlvTimeslot.getTotalConfirmed();
	}
	
	public EnumRegionServiceType getRegionSvcType() {
		return dlvTimeslot.getRegionSvcType();
	}

	public String getMinOrderMsg() {
		return minOrderMsg;
	}

	public void setMinOrderMsg(String minOrderMsg) {
		this.minOrderMsg = minOrderMsg;
	}

	public void setTimeslotRestricted(boolean tsRestricted) {
		this.timeslotRestricted = tsRestricted;
	}

	public boolean isTimeslotRestricted() {
		return timeslotRestricted;
	}

	public double getMinOrderAmt() {
		return minOrderAmt;
	}

	public boolean isMinOrderSlot() {
		return (minOrderAmt>0)?true:false;
	}

	public void setMinOrderAmt(double minOrderAmt) {
		this.minOrderAmt = minOrderAmt;
	}

	public boolean isMinOrderMet() {
		return minOrderMet;
	}

	public void setMinOrderMet(boolean minOrderMet) {
		this.minOrderMet = minOrderMet;
	}
}
