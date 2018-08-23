package com.freshdirect.ecomm.gateway;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.freshdirect.customer.ErpPaymentMethodDeserializer;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class AbstractBackOfficeService {

	private static final String BACKOFFICE_API_CONTEXT = "/FDService/service/transops/v0";

	private static final RestTemplate restTemplate;
	private static final Category LOGGER = LoggerFactory.getInstance(AbstractBackOfficeService.class);

	private static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper()
				.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"))
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(
						new SimpleModule().addDeserializer(ErpPaymentMethodI.class, new ErpPaymentMethodDeserializer()));
				MAPPER.getFactory()
				.configure(Feature.ESCAPE_NON_ASCII, true);
				
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
		converters.add(new ResourceHttpMessageConverter());
		converters.add(getMappingJackson2HttpMessageConverter());
		restTemplate = new RestTemplate(converters);

		PoolingHttpClientConnectionManager cManager = new PoolingHttpClientConnectionManager();
		cManager.setMaxTotal(FDStoreProperties.getBackOfficeConnectionPool());
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(FDStoreProperties.getBackOfficeConnectionTimeout() * 1000)
				.setConnectTimeout(FDStoreProperties.getBackOfficeConnectionTimeout() * 1000)
				.setConnectionRequestTimeout(FDStoreProperties.getBackOfficeConnectionRequestTimeout() * 1000).build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
				.setConnectionManager(cManager).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		requestFactory.setReadTimeout(FDStoreProperties.getBackOfficeConnectionReadTimeout() * 1000);
		requestFactory.setConnectTimeout(FDStoreProperties.getBackOfficeConnectionTimeout() * 1000);
		restTemplate.setRequestFactory(requestFactory);
		
	}

	public <T> T postData(String inputJson, String url, Class<T> clazz) throws FDResourceException {
		ResponseEntity<T> response=null;
		try {
			HttpEntity<String> entity = getEntity(inputJson);
			RestTemplate restTemplate = getRestTemplate();
			response = restTemplate.postForEntity(new URI(url), entity, clazz);
			return  response.getBody();
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:" + url);
			LOGGER.info("input json:" + inputJson);
			throw new FDResourceException("BackOffice API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:" + url);
			throw new FDResourceException("API syntax error");
		}
		
		
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
		try {
			RestTemplate restTemplate = getRestTemplate();
			ResponseEntity<T> response = restTemplate.getForEntity(new URI(url), clazz);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:" + url);
			throw new FDResourceException("API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:" + url);
			throw new FDResourceException("API syntax error");
		}
	}
	
	protected <T> T httpGetData( String url, Class<T> clazz, Object[] params) throws FDResourceException {
		
		try {
			RestTemplate restTemplate = getRestTemplate();		
			ResponseEntity<T> response = restTemplate.getForEntity(url, clazz, params);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
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
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
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
			LOGGER.info(e.getMessage());
			throw new FDEcommServiceException("Unable to process the request.");
		}
	}

	public String getBackOfficeEndPoint(String path) {
		return FDStoreProperties.getBackOfficeApiUrl() + BACKOFFICE_API_CONTEXT + path;
		
	}
	
	public <T> T postData(String inputJson, String url, Class<T> clazz,
			Object[] params) throws FDResourceException {

		try {
			HttpEntity<String> entity = getEntity(inputJson);

			RestTemplate restTemplate = getRestTemplate();

			ResponseEntity<T> response = restTemplate.postForEntity(url,
					entity, clazz, params);

			return response.getBody();

		} catch (RestClientException e) {

			LOGGER.info(e.getMessage());

			LOGGER.info("api url:" + url);

			LOGGER.info("input json:" + inputJson);

			throw new FDResourceException("BackOffice API connection failure");

		}

	}

}
