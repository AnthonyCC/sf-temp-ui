/* Generated by Together */

package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpCancelOrderModel;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpChargeInvoiceModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpDeliveryConfirmModel;
import com.freshdirect.customer.ErpResubmitPaymentModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpReversalModel;
import com.freshdirect.customer.ErpSaleI;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpFailedSettlementModel;
import com.freshdirect.customer.ErpChargeSettlementModel;
import com.freshdirect.customer.ErpFundsRedepositModel;
import com.freshdirect.customer.ErpFailedChargeSettlementModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.framework.core.EntityBeanRemoteI;

/**
 * ErpSale remote interface.
 * @version    $Revision:25$
 * @author     $Author:Viktor Szathmary$
 */
public interface ErpSaleEB extends EntityBeanRemoteI, ErpSaleI {

	/**
	 * Notification that the create order did not make it into SAP.
	 * Status will be NOT_SUBMITTED after this operation.
	 *
	 * @param message detailed description of the error
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
	public void submitFailed(String message) throws ErpTransactionException, RemoteException;

    /**
     * Notification that the create order is successfully completed in SAP.
     * Status will be SUBMITTED after this operation.
     *
     * @param sapOrderNumber order number assigned in SAP
     *
     * @throws ErpTransactionException on violation of business transaction rules
     */
    public void createOrderComplete(String sapOrderNumber) throws ErpTransactionException, RemoteException;

    /**
     * Change the order. Can only change a SUBMITTED order.
     * Status will be MODIFIED after this operation.
     *
     * @throws ErpTransactionException on violation of business transaction rules
     */
    public void modifyOrder(ErpModifyOrderModel model, Set usedPromotionCodes) throws ErpTransactionException, RemoteException;

    /**
     * Notification that the last change order is successfully completed in SAP.
     * Status will be SUBMITTED after this operation.
     *
     * @throws ErpTransactionException on violation of business transaction rules
     */
    public void modifyOrderComplete() throws ErpTransactionException, RemoteException;

    /**
     * Cancel the order.
     * Status will be MODIFIED after this operation.
     *
     * @throws ErpTransactionException on violation of business transaction rules
     */
    public void cancelOrder(ErpCancelOrderModel cancelOrder) throws ErpTransactionException, RemoteException;

    /**
     * Notification that the cancel order is successfully completed in SAP.
     * Status will be CANCELED after this operation.
     *
     * @throws ErpTransactionException on violation of business transaction rules
     */
    public void cancelOrderComplete() throws ErpTransactionException, RemoteException;

	/**
	 * Change the status  after cutoff time. The Sale must be in SUBMITTED status.
     * Status will be INPROCESS after this operation.
	 */
	public void cutoff() throws ErpTransactionException, RemoteException;

	/**
	 * Add the invoice coming from SAP. The Sale must be in the INPROCESS status.
	 * Status will be ENROUTE after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
    public void addInvoice(ErpInvoiceModel invoiceModel) throws ErpTransactionException, RemoteException;
    
    public void addChargeInvoice(ErpChargeInvoiceModel chargeInvoiceModel) throws ErpTransactionException, RemoteException;

    public List reconcileSale() throws ErpTransactionException, RemoteException;

    //public void addPayment(ErpPaymentModel payment) throws ErpTransactionException, RemoteException;

    public void addReturn(ErpReturnOrderModel returnModel) throws ErpTransactionException, RemoteException;

    public void addComplaint(ErpComplaintModel complaintModel) throws ErpTransactionException, RemoteException;

    public void updateComplaint(ErpComplaintModel complaintModel) throws ErpTransactionException, RemoteException;

	public void addAuthorization(ErpAuthorizationModel authorizationModel) throws ErpTransactionException, RemoteException;

	public void addCapture(ErpCaptureModel captureModel) throws ErpTransactionException, RemoteException;
	
	public void addVoidCapture(ErpVoidCaptureModel voidCapture) throws ErpTransactionException, RemoteException;

	public void addManualAuthorization(ErpAuthorizationModel authorizationModel) throws ErpTransactionException, RemoteException;

	public void addReversal(ErpReversalModel reversalModel) throws ErpTransactionException, RemoteException;

	public void addCashback(ErpCashbackModel cashbackModel) throws ErpTransactionException, RemoteException;
	
	public void addDeliveryConfirm(ErpDeliveryConfirmModel deliveryConfirmModel) throws ErpTransactionException, RemoteException;

    /**
     * Get how the current order looks like. Returns the last create or change order transaction.
     */
	public ErpAbstractOrderModel getCurrentOrder() throws RemoteException;

	public List getApprovedAuthorizations() throws ErpTransactionException, RemoteException;
	
	public List getAuthorizations() throws RemoteException; 
	
	public double getOutstandingCaptureAmount() throws RemoteException;
	
	public List getCaptures()throws ErpTransactionException, RemoteException;

	public List getFailedAuthorizations() throws RemoteException;

	public ErpInvoiceModel getInvoice() throws ErpTransactionException, RemoteException;

	public void addSettlement(ErpSettlementModel model) throws ErpTransactionException, RemoteException;

	public void addFailedSettlement(ErpFailedSettlementModel model) throws ErpTransactionException, RemoteException;

	public void addChargeSettlement(ErpChargeSettlementModel model) throws ErpTransactionException, RemoteException;

	public void addFundsRedeposit(ErpFundsRedepositModel model) throws ErpTransactionException, RemoteException;

	public void addFailedChargeSettlement(ErpFailedChargeSettlementModel model) throws ErpTransactionException, RemoteException;

	public void addChargeback(ErpChargebackModel model) throws ErpTransactionException, RemoteException;
	
	public void addAdjustment(ErpAdjustmentModel model) throws ErpTransactionException, RemoteException;
	
	public void addResubmitPayment(ErpResubmitPaymentModel model) throws ErpTransactionException, RemoteException;
	
	public void forcePaymentStatus() throws ErpTransactionException, RemoteException;
	
	public List getSettlements() throws ErpTransactionException, RemoteException;
	
	public List getAdjustments() throws ErpTransactionException, RemoteException;
	
	public void updateShippingInfo(ErpShippingInfo shippingInfo) throws RemoteException;
	
	public void markAsReturn() throws ErpTransactionException, RemoteException;
	
	public void markAsRedelivery() throws ErpTransactionException, RemoteException;
	
	public void markAsEnroute() throws ErpTransactionException, RemoteException;
	
	public void addRedelivery(ErpRedeliveryModel redeliveryModel) throws ErpTransactionException, RemoteException;
	
	public Date getCaptureDate() throws ErpTransactionException, RemoteException;
	
	public void addChargebackReversal(ErpChargebackReversalModel cbkReversal) throws ErpTransactionException, RemoteException;

	public void updateCartonInfo(List cartonInfo) throws ErpTransactionException, RemoteException;

	public List getCartonInfo() throws ErpTransactionException, RemoteException;
	
	public ErpChargeInvoiceModel getChargeInvoice() throws RemoteException;

	public  boolean getIsChargePayment(String authId) throws  RemoteException;
	
	public  boolean getIsChargePayment(double chargeAmount) throws  RemoteException;

	public boolean hasChargeSettlement()  throws  RemoteException;
	
	public boolean hasFundsRedeposit()  throws  RemoteException;
	
	public EnumTransactionType getCurrentTransactionType()   throws  RemoteException;

	public List getFailedSettlements() throws  RemoteException;

	public List getChargeSettlements() throws  RemoteException;

	public List getFundsRedeposits() throws RemoteException;
	
	public List getFailedChargeSettlements() throws RemoteException;

	public void reverseChargePayment() throws ErpTransactionException, RemoteException;
	
	public boolean getIsSettlementFailedAfterSettled() throws RemoteException;
	
	/**
	 * This method updates the delivery pass id of the sale model.
	 * @param dlvPassId
	 * @throws RemoteException
	 */
	public void updateDeliveryPassId(String dlvPassId) throws RemoteException;
}
