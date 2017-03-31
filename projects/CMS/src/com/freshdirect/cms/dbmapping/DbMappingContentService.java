package com.freshdirect.cms.dbmapping;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsResponseI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.service.AbstractContentService;
import com.freshdirect.cms.util.CollectionUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Content service that exposes arbitrary JDBC SQL queries as content objects.
 * Definitions are automatically discovered based on JDBC metadata.
 * 
 * @see com.freshdirect.cms.dbmapping.TableMapping
 * @see com.freshdirect.cms.dbmapping.RelationshipMapping
 */
public class DbMappingContentService extends AbstractContentService implements DbMappingContentServiceI {

    private final Category LOGGER = LoggerFactory.getInstance(DbMappingContentService.class);

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

	@Override
	public Set<ContentKey> getContentKeys(DraftContext draftContext) {
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
            LOGGER.error(MessageFormat.format("Error getting content keys for context {0} and content type definitions {1}", draftContext, typeService.getContentTypeDefinitions()), e);
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

	@Override
	public Set<ContentKey> getContentKeysByType(ContentType type, DraftContext draftContext) {
		DbContentTypeDef def = (DbContentTypeDef) typeService.getContentTypeDefinition(type);
		if (def == null) {
			return Collections.EMPTY_SET;
		}
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return def.queryKeys(conn);
		} catch (SQLException e) {
            LOGGER.error(MessageFormat.format("Error getting content keys by type for context {0} and content type {1}", draftContext, type), e);
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

	@Override
	public Set<ContentKey> getParentKeys(ContentKey key, DraftContext draftContext) {
		return Collections.EMPTY_SET;
	}

	@Override
	public ContentNodeI getContentNode(ContentKey key, DraftContext draftContext) {
		Set<ContentKey> s = new HashSet<ContentKey>();
		s.add(key);
		return (ContentNodeI) getContentNodes(s, draftContext).get(key);
	}

	@Override
	public Map<ContentKey, ContentNodeI> getContentNodes(Set<ContentKey> keys, DraftContext draftContext) {

		/** Map of ContentType -> Set of ContentKey */
		Map<ContentType, Set<ContentKey>> keysByType = new HashMap<ContentType, Set<ContentKey>>();
		for (Iterator<ContentKey> i = keys.iterator(); i.hasNext();) {
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
            LOGGER.error(MessageFormat.format("Error getting content nodes for context {0} and content keys {1}", draftContext, keys), e);
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

	@Override
	public ContentNodeI createPrototypeContentNode(ContentKey key, DraftContext draftContext) {
		return null;
	}

	@Override
	public CmsResponseI handle(CmsRequestI request) {
		return null;
	}

	@Override
	public ContentTypeServiceI getTypeService() {
		return this.typeService;
	}

	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}

	@Override
	public ContentNodeI getRealContentNode(ContentKey key, DraftContext draftContext) {
		return null;
	}

	
}
