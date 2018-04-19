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

/**
 *@deprecated Please use the DlvRestrictionController and DlvRestrictionManagerServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface DlvRestrictionManagerSB extends EJBObject {

	
	// dlv restriction
	@Deprecated
	public List getDlvRestrictions(String dlvReason,String dlvType,String dlvCriterion) throws FDResourceException, RemoteException;
	@Deprecated
	public RestrictionI getDlvRestriction(String restrictionId) throws FDResourceException, RemoteException;
	@Deprecated
	public AlcoholRestriction getAlcoholRestriction(String restrictionId, String municipalityId) throws FDResourceException, RemoteException;
	@Deprecated
	public RestrictedAddressModel getAddressRestriction(String address1,String apartment, String zipCode) throws FDResourceException, RemoteException;
	@Deprecated
	public void storeDlvRestriction(RestrictionI restriction)  throws FDResourceException, RemoteException;
	@Deprecated
	public void storeAddressRestriction(RestrictedAddressModel restriction,String address1, String apartment, String zipCode)  throws FDResourceException, RemoteException;
	@Deprecated
	public void deleteDlvRestriction(String restrictionId)  throws FDResourceException, RemoteException;
	@Deprecated
	public void deleteAlcoholRestriction(String restrictionId)  throws FDResourceException, RemoteException;
	@Deprecated
	public void deleteAddressRestriction(String address1,String apartment,String zipCode)  throws FDResourceException, RemoteException;
	@Deprecated
	public void addDlvRestriction(RestrictionI restriction)  throws FDResourceException, RemoteException;
	@Deprecated
	public void addAddressRestriction(RestrictedAddressModel restriction)  throws FDResourceException, RemoteException;
	@Deprecated
	public void setAlcoholRestrictedFlag(String municipalityId, boolean restricted)  throws FDResourceException, RemoteException;
	@Deprecated
	public Map<String, List<String>> getMunicipalityStateCounties()throws FDResourceException, RemoteException;
	@Deprecated
	public void storeAlcoholRestriction(AlcoholRestriction restriction)  throws FDResourceException, RemoteException;
	@Deprecated
	public String addAlcoholRestriction(AlcoholRestriction restriction)  throws FDResourceException, RemoteException;
	@Deprecated
	public List<RestrictionI> getDlvRestrictions() throws FDResourceException, RemoteException;
	@Deprecated
	public boolean checkForAlcoholDelivery(String scrubbedAddress, String zipcode, String apartment) throws RemoteException;
	@Deprecated
    public EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) throws RemoteException;
    

}
