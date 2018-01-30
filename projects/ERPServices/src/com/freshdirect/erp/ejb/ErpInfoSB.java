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
public interface ErpInfoSB extends EJBObject {
    
    public Collection findMaterialsByBatch(int batchNumber) throws RemoteException;
    
    public Collection findMaterialsBySapId(String sapId) throws RemoteException;
    
    public Collection findMaterialsBySku(String skuCode) throws RemoteException;
    
    public Collection findMaterialsByDescription(String description) throws RemoteException;
    
    public Collection findMaterialsByCharacteristic(String classAndCharName) throws RemoteException;
    
    public Collection findMaterialsByCharacteristic(String className, String charName) throws RemoteException;
    
    public Collection findMaterialsByClass(String className) throws RemoteException;
    
    public ErpProductInfoModel findProductBySku(String skuCode) throws RemoteException, ObjectNotFoundException;
    
    public ErpProductInfoModel findProductBySku(String skuCode, int version) throws RemoteException, ObjectNotFoundException;

    public Collection findProductsBySku(String[] skuCodes) throws RemoteException;
    
    public Collection findProductsBySapId(String sapId) throws RemoteException;
    
	public Collection findSkusBySapId(String sapId) throws RemoteException;

    
    public Collection findProductsByDescription(String description) throws RemoteException;
    
    public Collection findProductsLikeSku(String skuCode) throws RemoteException;
    
    public Collection findProductsByUPC(String upc) throws RemoteException;
    
    public Collection findProductsByCustomerUPC(String erpCustomerPK, String upc) throws RemoteException;
    
    public Collection findProductsLikeUPC(String upc) throws RemoteException;
    
    /** @return null if there's no inventory for material */
    public ErpInventoryModel getInventoryInfo(String materialNo) throws RemoteException;
    
    public Map loadInventoryInfo(Date modifiedDate) throws RemoteException;
    
    /** @return collection of sku codes */
    public Collection findNewSkuCodes(int days) throws RemoteException;
    
    /** @return map of sku -&gt; days, where days is the oldness */
    public Map<String, Integer> getSkusOldness() throws RemoteException;
    
	/** @return collection of sku codes */
    public Collection findReintroducedSkuCodes(int days) throws RemoteException;

	/** @return collection of sku codes */
	public Collection findOutOfStockSkuCodes() throws RemoteException;
	
	public Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes)throws RemoteException;

	public List findPeakProduceSKUsByDepartment(List skuPrefixes) throws RemoteException;

	public Map<String, Map<String,Date>> getNewSkus() throws RemoteException;

	public Map<String, Map<String,Date>> getBackInStockSkus() throws RemoteException;
	
	public Map<String, Map<String,Date>> getOverriddenNewSkus() throws RemoteException;

	public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws RemoteException;

	public List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws RemoteException;

	public void refreshNewAndBackViews() throws RemoteException;
	
	public List<Date> getAvailableDeliveryDates(String materialNumber, int daysInPast) throws RemoteException;
	
	public void setOverriddenNewness(String sku, Map<String,String> salesAreaOverrides)throws RemoteException;
	
	public void setOverriddenBackInStock(String sku, Map<String,String> salesAreaOverrides)throws RemoteException;
	
	public Map<String,String> getOverriddenNewness(String sku)throws RemoteException;
	
	public Map<String,String> getOverriddenBackInStock(String sku)throws RemoteException;
}

