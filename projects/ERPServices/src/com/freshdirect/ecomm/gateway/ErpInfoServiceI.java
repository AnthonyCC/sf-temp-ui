package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.ObjectNotFoundException;

import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpProductInfoModel;

public interface ErpInfoServiceI {
	
	
    public Collection findMaterialsByBatch(int batchNumber) throws RemoteException;
	 
    public Collection findMaterialsBySapId(String sapId) throws RemoteException;
	 
    public Collection findMaterialsBySku(String skuCode) throws RemoteException;
	 
    public Collection findMaterialsByDescription(String description) throws RemoteException;
	 
    public Collection findMaterialsByCharacteristic(String classAndCharName) throws RemoteException;
	 
    public Collection findMaterialsByClass(String className) throws RemoteException;
	 
    public ErpProductInfoModel findProductBySku(String skuCode) throws RemoteException, ObjectNotFoundException;
	 
    public Collection<ErpProductInfoModel> findProductsBySku(String[] skuCodes) throws RemoteException;
	 
    public Collection findProductsBySapId(String sapId) throws RemoteException;
	 
 
    public Collection findProductsByDescription(String description) throws RemoteException;
	 
    public Collection findProductsLikeSku(String skuCode) throws RemoteException;
	 
	public Collection<ErpProductInfoModel> findProductsByUPC(String upc) throws RemoteException;

	public Collection<String> findProductsByCustomerUPC(String erpCustomerPK, String upc) throws RemoteException;

	public Collection findProductsLikeUPC(String searchterm) throws RemoteException;

	 
    /** @return null if there's no inventory for material */
    public ErpInventoryModel getInventoryInfo(String materialNo) throws RemoteException;
	 
    public  Map<String, ErpInventoryModel>  loadInventoryInfo(Date modifiedDate) throws RemoteException;
	 
    /** @return collection of sku codes */
    public Collection<String> findNewSkuCodes(int days) throws RemoteException;
	 
    /** @return map of sku -&gt; days, where days is the oldness */
    public Map<String, Integer> getSkusOldness() throws RemoteException;
	 
	/** @return collection of sku codes */
    public Collection<String> findReintroducedSkuCodes(int days) throws RemoteException;
	 
	/** @return collection of sku codes */
	public Collection<String> findOutOfStockSkuCodes() throws RemoteException;
	 
	public Collection<String> findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes) throws RemoteException;

	 
	public Map<String, Map<String,Date>> getNewSkus() throws RemoteException;
	 
	public Map<String, Map<String,Date>> getBackInStockSkus() throws RemoteException;

 
	public Map<String, Map<String,Date>> getOverriddenNewSkus() throws RemoteException;
	 
	public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws RemoteException;
	 
	public List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws RemoteException;
	 
	public void refreshNewAndBackViews() throws RemoteException;
	 
	public void setOverriddenNewness(String sku, Map<String,String> salesAreaOverrides)throws RemoteException;
	 
	public void setOverriddenBackInStock(String sku, Map<String,String> salesAreaOverrides)throws RemoteException;
	 
	public Map<String,String> getOverriddenNewness(String sku)throws RemoteException;
	 
	public Map<String,String> getOverriddenBackInStock(String sku)throws RemoteException;

}
