/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.content.attributes.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.EnumAttributeType;
import com.freshdirect.content.attributes.FlatAttribute;
import com.freshdirect.content.attributes.FlatAttributeCollection;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 * @ejbHome <{AttributeFacadeHome}>
 * @ejbRemote <{AttributeFacadeSB}>
 * @ejbStateless
 *
 * @version $Revision$
 * @author $Author$
 */
public class AttributeFacadeSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(AttributeFacadeSessionBean.class);
	private final static boolean DEBUG = false;

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.content.attributes.ejb.AttributeFacadeHome";
	}

	public FlatAttributeCollection getAttributes(String[] rootIds) throws AttributeException {
		List attribs = new ArrayList();
		for (int i = 0; i < rootIds.length; i++) {
			attribs.addAll(this.getAttributeList(rootIds[i]));
		}
		return new FlatAttributeCollection(attribs);
	}
	
	private static final String LOAD_ATTR_QUERY = "select root_id, child1_id, child2_id, atr_type, atr_name, atr_value, date_modified " 
		+ "from erps.attributes "
		+ "where date_modified > ? "
		+ "order by root_id ";
	
	public Map loadAttributes(Date since) throws AttributeException {
		Connection conn = null;
		try{
			Map m = new HashMap();
			conn = this.getConnection();
			
			PreparedStatement ps = conn.prepareStatement(LOAD_ATTR_QUERY);
			ps.setTimestamp(1, new Timestamp(since.getTime()));
			ResultSet rs = ps.executeQuery();
			Date maxModified = since;
			while(rs.next()){
				Timestamp t = rs.getTimestamp("DATE_MODIFIED");
				if(maxModified == null || maxModified.before(t)){
					maxModified = t;
				}
				String rootId = rs.getString("ROOT_ID");
				String[] ids = null;

				String id = rs.getString("CHILD2_ID");
				if (id != null) {
					ids = new String[3];
					ids[2] = id;
				}

				id = rs.getString("CHILD1_ID");
				if (id != null) {
					if (ids == null) {
						ids = new String[2];
					}
					ids[1] = id;
				}

				if (ids == null) {
					ids = new String[1];
				}
				ids[0] = rootId;
				String type = rs.getString("ATR_TYPE");
				String name = rs.getString("ATR_NAME");
				Object value = null;
				if (type.equals(EnumAttributeType.STRING.getName())) {
					value = rs.getString(6);
				} else if (type.equals(EnumAttributeType.BOOLEAN.getName())) {
					value = Boolean.valueOf(rs.getString(6));
				} else if (type.equals(EnumAttributeType.INTEGER.getName())) {
					value = Integer.valueOf(rs.getString(6));
				} else {
					throw new AttributeException("Unknown attribute type " + type + " encountered for rootID " + rootId);
				}
				
				FlatAttribute ra = new FlatAttribute(ids, name, value);
				ra.setLastModified(t);
				List lst = (List) m.get(rootId);
				if(lst == null) {
					lst = new ArrayList();
					m.put(rootId, lst);
				}
				lst.add(ra);
			}
			rs.close();
			ps.close();
			return m;
		}catch (SQLException e) {
			throw new AttributeException(e, "SQLException occured");
		}finally {
			try {
				if (conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("Cleanup failed", e);
			}
		}
	}

	protected List getAttributeList(String rootId) throws AttributeException {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps =
				conn.prepareStatement(
					"select root_id, child1_id, child2_id, atr_type, atr_name, atr_value from erps.attributes where root_id = ?");
			ps.setString(1, rootId);
			ResultSet rs = ps.executeQuery();

			// create raw attributes from resultset
			List lst = new ArrayList();
			while (rs.next()) {

				// get IDs: root, child1, child2

				String[] ids = null;

				String id = rs.getString(3);
				if (id != null) {
					ids = new String[3];
					ids[2] = id;
				}

				id = rs.getString(2);
				if (id != null) {
					if (ids == null) {
						ids = new String[2];
					}
					ids[1] = id;
				}

				if (ids == null) {
					ids = new String[1];
				}
				ids[0] = rs.getString(1);

				String type = rs.getString(4);
				String name = rs.getString(5);
				Object value = null;
				if (type.equals(EnumAttributeType.STRING.getName())) {
					value = rs.getString(6);
				} else if (type.equals(EnumAttributeType.BOOLEAN.getName())) {
					value = Boolean.valueOf(rs.getString(6));
				} else if (type.equals(EnumAttributeType.INTEGER.getName())) {
					value = Integer.valueOf(rs.getString(6));
				} else {
					throw new AttributeException("Unknown attribute type " + type + " encountered for rootID " + rootId);
				}

				FlatAttribute ra = new FlatAttribute(ids, name, value);
				if (DEBUG)
					LOGGER.debug("Adding " + ra);
				lst.add(ra);
			}

			rs.close();
			ps.close();

			return lst;
		} catch (SQLException se) {
			throw new AttributeException(se, "SQLException occured");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				LOGGER.warn("Cleanup failed", se2);
			}
		}
	}

	public void storeAttributes(FlatAttributeCollection attributes) throws AttributeException {

		FlatAttribute[] rawAttributes = attributes.getFlatAttributes();

		Connection conn = null;
		try {
			conn = getConnection();

			// get all unique Root IDs
			Set rootIds = new HashSet();
			for (int i = 0; i < rawAttributes.length; i++) {
				rootIds.add(rawAttributes[i].getIdPath()[0]);
			}

			// remove everything below them
			PreparedStatement ps = conn.prepareStatement("delete from erps.attributes where root_id = ?");
			for (Iterator i = rootIds.iterator(); i.hasNext();) {
				ps.setString(1, (String) i.next());
				ps.executeUpdate();
			}

			ps.close();

			// store new attributes			
			ps = conn.prepareStatement(
					"insert into erps.attributes (id, root_id, child1_id, child2_id, atr_type, atr_name, atr_value, date_modified) values (?, ?, ?, ?, ?, ?, ?, sysdate)");

			for (int i = 0; i < rawAttributes.length; i++) {
				FlatAttribute ra = rawAttributes[i];
				if ((ra.getValue() == null) || ("".equals(ra.getValue())))
					continue;
				String[] ids = ra.getIdPath();
				ps.clearParameters();
				ps.setString(1, getNextId(conn, "ERPS"));
				ps.setString(2, ids[0]);
				switch (ids.length) {
					case 1 :
						ps.setNull(3, Types.VARCHAR);
						ps.setNull(4, Types.VARCHAR);
						break;
					case 2 :
						ps.setString(3, ids[1]);
						ps.setNull(4, Types.VARCHAR);
						break;
					case 3 :
						ps.setString(3, ids[1]);
						ps.setString(4, ids[2]);
						break;
					default :
						getSessionContext().setRollbackOnly();
						throw new AttributeException("Attribute hierarchy too deep " + ra);
				}

				ps.setString(5, ra.getAttributeType().getName());
				ps.setString(6, ra.getName());
				ps.setString(7, ra.getValue().toString());
			}
			
			//this should be outside of the loop so it commits them all at once
			if (ps.executeUpdate() != 1) {
				getSessionContext().setRollbackOnly();
				throw new AttributeException("Update failed");
			}

			ps.close();

		} catch (SQLException se) {
			getSessionContext().setRollbackOnly();
			LOGGER.error("SQLException occurred in storeAtributes", se);
			throw new AttributeException(se, "SQLException occured");
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				LOGGER.warn("Cleanup failed", se2);
			}
		}

	}

}
