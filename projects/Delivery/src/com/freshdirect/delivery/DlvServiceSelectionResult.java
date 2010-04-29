package com.freshdirect.delivery;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.freshdirect.common.customer.EnumServiceType;

/**
 *
 * @author  mrose
 * @version
 */
public class DlvServiceSelectionResult implements Serializable {
	
	private static final long	serialVersionUID	= 8634254710338888689L;
	
	private Map<EnumServiceType,EnumDeliveryStatus> serviceStatus = new HashMap<EnumServiceType,EnumDeliveryStatus>();
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
	
	public Set<EnumServiceType> getAvailableServices(){
		if(this.serviceStatus.isEmpty()){
			return Collections.<EnumServiceType>emptySet();
		}
		
		Set<EnumServiceType> s = new HashSet<EnumServiceType>();
		for ( EnumServiceType service : serviceStatus.keySet() ) {
			EnumDeliveryStatus status = this.getServiceStatus(service);
			if(EnumDeliveryStatus.DELIVER.equals(status) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status)|| EnumDeliveryStatus.COS_ENABLED.equals(status)){
				s.add(service);
			}
		}
		
		return s;
	}
}
