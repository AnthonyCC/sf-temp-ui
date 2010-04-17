package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ActivityLogSessionBean extends SessionBeanSupport {
	
	private static final long serialVersionUID = 1803752598761911127L;

	private final static Category LOGGER = LoggerFactory.getInstance(ActivityLogSessionBean.class);

	public Collection<ErpActivityRecord> findActivityByTemplate(ErpActivityRecord template) {
		Connection conn = null;
		try {
			conn = getConnection();
			
			return new ActivityDAO().getActivityByTemplate(conn, template);
			
		} catch (SQLException ex) {
			LOGGER.error("SQLException occured", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
	}

	public void logActivity(ErpActivityRecord rec) {
		Connection conn = null;
		try {
			conn = getConnection();
			
			new ActivityDAO().logActivity(conn, rec);
			
		} catch (SQLException ex) {
			LOGGER.error("SQLException occured", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
	}
	
	// Don't delete this method. It looks like it makes no sense, 
	// but some enterprise black-magic is going on in the background. 
	// See ejb-jar.xml for details.
	public void logActivityNewTX(ErpActivityRecord rec) {
		logActivity(rec);
	}

}