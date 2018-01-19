package com.freshdirect.fdstore.ewallet.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Category;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.braintreegateway.exceptions.AuthenticationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.ecommerce.data.attributes.FlatAttributeCollection;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ewallet.MasterPassGatewayRequest;
import com.freshdirect.fdstore.ewallet.MasterPassGatewayResponse;
import com.freshdirect.fdstore.ewallet.PaymentData;
import com.freshdirect.fdstore.ewallet.impl.MasterpassData;
//import com.freshdirect.fdstore.ewallet.impl.EWalletRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;


import com.freshdirect.payment.service.AbstractService;
import com.mastercard.mcwallet.sdk.MasterPassServiceRuntimeException;
import com.mastercard.mcwallet.sdk.xml.allservices.MerchantTransactions;

public class MasterPassGateway extends AbstractService implements IMasterPassService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(MasterPassGateway.class);

	private static final String CREATE_CUSTOMER_API ="/create/customer";
	private static final String PRE_CHECKOUT_DATA_API ="/preCheckoutData";
	private static final String EXPRESS_CHECKOUT_DATA_API ="/expressCheckoutData";
	private static final String PAIRIING_TOKEN_API ="/pairingToken";
	private static final String ACCESS_TOKEN_API ="/accessToken";
	private static final String LONG_ACCESS_TOKEN_API ="/longAccessToken";
	private static final String CHECKOUT_DATA_API ="/checkoutData";
	private static final String REQUEST_TOK_REDURL_API ="/requestTokenAndRedirectUrl";
	private static final String POST_SHOPPING_API ="/postShoppingCart";
	private static final String POST_MERCHENT_INIT_API ="/postMerchantInit";

	private static IMasterPassService INSTANCE;


	public static IMasterPassService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MasterPassGateway();

		return INSTANCE;
	}

	public <T> T postData(String inputJson, String url, Class<T> clazz) throws FDResourceException {

		try {
			
			HttpEntity<String> entity = getEntity(inputJson);
			RestTemplate restTemplate = getRestTemplate();
			ResponseEntity<T> response = restTemplate.postForEntity(new URI(url), entity, clazz);
			return response.getBody();
		} catch (RestClientException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("api url:" + url);
			LOGGER.error("input json:" + inputJson);
			throw new FDResourceException("MasterPass API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("api url:" + url);
			throw new FDResourceException("API syntax error");
		}
	}

	
	public <T,E> T postDataTypeMap(String inputJson, String url, TypeReference<E> type)
			throws FDResourceException {
		
		T responseOfTypestring = null;
		RestTemplate restTemplate = getRestTemplate();
		ResponseEntity<String> response;

		HttpEntity<String> entity = getEntity(inputJson);
		try {
			response = restTemplate.postForEntity(new URI(url), entity, String.class);
			responseOfTypestring = getMapper().readValue(response.getBody(), type);
		} catch (JsonParseException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("api url:" + url);
			throw new FDResourceException(e, "Json Parsing failure");
		} catch (JsonMappingException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("api url:" + url);
			throw new FDResourceException(e, "Json Mapping failure");
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("api url:" + url);
			throw new FDResourceException(e, "API connection failure");
		} catch (RestClientException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("api url:" + url);
			throw new FDResourceException(e, "API connection failure");
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("api url:" + url);
			throw new FDResourceException(e, "API Syntax failure");
		}
		
		return responseOfTypestring;
	}
	

	@Override
	public MasterpassData getPreCheckoutData(MasterpassData data,
			PaymentData paymentData) throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(data);
		masterPassGatewayRequest.setPaymentData(paymentData);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		try{
		LOGGER.info("getExpressCheckoutData : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(PRE_CHECKOUT_DATA_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}



	@Override
	public MasterpassData getExpressCheckoutData(MasterpassData data)
			throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(data);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		try{
		LOGGER.info("getExpressCheckoutData : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(EXPRESS_CHECKOUT_DATA_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}



	@Override
	public MasterpassData getPairingToken(MasterpassData data) throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(data);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		try{
		LOGGER.info("getPairingToken : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(PAIRIING_TOKEN_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}



	@Override
	public MasterpassData getAccessToken(MasterpassData data) throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(data);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		try{
			LOGGER.info("getAccessToken : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(ACCESS_TOKEN_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}



	@Override
	public MasterpassData getLongAccessToken(MasterpassData data)
			throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(data);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		try{
			LOGGER.info("getLongAccessToken : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(LONG_ACCESS_TOKEN_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}



	@Override
	public MasterpassData getCheckoutData(MasterpassData command)
			throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(command);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		try{
			LOGGER.info("getCheckoutData : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(CHECKOUT_DATA_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}



	@Override
	public MasterpassData getRequestTokenAndRedirectUrl(MasterpassData data)
			throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(data);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		
		try{
			LOGGER.info("getRequestTokenAndRedirectUrl : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(REQUEST_TOK_REDURL_API),new TypeReference<MasterPassGatewayResponse>() {});
		  if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}



	@Override
	public MasterpassData postShoppingCart(MasterpassData data,
			String shoppingCartRequestasXML) throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(data);
		masterPassGatewayRequest.setShoppingCartRequestasXML(shoppingCartRequestasXML);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		try{
			LOGGER.info("postShoppingCart : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(POST_SHOPPING_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}



	@Override
	public MasterpassData postMerchantInit(MasterpassData data)
			throws Exception {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(data);
		String inputJson = buildRequest(masterPassGatewayRequest);
		MasterPassGatewayResponse  response = null;
		try{
			LOGGER.info("postMerchantInit : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(POST_MERCHENT_INIT_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());

		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		return response.getMasterpassData();
		
	}

	
	@Override
	public MerchantTransactions postCheckoutTransaction(MasterpassData mpData,
			MerchantTransactions reqTrxns)
			throws MasterPassServiceRuntimeException {
		MasterPassGatewayRequest masterPassGatewayRequest = new MasterPassGatewayRequest();
		masterPassGatewayRequest.setMasterpassData(mpData);
		masterPassGatewayRequest.setMerchantTransactions(reqTrxns);
		String inputJson;
		MasterPassGatewayResponse  response = null;
		try{
		inputJson = buildRequest(masterPassGatewayRequest);
		LOGGER.info("postCheckoutTransaction : Payload : "+inputJson);
		  response = postDataTypeMap(inputJson,getMasterPassEndPoint(POST_MERCHENT_INIT_API),new TypeReference<MasterPassGatewayResponse>() {});
		    if(response.getStatus()!=null&&response.getStatus().equals(com.freshdirect.payment.Result.STATUS_FAILED))
			  throw new Exception(response.getStatusMessage());

		}catch (FDPayPalServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception exception){
			LOGGER.error(exception.getMessage());
			exception.printStackTrace();
			throw new MasterPassServiceRuntimeException(exception.getMessage());
		}
		return response.getMerchantTransactions();
		
	}
	
	
}
