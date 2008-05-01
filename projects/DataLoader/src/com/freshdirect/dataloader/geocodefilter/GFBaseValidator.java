package com.freshdirect.dataloader.geocodefilter;

import java.sql.Connection;
import java.sql.SQLException;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.ejb.DlvManagerDAO;
import com.freshdirect.delivery.ejb.DlvManagerSessionBean;

public abstract class GFBaseValidator implements IGeoValidator{
	
	private DlvManagerSessionBean dlvManager;
	 
	public void initialize(DlvManagerSessionBean dlvManager) {
		this.dlvManager = dlvManager;
		initialize();
	}
	public boolean validateAddress(Connection conn,Object object, boolean filterRestricted) throws SQLException,InvalidAddressException {
		
		GFRecord record = (GFRecord) object;
		
		AddressModel address = getAddressModel(record);				
		
		EnumDeliveryStatus result = dlvManager.getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.HOME));
		EnumDeliveryStatus resultCorp = dlvManager.getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.CORPORATE));
		boolean addressGood = isAddressValid(EnumDeliveryStatus.DELIVER.equals(result), EnumDeliveryStatus.DELIVER.equals(resultCorp));
		if(addressGood && filterRestricted){
			addressGood = EnumRestrictedAddressReason.NONE.equals(DlvManagerDAO.isAddressRestricted(conn, address));
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
