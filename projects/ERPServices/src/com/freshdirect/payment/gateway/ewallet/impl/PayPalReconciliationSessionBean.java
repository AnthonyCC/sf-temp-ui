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
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.ewallet.ErpPPSettlementInfo;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.Money;
import com.freshdirect.payment.ejb.ReconciliationHome;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.ejb.SettlementSummaryPersistentBean;
import com.freshdirect.payment.gateway.impl.ReconciliationConstants;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.model.ErpSettlementTransactionModel;

public class PayPalReconciliationSessionBean extends SessionBeanSupport {

	private static final Category LOGGER = LoggerFactory.getInstance(PayPalReconciliationSessionBean.class);
	private static final String PAYPAL_SETTLEMENT_IS_LOCKED = "Y";
	private static final String PAYPAL_NO_RECORDS_PROCESSED = "N";
	private static final String PENDING = "P";
	
	private static final String ACQUIRE_PP_LOCK_QUERY = "select ID, IS_LOCKED, ALL_RECORDS_PROCESSED from cust.settlement " +
			"where SETTLEMENT_SOURCE = 'PP' AND PROCESS_PERIOD_START = ? ";
	private static final String ACQUIRE_PENDING_PP_LOCK_QUERY = "select IS_LOCKED, PROCESS_PERIOD_START, ID from cust.settlement " +
			"where SETTLEMENT_SOURCE = 'PP' AND (ALL_RECORDS_PROCESSED != 'Y' OR ALL_RECORDS_PROCESSED is null)";
	private static final String ACQUIRE_PP_LOCK_INSERT = "insert into cust.settlement" +
			" (ID, PROCESS_PERIOD_START, PROCESS_PERIOD_END, BATCH_DATE, BATCH_NUMBER, PROCESS_DATE, DEPOSIT_DATE," +
			" NET_SALES_AMOUNT, NUM_ADJUSTMENTS, ADJUSTMENT_AMOUNT, SETTLEMENT_FILE_DATE, CREATED_TIME_DATE, IS_LOCKED," +
			" SETTLEMENT_SOURCE, ALL_RECORDS_PROCESSED)" +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, systimestamp, ?, ?, ?)";
	private static final String ACQUIRE_PP_LOCK_UDPATE = "update cust.settlement " +
			" set IS_LOCKED = 'Y' where id = ? and settlement_source = 'PP'";
	
	public List<String>  acquirePPLock(Date date) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> settlementIds = new ArrayList<String>();
		String settlementId = null;
		LOGGER.debug("Acquiring PayPal Settlements lock for date - " + date);
		try {
			conn = this.getConnection();
			if (date != null) {
				ps = conn.prepareStatement(ACQUIRE_PP_LOCK_QUERY);
				ps.setDate(1, new java.sql.Date(date.getTime()));

				rs = ps.executeQuery();
				
				while (rs.next()) {
					String locked = rs.getString("IS_LOCKED");
					String processed = rs.getString("ALL_RECORDS_PROCESSED");
					settlementId = rs.getString("ID");
					if ("Y".equals(processed)) {
						throw new EJBException("The batch for date " + date + " are already processed.");
					} else if (locked == null || StringUtils.isEmpty(locked) || locked.equals("N")) {
						PreparedStatement ps2 = conn.prepareStatement(ACQUIRE_PP_LOCK_UDPATE);
						ps2.setString(1, settlementId);
						ps2.executeUpdate();
						resetConnection(ps2, null, null);
						settlementIds.add(settlementId);
					} else if (locked.equals(PAYPAL_SETTLEMENT_IS_LOCKED)) {
						throw new EJBException("[PayPal Batch] Some other process should be running for the same date " + date);
					} else {
						throw new EJBException("Invalid locking state exists for date " + date);
					}
				}

				//new process
				if (settlementIds.isEmpty()) {
					resetConnection(ps, rs, conn);
					byte i = 1;
					conn = this.getConnection();
					ps = conn.prepareStatement(ACQUIRE_PP_LOCK_INSERT);
					settlementId = SequenceGenerator.getNextId(conn, "CUST");
					ps.setString(i++, settlementId);
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
					settlementIds.add(settlementId);
				}
			} else {
				ps = conn.prepareStatement(ACQUIRE_PENDING_PP_LOCK_QUERY);
				rs = ps.executeQuery();
				while (rs.next()) {
					String locked = rs.getString("IS_LOCKED");
					String id = rs.getString("ID");
					if ("Y".equals(locked)) {
						LOGGER.info("Ignoring acquiring lock for settlement id." + id);
					} else if (locked == null || StringUtils.isEmpty(locked) || locked.equals("N")) {
						PreparedStatement ps2 = conn.prepareStatement(ACQUIRE_PP_LOCK_UDPATE);
						ps2.setString(1, id);
						ps2.executeUpdate();
						resetConnection(ps2, null, null);
						settlementIds.add(id);
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new EJBException("SQLException: ", e);
		} finally {
			resetConnection(ps, rs, conn);
		}
		return settlementIds;
	}
	
	public List<String> addPPSettlementSummary(ErpSettlementSummaryModel[] models) {
		Date date = models[0].getProcessPeriodStart();
		List<String> settlementIds = new ArrayList<String>();
		try {
			cleanUp(date);
		} catch (SQLException e) {
			
		}
		for (ErpSettlementSummaryModel model: models) {
			if (model == null) continue;
			try {
				addPPSettlementSummary(model);
				settlementIds.add(model.getId());
			} catch (SQLException e) {
				throw new EJBException(e);
			}
		}
		return settlementIds;
	}
	
	private final static String ppSummaryQuery = "select id from cust.settlement where process_period_start = ? and affiliate_account_id = ?" +
														" and is_locked = 'Y'  and settlement_source = 'PP'";
	private void addPPSettlementSummary(ErpSettlementSummaryModel model) throws SQLException {
		LOGGER.debug("Got a settlement");

		SettlementSummaryPersistentBean bean = new SettlementSummaryPersistentBean(model);
			
		//now add new records
		model.setIsLocked(PAYPAL_SETTLEMENT_IS_LOCKED);
		model.setStatus(PENDING);
		List<ErpSettlementTransactionModel> txModels = model.getSettlementTrxns();
		for (ErpSettlementTransactionModel txModel : txModels) {
			txModel.setStatus(PENDING);
		}
		String id = bean.create(this.getConnection()).getId();
		model.setId(id);
	}
		
	private static final String RELEASE_PP_LOCK_UDPATE = "update cust.settlement " +
			" set IS_LOCKED = 'N' where id = ?";
	public void releasePPLock(List<String> settlementIds) {
		Connection conn = null;
		PreparedStatement ps = null;
		LOGGER.debug("Releasing PayPal Settlements Lock.");

		try {
			conn = this.getConnection();
			for (String settlementId : settlementIds) {
				ps = conn.prepareStatement(RELEASE_PP_LOCK_UDPATE);
				ps.setString(1, settlementId);
				ps.executeUpdate();
				resetConnection(ps, null, null);
			}
		} catch (SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new EJBException("SQLException: ", e);
		} finally {
			resetConnection(null, null, conn);
		}
	}
	
	/**
	 * This method settles trxns and also generates SettlementInfo for PayPal orders for SAP.
	 * @return
	 */
	public List<ErpPPSettlementInfo> processPPSettlements(List<String> ppStlmntIds) throws RemoteException, CreateException, ErpTransactionException {
	
		ReconciliationSB reconsSB = lookupReconciliationHome().create();
		
		List<ErpPPSettlementInfo> settlementInfos = new ArrayList<ErpPPSettlementInfo>();
		
		List<ErpSettlementSummaryModel> ppStlmntTrxns = getPPTrxns(ppStlmntIds);
		if (ppStlmntTrxns == null) {
			return null;
		}
		
		String gatewayOrderId = "";
		String saleId = "";
		int totalTrxns = processPPFee(ppStlmntTrxns, settlementInfos);
		if (totalTrxns == 0)
			return null;
		
		for (ErpSettlementSummaryModel summary : ppStlmntTrxns) {
			ErpAffiliate affiliate = getErpAffiliate(summary.getAffiliateAccountId());
			for (ErpSettlementTransactionModel trxn : summary.getSettlementTrxns()) {
				ErpSettlementModel model = new ErpSettlementModel();

				model.setAuthCode(null);
				model.setAmount(new Money(trxn.getGrossTransactionAmount()).getDollar());
				model.setCardType(EnumCardType.PAYPAL);
				model.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
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
					info = reconsSB.addChargeback(getChargebackModel(trxn, affiliate, summary.getProcessDate()));
				} else if (ErpServicesProperties.getPPCBREventCodes().contains(trxn.getTransactionEventCode())) {
					info = reconsSB.addChargebackReversal(getChargebackReversalModel(trxn, affiliate, summary.getProcessDate()));
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
	
	private int processPPFee(List<ErpSettlementSummaryModel> stlmntTrxns, List<ErpPPSettlementInfo> settlementInfos) {
		long txFee = 0;
		long miscFee = 0;
		int totalTrxns = 0;
		for (ErpSettlementSummaryModel summary : stlmntTrxns) {
			for (ErpSettlementTransactionModel tx : summary.getSettlementTrxns()) {
				totalTrxns++;
				if (ErpServicesProperties.getPPSTLEventCodes().contains(tx.getTransactionEventCode())) {
					txFee += tx.getFeeAmount();
				} else if (ErpServicesProperties.getPPSTFEventCodes().contains(tx.getTransactionEventCode()) ||
						ErpServicesProperties.getPPCBKEventCodes().contains(tx.getTransactionEventCode()) ||
						ErpServicesProperties.getPPREFEventCodes().contains(tx.getTransactionEventCode())) {
					miscFee += tx.getFeeAmount();
				}
			}
		}
		
		if (totalTrxns <= 0)
			return 0;
		ErpPPSettlementInfo txFeeInfo = new ErpPPSettlementInfo("FeeTrxnNOInvoice", ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
		txFeeInfo.setAmount(new Money(txFee).getDollar());
		txFeeInfo.setTxEventCode(ReconciliationConstants.FEE_KEY);
		if (txFee < 0)
			LOGGER.error("Unexpected Tx fee in settlement ");
		else
			settlementInfos.add(txFeeInfo);
		
		ErpPPSettlementInfo miscFeeInfo = new ErpPPSettlementInfo("FeeTrxnNOInvoice", ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
		miscFeeInfo.setAmount(new Money(miscFee).getDollar());
		miscFeeInfo.setTxEventCode(ReconciliationConstants.MISC_FEE_KEY);
		if (miscFee < 0)
			LOGGER.error("Unexpected Misc fee in settlement ");
		else if (miscFee > 0)
			settlementInfos.add(miscFeeInfo);
		return totalTrxns;
	}

	private static final String GET_PP_SETTLEMENT_SUMMARY = "select ID, AFFILIATE_ACCOUNT_ID, TOTAL_TRANS_FEE_CREDIT, " +
															"TOTAL_TRANS_FEE_DEBIT, ALL_RECORDS_PROCESSED, PROCESSED_TIME_DATE " +
			" FROM CUST.SETTLEMENT where id = ? AND SETTLEMENT_SOURCE = 'PP' ";
	
	private static final String GET_PP_SETTLEMENT_TRXNS = "select ID, TRXN_ID, GATEWAY_ORDER_ID, PP_REF_ID, PP_REF_TYPE, " +
			"TX_EVENT_CODE, TX_INITIATION_DATE, TX_COMPLETION_DATE, TX_DEBIT_CREDIT, TX_GROSS_AMOUNT, " +
			"TX_GROSS_CURRENCY, FEE_DEBIT_CREDIT, FEE_AMOUNT, FEE_CURRENCY, CUSTOM_FIELD, CONSUMER_ID, PAYMENT_TRACKING_ID," +
			" BANK_REF_ID, STATUS " +
			"FROM CUST.SETTLEMENT_TRANSACTION where SETTLEMENT_ID = ? AND PROCESSED_TIME_DATE IS NULL AND STATUS = 'P'";
	
	private List<ErpSettlementSummaryModel> getPPTrxns(List<String> ppStlmntIds) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ErpSettlementSummaryModel> ppStlmnts = new ArrayList<ErpSettlementSummaryModel>();

		try{
			LOGGER.debug("Processing PayPal Settlements.");
			conn = this.getConnection();

			String allRecordsProcessed = null;
			for (String settlementId : ppStlmntIds) {
				ErpSettlementSummaryModel summary = new ErpSettlementSummaryModel();
				summary.setId(settlementId);
				ps = conn.prepareStatement(GET_PP_SETTLEMENT_SUMMARY);
				ps.setString(1, settlementId);
				rs = ps.executeQuery();
				if (rs.next()) {
					Date processedDate = rs.getDate("PROCESSED_TIME_DATE");
					allRecordsProcessed = rs.getString("ALL_RECORDS_PROCESSED");
					String affiliate = rs.getString("AFFILIATE_ACCOUNT_ID");
					if (affiliate != null) {
						summary.setAffiliateAccountId(rs.getString("AFFILIATE_ACCOUNT_ID"));
						summary.setTotalTransactionFeeCredit(rs.getLong("TOTAL_TRANS_FEE_CREDIT"));
						summary.setTotalTransactionFeeDebit(rs.getLong("TOTAL_TRANS_FEE_DEBIT"));
					
						ppStlmnts.add(summary);
					}
				}
			}
			if (ppStlmnts.isEmpty()) {
				LOGGER.info("PayPal Settlement - No data found for today or supplied date");
				return null;
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
		
		List<ErpSettlementTransactionModel> ppStlmntTrxns = new ArrayList<ErpSettlementTransactionModel>();
		for (ErpSettlementSummaryModel stlmntSummary: ppStlmnts) {
			try {
				conn = getConnection();
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
				}
				stlmntSummary.setSettlementTrxns(ppStlmntTrxns);
				ppStlmntTrxns = new ArrayList<ErpSettlementTransactionModel>();
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
			" STATUS = 'C' where settlement_id = ? ";
	public void updatePayPalStatus(List<String> settlementIds) {
		Connection conn = null;
		PreparedStatement ps = null;

		LOGGER.debug("Updating status PayPal Settlements.");

		for (String settlementId : settlementIds) {
			
			try{
				conn = this.getConnection();
				ps = conn.prepareStatement(UPDATE_PP_SETTLEMENT);
				ps.setString(1, settlementId);
				ps.executeUpdate();
			} catch (Exception e) {
				LOGGER.error("Update failed. Ignoring " + e);
			} finally {
				try{
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
			
			try {
				conn = this.getConnection();
				ps = conn.prepareStatement(UPDATE_PP_SETTLEMENT_TX);
				ps.setString(1, settlementId);
				ps.executeUpdate();
			} catch (SQLException e) {
				LOGGER.info("Update of trxn records failed in settlement id " + settlementId);
			} finally {
				try{

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
	
	private void resetConnection(PreparedStatement ps, ResultSet rs, Connection conn) {
		try {
		if (ps != null)
			ps.close();
		if (rs != null)
			rs.close();
		if (conn != null)
			conn.close();
		} catch (SQLException e) {
			LOGGER.warn("[PayPal Batch]", e);
		}
		//conn = this.getConnection();
	}
}
