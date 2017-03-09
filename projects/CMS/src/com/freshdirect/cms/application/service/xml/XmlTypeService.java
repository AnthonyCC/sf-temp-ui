package com.freshdirect.cms.application.service.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.AbstractTypeService;
import com.freshdirect.cms.util.XmlResourceUtil;

/**
 * XML-based implementation of {@link com.freshdirect.cms.application.ContentTypeServiceI}.
 * 
 * @see com.freshdirect.cms.application.service.xml.ContentTypeContentHandler
 */
public class XmlTypeService extends AbstractTypeService implements ContentTypeServiceI {

	/**
	 * @param location resource location
	 */
	public XmlTypeService(String location) {
		InputStream stream = null;
		try {
		    stream = XmlResourceUtil.openXmlResource(location);

			initFromStream(stream);

		} catch (MalformedURLException e) {
		    throw new CmsRuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new CmsRuntimeException(e);
        } catch (IOException e) {
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
    
    
	/**
	 * Convenience method to open XML definiton files directly from file
	 * Also this method avoids using Hivemind.
	 * 
	 * @param defFile
	 */
	public XmlTypeService(final File defFile) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(defFile);

			initFromStream(stream);

		} catch (IOException ioe) {
			throw new CmsRuntimeException(ioe);
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
	
	
	protected void initFromStream(InputStream stream) throws CmsRuntimeException {
		try {
			ContentTypeContentHandler typeHandler = new ContentTypeContentHandler(this);
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);
			SAXParser parser = factory.newSAXParser();

			InputSource dataInputSource = new InputSource(stream);
			//dataInputSource.setEncoding("ISO-8859-1");
			parser.parse(dataInputSource, typeHandler);

			setContentTypes(typeHandler.getContentTypes());
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
}