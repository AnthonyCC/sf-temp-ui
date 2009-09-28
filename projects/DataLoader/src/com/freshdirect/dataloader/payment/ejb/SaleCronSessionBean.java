package com.freshdirect.dataloader.payment.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		+ "and sa1.id = gct.salesaction_id and gct.tran_status='P' and gct.tran_type IN ('PRE', 'REV-PRE') and customer_id = s.customer_id and action_type = 'PAG') " 
		+ "and sa.action_type in ('CRO','MOD') "
		+ "and sa.action_date=s.cromod_date and di.SALESACTION_ID=sa.ID and di.starttime > sysdate - 1 and di.starttime<SYSDATE+2";

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

	/**
	 * This method runs a AUTH_QUERY against the erpcustomer and get all the sales, that need payment authorization
	 * and then authorizes them one by one by calling ErpCustomerManager.
	 *
	 */
	public void authorizeSales(long timeout) {
		Connection con = null;
		List saleIds = new ArrayList();

		UserTransaction utx = null;
		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();
			PreparedStatement ps = con.prepareStatement(QUERY_AUTH_NEEDED);
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
			for (Iterator i = saleIds.iterator(); i.hasNext();) {
				if (System.currentTimeMillis() - startTime > timeout) {
					LOGGER.warn("Authorization process was running longer than" + timeout / 60 / 1000);
					break;
				}
				utx = null;
				try {
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();

					String saleId = (String) i.next();
					LOGGER.info("Going to authorize: " + saleId);
					sb.authorizeSale(saleId);
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
		Connection con = null;
		List saleIds = new ArrayList();

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
			for (Iterator i = saleIds.iterator(); i.hasNext();) {
				if (System.currentTimeMillis() - startTime > timeout) {
					LOGGER.warn("Authorization process was running longer than" + timeout / 60 / 1000);
					break;
				}
				utx = null;
				try {
					utx = this.getSessionContext().getUserTransaction();
					utx.begin();

					String saleId = (String) i.next();
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

	
	public void authorizeSubscriptions(long timeout) {
		Connection con = null;
		List saleIds = new ArrayList();
		List customerIds=new ArrayList();

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

					String saleId = (String)saleIds.get(i);
					String customerId=(String)customerIds.get(i);
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

	private List querySalesInStatusCPG(Connection conn, EnumSaleStatus status) throws SQLException {
		List saleIds = new ArrayList();
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


	private Map querySalesInStatusRPG(Connection conn) throws SQLException {
		//saleId -> FDIdentity
		Map sales = new HashMap();
		PreparedStatement ps = conn.prepareStatement(QUERY_SALE_IN_RPG_STATUS);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			sales.put(rs.getString("SALE_ID"), new Double(rs.getDouble("SUB_TOTAL")));
		}
		return sales;
	}
	
	private Map querySaleAndIdentityByStatus(Connection conn, EnumSaleStatus status) throws SQLException {
		//saleId -> FDIdentity
		Map sales = new HashMap();
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

		Map saleIds;
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
		for (Iterator i = saleIds.keySet().iterator(); i.hasNext();) {
			utx = null;
			try {
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();
				String saleId = (String) i.next();
				FDIdentity identity = (FDIdentity) saleIds.get(saleId);
				FDActionInfo info =
					new FDActionInfo(EnumTransactionSource.SYSTEM, identity, "SYSTEM", "Could not get AUTHORIZATION", null);

				LOGGER.debug("cancel sale-start: " + saleId);


				sb.cancelOrder(info, saleId, false);
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

		List saleIds;

		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();

			saleIds = this.querySalesInStatusCPG(con, EnumSaleStatus.CAPTURE_PENDING);

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
					command = new Capture((String) saleIds.get(i));
					sb.updateSaleDlvStatus(command);
					LOGGER.info("*******sending message to capture Queue for order:"+saleIds.get(i));
				}else{
					psb.captureAuthorization((String) saleIds.get(i));
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

		Connection con = null;
		UserTransaction utx = null;

		List saleIds;

		try {
			utx = this.getSessionContext().getUserTransaction();
			utx.begin();
			con = this.getConnection();

			saleIds = this.querySalesInStatusCPG(con, EnumSaleStatus.POST_AUTH_PENDING);

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
				gsb.postAuthorizeSales((String) saleIds.get(i));
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
		Connection con = null;
		UserTransaction utx = null;

		Map saleIds;

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
		for (Iterator i = saleIds.keySet().iterator(); i.hasNext();) {

			if (System.currentTimeMillis() - startTime > timeout) {
				LOGGER.warn("Gift Card Register transaction was running longer than" + timeout / 60 / 1000);
				break;
			}

			utx = null;
			try {
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();
				String saleId = (String) i.next();
				Double subTotal = (Double) saleIds.get(saleId);
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
				"java:comp/env/ejb/FDCustomerManager",
				FDCustomerManagerHome.class);
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
			PaymentHome home = (PaymentHome)LOCATOR.getRemoteHome("java:comp/env/ejb/Payment", PaymentHome.class);
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
			PaymentGatewayHome home = (PaymentGatewayHome)LOCATOR.getRemoteHome(DlvProperties.getPaymentGatewayHome(), PaymentGatewayHome.class);
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
			GCGatewayHome home = (GCGatewayHome)LOCATOR.getRemoteHome("freshdirect.giftcard.Gateway", GCGatewayHome.class);
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
				"java:comp/env/ejb/GiftCardManager",
				GiftCardManagerHome.class);
			return home.create();
		} catch (NamingException e) {
			throw new EJBException(e);
		}catch (CreateException e){
			throw new EJBException(e);
		}catch(RemoteException e){
			throw new EJBException(e);
		}
	}
}