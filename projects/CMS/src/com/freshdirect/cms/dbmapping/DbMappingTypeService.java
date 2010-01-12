package com.freshdirect.cms.dbmapping;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.util.CollectionUtil;

/**
 * Type service that automatically discovers definitions based on
 * JDBC metadata.
 */
public class DbMappingTypeService implements ContentTypeServiceI {

	/** Map of {@link ContentType} -> {@link DbContentTypeDef} */
	private final Map<ContentType,DbContentTypeDef> typeDefinitions = new HashMap<ContentType,DbContentTypeDef>();

	/** Map of {@link ContentType} (destination) -> List of {@link DbRelationshipDef} */
	private final Map<ContentType,DbRelationshipDef> parentRelationshipMappings = new HashMap<ContentType,DbRelationshipDef>();

	/**
	 * Create type service based on a data source and mappings.
	 * 
	 * @param dataSource JDBC data source
	 * @param mappings List of {@link TableMapping}, {@link RelationshipMapping}
	 */
	@SuppressWarnings("unchecked")
	public DbMappingTypeService(DataSource dataSource, List mappings) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();

			List l = new ArrayList(mappings);
			// process tables
			for (Iterator i = l.iterator(); i.hasNext();) {
				Object mapping = i.next();
				if (mapping instanceof TableMapping) {
					processTableMapping(conn, (TableMapping) mapping);
					i.remove();
				}
			}

			// process relationships
			for (Iterator i = l.iterator(); i.hasNext();) {
				Object mapping = i.next();
				if (mapping instanceof RelationshipMapping) {
					processRelationshipMapping((RelationshipMapping) mapping);
					i.remove();
				}
			}

			if (!l.isEmpty()) {
				throw new IllegalArgumentException("Unknown mappings " + l);
			}

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

	private void processTableMapping(Connection conn, TableMapping mapping) throws SQLException {
		DbContentTypeDef def = new DbContentTypeDef(conn, this, mapping);
		this.typeDefinitions.put(def.getType(), def);
	}

	private void processRelationshipMapping(RelationshipMapping mapping) {
		ContentType sourceType = ContentType.get(mapping.getSourceContentType());
		DbContentTypeDef typeDef = (DbContentTypeDef) typeDefinitions.get(sourceType);
		if (typeDef == null) {
			throw new IllegalArgumentException("No type definition for " + sourceType);
		}

		DbRelationshipDef relDef = new DbRelationshipDef(mapping);
		typeDef.addAttributeDef(relDef);

		for (ContentType destType :  relDef.getContentTypes()) {
			CollectionUtil.addToMapOfLists(parentRelationshipMappings, destType, mapping);
		}
	}

	public Set<ContentType> getContentTypes() {
		return typeDefinitions.keySet();
	}

	public Set<DbContentTypeDef> getContentTypeDefinitions() {
		return new HashSet<DbContentTypeDef>(typeDefinitions.values());
	}

	public ContentTypeDefI getContentTypeDefinition(ContentType type) {
		return (ContentTypeDefI) typeDefinitions.get(type);
	}

	public String generateUniqueId(ContentType type) {
		// read-only service: unsupported
		return null;
	}

	public ContentKey generateUniqueContentKey(ContentType type) {
		// read-only service: unsupported
		return null;
	}

}
