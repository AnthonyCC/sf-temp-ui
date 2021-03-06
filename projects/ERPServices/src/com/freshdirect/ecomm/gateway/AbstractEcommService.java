package com.freshdirect.ecomm.gateway;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Category;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.address.PhoneNumberDeserializer;
import com.freshdirect.customer.ErpPaymentMethodDeserializer;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpTransactionModel;
import com.freshdirect.customer.ErpTransactionModelDeserializer;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.monitor.RequestLogger;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class AbstractEcommService {

	private static final String FDCOMMERCE_API_CONTEXT = "/fdcommerceapi/fd/v1/";

	private static final RestTemplate restTemplate;
	private static final Category LOGGER = LoggerFactory.getInstance(AbstractEcommService.class);

	private static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"))
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(
						new SimpleModule().addDeserializer(ErpPaymentMethodI.class, new ErpPaymentMethodDeserializer())
								.addDeserializer(PhoneNumber.class, new PhoneNumberDeserializer())
								.addDeserializer(ErpTransactionModel.class, new ErpTransactionModelDeserializer()));
		MAPPER.getFactory().configure(Feature.ESCAPE_NON_ASCII, true);
				
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new ResourceHttpMessageConverter());
		converters.add(getMappingJackson2HttpMessageConverter());
		
		// add interceptors
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new HeaderRequestInterceptor(RequestLogger.FD_ORIGIN_NAME, RequestLogger.getServerName()));

		restTemplate = new RestTemplate(converters);
		restTemplate.setInterceptors(interceptors);

		PoolingHttpClientConnectionManager cManager = new PoolingHttpClientConnectionManager();
		
		// EcommService have no specific route, maxTotal = maxPerRotue. 
		cManager.setMaxTotal(FDStoreProperties.getLogisticsConnectionPool());
		cManager.setDefaultMaxPerRoute(FDStoreProperties.getLogisticsConnectionPool());
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(FDStoreProperties.getLogisticsConnectionTimeout() * 1000)
				.setConnectTimeout(FDStoreProperties.getLogisticsConnectionTimeout() * 1000)
				.setConnectionRequestTimeout(FDStoreProperties.getLogisticsConnectionRequestTimeout() * 1000).build();
		
		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.evictExpiredConnections()
				.disableCookieManagement()
				.setConnectionManager(cManager);
		if(!FDStoreProperties.isRetryEnabled()){
			httpClientBuilder.disableAutomaticRetries();
		}
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate.setRequestFactory(requestFactory);
	
	}
	

	protected <T> T parseResponse(Response<T> info) throws FDResourceException {
		if(info!=null && info.getResponseCode().equalsIgnoreCase("OK")){
			return info.getData();
		}else{
			throw new FDResourceException("API error");
		}
	}
	

	public <T> T postData(String inputJson, String url, Class<T> clazz) throws FDResourceException {

		try {
			long starttime = System.currentTimeMillis();
			HttpEntity<String> entity = getEntity(inputJson);
			RestTemplate restTemplate = getRestTemplate();
			ResponseEntity<T> response = restTemplate.postForEntity(new URI(url), entity, clazz);
			long endTime = System.currentTimeMillis() - starttime;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDSFGatewayStatsLogging)) {
				StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
				StackTraceElement stackElem = stackTraceElements[stackTraceElements.length
						- (stackTraceElements.length - 2)];

				LOGGER.info(String.format("classname: %s, method: %s elapsed time: %s ms", stackElem.getClassName(),
						stackElem.getMethodName(), endTime));
			}
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.error("api url:" + url, e);
			LOGGER.error("input json:" + inputJson);
			throw new FDResourceException("EComm API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.error("api url:" + url, e);
			LOGGER.error("input json:" + inputJson);
			throw new FDResourceException("API syntax error");
		}
	}

	/**
	 * This method implementation is to get data for the HTTP GET
	 * 
	 * @param url
	 * @param clazz
	 * @return
	 * @throws FDResourceException
	 */
	public <T, E> Response<T> httpGetDataTypeMap(String url, TypeReference<E> type) throws FDResourceException {
		long starttime = System.currentTimeMillis();
		Response<T> responseOfTypestring = null;
		RestTemplate restTemplate = getRestTemplate();
		ResponseEntity<String> response;
		String responseBody = null;
		try {
			response = restTemplate.getForEntity(new URI(url), String.class);
			responseBody = response.getBody();
			responseOfTypestring = getMapper().readValue(responseBody, type);
		} catch (JsonParseException e) {
			LOGGER.error("JsonParseException: url=" + url + ", requestBody=" + (responseBody != null? responseBody: ""), e);
			throw new FDResourceException(e, "Json Parsing failure");
		} catch (JsonMappingException e) {
			LOGGER.error("JsonMappingException: url" + url + ", requestBody=" + (responseBody != null? responseBody: ""), e);
			throw new FDResourceException(e, "Json Mapping failure");
		} catch (IOException e) {
			LOGGER.error("IOException: url" + url, e);
			throw new FDResourceException(e, "API connection failure");
		} catch (RestClientException e) {
			LOGGER.error("RestClientException: url" + url, e);
			throw new FDResourceException(e, "API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.error("URISyntaxException: url" + url, e);
			throw new FDResourceException(e, "API Syntax failure");
		}
		long endTime = System.currentTimeMillis() - starttime;
		if (FDEcommProperties.isFeatureEnabled(FDEcommProperties.FDSFGatewayStatsLogging)) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			StackTraceElement stackElem = stackTraceElements[stackTraceElements.length
					- (stackTraceElements.length - 2)];

			LOGGER.info(String.format("WebserviceExternalCall classname=%s, method= %s elapsed time=%s ms",
					stackElem.getClassName(), stackElem.getMethodName(), endTime));
		}

		return responseOfTypestring;
	}

	public <T, E> Response<T> postDataTypeMap(String inputJson, String url, TypeReference<E> type)
			throws FDResourceException {
		Response<T> responseOfTypestring = null;
		RestTemplate restTemplate = getRestTemplate();
		ResponseEntity<String> response;

		HttpEntity<String> entity = getEntity(inputJson);
		try {
			response = restTemplate.postForEntity(new URI(url), entity, String.class);
			responseOfTypestring = getMapper().readValue(response.getBody(), type);
		} catch (JsonParseException e) {
			LOGGER.error("JsonParseException: url=" + url, e);
			throw new FDResourceException(e, "Json Parsing failure");
		} catch (JsonMappingException e) {
			LOGGER.error("JsonMappingException: url" + url, e);
			throw new FDResourceException(e, "Json Mapping failure");
		} catch (IOException e) {
			LOGGER.error("IOException: url" + url, e);
			throw new FDResourceException(e, "API connection failure");
		} catch (RestClientException e) {
			LOGGER.error("RestClientException: url" + url, e);
			throw new FDResourceException(e, "API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.error("URISyntaxException: url" + url, e);
			throw new FDResourceException(e, "API Syntax failure");
		}
		
		return responseOfTypestring;
	}
	
	public <T, E> Response<T> postMFormDataTypeMap(MultiValueMap<String, Object> body, String url, TypeReference<E> type)
			throws FDResourceException {
		Response<T> responseOfTypestring = null;
		RestTemplate restTemplate = getRestTemplate();
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		ResponseEntity<String> response;

		HttpEntity<MultiValueMap<String, Object>> entity = getMFormEntity(body);
		try {
			response = restTemplate.postForEntity(new URI(url), entity, String.class);
			responseOfTypestring = getMapper().readValue(response.getBody(), type);
		} catch (JsonParseException e) {
			LOGGER.error("Json Parsing failure", e);
			LOGGER.error("api url:" + url);
			throw new FDResourceException(e, "Json Parsing failure");
		} catch (JsonMappingException e) {
			LOGGER.error("Json Mapping failure", e);
			LOGGER.error("api url:" + url);
			throw new FDResourceException(e, "Json Mapping failure");
		} catch (IOException e) {
			LOGGER.error("IOException: url" + url, e);
			throw new FDResourceException(e, "API connection failure");
		} catch (RestClientException e) {
			LOGGER.error("RestClientException: url" + url, e);
			throw new FDResourceException(e, "API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.error("URISyntaxException: url" + url, e);
			throw new FDResourceException(e, "API Syntax failure");
		}
		/*long endTime = System.currentTimeMillis() - starttime;
		if (FDEcommProperties.isFeatureEnabled(FDEcommProperties.FDSFGatewayStatsLogging)) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			StackTraceElement stackElem = stackTraceElements[stackTraceElements.length
					- (stackTraceElements.length - 2)];

			LOGGER.info(String.format("WebserviceExternalCall classname=%s, method= %s elapsed time=%s ms",
					stackElem.getClassName(), stackElem.getMethodName(), endTime));
		}*/

		return responseOfTypestring;
	}


	private HttpEntity<MultiValueMap<String, Object>>  getMFormEntity(MultiValueMap<String, Object> body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity
		 = new HttpEntity<MultiValueMap<String, Object>> (body, headers);

		return requestEntity;
	}


	/**
	 * This method implementation is to get data for the HTTP GET
	 * 
	 * @param url
	 * @param clazz
	 * @return
	 * @throws FDLogisticsServiceException
	 */
	public <T> T httpGetData(String url, Class<T> clazz) throws FDResourceException {
		long starttime = System.currentTimeMillis();
		try {
			RestTemplate restTemplate = getRestTemplate();
			ResponseEntity<T> response = restTemplate.getForEntity(new URI(url), clazz);

			long endTime = System.currentTimeMillis() - starttime;
			if (FDEcommProperties.isFeatureEnabled(FDEcommProperties.FDSFGatewayStatsLogging)) {
				StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
				StackTraceElement stackElem = stackTraceElements[stackTraceElements.length
						- (stackTraceElements.length - 2)];

				LOGGER.info(String.format("WebserviceExternalCall classname=%s, method= %s elapsed time=%s ms",
						stackElem.getClassName(), stackElem.getMethodName(), endTime));
			}

			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.error("api url:" + url, e);
			throw new FDResourceException("API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.error("api url:" + url, e);
			throw new FDResourceException("API syntax error");
		}
	}
	
	protected <T> T httpGetData( String url, Class<T> clazz, Object[] params) throws FDResourceException {
		
		try {
			RestTemplate restTemplate = getRestTemplate();		
			ResponseEntity<T> response = restTemplate.getForEntity(url, clazz, params);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.error("api url:"+url);
			throw new FDResourceException(e, "API connection failure");
		} 
	}

	protected <T> T httpPostData( String url, String inputJson, Class<T> clazz, Object[] params) throws FDResourceException {
		
		try {
			RestTemplate restTemplate = getRestTemplate();	
			HttpEntity<String> entity = getEntity(inputJson);
			
			ResponseEntity<T> response = restTemplate.postForEntity(url, entity, clazz, params);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.error("api url:"+url, e);
			throw new FDResourceException(e, "API connection failure");
		} 
	}


	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}

	protected static MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(getMapper());
		return converter;
	}

	public static ObjectMapper getMapper() {
		return MAPPER;
	}

	protected HttpEntity<String> getEntity(String inputJson) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = (inputJson != null) ? new HttpEntity<String>(inputJson, headers)
				: new HttpEntity<String>(headers);
		return entity;
	}

	protected String buildRequest(Object object) throws FDEcommServiceException {
		try {
			return getMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOGGER.error("error occured in buildRequest", e);
			throw new FDEcommServiceException("Unable to process the request.");
		}
	}

	public String getFdCommerceEndPoint(String path) {
		return FDStoreProperties.getFdCommerceApiUrl() + FDCOMMERCE_API_CONTEXT + path;
	}
	
	public <T> T postData(String inputJson, String url, Class<T> clazz,
			Object[] params) throws FDResourceException {

		try {

			long starttime = System.currentTimeMillis();

			HttpEntity<String> entity = getEntity(inputJson);

			RestTemplate restTemplate = getRestTemplate();

			ResponseEntity<T> response = restTemplate.postForEntity(url,
					entity, clazz, params);

			long endTime = System.currentTimeMillis() - starttime;

			if (FDStoreProperties
					.isSF2_0_AndServiceEnabled(FDEcommProperties.FDSFGatewayStatsLogging)) {

				StackTraceElement[] stackTraceElements = Thread.currentThread()
						.getStackTrace();

				StackTraceElement stackElem = stackTraceElements[stackTraceElements.length

						- (stackTraceElements.length - 2)];

				LOGGER.info(String.format(
						"classname: %s, method: %s elapsed time: %s ms",
						stackElem.getClassName(),

						stackElem.getMethodName(), endTime));

			}

			return response.getBody();

		} catch (RestClientException e) {
			LOGGER.error("api url:" + url, e);
			LOGGER.error("input json:" + inputJson);

			throw new FDResourceException("EComm API connection failure");

		}

	}

}
