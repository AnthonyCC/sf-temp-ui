package com.freshdirect.mktAdmin.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.model.ReferralAdminModel;
import com.freshdirect.mktAdmin.model.ReferralPromotionModel;
import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.CustomerAddressModel;

public interface MarketAdminDAOIntf {
	
	public void insertCompetitorAddrModel(Collection collection) throws SQLException;

	public Collection getCompetitorInfo() throws SQLException;
	
	public Collection getUpsOutageCustList(String fromDate, String endDate) throws SQLException;
		
	public CompetitorAddressModel getCompetitorInfo(String competitorId) throws SQLException;
	
	public void storeCompetitorInformation(Collection collection)	
	throws SQLException;
		// TODO Auto-generated method stub
	
	public void deleteCompetitorInfo(Collection collection) throws SQLException;
	
	public boolean isAddressAllreadyExists(CompetitorAddressModel model) throws SQLException;
	
	public boolean isCustomerAllreadyExists(CustomerAddressModel model) throws SQLException;
	
	public void insertCustomerAddrModel(Collection collection) throws SQLException;

	public Collection getRestrictedCustomers(String promotionCode,String searchKey,long startIndex,long endIndex) throws SQLException;
	
	public void deleteRestrictedCustomers(String promotionId,String customerId, String email) throws SQLException;
		
	public Collection getRestrictedCustomers(Collection restCustomerModel) throws SQLException;
	
	public Collection getInvalidCustomerIds(Collection restCustomerModel) throws SQLException;
	
	public void insertRestrictedCustomers(Collection restCustomerModel) throws SQLException;
	
	public void newInsertRestrictedCustomers(Collection restCustomerModel) throws SQLException;

	public void deleteRestrictedCustomers(Collection collection) throws SQLException;
	
	public Collection getInvalidPromotions(Collection restCustomerModel) throws SQLException;

	public void deleteRestrictedCustomerForPromotion(String promotionId) throws SQLException;
	
	public Map getCustomerIdsForEmailAddress(String emailAddress[]) throws SQLException;
	
	public void deleteRestrictedCustomer(String promotionCode) throws SQLException;
	
	public List<ReferralPromotionModel> getReferralPromotions() throws SQLException;
	
	public boolean isValidCustomer(String email, String referralId) throws SQLException;
	
	public String createRefPromo(ReferralAdminModel rModel) throws SQLException;
	
	public List<ReferralAdminModel> getAllRefPromotions() throws SQLException;
	
	public void deleteRefPromos(List<String> ids, String username) throws SQLException;
	
	public ReferralAdminModel getRefPromotionInfo(String id) throws SQLException;
	
	public void editRefPromo(ReferralAdminModel rModel) throws SQLException;
	
	public  List<String> addReferralCustomers(Collection<String> collection, String referralId) throws SQLException;
	
	public boolean defaultPromoExists(String referral_id) throws SQLException;
	
	public  List<String> getRefPromoUserList(String referral_id) throws SQLException;
			
}
