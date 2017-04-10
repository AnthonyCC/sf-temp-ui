package com.freshdirect.payment.service;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttributeCollection;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.payment.BINInfo;

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
	
	public String getFamilyIdForMaterial(String matId) throws RemoteException, FDResourceException;
	
	public ErpProductFamilyModel findFamilyInfo(String familyId) throws RemoteException, FDResourceException;

	public ErpProductFamilyModel findSkuFamilyInfo(String materialId)throws RemoteException, FDResourceException;
	
	
}
