package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerPreferenceService extends AbstractEcommService implements CustomerPreferenceServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerPreferenceService.class);

	private static final String GET_GO_GREEN = "/customerPreference/goGreen";
	private static final String STORE_GO_GREEN = "/customerPreference/goGreen";

	private static final String STORE_MOBILE = "/customerPreference/mobile";
	private static final String STORE_EMAIL = "/customerPreference/email";
	private static final String STORE_SMS = "/customerPreference/sms";
	private static CustomerPreferenceServiceI INSTANCE;

	public static CustomerPreferenceServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerPreferenceService();

		return INSTANCE;
	}

	@Override
	public String loadGoGreenPreference(String customerId) throws FDResourceException, RemoteException {
		Response<String> response = null;

		response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_GO_GREEN + "/" + customerId),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}

	@Override
	public void storeGoGreenPreferences(String customerId, String goGreen) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();

			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("customerId", customerId);
			rootNode.put("goGreen", goGreen);
			request.setData(rootNode);

			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_GO_GREEN),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void storeMobilePreferences(String customerId, String fdCustomerId, String mobileNumber, String textOffers, String textDelivery,
			String orderNotices, String orderExceptions, String offers, String partnerMessages, EnumEStoreId eStoreId)
			throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();

			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("customerId", customerId);
			rootNode.put("fdCustomerId", fdCustomerId);
			rootNode.put("mobileNumber", mobileNumber);
			rootNode.put("textOffers", textOffers);
			rootNode.put("textDelivery", textDelivery);
			rootNode.put("orderNotices", orderNotices);
			rootNode.put("orderExceptions", orderExceptions);
			rootNode.put("offers", offers);
			rootNode.put("partnerMessages", partnerMessages);
			rootNode.set("eStoreId", getMapper().convertValue(eStoreId, JsonNode.class));
			request.setData(rootNode);

			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_MOBILE),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public void storeSmsPrefereceFlag(String fdCustomerId, String flag, EnumEStoreId eStoreId)
			throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();

			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("fdCustomerId", fdCustomerId);
			rootNode.put("flag", flag);
			rootNode.set("eStoreId", getMapper().convertValue(eStoreId, JsonNode.class));
			request.setData(rootNode);

			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_SMS),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public void storeEmailPreferenceFlag(String fdCustomerId, String flag, EnumEStoreId eStoreId)
			throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();

			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("fdCustomerId", fdCustomerId);
			rootNode.put("flag", flag);
			rootNode.set("eStoreId", getMapper().convertValue(eStoreId, JsonNode.class));
			request.setData(rootNode);

			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_EMAIL),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
	}

}
