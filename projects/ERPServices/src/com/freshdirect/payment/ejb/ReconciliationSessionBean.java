package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import lcc.japi.LCC;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractSettlementModel;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpChargeSettlementModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpFailedChargeSettlementModel;
import com.freshdirect.customer.ErpFailedSettlementModel;
import com.freshdirect.customer.ErpFundsRedepositModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpCreateCaseCommand;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EFTTransaction;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.reconciliation.detail.CCDetailOne;

public class ReconciliationSessionBean extends SessionBeanSupport{
	
	private static final Category LOGGER = LoggerFactory.getInstance(ReconciliationSessionBean.class);
	private final static ServiceLocator LOCATOR = new ServiceLocator();
	
	public ErpSettlementInfo addSettlement(ErpSettlementModel model, String saleId, ErpAffiliate affiliate, boolean refund) {
		LOGGER.debug("adding Settlement for sale# "+saleId);
		
		try {
			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			EnumSaleStatus status = eb.getStatus();
			boolean found = false;
			if(EnumSaleStatus.SETTLED.equals(status) || EnumSaleStatus.SETTLEMENT_FAILED.equals(status)){
				ErpSettlementModel settlement = null;
				for(Iterator i = eb.getSettlements().iterator(); i.hasNext(); ){
					settlement = (ErpSettlementModel)i.next();
					if(model.getSequenceNumber().equals(settlement.getSequenceNumber())){
						found = true;
						LOGGER.info("Got another same settlement for: "+saleId);
						break;
					}
				}
			}
			
			if((EnumSaleStatus.PAYMENT_PENDING.equals(status) ||EnumSaleStatus.SETTLEMENT_FAILED.equals(status)) && !found){
				eb.addSettlement(model);
			}
			
			return this.getSettlementInfo(saleId, affiliate, model.getAmount(), model.getAuthCode(), false, refund, false, false, false);
			
		}catch(FinderException fe){
			LOGGER.warn("Cannot find sale for: "+saleId, fe);
			throw new EJBException(fe);	
		}catch(RemoteException re) {
			LOGGER.warn("RemoteException: ", re);
			throw new EJBException(re);
		}catch(ErpTransactionException te){
			throw new EJBException(te);
		}
			
	}
	
	private ErpSettlementInfo getSettlementInfo(String saleId, ErpAffiliate affiliate, double amount, String authCode, boolean chargeSettlement, boolean refund, boolean settlementFail, boolean cbk, boolean cbr) throws RemoteException, FinderException {
		ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
		ErpSaleModel sale = (ErpSaleModel) eb.getModel();
		ErpAbstractSettlementModel settlement = null;
		if(settlementFail) {
			settlement = sale.getLastFailSettlement();
		}else if(chargeSettlement) {
			settlement = sale.getLastChargeSettlement();
		}else{
			settlement = sale.getSettlement(affiliate, amount, authCode);
		}
		
		boolean settlementFailedAfterSettled = this.isSettlementFailedAfterSettled(saleId);
		
		ErpSettlementInfo info = new ErpSettlementInfo(sale.getLastInvoice().getInvoiceNumber(), affiliate);
		info.setsettlementFailedAfterSettled(settlementFailedAfterSettled);
		
		if(cbk) {
			info.setId(sale.getLastChargeback().getPK().getId());
		}else if(cbr){
			info.setId(sale.getLastChargebackReversal().getPK().getId());
		}else if((settlementFail || settlementFailedAfterSettled) && !chargeSettlement){
			info.setId(sale.getPreviousSettlementId(settlement, settlementFailedAfterSettled));
		}else if(refund){
			info.setId(sale.getCashbackId(affiliate, amount));
		}else{
			info.setId(settlement.getPK() != null ? settlement.getPK().getId() : "");
		}
		
		info.setTransactionCount(refund || settlementFail || cbk || cbr || chargeSettlement ? 1 : sale.getNumberOfCaptures());
		info.setSplitTransaction(refund || settlementFail || cbk || cbr || chargeSettlement ? false : sale.hasSplitTransaction());
		info.setChargeSettlement(chargeSettlement);
		
		return info;
	}
	
	public void addAdjustment (ErpAdjustmentModel adjustmentModel) {
		
		String saleId = adjustmentModel.getReferenceNumber();
		
		try{
			LOGGER.info("Add adjustment - start. saleId="+saleId);

			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			EnumSaleStatus status = eb.getStatus();
			boolean found = false;
			if(EnumSaleStatus.SETTLED.equals(status) || EnumSaleStatus.SETTLEMENT_FAILED.equals(status)){
				ErpAdjustmentModel model = null;
				for(Iterator i = eb.getAdjustments().iterator(); i.hasNext(); ){
					model = (ErpAdjustmentModel)i.next();
					if(model.getSequenceNumber().equals(adjustmentModel.getSequenceNumber())){
						found = true;
						break;
					}
				}
			}
			
			if(EnumSaleStatus.PAYMENT_PENDING.equals(status) && !found){
				eb.addAdjustment(adjustmentModel);
				String customerId = eb.getCustomerPk().getId();
				
				this.createCase(saleId, customerId, CrmCaseSubject.getEnum(CrmCaseSubject.CODE_PAYMENT_ERROR), "Sale failed to settle");
			}

			LOGGER.info("Add adjustment - done. saleId="+saleId);
			
		}catch(FinderException e){
			LOGGER.warn("Cannot fine sale: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}catch(ErpTransactionException e){
			LOGGER.warn(e);
			throw new EJBException(e);
		}	
	}
	
	public ErpSettlementInfo addChargeback(ErpChargebackModel model) throws ErpTransactionException {
		
		try{

			String saleId = model.getMerchantReferenceNumber();

			LOGGER.info("Add chargeback - start. saleId="+saleId);

			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			ErpPaymentMethodI paymentMethod = eb.getCurrentOrder().getPaymentMethod();
			model.setPaymentMethodType(paymentMethod.getPaymentMethodType());
			eb.addChargeback(model);
			String customerId = eb.getCustomerPk().getId();
			
			this.createCase(saleId, customerId, CrmCaseSubject.getEnum(CrmCaseSubject.CODE_PAYMENT_ERROR), "Chargeback was issued");
			
			LOGGER.info("Add chargeback - done. saleId="+saleId);
			ErpSettlementInfo info = this.getSettlementInfo(saleId, model.getAffiliate(), model.getAmount(), "", false, false, false, true, false); 
			info.setCardType(paymentMethod.getCardType());
			return info; 

		}catch(FinderException e){
			LOGGER.debug("FinderException: ", e);
			throw new EJBException("Cannot find sale for id: "+model.getMerchantReferenceNumber());
		}catch(RemoteException e){
			LOGGER.debug("RemoteException: ", e);
			throw new EJBException("Cannot talk to SaleEntityBean", e);
		}
	}
	
	public ErpSettlementInfo addChargebackReversal(ErpChargebackReversalModel model) throws ErpTransactionException {
		
		try{
			String saleId = model.getMerchantReferenceNumber();
			
			LOGGER.info("Add chargeback reversal - start. saleId="+saleId);
			
			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			eb.addChargebackReversal(model);
			ErpPaymentMethodI paymentMethod = eb.getCurrentOrder().getPaymentMethod();
			String customerId = eb.getCustomerPk().getId();
			
			this.createCase(saleId, customerId, CrmCaseSubject.getEnum(CrmCaseSubject.CODE_PAYMENT_ERROR), "Chargeback Reversal was issued");
			
			LOGGER.info("Add chargeback reversal - done. saleId="+saleId);
			ErpSettlementInfo info = this.getSettlementInfo(saleId, model.getAffiliate(), model.getAmount(), "", false, false, false, false, true); 
			info.setCardType(paymentMethod.getCardType());
			return info;
		}catch(FinderException e){
			LOGGER.debug("FinderException: ", e);
			throw new EJBException("Cannot find sale for id: "+model.getMerchantReferenceNumber(), e);
		}catch(RemoteException e){
			LOGGER.debug("RemoteException: ", e);
			throw new EJBException("Cannot talk to SaleEntityBean", e);
		}
	}
	
	private void createCase(String saleId, String customerId, CrmCaseSubject subject, String summary) {
		CrmSystemCaseInfo info = new CrmSystemCaseInfo(new PrimaryKey(customerId), new PrimaryKey(saleId), subject, summary);
		new ErpCreateCaseCommand(LOCATOR, info).execute();
	}
	
	private final static String summaryQuery = "select batch_number from cust.settlement where process_period_start = ? and process_period_end = ? ";
	
	public void addSettlementSummary(ErpSettlementSummaryModel model) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			LOGGER.debug("Got a settlement");
			conn = this.getConnection();
			ps = conn.prepareStatement(summaryQuery);
			ps.setDate(1, new java.sql.Date(model.getProcessPeriodStart().getTime()));
			ps.setDate(2, new java.sql.Date(model.getProcessPeriodEnd().getTime()));
			rs = ps.executeQuery();
			if(!rs.next()){
				SettlementSummaryPersistentBean bean = new SettlementSummaryPersistentBean(model);
				bean.create(conn);
			}
		}catch(SQLException e){
			LOGGER.debug("SQLException: ", e);
			throw new EJBException("SQLException: ", e);
		}finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(ps != null){
					ps.close();
					ps = null;
				}
				if(conn != null){
					conn.close();
					conn = null;
				}
			}catch(SQLException se){
				LOGGER.warn("Exception while trying to cleanup: ", se);
			}
		}
		
	}
	
	public ErpSettlementInfo processSettlement(String saleId, ErpAffiliate affiliate, String authCode, String accountNumber,  double chargeAmount, String sequenceNumber, EnumCardType ccType, boolean refund) {
		
		ErpSettlementInfo info = null;
		if (!isChargeSettlement(saleId, chargeAmount)) {
			ErpSettlementModel model = new ErpSettlementModel(); 
			model.setAmount(chargeAmount);
			model.setSequenceNumber(sequenceNumber);
			model.setTransactionSource(EnumTransactionSource.SYSTEM);
			model.setAffiliate(affiliate);
			model.setAuthCode(authCode);
			info = addSettlement(model, saleId, affiliate, refund);
			// once an order settles, authomatically removes the account from bad account list
			if (EnumCardType.ECP.equals(ccType)) {
				removeBadCustomerPaymentMethod(saleId, EnumPaymentMethodType.ECHECK, accountNumber);
			}
		} else {
			ErpChargeSettlementModel model = new ErpChargeSettlementModel();
			model.setAmount(chargeAmount);
			model.setSequenceNumber(sequenceNumber);
			model.setTransactionSource(EnumTransactionSource.SYSTEM);
			model.setAffiliate(affiliate);
			model.setAuthCode(authCode);
			info =  addChargeSettlement(model, saleId, affiliate);
		}
		
		return info;
		
	}
	
	public ErpSettlementInfo processECPReturn(String saleId, ErpAffiliate affiliate, String accountNumber, double amount, String sequenceNumber, EnumPaymentResponse paymentResponse, String description, int usageCode) {
		
		ErpAbstractSettlementModel model = null;

		if (usageCode == 1 && 
				(EnumPaymentResponse.INSUFFIENT_FUNDS_D.equals(paymentResponse) || 
				EnumPaymentResponse.INSUFFIENT_FUNDS_R.equals(paymentResponse) || 
				EnumPaymentResponse.UNCOLLECTED_FUNDS_D.equals(paymentResponse) || 
				EnumPaymentResponse.UNCOLLECTED_FUNDS_R.equals(paymentResponse))  ) {  // redeposit notice
			model = new ErpFundsRedepositModel();
		} else if (isChargeSettlement(saleId, amount)) {
				model = new ErpFailedChargeSettlementModel();				
		} else {
			model = new ErpFailedSettlementModel();
		}
		
		model.setPaymentMethodType(EnumPaymentMethodType.ECHECK);
		model.setCardType(EnumCardType.ECP);
		model.setAmount(amount);
		model.setSequenceNumber(sequenceNumber);
		model.setResponseCode(paymentResponse);
		model.setTransactionSource(EnumTransactionSource.SYSTEM);			
		model.setDescription(description);
		model.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
		model.setAffiliate(affiliate);

		ErpSettlementInfo info = null;		
		if (model instanceof ErpFundsRedepositModel) {
			info = addFundsRedeposit((ErpFundsRedepositModel)model, saleId, affiliate);
		} else if (model instanceof ErpFailedChargeSettlementModel) {
			info = addFailedChargeSettlement((ErpFailedChargeSettlementModel)model, saleId, affiliate);					
		} else {  // defaulted to ErpFailedSettlementModel
			info = addFailedSettlement((ErpFailedSettlementModel)model, saleId, affiliate);			
		}

		updateBadCustomerPaymentMethod(saleId, EnumPaymentMethodType.ECHECK, model.getResponseCode(), accountNumber);
		
		return info;
		
	}

	public boolean isChargeSettlement(String saleId, double chargeAmount) {
		LOGGER.debug("is charge settlement sale# "+saleId);
		
		try {
			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			return eb.getIsChargePayment(chargeAmount);
			
		}catch(FinderException fe){
			LOGGER.warn("Cannot find sale for: "+saleId, fe);
			throw new EJBException(fe);	
		}catch(RemoteException re) {
			LOGGER.warn("RemoteException: ", re);
			throw new EJBException(re);
		}
			
	}
		
	public boolean isSettlementFailedAfterSettled(String saleId) {
		LOGGER.debug("is settlment failed sale# "+saleId);
		
		try {
			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			return eb.getIsSettlementFailedAfterSettled();						
		}catch(FinderException fe){
			LOGGER.warn("Cannot find sale for: "+saleId, fe);
			throw new EJBException(fe);	
		}catch(RemoteException re) {
			LOGGER.warn("RemoteException: ", re);
			throw new EJBException(re);
		}
			
	}

	private ErpSettlementInfo addChargeSettlement(ErpChargeSettlementModel model, String saleId, ErpAffiliate affiliate) {
		
		try{
			LOGGER.debug("adding Charge Settlement for sale# "+saleId);

			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			EnumSaleStatus status = eb.getStatus();
			boolean found = false;
			if(EnumSaleStatus.SETTLED.equals(status) || EnumSaleStatus.SETTLEMENT_FAILED.equals(status) || EnumSaleStatus.PAYMENT_PENDING.equals(status)){
				ErpChargeSettlementModel chargeSettlement = null;
				List chargeSettlements = eb.getChargeSettlements();
				if (chargeSettlements != null && chargeSettlements.size() > 0) {
					for(Iterator i = chargeSettlements.iterator(); i.hasNext(); ){
						chargeSettlement = (ErpChargeSettlementModel)i.next();
						if(model.getSequenceNumber().equals(chargeSettlement.getSequenceNumber())){
							found = true;
							break;
						}
					}
				}
				if(!found){
					eb.addChargeSettlement(model);
				}
			}
			
			return this.getSettlementInfo(saleId, affiliate, model.getAmount(), model.getAuthCode(), true, false, false, false, false);
			
		}catch(FinderException e){
			LOGGER.warn("Cannot fine sale: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}catch(ErpTransactionException e){
			LOGGER.warn(e);
			throw new EJBException(e);
		}	
	}

	private ErpSettlementInfo addFailedSettlement(ErpFailedSettlementModel model, String saleId, ErpAffiliate affiliate) {
		
		try{
			LOGGER.debug("adding Failed Settlement for sale# "+saleId);

			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			EnumSaleStatus status = eb.getStatus();
			boolean found = false;
			if(EnumSaleStatus.SETTLED.equals(status) || EnumSaleStatus.SETTLEMENT_FAILED.equals(status)	|| EnumSaleStatus.PAYMENT_PENDING.equals(status)){
				ErpFailedSettlementModel failedSettlement = null;
				List failedSettlements = eb.getFailedSettlements();
				if (failedSettlements != null && failedSettlements.size() > 0) {
					for(Iterator i = failedSettlements.iterator(); i.hasNext(); ){
						failedSettlement = (ErpFailedSettlementModel)i.next();
						if(model.getSequenceNumber().equals(failedSettlement.getSequenceNumber())){
							found = true;
							break;
						}
					}
				}
				if(!found){
					eb.addFailedSettlement(model);
					String customerId = eb.getCustomerPk().getId();				
					this.createCase(saleId, customerId, CrmCaseSubject.getEnum(CrmCaseSubject.CODE_PAYMENT_ERROR), "Sale failed to settle");
				}
			}
			

			return this.getSettlementInfo(saleId, affiliate, model.getAmount(), model.getAuthCode(), false, false, true, false, false);
			
		}catch(FinderException e){
			LOGGER.warn("Cannot fine sale: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}catch(ErpTransactionException e){
			LOGGER.warn(e);
			throw new EJBException(e);
		}	
	}

	private ErpSettlementInfo addFailedChargeSettlement(ErpFailedChargeSettlementModel model, String saleId, ErpAffiliate affiliate) {
		
		try{
			LOGGER.debug("adding Failed Settlement for sale# "+saleId);

			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			EnumSaleStatus status = eb.getStatus();
			boolean found = false;
			if(EnumSaleStatus.SETTLED.equals(status) || EnumSaleStatus.SETTLEMENT_FAILED.equals(status) || EnumSaleStatus.PAYMENT_PENDING.equals(status)){
				ErpFailedChargeSettlementModel failedChargeSettlement = null;
				List failedChargeSettlements = eb.getFailedChargeSettlements();
				if (failedChargeSettlements != null && failedChargeSettlements.size() > 0) {
					for(Iterator i = failedChargeSettlements.iterator(); i.hasNext(); ){
						failedChargeSettlement = (ErpFailedChargeSettlementModel)i.next();
						if(model.getSequenceNumber().equals(failedChargeSettlement.getSequenceNumber())){
							found = true;
							break;
						}
					}
				}
				if (!found) {
					eb.addFailedChargeSettlement(model);
					String customerId = eb.getCustomerPk().getId();				
					this.createCase(saleId, customerId, CrmCaseSubject.getEnum(CrmCaseSubject.CODE_PAYMENT_ERROR), "Sale Charge Fee failed to settle");
				}
			}
			return this.getSettlementInfo(saleId, affiliate, model.getAmount(), model.getAuthCode(), true, false, false, false, false);
		}catch(FinderException e){
			LOGGER.warn("Cannot fine sale: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}catch(ErpTransactionException e){
			LOGGER.warn(e);
			throw new EJBException(e);
		}	
	}

	private ErpSettlementInfo addFundsRedeposit(ErpFundsRedepositModel model, String saleId, ErpAffiliate affiliate) {
		
		try{
			LOGGER.debug("adding Funds Redeposit Settlement for sale# "+saleId);

			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			EnumSaleStatus status = eb.getStatus();
			boolean found = false;
			if(EnumSaleStatus.SETTLED.equals(status) || EnumSaleStatus.PAYMENT_PENDING.equals(status)){
				ErpFundsRedepositModel fundsRedeposit = null;
				List fundsRedeposits = eb.getFundsRedeposits();
				if (fundsRedeposits != null && fundsRedeposits.size() > 0) {
					for(Iterator i = fundsRedeposits.iterator(); i.hasNext(); ){
						fundsRedeposit = (ErpFundsRedepositModel)i.next();
						if(model.getSequenceNumber().equals(fundsRedeposit.getSequenceNumber())){
							found = true;
							break;
						}
					}
				}
				if (!found) {
					eb.addFundsRedeposit(model);					
				}
			}
			
			return this.getSettlementInfo(saleId, affiliate, model.getAmount(), model.getAuthCode(), false, false, false, false, false);
			
		}catch(FinderException e){
			LOGGER.warn("Cannot fine sale: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}catch(ErpTransactionException e){
			LOGGER.warn(e);
			throw new EJBException(e);
		}	
	}
	
	private void removeBadCustomerPaymentMethod(String saleId, EnumPaymentMethodType paymentMethodType, String accountNumber) {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerHome().create();
			sb.removeBadCustomerPaymentMethod(saleId, paymentMethodType, accountNumber);
		}catch(CreateException e){
			LOGGER.warn("Cannot create CustomerManager Session bean: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}
	}

	private void updateBadCustomerPaymentMethod(String saleId, EnumPaymentMethodType paymentMethodType, EnumPaymentResponse paymentResponse, String accountNumber) {
		try{
			ErpCustomerManagerSB sb = this.getErpCustomerManagerHome().create();
			sb.updateBadCustomerPaymentMethod(saleId, paymentMethodType, paymentResponse, accountNumber);
		}catch(CreateException e){
			LOGGER.warn("Cannot create CustomerManager Session bean: "+saleId, e);
			throw new EJBException(e);
		}catch(RemoteException e){
			LOGGER.warn("RemoteException: ", e);
			throw new EJBException(e);
		}
	}

	private final static String QUERY_EFT_PAYMENT_TRANS_BASE_QUERY = "SELECT * FROM PAYLINX.EFT_TRANSACTION";
	
	private final static String TS_VOID		= "V";
	private final static String TS_BAD		= "B";
	

	public List loadBadTransactions(Date startDate, Date endDate) {
		String sql = QUERY_EFT_PAYMENT_TRANS_BASE_QUERY;			
		return loadTransactions(sql, TS_BAD, String.valueOf(LCC.ID_ACH_DEPOSIT), startDate, endDate);  // retrieve only ach deposit transactions
	}

	public List loadVoidTransactions(Date startDate, Date endDate) {
		
		String sql = QUERY_EFT_PAYMENT_TRANS_BASE_QUERY;			
		return loadTransactions(sql, TS_VOID, String.valueOf(LCC.ID_ACH_VOID), startDate, endDate);
	}

	private List loadTransactions(String sql, String transactionState, String transactionCode, Date startDate, Date endDate) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String newSql = sql;
			String whereOrAnd = (newSql.indexOf( " WHERE ") > -1) ? " AND " : " WHERE ";
			if (startDate != null && endDate != null) {
				newSql += whereOrAnd + " TRANS_DATE_TIME BETWEEN ? AND ?";
				whereOrAnd = " AND ";
			}else if (startDate != null) {
				newSql += whereOrAnd + " TRANS_DATE_TIME >= ?";
				whereOrAnd = " AND ";
			} else if (endDate != null) {
				newSql += whereOrAnd + " TRANS_DATE_TIME <= ?";
			}			
			if (transactionState != null) {
				newSql += whereOrAnd + " TRANSACTION_STATE = ?";				
				whereOrAnd = " AND ";
			}
			
			if (transactionCode != null) {
				newSql += whereOrAnd + " TRANSACTION_CODE = ?";				
				whereOrAnd = " AND ";
			}

			conn = this.getConnection();
			ps = conn.prepareStatement(newSql);
			int index = 1;
			if (startDate != null) {
				ps.setDate(index++, new java.sql.Date(startDate.getTime()));				
			}
			if (endDate != null) {
				ps.setDate(index++, new java.sql.Date(endDate.getTime()));				
			}
			if (transactionState != null) {
				ps.setString(index++, transactionState);
			}

			if (transactionCode != null) {
				ps.setString(index++, transactionCode);
			}
			rs = ps.executeQuery();
			
			ArrayList list = new ArrayList();
			while (rs.next()) {
				list.add(loadResultSet(rs));
			}
			return list;
		} catch (Exception e) {
			LOGGER.warn(e);
			throw new EJBException(e);
		} finally {
			try {
				if (rs != null) { rs.close(); }
				if (ps != null) { ps.close(); }
				if (conn != null) {	conn.close(); }
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}

	private EFTTransaction loadResultSet(ResultSet rs) throws SQLException {
		EFTTransaction trans = new EFTTransaction();
		trans.setServerId(rs.getString("SERVER_ID"));
		trans.setTransactionCode(rs.getString("TRANSACTION_CODE"));
		trans.setLccReturnCode(rs.getString("LCC_RETURN_CODE"));
		trans.setLccReturnMessage(rs.getString("LCC_RETURN_MSG"));
		trans.setBatchId(rs.getString("BATCH_ID"));
		trans.setDraftId(rs.getString("DRAFT_ID"));
		trans.setMerchantId(rs.getString("MERCHANT_ID"));
		trans.setTransactionState(rs.getString("TRANSACTION_STATE"));
		trans.setTransactionDateTime(rs.getDate("TRANS_DATE_TIME"));
		trans.setBankAccountNumber(rs.getString("BANK_ACCT_NUM"));
		trans.setBankId(rs.getString("BANK_ID"));
		trans.setOrderNumber(rs.getString("ORDER_NUMBER"));
		trans.setAccountType(rs.getString("ACCOUNT_TYPE"));
		trans.setAmount(rs.getDouble("AMOUNT"));
		trans.setVerificationResult(rs.getString("VERIFICATION_RESULT"));
		trans.setApprovalCode(rs.getString("APPROVAL_CODE"));
		trans.setProcResponseCode(rs.getString("PROC_RSP_CODE"));
		trans.setProcResponseMessage(rs.getString("PROC_RSP_MSG"));
		trans.setSequenceNumber(rs.getString("SEQUENCE_NUMBER"));
		trans.setBadFieldCode(rs.getString("BAD_FIELD_CODE"));
		trans.setBadFieldData(rs.getString("BAD_FIELD_DATA"));
		trans.setCustomerName(rs.getString("CUSTOMER_NAME"));
		return trans;
	}

	
	private ErpSaleHome getErpSaleHome() {
		try {
			return (ErpSaleHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpSale", ErpSaleHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpCustomerManagerHome getErpCustomerManagerHome() {
		try {
			return (ErpCustomerManagerHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpCustomerManager", ErpCustomerManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	String READY_TO_SETTLE_ECP_SALE_ID_QUERY =  "SELECT s.id as sale_id, sa.amount, p.sequence_number, p.auth_code, " +
												" (" +
												 " SELECT pi.account_number FROM cust.PAYMENTINFO pi, cust.SALESACTION sa2 " +
												 " WHERE pi.salesaction_id=sa2.id AND sa2.sale_id=s.id AND sa2.action_type IN ('CRO', 'MOD')" +
												 " AND sa2.action_date = (SELECT MAX(sa3.action_date) FROM cust.SALESACTION sa3 WHERE sa3.action_type IN ('CRO', 'MOD') AND sa3.sale_id=s.id)" +  
												" ) AS account_number" + 
												" FROM cust.SALE s, cust.SALESACTION sa, cust.PAYMENT p" +
												" WHERE s.id=sa.sale_id" +
												" AND sa.id=p.salesaction_id" +
												" AND s.status='PPG'" +
												" AND sa.action_type = 'CAP'" +
												" AND p.payment_method_type = 'EC'" +
												" AND sa.action_date = (" +
													" SELECT MAX(sa1.action_date)" +
													" FROM cust.SALESACTION sa1" +
													" WHERE sa1.action_type='CAP' AND sa1.sale_id=s.id"+
													" )";
													

	public List loadReadyToSettleECPSales(Date startDate, int maxNumSales) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = READY_TO_SETTLE_ECP_SALE_ID_QUERY 
					+ ((startDate != null) ? " AND sa.action_date <= ?" : "")
					+ ((maxNumSales > 0) ? " AND ROWNUM <= ?" : "");

		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql);
			int index = 1;
			if (startDate != null) {
				ps.setDate(index++, new java.sql.Date(startDate.getTime()));				
			}
			if (maxNumSales > 0) {
				ps.setInt(index++, maxNumSales);					
			}
			rs = ps.executeQuery();
			
			ArrayList list = new ArrayList();
			while (rs.next()) {
				list.add(loadReadyToSettleECPSalesResultSet(rs));
			}
			return list;
		} catch (SQLException e) {
			LOGGER.warn(e);
			throw new EJBException(e);
		} finally {
			try {
				if (rs != null) { rs.close(); }
				if (ps != null) { ps.close(); }
				if (conn != null) {	conn.close(); }
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		
	}

	public List loadReadyToSettleECPSales(List saleIds) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// the reason we don't do a sql IN is because there's a limit to the number of arguments in it.
		// hopefully this option is not used to often
		String sql = READY_TO_SETTLE_ECP_SALE_ID_QUERY + " AND s.id = ?";

		try {
			ArrayList list = new ArrayList();
			conn = this.getConnection();
			ps = conn.prepareStatement(sql);
			for (Iterator i = saleIds.iterator(); i.hasNext();) {
				String saleId = (String) i.next();
				ps.setString(1, saleId);
				rs = ps.executeQuery();				
				while (rs.next()) {
					list.add(loadReadyToSettleECPSalesResultSet(rs));
				}
				rs.close();
				rs = null;
			}
			return list;
		} catch (SQLException e) {
			LOGGER.warn(e);
			throw new EJBException(e);
		} finally {
			try {
				if (rs != null) { rs.close(); }
				if (ps != null) { ps.close(); }
				if (conn != null) {	conn.close(); }
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}	
	}
	
	private CCDetailOne loadReadyToSettleECPSalesResultSet(ResultSet rs) throws SQLException {
		CCDetailOne txn = new CCDetailOne();
		txn.setMerchantReferenceNumber(rs.getString("SALE_ID"));
		txn.setTransactionAmount(rs.getDouble("AMOUNT"));
		txn.setFDMSReferenceNumber(rs.getString("SEQUENCE_NUMBER"));
		txn.setAuthCode(rs.getString("AUTH_CODE"));
		txn.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
		return txn;
	}
}
