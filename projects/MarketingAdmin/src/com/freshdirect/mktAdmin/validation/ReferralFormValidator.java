package com.freshdirect.mktAdmin.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.mktAdmin.constants.EnumFileType;
import com.freshdirect.mktAdmin.model.ReferralAdminModel;

@Component
public class ReferralFormValidator implements Validator {

	public boolean supports(Class clazz) {
		return ReferralAdminModel.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		ReferralAdminModel model = (ReferralAdminModel) obj;        
			ValidationUtils.rejectIfEmpty(errors, "description", "fields.Description","Description is required field.");
			ValidationUtils.rejectIfEmpty(errors, "getText", "fields.GetText","Get Text is required field.");
			ValidationUtils.rejectIfEmpty(errors, "giveText", "fields.GiveText","Give Text is required field.");
			ValidationUtils.rejectIfEmpty(errors, "referralFee", "fields.referralFee","Referral Fee is required field.");
			ValidationUtils.rejectIfEmpty(errors, "shareHeader", "fields.shareHeader","Share Header is required field.");
			ValidationUtils.rejectIfEmpty(errors, "shareText", "fields.shareText","Share Text is required field.");
			ValidationUtils.rejectIfEmpty(errors, "giveHeader", "fields.giveHeader","Give Header is required field.");
			ValidationUtils.rejectIfEmpty(errors, "getHeader", "fields.getHeader","Get Header is required field.");
			ValidationUtils.rejectIfEmpty(errors, "fbHeadline", "fields.fbHeadline","Headline is required field.");
			ValidationUtils.rejectIfEmpty(errors, "fbText", "fields.fbText","Text is required field.");
			ValidationUtils.rejectIfEmpty(errors, "twitterText", "fields.twitterText","Text is required field.");
			ValidationUtils.rejectIfEmpty(errors, "referralPageText", "fields.referralPageText","Text is required field.");
			ValidationUtils.rejectIfEmpty(errors, "referralPageLegal", "fields.referralPageLegal","Legal is required field.");
			ValidationUtils.rejectIfEmpty(errors, "inviteEmailSubject", "fields.inviteEmailSubject","Subject line is required field.");
			ValidationUtils.rejectIfEmpty(errors, "inviteEmailOfferText", "fields.inviteEmailOfferText","Offer Text is a required field.");
			ValidationUtils.rejectIfEmpty(errors, "inviteEmailText", "fields.inviteEmailText","Email Text is required field.");
			ValidationUtils.rejectIfEmpty(errors, "inviteEmailLegal", "fields.inviteEmailLegal","Legal is required field.");
			ValidationUtils.rejectIfEmpty(errors, "referralCreditEmailSubject", "fields.referralCreditEmailSubject","Subject line is required.");
			ValidationUtils.rejectIfEmpty(errors, "referralCreditEmailText", "fields.referralCreditEmailText","Email Text is required.");
			ValidationUtils.rejectIfEmpty(errors, "fbFile", "fields.fbFile","Image file path is required.");
			ValidationUtils.rejectIfEmpty(errors, "siteAccessImageFile", "fields.siteAccessImageFile","Site Access Image file path is required.");
			
			//check promotion
			if(model.getPromotionId().equals("-1")) {
				errors.rejectValue("promotionId","fields.promotionId","Select a Promotion to apply.");
			}
			
			//check expiration date
			SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		    java.util.Date convertToDate = null;
		    try {
				convertToDate = dateFormat.parse(model.getExpirationDate());
			} catch (ParseException e) {
				errors.rejectValue("expirationDate","fields.expirationDate","Enter Date in MM/dd/YYYY format.");
			}
			
			System.out.println(model.getUserListFile());
			//check user list
			if(!model.getDefaultPromo()) {
				if(model.getUserListFileHolder() == null) {
					if(model.isEmpty() || model.getBytes()==null || model.getBytes().length==0) {
						errors.rejectValue("userListFile","fields.userListFile","Customer List file is required.");
					} else if((model.getBytes().length/1000)>FDStoreProperties.getMktAdminFileUploadSize()) {
						errors.rejectValue("userListFile","fields.userListFile","Customer List file size should not exceed " + FDStoreProperties.getMktAdminFileUploadSize() + ".");
					} else {
						String tmpFileType=model.getFileType();
						EnumFileType fileType=EnumFileType.getEnum(tmpFileType.toUpperCase());
						List fileList=EnumFileType.getEnumList();
						if(!fileList.contains(fileType)) {
							errors.rejectValue("userListFile","fields.userListFile","Uploded File should be of EXCEL, CSV types. Other file types are not supported.");
						}
					}
				}
			} else {
				//this is a default promo. Do not let user select the customer list
				if(!model.isEmpty()) {
					errors.rejectValue("defaultPromo","defaultPromo","Customer List cannot be uploaded for default promo.");
				}				
				
			}
			
			//check referral fee
			if(model.getReferralFee() != null && model.getReferralFee().length() > 0) {
				try {
					int i = Integer.parseInt(model.getReferralFee());
				} catch (Exception e) {
					errors.rejectValue("referralFee","field.referralFee", "Referral Fee must be a valid dollar amount.");
				}
			}
			
			//check notes for length
			if(model.getNotes() != null && model.getNotes().length() > 1999) {				
				errors.rejectValue("notes","field.notes", "Notes can take only upto 2000 characters.");				
			}
			
			//referral page text
			if(model.getReferralPageText() != null && model.getReferralPageText().length() > 160) {				
				errors.rejectValue("referralPageText","field.referralPageText", "Referral Page text can take only upto 160 characters.");				
			}
			
			//check invite email offer text
		
	}
}
