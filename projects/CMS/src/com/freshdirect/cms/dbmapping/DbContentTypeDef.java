package com.freshdirect.cms.dbmapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.meta.AttributeDef;
import com.freshdirect.cms.meta.ContentTypeDef;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.node.ContentNode;
import com.freshdirect.cms.util.DaoUtil;
import com.freshdirect.cms.util.DbMappingUtil;

/**
 * {@link com.freshdirect.cms.ContentTypeDefI} based on JDBC metadata.
 */
public class DbContentTypeDef extends ContentTypeDef {

	private final String query;
	private final AttributeDefI[] columnDefinitions;

	/**
	 * Create the type definition by discovering JDBC metadata from the query.
	 * 
	 * @param conn JDBC connection
	 * @param typeService type service
	 * @param mapping the table mapping defining this type
	 * @throws SQLException
	 */
	public DbContentTypeDef(Connection conn, DbMappingTypeService typeService, TableMapping mapping) throws SQLException {
		super(typeService, ContentType.get(mapping.getContentType()), "");
		this.query = mapping.getQuery();

		PreparedStatement ps = conn.prepareStatement("select * from (" + this.query + ") where 1=0");
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData metaData = rs.getMetaData();
		this.columnDefinitions = DbMappingUtil.getDefinitions(metaData);
		for (int i = 0; i < columnDefinitions.length; i++) {
			AttributeDefI def = columnDefinitions[i];
			if ("ID".equals(def.getName())) {
				columnDefinitions[i] = null;
				continue;
			}
			// FIXME silly cast
			this.addAttributeDef((AttributeDef) def);
		}

		rs.close();
		ps.close();
	}

	/**
	 * Get all keys from the repository.
	 * 
	 * @param conn JDBC connection
	 * @return Set of {@link ContentKey}
	 * @throws SQLException
	 */
	Set queryKeys(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select ID from (" + query + ")");
		ResultSet rs = ps.executeQuery();
		Set keys = new HashSet();
		while (rs.next()) {
			ContentKey key = new ContentKey(this.getType(), rs.getString(1));
			keys.add(key);
		}
		rs.close();
		ps.close();
		return keys;
	}

	/**
	 * Retrieve multiple content nodes.
	 * 
	 * @param contentService
	 * @param conn JDBC connection
	 * @param keys Set of {@link ContentKey}
	 * @return Map of {@link ContentKey} -> {@link com.freshdirect.cms.ContentNodeI}
	 * @throws SQLException
	 */
	Map loadNodes(DbMappingContentServiceI contentService, Connection conn, Set keys) throws SQLException {

		String[] idChunks = DaoUtil.chunkContentKeys(keys, false);

		Map nodeMap = new HashMap(keys.size());
		for (int i = 0; i < idChunks.length; i++) {
			process(contentService, conn, nodeMap, idChunks[i]);
		}

		return nodeMap;
	}

	/**
	 * @param contentService
	 * @param conn
	 * @param nodeMap
	 * @param ids
	 * @throws SQLException
	 */
	private void process(DbMappingContentServiceI contentService, Connection conn, Map nodeMap, String ids) throws SQLException {
		Statement stmt = conn.createStatement();
		String q = StringUtils.replace("select * from  (" + query + ") where ID IN (?)", "?", ids);
		ResultSet rs = stmt.executeQuery(q);

		while (rs.next()) {
			String id = rs.getString("ID");
			ContentKey key = new ContentKey(this.getType(), id);
			ContentNode node = new ContentNode(contentService, key);

			for (int i = 0; i < columnDefinitions.length; i++) {
				AttributeDefI def = columnDefinitions[i];
				if (def != null) {
					Object value = ContentTypeUtil.coerce(def.getAttributeType(), rs.getObject(i + 1));
					node.getAttribute(def.getName()).setValue(value);
				}
			}
			
			for (Iterator i = getSelfAttributeDefs().iterator(); i.hasNext();) {
				Object o = i.next();
				if (o instanceof DbRelationshipDef) {
					DbRelationshipDef relDef = (DbRelationshipDef) o;
					List lazyValue = relDef.loadLazyValues(contentService, key);
					node.getAttribute(relDef.getName()).setValue(lazyValue);
				}
			}

			nodeMap.put(key, node);
		}

		rs.close();
		stmt.close();

		/*
		for (Iterator i = getSelfAttributeDefs().iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof DbRelationshipDef) {
				DbRelationshipDef relDef = (DbRelationshipDef) o;
				relDef.loadValues(conn, this.getType(), nodeMap, ids);
			}
		}
		*/

	}
}
