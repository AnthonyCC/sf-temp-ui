/*
 * Created on Apr 9, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.dataloader.payment.reconciliation;

import java.util.Date;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.giftcard.ErpGCSettlementInfo;

/**
 * @author knadeem
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface SettlementBuilderI {
	public abstract void addChargeDetail(
		ErpSettlementInfo info,
		boolean refund,
		double amount,
		EnumCardType cardType);
	public abstract void addFailedSettlement(ErpSettlementInfo info, double amount, EnumCardType cardType);
	public abstract void addHeader(Date batchDate, String batchNumber, double netDeposit);
	public abstract void addCreditFee(EnumCardType ccType, double amount);
	public abstract void addInvoice(double amount, String description);
	public abstract void addChargeback(ErpSettlementInfo info, double amount);
	public abstract void addChargebackReversal(ErpSettlementInfo info, double amount);
	public abstract void addBounceCheckCharge(ErpSettlementInfo info, EnumCardType type, double amount);
	public abstract void addPaymentRecharge(ErpSettlementInfo info, EnumCardType type, double amount);
	public abstract void addGCChargeDetail(ErpGCSettlementInfo info, EnumCardType cardType);
	public void addFailedGCSettlement(ErpGCSettlementInfo info, EnumCardType cardType);
}