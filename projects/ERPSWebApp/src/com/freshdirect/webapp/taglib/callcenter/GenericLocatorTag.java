 package com.freshdirect.webapp.taglib.callcenter;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.BulkModifyOrderInfo;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.temails.ejb.TEmailInfoHome;
import com.freshdirect.fdstore.temails.ejb.TEmailInfoSB;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.EnumSearchType;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.temails.TEmailRuntimeException;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class GenericLocatorTag extends AbstractControllerTag {
	
	private static Category LOGGER = LoggerFactory.getInstance(GenericLocatorTag.class);
	//private GenericSearchCriteria criteria;
	private String searchParam;
	private String id;
	private static  final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static  final DateFormat timeFormat = new SimpleDateFormat("HH:mm a");
	private final static ServiceLocator LOCATOR = new ServiceLocator();
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		List searchResults = null;
		try {
			EnumSearchType searchType = EnumSearchType.getEnum(searchParam);
			HttpSession session = pageContext.getSession();
			if(searchType == null){
				LOGGER.error("Invalid Search Type passed from the JSP page - GenericLocator Tag.");
				actionResult.addError(true, "inputerror", SystemMessageList.MSG_TECHNICAL_ERROR);
				return true;
			}
			if(EnumSearchType.COMPANY_SEARCH.equals(searchType)){
				    searchResults = performCompanySearch(request);
			}
			else if(EnumSearchType.DEL_RESTRICTION_SEARCH.equals(searchType)){
				searchResults = performDlvRestrictedSearch(request);
			}
			else if(EnumSearchType.ALCOHOL_RESTRICTION_SEARCH.equals(searchType)){
				searchResults = performAlcoholRestrictedSearch(request);
			}	
			else if(EnumSearchType.PLATTER_RESTRICTION_SEARCH.equals(searchType)){
				searchResults = performPlatterRestrictedSearch(request);
			}
			else if(EnumSearchType.ADDR_RESTRICTION_SEARCH.equals(searchType)){
				searchResults = performAddrRestrictedSearch(request);
			}
			else if(EnumSearchType.EXEC_SUMMARY_SEARCH.equals(searchType)){
				    String summaryDate = NVL.apply(request.getParameter("summaryDate"),"").trim();
				    if(!validateDateField(summaryDate, actionResult, "Summary Date")){
				    	return true;
				    }
				    Date summaryDateValue  = dateFormat.parse(summaryDate);
					searchResults = performOrderSummarySearch(summaryDateValue);
			}else if(EnumSearchType.RESERVATION_SEARCH.equals(searchType)){
				if(!validate(request, actionResult)){
					return true;
				}
			    searchResults = performSearch(request);
			    request.setAttribute("RESULT","success");
			}else if(EnumSearchType.ORDERS_BY_RESV_SEARCH.equals(searchType)){
				if(!validate(request, actionResult)){
					return true;
				}
			    searchResults = performSearch(request);
			}else if(EnumSearchType.BROKEN_ACCOUNT_SEARCH.equals(searchType)){
				GenericSearchCriteria criteria = new GenericSearchCriteria(EnumSearchType.BROKEN_ACCOUNT_SEARCH);
				searchResults = CallCenterServices.doGenericSearch(criteria);
				
			}else if(EnumSearchType.CANCEL_ORDER_SEARCH.equals(searchType)){
				if(!validate(request, actionResult)){
					return true;
				}
			    searchResults = performSearch(request);
			    request.setAttribute("RESULT","success");
			}else if(EnumSearchType.RETURN_ORDER_SEARCH.equals(searchType)){
				if(!validate(request, actionResult)){
					return true;
				}
			    searchResults = performSearch(request);
			}else if(EnumSearchType.SETTLEMENT_BATCH_SEARCH.equals(searchType)){
				GenericSearchCriteria criteria = new GenericSearchCriteria(EnumSearchType.SETTLEMENT_BATCH_SEARCH);
				searchResults = CallCenterServices.doGenericSearch(criteria);

			}else if(EnumSearchType.ORDER_SEARCH_BY_SKUS.equals(searchType)){
				if(!validateSkuCodes(request, actionResult)){
					return true;
				}
			    searchResults = performSearch(request);
			    request.setAttribute("RESULT","success");
			}else if(EnumSearchType.GET_ORDERS_TO_MODIFY.equals(searchType)){
				GenericSearchCriteria criteria = new GenericSearchCriteria(searchType);
				criteria.setCriteriaMap("statuses", new String[]{"Pending", "Failed"});
			    searchResults = CallCenterServices.doGenericSearch(criteria);
			    if(searchResults != null && searchResults.size() > 0){
			    	BulkModifyOrderInfo info = (BulkModifyOrderInfo)searchResults.get(0);
			    	if(info != null){
			    		Date snapShotCreateTime = info.getCreateDate();
			    		session.setAttribute("SnapShotCreateTime", snapShotCreateTime);
			    	}
			    }
			    request.setAttribute("RESULT","success");
			} else if(EnumSearchType.TEMAILS_BATCH_SEARCH.equals(searchType)){
				
				try {
					TEmailInfoHome home= getTMailerHome();
					TEmailInfoSB remote= home.create();			
					searchResults=remote.getFailedTransactionList(100,false);
					if(searchResults!=null && searchResults.size()>0){
						request.setAttribute("trans_error", remote.getFailedTransactionStats());
					}
				} catch (CreateException ce) {
					//throw new FDResourceException(ce, "Cannot create MailerGatewayBean");
					actionResult.addError(true, "searchfailure", ce.getMessage());
				} catch (RemoteException re) {
					//throw new FDResourceException(re, re.getMessage());
					actionResult.addError(true, "searchfailure", re.getMessage());
				}
				catch(TEmailRuntimeException e){									
					actionResult.addError(true, "searchfailure", e.getMessage());
				}
				

			}
			pageContext.setAttribute(this.id, searchResults != null ? searchResults : Collections.EMPTY_LIST);
		} catch(FDResourceException e){
			LOGGER.error("System Error occurred while performing the search.", e);
			actionResult.addError(true, "searchfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		}catch(Exception e){
			LOGGER.error("Unknown Error occurred while performing the search.", e);
			actionResult.addError(true, "searchfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		}
		return true;
	}

	private List performSearch(HttpServletRequest request) throws FDResourceException {
		List searchResults = null;
		GenericSearchCriteria criteria = buildCriteria(request);
		searchResults = CallCenterServices.doGenericSearch(criteria);
		HttpSession session = pageContext.getSession();
		if(EnumSearchType.RESERVATION_SEARCH.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("RESV_SEARCH_CRITERIA", criteria);
		}
		if(EnumSearchType.ORDERS_BY_RESV_SEARCH.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("ORDERS_BY_RESV_CRITERIA", criteria);
		}
		if(EnumSearchType.CANCEL_ORDER_SEARCH.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("CANCEL_ORDERS_CRITERIA", criteria);
		}
		if(EnumSearchType.RETURN_ORDER_SEARCH.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("RETURN_ORDERS_CRITERIA", criteria);
		}
		if(EnumSearchType.ORDER_SEARCH_BY_SKUS.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("ORDER_SEARCH_BY_SKUS", criteria);
		}
		

		return searchResults;
	}
	
	private List performDlvRestrictedSearch(HttpServletRequest request) throws FDResourceException{
		
		List searchResults = null;
		GenericSearchCriteria criteria = buildRestrictedDeliveryCriteria(request);
		searchResults = CallCenterServices.doGenericSearch(criteria);
		HttpSession session = pageContext.getSession();
		if(EnumSearchType.DEL_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("DEL_RESTRICTION_CRITERIA", criteria);
		}
		return searchResults;
	}

	private List performAlcoholRestrictedSearch(HttpServletRequest request) throws FDResourceException{
		
		List searchResults = null;
		GenericSearchCriteria criteria = buildAlcoholRestrictionCriteria(request);
		searchResults = CallCenterServices.doGenericSearch(criteria);
		HttpSession session = pageContext.getSession();
		if(EnumSearchType.ALCOHOL_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("ALCOHOL_RESTRICTION_CRITERIA", criteria);
		}
		return searchResults;
	}

	private List performPlatterRestrictedSearch(HttpServletRequest request) throws FDResourceException{
		
		List searchResults = null;
		GenericSearchCriteria criteria = buildPlatterRestrictionCriteria(request);
		searchResults = CallCenterServices.doGenericSearch(criteria);
		HttpSession session = pageContext.getSession();
		if(EnumSearchType.PLATTER_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("PLATTER_RESTRICTION_SEARCH", criteria);
		}
		return searchResults;
	}

	private List performAddrRestrictedSearch(HttpServletRequest request) throws FDResourceException{
		
		List searchResults = null;
		GenericSearchCriteria criteria = buildRestrictedAddressCriteria(request);
		searchResults = CallCenterServices.doGenericSearch(criteria);
		HttpSession session = pageContext.getSession();
		if(EnumSearchType.ADDR_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
			//Cache the search criteria in session.
			session.setAttribute("DEL_RESTRICTION_CRITERIA", criteria);
		}
		return searchResults;
	}


	private GenericSearchCriteria buildRestrictedAddressCriteria(HttpServletRequest request) throws FDResourceException{
		GenericSearchCriteria criteria = null;
		try{
			    			
				String address1 = NVL.apply(request.getParameter("sAddress1"), "");
				String apartment = NVL.apply(request.getParameter("sApartment"), "");
				String zipCode = NVL.apply(request.getParameter("sZipCode"), "");
				String sortColumn = NVL.apply(request.getParameter("sortColumn"), "");
				String ascending = NVL.apply(request.getParameter("ascending"), "");
				
				LOGGER.debug("sortColumn :"+sortColumn);	
				
				criteria = new GenericSearchCriteria(EnumSearchType.getEnum(searchParam));
				criteria.setCriteriaMap("address1", address1);
				criteria.setCriteriaMap("apartment", apartment);
				criteria.setCriteriaMap("zipCode", zipCode);
				criteria.setCriteriaMap("sortColumn", sortColumn);			
				criteria.setCriteriaMap("ascending", ascending);
				String reasonCode = NVL.apply(request.getParameter("sReason"), "");
				EnumRestrictedAddressReason reason=EnumRestrictedAddressReason.getRestrictionReason(reasonCode);
				criteria.setCriteriaMap("reason",reason);
							
		}catch (Exception pe) {
			throw new FDResourceException(pe);
		}
		return criteria;
}
	
	private GenericSearchCriteria buildRestrictedDeliveryCriteria(HttpServletRequest request) throws FDResourceException{
		GenericSearchCriteria criteria = null;
		try{
				String sDate = NVL.apply(request.getParameter("startDate"),"").trim();							    				
				String message = NVL.apply(request.getParameter("message"), "");
				String reasonCode = NVL.apply(request.getParameter("reason"), "");
				String restrictedTypeCode = NVL.apply(request.getParameter("restrictedType"), "");
				String sortColumn = NVL.apply(request.getParameter("sortColumn"), "start_time");
				String ascending = NVL.apply(request.getParameter("ascending"), "asc");
				criteria = new GenericSearchCriteria(EnumSearchType.getEnum(searchParam));
				
				Date startDate  = dateFormat.parse(sDate);
				criteria.setCriteriaMap("startDate", startDate);				
				if(message.length() > 0){
					criteria.setCriteriaMap("message", message);	
				}				
				LOGGER.debug("message :"+message);								
				EnumDlvRestrictionReason reason=EnumDlvRestrictionReason.getEnum(reasonCode);
				criteria.setCriteriaMap("reason",reason);				
				LOGGER.debug("reasonCode :"+reasonCode+"adasd");								
				EnumDlvRestrictionType type=EnumDlvRestrictionType.getEnum(restrictedTypeCode);
				criteria.setCriteriaMap("type",type);				
				LOGGER.debug("type :"+type);							
				criteria.setCriteriaMap("sortColumn", sortColumn);			
				criteria.setCriteriaMap("ascending", ascending);				
		}catch (Exception pe) {
			throw new FDResourceException(pe);
		}
		return criteria;
	}

	private GenericSearchCriteria buildAlcoholRestrictionCriteria(HttpServletRequest request) throws FDResourceException{
		GenericSearchCriteria criteria = null;
		try{
				String state = NVL.apply(request.getParameter("state"),"").trim();							    				
				String county = NVL.apply(request.getParameter("county"), "");
				String reasonCode = NVL.apply(request.getParameter("reason"), "");
				String restrictedTypeCode = NVL.apply(request.getParameter("restrictedType"), "");
				String sortColumn = NVL.apply(request.getParameter("sortColumn"), "");
				String ascending = NVL.apply(request.getParameter("ascending"), "");
				criteria = new GenericSearchCriteria(EnumSearchType.getEnum(searchParam));
				
				LOGGER.debug("state :"+state);								
				criteria.setCriteriaMap("state", state);
				LOGGER.debug("county :"+county);								
				criteria.setCriteriaMap("county", county);
				EnumDlvRestrictionReason reason=EnumDlvRestrictionReason.getEnum(reasonCode);
				criteria.setCriteriaMap("reason",reason);				
				LOGGER.debug("reasonCode :"+reasonCode+"adasd");								
				EnumDlvRestrictionType type=EnumDlvRestrictionType.getEnum(restrictedTypeCode);
				criteria.setCriteriaMap("type",type);				
				LOGGER.debug("type :"+type);							
				criteria.setCriteriaMap("sortColumn", sortColumn);			
				criteria.setCriteriaMap("ascending", ascending);				
		}catch (Exception pe) {
			throw new FDResourceException(pe);
		}
		return criteria;
	}

	private GenericSearchCriteria buildPlatterRestrictionCriteria(HttpServletRequest request) throws FDResourceException{
		GenericSearchCriteria criteria = null;
		try{
			String sDate = NVL.apply(request.getParameter("startDate"),"").trim();							    				
			String message = NVL.apply(request.getParameter("message"), "");
			String reasonCode = NVL.apply(request.getParameter("reason"), "");
			String restrictedTypeCode = NVL.apply(request.getParameter("restrictedType"), "");
			String sortColumn = NVL.apply(request.getParameter("sortColumn"), "start_time");
			String ascending = NVL.apply(request.getParameter("ascending"), "asc");
			criteria = new GenericSearchCriteria(EnumSearchType.getEnum(searchParam));
			
			Date startDate  = dateFormat.parse(sDate);
			criteria.setCriteriaMap("startDate", startDate);				
			if(message.length() > 0){
				criteria.setCriteriaMap("message", message);	
			}				
			LOGGER.debug("message :"+message);								
			EnumDlvRestrictionReason reason=EnumDlvRestrictionReason.getEnum(reasonCode);
			criteria.setCriteriaMap("reason",reason);				
			LOGGER.debug("reasonCode :"+reasonCode);								
			EnumDlvRestrictionType type=EnumDlvRestrictionType.getEnum(restrictedTypeCode);
			criteria.setCriteriaMap("type",type);				
			LOGGER.debug("type :"+type);							
			criteria.setCriteriaMap("sortColumn", sortColumn);			
			criteria.setCriteriaMap("ascending", ascending);					
		}catch (Exception pe) {
			throw new FDResourceException(pe);
		}
		return criteria;
	}
	private GenericSearchCriteria buildCriteria(HttpServletRequest request) throws FDResourceException{
		GenericSearchCriteria criteria = new GenericSearchCriteria(EnumSearchType.getEnum(searchParam));
		try{
				String deliveryDate = NVL.apply(request.getParameter("deliveryDate"),"").trim();
				if(deliveryDate != null && deliveryDate.length() > 0){
					Date dlvDate  = dateFormat.parse(deliveryDate);
					criteria.setCriteriaMap("baseDate", dlvDate);
				}

				String toDeliveryDate = NVL.apply(request.getParameter("toDlvDate"),"").trim();
				if(toDeliveryDate != null && toDeliveryDate.length() > 0){
					Date toDlvDate  = dateFormat.parse(toDeliveryDate);
					criteria.setCriteriaMap("toBaseDate", toDlvDate);
				}

				String fromTimeSlot = NVL.apply(request.getParameter("fromTimeSlot"), "").trim();
				String toTimeSlot = NVL.apply(request.getParameter("toTimeSlot"), "").trim();
				
				String zones = NVL.apply(request.getParameter("zone"), "");
				String skuCodes = NVL.apply(request.getParameter("skuCodes"), "");

				if(zones.length() > 0){
					criteria.setCriteriaMap("zoneArray", getArray(zones));	
				}
				if(skuCodes.length() > 0){
					criteria.setCriteriaMap("skuArray", getArray(skuCodes));	
				}
				if(fromTimeSlot.length() > 0){
					String fromTimePeriod = request.getParameter("fromTimePeriod");
					fromTimeSlot = fromTimeSlot + " " + fromTimePeriod;
					criteria.setCriteriaMap("startTime", fromTimeSlot);	
				}
				if(toTimeSlot.length() > 0){
					String toTimePeriod = request.getParameter("toTimePeriod");
					toTimeSlot = toTimeSlot + " " + toTimePeriod;
					criteria.setCriteriaMap("endTime", toTimeSlot);	
				}
				if(EnumSearchType.RESERVATION_SEARCH.equals(criteria.getSearchType()) 
						|| EnumSearchType.CANCEL_ORDER_SEARCH.equals(criteria.getSearchType())
						|| EnumSearchType.ORDERS_BY_RESV_SEARCH.equals(criteria.getSearchType())) {
			
					String cutoffTime = request.getParameter("cutoffTime");
					if(!cutoffTime.equals("all")){
						criteria.setCriteriaMap("cutoffTime", cutoffTime);	
					}
				}
				if(EnumSearchType.RETURN_ORDER_SEARCH.equals(criteria.getSearchType())){
					String fromTruckNumber = NVL.apply(request.getParameter("fromTruckNumber"), "").trim();
					String toTruckNumber = NVL.apply(request.getParameter("toTruckNumber"), "").trim();
					if(fromTruckNumber.length() > 0){
						criteria.setCriteriaMap("fromTruckNumber", fromTruckNumber);	
					}
					if(toTruckNumber.length() > 0){
						criteria.setCriteriaMap("toTruckNumber", toTruckNumber);	
					}
				}
		}catch (Exception pe) {
			throw new FDResourceException(pe);
		}
		return criteria;
	}
	
	private List performOrderSummarySearch(Date summaryDateValue) throws FDResourceException {
		List searchResults;
		GenericSearchCriteria criteria = new GenericSearchCriteria(EnumSearchType.EXEC_SUMMARY_SEARCH);
		criteria.setCriteriaMap("summaryDate", summaryDateValue);
		searchResults = CallCenterServices.orderSummarySearch(criteria);
		return searchResults;
	}

	private List performCompanySearch(HttpServletRequest request) throws FDResourceException {
		List searchResults;
		String companyName = request.getParameter("companyName");
		GenericSearchCriteria criteria = new GenericSearchCriteria(EnumSearchType.COMPANY_SEARCH);
		criteria.setCriteriaMap("companyName", NVL.apply(companyName, "").trim().toLowerCase());
		searchResults = CallCenterServices.locateCompanyCustomers(criteria);
		return searchResults;
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = pageContext.getSession();
		EnumSearchType searchType = EnumSearchType.getEnum(searchParam);
		if(searchType == null){
			LOGGER.error("Invalid Search Type passed from the JSP page - GenericLocator Tag.");
			actionResult.addError(true, "inputerror", SystemMessageList.MSG_TECHNICAL_ERROR);
			return true;
		}
		//User navigated from a different page. So clean up the old session info.
		cleanUp(session, searchType);
		if(request.getParameter("method") != null && request.getParameter("method").equalsIgnoreCase("GET")){
			//perform action only if the page forces to do it. Otherwise perform action happens only when POST.
			performAction(request, actionResult);	
		}
		
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
			                            VariableInfo.NESTED),   
		        };

		    }
	}
	private boolean validateSkuCodes(HttpServletRequest request, ActionResult actionResult) throws ParseException {
		String skuCodes = NVL.apply(request.getParameter("skuCodes"), "").trim();
		if(skuCodes == null || skuCodes.length() == 0){
			actionResult.addError(true, "inputerror", "Enter at least one valid Sku Code.");
			return false;
		}
		return true;
	}
	
	private boolean validate(HttpServletRequest request, ActionResult actionResult) throws ParseException {
		String deliveryDate = NVL.apply(request.getParameter("deliveryDate"),"").trim();
	    if(!validateDateField(deliveryDate, actionResult, "Delivery Date")){
	    	return false;
	    }
	    
	    Date dlvDate  = dateFormat.parse(deliveryDate);
    
		String fromTimeSlot = NVL.apply(request.getParameter("fromTimeSlot"), "").trim();
		String toTimeSlot = NVL.apply(request.getParameter("toTimeSlot"), "").trim();
		
		if(fromTimeSlot.length() != 0){
			String fromTimePeriod = request.getParameter("fromTimePeriod");
			fromTimeSlot = fromTimeSlot + " " + fromTimePeriod;
		    if(!validateTimeField(fromTimeSlot, actionResult, "From TimeSlot")) {
		    	return false;
		    }
		}
		
		if(toTimeSlot.length() != 0){
			String toTimePeriod = request.getParameter("toTimePeriod");
			toTimeSlot = toTimeSlot + " " + toTimePeriod;
		    if(!validateTimeField(toTimeSlot, actionResult, "To TimeSlot")) {
		    	return false;
		    }
		}
		//Special Validation for Returns Search.
		if(EnumSearchType.RETURN_ORDER_SEARCH.getName().equals(searchParam)){
			String fromTruckNum = NVL.apply(request.getParameter("fromTruckNumber"),"").trim();
			String toTruckNum = NVL.apply(request.getParameter("toTruckNumber"),"").trim();
			
			boolean validateFromTruckNum = validateNumericField(fromTruckNum, actionResult, "From Truck Number");
			boolean validateToTruckNum = validateNumericField(toTruckNum, actionResult, "To Truck Number");
			if(!validateFromTruckNum || !validateToTruckNum){
				return false;
			}
			if(toTruckNum.length() != 0 && fromTruckNum.length() == 0){
				LOGGER.error("From Truck number cannot be blank in this case.");
				actionResult.addError(true, "inputerror", "\"From\" Truck Number cannot be blank in this case. Type in a valid Truck Number");
				return false;
			}
		}
		return true;
	}

	private boolean validateDateField(String dateValue, ActionResult actionResult, String dateType) {
	    if(dateValue.length() != 0){
	        try {
	        				        	
	        	dateFormat.parse(dateValue);
	        } catch (ParseException e) { 
				LOGGER.error("Invalid "+dateType);
				actionResult.addError(true, "inputerror", "Invalid "+dateType+". Please check the Date Format(yyyy-MM-dd) and retry.");
				return false;
	        }
	    }else {
			LOGGER.error(dateType+" is blank.");
			actionResult.addError(true, "inputerror", dateType+" cannot be left blank. Please enter a valid Date (yyyy-MM-dd) and retry.");
			return false;
	    }
	    return true;
	}
	
	private boolean validateTimeField(String timeValue, ActionResult actionResult, String timeType){
        try {
        				        	
        	timeFormat.parse(timeValue);
        } catch (ParseException e) { 
			LOGGER.error("Invalid "+timeType);
			actionResult.addError(true, "inputerror", "Invalid \""+timeType+"\". Please check the Time Format(HH:MI) and retry.");
			return false;
        }
	    return true;
	}

	private boolean validateNumericField(String numValue, ActionResult actionResult, String numType) {
        try {
        	if(numValue.length() == 0){
        		return true;
        	}
        	Integer.parseInt(numValue);
        } catch (NumberFormatException ne) { 
			LOGGER.error("Invalid "+numType);
			actionResult.addError(true, "inputerror", "Invalid \""+numType+"\". Please enter a numeric value and retry.");
			return false;
        }
	    return true;
	}
	private String[] getArray(String values){
		StringTokenizer tokenizer = new StringTokenizer(values, ",");
		String[] valueArray = new String[tokenizer.countTokens()];
		int index = 0;
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			valueArray[index] = token.trim();
			index++;
		}
		return valueArray;
	}
	
	public static List filterOrdersByResvType(List completeList, String filterType) {
		List filteredList = new ArrayList();
		Iterator iter = completeList.iterator();
		while(iter.hasNext()){
			FDCustomerOrderInfo info = (FDCustomerOrderInfo) iter.next();
			if(filterType.equals("STD") && info.getRsvType() == EnumReservationType.STANDARD_RESERVATION){
				//If filter type is standard.
				filteredList.add(info);
			}else if(filterType.equals("PRE") && info.getRsvType() != EnumReservationType.STANDARD_RESERVATION){
				//If filter type is Pre-reservation.
				filteredList.add(info);
			}
		}
		return filteredList;
		
	}
	
	private static TEmailInfoHome getTMailerHome() {
		try {
			return (TEmailInfoHome) LOCATOR.getRemoteHome("freshdirect.fdstore.TEmailInfoManager");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	private static void cleanUp(HttpSession session, EnumSearchType searchType) {
		session.removeAttribute("RESV_SEARCH_CRITERIA");
		session.removeAttribute("ORDERS_BY_RESV_CRITERIA");
		session.removeAttribute("FILTER_TYPE");
		session.removeAttribute("CANCEL_ORDERS_CRITERIA");
		session.removeAttribute("RETURN_ORDERS_CRITERIA");
		session.removeAttribute("DEL_RESTRICTION_CRITERIA");
	}
}
