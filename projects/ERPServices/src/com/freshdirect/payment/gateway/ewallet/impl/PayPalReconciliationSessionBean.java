package com.freshdirect.payment.gateway.ewallet.impl;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.Money;
import com.freshdirect.payment.ejb.PayPalSettlementTransactionCodes;
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
	private static final String DELETE_EXISING_RECORDS = "delete from cust.settlement where SETTLEMENT_SOURCE='PP' and PROCESS_PERIOD_START = ? and AFFILIATE_ACCOUNT_ID is null";
	
	public Map<String, Object>  acquirePPLock(Date date) {
		boolean isNEwRecordRequired = false;
		List<String> settlementIds = new ArrayList<String>();
		String settlementId = null;
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps_query_by_date = null;
		PreparedStatement ps_delete_existing_record = null;
		PreparedStatement ps_update_by_settlement_id = null;
		PreparedStatement ps_insert_new = null;
		PreparedStatement ps_query_pending = null;

		LOGGER.debug("Acquiring PayPal Settlements lock for date - " + date);
		try {
			conn = this.getConnection();
			
			if (date != null) {
				/*ps_delete_existing_record = conn.prepareStatement(DELETE_EXISING_RECORDS);
				ps_delete_existing_record.setDate(1, new java.sql.Date(date.getTime()));
				ps_delete_existing_record.executeQuery();
				*/
				ps_query_by_date = conn.prepareStatement(ACQUIRE_PP_LOCK_QUERY);
				ps_query_by_date.setDate(1, new java.sql.Date(date.getTime()));

				rs = ps_query_by_date.executeQuery();
				ps_update_by_settlement_id = conn.prepareStatement(ACQUIRE_PP_LOCK_UDPATE);
				while (rs.next()) {
					String locked = rs.getString("IS_LOCKED");
					String processed = rs.getString("ALL_RECORDS_PROCESSED");
					settlementId = rs.getString("ID");
					if ("Y".equals(processed)) {
						throw new EJBException("The batch for date " + date + " are already processed.");
					} else if (locked == null || StringUtils.isEmpty(locked) || locked.equals("N")) {
						ps_update_by_settlement_id.setString(1, settlementId);
//						ps_update_by_settlement_id.executeUpdate();
						ps_update_by_settlement_id.addBatch();
						settlementIds.add(settlementId);
					} else if (locked.equals(PAYPAL_SETTLEMENT_IS_LOCKED)) {
						throw new EJBException("[PayPal Batch] Some other process should be running for the same date " + date);
					} else {
						throw new EJBException("Invalid locking state exists for date " + date);
					}
				}
				
				ps_update_by_settlement_id.executeBatch();

				//new process
				if (settlementIds.isEmpty()) {
					isNEwRecordRequired = true;
					/*ps_insert_new = insertNewSettlementRecord(date,
							settlementIds, conn);*/
				}
			} else {
				ps_query_pending = conn.prepareStatement(ACQUIRE_PENDING_PP_LOCK_QUERY);
				rs = ps_query_pending.executeQuery();
				ps_update_by_settlement_id = conn.prepareStatement(ACQUIRE_PP_LOCK_UDPATE);
				while (rs.next()) {
					String locked = rs.getString("IS_LOCKED");
					String id = rs.getString("ID");
					if ("Y".equals(locked)) {
						LOGGER.info("Ignoring acquiring lock for settlement id." + id);
					} else if (locked == null || StringUtils.isEmpty(locked) || locked.equals("N")) {
						ps_update_by_settlement_id.setString(1, id);
//						ps_update_by_settlement_id.executeUpdate();
						ps_update_by_settlement_id.addBatch();
						settlementIds.add(id);
					}
				}
				ps_update_by_settlement_id.executeBatch();
			}
		} catch (SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new EJBException("SQLException: ", e);
		} finally {
			resetConnection(ps_query_by_date, rs, null);			
			resetConnection(ps_update_by_settlement_id, null, null);
			resetConnection(ps_query_pending, null, null);
			resetConnection(ps_delete_existing_record, null, null);
			resetConnection(null, null, conn);
		}
		Map<String, Object> lockInfo = new HashMap<String, Object>();
		lockInfo.put("isNewRecord", isNEwRecordRequired);
		lockInfo.put("settlementIds", settlementIds);
		return lockInfo;
	}

	public void insertNewSettlementRecord(Date date,
			List<String> settlementIds, Connection conn) {
		PreparedStatement ps_insert_new = null;
		if(null == conn){
	try {
		conn = this.getConnection();
				
		String settlementId;
		
		byte i = 1;
		ps_insert_new = conn.prepareStatement(ACQUIRE_PP_LOCK_INSERT);
		settlementId = SequenceGenerator.getNextId(conn, "CUST");
		ps_insert_new.setString(i++, settlementId);
		ps_insert_new.setDate(i++, new java.sql.Date(date.getTime()));
		ps_insert_new.setDate(i++, new java.sql.Date(date.getTime()));
		ps_insert_new.setDate(i++, new java.sql.Date(date.getTime()));
		ps_insert_new.setLong(i++, date.getTime());
		ps_insert_new.setDate(i++, new java.sql.Date(date.getTime()));
		ps_insert_new.setDate(i++, new java.sql.Date(date.getTime()));
		ps_insert_new.setLong(i++, 0);
		ps_insert_new.setLong(i++, 0);
		ps_insert_new.setLong(i++, 0);
		ps_insert_new.setDate(i++, new java.sql.Date(date.getTime()));
		ps_insert_new.setString(i++, PAYPAL_SETTLEMENT_IS_LOCKED);
		ps_insert_new.setString(i++, EnumPaymentMethodType.PAYPAL.getName());
		ps_insert_new.setString(i++, PAYPAL_NO_RECORDS_PROCESSED);
		ps_insert_new.executeUpdate();
		settlementIds.add(settlementId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 finally {
		 resetConnection(ps_insert_new, null, conn);
	 }
		}
	}
	
	public List<String> addPPSettlementSummary(ErpSettlementSummaryModel[] models) {
		Date date = models[0].getProcessPeriodStart();
		List<String> settlementIds = new ArrayList<String>();
		try {
			cleanUp(date);
		} catch (SQLException e) {
			LOGGER.warn("[PayPal Batch]", e);
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
		Connection conn = null;
		String id;
		try {
			conn = this.getConnection();
			id = bean.create(conn).getId();
		} finally {
			close(conn);
		}
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
			ps = conn.prepareStatement(RELEASE_PP_LOCK_UDPATE);
			for (String settlementId : settlementIds) {
				ps.setString(1, settlementId);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			LOGGER.debug("SQLException: ", e);
			throw new EJBException("SQLException: ", e);
		} finally {
			resetConnection(ps, null, conn);
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
				if (null !=PayPalSettlementTransactionCodes.EnumPPSTLEventCode.getEnum(trxn.getTransactionEventCode()))
					info = reconsSB.addSettlement(model, saleId, affiliate, false);
				else if (null !=PayPalSettlementTransactionCodes.EnumPPREFEventCode.getEnum(trxn.getTransactionEventCode()))
					info = reconsSB.addSettlement(model, saleId, affiliate, true);
				else if (null !=PayPalSettlementTransactionCodes.EnumPPCBKEventCode.getEnum(trxn.getTransactionEventCode())) {
					info = reconsSB.addChargeback(getChargebackModel(trxn, affiliate, trxn.getTransactionInitiationDate()));
				} else if (null !=PayPalSettlementTransactionCodes.EnumPPCBREventCode.getEnum(trxn.getTransactionEventCode())) {
					info = reconsSB.addChargebackReversal(getChargebackReversalModel(trxn, affiliate, trxn.getTransactionInitiationDate()));
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
					LOGGER.error("[PayPal Batch]", e);
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
	
	public int processPPFee(List<ErpSettlementSummaryModel> stlmntTrxns, List<ErpPPSettlementInfo> settlementInfos) {
		long txFee = 0;
		long miscFee = 0;
		int totalTrxns = 0;
		for (ErpSettlementSummaryModel summary : stlmntTrxns) {
			for (ErpSettlementTransactionModel tx : summary.getSettlementTrxns()) {
				totalTrxns++;
				if (null !=PayPalSettlementTransactionCodes.EnumPPSTLEventCode.getEnum(tx.getTransactionEventCode())) {
					txFee += tx.getFeeAmount();
				} else if (null !=PayPalSettlementTransactionCodes.EnumPPSTFEventCode.getEnum(tx.getTransactionEventCode()) ||
						null !=PayPalSettlementTransactionCodes.EnumPPCBKEventCode.getEnum(tx.getTransactionEventCode()) ||
								null !=PayPalSettlementTransactionCodes.EnumPPREFEventCode.getEnum(tx.getTransactionEventCode())) {
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
	
	private static final String GET_PP_SETTLEMENT_TRXNS = /*"select ID, TRXN_ID, GATEWAY_ORDER_ID, PP_REF_ID, PP_REF_TYPE, " +
			"TX_EVENT_CODE, TX_INITIATION_DATE, TX_COMPLETION_DATE, TX_DEBIT_CREDIT, TX_GROSS_AMOUNT, " +
			"TX_GROSS_CURRENCY, FEE_DEBIT_CREDIT, FEE_AMOUNT, FEE_CURRENCY, CUSTOM_FIELD, CONSUMER_ID, PAYMENT_TRACKING_ID," +
			" BANK_REF_ID, STATUS " +
			"FROM CUST.SETTLEMENT_TRANSACTION where SETTLEMENT_ID = ? AND PROCESSED_TIME_DATE IS NULL AND STATUS = 'P'";*/
	
	"select ID, TRXN_ID, GATEWAY_ORDER_ID, PP_REF_ID, PP_REF_TYPE, " +
	"TX_EVENT_CODE, TX_INITIATION_DATE, TX_COMPLETION_DATE, TX_DEBIT_CREDIT, TX_GROSS_AMOUNT, " +
	"TX_GROSS_CURRENCY, FEE_DEBIT_CREDIT, FEE_AMOUNT, FEE_CURRENCY, CUSTOM_FIELD, CONSUMER_ID, PAYMENT_TRACKING_ID," +
	" BANK_REF_ID, STATUS " +
	"FROM CUST.SETTLEMENT_TRANSACTION where SETTLEMENT_ID = ? ";
	
	public List<ErpSettlementSummaryModel> getPPTrxns(List<String> ppStlmntIds) {
		
		Connection conn = null;
		PreparedStatement psStlmntSumm = null;
		PreparedStatement psTrxns = null;
		ResultSet rs = null;
		List<ErpSettlementSummaryModel> ppStlmnts = new ArrayList<ErpSettlementSummaryModel>();

		try{
			LOGGER.debug("Processing PayPal Settlements.");
			conn = this.getConnection();

			psStlmntSumm = conn.prepareStatement(GET_PP_SETTLEMENT_SUMMARY);
			for (String settlementId : ppStlmntIds) {
				ErpSettlementSummaryModel summary = new ErpSettlementSummaryModel();
				summary.setId(settlementId);
				psStlmntSumm.setString(1, settlementId);
				rs = psStlmntSumm.executeQuery();
				if (rs.next()) {
					String affiliate = rs.getString("AFFILIATE_ACCOUNT_ID");
					if (affiliate != null) {
						summary.setAffiliateAccountId(rs.getString("AFFILIATE_ACCOUNT_ID"));
						summary.setTotalTransactionFeeCredit(rs.getLong("TOTAL_TRANS_FEE_CREDIT"));
						summary.setTotalTransactionFeeDebit(rs.getLong("TOTAL_TRANS_FEE_DEBIT"));
						summary.setProcessDate(rs.getTimestamp("PROCESSED_TIME_DATE"));
					
						ppStlmnts.add(summary);
					}
				}
				resetConnection(null, rs, null);
			}
			if (ppStlmnts.isEmpty()) {
				LOGGER.info("PayPal Settlement - No data found for today or supplied date");
				return null;
			}
									
			List<ErpSettlementTransactionModel> ppStlmntTrxns = new ArrayList<ErpSettlementTransactionModel>();
			psTrxns = conn.prepareStatement(GET_PP_SETTLEMENT_TRXNS);
			for (ErpSettlementSummaryModel stlmntSummary: ppStlmnts) {
				psTrxns.setString(1, stlmntSummary.getId());
				rs = psTrxns.executeQuery();
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
					trxn.setStatus(rs.getString("STATUS"));
					ppStlmntTrxns.add(trxn);
				}
				stlmntSummary.setSettlementTrxns(ppStlmntTrxns);
				ppStlmntTrxns = new ArrayList<ErpSettlementTransactionModel>();
				resetConnection(null, rs, null);
			}
		}	
		catch(SQLException e){
			LOGGER.debug("[PayPal Batch] SQLException: ", e);
			throw new EJBException("[PayPal Batch] SQLException: ", e);
		}finally{
			resetConnection(psStlmntSumm, rs, null);
			resetConnection(psTrxns, null, conn);
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
	
	private static final String UPDATE_PP_SETTLEMENT = "update CUST.SETTLEMENT S set PROCESSED_TIME_DATE = systimestamp, " +
			" ALL_RECORDS_PROCESSED = 'Y' where id = ?  and settlement_source = 'PP' and not exists(select 1 from CUST.SETTLEMENT_TRANSACTION ST where ST.settlement_id=s.id and status='P') ";
	
	public void updatePayPalStatus(List<String> settlementIds) {
		Connection conn = null;
		PreparedStatement psStlmntUpd = null;
		PreparedStatement psTrxnsUpd = null;

		LOGGER.debug("Updating status PayPal Settlements.");
		
		try {
			conn = this.getConnection();
			psStlmntUpd = conn.prepareStatement(UPDATE_PP_SETTLEMENT);
//			psTrxnsUpd = conn.prepareStatement(UPDATE_PP_SETTLEMENT_TX);
			for (String settlementId : settlementIds) {
				psStlmntUpd.setString(1, settlementId);
				psStlmntUpd.executeUpdate();
//				psTrxnsUpd.setString(1, settlementId);
//				psTrxnsUpd.executeUpdate();
			}
		} catch (SQLException e) {
			LOGGER.debug("[PayPal Batch] SQLException: ", e);
			throw new EJBException("[PayPal Batch] SQLException: ", e);
		}
		finally {
			resetConnection(psStlmntUpd, null, null);
			resetConnection(psTrxnsUpd, null, conn);
		}
	}
	
	private static final String UPDATE_PP_SETTLEMENT_TX_BY_ID = "update CUST.SETTLEMENT_TRANSACTION set PROCESSED_TIME_DATE = systimestamp, " +
			" STATUS = 'C' where id = ? ";
	
	public void updatePPSettlementTransStatus(String settlementTransId){
		Connection conn = null;;
		PreparedStatement psTrxnsUpd = null;

		LOGGER.debug("Update PayPal Settlement Transaction Status.."+settlementTransId);
		if(null !=settlementTransId){
			try {
				conn = this.getConnection();
				psTrxnsUpd = conn.prepareStatement(UPDATE_PP_SETTLEMENT_TX_BY_ID);							
				psTrxnsUpd.setString(1, settlementTransId);
				psTrxnsUpd.executeUpdate();
			} catch (SQLException e) {
				LOGGER.debug("[PayPal Batch] SQLException: ", e);
				throw new EJBException("[PayPal Batch] SQLException: ", e);
			}
			finally {
				resetConnection(psTrxnsUpd, null, conn);
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
		PreparedStatement psSummByAff = null;
		PreparedStatement psSummAll = null;
		PreparedStatement psSummUpd = null;
		PreparedStatement psTrxnUpd = null;
		PreparedStatement psDtlUpd = null;
		ResultSet rs = null;
		try {
			if (affiliateAccountId != null) {
				psSummByAff = conn.prepareStatement(ppSummaryQuery);
				psSummByAff.setDate(1, new java.sql.Date(date.getTime()));
				psSummByAff.setString(2, affiliateAccountId);
				rs = psSummByAff.executeQuery();
			} else {
				psSummAll = conn.prepareStatement(ppAllSummaryQuery);
				psSummAll.setDate(1, new java.sql.Date(date.getTime()));
				rs = psSummAll.executeQuery();
			}
			
			psTrxnUpd = conn.prepareStatement(ppSummaryTrxnUpdate);
			psDtlUpd = conn.prepareStatement(ppSummaryDtlUpdate);
			psSummUpd = conn.prepareStatement(ppSummaryUpdate);
			while (rs.next()) {
				String settlement_id = rs.getString(1);
				
				psTrxnUpd.setString(1, settlement_id);
				psTrxnUpd.executeUpdate();
				
				psDtlUpd.setString(1, settlement_id);
				psDtlUpd.executeUpdate();
				
				psSummUpd.setString(1, settlement_id);
				psSummUpd.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			resetConnection(psSummByAff, rs, null);
			resetConnection(psSummAll, null, null);
			resetConnection(psTrxnUpd, null, null);
			resetConnection(psDtlUpd, null, null);
			resetConnection(null, null, conn);
		}
	}
	
	private void resetConnection(PreparedStatement ps, ResultSet rs, Connection conn) {
		try {
			DaoUtil.closePreserveException(rs, ps, conn);
		} catch (SQLException e) {
			LOGGER.warn("[PayPal Batch]", e);
		}
		//conn = this.getConnection();
	}
	
	DateFormat sd=new SimpleDateFormat("EEE, MMM d, yyyy");
	
	public Map<String,String> getPPSettlementNotProcessed() {
		
		Map<String,String> ppNotProcessed=new HashMap<String,String>();
		Connection conn = null;
		PreparedStatement psStlmnt= null;
		ResultSet rs = null;

		try{
			LOGGER.debug(" START getPPSettlementNotProcessed.");
			  conn = this.getConnection();
			  psStlmnt=conn.prepareStatement(" SELECT BATCH_DATE FROM CUST.SETTLEMENT WHERE SETTLEMENT_SOURCE='PP' AND TRUNC(BATCH_DATE) >=TRUNC(SYSDATE)-6  ORDER BY BATCH_DATE DESC");
			  rs = psStlmnt.executeQuery();
			  while(rs.next()){
					ppNotProcessed.put(sd.format(rs.getDate("BATCH_DATE")), sd.format(rs.getDate("BATCH_DATE")));
			}

		  }	catch(SQLException e){
			LOGGER.debug("[getPPSettlementNotProcessed] SQLException: ", e);
			throw new EJBException("[getPPSettlementNotProcessed] SQLException: ", e);
		  }finally{
			resetConnection(psStlmnt, rs, conn);
		  }
		return ppNotProcessed;
	}
}