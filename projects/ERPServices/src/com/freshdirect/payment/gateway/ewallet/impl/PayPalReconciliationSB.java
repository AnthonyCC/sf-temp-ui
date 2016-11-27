package com.freshdirect.payment.gateway.ewallet.impl;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.ewallet.ErpPPSettlementInfo;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;

public interface PayPalReconciliationSB extends EJBObject {

	public List<String> addPPSettlementSummary(ErpSettlementSummaryModel[] model) throws RemoteException;
	
	public List<ErpPPSettlementInfo> processPPSettlements(List<String> ppStlmnts) throws RemoteException, CreateException, ErpTransactionException;
	public List<String> acquirePPLock(Date date) throws RemoteException;
	public void releasePPLock(List<String> settlementIds) throws RemoteException;
	public void updatePayPalStatus(List<String> settlementIds) throws RemoteException;
//	public int processPPFee(List<ErpSettlementSummaryModel> stlmntTrxns, List<ErpPPSettlementInfo> settlementInfos) throws RemoteException;
	public List<ErpSettlementSummaryModel> getPPTrxns(List<String> ppStlmntIds) throws RemoteException;
	public void updatePPSettlementTransStatus(String settlementTransId) throws RemoteException;
}
