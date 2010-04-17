package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.giftcard.ErpEmailGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardAuthModel;
import com.freshdirect.giftcard.ErpGiftCardDlvConfirmModel;
import com.freshdirect.giftcard.ErpGiftCardTransModel;
import com.freshdirect.giftcard.ErpPostAuthGiftCardModel;
import com.freshdirect.giftcard.ErpPreAuthGiftCardModel;
import com.freshdirect.giftcard.ErpRegisterGiftCardModel;
import com.freshdirect.giftcard.ErpReverseAuthGiftCardModel;
import com.freshdirect.payment.AuthorizationStrategy;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;

/**
 * ErpSale model class.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpSaleModel extends ModelSupport implements ErpSaleI {
	private static final long serialVersionUID = -5069029856539362509L;

	private static final String AVS_MATCH = "Y";

	private PrimaryKey customerPk;
	private List<ErpTransactionModel> transactions;
	private List<ErpComplaintModel> complaints;
	private EnumSaleStatus status;
	private String sapOrderNumber;
	private ErpShippingInfo shippingInfo;
	private Set<String> usedPromotionCodes;
	private List<ErpCartonInfo> cartonInfo = new ArrayList<ErpCartonInfo>();
	private String deliveryPassId;
	private EnumSaleType type;
	private double subTotal;
	private EnumDeliveryType deliveryType;
	private Date createDate;

	private String standingOrderId;

	/**
	 * @return Returns the deliveryPassId.
	 */
	public String getDeliveryPassId() {
		return deliveryPassId;
	}

	/**
	 * @param deliveryPassId The deliveryPassId to set.
	 */
	public void setDeliveryPassId(String deliveryPassId) {
		this.deliveryPassId = deliveryPassId;
	}


	/**
	 *
	 * @param customerPk
	 * @param order
	 * @param usedPromotionCodes
	 * @param dlvPassId
	 */
	public ErpSaleModel(PrimaryKey customerPk, ErpAbstractOrderModel order, Set<String> usedPromotionCodes, String dlvPassId, EnumSaleType type) {
		this.customerPk = customerPk;
		this.transactions = new ArrayList<ErpTransactionModel>();
		this.transactions.add(order);
		this.status = EnumSaleStatus.NEW;
		this.complaints = new ArrayList<ErpComplaintModel>();
		this.usedPromotionCodes = usedPromotionCodes;
		this.deliveryPassId = dlvPassId;
		this.type=type;
		this.subTotal = order.getSubTotal();
		this.createDate = new Date();
		this.deliveryType = EnumDeliveryType.getDeliveryType("");

	}
	/**
	 *
	 * @param customerPk
	 * @param status
	 * @param transactions
	 * @param complaints
	 * @param sapOrderNumber
	 * @param shippingInfo
	 * @param usedPromotionCodes
	 * @param cartonInfo
	 * @param dlvPassId
	 * @param type
	 * @param standingOrderId ID of StandingOrder that created this sale
	 */
	public ErpSaleModel(PrimaryKey customerPk, EnumSaleStatus status, List<ErpTransactionModel> transactions, List<ErpComplaintModel> complaints, String sapOrderNumber, ErpShippingInfo shippingInfo,
		Set<String> usedPromotionCodes, List<ErpCartonInfo> cartonInfo, String dlvPassId, EnumSaleType type, String standingOrderId) {
		this.customerPk = customerPk;
		this.status = status;
		this.transactions = transactions;
		this.complaints = complaints;
		this.sapOrderNumber = sapOrderNumber;
		this.shippingInfo = shippingInfo;
		this.cartonInfo = cartonInfo;
		this.usedPromotionCodes = usedPromotionCodes;
		this.deliveryPassId = dlvPassId;
		this.type=type;
		this.deliveryType = EnumDeliveryType.getDeliveryType("");
		this.standingOrderId = standingOrderId;
	}

	private boolean isStatus(EnumSaleStatus[] states) {
		for (int i = 0; i < states.length; i++) {
			if (status.equals(states[i])) {
				return true;
			}
		}
		return false;
	}

	private void assertStatus(EnumSaleStatus validState) throws ErpTransactionException {
		if (!status.equals(validState)) {
			throw new ErpTransactionException("Sale " + getPK().getId() + " is " + status + ". Expected " + validState);
		}
	}

	private void assertStatus(EnumSaleStatus[] validStates) throws ErpTransactionException {
		if (!isStatus(validStates)) {
			StringBuffer msg = new StringBuffer();
			msg.append("Sale ").append(getPK().getId());
			msg.append(" is ").append(status);
			msg.append(". Expected ");
			for (int i = 0; i < validStates.length; i++) {
				if (i != 0)
					msg.append(", ");
				msg.append(validStates[i].toString());
			}
			throw new ErpTransactionException(msg.toString());
		}
	}

	private EnumSaleStatus getNextState() {

		if (!isFullyAuthorized()) {
			List<ErpTransactionI> auths = filterTransaction(ErpAuthorizationModel.class);
			if(auths.isEmpty()) {
				return EnumSaleStatus.SUBMITTED;
			}else{
				ErpAuthorizationModel a = (ErpAuthorizationModel) auths.get(auths.size()-1);
				return a.isApproved() ? EnumSaleStatus.SUBMITTED : EnumSaleStatus.AUTHORIZATION_FAILED;
			}
		}

		boolean avsMatch = true;
		for ( ErpAuthorizationModel auth : getApprovedAuthorizations() ) {
			if (!AVS_MATCH.equalsIgnoreCase(auth.getAvs())) {
				avsMatch = false;
				break;
			}
		}
		return avsMatch ? EnumSaleStatus.AUTHORIZED : EnumSaleStatus.AVS_EXCEPTION;
	}

	private boolean isFullyAuthorized() {
		AuthorizationStrategy s = new AuthorizationStrategy(this);
		return s.getOutstandingAuthorizations().isEmpty();
	}

	public  ErpChargeInvoiceModel getLastChargeInvoice () {
		List<ErpTransactionI> lst = filterTransaction(ErpChargeInvoiceModel.class);
		if(lst.isEmpty()) {
			return null;
		}
		return (ErpChargeInvoiceModel) lst.get(lst.size() - 1);
	}

	private List<ErpTransactionI> filterTransaction(Class<? extends ErpTransactionModel> klazz) {
		List<ErpTransactionI> lst = new ArrayList<ErpTransactionI>();

		for( ErpTransactionModel m : transactions ) {
			if( klazz.isInstance( m ) ) {
				lst.add( m );
			}
		}

		Collections.sort(lst, ErpTransactionModel.TX_DATE_COMPARATOR);
		return Collections.unmodifiableList(lst);
	}

	public double getOutstandingCaptureAmount() {
		ErpInvoiceModel inv = getLastInvoice();
		double amount = MathUtil.roundDecimal(inv.getAmount());
		for( ErpCaptureModel cm : getGoodCaptures() ) {
			amount -= MathUtil.roundDecimal(cm.getAmount());
		}

		return MathUtil.roundDecimal(amount);
	}

	public List<ErpCaptureModel> getCaptures(ErpAffiliate affiliate) {
		List<ErpCaptureModel> lst = new ArrayList<ErpCaptureModel>();
		for(ErpCaptureModel capture : getGoodCaptures()) {
			if(capture.getAffiliate().equals(affiliate)) {
				lst.add(capture);
			}
		}
		return lst;
	}

	public List<ErpAuthorizationModel> getApprovedAuthorizations(ErpAffiliate affiliate, ErpPaymentMethodI pm) {
		List<ErpAuthorizationModel> auths = new ArrayList<ErpAuthorizationModel>();

		for(ErpAuthorizationModel auth : getApprovedAuthorizations()) {
			// ErpAuthorizationModel auth = (ErpAuthorizationModel) i.next();
			if(affiliate.equals(auth.getAffiliate())) {
				if(auth.getCardType().equals(pm.getCardType()) && pm.getAccountNumber().endsWith(auth.getCcNumLast4())){
					auths.add(auth);
				}
			}
		}

		return auths;
	}

	public List<ErpAuthorizationModel> getApprovedAuthorizations() {
		List<String> capturedAuthCodes = new ArrayList<String>();
		for (ErpTransactionModel o : transactions) {
			if (o instanceof ErpCaptureModel) {
				capturedAuthCodes.add(((ErpCaptureModel) o).getAuthCode());
			}
		}

		List<ErpAuthorizationModel> lst = new ArrayList<ErpAuthorizationModel>();
		for (ErpTransactionModel o : transactions) {
			if (o instanceof ErpAuthorizationModel) {
				ErpAuthorizationModel auth = (ErpAuthorizationModel) o;

				if (EnumPaymentResponse.APPROVED.equals(auth.getResponseCode())) {
					if (!capturedAuthCodes.contains(auth.getAuthCode())) {
						lst.add(auth);
					}
				}
			}
		}
		return lst;
	}

	public ErpAbstractOrderModel getCurrentOrder() {
		
		ErpAbstractOrderModel lastOrder = null;
		List<ErpTransactionModel> txs = new ArrayList<ErpTransactionModel>(transactions);
		Collections.sort(txs, ErpTransactionI.TX_DATE_COMPARATOR);
		for ( ErpTransactionModel m : txs ) {
			if (m instanceof ErpAbstractOrderModel) {
				lastOrder = (ErpAbstractOrderModel) m;
			}
		}
		return lastOrder;
	}

	public void submitFailed(String message) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.NEW,
				EnumSaleStatus.MODIFIED,
				EnumSaleStatus.MODIFIED_CANCELED,
				EnumSaleStatus.NOT_SUBMITTED,
				EnumSaleStatus.AVS_EXCEPTION });

		ErpSubmitFailedModel m = new ErpSubmitFailedModel();
		m.setTransactionSource(EnumTransactionSource.SYSTEM);
		m.setMessage(message);
		transactions.add(m);
		status = EnumSaleStatus.NOT_SUBMITTED;
	}

	public void createOrderComplete(String sapOrderNumber) throws ErpTransactionException {

		if(EnumSaleType.REGULAR.equals(type)) {
				assertStatus(new EnumSaleStatus[] { EnumSaleStatus.NEW, EnumSaleStatus.NOT_SUBMITTED });
		}
		else if(EnumSaleType.SUBSCRIPTION.equals(type)) {
				assertStatus(new EnumSaleStatus[] { EnumSaleStatus.NEW, EnumSaleStatus.MODIFIED,EnumSaleStatus.NOT_SUBMITTED });
		}

		this.sapOrderNumber = sapOrderNumber;
		status = getNextState();
	}

	public void modifyOrder(ErpModifyOrderModel model, Set<String> usedPromotionCodes) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.SUBMITTED,
				EnumSaleStatus.AUTHORIZED,
				EnumSaleStatus.AUTHORIZATION_FAILED,
				EnumSaleStatus.NOT_SUBMITTED,
				EnumSaleStatus.AVS_EXCEPTION,
				EnumSaleStatus.SETTLEMENT_FAILED
				});
		transactions.add(model);
		if (status != EnumSaleStatus.SETTLEMENT_FAILED) {
			status = EnumSaleStatus.MODIFIED;
		}

		this.usedPromotionCodes = usedPromotionCodes;
	}

	public void modifyOrderComplete() throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] { EnumSaleStatus.MODIFIED, EnumSaleStatus.NOT_SUBMITTED, EnumSaleStatus.AVS_EXCEPTION });

		status = getNextState();
	}

	public void cancelOrder(ErpCancelOrderModel cancelOrder) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.SUBMITTED,
				EnumSaleStatus.AUTHORIZED,
				EnumSaleStatus.LOCKED,
				EnumSaleStatus.AVS_EXCEPTION,
				EnumSaleStatus.AUTHORIZATION_FAILED });

		usedPromotionCodes.clear();
		transactions.add(cancelOrder);
		status = EnumSaleStatus.MODIFIED_CANCELED;
	}

	public void cancelGCOrder(ErpCancelOrderModel cancelOrder) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.ENROUTE});

		usedPromotionCodes.clear();
		transactions.add(cancelOrder);
		status = EnumSaleStatus.MODIFIED_CANCELED;
	}
	
	public void cancelOrderComplete() throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.MODIFIED_CANCELED, EnumSaleStatus.NOT_SUBMITTED });
		status = EnumSaleStatus.CANCELED;
	}

	public void addInvoice(ErpInvoiceModel invoiceModel) throws ErpTransactionException {

		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.INPROCESS, EnumSaleStatus.RETURNED });
		
		transactions.add(invoiceModel);
		if (status.equals(EnumSaleStatus.RETURNED)) {
			ErpAbstractOrderModel orderModel = getCurrentOrder();
			if ((int) Math.round(invoiceModel.getAmount() * 100) > 0) {
				if(orderModel.getAppliedGiftcards().size() > 0){
					//Gift card used on this order. set the status to POST AUTH Pending.
					status =  EnumSaleStatus.POST_AUTH_PENDING;
				} else {
					status = EnumSaleStatus.CAPTURE_PENDING;			
				}
			} else {
				if(orderModel.getAppliedGiftcards().size() > 0){
					//Gift card used on this order. set the status to POST AUTH Pending.
					//so auths get reversed.
					status =  EnumSaleStatus.POST_AUTH_PENDING;
				} else {
					status = EnumSaleStatus.SETTLED;			
				}
			}
		} else {
			status = EnumSaleStatus.ENROUTE;
		}
	}

	public void addChargeInvoice(ErpChargeInvoiceModel chargeInvoiceModel) throws ErpTransactionException {

		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.SUBMITTED, EnumSaleStatus.AUTHORIZED, EnumSaleStatus.SETTLEMENT_FAILED });

		transactions.add(chargeInvoiceModel);
	}

	public List<CrmSystemCaseInfo> reconcileSale() throws ErpTransactionException {

		assertStatus(EnumSaleStatus.ENROUTE);

		ErpAbstractOrderModel orderModel = getCurrentOrder();
		ErpInvoiceModel invoice = getInvoice();

		List<ErpOrderLineModel> orderLines = orderModel.getOrderLines();
		List<ErpInvoiceLineModel> invoiceLines = invoice.getInvoiceLines();
		ReconciliationCaseBuilder caseBuilder = new ReconciliationCaseBuilder(customerPk, getPK(), orderModel);

		if (orderLines.size() != invoiceLines.size()) {
			throw new ErpTransactionException(
				"Sale "
					+ getPK().getId()
					+ " has "
					+ orderLines.size()
					+ " order lines, but invoice contains "
					+ invoiceLines.size());
		}

		for (int i = 0; i < orderLines.size(); i++) {

			ErpOrderLineModel orderLine = orderLines.get(i);
			ErpInvoiceLineModel invoiceLine = null;

			for ( ErpInvoiceLineModel tmp : invoiceLines ) {
				if (tmp.getOrderLineNumber().equals(orderLine.getOrderLineNumber())) {
					invoiceLine = tmp;
					break;
				}
			}

			if (!invoiceLine.getMaterialNumber().equals(orderLine.getMaterialNumber())) {
				throw new ErpTransactionException(
					"Sale "
						+ getPK().getId()
						+ " Material number for orderLineNumber doesnt match: "
						+ orderLine.getOrderLineNumber());
			}

			caseBuilder.reconcile(orderLine, invoiceLine);
		}

		return caseBuilder.getCases();
	}

	public ErpInvoiceModel getInvoice() throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.ENROUTE,
				EnumSaleStatus.PAYMENT_PENDING,
				EnumSaleStatus.SETTLED,
				EnumSaleStatus.REFUSED_ORDER,
				EnumSaleStatus.SETTLEMENT_FAILED,
				EnumSaleStatus.RETURNED,
				EnumSaleStatus.CAPTURE_PENDING,
				EnumSaleStatus.SETTLED_RETURNED,
				EnumSaleStatus.PENDING,
				EnumSaleStatus.REDELIVERY,
				EnumSaleStatus.CHARGEBACK,
				EnumSaleStatus.POST_AUTH_PENDING,
				EnumSaleStatus.SETTLEMENT_PENDING});

		ErpInvoiceModel lastInvoice = null;
		List<ErpTransactionModel> txs = new ArrayList<ErpTransactionModel>(transactions);
		Collections.sort(txs, ErpTransactionI.TX_DATE_COMPARATOR);
		for ( ErpTransactionModel m : txs ) {
			if (m instanceof ErpInvoiceModel) {
				lastInvoice = (ErpInvoiceModel) m;
			}
		}
		if (lastInvoice == null) {
			throw new ErpTransactionException("No invoice in sale " + getPK().getId());
		}
		return lastInvoice;
	}

	public void updateShippingInfo(ErpShippingInfo shippingInfo) {
		this.shippingInfo = shippingInfo;
	}

	public void markAsRedelivery() throws ErpTransactionException {
		assertStatus(EnumSaleStatus.ENROUTE);
		status = EnumSaleStatus.PENDING;
	}

	public void markAsEnroute() throws ErpTransactionException {
		assertStatus(EnumSaleStatus.CAPTURE_PENDING);
		status = EnumSaleStatus.ENROUTE;
	}

	public void markAsReturn() throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.ENROUTE, EnumSaleStatus.REDELIVERY, EnumSaleStatus.PENDING });
		status = EnumSaleStatus.REFUSED_ORDER;
	}

	public void addReturn(ErpReturnOrderModel returnModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.REFUSED_ORDER, EnumSaleStatus.RETURNED });
		transactions.add(returnModel);
		status = EnumSaleStatus.RETURNED;
	}

	public void addRedelivery(ErpRedeliveryModel redeliveryModel) throws ErpTransactionException {
		assertStatus(EnumSaleStatus.PENDING);
		transactions.add(redeliveryModel);
		status = EnumSaleStatus.REDELIVERY;
	}

	public void addDeliveryConfirm(ErpDeliveryConfirmModel deliveryConfirmModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.ENROUTE});	
		transactions.add(deliveryConfirmModel);
		ErpAbstractOrderModel orderModel = getCurrentOrder();
		if(orderModel.getAppliedGiftcards().size() > 0){
			//Gift card used on this order. set the status to POST AUTH Pending.
			status =  EnumSaleStatus.POST_AUTH_PENDING;
		} else {
			status = EnumSaleStatus.CAPTURE_PENDING;			
		}
	}
	
	public void addDeliveryConfirm(ErpDeliveryConfirmModel deliveryConfirmModel, EnumSaleStatus enumSaleStatus) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.ENROUTE});		
		transactions.add(deliveryConfirmModel);
		status = enumSaleStatus;
	}
	
	public void addRegisterGiftCard(ErpGiftCardTransModel registerGCModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] {EnumSaleStatus.ENROUTE, EnumSaleStatus.REG_PENDING });
		transactions.add(registerGCModel);
		//status = EnumSaleStatus.CAPTURE_PENDING;
	}
	
	public ErpRegisterGiftCardModel getRecentRegisteration() {
		
		ErpRegisterGiftCardModel regModel = null;
		List<ErpTransactionModel> txs = new ArrayList<ErpTransactionModel>(transactions);
	    //for(int i=0;i<txs.size();i++) System.out.println("ErpTransactionModel :"+((ErpTransactionModel)txs.get(i)).getTransactionDate());  
		Collections.sort(txs, ErpTransactionI.TX_DATE_COMPARATOR);
		for ( ErpTransactionModel m : txs ) {
			if (m instanceof ErpRegisterGiftCardModel) {
				regModel = (ErpRegisterGiftCardModel) m;
			}
		}
		return regModel;
	}
	
	public void addGiftCardDlvConfirm(ErpGiftCardTransModel registerGCModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.AUTHORIZED, EnumSaleStatus.AVS_EXCEPTION, EnumSaleStatus.ENROUTE,EnumSaleStatus.EMAIL_PENDING });
		transactions.add(registerGCModel);
		status = EnumSaleStatus.CAPTURE_PENDING;
	}
	
	public void addGiftCardEmailInfo(ErpGiftCardTransModel emailGCModel) throws ErpTransactionException {
		//assertStatus(new EnumSaleStatus[] { EnumSaleStatus.AUTHORIZED, EnumSaleStatus.AVS_EXCEPTION, EnumSaleStatus.ENROUTE });
		assertStatus(
		new EnumSaleStatus[] {
				EnumSaleStatus.EMAIL_PENDING,
				EnumSaleStatus.PAYMENT_PENDING,
				EnumSaleStatus.SETTLED,				
				EnumSaleStatus.SETTLEMENT_FAILED,				
				EnumSaleStatus.CAPTURE_PENDING,
				EnumSaleStatus.CHARGEBACK
		 });
		transactions.add(emailGCModel);
		//status = EnumSaleStatus.CAPTURE_PENDING;
	}

	public void addComplaint(ErpComplaintModel complaintModel) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.ENROUTE,
				EnumSaleStatus.PENDING,
				EnumSaleStatus.CAPTURE_PENDING,
				EnumSaleStatus.SETTLED,
				EnumSaleStatus.PAYMENT_PENDING,
				EnumSaleStatus.RETURNED });

		complaints.add(complaintModel);
	}

	public void updateComplaint(ErpComplaintModel newComplaint) throws ErpTransactionException {
		List<EnumSaleStatus> allowedStatus = new ArrayList<EnumSaleStatus>();
		allowedStatus.add(EnumSaleStatus.PENDING);
		allowedStatus.add(EnumSaleStatus.SETTLED);
		allowedStatus.add(EnumSaleStatus.PAYMENT_PENDING);
		allowedStatus.add(EnumSaleStatus.RETURNED);

		if (EnumComplaintStatus.REJECTED.equals(newComplaint.getStatus())) {
			allowedStatus.add(EnumSaleStatus.ENROUTE);
		}

		assertStatus(allowedStatus.toArray(new EnumSaleStatus[allowedStatus.size()]));

		ErpComplaintModel oldComplaint = null;
		for (ListIterator<ErpComplaintModel> it = complaints.listIterator(); it.hasNext();) {
			oldComplaint = it.next();
			if (oldComplaint.getPK().getId().equals(newComplaint.getPK().getId())) {
				//
				// We do not allow changes to complaint amounts, so make sure the values
				// for the old model matches the new model's value
				//
				if (oldComplaint.getAmount() != newComplaint.getAmount()) {
					throw new ErpTransactionException(
						"Sale "
							+ getPK().getId()
							+ " Updated complaint amount does not match existing complaint amount. Existing:"
							+ oldComplaint.getAmount()
							+ ", updated:"
							+ newComplaint.getAmount());
				}
				if (oldComplaint.getComplaintLines().size() != newComplaint.getComplaintLines().size()
					|| !oldComplaint.getCreatedBy().equalsIgnoreCase(newComplaint.getCreatedBy())
					|| !oldComplaint.getCreateDate().equals(newComplaint.getCreateDate())) {

					throw new ErpTransactionException(
						"Sale " + getPK().getId() + " Updated complaint does not match existing complaint.");

				}
				it.remove();
				it.add(newComplaint);
				break;
			}
		}
	}

	public void addChargeback(ErpChargebackModel chargebackModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.SETTLED, EnumSaleStatus.CHARGEBACK });
		copyLastCapturePaymentMethodInfo(chargebackModel);
		transactions.add(chargebackModel);
		status = EnumSaleStatus.CHARGEBACK;
	}

	public void addChargebackReversal(ErpChargebackReversalModel cbkReversal) throws ErpTransactionException {
		assertStatus(EnumSaleStatus.CHARGEBACK);
		copyLastCapturePaymentMethodInfo(cbkReversal);
		transactions.add(cbkReversal);
		double cbk = 0.0;
		double cbr = 0.0;
		for (ErpTransactionModel o : transactions) {
			if(o instanceof ErpChargebackModel){
				cbk = MathUtil.roundDecimal(cbk + ((ErpChargebackModel)o).getAmount());
			}

			if(o instanceof ErpChargebackReversalModel){
				cbr = MathUtil.roundDecimal(cbr + ((ErpChargebackReversalModel)o).getAmount());
			}
		}
		if(cbr >= cbk){
			status = EnumSaleStatus.SETTLED;
		}
	}

	public void addAdjustment(ErpAdjustmentModel adjustmentModel) throws ErpTransactionException {
		assertStatus(EnumSaleStatus.PAYMENT_PENDING);
		copyLastCapturePaymentMethodInfo(adjustmentModel);
		transactions.add(adjustmentModel);
		status = EnumSaleStatus.SETTLEMENT_FAILED;
	}

	public void addResubmitPayment(ErpResubmitPaymentModel model) throws ErpTransactionException {
		assertStatus(EnumSaleStatus.SETTLEMENT_FAILED);
		transactions.add(model);
	}

	public void addPreAuthorization(ErpPreAuthGiftCardModel preAuth) throws ErpTransactionException {
		assertStatus(

				new EnumSaleStatus[] {
				EnumSaleStatus.NEW,
				EnumSaleStatus.SUBMITTED,
				EnumSaleStatus.AUTHORIZATION_FAILED,
				EnumSaleStatus.AUTHORIZED,
				EnumSaleStatus.MODIFIED
				//EnumSaleStatus.ENROUTE, //Enroute and Refused order is required in the case of renew auth < 48 hrs.
				//EnumSaleStatus.REFUSED_ORDER
				});
		transactions.add(preAuth);
		
	}

	public void addCancelPreAuthorization(ErpReverseAuthGiftCardModel cancelAuth) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.NEW,
				EnumSaleStatus.SUBMITTED,
				EnumSaleStatus.AUTHORIZATION_FAILED,
				EnumSaleStatus.AUTHORIZED,
				EnumSaleStatus.MODIFIED
				//EnumSaleStatus.ENROUTE, //Enroute and Refused order is required in the case of renew auth < 48 hrs.
				//EnumSaleStatus.REFUSED_ORDER
				});
		transactions.add(cancelAuth);
		
	}

	public void addPostAuthorization(ErpPostAuthGiftCardModel postAuth) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.POST_AUTH_PENDING,
				});
		transactions.add(postAuth);
		
	}
	
	public void addAuthorization(ErpAuthorizationModel auth) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.NEW,
				EnumSaleStatus.SUBMITTED,
				EnumSaleStatus.AUTHORIZATION_FAILED,
				EnumSaleStatus.INPROCESS_NO_AUTHORIZATION,
				EnumSaleStatus.MODIFIED,
				EnumSaleStatus.SETTLEMENT_FAILED
				});

		if(EnumSaleStatus.SETTLEMENT_FAILED.equals(status) && !auth.isApproved()) {
			throw new ErpTransactionException("Cannot add failed authorization in SETTLEMENT FAILED status");
		}

		transactions.add(auth);

		boolean fullyAuthorized = isFullyAuthorized();

		if(!fullyAuthorized && auth.isApproved()) {
			return;
		} else if(!auth.isApproved()) {
			if( EnumSaleStatus.SUBMITTED.equals(status)||
                ((EnumSaleStatus.NEW.equals(status)||EnumSaleStatus.MODIFIED.equals(status)) && EnumSaleType.SUBSCRIPTION.equals(type))) {
                    status = EnumSaleStatus.AUTHORIZATION_FAILED;
            }


		} else {
			if(EnumSaleStatus.INPROCESS_NO_AUTHORIZATION.equals(status)) {
				status = EnumSaleStatus.INPROCESS;
			}else if(EnumSaleStatus.AUTHORIZATION_FAILED.equals(status)
				|| EnumSaleStatus.SUBMITTED.equals(status)){
				if(auth.hasAvsMatched()) {
					status = EnumSaleStatus.AUTHORIZED;
				}else{
					status = EnumSaleStatus.AVS_EXCEPTION;
				}
			}

		}
	}

	public void addSettlement(ErpSettlementModel settlementModel) throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] { EnumSaleStatus.PAYMENT_PENDING, EnumSaleStatus.SETTLED, EnumSaleStatus.SETTLEMENT_FAILED });

		copyLastCapturePaymentMethodInfo(settlementModel);

		transactions.add(settlementModel);

		if (!status.equals(EnumSaleStatus.SETTLED)) {
			double capturedAmt = 0.0;
			for ( ErpCaptureModel m : getGoodCaptures() ) {
				double amount = m.getAmount();
				if (!getIsChargePayment(amount)) { // don't include any settlements pertaining to charges (i.e bounced check fee)
					capturedAmt += amount;
				}
			}

			double settledAmt = 0.0;
			double stlFail = 0.0;
			for (ErpTransactionModel o : transactions) {
				if (o instanceof ErpSettlementModel) {
					settledAmt = MathUtil.roundDecimal(settledAmt + ((ErpSettlementModel) o).getAmount());
				}
				if(o instanceof ErpFailedSettlementModel) {
					stlFail = MathUtil.roundDecimal(stlFail + ((ErpFailedSettlementModel)o).getAmount());
				}
			}

			int settle = (int) Math.round(MathUtil.roundDecimal(settledAmt - stlFail) * 100);
			int capture = (int) Math.round(capturedAmt * 100);
			if (settle >= capture) {
				status = EnumSaleStatus.SETTLED;
			}
		}
	}

	public void addFailedSettlement(ErpFailedSettlementModel failedSettlementModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.PAYMENT_PENDING, EnumSaleStatus.SETTLED, EnumSaleStatus.SETTLEMENT_FAILED });
		copyLastCapturePaymentMethodInfo(failedSettlementModel);
		transactions.add(failedSettlementModel);
		status = EnumSaleStatus.SETTLEMENT_FAILED;
	}

	public void addChargeSettlement(ErpChargeSettlementModel chargeSettlementModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.PAYMENT_PENDING, EnumSaleStatus.SETTLED, EnumSaleStatus.SETTLEMENT_FAILED });
		copyLastCapturePaymentMethodInfo(chargeSettlementModel);
		transactions.add(chargeSettlementModel);
	}

	public void addFundsRedeposit(ErpFundsRedepositModel fundsRedepositModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.PAYMENT_PENDING, EnumSaleStatus.SETTLED });
		copyLastCapturePaymentMethodInfo(fundsRedepositModel);
		transactions.add(fundsRedepositModel);
	}

	public void addFailedChargeSettlement(ErpFailedChargeSettlementModel failedChargeSettlementModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.PAYMENT_PENDING, EnumSaleStatus.SETTLED, EnumSaleStatus.SETTLEMENT_FAILED });
		copyLastCapturePaymentMethodInfo(failedChargeSettlementModel);
		transactions.add(failedChargeSettlementModel);
	}

	public void addManualAuthorization(ErpAuthorizationModel authorizationModel) throws ErpTransactionException {
		assertStatus(EnumSaleStatus.AUTHORIZATION_FAILED);
		transactions.add(authorizationModel);
	}

	public void addReversal(ErpReversalModel reversalModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.AUTHORIZED, EnumSaleStatus.AVS_EXCEPTION });
		transactions.add(reversalModel);
	}

	public void addCashback(ErpCashbackModel cashbackModel) throws ErpTransactionException {
		assertStatus(EnumSaleStatus.SETTLED);
		transactions.add(cashbackModel);
	}

	public void addCapture(ErpCaptureModel captureModel) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[]
			{EnumSaleStatus.ENROUTE,
			 EnumSaleStatus.CAPTURE_PENDING,
			 EnumSaleStatus.PENDING,
			 EnumSaleStatus.REDELIVERY,
			 EnumSaleStatus.PAYMENT_PENDING,
			 EnumSaleStatus.SETTLEMENT_FAILED
			 });

		transactions.add(captureModel);

		// Check to see if enough of the authorized amount has been captured to cover invoice amount and if
		// this is true change the status of the sale.

		double invoiceAmt = getInvoice().getAmount();

		double capturedAmt = 0.0;
		for (ErpTransactionModel o : transactions) {
			if (o instanceof ErpCaptureModel) {
				capturedAmt += ((ErpCaptureModel) o).getAmount();
			}
		}
		int capture = (int) Math.round(capturedAmt * 100);
		int invoice = (int) Math.round(invoiceAmt * 100);
		if (capture >= invoice) {
			status = EnumSaleStatus.PAYMENT_PENDING;
		}
	}

	public void addVoidCapture(ErpVoidCaptureModel voidCapture) throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] {EnumSaleStatus.PAYMENT_PENDING, EnumSaleStatus.ENROUTE});
		// if status is ENROUTE already nothing needs to be done.  All captures have been voided at this point
		if (status != EnumSaleStatus.ENROUTE) {
			transactions.add(voidCapture);
			//Check that every capture has a void capture and if its equal flip the sale status back to EN-ROUTE
			int captures = getCaptures().size();
			int voidCaptures = getVoidCaptures().size();
			if (captures == voidCaptures) {
				status = EnumSaleStatus.ENROUTE;
			}
		}
	}

	public java.util.Date getCaptureDate() throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.PAYMENT_PENDING, EnumSaleStatus.SETTLED });

		java.util.Date captureDate = null;
		for (ErpTransactionModel obj : transactions) {
			if (obj instanceof ErpCaptureModel) {
				captureDate = ((ErpCaptureModel) obj).getTransactionDate();
				break;
			}
		}
		if (captureDate == null) {
			throw new ErpTransactionException("No Capture Found");
		}
		return captureDate;
	}

	public List<ErpCaptureModel> getCaptures() {
		List<ErpCaptureModel> captures = new ArrayList<ErpCaptureModel>();
		for (ErpTransactionModel o : transactions) {
			if (o instanceof ErpCaptureModel) {
				captures.add((ErpCaptureModel) o);
			}
		}
		return captures;
	}

	public List<ErpVoidCaptureModel> getVoidCaptures() {
		List<ErpVoidCaptureModel> voidCaptures = new ArrayList<ErpVoidCaptureModel>();
		for (ErpTransactionModel o : transactions) {
			if (o instanceof ErpVoidCaptureModel) {
				voidCaptures.add((ErpVoidCaptureModel) o);
			}
		}
		return voidCaptures;
	}

	public List<ErpSettlementModel> getSettlements() throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.SETTLED, EnumSaleStatus.SETTLEMENT_FAILED, EnumSaleStatus.CHARGEBACK });
		List<ErpSettlementModel> settlements = new ArrayList<ErpSettlementModel>();
		for ( ErpTransactionModel o : transactions ) {
			if (o instanceof ErpSettlementModel) {
				settlements.add((ErpSettlementModel) o);
			}
		}
		return settlements;
	}

	public List<ErpAdjustmentModel> getAdjustments() throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.SETTLED, EnumSaleStatus.SETTLEMENT_FAILED });
		List<ErpAdjustmentModel> adjustments = new ArrayList<ErpAdjustmentModel>();
		for ( ErpTransactionModel o : transactions ) {
			if (o instanceof ErpAdjustmentModel) {
				adjustments.add((ErpAdjustmentModel) o);
			}
		}
		return adjustments;
	}

	public void forcePaymentStatus() throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.ENROUTE,
				EnumSaleStatus.PAYMENT_PENDING,
				EnumSaleStatus.CAPTURE_PENDING,
				EnumSaleStatus.PENDING,
				EnumSaleStatus.REDELIVERY });

		ErpInvoiceModel invoice = getInvoice();
		if (((int) Math.round(invoice.getAmount() * 100)) == 0) {
			status = EnumSaleStatus.SETTLED;
		} else {
			status = EnumSaleStatus.PAYMENT_PENDING;
		}
	}

	public void forceSettlement() throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
					EnumSaleStatus.SETTLEMENT_PENDING
			});

		status = EnumSaleStatus.SETTLED;
	}
	
	public void forceSettlementFailed() throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
					EnumSaleStatus.SETTLEMENT_PENDING
			});

		status = EnumSaleStatus.SETTLEMENT_FAILED;
	}
	
	public void cutoff() throws ErpTransactionException {
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.AUTHORIZED, EnumSaleStatus.AVS_EXCEPTION });
		status = EnumSaleStatus.INPROCESS;
	}


	public void emailPending() throws ErpTransactionException {		
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.ENROUTE, EnumSaleStatus.REG_PENDING});
		status = EnumSaleStatus.EMAIL_PENDING;
	}


	public void setGiftCardRegPending() throws ErpTransactionException {		
		assertStatus(new EnumSaleStatus[] { EnumSaleStatus.AUTHORIZED, EnumSaleStatus.AVS_EXCEPTION, EnumSaleStatus.ENROUTE });
		status = EnumSaleStatus.REG_PENDING;
	}

	
	
	public PrimaryKey getCustomerPk() {
		return customerPk;
	}

	public EnumSaleStatus getStatus() {
		return status;
	}

	public String getSapOrderNumber() {
		return sapOrderNumber;
	}

	public Collection<ErpTransactionModel> getTransactions() {
		return Collections.unmodifiableCollection(transactions);
	}

	public ErpAbstractOrderModel getRecentOrderTransaction() {
		// get transaction sorted by date
		List<ErpTransactionModel> txList = new ArrayList<ErpTransactionModel>(transactions);
		Collections.sort(txList, ErpTransactionModel.TX_DATE_COMPARATOR);

		// find the current state (last create or modify tx)
		ErpAbstractOrderModel lastOrder = null;
		for ( ErpTransactionModel o : txList ) {
			if (o instanceof ErpAbstractOrderModel) {
				lastOrder = (ErpAbstractOrderModel) o;
			}
		}
		return lastOrder;
	}

	public Collection<ErpComplaintModel> getComplaints() {
		return Collections.unmodifiableCollection(complaints);
	}

	public ErpComplaintModel getComplaint(String complaintId) {
		for (ErpComplaintModel cm : complaints) {
			if (cm.getPK().getId().equals(complaintId)) {
				return cm;
			}
		}
		return null;
	}

	public List<ErpAuthorizationModel> getAuthorizations() {
		List<ErpAuthorizationModel> auths = new ArrayList<ErpAuthorizationModel>();
		for ( ErpTransactionModel m : transactions ) {
			if ( m instanceof ErpAuthorizationModel ) {
				auths.add( (ErpAuthorizationModel)m );
			}
		}
		return Collections.unmodifiableList(auths);
	}

	public ErpGiftCardDlvConfirmModel getGCDeliveryConfirmation(){
		for ( ErpTransactionModel m : transactions ) {
			if ( m instanceof ErpGiftCardDlvConfirmModel ) {
				return (ErpGiftCardDlvConfirmModel)m;
			}
		}
		return null;		
	}

	public List<ErpEmailGiftCardModel> getGCResendEmailTransaction(){
		List<ErpEmailGiftCardModel> resendTransactions = new ArrayList<ErpEmailGiftCardModel>();
		for ( ErpTransactionModel m : transactions ) {
			if ( m instanceof ErpEmailGiftCardModel ) {
				resendTransactions.add( (ErpEmailGiftCardModel)m );
			}
		}
		return Collections.unmodifiableList(resendTransactions);
		
	}
	
	public List<ErpGiftCardAuthModel> getGCTransactions() {
		List<ErpGiftCardAuthModel> auths = new ArrayList<ErpGiftCardAuthModel>();
		for ( ErpTransactionModel m : transactions ) {
			if (m instanceof ErpPreAuthGiftCardModel ||
					m instanceof ErpReverseAuthGiftCardModel ||
					m instanceof ErpPostAuthGiftCardModel) {
					auths.add( (ErpGiftCardAuthModel)m );
			}
		}
		return Collections.unmodifiableList(auths);
	}

	public List<ErpPreAuthGiftCardModel> getGCAuthorizations(ErpPaymentMethodI pm) {
		List<ErpPreAuthGiftCardModel> auths = new ArrayList<ErpPreAuthGiftCardModel>();
		for ( ErpTransactionModel m : transactions ) {
			if (m instanceof ErpPreAuthGiftCardModel) {
				ErpPreAuthGiftCardModel auth = (ErpPreAuthGiftCardModel)m;
				if(auth.getCertificateNum().equals(pm.getCertificateNumber()))
					auths.add(auth);
			}
		}
		return Collections.unmodifiableList(auths);
	}
	
	public List<Object> getGCReverseAuthorizations(ErpPaymentMethodI pm) {
		List<Object> auths = new ArrayList<Object>();
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpReverseAuthGiftCardModel) {
				ErpReverseAuthGiftCardModel rauth = (ErpReverseAuthGiftCardModel)obj;
				if(rauth.getCertificateNum().equals(pm.getCertificateNumber()))
					auths.add(obj);

			}
		}
		return Collections.unmodifiableList(auths);
	}
	
	private List<String> getGCReversePreAuthcodes(ErpPaymentMethodI pm) {
		List<String> authCodes = new ArrayList<String>();
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpReverseAuthGiftCardModel) {
				ErpReverseAuthGiftCardModel rauth = (ErpReverseAuthGiftCardModel)obj;
				if(rauth.getCertificateNum().equals(pm.getCertificateNumber()))
					authCodes.add(rauth.getPreAuthCode());
			}
		}
		return Collections.unmodifiableList(authCodes);
	}

	private List<String> getGCReversePreAuthcodes() {
		List<String> authCodes = new ArrayList<String>();
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpReverseAuthGiftCardModel) {
				ErpReverseAuthGiftCardModel rauth = (ErpReverseAuthGiftCardModel)obj;
				authCodes.add(rauth.getPreAuthCode());
			}
		}
		return Collections.unmodifiableList(authCodes);
	}
	
	public boolean hasValidPostAuth(ErpPaymentMethodI pm, String preAuthCode) {
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpPostAuthGiftCardModel) {
				ErpPostAuthGiftCardModel pauth = (ErpPostAuthGiftCardModel)obj;
				if(pauth.getCertificateNum().equals(pm.getCertificateNumber()) && pauth.getPreAuthCode().equals(preAuthCode))
					return true;
			}
		}
		return false;
	}
	
	public List<ErpPreAuthGiftCardModel> getPendingGCAuthorizations(ErpPaymentMethodI pm) {
		List<ErpPreAuthGiftCardModel> pAuths = new ArrayList<ErpPreAuthGiftCardModel>();
		List<ErpPreAuthGiftCardModel> auths = getGCAuthorizations(pm);
		for ( ErpPreAuthGiftCardModel auth : auths ) {
			if(auth.getCertificateNum().equals(pm.getCertificateNumber())
						&& auth.isPending()){
				pAuths.add(auth);
			}
		}
		return Collections.unmodifiableList(pAuths);
	}
	
	public List<ErpPreAuthGiftCardModel> getPendingGCAuthorizations() {
		List<ErpPreAuthGiftCardModel> pAuths = new ArrayList<ErpPreAuthGiftCardModel>();
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpPreAuthGiftCardModel) {
				ErpPreAuthGiftCardModel auth = (ErpPreAuthGiftCardModel)obj;
				if(auth.isPending())
					pAuths.add(auth);
			}
		}
		return Collections.unmodifiableList(pAuths);
		
	}
	
	public List<ErpReverseAuthGiftCardModel> getPendingReverseGCAuthorizations() {
		List<ErpReverseAuthGiftCardModel> pAuths = new ArrayList<ErpReverseAuthGiftCardModel>();
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpReverseAuthGiftCardModel) {
				ErpReverseAuthGiftCardModel auth = (ErpReverseAuthGiftCardModel)obj;
				if(auth.isPending())
					pAuths.add(auth);
			}
		}
		return Collections.unmodifiableList(pAuths);
		
	}
	
	public List<ErpPreAuthGiftCardModel> getValidGCAuthorizations() {
		List<ErpPreAuthGiftCardModel> pAuths = new ArrayList<ErpPreAuthGiftCardModel>();
		List<String> reverseAuthCodes = getGCReversePreAuthcodes();
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpPreAuthGiftCardModel) {
				ErpPreAuthGiftCardModel auth = (ErpPreAuthGiftCardModel)obj;
				if(!reverseAuthCodes.contains(auth.getAuthCode())&& !auth.isCancelled() && !auth.isDeclined())
					pAuths.add(auth);
			}
		}
		return Collections.unmodifiableList(pAuths);
	}
	public List<ErpPostAuthGiftCardModel> getValidGCPostAuthorizations() {
		List<ErpPostAuthGiftCardModel> pAuths = new ArrayList<ErpPostAuthGiftCardModel>();
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpPostAuthGiftCardModel) {
				ErpPostAuthGiftCardModel pauth = (ErpPostAuthGiftCardModel)obj;
				if(pauth.isApproved() || pauth.isDeclined())
					pAuths.add(pauth);
			}
		}
		return Collections.unmodifiableList(pAuths);
	}
	public List<ErpReverseAuthGiftCardModel> getPendingGCReverseAuths(ErpPaymentMethodI pm) {
		List<ErpReverseAuthGiftCardModel> rauths = new ArrayList<ErpReverseAuthGiftCardModel>();
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpReverseAuthGiftCardModel) {
				ErpReverseAuthGiftCardModel rauth = (ErpReverseAuthGiftCardModel)obj;
				if(rauth.getCertificateNum().equals(pm.getCertificateNumber()) && rauth.isPending())
					rauths.add(rauth);

			}
		}
		return Collections.unmodifiableList(rauths);
	}
	
	public List<ErpPreAuthGiftCardModel> getValidGCAuthorizations(ErpPaymentMethodI pm) {
		List<ErpPreAuthGiftCardModel> vAuths = new ArrayList<ErpPreAuthGiftCardModel>();
		List<ErpPreAuthGiftCardModel> auths = getGCAuthorizations(pm);
		List<String> reverseAuthCodes = getGCReversePreAuthcodes(pm);
		for ( ErpPreAuthGiftCardModel auth : auths ) {
			if(auth.getCertificateNum().equals(pm.getCertificateNumber()) && !reverseAuthCodes.contains(auth.getAuthCode()) 
						&& !auth.isCancelled() && !auth.isDeclined()){
				vAuths.add(auth);
			}
		}
		return Collections.unmodifiableList(vAuths);
	}
	
	public void addGCAuthorization(ErpPreAuthGiftCardModel auth) throws ErpTransactionException{
		assertStatus(
				new EnumSaleStatus[] {
					EnumSaleStatus.NEW,
					EnumSaleStatus.SUBMITTED,
					EnumSaleStatus.AUTHORIZED,
					EnumSaleStatus.MODIFIED,
					});
		transactions.add(auth);
	}
	
	
	public void addGCBalanceTransfer(ErpGiftCardTransModel auth) throws ErpTransactionException{
		assertStatus(
				new EnumSaleStatus[] {					
					EnumSaleStatus.SETTLED,
					EnumSaleStatus.CAPTURE_PENDING,
					EnumSaleStatus.PAYMENT_PENDING
					});
		transactions.add(auth);
	}
	
	
	public void cancelGCAuthorization(ErpPreAuthGiftCardModel auth) throws ErpTransactionException{
		assertStatus(
				new EnumSaleStatus[] {
					EnumSaleStatus.NEW,
					EnumSaleStatus.SUBMITTED,
					EnumSaleStatus.AUTHORIZED,
					EnumSaleStatus.MODIFIED,
					EnumSaleStatus.MODIFIED_CANCELED,
					EnumSaleStatus.CANCELED					
					});
		auth.setPostedTime(new Date());
		int i = transactions.indexOf(auth);
		auth.setGcTransactionStatus(EnumGiftCardTransactionStatus.CANCEL);
		transactions.set(i, auth);
	}
	
	public void addReverseGCAuthorization(ErpReverseAuthGiftCardModel rauth) throws ErpTransactionException{
		assertStatus(
				new EnumSaleStatus[] {
					EnumSaleStatus.NEW,
					EnumSaleStatus.SUBMITTED,
					EnumSaleStatus.AUTHORIZED,
					EnumSaleStatus.MODIFIED,
					EnumSaleStatus.MODIFIED_CANCELED,					
					EnumSaleStatus.CANCELED,			
					EnumSaleStatus.POST_AUTH_PENDING
					});
		transactions.add(rauth);
	}
	
	public void updateGCAuthorization(ErpGiftCardAuthModel auth) throws ErpTransactionException{
		assertStatus(
				new EnumSaleStatus[] {
					EnumSaleStatus.NEW,
					EnumSaleStatus.SUBMITTED,
					EnumSaleStatus.AUTHORIZED,
					EnumSaleStatus.AVS_EXCEPTION,
					EnumSaleStatus.MODIFIED,
					EnumSaleStatus.CANCELED,
					EnumSaleStatus.POST_AUTH_PENDING
					});
		auth.setPostedTime(new Date());
		int i = transactions.indexOf(auth);
		transactions.set(i, auth);
	}

	public void markAsCapturePending() throws ErpTransactionException {
		assertStatus(EnumSaleStatus.POST_AUTH_PENDING);
		status = EnumSaleStatus.CAPTURE_PENDING;
	} 

	/*
	 * This method is only used in the case of gro orders that has gift payments only.
	 */
	public void markAsSettlementPending() throws ErpTransactionException {
		assertStatus(EnumSaleStatus.PAYMENT_PENDING);
		status = EnumSaleStatus.SETTLEMENT_PENDING;
	}
	
	public List<ErpAuthorizationModel> getFailedAuthorizations() {
		List<ErpAuthorizationModel> failedAuths = new ArrayList<ErpAuthorizationModel>();
		ErpAuthorizationModel authorization = null;
		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpAuthorizationModel) {
				authorization = (ErpAuthorizationModel) obj;
				if (!EnumPaymentResponse.APPROVED.equals(authorization.getResponseCode())) {
					failedAuths.add(authorization);
				}
			}
		}
		return Collections.unmodifiableList(failedAuths);
	}

	public boolean hasInvoice() {
		for ( ErpTransactionModel m : transactions ) {
			if ( m instanceof ErpInvoiceModel ) {
				return true;
			}
		}
		return false;
	}

	public List<ErpInvoiceModel> getInvoices() {

		List<ErpInvoiceModel> invoices = new ArrayList<ErpInvoiceModel>();
		for ( ErpTransactionModel m : transactions ) {
			if ( m instanceof ErpInvoiceModel ) {
				invoices.add( (ErpInvoiceModel)m );
			}
		}

		Collections.sort(invoices, ErpTransactionModel.TX_DATE_COMPARATOR);
		return Collections.unmodifiableList(invoices);
	}

	public ErpInvoiceModel getFirstInvoice() {
		List<ErpInvoiceModel> invoices = getInvoices();
		if (invoices.size() == 0) {
			return null;
		}

		return invoices.get(0);
	}

	public ErpInvoiceModel getLastInvoice() {
		List<ErpInvoiceModel> invoices = getInvoices();
		if (invoices.isEmpty()) {
			return null;
		}
		return invoices.get(invoices.size() - 1);
	}

	public int getNumberOfInvoices() {
		return getInvoices().size();
	}

	public ErpShippingInfo getShippingInfo() {
		return shippingInfo;
	}

	public Set<String> getUsedPromotionCodes() {
		return Collections.<String>unmodifiableSet(usedPromotionCodes);
	}
	public boolean hasUsedPromotionCodes() {
		if((usedPromotionCodes!=null))
			return true;
		return false;
	}
	public List<ErpCartonInfo> getCartonInfo() {
		return cartonInfo;
	}


	public ErpChargeInvoiceModel getChargeInvoice() {

		ErpChargeInvoiceModel lastChargeInvoice = null;
		List<ErpTransactionModel> txs = new ArrayList<ErpTransactionModel>(transactions);
		Collections.sort(txs, ErpTransactionI.TX_DATE_COMPARATOR);
		for ( ErpTransactionModel o : txs ) {
			if (o instanceof ErpChargeInvoiceModel) {
				lastChargeInvoice = (ErpChargeInvoiceModel) o;
			}
		}
		return lastChargeInvoice;
	}

	public boolean getIsChargePayment( String authId ) {

		for ( ErpTransactionModel obj : transactions ) {
			if ( obj instanceof ErpAuthorizationModel ) {
				ErpAuthorizationModel auth = ( (ErpAuthorizationModel)obj );
				if ( authId.equalsIgnoreCase( auth.getAuthCode() ) && auth.getIsChargePayment() ) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean getIsChargePayment( double chargeAmount ) {

		for ( ErpTransactionModel obj : transactions ) {
			if ( obj instanceof ErpAuthorizationModel ) {
				ErpAuthorizationModel auth = ( (ErpAuthorizationModel)obj );
				if ( auth.getIsChargePayment() && MathUtil.roundDecimal( chargeAmount ) == MathUtil.roundDecimal( auth.getAmount() ) ) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasChargeSettlement() {

		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpChargeSettlementModel) {
				return true;
			}
		}
		return false;
	}

	public boolean hasFundsRedeposit() {

		for ( ErpTransactionModel obj : transactions ) {
			if (obj instanceof ErpFundsRedepositModel) {
				return true;
			}
		}
		return false;
	}

	public EnumTransactionType getCurrentTransactionType() {

		List<ErpTransactionModel> txs = new ArrayList<ErpTransactionModel>(transactions);
		Collections.sort(txs, ErpTransactionI.TX_DATE_COMPARATOR);
		if (txs != null && txs.size() > 0) {
			return (txs.get(txs.size()-1)).getTransactionType();
		}
		return null;
	}

	public List<ErpFailedSettlementModel> getFailedSettlements() {
		List<ErpFailedSettlementModel> failedSettlements = new ArrayList<ErpFailedSettlementModel>();
		for ( ErpTransactionModel m : transactions ) {
			if (m instanceof ErpFailedSettlementModel) {
				failedSettlements.add( (ErpFailedSettlementModel)m );
			}
		}
		return failedSettlements;
	}

	public List<ErpChargeSettlementModel> getChargeSettlements() {
		List<ErpChargeSettlementModel> chargeSettlements = new ArrayList<ErpChargeSettlementModel>();
		for ( ErpTransactionModel m : transactions ) {
			if (m instanceof ErpChargeSettlementModel) {
				chargeSettlements.add( (ErpChargeSettlementModel)m );
			}
		}
		return chargeSettlements;
	}

	public List<ErpFundsRedepositModel> getFundsRedeposits() {
		List<ErpFundsRedepositModel> fundsRedeposits = new ArrayList<ErpFundsRedepositModel>();
		for ( ErpTransactionModel m : transactions ) {
			if (m instanceof ErpFundsRedepositModel) {
				fundsRedeposits.add( (ErpFundsRedepositModel)m );
			}
		}
		return fundsRedeposits;
	}

	public List<ErpFailedChargeSettlementModel> getFailedChargeSettlements() {
		List<ErpFailedChargeSettlementModel> failedChargeSettlements = new ArrayList<ErpFailedChargeSettlementModel>();
		for ( ErpTransactionModel m : transactions ) {
			if (m instanceof ErpFailedChargeSettlementModel) {
				failedChargeSettlements.add( (ErpFailedChargeSettlementModel)m );
			}
		}
		return failedChargeSettlements;
	}

	public void reverseChargePayment() throws ErpTransactionException {
		assertStatus(
			new EnumSaleStatus[] {
				EnumSaleStatus.ENROUTE,
				EnumSaleStatus.PAYMENT_PENDING
				});
		status = EnumSaleStatus.SETTLEMENT_FAILED;
	}

	public List<ErpCaptureModel> getGoodCaptures() {

		ArrayList<ErpCaptureModel> goodCaptures = new ArrayList<ErpCaptureModel>();
		List<ErpVoidCaptureModel> voidCaptures = getVoidCaptures();
		List<ErpFailedSettlementModel> failedSettlements = getFailedSettlements();

		for ( ErpCaptureModel cm : getCaptures() ) {
			boolean found = false;
			for ( ErpVoidCaptureModel vcm : voidCaptures ) {
				if (MathUtil.roundDecimal(cm.getAmount()) == MathUtil.roundDecimal(vcm.getAmount())
				&& cm.getPaymentMethodType().equals(vcm.getPaymentMethodType())
				&& (cm.getCcNumLast4() != null && cm.getCcNumLast4().equals(vcm.getCcNumLast4()))) {
					voidCaptures.remove(vcm);
					found = true;
					break;
				}
			}
			if (!found) {
				for ( ErpFailedSettlementModel fsm : failedSettlements ) {
					if (MathUtil.roundDecimal(cm.getAmount()) == MathUtil.roundDecimal(fsm.getAmount())
					&& cm.getPaymentMethodType().equals(fsm.getPaymentMethodType())
					&& (cm.getCcNumLast4() != null && cm.getCcNumLast4().equals(fsm.getCcNumLast4()))) {
						failedSettlements.remove(fsm);
						found = true;
						break;
					}
				}
			}
			if (!found) {
				goodCaptures.add(cm);
			}
		}

		return goodCaptures;
	}


	public boolean getIsSettlementFailedAfterSettled() {

		if(EnumSaleStatus.SETTLEMENT_FAILED.equals(status)){
			return true;
		}

		List<ErpTransactionModel> txs = new ArrayList<ErpTransactionModel>(transactions);
		Collections.sort(txs, ErpTransactionI.TX_DATE_COMPARATOR);
		int failSettlements = 0;
		int settlements = 0;
		boolean stf = false;

		for ( ErpTransactionModel o : txs ) {
			if (o instanceof ErpFailedSettlementModel) {
				failSettlements++;
				stf = true;
			}
			if (o instanceof ErpSettlementModel && stf) {
				settlements++;
			}
		}

		return stf ? failSettlements >= settlements : false;
	}

	private void copyLastCapturePaymentMethodInfo(ErpPaymentModel destPaymentModel) {

		if (destPaymentModel != null) {
			List<ErpTransactionModel> txs = new ArrayList<ErpTransactionModel>(transactions);
			Collections.sort(txs, ErpTransactionI.TX_DATE_COMPARATOR);
			Collections.reverse(txs);
			// get the last captured
			for ( ErpTransactionModel o : txs ) {
				if (o instanceof ErpCaptureModel) {
					ErpCaptureModel captureModel = (ErpCaptureModel) o;
					destPaymentModel.setPaymentMethodType(captureModel.getPaymentMethodType());
					destPaymentModel.setCcNumLast4(captureModel.getCcNumLast4());
					destPaymentModel.setCardType(captureModel.getCardType());
					destPaymentModel.setAbaRouteNumber(captureModel.getAbaRouteNumber());
					destPaymentModel.setBankAccountType(captureModel.getBankAccountType());
					break;
				}
			}
		}

	}

	public int getNumberOfCaptures(){
		List<ErpCaptureModel> l = getGoodCaptures();
		for (ListIterator<ErpCaptureModel> i = l.listIterator(); i.hasNext();) {
			ErpCaptureModel c = i.next();
			if(getIsChargePayment(c.getAmount())){
				i.remove();
			}
		}
		return l.size();
	}

	public List<ErpTransactionI> getCashbacks(){
		return filterTransaction(ErpCashbackModel.class);
	}

	public ErpSettlementModel getSettlement (ErpAffiliate affiliate, double amount, String authCode) {
		List<ErpTransactionI> l = filterTransaction(ErpSettlementModel.class);
		ErpSettlementModel model = null;
		for( ErpTransactionI tr : l ) {
			ErpSettlementModel m = (ErpSettlementModel) tr;
			if(m.getAffiliate().equals(affiliate) && m.getAmount() == amount && authCode != null && authCode.equals(m.getAuthCode())) {
				model = m;
				break;
			}
		}

		return model;
	}

	public ErpFailedSettlementModel getLastFailSettlement() {
		List<ErpTransactionI> l = filterTransaction(ErpFailedSettlementModel.class);
		if(l.isEmpty()) {
			return null;
		}

		return (ErpFailedSettlementModel) l.get(l.size() - 1);
	}

	public ErpChargebackModel getLastChargeback(){
		List<ErpTransactionI> l = filterTransaction(ErpChargebackModel.class);
		if(l.isEmpty()) {
			return null;
		}

		return (ErpChargebackModel) l.get(l.size() - 1);
	}

	public ErpChargebackReversalModel getLastChargebackReversal(){
		List<ErpTransactionI> l = filterTransaction(ErpChargebackReversalModel.class);
		if(l.isEmpty()) {
			return null;
		}

		return (ErpChargebackReversalModel)l.get(l.size() - 1);
	}

	public ErpChargeSettlementModel getLastChargeSettlement() {
		List<ErpTransactionI> l = filterTransaction(ErpChargeSettlementModel.class);
		if(l.isEmpty()) {
			return null;
		}

		return (ErpChargeSettlementModel) l.get(l.size() - 1);
	}

	public boolean hasSplitTransaction() {
		boolean fd = false;
		boolean bc = false;
		boolean usq = false;
		
		final ErpAffiliate fdAff = ErpAffiliate.getPrimaryAffiliate();
		final ErpAffiliate bcAff = ErpAffiliate.getEnum(ErpAffiliate.CODE_BC);
		final ErpAffiliate usqAff = ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ);
		
		for ( ErpCaptureModel c : getGoodCaptures() ) {
			if(fdAff.equals(c.getAffiliate())) {
				fd = true;
			}

			if(bcAff.equals(c.getAffiliate())) {
				bc = true;
			}
			if(usqAff.equals(c.getAffiliate())) {
				usq = true;
			}

		}

		return fd && (bc || usq);
	}

	public String getPreviousSettlementId(ErpAbstractSettlementModel settlement, boolean stlForStf) {
		List<ErpTransactionI> l = filterTransaction(ErpSettlementModel.class);
		String id = "";
		for ( ErpTransactionI i : l ) {
			ErpSettlementModel s = (ErpSettlementModel) i;
			if(MathUtil.roundDecimal(s.getAmount()) == MathUtil.roundDecimal(settlement.getAmount()) && s.getAffiliate().equals(settlement.getAffiliate())
				&& (s.getCcNumLast4().equals(settlement.getCcNumLast4()) || stlForStf)){
				id = s.getPK().getId();
				break;
			}
		}

		return id;
	}

	public String getCashbackId(ErpAffiliate affiliate, double amount) {
		for( ErpTransactionI i : getCashbacks() ) {
			ErpCashbackModel c = (ErpCashbackModel) i;
			if(c.getAffiliate().equals(affiliate) && MathUtil.roundDecimal(amount) == MathUtil.roundDecimal(c.getAmount())) {
				return c.getPK().getId();
			}
		}

		return null;
	}
	
	public EnumSaleType getType() {
		return type;
	}
	
	public double getSubTotal() {
		return subTotal;
	}
	
	public EnumDeliveryType getDeliveryType() {
		return deliveryType;
	}
	
	public void setDeliveryType(EnumDeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}
	
	public Date getCreateDate(){
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getStandingOrderId() {
		return standingOrderId;
	}
}
