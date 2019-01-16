package com.freshdirect.ecomm.gateway;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.sap.ejb.SapException;

public interface ReconciliationServiceI {

	public ErpSettlementInfo addSettlement(ErpSettlementModel model, String saleId,
			ErpAffiliate affiliate, boolean refund) throws RemoteException;

	public ErpSettlementInfo addChargeback(ErpChargebackModel chargebackModel)throws RemoteException;

	public ErpSettlementInfo addChargebackReversal(	ErpChargebackReversalModel chargebackReversalModel)throws RemoteException;

	public void addSettlementSummary(ErpSettlementSummaryModel settlementSummary)throws RemoteException;

	public void addAdjustment(ErpAdjustmentModel adjustmentModel)throws RemoteException;

	public boolean isChargeSettlement(String saleId, double chargeAmount)throws RemoteException;

	public ErpSettlementInfo processSettlement(String saleId, ErpAffiliate aff,
			String authId, String accountNumber, double chargeAmount,
			String sequenceNumber, EnumCardType ccType, boolean b)throws RemoteException;

	public ErpSettlementInfo processECPReturn(String saleId, ErpAffiliate aff,
			String accountNumber, double amount, String sequenceNumber,
			EnumPaymentResponse paymentResponse, String description,
			int usageCode)throws RemoteException;

	public List loadBadTransactions(Date startDate, Date endDate)throws RemoteException;

	public List loadReadyToSettleECPSales(Date startDate, int maxNumSales)throws RemoteException;

	public List loadReadyToSettleECPSales(List<String> saleIds)throws RemoteException;

	public boolean isSettlementFailedAfterSettled(String saleId)throws RemoteException;

	public List processGCSettlement(String saleId)throws RemoteException;

	public List processSettlementPendingOrders()throws RemoteException;

	public void sendFile(InputStream is, String fileName) throws  RemoteException;

	void sendSettlementReconToSap(InputStream is, String fileName,String sapUploadFolder) throws SapException, RemoteException;


}
