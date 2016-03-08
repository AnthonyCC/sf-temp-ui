package com.freshdirect.fdstore.standingorders.service;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.standingorders.ejb.FDStandingOrderDAO;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.ejb.SapException;

public class StandingOrder3CronSessionBean extends SessionBeanSupport {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static Category LOGGER = LoggerFactory.getInstance(StandingOrder3CronSessionBean.class);
    
    
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
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void removeTimeSlotInfoFromSO(List<String> list) throws SQLException,RemoteException {
		
		Connection con = null;
		
			con = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			dao.removeTimeSlotInfoFromSO(con,list);
		
	}
	
	

}