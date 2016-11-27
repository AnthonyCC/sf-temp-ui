/*
 * Created on Jan 10, 2005
 *
 */
package com.freshdirect.cms.application.service.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

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
import com.freshdirect.framework.util.log.LoggerFactory;

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
	private static final Logger LOGGER = LoggerFactory.getInstance(DbTypeService.class); 
	
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
                        // this will only work when all destination type has reverse attribute name, or none of them.
                        RelationshipDef attributeDef = (RelationshipDef) typeDef.removeSelfAttributeDef(name);
                        BidirectionalReferenceDef br = null;
                        if (attributeDef instanceof BidirectionalReferenceDef) {
                            br =  (BidirectionalReferenceDef) attributeDef;
                        } else {
                            br = new BidirectionalReferenceDef (attributeDef);
                        }
                        br.addOtherSide(rDest, reverseAttributeName, reverseAttributeLabel);
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

	private Map loadLookup(Connection conn, EnumAttributeType type, String lookupCode) throws SQLException {
		Map m = new LinkedHashMap();
		String lookupSource = null;
		String query = null;
		
		try{
			PreparedStatement lookupTypeStmt = conn.prepareStatement("select code, name, lookup_source, query from cms_lookuptype where code=?");
			lookupTypeStmt.setString(1, lookupCode);
			ResultSet typeResultSet = lookupTypeStmt.executeQuery();
			while(typeResultSet.next()){
				lookupSource = typeResultSet.getString("lookup_source");
				query = typeResultSet.getString("query");
			}
			closeQuietly(typeResultSet);
			closeQuietly(typeResultSet);
		} catch(Exception e){
			System.out.println(query);
			e.printStackTrace();
			LOGGER.error("Error Executing Query:"+query,e);
		}
		
		
		if("EXTERNAL".equals(lookupSource) && query != null){
			System.out.println(query);
			LOGGER.error("Query:"+query);
			PreparedStatement ps = null;
			ResultSet rs = null;
			try{
				ps = conn.prepareStatement(query);
				rs = ps.executeQuery();
				while(rs.next()){
					Object value = ContentTypeUtil.coerce(type, rs.getString(1));
					m.put(value, rs.getString(2));
				}
			} catch(Exception e){
				e.printStackTrace();
				LOGGER.error("Error Executing Query:"+query, e);
			} finally{
				closeQuietly(rs);
				closeQuietly(ps);
			}
		} else {
			PreparedStatement ps = conn.prepareStatement("select code, label from cms_lookup where lookuptype_code=? order by ordinal");
			ps.setString(1, lookupCode);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Object value = ContentTypeUtil.coerce(type, rs.getString("code"));
				m.put(value, rs.getString("label"));
			}
			closeQuietly(rs);
			closeQuietly(ps);	
		}
		return m;
	}
	
	public static void closeQuietly(Connection connection){
		try{
			connection.close();
		} catch(Exception e){
			LOGGER.error("Error closing connection", e);
		}
	}
	
	public static void closeQuietly(ResultSet resultSet){
		try{
			resultSet.close();
		} catch(Exception e){
			LOGGER.error("Error closing resultset", e);
		}
	}
	
	public static void closeQuietly(Statement statement){
		try{
			statement.close();
		} catch(Exception e){
			LOGGER.error("Error closing statement", e);
		}
	}
	
	private static boolean stringToBoolean(String s) {
		if (s==null || "".equals(s.trim()))
			return false;
		char c = s.toLowerCase().charAt(0);
		return 't' == c ? true : false;
	}


}
