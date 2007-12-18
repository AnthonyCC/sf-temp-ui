/**
 * @author ekracoff
 * Created on Jun 22, 2005*/

package com.freshdirect.ocf.quartz;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.mail.MessagingException;

import org.apache.log4j.Category;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.hibernate.HibernateDaoSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;


public abstract class FDJob implements Job {
	public final static Category LOGGER = LoggerFactory.getInstance(FDJob.class);
	private final static ErpMailSender MAILER = new ErpMailSender();
	
	public abstract void run(JobExecutionContext ctx);
	
	/**
	 *  Return a hibernate DAO support object.
	 * 
	 *  @return a hibernate DAO support object used by the job.
	 */
	public abstract HibernateDaoSupport getDaoSupport();

	public final void execute(JobExecutionContext arg0) throws JobExecutionException {
		try{
			run(arg0);
		} catch (Exception e){
			LOGGER.error("Job failed "+this, e);
			sendMail(stack2string(e));
			throw new JobExecutionException(e);
		} finally {
			getDaoSupport().closeSession();
		}

	}
	
	public void sendMail(String message){
		try {
			if(ErpServicesProperties.isSendOcfEmail()){
				MAILER.sendMail(ErpServicesProperties.getOcfMailFrom(), ErpServicesProperties.getOcfMailTo(), ErpServicesProperties.getOCfMailCC(), "**OCF ERROR NOTIFICATION** Error running job", message);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public String stack2string(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return "------\r\n" + sw.toString() + "------\r\n";
	} 

}
