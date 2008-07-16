package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class VariantSelectionSessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = 7401851794123258702L;

	private static Category LOGGER = LoggerFactory.getInstance(VariantSelectionSessionBean.class);





	// This command returns (Cn, Vn) pairs
	//   'C1', 'Random DYF'
	//   'C2', 'freqbought'
	//   ...
	//
	final String SQL_GETVARIANTMAP =
		"SELECT T1.ID AS COHORT_ID, T2.VARIANT_ID AS VARIANT_ID " +
		"FROM CUST.SS_COHORTS T1, CUST.SS_VARIANT_ASSIGNMENT T2 " +
		"WHERE T2.COHORT_ID=T1.ID " +
		"AND T2.\"DATE\"=( " +
		"  SELECT MAX(T3.\"DATE\") " +
		"  FROM CUST.SS_VARIANT_ASSIGNMENT T3 " +
		"  WHERE T3.COHORT_ID=T1.ID " +
		"  AND T3.\"DATE\" < SYSDATE " +
		") " +
		"AND T2.VARIANT_ID IN ( " +
		"  SELECT ID FROM CUST.SS_VARIANTS WHERE FEATURE=? " +
		") " +
		"ORDER BY COHORT_ID";

	/**
	 * Returns (Cn,Vn) pairs where Cn is a Cohort and Vn is the assigned Variant ID
	 * @param feature Name of Site Feature
	 * @return Variant assignment map
	 * @throws RemoteException
	 */
	public Map getVariantMap(EnumSiteFeature feature) throws RemoteException {
		Connection conn = null;
		Map cohortVariantMap = new HashMap();
		
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(SQL_GETVARIANTMAP);
			ps.setString(1, feature.getName());
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				cohortVariantMap.put(rs.getString(1), rs.getString(2));
			}

			// free resources
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("VariantSelectionSessionBean.getVariantMap failed; exc=" + e);
			try {
				LOGGER.error("Connection URL: " + conn.getMetaData().getURL() + "/ User: " + conn.getMetaData().getUserName());
			} catch (SQLException e1) {
			}
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                	conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
		}

        return cohortVariantMap;
	}
	
	
	
	/**
	 * Returns (cohort ID, weight) pairs indexed by cohort IDs
	 * @return
	 */
	public Map getCohorts() throws RemoteException {
		Connection conn = null;
		Map cohortMap = new HashMap();
		
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement("SELECT ID, WEIGHT FROM CUST.SS_COHORTS");
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				cohortMap.put(rs.getString(1), new Integer(rs.getString(2)));
			}

			// free resources
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("VariantSelectionSessionBean.getCohorts failed; exc=" + e);
			try {
				LOGGER.error("Connection URL: " + conn.getMetaData().getURL() + "/ User: " + conn.getMetaData().getUserName());
			} catch (SQLException e1) {
			}
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                	conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
		}

        return cohortMap;
	}
	
	
	/**
	 * Returns the list of variant IDs belonging to a site feature
	 * @param feature
	 * @return list of variant IDs
	 * @throws RemoteException
	 */
	public List getVariants(EnumSiteFeature feature) throws RemoteException {
		Connection conn = null;
		List variantList = new ArrayList();
		
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SS_VARIANTS WHERE FEATURE=?");
			ps.setString(1, feature.getName());
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				variantList.add(rs.getString(1));
			}

			// free resources
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("VariantSelectionSessionBean.getVariants failed; exc=" + e);
			try {
				LOGGER.error("Connection URL: " + conn.getMetaData().getURL() + "/ User: " + conn.getMetaData().getUserName());
			} catch (SQLException e1) {
			}
            throw new EJBException(e);
        } finally {
            try {
                if (conn != null)
                	conn.close();
            } catch (SQLException sqle) {
                throw new EJBException(sqle);
            }
		}

        return variantList;
		
	}
	
}
