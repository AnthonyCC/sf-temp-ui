/*
 * Created on Nov 16, 2003
 *
 */
 
package com.freshdirect.dataloader.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author miker
 *
 */
public class MailAttachmentSender {

    public static void main(String[] args) {

        try {

			ListBuilder builder = new ListBuilder();
			List<MailInfo> emailAddresses = builder.parseEmailList(new File("c:/spam/survey/survey_recipients_best.txt"));
			for (MailInfo mi : emailAddresses) {
				MailAttachmentSender ms = new MailAttachmentSender();
	            Message msg = ms.makeMessage(mi.getEmail());
	
	            Multipart mp = new MimeMultipart();
	            mp.addBodyPart(ms.makeGreeting("c:/spam/survey/nfi_survey_greeting.html"));
	            mp.addBodyPart(ms.makeAttachment("c:/spam/survey/nfi_survey_2.html"));
	            msg.setContent(mp);
	
	            Transport.send(msg);
			}

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MailAttachmentSender() {
        super();
    }

    public Message makeMessage(String recipient) throws Exception {

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "ns2.nyc1.freshdirect.com");

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("announcements@freshdirect.com", "FreshDirect Customer Service"));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

        msg.setSubject("Help us better serve you");

        msg.setHeader("X-Mailer", "FreshDirectCustomerServiceManager");
        msg.setSentDate(new Date());

        return msg;

    }

    public MimeBodyPart makeGreeting(String filename) throws Exception {

        BufferedReader in = new BufferedReader(new FileReader(filename));

        String line;
        StringBuffer sb = new StringBuffer();
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        in.close();

        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(sb.toString(), "text/html");
        mbp.setDisposition(Part.INLINE);

        return mbp;

    }

    public MimeBodyPart makeAttachment(String filename) throws Exception {

        MimeBodyPart mbp = new MimeBodyPart();

        FileDataSource fds = new FileDataSource(filename);
        mbp.setDataHandler(new DataHandler(fds));
        mbp.setFileName("FreshDirect_Survey.html");
        mbp.setHeader("Content-Type", "text/html");
		mbp.setDisposition(Part.ATTACHMENT);

        return mbp;

    }

}
