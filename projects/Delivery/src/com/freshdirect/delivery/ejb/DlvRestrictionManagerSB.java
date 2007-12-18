package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDResourceException;

public interface DlvRestrictionManagerSB extends EJBObject {

	
	// dlv restriction
	
	public List getDlvRestrictions(String dlvReason,String dlvType,String dlvCriterion) throws FDResourceException, RemoteException;
	
	public RestrictionI getDlvRestriction(String restrictionId) throws FDResourceException, RemoteException;
	
	public RestrictedAddressModel getAddressRestriction(String address1,String apartment, String zipCode) throws FDResourceException, RemoteException;
	
	public void storeDlvRestriction(RestrictionI restriction)  throws FDResourceException, RemoteException;
	
	public void storeAddressRestriction(RestrictedAddressModel restriction,String address1, String apartment, String zipCode)  throws FDResourceException, RemoteException;
	
	public void deleteDlvRestriction(String restrictionId)  throws FDResourceException, RemoteException;
	
	public void deleteAddressRestriction(String address1,String apartment,String zipCode)  throws FDResourceException, RemoteException;
	
	public void addDlvRestriction(RestrictionI restriction)  throws FDResourceException, RemoteException;
	
	public void addAddressRestriction(RestrictedAddressModel restriction)  throws FDResourceException, RemoteException;

}
