package com.freshdirect.payment.gateway.ewallet.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpCashbackModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpSettlementPersistentBean;
import com.freshdirect.fdstore.ewallet.ErpPPSettlementInfo;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.ejb.ReconciliationHome;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.ejb.SettlementSummaryPersistentBean;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.impl.ReconciliationConstants;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.model.ErpSettlementTransactionModel;

public class PayPalReconciliationSessionBean extends SessionBeanSupport {

	private static final Category LOGGER = LoggerFactory.getInstance(PayPalReconciliationSessionBean.class);
	private static final String PAYPAL_SETTLEMENT_IS_LOCKED = "Y";
	private static final String PAYPAL_NO_RECORDS_PROCESSED = "N";
	private static final String PENDING = "P";
	
	private static final String ACQUIRE_PP_LOCK_QUERY = "select IS_LOCKED, ALL_RECORDS_PROCESSED from cust.settlement " +
			"where SETTLEMENT_SOURCE = 'PP' AND PROCESS_PERIOD_START = ? ";
	private static final String ACQUIRE_PP_LOCK_INSERT = "insert into cust.settlement" +
			" (ID, PROCESS_PERIOD_START, PROCESS_PERIOD_END, BATCH_DATE, BATCH_NUMBER, PROCESS_DATE, DEPOSIT_DATE," +
			" NET_SALES_AMOUNT, NUM_ADJUSTMENTS, ADJUSTMENT_AMOUNT, SETTLEMENT_FILE_DATE, CREATED_TIME_DATE, IS_LOCKED," +
			" SETTLEMENT_SOURCE, ALL_RECORDS_PROCESSED)" +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, systimestamp, ?, ?, ?)";
	private static final String ACQUIRE_PP_LOCK_UDPATE = "update cust.settlement " +
			" set IS_LOCKED = 'Y' where PROCESS_PERIOD_START = ? and settlement_source = 'PP'";
	public void acquirePPLock(Date date, boolean force) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LOGGER.debug("Acquiring PayPal Settlements lock for date - " + date);
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(ACQUIRE_PP_LOCK_QUERY);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				String locked = rs.getString("IS_LOCKED");
				String processed = rs.getString("ALL_RECORDS_PROCESSED");
				if (force) {
					cleanUp(date, null);
				} else if ("Y".equals(processed)) {
					throw new EJBException("The batch for date " + date + " are already processed. Please rerun with force option to process again ");
				} else if (locked == null || StringUtils.isEmpty(locked) || locked.equals("N")) {
					ps = conn.prepareStatement(ACQUIRE_PP_LOCK_UDPATE);
					ps.setDate(1, new java.sql.Date(date.getTime()));
					ps.executeUpdate();
					return;
				} else if (locked.equals(PAYPAL_SETTLEMENT_IS_LOCKED)) {
					throw new EJBException("[PayPal Batch] Some other process should be running for the same date " + date);
				} else {
					throw new EJBException("Invalid locking state exists for date " + date);
				}
			}
			//new process
			byte i = 1;
			ps = conn.prepareStatement(ACQUIRE_PP_LOCK_INSERT);
			ps.setString(i++, SequenceGenerator.getNextId(conn, "CUST"));
			ps.setDate(i++, new java.sql.Date(date.getTime()));
			ps.setDate(i++, new java.sql.Date(date.getTime()));
			ps.setDate(i++, new java.sql.Date(date.getTime()));
			ps.setLong(i++, date.getTime());
			ps.setDate(i++, new java.sql.Date(date.getTime()));
			ps.setDate(i++, new java.sql.Date(date.getTime()));
			ps.setLong(i++, 0);
			ps.setLong(i++, 0);
			ps.setLong(i++, 0);
			ps.setDate(i++, new java.sql.Date(date.getTime()));
			ps.setString(i++, PAYPAL_SETTLEMENT_IS_LOCKED);
			ps.setString(i++, EnumPaymentMethodType.PAYPAL.getName());
			ps.setString(i++, PAYPAL_NO_RECORDS_PROCESSED);
			ps.executeUpdate();

		} catch (SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new EJBException("SQLException: ", e);
		} finally {
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
				LOGGER.warn("Exception while trying to acquire PP Lock: ", se);
			}
		}
	}
	
	public void addPPSettlementSummary(ErpSettlementSummaryModel[] models, boolean overrideLock) {
		Date date = models[0].getProcessPeriodStart();
		try {
			cleanUp(date);
		} catch (SQLException e) {
			
		}
		for (ErpSettlementSummaryModel model: models) {
			if (model == null) continue;
			try {
				addPPSettlementSummary(model, overrideLock);
			} catch (SQLException e) {
				throw new EJBException(e);
			}
		}
	}
	
	private final static String ppSummaryQuery = "select id from cust.settlement where process_period_start = ? and affiliate_account_id = ?" +
														" and is_locked = 'Y'  and settlement_source = 'PP'";
	private void addPPSettlementSummary(ErpSettlementSummaryModel model, boolean overrideLock) throws SQLException {
		LOGGER.debug("Got a settlement");

		SettlementSummaryPersistentBean bean = new SettlementSummaryPersistentBean(model);
			
		//now add new records
		model.setIsLocked(PAYPAL_SETTLEMENT_IS_LOCKED);
		model.setStatus(PENDING);
		List<ErpSettlementTransactionModel> txModels = model.getSettlementTrxns();
		for (ErpSettlementTransactionModel txModel : txModels) {
			txModel.setStatus(PENDING);
		}
		bean.create(this.getConnection());

	}
		
	private static final String RELEASE_PP_LOCK_UDPATE = "update cust.settlement " +
			" set IS_LOCKED = 'N' where PROCESS_PERIOD_START = ?";
	public void releasePPLock(Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LOGGER.debug("Releasing PayPal Settlements Lock.");
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(RELEASE_PP_LOCK_UDPATE);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			rs = ps.executeQuery();
			if (!rs.next()) {
				LOGGER.warn("Exception while trying to release lock. Ignoring it. ");
			}
		} catch (SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new EJBException("SQLException: ", e);
		} finally {
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
				LOGGER.warn("Exception while trying to release lock. Can be ignored: ", se);
			}
		}
	}
	
	/**
	 * This method settles trxns and also generates SettlementInfo for PayPal orders for SAP.
	 * @return
	 */
	public List<ErpPPSettlementInfo> processPPSettlement(Date date) throws RemoteException, CreateException, ErpTransactionException {
	
		ReconciliationSB reconsSB = lookupReconciliationHome().create();
		
		List<ErpPPSettlementInfo> settlementInfos = new ArrayList<ErpPPSettlementInfo>();
		
		List<ErpSettlementSummaryModel> ppStlmntTrxns = getPPTrxns(date);
		if (ppStlmntTrxns == null) {
			return null;
		}
		
		String gatewayOrderId = "";
		String saleId = "";
		processPPFee(ppStlmntTrxns, settlementInfos);
		
		for (ErpSettlementSummaryModel summary : ppStlmntTrxns) {
			ErpAffiliate affiliate = getErpAffiliate(summary.getAffiliateAccountId());
			for (ErpSettlementTransactionModel trxn : summary.getSettlementTrxns()) {
				ErpSettlementModel model = new ErpSettlementModel();

				model.setAuthCode(null);
				model.setAmount(new Money(trxn.getGrossTransactionAmount()).getDollar());
				model.setCardType(EnumCardType.PAYPAL);
				model.setTransactionSource(EnumTransactionSource.SYSTEM);
				model.setSequenceNumber(trxn.getPaypalReferenceId());
				gatewayOrderId = trxn.getGatewayOrderId();
				model.setGatewayOrderID(gatewayOrderId);

				if (gatewayOrderId != null) {
					saleId = gatewayOrderId.substring(0, gatewayOrderId.indexOf("X"));
				} else {
					throw new RuntimeException("PayPal settlement failed as Order Id is null ");
				}
				
				model.setProcessorTrxnId(trxn.getPaypalReferenceId());
				model.setAffiliate(affiliate);
				ErpSettlementInfo info = null;
				if (ErpServicesProperties.getPPSTLEventCodes().contains(trxn.getTransactionEventCode()))
					info = reconsSB.addSettlement(model, saleId, affiliate, false);
				else if (ErpServicesProperties.getPPREFEventCodes().contains(trxn.getTransactionEventCode()))
					info = reconsSB.addSettlement(model, saleId, affiliate, true);
				else if (ErpServicesProperties.getPPCBKEventCodes().contains(trxn.getTransactionEventCode())) {
					info = reconsSB.addChargeback(getChargebackModel(trxn, affiliate, date));
				} else if (ErpServicesProperties.getPPCBREventCodes().contains(trxn.getTransactionEventCode())) {
					info = reconsSB.addChargebackReversal(getChargebackReversalModel(trxn, affiliate, date));
				} else
					LOGGER.info("Transction with event codes is not being update to FD DB" + trxn.getTransactionEventCode());
				
				try {
					ErpPPSettlementInfo ppInfo = new ErpPPSettlementInfo(info.getInvoiceNumber(), affiliate);
					if (info.getId() != null)
						ppInfo.setId(info.getId());
					ppInfo.setAmount(new Money(trxn.getGrossTransactionAmount()).getDollar());
					ppInfo.setChargeSettlement(info.isChargeSettlement());
					ppInfo.setSplitTransaction(info.hasSplitTransaction());
					ppInfo.setsettlementFailedAfterSettled(info.isSettlementFailedAfterSettled());
					ppInfo.setTransactionCount(info.getTransactionCount());
					ppInfo.setTxEventCode(trxn.getTransactionEventCode());
					ppInfo.setCardType(info.getCardType());
					settlementInfos.add(ppInfo);
				} catch (Exception e) {
					// Not expecting as of now
				}
			}
		}

		return settlementInfos;
	}
	
	private ErpChargebackModel getChargebackModel(ErpSettlementTransactionModel trxn, ErpAffiliate affiliate, Date date) {
		ErpChargebackModel cbModel = new ErpChargebackModel();
		populateChargeBack(cbModel, trxn, affiliate, date);
		return cbModel;
	}
	
	private ErpChargebackReversalModel getChargebackReversalModel(ErpSettlementTransactionModel trxn, ErpAffiliate affiliate, Date date) {
		ErpChargebackReversalModel cbModel = new ErpChargebackReversalModel();
		populateChargeBack(cbModel, trxn, affiliate, date);
		return cbModel;
	}
	
	private void populateChargeBack(ErpChargebackModel cbModel, ErpSettlementTransactionModel trxn, ErpAffiliate affiliate, Date date) {
		cbModel.setAffiliate(affiliate);
		cbModel.setAmount(new Money(trxn.getGrossTransactionAmount()).getDollar());
		cbModel.setCardType(EnumCardType.PAYPAL);
		cbModel.setBatchDate(date);
		String ppRefId = trxn.getPaypalReferenceId();
		String cbRefId = ppRefId != null ? ppRefId.substring(0, 14) : "";
		cbModel.setCbkReferenceNumber(cbRefId);
		cbModel.setCcNumLast4("1111");
		cbModel.setCbkRespondDate(date);
		cbModel.setCbkWorkDate(date);
		cbModel.setGatewayOrderID(trxn.getGatewayOrderId());
		cbModel.setMerchantReferenceNumber(trxn.getOrderId());
		cbModel.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
		cbModel.setTransactionSource(EnumTransactionSource.SYSTEM);
	}
	
	private void processPPFee(List<ErpSettlementSummaryModel> stlmntTrxns, List<ErpPPSettlementInfo> settlementInfos) {
		long txFee = 0;
		long miscFee = 0;
		for (ErpSettlementSummaryModel summary : stlmntTrxns) {
			for (ErpSettlementTransactionModel tx : summary.getSettlementTrxns()) {
				if (ErpServicesProperties.getPPSTLEventCodes().contains(tx.getTransactionEventCode())) {
					txFee += tx.getFeeAmount();
				} else if (ErpServicesProperties.getPPSTFEventCodes().contains(tx.getTransactionEventCode()) ||
						ErpServicesProperties.getPPCBKEventCodes().contains(tx.getTransactionEventCode()) ||
						ErpServicesProperties.getPPREFEventCodes().contains(tx.getTransactionEventCode())) {
					miscFee += tx.getFeeAmount();
				}
			}
		}
		
		ErpPPSettlementInfo txFeeInfo = new ErpPPSettlementInfo("FeeTrxnNOInvoice", ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
		txFeeInfo.setAmount(new Money(txFee).getDollar());
		txFeeInfo.setTxEventCode(ReconciliationConstants.FEE_KEY);
		if (txFee > 0)
			settlementInfos.add(txFeeInfo);
		else
			LOGGER.error("Unexpected Tx fee in settlement ");
		
		ErpPPSettlementInfo miscFeeInfo = new ErpPPSettlementInfo("FeeTrxnNOInvoice", ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
		miscFeeInfo.setAmount(new Money(miscFee).getDollar());
		miscFeeInfo.setTxEventCode(ReconciliationConstants.MISC_FEE_KEY);
		if (miscFee > 0)
			settlementInfos.add(miscFeeInfo);
		else
			LOGGER.error("Unexpected Misc fee in settlement ");
	}

	private static final String GET_PP_SETTLEMENT_ID = "select ID, AFFILIATE_ACCOUNT_ID, TOTAL_TRANS_FEE_CREDIT, " +
															"TOTAL_TRANS_FEE_DEBIT, ALL_RECORDS_PROCESSED, PROCESSED_TIME_DATE " +
			" FROM CUST.SETTLEMENT where PROCESS_PERIOD_START = ? AND SETTLEMENT_SOURCE = ? ";
	private static final String GET_ALL_PP_SETTLEMENT_ID = "select ID, AFFILIATE_ACCOUNT_ID, TOTAL_TRANS_FEE_CREDIT, " +
															"TOTAL_TRANS_FEE_DEBIT, ALL_RECORDS_PROCESSED " +
			" FROM CUST.SETTLEMENT where PROCESS_PERIOD_START = ? AND SETTLEMENT_SOURCE = ? AND PROCESSED_TIME_DATE IS NULL";
	
	private static final String GET_PP_SETTLEMENT_TRXNS = "select ID, TRXN_ID, GATEWAY_ORDER_ID, PP_REF_ID, PP_REF_TYPE, " +
			"TX_EVENT_CODE, TX_INITIATION_DATE, TX_COMPLETION_DATE, TX_DEBIT_CREDIT, TX_GROSS_AMOUNT, " +
			"TX_GROSS_CURRENCY, FEE_DEBIT_CREDIT, FEE_AMOUNT, FEE_CURRENCY, CUSTOM_FIELD, CONSUMER_ID, PAYMENT_TRACKING_ID," +
			" BANK_REF_ID, STATUS " +
			"FROM CUST.SETTLEMENT_TRANSACTION where SETTLEMENT_ID = ? AND PROCESSED_TIME_DATE IS NULL AND STATUS = 'P'";
	
	private List<ErpSettlementSummaryModel> getPPTrxns(Date date) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ErpSettlementSummaryModel> ppStlmnts = new ArrayList<ErpSettlementSummaryModel>();

		try{
			LOGGER.debug("Processing PayPal Settlements.");
			conn = this.getConnection();
			if (date != null) {
				ps = conn.prepareStatement(GET_PP_SETTLEMENT_ID);
				ps.setDate(1, new java.sql.Date(date.getTime()));
				ps.setString(2, EnumPaymentMethodType.PAYPAL.getName());
			} else {
				ps = conn.prepareStatement(GET_ALL_PP_SETTLEMENT_ID);
				ps.setString(2, EnumCardType.PAYPAL.getFdName());
			}
			rs = ps.executeQuery();

			String allRecordsProcessed = null;
			while (rs.next()) {
				ErpSettlementSummaryModel summary = new ErpSettlementSummaryModel();
				
				summary.setId(rs.getString("ID"));
				if (summary.getId() == null || StringUtils.isEmpty(summary.getId())) {
					if (ppStlmnts.isEmpty()) {
						LOGGER.error("PayPal - No data found for today or supplied date.\n" +
								"Please check whether PayPalSFTPSettlementLoader is run successfully for the date of interest.");
						return null;
					} else {
						return ppStlmnts;
					}
				}
				
				Date processedDate = rs.getDate("PROCESSED_TIME_DATE");
				allRecordsProcessed = rs.getString("ALL_RECORDS_PROCESSED");
				if (allRecordsProcessed != null && allRecordsProcessed.equals("Y") && processedDate != null) {
					if (ppStlmnts.isEmpty()) {
						LOGGER.info("PayPal - Records for today or supplied date are already processed ");
						return null;
					} else {
						return ppStlmnts;
					}
				}
				
				summary.setAffiliateAccountId(rs.getString("AFFILIATE_ACCOUNT_ID"));
				summary.setTotalTransactionFeeCredit(rs.getLong("TOTAL_TRANS_FEE_CREDIT"));
				summary.setTotalTransactionFeeDebit(rs.getLong("TOTAL_TRANS_FEE_DEBIT"));
				
				ppStlmnts.add(summary);
			}
			if (ppStlmnts.isEmpty()) {
				LOGGER.info("PayPal Settlement - No data found for today or supplied date");
				return null;
			}
			
			List<ErpSettlementTransactionModel> ppStlmntTrxns = new ArrayList<ErpSettlementTransactionModel>();
			for (ErpSettlementSummaryModel stlmntSummary: ppStlmnts) {
				ps = conn.prepareStatement(GET_PP_SETTLEMENT_TRXNS);
				ps.setString(1, stlmntSummary.getId());
				rs = ps.executeQuery();
				while(rs.next()){
					ErpSettlementTransactionModel trxn = null;
					trxn = new ErpSettlementTransactionModel();
					trxn.setId(rs.getString("ID"));
					trxn.setTransactionId(rs.getString("TRXN_ID"));
					trxn.setGatewayOrderId(rs.getString("GATEWAY_ORDER_ID"));
					trxn.setPaypalReferenceId(rs.getString("PP_REF_ID"));
					trxn.setPaypalReferenceIdType(rs.getString("PP_REF_TYPE"));
					trxn.setTransactionEventCode(rs.getString("TX_EVENT_CODE"));
					trxn.setTransactionInitiationDate(rs.getDate("TX_INITIATION_DATE"));
					trxn.setTransactionCompletionDate(rs.getDate("TX_COMPLETION_DATE"));
					trxn.setTransactionDebitOrCredit(rs.getString("TX_DEBIT_CREDIT"));
					trxn.setGrossTransactionAmount(rs.getLong("TX_GROSS_AMOUNT"));
					trxn.setGrossTransactionCurrency(rs.getString("TX_GROSS_CURRENCY"));
					trxn.setFeeDebitOrCredit(rs.getString("FEE_DEBIT_CREDIT"));
					trxn.setFeeAmount(rs.getLong("FEE_AMOUNT"));
					trxn.setFeeCurrency(rs.getString("FEE_CURRENCY"));
					trxn.setConsumerId(rs.getString("CONSUMER_ID"));
					trxn.setPaymentTrackingId(rs.getString("PAYMENT_TRACKING_ID"));
					ppStlmntTrxns.add(trxn);
					stlmntSummary.setSettlementTrxns(ppStlmntTrxns);
				}
				ppStlmntTrxns = new ArrayList<ErpSettlementTransactionModel>();
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
		return ppStlmnts;
	}
	
	private ErpAffiliate getErpAffiliate(String accountId) {
		if (ErpServicesProperties.getPPFDAccountIds().contains(accountId)) {
			return ErpAffiliate.getEnum(ErpAffiliate.CODE_FD);
		} else if (ErpServicesProperties.getPPFDWAccountIds().contains(accountId)) {
			return ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW);
		} else {
			throw new RuntimeException("Unknown ErpAffiliate identified " + accountId);
		}
	}
	
	private class Money {
		BigDecimal dollar = null;
		BigDecimal cents = null;
		BigDecimal orig = null;
		
		public Money(long actual) {
			this.cents = BigDecimal.valueOf(actual);
			this.dollar = cents.movePointLeft(2);
			this.orig = cents;
		}
		
		public Money(double actual) {
			this.dollar = BigDecimal.valueOf(actual);
			this.cents = dollar.movePointRight(2);
			this.orig = dollar;
		}
		
		public Money(BigDecimal actual) {
			
			this.orig = actual;
		}
		
		public double getDollar() {
			return dollar.doubleValue();
		}
		
		public long getCents() {
			return cents.longValue();
		}
	}
	
	public static ReconciliationHome lookupReconciliationHome() throws EJBException {
		Context ctx = null;
		try {
			ctx = getInitialContext();
			return (ReconciliationHome) ctx.lookup("freshdirect.payment.Reconciliation");
		} catch (NamingException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException ne) {
				LOGGER.debug(ne);
			}
		}
	}
	
	/** 
	 * helper method to find the naming context for locating objects on a server
	 * 
	 * @throws NamingException any problems encountered locating the remote server
	 * @return the naming context to use to locate remote components on the server
	 */
	private static Context getInitialContext() throws NamingException {

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL()); //t3://localhost:7006
		env.put(Context.INITIAL_CONTEXT_FACTORY, weblogic.jndi.WLInitialContextFactory.class.getName());
		return new InitialContext(env);

	}
	
	private static final String UPDATE_PP_SETTLEMENT = "update CUST.SETTLEMENT set PROCESSED_TIME_DATE = systimestamp, " +
			" ALL_RECORDS_PROCESSED = 'Y' where id = ?  and settlement_source = 'PP' ";
	private static final String UPDATE_PP_SETTLEMENT_TX = "update CUST.SETTLEMENT_TRANSACTION set PROCESSED_TIME_DATE = systimestamp, " +
			" STATUS = 'C' where id = ? and settlement_source = 'PP'";
	public void updatePayPalStatus(Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			LOGGER.debug("Processing PayPal Settlements.");
			conn = this.getConnection();
			if (date != null) {
				ps = conn.prepareStatement(GET_PP_SETTLEMENT_ID);
				ps.setDate(1, new java.sql.Date(date.getTime()));
				ps.setString(2, PaymentMethodType.PP.name());
			} else {
				ps = conn.prepareStatement(GET_ALL_PP_SETTLEMENT_ID);
				ps.setString(1, PaymentMethodType.PP.name());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				ps = conn.prepareStatement(UPDATE_PP_SETTLEMENT);
				ps.setString(1, rs.getString(1));
				ps.executeUpdate();
				ps = conn.prepareStatement(UPDATE_PP_SETTLEMENT_TX);
				ps.setString(1, rs.getString(1));
				ps.executeUpdate();
			}
			
		} catch (Exception e) {
			LOGGER.error("Update failed. Reprocess with force on " + e);
		}
	}
	
	private void cleanUp(Date date) throws SQLException {
		cleanUp(date, null);
	}
	
	private final static String ppAllSummaryQuery = "select id from cust.settlement where process_period_start = ? and settlement_source = 'PP'";
	private final static String ppSummaryUpdate = "delete from cust.settlement where id = ? and settlement_source = 'PP'";
	private final static String ppSummaryTrxnUpdate = "delete from cust.settlement_transaction where settlement_id = ?";
	private final static String ppSummaryDtlUpdate = "delete from cust.settlement_detail where settlement_id = ?";
	private void cleanUp(Date date, String affiliateAccountId) throws SQLException {
		Connection conn = this.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		if (affiliateAccountId != null) {
			ps = conn.prepareStatement(ppSummaryQuery);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setString(2, affiliateAccountId);
			rs = ps.executeQuery();
		} else {
			ps = conn.prepareStatement(ppAllSummaryQuery);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			rs = ps.executeQuery();
		}
		while (rs.next()) {
			String settlement_id = rs.getString(1);
			
			ps = conn.prepareStatement(ppSummaryTrxnUpdate);
			ps.setString(1, settlement_id);
			ps.executeUpdate();
			
			ps = conn.prepareStatement(ppSummaryDtlUpdate);
			ps.setString(1, settlement_id);
			ps.executeUpdate();
			
			ps = conn.prepareStatement(ppSummaryUpdate);
			ps.setString(1, settlement_id);
			ps.executeUpdate();
		}
	}
}
