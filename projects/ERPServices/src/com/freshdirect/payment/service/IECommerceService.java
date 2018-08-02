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

import javax.ejb.ObjectNotFoundException;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttributeCollection;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpRestrictedAvailabilityModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.delivery.AddressAndRestrictedAdressData;
import com.freshdirect.ecommerce.data.delivery.AlcoholRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RestrictedAddressModelData;
import com.freshdirect.ecommerce.data.delivery.RestrictionData;
import com.freshdirect.ecommerce.data.delivery.sms.SmsAlertETAInfoData;
import com.freshdirect.ecommerce.data.dlv.AddressData;
import com.freshdirect.ecommerce.data.sessionimpressionlog.SessionImpressionLogEntryData;
import com.freshdirect.ecommerce.data.smartstore.EnumSiteFeatureData;
import com.freshdirect.ecommerce.data.survey.FDSurveyData;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.ecommerce.data.survey.SurveyKeyData;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.erp.model.ErpCharacteristicValuePriceModel;
import com.freshdirect.erp.model.ErpClassModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpMaterialBatchHistoryModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpMaterialPriceModel;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponHistoryInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.framework.mail.EmailI;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.delivery.model.ReservationException;
import com.freshdirect.logistics.delivery.model.ReservationUnavailableException;
import com.freshdirect.logistics.delivery.model.SiteAnnouncement;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.logistics.fdstore.ZipCodeAttributes;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Response;
import com.freshdirect.payment.gateway.ejb.FDGatewayActivityLogModel;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;
import com.freshdirect.rules.Rule;
import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.security.ticket.Ticket;
import com.freshdirect.sms.model.st.STSmsResponse;
//import com.freshdirect.sap.ejb.SapException;

public interface IECommerceService {

	public Map<String,List<String>> updateCacheWithProdFly(List<String> familyIds)throws FDResourceException;
	
	public void loadData(List<ErpProductFamilyModel> productFamilyList) throws FDResourceException;
	
	public NavigableMap<Long, BINInfo> getActiveBINs() throws FDResourceException;

	public void saveBINInfo(List<List<BINInfo>> binInfos) throws FDResourceException;

	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType) throws FDResourceException;

	public BatchModel getBatch(int batchId)throws FDResourceException;

	public Collection getRecentBatches()throws FDResourceException;
	
	public ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws RemoteException, FDResourceException;

	public Collection<String> loadAllZoneInfoMaster() throws RemoteException, FDResourceException;

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
	
	public ZipCodeAttributes lookupZipCodeAttributes(String zipcode) throws FDResourceException;

	public void commitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) throws FDResourceException, ReservationUnavailableException, ReservationException;

	public void recommitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1) throws FDResourceException, ReservationUnavailableException, ReservationException;

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


	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType, Date lastPublished) throws FDResourceException;

	public Map<String, Map<ZoneInfo, List<FDProductPromotionInfo>>> getAllPromotionsByType(	String ppType, Date lastPublishedDate)throws FDResourceException;

	List<FDProductPromotionInfo> getProductsByZoneAndType(String ppType,String zoneId) throws FDResourceException;

	public ErpProductPromotionPreviewInfo getProductPromotionPreviewInfo(String ppPreviewId)throws FDResourceException;


	public void log(FDRecommendationEvent event, int frequency) throws RemoteException;

	/// Group Scale (ERPGrpInfoSessionBean) API
	public Collection<GroupScalePricing> findGrpInfoMaster(FDGroup grpIds[]) throws RemoteException;
	public Collection<FDGroup> loadAllGrpInfoMaster() throws RemoteException;
	public GroupScalePricing findGrpInfoMaster(FDGroup group) throws FDGroupNotFoundException, RemoteException;
	public  Map<String,FDGroup> getGroupIdentityForMaterial(String matId) throws RemoteException;
	public Map<SalesAreaInfo, FDGroup> getGroupIdentitiesForMaterial(String matId) throws RemoteException;
	public Collection getFilteredSkus(List skuList) throws RemoteException;
	public int getLatestVersionNumber(String grpId) throws RemoteException;
	public Collection<FDGroup> findGrpsForMaterial(String matId) throws RemoteException;
	public FDGroup getLatestActiveGroup(String groupID) throws FDGroupNotFoundException, RemoteException;
	public Map<String,List<String>> getModifiedOnlyGroups(Date lastModified) throws RemoteException;
	// End Group Scale API 
	

	public void log(Class<? extends FDRecommendationEvent> eventClazz, Collection<RecommendationEventsAggregate> events) throws RemoteException;
	
	public Map<String, double[]> getPersonalizedFactors(String eStoreId,
			String erpCustomerId, List<String> factors) throws FDRuntimeException;

	public Map<String, double[]> getGlobalFactors(String eStoreId, List<String> factors);

	public Set<String> getPersonalizedFactorNames();

	public Set<String> getGlobalFactorNames();

	public Set<String> getGlobalProducts(String eStoreId);

	public Set<String> getPersonalizedProducts(String eStoreId, String erpCustomerId);

	public List<String> getProductRecommendations(String recommender, String contentKey); // need to fix this

	public List<String> getPersonalRecommendations(String recommender,
			String erpCustomerId);

	public String getPreferredWinePrice(String erpCustomerId);


	void logGatewayActivity(GatewayType gatewayType,Response response)throws RemoteException;

	public Map<String, Rule> getRules(String subsystem) throws FDResourceException, RemoteException;


	public Rule getRule(String ruleId) throws FDResourceException,RemoteException;
	
	public void deleteRule(String ruleId) throws FDResourceException,RemoteException;

	public void storeRule(Rule rule) throws FDResourceException,RemoteException;
	
	public Collection<ErpActivityRecord> findActivityByTemplate(ErpActivityRecord template) throws FDResourceException,RemoteException;

	public void logActivity(ErpActivityRecord rec) throws RemoteException;

	public void logActivityNewTX(ErpActivityRecord rec) throws RemoteException;

	public Map<String, List> getFilterLists(ErpActivityRecord template)throws FDResourceException,RemoteException;

	public Collection<ErpActivityRecord> getCCActivitiesByTemplate(ErpActivityRecord template) throws FDResourceException,RemoteException;
	
	public void sendRegisterGiftCard(String saleId, double saleAmount) throws RemoteException;

	public ErpProductInfoModel findProductBySkuAndVersion(String skuCode, int version) throws RemoteException;

	public Collection<String> findPeakProduceSKUsByDepartment(List<String> skuPrefixes) throws RemoteException;

	public List getDlvRestrictions(String dlvReason,String dlvType,String dlvCriterion) throws FDResourceException, RemoteException;
	
	public Object getDlvRestriction(String restrictionId) throws FDResourceException, RemoteException;
	
	public AlcoholRestrictionData getAlcoholRestriction(String restrictionId, String municipalityId) throws FDResourceException, RemoteException;
	
	public RestrictedAddressModelData getAddressRestriction(String address1,String apartment, String zipCode) throws FDResourceException, RemoteException;
	
	public void storeDlvRestriction(RestrictionData restriction)  throws FDResourceException, RemoteException;
	
	public void storeAddressRestriction(AddressAndRestrictedAdressData restriction)  throws FDResourceException, RemoteException;
	
	public void deleteDlvRestriction(String restrictionId)  throws FDResourceException, RemoteException;
	
	public void deleteAlcoholRestriction(String restrictionId)  throws FDResourceException, RemoteException;
	
	public void deleteAddressRestriction(String address1,String apartment,String zipCode)  throws FDResourceException, RemoteException;
	
	public void addDlvRestriction(RestrictionData restriction)  throws FDResourceException, RemoteException;
	
	public void addAddressRestriction(RestrictedAddressModelData restriction)  throws FDResourceException, RemoteException;
	
	public void setAlcoholRestrictedFlag(String municipalityId, boolean restricted)  throws FDResourceException, RemoteException;
	
	public Map<String, List<String>> getMunicipalityStateCounties()throws FDResourceException, RemoteException;
	
	public void storeAlcoholRestriction(AlcoholRestrictionData restriction)  throws FDResourceException, RemoteException;
	
	public String addAlcoholRestriction(AlcoholRestrictionData restriction)  throws FDResourceException, RemoteException;

	public List<RestrictionData> getDlvRestrictions() throws FDResourceException, RemoteException;
	
	public boolean checkForAlcoholDelivery(String scrubbedAddress, String zipcode, String apartment) throws RemoteException;
	
    public String checkAddressForRestrictions(AddressData address) throws RemoteException;
    
	public void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) throws RemoteException;
	
    public void sendSubmitOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType, 
			String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol) throws RemoteException;
	
    public void sendCancelOrderRequest(String saleId) throws RemoteException;
	
    public void sendModifyOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType, 
			String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol) throws RemoteException;
    
    public Ticket createTicket(Ticket ticket) throws RemoteException;
    
    public  Ticket updateTicket(Ticket ticket) throws RemoteException;
    
    public Ticket retrieveTicket(String key) throws RemoteException;
    
    public Map<String,String> getVariantMap(EnumSiteFeatureData feature) throws RemoteException;
    
	public Map<String,String> getVariantMap(EnumSiteFeatureData feature, Date date) throws RemoteException;
	
	public Map<String, Integer> getCohorts() throws RemoteException;
	
	public List<String> getCohortNames() throws RemoteException;
	
	public List<String> getVariants(EnumSiteFeatureData feature) throws RemoteException;
	
	public List<Date> getStartDates() throws RemoteException;
	//cms manager
	public String createFeedCmsFeed(String feedId, String storeId, String feedData) throws FDResourceException;
	public String getCmsFeed(String storeID) throws FDResourceException;
	
	public List<SapOrderPickEligibleInfo> queryForFDXSalesPickEligible() throws RemoteException;
	public void sendFDXEligibleOrdersToSap(List<SapOrderPickEligibleInfo> eligibleSapOrderLst) throws RemoteException;

	public ErpMaterialBatchHistoryModel getMaterialBatchInfo() throws RemoteException;
	
	public int createBatch() throws RemoteException;
	
	public void updateBatchStatus(int batchNumber, EnumApprovalStatus batchStatus) throws RemoteException;
	
	public void loadData(int batchNumber, ErpMaterialModel model,
			Map<String, ErpClassModel> createErpClassModel,
			Map<ErpCharacteristicValuePriceModel, Map<String, String>> chMap)
			throws RemoteException;

	public void loadSalesUnits(int batchNumber, String materialNo,
			Set<ErpSalesUnitModel> salesUnitModels) throws RemoteException;

	public void loadPriceRows(int batchNumber, String materialNo,
			List<ErpMaterialPriceModel> priceRowModels) throws RemoteException;

	public void loadMaterialPlantsAndSalesAreas(int batchNumber, String materialNo,
			List<ErpPlantMaterialModel> erpPlantModels,
			List<ErpMaterialSalesAreaModel> salesAreaModels)
			throws RemoteException;

    public void loadAndSaveCoupons( FDCouponActivityContext context) throws RemoteException;
	
	public List<FDCouponInfo> getActiveCoupons() throws RemoteException;
	
	public List<FDCouponInfo> getActiveCoupons(Date lastModified) throws RemoteException;
	
	public FDCustomerCouponWallet getCouponsForUser(FDCouponCustomer couponCustomer, FDCouponActivityContext context) throws RemoteException;	
	
	public boolean doClipCoupon(String couponId, FDCouponCustomer couponCustomer, FDCouponActivityContext context) throws RemoteException;
	
	public Map<String, FDCouponEligibleInfo> evaluateCartAndCoupons(CouponCart couponCart, FDCouponActivityContext context) throws RemoteException;

	public void postCancelPendingCouponTransactions() throws RemoteException;
	
	public void postConfirmPendingCouponTransactions(String saleId) throws RemoteException;
	
	public List<String> getConfirmPendingCouponSales() throws RemoteException;
	
	public List<String> getSubmitPendingCouponSales() throws RemoteException;
	
	public void postSubmitPendingCouponTransactions(String saleId) throws RemoteException;
	
	public void enqueueEmail(EmailI email) throws RemoteException;
	
	public PrimaryKey createRestrictedPaymentMethod(RestrictedPaymentMethodModel restrictedPaymentMethod) throws RemoteException;

	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPrimaryKey(PrimaryKey pk) throws RemoteException;

	public List<RestrictedPaymentMethodModel> findRestrictedPaymentMethodByCustomerId(String customerId, EnumRestrictedPaymentMethodStatus status) throws RemoteException;

	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPaymentMethodId(String paymentMethodId, EnumRestrictedPaymentMethodStatus status) throws RemoteException;

	public List<RestrictedPaymentMethodModel> findRestrictedPaymentMethods(RestrictedPaymentMethodCriteria criteria) throws RemoteException;

	public void storeRestrictedPaymentMethod(RestrictedPaymentMethodModel restrictedPaymentMethod) throws RemoteException;

	public void removeRestrictedPaymentMethod(PrimaryKey pk, String lastModifyUser) throws RemoteException;

	public List<RestrictedPaymentMethodModel> loadAllPatterns() throws FDResourceException, RemoteException;

	public List<RestrictedPaymentMethodModel> loadAllRestrictedPaymentMethods() throws RemoteException;

	public List<RestrictedPaymentMethodModel> loadAllBadPaymentMethods() throws RemoteException;

	public boolean checkBadAccount(ErpPaymentMethodI erpPaymentMethod, boolean useBadAccountCache) throws RemoteException;

	public ErpPaymentMethodI findPaymentMethodByAccountInfo(RestrictedPaymentMethodModel restrictedPaymentMethod) throws RemoteException;

	public HLBrandProductAdResponse getHLadproductToHomeByFDPriority(HLBrandProductAdRequest hLBrandProductAdRequest) throws RemoteException;

	public HLBrandProductAdResponse getPdpAdProduct(HLBrandProductAdRequest hLBrandProductAdRequest) throws RemoteException;

	public Collection<ErpMaterialSalesAreaModel> getGoingOutOfStockSalesAreas()throws RemoteException;

	public Collection<ErpCharacteristicValuePriceModel> findByMaterialId(String materialId, int version) throws RemoteException;

	public boolean uploadProductFeed() throws FDResourceException;
	
	public Map<String, String> getPPSettlementNotProcessed() throws RemoteException;
	
	public Map<String, Object> acquirePPLock(Date date) throws RemoteException;
	
	public void releasePPLock(List<String> settlementIds) throws RemoteException;
	
	public void insertNewSettlementRecord(Date date) throws RemoteException;
	
	public List<String> addPPSettlementSummary(ErpSettlementSummaryModel[] models) throws RemoteException;
	
	public void updatePayPalStatus(List<String> settlementIds) throws RemoteException;
	
	public List<ErpSettlementSummaryModel> getPPTrxns(List<String> ppStlmntIds) throws RemoteException;
	
	public void updatePPSettlementTransStatus(String settlementTransId) throws RemoteException;

	public void generateSitemap() throws RemoteException;

	public boolean isValidVaultToken(String token, String customerId) throws RemoteException;

	public ErpMaterialModel findBySapID(String materialNo) throws RemoteException;
}