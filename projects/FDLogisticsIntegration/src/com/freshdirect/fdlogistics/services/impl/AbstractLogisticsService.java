package com.freshdirect.fdlogistics.services.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Category;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractLogisticsService {
	
	private static final String API_CONTEXT = "/logisticsapi/v/1/";
	private static final String OMS_API_CONTEXT = "/fdlogistics/v/1/";
	
	private static final RestTemplate restTemplate;
	private static final Category LOGGER = LoggerFactory.getInstance(AbstractLogisticsService.class);

	static {
	    
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new ResourceHttpMessageConverter());
		converters.add(getMappingJackson2HttpMessageConverter());
		restTemplate = new RestTemplate(converters);
		
		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();
		connectionManager.setMaxTotal(FDStoreProperties.getConnectionPoolSize());
		
		BasicHttpParams httpParams=new BasicHttpParams();
		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, FDStoreProperties.getLogisticsConnectionTimeout()*1000);
		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, FDStoreProperties.getLogisticsConnectionTimeout()*1000);
		HttpClient defaultHttpClient = new DefaultHttpClient(connectionManager, httpParams);
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(defaultHttpClient);
	    //requestFactory.setReadTimeout(FDStoreProperties.getLogisticsConnectionReadTimeout()*1000);
	    requestFactory.setConnectTimeout(FDStoreProperties.getLogisticsConnectionTimeout()*1000);
	    restTemplate.setRequestFactory(requestFactory);

	}
			
	protected <T> T getData(String inputJson, String url, Class<T> clazz) throws FDLogisticsServiceException {
		
		try {
			HttpEntity<String> entity = getEntity(inputJson);
			RestTemplate restTemplate = getRestTemplate();		
			ResponseEntity<T> response = restTemplate.postForEntity(new URI(url),
					entity, clazz);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			throw new FDLogisticsServiceException("API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			throw new FDLogisticsServiceException("API syntax error");
		}
	}

	protected RestTemplate getRestTemplate(){
		return restTemplate;
	}
	
	protected static MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter(){
		MappingJackson2HttpMessageConverter converter =  new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(getMapper());
		return converter;
	}
	
	private static ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	protected HttpEntity<String> getEntity(String inputJson){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = (inputJson != null)? new HttpEntity<String>(inputJson, headers): new HttpEntity<String>(headers);
		return entity;
	}
	
	protected String buildRequest(Object object){
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mmZ");
		Gson gson = builder.create();
		return gson.toJson(object);
	}

	public String getEndPoint(String path){
		return FDStoreProperties.getLogisticsAPIUrl()	+ API_CONTEXT 
												+ FDStoreProperties.getLogisticsCompanyCode() 
													+ path;
	}
	
	public String getOMSEndPoint(String path){
		return FDStoreProperties.getOMSAPIUrl()	+ OMS_API_CONTEXT 
												+ FDStoreProperties.getLogisticsCompanyCode() 
													+ path;
	}
}
