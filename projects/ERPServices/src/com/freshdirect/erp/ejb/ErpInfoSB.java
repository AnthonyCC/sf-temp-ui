/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpProductInfoModel;

/**
 * the remote interface for the ErpMaterialInfo session bean
 *
 *
 * @version $Revision$
 * @author $Author$
 */
/**
 *@deprecated Please use the ErpInfoController and ErpInfoServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface ErpInfoSB extends EJBObject {
	 @Deprecated
    public Collection findMaterialsByBatch(int batchNumber) throws RemoteException;
	 @Deprecated
    public Collection findMaterialsBySapId(String sapId) throws RemoteException;
	 @Deprecated
    public Collection findMaterialsBySku(String skuCode) throws RemoteException;
	 @Deprecated
    public Collection findMaterialsByDescription(String description) throws RemoteException;
	 @Deprecated
    public Collection findMaterialsByCharacteristic(String classAndCharName) throws RemoteException;
	 @Deprecated
    public Collection findMaterialsByCharacteristic(String className, String charName) throws RemoteException;
	 @Deprecated
    public Collection findMaterialsByClass(String className) throws RemoteException;
	 @Deprecated
    public ErpProductInfoModel findProductBySku(String skuCode) throws RemoteException, ObjectNotFoundException;
	 @Deprecated
    public ErpProductInfoModel findProductBySku(String skuCode, int version) throws RemoteException, ObjectNotFoundException;
	 @Deprecated
    public Collection findProductsBySku(String[] skuCodes) throws RemoteException;
	 @Deprecated
    public Collection findProductsBySapId(String sapId) throws RemoteException;
	 @Deprecated
	public Collection findSkusBySapId(String sapId) throws RemoteException;

	 @Deprecated
    public Collection findProductsByDescription(String description) throws RemoteException;
	 @Deprecated
    public Collection findProductsLikeSku(String skuCode) throws RemoteException;
	 @Deprecated
    public Collection findProductsByUPC(String upc) throws RemoteException;
	 @Deprecated
    public Collection findProductsByCustomerUPC(String erpCustomerPK, String upc) throws RemoteException;
	 @Deprecated
    public Collection findProductsLikeUPC(String upc) throws RemoteException;
	 @Deprecated
    /** @return null if there's no inventory for material */
    public ErpInventoryModel getInventoryInfo(String materialNo) throws RemoteException;
	 @Deprecated
    public Map loadInventoryInfo(Date modifiedDate) throws RemoteException;
	 @Deprecated
    /** @return collection of sku codes */
    public Collection findNewSkuCodes(int days) throws RemoteException;
	 @Deprecated
    /** @return map of sku -&gt; days, where days is the oldness */
    public Map<String, Integer> getSkusOldness() throws RemoteException;
	 @Deprecated
	/** @return collection of sku codes */
    public Collection findReintroducedSkuCodes(int days) throws RemoteException;
	 @Deprecated
	/** @return collection of sku codes */
	public Collection findOutOfStockSkuCodes() throws RemoteException;
	 @Deprecated
	public Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes)throws RemoteException;
	 @Deprecated
	public List findPeakProduceSKUsByDepartment(List skuPrefixes) throws RemoteException;
	 @Deprecated
	public Map<String, Map<String,Date>> getNewSkus() throws RemoteException;
	 @Deprecated
	public Map<String, Map<String,Date>> getBackInStockSkus() throws RemoteException;
	 @Deprecated
	public Map<String, Map<String,Date>> getOverriddenNewSkus() throws RemoteException;
	 @Deprecated
	public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws RemoteException;
	 @Deprecated
	public List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws RemoteException;
	 @Deprecated
	public void refreshNewAndBackViews() throws RemoteException;
	 @Deprecated
	public List<Date> getAvailableDeliveryDates(String materialNumber, int daysInPast) throws RemoteException;
	 @Deprecated
	public void setOverriddenNewness(String sku, Map<String,String> salesAreaOverrides)throws RemoteException;
	 @Deprecated
	public void setOverriddenBackInStock(String sku, Map<String,String> salesAreaOverrides)throws RemoteException;
	 @Deprecated
	public Map<String,String> getOverriddenNewness(String sku)throws RemoteException;
	 @Deprecated
	public Map<String,String> getOverriddenBackInStock(String sku)throws RemoteException;
}

