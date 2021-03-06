package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintInfoModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.customer.ErpCustomerEmailModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.customer.OrderHistoryI;
import com.freshdirect.customer.RedeliverySaleInfo;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDConfiguredProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.payment.EnumPaymentMethodType;


/**
 *
 *
 * @version $Revision:52$
 * @author $Author:Viktor Szathmary$
 */

/**
 *@deprecated Please use the OrderResource in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */

@Deprecated
public interface ErpCustomerManagerSB extends EJBObject {
    
    /**
     * Create an ErpCustomer and enqueue request in SAP.
     *
     * @param erpCustomer ErpCustomerModel
     *
     * @return primary key assigned to ErpCustomer
     */
    public PrimaryKey createCustomer(ErpCustomerModel erpCustomer) throws ErpDuplicateUserIdException, RemoteException;
    
    public PrimaryKey createCustomer(ErpCustomerModel erpCustomer, boolean isGiftCardBuyer) throws ErpDuplicateUserIdException, RemoteException;
    
    /**
     * Creates sale object and enqueues order in SAP.
     *
     * @param erpCustomerPk primary key of ErpCustomer
     * @param order the create order transaction
     *
     * @return erp sale PK
     */
    public PrimaryKey placeOrder(
		PrimaryKey erpCustomerPk,
		ErpCreateOrderModel order,
		Set<String> usedPromotionCodes,
		CustomerRatingI rating,
		CrmAgentRole agentRole,
		String dlvPassId,
		EnumSaleType saleType) throws ErpFraudException, RemoteException;

    
    
    /**
     * Resubmit a failed ("not submitted") order to SAP.
     * If there's an SAP ID assigned, it calls change order, otherwise it calls create.
     *
     * @param saleId
     * @throws ErpTransactionException if the order is not in the NOT_SUBMITTED STATE
     */
    public void resubmitOrder(String saleId,CustomerRatingI cra,EnumSaleType saleType, String regionId) throws ErpTransactionException, RemoteException;
    
    /**
     * Cancels a sale, enqueues request in SAP.
     *
     * @param saleId
     * @return String reservation id of the canceled order so it can be releases
     */
    public String cancelOrder(String saleId, EnumTransactionSource source,String initiator) throws ErpTransactionException, RemoteException;
    
    /**
     * Modifies an order, enqueues request in SAP.
     *
     * @param saleId
     */
    public void modifyOrder(
		String saleId,
		ErpModifyOrderModel order,
		Set<String> usedPromotionCodes,
		CustomerRatingI cra,
		CrmAgentRole agentRole,
		boolean sendToSap) throws ErpTransactionException, ErpFraudException, RemoteException;
    
    /**
     * Check availability for an order.
     *
     * @param erpCustomerPk primary key of ErpCustomer
     * @param order ErpAbstractOrderModel
     *
     * @return Map of order line number -> List of ErpInventoryModel objects
     */
    public Map<String, List<ErpInventoryModel>> checkAvailability(PrimaryKey erpCustomerPk, ErpCreateOrderModel order, long timeout, String isFromLogin) throws RemoteException;
    
    /**
     * Get a specific sale.
     */
    public ErpSaleModel getOrder(PrimaryKey erpSalePk) throws RemoteException;
    
  
    
    /**
     * Add an invoice to ErpSale
     *
     * @parameter ErpInvoiceModel invoice to be added
     * @return void
     */
    public void addInvoice(ErpInvoiceModel invoice, String saleId, ErpShippingInfo shippingInfo) throws ErpTransactionException, RemoteException;
    
    
    public void addAndReconcileInvoice(String saleId, ErpInvoiceModel invoice, ErpShippingInfo shippingInfo) throws ErpTransactionException, RemoteException;
    
    public void reconcileSale(String saleId, Boolean isShorted) throws ErpTransactionException, RemoteException;
     
    public Collection<ModelI> getFailedAuthorizationSales() throws RemoteException;
    
    public PrimaryKey addComplaint(ErpComplaintModel complaint, String saleId, boolean autoApproveAuthorized, Double limit ) throws ErpComplaintException, RemoteException, FDResourceException;
    
    public String approveComplaint(String complaintId, boolean isApproved, String csrId, Double limit) throws ErpComplaintException, RemoteException, FDResourceException;
    
    public void updateCustomerCredit(PrimaryKey pk, String customerCreditId, double delta) throws RemoteException;
    
    public void setActive(PrimaryKey pk, boolean b) throws RemoteException;
    
    public boolean setAlert(PrimaryKey pk, ErpCustomerAlertModel customerAlert, boolean isOnAlert) throws RemoteException;

	public List<ErpCustomerAlertModel> getAlerts(PrimaryKey pk) throws RemoteException;

	public boolean isOnAlert(PrimaryKey pk) throws RemoteException;
	
	public boolean isCustomerActive(PrimaryKey pk) throws RemoteException;

	public boolean isOnAlert(PrimaryKey pk, String alertType) throws RemoteException;

    public ErpDeliveryInfoModel getDeliveryInfo(String saleId) throws ErpSaleNotFoundException, RemoteException;
    
    public void processSaleReturn(String saleId, ErpReturnOrderModel returnOrder) throws ErpTransactionException, RemoteException;
    
    public EnumPaymentResponse resubmitPayment(String saleId, ErpPaymentMethodI paymentMethod, Collection<ErpChargeLineModel> charges) throws ErpTransactionException, RemoteException;
    
    public List<ErpTruckInfo> getTruckNumbersForDate(Date deliveryDate) throws RemoteException;
    
    public DlvSaleInfo getDlvSaleInfo (String orderNumber) throws ErpSaleNotFoundException, RemoteException;
    
    public void markAsReturn(String saleId, boolean fullReturn, boolean alcoholOnly) throws ErpTransactionException, ErpSaleNotFoundException, RemoteException;
    
    public void approveReturn(String saleId, ErpReturnOrderModel returnOrder) throws ErpTransactionException, RemoteException;
     
    public ErpComplaintInfoModel getComplaintInfo(String saleId, String complaintId) throws RemoteException;
     
	public void reverseCustomerCredit(String saleId, String complaintId) throws ErpTransactionException, RemoteException;

	public List<DlvSaleInfo> getOrdersForDateAndAddress(Date date, String address, String zipcode) throws RemoteException;
	
	public void resubmitCustomer(PrimaryKey erpCustomerPk) throws RemoteException;
	
	public void createCase(CrmSystemCaseInfo caseInfo, boolean requiresNewTx) throws RemoteException;
    
    public void updateEmailSentFlag(ErpCustomerEmailModel cem) throws RemoteException;
    
    public void updateWaveInfo(String saleId, ErpShippingInfo shippingInfo) throws RemoteException, FinderException;    
    
    public void updateCartonInfo(String saleId, List<ErpCartonInfo> cartonList) throws RemoteException, FinderException;

	public void updateBadCustomerPaymentMethod(String saleId, EnumPaymentMethodType paymentMethodType, EnumPaymentResponse paymentResponse, String maskedAccountNumber) throws RemoteException; 

	public void removeBadCustomerPaymentMethod(String saleId, EnumPaymentMethodType paymentMethodType, String maskedAccountNumber) throws RemoteException;
	
	public void addChargeInvoice(String saleId, double charge) throws RemoteException;
	
	public void updateDlvPassIdToSale(String saleId, String dlvPassId) throws RemoteException;
	
	public int getValidOrderCount(PrimaryKey erpCustomerPk) throws RemoteException;
	
	public String getLastOrderID(PrimaryKey erpCustomerPk) throws RemoteException;
	
	   /**
     * Get lightweight info about a customer's used promotions.
     *
     * @param erpCustomerPk primary key of ErpCustomer
     *
     * @return ErpPromotionHistory
     * @deprecated
     */
    public ErpPromotionHistory getPromoHistoryInfo(PrimaryKey erpCustomerPk) throws RemoteException;
    
    /**
     * Get lightweight info about a customer's orders.
     *
     * @param erpCustomerPk primary key of ErpCustomer
     *
     * @return collection of ErpSaleInfo objects
     */
    public OrderHistoryI getWebOrderHistoryInfo(PrimaryKey erpCustomerPk) throws RemoteException;
    
	public ErpSaleModel getLastNonCOSOrder(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus, EnumPaymentMethodType paymentType) throws ErpSaleNotFoundException, RemoteException; 
	
	public ErpSaleModel getLastNonCOSOrder(String customerID,	EnumSaleType saleType, EnumSaleStatus saleStatus, List<EnumPaymentMethodType> pymtMethodTypes, EnumEStoreId eStore) throws ErpSaleNotFoundException, RemoteException;
	
	public ErpSaleModel getLastNonCOSOrder(String customerID,	EnumSaleType saleType, List<EnumPaymentMethodType> pymtMethodTypes, EnumEStoreId eStore) throws ErpSaleNotFoundException, RemoteException;
    
    public void sendCreateOrderToSAP(String erpCustomerID, String saleID,EnumSaleType saleType, CustomerRatingI rating) throws RemoteException, ErpSaleNotFoundException;

	public void assignAutoCaseToComplaint(PrimaryKey complaintPk, PrimaryKey autoCasePK) throws RemoteException;

    public void sendCreateDonationOrderToSAP(String erpCustomerID, String saleID, EnumSaleType saleType, CustomerRatingI rating) throws RemoteException, ErpSaleNotFoundException, ErpTransactionException;
    
    public String getSapCustomerId(String erpCustomerPk) throws RemoteException;

    public List<ErpSaleInfo> getNSMOrdersForGC()  throws RemoteException;
    public String getLastOrderID(PrimaryKey erpCustomerPk, EnumEStoreId eStore) throws RemoteException;

    public List<ErpCustomerCreditModel> getCustomerCreditsByErpCustId(String erpCustomerId) throws RemoteException;
    
}
