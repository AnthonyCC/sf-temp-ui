package com.freshdirect.fdstore.util;

import java.util.Calendar;

import com.freshdirect.fdstore.FDStoreProperties;

public class ClickToCallUtil {

	public static boolean isBusinessHour() {

		Calendar calStart=Calendar.getInstance();
		String hourRange=(String)FDStoreProperties.getCustServHoursRange(calStart.get(Calendar.DAY_OF_WEEK));

		String startHour=hourRange.substring(0,hourRange.indexOf(':'));
		String startMinute=hourRange.substring(hourRange.indexOf(':')+1,hourRange.indexOf('-'));
		String endHour=hourRange.substring(hourRange.indexOf('-')+1,hourRange.lastIndexOf(':'));
		String endMinute=hourRange.substring(hourRange.lastIndexOf(':')+1);
		calStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHour));
		calStart.set(Calendar.MINUTE, Integer.parseInt(startMinute));
		calStart.set(Calendar.SECOND, 0);

		Calendar calEnd=Calendar.getInstance();
		calEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endHour));
		calEnd.set(Calendar.MINUTE, Integer.parseInt(endMinute));
		calEnd.set(Calendar.SECOND, 0);

		Calendar calNow=Calendar.getInstance();

		if ((calNow.equals(calStart) || calNow.after(calStart) ) &&
				(calNow.equals(calEnd) || calNow.before(calEnd)) ) {
			return true;
		}
		return false;
	}

	private static void print(Calendar now) {

        System.out.println(now.get(Calendar.MONTH)+1+"/"+ now.get(Calendar.DAY_OF_MONTH)+"/"+now.get(Calendar.YEAR)+" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE));

	}

	public static void main (String[] a) {

        System.out.println(ClickToCallUtil.isBusinessHour());

	}

}
