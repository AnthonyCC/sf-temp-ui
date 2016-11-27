package com.freshdirect.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.ModelSupport;

public class ErpRestrictedAvailabilityModel extends ModelSupport {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -9027449468761131506L;
	private String materialNumber;
	private Date restrictedDate;
	private boolean deleted;
	

	public  ErpRestrictedAvailabilityModel(String materialNumber,Date restrictedDate,boolean deleted){		
		this.materialNumber=materialNumber;
		this.restrictedDate=restrictedDate;
		this.deleted=deleted;
	}


	public String getMaterialNumber() {
		return materialNumber;
	}


	public Date getRestrictedDate() {
		return restrictedDate;
	}


	public boolean isDeleted() {
		return deleted;
	}

	public boolean equals(Object param) {
		if(this.materialNumber == null || this.restrictedDate == null) return false;
		ErpRestrictedAvailabilityModel model = (ErpRestrictedAvailabilityModel) param;
		return (this.materialNumber.equals(model.getMaterialNumber()) 
				&& this.restrictedDate.equals(model.getRestrictedDate()));
	}
}
