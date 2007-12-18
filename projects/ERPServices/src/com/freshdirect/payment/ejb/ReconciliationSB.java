package com.freshdirect.payment.ejb;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.*;
import com.freshdirect.payment.model.*;


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
}
