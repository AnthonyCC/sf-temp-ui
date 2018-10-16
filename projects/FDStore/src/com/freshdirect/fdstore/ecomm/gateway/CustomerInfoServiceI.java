package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.CrmClick2CallModel;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateDisplayNameException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.ecommerce.data.product.ProductRequestData;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.URLRewriteRule;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.SilverPopupDetails;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.fdstore.iplocator.IpLocatorEventDTO;
import com.freshdirect.fdstore.promotion.AssignedCustomerParam;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumReservationType;

public interface CustomerInfoServiceI {
	public PrimaryKey getCustomerId(String userId) throws FDResourceException, RemoteException;

	public FDCustomerInfo getCustomerInfo(FDIdentity identity) throws FDResourceException, RemoteException;

	public ErpCustomerInfoModel getErpCustomerInfo(String erpCustomerId)  throws FDResourceException;
	
	public FDCustomerInfo getSOCustomerInfo(FDIdentity identity) throws FDResourceException, RemoteException;

	public void updateUserId(FDActionInfo info, String userId)
			throws FDResourceException, ErpDuplicateUserIdException, RemoteException;

	/**
	 * Store the user.
	 *
	 * @param user
	 *            the customer's user object
	 *
	 * @throws FDResourceException
	 *             if an error occurred using remote resources
	 */
	public void storeUser(FDUser user) throws FDResourceException, RemoteException;

	public void storeCohortName(String userId, String cohortName) throws FDResourceException, RemoteException;

	public boolean isPasswordRequestExpired(String emailAddress, String passReq)
			throws FDResourceException, RemoteException;

	public void changePassword(FDActionInfo info, String emailAddress, String password)
			throws FDResourceException, RemoteException;

	public void setProfileAttribute(FDIdentity identity, String key, String value, FDActionInfo info)
			throws RemoteException, FDResourceException;

	public void createCase(CrmSystemCaseInfo caseInfo) throws RemoteException, FDResourceException;

	public FDCustomerCreditHistoryModel getCreditHistory(FDIdentity identity)
			throws FDResourceException, RemoteException;

	public FDReservation makeReservation(FDUserI user, String timeslotId, EnumReservationType rsvType, String addressId,
			FDActionInfo aInfo, boolean chefsTable, TimeslotEvent event, boolean isForced)
			throws FDResourceException, ReservationException, RemoteException;

	public void cancelReservation(FDIdentity identity, FDReservation reservation, FDActionInfo actionInfo,
			TimeslotEvent event) throws FDResourceException, RemoteException;

	public void storeProductRequest(List<FDProductRequest> productRequest) throws RemoteException, FDResourceException;

	public ProductRequestData productRequestFetchAll() throws FDResourceException, RemoteException;

	public boolean isECheckRestricted(FDIdentity identity) throws FDResourceException, RemoteException;

	public String getNextId(String schema, String sequence) throws FDResourceException, RemoteException;

	public ErpCustomerModel getCustomerPaymentAndCredit(FDIdentity identity)
			throws FDResourceException, RemoteException;

    public Map<String, AssignedCustomerParam> getAssignedCustomerParams(FDUserI user)
			throws FDResourceException, RemoteException;

	public void logIpLocatorEvent(IpLocatorEventDTO ipLocatorEventDTO) throws FDResourceException, RemoteException;

	public IpLocatorEventDTO loadIpLocatorEvent(String fdUserId) throws FDResourceException, RemoteException;

	/**
	 * Get lightweight info about a customer's used promotions.
	 *
	 */
	public ErpPromotionHistory getPromoHistoryInfo(String customerId) throws FDResourceException, RemoteException;

	public List<URLRewriteRule> loadRewriteRules() throws FDResourceException, RemoteException;

	public List<String> getTopFaqs() throws FDResourceException, RemoteException;

	public boolean isDisplayNameUsed(String displayName, String custId)
			throws ErpDuplicateDisplayNameException, FDResourceException, RemoteException;

	public List<SilverPopupDetails> getSilverPopupDetails() throws FDResourceException, RemoteException;

	public void updateSPSuccessDetails(SilverPopupDetails detail) throws FDResourceException, RemoteException;
	
	public boolean insertOrUpdateSilverPopup(SilverPopupDetails silverPopupDetails) throws FDResourceException, RemoteException;

	public String getCookieByFdCustomerId(String fdCustomerId) throws FDResourceException, RemoteException;
	
	public boolean setAcknowledge(FDIdentity identity, boolean acknowledge,String ackType)throws FDResourceException, RemoteException;
	
	public List<String> getUsedReservations(String customerId) throws FDResourceException, RemoteException;

	public CrmClick2CallModel getClick2CallInfo() throws FDResourceException, RemoteException;
	
	public void setActive(FDActionInfo info, boolean active) throws RemoteException, FDResourceException;
	
	public boolean updateCustomerInfo(FDActionInfo info, ErpCustomerInfoModel customerInfo) throws FDResourceException, RemoteException;
	
	public void updateFDCustomerEStoreInfo(FDCustomerEStoreModel fdCustomerEStoreModel, String custId) throws FDResourceException, RemoteException;

    public FDCustomerCreditHistoryModel getPendingCreditHistory(FDIdentity identity) throws FDResourceException;

	public boolean isReferrerRestricted(FDIdentity identity) throws RemoteException;

	public boolean isCreditRestricted(FDIdentity identity)throws RemoteException;
	
}
