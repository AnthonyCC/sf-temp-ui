package com.freshdirect.dataloader.marketing;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;


public class MarketAdminAutoUploadCron {
	
	private static Category LOGGER = LoggerFactory.getInstance(MarketAdminAutoUploadCron.class);
	public static void main(String[] args){	
		try {
			HttpClient client = new HttpClient();
			LOGGER.info("Auto upload started");
			PostMethod meth = new PostMethod(FDStoreProperties.getMarketingAdminUrl());
			meth.addRequestHeader("User-Agent", "PromoCustAutoUpload/1.0");
			int status = client.executeMethod(meth);
			LOGGER.info("Auto upload finished with status:"+status);
		} catch (HttpException e) {
			LOGGER.error("Failed to auto upload", e);
			
		} catch (IOException e) {
			LOGGER.error("Failed to auto upload", e);
			
		}
	}
}
