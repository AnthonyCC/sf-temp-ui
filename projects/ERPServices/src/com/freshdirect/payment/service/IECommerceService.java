package com.freshdirect.payment.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttributeCollection;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.payment.BINInfo;
import com.freshdirect.referral.extole.ExtoleServiceException;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;

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

	public List loadEnum(String daoClassName)throws RemoteException;
	
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

}
