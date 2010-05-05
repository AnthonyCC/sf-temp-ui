/*
 * Created on Jan 10, 2005
 *
 */
package com.freshdirect.cms.application.service.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.AbstractTypeService;
import com.freshdirect.cms.meta.AttributeDef;
import com.freshdirect.cms.meta.BidirectionalReferenceDef;
import com.freshdirect.cms.meta.ContentTypeDef;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.meta.RelationshipDef;

/**
 * {@link com.freshdirect.cms.application.ContentTypeServiceI} implementation
 * that retrieves definitions from an RDBMS.
 * 
 * Accesses the following tables:
 * <pre>
 * cms_contenttype
 * cms_attributedefinition
 * cms_relationshipdefinition
 * cms_relationshipdestination
 * cms_lookup
 * </pre>
 */
public class DbTypeService extends AbstractTypeService implements ContentTypeServiceI {

	private DataSource dataSource = null;

	public DbTypeService() {
		super();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public void initialize() {
		Connection conn = null;
		try {
			conn = getConnection();
			
			Map<ContentType,ContentTypeDefI> defs = new HashMap<ContentType,ContentTypeDefI>();
			Set<ContentType> types = loadContentTypesFromDb(conn);
			for (ContentType type : types) {
				defs.put(type, loadContentTypeDefinitionFromDb(conn, type));
			}
			
			this.setContentTypes(defs);
			
		} catch (SQLException sqle1) {
			throw new CmsRuntimeException(sqle1);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle2) {
					throw new CmsRuntimeException(sqle2);
				}
			}
		}
		
		
	}

	private final static String contentTypesQuery = "select id, name, description from cms_contenttype";

	private Set<ContentType> loadContentTypesFromDb(Connection conn) throws SQLException {
		Set<ContentType> returnSet = new HashSet<ContentType>();

		PreparedStatement ps = conn.prepareStatement(contentTypesQuery);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			returnSet.add(ContentType.get(rs.getString("id")));
		}
		rs.close();
		ps.close();

		return returnSet;
	}

	private final static String generateIdQuery = "select generate_id "
		+ "from cms_contenttype "
		+ "where id = ?";
	
	private final static String attributeDefQuery = "select ad.id, ad.name, ad.lookup_code, ad.attributetype_code, ad.inheritable, ad.required, ad.cardinality_code, ad.label "
		+ "from cms_contenttype ct, cms_attributedefinition ad "
		+ "where ct.id=ad.contenttype_id and ct.id=? ";

	private final static String relationshipDefQuery = "select rdef.id, rdef.name, rdef.required, rdef.inheritable, rdef.navigable, rdef.cardinality_code, rdef.label, rdef.readonly "
		+ "from cms_contenttype ct, cms_relationshipdefinition rdef "
		+ "where ct.id=rdef.contenttype_id and ct.id=? "
		+ "order by ct.id, rdef.id";

	private final static String relationshipDestQuery = "select rdef.name, rdest.id, rdest.contenttype_id, rdest.reverse_attribute_name, rdest.reverse_attribute_label "
		+ "from cms_contenttype ct, cms_relationshipdefinition rdef, cms_relationshipdestination rdest "
		+ "where ct.id=rdef.contenttype_id and rdef.id=rdest.relationshipdefinition_id and ct.id=? "
		+ "order by rdef.name";

	private ContentTypeDefI loadContentTypeDefinitionFromDb(final Connection conn, ContentType type) throws SQLException {

		ContentTypeDef typeDef = new ContentTypeDef(this, type, null);

		PreparedStatement ps;
		ResultSet		  rs;
		
		ps   = conn.prepareStatement(generateIdQuery);
		ps.setString(1, type.getName());
		rs = ps.executeQuery();
		if (rs.next()) {
			typeDef.setIdGenerated(stringToBoolean(rs.getString("generate_id")));
		}
		
		ps = conn.prepareStatement(attributeDefQuery);
		ps.setString(1, type.getName());
		rs = ps.executeQuery();
		while (rs.next()) {

			String name = rs.getString("name");
			String label = rs.getString("label");
			EnumAttributeType attrType = EnumAttributeType.getEnum(rs.getString("attributetype_code"));
			boolean required = stringToBoolean(rs.getString("required"));
			boolean inheritable = stringToBoolean(rs.getString("inheritable"));

			// TODO read readOnly flag from DB typedef
			boolean readOnly = false;

			EnumCardinality cardinality = EnumCardinality.getEnum(rs.getString("cardinality_code"));

			AttributeDef def;
			String lookupCode = rs.getString("lookup_code");
			if (rs.wasNull()) {
				def = new AttributeDef(attrType, name, label, required, inheritable, readOnly, cardinality);
			} else {
				Map lookupValues = loadLookup(conn, attrType, lookupCode);
				def = new EnumDef(name, label, required, inheritable, readOnly, attrType, lookupValues);
			}
			typeDef.addAttributeDef(def);

		}
		rs.close();
		ps.close();

		ps = conn.prepareStatement(relationshipDefQuery);
		ps.setString(1, type.getName());
		rs = ps.executeQuery();
		while (rs.next()) {

			String name = rs.getString("name");

			String label = rs.getString("label");
			boolean required = stringToBoolean(rs.getString("required"));
			boolean inheritable = stringToBoolean(rs.getString("inheritable"));
			boolean navigable = stringToBoolean(rs.getString("navigable"));

			boolean readOnly = stringToBoolean(rs.getString("readonly"));

			EnumCardinality cardinality = EnumCardinality.getEnum(rs.getString("cardinality_code"));

			RelationshipDef rDef = new RelationshipDef(typeDef.getType(), name, label, required, inheritable, navigable, readOnly, cardinality);
			typeDef.addAttributeDef(rDef);

		}
		rs.close();
		ps.close();

		ps = conn.prepareStatement(relationshipDestQuery);
		ps.setString(1, type.getName());
		rs = ps.executeQuery();
                while (rs.next()) {
        
                    ContentType rDest = ContentType.get(rs.getString("contenttype_id"));
                    String reverseAttributeName = rs.getString("reverse_attribute_name");
                    String reverseAttributeLabel = rs.getString("reverse_attribute_label");
                    String name = rs.getString("name"); 
                    if (reverseAttributeName != null) {
                        RelationshipDef attributeDef = (RelationshipDef) typeDef.removeSelfAttributeDef(name);
                        BidirectionalReferenceDef br = new BidirectionalReferenceDef (attributeDef, rDest, reverseAttributeName, reverseAttributeLabel); 
                        typeDef.addAttributeDef(br);
                    } else {
                        RelationshipDef rDef = (RelationshipDef) typeDef.getSelfAttributeDef(name);
                        if (rDef != null) {
                            rDef.addContentType(rDest);
                        }
                    }
                }
		rs.close();
		ps.close();

		return typeDef;
	}

	private LinkedHashMap loadLookup(Connection conn, EnumAttributeType type, String lookupCode) throws SQLException {
		LinkedHashMap m = new LinkedHashMap();
		PreparedStatement ps = conn.prepareStatement("select code, label from cms_lookup where lookuptype_code=? order by ordinal");
		ps.setString(1, lookupCode);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Object value = ContentTypeUtil.coerce(type, rs.getString("code"));
			m.put(value, rs.getString("label"));
		}

		rs.close();
		ps.close();

		return m;
	}

	private static boolean stringToBoolean(String s) {
		if (s==null || "".equals(s.trim()))
			return false;
		char c = s.toLowerCase().charAt(0);
		return 't' == c ? true : false;
	}


}
