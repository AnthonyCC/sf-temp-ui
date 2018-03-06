package com.freshdirect.fdstore.iplocator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Category;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CmsCaches;
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
		IpLocatorData ipLocatorData = null;
		if (FDStoreProperties.isIpLocatorCacheEnabled()) {
			ipLocatorData = this.getCachedIpInfo(ip);
			if (ipLocatorData != null) {
				LOGGER.debug("##############  IP data retrieved from ehCache " + ip);
			}
		} else {
			LOGGER.debug("##############  IP ehCache disabled.");
		}
		if (ipLocatorData == null) {
			String urlStr = FDStoreProperties.getIpLocatorV4Url() + "?id=" + FDStoreProperties.getIpLocatorClientId() + "&ip=" + StringUtil.encodeUrl(ip);
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
			if (FDStoreProperties.isIpLocatorCacheEnabled()) {
				this.putCachedIpInfo(ip, ipLocatorData);
				LOGGER.info("############## IP data set to ehCache " + ip);
			}
		}
		return ipLocatorData;
	}		
	
	public IpLocatorData getCachedIpInfo(String key) {
		return CmsServiceLocator.ehCacheUtil().getObjectFromCache(CmsCaches.IP_LOCATOR_CACHE_NAME.cacheName, key);
	}
	public void putCachedIpInfo(String key, IpLocatorData value) {
		CmsServiceLocator.ehCacheUtil().putObjectToCache(CmsCaches.IP_LOCATOR_CACHE_NAME.cacheName, key, value);
	}	
}
