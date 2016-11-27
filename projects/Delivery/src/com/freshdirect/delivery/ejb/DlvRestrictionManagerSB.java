package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdstore.FDResourceException;

public interface DlvRestrictionManagerSB extends EJBObject {

	
	// dlv restriction
	
	public List getDlvRestrictions(String dlvReason,String dlvType,String dlvCriterion) throws FDResourceException, RemoteException;
	
	public RestrictionI getDlvRestriction(String restrictionId) throws FDResourceException, RemoteException;
	
	public AlcoholRestriction getAlcoholRestriction(String restrictionId, String municipalityId) throws FDResourceException, RemoteException;
	
	public RestrictedAddressModel getAddressRestriction(String address1,String apartment, String zipCode) throws FDResourceException, RemoteException;
	
	public void storeDlvRestriction(RestrictionI restriction)  throws FDResourceException, RemoteException;
	
	public void storeAddressRestriction(RestrictedAddressModel restriction,String address1, String apartment, String zipCode)  throws FDResourceException, RemoteException;
	
	public void deleteDlvRestriction(String restrictionId)  throws FDResourceException, RemoteException;
	
	public void deleteAlcoholRestriction(String restrictionId)  throws FDResourceException, RemoteException;
	
	public void deleteAddressRestriction(String address1,String apartment,String zipCode)  throws FDResourceException, RemoteException;
	
	public void addDlvRestriction(RestrictionI restriction)  throws FDResourceException, RemoteException;
	
	public void addAddressRestriction(RestrictedAddressModel restriction)  throws FDResourceException, RemoteException;
	
	public void setAlcoholRestrictedFlag(String municipalityId, boolean restricted)  throws FDResourceException, RemoteException;
	
	public Map<String, List<String>> getMunicipalityStateCounties()throws FDResourceException, RemoteException;
	
	public void storeAlcoholRestriction(AlcoholRestriction restriction)  throws FDResourceException, RemoteException;
	
	public String addAlcoholRestriction(AlcoholRestriction restriction)  throws FDResourceException, RemoteException;

	public List<RestrictionI> getDlvRestrictions() throws FDResourceException, RemoteException;
	
	public boolean checkForAlcoholDelivery(String scrubbedAddress, String zipcode, String apartment) throws RemoteException;
	
    public EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) throws RemoteException;
    

}
