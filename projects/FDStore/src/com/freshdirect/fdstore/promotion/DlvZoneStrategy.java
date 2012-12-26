package com.freshdirect.fdstore.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.delivery.EnumDeliveryOption;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.TimeOfDay;

public class DlvZoneStrategy implements PromotionStrategyI {

	private String dlvDays;
	private List<String> dlvZones;
	private Map<Integer,List<PromotionDlvTimeSlot>> dlvTimeSlots;
	private List<PromotionDlvDate> dlvDates;
	private String dlvZoneId;
	private EnumDeliveryOption dlvDayType;
	
	
	public String getDlvDays() {
		return dlvDays;
	}

	public void setDlvDays(String dlvDays) {
		this.dlvDays = dlvDays;
	}

	public List<String> getDlvZones() {
		return dlvZones;
	}

	public void setDlvZones(List<String> dlvZones) {
		this.dlvZones = dlvZones;
	}

	public Map<Integer,List<PromotionDlvTimeSlot>> getDlvTimeSlots() {
		return dlvTimeSlots;
	}

	public void setDlvTimeSlots(Map<Integer,List<PromotionDlvTimeSlot>> dlvTimeSlots) {
		this.dlvTimeSlots = dlvTimeSlots;
	}

	public List<PromotionDlvDate> getDlvDates() {
		return dlvDates;
	}

	public void setDlvDates(List<PromotionDlvDate> dlvDates) {
		this.dlvDates = dlvDates;
	}

	public String getDlvZoneId() {
		return dlvZoneId;
	}

	public void setDlvZoneId(String dlvZoneId) {
		this.dlvZoneId = dlvZoneId;
	}

	@Override
	public int evaluate(String promotionCode, PromotionContextI context) {
		String zoneCode =context.getDeliveryZone();

		boolean isPremiumSlot = null !=context.getDeliveryReservation()?context.getDeliveryReservation().isPremium():false;
		if(checkDlvDayTypeEligibility(isPremiumSlot)){
			if(null != zoneCode && !"".equals(zoneCode.trim())){
				if(null == dlvDates || dlvDates.size()==0 || checkDlvDates(context.getDeliveryReservation())){
					if(null != dlvZones && dlvZones.size() != 0 && ((dlvZones.contains(zoneCode) || dlvZones.contains("ALL")))){
						FDReservation dlvReservation = context.getDeliveryReservation();
						if(null == dlvDays || dlvDays.isEmpty() || null ==dlvReservation){
							context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_TIMESLOT_SELECTED.getErrorCode());
							return DENY;
						}
						int day = dlvReservation.getTimeslot().getDayOfWeek();		
						boolean e = dlvDays.contains(String.valueOf(day));
						if(e && null !=dlvTimeSlots && !dlvTimeSlots.isEmpty()){
							List<PromotionDlvTimeSlot> dlvTimeSlotList = dlvTimeSlots.get(day);
							if(null != dlvTimeSlotList) {								
								if((!(dlvReservation.getTimeslot().getDlvTimeslot().isPremiumSlot() && promotionCode.startsWith("WS_"))
										|| FDStoreProperties.allowDiscountsOnPremiumSlots())
										&& checkDlvTimeSlots(dlvReservation.getTimeslot().getDlvTimeslot(), dlvTimeSlotList, null, context.getUser(), true))
									return ALLOW;
								else{
									context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_TIMESLOT_SELECTED.getErrorCode());
									return DENY;
								}

							}
						}
						if(!e){
							context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_TIMESLOT_SELECTED.getErrorCode());
						}
						return e ? ALLOW : DENY;
					}else {
						if(null != dlvZones && dlvZones.size() != 0){
							return DENY;
						}
						return ALLOW;
					}
				}
			}
		}
		context.getUser().addPromoErrorCode(promotionCode, PromotionErrorType.NO_ELIGIBLE_TIMESLOT_SELECTED.getErrorCode());
		return DENY;
	}
	
	private boolean checkTimeSlotRadius(TimeOfDay dlvStartTimeOfDay, TimeOfDay dlvEndTimeOfDay, List<FDTimeslot> tsLst){
		
		Map<Integer, List<FDTimeslot>> tsRadiusMap = new HashMap<Integer, List<FDTimeslot>>();		
		boolean foundSlot = false;		
		if(tsLst != null) {			
			Iterator<FDTimeslot> _tsItr = tsLst.iterator();
			int _rootTSRadius = -1;
			while(_tsItr.hasNext()) {
				FDTimeslot _ts = _tsItr.next();
				if(_ts.getDlvTimeslot().getRoutingSlot() != null && _ts.getDlvTimeslot().getRoutingSlot().getDeliveryCost() != null) {					
					if(_rootTSRadius != -1 && _ts.getDlvTimeslot().getRoutingSlot().getDeliveryCost().getAdditionalDistance() < _rootTSRadius) {
						_rootTSRadius = _ts.getDlvTimeslot().getRoutingSlot().getDeliveryCost().getAdditionalDistance();
					} else if (_rootTSRadius == -1) {
						_rootTSRadius = _ts.getDlvTimeslot().getRoutingSlot().getDeliveryCost().getAdditionalDistance();
					}
					if(!tsRadiusMap.containsKey(_ts.getDlvTimeslot().getRoutingSlot().getDeliveryCost().getAdditionalDistance())){
						tsRadiusMap.put(_ts.getDlvTimeslot().getRoutingSlot().getDeliveryCost().getAdditionalDistance(), new ArrayList<FDTimeslot>());
					}
					tsRadiusMap.get(_ts.getDlvTimeslot().getRoutingSlot().getDeliveryCost().getAdditionalDistance()).add(_ts);
				}
			}
			
			List<FDTimeslot> tsRadiusLst = tsRadiusMap.get(_rootTSRadius);
			if(tsRadiusLst != null){
				for(FDTimeslot _slot : tsRadiusLst){
					if(dlvStartTimeOfDay.equals(_slot.getDlvTimeslot().getStartTime()) && dlvEndTimeOfDay.equals(_slot.getDlvTimeslot().getEndTime())){
						foundSlot = true;
						break;
					}
				}
			}
		}
		return foundSlot;		
	}
	
	private boolean checkSteeringDiscountFlag(FDUserI user, DlvTimeslotModel dlvTimeslotModel) {
		if(user.getShoppingCart() != null && user.getShoppingCart().getDeliveryReservation() != null){
			if(dlvTimeslotModel.getId().equals(user.getShoppingCart().getDeliveryReservation().getTimeslotId())) {
				return user.getShoppingCart().getDeliveryReservation().hasSteeringDiscount();
			}
		}
		return true;
	}
	
	private boolean checkDlvTimeSlots(DlvTimeslotModel dlvTimeslotModel, List<PromotionDlvTimeSlot> dlvTimeSlotList, Map<Double, List<FDTimeslot>> tsWindowMap, FDUserI user, boolean radiusEvaluated) {
		boolean isOK = false;
		if(null != dlvTimeslotModel) {
			TimeOfDay dlvStartTimeOfDay = dlvTimeslotModel.getStartTime();
			TimeOfDay dlvEndTimeOfDay = dlvTimeslotModel.getEndTime();
			for (Iterator<PromotionDlvTimeSlot> iterator = dlvTimeSlotList.iterator(); iterator.hasNext();) {
				PromotionDlvTimeSlot promoDlvTimeSlot = iterator.next();
				String[] windowType = promoDlvTimeSlot.getWindowTypes();
				double windowDuration = TimeOfDay.getDurationAsMinutes(dlvStartTimeOfDay, dlvEndTimeOfDay);
				
				if(windowType != null && windowType.length > 0 
						&& ((radiusEvaluated && checkSteeringDiscountFlag(user, dlvTimeslotModel))
								||(dlvTimeslotModel.hasSteeringRadius() 
										&& tsWindowMap != null && checkTimeSlotRadius(dlvStartTimeOfDay, dlvEndTimeOfDay, tsWindowMap.get(windowDuration))))) {					
						
						for(int i = 0;i < windowType.length; i++) {
							if(!"ALL".equalsIgnoreCase(windowType[i].trim())){
								double d = Double.valueOf(windowType[i].trim()).doubleValue();
								if(windowDuration == d){
									isOK = true;
									if(!user.getSteeringSlotIds().contains(dlvTimeslotModel.getId())){
										user.getSteeringSlotIds().add(dlvTimeslotModel.getId());
									}									
									break;
								}
							} else {
								isOK = true;
								if(!user.getSteeringSlotIds().contains(dlvTimeslotModel.getId())){
									user.getSteeringSlotIds().add(dlvTimeslotModel.getId());
								}
								break;
							}
						}
				} else {
					if(promoDlvTimeSlot.getDlvTimeStart() != null && promoDlvTimeSlot.getDlvTimeEnd() != null){
						TimeOfDay promoDlvSlotStartTime = new TimeOfDay(promoDlvTimeSlot.getDlvTimeStart());									
						TimeOfDay promoDlvSlotEndTime = new TimeOfDay(promoDlvTimeSlot.getDlvTimeEnd());
						if((dlvStartTimeOfDay.equals(promoDlvSlotStartTime) || dlvStartTimeOfDay.after(promoDlvSlotStartTime)) && 
								(dlvEndTimeOfDay.before(promoDlvSlotEndTime) || dlvEndTimeOfDay.equals(promoDlvSlotEndTime))
								&& checkDlvDayTypeEligibility(dlvTimeslotModel.isPremiumSlot())){
							isOK = true;
							break;
						}
					}
				}
			}
		}
		return isOK;
	}

	public boolean checkDlvDates(FDReservation dlvReservation) {
		boolean isOK = false;
		if(null != dlvReservation){
			
			Date dlvSlotBaseDate = DateUtil.truncate(dlvReservation.getTimeslot().getDlvTimeslot().getBaseDate());
			//Calendar cal =Calendar.getInstance();
			//cal.setTime(dlvSlotBaseDate);
			isOK = checkBaseDateRange(dlvSlotBaseDate);
		}
		return isOK;
	}
	
	public boolean checkDlvDates(FDTimeslot ts) {
		boolean isOK = false;
		if(null != ts){
			
			Date dlvSlotBaseDate = DateUtil.truncate(ts.getDlvTimeslot().getBaseDate());
			//Calendar cal =Calendar.getInstance();
			//cal.setTime(dlvSlotBaseDate);
			isOK = checkBaseDateRange(dlvSlotBaseDate);
		}
		return isOK;
	}

	private boolean checkBaseDateRange(Date dlvSlotBaseDate) {
		for (Iterator<PromotionDlvDate> iterator = dlvDates.iterator(); iterator.hasNext();) {
			PromotionDlvDate dlvDate = iterator.next();
			Date dlvStartDate = DateUtil.truncate(dlvDate.getDlvDateStart());
			Date dlvEndDate = DateUtil.truncate(dlvDate.getDlvDateEnd());
			
			if(((dlvSlotBaseDate.equals(dlvStartDate) || dlvSlotBaseDate.after(dlvStartDate)) 
					&& (dlvSlotBaseDate.equals(dlvEndDate) || dlvSlotBaseDate.before(dlvEndDate))) 
					|| DateUtil.isSameDay(dlvSlotBaseDate, dlvStartDate) 
					|| DateUtil.isSameDay(dlvSlotBaseDate, dlvEndDate)){
				return true;
			}				
		}
		return false;
	}

	public boolean isZonePresent(String zoneId) {
		if(dlvZones == null || dlvZones.isEmpty()) return false;
			if(zoneId != null) return (dlvZones.contains(zoneId) || dlvZones.contains("ALL"));
		return false;
	}
	
	public boolean isTimeSlotEligible(FDTimeslot ts, Map<Double, List<FDTimeslot>> tsWindowMap, FDUserI user) {
		if(null == dlvDays || dlvDays.isEmpty())return false;
		int day = ts.getDayOfWeek();
		boolean e = dlvDays.contains(String.valueOf(day));
		if(e && null != dlvDates && !dlvDates.isEmpty()){
			e = checkBaseDateRange(ts.getDlvTimeslot().getBaseDate());
		}
		if(e && null !=dlvTimeSlots && !dlvTimeSlots.isEmpty()){
			List<PromotionDlvTimeSlot> dlvTimeSlotList = dlvTimeSlots.get(day);
			if(null!= dlvTimeSlotList){
				return checkDlvTimeSlots(ts.getDlvTimeslot(), dlvTimeSlotList, tsWindowMap, user, false);
			}
		}
		return e;
	}
	
	@Override
	public int getPrecedence() {
		
		return 0;
	}

	public EnumDeliveryOption getDlvDayType() {
		return dlvDayType;
	}

	public void setDlvDayType(EnumDeliveryOption dlvDayType) {
		this.dlvDayType = dlvDayType;
	}
	
	public boolean checkDlvDayTypeEligibility(boolean isPremiumSlot){
		boolean isOK = false;
		if(null == dlvDayType || EnumDeliveryOption.ALL.equals(dlvDayType)){
			isOK = true;
		}else{
			if(isPremiumSlot){
				isOK = EnumDeliveryOption.SAMEDAY.equals(dlvDayType) ;
			}else{
				isOK = EnumDeliveryOption.REGULAR.equals(dlvDayType); 
			}
		}
		return isOK;		
	}
	
	private class TimeSlotComparator implements Comparator<FDTimeslot> {

		public int compare(FDTimeslot ts1, FDTimeslot ts2) {
			if(ts1.getDlvTimeslot().getRoutingSlot() != null &&  ts2.getDlvTimeslot().getRoutingSlot() != null) {
				if(ts1.getDlvTimeslot().getRoutingSlot().getDeliveryCost() != null && ts2.getDlvTimeslot().getRoutingSlot().getDeliveryCost() != null){
					return ts1.getDlvTimeslot().getRoutingSlot().getDeliveryCost().getAdditionalDistance() - ts2.getDlvTimeslot().getRoutingSlot().getDeliveryCost().getAdditionalDistance();
				}
			}
			return 0;
		}
	}
	
	/*public int evaluateByZoneCode(String zoneCode){
		if(null != zoneCode && !"".equals(zoneCode.trim())){
			if(null != dlvZones && dlvZones.size() != 0 && (dlvZones.contains(zoneCode) || dlvZones.contains("ALL"))){
				return ALLOW;
			}
		}
		return DENY;
	}*/
	
	/*public int evaluateByTimeslot(FDTimeslot ts){
		if(null != ts){
			
		}
		return DENY;
	}*/

}
 