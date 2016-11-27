package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJBException;

import org.apache.log4j.Category;


import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpProductFamilySessionBean extends SessionBeanSupport{
	
	
	 /**
	 * logger for messages
	 */
	private static Category LOGGER = LoggerFactory
			.getInstance(ErpProductFamilySessionBean.class);

	public String getFamilyIdForMaterial(String matId) throws RemoteException {
	     
		Connection conn = null;
		String familyID=null;
		try{
			conn = getConnection();			
			familyID=ErpProductFamilyDAO.getFamilyIdForMaterial(conn,matId);			
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all findGrpInfoMaster " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		return familyID;
				
	}
	
	
	public ErpProductFamilyModel  findFamilyInfo(String familyId) throws RemoteException {
		
		Connection conn = null;
		ErpProductFamilyModel familyInfo = null;
		try{
			conn = getConnection();	
			familyInfo = ErpProductFamilyDAO.getFamilyInfo(conn, familyId);
			}
		catch(SQLException sqle){
			LOGGER.error("Unable to load all findFamilyInfo " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		
		return familyInfo;			
	}
	public ErpProductFamilyModel  findSkyFamilyInfo(String materailId) throws RemoteException {
		
		Connection conn = null;
		ErpProductFamilyModel familyInfo = null;
		try{
			conn = getConnection();	
			familyInfo = ErpProductFamilyDAO.getSkuFamilyInfo(conn, materailId);
			}
		catch(SQLException sqle){
			LOGGER.error("Unable to load all findFamilyInfo " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		
		return familyInfo;			
	}
	
}
   
