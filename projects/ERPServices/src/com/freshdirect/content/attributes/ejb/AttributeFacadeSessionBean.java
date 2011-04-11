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
import java.util.Enumeration;
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
import com.freshdirect.erp.model.ActivityLog;

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

	private static final long serialVersionUID = 1L;
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

	public void storeAttributes(FlatAttributeCollection attributes, String user, String sapId) throws AttributeException {

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
			PreparedStatement ps1 = conn.prepareStatement("select root_id, child1_ID, child2_ID, atr_type, atr_name, atr_value from erps.attributes where root_id = ?");
			PreparedStatement ps = conn.prepareStatement("delete from erps.attributes where root_id = ?");
			ResultSet rset = null;
			java.util.Hashtable<String,ActivityLog> oldHash = new java.util.Hashtable<String,ActivityLog>();
			//get timestamp to replace sysdate
			Timestamp ts = new Timestamp(new Date().getTime());
			
			for (Iterator i = rootIds.iterator(); i.hasNext();) {
				/*
				 * Swathi: Before removing everything read them into hash. 
				 * We need to save these old values for a while for audit trail			  
				 */
				String root_id = (String) i.next();
				LOGGER.debug("RootID: " + root_id);
				ps1.setString(1, root_id);
				rset = ps1.executeQuery();
				while(rset.next()) {
					StringBuffer sb = new StringBuffer(rset.getString("root_id"));
					sb.append("|");
					if(rset.getString("child1_ID") != null) {						
						sb.append(rset.getString("child1_ID"));
						sb.append("|");
					}
					if(rset.getString("child2_ID") != null) {
						sb.append(rset.getString("child2_ID"));
						sb.append("|");
					}
					sb.append(rset.getString("atr_name"));
					String key = sb.toString();
					String value = rset.getString("atr_value");
					ActivityLog al = new ActivityLog(sapId, null, "ATTR_VALUE", value, null, ts, user);
	            	al.setRootId(rset.getString("root_id"));
	            	al.setChild1Id(rset.getString("child1_ID"));
	            	al.setChild2Id(rset.getString("child2_ID"));
	            	al.setAtrType(rset.getString("atr_type"));
	            	al.setAtrName(rset.getString("atr_name"));
					oldHash.put(key, al);
				}
				//now delete them
				ps.setString(1, root_id);
				ps.executeUpdate();
			}

			LOGGER.debug("OldHash = " + oldHash);

			ps.close();
			if(ps1 != null)
				ps1.close();
			if(rset != null)
				rset.close();

			// store new attributes			
			ps = conn.prepareStatement(
					"insert into erps.attributes (id, root_id, child1_id, child2_id, atr_type, atr_name, atr_value, date_modified) values (?, ?, ?, ?, ?, ?, ?, ?)");
			
			List<ActivityLog> aLog = new ArrayList<ActivityLog>();

			for (int i = 0; i < rawAttributes.length; i++) {
				StringBuffer sb = new StringBuffer();
				FlatAttribute ra = rawAttributes[i];
				if ((ra.getValue() == null) || ("".equals(ra.getValue())))
					continue;
				String[] ids = ra.getIdPath();
				ps.clearParameters();
				ps.setString(1, getNextId(conn, "ERPS"));
				ps.setString(2, ids[0]);
				sb.append(ids[0]);
				sb.append("|");
				String value = ra.getValue().toString();
				String child1 = null;
				String child2 = null;
				switch (ids.length) {
					case 1 :
						ps.setNull(3, Types.VARCHAR);
						ps.setNull(4, Types.VARCHAR);
						break;
					case 2 :
						ps.setString(3, ids[1]);
						ps.setNull(4, Types.VARCHAR);
						child1 = ids[1];
						sb.append(ids[1]);
						sb.append("|");
						break;
					case 3 :
						ps.setString(3, ids[1]);
						ps.setString(4, ids[2]);
						child1 = ids[1];
						child2 = ids[2];
						sb.append(ids[1]);
						sb.append("|");
						sb.append(ids[2]);
						sb.append("|");
						break;
					default :
						getSessionContext().setRollbackOnly();
						throw new AttributeException("Attribute hierarchy too deep " + ra);
				}
				sb.append(ra.getName());

				ps.setString(5, ra.getAttributeType().getName());
				ps.setString(6, ra.getName());
				ps.setString(7, ra.getValue().toString());
				//use timestamp instead of sysdate
				ps.setTimestamp(8, ts);

				if (ps.executeUpdate() != 1) {
					getSessionContext().setRollbackOnly();
					throw new AttributeException("Update failed");
				}
				
				LOGGER.debug("newvalue:" + value);
				if(sb != null && value != null) {					
					String key = sb.toString();
					String oldValue = "";
					if(oldHash.containsKey(key)) {
		            	oldValue = ((ActivityLog) oldHash.get(key)).getOldValue();
		            	oldHash.remove(key);
			}
		            if(!oldValue.equalsIgnoreCase(value)) {
		            	ActivityLog al = new ActivityLog(sapId, null, "ATTR_VALUE", oldValue, value, ts, user);
		            	al.setRootId(ids[0]);
		            	al.setChild1Id(child1);
		            	al.setChild2Id(child2);
		            	al.setAtrType(ra.getAttributeType().getName());
		            	al.setAtrName(ra.getName());
		            	aLog.add(al);
		            	LOGGER.debug("ActivityLog:" + al.toString());
		            }
				}				
			}

			ps.close();

			Enumeration e = oldHash.keys();
			//iterate through Hashtable keys Enumeration
			while(e.hasMoreElements()) {
				String key = (String) e.nextElement();
				ActivityLog al = (ActivityLog) oldHash.get(key);
				aLog.add(al);
			}
			
			//call activity log method
			com.freshdirect.erp.ejb.ErpActivityLogDAO.logActivity(conn, aLog);

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
