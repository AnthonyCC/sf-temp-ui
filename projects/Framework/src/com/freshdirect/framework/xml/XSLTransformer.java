/**
 * 
 * XSLTransformer.java
 * Created Dec 4, 2002
 */
package com.freshdirect.framework.xml;

/**
 *
 *  @author knadeem
 */
import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.log4j.*;
import com.freshdirect.framework.util.log.LoggerFactory;


public class XSLTransformer {
	
	private static Category LOGGER = LoggerFactory.getInstance( XSLTransformer.class );
	
	/**
	 * Transforms an XML document according to an XSLT definition.
	 * 
	 * @param xml the contents of the XML document to transform
	 * @param xslPath a classpath, pointing the a resource that contains the
	 *        XSLT to use for transformation. the delimiter in the classpath
	 *        is "/"
	 * @return the result of the XSLT transformation on the supplied XML document
	 * @throws TransformerException
	 * @see ClassLoader#getResource(java.lang.String)
	 */
	public String transform(String xml, String xslPath) throws TransformerException {
		
		StringWriter mailBody = new StringWriter();
		XslPathResolver resolver = new XslPathResolver(xslPath.substring(0, xslPath.lastIndexOf("/")));
		TransformerFactory tFactory = TransformerFactory.newInstance();
		tFactory.setURIResolver(resolver);
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(xslPath);
		
		LOGGER.debug("Found stream "+stream+" for xslPath "+xslPath);
		
		Transformer transformer = tFactory.newTransformer(new StreamSource(stream));
		transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(mailBody));

		return mailBody.toString();
	}
	
	
	private static class XslPathResolver implements URIResolver {

		private String xslRoot = null;

		public XslPathResolver(String xslRoot) {
			this.xslRoot = xslRoot;
		}

		public Source resolve(String href, String base) throws TransformerException {
			try {
				return new StreamSource( this.getClass().getClassLoader().getResourceAsStream(xslRoot+"/"+href) );
			} catch (Exception e) {
				throw new TransformerException(e);
			}
		}
	}

}
