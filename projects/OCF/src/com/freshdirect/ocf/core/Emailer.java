/**
 * @author ekracoff
 * Created on Apr 28, 2005*/

package com.freshdirect.ocf.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.ocf.silverpop.SilverpopInitiateImport;
import com.freshdirect.ocf.silverpop.SilverpopSendMail;

public class Emailer extends Action implements ActionI {

	private final static int REPORT_POST_PROCESSING_PERIOD = 60;
	
	private DateFormat dateFormat = new SimpleDateFormat("yyMMdd_HHmmss");

	private String mailingId;
	private String notifyEmailAddress;

	private Date nextEnqueueDate;
	private int enqueueFrequency;

	private String silverpopJobId;
	private String silverpopFilePath;

	public void setMailingId(String mailingId) {
		this.mailingId = mailingId;
	}

	public String getMailingId() {
		return mailingId;
	}

	public String getSilverpopJobId() {
		return silverpopJobId;
	}

	public void setSilverpopJobId(String jobId) {
		this.silverpopJobId = jobId;
	}

	public Date getNextEnqueueDate() {
		return nextEnqueueDate;
	}

	public void setNextEnqueueDate(Date nextRunDate) {
		this.nextEnqueueDate = nextRunDate;
	}

	public String getNotifyEmailAddress() {
		return notifyEmailAddress;
	}

	public void setNotifyEmailAddress(String notifyEmailAddress) {
		this.notifyEmailAddress = notifyEmailAddress;
	}

	public int getEnqueueFrequency() {
		return enqueueFrequency;
	}

	public void setEnqueueFrequency(int frequency) {
		this.enqueueFrequency = frequency;
	}

	public String getSilverpopFilePath() {
		return silverpopFilePath;
	}

	public void setSilverpopFilePath(String filePath) {
		this.silverpopFilePath = filePath;
	}

	public void scheduleNextEnqueue() {
		Date now = new Date();
		Date finishReportingDate = DateUtil.addDays(getFlight().getCampaign().getEndDate(), REPORT_POST_PROCESSING_PERIOD);
		if (now.after(finishReportingDate)) {
			setNextEnqueueDate(null);
		} else {
			setNextEnqueueDate(DateUtil.addHours(now, getEnqueueFrequency()));
		}
		setSilverpopJobId(null);
		setSilverpopFilePath(null);
	}

	public void jobEnqueued(String jobId, String filePath) {
		this.setNextEnqueueDate(null);
		this.setSilverpopJobId(jobId);
		this.setSilverpopFilePath(filePath);
	}

	public void execute(OcfTableI customers) throws OcfRecoverableException {
		File temp = null;
		try {
			List custs;
			try {
				custs = customers.getValuesByColumn("EMAIL");
				
				String timeStamp = getCurrentTimestamp();
				
				String custFileName = getFileNameWithTimestamp("users", null, timeStamp);
				
				temp = File.createTempFile(custFileName, ".csv");

				customers.toFile(temp.getAbsolutePath(), "|");

				SilverpopInitiateImport.uploadFile(temp.getName(), new FileInputStream(temp));
				String mappingFileName = getFileNameWithTimestamp("mapping", "xml",timeStamp);
				SilverpopInitiateImport.uploadFile(mappingFileName, ResourceUtil
								.openResource("classpath:/com/freshdirect/ocf/silverpop/mapping.xml"));

				SilverpopInitiateImport.execute(mappingFileName, temp.getName());
			} catch (IOException e) {
				throw new OcfRecoverableException(e);
			}

			SilverpopSendMail.send(mailingId, custs);

			this.scheduleNextEnqueue();
			OcfManager.getInstance().saveOrUpdateAction(this);
		} catch (IOException e) {
			throw new FDRuntimeException(e);
		} finally {
			if (temp != null) {
				temp.delete();
			}
		}
	}
	
	//Method to add timestmap to silverpop ftp files
	private String getFileNameWithTimestamp(String original, String extension, String timeStamp) {
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(original);
		if(SilverpopInitiateImport.isAppendTimeStamp()) {
			strBuf.append("_").append(timeStamp);
		}
		if(extension != null) {
			strBuf.append(".").append(extension);
		}
		return strBuf.toString();
	}
	
	private String getCurrentTimestamp() {		
		Date date = new Date();
        return dateFormat.format(date);
	}

	public String toString() {
		return "Email action: mailingId=" + mailingId + ", freq=" + enqueueFrequency;
	}
}