package com.freshdirect.fdstore.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.crm.CrmClick2CallModel;
import com.freshdirect.crm.CrmClick2CallTimeModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.enums.WeekDay;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.DateUtil;

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

		if ( FDStoreProperties.getClickToCall() && (calNow.equals(calStart) || calNow.after(calStart) ) &&
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
	
	public static boolean evaluateClick2CallInfoDisplay(FDUserI user) throws FDResourceException{
		CrmClick2CallModel click2CallModel = FDCustomerManager.getClick2CallInfo();
//		FDUserI user = getUser();
		boolean displayClick2CallInfo = false;
		if(null != user && click2CallModel.isStatus()){			
			Calendar calendar = Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			WeekDay weekDayEnum[] = WeekDay.values();
			CrmClick2CallTimeModel click2CallTime[] = click2CallModel.getDays();
			CrmClick2CallTimeModel click2CallDay = click2CallTime[day-1];
			if(click2CallDay.isShow()){
				String startTime = click2CallDay.getStartTime();
				Integer startHour = Integer.parseInt(startTime);
				String endTime = click2CallDay.getEndTime();
				Integer endHour = Integer.parseInt(endTime);
				Integer currentHour = calendar.get(Calendar.HOUR_OF_DAY);
				if(startHour <= currentHour && currentHour <= endHour){
					String[] dlvZones = click2CallModel.getDeliveryZones();
					if(null !=dlvZones && dlvZones.length > 0){
						AddressModel address = user.getShoppingCart().getDeliveryAddress();
						try {
							DlvZoneInfoModel dlvZoneInfo = FDDeliveryManager.getInstance().getZoneInfo(address, date);
							if(null != dlvZoneInfo && null !=dlvZoneInfo.getZoneCode()){
								List dlvZonesList =Arrays.asList(dlvZones);
								if(dlvZonesList.contains(dlvZoneInfo.getZoneCode())){
									//Eligibility Check
									displayClick2CallInfo = checkEligibleCustomers(
											user, click2CallModel,
											displayClick2CallInfo, address);
//									displayClick2CallInfo = checkNextDayTimeSlots(click2CallModel,displayClick2CallInfo, address);
								}
							}
						} catch (FDInvalidAddressException e) {
							throw new FDResourceException(e);
						}
					}
				}
				
			}
			
		}
		return displayClick2CallInfo;
	}

	private static boolean checkEligibleCustomers(FDUserI user,
			CrmClick2CallModel click2CallModel, boolean displayClick2CallInfo,
			AddressModel address) throws FDResourceException {
		String eligibleCustomers = click2CallModel.getEligibleCustomers();
		if(null != eligibleCustomers){
			String[] eligibleCustomer = eligibleCustomers.split(",");
			if(null != eligibleCustomer && eligibleCustomer.length > 0){
				List elgCustList = Arrays.asList(eligibleCustomer);
				if(elgCustList.contains("everyone")){
					displayClick2CallInfo = checkNextDayTimeSlots(click2CallModel,displayClick2CallInfo, address);
					
				}else if(elgCustList.contains("ct_dp")){
					if(user.isChefsTable() && (user.isDlvPassActive()||user.isDlvPassPending())){
						displayClick2CallInfo = checkNextDayTimeSlots(click2CallModel,displayClick2CallInfo, address);
					}
					
				}else if(elgCustList.contains("ct_ndp")){
					if(user.isChefsTable() && !user.isDlvPassActive()&& !user.isDlvPassPending()){
						displayClick2CallInfo = checkNextDayTimeSlots(click2CallModel,displayClick2CallInfo, address);
					}
					
				}else if(elgCustList.contains("nct_dp")){
					if(!user.isChefsTable() && (user.isDlvPassActive()||user.isDlvPassPending())){
						displayClick2CallInfo = checkNextDayTimeSlots(click2CallModel,displayClick2CallInfo, address);
					}
					
				}else if(elgCustList.contains("nct_ndp")){
					if(!user.isChefsTable() && !user.isDlvPassActive()&& !user.isDlvPassPending()){
						displayClick2CallInfo = checkNextDayTimeSlots(click2CallModel,displayClick2CallInfo, address);
					}
				}
					
			}
			
		}
		return displayClick2CallInfo;
	}

	private static boolean checkNextDayTimeSlots(
			CrmClick2CallModel click2CallModel, boolean displayClick2CallInfo,
			AddressModel address) throws FDResourceException {
		if(click2CallModel.isNextDayTimeSlot()){
			if(address instanceof ContactAddressModel){
				Calendar begCal = Calendar.getInstance();
				begCal.add(Calendar.DATE, 1);
				begCal = DateUtil.truncate(begCal);
				List<FDTimeslot> timeSlots = FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZone(begCal.getTime(), begCal.getTime(), (ContactAddressModel)address);
				if(null == timeSlots || timeSlots.size()==0){
					displayClick2CallInfo = true;
				}
			}
		}else{
			displayClick2CallInfo = true;
		}
		return displayClick2CallInfo;
	}

}
