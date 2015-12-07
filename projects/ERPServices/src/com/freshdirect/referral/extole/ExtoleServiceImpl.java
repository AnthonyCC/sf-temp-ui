package com.freshdirect.referral.extole;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;

public class ExtoleServiceImpl implements ExtoleService {

	private static final Logger LOG = Logger.getLogger(ExtoleServiceImpl.class);

	private static final String SCHEME_HTTPS = FDStoreProperties
			.get(FDStoreProperties.PROP_SCHEME_HTTPS);
	private static final String BASE_URL = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_BASE_URL);

	private static final String ENDPOINT_CREATE_CONVERSION = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_ENDPOINT_CREATE_CONVERSION);
	private static final String ENDPOINT_APPROVE_CONVERSION = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_ENDPOINT_APPROVE_CONVERSION);

	private static final String API_KEY = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_API_KEY);
	private static final String API_SECRET = FDStoreProperties
			.get(FDStoreProperties.PROP_EXTOLE_API_SECRET);

	private static final String ETL_PARAM_EVENT_TYPE = "event_type";
	private static final String ETL_PARAM_FIRST_NAME = "f";
	private static final String ETL_PARAM_LAST_NAME = "l";
	private static final String ETL_PARAM_EMAIL = "e";
	private static final String ETL_PARAM_CLICK_ID = "via_click_id";
	private static final String ETL_PARAM_COUPON_CODE = "coupon_code";
	private static final String ETL_PARAM_EVENT_DATE = "event_date";
	private static final String ETL_PARAM_PARTNER_CONVERSION_ID = "partner_conversion_id";
	private static final String ETL_PARAM_PARTNER_USER_ID = "partner_user_id";
	private static final String ETL_PARAM_EVENT_STATUS = "event_status";
	private static final String ETL_PARAM_EVENT_ID = "event_id";
	private static final String ETL_PARAM_NOTE = "note";

	CloseableHttpClient httpclient = null;
	CloseableHttpResponse response = null;

	@Override
	public ExtoleResponse createConversion(ExtoleConversionRequest conversion)
			throws ExtoleServiceException, IOException {
		String jsonResponse = null;
		try {
			URI uri = buildExtoleUrl(ENDPOINT_CREATE_CONVERSION, conversion);
			System.out.println(uri.toString());
			httpclient = HttpClients.createDefault();
			HttpGet getRequest = new HttpGet(uri);
			getRequest.addHeader("content-type", "application/json");
			response = httpclient.execute(getRequest,
					getHttpClientAuthContext());

			HttpEntity entity = response.getEntity();
			LOG.debug(entity);
			jsonResponse = EntityUtils.toString(entity);
			LOG.debug(jsonResponse);
			// consume the entity content and ,if exists content stream, close
			// it.
			EntityUtils.consume(entity);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtoleServiceException(e.getMessage());
		} 
		return parseResponse(jsonResponse);
	}

	@Override
	public ExtoleResponse approveConversion(
			ExtoleConversionRequest approveConversion)
			throws ExtoleServiceException, IOException {
		String jsonResponse = null;
		try {
			URI uri = buildExtoleUrl(ENDPOINT_APPROVE_CONVERSION,
					approveConversion);
			// System.out.println(uri.toString());
			httpclient = HttpClients.createDefault();
			HttpGet getRequest = new HttpGet(uri);
			getRequest.addHeader("content-type", "application/json");
			response = httpclient.execute(getRequest,
					getHttpClientAuthContext());

			// printing the status code and reason phrase
			LOG.debug("Status code : "
					+ response.getStatusLine().getStatusCode());
			LOG.debug("Status line : "
					+ response.getStatusLine().getReasonPhrase());

			HttpEntity entity = response.getEntity();
			jsonResponse = EntityUtils.toString(entity);
			// consume the entity content and ,if exists content stream, close
			// it.
			EntityUtils.consume(entity);

			LOG.debug(jsonResponse);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtoleServiceException(e.getMessage());
		} 
		return parseResponse(jsonResponse);

	}

	/**
	 * Create extole endpoint urls and prepare the get parameters
	 * 
	 * @param endpoint
	 * @param conversion
	 * @return URI
	 * @throws URISyntaxException
	 */
	private URI buildExtoleUrl(String endpoint,
			ExtoleConversionRequest conversion) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(SCHEME_HTTPS);
		uriBuilder.setHost(BASE_URL);
		uriBuilder.setPath(endpoint);

		if (conversion != null) {
			if (conversion.getEventType() != null) {

				uriBuilder.setParameter(ETL_PARAM_EVENT_TYPE, conversion
						.getEventType().toString());
			}
			if (conversion.getFirstName() != null) {
				uriBuilder.setParameter(ETL_PARAM_FIRST_NAME,
						conversion.getFirstName());
			}
			if (conversion.getLastName() != null) {
				uriBuilder.setParameter(ETL_PARAM_LAST_NAME,
						conversion.getLastName());
			}
			if (conversion.getEmail() != null) {
				uriBuilder.setParameter(ETL_PARAM_EMAIL, conversion.getEmail());
			}
			if (conversion.getEventDate() != null) {
				uriBuilder.setParameter(ETL_PARAM_EVENT_DATE,
						conversion.getEventDate());
			}
			if (conversion.getCouponCode() != null) {
				uriBuilder.setParameter(ETL_PARAM_COUPON_CODE,
						conversion.getCouponCode());
			}
			if (conversion.getClickId() != null) {
				uriBuilder.setParameter(ETL_PARAM_CLICK_ID,
						conversion.getClickId());
			}
			if (conversion.getPartnerUserId() != null) {
				uriBuilder.setParameter(ETL_PARAM_PARTNER_USER_ID,
						conversion.getPartnerUserId());
			}
			if (conversion.getEventid() != null) {
				uriBuilder.setParameter(ETL_PARAM_EVENT_ID,
						conversion.getEventid());
			}
			if (conversion.getEventStatus() != null) {
				uriBuilder.setParameter(ETL_PARAM_EVENT_STATUS,
						conversion.getEventStatus());
			}
			if (conversion.getNote() != null) {
				uriBuilder.setParameter(ETL_PARAM_NOTE, conversion.getNote());
			}
			if (conversion.getPartnerConversionId() != null) {
				uriBuilder.setParameter(ETL_PARAM_PARTNER_CONVERSION_ID,
						conversion.getPartnerConversionId());
			}
		}
		return uriBuilder.build();
	}

	/**
	 * create the http client authentication context used to authenticate the
	 * request on the Extole server
	 * 
	 * @return HttpClientContext
	 */
	private HttpClientContext getHttpClientAuthContext() {
		// this method is for server authentication using api key
		HttpClientContext context = new HttpClientContext();
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		// setting the api key and password
		Credentials defaultCredentials = new UsernamePasswordCredentials(
				API_KEY, API_SECRET);
		credentialsProvider.setCredentials(AuthScope.ANY, defaultCredentials);
		context.setCredentialsProvider(credentialsProvider);
		return context;
	}

	/**
	 * Parse the Extole server Json response and convert into an object
	 * 
	 * @param jsonResponse
	 * @return ExtoleResponse
	 * @throws ExtoleServiceException
	 */
	private ExtoleResponse parseResponse(String jsonResponse)
			throws ExtoleServiceException {
		ExtoleResponse response = null;
		ObjectMapper mapper = getObjectMapper();
		try {
			// response = mapper.readValue(jsonResponse, _class);
			response = mapper.readValue(jsonResponse, ExtoleResponse.class);
		} catch (Exception e) {
			throw new ExtoleServiceException(e.getMessage());
		}

		return response;
	}

	/**
	 * Configure a Json object mapper to serialize string to object and
	 * deserialize object to string
	 * 
	 * @return ObjectMapper
	 */
	private ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		return mapper;
	}

}
