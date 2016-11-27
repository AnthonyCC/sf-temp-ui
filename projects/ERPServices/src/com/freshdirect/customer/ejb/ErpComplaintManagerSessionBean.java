/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @version $Revision$
 * @author $Author$
 */
public class ErpComplaintManagerSessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = -4023575003708712037L;

	private static Category LOGGER = LoggerFactory.getInstance( ErpComplaintManagerSessionBean.class );

	private ComplaintDAO dao = new ComplaintDAO();

    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    protected String getResourceCacheKey() {
        return "com.freshdirect.customer.ejb.ErpComplaintHome";
    }

    public Map<String, List<ErpComplaintReason>> getReasons(boolean excludeCartonReq) {
		Connection conn = null;
		try {
			conn = getConnection();
			return dao.getReasons(conn, excludeCartonReq);
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
    
    /* get complaint codes */
    public Map<String,String> getComplaintCodes() {
		Connection conn = null;
		try {
			conn = getConnection();
			return dao.getComplaintCodes(conn);
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

	public Collection<String> getPendingComplaintSaleIds() {
		Connection conn = null;
		try {
			conn = getConnection();
			return dao.getPendingComplaintSaleIds(conn);
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
	public void rejectMakegoodComplaint(String makegood_sale_id) {
		Connection conn = null;
		try {
			conn = getConnection();
			dao.rejectMakegoodComplaint(conn, makegood_sale_id);
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
	
	public ErpComplaintReason getReasonByCompCode(String cCode) {
		Connection conn = null;
		try {
			conn = getConnection();
			return dao.getReasonByCompCode(conn, cCode);
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


	public void ejbCreate() throws CreateException {
		// nothing required
	}

	public void ejbRemove() {
		// nothing required
	}

} // class KanaGatewaySessionBean
