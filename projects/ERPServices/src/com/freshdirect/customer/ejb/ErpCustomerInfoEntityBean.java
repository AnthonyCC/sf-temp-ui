package com.freshdirect.customer.ejb;

import java.sql.*;

import com.freshdirect.framework.core.*;

import com.freshdirect.customer.ErpCustomerInfoModel;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

public class ErpCustomerInfoEntityBean extends EntityBeanSupport {

	private static final long serialVersionUID = 2813339097491386216L;
	
	private ErpCustomerInfoPersistentBean customerInfo;
	
	@Override
	public void initialize() {
		customerInfo = null;
	}

	@Override
	public ModelI getModel() {
		return (ErpCustomerInfoModel) (customerInfo == null? null : customerInfo.getModel());
	}

	@Override
	public void setFromModel(ModelI model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PrimaryKey create(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load(Connection conn) throws SQLException {
		customerInfo = new ErpCustomerInfoPersistentBean(this.getPK(), conn);
		
	}

	@Override
	public void store(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Connection conn) throws SQLException {
		// TODO Auto-generated method stub
	}
	public PrimaryKey ejbFindByErpCustomerId(String id) throws FinderException {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			connection = this.getConnection();
			ps = connection.prepareStatement("SELECT CUSTOMER_ID FROM CUST.CUSTOMERINFO WHERE CUSTOMER_ID=?");
			ps.setString(1, id);
			rs = ps.executeQuery();
			if(!rs.next()){
				throw new FinderException("No account with user ID: "+id);
			}
			return new PrimaryKey(rs.getString("CUSTOMER_ID"));
		} catch(SQLException se) {
			throw new FinderException(se.getMessage());
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(connection != null) connection.close();
			} catch(SQLException ex) {
				//eat it for the time being
			}
		}
	}
	
	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk) throws ObjectNotFoundException, FinderException {
		return null;
	}
	
    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    protected String getResourceCacheKey() {
        return "com.freshdirect.customer.ejb.ErpCustomerInfoHome";
    }


}
