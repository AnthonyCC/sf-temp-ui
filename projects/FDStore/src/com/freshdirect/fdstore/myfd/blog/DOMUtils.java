package com.freshdirect.fdstore.myfd.blog;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMUtils {

	private static class NullErrorHandler implements ErrorHandler {
		public void error(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
			throw exception; 
		}
		public void warning(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
			throw exception;
		}
		public void fatalError(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
			throw exception;
		}
	}
	
	public static Document urlToNode(String url) throws RuntimeException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();			
		DocumentBuilder builder;
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e.getMessage());			
		}
		
		builder.setErrorHandler(new NullErrorHandler());
				
		try {
			return builder.parse(url);
		} catch (SAXException e) {
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static Document stringToNode(String html) throws RuntimeException {
		UserAgentContext context = new SimpleUserAgentContext();
		DocumentBuilderImpl builder = new DocumentBuilderImpl(context);
			
		builder.setErrorHandler(new NullErrorHandler());
		
		StringReader reader = new StringReader(html);			
		InputSource s = new InputSource(reader);			
		try {
			Document document = builder.parse(s);
			return document;
		} catch (SAXException e) {
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
				
	}
	
	public static String nodeToString(Node node) throws RuntimeException {
		Source source = new DOMSource(node);
        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        TransformerFactory tfactory = TransformerFactory.newInstance();
       
		try {
			Transformer transformer = tfactory.newTransformer();
		    transformer.setOutputProperty("method","html");
	        transformer.transform(source, result);
	        return stringWriter.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e.getMessage());
		} catch (TransformerException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
