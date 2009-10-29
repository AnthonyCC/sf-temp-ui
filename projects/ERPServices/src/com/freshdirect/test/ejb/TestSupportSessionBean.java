package com.freshdirect.test.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class TestSupportSessionBean extends SessionBeanSupport {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5704438556535855250L;

	private static Category LOGGER = LoggerFactory.getInstance(TestSupportSessionBean.class);

	public boolean ping() throws RemoteException {
		LOGGER.info("ping");
		
		return true;
	}
	
	
	public final String DYF_ELIGIBLE_CUSTOMERS_SQL =
		"SELECT ID, ORDERS FROM (" +
		"  SELECT S.CUSTOMER_ID AS ID, COUNT(S.CUSTOMER_ID) AS ORDERS" +
		"  FROM CUST.SALE S" +
		"  WHERE S.STATUS='STL'" +
		"  GROUP BY S.CUSTOMER_ID" +
		")" +
		"WHERE (ORDERS>2)" +
		"ORDER BY ORDERS DESC";



	/**
	 * Returns the IDs of customers who is eligible for DYF Service
	 * @return List<Long> List of customer IDs
	 * @throws RemoteException
	 */
	public List getDYFEligibleCustomerIDs() throws RemoteException {
		Connection conn = null;
		ArrayList idArray = new ArrayList();
		
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(DYF_ELIGIBLE_CUSTOMERS_SQL);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Long customerID = new Long(rs.getLong(1));
				idArray.add(customerID);
			}

			// free resources
			rs.close();
			ps.close();
		} catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                	conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
		}
		
		return idArray;
	}
	
	
	public List getErpCustomerIds() throws RemoteException {
		Connection conn = null;
		ArrayList idArray = new ArrayList();
		
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement("SELECT ERP_CUSTOMER_ID FROM CUST.FDCUSTOMER");
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Long customerID = new Long(rs.getLong(1));
				idArray.add(customerID);
			}

			// free resources
			rs.close();
			ps.close();
		} catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                	conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
		}
		
		return idArray;
	}

	public String getFDCustomerIDForErpId(String erpCustomerPK) throws RemoteException {
		Connection conn = null;
		String fdCustomerPK = null;
		
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.FDCUSTOMER WHERE ERP_CUSTOMER_ID=?");
			ps.setString(1, erpCustomerPK);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fdCustomerPK = rs.getString(1);
			}

			// free resources
			rs.close();
			ps.close();
		} catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                	conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
		}

        return fdCustomerPK;
	}



	public String getErpIDForUserID(String userID) throws RemoteException {
		Connection conn = null;
		String fdCustomerPK = null;
		
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(
					"SELECT ID AS ERP_ID FROM CUST.CUSTOMER WHERE USER_ID=?");
			ps.setString(1, userID);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fdCustomerPK = rs.getString(1);
			}

			// free resources
			rs.close();
			ps.close();
		} catch (SQLException e) {
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                	conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
		}

        return fdCustomerPK;
	}
}
