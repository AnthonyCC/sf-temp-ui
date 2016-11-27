package com.freshdirect.dataloader.sap;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.mail.MessagingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;



public class EmployeeRefreshCron {
	
	private static Category LOGGER = LoggerFactory.getInstance(EmployeeRefreshCron.class);
	public static void main(String[] args){	
		GetMethod meth = null;
		try {
			HttpClient client = new HttpClient();
			client.setHttpConnectionFactoryTimeout(0);
			client.setTimeout(0);
			client.setConnectionTimeout(0);
			HttpState state = client.getState( );
			
			meth = new GetMethod(FDStoreProperties.getEmployeeRefreshUrl());
			
			meth.addRequestHeader("User-Agent", "EMPREFRESH/1.0");
			
			int status = client.executeMethod(meth);
			String exceptionMsg = meth.getResponseBodyAsString();
			if(status==200){
				LOGGER.info("Employee refresh finished with status:"+status);
			}else{
				throw new Exception(exceptionMsg);
			}
			
		} catch (HttpException e) {
			LOGGER.error("Failed to refresh employees", e);
			email(e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Failed to refresh employees", e);
			email(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Failed to refresh employees", e);
			email(e.getMessage());
		}
		finally{
			meth.releaseConnection();
		}
	}
	
	private static void email(String exceptionMsg) {
		try {
			String subject="Kronos Employee Refresh: ";

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending Employee Refresh Cron report email: ", e);
		}
		
	}
	
}
