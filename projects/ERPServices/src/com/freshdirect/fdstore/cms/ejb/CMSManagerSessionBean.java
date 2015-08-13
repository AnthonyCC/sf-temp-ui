package com.freshdirect.fdstore.cms.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CMSManagerSessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = -3247737890824031137L;

	private final static Logger LOGGER = LoggerFactory.getInstance(CMSManagerSessionBean.class);

	public void createFeed(String feedId, String storeId, String feedData) throws RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			CMSManagerDAO.publishFeed(conn,feedId, storeId, feedData);
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw new EJBException(sqle.getMessage());
		} finally {
		    close(conn);
		}
	}
	
	public String getLatestFeed(String storeName) throws RemoteException{
		Connection conn = null;
		String response = null;
		try {
			conn = getConnection();
			response = CMSManagerDAO.getFeedContent(conn, storeName);
		} catch (SQLException sqle) {
			LOGGER.error(sqle);
			throw new EJBException(sqle.getMessage());
		} finally {
		    close(conn);
		}
		return response;
	}
}