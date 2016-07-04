package com.freshdirect.cms;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.xml.XSLTransformer;

import junit.framework.TestCase;

public class XmlToDbContentTypeDefTest extends TestCase {

	private static final String XSLT_RESOURCE= "com/freshdirect/cms/resource/XmlToDbDataDef.xsl";
	
	private static final String XML_DATA_DEF_RESOURCE = "classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml";
	
	/**
	 *  Test the XSLT transformation, and if the result can be loaded into the database.
	 */
	public void testLoadIntoDatabase() throws IOException, TransformerException  {
		final String          xmlDataDefPath = ResourceUtil.readResource(XML_DATA_DEF_RESOURCE);
        final XSLTransformer  xslTransformer = new XSLTransformer();

		final String          dbDataDef = xslTransformer.transform(xmlDataDefPath, XSLT_RESOURCE);

		System.out.println(dbDataDef);
	}
}
