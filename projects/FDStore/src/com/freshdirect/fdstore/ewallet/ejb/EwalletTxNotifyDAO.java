package com.freshdirect.fdstore.ewallet.ejb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.fdstore.ewallet.EwalletPostBackModel;
import com.freshdirect.framework.util.log.LoggerFactory;

//TODO merge with other EwalletNotifyDAO
public class EwalletTxNotifyDAO {
	
	@SuppressWarnings( "unused" )
	private static Category LOGGER = LoggerFactory.getInstance(EwalletTxNotifyDAO.class);

	private static final String CURRENCY = "USD";
	
	private static final String UNAVAIL_AUTH_CODE = "UNAVBL";
	
	private HashMap<String, String> orderAmountMap = new HashMap<String, String>();
	private Map<String, Date> orderPurchDateMap = new HashMap<String, Date>();
	private Map<String, String> orderCustomerIdMap = new HashMap<String, String>();
	
	/**********************************************************************
	 * INSERT SQL statements to pull the transactions for post back
	 * 		available during job run time and prior to it.
	 **********************************************************************/
	//Below code can be used for posting trxns separately instead of consolidating at order level
/*	private static final String IDENTIFY_SETTLEMENT_TRXNS_FOR_POSTBACK = "insert into CUST.ewallet_txnotify (id, status, ewallet_id, vendor_ewallet_id, transaction_id, " +
																							"customer_id, order_id, salesaction_id, notify_status) ( " +
																				"select CUST.SYSTEM_SEQ.nextval, a.status, f.ewallet_id, f.vendor_ewallet_id, f.ewallet_trxn_id, " +
																						"a.customer_id, a.id sale_id, b.id salesaction_id, 'Pending' " +
																				"from cust.sale a, CUST.salesaction b, cust.payment c, " +
																					"(select sale_id, ewallet_id, vendor_ewallet_id, max(d.ewallet_tx_id) ewallet_trxn_id, max(e.id) " +
																						"from CUST.paymentinfo_new d, CUST.salesaction e " +
																						"where d.salesaction_id = e.id and (e.action_type = 'CRO' or e.action_type = 'MOD') " +
																						"group by (sale_id, ewallet_id, vendor_ewallet_id )) f " +
																				"where a.id = b.sale_id and b.id = c.salesaction_id and a.id = f.sale_id and " +
																					"a.status in ('"+
																						EnumSaleStatus.SETTLED.getStatusCode() + "', '" +
																						EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode() + "') and " +
																					"f.ewallet_trxn_id is not null and " +
																					"a.status = b.action_type and " +
																					"b.action_date > (sysdate - 7) and " +
																					"not exists (select 1 " +
																									"from cust.ewallet_txnotify g " +
																									"where b.id = g.salesaction_id))";
	
	private static final String IDENTIFY_OFFLINE_AUF_TRXNS_FOR_POSTBACK = "insert into CUST.ewallet_txnotify (id, status, ewallet_id, vendor_ewallet_id, transaction_id, " +
																							"customer_id, order_id, salesaction_id, notify_status) ( " +
																			"select CUST.SYSTEM_SEQ.nextval, a.status, f.ewallet_id, f.vendor_ewallet_id, f.ewallet_trxn_id, " +
																					"a.customer_id, a.id sale_id, b.id  salesaction_id, 'Pending' " +
																				"from cust.sale a, CUST.salesaction b, cust.payment c, " +
																					"(select sale_id, ewallet_id, vendor_ewallet_id, max(d.ewallet_tx_id) ewallet_trxn_id, max(e.id) " +
																						"from CUST.paymentinfo_new d, CUST.salesaction e " +
																						"where d.salesaction_id = e.id and (e.action_type = 'CRO' or e.action_type = 'MOD') " +
																						"group by sale_id, ewallet_id, vendor_ewallet_id ) f " +
																				"where a.id = b.sale_id and b.id = c.salesaction_id and a.id = f.sale_id and " +
																					"a.status = '"+
																						EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "' and " +
																					"b.action_type = '"+
																						EnumSaleStatus.AUTHORIZED.getStatusCode() + "' and " +
																					"f.ewallet_trxn_id is not null and " +
																					"c.response_code != 'A' and " +
																					"b.action_date > (sysdate - 7) and " +
																					"not exists (select 1 " +
																									"from cust.ewallet_txnotify g " +
																									"where b.id = g.salesaction_id))";
	
	private static final String IDENTIFY_ONLINE_AUF_FOR_POSTBACK = "insert into cust.ewallet_txnotify (id, status, ewallet_id, transaction_id, customer_id, " +
																				"order_id, gateway_activity_log_id, notify_status) (" +
																			"select CUST.SYSTEM_SEQ.nextval, 'AUF', a.ewallet_id, a.ewallet_tx_id, a.customer_id," +
																				"SUBSTR(a.ORDER_ID,1,INSTRB(a.ORDER_ID, 'X', 1, 1)-1), a.id, 'Pending' " +
																			"from MIS.gateway_activity_log a " +
																			"where a.transaction_time > (sysdate -7) and a.transaction_type = 'AUTHORIZE' and " +
																				"a.ewallet_tx_id is not null and " +
																				"a.is_approved = 'N' and a.status_code != 'ERROR' )";*/
	
	private static final String IDENTIFY_SETTLEMENT_TRXNS_FOR_POSTBACK = 
			"insert into CUST.ewallet_txnotify (id, status, ewallet_id, vendor_ewallet_id, transaction_id, " +
					"customer_id, order_id, salesaction_id, notify_status) ( " +
						"select CUST.SYSTEM_SEQ.nextval, a.status, f.ewallet_id, f.vendor_ewallet_id, f.ewallet_trxn_id, " +
							"a.customer_id, a.id sale_id, b.id salesaction_id, 'Pending' " +
						"from cust.sale a, CUST.salesaction b, cust.payment c, " +
							"(select sale_id, ewallet_id, vendor_ewallet_id, max(d.ewallet_tx_id) ewallet_trxn_id " +
								"from CUST.paymentinfo_new d, CUST.salesaction e " +
								"where d.salesaction_id = e.id and (e.action_type = 'CRO' or e.action_type = 'MOD') and e.action_date > (sysdate - 7) " +
								"group by (sale_id, ewallet_id, vendor_ewallet_id )) f, " +
							"(select sale_id, max(id) latest_salesaction_id " +
								"from CUST.salesaction " +
								"where (action_type = 'STF' or action_type = 'STL') and action_date > (sysdate - 7) " +
								"group by (sale_id)) g " +
						"where a.id = b.sale_id and b.id = c.salesaction_id and b.id = g.latest_salesaction_id and a.id = f.sale_id and a.id = g.sale_id and " +
						"a.status in ('STL', 'STF') and " +
						"f.ewallet_trxn_id is not null and " +
						"a.status = b.action_type and " +
						"b.action_date > (sysdate - 7) and " +
							"not exists (select 1 " +
										"from cust.ewallet_txnotify h " +
										"where b.id = h.salesaction_id))";
	
	//TODO unit test
	private static final String IDENTIFY_OFFLINE_AUF_TRXNS_FOR_POSTBACK =
		"insert into CUST.ewallet_txnotify (id, status, ewallet_id, vendor_ewallet_id, transaction_id, " +
			"customer_id, order_id, salesaction_id, notify_status) ( " +
				"select CUST.SYSTEM_SEQ.nextval, a.status, f.ewallet_id, f.vendor_ewallet_id, f.ewallet_trxn_id, " +
					"a.customer_id, a.id sale_id, b.id  salesaction_id, 'Pending' " +
				"from cust.sale a, CUST.salesaction b, cust.payment c, " +
					"(select sale_id, ewallet_id, vendor_ewallet_id, max(d.ewallet_tx_id) ewallet_trxn_id, max(e.id) " +
						"from CUST.paymentinfo_new d, CUST.salesaction e " +
						"where d.salesaction_id = e.id and (e.action_type = 'CRO' or e.action_type = 'MOD' and e.action_date > (sysdate - 7) ) " +
						"group by sale_id, ewallet_id, vendor_ewallet_id ) f, " +
					"(select sale_id, max(id) latest_salesaction_id " +
						"from CUST.salesaction " +
						"where (action_type = 'STF' or action_type = 'STL') and action_date > (sysdate - 7) " +
						"group by (sale_id)) g " +
				"where a.id = b.sale_id and b.id = g.latest_salesaction_id and a.id = f.sale_id and " +
				"a.status = '"+
					EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "' and " +
				"b.action_type = '"+
					EnumSaleStatus.AUTHORIZED.getStatusCode() + "' and " +
				"f.ewallet_trxn_id is not null and " +
				"c.response_code != 'A' and " +
				"b.action_date > (sysdate - 7) and " +
				"not exists (select 1 " +
							"from cust.ewallet_txnotify h " +
							"where b.id = h.salesaction_id))";																			
		
		//TODO unit test
		private static final String IDENTIFY_ONLINE_AUF_FOR_POSTBACK = 
									"insert into cust.ewallet_txnotify (id, status, ewallet_id, transaction_id, customer_id, " +
										"order_id, gateway_activity_log_id, notify_status) (" +
										"select CUST.SYSTEM_SEQ.nextval, 'AUF', a.ewallet_id, a.ewallet_tx_id, a.customer_id," +
										"SUBSTR(a.ORDER_ID,1,INSTRB(a.ORDER_ID, 'X', 1, 1)-1), a.id, 'Pending' " +
										"from MIS.gateway_activity_log a " +
										"where a.transaction_time > (sysdate -7) and a.transaction_type = 'AUTHORIZE' and " +
										"a.ewallet_tx_id is not null and " +
										"a.is_approved = 'N' and a.status_code != 'ERROR' )";
	/****************************************************************
	 * Queries for non GAL (Gateway Activity Log) transactional data
	 ****************************************************************/
	private static final String GET_NONGAL_TRXNS_FOR_POSTBACK = "select a.transaction_id transaction_id, a.salesaction_id salesaction_id, a.order_id order_id, a.status " +
																"from cust.ewallet_txnotify a, cust.ewallet b " +
														   		"where a.ewallet_id = b.id and a.notify_status like 'Pending' and a.status in ('" +
															   			EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "', '" +
																   		EnumSaleStatus.SETTLED.getStatusCode() + "', '" +
																   		EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode() + "') and " +
														   			"a.salesaction_id is not null and " +
														   			"a.transaction_id is not null and " +
													   				"b.ewallet_type like ?";
	
	//Nested SQL is redundant but is included to avoid too may SQLs which cannot be prepared and hence are slow.
	
	//Below query can be used if each trxn should be posted separately
/*	private static final String GET_OTHER_DATA_FOR_ORDER = "select b.id salesaction_id, b.amount amount, c.auth_code auth_code, a.status status " +
																"from cust.sale a, cust.salesaction b, cust.payment c " +
																"where a.id = b.sale_id and b.id = c.salesaction_id and a.status = b.action_type and b.id in (" +
																	"select d.salesaction_id " +
																		"from cust.ewallet_txnotify d, cust.ewallet f " +
																   		"where d.ewallet_id = f.id and d.notify_status like 'Pending' and d.status in ('" +
																		   		EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "', '" +
																		   		EnumSaleStatus.SETTLED.getStatusCode() + "', '" +
																		   		EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode() + "') and " +
																	   		"d.salesaction_id is not null and " +
																   			"d.transaction_id is not null and " +
																	   		"f.ewallet_type like ? )";*/
	
	private static final String GET_AMOUNT_DATA_FOR_ORDER = "select a.id id, sum(b.amount) amount " +
			"from cust.sale a, cust.salesaction b " +
			"where a.id = b.sale_id and b.action_date > (sysdate - 7)  and (b.action_type = 'STF' or b.action_type = 'STL') " +
			"group by a.id";
	
	private static final String GET_AMOUNT_DATA_FOR_AUF_ORDER =
			"select distinct a.id id, b.amount amount " +
				"from cust.sale a, cust.salesaction b, " +
					"(select sale_id, max(id) max_salesaction_id " +
						"from cust.salesaction " +
						"where action_date > (sysdate - 7) and (action_type = 'CRO' or action_type = 'MOD') " +
						"group by sale_id) d " +
				"where a.id = b.sale_id and b.action_date > (sysdate - 7) and b.id = max_salesaction_id";
	
	private static final String GET_OTHER_DATA_FOR_ORDER = "select a.id id, max(b.id) salesaction_id, max(auth_code) auth_code " +
			"from cust.sale a, cust.salesaction b, cust.payment c " +
			"where a.id = b.sale_id and b.id = c.salesaction_id and b.id in (" +
				"select d.salesaction_id " +
					"from cust.ewallet_txnotify d, cust.ewallet f " +
			   		"where d.ewallet_id = f.id and d.notify_status like 'Pending' and d.status in ('" +
					   		EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "', '" +
					   		EnumSaleStatus.SETTLED.getStatusCode() + "', '" +
					   		EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode() + "') and " +
				   		"d.salesaction_id is not null and " +
			   			"d.transaction_id is not null and " +
				   		"f.ewallet_type like ? ) " +
			"group by a.id";
	
	
	//Nested SQL is redundant but is included to avoid too may SQLs which cannot be prepared and hence are slow.
	private static final String GET_ORDER_PURCHASE_DATE = "select b.id sale_id, b.customer_id customer_id, action_date " +
																"from cust.salesaction a, cust.sale b " +
																"where a.sale_id = b.id and action_type = 'CRO' and b.id in (" +
																	" select d.order_id from " +
																	   		"cust.ewallet_txnotify d, cust.sale e, cust.ewallet f " +
																	   		"where d.order_id = e.id and d.notify_status like 'Pending' and e.status in ('" +
																			   		EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "', '" +
																			   		EnumSaleStatus.SETTLED.getStatusCode() + "', '" +
																			   		EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode() + "') and " +
																	   			"d.salesaction_id is not null and " +
																	   			"d.transaction_id is not null and " +
																	   			"a.action_date > (sysdate - 7) and " +
																	   			"d.ewallet_id = f.id and f.ewallet_type like ? )";
	
	/****************************************************************
	 * Queries for GAL (Gateway Activity Log) transactional data
	 ****************************************************************/
	private static final String GET_GAL_TRXNS_FOR_POSTBACK = "select a.transaction_id transaction_id, a.gateway_activity_log_id gateway_activity_log_id, a.order_id order_id " +
																"from cust.ewallet_txnotify a, cust.ewallet b " +
														   		"where a.ewallet_id = b.id and a.notify_status like 'Pending' and a.status = '" +
														   				EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "' and " +
														   			"a.gateway_activity_log_id is not null and " +
														   			"a.transaction_id is not null and " +
													   				"b.ewallet_type like ?";

	private static final String GET_OTHER_DATA_FOR_GAL_ORDER = "select a.id GALId, a.amount amount, a.auth_code auth_code, 'AUF' status, a.transaction_time transaction_time " +
																		"from MIS.gateway_activity_log a " +
																		"where a.transaction_time > (sysdate -7) and a.transaction_type = 'AUTHORIZE' and " +
																			"a.ewallet_tx_id is not null and " +
																			"a.is_approved = 'N' and a.status_code != 'ERROR'";
	
	/****************************************************************
	 * Final update to Post back
	 ****************************************************************/
	private static final String UPDATE_NONGAL_SUCCESS_TXNS = "update cust.ewallet_txnotify set notify_status = 'Completed' where salesaction_id in (";
	private static final String UPDATE_GAL_SUCCESS_TXNS = "update cust.ewallet_txnotify set notify_status = 'Completed' where gateway_activity_log_id in (";
	
	
	public void prepareForPostBack(Connection conn) throws SQLException {
		PreparedStatement settlementPS = conn.prepareStatement( IDENTIFY_SETTLEMENT_TRXNS_FOR_POSTBACK );
		int noOfSettlementRecs = settlementPS.executeUpdate();
		LOGGER.info("Settlement records for Ewallet Postback today : " + noOfSettlementRecs);
		PreparedStatement onlineAufPS = conn.prepareStatement( IDENTIFY_ONLINE_AUF_FOR_POSTBACK );
		int noOfOnlineAUFRecs = onlineAufPS.executeUpdate();
		LOGGER.info("Online Auth Failure records for Ewallet Postback today (GAL) : " + noOfOnlineAUFRecs);
		PreparedStatement offlineAufPS = conn.prepareStatement( IDENTIFY_OFFLINE_AUF_TRXNS_FOR_POSTBACK );
		int noOfOfflineAUFRecs = offlineAufPS.executeUpdate();
		LOGGER.info("Offline Auth Failure records for Ewallet Postback today : " + noOfOfflineAUFRecs);
		
	}
	
	public List<EwalletPostBackModel> getAllTrxnsForPostback( Connection conn, String walletType ) throws SQLException {

		List<EwalletPostBackModel> allTrxns = new ArrayList<EwalletPostBackModel>();
		Map<String, EwalletPostBackModel> nonGALTrxnMap = new HashMap<String, EwalletPostBackModel>();
		
		loadCommonData(conn, walletType);
		
		PreparedStatement nonGALTrxnsPS = conn.prepareStatement( GET_NONGAL_TRXNS_FOR_POSTBACK );
		nonGALTrxnsPS.setString(1, walletType);
		ResultSet nonGALTrxnsRS = nonGALTrxnsPS.executeQuery();
		while (nonGALTrxnsRS.next()) {
			String salesActionId = nonGALTrxnsRS.getString("salesaction_id");
			EwalletPostBackModel pbItem = new EwalletPostBackModel();
			if (nonGALTrxnMap.get(salesActionId) != null) {
				LOGGER.error("################### ERROR Duplicate records while obtaining" +
						" Postback Data. SalesAction Id - " + salesActionId + " ###################");
				continue;
			} else {
				pbItem.setSalesActionId(salesActionId);
				pbItem.setOrderId(nonGALTrxnsRS.getString("order_id"));
				pbItem.setTransactionId(nonGALTrxnsRS.getString("transaction_id"));
				pbItem.setTransactionStatus(getTrxnStatus(nonGALTrxnsRS.getString("status")));
				nonGALTrxnMap.put(salesActionId, pbItem);
			}
		}
	
		PreparedStatement otherDataPS = conn.prepareStatement(GET_OTHER_DATA_FOR_ORDER);
		otherDataPS.setString(1, walletType);
		ResultSet otherDataRS = otherDataPS.executeQuery();
		while (otherDataRS.next()) {
			EwalletPostBackModel pbItem = nonGALTrxnMap.get(otherDataRS.getString("salesaction_id"));
			if (pbItem == null) {
				LOGGER.error("################### ERROR Mismatch in records occured for Other Data  during" +
						" Postback Data. SalesAction Id - " + otherDataRS.getString("salesaction_id") + " - ###################");
				continue;
			}
			loadPostBackReqData(otherDataRS, pbItem);
		}
		
		//Code for post back by salesaction id
/*		PreparedStatement otherDataPS = conn.prepareStatement(GET_OTHER_DATA_FOR_ORDER);
		otherDataPS.setString(1, walletType);
		ResultSet otherDataRS = otherDataPS.executeQuery();
		while (otherDataRS.next()) {
			EwalletPostBackModel pbItem = nonGALTrxnMap.get(otherDataRS.getString("salesaction_id"));
			
			if (pbItem == null) {
				LOGGER.error("################### ERROR Mismatch in records occured for Other Data  during" +
						" Postback Data. SalesAction Id - " + otherDataRS.getString("salesaction_id") + " - ###################");
				continue;
			}
			loadPostBackReqData(otherDataRS, pbItem);
		}*/
		
		for(EwalletPostBackModel trxn : nonGALTrxnMap.values()) {
			trxn.setPurchaseDate(orderPurchDateMap.get(trxn.getOrderId()));
			trxn.setCustomerId(orderCustomerIdMap.get(trxn.getOrderId()));
		}
		
		allTrxns.addAll(nonGALTrxnMap.values());
		allTrxns.addAll(getGALTrxnsForPostback(conn, walletType));
		
		return allTrxns;
	}

	
	private List<EwalletPostBackModel> getGALTrxnsForPostback( Connection conn, String walletType ) throws SQLException {

		Map<String, EwalletPostBackModel> gALTrxnMap = new HashMap<String, EwalletPostBackModel>();
		
		PreparedStatement trxnPS = conn.prepareStatement( GET_GAL_TRXNS_FOR_POSTBACK );
		trxnPS.setString(1, walletType);
		ResultSet trxnRS = trxnPS.executeQuery();

		while (trxnRS.next()) {
			String gALId = trxnRS.getString("gateway_activity_log_id");
			EwalletPostBackModel pbItem = new EwalletPostBackModel();
			pbItem.setgAL(true);
			if (gALTrxnMap.get(gALId) != null) {
				LOGGER.error("################### ERROR Duplicate records while obtaining" +
						" Postback Data. GAL Id - " + gALId + " ###################");
				continue;
			} else {
				String trxnId = trxnRS.getString("transaction_id");
				pbItem.setTransactionId(trxnId);
				pbItem.setOrderId(trxnRS.getString("order_id"));
				pbItem.setgALId(gALId);
				gALTrxnMap.put(gALId, pbItem);
			}

		}

		PreparedStatement otherDataPS = conn.prepareStatement(GET_OTHER_DATA_FOR_GAL_ORDER);

		ResultSet otherDataRS = otherDataPS.executeQuery();
		while (otherDataRS.next()) {
			EwalletPostBackModel pbItem = gALTrxnMap.get(otherDataRS.getString("GALId"));
			if (pbItem == null) {
				LOGGER.error("################### ERROR Mismatch in records occured for Other Data  during" +
						" Postback Data. GAL Id - " + otherDataRS.getString("GALId") + " - ###################");
				continue;
			}
			loadPostBackReqData(otherDataRS, pbItem);
			pbItem.setPurchaseDate(otherDataRS.getDate("transaction_time"));
		}

		return new ArrayList<EwalletPostBackModel>(gALTrxnMap.values());
	}
	
	public void updateTrxnStatus(Connection conn, List<EwalletPostBackModel> resps) throws SQLException {
		
		if (resps.size() <= 0) {
			LOGGER.info("No transactions to post for today ");
			return;
		}
		String nonGALTrxnsStr = "";
		String gALTrxnsStr = "";
		int noOfFailedTrxns = 0;
		for (EwalletPostBackModel resp : resps) {
			if (!resp.isError()) {
				if (!resp.isgAL())
					nonGALTrxnsStr += "'" + resp.getKey() + "', ";
				else
					gALTrxnsStr += "'" + resp.getKey() + "', ";
			}
			else
				noOfFailedTrxns++;
		}
		
		String updQry = "";
		if (!nonGALTrxnsStr.isEmpty()) {
			updQry += UPDATE_NONGAL_SUCCESS_TXNS + nonGALTrxnsStr.substring(0, nonGALTrxnsStr.lastIndexOf(",")) + ")";
		}
		
		if (!updQry.isEmpty()) {
			PreparedStatement ps = conn.prepareStatement(updQry);
			int noOfUpdatedRecs = ps.executeUpdate();
			LOGGER.info("Postback Service : Total updated records with success  for non" + noOfUpdatedRecs + " Total failures are " + noOfFailedTrxns);
			updQry = "";
		}
		
		if (!gALTrxnsStr.isEmpty()) {
			updQry += UPDATE_GAL_SUCCESS_TXNS + gALTrxnsStr.substring(0, nonGALTrxnsStr.lastIndexOf(",")) + ")";
		}
		
		if (!updQry.isEmpty()) {
			PreparedStatement ps = conn.prepareStatement(updQry);
			int noOfUpdatedRecs = ps.executeUpdate();
			LOGGER.info("Postback Service : Total updated records with success " + noOfUpdatedRecs + " Total failures are " + noOfFailedTrxns);
		}
	}
	
	void loadPostBackReqData(ResultSet otherData, EwalletPostBackModel pbItem) throws SQLException {
		pbItem.setOrderAmount((long)(Double.valueOf(orderAmountMap.get(pbItem.getOrderId()))*100));

		pbItem.setCurrency(CURRENCY);
		
		String authCd = otherData.getString("auth_code");
		if (authCd == null || authCd.isEmpty()) {
			authCd = UNAVAIL_AUTH_CODE;
		}
		
		pbItem.setApprovalCode(authCd);
		
		//pbItem.setTransactionStatus(getTrxnStatus(otherData.getString("status")));
		
		//TODO This should be based on configuration
		pbItem.setExpressCheckoutIndicator(false);
	}
	
	private boolean getTrxnStatus(String orderStatus) {
		boolean success = true;
		if (orderStatus.equalsIgnoreCase(EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode()) ||
				orderStatus.equalsIgnoreCase(EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode())) {
			return !success;
		} else if (orderStatus.equalsIgnoreCase(EnumSaleStatus.SETTLED.getStatusCode())) {
			return success;
		} else {
			throw new AssertionError("Status of TxNofiy in DB is incorrect " + orderStatus);
		}
	}
	
	private void loadCommonData(Connection conn, String walletType) throws SQLException {
		PreparedStatement otherDataPS = conn.prepareStatement(GET_AMOUNT_DATA_FOR_ORDER);
		ResultSet otherDataRS = otherDataPS.executeQuery();
		while (otherDataRS.next()) {
			orderAmountMap.put(otherDataRS.getString("id"), otherDataRS.getString("amount"));
		}
		
		PreparedStatement otherAUFDataPS = conn.prepareStatement(GET_AMOUNT_DATA_FOR_AUF_ORDER);
		ResultSet otherAUFDataRS = otherAUFDataPS.executeQuery();
		while (otherAUFDataRS.next()) {
			orderAmountMap.put(otherAUFDataRS.getString("id"), otherAUFDataRS.getString("amount"));
		}
		
		PreparedStatement orderPurchPS = conn.prepareStatement(GET_ORDER_PURCHASE_DATE);
		orderPurchPS.setString(1, walletType);
		ResultSet orderPurchRS = orderPurchPS.executeQuery();

		while (orderPurchRS.next()) {
			orderPurchDateMap.put(orderPurchRS.getString("sale_id"), orderPurchRS.getDate("action_date"));
			orderCustomerIdMap.put(orderPurchRS.getString("sale_id"), orderPurchRS.getString("customer_id"));
		}
	}
	
	//for unit testing purposes only
	public static void main(String args[]) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:fdstore_prda/fdstore_prda@scan-dev.dev.nyc1.freshdirect.com:1521/devint");
		//new EwalletTxNotifyDAO().prepareForPostBack(conn);
/*		PreparedStatement stmt = conn.prepareStatement(IDENTIFY_SETTLEMENT_TRXNS_FOR_POSTBACK);

		int rows = stmt.executeUpdate();
		System.out.println(rows);

		PreparedStatement onlineAUFstmt = conn.prepareStatement(IDENTIFY_ONLINE_AUF_FOR_POSTBACK);

		rows = onlineAUFstmt.executeUpdate();
		System.out.println(rows);
		
		
		PreparedStatement offlineAUFstmt = conn.prepareStatement(IDENTIFY_OFFLINE_AUF_TRXNS_FOR_POSTBACK);

		rows = offlineAUFstmt.executeUpdate();
		System.out.println(rows);
		
		PreparedStatement nonGALTrxnsPS = conn.prepareStatement(GET_NONGAL_TRXNS_FOR_POSTBACK);
		nonGALTrxnsPS.setString(1, "MP");
		ResultSet nonGALTrxnsRS = nonGALTrxnsPS.executeQuery();
		while (nonGALTrxnsRS.next()) {
			System.out.println(nonGALTrxnsRS.getString("salesaction_id"));
		}
		
		PreparedStatement amtDataPS = conn.prepareStatement(GET_AMOUNT_DATA_FOR_ORDER);
		ResultSet amtDataRS = amtDataPS.executeQuery();
		while (amtDataRS.next()) {
			System.out.println(amtDataRS.getString("id") + " , " + amtDataRS.getString("amount"));
		}
		
		PreparedStatement otherDataPS = conn.prepareStatement(GET_OTHER_DATA_FOR_ORDER);
		otherDataPS.setString(1, "MP");
		ResultSet otherDataRS = otherDataPS.executeQuery();
		while (otherDataRS.next()) {
			System.out.println(otherDataRS.getString("auth_code"));
		}
		
		PreparedStatement nonGALPurchDatePS = conn.prepareStatement(GET_ORDER_PURCHASE_DATE);
		nonGALPurchDatePS.setString(1, "MP");
		ResultSet purchDateRS = nonGALPurchDatePS.executeQuery();
		while (purchDateRS.next()) {
			System.out.println(purchDateRS.getString("action_date"));
		}

		PreparedStatement gALTrxnsPS = conn.prepareStatement(GET_GAL_TRXNS_FOR_POSTBACK);
		gALTrxnsPS.setString(1, "MP");
		ResultSet gALTrxnsRS = gALTrxnsPS.executeQuery();
		while (gALTrxnsRS.next()) {
			System.out.println(gALTrxnsRS.getString("gateway_activity_log_id"));
		}
		
		
		PreparedStatement gALOtherDataPS = conn.prepareStatement(GET_OTHER_DATA_FOR_GAL_ORDER);
		ResultSet gALOtherDataRS = gALOtherDataPS.executeQuery();
		while (gALOtherDataRS.next()) {
			System.out.println(gALOtherDataRS.getString("GALId"));
		}*/
		
		EwalletTxNotifyDAO dao = new EwalletTxNotifyDAO();
		System.out.println(dao.getAllTrxnsForPostback(conn, "MP"));
		
		//List<EwalletPostBackModel> data = new EwalletTxNotifyDAO().getAllTrxnsForPostback(conn, "MP");
		conn.close();
	}

}

