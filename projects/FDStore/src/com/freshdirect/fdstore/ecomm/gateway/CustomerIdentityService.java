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
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerIdentityService extends AbstractEcommService implements CustomerIdentityServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(RegistrationService.class);

	private static final String LOGIN = "/customerIdentity/login";

	private static final String RECOGNIZE = "/customerIdentity/recognize";

	private static CustomerIdentityServiceI INSTANCE;

	public static CustomerIdentityServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerIdentityService();

		return INSTANCE;
	}

	@Override
	public FDIdentity login(String userId, String password)
			throws FDAuthenticationException, FDResourceException, RemoteException {
		Response<FDIdentity> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("userId", userId);
			rootNode.put("password", password);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOGIN),
					new TypeReference<Response<FDIdentity>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("FDAuthenticationException".equals(response.getMessage())) {
					throw new FDAuthenticationException(
							response.getError().get("FDAuthenticationException").toString());
				} else if ("FDResourceException".equals(response.getMessage())) {
					throw new FDResourceException(response.getError().get("FDResourceException").toString());
				}
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public FDUser recognize(FDIdentity identity, EnumEStoreId eStoreId, boolean lazy, boolean populateDeliveryPlantInfo)
			throws FDAuthenticationException, FDResourceException, RemoteException {
		Response<FDUser> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();

			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.set("eStoreId", getMapper().convertValue(eStoreId, JsonNode.class));
			rootNode.put("lazy", lazy);
			rootNode.put("populateDeliveryPlantInfo", populateDeliveryPlantInfo);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(RECOGNIZE),
					new TypeReference<Response<FDUser>>() {
					});
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

}
