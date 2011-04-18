package com.freshdirect.mktAdmin.service;

import java.util.Collection;

import com.freshdirect.mktAdmin.constants.EnumFileType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.RestrictionSearchBean;

public interface MarketAdminServiceIntf {

	public Collection parseMktAdminFile(FileUploadBean bean) throws MktAdminApplicationException;
			
	public void storeCompetitorInformation(Collection collection) throws MktAdminApplicationException;
	
	public void removeCompetitorInformation(Collection collection) throws MktAdminApplicationException;
	
	public void addCompetitorInformation(Collection collection) throws MktAdminApplicationException;
	
	public void newAddPromoRestrictedCustomers(Collection collection) throws MktAdminApplicationException;
	
	public Collection getCompetitorInformation() throws MktAdminApplicationException;
	
	public CompetitorAddressModel getCompetitorInformation(String competitorId) throws MktAdminApplicationException;
	
	public void addCustomerInformation(Collection collection) throws MktAdminApplicationException;
	
	public Collection getAllPromotions() throws MktAdminApplicationException;

	public Collection getRestrictedCustomers(RestrictionSearchBean model) throws MktAdminApplicationException;

	public Collection getPromotionModel(String[] promotionCodes) throws MktAdminApplicationException;
	
	public void removeRestrictedCustomers(String promotionCode,String customerId) throws MktAdminApplicationException;
	
	public void appendRestrictedCustomersFromEmailAddress(String[] emailAddress,String promotionCode) throws MktAdminApplicationException;
	
	public boolean isRestrictedCustomersExist(String promotionCode) throws MktAdminApplicationException;
	
	public String generateRestrictedCustomerFileContents(String promotionCode,EnumFileType fileType) throws MktAdminApplicationException;
	
	public void deleteRestrictionCustomer(String promotionCode) throws MktAdminApplicationException;
	
	public void  newDeletePromoRestrictedCustomers(Collection collection) throws MktAdminApplicationException;
	
	public void newReplacePromoRestrictedCustomers(Collection collection) throws MktAdminApplicationException;
}
