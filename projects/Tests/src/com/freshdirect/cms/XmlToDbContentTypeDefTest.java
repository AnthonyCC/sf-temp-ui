package com.freshdirect.cms;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.xml.XSLTransformer;

public class XmlToDbContentTypeDefTest extends DbTestCaseSupport {

	private static String XSLT_RESOURCE= "com/freshdirect/cms/resource/XmlToDbDataDef.xsl";
	
	private static String XML_DATA_DEF_RESOURCE = "classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml";
	
	private XSLTransformer	xslTransformer;
	
	public XmlToDbContentTypeDefTest(String name) {
		super(name);
	}

	public void setUp() throws Exception {
		super.setUp();

		xslTransformer = new XSLTransformer();
	}
	
	public void tearDown() throws Exception {
		super.tearDown();
	}
		
	/**
	 *  Test the XSLT transformation, and if the result can be loaded into the database.
	 */
	public void testLoadIntoDatabase() throws IOException, TransformerException, SQLException  {
		String    xmlDataDefPath;
		String    dbDataDef  = null;
		
		xmlDataDefPath = ResourceUtil.readResource(XML_DATA_DEF_RESOURCE);
		dbDataDef = xslTransformer.transform(xmlDataDefPath, XSLT_RESOURCE);
		executeSqlScript(dbDataDef);
	}

	protected String getSchema() {
		return "CMS";
	}

	protected String[] getAffectedTables() {
		return new String[] { "cms.contenttype",
				   			  "cms.lookuptype",
				 			  "cms.lookup",
			     			  "cms.relationshipdefinition",
			     			  "cms.relationshipdestination",
				 			  "cms.attributedefinition"
			   				};
	}
}
