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

	private static final String GET_VARIANTS_QUERY = "SELECT v.id, v.type, v.feature, v.alias_id FROM cust.ss_variants v WHERE v.feature = ?";

	private static final String GET_ALL_VARIANTS_QUERY = "SELECT v.id, v.type, v.feature, v.alias_id FROM cust.ss_variants v";

	private static final String GET_VARIANT_CONFIG = "SELECT * FROM cust.SS_VARIANT_PARAMS WHERE ID = ?";

	private static final String GET_VARIANT_ALIAS = "SELECT v.id, v.type, v.feature FROM cust.ss_variants v WHERE id = ?";
	
    protected void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	protected void close(PreparedStatement rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	protected void close(Connection rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}



	/**
	 * Configures a recommendation service
	 * 
	 * @param conn
	 * @param variantId
	 * @param type
	 * @return
	 * @throws RemoteException
	 */
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
		} catch (SQLException e) {
			LOGGER.error("Failed to configure variant " + variantId, e);
			throw new RemoteException(e.getMessage(), e);
		} finally {
			close(rs);
			close(ps);
		}
		
		return result;
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
			
			if ( feature != null ) {
				ps = conn.prepareStatement( GET_VARIANTS_QUERY );
			    ps.setString(1, feature.getName());
			} else {
				ps = conn.prepareStatement( GET_ALL_VARIANTS_QUERY );				
			}
			
			rs = ps.executeQuery();
			while (rs.next()) {
				
				String variantId = rs.getString( "id" );
				String typeStr = rs.getString( "type" );
				String featureStr = rs.getString( "feature" );
				String aliasId = rs.getString( "alias_id" );
				String variantIdConfig = variantId;
				
				RecommendationServiceType type = null; 
				
				// -------------------
				// ALIAS type variant
				// -------------------
				// typeStr = type of the aliased variant
				// variantIdConfig = id of the aliased variant (used for configuration creation)
				// variantId = id of the original(alias type) variant
				// featureStr = feature of the original(alias type) variant
				// aliasId is not used again (recursive aliasing not supported)
				
				if ( "alias".equals(typeStr) ) {
					
					if ( aliasId == null ) {
						LOGGER.error("Skipping variant " + variantId + " due to missing alias id...");
						continue;
					}
					
					PreparedStatement aliasPs = null;
					ResultSet aliasRs = null;
					try {
						aliasPs = conn.prepareStatement( GET_VARIANT_ALIAS );
						aliasPs.setString( 1, aliasId );
						aliasRs = aliasPs.executeQuery();
						
						if ( !aliasRs.next() ) {
							LOGGER.error("Skipping variant " + variantId + " due to missing alias in DB... (invalid alias id)");
							continue;							
						}

						// id used for creating config (aliased variants id)
						variantIdConfig = aliasId;
						
						// override type from alias
						typeStr = aliasRs.getString( "type" );
						
						if ( "alias".equals(typeStr) ) {
							LOGGER.error("Skipping variant " + variantId + " due to recursive aliasing, which is not supported...");
							continue;							
						}						
						
					} catch (SQLException e) {
						LOGGER.error("Skipping variant " + variantId + " due to sql error while getting variant...");
						continue;	
						
					} finally {
						if (aliasRs != null) aliasRs.close();
						if (aliasPs != null) aliasPs.close();
					}					
				}
				
				// continue normally...			
				try {
					type = RecommendationServiceType.getEnum( typeStr );				
					feature = EnumSiteFeature.getEnum( featureStr );
					
					if ( feature != null && type != null ) {
						result.add( new Variant( variantId, feature, createConfig(conn, variantIdConfig, type) ) );
					}
					
				} catch (RemoteException exc) {
					// Ignore variant if error occures during configuration
					LOGGER.error("Skipping variant " + variantId + " due to configuration error...");
				}
			}

			return result;
			
		} catch (Exception e) {
			LOGGER.error("Getting variants failed. ",e);
			throw new RemoteException(e.getMessage(),e);
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		}
	}
}
