package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.ejb.FDStandingOrderDAO;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.sap.ejb.SapException;

public class StandingOrder3CronSessionBean extends SessionBeanSupport {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static Category LOGGER = LoggerFactory.getInstance(StandingOrder3CronSessionBean.class);
    
	
	private static FDStandingOrdersManager	soManager			= FDStandingOrdersManager.getInstance();
	private static MailerGatewayHome		mailerHome			= null;
	
	public static final String INITIATOR_NAME = "Standing Order Service";
	

	private static void invalidateMailerHome() {
		mailerHome = null;
	}
	
	private static void lookupMailerHome() {
		if (mailerHome != null) {
			return;
		}		
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			mailerHome = (MailerGatewayHome) ctx.lookup( "freshdirect.mail.MailerGateway" );
		} catch ( NamingException ne ) {
			LOGGER.error("NamingException",ne);
		} finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch ( NamingException ne ) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}
	public List<String> queryForDeactivatingTimeslotEligible() throws RemoteException{
		List<String> list =new ArrayList<String>();
		Connection con = null;
		try {
			con = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			list = dao.queryForStandingOrders(con);	
			
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
		return list;
	}
	public void removeSOfromLogistics(List<String> list) throws FinderException, RemoteException,
			ErpTransactionException, SapException {
		try {
			FDDeliveryManager.getInstance().removeOrdersfromLogistics(list);
			sendErrorMail(list);
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			LOGGER.error("CreateException removeSOfromLogistics" , e);
		}
		
	}
	
	
	
	public void removeTimeSlotInfoFromSO(List<String> list) throws SQLException,RemoteException {
		
		Connection con = null;
		
			con = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			dao.removeTimeSlotInfoFromSO(con,list);
		
	}
	
	/**
	 * Sends an error report email to the customer about a expired timeslot SO.
	 * @param msg
	 */
	private boolean sendErrorMail ( List<String> soIds ) {
		lookupMailerHome();
		try {
			for(String id:soIds){
			FDStandingOrder	so=soManager.load(new PrimaryKey(id));
			so.setLastError(FDStandingOrder.ErrorCode.RELEASE_TIMESLOT, FDStandingOrder.ErrorCode.RELEASE_TIMESLOT.getErrorHeader()
					,FDStandingOrder.ErrorCode.RELEASE_TIMESLOT.getErrorDetail(null));
			
			XMLEmailI mail = FDEmailFactory.getInstance().createStandingOrderErrorEmail( so.getUserInfo(), so );		

			MailerGatewaySB mailer;
			mailer = mailerHome.create();
			mailer.enqueueEmail(mail);
			}
			return true;
		} catch ( RemoteException e ) {
			invalidateMailerHome();
			LOGGER.error("RemoteException", e);
		} catch ( CreateException e ) {
			invalidateMailerHome();
			LOGGER.error("CreateException", e);
		} catch (FDResourceException e) {
			invalidateMailerHome();
			LOGGER.error("CreateException", e);
		}
		return false;
	}

}