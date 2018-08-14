package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;

public interface PayPalReconciliationServiceI {

	public Map<String, String> getPPSettlementNotProcessed() throws RemoteException;

	public Map<String, Object> acquirePPLock(Date date) throws RemoteException;

	public void releasePPLock(List<String> settlementIds) throws RemoteException;

	public void insertNewSettlementRecord(Date date) throws RemoteException;

	public List<String> addPPSettlementSummary(ErpSettlementSummaryModel[] models) throws RemoteException;

	public void updatePayPalStatus(List<String> settlementIds) throws RemoteException;

	public List<ErpSettlementSummaryModel> getPPTrxns(List<String> ppStlmntIds) throws RemoteException;

	public void updatePPSettlementTransStatus(String settlementTransId) throws RemoteException;

}
