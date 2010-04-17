package com.freshdirect.customer.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.crm.CrmSystemCaseInfo;
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
import com.freshdirect.giftcard.ErpGiftCardTransModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpChargeInvoiceModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpDeliveryConfirmModel;
import com.freshdirect.giftcard.ErpRegisterGiftCardModel;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpCartonInfo;
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
import com.freshdirect.giftcard.ErpGiftCardAuthModel;
import com.freshdirect.giftcard.ErpPostAuthGiftCardModel;
import com.freshdirect.giftcard.ErpPreAuthGiftCardModel;
import com.freshdirect.giftcard.ErpReverseAuthGiftCardModel;

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
    public void modifyOrder(ErpModifyOrderModel model, Set<String> usedPromotionCodes) throws ErpTransactionException, RemoteException;

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

    public void cancelGCOrder(ErpCancelOrderModel cancelOrder) throws ErpTransactionException, RemoteException;
    
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

	public void emailPending() throws ErpTransactionException, RemoteException;
	
	/**
	 * Add the invoice coming from SAP. The Sale must be in the INPROCESS status.
	 * Status will be ENROUTE after this operation.
	 *
	 * @throws ErpTransactionException on violation of business transaction rules
	 */
    public void addInvoice(ErpInvoiceModel invoiceModel) throws ErpTransactionException, RemoteException;
    
    public void addChargeInvoice(ErpChargeInvoiceModel chargeInvoiceModel) throws ErpTransactionException, RemoteException;

    public List<CrmSystemCaseInfo> reconcileSale() throws ErpTransactionException, RemoteException;

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
	
	public void addRegisterGiftCard(ErpGiftCardTransModel registerGCModel) throws ErpTransactionException, RemoteException;
    /**
     * Get how the current order looks like. Returns the last create or change order transaction.
     */
	public ErpAbstractOrderModel getCurrentOrder() throws RemoteException;

	public List<ErpAuthorizationModel> getApprovedAuthorizations() throws ErpTransactionException, RemoteException;
	
	public List<ErpAuthorizationModel> getAuthorizations() throws RemoteException; 
	
	public double getOutstandingCaptureAmount() throws RemoteException;
	
	public List<ErpCaptureModel> getCaptures()throws ErpTransactionException, RemoteException;

	public List<ErpAuthorizationModel> getFailedAuthorizations() throws RemoteException;

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
	
	public void forceSettlement() throws ErpTransactionException, RemoteException;
	
	public void forceSettlementFailed() throws ErpTransactionException, RemoteException;
	
	public List<ErpSettlementModel> getSettlements() throws ErpTransactionException, RemoteException;
	
	public List<ErpAdjustmentModel> getAdjustments() throws ErpTransactionException, RemoteException;
	
	public void updateShippingInfo(ErpShippingInfo shippingInfo) throws RemoteException;
	
	public void markAsReturn() throws ErpTransactionException, RemoteException;
	
	public void markAsRedelivery() throws ErpTransactionException, RemoteException;
	
	public void markAsEnroute() throws ErpTransactionException, RemoteException;
	
	public void addRedelivery(ErpRedeliveryModel redeliveryModel) throws ErpTransactionException, RemoteException;
	
	public Date getCaptureDate() throws ErpTransactionException, RemoteException;
	
	public void addChargebackReversal(ErpChargebackReversalModel cbkReversal) throws ErpTransactionException, RemoteException;

	public void updateCartonInfo(List<ErpCartonInfo> cartonInfo) throws ErpTransactionException, RemoteException;

	public List<ErpCartonInfo> getCartonInfo() throws ErpTransactionException, RemoteException;
	
	public ErpChargeInvoiceModel getChargeInvoice() throws RemoteException;

	public  boolean getIsChargePayment(String authId) throws  RemoteException;
	
	public  boolean getIsChargePayment(double chargeAmount) throws  RemoteException;

	public boolean hasChargeSettlement()  throws  RemoteException;
	
	public boolean hasFundsRedeposit()  throws  RemoteException;
	
	public EnumTransactionType getCurrentTransactionType()   throws  RemoteException;

	public List<ErpFailedSettlementModel> getFailedSettlements() throws  RemoteException;

	public List<ErpChargeSettlementModel> getChargeSettlements() throws  RemoteException;

	public List<ErpFundsRedepositModel> getFundsRedeposits() throws RemoteException;
	
	public List<ErpFailedChargeSettlementModel> getFailedChargeSettlements() throws RemoteException;

	public void reverseChargePayment() throws ErpTransactionException, RemoteException;
	
	public boolean getIsSettlementFailedAfterSettled() throws RemoteException;
	
	/**
	 * This method updates the delivery pass id of the sale model.
	 * @param dlvPassId
	 * @throws RemoteException
	 */
	public void updateDeliveryPassId(String dlvPassId) throws RemoteException;
	
	public List<ErpPreAuthGiftCardModel> getValidGCAuthorizations(ErpPaymentMethodI pm) throws RemoteException;
	
	public void addGCPreAuthorization(ErpPreAuthGiftCardModel auth) throws ErpTransactionException, RemoteException;
	
	
	public void addGiftCardEmailInfo(ErpGiftCardTransModel emailGCModel) throws ErpTransactionException,RemoteException;
	
	
	public void addGiftCardDeliveryConfirm(ErpGiftCardTransModel registerGCModel) throws ErpTransactionException,RemoteException;
	
	public void setGiftCardRegPending() throws ErpTransactionException,RemoteException;
	
	public void addReverseGCPreAuthorization(ErpReverseAuthGiftCardModel rauth) throws ErpTransactionException, RemoteException;
	
	public void cancelGCPreAuthorization(ErpPreAuthGiftCardModel auth) throws ErpTransactionException, RemoteException;
	
	public ErpRegisterGiftCardModel getRecentRegistration() throws RemoteException;
	
	public List<ErpPreAuthGiftCardModel> getPendingGCAuthorizations(ErpPaymentMethodI pm) throws RemoteException;
	
	public List<ErpPreAuthGiftCardModel> getPendingGCAuthorizations() throws RemoteException;
	
	public List<ErpReverseAuthGiftCardModel> getPendingReverseGCAuthorizations() throws RemoteException;
	
	public List<ErpReverseAuthGiftCardModel> getPendingGCReverseAuths(ErpPaymentMethodI pm) throws RemoteException;
	
	public void updateGCAuthorization(ErpGiftCardAuthModel auth) throws RemoteException;
	
	public void addPostAuthorization(ErpPostAuthGiftCardModel postAuth) throws ErpTransactionException, RemoteException;
	
	public void markAsCapturePending() throws ErpTransactionException, RemoteException;
	
	public void markAsSettlementPending() throws ErpTransactionException, RemoteException;
	
	public boolean hasValidPostAuth(ErpPaymentMethodI pm, String preAuthCode) throws RemoteException;
	
	public List<ErpPreAuthGiftCardModel> getValidGCAuthorizations() throws RemoteException;
	
	public List<ErpPostAuthGiftCardModel> getValidGCPostAuthorizations() throws RemoteException;
	
	public void addGiftCardBalanceTransfer(ErpGiftCardTransModel authorizationModel) throws ErpTransactionException, RemoteException;
	
	public void addDeliveryConfirm(ErpDeliveryConfirmModel deliveryConfirmModel, EnumSaleStatus enumSaleStatus) throws ErpTransactionException, RemoteException;
	
}
