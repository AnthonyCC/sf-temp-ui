package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerAddressService extends AbstractEcommService implements CustomerAddressServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerAddressService.class);

	private static final String ASSUME_DELIVERY_ADDRESS = "customerAddress/assumeDeliveryAddress";
	private static final String GET_PARENT_ORDER_ADDRESS_ID = "customerAddress/parentOrderAddressId";
	private static final String GET_ADDRESS = "customerAddress/address";
	private static final String GET_SHIPPING_ADDRESSES = "customerAddress/shippingAddresses";
	private static final String GET_DEFAULT_SHIPPING_ADDRESS = "customerAddress/defaultShippingAddress";
	private static final String SET_DEFAULT_SHIPPING_ADDRESS = "customerAddress/defaultShippingAddress";
	private static final String ADD_SHIPPING_ADDRESS = "customerAddress/shippingAddress/add";
	private static final String UPDATE_SHIPPING_ADDRESS = "customerAddress/shippingAddress/update";
	private static final String REMOVE_SHIPPING_ADDRESS = "customerAddress/shippingAddress/remove";

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
			getMapper().setSerializationInclusion(Include.NON_NULL);
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.put("lastOrderId", lastOrderId);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ASSUME_DELIVERY_ADDRESS),
					new TypeReference<Response<ErpAddressModel>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.warn("Error in CustomerAddressService.assumeDeliveryAddress: inputJson=" + inputJson
						+ ", response=" + response);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.assumeDeliveryAddress: identity=" + identity
					+ ", lastOrderId=" + lastOrderId, e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String getParentOrderAddressId(String parentOrderAddressId) throws FDResourceException {
		Response<String> response = null;

		response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_PARENT_ORDER_ADDRESS_ID + "/" + parentOrderAddressId),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error("Error in CustomerAddressService.getParentOrderAddressId: parentOrderAddressId="
					+ parentOrderAddressId);
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();

	}

	@Override
	public ErpAddressModel getAddress(FDIdentity identity, String addressId)
			throws FDResourceException, RemoteException {
		Response<ErpAddressModel> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			getMapper().setSerializationInclusion(Include.NON_NULL);
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.put("addressId", addressId);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_ADDRESS),
					new TypeReference<Response<ErpAddressModel>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error(
						"Error in CustomerAddressService.getAddress: data=" + inputJson + ", response=" + response);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.getAddress: identity=" + identity + ", addressId" + addressId,
					e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<ErpAddressModel> getShippingAddresses(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<List<ErpAddressModel>> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			getMapper().setSerializationInclusion(Include.NON_NULL);
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_SHIPPING_ADDRESSES),
					new TypeReference<Response<List<ErpAddressModel>>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerAddressService.getShippingAddresses: data=" + inputJson + ", response="
						+ response);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.getShippingAddresses: identity=" + identity, e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean addShippingAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address)
			throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			getMapper().setSerializationInclusion(Include.NON_NULL);
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("checkUniqueness", checkUniqueness);
			rootNode.set("address", getMapper().convertValue(address, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADD_SHIPPING_ADDRESS),
					new TypeReference<Response<Boolean>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerAddressService.addShippingAddress: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.addShippingAddress: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean updateShippingAddress(FDActionInfo info, boolean checkUniqueness, ErpAddressModel address)
			throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			getMapper().setSerializationInclusion(Include.NON_NULL);
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("checkUniqueness", checkUniqueness);
			rootNode.set("address", getMapper().convertValue(address, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_SHIPPING_ADDRESS),
					new TypeReference<Response<Boolean>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerAddressService.updateShippingAddress: data=" + inputJson + ", response="
						+ response);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.updateShippingAddress: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void removeShippingAddress(FDActionInfo info, PrimaryKey pk) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			getMapper().setSerializationInclusion(Include.NON_NULL);
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("addressId", pk.getId());

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(REMOVE_SHIPPING_ADDRESS),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerAddressService.removeShippingAddress: data=" + inputJson + ", response="
						+ response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.removeShippingAddress: info=" + info + ", pk=" + pk, e);
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public String getDefaultShipToAddressPK(FDIdentity identity) throws FDResourceException {
		Response<String> response = null;
		response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_DEFAULT_SHIPPING_ADDRESS + "/" + identity.getFDCustomerPK()),
				new TypeReference<Response<String>>() {
				});

		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error("Error in CustomerAddressService.getDefaultShipToAddressPK: identity=" + identity
					+ ", response=" + response);
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();

	}

	@Override
	public void setDefaultShippingAddressPK(FDIdentity identity, String shipToAddressPK)
			throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			getMapper().setSerializationInclusion(Include.NON_NULL);
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("customerId", identity.getFDCustomerPK());
			rootNode.put("shippingAddressPK", shipToAddressPK);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SET_DEFAULT_SHIPPING_ADDRESS),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerAddressService.setDefaultShippingAddressPK: inputJson=" + inputJson
						+ ", response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.setDefaultShippingAddressPK: identity=" + identity
					+ ", shipToAddressPK=" + shipToAddressPK, e);
			throw new RemoteException(e.getMessage());
		}

	}

}
