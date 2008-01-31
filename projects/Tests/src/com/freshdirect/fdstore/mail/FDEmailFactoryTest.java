package com.freshdirect.fdstore.mail;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.regex.Pattern;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.xml.XSLTransformer;
import com.freshdirect.mail.MailName;

public class FDEmailFactoryTest extends TestCase implements MailName {
	
	public static final Logger LOG = Logger.getLogger(FDEmailFactoryTest.class);
	
	/**
	 * Fulfill a customer info instance 
	 * @param isChefsTable
	 * @param isHtmlEmail
	 */
	FDCustomerInfo createCustomerInfo(final boolean isChefsTable, final boolean isHtmlEmail) {
		FDCustomerInfo custInfo = new FDCustomerInfo("Ubul", "Vajas");

		custInfo.setHtmlEmail(isHtmlEmail);
		custInfo.setEmailAddress("ubul@vajas.com");
		custInfo.setDepotCode("123456");
		custInfo.setChefsTable(isChefsTable);
		custInfo.setCustomerServiceContact(isChefsTable ? "1-866-511-1240" : "1-212-796-8002");
		
		return custInfo;
	}



	
	public void testAuthorizationFailedHtml() throws TransformerException {
		final boolean isChefsTable = true;
		Pattern p;

		
		FDCustomerInfo custInfo = createCustomerInfo(isChefsTable, true);

		// template parameters
		Date stDate = new Date();
		Date endDate = new Date(stDate.getTime() + 600000);
		
		// get email template 
		XMLEmailI email = FDEmailFactoryHack.getInstance().createAuthorizationFailedEmail(custInfo, "123456", stDate, endDate, endDate);

		// compile mail template to string content
		String xml = email.getXML();
		String xslPath = email.getXslPath();
	
		LOG.info("testAuthorizationFailedHtml: xml="+xml+" xslPath:"+xslPath);
		String mailBody = transform(xml, xslPath);
		
		/// debug System.out.println(mailBody);
		
		// check customer name
		p = Pattern.compile("\\<b\\>Dear " + custInfo.getFirstName() + "\\</b\\>,");
		assertTrue(p.matcher(mailBody).find());

		// check customer service contact number in email
		p = Pattern.compile("please\\scall\\scustomer\\sservice\\sat\\s" + custInfo.getCustomerServiceContact() + "\\.");
		assertTrue(p.matcher(mailBody).find());
	}
	
	
	public String transform(String xml, String xslPath) throws TransformerException {
		
		StringWriter mailBody = new StringWriter();
		XslPathResolver resolver = new XslPathResolver(xslPath.substring(0, xslPath.lastIndexOf("/")));
		TransformerFactory tFactory = TransformerFactory.newInstance();
		tFactory.setURIResolver(resolver);
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(xslPath);
		
		LOG.info("Found stream "+stream+" for xslPath "+xslPath);
		
		Transformer transformer = tFactory.newTransformer(new StreamSource(stream));
		transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(mailBody));

		return mailBody.toString();
	}

	
	static class XslPathResolver implements URIResolver {

		private String xslRoot = null;

		public XslPathResolver(String xslRoot) {
			this.xslRoot = xslRoot;
		}

		public Source resolve(String href, String base) throws TransformerException {
			try {
				LOG.info("resolve href:"+href+" base:"+base);
				return new StreamSource( this.getClass().getClassLoader().getResourceAsStream(xslRoot+"/"+href) );
			} catch (Exception e) {
				throw new TransformerException(e);
			}
		}
	}


	
	
	public void testAuthorizationFailedPlain() throws TransformerException {
		final boolean isChefsTable = true;
		Pattern p;
		
		FDCustomerInfo custInfo = createCustomerInfo(isChefsTable, false);

		// template parameters
		Date stDate = new Date();
		Date endDate = new Date(stDate.getTime() + 600000);
		
		// get email template 
		XMLEmailI email = FDEmailFactoryHack.getInstance().createAuthorizationFailedEmail(custInfo, "123456", stDate, endDate, endDate);

		// compile mail template to string content
		String mailBody = new XSLTransformer().transform(email.getXML(), email.getXslPath());
		
		/// debug System.out.println(mailBody);
		
		// check customer name
		p = Pattern.compile("Dear " + custInfo.getFirstName() + ",");
		assertTrue(p.matcher(mailBody).find());
		
		// check customer service contact number in email
		p = Pattern.compile("please\\scall\\scustomer\\sservice\\sat\\s" + custInfo.getCustomerServiceContact() + "\\.");
		assertTrue(p.matcher(mailBody).find());
	}
}



class FDEmailFactoryHack extends FDEmailFactory {
	private static FDEmailFactoryHack _sharedInstance;

	// default instance getter
	public static FDEmailFactory getInstance() {
		if (_sharedInstance == null) {
			_sharedInstance = new FDEmailFactoryHack();
		}
		return _sharedInstance;
	}

	// mocked method
	protected String getFromAddress(String depotCode) {
		return GENERAL_CS_EMAIL;
	}
}
