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
		// System.out.println(this.getClass().getSimpleName() + "recieved a
		// message ");
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
		try {
			/* TODO */
			// turn this off before checkin
			if (LOGGER.isDebugEnabled()) {
				printMailI(tEmail);
			}
			{
				conn = getConnection();
				TMailerGatewayDAO.updateTransactionEmailInfoStatus(conn, tEmail.getId(),
						EnumTEmailStatus.PROCESSING.getName(), null, null);

				String statusStr = TMailerGatewayDAO.getTransactionEmailStatus(conn, tEmail.getId());
				if (EnumTEmailStatus.SUCESS.getName().equalsIgnoreCase(statusStr)
						|| EnumTEmailStatus.INFO.getName().equalsIgnoreCase(statusStr))
					return "allready done";

			}
			TranMailServiceI tranMailService = TranMailServiceFactory.getTranMailService(tEmail.getProvider());
			String response = tranMailService.sendTranEmail(tEmail);

			// System.out.println(this.getClass().getSimpleName() + " send
			// XTEmail or whatever response :" + response);

			SilverPopSendStatus enmStat = checkStatusOfTEmailResponse(response);
			switch (enmStat) {
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
			case ERROR:

			{

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
		return "OK";
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
			if (response.length() > 4) {

				retstat = SilverPopSendStatus.INFO;
			} else {
				retstat = SilverPopSendStatus.OK;
			}

		} else {
			retstat = SilverPopSendStatus.ERROR;
		}
		return retstat;

	}

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
		System.out.println(this.getClass().getSimpleName()
				+ " XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     printing out email info off of the queue: ");
		System.out.println("getCustomerId  " + mail.getCustomerId());
		System.out.println("getEmailContent  " + mail.getEmailContent());
		System.out.println("getEmailStatus  " + mail.getEmailStatus());
		System.out.println("getEmailTransactionType  " + mail.getEmailTransactionType());
		System.out.println("getEmailType  " + mail.getEmailType());

		System.out.println("getId  " + mail.getId());

		System.out.println("getOasQueryString  " + mail.getOasQueryString());

		System.out.println("getOrderId  " + mail.getOrderId());

		System.out.println("getProvider  " + mail.getProvider());

		System.out.println("getRecipient  " + mail.getRecipient());

		System.out.println("getSubject  " + mail.getSubject());

		System.out.println("getTargetProgId  " + mail.getTargetProgId());

		System.out.println("getTemplateId  " + mail.getTemplateId());

		System.out.println("getBCCList  " + mail.getBCCList());

		System.out.println("getFromAddress  " + mail.getFromAddress());

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
