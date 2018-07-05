package com.freshdirect.webapp.taglib.callcenter;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.delivery.EnumDeliveryOption;
import com.freshdirect.delivery.EnumPromoFDXTierType;
import com.freshdirect.enums.WeekDay;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionSection;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvDateModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvDayModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvTimeSlotModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromotionAttributeParam;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.promotion.management.WSPromotionInfo;
import com.freshdirect.fdstore.promotion.pxp.PromoPublisher;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.CCFormatter;

public class WSPromoControllerTag extends AbstractControllerTag {
	
	private static Category LOGGER = LoggerFactory.getInstance(WSPromoControllerTag.class);
	private static  final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static  final DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
	private final static String JUST_BEFORE_MIDNIGHT = "11:59:59 PM";
	private static  final DateFormat endDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");	
	
	private final static String PROMO_DESCRIPTION = "${0} timeslot discount";
	private final static String PROMO_FREE_DELIVERY_DESCRIPTION = "{0} timeslot discount";
	private final static String OFFER_DESCRIPTION = "This offer is good for a ${0} discount on orders received in Zone {1} " +
													"on {2} during the timeslot window {3} - {4}";
	private final static String OFFER_FREE_DELIVERY_DESCRIPTION = "This offer is good for a {0} discount on orders received in Zone {1} " +
			"on {2} during the timeslot window {3} - {4}";
    private final static String AUDIENCE_DESCRIPTION = "This promotion is only good for customers receiving delivery in Zone {0} " +
    												"on {1} during the timeslot window {2} - {3}";
    private final static String TERMS = "${0} Discount for orders delivered on {1} in timeslot {2} - {3}. " +
    						"Addresses eligible for this promotion are limited. Other restrictions may apply. Expires tonight at 11:59pm. " +
    						"Offers modified after this time will not be eligible. Offer is nontransferable. Void where prohibited.";
    private final static String TERMS_FREE_DELIVERY = "{0} Discount for orders delivered on {1} in timeslot {2} - {3}. " +
			"Addresses eligible for this promotion are limited. Other restrictions may apply. Expires tonight at 11:59pm. " +
			"Offers modified after this time will not be eligible. Offer is nontransferable. Void where prohibited.";

	
	@SuppressWarnings("unchecked")
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = request.getParameter("actionName");
		try {
			HttpSession session = pageContext.getSession();
			CrmAgentModel agent = CrmSession.getCurrentAgent(session);
			if(("publish").equals(actionName)) {
				if(preValidate(request, actionResult)) {
					String effectiveDate = request.getParameter("effectiveDate"); //delivery date
					String startDateStr = request.getParameter("startDate"); //start date of the promotion
					String endDateStr = request.getParameter("endDate"); //expiration date of the promotion
					String zone = request.getParameter("selectedZoneId");
					String startTime = request.getParameter("startTime");
					String endTime = request.getParameter("endTime");
					String discount = request.getParameter("discount");
					String redeemLimit = request.getParameter("redeemlimit");
					String promoCode =  NVL.apply(request.getParameter("promoCode"), "").trim();
					String deliveryDayType =NVL.apply(request.getParameter("deliveryDayType"), "").trim();
					String rollingExpType = request.getParameter("rollingType");
					String fdxTierType = request.getParameter("fdxTierType");
					boolean isRollingFrom1stOrder = false;
					String strRollingExpDays = null;
					Integer rollingExpDays = null;
					if(null != rollingExpType){						
						if("rolling_induction".equalsIgnoreCase(rollingExpType)){
							strRollingExpDays =request.getParameter("rolling_days_induction");								
						}else{
							isRollingFrom1stOrder = true;
							strRollingExpDays = request.getParameter("rolling_days_firstorder");
						}
						try {
							if(null != strRollingExpDays && !"".equals(strRollingExpDays.trim())){
								rollingExpDays = Integer.parseInt(strRollingExpDays);
							}
						} catch (Exception e) {
							LOGGER.warn(e);
						}
					}
					String sapConditionType = NVL.apply(request.getParameter("sapConditionType"), "").trim();
					if("".equalsIgnoreCase(deliveryDayType)){
						deliveryDayType = "R";//REGULAR
					}
					Date startDate = dateFormat.parse(startDateStr);
					String[] cohorts = request.getParameterValues("cohorts");
					String radius = request.getParameter("radius");					
					List<FDPromotionAttributeParam>  attrParamList = getAttributeParamList(request);
					Collections.sort(attrParamList);
					List<String> windowTypeList  = getWindowTypeParamList(request);
					Collections.sort(windowTypeList);
					String[] windowTypes = windowTypeList.toArray(new String[windowTypeList.size()]);
					if("X".equalsIgnoreCase(radius) && windowTypes != null && windowTypes.length == 0){
						//set to all window types
						windowTypes = new String[]{"ALL"};
					}
					String profileOperator = NVL.apply(request.getParameter("custreq_profileAndOr"),"").trim();
					String dlvRestrictionType = NVL.apply(request.getParameter("edit_dlvreq_Rest"),"").trim();
					
					if(promoCode.length() == 0 ){
						//Validate if the given effecttive_date/zone/timeslot or window type combination already exists.
						WSPromotionInfo pInfo = FDPromotionNewManager.getWSPromotionInfo(zone, startTime, endTime, startDate, windowTypes);
						if(pInfo != null){
							StringBuffer buf = new StringBuffer();
							buf.append("Promotion for zone ");
							buf.append(zone);
							if(!"X".equalsIgnoreCase(radius)) {
								buf.append(", timeslot ");
								buf.append(startTime);
								buf.append(" to ");
								buf.append(endTime);
							} else {
								buf.append(", window { ");
								for(int intCount = 0; intCount < windowTypes.length; intCount++ ) {
									buf.append(windowTypes[intCount]);				
									if(intCount < windowTypes.length-1) {
										buf.append(",");
									}
								}
								buf.append(" } min");
							}
							buf.append(" and delivery date ");
							buf.append(effectiveDate);
							buf.append(" already exists.");
							actionResult.addError(true, "actionfailure", buf.toString());
							return true;
						}
						//Create a new WS Promotion.
						FDPromotionNewModel promotion = constructPromotion(request, effectiveDate, startDateStr, endDateStr, zone, startTime, endTime, discount, redeemLimit, cohorts, 
								deliveryDayType, radius, windowTypes, attrParamList, profileOperator, dlvRestrictionType,isRollingFrom1stOrder,rollingExpDays,sapConditionType,fdxTierType);
						postValidate(promotion, actionResult);
						

						if(actionResult.isSuccess()){
							promotion.setStatus(EnumPromotionStatus.APPROVE);
							promotion.setCreatedBy(agent.getUserId());
							promotion.setCreatedDate(new Date());
							PrimaryKey pk = FDPromotionNewManager.createPromotion(promotion);
							if(pk == null)
								throw new FDResourceException("Error Creating Windows Steering Promotion. Please contact AppSupport.");
							FDPromotionNewModel promo = FDPromotionNewManager.getPromotionByPk(pk.getId());
							doPublish(promotion, actionResult);
							if(actionResult.isSuccess()) {
								setSuccessPage("/promotion/promo_ws_create.jsp?publish=success&action=create&promoId="+promo.getPromotionCode());
							}
							else {
								actionResult.addError(true, "actionfailure", "Promotion Successfully Created and Publish Failed.");
							}
						}
					} else {
						//Update an existing WS Promotion.
						FDPromotionNewModel promotion = FDPromotionNewManager.loadPromotion(promoCode);
						if(promotion == null){
							throw new FDResourceException("Unable to locate Windows Steering Promotion. Please contact AppSupport.");
						}
						promotion.setAuditChanges(FDPromotionNewManager.loadPromoAuditChanges(promotion.getId()));
						if("X".equalsIgnoreCase(radius) && windowTypes != null && windowTypes.length == 0){
							//set to all window types
							windowTypes = new String[]{"ALL"};
						}
						//Validate if the given effecttive_date/zone/timeslot or window type combination already exists.
						WSPromotionInfo pInfo = FDPromotionNewManager.getWSPromotionInfo(zone, startTime, endTime, startDate, windowTypes);
						if(pInfo != null && !pInfo.getPromotionCode().equals(promotion.getPromotionCode())) {
							StringBuffer buf = new StringBuffer();
							buf.append("Promotion for zone ");
							buf.append(zone);
							if(!"X".equalsIgnoreCase(radius)) {
								buf.append(", timeslot ");
								buf.append(startTime);
								buf.append(" to ");
								buf.append(endTime);
							} else {
								buf.append(", window { ");								
								for(int intCount = 0; intCount < windowTypes.length; intCount++ ) {
									buf.append(windowTypes[intCount]);				
									if(intCount < windowTypes.length-1) {
										buf.append(",");
									}
								}								
								buf.append(" } min");
							}						
							buf.append(" and delivery date ");
							buf.append(effectiveDate);
							buf.append(" already exists.");
							actionResult.addError(true, "actionfailure", buf.toString());
							return true;
						}
						promotion.setModifiedBy(agent.getUserId());
						promotion.setModifiedDate(new Date());

						updatePromotion(request, promotion, effectiveDate, startDateStr, endDateStr, zone, startTime, endTime, discount, redeemLimit, deliveryDayType, 
								radius, windowTypes, attrParamList, profileOperator, dlvRestrictionType,isRollingFrom1stOrder,rollingExpDays,sapConditionType,fdxTierType);

						postValidate(promotion, actionResult);
						/*APPDEV-2004*/
						List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
						FDPromoCustStrategyModel model = custStrategies.get(0);
						model.setCohorts(cohorts);
						/*APPDEV-2304*/
						promotion.setAttributeList(attrParamList);
						
						if(actionResult.isSuccess()){
							promotion.setStatus(EnumPromotionStatus.APPROVE);
							FDPromotionNewManager.storePromotion(promotion, true);
							FDPromotionNewModel promo = FDPromotionNewManager.loadPromotion(promoCode);
							doPublish(promo, actionResult);
							if(actionResult.isSuccess()) {
								setSuccessPage("/promotion/promo_ws_create.jsp?publish=success&action=edit&promoId="+promotion.getPromotionCode());
							}
							else {
								actionResult.addError(true, "actionfailure", "Promotion Successfully Updated and Publish Failed.");
							}
						}
						
					}
				}
			}else if(("cancel").equals(actionName)) {
				String promoCode =  NVL.apply(request.getParameter("promoCode"), "").trim();
				if(promoCode.length() > 0 ){
					FDPromotionNewModel promotion = FDPromotionNewManager.loadPromotion(promoCode);
					if(promotion == null){
						throw new FDResourceException("Unable to locate Windows Steering Promotion. Please contact AppSupport.");
					}
					promotion.setAuditChanges(FDPromotionNewManager.loadPromoAuditChanges(promotion.getId()));
					promotion.setStatus(EnumPromotionStatus.CANCELLING);
					promotion.setModifiedBy(agent.getUserId());
					promotion.setModifiedDate(new Date());						
					FDPromoChangeModel changeModel = new FDPromoChangeModel();
					List promoChanges = new ArrayList<FDPromoChangeModel>();
					changeModel.setPromotionId(promotion.getId());
					changeModel.setActionDate(new Date());
					changeModel.setActionType(EnumPromoChangeType.CANCEL);
					changeModel.setUserId(agent.getUserId());
					promoChanges.add(changeModel);
					promotion.setAuditChanges(promoChanges);
					
					FDPromotionNewManager.storePromotionStatus(promotion,EnumPromotionStatus.CANCELLING,true);
					FDPromotionNewModel promo = FDPromotionNewManager.loadPromotion(promoCode);
					doPublish(promo, actionResult);
					if(actionResult.isSuccess()) {
						setSuccessPage("/promotion/promo_ws_view.jsp?publish=success");
					}
					else {
						actionResult.addError(true, "actionfailure", "Promotion Successfully Created and Publish Failed.");
					}

				}
			} else if(("setDOWLimit").equals(actionName)) {
				String dayofweekstr = request.getParameter("dayofweek");
				String limitstr = request.getParameter("limit");
				if(limitstr == null || limitstr.length() == 0){
					actionResult.addError(true, "actionfailure", "Max Spending Amount cannot be empty");
				} 
				if(actionResult.isSuccess()) {
					Integer dayofweek = Integer.parseInt(dayofweekstr);
					double limit = Double.parseDouble(limitstr);
					FDPromotionNewManager.setDOWLimit(dayofweek, limit);
					actionResult.addWarning(true, "actionsuccess", "Max Spending Amount updated successfully.");
				}
				
			}
		}  catch(FDDuplicatePromoFieldException e){
			LOGGER.error("System Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure", e.getMessage());
		}	catch(FDPromoTypeNotFoundException e){
			LOGGER.error("System Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure",  e.getMessage());
		}	catch(FDPromoCustNotFoundException e){
			LOGGER.error("System Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure",  e.getMessage());
		}	catch(FDResourceException e){
			LOGGER.error("System Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure",  e.getMessage());
		}  catch(Exception e){
			LOGGER.error("Unknown Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		}								
		
		return true;
	}

	private boolean preValidate(HttpServletRequest request, ActionResult actionResult) throws ParseException {
		boolean success = true;
		String startDateStr = NVL.apply(request.getParameter("startDate"), "").trim();
		if(startDateStr == null || startDateStr.length() == 0){
			actionResult.addError(true, "startDate", "Please select a valid Start date.");
			success = false; 
		}

		String endDateStr = NVL.apply(request.getParameter("endDate"), "").trim();
		if(endDateStr == null || endDateStr.length() == 0){
			actionResult.addError(true, "endDate", "Please select a valid End date.");
			success = false; 
		}
		
		Date startDate  = dateFormat.parse(startDateStr);
		Date endDate  = endDateFormat.parse(endDateStr+" "+JUST_BEFORE_MIDNIGHT);

		LOGGER.debug("startDate"+startDate);
		LOGGER.debug("endDate"+endDate);

		if(endDate.before(startDate)){
			actionResult.addError(
					true,
					"newBlkEndDate",
					"End Date cannot be less than the Start Date");
			success = false;
		}

		String zone = NVL.apply(request.getParameter("selectedZoneId"), "").trim();
		if(zone == null || zone.length() == 0){
			actionResult.addError(true, "zone", "Please select a Zone.");
			success = false; 
		}
		String startTime = NVL.apply(request.getParameter("startTime"), "").trim();
		if(!"X".equalsIgnoreCase(request.getParameter("radius")) && (startTime == null || startTime.length() == 0)){
			actionResult.addError(true, "startTime", "Please select a Start Time.");
			success = false; 
		}
		String endTime = NVL.apply(request.getParameter("endTime"), "").trim();
		if(!"X".equalsIgnoreCase(request.getParameter("radius")) && (endTime == null || endTime.length() == 0)){
			actionResult.addError(true, "endTime", "Please select a End Time.");
			success = false; 
		}
		String discount = NVL.apply(request.getParameter("discount"), "").trim();
		if(discount == null || discount.length() == 0){
			actionResult.addError(true, "discount", "Please select a Discount Amount.");
			success = false; 
		}
		String deliveryDayType = NVL.apply(request.getParameter("deliveryDayType"), "").trim();
		if(deliveryDayType == null || deliveryDayType.length() == 0){
			actionResult.addError(true, "deliveryDayType", "Please choose a Delivery Day Type.");
			success = false; 
		}
		
		String[] cohorts = request.getParameterValues("cohorts"); 
		if(cohorts == null || cohorts.length == 0) {
			actionResult.addError(true, "cohortsEmpty", "Select atleast one Cohort to create this WSPromotion.");
		}
		
		try {
			Date startDateTime = timeFormat.parse(startTime);
			Date endDateTime = timeFormat.parse(endTime);
			if(startDateTime.after(endDateTime)){
				actionResult.addError(true,"timeslotError","Start time should be lesser than end time for each timeslot range.");
			}
		} catch (ParseException e) {
			//Do nothing.
		}
		String dlvRestrictionType = NVL.apply(request.getParameter("edit_dlvreq_Rest"),"").trim();
		if("DELIVERYDAY".equals(dlvRestrictionType)) {
			WeekDay weekNames[] = WeekDay.values();
			boolean isADayAdded = false;
			for (int i = 0; i < weekNames.length; i++) {
				String daySelected = request.getParameter("edit_dlvreq_chk"+weekNames[i].name());
				if(null != daySelected) {
					isADayAdded = true;	
					break;
				}
			}
			if(!isADayAdded){
				actionResult.addError(true, "daysEmpty", " Atleast a day should be selected for the ZONE type geography restriction.");
			}
		} else {
			String effectiveDate = NVL.apply(request.getParameter("effectiveDate"), "").trim();
			if(effectiveDate == null || effectiveDate.length() == 0){
				actionResult.addError(true, "effectiveDate", "Please select a valid Delivery date.");
				success = false; 
			}
			Date dlvDate  = dateFormat.parse(effectiveDate);
			if(success && (dlvDate.before(startDate) || dlvDate.after(endDate))) {
				actionResult.addError(true, "effectiveDate", "Delivery date should fall within Promotion start date and end date.");
				success = false; 
			}
		}

		String redeemlimit = NVL.apply(request.getParameter("redeemlimit"), "").trim();
		if(redeemlimit == null || redeemlimit.length() == 0 || "0".equalsIgnoreCase(redeemlimit)){
			actionResult.addError(true, "redeemlimit", "Please select a Redemtion limit > 0");
			success = false; 
		}
		
		return success; 
	}
	
	public void doPublish(FDPromotionNewModel promotion, ActionResult actionResult) {
			List<FDPromotionNewModel> ppList = new ArrayList<FDPromotionNewModel>();
			
			if (promotion != null) {
				// do some cleanup

				promotion.doCleanup();
				
				// add to list of valid promos
				ppList.add(promotion);
			}
			
			HttpSession session = pageContext.getSession();
			CrmAgentModel agent = CrmSession.getCurrentAgent(session);
			
			PromoPublisher publisher = new PromoPublisher();
			publisher.setPromoList(ppList);
			publisher.setAgent(agent);
	
			final boolean result = publisher.doPublish();
	
			
			// Refresh promo cache
			FDPromotionNewModelFactory.getInstance().forceRefresh();
	
	
			// Post mortem actions
			if (!result) {
				actionResult.addError(true, "promo.publish", "An error occured during publish");
			}
	}
	@SuppressWarnings("unchecked")
	private FDPromotionNewModel constructPromotion(HttpServletRequest request, String effectiveDate, String startDateStr, String endDateStr, String zone, String startTime,
			String endTime, String discount, String redeemLimit, String[] cohorts, String deliveryDayType, String radius, String[] windowTypes, List attrParamList, String profileOperator,
			String dlvRestrictionType, boolean isRollingFrom1stOrder, Integer rollingExpDays,String sapConditionType, String fdxTierType) throws FDResourceException {

		try {
			Date startDate = dateFormat.parse(startDateStr);
			Date expDate = endDateFormat.parse(endDateStr+" "+JUST_BEFORE_MIDNIGHT);
			int redeemCnt = 0;
			if(redeemLimit != null && redeemLimit.length() > 0){
				redeemCnt = Integer.parseInt(redeemLimit);
			}
			FDPromotionNewModel promotion = new FDPromotionNewModel();
			Date today = new Date();
			promotion.setPromotionCode("WS_"+today.getTime());
			
			long E4 = Math.round(Math.random()*1000); //Unique counter
			String section1 = "X".equalsIgnoreCase(radius) ? "RADIUS" : "STATIC";
			if(!"DLV".equalsIgnoreCase(discount)){
				promotion.setName("WS_"+DateUtil.format(DateUtil.parseMDY(effectiveDate))+"_Zone"+zone+"_"+section1+"_"+E4+"_$"+discount);
				promotion.setPromotionType(EnumPromotionType.HEADER.getName());
				promotion.setOfferType(EnumOfferType.WINDOW_STEERING.getName());
				promotion.setCombineOffer(true);
				promotion.setMinSubtotal(String.valueOf(ErpServicesProperties.getMinimumOrderAmount()));
				promotion.setDescription(formatPromoDescription(PROMO_DESCRIPTION, discount));
				promotion.setOfferDesc(formatAudienceDescription(OFFER_DESCRIPTION, discount, zone, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
				promotion.setAudienceDesc(formatOfferDescription(AUDIENCE_DESCRIPTION, zone, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
				promotion.setTerms(formatTerms(TERMS, discount, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
			}else{
				promotion.setName("WS_"+DateUtil.format(DateUtil.parseMDY(effectiveDate))+"_Zone"+zone+"_"+section1+"_"+E4+"_"+"FREE_DELIVERY");
				promotion.setPromotionType(EnumPromotionType.HEADER.getName());
				promotion.setOfferType(EnumOfferType.WINDOW_STEERING.getName());
				promotion.setCombineOffer(true);
				//promotion.setMinSubtotal(String.valueOf(FDUserI.FDX_MINIMUM_ORDER_AMOUNT));
				promotion.setMinSubtotal(String.valueOf(0));
				promotion.setDescription(formatPromoDescription(PROMO_FREE_DELIVERY_DESCRIPTION, "Free Delivery"));
				promotion.setOfferDesc(formatAudienceDescription(OFFER_FREE_DELIVERY_DESCRIPTION, "Free Delivery", zone, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
				promotion.setAudienceDesc(formatOfferDescription(AUDIENCE_DESCRIPTION, zone, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
				promotion.setTerms(formatTerms(TERMS_FREE_DELIVERY, "Free Delivery", CCFormatter.defaultFormatDate(startDate), startTime, endTime));
			}
			
			
			promotion.setMaxUsage("999");
			if(redeemCnt > 0 && "DELIVERYDATE".equals(dlvRestrictionType))
				promotion.setRedeemCount(redeemCnt);
			else
				promotion.setRedeemCount(0);
			List<FDPromoCustStrategyModel> custStrategies = new ArrayList<FDPromoCustStrategyModel>();
			FDPromoCustStrategyModel custModel = new FDPromoCustStrategyModel();
			custModel.setOrderTypeHome(true);
			custModel.setOrderTypeCorporate(true);
			custModel.setOrderTypePickup(true);
			custModel.setOrderTypeFDX(true);
			custModel.setFdxTierType(EnumPromoFDXTierType.getEnum(fdxTierType));
			custModel.setCohorts(cohorts);
			EnumDeliveryOption deliveryOption= EnumDeliveryOption.getEnum(deliveryDayType);
			custModel.setDeliveryDayType(deliveryOption);
			custStrategies.add(custModel);
			promotion.setCustStrategies(custStrategies);
			promotion.setGeoRestrictionType("ZONE");
			if(DateUtil.isSameDay(startDate, today)) {
				//If start date is today then Should default to today and the current time.
				promotion.setStartDate(today);
			} else {
				//If another date is selected, 12:00am can be the default.
				promotion.setStartDate(DateUtil.truncate(startDate));
			}
			promotion.setExpirationDate(expDate);

			promotion.setRadius(radius);
			promotion.setRollingExpDayFrom1stOrder(isRollingFrom1stOrder);
			promotion.setNeedCustomerList(null !=rollingExpDays && rollingExpDays >0 && !isRollingFrom1stOrder);
			promotion.setRollingExpirationDays(rollingExpDays);
			promotion.setSapConditionType(sapConditionType);

			Date dlvDate = null;
			FDPromoDlvTimeSlotModel timeSlotModel = null;
			FDPromoDlvDayModel dlvDay = null;
			
			FDPromoDlvZoneStrategyModel dlvZoneModel = new FDPromoDlvZoneStrategyModel();
			List<FDPromoDlvTimeSlotModel> dlvTimeSlots = new ArrayList<FDPromoDlvTimeSlotModel>();
			List<FDPromoDlvDayModel> dlvDays = new ArrayList<FDPromoDlvDayModel>();

			dlvZoneModel.setDlvDayRedemtions(dlvDays);
			dlvZoneModel.setDlvTimeSlots(dlvTimeSlots);
			dlvZoneModel.setDlvZones(new String[]{zone});
			dlvZoneModel.setPromotionId("");
			
			if("DELIVERYDATE".equals(dlvRestrictionType)) {
				dlvDate = dateFormat.parse(effectiveDate);
				FDPromoDlvDateModel dateModel = new FDPromoDlvDateModel();
				dateModel.setDlvDateStart(dlvDate);
				dateModel.setDlvDateEnd(dlvDate);
				List<FDPromoDlvDateModel> dlvDates = new ArrayList<FDPromoDlvDateModel>();
				dlvDates.add(dateModel);
				promotion.setDlvDates(dlvDates);
			
				timeSlotModel = new FDPromoDlvTimeSlotModel();
				timeSlotModel.setPromoDlvZoneId(zone);
			
				if(!"X".equalsIgnoreCase(radius)){
					timeSlotModel.setDlvTimeStart(startTime);
					timeSlotModel.setDlvTimeEnd(endTime);
				} else {
					timeSlotModel.setWindowTypes(windowTypes);
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(dlvDate);
				int dayId = cal.get(Calendar.DAY_OF_WEEK);
				timeSlotModel.setDayId(dayId);
				dlvZoneModel.setDlvDays(String.valueOf(dayId));
				dlvTimeSlots.add(timeSlotModel);
				
			} else if("DELIVERYDAY".equals(dlvRestrictionType)) {

				WeekDay weekNames[] = WeekDay.values();
				StringBuffer daysSelected = new StringBuffer();								
				for (int i = 0; i < weekNames.length; i++) {
					String daySelected = request.getParameter("edit_dlvreq_chk"+weekNames[i].name());
					if(null != daySelected) {
						timeSlotModel = new FDPromoDlvTimeSlotModel();
						timeSlotModel.setPromoDlvZoneId(zone);
						if(!"X".equalsIgnoreCase(radius)){
							timeSlotModel.setDlvTimeStart(startTime);
							timeSlotModel.setDlvTimeEnd(endTime);
						} else {
							timeSlotModel.setWindowTypes(windowTypes);
						}
						timeSlotModel.setDayId(i+1);
						dlvTimeSlots.add(timeSlotModel);
						daysSelected.append(i+1);
						
						dlvDay = new FDPromoDlvDayModel();
						dlvDay.setDayId(i+1);
						dlvDay.setRedeemCount(redeemCnt);
						dlvDays.add(dlvDay);
					}	
				}
				dlvZoneModel.setDlvDays(daysSelected.toString());
			}
			
			List<FDPromoDlvZoneStrategyModel> dlvZones = new ArrayList<FDPromoDlvZoneStrategyModel>();
			dlvZones.add(dlvZoneModel);
			promotion.setDlvZoneStrategies(dlvZones);
			if("DLV".equalsIgnoreCase(discount)){
				promotion.setWaiveChargeType("DLV");
				promotion.setMaxAmount(null);
			}else{
				promotion.setMaxAmount(discount);
				promotion.setWaiveChargeType(null);
				promotion.setRollingExpirationDays(null);
				promotion.setRollingExpDayFrom1stOrder(false);
			}
			promotion.setStatus(EnumPromotionStatus.DRAFT);
			populatePromoChangeModelOnCreate(promotion);
			promotion.setAttributeList(attrParamList);
			promotion.setProfileOperator(profileOperator);
			return promotion;
		}catch(ParseException pe){
			throw new IllegalArgumentException("Invalid Date Format");
		}
	}

	private void updatePromotion(HttpServletRequest request, FDPromotionNewModel promotion, String effectiveDate, String startDateStr, String endDateStr, String zone, String startTime, 
			String endTime, String discount, String redeemLimit, String deliveryDayType, String radius, String[] windowTypes, List attrParamList, String profileOperator,
			String dlvRestrictionType, boolean isRollingFrom1stOrder, Integer rollingExpDays,String sapConditionType, String fdxTierType) throws FDResourceException{
		try {
			Date startDate = dateFormat.parse(startDateStr);
			Date expDate = endDateFormat.parse(endDateStr+" "+JUST_BEFORE_MIDNIGHT);
			int redeemCnt = 0;
			if(redeemLimit != null && redeemLimit.length() > 0){
				redeemCnt = Integer.parseInt(redeemLimit);
			}
			
			long E4 = Math.round(Math.random()*1000); //Unique counter
			String section1 = "X".equalsIgnoreCase(radius)?"RADIUS":"STATIC";
			if(!"DLV".equalsIgnoreCase(discount)){
				promotion.setName("WS_"+DateUtil.format(DateUtil.parseMDY(effectiveDate))+"_Zone"+zone+"_"+section1+"_"+E4+"_$"+discount);
				promotion.setDescription(formatPromoDescription(PROMO_DESCRIPTION, discount));
				promotion.setOfferDesc(formatAudienceDescription(OFFER_DESCRIPTION, discount, zone, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
				promotion.setAudienceDesc(formatOfferDescription(AUDIENCE_DESCRIPTION, zone, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
				promotion.setTerms(formatTerms(TERMS, discount, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
			}else{
				promotion.setName("WS_"+DateUtil.format(DateUtil.parseMDY(effectiveDate))+"_Zone"+zone+"_"+section1+"_"+E4+"_"+"FREE_DELIVERY");				
				promotion.setDescription(formatPromoDescription(PROMO_FREE_DELIVERY_DESCRIPTION, "Free Delivery"));
				promotion.setOfferDesc(formatAudienceDescription(OFFER_FREE_DELIVERY_DESCRIPTION, "Free Delivery", zone, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
				promotion.setAudienceDesc(formatOfferDescription(AUDIENCE_DESCRIPTION, zone, CCFormatter.defaultFormatDate(startDate), startTime, endTime));
				promotion.setTerms(formatTerms(TERMS_FREE_DELIVERY, "Free Delivery", CCFormatter.defaultFormatDate(startDate), startTime, endTime));
			}
			
			Date today = new Date();
			if(DateUtil.isSameDay(startDate, today)) {
				//If start date is today then Should default to today and the current time.
				promotion.setStartDate(today);
			} else {
				//If another date is selected, 12:00am can be the default.
				promotion.setStartDate(DateUtil.truncate(startDate));
			}
			promotion.setExpirationDate(expDate);
			if(redeemCnt > 0 && "DELIVERYDATE".equals(dlvRestrictionType))
				promotion.setRedeemCount(redeemCnt);
			else
				promotion.setRedeemCount(0);
				
			Date dlvDate = null;
			FDPromoDlvTimeSlotModel timeSlotModel = null;
			FDPromoDlvDayModel dlvDay = null;
			
			FDPromoDlvZoneStrategyModel dlvZoneModel = new FDPromoDlvZoneStrategyModel();
			List<FDPromoDlvTimeSlotModel> dlvTimeSlots = new ArrayList<FDPromoDlvTimeSlotModel>();	
			List<FDPromoDlvDateModel> dlvDates = new ArrayList<FDPromoDlvDateModel>();
			List<FDPromoDlvDayModel> dlvDays = new ArrayList<FDPromoDlvDayModel>();
			
			dlvZoneModel.setDlvDayRedemtions(dlvDays);
			dlvZoneModel.setDlvTimeSlots(dlvTimeSlots);
			dlvZoneModel.setDlvZones(new String[]{zone});			
			dlvZoneModel.setPromotionId("");
			
			if("DELIVERYDATE".equals(dlvRestrictionType)) {
				dlvDate = dateFormat.parse(effectiveDate);
				FDPromoDlvDateModel dateModel = new FDPromoDlvDateModel();
				dateModel.setDlvDateStart(dlvDate);
				dateModel.setDlvDateEnd(dlvDate);
				
				dlvDates.add(dateModel);
				promotion.setDlvDates(dlvDates);				
			
				timeSlotModel = new FDPromoDlvTimeSlotModel();
				timeSlotModel.setPromoDlvZoneId(zone);
			
				if(!"X".equalsIgnoreCase(radius)){
					timeSlotModel.setDlvTimeStart(startTime);
					timeSlotModel.setDlvTimeEnd(endTime);
				} else {
					timeSlotModel.setWindowTypes(windowTypes);
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(dlvDate);
				int dayId = cal.get(Calendar.DAY_OF_WEEK);
				timeSlotModel.setDayId(dayId);
				dlvZoneModel.setDlvDays(String.valueOf(dayId));
				dlvTimeSlots.add(timeSlotModel);
				
			} else if("DELIVERYDAY".equals(dlvRestrictionType)) {

				WeekDay weekNames[] = WeekDay.values();
				StringBuffer daysSelected = new StringBuffer();								
				for (int i = 0; i < weekNames.length; i++) {
					String daySelected = request.getParameter("edit_dlvreq_chk"+weekNames[i].name());
					if(null != daySelected) {
						timeSlotModel = new FDPromoDlvTimeSlotModel();
						timeSlotModel.setPromoDlvZoneId(zone);
						if(!"X".equalsIgnoreCase(radius)){
							timeSlotModel.setDlvTimeStart(startTime);
							timeSlotModel.setDlvTimeEnd(endTime);
						} else {
							timeSlotModel.setWindowTypes(windowTypes);
						}
						timeSlotModel.setDayId(i+1);
						dlvTimeSlots.add(timeSlotModel);
						daysSelected.append(i+1);
						
						dlvDay = new FDPromoDlvDayModel();
						dlvDay.setDayId(i+1);
						dlvDay.setRedeemCount(redeemCnt);
						dlvDays.add(dlvDay);
					}	
				}
				dlvZoneModel.setDlvDays(daysSelected.toString());
				promotion.setDlvDates(dlvDates);
			}

			List<FDPromoDlvZoneStrategyModel> dlvZones = new ArrayList<FDPromoDlvZoneStrategyModel>();
			dlvZones.add(dlvZoneModel);
			promotion.setDlvZoneStrategies(dlvZones);

			List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
			if(custStrategies != null && !custStrategies.isEmpty()){
				FDPromoCustStrategyModel custStrategy = custStrategies.get(0);
				if(custStrategy != null){
					//Clear PK
					custStrategy.setPK(null);
				}
				custStrategy.setFdxTierType(EnumPromoFDXTierType.getEnum(fdxTierType));
				EnumDeliveryOption deliveryOption= EnumDeliveryOption.getEnum(deliveryDayType);
				custStrategy.setDeliveryDayType(deliveryOption);
			}
//			promotion.setMaxAmount(discount);

			promotion.setRollingExpDayFrom1stOrder(isRollingFrom1stOrder);
			promotion.setNeedCustomerList(null !=rollingExpDays && rollingExpDays >0 && !isRollingFrom1stOrder);
			promotion.setRollingExpirationDays(rollingExpDays);
			if("DLV".equalsIgnoreCase(discount)){
				promotion.setWaiveChargeType("DLV");
				promotion.setMaxAmount(null);
			}else{
				promotion.setMaxAmount(discount);
				promotion.setWaiveChargeType(null);
				promotion.setRollingExpirationDays(null);
				promotion.setRollingExpDayFrom1stOrder(false);
			}
			promotion.setStatus(EnumPromotionStatus.PROGRESS);
			populatePromoChangeModelOnModify(promotion, dlvZoneModel);
			promotion.setRadius(radius);
			promotion.setSapConditionType(sapConditionType);
			promotion.setAttributeList(attrParamList);
			promotion.setProfileOperator(profileOperator);
		}catch(ParseException pe){
			throw new IllegalArgumentException("Invalid Date Format");
		}
		
	}

	
	private void populatePromoChangeModelOnCreate(FDPromotionNewModel promotion) throws FDResourceException{
		FDPromoChangeModel changeModel = new FDPromoChangeModel();
		List<FDPromoChangeDetailModel> promoChangeDetails = new ArrayList<FDPromoChangeDetailModel>();
		List<FDPromoChangeModel> promoChanges = new ArrayList<FDPromoChangeModel>();
		changeModel.setChangeDetails(promoChangeDetails);
		changeModel.setPromotionId(promotion.getId());
		if(null == promotion.getId()){
			changeModel.setActionDate(promotion.getCreatedDate());
			changeModel.setActionType(EnumPromoChangeType.CREATE);
			changeModel.setUserId(promotion.getCreatedBy());
		}
		promoChanges.add(changeModel);
		promotion.setAuditChanges(promoChanges);
		
	}

	private void populatePromoChangeModelOnModify(FDPromotionNewModel promotion, FDPromoDlvZoneStrategyModel model) throws FDResourceException
	 {
			
			List promoChanges = new ArrayList<FDPromoChangeModel>();
			List promoChangeDetails = new ArrayList<FDPromoChangeDetailModel>();
			/*if(null !=promotion.getAuditChanges()){
				promoChanges = promotion.getAuditChanges();				
			}*/
			FDPromoChangeModel changeModel = new FDPromoChangeModel();
			changeModel.setActionDate(promotion.getModifiedDate());
			changeModel.setActionType(EnumPromoChangeType.MODIFY_WS);
			changeModel.setUserId(promotion.getModifiedBy());
			changeModel.setPromotionId(promotion.getId());
			changeModel.setChangeDetails(promoChangeDetails);
			promoChanges.add(changeModel);
			promotion.setAuditChanges(promoChanges);
			
			FDPromotionNewModel oldPromotion = FDPromotionNewManager.loadPromotion(promotion.getPromotionCode());

			if(null != oldPromotion && !promotion.getStartDate().equals(oldPromotion.getStartDate())){
				FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
				changeDetailModel.setChangeFieldName("Start Date");
				changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getStartDateStr(),""));
				changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getStartDateStr(),""));
				changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
				promoChangeDetails.add(changeDetailModel);	
			}

			if(null != oldPromotion && !promotion.getExpirationDate().equals(oldPromotion.getExpirationDate())){
				FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
				changeDetailModel.setChangeFieldName("Expiration Date");
				changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getExpirationDateStr(),""));
				changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getExpirationDateStr(),""));
				changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
				promoChangeDetails.add(changeDetailModel);	
			}

			if(null != oldPromotion && null != oldPromotion.getDlvZoneStrategies() && !oldPromotion.getDlvZoneStrategies().isEmpty()){
			
				FDPromoDlvZoneStrategyModel oldDlvModel = (FDPromoDlvZoneStrategyModel)oldPromotion.getDlvZoneStrategies().get(0);
				if(null != oldDlvModel){
					if(null !=oldDlvModel.getDlvZones() && (null !=model.getDlvZones())){					
						FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
						changeDetailModel.setChangeFieldName("Zones");
						changeDetailModel.setChangeFieldOldValue(null !=oldDlvModel.getDlvZones() && oldDlvModel.getDlvZones().length > 0?Arrays.asList(oldDlvModel.getDlvZones()).toString():"");
						changeDetailModel.setChangeFieldNewValue(null !=model.getDlvZones() && model.getDlvZones().length > 0?Arrays.asList(model.getDlvZones()).toString():"");
						changeDetailModel.setChangeSectionId(EnumPromotionSection.DELIVERY_REQ_INFO);
						promoChangeDetails.add(changeDetailModel);
					}
					if(oldDlvModel.getDlvDays()!= model.getDlvDays()){
						if(null == oldDlvModel.getDlvDays()|| null == model.getDlvDays() || !oldDlvModel.getDlvDays().equalsIgnoreCase(model.getDlvDays())){
							FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
							changeDetailModel.setChangeFieldName("Delivery Days");
							changeDetailModel.setChangeFieldOldValue(oldDlvModel.getDlvDays());
							changeDetailModel.setChangeFieldNewValue(model.getDlvDays());
							changeDetailModel.setChangeSectionId(EnumPromotionSection.DELIVERY_REQ_INFO);
							promoChangeDetails.add(changeDetailModel);
						}						
					}
					if(oldDlvModel.getDlvTimeSlots()!= model.getDlvTimeSlots()){
						if(null == oldDlvModel.getDlvTimeSlots()|| null == model.getDlvTimeSlots() || oldDlvModel.getDlvTimeSlots().size()!=(model.getDlvTimeSlots().size())){
							FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
							changeDetailModel.setChangeFieldName("No.of Delivery Timeslots");
							changeDetailModel.setChangeFieldOldValue(null == oldDlvModel.getDlvTimeSlots()?" ":""+oldDlvModel.getDlvTimeSlots().size());
							changeDetailModel.setChangeFieldNewValue(null == model.getDlvTimeSlots()?" ":""+model.getDlvTimeSlots().size());
							changeDetailModel.setChangeSectionId(EnumPromotionSection.DELIVERY_REQ_INFO);
							promoChangeDetails.add(changeDetailModel);
						}
					}
				}
				if(null != oldPromotion && (null== promotion.getMaxAmount() || !promotion.getMaxAmount().equalsIgnoreCase(oldPromotion.getMaxAmount()))){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Max Amount");
					changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getMaxAmount(),"").trim());
					changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getMaxAmount(),"").trim());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
					promoChangeDetails.add(changeDetailModel);	
				}
				
			}
			
		}
				
			
	private void postValidate(FDPromotionNewModel promotion,ActionResult result) throws FDResourceException{

				if(null == promotion.getStartDate()){
					result.addError(true, "startDateEmpty", "Promotion Start Date can't be empty.");
				}
				if(null == promotion.getExpirationDate()){
					result.addError(true, "endDateEmpty", "Promotion End Date can't be empty.");
				}
				if("".equals(NVL.apply(promotion.getMaxUsage(),"").trim()) || "0".equals(promotion.getMaxUsage().trim())){
					result.addError(true, "usageCountEmpty", "Promotion Usage Count is required.");
				}
				if("".equals(NVL.apply(promotion.getPromotionType(),"").trim())){
					result.addError(true, "offerTypeEmpty", "Promotion Offer Type can't be empty.");
				}
				else if(EnumPromotionType.HEADER.getName().equals(promotion.getPromotionType()) ){
					if(!"DLV".equalsIgnoreCase(promotion.getWaiveChargeType())){
						if("".equals(NVL.apply(promotion.getPercentOff(),"").trim()) && "".equals(NVL.apply(promotion.getMaxAmount(),"").trim()) && null== promotion.getExtendDpDays()){
							result.addError(true, "maxAmountEmpty", "Discount value is required");
						}
					}
					if(EnumPromotionType.WINDOW_STEERING.getName().equalsIgnoreCase(promotion.getOfferType())){
						if(!"ZONE".equalsIgnoreCase(promotion.getGeoRestrictionType())){					
							result.addError(true, "wsZoneRequired", "For a Window Steering promotion, ZONE type georestriction should be configured.");
						}
						if(!promotion.isCombineOffer()){
							result.addError(true, "combineOfferRequired", "For a Window Steering promotion, 'combine offer' should be selected.");
						}
					}
				}
				
				List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
				if(null!= custStrategies && !custStrategies.isEmpty()){
					FDPromoCustStrategyModel custModel = (FDPromoCustStrategyModel)custStrategies.get(0);
					if(!custModel.isOrderTypeHome() && !custModel.isOrderTypeCorporate() && !custModel.isOrderTypePickup()){
						result.addError(true, "addressTypeEmpty", "Promotion delivery address type must be selected.");
					}
					if("DLV".equalsIgnoreCase(promotion.getWaiveChargeType()) && null == custModel.getFdxTierType()){
						result.addError(true, "fdxTierTypeEmpty", "For 'Free Delivery' window steering promotion, FDX timeslot type must be selected.");
					}
				}else{
					result.addError(true, "addressTypeEmpty", "Promotion delivery address type must be selected.");
					result.addError(true, "fdxTierTypeEmpty", "For 'Free Delivery' window steering promotion, FDX timeslot type must be selected.");
				}
				/*if(null == promotion.getMinSubtotal() || "".equals(promotion.getMinSubtotal())){
					result.addError(true, "minSubTotalEmpty", "Minimum Sub Total is required for the promotion.");
				}*/
				if(FDPromotionNewManager.isRedemptionCodeExists(promotion.getRedemptionCode(),promotion.getId())){
					result.addError(true, "redemptionCodeDuplicate", " An active promotion exists with the same redemption code, please change the redemption code.");				
				}
				
				if(FDPromotionNewManager.isRafPromoCodeExists (promotion.getRafPromoCode(),promotion.getId())){
					result.addError(true, "rafPromoCodeDuplicate", " An active promotion exists with the same RAF promo code, please change the RAF promo code.");				
				}

				if("X".equals(promotion.getRadius())) {
					List<FDPromoDlvZoneStrategyModel> zoneStrategies = promotion.getDlvZoneStrategies();
				
					if(null!= zoneStrategies && !zoneStrategies.isEmpty()){
						FDPromoDlvZoneStrategyModel zoneModel = (FDPromoDlvZoneStrategyModel)zoneStrategies.get(0);
						if(zoneModel.getDlvTimeSlots() != null){
							FDPromoDlvTimeSlotModel tm = (FDPromoDlvTimeSlotModel) zoneModel.getDlvTimeSlots().get(0);
							if(tm.getWindowTypes() != null && tm.getWindowTypes().length == 0){
								result.addError(true, "windowTypeEmpty", "Window Type cannot be Empty.");
							}
						}
					}
				}
	}

	private String formatPromoDescription(String pattern, String discount) {
		return MessageFormat.format(
			pattern,
			new Object[] {discount});
	}

	private String formatAudienceDescription(String pattern, String discount, String zoneCode, String effectiveDate, String startTime, String endTime) {
		return MessageFormat.format(
			pattern,
			new Object[] {discount, zoneCode, effectiveDate, startTime, endTime});
	}

	private String formatOfferDescription(String pattern, String zoneCode, String effectiveDate, String startTime, String endTime) {
		return MessageFormat.format(
			pattern,
			new Object[] {zoneCode, effectiveDate, startTime, endTime});
	}

	private String formatTerms(String pattern, String discount, String effectiveDate, String startTime, String endTime) {
		return MessageFormat.format(
			pattern,
			new Object[] {discount, effectiveDate, startTime, endTime});
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
			            new VariableInfo(data.getAttributeString("result"),
                            "com.freshdirect.framework.webapp.ActionResult",
                            true,
                            VariableInfo.NESTED),   
		        };

		    }
	}
	
	public static List<String> getWindowTypeParamList(HttpServletRequest request) {
		
		Enumeration paramNames = request.getParameterNames();
		
		List<String> dataList = new ArrayList<String>();
		List dataTmpKeyList = new ArrayList();
		
		String paramName = null;
				
		String tmpIndex = null;
		int tmpBracesIndex = -1;
		int endBracesIndex = -1;
				
	    while(paramNames.hasMoreElements()) {
	      paramName = (String)paramNames.nextElement();
	      if(paramName.startsWith("windowTypeList")) {	    	  
	       	  tmpBracesIndex = paramName.indexOf("[");
	       	  endBracesIndex =	paramName.indexOf("]", tmpBracesIndex);
	    	  if(tmpBracesIndex != -1 && endBracesIndex != -1) {
	    		  tmpIndex = paramName.substring(tmpBracesIndex+1,endBracesIndex);
	    		  
	    		  if(!dataTmpKeyList.contains(tmpIndex)) {
	    			  dataList.add(getWindowTypeParam(request, tmpIndex));
	    			  dataTmpKeyList.add(tmpIndex);
	    		  }
	    	  }
	      }
	    }	    
	    return dataList;
	}
	
	public static List<FDPromotionAttributeParam> getAttributeParamList(HttpServletRequest request) {
		
		Enumeration paramNames = request.getParameterNames();
		
		List<FDPromotionAttributeParam> dataList = new ArrayList<FDPromotionAttributeParam>();
		List dataTmpKeyList = new ArrayList();
		
		String paramName = null;
				
		String tmpIndex = null;
		int tmpBracesIndex = -1;
		int endBracesIndex = -1;
				
	    while(paramNames.hasMoreElements()) {
	      paramName = (String)paramNames.nextElement();
	      if(paramName.startsWith("attributeList")) {	    	  
	       	  tmpBracesIndex = paramName.indexOf("[");
	       	  endBracesIndex =	paramName.indexOf("]", tmpBracesIndex);
	    	  if(tmpBracesIndex != -1 && endBracesIndex != -1) {
	    		  tmpIndex = paramName.substring(tmpBracesIndex+1,endBracesIndex);
	    		  
	    		  if(!dataTmpKeyList.contains(tmpIndex)) {
	    			  dataList.add(getAttributeParam(request, tmpIndex));
	    			  dataTmpKeyList.add(tmpIndex);
	    		  }
	    	  }
	      }
	    }
	    return dataList;
	}

	public static FDPromotionAttributeParam getAttributeParam(HttpServletRequest request, String index) {

		FDPromotionAttributeParam tmpParam = new FDPromotionAttributeParam();
		String key = "attributeList["+index+"].";
		tmpParam.setAttributeIndex(index);
		tmpParam.setAttributeName(request.getParameter(key+"attributeName"));
		tmpParam.setDesiredValue(request.getParameter(key+"desiredValue").trim());// Fix to trim invalid profiles with space.		
		return tmpParam;
	}

	private static String getWindowTypeParam(HttpServletRequest request, String index) {
	
		String key = "windowTypeList["+index+"].";
		String atrValue = request.getParameter(key+"desiredValue").trim();		
		return atrValue;
	}

}
