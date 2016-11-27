package com.freshdirect.routing.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RoutingStringUtil {
	
	private static NumberFormat formatter = new DecimalFormat("00");
	
	public static String calcHMS(int timeInSeconds) {
	      int hours, minutes, seconds;
	      hours = timeInSeconds / 3600;
	      timeInSeconds = timeInSeconds - (hours * 3600);
	      minutes = timeInSeconds / 60;
	      timeInSeconds = timeInSeconds - (minutes * 60);
	      seconds = timeInSeconds;
	      return formatTwoDigitNumber(hours) + ":" + formatTwoDigitNumber(minutes) + ":" + formatTwoDigitNumber(seconds) ;
	}
	
	public static String calcHM(int timeInSeconds) {
	      int hours, minutes, seconds;
	      hours = timeInSeconds / 3600;
	      timeInSeconds = timeInSeconds - (hours * 3600);
	      minutes = timeInSeconds / 60;
	      timeInSeconds = timeInSeconds - (minutes * 60);
	      seconds = timeInSeconds;
	      return formatTwoDigitNumber(hours) + ":" + formatTwoDigitNumber(minutes);
	}
	
	public static String calcHMSFlat(int timeInSeconds) {
	      int hours, minutes, seconds;
	      hours = timeInSeconds / 3600;
	      timeInSeconds = timeInSeconds - (hours * 3600);
	      minutes = timeInSeconds / 60;
	      timeInSeconds = timeInSeconds - (minutes * 60);
	      seconds = timeInSeconds;
	      return formatTwoDigitNumber(hours) + formatTwoDigitNumber(minutes) + "00";
	}
	
	public static String formatTwoDigitNumber(int input) {
		return formatter.format(input);
	}
}
