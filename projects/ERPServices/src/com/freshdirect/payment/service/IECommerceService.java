package com.freshdirect.payment.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttributeCollection;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpRestrictedAvailabilityModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.delivery.sms.SmsAlertETAInfoData;
import com.freshdirect.ecommerce.data.sessionimpressionlog.SessionImpressionLogEntryData;
import com.freshdirect.ecommerce.data.survey.FDSurveyData;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.ecommerce.data.survey.SurveyKeyData;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.delivery.model.SiteAnnouncement;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;
import com.freshdirect.sms.model.st.STSmsResponse;

public interface IECommerceService {

	public Map<String,List<String>> updateCacheWithProdFly(List<String> familyIds)throws FDResourceException;
	
	public void loadData(List<ErpProductFamilyModel> productFamilyList) throws FDResourceException;
	
	public NavigableMap<Long, BINInfo> getActiveBINs() throws FDResourceException;

	public void saveBINInfo(List<List<BINInfo>> binInfos) throws FDResourceException;

	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType) throws FDResourceException;

	public FlatAttributeCollection getAttributes(String[] rootIds);

	public void storeAttributes(FlatAttributeCollection attrs, String user,	String sapId) throws FDResourceException;

	public Map loadAttributes(Date since) throws AttributeException;

	public BatchModel getBatch(int batchId)throws FDResourceException;

	public Collection getRecentBatches()throws FDResourceException;
	
	public ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws RemoteException, FDResourceException;

	public Collection<Object> loadAllZoneInfoMaster() throws RemoteException, FDResourceException;

	public String findZoneId(String zoneServiceType, String zoneId) throws RemoteException, FDResourceException;
	
    public String findZoneId(String servType) throws RemoteException;
    
    public List<ErpZoneMasterInfo> getAllZoneInfoDetails() throws RemoteException;
    
    public Collection findZoneInfoMaster(String zoneIds[]) throws RemoteException;
	
	
	public String getFamilyIdForMaterial(String matId) throws RemoteException, FDResourceException;
	
	public ErpProductFamilyModel findFamilyInfo(String familyId) throws RemoteException, FDResourceException;

	public ErpProductFamilyModel findSkuFamilyInfo(String materialId)throws RemoteException, FDResourceException;

	public Map<ErpCOOLKey, ErpCOOLInfo> getCountryOfOriginData(Date since) throws RemoteException;

	public <E> List loadEnum(String daoClassName)throws RemoteException;
	
	
	public String getUserIdForUserToken(String userToken) ;
	
	public boolean isUserEmailAlreadyExist(String email);
	
	public int isUserEmailAlreadyExist(String email, String provider) ;
	
	public void linkUserTokenToUserId(String customerId, String userId,String userToken, String identityToken, String provider, String displayName, String preferredUserName, String email, String emailVerified) ;

	public List<String> getConnectedProvidersByUserId(String userId, EnumExternalLoginSource source);
	
	public boolean isExternalLoginOnlyUser(String userId, EnumExternalLoginSource source) ;

	public void unlinkExternalAccountWithUser(String email, String userToken, String provider) ;
	
	public void unlinkExternalAccountWithUser(String customerId, String provider) ;
	
	public boolean isSocialLoginOnlyUser(String customer_id); 

	public List<String> getConnectedProvidersByUserId(String userId);

	public HLBrandProductAdResponse getSearchbykeyword(HLBrandProductAdRequest hLRequestData)throws RemoteException;

	public HLBrandProductAdResponse getCategoryProducts(HLBrandProductAdRequest hLRequestData)throws RemoteException;

	public Date getLastSentFeedOrderTime()throws RemoteException;

	public void submittedOrderdDetailsToHL(Date orderFeedDateFrom)throws RemoteException;

	public void submittedOrderdDetailsToHL(List<String> ordersList)throws RemoteException;

	String findZoneId(String zoneServiceType, String zipCode,boolean isPickupOnlyORNotServicebleZip) throws RemoteException;
	
	public void log(FDWebEvent fdWebEvent) throws RemoteException;
	
	public List<ExtoleConversionRequest> getExtoleCreateConversionRequest() throws FDResourceException, RemoteException;

	public List<ExtoleConversionRequest> getExtoleApproveConversionRequest() throws FDResourceException, RemoteException;

	public void updateConversionRequest(ExtoleResponse convResponse) throws FDResourceException, RemoteException;

	public void saveExtoleRewardsFile(List<FDRafCreditModel> rewards) throws FDResourceException, RemoteException;

	public void createConversion() throws ExtoleServiceException, IOException, FDResourceException, RemoteException;

	public void approveConversion() throws ExtoleServiceException, IOException, FDResourceException, RemoteException;

	public void downloadAndSaveRewards(String fileName) throws ExtoleServiceException, IOException, FDResourceException, RemoteException, ParseException;

	public void logCouponActivity(FDCouponActivityLogModel log)throws FDResourceException,RemoteException;
	
	public void logActivity(EwalletActivityLogModel logModel)throws RemoteException;

	public FDSurveyData getSurvey(SurveyKeyData key) throws RemoteException;
	
	public FDSurveyResponseData getCustomerProfile(FDIdentity identity, EnumServiceType serviceType) throws RemoteException ;
	
	public FDSurveyResponseData getSurveyResponse(FDIdentity identity, SurveyKeyData key) throws RemoteException ;
	
	public void storeSurvey(FDSurveyResponseData survey) throws FDResourceException;

	public void loadGroupPriceData(List<ErpGrpPriceModel> grpPriceZonelist)throws FDResourceException;

//	public void updateCOOLInfo(List<ErpCOOLInfo> erpCOOLInfoList)throws RemoteException;

	public List<ErpEWalletModel> getAllEWallets() throws RemoteException;

	public ErpEWalletModel findEWalletById(String eWalletId) throws RemoteException;

	public ErpEWalletModel findEWalletByType(String eWalletType) throws RemoteException;

	public int insertCustomerLongAccessToken(ErpCustEWalletModel custEWallet) throws RemoteException;

	public ErpCustEWalletModel getLongAccessTokenByCustID(String custID, String eWalletType) throws RemoteException;

	public int updateLongAccessToken(String custId, String longAccessToken, String eWalletType) throws RemoteException;

	public int deleteLongAccessToken(String custId, String eWalletID) throws RemoteException;

	public void saveLogEntry(Request<SessionImpressionLogEntryData> entry) throws RemoteException, FDResourceException;
	
	public void saveLogEntries(Request<Collection<SessionImpressionLogEntryData>> entries) throws FDResourceException, RemoteException;
	
	public void saveFutureZoneNotification(String email, String zip,String serviceType) throws FDResourceException;

	public List<SiteAnnouncement> getSiteAnnouncements() throws FDResourceException;

	public void logFailedFdxOrder(String orderId) throws FDResourceException;

	public List<MunicipalityInfo> getMunicipalityInfos() throws FDResourceException;

	public void sendOrderSizeFeed() throws FDResourceException;

	public void sendLateOrderFeed() throws FDResourceException;

	public Set<StateCounty> getCountiesByState(String state) throws FDResourceException;

	public int unlockInModifyOrders() throws FDResourceException;

	public StateCounty lookupStateCountyByZip(String zipcode) throws FDResourceException;

	public void commitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) throws FDResourceException;

	public void recommitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1) throws FDResourceException;

	public Map<String, DeliveryException> getCartonScanInfo() throws FDResourceException;

	public int queryForMissingFdxOrders() throws FDResourceException;
	
	public void updateInventories(List<ErpInventoryModel> stockEntries) throws FDResourceException;

	public void updateRestrictedInfos(
			Set<ErpRestrictedAvailabilityModel> restrictedInfos,
			Set<String> deletedMaterials) throws FDResourceException;
	
	public boolean smsOptIn(String customerId,String mobileNumber, String eStoreId) throws FDResourceException;
	
	public boolean smsOptInNonMarketing(String customerId,String mobileNumber, String eStoreId) throws FDResourceException;
	
	public boolean smsOptInMarketing(String customerId,String mobileNumber, String eStoreId) throws FDResourceException;
	
	public void expireOptin() throws FDResourceException;
	
	public void updateSmsReceived(String mobileNumber, String shortCode, String carrierName, Date receivedDate, String message, EnumEStoreId eStoreId) throws FDResourceException;
	
	public List<STSmsResponse> sendSmsToGateway(List<SmsAlertETAInfoData> etaInfoList) throws FDResourceException;
	
	public boolean smsOrderCancel(String customerId, String mobileNumber, String orderId, String eStoreId) throws FDResourceException ;
	
	public boolean smsOrderConfirmation(String customerId, String mobileNumber, String orderId, String eStoreId) throws FDResourceException;
	
	public boolean smsOrderModification(String customerId, String mobileNumber, String orderId, String eStoreId) throws  FDResourceException;

	
}
