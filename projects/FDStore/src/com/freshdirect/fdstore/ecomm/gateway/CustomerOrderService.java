package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.UnsettledOrdersInfo;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.dto.CustomerAvgOrderSize;

public class CustomerOrderService extends AbstractEcommService implements CustomerOrderServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerOrderService.class);

	private static final String GET_ORDER = "orders/";
	private static final String GET_HISTORIC_ORDER_SIZE = "customerOrder/getHistoricOrderSize/";
	private static final String UPDATE_ORDER_MODIFY_STATE = "customerOrder/updateOrderInModifyState";
	private static final String CLEAR_MODIFY_CART = "customerOrder/clearModifyCartlines/";
	private static final String GET_UNSETTED_ORDERS = "customerOrder/getUnsettledOrders";
	private static final String GET_MODIFIED_CART = "customerOrder/modifiedCartlines/get/";
	private static final String SAVE_MODIFIED_CART = "customerOrder/modifiedCartlines/save";
	private static final String REMOVE_MODIFIED_CART = "customerOrder/modifiedCartlines/remove";
	private static final String UPDATE_MODIFIED_CART = "customerOrder/modifiedCartlines/update";

	private static CustomerOrderServiceI INSTANCE;

	public static CustomerOrderServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerOrderService();

		return INSTANCE;
	}

	@Override
	public FDOrderI getOrder(FDIdentity identity, String saleId) throws FDResourceException, RemoteException {
		FDOrderI order = getOrder(saleId);
		if (!order.getCustomerId().equals(identity.getErpCustomerPK())) {
			throw new FDResourceException("Sale doesn't belong to customer");
		}

		return order;
	}

	@Override
	public FDOrderI getOrder(String saleId) throws FDResourceException, RemoteException {
		Response<ErpSaleModel> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ORDER + saleId),
				new TypeReference<Response<ErpSaleModel>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error("Error in CustomerOrderService.getOrder: saleId=" + saleId);
			throw new FDResourceException(response.getMessage());
		}
		return new FDOrderAdapter(response.getData(), false);
	}

	@Override
	public CustomerAvgOrderSize getHistoricOrderSize(String customerId) throws FDResourceException, RemoteException {
		Response<CustomerAvgOrderSize> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_HISTORIC_ORDER_SIZE + customerId),
				new TypeReference<Response<CustomerAvgOrderSize>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error("Error in CustomerOrderService.getHistoricOrderSize: customerId=" + customerId);
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}

	@Override
	public void updateOrderInModifyState(ErpSaleModel sale) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("sale", getMapper().convertValue(sale, JsonNode.class));
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_ORDER_MODIFY_STATE),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerOrderService updateOrderInModifyState data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerOrderService updateOrderInModifyState: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void clearModifyCartlines(String currentOrderId) throws FDResourceException, RemoteException {
		Response<Void> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CLEAR_MODIFY_CART + currentOrderId),
				new TypeReference<Response<Void>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error("Error in CustomerOrderService.clearModifyCartlines: currentOrderId=" + currentOrderId);
			throw new FDResourceException(response.getMessage());
		}
	}

	@Override
	public List<UnsettledOrdersInfo> getUnsettledOrders() throws FDResourceException, RemoteException {
		Response<List<UnsettledOrdersInfo>> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_UNSETTED_ORDERS), new TypeReference<Response<List<UnsettledOrdersInfo>>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error("Error in CustomerOrderService.getUnsettledOrders");
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}

	@Override
	public List<FDCartLineI> getModifiedCartlines(String orderId, UserContext userContext)
			throws FDResourceException, RemoteException {
		Response<List<ErpOrderLineModel>> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_MODIFIED_CART + orderId),
				new TypeReference<Response<List<ErpOrderLineModel>>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error("Error in CustomerOrderService.getModifiedCartlines, orderId=" + orderId);
			throw new FDResourceException(response.getMessage());
		}
		List<ErpOrderLineModel> cartlines = response.getData();
		List<FDCartLineI> fdCartlines = new ArrayList<FDCartLineI>();
		for (ErpOrderLineModel line : cartlines) {
			line.setUserContext(userContext);
			line.setPlantID(userContext.getFulfillmentContext().getPlantId());
			fdCartlines.add(new FDCartLineModel(line, false));
		}
		return fdCartlines;
	}

	@Override
	public void saveModifiedCartline(PrimaryKey userpk, StoreContext storeContext, FDCartLineI newLine, String orderId,
			boolean isModifiedCartLine) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("userId", userpk.getId());
			rootNode.put("eStoreContentId",
					(null != storeContext && null != storeContext.getEStoreId()
							? storeContext.getEStoreId().getContentId()
							: EnumEStoreId.FD.getContentId()));
			rootNode.set("newLine", getMapper().convertValue(newLine, JsonNode.class));
			rootNode.put("orderId", orderId);
			rootNode.put("isModifiedCartLine", isModifiedCartLine);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SAVE_MODIFIED_CART),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerOrderService.saveModifiedCartline, inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.saveModifiedCartline: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void removeModifiedCartline(FDCartLineI cartLine) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("cartLineId", cartLine.getCartlineId());
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(REMOVE_MODIFIED_CART),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerOrderService.removeModifiedCartline, inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.removeModifiedCartline: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void updateModifiedCartlineQuantity(FDCartLineI cartLine) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("cartLineId", cartLine.getCartlineId());
			rootNode.put("quantity", cartLine.getQuantity());
			rootNode.put("orderId", cartLine.getOrderId());
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_MODIFIED_CART),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerOrderService.updateModifiedCartlineQuantity, inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerAddressService.updateModifiedCartlineQuantity: ", e);
			throw new RemoteException(e.getMessage());
		}

	}

}
