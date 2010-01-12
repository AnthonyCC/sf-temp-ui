package com.freshdirect.cms.application.service.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.framework.conf.ResourceUtil;

/**
 * XML-based implementation of {@link com.freshdirect.cms.application.ContentTypeServiceI}.
 * 
 * @see com.freshdirect.cms.application.service.xml.ContentTypeContentHandler
 */
public class XmlTypeService implements ContentTypeServiceI {

	/** Map of {@link ContentType} -> {@link ContentTypeDefI} */
	private Map<ContentType,ContentTypeDefI> defsByType;

	/**
	 * @param location resource location
	 */
	public XmlTypeService(String location) {
		InputStream stream = null;
		try {
			stream = ResourceUtil.openResource(location);

			ContentTypeContentHandler typeHandler = new ContentTypeContentHandler(this);
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);
			SAXParser parser = factory.newSAXParser();

			InputSource dataInputSource = new InputSource(stream);
			//dataInputSource.setEncoding("ISO-8859-1");
			parser.parse(dataInputSource, typeHandler);

			defsByType = typeHandler.getContentTypes();
		} catch (IOException ioe) {
			throw new CmsRuntimeException(ioe);
		} catch (SAXException se) {
			throw new CmsRuntimeException(se);
		} catch (ParserConfigurationException e) {
			throw new CmsRuntimeException(e);
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new CmsRuntimeException(ex);
			}
		}
	}

	public Set<ContentType> getContentTypes() {
		return Collections.unmodifiableSet(defsByType.keySet());
	}

	public Set<ContentTypeDefI> getContentTypeDefinitions() {
		return new HashSet<ContentTypeDefI>(defsByType.values());
	}

	public ContentTypeDefI getContentTypeDefinition(ContentType type) {
		return (ContentTypeDefI) defsByType.get(type);
	}

	public String generateUniqueId(ContentType type) {
		ContentTypeDefI def = getContentTypeDefinition(type);
		if (def == null || !def.isIdGenerated()) {
			return null;
		}
		
		// generate a UUID
		UUIDGenerator idGenerator = UUIDGenerator.getInstance();
		UUID          id          = idGenerator.generateRandomBasedUUID();
		
		return id.toString();
	}

	public ContentKey generateUniqueContentKey(ContentType type) {
		String id = generateUniqueId(type);
		
		return id == null ? null
				          : new ContentKey(type, id);
	}

}