package com.freshdirect.fdstore.iplocator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Category;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.marker.ThirdPartyIntegration;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class IpLocatorClient implements ThirdPartyIntegration {

	private static final Category LOGGER = LoggerFactory.getInstance(IpLocatorClient.class);
	
	private IpLocatorClient(){
	}
	
	public static synchronized IpLocatorClient getInstance(){
		return new IpLocatorClient();
	}
	
	public static void main(String a[]) throws Exception {
		IpLocatorClient.getInstance().getData("8.42.37.100"); //"8.41.212.56"
	}

	public IpLocatorData getData(String ip) throws IpLocatorException {
		
		String urlStr = FDStoreProperties.getIpLocatorV4Url() + "?id=" + FDStoreProperties.getIpLocatorClientId() + "&ip=" + StringUtil.encodeUrl(ip);
		IpLocatorData ipLocatorData = null;
		LOGGER.info("IP Locator V4 URL: " + urlStr);
		HttpMethod request = null;
		
		try {
			request = new GetMethod(urlStr);
			
			HttpClient client = new HttpClient();
			client.setTimeout(FDStoreProperties.getIpLocatorTimeout());
			client.executeMethod(request);
			
			ObjectMapper mapper = new ObjectMapper(); 
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 
			IpLocatorResponse response = mapper.readValue(request.getResponseBodyAsStream(), IpLocatorResponse.class);
			
			
			IpLocatorRecord record = null;
			
			if(response != null && response.getRecords() != null && response.getRecords().size() > 0) {
				
				record = response.getRecords().get(0);
				
				ipLocatorData = new IpLocatorData();				
				ipLocatorData.setZipCode(record.getPostalCode());
				ipLocatorData.setRegion(record.getRegion());
				ipLocatorData.setCity(record.getCity());
				ipLocatorData.setCountryCode(record.getCountryAbbreviation());
			}
				
			LOGGER.debug("IP Locator V4 Result >"+ ipLocatorData);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new IpLocatorException(e);
		} finally {
			if(request != null) {
				request.releaseConnection();
			}
		}
		
		return ipLocatorData;
	}		
}
