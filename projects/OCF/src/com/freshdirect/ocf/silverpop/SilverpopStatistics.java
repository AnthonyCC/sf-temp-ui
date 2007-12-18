/**
 * @author ekracoff
 * Created on Apr 29, 2005*/

package com.freshdirect.ocf.silverpop;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.freshdirect.ocf.core.Emailer;
import com.freshdirect.ocf.core.JobDetail;

public class SilverpopStatistics extends SilverpopBase {

	public static JobDetail getStatistics(Emailer emailer) throws IOException {

		///////////////////////

		Document sendMailDoc = DocumentHelper.createDocument();
		Element body = sendMailDoc.addElement("Envelope").addElement("Body");

		Element mte = body.addElement("MailingTrackingExport");
		mte.addElement("MAILING_ID").addText(emailer.getMailingId());
		if (emailer.getNotifyEmailAddress() != null) {
			mte.addElement("EMAIL").addText(emailer.getNotifyEmailAddress());
		}
		mte.addElement("DATE_START").addText(SilverpopBase.DATE_FORMATTER.format(emailer.getFlight().getCampaign().getStartDate()));
		mte.addElement("DATE_END").addText(DATE_FORMATTER.format(new Date()));
		mte.addElement("MOVE_TO_FTP");
		mte.addElement("ALL");

		String sessionId = login(SERVICE.getUsername(), SERVICE.getPassword());

		Document doc = postXml(SERVICE.getUrl(), sendMailDoc, sessionId);

		if (SERVICE.isPrintXml()) {
			System.out.println(prettyPrint(doc));
		}

		Node success = doc.selectSingleNode("/Envelope/Body/RESULT/SUCCESS");
		if ("TRUE".equalsIgnoreCase(success.getText())) {
			Element mailing = (Element) doc.selectSingleNode("/Envelope/Body/RESULT/MAILING");
			JobDetail jd = new JobDetail();

			for (Iterator i = mailing.elementIterator(); i.hasNext();) {
				Element e = (Element) i.next();
				if (e.getName().equals("JOB_ID")) {
					jd.setJobId(e.getText());
				} else if ("FILE_PATH".equals(e.getName())) {
					jd.setFilePath((String) e.getData());
				} else if ("SENT_DATE".equals(e.getName())) {
					//jd.setSentDate((Date) e.getData());
				}
			}
			return jd;
		}

		return null;
	}

	public static boolean getJobStatus(String jobId) throws IOException {
		String sessionId = login(SERVICE.getUsername(), SERVICE.getPassword());
		return getJobStatus(sessionId, jobId);
	}

}