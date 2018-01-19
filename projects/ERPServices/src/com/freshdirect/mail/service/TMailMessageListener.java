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

		LOGGER.debug(this.getClass().getSimpleName() + "recieved a message ");
		if (!(msg instanceof TextMessage || msg instanceof ObjectMessage)) {
			LOGGER.error("Not a TextMessage/ObjectMessage, consuming message");
			// silently consume it, no point in throwing it back to the queue
			return;
		}


		// get the object
		// update the database status
		// connect to the mail sender and send the mail
		// get the result and update the db

		TEmailI tEmail = null;
		LOGGER.debug("message recieved");
		// Connection conn = null;
		if (msg instanceof ObjectMessage) {
			tEmail = getTEmailIfromJMSObject(msg);

			String strStatus = processAndSendTEmail(tEmail);
			LOGGER.debug("sent email, status was: " + strStatus);
		} // if object message
		else {
			LOGGER.error("Drained a non object message from the TEMail queue and dont know how to handle it ");
		}

	}// onmessage

	public TEmailI getTEmailIfromJMSObject(javax.jms.Message msg) {
		TEmailI tEmail = null;
		ObjectMessage objMsg = (ObjectMessage) msg;
		try {
			tEmail = (TEmailI) objMsg.getObject();
		} catch (JMSException e) {
			e.printStackTrace();
			LOGGER.error("JMSException trying to send email", e);

			insertTransactionEmailFailureInfo(tEmail, TranMailServiceI.ERROR_EXTERNAL, e.getMessage());
		} // catch
		return tEmail;
	}

	public String processAndSendTEmail(TEmailI tEmail) {
		Connection conn = null;
		SilverPopSendStatus silverPopSendStatus =null;
		try {
			/* TODO */
			// turn this off before checkin
			if (LOGGER.isDebugEnabled()) {
				printMailI(tEmail);
			}

			conn = getConnection();

			String statusStr = TMailerGatewayDAO.getTransactionEmailStatus(conn, tEmail.getId());
			if (EnumTEmailStatus.SUCESS.getName().equalsIgnoreCase(statusStr)
					|| EnumTEmailStatus.INFO.getName().equalsIgnoreCase(statusStr)) {
				return "OK: Email was allready processed";
			}

			TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(),
					EnumTEmailStatus.PROCESSING.getName(), null, null);

			TranMailServiceI tranMailService = TranMailServiceFactory.getTranMailService(tEmail.getProvider());
			String response = tranMailService.sendTranEmail(tEmail);

			 silverPopSendStatus = checkStatusOfTEmailResponse(response);
			 
			 
			switch (silverPopSendStatus) {
			case OK: {
				TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(),
						EnumTEmailStatus.SUCESS.getName(), null, null);
			}
				break;
			case INFO: {
				TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(),
						EnumTEmailStatus.INFO.getName(), TranMailServiceI.INFO_WARNING, response);

			}
				break;
			case ERROR:{
				
				insertTransactionEmailFailureInfo(tEmail, TranMailServiceI.ERROR_EXTERNAL, response);
			}
				break;

			}

			LOGGER.debug("message sent");
		}

		catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("SQLException trying to send email", e);

			insertTransactionEmailFailureInfo(tEmail, TranMailServiceI.ERROR_EXTERNAL, e.getMessage());
		}

		catch (TranEmailServiceException ex) {
			

			insertTransactionEmailFailureInfo(tEmail, TranMailServiceI.ERROR_EXTERNAL, ex.getMessage());
		}
		
		catch (Exception e) {
			e.printStackTrace();

			insertTransactionEmailFailureInfo(tEmail, TranMailServiceI.ERROR_EXTERNAL, e.getMessage());
		} finally {
			try {
				close(conn);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("unknown exception closing connection", e);
			}
		} // finally
		return silverPopSendStatus !=null? silverPopSendStatus.status: "OK";
	}

	/**
	 * The response string that comes back has both the status and a message in
	 * it. It has to be carefully parsed to see whats the actual status. P.S.
	 * should consider a more complex return than a simple string.
	 * 
	 * @param response
	 * @return
	 */
	public SilverPopSendStatus checkStatusOfTEmailResponse(String response) {
		SilverPopSendStatus retstat;
		if (response.contains("OK")) {
			if (response.trim().length() > 4) {

				retstat = SilverPopSendStatus.INFO;
			} else {
				retstat = SilverPopSendStatus.OK;
			}

		} else {
			retstat = SilverPopSendStatus.ERROR;
		}
		return retstat;

	}

	/**
	 * Both UPDATES	 the Tanns_Email_master with a failure status as well as inserting an error into the  CUST.TRANS_EMAIL_ERROR_DETAILS table
	 * @param tEmail
	 * @param errorType
	 * @param errorDesc
	 */
	public void insertTransactionEmailFailureInfo(TEmailI tEmail, String errorType, String errorDesc) {
		Connection conn = null;

		if (tEmail != null) {
			try {
				conn = getConnection();
				TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(),
						EnumTEmailStatus.FAILED.getName(), errorType, errorDesc);
				TMailerGatewayDAO.insertTransactionEmailFailureInfo(conn, tEmail, EnumTEmailStatus.FAILED.getName(),
						errorType, errorDesc);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			} finally {
				close(conn);
			}
		}
	}

	private void printMailI(TEmailI mail) {
		LOGGER.debug(this.getClass().getSimpleName()
				+ " XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     printing out email info off of the queue: ");
		LOGGER.debug("getCustomerId  " + mail.getCustomerId());
		LOGGER.debug("getEmailContent  " + mail.getEmailContent());
		LOGGER.debug("getEmailStatus  " + mail.getEmailStatus());
		LOGGER.debug("getEmailTransactionType  " + mail.getEmailTransactionType());
		LOGGER.debug("getEmailType  " + mail.getEmailType());

		LOGGER.debug("getId  " + mail.getId());

		LOGGER.debug("getOasQueryString  " + mail.getOasQueryString());

		LOGGER.debug("getOrderId  " + mail.getOrderId());

		LOGGER.debug("getProvider  " + mail.getProvider());

		LOGGER.debug("getRecipient  " + mail.getRecipient());

		LOGGER.debug("getSubject  " + mail.getSubject());

		LOGGER.debug("getTargetProgId  " + mail.getTargetProgId());

		LOGGER.debug("getTemplateId  " + mail.getTemplateId());

		LOGGER.debug("getBCCList  " + mail.getBCCList());

		LOGGER.debug("getFromAddress  " + mail.getFromAddress());

	}

	public enum SilverPopSendStatus {
		OK("OK", "NO ERRORS"), ERROR("ERROR", "NO ERRORS"), INFO("INFO", "WARNING IN RESPONSE");
		private String status;
		private String description;

		SilverPopSendStatus(String stat, String desc) {
			this.status = stat;
			this.description = desc;
		}

	}

}
