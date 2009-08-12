/*
 * Created on Jul 11, 2003
 */
package com.freshdirect.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author knadeem
 */

public class ErpMailSender {
	
	private static Category LOGGER = LoggerFactory.getInstance(ErpMailSender.class);
	
	public void sendMail(String from, String recipient, String cc, String subject, String message) throws MessagingException {
		this.sendMail(from, recipient, cc, subject, message, false, "");
	}

	public void sendMail(String from, String recipient, String recipientCc, String subject, String message, 
			boolean isHtml, String personalLabel) throws MessagingException {
		sendMail(from, recipient, recipientCc, null, subject, message, isHtml, personalLabel);
	}
	
	public void sendMail(String from, String recipient, String recipientCc, String recipientBcc, String subject, String message, 
		boolean isHtml, String personalLabel) throws MessagingException {
		
		try {

			Properties props = new Properties();
			props.put("mail.smtp.host", ErpServicesProperties.getMailerHost());
			props.put("mail.transport.protocol", ErpServicesProperties.getMailerProtocol());
			props.put("mail.from", ErpServicesProperties.getMailerFromAddress());

			javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);

			//
			// Create a message
			//
			javax.mail.Message msg = new MimeMessage(session);
			//
			// Set the from and to address
			//
			msg.setFrom(new InternetAddress(from, personalLabel));
			InternetAddress[] addressTo = new InternetAddress[1];
			addressTo[0] = new InternetAddress(recipient);
			msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);

			StringTokenizer st = new StringTokenizer(recipientCc, ";");
			InternetAddress[] addressCc = new InternetAddress[st.countTokens()];
			int i = 0;
			while (st.hasMoreTokens()) {
				addressCc[i] = new InternetAddress(st.nextToken());
				i++;
			}
			msg.setRecipients(javax.mail.Message.RecipientType.CC, addressCc);

			st = new StringTokenizer(recipientBcc, ";");
			InternetAddress[] addressBcc = new InternetAddress[st.countTokens()];
			i = 0;
			while (st.hasMoreTokens()) {
				addressBcc[i] = new InternetAddress(st.nextToken());
				i++;
			}
			msg.setRecipients(javax.mail.Message.RecipientType.BCC, addressBcc);
			//
			// Setting the Subject and Content Type
			//
			msg.setSubject(subject);
			msg.setContent(message, isHtml ? "text/html" : "text/plain");
			//
			// sent the sent-date
			//
			msg.setSentDate(new java.util.Date());


			if ("true".equalsIgnoreCase(ErpServicesProperties.getSendEmail())) {
				Transport.send(msg);
			}

		} catch (UnsupportedEncodingException ex) {
			LOGGER.warn("UnsupportedEncodingException while constructing email address", ex);
			throw new MessagingException(ex.getMessage());
		}	
	}

}
