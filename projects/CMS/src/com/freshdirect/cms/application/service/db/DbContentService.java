/*
 * Created on Jan 12, 2005
 *
 */
package com.freshdirect.cms.application.service.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponse;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.AbstractContentService;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.node.ContentNode;
import com.freshdirect.cms.util.DaoUtil;

/**
 * {@link com.freshdirect.cms.application.ContentServiceI} implementation
 * that persists nodes to/from an Oracle RDBMS in a generic schema.
 * 
 * Uses the following tables:
 * <pre>
 * cms_contentnode
 * cms_attribute
 * cms_relationship
 * cms_all_nodes
 * </pre>
 * 
 * Uses the <code>cms_system_seq</code> sequence.
 */
public class DbContentService extends AbstractContentService implements ContentServiceI {

	private final static boolean DEBUG_MISSING = false;

	private ContentTypeServiceI typeService;

	private DataSource dataSource;

	public DbContentService() {
		super();
	}

	public void setContentTypeService(ContentTypeServiceI typeService) {
		this.typeService = typeService;
	}

	public ContentTypeServiceI getTypeService() {
		return typeService;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public void initialize() {
	}

	private final static String QUERY_ALL_KEYS = "select id, contenttype_id from cms_contentnode";

	private final static String QUERY_KEYS_BY_TYPE = QUERY_ALL_KEYS + " where contenttype_id = ?";

	private Set getContentKeys(String query, String[] args) {
		Connection conn = null;

		HashSet returnSet = new HashSet();

		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			for (int i = 0; i < args.length; i++) {
				ps.setString(i + 1, args[i]);
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				//returnSet.add(new ContentKey(new ContentType(rs.getString("contenttype_id")), rs.getString("id")));
				returnSet.add(ContentKey.decode(rs.getString("id")));
			}
			rs.close();
			ps.close();

		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new CmsRuntimeException(e);
				}
			}
		}
		return returnSet;
	}

	public Set getContentKeys() {
		return getContentKeys(QUERY_ALL_KEYS, new String[] {});
	}

	public Set getContentKeysByType(ContentType type) {
		return getContentKeys(QUERY_KEYS_BY_TYPE, new String[] {type.getName()});
	}

	public ContentNodeI getContentNode(ContentKey key) {
		HashSet p = new HashSet();
		p.add(key);
		Map map = getContentNodes(p);
		ContentNodeI c = (ContentNodeI) map.get(key);
		return c;
	}

	private final static String SELECT_MANY_NODES = "select /*+ FIRST_ROWS */ id from cms_contentnode where id in (?)";

	private final static String SELECT_MANY_ATTR = "select /*+ FIRST_ROWS */ atr.contentnode_id, atr.def_name, atr.value"
		+ " from cms_attribute atr"
		+ " where atr.contentnode_id in (?)"
		+ " order by atr.contentnode_id, atr.def_name";

	private final static String SELECT_MANY_REL = "select /*+ FIRST_ROWS */ r.parent_contentnode_id, r.def_name, r.child_contentnode_id"
		+ " from cms_relationship r"
		+ " where r.parent_contentnode_id in (?)"
		+ " order by r.parent_contentnode_id, r.def_name, r.ordinal";

	private final static String SELECT_PARENT_NODE = "select a.parent_contentnode_id"
		+ " from cms_all_nodes a, cms_contentnode c"
		+ " where a.child_contentnode_id = (?)"
		+ " and a.child_contentnode_id = c.id";

	public Map getContentNodes(Set keys) {
		Connection conn = null;
		try {
			conn = getConnection();

			String[] idChunks = DaoUtil.chunkContentKeys(keys);

			Map nodeMap = new HashMap(keys.size());
			for (int i = 0; i < idChunks.length; i++) {
				process(conn, nodeMap, idChunks[i]);
			}

			if (DEBUG_MISSING && nodeMap.size() < keys.size()) {
				Set s = new HashSet(keys);
				s.removeAll(nodeMap.keySet());
				System.err.println("DbContentService.getContentNodes() NULL for " + s);
			}

			return nodeMap;

		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new CmsRuntimeException(e);
				}
			}
		}
	}
	
	public Set getParentKeys(ContentKey key) {
		Set set = new HashSet();
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(SELECT_PARENT_NODE);
			ps.setString(1, key.getEncoded());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ContentKey parentKey = ContentKey.decode(rs.getString(1));
				set.add(parentKey);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new CmsRuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle2) {
					throw new CmsRuntimeException(sqle2);
				}
			}
		}

		return set;
	}

	private final static int FETCH_SIZE = 1000;

	private void process(Connection conn, Map nodeMap, String ids) throws SQLException {

		Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		stmt.setFetchSize(FETCH_SIZE);

		String query = StringUtils.replace(SELECT_MANY_NODES, "?", ids);
		ResultSet rs = stmt.executeQuery(query);
		processNodesResultSet(rs, nodeMap);
		rs.close();

		query = StringUtils.replace(SELECT_MANY_ATTR, "?", ids);
		rs = stmt.executeQuery(query);
		processAttributesResultSet(rs, nodeMap);
		rs.close();

		query = StringUtils.replace(SELECT_MANY_REL, "?", ids);
		rs = stmt.executeQuery(query);
		processAttributesResultSet(rs, nodeMap);
		rs.close();

		stmt.close();

	}

	private final static String DELETE_ATTRIBUTE = "delete from cms_attribute where contentnode_id = ?";

	private final static String DELETE_RELATIONSHIP = "delete from cms_relationship where parent_contentnode_id = ?";

	/** conditional insert */
	private final static String INSERT_NODE = "insert into cms_contentnode(id, contenttype_id) select ?, ? from dual where not exists (select id from cms_contentnode where id=?)";

	private final static String INSERT_ATTRIBUTE = "insert into cms_attribute(id, contentnode_id, def_name, def_contenttype, value, ordinal) "
		+ "values (cms_system_seq.nextVal, ?, ?, ?, ?, ?)";

	private final static String INSERT_RELATIONSHIP = "insert into cms_relationship(id, parent_contentnode_id, def_name, def_contenttype, child_contentnode_id, ordinal) "
		+ "values (cms_system_seq.nextVal, ?, ?, ?, ?, ?)";

	private void storeContentNode(ContentNodeI node) {
		//System.out.println("Storing content node in DB: " + node.getKey());
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_ATTRIBUTE);
			ps.setString(1, node.getKey().getEncoded());
			ps.executeQuery();
			ps.close();

			ps = conn.prepareStatement(DELETE_RELATIONSHIP);
			ps.setString(1, node.getKey().getEncoded());
			ps.executeQuery();
			ps.close();

			PreparedStatement insPs = conn.prepareStatement(INSERT_NODE);
			addInsBatch(insPs, node.getKey());

			Map attrMap = node.getAttributes();
			PreparedStatement atrPs = conn.prepareStatement(INSERT_ATTRIBUTE);
			PreparedStatement relPs = conn.prepareStatement(INSERT_RELATIONSHIP);
			for (Iterator i = attrMap.values().iterator(); i.hasNext();) {
				AttributeI attr = (AttributeI) i.next();
				//System.out.println("Saving attribute - " + node.getKey() + ":"+ attr.getName());
				if (attr.getValue() == null) {
					continue;
				}
				if (attr instanceof RelationshipI) {
					RelationshipI rel = (RelationshipI) attr;
					if (EnumCardinality.ONE.equals(rel.getDefinition().getCardinality())) {
						ContentKey k = (ContentKey) rel.getValue();
						addInsBatch(insPs, k);
						addRelBatch(relPs, rel, k, 0);
					} else {
						List l = (List) rel.getValue();
						for (int m = 0; m < l.size(); m++) {
							ContentKey k = (ContentKey) l.get(m);
							addInsBatch(insPs, k);
							addRelBatch(relPs, rel, k, m);
						}
					}
				} else {
					atrPs.setString(1, node.getKey().getEncoded());
					atrPs.setString(2, attr.getName());
					atrPs.setString(3, node.getKey().getType().getName());
					atrPs.setString(4, ContentTypeUtil.attributeToString(attr));
					atrPs.setInt(5, 0);
					atrPs.addBatch();
				}
			}

			insPs.executeBatch();
			insPs.close();

			atrPs.executeBatch();
			atrPs.close();

			relPs.executeBatch();
			relPs.close();

			/* TODO: ok...who commits...we commit for now but.... */
			conn.commit();
		} catch (Exception sqle1) {
			throw new CmsRuntimeException(sqle1);
		} finally {
			if (conn != null) {
				try {
					conn.rollback();
					conn.close();
				} catch (SQLException sqle2) {
					throw new CmsRuntimeException(sqle2);
				}
			}
		}

	}

	private void addInsBatch(PreparedStatement insPs, ContentKey key) throws SQLException {
		String id = key.getEncoded();
		insPs.setString(1, id);
		insPs.setString(2, key.getType().getName());
		insPs.setString(3, id);
		insPs.addBatch();
	}

	private void addRelBatch(PreparedStatement relPs, RelationshipI relationship, ContentKey destKey, int ordinal)
		throws SQLException {
		relPs.setString(1, relationship.getContentNode().getKey().getEncoded());
		relPs.setString(2, relationship.getName());
		relPs.setString(3, destKey.getType().getName());
		relPs.setString(4, destKey.getEncoded());
		relPs.setInt(5, ordinal);
		relPs.addBatch();
	}

	public ContentNodeI createPrototypeContentNode(ContentKey key) {
		if (!typeService.getContentTypes().contains(key.getType())) {
			return null;
		}
		return new ContentNode(this, key);
	}

	private void processNodesResultSet(ResultSet rs, Map nodeMap) throws SQLException {
		while (rs.next()) {
			ContentKey key = ContentKey.decode(rs.getString("id"));
			ContentNodeI node = new ContentNode(this, key);
			nodeMap.put(key, node);
		}
	}

	private void processAttributesResultSet(ResultSet rs, Map nodeMap) throws SQLException {
		ContentKey currKey = null;
		String currAttrName = null;
		List currValue = new ArrayList();

		while (rs.next()) {
			ContentKey key = ContentKey.decode(rs.getString(1));
			String attrName = rs.getString(2);

			if (!key.equals(currKey) || !attrName.equals(currAttrName)) {
				if (currKey != null) {
					recordAttribute(nodeMap, currKey, currAttrName, currValue);
				}
				currKey = key;
				currAttrName = attrName;
				currValue = new ArrayList();
			}

			currValue.add(rs.getString(3));
		}
		if (currKey != null) {
			recordAttribute(nodeMap, currKey, currAttrName, currValue);
		}
	}

	private void recordAttribute(Map nodeMap, ContentKey key, String attrName, List value) {
		ContentNodeI node = (ContentNodeI) nodeMap.get(key);
		AttributeI attrib = node.getAttribute(attrName);

		AttributeDefI aDef = typeService.getContentTypeDefinition(node.getKey().getType()).getAttributeDef(attrName);
		if (aDef == null) {
			return;
		}

		attrib.setValue(ContentTypeUtil.convertAttributeValues(aDef, value));
	}

	public CmsResponseI handle(CmsRequestI request) {
		// TODO distinguish create/update/delete
		for (Iterator i = request.getNodes().iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			storeContentNode(node);
		}
		return new CmsResponse();
	}

}