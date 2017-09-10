package com.freshdirect.fdlogistics.services.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdlogistics.converter.EnumZoneServiceTypeToString;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class AbstractLogisticsService {
	
	private static final String API_CONTEXT = "/logisticsapi/v/1/";
	private static final String OMS_API_CONTEXT = "/fdlogistics/v/1/";
	private static final String FDCOMMERCE_API_CONTEXT = "/fdcommerceapi/fd/v1/";
	
	private static final RestTemplate restTemplate;
	private static final Category LOGGER = LoggerFactory.getInstance(AbstractLogisticsService.class);

	static {
	    
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new ResourceHttpMessageConverter());
		converters.add(getMappingJackson2HttpMessageConverter());
		restTemplate = new RestTemplate(converters);
		
		PoolingHttpClientConnectionManager cManager = new PoolingHttpClientConnectionManager();
		cManager.setMaxTotal(FDStoreProperties.getLogisticsConnectionPool());
		RequestConfig requestConfig = RequestConfig.custom()
			       .setSocketTimeout(FDStoreProperties.getLogisticsConnectionTimeout()*1000)
			       .setConnectTimeout(FDStoreProperties.getLogisticsConnectionTimeout()*1000)
			       .setConnectionRequestTimeout(FDStoreProperties.getLogisticsConnectionRequestTimeout()*1000)
			       .build();
		CloseableHttpClient httpClient = HttpClients
				.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(cManager)
			    .build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		
	 
	    requestFactory.setReadTimeout(FDStoreProperties.getLogisticsConnectionReadTimeout()*1000);
	    requestFactory.setConnectTimeout(FDStoreProperties.getLogisticsConnectionTimeout()*1000);
	    restTemplate.setRequestFactory(requestFactory);

	}

	
	public static MapperFacade getOrikaMapper(){
	    	MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
	    	mapperFactory.getConverterFactory().registerConverter(new EnumZoneServiceTypeToString());
	    	return mapperFactory.getMapperFacade();
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
			LOGGER.info("api url:"+url);
			LOGGER.info("input json:"+inputJson);
			throw new FDLogisticsServiceException(e,"API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDLogisticsServiceException("API syntax error");
		}
	}
	
	/**
	 *  This method implementation is to get data for the HTTP GET
	 * @param url
	 * @param clazz
	 * @return
	 * @throws FDLogisticsServiceException
	 */
	protected <T> T httpGetData( String url, Class<T> clazz) throws FDLogisticsServiceException {
		
		try {
			RestTemplate restTemplate = getRestTemplate();		
			ResponseEntity<T> response = restTemplate.getForEntity(new URI(url),clazz);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDLogisticsServiceException(e, "API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDLogisticsServiceException("API syntax error");
		}
	}
protected <T,E> Response<T> httpGetDataTypeMap( String url, TypeReference<E> type) throws FDLogisticsServiceException {
			Response<T> responseOfTypestring = null;
			RestTemplate restTemplate = getRestTemplate();	
			ResponseEntity<String> response;
			try {
				response = restTemplate.getForEntity(new URI(url),String.class);
				responseOfTypestring = getMapper().readValue(response.getBody(), type);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		return responseOfTypestring;
	}
	
	protected RestTemplate getRestTemplate(){
		return restTemplate;
	}
	
	protected static MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter(){
		MappingJackson2HttpMessageConverter converter =  new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(getMapper());
		return converter;
	}
	
	public static ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.getFactory().configure(Feature.ESCAPE_NON_ASCII, true);
		return mapper;
	}

	protected HttpEntity<String> getEntity(String inputJson){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = (inputJson != null)? new HttpEntity<String>(inputJson, headers): new HttpEntity<String>(headers);
		return entity;
	}
	
	protected String buildRequest(Object object) throws FDLogisticsServiceException{
		try {
			return getMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOGGER.info(e.getMessage());
			throw new FDLogisticsServiceException("Unable to process the request.");
		} 
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
	
	public String isDispatchedEndPoint(String path, String companyCode){
		return FDStoreProperties.getLogisticsAPIUrl()	+ API_CONTEXT 
				+ companyCode
					+ path;
		
	}
	
	public String getFdCommerceEndPoint(String path){
		return FDStoreProperties.getFdCommerceApiUrl()	+ FDCOMMERCE_API_CONTEXT + 
												 path;
	}
}
