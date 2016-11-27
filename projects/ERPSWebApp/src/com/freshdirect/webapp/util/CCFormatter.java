package com.freshdirect.webapp.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.framework.util.DurationFormat;

public class CCFormatter { 

	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM.dd.yyyy");
	private static final DateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat DATE_YEAR_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat DATE_MONTH_YEAR_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy");
	private static final DateFormat CASE_DATE_FORMATTER = new SimpleDateFormat("MM.dd.yyyy h:mm a");
	private static final DateFormat DAY_MONTH_DATE_FORMATTER = new SimpleDateFormat("EEE MM/dd");
	private static final DateFormat AVAIL_DATE_FORMATTER = new SimpleDateFormat("EEE, MM/dd");
	private static final DateFormat DLV_DATE_FORMATTER = new SimpleDateFormat("EEEE, MMM d yyyy");
	private static final DateFormat DLV_SHORT_DATE_FORMATTER = new SimpleDateFormat("EE MM/dd/yy");
	private static final DateFormat REQUESTED_DATE_FORMATTER = new SimpleDateFormat("EEEEE, MMM d");
	private static final DateFormat DLV_TIME_FORMATTER = new SimpleDateFormat("h:mm a");
	private static final DateFormat TIME_FORMATTER = new SimpleDateFormat("h:mm");
	private static final DateFormat CC_EXP_DATE_FORMATTER = new SimpleDateFormat("MM.yyyy");
	private static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("MM.dd.yyyy hh:mm a");
	private static final DateFormat DAY_NAME_FORMATTER = new SimpleDateFormat("EEEE");
	private static final DecimalFormat QUANTITY_FORMATTER = new java.text.DecimalFormat("0.##");
	private static final DurationFormat DURATION_FORMATTER = new DurationFormat(false, DurationFormat.MASK_DAY | DurationFormat.MASK_HOUR | DurationFormat.MASK_MINUTE | DurationFormat.MASK_SECOND);
	private static final DecimalFormat LATLONG_FORMATTER = new java.text.DecimalFormat("0.000000");
	private static final DecimalFormat PERCENTAGE_FORMATTER = new java.text.DecimalFormat("#.00%");
	private static final DateFormat DATE_MONTH_FORMATTER = new SimpleDateFormat("EEE, d MMM yyyy");
	//private static final DurationFormat DURATION_FORMATTER = new DurationFormat(false, DurationFormat.MASK_HOUR | DurationFormat.MASK_MINUTE);

	public static synchronized String formatDate(Date date) {
		return DATE_FORMATTER.format(date);
	}

	public static synchronized String formatDateMonth(Date date) {
		return DATE_MONTH_FORMATTER.format(date);
	}
	
	
	public static synchronized String defaultFormatDate(Date date) {
		return DEFAULT_DATE_FORMATTER.format(date);
	}
	
	public static synchronized String formatDateYear(Date date){
		return DATE_YEAR_FORMATTER.format(date);
	}
	
	public static synchronized String formatDateMonthYear(Date date){
		return DATE_MONTH_YEAR_FORMATTER.format(date);
	}

	public static synchronized String formatCaseDate(Date date) {
		return CASE_DATE_FORMATTER.format(date);
	}
	
	public static synchronized String formatAvailabilityDate(Date date) {
		return AVAIL_DATE_FORMATTER.format(date);
	}
	
	public static synchronized String formatReservationDate(Date date) {
		return DAY_MONTH_DATE_FORMATTER.format(date);
	}

	public static synchronized String formatDeliveryDate(Date date) {
		return DLV_DATE_FORMATTER.format(date);
	}
	
	public static synchronized String formatShortDlvDate(Date date) {
		return DLV_SHORT_DATE_FORMATTER.format(date);
	}
	
	public static synchronized String formatTime(Date date){
		return TIME_FORMATTER.format(date);
	}

	public static synchronized String formatRequestedDate(Date date) {
		return REQUESTED_DATE_FORMATTER.format(date);
	}

	public static synchronized String formatDeliveryTime(Date date) {
		return DLV_TIME_FORMATTER.format(date);
	}

	public static synchronized String formatCreditCardExpDate(Date date) {
		return CC_EXP_DATE_FORMATTER.format(date);
	}

	public static synchronized String formatDateTime(Date date) {
		return DATE_TIME_FORMATTER.format(date);
	}
	
	public static synchronized String formatDayName(Date date) {
		return DAY_NAME_FORMATTER.format(date);
	}

	public static synchronized String formatQuantity(double quantity) {
		return QUANTITY_FORMATTER.format(quantity);
	}

	public static synchronized String formatDuration(long milliseconds) {
		return DURATION_FORMATTER.format(milliseconds);
	}
	
	public static synchronized String formatLatLong(double latLong) {
			return LATLONG_FORMATTER.format(latLong);
	}
	
	public static synchronized String formatPercentage(double percent) {
			return PERCENTAGE_FORMATTER.format(percent);
	}

}
