package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.fdstore.standingorders.SOResult.Result;
import com.freshdirect.fdstore.standingorders.UnavDetailsReportingBean;
import com.freshdirect.framework.core.PrimaryKey;

public interface FDStandingOrdersServiceI {
	public Collection<FDStandingOrder> loadCustomerStandingOrders(FDIdentity identity) throws FDResourceException, FDInvalidConfigurationException,RemoteException;
	public FDStandingOrder load(PrimaryKey pk) throws FDResourceException, RemoteException;
	public void delete(FDActionInfo info, FDStandingOrder so) throws FDResourceException, RemoteException;
	public String save(FDActionInfo info, FDStandingOrder so, String saleId) throws FDResourceException, RemoteException;
	public void assignStandingOrderToOrder(PrimaryKey salePK, PrimaryKey standingOrderPK) throws FDResourceException, RemoteException;
	public void markSaleAltDeliveryDateMovement(PrimaryKey salePK) throws FDResourceException, RemoteException;
	public void logActivity(ErpActivityRecord record) throws FDResourceException, RemoteException;
//	public FDStandingOrderInfoList getActiveStandingOrdersCustInfo(FDStandingOrderFilterCriteria filter)throws FDResourceException, RemoteException;
//	public void clearStandingOrderErrors(String[] soIDs,String agentId)throws FDResourceException, RemoteException;
	public FDStandingOrderInfoList getFailedStandingOrdersCustInfo()throws FDResourceException, RemoteException;
	public FDStandingOrderInfoList getMechanicalFailedStandingOrdersCustInfo()throws FDResourceException, RemoteException;
//	public Map<Date,Date> getStandingOrdersAlternateDeliveryDates() throws FDResourceException, RemoteException;
//	public List<FDStandingOrderAltDeliveryDate> getStandingOrderAltDeliveryDates() throws FDResourceException, RemoteException;	
//	public void addStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws FDResourceException, RemoteException;	
//	public void updateStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws FDResourceException, RemoteException;
//	public void deleteStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate) throws FDResourceException, RemoteException;
	public boolean lock(FDStandingOrder so, String lockId) throws FDResourceException,RemoteException;
	public boolean unlock(FDStandingOrder so, String lockId) throws FDResourceException,RemoteException;
	
	public String getLockId(String soId) throws FDResourceException,RemoteException;
	public void checkForDuplicateSOInstances(FDIdentity identity) throws FDResourceException,FDInvalidConfigurationException,RemoteException;
	public void insertIntoCoremetricsUserinfo(FDUserI fdUser, int flag) throws FDResourceException, RemoteException;
	public boolean getCoremetricsUserinfo(FDUserI fdUser) throws FDResourceException, RemoteException;
	public Map<Date, List<FDStandingOrderAltDeliveryDate>> getStandingOrdersGlobalAlternateDeliveryDates() throws FDResourceException, RemoteException;
//	public FDStandingOrderAltDeliveryDate getStandingOrderAltDeliveryDateById(String id) throws FDResourceException, RemoteException ;
//	public void deleteStandingOrderAltDeliveryDateById(String[] altIds) throws FDResourceException, RemoteException;
//	public void addStandingOrderAltDeliveryDates(List<FDStandingOrderAltDeliveryDate> altDeliveryDates) throws FDResourceException, RemoteException;
//	public boolean checkIfAlreadyExists(FDStandingOrderAltDeliveryDate altDate) throws FDResourceException,RemoteException;
//	public boolean isValidSoId(String soId) throws FDResourceException,RemoteException;
	
//	public FDStandingOrderSkuResultInfo replaceSkuCode(String existingSku, String replacementSku, String userId) throws FDResourceException,RemoteException;
//	public FDStandingOrderSkuResultInfo validateSkuCode(String existingSku, String replacementSku) throws FDResourceException,RemoteException;
	public void persistUnavailableDetailsToDB(List<Result> resultsList) throws FDResourceException,RemoteException;
	public UnavDetailsReportingBean getDetailsForReportGeneration() throws FDResourceException,RemoteException;
//	public Collection<FDStandingOrder> getStandingOrderDetails(Collection<FDStandingOrder> fdStandingOrders)throws FDResourceException, FDInvalidConfigurationException,RemoteException ;
	public Collection<FDStandingOrder> getValidStandingOrder(FDIdentity identity) throws FDResourceException, RemoteException,FDInvalidConfigurationException;
	public boolean activateStandingOrder(FDStandingOrder so) throws FDResourceException,RemoteException; 
	public boolean checkIfCustomerHasStandingOrder(FDIdentity identity) throws FDResourceException,RemoteException;
	public boolean updateDefaultStandingOrder(String listId,FDIdentity userIdentity)throws FDResourceException,RemoteException;
	public Collection<FDStandingOrder> loadCustomerNewStandingOrders(FDIdentity identity) throws FDResourceException, FDInvalidConfigurationException,RemoteException;
//	public FDStandingOrder loadSOCron(PrimaryKey pk) throws FDResourceException,RemoteException;
	public void turnOffReminderOverLayNewSo(String standingOrderId)throws FDResourceException,RemoteException;
	public void updateSoCartOverlayFirstTimePreferences(String customerId)throws FDResourceException,RemoteException;
	public void updateNewSoFeaturePreferences(String customerId)throws FDResourceException,RemoteException;
	public void updateDeActivatedSOError(String soId)throws FDResourceException,RemoteException;
	public void deleteActivatedSO(FDActionInfo info, FDStandingOrder so, String deleteDate) throws FDResourceException, RemoteException;
	
}
