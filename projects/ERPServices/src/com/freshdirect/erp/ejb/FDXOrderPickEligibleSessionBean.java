package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.command.SapSendOrderPickEligibleCommand;
import com.freshdirect.sap.ejb.SapException;
/**
 *@deprecated Please use the FDXOrderPickEligibleController and SessionImpressionLogServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public class FDXOrderPickEligibleSessionBean extends SessionBeanSupport {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static Category LOGGER = LoggerFactory.getInstance(FDXOrderPickEligibleSessionBean.class);
    
    private final static ServiceLocator LOCATOR = new ServiceLocator();

	private final static String QUERY_SALE_ELIGIBLE_FOR_PICKING =
			"select s.id, s.sap_number, sa.requested_date from cust.sale s, cust.salesaction sa, cust.deliveryinfo di where s.status in ( 'SUB' , 'AUT', 'AVE') and SA.SALE_ID=s.id and SA.ACTION_DATE=S.CROMOD_DATE " +
			"and SA.ACTION_TYPE in('CRO','MOD') " +
			"and sa.id=di.salesaction_id and (sysdate - sa.ACTION_DATE)*24* 60 > di.mod_start_x and di.delivery_type = 'X' and sa.requested_date = trunc(sysdate)"; 


	public void queryForSalesPickEligible() {
		
		if(SapProperties.isBlackhole()){
			LOGGER.warn("SAP Blackhole enabled.");
			return;
		}
		Connection con = null;
		try {
			con = this.getConnection();
			List<SapOrderPickEligibleInfo> list = this.queryForSales(con, QUERY_SALE_ELIGIBLE_FOR_PICKING);	
			sendOrdersToSAP(list);
		} catch (Exception e) {
			LOGGER.warn(e);
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
	}
	public void sendOrdersToSAP(List<SapOrderPickEligibleInfo> list) throws FinderException, RemoteException,
			ErpTransactionException, SapException {
			
		if(null !=list && !list.isEmpty()){
				LOGGER.info("Sales to be sent to SAP:"+list);
				SapSendOrderPickEligibleCommand sapCommand = new SapSendOrderPickEligibleCommand(list);
				sapCommand.execute();
				LOGGER.info("Sales sent to SAP successfully.");
				
			
		}
	}
	
	
	private List<SapOrderPickEligibleInfo> queryForSales(Connection conn, String query) throws SQLException {
		List<SapOrderPickEligibleInfo> list = new ArrayList<SapOrderPickEligibleInfo>();
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(new SapOrderPickEligibleInfo(new java.util.Date(rs.getDate("requested_date").getTime()), rs.getString("sap_number"), rs.getString("id")));
		}
		LOGGER.info(list);
		rs.close();
		ps.close();
		return list;
	}

}