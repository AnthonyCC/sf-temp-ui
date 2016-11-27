package com.freshdirect.mktAdmin.service;

import java.util.Collection;
import java.util.List;

import com.freshdirect.mktAdmin.model.ReferralPromotionModel;
import com.freshdirect.mktAdmin.constants.EnumFileType;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.FileUploadBean;
import com.freshdirect.mktAdmin.model.FileUploadedInfo;
import com.freshdirect.mktAdmin.model.ReferralAdminModel;
import com.freshdirect.mktAdmin.model.RestrictionSearchBean;

public interface MarketAdminServiceIntf {

	public Collection parseMktAdminFile(FileUploadBean bean) throws MktAdminApplicationException;
	
	public Collection getUpsOutageList(String fromDate, String endDate) throws MktAdminApplicationException;
			
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
	
	public void removeRestrictedCustomers(String promotionCode,String customerId, String email) throws MktAdminApplicationException;
	
	public void appendRestrictedCustomersFromEmailAddress(String[] emailAddress,String promotionCode) throws MktAdminApplicationException;
	
	public boolean isRestrictedCustomersExist(String promotionCode) throws MktAdminApplicationException;
	
	public String generateRestrictedCustomerFileContents(String promotionCode,EnumFileType fileType) throws MktAdminApplicationException;
	
	public void deleteRestrictionCustomer(String promotionCode) throws MktAdminApplicationException;
	
	public void  newDeletePromoRestrictedCustomers(Collection collection) throws MktAdminApplicationException;
	
	public void newReplacePromoRestrictedCustomers(Collection collection) throws MktAdminApplicationException;
	
	public FileUploadedInfo parseMktAdminAutoUploadFile(FileUploadBean bean) throws MktAdminApplicationException;
	
	public List<ReferralPromotionModel> getReferralPromotions() throws MktAdminApplicationException;
	
	public String validateCustomerList(String userList, String referralId) throws MktAdminApplicationException;
	
	public String createRefPromo(ReferralAdminModel rModel) throws MktAdminApplicationException;
	
	public List<ReferralAdminModel> getAllRefPromotions() throws MktAdminApplicationException;
	
	public void deleteRefPromos(List<String> ids, String username) throws MktAdminApplicationException;
	
	public ReferralAdminModel getRefPromotionInfo(String id) throws MktAdminApplicationException;
	
	public void editRefPromo(ReferralAdminModel rModel) throws MktAdminApplicationException;
	
	public  List<String> addReferralCustomers(Collection<String> collection, String referralId) throws MktAdminApplicationException;
	
	public  boolean defaultPromoExists(String referral_id) throws MktAdminApplicationException;
	
	public  List<String> getRefPromoUserList(String referral_id) throws MktAdminApplicationException;
}
