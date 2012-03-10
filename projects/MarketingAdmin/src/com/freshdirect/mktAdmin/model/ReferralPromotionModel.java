package com.freshdirect.mktAdmin.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.mktAdmin.constants.EnumCompetitorStatusType;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;

public class ReferralPromotionModel extends AddressModel {
	
	
	private static final long serialVersionUID = 1L;
	private String description;
	private String promotionId;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	
	@Override
	public String toString() {
		return "[description=" + description + ", promotionId=" + promotionId + "]";
	}
	

}
