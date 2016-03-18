package com.freshdirect.payment.gateway.ewallet.impl;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.ewallet.ErpPPSettlementInfo;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;

public interface PayPalReconciliationSB extends EJBObject {

	public void addPPSettlementSummary(ErpSettlementSummaryModel[] model, boolean ignoreLock) throws RemoteException;
	
	public List<ErpPPSettlementInfo> processPPSettlement(Date date) throws RemoteException;
	public void acquirePPLock(Date date, boolean force) throws RemoteException;
	public void releasePPLock(Date date) throws RemoteException;
	public void updatePayPalStatus(Date date) throws RemoteException;
}
