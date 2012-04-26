package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;

public interface ErpProductPromotionInfoSB extends EJBObject{
	
	public Map<String, List<FDProductPromotionInfo>> getAllProductsByType(String ppType) throws FDResourceException, RemoteException;
	public Map<String, List<FDProductPromotionInfo>> getAllProductsByType(String ppType,Date lastPublishDate) throws FDResourceException, RemoteException;
	public List<FDProductPromotionInfo> getProductsByZoneAndType(String ppType, String zoneId) throws FDResourceException, RemoteException;
	public ErpProductPromotionPreviewInfo getProductPromotionPreviewInfo(String ppPreviewId)throws FDResourceException, RemoteException;

}
