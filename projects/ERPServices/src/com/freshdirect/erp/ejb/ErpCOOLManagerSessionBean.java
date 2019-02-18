package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *@deprecated Please use the CountryOfOriginController and CountryOfOriginServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
 
public class ErpCOOLManagerSessionBean extends SessionBeanSupport {

	private static final long	serialVersionUID	= -3028263832663124140L;
	private static Category LOGGER = LoggerFactory.getInstance(ErpInventoryManagerSessionBean.class);
	private ErpCOOLInfoDAO dao = new ErpCOOLInfoDAO();
	
	public void updateCOOLInfo(List<ErpCOOLInfo> erpCOOLInfo) {
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
	public Map<ErpCOOLKey, ErpCOOLInfo> load(Date lastModified) {
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