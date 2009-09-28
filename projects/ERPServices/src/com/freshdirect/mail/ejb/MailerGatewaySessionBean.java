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
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.mail.EmailI;
import com.freshdirect.framework.mail.FTLEmailI;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.MailName;

public class MailerGatewaySessionBean extends GatewaySessionBeanSupport implements MailName {
	
	private static Category LOGGER = LoggerFactory.getInstance( MailerGatewaySessionBean.class );
	
	public void enqueueEmail(EmailI email) {
		this.enqueue(email);
	}
	
	private TextMessage enqueue(EmailI email)  {

		try {
			
			TextMessage mailMsg = qsession.createTextMessage();
			if(null != email){
				if(email instanceof XMLEmailI){
				
					XMLEmailI xmlEmail = (XMLEmailI)email;
					mailMsg.setText(xmlEmail.getXML());
					mailMsg.setStringProperty(MailName.TO_ADDRESS, xmlEmail.getRecipient());
					
					StringBuffer ccList = new StringBuffer(128);
					Iterator it = email.getCCList().iterator();
					if (it.hasNext())
						ccList.append(it.next());
					while (it.hasNext()) {
						ccList.append(';');
						ccList.append(it.next());
					}
					StringBuffer bccList = new StringBuffer(128);
					it = email.getBCCList().iterator();
					if (it.hasNext())
						bccList.append(it.next());
					while (it.hasNext()) {
						bccList.append(';');
						bccList.append(it.next());
					}
					mailMsg.setStringProperty(CC_ADDRESS, ccList.toString().trim());
					mailMsg.setStringProperty(BCC_ADDRESS, bccList.toString().trim());
					
					mailMsg.setStringProperty(FROM_ADDRESS, xmlEmail.getFromAddress().getAddress()); 
					mailMsg.setStringProperty(FROM_ADDRESS_NAME, xmlEmail.getFromAddress().getName());
					mailMsg.setStringProperty(XSL, xmlEmail.getXslPath());
					mailMsg.setStringProperty(TITLE, xmlEmail.getSubject());
					mailMsg.setBooleanProperty(IS_HTML, xmlEmail.isHtmlEmail());
					this.qsender.send(mailMsg);
				}else if(email instanceof FTLEmailI){
					FTLEmailI ftlEmail = (FTLEmailI)email;
					ObjectMessage objMsg = qsession.createObjectMessage(ftlEmail);
					objMsg.setStringProperty(MailName.TO_ADDRESS, ftlEmail.getRecipient());
					
					StringBuffer ccList = new StringBuffer(128);
					for (Iterator i = ftlEmail.getCCList().iterator(); i.hasNext(); ) {
						    String e = (String) i.next();
							ccList.append(e);
							ccList.append(" ");
					}
					
					objMsg.setStringProperty(CC_ADDRESS, ccList.toString().trim());
					
					objMsg.setStringProperty(FROM_ADDRESS, ftlEmail.getFromAddress().getAddress()); 
					objMsg.setStringProperty(FROM_ADDRESS_NAME, ftlEmail.getFromAddress().getName());				
					objMsg.setStringProperty(TITLE, ftlEmail.getSubject());
					objMsg.setBooleanProperty(IS_HTML, true);//FTL is always of HTML only.
					this.qsender.send(objMsg);
				}			
			}
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
