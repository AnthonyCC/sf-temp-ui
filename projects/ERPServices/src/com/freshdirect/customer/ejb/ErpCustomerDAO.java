/**
 * 
 */
package com.freshdirect.customer.ejb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author ksriram
 *
 */
public class ErpCustomerDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7975703282135574908L;
	
	private final static Category LOGGER = LoggerFactory.getInstance(ErpCustomerDAO.class);
	
	public static boolean isCustomerActive(Connection conn, String erpCustomerId) throws SQLException {
		boolean isCustomerActive = false;
		PreparedStatement ps = null; 
        ResultSet rs = null;
	        
	    try {
	    	if(null != erpCustomerId){
				ps = conn.prepareStatement("SELECT ACTIVE FROM CUST.CUSTOMER WHERE ID = ? ");
				ps.setString(1, erpCustomerId);
				rs = ps.executeQuery();
				if (rs.next()) {
					String active = rs.getString("ACTIVE");
					isCustomerActive = "1".equals(active);//1 is active, 0 is inactive.
				}
	    	}
		} finally {
			DaoUtil.close(rs, ps);
		}
		return isCustomerActive;
	}
}
