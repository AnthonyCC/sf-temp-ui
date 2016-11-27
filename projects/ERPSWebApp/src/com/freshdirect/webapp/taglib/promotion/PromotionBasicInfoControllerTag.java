package com.freshdirect.webapp.taglib.promotion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionSection;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.NumberUtil;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class PromotionBasicInfoControllerTag extends AbstractControllerTag {
	
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
				this.populatePromotionModel(request);
				validatePromotion(request,actionResult);
				if(actionResult.isSuccess()){
					if("createBasicPromo".equalsIgnoreCase(getActionName())){//null == promotion.getId()){
						this.promotion.setStatus(EnumPromotionStatus.DRAFT);		
						
						/*APPDEV-2385*/
						if(this.promotion.isBatchPromo()) {
							//Save batch promo
							int batchCount = Integer.parseInt(this.promotion.getBatchNumber());
							//Create Batch
							String batch_id = FDPromotionNewManager.createPromotionBatch(promotion);
							//set batch number to promotion object
							this.promotion.setBatchId(batch_id);
							String promo_redemption = this.promotion.getRedemptionCode();
							String promo_name = this.promotion.getName();
							String promo_code = "CD_";
							String tsa_code = this.promotion.getTsaPromoCode();
							if(EnumOfferType.WINDOW_STEERING.getName().equalsIgnoreCase(promotion.getOfferType())){
								promo_code = "WS_";
							}
							for(int i=0; i < batchCount; i++) {						
								//Start Creating promotions
								String randString = getRandomString();
								String promoCode = promo_code + randString;						
								String redemptionCode = promo_redemption + randString; 
								String name = promo_name + "_" + randString;
								if(tsa_code != null && tsa_code.length() > 0) {
									String tsa = tsa_code + randString;
									this.promotion.setTsaPromoCode(tsa);
								}
								this.promotion.setPromotionCode(promoCode);
								this.promotion.setRedemptionCode(redemptionCode);
								this.promotion.setName(name);					
								try {
									FDPromotionNewManager.createPromotionBasic(this.promotion);
								} catch (FDDuplicatePromoFieldException e) {
									reSubmitPromoCreation(this.promotion);
								} catch (FDPromoTypeNotFoundException e) {
									e.printStackTrace();
								} catch (FDPromoCustNotFoundException e) {
									e.printStackTrace();
								}
							}
						} else {						
							if(EnumOfferType.WINDOW_STEERING.getName().equalsIgnoreCase(promotion.getOfferType())){
								this.promotion.setPromotionCode("WS_"+new Date().getTime());
							}else{
								this.promotion.setPromotionCode("CD_"+new Date().getTime());
							}
							FDPromotionNewManager.createPromotionBasic(promotion);
						}
						setSuccessPage(getSuccessPage()+promotion.getPromotionCode()+"&SUCCESS=SUCCESS");
					}else{
						FDPromotionNewManager.storePromotionBasic(promotion);						
					}
				}
				
		} catch (FDResourceException e) {
			throw new JspException(e);
		}catch (FDDuplicatePromoFieldException e) {
			actionResult.setError(e.getMessage());
			return true;
		}catch (FDPromoTypeNotFoundException e) {
			actionResult.setError(e.getMessage());
			return true;
		}catch (FDPromoCustNotFoundException e) {
			actionResult.setError(e.getMessage());
			return true;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {		
		return true;
	}
	
	private void populatePromotionModel(HttpServletRequest request) throws FDResourceException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		this.promotion.setName(NVL.apply(request.getParameter("promo_name"), "").trim());
		//this.promotion.setPromotionCode(NVL.apply(request.getParameter("code_name"), " ").trim());
		
		this.promotion.setDescription(NVL.apply(request.getParameter("promo_description"), "").trim());
		this.promotion.setRedemptionCode(NVL.apply(request.getParameter("redemption_code"), "").trim());
		this.promotion.setTsaPromoCode(NVL.apply(request.getParameter("tsapromo_code"), "").trim());
		this.promotion.setRafPromoCode(NVL.apply(request.getParameter("raf_promo_code"), "").trim());
		this.promotion.setOfferDesc(NVL.apply(request.getParameter("offer_description"), "").trim());
		this.promotion.setAudienceDesc(NVL.apply(request.getParameter("audience_description"), "").trim());
		this.promotion.setTerms(NVL.apply(request.getParameter("terms"), "").trim());		
		String startDateStr = request.getParameter("edit_basic_cal_start_in");
		String startTimeStr =request.getParameter("edit_basic_cal_startTime_in");
		this.promotion.setStartDate(getDate(startDateStr,startTimeStr));
		if(null !=promotion.getStartDate()){
			this.promotion.setStartDateStr(DateUtil.formatDate(promotion.getStartDate()));
			this.promotion.setStartTimeStr(DateUtil.formatTime(promotion.getStartDate()));
		}
		String endType =NVL.apply(request.getParameter("end_type"), "").trim();
		if("rolling".equalsIgnoreCase(endType)){
			String rollingExpirationDays = NVL.apply(request.getParameter("rolling_expiration_days"), "").trim();
			if(!"".equals(rollingExpirationDays) && NumberUtil.isInteger(rollingExpirationDays)){
				this.promotion.setRollingExpirationDays(Integer.parseInt(rollingExpirationDays));
				this.promotion.setRedeemCount(null);
			}
			String expDateStr = request.getParameter("edit_basic_cal_endRolling_in");
			String expTimeStr =request.getParameter("edit_basic_cal_endRollingTime_in");
			this.promotion.setExpirationDate(getDate(expDateStr,expTimeStr));
		}else if("redemption".equalsIgnoreCase(endType)){
			String redeemCount = request.getParameter("redeem_count");
			if(null != redeemCount && NumberUtil.isInteger(redeemCount)){
				this.promotion.setRedeemCount(Integer.parseInt(redeemCount));
				this.promotion.setRollingExpirationDays(null);
			}
			String expDateStr = request.getParameter("edit_basic_cal_endRedemption_in");
			String expTimeStr =request.getParameter("edit_basic_cal_endRedemptionTime_in");
			this.promotion.setExpirationDate(getDate(expDateStr,expTimeStr));
		}else{
			String expDateStr = request.getParameter("edit_basic_cal_endSingle_in");
			String expTimeStr =request.getParameter("edit_basic_cal_endSingleTime_in");
			this.promotion.setRedeemCount(null);
			this.promotion.setRollingExpirationDays(null);
			this.promotion.setExpirationDate(getDate(expDateStr,expTimeStr));
			
		}
		
		if(null !=promotion.getExpirationDate()){
			this.promotion.setExpirationDateStr(DateUtil.formatDate(promotion.getExpirationDate()));
			this.promotion.setExpirationTimeStr(DateUtil.formatTime(promotion.getExpirationDate()));
		}
		this.promotion.setMaxUsage(NVL.apply(request.getParameter("usage_limit"), "").trim());		
//		if(request.getParameter("percentOff1")!=null && request.getParameter("percentOff1").trim().length()>0)
//		    this.promotion.setPercentOff(NVL.apply(request.getParameter("percentOff1"), "").trim());
//		this.promotion.setWaiveChargeType(NVL.apply(request.getParameter("waiveChargeType"), "").trim());		
		this.promotion.setRuleBased("on".equals(NVL.apply(request.getParameter("ruleBased"), "off")));
		this.promotion.setApplyFraud("on".equals(NVL.apply(request.getParameter("dontApplyFraud"), "off"))); //opposite
		this.promotion.setNeedCustomerList("on".equals(NVL.apply(request.getParameter("eligibilityList"), "off")));
		if("on".equals(request.getParameter("referralpromo"))) {
			this.promotion.setReferralPromo(true);
		} else {
			this.promotion.setReferralPromo(false);
		}
		
				
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		
		if("createBasicPromo".equalsIgnoreCase(getActionName())){//null == promotion.getId()){
			this.promotion.setPromotionType(EnumPromotionType.HEADER.getName());
//			this.promotion.setOfferType(EnumOfferType.GENERIC.getName());
			/*this.promotion.setStatus(EnumPromotionStatus.DRAFT);			
			this.promotion.setPromotionCode("CD_"+new Date().getTime());*/
			this.promotion.setCreatedBy(agent.getUserId());
			this.promotion.setCreatedDate(date);
			this.promotion.setModifiedBy(agent.getUserId());
			this.promotion.setModifiedDate(date);
			String isWindowSteering = NVL.apply(request.getParameter("windowStrg"), "");
			if("true".equalsIgnoreCase(isWindowSteering)){
				this.promotion.setOfferType(EnumOfferType.WINDOW_STEERING.getName());
			}else if("false".equalsIgnoreCase(isWindowSteering)){
				this.promotion.setOfferType(EnumOfferType.GENERIC.getName());
			}
		}else{
			this.promotion.setModifiedBy(agent.getUserId());
			this.promotion.setModifiedDate(date);			
		}
		populatePromoChangeModel();
		
		/*APPDEV - 2385*/
		if("true".equals(request.getParameter("batch_promo"))) {
			//User wants to create batch promotions.. 
			this.promotion.setBatchPromo(true);
			this.promotion.setBatchNumber(NVL.apply(request.getParameter("batchnumber"), "").trim());
			
			if(request.getParameter("batch_id") != null) {
				this.promotion.setBatchId(request.getParameter("batch_id"));
			}
		}
		
	}


	private void populatePromoChangeModel() throws FDResourceException{
		FDPromoChangeModel changeModel = new FDPromoChangeModel();
		List promoChangeDetails = new ArrayList<FDPromoChangeDetailModel>();
		List promoChanges = new ArrayList<FDPromoChangeModel>();
		changeModel.setChangeDetails(promoChangeDetails);
		changeModel.setPromotionId(promotion.getId());
		if(null == promotion.getId()){
			changeModel.setActionDate(promotion.getCreatedDate());
			changeModel.setActionType(EnumPromoChangeType.CREATE);
			changeModel.setUserId(promotion.getCreatedBy());
		}else{
			changeModel.setActionDate(promotion.getModifiedDate());
			changeModel.setActionType(EnumPromoChangeType.BASIC_INFO);
			changeModel.setUserId(promotion.getModifiedBy());
			FDPromotionNewModel oldPromotion = FDPromotionNewManager.loadPromotion(promotion.getPromotionCode());
			if(null != oldPromotion){
				if(!this.promotion.getName().equalsIgnoreCase(oldPromotion.getName())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Name");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getName());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getName());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(!this.promotion.getDescription().equalsIgnoreCase(oldPromotion.getDescription())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Description");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getDescription());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getDescription());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(!this.promotion.getRedemptionCode().equalsIgnoreCase(oldPromotion.getRedemptionCode())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Redemption Code");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getRedemptionCode());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getRedemptionCode());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(!this.promotion.getOfferDesc().equalsIgnoreCase(oldPromotion.getOfferDesc())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Offer Description");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getOfferDesc());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getOfferDesc());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(!this.promotion.getAudienceDesc().equalsIgnoreCase(oldPromotion.getAudienceDesc())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Audience Description");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getAudienceDesc());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getAudienceDesc());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(!this.promotion.getTerms().equalsIgnoreCase(oldPromotion.getTerms())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Terms");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getTerms());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getTerms());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(!this.promotion.getMaxUsage().equalsIgnoreCase(oldPromotion.getMaxUsage())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Max Usage");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getMaxUsage());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getMaxUsage());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(!this.promotion.getPercentOff().equalsIgnoreCase(oldPromotion.getPercentOff())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Percent Off");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getPercentOff());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getPercentOff());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(!this.promotion.getWaiveChargeType().equalsIgnoreCase(oldPromotion.getWaiveChargeType())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Waive Charge Type");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.getWaiveChargeType());
					changeDetailModel.setChangeFieldNewValue(this.promotion.getWaiveChargeType());
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(this.promotion.isRuleBased()!=oldPromotion.isRuleBased()){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Rule Based");
					changeDetailModel.setChangeFieldOldValue((oldPromotion.isRuleBased()?"Y":"N"));
					changeDetailModel.setChangeFieldNewValue((this.promotion.isRuleBased()?"Y":"N"));
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(this.promotion.isApplyFraud()!=(oldPromotion.isApplyFraud())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Apply Fraud");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.isApplyFraud()?"Y":"N");
					changeDetailModel.setChangeFieldNewValue(this.promotion.isApplyFraud()?"Y":"N");
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
				if(this.promotion.isNeedCustomerList()!=(oldPromotion.isNeedCustomerList())){
					FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
					changeDetailModel.setChangeFieldName("Need Eligibility List");
					changeDetailModel.setChangeFieldOldValue(oldPromotion.isNeedCustomerList()?"Y":"N");
					changeDetailModel.setChangeFieldNewValue(this.promotion.isNeedCustomerList()?"Y":"N");
					changeDetailModel.setChangeSectionId(EnumPromotionSection.BASIC_INFO);
					promoChangeDetails.add(changeDetailModel);
				}
			}
		}
		
		promoChanges.add(changeModel);
		this.promotion.setAuditChanges(promoChanges);
	}
	
	
	
	
	private Date getDate(String dateStr, String time){
		
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				Date date = sdf.parse(dateStr+" "+time);
				Calendar cal = DateUtil.toCalendar(date);				
				return cal.getTime();
			} catch (ParseException pe) { }
		
		
		return null;
	}
	
	private void validatePromotion(HttpServletRequest request,ActionResult result) throws FDResourceException{
		boolean isRafPromoCodeError=false;
		if(null == promotion.getName() ||"".equals(promotion.getName())){
			result.addError(true, "nameEmpty", " Name is required.");
		}
		if(null == promotion.getDescription() ||"".equals(promotion.getDescription())){
			result.addError(true, "descriptionEmpty", " Description is required.");
		}
		if(null != promotion.getRedemptionCode() && !"".equals(promotion.getRedemptionCode())){			 
			if(null == promotion.getId() && FDPromotionNewManager.isRedemptionCodeExists(promotion.getRedemptionCode())){
				result.addError(true, "redemptionCodeDuplicate", " An active promotion exists with the same redemption code, please update to save changes.");
			}else if(FDPromotionNewManager.isRedemptionCodeExists(promotion.getRedemptionCode(),promotion.getId())){
				result.addError(true, "redemptionCodeDuplicate", " An active promotion exists with the same redemption code, please update to save changes.");				
			}else  if(promotion.isReferralPromo()) {
				//promotion should be an automatic promotion for referral program
				result.addError(true, "automaticpromo", " Referral Promotion should be automatic. Please clear the Redemption code.");
				isRafPromoCodeError=true;
			}
		}
		
		if(null != promotion.getRafPromoCode() && !"".equals(promotion.getRafPromoCode())){			 
			if(null == promotion.getId() && FDPromotionNewManager.isRafPromoCodeExists(promotion.getRafPromoCode())){
				result.addError(true, "rafPromoCodeDuplicate", " An active promotion exists with the same RAF promo code, please update to save changes.");
			}else if(FDPromotionNewManager.isRafPromoCodeExists(promotion.getRafPromoCode(),promotion.getId())){
				result.addError(true, "rafPromoCodeDuplicate", " An active promotion exists with the same RAF promo code, please update to save changes.");				
			}else  if(promotion.isReferralPromo() && null != promotion.getRedemptionCode() && !"".equals(promotion.getRedemptionCode()) && !isRafPromoCodeError) {
				//promotion should be an automatic promotion for referral program
				result.addError(true, "automaticpromo1", " Referral Promotion should be automatic. Please clear the Redemption Code.");
			}
			/*else  if(promotion.isReferralPromo() && !isRafPromoCodeError) {
				//promotion should be an automatic promotion for referral program
				result.addError(true, "automaticpromo1", " Referral Promotion should be automatic. Please clear the Redemption Code.");
			}*/
		}
		
				
		if(null != promotion.getTsaPromoCode() && !"".equals(promotion.getTsaPromoCode())){			 
			if(null == promotion.getId() && FDPromotionNewManager.isTSAPromoCodeExists(promotion.getTsaPromoCode())){
				result.addError(true, "tsaCodeDuplicate", " An active promotion exists with the same TSA promo code, please update to save changes.");
			}else if(FDPromotionNewManager.isTSAPromoCodeExists(promotion.getTsaPromoCode(),promotion.getId())){
				result.addError(true, "tsaCodeDuplicate", " An active promotion exists with the same TSA promo code, please update to save changes.");				
			}else  if(promotion.isReferralPromo()) {
				//promotion should be an automatic promotion for referral program
				result.addError(true, "automaticpromo", " Referral Promotion should be automatic. Please clear the TSA promo code.");
			} else if(null == promotion.getRedemptionCode() || "".equals(promotion.getRedemptionCode())) {
				result.addError(true, "tsaCodeDuplicate", " Redemption Code is required to set TSA promo code. Please enter valid redemption code.");
			}
		}
		
		if(null == promotion.getOfferDesc() ||"".equals(promotion.getOfferDesc())){
			result.addError(true, "offerDescEmpty", " Offer Description is required.");
		}
		if(null == promotion.getTerms() ||"".equals(promotion.getTerms())){
			result.addError(true, "termsEmpty", " Terms & Conditions is required.");
		}
		if(null == promotion.getAudienceDesc() ||"".equals(promotion.getAudienceDesc())){
			result.addError(true, "audiDescEmpty", " Audience Description is required.");
		}
		if(null != promotion.getMaxUsage() && !"".equals(promotion.getMaxUsage()) && !NumberUtil.isInteger(promotion.getMaxUsage())){
			result.addError(true, "usageError", " Usage should be a number.");
		}
		if(null == promotion.getStartDate()){
			result.addError(true, "startDateEmpty", " Start Date can't be empty.");
		}
		if(null == promotion.getExpirationDate()){
			result.addError(true, "endDateEmpty", " End Date can't be empty.");
		}
		if(null != promotion.getStartDate() && null != promotion.getExpirationDate() && promotion.getExpirationDate().before(promotion.getStartDate())){
			result.addError(true, "endDateBefore", " Promotion Expiration/End Date should not be before promotion start date.");
		}
		if(null == promotion.getOfferType() || "".equals(promotion.getOfferType())){
			result.addError(true, "windowStrgEmpty", " Promotion 'Window Steering'/'Non Window Steering' should be selected.");
		}
		
		/*APPDEV-2385*/
		if(promotion.isBatchPromo() && (promotion.getBatchId() == null || promotion.getBatchId() == "")) {
			if(!NumberUtil.isInteger(promotion.getBatchNumber())) {
				result.addError(true, "batchCount", " No of promotions should be a number.");
			}
			if(null != promotion.getRedemptionCode() && !"".equals(promotion.getRedemptionCode())) {
				if(promotion.getRedemptionCode().length() > 8) {
					result.addError(true, "redemptionCodeDuplicate", " Redemption code length for batch promotions should be only 8 characters in length, as they are appended with an additional random String.");
				}
			}
			if(null != promotion.getTsaPromoCode() && !"".equals(promotion.getTsaPromoCode())) {
				if(promotion.getTsaPromoCode().length() > 7) {
					result.addError(true, "tsaCodeDuplicate", " APC Code length for batch promotions should be only 7 characters in length, as they are appended with an additional random String.");
				}
			}
		}
	}
	
	private boolean isCompleteDate (String day, String month, String year) {		
		return (!"".equals(day) && !"".equals(month) && !"".equals(year));
	}
	
	private String getRandomString() {
		StringBuffer sb = new StringBuffer();
		Random diceRandom = new Random();
		for(int i=0;i<4;i++) {
			char ch = (char) (diceRandom.nextInt('z' - 'a' + 1) + 'a');
			sb.append(Character.toUpperCase(ch));
		}
		for(int i=0;i<4;i++) {			
			sb.append(diceRandom.nextInt(9));
		}
		return sb.toString();
	}
	
	private void reSubmitPromoCreation(FDPromotionNewModel step1Promotion) {
		boolean submitted = false;
		String promo_redemption = step1Promotion.getRedemptionCode();
		String promo_description = step1Promotion.getDescription();
		String promo_name = step1Promotion.getName();
		String tsa_code = step1Promotion.getTsaPromoCode();
		String promo_code = "CD_";
		if(EnumOfferType.WINDOW_STEERING.getName().equalsIgnoreCase(promotion.getOfferType())){
			promo_code = "WS_";
		}
		while(!submitted) {
			String randString = getRandomString();
			String promoCode = promo_code + randString;						
			String redemptionCode = promo_redemption + "_" + randString; 
			String name = promo_name + "_" + randString;
			String descr = promo_description + "_" + randString;
			if(tsa_code != null && tsa_code.length() > 0) {
				String tsa = tsa_code + randString;
				step1Promotion.setTsaPromoCode(tsa);
			}
			step1Promotion.setPromotionCode(promoCode);
			step1Promotion.setRedemptionCode(redemptionCode);
			step1Promotion.setDescription(descr);
			step1Promotion.setName(name);	
			try {
					FDPromotionNewManager.createPromotionBasic(step1Promotion);				
			} catch (FDDuplicatePromoFieldException e) {
				submitted = false;
			} catch (FDPromoTypeNotFoundException e) {
				e.printStackTrace();
			} catch (FDPromoCustNotFoundException e) {
				e.printStackTrace();
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
			submitted = true;
		}
		
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}

