package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.sap.ejb.SapException;

/**
 *@deprecated Please use the ReconciliationController and ReconciliationServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */

public interface ReconciliationSB extends EJBObject {
	@Deprecated
	public ErpSettlementInfo addSettlement(ErpSettlementModel model, String saleId, ErpAffiliate affiliate, boolean refund) throws RemoteException;
	@Deprecated
	public void addSettlementSummary(ErpSettlementSummaryModel model) throws RemoteException;
	@Deprecated
	public void addAdjustment(ErpAdjustmentModel adjustmentModel) throws RemoteException;
	@Deprecated
	public ErpSettlementInfo addChargeback(ErpChargebackModel chargebackModel) throws ErpTransactionException, RemoteException;
	@Deprecated
	public ErpSettlementInfo addChargebackReversal(ErpChargebackReversalModel chargebackModel) throws ErpTransactionException, RemoteException;
	@Deprecated
	public boolean isChargeSettlement(String saleId, double chargeAmount) throws RemoteException;
	@Deprecated
	public ErpSettlementInfo processSettlement(String saleId, ErpAffiliate affiliate, String authId, String accountNumber,  double chargeAmount, String sequenceNumber, EnumCardType ccType, boolean refund) throws RemoteException;
	@Deprecated
	public ErpSettlementInfo processECPReturn(String saleId, ErpAffiliate affiliate, String accountNumber, double amount, String sequenceNumber, EnumPaymentResponse paymentResponse, String description, int usageCode) throws RemoteException;
	@Deprecated
	public List loadBadTransactions(Date startDate, Date endDate) throws RemoteException;
	@Deprecated
	public List loadVoidTransactions(Date startDate, Date endDate) throws RemoteException;
	@Deprecated
	public List loadReadyToSettleECPSales(Date startDate, int maxNumSales) throws RemoteException;
	@Deprecated
	public List loadReadyToSettleECPSales(List saleIds) throws RemoteException;
	@Deprecated
	public boolean isSettlementFailedAfterSettled(String saleId) throws RemoteException;
	@Deprecated
	public List processGCSettlement(String saleId) throws RemoteException;
	@Deprecated
	public List processSettlementPendingOrders() throws RemoteException;
	@Deprecated
	public void sendSettlementReconToSap(String fileName, String folder) throws SapException, RemoteException;
	@Deprecated
	public ErpSettlementInfo getSettlementInfo(String saleId, ErpAffiliate affiliate, double amount, String authCode, boolean chargeSettlement, boolean refund, boolean settlementFail, boolean cbk, boolean cbr) throws RemoteException, FinderException;
}
