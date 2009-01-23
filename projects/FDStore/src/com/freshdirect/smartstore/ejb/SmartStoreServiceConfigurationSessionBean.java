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

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SimpleCRSC;
import com.freshdirect.smartstore.Variant;

/**
 * Service configuration session bean implementation.
 * 
 * @author istvan
 */
public class SmartStoreServiceConfigurationSessionBean extends SessionBeanSupport {
	
	// logger
	final private static Category LOGGER = Category.getInstance(SmartStoreServiceConfigurationSessionBean.class);

	// generated serial version id
	private static final long serialVersionUID = -3423410150966343739L;

	private static final String GET_VARIANTS_QUERY = 
		"SELECT v.id, v.config_id, sc.type " +
		"FROM cust.ss_variants v, cust.ss_service_configs sc " +
		"WHERE v.feature = ? AND sc.id = v.config_id";
	
	private static final String GET_COMPOSITES_QUERY = 
		"SELECT cc.part_config_id, cc.frequency, sc.type " +
		"FROM cust.ss_composite_configs cc, cust.ss_service_configs sc WHERE " +
		"cc.config_id = ? AND cc.part_config_id = sc.id";
	
	private static final String GET_VARIANT_CONFIG = "SELECT * FROM cust.SS_SERVICE_PARAMS WHERE ID = ?";
	   
	/**
	 * Get the configuration of a composite config.
	 * @param conn db connection
	 * @param configId
	 * @return composite config
	 */
	private RecommendationServiceConfig getCompositeConfig(final Connection conn, final String configId)  {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		SimpleCRSC result = new SimpleCRSC(configId);
		
		try {
			ps = conn.prepareStatement(GET_COMPOSITES_QUERY);
			ps.setString(1, configId);

			rs = ps.executeQuery();
								
			while(rs.next()) { 
				RecommendationServiceType type = RecommendationServiceType.getEnum(rs.getString(3));
				result.addPart(
					RecommendationServiceType.COMPOSITE == type ?
						getCompositeConfig(conn, rs.getString(1)) :
						new RecommendationServiceConfig(rs.getString(1),type),
					rs.getInt(2));
			}

			return result;
		} catch (SQLException e) {
			LOGGER.error("Getting composite configs failed. ",e);
			throw new FDRuntimeException(e);
		} finally {
		    close(rs);
		    close(ps);
		}
	}
	
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

        
	private RecommendationServiceConfig createConfig(Connection conn, String configId, RecommendationServiceType type) throws RemoteException {
            PreparedStatement ps = null;
            ResultSet rs = null;
            RecommendationServiceConfig result = new RecommendationServiceConfig(configId, type);

            try {
                ps = conn.prepareStatement(GET_VARIANT_CONFIG);
                ps.setString(1, configId);
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
			ps = conn.prepareStatement(GET_VARIANTS_QUERY);
			ps.setString(1, feature.getName());
			
			rs = ps.executeQuery();
			while (rs.next()) {
				String variantId = rs.getString(1);
				String configId = rs.getString(2);
				RecommendationServiceType type = RecommendationServiceType.getEnum(rs.getString(3));
			
				result.add(
					new Variant(
						variantId,
						feature,
						RecommendationServiceType.COMPOSITE == type ?
							getCompositeConfig(conn, configId) :
							createConfig(conn, configId,type)
					)
				);								
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
