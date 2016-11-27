package com.freshdirect.dataloader.geocodefilter;

import java.sql.Connection;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;

public abstract class GFBaseValidator implements IGeoValidator{
	
	public boolean validateAddress(Connection conn,Object object, boolean filterRestricted) throws FDResourceException, FDInvalidAddressException {
		
		GFRecord record = (GFRecord) object;
		
		AddressModel address = getAddressModel(record);		
		FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByAddress(address);
		
		EnumDeliveryStatus result = serviceResult.getServiceStatus(EnumServiceType.HOME);
		EnumDeliveryStatus resultCorp = serviceResult.getServiceStatus(EnumServiceType.CORPORATE);
		boolean addressGood = isAddressValid(EnumDeliveryStatus.DELIVER.equals(result), EnumDeliveryStatus.DELIVER.equals(resultCorp));
		if(addressGood && filterRestricted){
			addressGood = EnumRestrictedAddressReason.NONE.equals(serviceResult.getRestrictionReason().NONE);
		}
		if (addressGood) {
			addResult(record, EnumDeliveryStatus.DELIVER.equals(result), EnumDeliveryStatus.DELIVER.equals(resultCorp));
			
		} 
		return addressGood;
	}
	
	public abstract AddressModel getAddressModel(GFRecord record);
	
	public abstract void initialize();
	
	public abstract void addResult(GFRecord record, boolean homeDelivery, boolean corporateDelivery);
	
	public abstract boolean isAddressValid(boolean homeDelivery, boolean corporateDelivery);
}
