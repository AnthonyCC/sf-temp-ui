package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;

/**
 * Service configuration session bean implementation.
 * 
 * @author istvan
 */
public class SmartStoreServiceConfigurationSessionBean extends SessionBeanSupport {
	
	// logger
	final private static Logger LOGGER = Logger.getLogger(SmartStoreServiceConfigurationSessionBean.class);

	// generated serial version id
	private static final long serialVersionUID = -3423410150966343739L;

	private static final String GET_VARIANTS_QUERY = "SELECT v.id, v.type, v.feature FROM cust.ss_variants v WHERE v.feature = ?";

        private static final String GET_ALL_VARIANTS_QUERY = "SELECT v.id, v.type, v.feature FROM cust.ss_variants v";
	
	
	private static final String GET_VARIANT_CONFIG = "SELECT * FROM cust.SS_VARIANT_PARAMS WHERE ID = ?";

    protected void close(ResultSet rs) {
        if (rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                
            }
        }
    }

    protected void close(PreparedStatement rs) {
        if (rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                
            }
        }
    }
    
    protected void close(Connection rs) {
        if (rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                
            }
        }
    }
        
	private RecommendationServiceConfig createConfig(Connection conn, String variantId, RecommendationServiceType type) throws RemoteException {
            PreparedStatement ps = null;
            ResultSet rs = null;
            RecommendationServiceConfig result = new RecommendationServiceConfig(variantId, type);

            try {
                ps = conn.prepareStatement(GET_VARIANT_CONFIG);
                ps.setString(1, variantId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    result.set(rs.getString("KEY"), rs.getString("VALUE"));
                }
                return result;
            } catch (SQLException e) {
                LOGGER.error("Getting variants failed. ",e);
                    throw new RemoteException(e.getMessage(),e);
            } finally {
                close(rs);
                close(ps);
            }
	}
	
	
	/**
	 * Get the variants for the requested feature. 
	 * @param feature
	 * @return variants available, {@link Collection}<{@link Variant}>
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public Collection getVariants(EnumSiteFeature feature) throws RemoteException, SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			
			List result = new LinkedList();
			
			conn = getConnection();
			ps = conn.prepareStatement(feature != null ? GET_VARIANTS_QUERY : GET_ALL_VARIANTS_QUERY);
			if (feature!=null) {
			    ps.setString(1, feature.getName());
			}
			
			rs = ps.executeQuery();
			while (rs.next()) {
                            String variantId = rs.getString(1);
                            RecommendationServiceType type = RecommendationServiceType.getEnum(rs.getString(2));
                            feature = EnumSiteFeature.getEnum(rs.getString("feature"));
            
                            result.add(new Variant(variantId, feature, createConfig(conn, variantId, type)));
                        }

			return result;
		} catch (Exception e) {
			LOGGER.error("Getting variants failed. ",e);
			throw new RemoteException(e.getMessage());
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		}
	}
}
