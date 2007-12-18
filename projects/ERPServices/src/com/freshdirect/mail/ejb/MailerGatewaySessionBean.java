/*
 * 
 * MailerGatewaySessionBean.java
 * Date: Oct 4, 2002 Time: 4:24:32 PM
 */
package com.freshdirect.mail.ejb;

/**
 * 
 * @author knadeem
 */
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.MailName;

public class MailerGatewaySessionBean extends GatewaySessionBeanSupport implements MailName {
	
	private static Category LOGGER = LoggerFactory.getInstance( MailerGatewaySessionBean.class );
	
	public void enqueueEmail(XMLEmailI email) {
		this.enqueue(email);
	}
	
	private TextMessage enqueue(XMLEmailI email)  {

		try {
			
			TextMessage mailMsg = qsession.createTextMessage();
			mailMsg.setText(email.getXML());
			mailMsg.setStringProperty(MailName.TO_ADDRESS, email.getRecipient());
			
			StringBuffer ccList = new StringBuffer(128);
			for (Iterator i = email.getCCList().iterator(); i.hasNext(); ) {
				    String e = (String) i.next();
					ccList.append(e);
					ccList.append(" ");
			}
			
			mailMsg.setStringProperty(CC_ADDRESS, ccList.toString().trim());
			
			mailMsg.setStringProperty(FROM_ADDRESS, email.getFromAddress().getAddress()); 
			mailMsg.setStringProperty(FROM_ADDRESS_NAME, email.getFromAddress().getName());
			mailMsg.setStringProperty(XSL, email.getXslPath());
			mailMsg.setStringProperty(TITLE, email.getSubject());
			mailMsg.setBooleanProperty(IS_HTML, email.isHtmlEmail());
			
			
			this.qsender.send(mailMsg);
			
			return mailMsg;
			
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing Capture Message",ex);
			throw new EJBException(ex);
		} catch (Throwable t) {
			LOGGER.warn("Unexpected exception sending email", t);
			throw new EJBException(t.getMessage());
		}
	}


	protected void initialize(Context ctx) throws NamingException, CreateException {
		super.initialize(ctx);
	}

}
