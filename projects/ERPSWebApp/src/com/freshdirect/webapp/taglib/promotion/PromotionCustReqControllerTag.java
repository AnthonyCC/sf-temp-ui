package com.freshdirect.webapp.taglib.promotion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.delivery.EnumComparisionType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionSection;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustomerInfo;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromotionAttributeParam;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.NumberUtil;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class PromotionCustReqControllerTag extends AbstractControllerTag {

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
			if("promoCustReq".equalsIgnoreCase(this.getActionName())){
				String orderRangeStart = NVL.apply(request.getParameter("orderRangeStart"),"").trim();
				String orderRangeEnd = NVL.apply(request.getParameter("orderRangeEnd"),"").trim();
				String[] cohorts = request.getParameterValues("cohorts");
				String[] dpTypes = request.getParameterValues("dpTypes");
				String dpStatus = NVL.apply(request.getParameter("dpStatus"),"").trim();
				String dpExpStartDate = NVL.apply(request.getParameter("edit_custreq_cal_dlvPassStart_in"),"").trim();
				String dpExpEndDate = NVL.apply(request.getParameter("edit_custreq_cal_dlvPassEnd_in"),"").trim();
				List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
				String profileOperator = NVL.apply(request.getParameter("custreq_profileAndOr"),"").trim();
				promotion.setProfileOperator(profileOperator);
				FDPromoCustStrategyModel model = null;
				if(null != custStrategies && !custStrategies.isEmpty()){
					model = custStrategies.get(0);					
				}else{
					custStrategies = new ArrayList<FDPromoCustStrategyModel>();
					model = new FDPromoCustStrategyModel();
					model.setPromotionId(promotion.getId());
					custStrategies.add(model);
					promotion.setCustStrategies(custStrategies);
				}
				model.setCohorts(cohorts);
				model.setDpTypes(dpTypes);
				model.setDpStatus(dpStatus);
				validateCustReq(actionResult, orderRangeStart, orderRangeEnd,
						dpExpStartDate, dpExpEndDate, model);
				
				
				List attrParamList = getAttributeParamList(request);
				Collections.sort(attrParamList);
				promotion.setAttributeList(attrParamList);
				HttpSession session = pageContext.getSession();
				CrmAgentModel agent = CrmSession.getCurrentAgent(session);
				promotion.setModifiedBy(agent.getUserId());
				promotion.setModifiedDate(new Date());
				if("true".equals(request.getParameter("batch_promo"))) {
					promotion.setBatchPromo(true);
				}
				if(actionResult.isSuccess()){
					if(promotion.isBatchPromo()) {
						FDPromotionNewManager.storePromotionStatus(promotion,EnumPromotionStatus.PROGRESS,true);					
					}
					populatePromoChangeModel(model);
					FDPromotionNewManager.storePromotionCustReqInfo(promotion);
				}
				
			}else if("promoPayment".equalsIgnoreCase(this.getActionName())){
				String amex = NVL.apply(request.getParameter("amex"),"").trim();
				String masterCard = NVL.apply(request.getParameter("masterCard"),"").trim();
				String visa = NVL.apply(request.getParameter("visa"),"").trim();
				String discover = NVL.apply(request.getParameter("discover"),"").trim();
				String masterPass = NVL.apply(request.getParameter("masterPass"),"").trim();
				String payPal = NVL.apply(request.getParameter("payPal"),"").trim();
				String eCheck = NVL.apply(request.getParameter("eCheck"),"").trim();
				String debitCard = NVL.apply(request.getParameter("debitCard"),"").trim();
				String minOrders = NVL.apply(request.getParameter("minOrders"),"").trim();
				String eCheckMatch= NVL.apply(request.getParameter("eCheckMatchType"),"").trim();
				EnumComparisionType eCheckMatchType=EnumComparisionType.getEnum(eCheckMatch);
				
				StringBuffer paymentType = new StringBuffer();
				boolean isSelected = false;
				EnumCardType paymentTypes[] = new EnumCardType[]{null,null,null,null,null,null,null,null};
				if(!"".equalsIgnoreCase(amex)){
					paymentTypes[0]=EnumCardType.AMEX;
					isSelected = true;
				}
				if(!"".equalsIgnoreCase(masterCard)){
					if(isSelected){
						paymentType.append(",");
					}
					paymentTypes[1]=EnumCardType.MC;
					isSelected = true;
				}
				if(!"".equalsIgnoreCase(visa)){
					if(isSelected){
						paymentType.append(",");
					}
					paymentTypes[2]=EnumCardType.VISA;
					isSelected = true;
				}
				if(!"".equalsIgnoreCase(discover)){
					if(isSelected){
						paymentType.append(",");
					}
					paymentTypes[3]=EnumCardType.DISC;
					isSelected = true;
				}
				if(!"".equalsIgnoreCase(eCheck)){
					if(isSelected){
						paymentType.append(",");
					}
					paymentTypes[4]=EnumCardType.ECP;
					isSelected = true;
				}
				if(!"".equalsIgnoreCase(debitCard)){
					if(isSelected){
						paymentType.append(",");
					}
					paymentTypes[5]=EnumCardType.DEBIT;
					isSelected = true;
				}				
				if(!"".equalsIgnoreCase(masterPass)){
					if(isSelected){
						paymentType.append(",");
					}
					paymentTypes[6]=EnumCardType.MASTERPASS;
					isSelected = true;
				}
				if(!"".equalsIgnoreCase(payPal)){
					if(isSelected){
						paymentType.append(",");
					}
					paymentTypes[7]=EnumCardType.PAYPAL;
					isSelected = true;
				}
				
				List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
//				if(isSelected){
					FDPromoCustStrategyModel model = null;
					if(null != custStrategies && !custStrategies.isEmpty()){
						model = custStrategies.get(0);					
					}else{
						custStrategies = new ArrayList<FDPromoCustStrategyModel>();
						model = new FDPromoCustStrategyModel();						
						custStrategies.add(model);
						promotion.setCustStrategies(custStrategies);
					}
					model.setPaymentType(paymentTypes);
					model.setPromotionId(promotion.getId());
					if(!"".equalsIgnoreCase(eCheck)){
						model.setPriorEcheckUse(minOrders);
						if(!NumberUtil.isInteger(minOrders)){
							actionResult.addError(true, "minOrdersNumber", " Prior eCheck use value should be integer.");
						}
						model.setEcheckMatchType(eCheckMatchType);
						if(null ==eCheckMatchType){
							actionResult.addError(true, "eCheckMatchCondition", " Prior eCheck use, required matching condition should be selected.");
						}
					}else{
						model.setPriorEcheckUse("");
						model.setEcheckMatchType(null);
					}
					HttpSession session = pageContext.getSession();
					CrmAgentModel agent = CrmSession.getCurrentAgent(session);
					promotion.setModifiedBy(agent.getUserId());
					promotion.setModifiedDate(new Date());
					if("true".equals(request.getParameter("batch_promo"))) {
						promotion.setBatchPromo(true);
					}
					
					populatePromoChangeModel(model);
					if(actionResult.isSuccess()) {
						if(promotion.isBatchPromo()) {
							FDPromotionNewManager.storePromotionStatus(promotion,EnumPromotionStatus.PROGRESS,true);					
						}
						FDPromotionNewManager.storePromotionPaymentInfo(promotion);
					}
					
//				}
			}else if("searchCustomerRestriction".equalsIgnoreCase(this.getActionName())){
				String userId = request.getParameter("userId");
				boolean isAssigned = FDPromotionNewManager.isCustomerInAssignedList(userId, promotion.getId());
				request.setAttribute("IS_USER_ASSIGNED", Boolean.valueOf(isAssigned));
			}else if("updateCustomerRestriction".equalsIgnoreCase(this.getActionName())){
				String custIds = request.getParameter("assignedCustIds");
				if(null != custIds && !"".equals(custIds.trim())){
					promotion.setTmpAssignedCustomerUserIds(custIds);
					FDPromotionNewManager.storeAssignedCustomers(promotion, custIds);
					promotion.setTmpAssignedCustomerUserIds("");
					HttpSession session = pageContext.getSession();
					CrmAgentModel agent = CrmSession.getCurrentAgent(session);
					if(agent.getRole().getCode().equals("HR")) {
						setSuccessPage("/promotion/promo_hronly.jsp?promoId="+promotion.getPromotionCode()+"&SUCCESSCUST=true");
					} else {
						setSuccessPage("/promotion/promo_details.jsp?promoId="+promotion.getPromotionCode()+"&SUCCESSCUST=true");
					}
//					request.setAttribute("SUCCESS", true);
				}
			}
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
			request.setAttribute("custNotErr", true);
			return true;
		} catch(FinderException fe){
			throw new JspException(fe);
		}
		return true;
	}


	private void populatePromoChangeModel(FDPromoCustStrategyModel model) throws FDResourceException,
			FinderException {
		List newPaymentTypes = (null!= model.getPaymentType())?Arrays.asList(model.getPaymentType()):new ArrayList();
		List promoChanges = new ArrayList<FDPromoChangeModel>();
		List promoChangeDetails = new ArrayList<FDPromoChangeDetailModel>();
		FDPromoChangeModel changeModel = new FDPromoChangeModel();
		changeModel.setActionDate(promotion.getModifiedDate());
//		changeModel.setActionType(EnumPromoChangeType.MODIFY);
		changeModel.setUserId(promotion.getModifiedBy());
		changeModel.setPromotionId(promotion.getId());
		changeModel.setChangeDetails(promoChangeDetails);
		promoChanges.add(changeModel);
		promotion.setAuditChanges(promoChanges);
		
		if("promoPayment".equalsIgnoreCase(this.getActionName())){
			changeModel.setActionType(EnumPromoChangeType.PAYMENT_INFO);
		}else if("promoCustReq".equalsIgnoreCase(this.getActionName())){
			changeModel.setActionType(EnumPromoChangeType.CUST_REQ_INFO);
		}
		FDPromotionNewModel oldPromotion = FDPromotionNewManager.loadPromotion(promotion.getPromotionCode());
		if(null != oldPromotion && null != oldPromotion.getCustStrategies() && !oldPromotion.getCustStrategies().isEmpty()){
			
			FDPromoCustStrategyModel oldCustModel = (FDPromoCustStrategyModel)oldPromotion.getCustStrategies().get(0);
			if("promoPayment".equalsIgnoreCase(this.getActionName())){
				if(null != oldCustModel.getPaymentType()){
					List oldPaymentTypes = Arrays.asList(oldCustModel.getPaymentType());
					if(!newPaymentTypes.containsAll(oldPaymentTypes)){
						FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
						changeDetailModel.setChangeFieldName("Payment Type");
						changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPaymentTypes.toString(),"").trim());
						changeDetailModel.setChangeFieldNewValue(newPaymentTypes.toString());
						changeDetailModel.setChangeSectionId(EnumPromotionSection.PAYMENT_INFO);
						promoChangeDetails.add(changeDetailModel);
					}
				}
				if(!model.getPriorEcheckUse().equalsIgnoreCase(NVL.apply(oldCustModel.getPriorEcheckUse(),"").trim())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Prior Echeck Use");
					changeDetailModel.setChangeFieldOldValue(NVL.apply(oldCustModel.getPriorEcheckUse()," "));
					changeDetailModel.setChangeFieldNewValue(model.getPriorEcheckUse());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.PAYMENT_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				
				if(model.getEcheckMatchType()!=oldCustModel.getEcheckMatchType()){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Prior Echeck Match Type");
					changeDetailModel.setChangeFieldOldValue(null!=oldCustModel.getEcheckMatchType()?oldCustModel.getEcheckMatchType().getName():null);
					changeDetailModel.setChangeFieldNewValue(null!=model.getEcheckMatchType()?model.getEcheckMatchType().getName():null);
					changeDetailModel.setChangeSectionId(EnumPromotionSection.PAYMENT_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
			}else if("promoCustReq".equalsIgnoreCase(this.getActionName())){
				if(oldCustModel.getOrderRangeStart()!= model.getOrderRangeStart()){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Order Instance Start");
					changeDetailModel.setChangeFieldOldValue(String.valueOf(oldCustModel.getOrderRangeStart()));
					changeDetailModel.setChangeFieldNewValue(String.valueOf(model.getOrderRangeStart()));
					changeDetailModel.setChangeSectionId(EnumPromotionSection.CUST_REQ_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(oldCustModel.getOrderRangeEnd()!= model.getOrderRangeEnd()){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Order Instance End");
					changeDetailModel.setChangeFieldOldValue(String.valueOf(oldCustModel.getOrderRangeEnd()));
					changeDetailModel.setChangeFieldNewValue(String.valueOf(model.getOrderRangeEnd()));
					changeDetailModel.setChangeSectionId(EnumPromotionSection.CUST_REQ_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(oldCustModel.getCohorts()!= null && model.getCohorts()!=null){
					Arrays.sort(oldCustModel.getCohorts());
					List oldCohorts = Arrays.asList(oldCustModel.getCohorts());
					Arrays.sort(model.getCohorts());
					List cohorts = Arrays.asList(model.getCohorts());
					if(cohorts.size() != oldCohorts.size() || !cohorts.containsAll(oldCohorts) || oldCohorts.containsAll(cohorts)){
						FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
						changeDetailModel.setChangeFieldName("Cohorts");
						changeDetailModel.setChangeFieldOldValue(oldCohorts.toString());
						changeDetailModel.setChangeFieldNewValue(cohorts.toString());
						changeDetailModel.setChangeSectionId(EnumPromotionSection.CUST_REQ_INFO);
						promoChangeDetails.add(changeDetailModel);
					}
				}
				if(oldCustModel.getDpTypes()!= null && model.getDpTypes()!=null){
					Arrays.sort(oldCustModel.getDpTypes());
					List oldDpTypes = Arrays.asList(oldCustModel.getDpTypes());
					Arrays.sort(model.getDpTypes());
					List dpTypes = Arrays.asList(model.getDpTypes());
					if(dpTypes.size() != oldDpTypes.size() || !dpTypes.containsAll(dpTypes) || oldDpTypes.containsAll(dpTypes)){
						FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
						changeDetailModel.setChangeFieldName("Dp Types");
						changeDetailModel.setChangeFieldOldValue(oldDpTypes.toString());
						changeDetailModel.setChangeFieldNewValue(dpTypes.toString());
						changeDetailModel.setChangeSectionId(EnumPromotionSection.CUST_REQ_INFO);
						promoChangeDetails.add(changeDetailModel);
					}
				}
				if((null !=oldCustModel.getDpStatus() && null != model.getDpStatus() && !model.getDpStatus().equalsIgnoreCase(oldCustModel.getDpStatus()) )|| (null ==oldCustModel.getDpStatus()&& null !=model.getDpStatus()) || (null == model.getDpStatus() && null != oldCustModel.getDpStatus())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("DP Status");
					changeDetailModel.setChangeFieldOldValue(oldCustModel.getDpStatus());
					changeDetailModel.setChangeFieldNewValue(model.getDpStatus());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.CUST_REQ_INFO);
					promoChangeDetails.add(changeDetailModel);
					
				}
				if(null != oldCustModel.getDpExpStart() && null != model.getDpExpStart() && !model.getDpExpStart().equals(oldCustModel.getDpExpStart())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("DP Expiration Start Date");
					changeDetailModel.setChangeFieldOldValue(oldCustModel.getDpExpStart().toString());
					changeDetailModel.setChangeFieldNewValue(model.getDpExpStart().toString());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.CUST_REQ_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(null != oldCustModel.getDpExpEnd() && null != model.getDpExpEnd() && !model.getDpExpEnd().equals(oldCustModel.getDpExpEnd())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("DP Expiration End Date");
					changeDetailModel.setChangeFieldOldValue(oldCustModel.getDpExpEnd().toString());
					changeDetailModel.setChangeFieldNewValue(model.getDpExpEnd().toString());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.CUST_REQ_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(null != promotion.getProfileOperator() && null != oldPromotion.getProfileOperator() && !promotion.getProfileOperator().equalsIgnoreCase(oldPromotion.getProfileOperator())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Profile Operator");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getProfileOperator());
					changeDetailModel.setChangeFieldNewValue(promotion.getProfileOperator());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.CUST_REQ_INFO);
					promoChangeDetails.add(changeDetailModel);
				}				
			}
		}/*else{
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Payment Type");
			changeDetailModel.setChangeFieldOldValue(" ");
			changeDetailModel.setChangeFieldNewValue(newPaymentTypes.toString());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.PAYMENT_INFO);
			promoChangeDetails.add(changeDetailModel);
			changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Prior Echeck Use");
			changeDetailModel.setChangeFieldOldValue(" ");
			changeDetailModel.setChangeFieldNewValue(model.getPriorEcheckUse());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.PAYMENT_INFO);
			promoChangeDetails.add(changeDetailModel);
		}*/
	}


	private void validateCustReq(ActionResult actionResult,
			String orderRangeStart, String orderRangeEnd,
			String dpExpStartDate, String dpExpEndDate,
			FDPromoCustStrategyModel model) {
		
		try {
			if(!"".equalsIgnoreCase(dpExpStartDate)){			
				model.setDpExpStart(getDate(dpExpStartDate,"11:59:59 pm"));//DateUtil.parseMDY(dpExpStartDate));
			}else{
				model.setDpExpStart(null);
			}
		} catch (ParseException e) {
			model.setDpExpStart(null);
			actionResult.addError(true, "dpStartDateFormat", " Dlv Pass expiration start date '"+ dpExpStartDate+"' should be in 'mm/dd/yyyy' format.");
		}
		try {
			if(!"".equalsIgnoreCase(dpExpEndDate)){			
				model.setDpExpEnd(getDate(dpExpEndDate,"11:59:59 pm"));//DateUtil.parseMDY(dpExpEndDate));
			}else{
				model.setDpExpEnd(null);
			}
		} catch (ParseException e) {
			model.setDpExpEnd(null);
			actionResult.addError(true, "dpEndDateFormat", " Dlv Pass expiration end date '"+ dpExpEndDate+"' should be in 'mm/dd/yyyy' format.");
		}
		if(null !=model.getDpExpStart() && null != model.getDpExpEnd() && model.getDpExpStart().after(model.getDpExpEnd())){
			actionResult.addError(true, "dpDates", " Dlv Pass expiration end date should not be before start date.");
		} 
		
		if("".equals(orderRangeStart)){
			model.setOrderRangeStart(null);
		}else{
			if( !isInteger(orderRangeStart)){
				model.setOrderRangeStart(null);
				actionResult.addError(true, "orderRangesStart", " Order instance value '"+orderRangeStart +"' should be Number.");
			}else{
				model.setOrderRangeStart(Integer.parseInt(orderRangeStart));
			}
		}
		if("".equals(orderRangeEnd)){
			model.setOrderRangeEnd(null);
		}else{
			if(!isInteger(orderRangeEnd)){
				model.setOrderRangeEnd(null);
				actionResult.addError(true, "orderRangesStart", " Order instance value '"+orderRangeEnd +"' should be Number.");
			}else{
				model.setOrderRangeEnd(Integer.parseInt(orderRangeEnd));
			}
		}
		if(model.getOrderRangeEnd()!= null && model.getOrderRangeStart()!=null && model.getOrderRangeEnd()<model.getOrderRangeStart()){
			actionResult.addError(true, "orderRanges", " Order instance start value must be less than or equal to end value.");
		}
		/*if(!"".equals(orderRangeStart)){
			if( !isInteger(orderRangeStart)){
				actionResult.addError(true, "orderRangesStart", " Order instance "+orderRangeStart +" should be Number.");
			}else{
				model.setOrderRangeStart(Integer.parseInt(orderRangeStart));
			}
		}
		if(!"".equals(orderRangeEnd)){
			if(!isInteger(orderRangeEnd)){
				actionResult.addError(true, "orderRangesEnd", " Order instance "+orderRangeEnd +" should be Number.");
			}else{
				model.setOrderRangeEnd(Integer.parseInt(orderRangeEnd));
			}
		}
		if((!"".equals(orderRangeStart) && !"".equals(orderRangeEnd)) && Integer.parseInt(orderRangeEnd)<Integer.parseInt(orderRangeStart)){
			actionResult.addError(true, "orderRanges", " Order instance start value must be less than or equal to end value.");
		}*/
	}
	
	private List getAttributeParamList(HttpServletRequest request) {
		
		Enumeration paramNames = request.getParameterNames();
		
		List dataList = new ArrayList();
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
	
	private FDPromotionAttributeParam getAttributeParam(HttpServletRequest request, String index) {
		
		FDPromotionAttributeParam tmpParam = new FDPromotionAttributeParam();
		String key = "attributeList["+index+"].";
		tmpParam.setAttributeIndex(index);
		tmpParam.setAttributeName(request.getParameter(key+"attributeName"));
		tmpParam.setDesiredValue(request.getParameter(key+"desiredValue").trim());// Fix to trim invalid profiles with space.
		//tmpParam.setOperator(request.getParameter(key+"operator"));
		return tmpParam;			
	}
	private boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	private Date getDate(String dateStr, String time) throws ParseException{
				
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		Date date = sdf.parse(dateStr+" "+time);
		Calendar cal = DateUtil.toCalendar(date);				
		return cal.getTime();	
}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
	
	
}
