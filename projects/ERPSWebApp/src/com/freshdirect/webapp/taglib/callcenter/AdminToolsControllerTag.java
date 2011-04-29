package com.freshdirect.webapp.taglib.callcenter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.MunicipalityInfoWrapper;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvAddressVerificationResponse;
import com.freshdirect.delivery.DlvRestrictionManager;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumAddressVerificationResult;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.OneTimeRestriction;
import com.freshdirect.delivery.restriction.OneTimeReverseRestriction;
import com.freshdirect.delivery.restriction.RecurringRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.enums.RestrictionWeekDay;
import com.freshdirect.enums.WeekDay;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.customer.ExtendDPDiscountModel;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.promotion.ExtendDeliveryPassApplicator;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.TimeOfDayRange;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ActionWarning;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class AdminToolsControllerTag extends AbstractControllerTag {
	
	private static Category LOGGER = LoggerFactory.getInstance(AdminToolsControllerTag.class);
	private static  final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static  final DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
	private final static TimeOfDay JUST_BEFORE_MIDNIGHT = new TimeOfDay("11:59 PM");
	private static  final DateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");	
	
	
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = request.getParameter("actionName");
		try {
			HttpSession session = pageContext.getSession();
			int prcLimit = FDStoreProperties.getOrderProcessingLimit();
			if(actionName != null && actionName.equals("deleteReservations")){
				GenericSearchCriteria criteria = (GenericSearchCriteria)session.getAttribute("RESV_SEARCH_CRITERIA");
				if(criteria == null){
					LOGGER.error("The reservation criteria object is not available in the Session.");
					actionResult.addError(true, "actionfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
					return true;
				}
				List resvList = (List) session.getAttribute("SEARCH_RESULTS");
				int updateCount = doDeleteReservations(request, criteria, resvList);
				if(updateCount > 0){
					LOGGER.info(updateCount+" Reservation(s) were successfully deleted from the System.");
					actionResult.addWarning(true, "deletesuccess", updateCount+" Reservation(s) were successfully deleted from the System.");
					return true;
				} else{
					LOGGER.info("No Reservations were deleted from the System.");
					actionResult.addError(true, "deletesuccess", "No Reservations were deleted from the System.");
					return true;
				}
			}else if(actionName != null && actionName.equals("fixBrokenAccounts")){
				int updateCount = CallCenterServices.fixBrokenAccounts();
				if(updateCount > 0){
					
					LOGGER.info(updateCount+" Broken Account(s) were successfully fixed by the System.");
					actionResult.addWarning(true, "fixsuccess", updateCount+" Broken Account(s) were successfully fixed by the System.");
					setSuccessPage("/supervisor/broken_acct_list.jsp?method=GET");
					return true;
				} else{
					LOGGER.info("No Broken Accounts were fixed by the System.");
					actionResult.addError(true, "fixsuccess", "No Broken Accounts were fixed by the System.");
					return true;
				}
				
			}else if(actionName != null && actionName.equals("cancelOrders")){
				//Submit n orders at a time. Default is 100.
				List cancelOrders = buildCustomerOrdersFromRequest(request, prcLimit);
				doCancelOrders(request, cancelOrders, actionResult);
				
			}else if(actionName != null && actionName.equals("returnOrders")){
				//Submit n orders at a time. Default is 100.
				List returnOrders = buildCustomerOrdersFromRequest(request, prcLimit);
				doReturnOrders(request, returnOrders, actionResult);
			}else if(actionName != null && actionName.equals("createSnapShot")){
				//Take the snapshot of the orders
				GenericSearchCriteria criteria = (GenericSearchCriteria)session.getAttribute("ORDER_SEARCH_BY_SKUS");
				if(criteria == null){
					LOGGER.error("The search criteria is not available in the Session.");
					actionResult.addError(true, "actionfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
					return true;
				}
				CallCenterServices.createSnapShotForModifyOrders(criteria);
				actionResult.addWarning(new ActionWarning("createSnapSuccess", "SnapShot Successfully Created."));

			}else if(actionName != null && actionName.equals("modifyOrders")){
				if(!validateSkuCodes(request, actionResult)){
					return true;
				}
				//Submit n orders at a time. Default is 100.
				List<FDCustomerOrderInfo> modifyOrders = buildCustomerOrdersFromRequest(request, prcLimit);
				doBulkModifyOrders(request, modifyOrders, actionResult);
				
			}else if(actionName != null && actionName.equals("fixSettlemnentBatch")){
				String batch_id = request.getParameter("batch_id");
				int updateCount = CallCenterServices.fixSettlemnentBatch(batch_id);
				if(updateCount > 0){
					
					LOGGER.info(updateCount+" settlement batch(s) were successfully fixed.");
					actionResult.addWarning(true, "fixsuccess", updateCount+" settlement batch(s) were successfully fixed.");
					setSuccessPage("/admintools/settlement_batch.jsp?method=GET");
					return true;
				} else{
					LOGGER.info("No settlement batch were fixed by the System.");
					actionResult.addError(true, "fixsuccess", "No settlement batch were fixed by the System.");
					return true;
				}
				
			}else if("updateRestriction".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				doUpdateRestriction(request,actionResult);
				// if update is successful then show the success message
				if(!actionResult.isFailure()){
					  request.setAttribute("successMsg","record is updated successfully");
				}				
			}
			else if("deleteRestrctions".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				doDeleteRestriction(request);
				// if update is successful then show the success message
				String restrictionId=request.getParameter("restrictionId");
				request.setAttribute("successMsg"," Restriction: "+restrictionId+"is deleted successfully");
			}else if("saveAlcoholRestriction".equalsIgnoreCase(actionName)){
				String restrictionId = request.getParameter("restrictionId");
				if(restrictionId == null || restrictionId.length() == 0) {
					doAddAlcoholRestriction(request, actionResult);
					if(!actionResult.isFailure()){
						  request.setAttribute("successMsg","Restriction added successfully");
					}				
				} else {
					doUpdateAlcoholRestriction(request, actionResult);
					// if update is successful then show the success message
					if(!actionResult.isFailure()){
						  request.setAttribute("successMsg","Restriction updated successfully");
					}				
				}
			}else if("deleteAlcoholRestriction".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				doDeleteAlcoholRestriction(request);
				// if update is successful then show the success message
				String restrictionId=request.getParameter("restrictionId");
				request.setAttribute("successMsg"," Restriction: "+restrictionId+" deleted successfully.");
			}else if("addBlockedDays".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				RestrictionI restriction=validateAndConstructARDRestriction(request,actionResult);
				if(!actionResult.isFailure()){
					  addBlockedDay(request,restriction);
					  request.setAttribute("successMsg","record is updated successfully");
				}		
				
				// if update is successful then show the success message
				//String restrictionId=request.getParameter("restrictionId");
				request.setAttribute("recentRestriction",restriction);
			}else if("updatePlatter".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				doUpdatePlatter(request);
				// if update is successful then show the success message
				request.setAttribute("successMsg","record is updated successfully");
			}else if("updateKosher".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				doUpdateKosher(request);
				// if update is successful then show the success message
				request.setAttribute("successMsg","record is updated successfully");
			}else if("updateAddrRestriction".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				doUpdateAddressRestriction(request,actionResult);
				// if update is successful then show the success message
				if(!actionResult.isFailure()){
				  request.setAttribute("successMsg","record is updated successfully");
				}
			}else if("deleteAddressRestrctions".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				doDeleteAddressRestriction(request);
				// if update is successful then show the success message
				String restrictionId=request.getParameter("restrictionId");
				request.setAttribute("successMsg"," Restriction: "+restrictionId+"is deleted successfully");
			} else if("updateAlcoholRestrictionFlag".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				doUpdateAlcoholRestrictionFlag(request);
				// if update is successful then show the success message
				request.setAttribute("successMsg","Alcohol restriction flag updated successfully for this Municipality.");
			} else if("addAddressRestriction".equalsIgnoreCase(actionName)){
				//Submit n orders at a time. Default is 100.
				RestrictedAddressModel restriction=validateAndConstructAddressRestriction(request,actionResult);
				LOGGER.debug("actionResult.isFailure :"+actionResult.isFailure());
				LOGGER.debug("actionResult :"+actionResult);
				if(actionResult.isFailure()){
					return true;
				}
				
				RestrictedAddressModel model=DlvRestrictionManager.getAddressRestriction(restriction.getAddress1(),restriction.getApartment(),restriction.getZipCode());
				if(model!=null && model.getAddress1()!=null){
					actionResult.addError(true,"GLOBAL_ERROR","Address Already Exist");
				}
				
				if(actionResult.isFailure()){
					return true;
				}

				performAddressCheck(request,actionResult,restriction);
				
				if(actionResult.isFailure()){
					return true;
				}
							
				addAddressRestriction(request,restriction);
				request.setAttribute("recentAddrRestriction",restriction);								
				// if update is successful then show the success message
				//String restrictionId=request.getParameter("restrictionId");
				
			}
 
			
		} catch(FDResourceException e){
			LOGGER.error("System Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		}catch(Exception e){
			LOGGER.error("Unknown Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		}								
		
		return true;
	}

	private void doUpdateAddressRestriction(HttpServletRequest request,ActionResult result) throws FDResourceException {
		HttpSession session = pageContext.getSession();		
		RestrictedAddressModel restriction=validateAndConstructAddressRestriction(request,result);
		if(result.isFailure()){
			return;
		}
		
		RestrictedAddressModel model=DlvRestrictionManager.getAddressRestriction(restriction.getAddress1(),restriction.getApartment(),restriction.getZipCode());
		if(model!=null && (model.getAddress1()!=null && (model.getAddress1().equalsIgnoreCase(restriction.getAddress1()) && model.getZipCode().equalsIgnoreCase(restriction.getZipCode())))){
			
			if(restriction.getApartment()==null && model.getApartment()==null) 
			{
				result.addError(true,"GLOBAL_ERROR","Address Already Exist");
			}
			else if((restriction.getApartment()!=null && model.getApartment()!=null) && (restriction.getApartment().equalsIgnoreCase(model.getApartment())))
			{
				result.addError(true,"GLOBAL_ERROR","Address Already Exist");
			}
		}
		
		if(result.isFailure()){
			return;
		}

		performAddressCheck(request,result,restriction);
		
		if(result.isFailure()){
			return;
		}
		
		String address1=request.getParameter("oldAddress1");						
		String apartment=request.getParameter("oldApt");
		String zipCode=request.getParameter("oldZipCode");		
										
		LOGGER.debug("address1"+address1);					
		LOGGER.debug("apartment"+apartment);
		LOGGER.debug("zipCode"+zipCode);	
		
		if(!result.isFailure()){
			DlvRestrictionManager.storeAddressRestriction(restriction,address1,apartment,zipCode);
		}
								
	}
	
	private void performAddressCheck(HttpServletRequest request, ActionResult actionResult, AddressModel dlvAddress) throws FDResourceException {


		DlvAddressVerificationResponse verifyResponse = FDDeliveryManager.getInstance().scrubAddress(dlvAddress);

		
		//
		// set to scrubbed address
		//
		dlvAddress = verifyResponse.getAddress();

		EnumAddressVerificationResult  verificationResult = verifyResponse.getResult();
		
		LOGGER.debug("verifyResponse11 :"+verificationResult); 

		addVerificationResultErrors(verificationResult, actionResult,dlvAddress);
		
		if (!EnumAddressVerificationResult.ADDRESS_BAD.equals(verificationResult) && !EnumAddressVerificationResult.APT_WRONG.equals(verificationResult)) {
			//
			// geocode address
			//
			String geocodeResult=null;
			try {
				DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager.getInstance().geocodeAddress(dlvAddress);
				geocodeResult = geocodeResponse.getResult();				
				
			} catch (FDInvalidAddressException iae) {
				actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				return;
			}

			addGeocodeResultErrors(geocodeResult, actionResult);

		}
		else if (
			 EnumAddressVerificationResult.APT_WRONG.equals(verificationResult)) {
			//
			// get building apartments
			//
			    actionResult.addError(true, "GLOBAL_ERROR", SystemMessageList.MSG_UNRECOGNIZE_APARTMENT_NUMBER);				
			
		}
	}

	
	private void addVerificationResultErrors(EnumAddressVerificationResult result, ActionResult actionResult, AddressModel dlvAddress) {
		
		if (EnumAddressVerificationResult.ADDRESS_BAD.equals(result)){
			actionResult.addError(true, "address1", EnumAddressVerificationResult.ADDRESS_BAD.getCode());
			//assume address1 indecipherable if has entry
			if (!"".equals(dlvAddress.getAddress1())) {
				actionResult.addError(true, "GLOBAL_ERROR", SystemMessageList.MSG_INVALID_ADDRESS);
			}
		} else if (!EnumAddressVerificationResult.ADDRESS_OK.equals(result)) {
			actionResult.addError(true, "GLOBAL_ERROR", result.getCode());
		}

	}
	
	private void addGeocodeResultErrors(String result, ActionResult actionResult) {
		if (!result.equals("GEOCODE_OK")) {
			actionResult.addError(true, "GLOBAL_ERROR", result);
		}
	}

	
	private void doUpdateKosher(HttpServletRequest request) throws FDResourceException {
		List restrictedList=DlvRestrictionManager.getDlvRestrictions(EnumDlvRestrictionReason.KOSHER.getName(),EnumDlvRestrictionType.ONE_TIME_RESTRICTION.getName(),EnumDlvRestrictionCriterion.DELIVERY.getName());
		// now update the time for the list
		for(int i=0;i<restrictedList.size();i++){			
			OneTimeRestriction r=(OneTimeRestriction)restrictedList.get(i);
			String startDateStr=request.getParameter("newStartDate-"+r.getId());			
			String endDateStr=request.getParameter("newEndDate-"+r.getId());
			LOGGER.debug("startDateStr :"+startDateStr);
			LOGGER.debug("endDateStr :"+endDateStr);			
			Date newStartDate=null;
			Date newEndDate=null;
			try {
				newStartDate=dateFormat.parse(startDateStr);
				newEndDate=endDateFormat.parse(endDateStr+" 11:59 PM");
				LOGGER.debug("newSDate"+newStartDate);
				LOGGER.debug("newEDate"+newEndDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
									
			OneTimeRestriction rNew=new OneTimeRestriction(r.getId(),r.getCriterion(),r.getReason(),r.getName(),r.getMessage(),newStartDate,newEndDate,r.getPath());
			DlvRestrictionManager.storeDlvRestriction(rNew);
		}						
	}
	
	private void doUpdatePlatter(HttpServletRequest request) throws FDResourceException {
		List restrictedList=DlvRestrictionManager.getDlvRestrictions(EnumDlvRestrictionReason.PLATTER.getName(),EnumDlvRestrictionType.RECURRING_RESTRICTION.getName(),EnumDlvRestrictionCriterion.PURCHASE.getName());
		// now update the time for the list
		for(int i=0;i<restrictedList.size();i++){
			String hour=request.getParameter("platterStartTime"+i);
			RecurringRestriction r=(RecurringRestriction)restrictedList.get(i);			
			Date newStartDate=null;
			Date newEndDate=null;
			try {
				newStartDate=timeFormat.parse(hour);
				newEndDate=timeFormat.parse("11:59 PM");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			LOGGER.debug(r.getTimeRange().getStartTime());			
			Calendar calendarStart=DateUtil.toCalendar(newStartDate);
			Calendar calendarEnd=DateUtil.toCalendar(newEndDate);
			TimeOfDay tStart=new TimeOfDay(calendarStart.getTime());
			TimeOfDay tEnd=new TimeOfDay(calendarEnd.getTime());
			// stupid thing this class is immutable
			RecurringRestriction rNew=new RecurringRestriction(r.getId(),r.getCriterion(),r.getReason(),r.getName(),r.getMessage(),r.getDayOfWeek(),tStart,tEnd,r.getPath());
			DlvRestrictionManager.storeDlvRestriction(rNew);
		}						
	}
	
	
	private void addBlockedDay(HttpServletRequest request,RestrictionI restriction) throws FDResourceException {
		//HttpSession session = pageContext.getSession();				
		DlvRestrictionManager.addDlvRestriction(restriction);				
	}
	
	
	private void addAddressRestriction(HttpServletRequest request,RestrictedAddressModel restriction) throws FDResourceException {
		//HttpSession session = pageContext.getSession();				
		DlvRestrictionManager.addAddressRestriction(restriction);				
	}
	
	private void doDeleteAddressRestriction(HttpServletRequest request) throws FDResourceException {
		HttpSession session = pageContext.getSession();		
		String address1=request.getParameter("delete_address1");						
		String apartment=request.getParameter("delete_apartment");
		String zipCode=request.getParameter("delete_zipCode");
		DlvRestrictionManager.deleteAddressRestriction(address1,apartment,zipCode);				
	}
	
	private void doDeleteRestriction(HttpServletRequest request) throws FDResourceException {
		HttpSession session = pageContext.getSession();		
		String restrictionId=request.getParameter("restrictionId");
		DlvRestrictionManager.deleteDlvRestriction(restrictionId);				
	}


	private void doUpdateAlcoholRestrictionFlag(HttpServletRequest request) throws FDResourceException {
		HttpSession session = pageContext.getSession();		
		String municipalityId=request.getParameter("municipalityId");
		boolean restricted = "on".equals(NVL.apply(request.getParameter("restricted"), "off"));
		DlvRestrictionManager.setAlcoholRestrictedFlag(municipalityId, restricted);				
	}
	
	private void doDeleteAlcoholRestriction(HttpServletRequest request) throws FDResourceException {
		HttpSession session = pageContext.getSession();		
		String restrictionId=request.getParameter("restrictionId");
		DlvRestrictionManager.deleteAlcoholRestriction(restrictionId);				
	}
	
	private void doUpdateRestriction(HttpServletRequest request,ActionResult result) throws FDResourceException {
		HttpSession session = pageContext.getSession();		
		RestrictionI restriction=validateAndConstructRestriction(request,result);				
		if(!result.isFailure()){
			DlvRestrictionManager.storeDlvRestriction(restriction);
		}
	}

	private void doUpdateAlcoholRestriction(HttpServletRequest request,ActionResult result) throws FDResourceException {
		HttpSession session = pageContext.getSession();		
		AlcoholRestriction restriction=(AlcoholRestriction) validateAndConstructRestriction(request,result);				
		if(!result.isFailure()){
			DlvRestrictionManager.storeAlcoholRestriction(restriction);
		}
		request.setAttribute("restriction", restriction);
	}
	
	private void doAddAlcoholRestriction(HttpServletRequest request,ActionResult result) throws FDResourceException {
		HttpSession session = pageContext.getSession();		
		AlcoholRestriction restriction=(AlcoholRestriction) validateAndConstructRestriction(request,result);				
		if(!result.isFailure()){
			String restrictionId = DlvRestrictionManager.addAlcoholRestriction(restriction);
			restriction.setId(restrictionId);
		}
		request.setAttribute("restriction", restriction);
	}
		
	private RestrictedAddressModel validateAndConstructAddressRestriction(HttpServletRequest request,ActionResult result) throws FDResourceException{
		RestrictedAddressModel restriction=null;
		try{
			//String id=request.getParameter("restrictionId");
			HttpSession session = pageContext.getSession();
			String address1=request.getParameter("address1");						
			String apartment=request.getParameter("apartment");
			String zipCode=request.getParameter("zipCode");		
			EnumRestrictedAddressReason reason=EnumRestrictedAddressReason.getRestrictionReason(request.getParameter("reason"));								
			LOGGER.debug("address11"+address1);					
			LOGGER.debug("apartmen1"+apartment);
			LOGGER.debug("zipCode1"+zipCode);			
			LOGGER.debug("reason"+reason);
			CrmAgentModel agentModel = CrmSession.getCurrentAgent(session);
			
			result.addError(
					address1==null || address1.trim().length()==0,
					"address1",
					SystemMessageList.MSG_REQUIRED);
			
			result.addError(
					zipCode==null || zipCode.trim().length()==0,
					"zipCode",
					SystemMessageList.MSG_REQUIRED);
			
			// FIXME one-time reverse restrictions should have a different EnumDlvRestrictionType 
			restriction=new RestrictedAddressModel();
			restriction.setAddress1(address1);
			restriction.setApartment(apartment);
			restriction.setZipCode(zipCode);
			restriction.setReason(reason);			
			restriction.setModifiedBy(agentModel.getUserId());			
		}catch (Exception pe) {
			throw new FDResourceException(pe);
		}
		return restriction;
	}
	
	private RestrictionI validateAndConstructARDRestriction(HttpServletRequest request,ActionResult result) throws FDResourceException{
		RestrictionI restriction=null;
		try{
			//String id=request.getParameter("restrictionId");
			
			String message=request.getParameter("blkMessage");						
			String startTimeStr=request.getParameter("blkStartDate");
			String endTimeStr=request.getParameter("blkEndDate");		
			String path=request.getParameter("blkPath");
			
			result.addError(
					message==null || message.trim().length()==0,
					"blkMessage",
					SystemMessageList.MSG_REQUIRED);
			
			LOGGER.debug("message"+message);					
			LOGGER.debug("startTimeStr0"+startTimeStr);
			LOGGER.debug("endTimeStr"+endTimeStr);			
			LOGGER.debug("message"+path);
						   
			Date startDate  = dateFormat.parse(startTimeStr);
			Date endDate  = endDateFormat.parse(endTimeStr+" 11:59 PM");

			LOGGER.debug("startDate"+startDate);
			LOGGER.debug("endDate"+endDate);

			if(endDate.before(startDate)){
				result.addError(
						true,
						"newBlkEndDate",
						"End Date cannot be less than the Start Date");
				return null;
			}	
			
			// FIXME one-time reverse restrictions should have a different EnumDlvRestrictionType 
				restriction=new OneTimeRestriction(null, EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.CLOSED, "Closed",message, startDate, endDate,path);
						
		}catch (Exception pe) {
			throw new FDResourceException(pe);
		}
		return restriction;
	}
	
	
	private RestrictionI validateAndConstructRestriction(HttpServletRequest request,ActionResult result) throws FDResourceException{
		RestrictionI restriction=null;
		try{
			String id=request.getParameter("restrictionId");
			String name=request.getParameter("name");
			String message=request.getParameter("message");
			EnumDlvRestrictionReason reason=EnumDlvRestrictionReason.getEnum(request.getParameter("reason"));
			EnumDlvRestrictionCriterion criterion=EnumDlvRestrictionCriterion.getEnum(request.getParameter("criterion"));
			EnumDlvRestrictionType restrictedType=EnumDlvRestrictionType.getEnum(request.getParameter("restrictedType"));
			String startTimeStr=request.getParameter("startDate");
			String endTimeStr=request.getParameter("endDate");		
			String dayOfWeekStr=request.getParameter("dayOfWeek");
			String path=request.getParameter("path");
			
			result.addError(
					name==null || name.trim().length()==0,
					"name",
					SystemMessageList.MSG_REQUIRED);
			
			result.addError(
					message==null || message.trim().length()==0,
					"message",
					SystemMessageList.MSG_REQUIRED);
			
			result.addError(
					criterion==null,
					"criterion",
					SystemMessageList.MSG_REQUIRED);

			result.addError(
					reason==null,
					"reason",
					SystemMessageList.MSG_REQUIRED);
			
			result.addError(
					restrictedType==null,
					"restrictedType",
					SystemMessageList.MSG_REQUIRED);
			
			result.addError(
					startTimeStr==null || startTimeStr.trim().length()==0,
					"startDate",
					SystemMessageList.MSG_REQUIRED);

			result.addError(
					endTimeStr==null || endTimeStr.trim().length()==0,
					"endDate",
					SystemMessageList.MSG_REQUIRED);
						
			LOGGER.debug("id"+id);
			LOGGER.debug("name"+name);
			LOGGER.debug("message"+message);
			LOGGER.debug("reason"+reason);
			LOGGER.debug("criterion"+criterion);
			LOGGER.debug("restrictedType"+restrictedType);
			LOGGER.debug("startTimeStr"+startTimeStr);
			LOGGER.debug("endTimeStr12"+endTimeStr);
			LOGGER.debug("dayOfWeekStr"+dayOfWeekStr);
			
			int dayOfWeek=1;
			if(dayOfWeekStr!=null && dayOfWeekStr.trim().length()>0){
				try{
				 dayOfWeek=Integer.parseInt(dayOfWeekStr);
				}catch(NumberFormatException e){
					result.addError(
							true,
							"dayOfWeek",
							"Day Of Week should be number");					
				}
				result.addError(
						dayOfWeek>7 || dayOfWeek<1,
						"dayOfWeek",
						"Day Of Week should be between 1 to 7");
				 
			}
			
			if(result.isFailure()){
				return null;
			}
			
			Date startDate  = dateFormat.parse(startTimeStr);
			Date endDate  = endDateFormat.parse(endTimeStr+" 11:59 PM");
			
			LOGGER.debug("startDate"+startDate);
			LOGGER.debug("endDate"+endDate);
			
			if(endDate.before(startDate)){
				LOGGER.debug("endDate.before(startDate) :"+endDate.before(startDate));
				result.addError(
						true,
						"endDate",
						"End Date cannot be less than the Start Date");
				return null;
			}
			if (EnumDlvRestrictionReason.ALCOHOL.equals(reason) || EnumDlvRestrictionReason.WINE.equals(reason) || EnumDlvRestrictionReason.BEER.equals(reason)) {
				String state=request.getParameter("state");
				String county=request.getParameter("county");
				MunicipalityInfoWrapper muni = FDDeliveryManager.getInstance().getMunicipalityInfos(true);
				MunicipalityInfo muniInfo = muni.getMunicipalityInfo(state, county, null);
				
				String municipalityId=muniInfo.getId();
				boolean alcoholRestricted = Boolean.getBoolean(request.getParameter("restricted"));
				
				//Create a Alcohol Restriction.
				restriction=new AlcoholRestriction(id, criterion, reason, name, message, startDate, endDate,restrictedType, path, state, county, null, municipalityId, alcoholRestricted);
				validateAndSetTimeRanges(request, (AlcoholRestriction)restriction, result);
			} else if (EnumDlvRestrictionType.ONE_TIME_RESTRICTION.equals(restrictedType)) {
				
				// FIXME one-time reverse restrictions should have a different EnumDlvRestrictionType 
				if (reason.isSpecialHoliday()) {
					restriction=new OneTimeReverseRestriction(id,criterion, reason, name, message, startDate, endDate,path);
				} else {
					restriction=new OneTimeRestriction(id,criterion, reason, name, message, startDate, endDate,path);
				}

			} else if (EnumDlvRestrictionType.RECURRING_RESTRICTION.equals(restrictedType)) {

				TimeOfDay startTime = new TimeOfDay(startDate);
				TimeOfDay endTime = new TimeOfDay(endDate);
				// round up 11:59 to next midnight
				if (JUST_BEFORE_MIDNIGHT.equals(endTime)) {
					endTime = TimeOfDay.NEXT_MIDNIGHT;
				}
				restriction=new RecurringRestriction(id,criterion, reason, name, message, dayOfWeek, startTime, endTime,path);
			} 
			
		}catch (Exception pe) {
			throw new FDResourceException(pe);
		}
		return restriction;
	}
	
	private void validateAndSetTimeRanges(HttpServletRequest request, AlcoholRestriction restriction, ActionResult actionResult) {
		RestrictionWeekDay weekNames[] = RestrictionWeekDay.values();

		Map<Integer, List<TimeOfDayRange>> timeRangeMap = new HashMap<Integer, List<TimeOfDayRange>>();
		for (int i = 0; i < weekNames.length; i++) {
			String daySelected = request.getParameter("edit_dlvreq_chk"+weekNames[i].name());
			if(null !=daySelected){{
				
			}
				List<TimeOfDayRange> dlvTimeSlots = new ArrayList<TimeOfDayRange>();
				String dlvTimeSlotsLength = NVL.apply(request.getParameter("dlvDay"+weekNames[i].name()+"IndexValue"),"");
				if("".equalsIgnoreCase(dlvTimeSlotsLength)){
					actionResult.addError(true,"timeslotError","Enter atleast one timeslot for selected day(s) of the week");
				}
				if(!"".equalsIgnoreCase(dlvTimeSlotsLength)){
					for(int j=0;j<Integer.parseInt(dlvTimeSlotsLength);j++){
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
								TimeOfDay startTimeofDay = new TimeOfDay(startTime);
								TimeOfDay endTimeofDay = new TimeOfDay(endTime);
								dlvTimeSlots.add(new TimeOfDayRange(startTimeofDay, endTimeofDay));
							} catch (ParseException e) {
								actionResult.addError(true,"timeslotFormatError","One or more of the timeslots are in wrong format. It should be in 'hh:mm am/pm' format");
							}

						} else {
							actionResult.addError(true,"timeslotError","Enter atleast one timeslot for selected day(s) of the week");
						}
					}
				}
				timeRangeMap.put(new Integer(i), dlvTimeSlots);
			}				 
		}
		if(restriction.getType().getName().equals(EnumDlvRestrictionType.RECURRING_RESTRICTION.getName())){
			if(timeRangeMap.isEmpty())
				actionResult.addError(true,"timeslotError","For recurring restrictions select atleast one day of the week with one timeslot.");
		}
		restriction.setTimeRangeMap(timeRangeMap);

	}
	private int doDeleteReservations(HttpServletRequest request, GenericSearchCriteria criteria, List resvList) throws FDResourceException {
		HttpSession session = pageContext.getSession();
		CrmAgentModel agentModel = CrmSession.getCurrentAgent(session);
		String notes = request.getParameter("notes");
		int updateCount = CallCenterServices.cancelReservations(criteria, agentModel.getUserId(), notes);
		return updateCount;
	}

	private void doCancelOrders(HttpServletRequest request, List cancelOrders, ActionResult actionResult) throws FDResourceException {
		HttpSession session = pageContext.getSession();
		String notes = request.getParameter("notes");
		FDActionInfo info = AccountActivityUtil.getActionInfo(session, notes);
		String value = request.getParameter("sendEmail");
		value = value != null ? value.trim() : "";
		boolean sendEmail =  false;
		if(value != null && value.equalsIgnoreCase("true")){
			sendEmail =  true;
		}
		//Initiate Mass Cancellation.
		Map results = FDCustomerManager.cancelOrders(info, cancelOrders, sendEmail);
		List successList = (List)results.get("SUCCESS_ORDERS");
		List failureList = (List)results.get("FAILURE_ORDERS");
		actionResult.addWarning(new ActionWarning("cancelsuccess", successList.size()+" Order(s) Cancelled. "+failureList.size()+
				" Order(s) Failed."));

	}
	private void doBulkModifyOrders(HttpServletRequest request, List<FDCustomerOrderInfo> modifyOrders, ActionResult actionResult) throws FDResourceException {
		HttpSession session = pageContext.getSession();
		Set<String> skuCodes = getSet(request.getParameter("skuCodes").trim());
		String value = request.getParameter("sendEmail");
		value = value != null ? value.trim() : "";
		boolean sendEmail =  false;
		if(value != null && value.equalsIgnoreCase("true")){
			sendEmail =  true;
		}
		int failedCount = 0;
		int successCount = 0;
		int noMatchCount = 0;
		for(Iterator<FDCustomerOrderInfo> it = modifyOrders.iterator(); it.hasNext();){
			FDCustomerOrderInfo custOrderinfo = it.next();
			int success = bulkModifyOrder(request, custOrderinfo.getIdentity(), custOrderinfo.getSaleId(), skuCodes, sendEmail);
			switch(success) {
				case 1 : successCount++;
						break;
				case -1 : failedCount++;
						 break;
				default:  noMatchCount++;
			}
		}
		if(noMatchCount == modifyOrders.size()){
			actionResult.addWarning(new ActionWarning("modifysuccess", "Zero Orders found in the batch with matching SkuCode. Please Check the SkuCodes."));
			return;
		}
		if(successCount > 0 && failedCount == 0){
			actionResult.addWarning(new ActionWarning("modifysuccess", "All Order(s) were processed in the batch. There were no failures. "));
		} else {
			actionResult.addWarning(new ActionWarning("modifysuccess", "All Order(s) were processed in the batch. Click on Export Failed Orders Button to view them."));
		}
	}
	
	private Set<String> getSet(String values){
		StringTokenizer tokenizer = new StringTokenizer(values, ",");
		Set<String> s =  new HashSet<String>();
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			if(token != null){
				s.add(token.trim());
			}
		}
		return s;
	}
	
	
	private void doReturnOrders(HttpServletRequest request, List returnOrders, ActionResult actionResult) throws FDResourceException {
		HttpSession session = pageContext.getSession();
		String notes = request.getParameter("notes");
		FDActionInfo info = AccountActivityUtil.getActionInfo(session, notes);

		//Initiate Mass Returns.
		Map results = CallCenterServices.returnOrders(info, returnOrders);
		List successList = (List)results.get("SUCCESS_ORDERS");
		List failureList = (List)results.get("FAILURE_ORDERS");
		actionResult.addWarning(new ActionWarning("returnsuccess", successList.size()+" Order(s) Returned. "+failureList.size()+
				" Order(s) Failed."));
		
	}
	
	private List<FDCustomerOrderInfo> buildCustomerOrdersFromRequest(HttpServletRequest request, int limit){
		List<FDCustomerOrderInfo> custOrders = new ArrayList<FDCustomerOrderInfo>();
		String[] saleIds = request.getParameterValues("saleIds");
		String[] custIds = request.getParameterValues("custIds");
		String[] fdCustIds = request.getParameterValues("fdCustIds");
		
		if(saleIds.length < limit){
			limit = saleIds.length;
		}
		if(fdCustIds != null && fdCustIds.length > 0) {
			for(int i =0; i< limit; i++){
				FDCustomerOrderInfo orderInfo = new FDCustomerOrderInfo();
				FDIdentity identity = new FDIdentity(custIds[i], fdCustIds[i]);
				orderInfo.setIdentity(identity);
				orderInfo.setSaleId(saleIds[i]);
				custOrders.add(orderInfo);
			}
		} else {
			for(int i =0; i< limit; i++){
				FDCustomerOrderInfo orderInfo = new FDCustomerOrderInfo();
				FDIdentity identity = new FDIdentity(custIds[i]);
				orderInfo.setIdentity(identity);
				orderInfo.setSaleId(saleIds[i]);
				custOrders.add(orderInfo);
			}
		}
		return custOrders;
	}

	protected int bulkModifyOrder(HttpServletRequest request, FDIdentity identity, String orderId, Set<String> skuCodes, boolean sendEmail) {
		HttpSession session = request.getSession();
		Date snapShotCreateTime = (Date) session.getAttribute("SnapShotCreateTime");
			try {
				FDOrderAdapter originalOrder = (FDOrderAdapter) FDCustomerManager.getOrder(orderId );
				if(snapShotCreateTime != null && originalOrder.getLastModifiedDate().after(snapShotCreateTime)) {
					//Customer modified the order after snapshot was created. Do not attempt to modify the order.
					try {
						CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", "Customer modified the order " +
																								"after SnapShot was Created.");
					}catch(FDResourceException fe){
						//ignore
					}
					return -1;
				}
				//
				// Get ModifyCart model
				//
				FDModifyCartModel modCart = new FDModifyCartModel(originalOrder);
				//remove old sku and add new sku.
				boolean modified = false;
				String failureMessage = null;
				List<FDCartLineI> orderLines = modCart.getOrderLines();
				for(int index=0; index<orderLines.size();index++){
					FDCartLineI oldCartLine = orderLines.get(index);
					String skuCode = oldCartLine.getSkuCode();
					if(oldCartLine instanceof FDModifyCartLineI && skuCodes.contains(skuCode)) {
						try {
							//it.remove();
							FDSku oldSku = oldCartLine.getSku();
							FDProductInfo pInfo = FDCachedFactory.getProductInfo(skuCode);
							if(oldSku.getVersion() == pInfo.getVersion()){
								//versions are still same. need to call second time if the cache is not refreshed yet.
								pInfo = FDCachedFactory.getProductInfo(skuCode);
							}
							
							if(oldSku.getVersion() == pInfo.getVersion()){
								//Not yet refreshed. Log it as a failure and retry later.
								failureMessage = skuCode+" : "+"No Version Change. Possible Cause: ProductInfo Cache is still holding the old version. Please try again.";
								break;
							}

							if(pInfo.isDiscontinued() || pInfo.isTempUnavailable() || pInfo.isOutOfSeason()){
								//SKu is Unavailable. Log it as a failure.
								failureMessage = skuCode+" : "+"SKU is Unavailable to process. Please check with SAP.";
								break;
								
							}
							modCart.removeOrderLineById(oldCartLine.getRandomId());
							FDSku newSku = new FDSku(pInfo.getSkuCode(), pInfo.getVersion());
							FDConfigurableI oldConfig = oldCartLine.getConfiguration();
							FDProduct product = FDCachedFactory.getProduct(newSku);
							Map<String, String> newOptions = getNewOptions(oldConfig.getOptions(), product);
							FDConfigurableI newConfig = new FDConfiguration(oldCartLine.getQuantity(), oldCartLine.getSalesUnit(), newOptions);
							FDCartLineI newCartLine = new FDCartLineModel(newSku, oldCartLine.getProductRef().lookupProductModel(), newConfig
									, oldCartLine.getVariantId(), 
									oldCartLine.getPricingContext().getZoneId());
							//Apply Line Item discount if Applicable.
							Discount d = oldCartLine.getDiscount();
							if( d != null && !(d.getDiscountType().isSample())) {
								newCartLine.setDiscount(d);
								newCartLine.setDiscountFlag(true);
							}
							if(oldCartLine.getSavingsId() != null)
								newCartLine.setSavingsId(oldCartLine.getSavingsId());			

							modCart.addOrderLine(newCartLine);
							modified = true;
						}catch(FDSkuNotFoundException se){
							LOGGER.error("Sku Not Found : "+skuCode);
						}
						
					}
				}
				if(failureMessage != null && failureMessage.length() > 0){
					try {
						CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", failureMessage);
					}catch(FDResourceException fe){
						//ignore
					}
					return -1;
				}
				if(!modified){
					try {
						CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", skuCodes.toString() + ": No Matching Sku Found in the Order");
					}catch(FDResourceException fe){
						//ignore
					}
					return 0;
				}
				//Add Sample Line items if Any from Original order if any as we don't load in the Modify Cart model.
				List<FDCartLineI> sampleLines = modCart.getOriginalOrder().getSampleLines();
				if ( sampleLines != null && sampleLines.size() > 0) {
					for ( FDCartLineI oldSampleLine : sampleLines ) {
						FDCartLineI newSampleLine =  new FDCartLineModel(oldSampleLine);
						newSampleLine.setDiscount(oldSampleLine.getDiscount());
						newSampleLine.setDiscountFlag(true);
						modCart.addSampleLine(newSampleLine);
					}
				}

				modCart.refreshAll(true);
				modCart.recalculateTaxAndBottleDeposit(modCart.getDeliveryAddress().getZipCode());
	        	//CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
	        	Set<String> appliedPromos = originalOrder.getUsedPromotionCodes();
	        	for(Iterator<ErpDiscountLineModel> it = originalOrder.getDiscounts().iterator();it.hasNext();){
	        		ErpDiscountLineModel model = it.next();
	        		ErpDiscountLineModel newModel = new ErpDiscountLineModel(model);
	        		modCart.addDiscount(newModel);
	        	}
	        	modCart.setSelectedGiftCards(ErpGiftCardUtil.getGiftcardPaymentMethods(originalOrder.getAppliedGiftCards()));
	        	FDCustomerCreditUtil.applyCustomerCredit(modCart,identity);
	        	modCart.setDlvPassApplied(originalOrder.isDlvPassApplied());
	        	for(Iterator<String> it = appliedPromos.iterator(); it.hasNext();){
	        		Promotion promotion = (Promotion)PromotionFactory.getInstance().getPromotion(it.next());
	        		if(promotion.isWaiveCharge()){
	        			modCart.setDlvPromotionApplied(true);
	        			break;
	        		}
	        		if(promotion.isExtendDeliveryPass()) {
	        			ExtendDeliveryPassApplicator app = (ExtendDeliveryPassApplicator)promotion.getApplicator();
	        			modCart.setDlvPassExtn(new ExtendDPDiscountModel(promotion.getPromotionCode(), app.getExtendDays()));
	        			break;
	        		}
	        	}
	        	//Set Delivery Region Info
	        	FDTimeslot selectedTimeslot = modCart.getDeliveryReservation().getTimeslot();
	        	DlvZoneInfoModel zInfo = FDDeliveryManager.getInstance().getZoneInfo(modCart.getDeliveryAddress(), selectedTimeslot.getBegDateTime());
	        	if(zInfo != null){
		        	DlvZoneInfoModel zoneInfo = new DlvZoneInfoModel(originalOrder.getDeliveryZone(), null, zInfo.getRegionId(), EnumZipCheckResponses.DELIVER,false,false);
		        	modCart.setZoneInfo(zoneInfo);
	        	}
	        	FDActionInfo info = AccountActivityUtil.getActionInfo(session);
	        	info.setIdentity(identity);
	        	info.setSource(EnumTransactionSource.SYSTEM);
				FDCustomerManager.bulkModifyOrder(identity, info, modCart, appliedPromos, sendEmail);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Completed", "");
				}catch(FDResourceException fe){
					//ignore
				}
				return 1;
			} catch (FDInvalidAddressException ex) {
				LOGGER.warn("Invalid delivery Address ", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}
				return -1;
			} catch (ErpFraudException ex) {
				LOGGER.warn("Possible fraud occured", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}
				return -1;
			} catch (ErpAuthorizationException ex) {
				LOGGER.warn("Authorization failed", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}
				return -1;				
		
			} catch (FDPaymentInadequateException ex) {
				LOGGER.error("Payment Inadequate to process the ReAuthorization", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}

				return -1;
			} catch (ErpTransactionException ex) {
				LOGGER.error("Current sale status incompatible with requested action", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}

				return -1;
			} catch (FDInvalidConfigurationException ex) {
				LOGGER.error("FDInvalidConfigurationException occured in bulkModifyOrder.", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}
				
			} catch (FDResourceException ex) {
				LOGGER.error("FDResourceException while attempting to perform bulkModifyOrder.", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}
				return -1;
			} catch(DeliveryPassException ex) {
				LOGGER.error("Error performing a Delivery pass operation. ", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}
				return -1;
			}
			catch(ErpAddressVerificationException ex){
				LOGGER.error("Error performing a modify order operation. ", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}
				return -1;						
			}catch(InvalidCardException ex){
				LOGGER.error("Error performing a modify order operation. ", ex);
				try {
					CallCenterServices.updateOrderModifiedStatus(orderId, "Failed", ex.getMessage());
				}catch(FDResourceException fe){
					//ignore
				}
				return -1;						
			}
			return -1;		
	}
	private Map<String, String> getNewOptions(Map<String,String> oldOptions, FDProduct product) throws FDInvalidConfigurationException{
		Map<String, String> newOptions = new HashMap<String, String>();

		FDVariation[] variations = product.getVariations();
		for (int i = 0; i < variations.length; i++) {
			FDVariation variation = variations[i];
			String userOption = null;
			if(oldOptions != null && ! oldOptions.isEmpty())
				userOption = oldOptions.get(variation.getName());
			if(userOption != null){
				newOptions.put(variation.getName(), userOption);
			} else {
				//New options added during new SAP export. 
				FDVariationOption[] options = variation.getVariationOptions();
				if (options.length == 1) {
					//
					// there's only a single option, pick that
					//
					newOptions.put(variation.getName(), options[0].getName());

				}else if (variation.isOptional()) {
					//
					// More than one option. pick the SELECTED option for them.
					//
					String selected = null;
					for (int j = 0; j < options.length; j++) {
						if (options[j].isSelected())
							selected = options[j].getName();
					}
					newOptions.put(variation.getName(), selected);
				}else{
					//It needs user input.
					throw new FDInvalidConfigurationException("SKU configuration for : "+product.getSkuCode()+" has changed which needs user intervention.");
				}
			}
			
		}
		return newOptions;
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = request.getParameter("action");
		try{
	
			if("getAlcoholRestriction".equals(actionName)){
				String restrictionId = request.getParameter("restrictionId");
				String municipalityId = request.getParameter("municipalityId");
				if(restrictionId != null && restrictionId.length() > 0){
					AlcoholRestriction restriction = DlvRestrictionManager.getAlcoholRestriction(restrictionId, municipalityId);
					request.setAttribute("restriction", restriction);
				}
			}
		}catch(FDResourceException e){
			LOGGER.error("System Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		}catch(Exception e){
			LOGGER.error("Unknown Error occurred while performing the requested operation. Action name is "+actionName, e);
			actionResult.addError(true, "actionfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		}
		return true;
	}
//	private void addToResultsList(HttpSession session, Map subResults){
//		Map results = (Map)session.getAttribute("EXPORT_RESULTS");
//		if(results == null){
//			results = new HashMap();
//		}
//		List successOrders = (List)results.get("SUCCESS_ORDERS");
//		if(successOrders == null){
//			successOrders = new ArrayList();
//		}
//		List subList = (List)subResults.get("SUCCESS_ORDERS");
//		successOrders.addAll(subList);
//		LOGGER.info(successOrders.size()+" Order(s) were processed successfully.");
//		results.put("SUCCESS_ORDERS", successOrders);
//		List failureOrders = (List)results.get("FAILURE_ORDERS");
//		if(failureOrders == null){
//			failureOrders = new ArrayList();
//		}
//		List subList1 = (List)subResults.get("FAILURE_ORDERS");
//		failureOrders.addAll(subList1);
//		LOGGER.info(failureOrders.size()+" Order(s) failed.");
//		results.put("FAILURE_ORDERS", failureOrders);
//		//Set the results to the Request object.
//		session.setAttribute("EXPORT_RESULTS", results);
// 	}

	private boolean validateSkuCodes(HttpServletRequest request, ActionResult actionResult) throws ParseException {
		String skuCodes = NVL.apply(request.getParameter("skuCodes"), "").trim();
		if(skuCodes == null || skuCodes.length() == 0){
			actionResult.addError(true, "inputerror", "Enter at least one valid Sku Code.");
			return false;
		}
		return true;
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
	
	
}
