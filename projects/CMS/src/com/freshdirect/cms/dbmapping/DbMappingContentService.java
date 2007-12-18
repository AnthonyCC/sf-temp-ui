package com.freshdirect.cms.dbmapping;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.AbstractContentService;
import com.freshdirect.cms.util.CollectionUtil;

/**
 * Content service that exposes arbitary JDBC SQL queries as content objects.
 * Definitions are automatically discovered based on JDBC metadata.
 * 
 * @see com.freshdirect.cms.dbmapping.TableMapping
 * @see com.freshdirect.cms.dbmapping.RelationshipMapping
 */
public class DbMappingContentService extends AbstractContentService implements DbMappingContentServiceI {

	private final DataSource dataSource;
	private final DbMappingTypeService typeService;

	/**
	 * Create and initialize the service.
	 * 
	 * @param dataSource JDBC data source
	 * @param mappings List of {@link TableMapping}, {@link RelationshipMapping}
	 */
	public DbMappingContentService(DataSource dataSource, List mappings) {
		this.dataSource = dataSource;
		this.typeService = new DbMappingTypeService(dataSource, mappings);
	}

	public Set getContentKeys() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();

			Set keys = new HashSet();
			Set typeDefs = typeService.getContentTypeDefinitions();
			for (Iterator i = typeDefs.iterator(); i.hasNext();) {
				DbContentTypeDef def = (DbContentTypeDef) i.next();
				keys.addAll(def.queryKeys(conn));
			}
			return keys;

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

	public Set getContentKeysByType(ContentType type) {
		DbContentTypeDef def = (DbContentTypeDef) typeService.getContentTypeDefinition(type);
		if (def == null) {
			return Collections.EMPTY_SET;
		}
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return def.queryKeys(conn);
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
		return Collections.EMPTY_SET;
	}

	public ContentNodeI getContentNode(ContentKey key) {
		Set s = new HashSet();
		s.add(key);
		return (ContentNodeI) getContentNodes(s).get(key);
	}

	public Map getContentNodes(Set keys) {

		/** Map of ContentType -> Set of ContentKey */
		Map keysByType = new HashMap();
		for (Iterator i = keys.iterator(); i.hasNext();) {
			ContentKey key = (ContentKey) i.next();
			CollectionUtil.addToMapOfSets(keysByType, key.getType(), key);
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			Map nodeMap = new HashMap(keys.size());
			for (Iterator i = keysByType.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry) i.next();
				ContentType type = (ContentType) e.getKey();
				Set typeKeys = (Set) e.getValue();
				DbContentTypeDef def = (DbContentTypeDef) typeService.getContentTypeDefinition(type);
				if (def == null) {
					continue;
				}
				Map nodes = def.loadNodes(this, conn, typeKeys);

				nodeMap.putAll(nodes);
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

	public ContentNodeI createPrototypeContentNode(ContentKey key) {
		return null;
	}

	public CmsResponseI handle(CmsRequestI request) {
		return null;
	}

	public ContentTypeServiceI getTypeService() {
		return this.typeService;
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

}
