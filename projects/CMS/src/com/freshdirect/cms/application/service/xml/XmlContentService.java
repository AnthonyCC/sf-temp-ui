package com.freshdirect.cms.application.service.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Category;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.ResourceInfoServiceI;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * XML-based in-memory implementation of {@link com.freshdirect.cms.application.ContentServiceI}.
 * 
 * A {@link com.freshdirect.cms.application.service.xml.CmsNodeHandler} implementation provides parsing.
 * Type definitions are obtained from a {@link com.freshdirect.cms.application.ContentTypeServiceI}.
 * 
 * @see com.freshdirect.cms.application.service.xml.FlexContentHandler
 */
public class XmlContentService extends SimpleContentService implements ContentServiceI {

	private final Category LOGGER = LoggerFactory.getInstance(XmlContentService.class);	
	private CmsNodeHandler handler = null;
	
	
	protected XmlContentService(ContentTypeServiceI typeService) {
	    super(typeService);
	}
	
	/**
	 * @see #XmlContentService(ContentTypeServiceI, CmsNodeHandler, String, ResourceInfoServiceI)
	 */
	public XmlContentService(ContentTypeServiceI typeService, CmsNodeHandler nodeHandler, String resourceFiles) {
	    super(typeService);
	    handler = nodeHandler;
	    init(nodeHandler, resourceFiles, null);
	}

	/**
	 * 
	 * @param typeService {@link ContentTypeServiceI} for type definitions
	 * @param nodeHandler {@link CmsNodeHandler} implementation to use for parsing
	 * @param resourceFiles Comma separated list of resource locations
	 * @param resourceInfoService optional {@link ResourceInfoServiceI} to notify of meta-data parsed.
	 */
	public XmlContentService(
		ContentTypeServiceI typeService,
		CmsNodeHandler nodeHandler,
		String resourceFiles,
		ResourceInfoServiceI resourceInfoService) {

		super(typeService);
		init(nodeHandler, resourceFiles, resourceInfoService);
	}

        protected void init(CmsNodeHandler nodeHandler, String resourceFiles, ResourceInfoServiceI resourceInfoService) {
            nodeHandler.setContentService(this);
            nodeHandler.setResourceInfoService(resourceInfoService);
            StringTokenizer tok = new StringTokenizer(resourceFiles, ",");
            // Map allNodes = new HashMap();
            while (tok.hasMoreTokens()) {
                String location = tok.nextToken();
                Map<ContentKey, ContentNodeI> nodes = loadNodes(nodeHandler, location);
                // allNodes.putAll(nodes);
                putNodes(nodes);
            }
        }

	private Map<ContentKey, ContentNodeI> loadNodes(CmsNodeHandler nodeHandler, String location) {
		InputStream storeDataStream = null;
		try {
			LOGGER.info("Loading: " + location);

			storeDataStream = ResourceUtil.openResource(location);

			if (location.endsWith(".zip")) {
				storeDataStream = new ZipInputStream(storeDataStream);
				((ZipInputStream) storeDataStream).getNextEntry();
			} else if (location.endsWith(".gz")) {
				storeDataStream = new GZIPInputStream(storeDataStream);
			}

			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			SAXParser parser = factory.newSAXParser();

			nodeHandler.setContentService(this);

			InputSource dataInputSource = new InputSource(storeDataStream);
			//dataInputSource.setEncoding("ISO-8859-1");
			dataInputSource.setEncoding("UTF-8");
			parser.parse(dataInputSource, nodeHandler);

			return nodeHandler.getContentNodes();

		} catch (IOException ioe) {
			throw new CmsRuntimeException(ioe);
		} catch (SAXException se) {
			throw new CmsRuntimeException(se);
		} catch (ParserConfigurationException e) {
			throw new CmsRuntimeException(e);
		} finally {
			try {
				if (storeDataStream != null)
					storeDataStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new CmsRuntimeException(ex);
			}
		}
	}
}