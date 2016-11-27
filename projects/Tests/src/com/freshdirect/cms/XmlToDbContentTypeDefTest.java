package com.freshdirect.cms;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.xml.XSLTransformer;

import junit.framework.TestCase;

public class XmlToDbContentTypeDefTest extends TestCase {

    private static final String XSLT_RESOURCE = "com/freshdirect/cms/resource/XmlToDbDataDef.xsl";

    private static final String XML_DATA_DEF_RESOURCE = "classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml";

    /**
     * Test the XSLT transformation, and if the result can be loaded into the database.
     */
    public void testLoadIntoDatabase() throws IOException, TransformerException, SQLException {
        final XSLTransformer xslTransformer = new XSLTransformer();

        final String xmlDataDefPath;
        final String dbDataDef;

        xmlDataDefPath = ResourceUtil.readResource(XML_DATA_DEF_RESOURCE);
        dbDataDef = xslTransformer.transform(xmlDataDefPath, XSLT_RESOURCE);
        System.out.println(dbDataDef);
    }
}
