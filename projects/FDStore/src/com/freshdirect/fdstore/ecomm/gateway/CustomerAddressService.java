package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerAddressService extends AbstractEcommService implements CustomerAddressServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerAddressService.class);

	private static final String ASSUME_DELIVERY_ADDRESS = "/customerAddress/assumeDeliveryAddress";

	private static final String GET_PARENT_ORDER_ADDRESS_ID = "/customerAddress/parentOrderAddressId";

	private static CustomerAddressServiceI INSTANCE;

	public static CustomerAddressServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerAddressService();

		return INSTANCE;
	}

	@Override
	public ErpAddressModel assumeDeliveryAddress(FDIdentity identity, String lastOrderId)
			throws FDResourceException, RemoteException {
		Response<ErpAddressModel> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.put("lastOrderId", lastOrderId);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ASSUME_DELIVERY_ADDRESS),
					new TypeReference<Response<ErpAddressModel>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				
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
	public String getParentOrderAddressId(String parentOrderAddressId) throws FDResourceException {
		Response<String> response = null;
	
		response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_PARENT_ORDER_ADDRESS_ID + "/" + parentOrderAddressId),  new TypeReference<Response<String>>(){});
		if(!response.getResponseCode().equals("OK")){
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
		
	}

}
