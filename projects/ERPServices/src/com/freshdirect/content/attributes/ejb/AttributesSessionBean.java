package com.freshdirect.content.attributes.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.content.attributes.EnumAttributeType;
import com.freshdirect.content.attributes.ErpsAttributes;
import com.freshdirect.content.attributes.ErpsAttributesKey;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AttributesSessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = 7084772822981921618L;

	private static final Logger LOGGER = LoggerFactory.getInstance(AttributesSessionBean.class);

	private static final String LOAD_ATTRIBUTES_SINCE =
			"select root_id, child1_id, child2_id, atr_type, atr_name, atr_value, date_modified from erps.attributes where date_modified > ?";
	
	public Map<ErpsAttributesKey, ErpsAttributes> loadAttributes(Date since) {
		Map<ErpsAttributesKey, ErpsAttributes> values = new HashMap<ErpsAttributesKey, ErpsAttributes>(150000);

		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(LOAD_ATTRIBUTES_SINCE);
			ps.setTimestamp(1, new Timestamp(since.getTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ErpsAttributesKey key = new ErpsAttributesKey(rs.getString(1), rs.getString(2), rs.getString(3));
				EnumAttributeType type = EnumAttributeType.getByName(rs.getString(4));
				if (type == null) {
					// skip this illegal value
					continue;
				}
				EnumAttributeName name = EnumAttributeName.getByName(rs.getString(5));
				if (name == null) {
					// skip this illegal value
					continue;
				}
				Object value = type.parseValue(rs.getString(6));
				Date date = rs.getTimestamp(7);
				ErpsAttributes attributes;
				if (values.containsKey(key))
					attributes = values.get(key);
				else {
					attributes = new ErpsAttributes(key);
					values.put(key, attributes);
				}
				attributes.setAttributeValue(name, value, date);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			LOGGER.error("SQLException occurred in loadAtributes(since)", e);
			throw new FDRuntimeException(e, "SQLException occured");
		} finally {
		    close(conn);
		}
		
		return values;
	}

	private static final String LOAD_ATTRIBUTES_BY_KEY =
		"select atr_type, atr_name, atr_value, date_modified from erps.attributes where root_id = ? and child1_id = ? child2_id = ?";
	
	public ErpsAttributes loadAttributes(ErpsAttributesKey key) {
		ErpsAttributes value = null;
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("select atr_type, atr_name, atr_value, date_modified from erps.attributes where "+key.getQueryFragment());
			key.fillStatement(ps, 1, false);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				EnumAttributeType type = EnumAttributeType.getByName(rs.getString(1));
				if (type == null) {
					// skip this illegal value
					continue;
				}
				EnumAttributeName name = EnumAttributeName.getByName(rs.getString(2));
				if (name == null) {
					// skip this illegal value
					continue;
				}
				Object val = type.parseValue(rs.getString(3));
				Date date = rs.getTimestamp(4);
				if (value == null)
					value = new ErpsAttributes(key);
				value.setAttributeValue(name, val, date);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			getSessionContext().setRollbackOnly();
			LOGGER.error("SQLException occurred in loadAtributes(key)", e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				LOGGER.warn("Cleanup failed", e);
			}
		}
		return value;
	}


	private static final String INSERT_ATTRIBUTES = "insert into erps.attributes " +
			"(id, root_id, child1_id, child2_id, atr_type, atr_name, atr_value, date_modified) values (?, ?, ?, ?, ?, ?, ?, ?)";
	
        public void storeAttributes(ErpsAttributes attributes) {
            Connection conn = null;
            try {
                conn = getConnection();
                storeAttributes(conn, attributes);
            } catch (SQLException e) {
                getSessionContext().setRollbackOnly();
                LOGGER.error("SQLException occurred in storeAtributes", e);
                throw new FDRuntimeException(e, "SQLException occured");
            } finally {
                close(conn);
            }
        }

	private void storeAttributes(Connection conn, ErpsAttributes attributes) throws SQLException {
		ErpsAttributesKey key = attributes.getKey();
		PreparedStatement ps = conn.prepareStatement("delete from erps.attributes where "+key.getQueryFragment());
		key.fillStatement(ps, 1, false);
		int count = ps.executeUpdate();
                LOGGER.info("deleting "+count+" attributes with key: "+key);
		ps.close();

		// store new attributes			
		ps = conn.prepareStatement(INSERT_ATTRIBUTES);
		Timestamp now = new Timestamp(new Date().getTime());
                final Map<EnumAttributeName, Object> updatedAttributes = attributes.getUpdatedAttributes();
                if (updatedAttributes.isEmpty()) {
                    updatedAttributes.put(EnumAttributeName.RESET_TO_DEFAULT, "#empty_value");
                }
                LOGGER.info("storing "+updatedAttributes+" for "+key);
                for (Map.Entry<EnumAttributeName, Object> entry : updatedAttributes.entrySet()) {
                    int pos = 1;
                    String id = getNextId(conn);
                    ps.setString(pos++, id);
                    pos = key.fillStatement(ps, pos, true);
                    ps.setString(pos++, entry.getKey().getType().getName());
                    ps.setString(pos++, entry.getKey().getName());
                    ps.setString(pos++, entry.getValue().toString());
                    ps.setTimestamp(pos++, now);
                    ps.addBatch();
                }
		ps.executeBatch();
		ps.close();
	}


        public void storeAttributes(Collection<ErpsAttributes> attributes) {
            Connection conn = null;
            try {
                conn = getConnection();
                for (ErpsAttributes attr : attributes) {
                    storeAttributes(conn, attr);
                }
            } catch (SQLException e) {
                getSessionContext().setRollbackOnly();
                LOGGER.error("SQLException occurred in storeAtributes(Collection)", e);
                throw new FDRuntimeException(e, "SQLException occured");
            } finally {
                close(conn);
            }
        }
	
    protected String getNextId(Connection conn) throws SQLException {
    	return SequenceGenerator.getNextId(conn, "ERPS");
    }
}
