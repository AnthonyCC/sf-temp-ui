package com.freshdirect.mktAdmin.model;

import com.freshdirect.fdstore.promotion.FDPromotionModelFactory;
import com.freshdirect.fdstore.promotion.management.FDPromotionModel;
import com.freshdirect.mktAdmin.constants.EnumFileContentType;
import com.freshdirect.mktAdmin.constants.EnumListUploadActionType;

public class RestrictionListUploadBean extends FileUploadBean {

	private EnumListUploadActionType actionType=null;
	private String promotionCode=null;

	public RestrictionListUploadBean(){		
	}
	
	public EnumListUploadActionType getActionType() {

		      return actionType;
		
//		else{
//			FDPromotionModel model=FDPromotionModelFactory.getInstance().getPromotion(promotionCode);
//			if(model.getAssignedCustomerSize()==0)
//		}
	}

	public void setActionType(EnumListUploadActionType actionType) {
		this.actionType = actionType;
	} 
	
	
	public EnumFileContentType getFileContentType() {
		return EnumFileContentType.RESTRICTION_LIST_FILE_TYPE;  
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	
	
	
}
