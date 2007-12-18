/*
 * DlvServiceSelectionResult.java
 *
 * Created on May 12, 2003, 10:18 PM
 */

package com.freshdirect.delivery;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.common.customer.EnumServiceType;

/**
 *
 * @author  mrose
 * @version
 */
public class DlvServiceSelectionResult implements Serializable {
	
	private HashMap serviceStatus = new HashMap();
	private EnumRestrictedAddressReason restrictionReason = EnumRestrictedAddressReason.NONE;

	public void setRestrictionReason(EnumRestrictedAddressReason restrictionReason) {
		this.restrictionReason = restrictionReason;
	}

	public EnumRestrictedAddressReason getRestrictionReason() {
		return this.restrictionReason;
	}

	public boolean isServiceRestricted() {
		return this.restrictionReason == null || !EnumRestrictedAddressReason.NONE.equals(this.restrictionReason);
	}
	
	public void addServiceStatus(EnumServiceType serviceType, EnumDeliveryStatus status){
		this.serviceStatus.put(serviceType, status);
	}
	
	public EnumDeliveryStatus getServiceStatus(EnumServiceType serviceType){
		EnumDeliveryStatus status = (EnumDeliveryStatus)this.serviceStatus.get(serviceType);
		return status != null ? status : EnumDeliveryStatus.DONOT_DELIVER ;
	}
	
	public Set getAvailableServices(){
		if(this.serviceStatus.isEmpty()){
			return Collections.EMPTY_SET;
		}
		
		Set s = new HashSet();
		for(Iterator i = this.serviceStatus.keySet().iterator(); i.hasNext(); ){
			EnumServiceType service = (EnumServiceType)i.next();
			EnumDeliveryStatus status = this.getServiceStatus(service);
			if(EnumDeliveryStatus.DELIVER.equals(status) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status)){
				s.add(service);
			}
		}
		
		return s;
	}
}
