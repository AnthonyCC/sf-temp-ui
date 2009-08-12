/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.mail.service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.mail.MessagingException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.xml.XSLTransformer;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.MailName;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class MailMessageListener extends MessageDrivenBeanSupport implements MailName {

	private final static Category LOGGER = LoggerFactory.getInstance(MailMessageListener.class);

	public void onMessage(javax.jms.Message msg) {

		if (!(msg instanceof TextMessage)) {
			LOGGER.error("Not a TextMessage, consuming message");
			// silently consume it, no point in throwing it back to the queue
			return;
		}

		String mailBodyXml, mailFrom, mailTo, mailCc, mailBcc, mailTitle, xslPath, personalLabel;
		boolean isHtml;
		try {

			//
			// Get mail properties
			//
			TextMessage mailMsg = (TextMessage) msg;
			mailBodyXml = mailMsg.getText();
			mailFrom = mailMsg.getStringProperty(MailName.FROM_ADDRESS);
			mailTo = mailMsg.getStringProperty(MailName.TO_ADDRESS);
			mailCc = mailMsg.getStringProperty(MailName.CC_ADDRESS);
			mailBcc = mailMsg.getStringProperty(MailName.BCC_ADDRESS);
			mailTitle = mailMsg.getStringProperty(MailName.TITLE);
			xslPath = mailMsg.getStringProperty(MailName.XSL);
			personalLabel = mailMsg.getStringProperty(MailName.FROM_ADDRESS_NAME);
			isHtml = mailMsg.getBooleanProperty(MailName.IS_HTML);

		} catch (JMSException ex) {
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
			throw new RuntimeException("JMSException occured while reading command: " + ex.getMessage());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Recipient: " + mailTo
				+ "\nSender: " + mailFrom
				+ "\nTitle: " + mailTitle
				+ "\nXsl path: " + xslPath
				+ "\nMessage Body: "+ mailBodyXml);
		}

		//
		// Do transformation
		//
		String mailBody;
		try {
			mailBody = new XSLTransformer().transform(mailBodyXml, xslPath);
		} catch (TransformerException ex) {
			// silently consume it, no point in throwing it back to the queue
			LOGGER.error("XSL transformation failed, consuming message", ex);
			return;
		}

		try {
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(mailFrom, mailTo, mailCc, mailBcc, mailTitle, mailBody, isHtml, personalLabel);
		} catch (MessagingException ex) {
			LOGGER.error("Unable to send message, throwing RuntimeException", ex);
			throw new RuntimeException("Unable to send message: " + ex.getMessage());
		}

		LOGGER.debug("message sent");

	}
}
