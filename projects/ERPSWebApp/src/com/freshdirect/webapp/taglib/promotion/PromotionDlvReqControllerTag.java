package com.freshdirect.webapp.taglib.promotion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.enums.WeekDay;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvDateModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvTimeSlotModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoZipRestriction;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class PromotionDlvReqControllerTag extends AbstractControllerTag {

	private FDPromotionNewModel promotion;
	
	public void setPromotion(FDPromotionNewModel promotion) {
		this.promotion = promotion;
	}

	
	public FDPromotionNewModel getPromotion() {
		return promotion;
	}
	
	@Override
	protected boolean performAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {

		try {
			String residential = NVL.apply(request.getParameter("residential"),"").trim();
			String commerical = NVL.apply(request.getParameter("commerical"),"").trim();
			String pickup = NVL.apply(request.getParameter("pickup"),"").trim();
			String exSameDayDlv = NVL.apply(request.getParameter("exSameDayDlv"),"").trim();
			String geoRestrictionType= NVL.apply(request.getParameter("edit_dlvreq_geoRest"),"").trim();
			String dlvDatesLength = NVL.apply(request.getParameter("dlvDatesIndexValue"),"");
			
			if(!"".equalsIgnoreCase(dlvDatesLength)){
				List<FDPromoDlvDateModel> dlvDates = new ArrayList<FDPromoDlvDateModel>();
			
				for(int i=0;i<Integer.parseInt(dlvDatesLength);i++){
					String startDate = request.getParameter("dlvDatesStartDate_in["+i+"]");
					String endDate = request.getParameter("dlvDatesEndDate_in["+i+"]");
//					SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
					if(null != startDate && null != endDate){
						FDPromoDlvDateModel dateModel = new FDPromoDlvDateModel();
						try {
							dateModel.setDlvDateStart(DateUtil.parseMDY(startDate));
							dateModel.setDlvDateEnd(DateUtil.parseMDY(endDate));
						} catch (ParseException e) {
							continue;
						}
						
						if(dateModel.getDlvDateStart()!= null & dateModel.getDlvDateEnd() !=null && dateModel.getDlvDateStart().after(dateModel.getDlvDateEnd())){
							actionResult.addError(true, "startDateGreater", " Delivery Start Date can't be later than End Date.");
						}
						dateModel.setPromoId(promotion.getId());
						dlvDates.add(dateModel);
					}
				}
				promotion.setDlvDates(dlvDates);
			}
			promotion.setGeoRestrictionType(geoRestrictionType);
			if("ZIP".equalsIgnoreCase(geoRestrictionType)){				
				String zipType = NVL.apply(request.getParameter("edit_dlvreq_zipType"),"").trim();
				FDPromoZipRestriction zipRestriction = new FDPromoZipRestriction();
				TreeMap zipRestrictionMap = new TreeMap();	
				zipRestriction.setStartDate(new Date());
				zipRestriction.setType(zipType);
//				zipRestriction.setZipCodes(zipCodes);
				zipRestriction.setZipCodes(NVL.apply(request.getParameter("zip_codes"), "").replaceAll("[\r\n | \"]","").toUpperCase());
				zipRestrictionMap.put(zipRestriction.getStartDate(), zipRestriction);
				promotion.setZipRestrictions(zipRestrictionMap);
				promotion.setDlvZoneStrategies(Collections.EMPTY_LIST);
			}else if("ZONE".equalsIgnoreCase(geoRestrictionType)){
				String zoneType = request.getParameter("edit_dlvreq_zoneType");
				String[] selectedZones = request.getParameterValues("edit_dlvreq_selected");
				
				WeekDay weekNames[] = WeekDay.values();
				StringBuffer daysSelected = new StringBuffer();
				boolean isADayAdded = false;
				List<FDPromoDlvTimeSlotModel> dlvTimeSlots = new ArrayList<FDPromoDlvTimeSlotModel>();
				for (int i = 0; i < weekNames.length; i++) {
					String daySelected = request.getParameter("edit_dlvreq_chk"+weekNames[i].name());
					if(null !=daySelected){
						String dlvTimeSlotsLength = NVL.apply(request.getParameter("dlvDay"+weekNames[i].name()+"IndexValue"),"");
						if(!"".equalsIgnoreCase(dlvTimeSlotsLength)){
							
							for(int j=0;j<Integer.parseInt(dlvTimeSlotsLength);j++){
								FDPromoDlvTimeSlotModel timeSlotModel = new FDPromoDlvTimeSlotModel();
								String startTime = NVL.apply(request.getParameter("dlvDay"+weekNames[i].name()+"StartTime_in["+j+"]"),"");
								String endTime  = NVL.apply(request.getParameter("dlvDay"+weekNames[i].name()+"EndTime_in["+j+"]"),"");
								if(!"".equals(startTime)&&!"".equals(endTime)&& !"null".equalsIgnoreCase(startTime)&&!"null".equalsIgnoreCase(endTime)){
									DateFormat MIN_AMPM_FORMATTER = new SimpleDateFormat("h:mm a");
									try {
										Date startDate = MIN_AMPM_FORMATTER.parse(startTime);
										Date endDate = MIN_AMPM_FORMATTER.parse(endTime);
										if(startDate.after(endDate)){
											actionResult.addError(true,"timeslotError","Start time should be lesser than end time for each timeslot range.");
										}
									} catch (ParseException e) {
										actionResult.addError(true,"timeslotFormatError","One or more of the timeslots are in wrong format. It should be in 'hh:mm am/pm' format");
									}
									timeSlotModel.setDlvTimeStart(startTime);
									timeSlotModel.setDlvTimeEnd(endTime);
									timeSlotModel.setDayId(i+1);
									dlvTimeSlots.add(timeSlotModel);
								}
							}
						}
						
						/*if(isADayAdded){
							daysSelected.append(",");
						}*/
						daysSelected.append(i+1);
						isADayAdded = true;
						
					}				 
				}
				/*if(null !=request.getParameter("edit_dlvreq_chk"+weekNames[0].name())){
					if(isADayAdded){
						daysSelected.append(",");
					}
					daysSelected.append(7);
					isADayAdded = true;
				}*/
				if(!isADayAdded){
					actionResult.addError(true, "daysEmpty", " Atleast a day should be selected for the ZONE type geography restriction.");
				}
				FDPromoDlvZoneStrategyModel dlvZoneModel = new FDPromoDlvZoneStrategyModel();
				dlvZoneModel.setDlvDays(daysSelected.toString());
				if("ALL".equals(zoneType)){
					dlvZoneModel.setDlvZones(new String[]{"ALL"});
				}else{
					dlvZoneModel.setDlvZones(selectedZones);
				}
				dlvZoneModel.setPromotionId(promotion.getId());
				dlvZoneModel.setDlvTimeSlots(dlvTimeSlots);
				List<FDPromoDlvZoneStrategyModel> dlvZones = new ArrayList<FDPromoDlvZoneStrategyModel>();
				dlvZones.add(dlvZoneModel);
				promotion.setDlvZoneStrategies(dlvZones);
				promotion.setZipRestrictions(null);
//			String mon = request.getParameter("edit_dlvreq_chkMon");
//			String tue = request.getParameter("edit_dlvreq_chkTue");
//			String wed = request.getParameter("edit_dlvreq_chkWed");
//			String thu = request.getParameter("edit_dlvreq_chkThu");
//			String fri = request.getParameter("edit_dlvreq_chkFri");
//			String sat = request.getParameter("edit_dlvreq_chkSat");
//			String sun = request.getParameter("edit_dlvreq_chkSun");			
			}				
			List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
			if(null != custStrategies && !custStrategies.isEmpty()){
				FDPromoCustStrategyModel custModel = (FDPromoCustStrategyModel)custStrategies.get(0);
				custModel.setOrderTypeHome(!"".equals(residential));
				custModel.setOrderTypeCorporate(!"".equals(commerical));
				custModel.setOrderTypePickup(!"".equals(pickup));
				custModel.setExcludeSameDayDlv(!"".equals(exSameDayDlv));
				custStrategies.add(custModel);
			}else{
				custStrategies = new ArrayList<FDPromoCustStrategyModel>();
				FDPromoCustStrategyModel custModel = new FDPromoCustStrategyModel();
				custModel.setOrderTypeHome(!"".equals(residential));
				custModel.setOrderTypeCorporate(!"".equals(commerical));
				custModel.setOrderTypePickup(!"".equals(pickup));
				custModel.setExcludeSameDayDlv(!"".equals(exSameDayDlv));
				custModel.setPromotionId(promotion.getId());
				custStrategies.add(custModel);
			}
			promotion.setCustStrategies(custStrategies);
			validateDlvReq(request,actionResult);
			
			if(actionResult.isSuccess())
			FDPromotionNewManager.storePromotionDlvZoneInfo(promotion);
		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (FDDuplicatePromoFieldException e) {
			actionResult.setError(e.getMessage());
			return true;
		} catch (FDPromoTypeNotFoundException e) {
			actionResult.setError(e.getMessage());
			return true;
		} catch (FDPromoCustNotFoundException e) {
			actionResult.setError(e.getMessage());
			return true;
		}
		pageContext.setAttribute("availableDeliveryZones", getAvailableDeliveryZones(actionResult));
		return true;
	}

	private void validateDlvReq(HttpServletRequest request,ActionResult actionResult) {
		String residential = NVL.apply(request.getParameter("residential"),"").trim();
		String commerical = NVL.apply(request.getParameter("commerical"),"").trim();
		String pickup = NVL.apply(request.getParameter("pickup"),"").trim();
		if("".equals(residential)&& "".equals(commerical) && "".equals(pickup)){
			actionResult.addError(true, "addressTypeEmpty", " Atleast one address type is required.");			
		}
		if("ZIP".equalsIgnoreCase(promotion.getGeoRestrictionType())){
//			String zipCodes = NVL.apply(request.getParameter("zip_codes"),"").trim();
//			if("".equals(zipCodes)){
//				actionResult.addError(true, "zipCodesEmpty", " Zip Codes can't empty for the ZIP type geography restriction.");
//			}
			String zipType = NVL.apply(request.getParameter("edit_dlvreq_zipType"),"").trim();
			if("".equals(zipType)){
				actionResult.addError(true, "zipCodeTypeEmpty", " Zip Type from 'ALL EXCEPT' and 'ONLY' can't empty for the ZIP type geography restriction.");
			}
			
			if(!this.promotion.getZipRestrictions().isEmpty()){
				TreeMap map = (TreeMap)this.promotion.getZipRestrictions();
				for(Iterator i = map.keySet().iterator(); i.hasNext(); ){
					FDPromoZipRestriction zipRestriction = (FDPromoZipRestriction) map.get((Date)i.next());
					String zipCodes = NVL.apply(zipRestriction.getZipCodes(), "");
					actionResult.addError(zipCodes.equals(""), "zipCodesEmpty", " Zip Codes can't empty for the ZIP type geography restriction.");
					actionResult.addError((zipCodes.toUpperCase().indexOf("ALL")!=-1 && zipCodes.trim().length()>3), "zip_codes", "Zip Codes can only contain ALL");
					if(!zipCodes.toUpperCase().trim().equals("ALL") && !zipCodes.trim().equals("")){
						StringTokenizer st = new StringTokenizer(zipCodes.trim(), ",");
						int count = st.countTokens(); 
						for (int x = 0; x < count; x++) {
							String token = st.nextToken().trim();
							actionResult.addError(token.length()!=5,"zip_length","One or more zip codes are not of the right length (5)");
							actionResult.addError(!Pattern.compile("[0-9]{5}").matcher(token).find(), "zip_pattern","One or more zip codes are not numbers");
						}
					}
				}
			}
		}else if("ZONE".equalsIgnoreCase(promotion.getGeoRestrictionType())){
			String zoneType = request.getParameter("edit_dlvreq_zoneType");
			String[] selectedZones = request.getParameterValues("edit_dlvreq_selected");
			if("ONLY".equalsIgnoreCase(zoneType) && null == selectedZones){
				actionResult.addError(true, "zonesEmpty", " Selected zones can't be empty. Please add atleast one zone from the available zones.");
			}
		}
		
	}


	private List<DlvZoneModel> getAvailableDeliveryZones(ActionResult actionResult){
		
		try {
			return FDDeliveryManager.getInstance().getActiveZones();
		} catch (FDResourceException e) {
			actionResult.addError(true, "getAvailableDlvZonesError", " Failed to get the available delivery zones.");
			return Collections.EMPTY_LIST;
		}
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
		pageContext.setAttribute("availableDeliveryZones", getAvailableDeliveryZones(actionResult));
		return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
		            new VariableInfo(data.getAttributeString("id"),
		                            "java.util.List",
		                            true,
		                            VariableInfo.NESTED),
						            new VariableInfo(data.getAttributeString("result"),
			                            "com.freshdirect.framework.webapp.ActionResult",
			                            true,
			                            VariableInfo.NESTED)             
		        };

		    }
	}
}
