package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;

/**
 * 
 * @deprecated
 *
 */
public interface ErpProductPromotionInfoSB extends EJBObject{
	@Deprecated
	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType) throws FDResourceException, RemoteException;
	@Deprecated
	public Map<ZoneInfo, List<FDProductPromotionInfo>> getAllProductsByType(String ppType,Date lastPublishDate) throws FDResourceException, RemoteException;
	@Deprecated
	public List<FDProductPromotionInfo> getProductsByZoneAndType(String ppType, String zoneId) throws FDResourceException, RemoteException;
	@Deprecated
	public ErpProductPromotionPreviewInfo getProductPromotionPreviewInfo(String ppPreviewId)throws FDResourceException, RemoteException;
	@Deprecated
	public Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> getAllPromotionsByType(String ppType,Date lastPublishDate) throws FDResourceException, RemoteException;

}
