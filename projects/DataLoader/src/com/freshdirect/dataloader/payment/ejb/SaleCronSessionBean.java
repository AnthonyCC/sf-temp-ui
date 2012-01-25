package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ejb.GCGatewayHome;
import com.freshdirect.giftcard.ejb.GCGatewaySB;
import com.freshdirect.giftcard.ejb.GiftCardManagerHome;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;
import com.freshdirect.payment.command.Capture;
import com.freshdirect.payment.command.PaymentCommandI;
import com.freshdirect.payment.ejb.PaymentGatewayHome;
import com.freshdirect.payment.ejb.PaymentGatewaySB;
import com.freshdirect.payment.ejb.PaymentHome;
import com.freshdirect.payment.ejb.PaymentSB;

public class SaleCronSessionBean extends SessionBeanSupport {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static Category LOGGER = LoggerFactory.getInstance(SaleCronSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

	private final static String QUERY_AUTH_NEEDED =
		"select s.id from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where s.status in ('SUB', 'PNA')and s.type='REG' and sa.sale_id=s.id and sa.action_type in ('CRO','MOD') "
			+ "and sa.action_date=(select max(action_date) "
			+ "from cust.salesaction za where za.action_type in ('CRO','MOD') and za.sale_id=s.id) and di.SALESACTION_ID=sa.ID and di.starttime > sysdate - 1 and di.starttime<SYSDATE+2";


	private final static String QUERY_PRE_AUTH_NEEDED =
		"select s.id from cust.sale s, cust.salesaction sa, cust.deliveryinfo di " 
		+ "where s.status in ('AUT','SUB','AVE')and s.type='REG' and sa.customer_id = s.customer_id and " 
		+ "sa.sale_id=s.id and sa.sale_id = (select distinct sale_id from cust.salesaction sa1, cust.gift_card_trans gct where sa1.sale_id = s.id " 
		+ "and sa1.id = gct.salesaction_id and gct.tran_status='P' and gct.tran_type IN ('PRE', 'REV-PRE') and customer_id = s.customer_id and action_type IN('PAG','RAG')) " 
		+ "and sa.action_type in ('CRO','MOD') "
		+ "and sa.action_date=s.cromod_date and di.SALESACTION_ID=sa.ID and di.starttime > sysdate - 1 and di.starttime<SYSDATE+2";

	private final static String QUERY_REVERSE_AUTH_NEEDED =
		"select s.id from cust.sale s, cust.salesaction sa "  
		+ "where s.status in ('CAN')and s.type='REG' and sa.customer_id = s.customer_id and "  
		+ "sa.sale_id=s.id and sa.sale_id = (select distinct sale_id from cust.salesaction sa1, cust.gift_card_trans gct where "
		+ "sa1.sale_id = s.id  and sa1.id = gct.salesaction_id and gct.tran_status='P' and gct.tran_type = 'REV-PRE' and customer_id = s.customer_id and action_type = 'RAG') "  
		+ "and sa.action_type in ('CAO') and sa.ACTION_DATE > (sysdate - 8) "; //Just to be safe we are looking for past 8 days of cancelled orders.
	
	private final static String QUERY_AUTH_NEEDED_SUBSCRIPTIONS=
		"SELECT S.ID,S.CUSTOMER_ID FROM CUST.SALE S,CUST.SALESACTION SA WHERE S.CROMOD_DATE BETWEEN (SYSDATE-5) AND (SYSDATE-1/48) AND "+
	    "S.ID=SA.SALE_ID AND "+
	    "S.CUSTOMER_ID=SA.CUSTOMER_ID AND "+
	    "S.CROMOD_DATE=SA.ACTION_DATE AND "+
	    "SA.ACTION_TYPE IN ('CRO','MOD') AND "+
	    "S.STATUS='NEW' AND S.TYPE='SUB' AND "+
	    " NOT EXISTS (SELECT 1 FROM CUST.SALESACTION SA WHERE SALE_ID=S.ID AND ACTION_TYPE='AUT')";



	private final static String QUERY_SALE_IN_CPG_STATUS =
		"select distinct s.id from cust.sale s, cust.salesaction sa where s.status = ? "
			+ "and sa.sale_id=s.id and (sa.action_type='RET' or(sa.action_type='DLC' "
			+ "and (sysdate-(select max(action_date) from cust.salesaction za where za.action_type='DLC' and za.sale_id = s.id))*24 >=?) " 
			+ "or(sa.action_type='GCD' "
			+ "and (sysdate-(select max(action_date) from cust.salesaction za where za.action_type='GCD' and za.sale_id = s.id))*24 >=?))";

	private final static String QUERY_SALE_IN_CPG_STATUS_NO_BIND =
		"select distinct s.id from cust.sale s, cust.salesaction sa where s.status = 'CPG' "
			+ "and sa.sale_id=s.id and (sa.action_type='RET' or(sa.action_type='DLC' "
			+ "and (sysdate-(select max(action_date) from cust.salesaction za where za.action_type='DLC' and za.sale_id = s.id))*24 >=4) " 
			+ "or(sa.action_type='GCD' "
			+ "and (sysdate-(select max(action_date) from cust.salesaction za where za.action_type='GCD' and za.sale_id = s.id))*24 >=4))";
	
	private final static String QUERY_SALE_IN_POG_STATUS_NO_BIND =
		"select distinct s.id from cust.sale s, cust.salesaction sa where s.status = 'POG' "
			+ "and sa.sale_id=s.id and (sa.action_type='RET' or(sa.action_type='DLC' "
			+ "and (sysdate-(select max(action_date) from cust.salesaction za where za.action_type='DLC' and za.sale_id = s.id))*24 >=4) " 
			+ "or(sa.action_type='GCD' "
			+ "and (sysdate-(select max(action_date) from cust.salesaction za where za.action_type='GCD' and za.sale_id = s.id))*24 >=4))";
	
	private final static String QUERY_SALE_IN_RPG_STATUS =
			"select s.id, sa.sub_total from cust.sale s, cust.salesaction sa where sa.customer_id = s.customer_id and" +
			" sa.sale_id = s.id and s.type = 'GCD' and s.status = 'RPG' and sa.action_type = 'CRO'";

	private final static String QUERY_SALE_AND_IDENTITY_BY_STATUS =
		"select s.id as sale_id, s.customer_id as erpcustomer_id, fc.id as fdcustomer_id "
			+ "from cust.sale s, cust.fdcustomer fc "
			+ "where s.customer_id = fc.erp_customer_id and s.status = ? ";

/*	Old query before GC
 	private final static String LOCK_AUF_SALES =
		"update cust.sale set status = ? where status = ? "
			+ "and exists (select s.id from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where sale.id = s.id and s.type='REG' and sa.sale_id=s.id and sa.action_type in ('CRO','MOD')  "
			+ "and sa.action_date=(select max(action_date) from cust.salesaction za where za.action_type in ('CRO','MOD') and za.sale_id=s.id) "
			+ "and di.salesaction_id=sa.id and di.starttime > sysdate - 1 and (di.cutofftime - sysdate) * 24 <= ?)";
			
*/
	//New Query modified for GC. This query puts sale status to LOC for sale with either status = AUF
	//or has one or more GC Pre AUTH failed.
	
	private final static String LOCK_AUF_SALES =
		"update cust.sale set status = ? where (status = ? or exists ( "+
		"	   select s.id from cust.sale s, cust.salesaction sa, cust.gift_card_trans gt "+
		"	   where  "+
		"	   sale.id = s.id and s.type = 'REG' and s.status in ('AUT','SUB') and sa.sale_id=s.id and gt.salesaction_id=sa.id  "+
		"	   and sa.action_type='PAG'and gt.certificate_num in (select distinct ag.certificate_num from cust.salesaction sa1, cust.applied_gift_card ag "+
		"	   where  sa1.sale_id=s.id and ag.salesaction_id=sa1.id "+
		"	   and sa1.action_type in ('CRO','MOD') and sa1.action_date = s.cromod_date) "+
		"	   and sa.action_date = (select max(action_date) from cust.salesaction za, cust.gift_card_trans zgt where   "+
		"	   za.sale_id=s.id and za.ID = zgt.salesaction_id and za.action_type in ('PAG') and zgt.certificate_num = gt.certificate_num) "+
		"	   and gt.tran_status = 'F')) "+
		"and exists (select s.id from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "+
		"where sale.id = s.id and s.type='REG' and sa.sale_id=s.id and sa.action_type in ('CRO','MOD')   "+
		"and sa.action_date=(select max(action_date) from cust.salesaction za where za.action_type in ('CRO','MOD') and za.sale_id=s.id)  "+
		"and di.salesaction_id=sa.id and di.starttime > sysdate - 1 and (di.cutofftime - sysdate) * 24 <= ?) ";

	/**
	 * update sales past cutoff
	 * SUB -> PNA
	 * AUT, AVE -> PRC
	 */
	private final static String UPDATE_TO_PROCESS_STATUS =
		"update cust.sale set status = decode(status, 'SUB', 'PNA', 'PRC') where status in ('AUT', 'AVE', 'SUB') "
			+ "and exists (select s.id from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ "where sale.id = s.id and sale.type='REG' and sa.sale_id=s.id and sa.action_type in ('CRO','MOD') "
			+ "and sa.action_date=(select max(action_date) from cust.salesaction za where za.action_type in ('CRO','MOD') and za.sale_id=s.id) "
			+ "and di.salesaction_id=sa.id and di.starttime > sysdate - 1 and di.cutofftime < sysdate)";
	private final static String AUTH_NEEDED_CORP_ORDERS_FOR_MONDAY_TUESDAY =
	"SELECT s.id FROM cust.sale s, cust.salesaction sa, cust.deliveryinfo di "+ 
    "WHERE s.status='SUB' AND s.type='REG' AND sa.sale_id=s.id AND sa.action_type in ('CRO','MOD') "+ 
	"AND s.cromod_date=sa.action_date AND di.SALESACTION_ID=sa.ID AND di.starttime > sysdate - 1 AND DI.DELIVERY_TYPE='C' "+
	"AND  DI.STARTTIME<sysdate+6 AND ( rtrim(to_char(sa.requested_date,'DAY'))='MONDAY' OR rtrim(to_char(sa.requested_date,'DAY'))='TUESDAY') ";
	//"AND rtrim(to_char(sysdate,'DAY'))='FRIDAY' ";
	
	
	private List<String> getSaleIds(Connection con, String query) throws SQLException {
		
		List<String> saleIds=new ArrayList<String>(10);
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			saleIds.add(rs.getString(1));
		}

		rs.close();
		ps.close();	
		return saleIds;
	}
	/**
	 * This method runs a AUTH_QUERY against the erpcustomer and get all the sales, that need payment authorization
	 * and then authorizes them one by one by calling ErpCustomerManager.
	 *
	 */
	public void authorizeSales(long timeout) {
		Connection con = null;
		Set<String> saleIds = new HashSet<String>();
		Set<String> corpSaleIds = new HashSet<String>();
		
		UserTransaction utx = null;
		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();
			saleIds.addAll(getSaleIds(con,QUERY_AUTH_NEEDED));
			Calendar now=Calendar.getInstance();
			DAY_OF_WEEK dayOfWeek=getDayOfWeek(/*FDStoreProperties.getDayOfWeekForCOSMondayAuths()*/4);
			
			
			if (dayOfWeek.getCode()== now.get(Calendar.DAY_OF_WEEK))  {
				StringBuilder query=new StringBuilder(AUTH_NEEDED_CORP_ORDERS_FOR_MONDAY_TUESDAY);
				query.append(" AND rtrim(to_char(sysdate,'DAY'))='").append(dayOfWeek.toString())
				.append("' ");
				List<String> tmp=getSaleIds(con,query.toString());
				
				for(String sale:tmp) {
					
					if(!saleIds.contains(sale)) {
						corpSaleIds.add(sale);
					}
				}
			}
			utx.commit();

		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		long startTime = System.currentTimeMillis();
		authorizeSale(saleIds,startTime, timeout,false);
		authorizeSale(corpSaleIds,startTime, timeout,true);
		
	}

	private void authorizeSale(Set<String> saleIds, long startTime, long timeout, boolean force) {
		UserTransaction utx = null;
		FDCustomerManagerSB sb = this.getFDCustomerManagerSB();
		if (saleIds.size() > 0) {
			
			for (Iterator<String> i = saleIds.iterator(); i.hasNext();) {
				if (System.currentTimeMillis() - startTime > timeout) {
					LOGGER.warn("Authorization process was running longer than" + timeout / 60 / 1000);
					break;
				}
				utx = null;
				try {
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();

					String saleId = i.next();
					LOGGER.info("Going to authorize: " + saleId);
					if(force) {
						sb.authorizeSale(saleId, force);
					} else {
						sb.authorizeSale(saleId);
					}
					utx.commit();

				} catch (Exception e) {
					LOGGER.warn("Exception occured during authorization", e);
					if (utx != null)
						try {
							utx.rollback();
						} catch (SystemException se) {
							LOGGER.warn("Error while trying to rollback transaction", se);
						}
					// just keep going :)
				}
			}
		}
	}
	public void preAuthorizeSales(long timeout) {
		if(FDStoreProperties.isGivexBlackHoleEnabled()){
			//Pre auth is not performed at this point.
			return;
		}
			
		Connection con = null;
		List<String> saleIds = new ArrayList<String>();

		UserTransaction utx = null;
		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_PRE_AUTH_NEEDED);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				saleIds.add(rs.getString(1));
			}

			rs.close();
			ps.close();

			utx.commit();

		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		FDCustomerManagerSB sb = this.getFDCustomerManagerSB();
		if (saleIds.size() > 0) {
			long startTime = System.currentTimeMillis();
			for (Iterator<String> i = saleIds.iterator(); i.hasNext();) {
				if (System.currentTimeMillis() - startTime > timeout) {
					LOGGER.warn("Authorization process was running longer than" + timeout / 60 / 1000);
					break;
				}
				utx = null;
				try {
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();

					String saleId = i.next();
					LOGGER.info("Going to authorize: " + saleId);
					sb.preAuthorizeSales(saleId);
					
					utx.commit();

				} catch (Exception e) {
					LOGGER.warn("Exception occured during authorization", e);
					if (utx != null)
						try {
							utx.rollback();
						} catch (SystemException se) {
							LOGGER.warn("Error while trying to rollback transaction", se);
						}
					// just keep going :)
				}
			}
		}
	}
	/*
	 * clearing pending reverse auths due to cancelled orders.  
	 */
	public void reverseAuthorizeSales(long timeout) {
		if(FDStoreProperties.isGivexBlackHoleEnabled()){
			//Reverse Pre auth is not performed at this point.
			return;
		}
		
		Connection con = null;
		List<String> saleIds = new ArrayList<String>();

		UserTransaction utx = null;
		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_REVERSE_AUTH_NEEDED);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				saleIds.add(rs.getString(1));
			}

			rs.close();
			ps.close();

			utx.commit();

		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		GiftCardManagerSB sb = this.getGiftCardManagerSB();
		if (saleIds.size() > 0) {
			long startTime = System.currentTimeMillis();
			for (Iterator<String> i = saleIds.iterator(); i.hasNext();) {
				if (System.currentTimeMillis() - startTime > timeout) {
					LOGGER.warn("Reverse Authorization process was running longer than" + timeout / 60 / 1000);
					break;
				}
				utx = null;
				try {
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();

					String saleId = i.next();
					LOGGER.info("Going to reverse authorize: " + saleId);
					sb.reversePreAuthForCancelOrders(saleId);
					utx.commit();

				} catch (Exception e) {
					LOGGER.warn("Exception occured during reverse authorization", e);
					if (utx != null)
						try {
							utx.rollback();
						} catch (SystemException se) {
							LOGGER.warn("Error while trying to rollback transaction", se);
						}
					// just keep going :)
				}
			}
		}
	}
	
	public void authorizeSubscriptions(long timeout) {
		Connection con = null;
		List<String> saleIds = new ArrayList<String>();
		List<String> customerIds=new ArrayList<String>();

		UserTransaction utx = null;
		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_AUTH_NEEDED_SUBSCRIPTIONS);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				saleIds.add(rs.getString(1));
				customerIds.add(rs.getString(2));
			}

			rs.close();
			ps.close();

			utx.commit();

		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}

		FDCustomerManagerSB sb = this.getFDCustomerManagerSB();
		if (saleIds.size() > 0) {
			long startTime = System.currentTimeMillis();
			for (int i=0;i<saleIds.size();i++) {
				if (System.currentTimeMillis() - startTime > timeout) {
					LOGGER.warn("Authorization process was running longer than" + timeout / 60 / 1000);
					break;
				}
				utx = null;
				try {
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();

					String saleId = saleIds.get(i);
					String customerId=customerIds.get(i);
					FDIdentity identity=getFDIdentity(customerId);
					FDUser user=FDCustomerManager.getFDUser(identity);
					CustomerRatingAdaptor cra=new CustomerRatingAdaptor(user.getFDCustomer().getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
					LOGGER.info("Going to authorize subscription: " + saleId);
					sb.authorizeSale(customerId,saleId,EnumSaleType.SUBSCRIPTION,null);
					utx.commit();

				} catch (Exception e) {
					LOGGER.warn("Exception occured during authorization", e);
					if (utx != null)
						try {
							utx.rollback();
						} catch (SystemException se) {
							LOGGER.warn("Error while trying to rollback transaction", se);
						}
					// just keep going :)
				}
			}
		}
	}


	private void lockAuthFaileSales(Connection conn, EnumSaleStatus from, EnumSaleStatus to) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOCK_AUF_SALES);
		ps.setString(1, to.getStatusCode());
		ps.setString(2, from.getStatusCode());
		ps.setInt(3, ErpServicesProperties.getCancelOrdersB4Cutoff());
		ps.executeUpdate();
		ps.close();
	}

	private int updateToProcessStatus(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(UPDATE_TO_PROCESS_STATUS);
		int affected = ps.executeUpdate();
		ps.close();
		return affected;
	}

	private List<String> querySalesInStatusCPG(Connection conn, EnumSaleStatus status) throws SQLException {
		List<String> saleIds = new ArrayList<String>();
		PreparedStatement ps = conn.prepareStatement(QUERY_SALE_IN_CPG_STATUS);
		ps.setString(1, status.getStatusCode());
		ps.setInt(2, ErpServicesProperties.getWaitingTimeAfterConfirm());
		ps.setInt(3, ErpServicesProperties.getWaitingTimeAfterConfirm());
		//LOGGER.info("waiting time after confirm:"+ErpServicesProperties.getWaitingTimeAfterConfirm());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			saleIds.add(rs.getString(1));
		}
		rs.close();
		ps.close();
		return saleIds;
	}
	private List<String> querySalesInStatusCPG(Connection conn, String query) throws SQLException {
		List<String> saleIds = new ArrayList<String>();
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			saleIds.add(rs.getString(1));
		}
		rs.close();
		ps.close();
		return saleIds;
	}


	private Map<String, Double> querySalesInStatusRPG(Connection conn) throws SQLException {
		//saleId -> FDIdentity
		Map<String, Double> sales = new HashMap<String, Double>();
		PreparedStatement ps = conn.prepareStatement(QUERY_SALE_IN_RPG_STATUS);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			sales.put(rs.getString("ID"), new Double(rs.getDouble("SUB_TOTAL")));
		}
		return sales;
	}
	
	private Map<String, FDIdentity> querySaleAndIdentityByStatus(Connection conn, EnumSaleStatus status) throws SQLException {
		//saleId -> FDIdentity
		Map<String, FDIdentity> sales = new HashMap<String, FDIdentity>();
		PreparedStatement ps = conn.prepareStatement(QUERY_SALE_AND_IDENTITY_BY_STATUS);
		ps.setString(1, status.getStatusCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			sales.put(rs.getString("SALE_ID"), new FDIdentity(rs.getString("ERPCUSTOMER_ID"), rs.getString("FDCUSTOMER_ID")));
		}
		return sales;
	}

	/**
	 * It also make sure that if the system. was unable to obtain an authorization for this sale,
	 * then it cancels the order both in ERPS and SAP. it also locks the sale from further modifications until
	 * the process is funished by changing the state of the order to LOCKED.
	 */
	public void cancelAuthorizationFailed() {

		Connection con = null;
		UserTransaction utx = null;

		Map<String, FDIdentity> saleIds;
		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();

			this.lockAuthFaileSales(con, EnumSaleStatus.AUTHORIZATION_FAILED, EnumSaleStatus.LOCKED);

			// get LOCKED sale IDs
			saleIds = this.querySaleAndIdentityByStatus(con, EnumSaleStatus.LOCKED);
			utx.commit();
		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}


		// cancel all LOCKED sales
		FDCustomerManagerSB sb = this.getFDCustomerManagerSB();
		for (Iterator<String> i = saleIds.keySet().iterator(); i.hasNext();) {
			utx = null;
			try {
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();
				String saleId = i.next();
				FDIdentity identity = saleIds.get(saleId);
				FDActionInfo info =
					new FDActionInfo(EnumTransactionSource.SYSTEM, identity, "SYSTEM", "Could not get AUTHORIZATION", null);

				LOGGER.debug("cancel sale-start: " + saleId);
				sb.cancelOrder(info, saleId, false, 0);
				LOGGER.debug("cancel sale-complete: " + saleId);
				utx.commit();
			} catch (Exception e) {
				LOGGER.warn("Exception occured during Caneling Sale", e);
				if (utx != null)
					try {
						utx.rollback();
					} catch (SystemException se) {
						LOGGER.warn("Error while trying to rollback transaction", se);
					}
				// just keep going :)
			}
		}
	}

	/**
	 * This method updates status for sales whose cutoff time has elapsed.
	 */
	public int cutoffSales() {
		Connection con = null;
		UserTransaction utx = null;
		int affected = 0;

		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();

			affected = this.updateToProcessStatus(con);

			utx.commit();
		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}


		return affected;
	}

	public void captureSales(long timeout) throws EJBException {

		Connection con = null;
		UserTransaction utx = null;

		List<String> saleIds;

		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();

			//saleIds = this.querySalesInStatusCPG(con, EnumSaleStatus.CAPTURE_PENDING);
			saleIds = this.querySalesInStatusCPG(con, QUERY_SALE_IN_CPG_STATUS_NO_BIND);

			utx.commit();
		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}
		long startTime = System.currentTimeMillis();

		PaymentGatewaySB sb = null;
		PaymentSB psb = null;
		PaymentCommandI command = null;
		boolean useQueue = ErpServicesProperties.isUseQueue();
		if(useQueue){
			sb = this.getPaymentGatewaySB();
		}else{
			psb = this.getPaymentSB();
		}
		//LOGGER.info("********** use queue:"+ErpServicesProperties.isUseQueue());
		for (int i = 0, size = saleIds.size(); i < size; i++) {

			if (System.currentTimeMillis() - startTime > timeout) {
				LOGGER.warn("Capture Authorization process was running longer than" + timeout / 60 / 1000);
				break;
			}

			utx = null;
			try {
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();
				if(useQueue){
					command = new Capture(saleIds.get(i));
					sb.updateSaleDlvStatus(command);
					LOGGER.info("*******sending message to capture Queue for order:"+saleIds.get(i));
				}else{
					psb.captureAuthorization(saleIds.get(i));
					LOGGER.info("*******do capture transaction for order:"+saleIds.get(i));
				}

				utx.commit();
			} catch (Exception e) {
				LOGGER.warn("Exception occured during capture", e);
				if (utx != null) {
					try {
						utx.rollback();
					} catch (SystemException se) {
						LOGGER.warn("Error while trying to rollback transaction", se);
					}
					// just keep going :)
				}
			}
		}

	}

	public void postAuthSales(long timeout) {
		if(FDStoreProperties.isGivexBlackHoleEnabled()){
			//Post auth is not performed at this point.
			return;
		}

		Connection con = null;
		UserTransaction utx = null;

		List<String> saleIds;

		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();

			//saleIds = this.querySalesInStatusCPG(con, EnumSaleStatus.POST_AUTH_PENDING);
			saleIds = this.querySalesInStatusCPG(con, QUERY_SALE_IN_POG_STATUS_NO_BIND);

			utx.commit();
		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}
		long startTime = System.currentTimeMillis();

		GiftCardManagerSB gsb = this.getGiftCardManagerSB();
 
		//LOGGER.info("********** use queue:"+ErpServicesProperties.isUseQueue());
		for (int i = 0, size = saleIds.size(); i < size; i++) {

			if (System.currentTimeMillis() - startTime > timeout) {
				LOGGER.warn("Capture Authorization process was running longer than" + timeout / 60 / 1000);
				break;
			}

			utx = null;
			try {
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();
				gsb.postAuthorizeSales(saleIds.get(i));
				LOGGER.info("*******do post authorize transaction for order:"+saleIds.get(i));

				utx.commit();
			} catch (Exception e) {
				LOGGER.warn("Exception occured during Post Auth", e);
				if (utx != null) {
					try {
						utx.rollback();
					} catch (SystemException se) {
						LOGGER.warn("Error while trying to rollback transaction", se);
					}
					// just keep going :)
				}
			}
		}

	}

	public void registerGiftCards(long timeout) {
		if(FDStoreProperties.isGivexBlackHoleEnabled()){
			//Pending register transactions are not sent to Givex at this point.
			return;
		}

		Connection con = null;
		UserTransaction utx = null;

		Map<String, Double> saleIds;

		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();

			saleIds = this.querySalesInStatusRPG(con);

			utx.commit();
		} catch (Exception e) {
			LOGGER.warn(e);
			try {
				utx.rollback();
			} catch (SystemException se) {
				LOGGER.warn("Error while trying to rollback transaction", se);
			}
			throw new EJBException(e);
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while trying to cleanup", se);
			}
		}
		long startTime = System.currentTimeMillis();

		GCGatewaySB gsb = null;
		GiftCardManagerSB gmb = null;
		PaymentCommandI command = null;
		boolean useQueue = ErpServicesProperties.isUseRegisterQueue();
		if(useQueue){
			gsb = this.getGCGatewaySB();
		}else{
			gmb = this.getGiftCardManagerSB();
		}
		//LOGGER.info("********** use queue:"+ErpServicesProperties.isUseQueue());
		for (Iterator<String> i = saleIds.keySet().iterator(); i.hasNext();) {

			if (System.currentTimeMillis() - startTime > timeout) {
				LOGGER.warn("Gift Card Register transaction was running longer than" + timeout / 60 / 1000);
				break;
			}

			utx = null;
			try {
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();
				String saleId = i.next();
				Double subTotal = saleIds.get(saleId);
				if(useQueue){
					gsb.sendRegisterGiftCard(saleId, subTotal.doubleValue());
					LOGGER.info("*******sending message to register Queue for order:"+saleIds.get(i));
				}else{
					gmb.registerGiftCard(saleId, subTotal.doubleValue());
					LOGGER.info("*******do register transaction for order:"+saleIds.get(i));
				}

				utx.commit();
			} catch (Exception e) {
				LOGGER.warn("Exception occured during register", e);
				if (utx != null) {
					try {
						utx.rollback();
					} catch (SystemException se) {
						LOGGER.warn("Error while trying to rollback transaction", se);
					}
					// just keep going :)
				}
			}
		}
		
	}
	
	private static FDIdentity getFDIdentity(String erpCustomerID) {
		return new FDIdentity(erpCustomerID,null);
	}


	private FDCustomerManagerSB getFDCustomerManagerSB() {
		try {
			FDCustomerManagerHome home = (FDCustomerManagerHome) LOCATOR.getRemoteHome(
				"java:comp/env/ejb/FDCustomerManager");
			return home.create();
		} catch (NamingException e) {
			throw new EJBException(e);
		}catch (CreateException e){
			throw new EJBException(e);
		}catch(RemoteException e){
			throw new EJBException(e);
		}
	}

	private PaymentSB getPaymentSB() {
		try {
			PaymentHome home = (PaymentHome)LOCATOR.getRemoteHome("java:comp/env/ejb/Payment");
			return home.create();
		} catch (NamingException e) {
			throw new EJBException(e);
		}catch (CreateException e){
			throw new EJBException(e);
		}catch(RemoteException e){
			throw new EJBException(e);
		}
	}

	private PaymentGatewaySB getPaymentGatewaySB() {
		try {
			PaymentGatewayHome home = (PaymentGatewayHome)LOCATOR.getRemoteHome(DlvProperties.getPaymentGatewayHome());
			return home.create();
		} catch (NamingException e) {
			throw new EJBException(e);
		}catch (CreateException e){
			throw new EJBException(e);
		}catch(RemoteException e){
			throw new EJBException(e);
		}
	}
	
	private GCGatewaySB getGCGatewaySB() {
		try {
			GCGatewayHome home = (GCGatewayHome)LOCATOR.getRemoteHome("freshdirect.giftcard.Gateway");
			return home.create();
		} catch (NamingException e) {
			throw new EJBException(e);
		}catch (CreateException e){
			throw new EJBException(e);
		}catch(RemoteException e){
			throw new EJBException(e);
		}
	}
	
	private GiftCardManagerSB getGiftCardManagerSB() {
		try {
			GiftCardManagerHome home = (GiftCardManagerHome) LOCATOR.getRemoteHome(
				"java:comp/env/ejb/GiftCardManager");
			return home.create();
		} catch (NamingException e) {
			throw new EJBException(e);
		}catch (CreateException e){
			throw new EJBException(e);
		}catch(RemoteException e){
			throw new EJBException(e);
		}
	}
	private  enum DAY_OF_WEEK {
		   SUNDAY(1), MONDAY(2),TUESDAY(3), WEDNESDAY(4), THURSDAY (5), FRIDAY(6),
		   SATURDAY(7);
		   private int code;

		   private DAY_OF_WEEK(int c) {
		     code = c;
		   }

		   public int getCode() {
		     return code;
		   }
		 }
	
	private  DAY_OF_WEEK getDayOfWeek(int code) {
		DAY_OF_WEEK selected=DAY_OF_WEEK.FRIDAY;
		for(DAY_OF_WEEK c : DAY_OF_WEEK.values()) {
			 if(c.getCode()==code) {
				 selected=c;
				 break;
			 }
		}
		return selected;
	}
}