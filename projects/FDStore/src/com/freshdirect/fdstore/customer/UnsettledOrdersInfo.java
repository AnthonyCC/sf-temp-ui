package com.freshdirect.fdstore.customer;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.common.date.SimpleDateDeserializer;

public class UnsettledOrdersInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1723878471748911627L;
	private String Settled;
	private String ChargeBack;
	private String SettlementFailed;
	private String Enroute;
	private String PaymentPending;
	private String CapturePending;
	private String GCSettlementPending;
	private String PendingRefusal;
	private String GCPaymentPending;
	private String GCRegistrationPending;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date deliveryDate;
	
	public String getSettled() {
		return Settled;
	}
	public void setSettled(String settled) {
		Settled = settled;
	}
	public String getChargeBack() {
		return ChargeBack;
	}
	public void setChargeBack(String chargeBack) {
		ChargeBack = chargeBack;
	}
	public String getSettlementFailed() {
		return SettlementFailed;
	}
	public void setSettlementFailed(String settlementFailed) {
		SettlementFailed = settlementFailed;
	}
	public String getEnroute() {
		return Enroute;
	}
	public void setEnroute(String enroute) {
		Enroute = enroute;
	}
	public String getPaymentPending() {
		return PaymentPending;
	}
	public void setPaymentPending(String paymentPending) {
		PaymentPending = paymentPending;
	}
	public String getCapturePending() {
		return CapturePending;
	}
	public void setCapturePending(String capturePending) {
		CapturePending = capturePending;
	}
	public String getGCSettlementPending() {
		return GCSettlementPending;
	}
	public void setGCSettlementPending(String gCSettlementPending) {
		GCSettlementPending = gCSettlementPending;
	}
	public String getPendingRefusal() {
		return PendingRefusal;
	}
	public void setPendingRefusal(String pendingRefusal) {
		PendingRefusal = pendingRefusal;
	}
	public String getGCPaymentPending() {
		return GCPaymentPending;
	}
	public void setGCPaymentPending(String gCPaymentPending) {
		GCPaymentPending = gCPaymentPending;
	}
	public String getGCRegistrationPending() {
		return GCRegistrationPending;
	}
	public void setGCRegistrationPending(String gCRegistrationPending) {
		GCRegistrationPending = gCRegistrationPending;
	}
	public Date getDeliveryDate() {
		return this.deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}	
}
