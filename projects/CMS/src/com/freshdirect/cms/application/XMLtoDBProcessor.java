/*
 * 
 * Created on Dec 21, 2004
 *
 */
package com.freshdirect.cms.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.framework.conf.FDRegistry;

/**
 * 
 * @author mrose
 * 
 * reads up the content from the XML stores and inserts it into the cms db schema
 *
 */
public class XMLtoDBProcessor {

	private final static boolean CREATE_DEFINITIONS = false;

	public static void main(String[] args) {

		java.util.Date start = new java.util.Date();

		try {
			XMLtoDBProcessor proc = new XMLtoDBProcessor();

			//CmsManager.getInstance().initialize();

			File log = new File("C:/tmp/cms/xml2db.log");
			FileOutputStream fos = new FileOutputStream(log, true);
			PrintWriter pw = new PrintWriter(fos);

			proc.convert(pw);

			pw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		java.util.Date end = new java.util.Date();

		System.out.println("started  " + start);
		System.out.println("finished " + end);

	}

	private final static String insertTypeDef = "insert into contenttype " + "(id, name, description) " + "values (?,?,?)";

	private final static String insertAttrDef = "insert into attributedefinition "
		+ "(id, contenttype_id, name, attributetype_code, cardinality_code, required, inheritable, label) "
		+ "values (?,?,?,?,?,?,?,?)";

	private final static String insertRelDef = "insert into relationshipdefinition "
		+ "(id, contenttype_id, name, cardinality_code, required, inheritable, navigable, label) "
		+ "values (?,?,?,?,?,?,?,?)";

	private final static String insertRelDefDest = "insert into relationshipdestination "
		+ "(id, relationshipdefinition_id, contenttype_id) "
		+ "values (?,?,?)";

	private final static String insertNode = "insert into cms.contentnode " + "(id, contenttype_id) " + "values (?,?)";

	private final static String insertAttr = "insert into cms.attribute "
		+ "(id, contentnode_id, attributedefinition_id, value, ordinal) "
		+ "values (?,?,(select id from cms.attributedefinition where contenttype_id=? and name=?),?,?)";

	private final static String insertRel = "insert into cms.relationship "
		+ "(id, relationshipdefinition_id, contentnode_id) "
		+ "values (?,(select id from cms.relationshipdefinition where contenttype_id=? and name=?),?)";

	private final static String insertRelDest = "insert into cms.relationshipcontentnode "
		+ "(id, relationship_id, contentnode_id, ordinal) "
		+ "values (?,?,?,?)";

	public void convert(PrintWriter errorlog) {

		Connection conn = null;

		ContentServiceI repository = CmsManager.getInstance();

		try {
			conn = getConnection();

			/* BEGIN SAVE DEFs */
			if (CREATE_DEFINITIONS) {
				//
				// first insert the content type definitions and the attribute definitions
				//
				PreparedStatement psInsertTypeDef = conn.prepareStatement(insertTypeDef);
				PreparedStatement psInsertAttrDef = conn.prepareStatement(insertAttrDef);

				System.out.println("XMLtoDBProcessor.convert() " + repository.getName());
				for (Iterator i = repository.getTypeService().getContentTypes().iterator(); i.hasNext();) {
					ContentType cType = (ContentType) i.next();
					ContentTypeDefI cTypeDef = repository.getTypeService().getContentTypeDefinition(cType);

					System.out.println("Inserting definition of ContentType " + cType.getName());

					psInsertTypeDef.clearParameters();
					psInsertTypeDef.setString(1, cTypeDef.getName());
					psInsertTypeDef.setString(2, cTypeDef.getName());
					psInsertTypeDef.setString(3, "definition of " + cTypeDef.getName() + " ContentType");
					try {
						psInsertTypeDef.executeUpdate();
					} catch (Exception ex) {
					}

					//
					// find the attribute definitions
					//
					Set aDefs = new HashSet();
					for (Iterator j = cTypeDef.getSelfAttributeDefs().iterator(); j.hasNext();) {

						AttributeDefI aDef = (AttributeDefI) j.next();
						if (aDef instanceof RelationshipDefI)
							continue;

						int id = getNextSequenceNumber(conn);

						System.out.println("Inserting definition of attribute "
							+ aDef.getName()
							+ " of ContentType "
							+ cType.getName());

						psInsertAttrDef.clearParameters();
						psInsertAttrDef.setInt(1, id);
						psInsertAttrDef.setString(2, cType.getName());
						psInsertAttrDef.setString(3, aDef.getName());
						psInsertAttrDef.setString(4, aDef.getAttributeType().getName());
						psInsertAttrDef.setString(5, aDef.getCardinality().getName());
						psInsertAttrDef.setString(6, aDef.isRequired() ? "T" : "F");
						psInsertAttrDef.setString(7, aDef.isInheritable() ? "T" : "F");
						psInsertAttrDef.setString(8, aDef.getLabel());
						psInsertAttrDef.executeUpdate();

					}

				}

				psInsertAttrDef.close();
				psInsertTypeDef.close();

				//
				// then go back and insert the relationship definitions
				//
				PreparedStatement psInsertRelDef = conn.prepareStatement(insertRelDef);
				PreparedStatement psInsertRelDefDest = conn.prepareStatement(insertRelDefDest);

				for (Iterator i = repository.getTypeService().getContentTypes().iterator(); i.hasNext();) {
					ContentType cType = (ContentType) i.next();
					ContentTypeDefI cTypeDef = repository.getTypeService().getContentTypeDefinition(cType);

					for (Iterator j = cTypeDef.getSelfAttributeDefs().iterator(); j.hasNext();) {
						AttributeDefI aDef = (AttributeDefI) j.next();
						if (aDef instanceof RelationshipDefI) {
							RelationshipDefI rDef = (RelationshipDefI) aDef;

							int relId = getNextSequenceNumber(conn);

							System.out.println("Inserting definition of relationship "
								+ rDef.getName()
								+ " of ContentType "
								+ cType.getName());

							psInsertRelDef.clearParameters();
							psInsertRelDef.setInt(1, relId);
							psInsertRelDef.setString(2, cType.getName());
							psInsertRelDef.setString(3, rDef.getName());
							psInsertRelDef.setString(4, rDef.getCardinality().getName());
							psInsertRelDef.setString(5, rDef.isRequired() ? "T" : "F");
							psInsertRelDef.setString(6, rDef.isInheritable() ? "T" : "F");
							psInsertRelDef.setString(7, rDef.isNavigable() ? "T" : "F");
							psInsertRelDef.setString(8, rDef.getLabel());
							psInsertRelDef.executeUpdate();

							for (Iterator k = rDef.getContentTypes().iterator(); k.hasNext();) {
								ContentType destType = (ContentType) k.next();

								int relDestId = getNextSequenceNumber(conn);

								psInsertRelDefDest.clearParameters();
								psInsertRelDefDest.setInt(1, relDestId);
								psInsertRelDefDest.setInt(2, relId);
								psInsertRelDefDest.setString(3, destType.getName());
								psInsertRelDefDest.executeUpdate();
							}
						}

					}

				}

				psInsertRelDefDest.close();
				psInsertRelDef.close();

				conn.commit();
			}
			/* END SAVE DEFS */

			List workKeys = new ArrayList(repository.getContentKeys());
			//while (workKeys.size() > 100) {
			//    workKeys.remove(0);
			//}

			ContentServiceI dbContent = (ContentServiceI) FDRegistry.getInstance().getService(
				"com.freshdirect.cms.DbStoreContent",
				com.freshdirect.cms.application.ContentServiceI.class);
			int j = 0;
			for (Iterator i = workKeys.iterator(); i.hasNext();) {
				ContentKey nextKey = (ContentKey) i.next();
				System.out.println("BEG " + new java.util.Date() + " " + j++ + ": " + nextKey);
				ContentNodeI n = CmsManager.getInstance().getContentNode(nextKey);
				CmsRequest req = new CmsRequest(new CmsUser("sys"));
				req.addNode(n);
				dbContent.handle(req);
				System.out.println("END " + new java.util.Date());
			}

		} catch (Exception sqle) {
			sqle.printStackTrace();
			sqle.printStackTrace(errorlog);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqlf) {
					sqlf.printStackTrace();
				}
			}

			System.out.println("finished");

		}

	}

	static {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		javax.sql.DataSource ds = (javax.sql.DataSource) FDRegistry.getInstance().getService(
			"com.freshdirect.cms.BasicDataSource",
			javax.sql.DataSource.class);
		return ds.getConnection();
	}

	private final static String nextSequenceNumber = "select cms.system_seq.nextval from dual";

	private int getNextSequenceNumber(Connection conn) throws SQLException {

		int retval = -1;
		PreparedStatement ps = conn.prepareStatement(nextSequenceNumber);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			retval = rs.getInt(1);
		}
		rs.close();
		ps.close();
		return retval;

	}

}
