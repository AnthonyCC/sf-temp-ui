package com.freshdirect.cms.dbmapping;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Factory;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.meta.RelationshipDef;
import com.freshdirect.framework.util.LazyInstanceProxy;

/**
 * Relationship definition based on a {@link com.freshdirect.cms.dbmapping.RelationshipMapping}.
 */
public class DbRelationshipDef extends RelationshipDef {

	private final ContentType destinationType;
	private final String query;

	/**
	 * @param mapping relationship mapping descriptor
	 */
	public DbRelationshipDef(RelationshipMapping mapping) {
		super(mapping.getName(), mapping.getLabel(), false, false, mapping.isNavigable(), true, EnumCardinality.MANY);
		this.query = mapping.getQuery();
		this.destinationType = ContentType.get(mapping.getDestinationContentType());
		addContentType(destinationType);
	}

	List loadLazyValues(DbMappingContentServiceI contentService, ContentKey key) {
		return (List) LazyInstanceProxy.newInstance(List.class, new RelationshipFactory(contentService, key));
	}

	private final class RelationshipFactory implements Factory, Serializable {

		private final DbMappingContentServiceI contentService;
		private final ContentKey key;

		public RelationshipFactory(DbMappingContentServiceI contentService, ContentKey key) {
			this.contentService = contentService;
			this.key = key;
		}

		public Object create() {
			Connection conn = null;
			try {
				conn = contentService.getDataSource().getConnection();

				return loadValues(conn, key);

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
	}

	private List loadValues(Connection conn, ContentKey key) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select DESTINATION_ID from (" + query + ") where SOURCE_ID=?");
		ps.setString(1, key.getId());
		ResultSet rs = ps.executeQuery();
		List keys = new ArrayList();
		while (rs.next()) {
			ContentKey destKey = new ContentKey(destinationType, rs.getString(1));
			keys.add(destKey);
		}
		rs.close();
		ps.close();
		return keys;
	}

	/*
	 void loadValues(Connection conn, ContentType sourceType, Map nodeMap, String ids) throws SQLException {

	 String q = StringUtils.replace("select SOURCE_ID, DESTINATION_ID from (" + query + ") where SOURCE_ID IN (?)", "?", ids);
	 Statement stmt = conn.createStatement();
	 ResultSet rs = stmt.executeQuery(q);
	 while (rs.next()) {
	 ContentKey sourceKey = new ContentKey(sourceType, rs.getString(1));
	 ContentKey destKey = new ContentKey(destinationType, rs.getString(2));
	 ContentNodeI node = (ContentNodeI) nodeMap.get(sourceKey);
	 AttributeI a = node.getAttribute(this.getName());
	 List l = (List) a.getValue();
	 if (l == null) {
	 l = new ArrayList();
	 a.setValue(l);
	 }
	 l.add(destKey);
	 }

	 rs.close();
	 stmt.close();
	 }
	 */

}
