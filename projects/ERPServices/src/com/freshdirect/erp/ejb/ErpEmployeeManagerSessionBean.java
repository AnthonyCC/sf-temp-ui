package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpEmployeeManagerSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ErpEmployeeManagerSessionBean.class);
	private ErpEmployeeInfoDAO dao = new ErpEmployeeInfoDAO();
	
	public List getEmployees() {
		Connection conn = null;
		try {
			conn = getConnection();
			return dao.getEmployees(conn);
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