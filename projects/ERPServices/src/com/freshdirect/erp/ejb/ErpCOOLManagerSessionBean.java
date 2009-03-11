package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpCOOLManagerSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ErpInventoryManagerSessionBean.class);
	private ErpCOOLInfoDAO dao = new ErpCOOLInfoDAO();
	
	public void updateCOOLInfo(List erpCOOLInfo) {
		Connection conn = null;
		try {
			conn = getConnection();
			dao.updateCOOLInfo(conn, erpCOOLInfo);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occured", ex);
			throw new EJBException( ex.getMessage() );
		} finally {
			try {
				if (conn!=null) conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException( ex.getMessage() );
			}
		}
	}
	public Map load(Date lastModified) {
		Connection conn = null;
		try {
			conn = getConnection();
			return dao.load(conn, lastModified);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occured", ex);
			throw new EJBException( ex.getMessage() );
		} finally {
			try {
				if (conn!=null) conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException( ex.getMessage() );
			}
		}
	}
}