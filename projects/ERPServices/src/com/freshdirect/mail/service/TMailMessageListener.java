package com.freshdirect.mail.service;

/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */


import java.sql.Connection;
import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.EnumTEmailStatus;
import com.freshdirect.mail.MailName;
import com.freshdirect.mail.TranMailServiceFactory;
import com.freshdirect.mail.ejb.TMailerGatewayDAO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class TMailMessageListener extends MessageDrivenBeanSupport implements MailName {

	private final static Category LOGGER = LoggerFactory.getInstance(TMailMessageListener.class);

	public void onMessage(javax.jms.Message msg) {

		if (!(msg instanceof TextMessage || msg instanceof ObjectMessage)) {
			LOGGER.error("Not a TextMessage/ObjectMessage, consuming message");
			// silently consume it, no point in throwing it back to the queue
			return;
		}
		
		// get the object 
		//update the database status
		// connect to the mail sender and send the mail
		// get the result and update the db
				
		boolean isSucess=false;
		TEmailI tEmail=null;
		LOGGER.debug("message sent");
		Connection conn=null;
		if(msg instanceof ObjectMessage){			
			String errorType=null;
			String errorDesc=null;
			try {
				conn=getConnection();
				
				ObjectMessage objMsg = (ObjectMessage)msg;
				 tEmail = (TEmailI)objMsg.getObject();
				//Map parameters = tEmail.getParameters();
				 String statusStr=TMailerGatewayDAO.getTransactionEmailStatus(conn, tEmail.getId());
					if(EnumTEmailStatus.SUCESS.getName().equalsIgnoreCase(statusStr)) return;
					
				 
				 TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(), EnumTEmailStatus.PROCESSING.getName(), null,null);
			
				 TranMailServiceI service=  TranMailServiceFactory.getTranMailService(tEmail.getProvider());
				 String response=service.sendTranEmail(tEmail);
				 
				 System.out.println("resposnse :"+response);
				 
				 if(response !=null && "success".equalsIgnoreCase(response.trim()) || "OK".equalsIgnoreCase(response.trim())){
					
					 TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(), EnumTEmailStatus.SUCESS.getName(), null,null);
					 isSucess=true;					 
				 }else{
					 isSucess=false;
					 errorType=TranMailServiceI.ERROR_INTERNAL;
					 errorDesc=response;
				 }				 				 
				
				//ErpMailSender mailer = new ErpMailSender();
				//mailer.sendMail(mailFrom, mailTo, mailCc, mailTitle, mailBody, isHtml, personalLabel);
				LOGGER.debug("message sent");
			} catch (JMSException e) {
				e.printStackTrace();
				LOGGER.error("JMSException trying to send email", e);
				isSucess=false;
				errorType=TranMailServiceI.ERROR_EXTERNAL;
				errorDesc=e.getMessage();
			} catch(SQLException e){
				e.printStackTrace();
				LOGGER.error("SQLException trying to send email", e);
				isSucess=false;
				errorType=TranMailServiceI.ERROR_EXTERNAL;
				errorDesc=e.getMessage();
			} catch (TranEmailServiceException e){
				e.printStackTrace();
				isSucess=false;
				errorType=e.getErrorType();
				errorDesc=e.getErrorDetails();
			}catch (Exception e){
				e.printStackTrace();
				isSucess=false;
				errorType=TranMailServiceI.ERROR_EXTERNAL;
				errorDesc=e.getMessage();
			}finally {
				try{
	            close(conn);
				}catch(Exception e){e.printStackTrace();}
	        }
						
			if(!isSucess && tEmail!=null){
				try{
				  conn=getConnection();
			      TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(), EnumTEmailStatus.FAILED.getName(),errorType,errorDesc);
			      TMailerGatewayDAO.insertTransactionEmailFailureInfo(conn, tEmail, EnumTEmailStatus.FAILED.getName(), errorType, errorDesc);
				}catch (Exception e){
					LOGGER.error(e.getMessage());
				}finally {
		            close(conn);
		        }
			}

		}

	}
	
	


}
