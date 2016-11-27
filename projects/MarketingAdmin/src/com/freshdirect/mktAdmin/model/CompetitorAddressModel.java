package com.freshdirect.mktAdmin.model;

import java.util.Date;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.mktAdmin.constants.EnumCompetitorStatusType;
import com.freshdirect.mktAdmin.constants.EnumCompetitorType;

public class CompetitorAddressModel extends AddressModel {
	
	private EnumCompetitorType competitorType=null;
	private EnumCompetitorStatusType status=null;
	private Date dateCreated=null;
	
	
	public EnumCompetitorType getCompetitorType() {
		return competitorType;
	}
	public void setCompetitorType(EnumCompetitorType competitorType) {
		this.competitorType = competitorType;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public EnumCompetitorStatusType getStatus() {
		return this.status;
	}
	public void setStatus(EnumCompetitorStatusType status) {
		this.status = status;
	}
	
	public boolean isNew() {
		return (this.getId() == null);
	}
	
	public void setFrom(ErpAddressModel erpAddress) {
		super.setFrom(erpAddress);		
	}

	

}
