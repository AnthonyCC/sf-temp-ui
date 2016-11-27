package com.freshdirect.dataloader.sap;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;



public class TruckRefreshCron {
	
	private static Category LOGGER = LoggerFactory.getInstance(TruckRefreshCron.class);
	public static void main(String[] args){	
		try {
			HttpClient client = new HttpClient();
			client.setHttpConnectionFactoryTimeout(0);
			client.setTimeout(0);
			client.setConnectionTimeout(0);
			HttpState state = client.getState( );
			
			GetMethod meth = new GetMethod(FDStoreProperties.getTruckRefreshUrl());
			
			meth.addRequestHeader("User-Agent", "TRUCKREFRESH/1.0");
			NameValuePair param = new NameValuePair("source", "cron");
			NameValuePair[] params;
			if(args.length>=1){
				NameValuePair param2 = new NameValuePair("dispDate", args[0]);
				params = new NameValuePair[]{param,param2};
			}
			else{
				params = new NameValuePair[]{param};
			}
			meth.setQueryString(params);
			
			int status = client.executeMethod(meth);
			LOGGER.info(meth.getResponseBodyAsString());
			LOGGER.info("Truck refresh finished with status:"+status);
			meth.releaseConnection();
		} catch (HttpException e) {
			LOGGER.error("Failed to refresh trucks", e);
			
		} catch (IOException e) {
			LOGGER.error("Failed to refresh trucks", e);
			
		}
	}
}
