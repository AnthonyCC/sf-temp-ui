package com.freshdirect.payment.gateway.ewallet.impl;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.ewallet.ErpPPSettlementInfo;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;

/**
 * 
 * @deprecated
 *
 */
public interface PayPalReconciliationSB extends EJBObject {
	@Deprecated
	public List<String> addPPSettlementSummary(ErpSettlementSummaryModel[] model) throws RemoteException;
	@Deprecated
	public List<ErpPPSettlementInfo> processPPSettlements(List<String> ppStlmnts) throws RemoteException, CreateException, ErpTransactionException;
	@Deprecated
	public Map<String, Object> acquirePPLock(Date date) throws RemoteException;
	@Deprecated
	public void releasePPLock(List<String> settlementIds) throws RemoteException;
	@Deprecated
	public void updatePayPalStatus(List<String> settlementIds) throws RemoteException;
	@Deprecated
	public List<ErpSettlementSummaryModel> getPPTrxns(List<String> ppStlmntIds) throws RemoteException;
	@Deprecated
	public void updatePPSettlementTransStatus(String settlementTransId) throws RemoteException;
	@Deprecated
	public Map<String,String> getPPSettlementNotProcessed() throws RemoteException;
	@Deprecated
	public void insertNewSettlementRecord(Date date,
			List<String> settlementIds, Connection conn) throws RemoteException;
}
