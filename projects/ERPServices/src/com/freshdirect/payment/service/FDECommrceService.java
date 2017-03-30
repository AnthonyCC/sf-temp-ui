package com.freshdirect.payment.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
//import com.freshdirect.fdstore.ewallet.impl.EWalletRuntimeException;

public class FDECommrceService extends AbstractService implements IECommrceService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDECommrceService.class);

	private static final String SAP_PRODUCT_FAMILY_LOADER_GET_SKUCODE_BYPRODFLY_API ="dataloader/sap/skucodesbyproductfamily";
	private static final String SAP_PRODUCT_FAMILY_LOADER_LOAD_API ="dataloader/sap/productfamily";
	
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
	
	@Override
	public Map<String,List<String>> updateCacheWithProdFly(
			List<String> familyIds) throws FDPayPalServiceException {
		try {
			Request<List<String>> request = new Request<List<String>>();
			request.setData(familyIds);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<Map<String,List<String>>> response = getData(inputJson, getFdCommerceEndPoint(SAP_PRODUCT_FAMILY_LOADER_GET_SKUCODE_BYPRODFLY_API), Response.class);
			if(response != null && response.getData() != null){
				return response.getData();
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDPayPalServiceException(e, "Unable to process the request.");
		}
		return null;
	}

	@Override
	public String loadData(List<ErpProductFamilyModel> productFamilyList)
			throws FDPayPalServiceException {
		try {
			Request<List<ErpProductFamilyModel>> request = new Request<List<ErpProductFamilyModel>>();
			request.setData(productFamilyList);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<String> response = getData(inputJson, getFdCommerceEndPoint(SAP_PRODUCT_FAMILY_LOADER_LOAD_API), Response.class);
			if(response != null && response.getData() != null){
				return response.getData();
			}
		} catch (FDPayPalServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDPayPalServiceException(e, "Unable to process the request.");
		}
		return null;
	}

	
}
