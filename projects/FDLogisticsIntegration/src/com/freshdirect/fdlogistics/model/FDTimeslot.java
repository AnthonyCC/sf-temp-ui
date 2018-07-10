package com.freshdirect.fdlogistics.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.fdlogistics.deserializer.EnumRegionServiceTypeDeserializer;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.EnumDayShift;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;

/**
 * @version $Revision:3$
 * @author $Author:Kashif Nadeem$
 */

public class FDTimeslot implements Serializable, Comparable<FDTimeslot> {

	public FDTimeslot() {
		super();
	}

	public FDTimeslot(String id, Date deliveryDate, TimeOfDay dlvStartTime,
			TimeOfDay dlvEndTime, TimeOfDay cutoffTime, TimeOfDay premiumCutoffTime, Date cutoffDateTime, Date premiumCutoffDateTime, 
			String zoneId, String zoneCode,
			boolean normalAvailCapacity, boolean availCTCapacity,
			boolean isGeoRestricted, boolean isTimeslotRestricted, boolean isTimeslotRemoved, String storeFrontAvailable,
			boolean isUnavailable, boolean isEcoFriendly, boolean isSoldOut, boolean isDepot, 
			boolean isPremiumSlot, boolean isFdxSlot, double totalAvailable, double baseAvailable, double chefsTableAvailble,
			boolean hasSteeringRadius, String travelZone, double minDurationForModStart, double minDurationForModification,
			int additionalDistance, EnumRegionServiceType regionSvcType,Date  soFirstDeliveryDate, Date originalCutoffDateTime , int capacityUtilizationPercentage)
	{
		super();
		this.id = id;
		this.deliveryDate = deliveryDate;
		this.dlvStartTime = dlvStartTime;
		this.dlvEndTime = dlvEndTime;
		this.cutoffTime = cutoffTime;
		this.premiumCutoffTime = premiumCutoffTime;
		
		this.cutoffDateTime = cutoffDateTime;
		this.premiumCutoffDateTime = premiumCutoffDateTime;
		
		this.zoneId = zoneId;
		this.zoneCode = zoneCode;
		
		this.normalAvailCapacity = normalAvailCapacity;
		this.availCTCapacity = availCTCapacity;
		
		this.isGeoRestricted = isGeoRestricted;
		this.isTimeslotRestricted = isTimeslotRestricted;
		this.isUnavailable = isUnavailable;
		this.isEcoFriendly = isEcoFriendly;
		this.isSoldOut = isSoldOut;
		this.isDepot = isDepot;
		this.isPremiumSlot = isPremiumSlot;
		
		this.isTimeslotRemoved = isTimeslotRemoved;
		this.storeFrontAvailable = storeFrontAvailable;
		this.isFdxSlot = isFdxSlot;
		
		this.totalAvailable = totalAvailable;
		this.baseAvailable = baseAvailable;
		this.chefsTableAvailble = chefsTableAvailble;
		this.additionalDistance = additionalDistance;
		this.hasSteeringRadius = hasSteeringRadius;
		this.travelZone = travelZone;
		this.minDurationForModStart = minDurationForModStart;
		this.minDurationForModification = minDurationForModification;
		this.regionSvcType = regionSvcType;
		this.soFirstDeliveryDate=soFirstDeliveryDate;
		this.originalCutoffDateTime = originalCutoffDateTime;
		this.capacityUtilizationPercentage = capacityUtilizationPercentage;
	}

	
	private static final long serialVersionUID = 4180048326412481300L;

	private final static Logger LOGGER = LoggerFactory
			.getInstance(FDTimeslot.class);

	// gets sets properties
	private String id;
	private Date deliveryDate;
	private TimeOfDay dlvStartTime;
	private TimeOfDay dlvEndTime;
	private TimeOfDay premiumCutoffTime;
	private TimeOfDay cutoffTime;
	private Date cutoffDateTime;
	private Date premiumCutoffDateTime;

	private String zoneId;
	private String zoneCode;

	private boolean normalAvailCapacity;
	private boolean availCTCapacity;

	private boolean isAlcoholRestricted;
	private boolean isHolidayRestricted;
	private boolean isGeoRestricted;
	private boolean isTimeslotRestricted;

	private boolean isTimeslotRemoved;
	private String storeFrontAvailable;
	private boolean isUnavailable;
	
	private boolean isEcoFriendly;
	private boolean isSoldOut;
	private boolean isDepot;
	private boolean isPremiumSlot;
	private boolean isFdxSlot;
	
	private String minOrderMsg = "";
	private double minOrderAmt = 0;
	private boolean minOrderMet = true;
	
	private double steeringDiscount;

	private double totalAvailable;
	private double baseAvailable;
	private double chefsTableAvailble;

	private int additionalDistance;
	private boolean hasSteeringRadius;

	// gets properties only
	private Date handoffDateTime;

	private double premiumAmount;
	
	//fdx properties
	
	private String travelZone;
	private double minDurationForModStart;
	private double minDurationForModification;
	@JsonDeserialize(using = EnumRegionServiceTypeDeserializer.class)
	private EnumRegionServiceType regionSvcType;
	private Date soFirstDeliveryDate;
	private double deliveryFee;
	private double promoDeliveryFee;
	private EnumDeliveryFeeTier dlvfeeTier;
	private Date originalCutoffDateTime;
	private FDDeliveryZoneInfo zoneInfo;
	private int capacityUtilizationPercentage;
	private boolean isMidWeekDlvPassApplicable;

	private static final DecimalFormat premiumAmountFmt = new DecimalFormat(
			"#.##");

	public String getDisplayString(boolean forceAmPm) {
		return format(forceAmPm, DateUtil.toCalendar(this.getStartDateTime()),
				DateUtil.toCalendar(this.getEndDateTime()));
	}

	public String getDisplayString() {
		return getDisplayString(false);
	}

	public int getDayOfWeek() {
		return DateUtil.getDayOfWeek(this.getDeliveryDate());
	}

	public static String format(Date startDate, Date endDate) {
		return format(true, DateUtil.toCalendar(startDate),
				DateUtil.toCalendar(endDate));
	}

	private static String format(boolean forceAmPm, Calendar startCal,
			Calendar endCal) {
		StringBuffer sb = new StringBuffer();

		formatCal(startCal, true, sb);
		sb.append(" - ");
		formatCal(endCal, true, sb);

		return sb.toString();
	}

	private static void formatCal(Calendar cal, boolean showAmPm,
			StringBuffer sb) {
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
				sb.append(" am");
			} else {
				sb.append(" pm");
			}
		}
	}

	public boolean isMatching(Date baseDate, Date startTime, Date endTime) {

		if (this.getDeliveryDate().equals(DateUtil.truncate(baseDate))
				&& this.getDlvStartTime().equals(new TimeOfDay(startTime))
				&& this.getDlvEndTime().equals(new TimeOfDay(endTime))) {

			return true;
		}
		return false;

	}

	public boolean isWithinRange(Date baseDate, Date t) {
		final TimeOfDay __t = new TimeOfDay(t);
		if (this.getDeliveryDate().equals(DateUtil.truncate(baseDate))
				&& this.getDlvStartTime().compareTo(__t) <= 0
				&& this.getDlvEndTime().compareTo(__t) > 0) {
			return true;
		}
		return false;

	}

	// Early AM timeslot
	public boolean isEarlyAM() {
		if (this.getStartTime() != null
				&& this.getStartTime().before(
						new TimeOfDay(getEarlyAMTime()).getNormalDate())) {
			return true;
		}
		return false;
	}

	private Date getEarlyAMTime() {
		Calendar _earlyWindowCalInstance = Calendar.getInstance();
		_earlyWindowCalInstance.set(Calendar.HOUR_OF_DAY,
				FDStoreProperties.getEarlyAMWindowHour());
		_earlyWindowCalInstance.set(Calendar.MINUTE,
				FDStoreProperties.getEarlyAMWindowMinute());
		_earlyWindowCalInstance.set(Calendar.SECOND, 0);
		_earlyWindowCalInstance.set(Calendar.MILLISECOND, 0);
		return _earlyWindowCalInstance.getTime();
	}

	public String getTimeslotShift() {
		Calendar startTimeCal = DateUtil.toCalendar(this.getStartDateTime());
		int startHour = startTimeCal.get(Calendar.HOUR_OF_DAY);
		if (startHour > 12)
			return EnumDayShift.DAY_SHIFT_PM.getName();
		else
			return EnumDayShift.DAY_SHIFT_AM.getName();
	}

	@Override
	public int compareTo(FDTimeslot t1) {
		return this.getStartTime().compareTo(t1.getStartTime());		
	}

	public static String getDisplayString(boolean forceAmPm, Date startTime,
			Date endTime) {
		return format(forceAmPm, DateUtil.toCalendar(startTime),
				DateUtil.toCalendar(endTime));
	}

	public String getPremiumAmountFmt() {
		return "ADD&nbsp;$" + premiumAmountFmt.format(premiumAmount);
	}

	public String getPromoDeliveryFeeFmt() {
		return "ADD&nbsp;$" + premiumAmountFmt.format(promoDeliveryFee);
	}
	
	public boolean isMinOrderSlot() {
		return (minOrderAmt > 0) ? true : false;
	}

	public Date getStartTime() {
		return getDlvStartTime().getNormalDate();
	}

	public Date getStartDateTime() {
		return getDlvStartTime().getAsDate(getDeliveryDate());
	}

	public Date getEndTime() {
		return getDlvEndTime().getNormalDate();
	}

	public Date getEndDateTime() {
		return getDlvEndTime().getAsDate(getDeliveryDate());
	}

	public Date getCutoffDateTime() {
		return cutoffDateTime;
	}

	public TimeOfDay getPremiumCutoffTime() {
		return premiumCutoffTime;
	}

	public void setPremiumCutoffTime(TimeOfDay premiumCutoffTime) {
		this.premiumCutoffTime = premiumCutoffTime;
	}

	public Date getPremiumCutoffDateTime() {
		return premiumCutoffDateTime;
	}

	public Date getCutoffNormalDateTime() {
		return getCutoffTimeAsNormalDate();
	}

	private Date getCutoffTimeAsNormalDate() {
		return getCutoffTime().getNormalDate();
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public TimeOfDay getDlvStartTime() {
		return dlvStartTime;
	}

	public void setDlvStartTime(TimeOfDay dlvStartTime) {
		this.dlvStartTime = dlvStartTime;
	}

	public TimeOfDay getDlvEndTime() {
		return dlvEndTime;
	}

	public void setDlvEndTime(TimeOfDay dlvEndTime) {
		this.dlvEndTime = dlvEndTime;
	}

	public TimeOfDay getCutoffTime() {
		return cutoffTime;
	}

	public void setCutoffTime(TimeOfDay cutoffTime) {
		this.cutoffTime = cutoffTime;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
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

	public boolean isAlcoholRestricted() {
		return isAlcoholRestricted;
	}

	public void setAlcoholRestricted(boolean isAlcoholRestricted) {
		this.isAlcoholRestricted = isAlcoholRestricted;
	}

	public boolean isHolidayRestricted() {
		return isHolidayRestricted;
	}

	public void setHolidayRestricted(boolean isHolidayRestricted) {
		this.isHolidayRestricted = isHolidayRestricted;
	}

	public String getStoreFrontAvailable() {
		return storeFrontAvailable;
	}

	public void setStoreFrontAvailable(String storeFrontAvailable) {
		this.storeFrontAvailable = storeFrontAvailable;
	}

	public boolean isEcoFriendly() {
		return isEcoFriendly;
	}

	public void setEcoFriendly(boolean isEcoFriendly) {
		this.isEcoFriendly = isEcoFriendly;
	}

	public boolean isSoldOut() {
		return isSoldOut;
	}

	public void setSoldOut(boolean isSoldOut) {
		this.isSoldOut = isSoldOut;
	}

	public boolean isDepot() {
		return isDepot;
	}

	public void setDepot(boolean isDepot) {
		this.isDepot = isDepot;
	}

	public boolean isPremiumSlot() {
		return isPremiumSlot;
	}

	public void setPremiumSlot(boolean isPremiumSlot) {
		this.isPremiumSlot = isPremiumSlot;
	}

	public String getMinOrderMsg() {
		return minOrderMsg;
	}

	public void setMinOrderMsg(String minOrderMsg) {
		this.minOrderMsg = minOrderMsg;
	}

	public double getMinOrderAmt() {
		return minOrderAmt;
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
	

	public double getPremiumAmount() {
		return premiumAmount;
	}
	
	public void setPremiumAmount(double premiumAmount) {
		this.premiumAmount = premiumAmount;
	}
	

	public double getSteeringDiscount() {
		return steeringDiscount;
	}

	public void setSteeringDiscount(double steeringDiscount) {
		this.steeringDiscount = steeringDiscount;
	}

	public double getTotalAvailable() {
		return totalAvailable;
	}

	public void setTotalAvailable(double totalAvailable) {
		this.totalAvailable = totalAvailable;
	}

	public double getBaseAvailable() {
		return baseAvailable;
	}

	public void setBaseAvailable(double baseAvailable) {
		this.baseAvailable = baseAvailable;
	}

	public double getChefsTableAvailble() {
		return chefsTableAvailble;
	}

	public void setChefsTableAvailble(double chefsTableAvailble) {
		this.chefsTableAvailble = chefsTableAvailble;
	}

	public int getAdditionalDistance() {
		return additionalDistance;
	}

	public void setAdditionalDistance(int additionalDistance) {
		this.additionalDistance = additionalDistance;
	}

	public Date getHandoffDateTime() {
		return handoffDateTime;
	}

	public void setHandoffDateTime(Date handoffDateTime) {
		this.handoffDateTime = handoffDateTime;
	}

	public boolean hasSteeringRadius() {
		return hasSteeringRadius;
	}

	public void setHasSteeringRadius(boolean hasSteeringRadius) {
		this.hasSteeringRadius = hasSteeringRadius;
	}

	public boolean isGeoRestricted() {
		return isGeoRestricted;
	}

	public void setGeoRestricted(boolean isGeoRestricted) {
		this.isGeoRestricted = isGeoRestricted;
	}

	public boolean isTimeslotRestricted() {
		return isTimeslotRestricted;
	}

	public void setTimeslotRestricted(boolean isTimeslotRestricted) {
		this.isTimeslotRestricted = isTimeslotRestricted;
	}

	public boolean isTimeslotRemoved() {
		return isTimeslotRemoved;
	}

	public void setTimeslotRemoved(boolean isTimeslotRemoved) {
		this.isTimeslotRemoved = isTimeslotRemoved;
	}

	public boolean isUnavailable() {
		return isUnavailable;
	}

	public void setUnavailable(boolean isUnavailable) {
		this.isUnavailable = isUnavailable;
	}

	public String getTravelZone() {
		return travelZone;
	}

	public void setTravelZone(String travelZone) {
		this.travelZone = travelZone;
	}

	public double getMinDurationForModStart() {
		return minDurationForModStart;
	}

	public void setMinDurationForModStart(double minDurationForModStart) {
		this.minDurationForModStart = minDurationForModStart;
	}

	public double getMinDurationForModification() {
		return minDurationForModification;
	}

	public void setMinDurationForModification(double minDurationForModification) {
		this.minDurationForModification = minDurationForModification;
	}

	public boolean isFdxSlot() {
		return isFdxSlot;
	}

	public void setFdxSlot(boolean isFdxSlot) {
		this.isFdxSlot = isFdxSlot;
	}

	public EnumRegionServiceType getRegionSvcType() {
		return regionSvcType;
	}
	public void setRegionSvcType(EnumRegionServiceType regionSvcType) {
		this.regionSvcType = regionSvcType;
	}

	public void setCutoffDateTime(Date cutoffDateTime) {
		this.cutoffDateTime = cutoffDateTime;
	}

	public Date getSoFirstDeliveryDate() {
		return soFirstDeliveryDate;
	}

	public String getFormattedSoFirstDeliveryDate() {
		String formattedSoFirstDeliveryDate = null;
		if(null !=soFirstDeliveryDate){
			formattedSoFirstDeliveryDate = DateUtil.formatDayAndMonth(soFirstDeliveryDate);
		}
		return formattedSoFirstDeliveryDate;
	}
	
	public String getFormattedSoFirstDeliveryDateFull() {
		String formattedSoFirstDeliveryDateFull = null;
		if(null !=soFirstDeliveryDate){
			formattedSoFirstDeliveryDateFull = DateUtil.formatDate(soFirstDeliveryDate);
		}
		return formattedSoFirstDeliveryDateFull;
	} 
	
	public void setSoFirstDeliveryDate(Date soFirstDeliveryDate) {
		this.soFirstDeliveryDate = soFirstDeliveryDate;
	}

	public double getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public double getPromoDeliveryFee() {
		return promoDeliveryFee;
	}

	public void setPromoDeliveryFee(double promoDeliveryFee) {
		this.promoDeliveryFee = promoDeliveryFee;
	}

	public EnumDeliveryFeeTier getDlvfeeTier() {
		return dlvfeeTier;
	}

	public void setDlvfeeTier(EnumDeliveryFeeTier dlvfeeTier) {
		this.dlvfeeTier = dlvfeeTier;
	}

	public Date getOriginalCutoffDateTime() {
		return originalCutoffDateTime;
	}

	public void setOriginalCutoffDateTime(Date originalCutoffDateTime) {
		this.originalCutoffDateTime = originalCutoffDateTime;
	}

	public FDDeliveryZoneInfo getZoneInfo() {
		return zoneInfo;
	}

	public void setZoneInfo(FDDeliveryZoneInfo zoneInfo) {
		this.zoneInfo = zoneInfo;
	}

	public int getCapacityUtilizationPercentage() {
		return capacityUtilizationPercentage;
	}

	public void setCapacityUtilizationPercentage(int capacityUtilizationPercentage) {
		this.capacityUtilizationPercentage = capacityUtilizationPercentage;
	}

	public boolean isMidWeekDlvPassApplicable() {
		return isMidWeekDlvPassApplicable;
	}

	public void setMidWeekDlvPassApplicable(boolean isMidWeekDlvPassApplicable) {
		this.isMidWeekDlvPassApplicable = isMidWeekDlvPassApplicable;
	}
	
	
}
