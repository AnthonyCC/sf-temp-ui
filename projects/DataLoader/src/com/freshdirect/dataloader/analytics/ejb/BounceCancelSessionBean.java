package com.freshdirect.dataloader.analytics.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Category;
import com.freshdirect.analytics.BounceDAO;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BounceCancelSessionBean extends SessionBeanSupport{
	
	private static final Category LOGGER = LoggerFactory.getInstance(BounceCancelSessionBean.class);

	public void cancelBounce() throws RemoteException
	{
		Connection conn = null;
		try 
		{
			conn  = this.getConnection();
			BounceDAO.cancelBounce(conn);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}	    
	  
}