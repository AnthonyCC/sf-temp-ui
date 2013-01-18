package com.freshdirect.fdstore.mail;

import java.util.Date;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import junit.framework.TestCase;

import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.xml.XSLTransformer;
import com.freshdirect.mail.MailName;

public class FDEmailFactoryTest extends TestCase implements MailName {
	/**
	 * Fulfill a customer info instance 
	 * @param isChefsTable
	 * @param isHtmlEmail
	 */
	FDCustomerInfo createCustomerInfo(final boolean isChefsTable, final boolean isHtmlEmail) {
		FDCustomerInfo custInfo = new FDCustomerInfo("Ubul", "Vajas");

		custInfo.setHtmlEmail(isHtmlEmail);
		custInfo.setEmailAddress("uvajas@freshdirect.com");
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
		XMLEmailI email = FDEmailFactoryHack.getInstance().createAuthorizationFailedEmail(custInfo, "123456", stDate, endDate, endDate, 1);  //Instant bugfix because of broken trunk

		// compile mail template to string content
		String mailBody = new XSLTransformer().transform(email.getXML(), email.getXslPath());
		
		//System.out.println(mailBody);
		
		// check customer name
		p = Pattern.compile("\\<b\\>Dear " + custInfo.getFirstName() + "\\</b\\>,");
		assertTrue(p.matcher(mailBody).find());

		// check customer service contact number in email
		p = Pattern.compile("please\\scall\\sour\\sCustomer\\sService\\sTeam\\s\\snow\\sat\\s\\s\\<b\\>" + custInfo.getCustomerServiceContact());
		assertTrue(p.matcher(mailBody).find());
	}


	
	
	public void testAuthorizationFailedPlain() throws TransformerException {
		final boolean isChefsTable = true;
		Pattern p;
		
		FDCustomerInfo custInfo = createCustomerInfo(isChefsTable, false);

		// template parameters
		Date stDate = new Date();
		Date endDate = new Date(stDate.getTime() + 600000);
		
		// get email template 
		XMLEmailI email = FDEmailFactoryHack.getInstance().createAuthorizationFailedEmail(custInfo, "123456", stDate, endDate, endDate, 1);  //Instant bugfix because of broken trunk

		// compile mail template to string content
		String mailBody = new XSLTransformer().transform(email.getXML(), email.getXslPath());
		
		//System.out.println(mailBody);
		
		// check customer name
		p = Pattern.compile("Dear " + custInfo.getFirstName() + ",");
		assertTrue(p.matcher(mailBody).find());
		
		// check customer service contact number in email
		p = Pattern.compile("please\\scall\\sour\\sCustomer\\sService\\sTeam\\snow\\sat\\s" + custInfo.getCustomerServiceContact());
		assertTrue(p.matcher(mailBody).find());
	}
	public static void main(String[] a) throws TransformerException {
		FDEmailFactoryTest t= new FDEmailFactoryTest();
		t.testAuthorizationFailedHtml();
		t.testAuthorizationFailedPlain();
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
