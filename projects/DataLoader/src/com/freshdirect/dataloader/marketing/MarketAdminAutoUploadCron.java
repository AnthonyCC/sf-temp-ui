package com.freshdirect.dataloader.marketing;

import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;



public class MarketAdminAutoUploadCron {
	
	private static Category LOGGER = LoggerFactory.getInstance(MarketAdminAutoUploadCron.class);
	private final static int TIMEOUT = 30000;
	public static void main(String[] args){	
		try {
			HttpClient client = new HttpClient();
			client.setHttpConnectionFactoryTimeout(0);
			client.setTimeout(0);
			client.setConnectionTimeout(0);
			HttpState state = client.getState( );

			LOGGER.info("Auto upload started");
			PostMethod meth = new PostMethod(FDStoreProperties.getMktAdminAutouploadUrl());
			//http://localhost:7001/mktAdmin/mktAdmin_autoUpload
			//http://adm01.stdev03.nyc1.freshdirect.com:7011/MrktAdmin/mktAdmin_autoUpload
			meth.addRequestHeader("User-Agent", "PromoCustAutoUpload/1.0");
			/*Credentials credentials =	new UsernamePasswordCredentials( FDStoreProperties.getMktAdminUserName(), FDStoreProperties.getMktAdminPassword());
			state.setCredentials( null, null, credentials );*/

			/*meth.setRequestHeader("Content-Type", "text/html");
			meth.setRequestBody("No Body");
			meth.setUseExpectHeader(false);*/
//			BASE64Encoder encoder = new BASE64Encoder ();
//			String auth="";
//			encoder.encode(auth.getBytes());
//			meth.addRequestHeader("Authorization", "Basic "+encoder.toString());
			int status = client.executeMethod(meth);
			LOGGER.info("Auto upload finished with status:"+status);
			meth.releaseConnection();
		} catch (HttpException e) {
			LOGGER.error("Failed to auto upload", e);
			
		} catch (IOException e) {
			LOGGER.error("Failed to auto upload", e);
			
		}
	}
}
