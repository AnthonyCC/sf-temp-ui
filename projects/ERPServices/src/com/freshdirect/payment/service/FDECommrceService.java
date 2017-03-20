package com.freshdirect.payment.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Category;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.exceptions.AuthenticationException;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.fdstore.FDResourceException;
//import com.freshdirect.fdstore.ewallet.impl.EWalletRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;


import com.freshdirect.payment.PayPalRequest;
import com.freshdirect.payment.PayPalResponse;
import com.freshdirect.payment.gateway.GatewayType;
import com.freshdirect.payment.gateway.Request;
import com.freshdirect.payment.gateway.Response;

public class FDECommrceService extends AbstractService implements IECommrceService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDECommrceService.class);

	
	private static FDECommrceService INSTANCE;


	public static FDECommrceService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDECommrceService();

		return INSTANCE;
	}

protected <T> T postData(String inputJson, String url, Class<T> clazz) throws FDResourceException {
		
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
			throw new FDResourceException("PayPal API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException("API syntax error");
		}
	}
	
	/**
	 *  This method implementation is to get data for the HTTP GET
	 * @param url
	 * @param clazz
	 * @return
	 * @throws FDResourceException
	 */
	protected <T> T getData( String url, Class<T> clazz) throws FDResourceException {
		
		try {
			RestTemplate restTemplate = getRestTemplate();		
			ResponseEntity<T> response = restTemplate.getForEntity(new URI(url),clazz);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException("API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.info(e.getMessage());
			LOGGER.info("api url:"+url);
			throw new FDResourceException("API syntax error");
		}
	}
	




	
}
