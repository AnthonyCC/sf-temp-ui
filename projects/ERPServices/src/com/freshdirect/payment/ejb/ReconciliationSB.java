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


public interface ReconciliationSB extends EJBObject {
	
	public ErpSettlementInfo addSettlement(ErpSettlementModel model, String saleId, ErpAffiliate affiliate, boolean refund) throws RemoteException;
	
	public void addSettlementSummary(ErpSettlementSummaryModel model) throws RemoteException;
	
	public void addAdjustment(ErpAdjustmentModel adjustmentModel) throws RemoteException;
	
	public ErpSettlementInfo addChargeback(ErpChargebackModel chargebackModel) throws ErpTransactionException, RemoteException;
	
	public ErpSettlementInfo addChargebackReversal(ErpChargebackReversalModel chargebackModel) throws ErpTransactionException, RemoteException;

	public boolean isChargeSettlement(String saleId, double chargeAmount) throws RemoteException;
	
	public ErpSettlementInfo processSettlement(String saleId, ErpAffiliate affiliate, String authId, String accountNumber,  double chargeAmount, String sequenceNumber, EnumCardType ccType, boolean refund) throws RemoteException;

	public ErpSettlementInfo processECPReturn(String saleId, ErpAffiliate affiliate, String accountNumber, double amount, String sequenceNumber, EnumPaymentResponse paymentResponse, String description, int usageCode) throws RemoteException;

	public List loadBadTransactions(Date startDate, Date endDate) throws RemoteException;
	
	public List loadVoidTransactions(Date startDate, Date endDate) throws RemoteException;
	
	public List loadReadyToSettleECPSales(Date startDate, int maxNumSales) throws RemoteException;

	public List loadReadyToSettleECPSales(List saleIds) throws RemoteException;

	public boolean isSettlementFailedAfterSettled(String saleId) throws RemoteException;
	
	public List processGCSettlement(String saleId) throws RemoteException;
	
	public List processSettlementPendingOrders() throws RemoteException;
	
	public void sendSettlementReconToSap(String fileName, String folder) throws SapException, RemoteException;
	
	public ErpSettlementInfo getSettlementInfo(String saleId, ErpAffiliate affiliate, double amount, String authCode, boolean chargeSettlement, boolean refund, boolean settlementFail, boolean cbk, boolean cbr) throws RemoteException, FinderException;
}
