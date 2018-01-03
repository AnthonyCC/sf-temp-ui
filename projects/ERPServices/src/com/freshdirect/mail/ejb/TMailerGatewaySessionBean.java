package com.freshdirect.mail.ejb;

/*
 * 
 * MailerGatewaySessionBean.java
 * Date: Oct 4, 2002 Time: 4:24:32 PM
 */


/**
 * 
 * @author knadeem
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.MailName;
import com.freshdirect.temails.TEmailRuntimeException;

public class TMailerGatewaySessionBean extends GatewaySessionBeanSupport implements MailName {
	
	private static Category LOGGER = LoggerFactory.getInstance( TMailerGatewaySessionBean.class );
	
	public void enqueue(TEmailI email) {
		this.enqueueEmail(email);
	}
	
	private ObjectMessage enqueueEmail(TEmailI email)  {

		// store the db entry in database
		// send the email
		
		LOGGER.debug("-----------------------------------------Enquing the transactional email");
		ObjectMessage objMsg=null;	
		try {
			
					
			
					TEmailI tEmail = (TEmailI)email;
					 objMsg = qsession.createObjectMessage(tEmail);
					objMsg.setStringProperty(MailName.TO_ADDRESS, tEmail.getRecipient());
					
					StringBuffer bccList = new StringBuffer(128);
					Iterator it = email.getBCCList().iterator();
					if (it.hasNext())
						bccList.append(it.next());
					while (it.hasNext()) {
						bccList.append(';');
						bccList.append(it.next());
					}
					
					StringBuffer ccList = new StringBuffer(128);
					 it = email.getCCList().iterator();
					if (it.hasNext())
						ccList.append(it.next());
					while (it.hasNext()) {
						ccList.append(';');
						ccList.append(it.next());
					}
					
					//objMsg.setStringProperty(TEMPLATE_ID, tEmail.getTemplateId());
					objMsg.setStringProperty(CC_ADDRESS, ccList.toString().trim());
					objMsg.setStringProperty(BCC_ADDRESS, bccList.toString().trim());
					objMsg.setStringProperty(FROM_ADDRESS, tEmail.getFromAddress().getAddress()); 
					objMsg.setStringProperty(FROM_ADDRESS_NAME, tEmail.getFromAddress().getName());				
					objMsg.setStringProperty(TITLE, tEmail.getSubject());
					//objMsg.setSProperty(IS_HTML, tEmail);//FTL is always of HTML only.
					System.out.println(this.getClass().getName() +"  enquing the object Message ++++++++" );
					this.qsender.send(objMsg);
					
					//conn=getConnection();
					//TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(), tEmail.getEmailStatus(),null,null);
				
			
			return objMsg;
			
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing Capture Message",ex);
			throw new EJBException(ex);
		} catch (Throwable t) {
			LOGGER.warn("Unexpected exception sending email", t);
			throw new EJBException(t.getMessage());
		}
		
	}

	
	public void sendFailedTransactions(){
		// get the failed transactions
		// get the service
		// send the transactions
	}
	
	
	
	public void notifyFailedTransactionEmails(){
		Connection conn=null;
		try{
			conn=getConnection();
			TMailerGatewayDAO.resetTransactionalEmailInfo(conn);
			int count= TMailerGatewayDAO.getFailedTransactionalEmailCount(conn);
			if(count>0){
				
				String subject="[System] Failed to send transactional email.";
				String body="There are "+count+" number of failed transactional emails. Please check resend the emails.";
				sendNotificationEmail(subject, body);
			}
			
		}catch(SQLException re){			
			throw new TEmailRuntimeException(re);				
		}finally {			
				try{
	            close(conn);
				}catch(Exception e){e.printStackTrace();}				
	        }
	}
	
	
	private void sendNotificationEmail(String subject, String body) {
		try {
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getGCMailFrom(), 
							ErpServicesProperties.getGCMailTo(), 
							ErpServicesProperties.getGCMailCC(), 
							subject, body);
		} catch (MessagingException e) {
			throw new EJBException(e);
		}		
	}
	
						
	protected void initialize(Context ctx) throws NamingException, CreateException {
		super.initialize(ctx);
	}

}
