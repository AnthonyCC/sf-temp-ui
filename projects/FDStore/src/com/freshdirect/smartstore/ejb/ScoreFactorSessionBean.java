package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Session bean implementation.
 * 
 * @author istvan
 *
 */
public class ScoreFactorSessionBean extends SessionBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance(ScoreFactorSessionBean.class);

	private static final long serialVersionUID = 8645402837591723847L;

	private static String GLOBAL_FACTORS_TABLE = "CUST.SS_GLOBAL_PRODUCT_SCORES";
	private static String PERSONALIZED_FACTORS_TABLE = "CUST.SS_PERSONALIZED_PRODUCT_SCORES";
	
	private static String PERSONALIZED_FACTORS_QUERY =
		"SELECT * FROM " + PERSONALIZED_FACTORS_TABLE + " WHERE CUSTOMER_ID = ?";
	
	private static String GLOBAL_FACTORS_QUERY =
		"SELECT * FROM " + GLOBAL_FACTORS_TABLE;
	
	private static String GLOBAL_FACTOR_NAMES_QUERY = GLOBAL_FACTORS_QUERY + " WHERE PRODUCT_ID = \'CSULKOS BABLEVES\'";
	
	private static String GLOBAL_PRODUCTS_QUERY =
		"SELECT PRODUCT_ID FROM " + GLOBAL_FACTORS_TABLE;

	private static String PERSONALIZED_PRODUCTS_QUERY =
		"SELECT PRODUCT_ID FROM " + PERSONALIZED_FACTORS_TABLE + " WHERE CUSTOMER_ID = ?";
	
	private boolean isFactorColumn(String column) {
		return "PRODUCT_ID".equalsIgnoreCase(column) || "CUSTOMER_ID".equalsIgnoreCase(column);
	}
	
	/**
	 * 
	 * @param erpCustomerId null means global
	 * @return Set<ProductId:String>
	 */
	private Set retrieveProducts(String erpCustomerId) {
		Connection conn = null;
		try {
			conn = getConnection();
			
			PreparedStatement ps = conn.prepareStatement(erpCustomerId == null ? GLOBAL_PRODUCTS_QUERY : PERSONALIZED_PRODUCTS_QUERY);
			if (erpCustomerId != null) {
				ps.setString(1, erpCustomerId);
			}
			
			ResultSet rs = ps.executeQuery();
			
			Set productSet = new HashSet();
			
			while(rs.next()) {
				productSet.add(rs.getString(1));
			}
			
			return productSet;
			
		} catch (SQLException e) {
			LOGGER.warn("Could not retrieve personalized scores for " + erpCustomerId,e);
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				LOGGER.warn("Could not close connection",e);
			}
		}
	}
	
	/**
	 * 
	 * @param erpCustomerId null means global
	 * @param factors null means names only
	 * @return
	 */
	private Object retrieveScores(String erpCustomerId, List factors) {
		Connection conn = null;
	
		try {
			Object results = factors != null ? (Object)Collections.EMPTY_MAP : (Object)Collections.EMPTY_SET;
			
			conn = getConnection();
				
			PreparedStatement ps = null;
			
			// is global query
			if (erpCustomerId == null) {
				ps = conn.prepareStatement(factors == null ? GLOBAL_FACTOR_NAMES_QUERY : GLOBAL_FACTORS_QUERY);
			} else {
				ps = conn.prepareStatement(PERSONALIZED_FACTORS_QUERY);
				ps.setString(1, erpCustomerId);
			}
			
			ResultSet rs = ps.executeQuery();
			
			if (factors == null) {
				ResultSetMetaData meta = rs.getMetaData();
				Set names = new HashSet(4*meta.getColumnCount()/3);
				for (int j = 0; j< meta.getColumnCount(); ++j) {
					String column = meta.getColumnName(j+1);
					if (!isFactorColumn(column)) {
						names.add(column);
					}
				}
				
				results = names;
			} else {
				Map scores = new HashMap();
				
				while(rs.next()) {
					String productId = rs.getString("PRODUCT_ID");
					double[] factorValues = (double[])scores.get(productId);
					if (factorValues == null) {
						factorValues = new double[factors.size()];
						scores.put(productId,factorValues);
					}
					for(int i=0; i< factors.size(); ++i) {
						factorValues[i] = rs.getDouble(factors.get(i).toString());
					}
				}
				
				results = scores;
			}
			
			
			rs.close();
			ps.close();
			
			return results;
		} catch (SQLException e) {
			LOGGER.warn("Could not retrieve personalized scores for " + erpCustomerId,e);
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				LOGGER.warn("Could not close connection",e);
			}
		}
	}
	
	
	/**
	 * Get the required personalized factors for user.
	 * 
	 * @param erpCustomerId 
	 * @param factors List<FactorName:{@link String}>
	 * @throws RemoteException
	 * @return Map<ProductId:{@link String},double[]>
	 * @see ScoreFactorSB#getPersonalizedFactors(String, List)
	 */
	public Map getPersonalizedFactors(String erpCustomerId, List factors) throws RemoteException {
		return (Map)retrieveScores(erpCustomerId, factors);
	}
	
	/**
	 * Get the required global factors.
	 * 
	 * @param factors List<FactorName:{@link String}>
	 * @throws RemoteException
	 * @return Map<ProductId:{@link String},double[]>
	 * @see ScoreFactorSB#getGlobalFactors()
	 */	
	public Map getGlobalFactors(List factors) throws RemoteException {
		return (Map)retrieveScores(null, factors);
	}
	
	/**
	 * Get personalized factor names.
	 * 
	 * @return Set<{@link String}>
	 * @throws RemoteException
	 * @see ScoreFactorSB#getPersonalizedFactorNames()
	 */
	public Set getPersonalizedFactorNames() throws RemoteException {
		return (Set)retrieveScores("BATTHYANYI LAJOS", null);
	}

	/**
	 * Get the global factor names.
	 * 
	 * @return Set<{@link String>
	 * @throws RemoteException
	 * @see @link ScoreFactorSB#getPersonalizedFactorNames()
	 */
	public Set getGlobalFactorNames() throws RemoteException {
		return (Set)retrieveScores(null, null);
	}

	/**
	 * Get the product ids that have any scores.
	 * 
	 * @return Set<ProductId:String>
	 * @throws RemoteException
	 */
	public Set getGlobalProducts() throws RemoteException {
		return retrieveProducts(null);
	}
	
	/**
	 * Get the product ids for the user that have any scores.
	 * 
	 * @param erpCustomerId 
	 * @return Set<ProductId:String>
	 * @throws RemoteException
	 */
	public Set getPersonalizedProducts(String erpCustomerId) throws RemoteException {
		return retrieveProducts(erpCustomerId);
	}
}
