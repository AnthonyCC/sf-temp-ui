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
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.ewallet.EwalletOldOrderException;
import com.freshdirect.fdstore.ewallet.EwalletPostBackModel;
import com.freshdirect.framework.util.log.LoggerFactory;

//TODO merge with other EwalletNotifyDAO
public class EwalletTxNotifyDAO {
	
	@SuppressWarnings( "unused" )
	private static Category LOGGER = LoggerFactory.getInstance(EwalletTxNotifyDAO.class);

	private static final String CURRENCY = "USD";
	
	private static final String UNAVAIL_AUTH_CODE = "UNAVBL";
	
	private HashMap<String, String> orderAmountMap = new HashMap<String, String>();
	private HashMap<String, String> gALOrderAmountMap = new HashMap<String, String>();
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
			"insert into CUST.ewallet_txnotify (id, status, ewallet_id, vendor_ewallet_id, transaction_id, "+
					"customer_id, order_id, salesaction_id, notify_status) (  "+
						"select CUST.SYSTEM_SEQ.nextval, s.status, f.ewallet_id, f.vendor_ewallet_id, f.ewallet_trxn_id,  "+
							"s.customer_id, s.id sale_id, sa.id salesaction_id, 'Pending'  "+
						"from cust.sale s, CUST.salesaction sa, cust.payment p,  "+
							"(select sa2.sale_id, pi.ewallet_id, pi.vendor_ewallet_id, max(pi.ewallet_tx_id) ewallet_trxn_id  "+
								"from  CUST.salesaction sa2, CUST.paymentinfo_new pi "+
								"where sa2.id = pi.salesaction_id and (sa2.action_type = 'CRO' or sa2.action_type = 'MOD') "+
                    "and sa2.action_date > (sysdate - 7) "+
                    "and pi.ewallet_tx_id is not null "+
								"group by (sa2.sale_id, pi.ewallet_id, pi.vendor_ewallet_id )) f,  "+
							"(select sa3.sale_id, max(sa3.id) latest_salesaction_id  "+
								"from CUST.salesaction sa3 "+
								"where (sa3.action_type = 'STF' or sa3.action_type = 'STL') and sa3.action_date > (sysdate - 7)  "+
								"group by (sa3.sale_id)) g  "+
						"where s.id = sa.sale_id and sa.id = p.salesaction_id and sa.id = g.latest_salesaction_id and s.id = f.sale_id and s.id = g.sale_id and  "+
							"s.status in ('STL', 'STF') and  "+
							"f.ewallet_trxn_id is not null and  "+
							"s.status = sa.action_type and  "+
							"sa.action_date > (sysdate - 7) and  "+
								"not exists (select 1  "+
											"from cust.ewallet_txnotify h  "+
											"where sa.id = h.salesaction_id))";
	
	//TODO unit test
	private static final String IDENTIFY_OFFLINE_AUF_TRXNS_FOR_POSTBACK =
		"INSERT " +
				"INTO CUST.ewallet_txnotify ( id, status, ewallet_id, vendor_ewallet_id, transaction_id, customer_id, order_id, salesaction_id, notify_status ) " +
					"(SELECT CUST.SYSTEM_SEQ.nextval, 'AUF', f.ewallet_id, f.vendor_ewallet_id, f.ewallet_trxn_id,  s.customer_id, s.id sale_id, sa.id salesaction_id, 'Pending' " +
				    	"FROM cust.sale s, CUST.salesaction sa, cust.payment p, " +
				    		"(SELECT sale_id, ewallet_id, vendor_ewallet_id, MAX(d.ewallet_tx_id) ewallet_trxn_id, MAX(e.id) latest_cromod_sa_id " +
				    			"FROM CUST.paymentinfo_new d, CUST.salesaction e " +
				    			" WHERE d.salesaction_id = e.id AND (e.action_type     = 'CRO' OR e.action_type       = 'MOD') " +
				    					"AND e.action_date      > (sysdate - 7) AND d.ewallet_tx_id   IS NOT NULL " +
				    			"GROUP BY sale_id, ewallet_id, vendor_ewallet_id ) f, " +
				    		"(SELECT i.id, MAX(j.id) max_order_sa_id " +
				    			"FROM cust.sale i, cust.salesaction j, cust.payment n " +
				    			"WHERE i.id = j.sale_id AND j.id = n.salesaction_id AND j.action_date > (sysdate -7) " +
				    				" AND j.action_type     = 'AUT' " +
				    				" AND n.response_code     != 'A' " +
				    			"GROUP BY i.id ) m " +
				   " WHERE s.id  = sa.sale_id AND sa.id = p.salesaction_id AND sa.id = m.max_order_sa_id AND s.id = f.sale_id " +
				    	"AND sa.action_type = 'AUT' AND f.ewallet_trxn_id IS NOT NULL " +
				    	"AND p.response_code != 'A' AND sa.action_date  > (sysdate - 7) AND NOT EXISTS " +
						      "(SELECT 1 " +
						      "FROM cust.ewallet_txnotify g " +
						     " WHERE max_order_sa_id = g.salesaction_id " +
						      ") " +
					 ")";
		
		//TODO unit test
		private static final String IDENTIFY_ONLINE_AUF_FOR_POSTBACK = 
									"insert into cust.ewallet_txnotify (id, status, ewallet_id, transaction_id, customer_id, " +
										"order_id, gateway_activity_log_id, notify_status) (" +
										"select CUST.SYSTEM_SEQ.nextval, 'AUF', gl.ewallet_id, gl.ewallet_tx_id, gl.customer_id," +
										"SUBSTR(gl.ORDER_ID,1,INSTRB(gl.ORDER_ID, 'X', 1, 1)-1), gl.id, 'Pending' " +
										"from MIS.gateway_activity_log gl " +
										"where gl.transaction_time > (sysdate -7) and gl.transaction_type = 'AUTHORIZE' and " +
										"gl.ewallet_tx_id is not null and " +
										"gl.is_approved = 'N' and gl.status_code != 'ERROR'  and " +
										" not exists ( select 1 " +
											"from cust.ewallet_txnotify " +
											"where gateway_activity_log_id = gl.id " +
										"))";
	/****************************************************************
	 * Queries for non GAL (Gateway Activity Log) transactional data
	 ****************************************************************/
	private static final String GET_NONGAL_TRXNS_FOR_POSTBACK = "select ewtxn.transaction_id transaction_id, ewtxn.salesaction_id salesaction_id, ewtxn.order_id order_id, ewtxn.status " +
																"from cust.ewallet_txnotify ewtxn, cust.ewallet ew " +
														   		"where ewtxn.ewallet_id = ew.id and ewtxn.notify_status like 'Pending' and ewtxn.status in ('" +
															   			EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "', '" +
																   		EnumSaleStatus.SETTLED.getStatusCode() + "', '" +
																   		EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode() + "') and " +
														   			"ewtxn.salesaction_id is not null and " +
														   			"ewtxn.transaction_id is not null and " +
													   				"ew.ewallet_type like ?";
	
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
	
	private static final String GET_AMOUNT_DATA_FOR_ORDER = "select s.id id, sum(sa.amount) amount " +
			"from cust.sale s, cust.salesaction sa " +
			"where s.id = sa.sale_id and sa.action_date > (sysdate - 7)  and sa.action_type = 'STL' " +
			"group by s.id";
	
	private static final String GET_AMOUNT_DATA_FOR_STF_AUF_ORDER =
			"select distinct s.id id, sa.amount amount " +
				"from cust.sale s, cust.salesaction sa, " +
					"(select sale_id, max(id) max_salesaction_id " +
						"from cust.salesaction " +
						"where action_date > (sysdate - 7) and (action_type = 'CRO' or action_type = 'MOD') " +
						"group by sale_id) d " +
				"where s.id = sa.sale_id and sa.action_date > (sysdate - 7) and sa.id = max_salesaction_id ";
	
	private static final String GET_OTHER_DATA_FOR_ORDER = "select s.id id, max(sa.id) salesaction_id, max(auth_code) auth_code " +
			"from cust.sale s, cust.salesaction sa, cust.payment p " +
			"where s.id = sa.sale_id and sa.id = p.salesaction_id and sa.id in (" +
				"select ewtxn.salesaction_id " +
					"from cust.ewallet_txnotify ewtxn, cust.ewallet ew " +
			   		"where ewtxn.ewallet_id = ew.id and ewtxn.notify_status like 'Pending' and ewtxn.status in ('" +
					   		EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "', '" +
					   		EnumSaleStatus.SETTLED.getStatusCode() + "', '" +
					   		EnumSaleStatus.SETTLEMENT_FAILED.getStatusCode() + "') and " +
				   		"ewtxn.salesaction_id is not null and " +
			   			"ewtxn.transaction_id is not null and " +
				   		"ew.ewallet_type like ? ) " +
			"group by s.id";
	
	
	//Nested SQL is redundant but is included to avoid too may SQLs which cannot be prepared and hence are slow.
	private static final String GET_ORDER_PURCHASE_DATE = "select sa.customer_id customer_id, sa.sale_id sale_id, max(sa.action_date) action_date " +
																"from cust.salesaction sa " +
																"where (sa.action_type = 'CRO' or sa.action_type = 'MOD' ) and sa.sale_id in (" +
																	" select ewtxn.order_id from " +
																	   		"cust.ewallet_txnotify ewtxn, cust.ewallet ew " +
																	   		"where ewtxn.order_id = sa.sale_id and ewtxn.notify_status like 'Pending' and " +
																	   			"ewtxn.salesaction_id is not null and " +
																	   			"ewtxn.transaction_id is not null and " +
																	   			"sa.action_date > (sysdate - 7) and " +
																	   			"ewtxn.ewallet_id = ew.id and ew.ewallet_type like ?) " +
																"group by sa.customer_id, sa.sale_id ";
	
	/****************************************************************
	 * Queries for GAL (Gateway Activity Log) transactional data
	 ****************************************************************/
	private static final String GET_GAL_TRXNS_FOR_POSTBACK = "select ewtxn.transaction_id transaction_id, ewtxn.gateway_activity_log_id gateway_activity_log_id, ewtxn.order_id order_id " +
																"from cust.ewallet_txnotify ewtxn, cust.ewallet ew " +
														   		"where ewtxn.ewallet_id = ew.id and ewtxn.notify_status like 'Pending' and ewtxn.status = '" +
														   				EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode() + "' and " +
														   			"ewtxn.gateway_activity_log_id is not null and " +
														   			"ewtxn.transaction_id is not null and " +
													   				"ew.ewallet_type like ?";

	private static final String GET_OTHER_DATA_FOR_GAL_ORDER = "select gl.id GALId, gl.amount amount, gl.auth_code auth_code, 'AUF' status, gl.transaction_time transaction_time " +
																		"from MIS.gateway_activity_log gl " +
																		"where gl.transaction_time > (sysdate -7) and gl.transaction_type = 'AUTHORIZE' and " +
																			"gl.ewallet_tx_id is not null and " +
																			"gl.is_approved = 'N' and gl.status_code != 'ERROR' ";
	
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
	
	public List<EwalletPostBackModel> getAllTrxnsForPostback( Connection conn, EnumEwalletType walletType ) throws SQLException {

		List<EwalletPostBackModel> allTrxns = new ArrayList<EwalletPostBackModel>();
		Map<String, EwalletPostBackModel> nonGALTrxnMap = new HashMap<String, EwalletPostBackModel>();
		
		loadCommonData(conn, walletType);
		
		PreparedStatement nonGALTrxnsPS = conn.prepareStatement( GET_NONGAL_TRXNS_FOR_POSTBACK );
		nonGALTrxnsPS.setString(1, walletType.getName());
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
		otherDataPS.setString(1, walletType.getName());
		ResultSet otherDataRS = otherDataPS.executeQuery();
		while (otherDataRS.next()) {
			EwalletPostBackModel pbItem = nonGALTrxnMap.get(otherDataRS.getString("salesaction_id"));
			if (pbItem == null) {
				LOGGER.error("################### ERROR Mismatch in records occured for Other Data  during" +
						" Postback Data. SalesAction Id - " + otherDataRS.getString("salesaction_id") + " - ###################");
				continue;
			}
			
			try {
				loadPostBackReqData(otherDataRS, pbItem);
			} catch (EwalletOldOrderException e) {
				nonGALTrxnMap.remove(otherDataRS.getString("salesaction_id"));
			}
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

	
	private List<EwalletPostBackModel> getGALTrxnsForPostback( Connection conn, EnumEwalletType walletType ) throws SQLException {

		Map<String, EwalletPostBackModel> gALTrxnMap = new HashMap<String, EwalletPostBackModel>();
		
		PreparedStatement trxnPS = conn.prepareStatement( GET_GAL_TRXNS_FOR_POSTBACK );
		trxnPS.setString(1, walletType.getName());
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
			gALOrderAmountMap.put(otherDataRS.getString("GALId"), otherDataRS.getString("amount"));
			if (pbItem == null) {
				LOGGER.error("################### ERROR Mismatch in records occured for Other Data  during" +
						" Postback Data. GAL Id - " + otherDataRS.getString("GALId") + " - ###################");
				continue;
			}
			
			try {
				loadPostBackReqData(otherDataRS, pbItem);
			} catch (EwalletOldOrderException e) {
				gALOrderAmountMap.remove(otherDataRS.getString("GALId"));
			}
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
			if (!resp.isError() ||  (resp.isError() && resp.getRecoverable() != null && resp.getRecoverable().equals("false"))) {
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
			LOGGER.info("Postback Service : Total updated records with success  for " + noOfUpdatedRecs + " Total failures are " + noOfFailedTrxns);
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
	
	void loadPostBackReqData(ResultSet otherData, EwalletPostBackModel pbItem) throws SQLException, EwalletOldOrderException {
		if (pbItem.isgAL()) {
			String amt = gALOrderAmountMap.get(otherData.getString("GALId"));
			if (amt != null) {
				pbItem.setOrderAmount((long)(Double.valueOf(amt) * 100));
			}
			else {
				LOGGER.error("Order amount of order in Postback is null. Order may be older than 1 week. Ignoring it. " + pbItem.getTransactionId());
				throw new EwalletOldOrderException("Order amount of order in Postback is null. Order may be older than 1 week. Ignoring it. " + pbItem.getTransactionId());
			}
		} else {
			String amt = orderAmountMap.get(pbItem.getOrderId());
			if (amt != null) {
				pbItem.setOrderAmount((long)(Double.valueOf(amt) * 100));
			}
			else {
				LOGGER.error("Order amount of order in Postback is null. Order may be older than 1 week. Ignoring it. " + pbItem.getTransactionId());
				throw new EwalletOldOrderException("Order amount of order in Postback is null. Order may be older than 1 week. Ignoring it. " + pbItem.getTransactionId());
			}

		}

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
	
	private void loadCommonData(Connection conn, EnumEwalletType walletType) throws SQLException {
		PreparedStatement otherDataPS = conn.prepareStatement(GET_AMOUNT_DATA_FOR_ORDER);
		ResultSet otherDataRS = otherDataPS.executeQuery();
		while (otherDataRS.next()) {
			orderAmountMap.put(otherDataRS.getString("id"), otherDataRS.getString("amount"));
		}
		
		PreparedStatement otherAUFDataPS = conn.prepareStatement(GET_AMOUNT_DATA_FOR_STF_AUF_ORDER);
		ResultSet otherAUFDataRS = otherAUFDataPS.executeQuery();
		while (otherAUFDataRS.next()) {
			orderAmountMap.put(otherAUFDataRS.getString("id"), otherAUFDataRS.getString("amount"));
		}
		
		PreparedStatement orderPurchPS = conn.prepareStatement(GET_ORDER_PURCHASE_DATE);
		orderPurchPS.setString(1, walletType.getName());
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
		System.out.println(dao.getAllTrxnsForPostback(conn, EnumEwalletType.MP));
		
		//List<EwalletPostBackModel> data = new EwalletTxNotifyDAO().getAllTrxnsForPostback(conn, "MP");
		conn.close();
	}

}

