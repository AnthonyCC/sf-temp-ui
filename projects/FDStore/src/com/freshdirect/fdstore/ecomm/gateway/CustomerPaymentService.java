package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerPaymentService extends AbstractEcommService implements CustomerPaymentServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerPaymentService.class);

	private static final String GET_PAYMENT_METHODS = "customerPayment/paymentMethods";
	private static final String ADD_PAYMENT_METHOD = "customerPayment/paymentMethod/add";
	private static final String UPDATE_PAYMENT_METHOD = "customerPayment/paymentMethod/update";
	private static final String REMOVE_PAYMENT_METHOD = "customerPayment/paymentMethod/remove";
	private static final String GET_DEFAULT_PAYMENT_METHOD_PK = "customerPayment/defaultPaymentMethodPK";
	private static final String SET_DEFAULT_PAYMENT_METHOD_PK = "customerPayment/defaultPaymentMethod";
	private static final String RESET_DEFAULT_PAYMENT_METHOD_TYPE = "customerPayment/defaultPaymentMethodType/reset";
	private static CustomerPaymentServiceI INSTANCE;

	public static CustomerPaymentServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerPaymentService();

		return INSTANCE;
	}

	@Override
	public List<ErpPaymentMethodI> getPaymentMethods(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<List<ErpPaymentMethodI>> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_PAYMENT_METHODS + "/" + identity.getErpCustomerPK()),
				new TypeReference<Response<List<ErpPaymentMethodI>>>() {
				});

		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();

	}

	@Override
	public void addPaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod, boolean paymentechEnabled,
			boolean isDebitCardSwitch) throws FDResourceException, RemoteException, ErpPaymentMethodException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.set("paymentMethod", getMapper().convertValue(paymentMethod, JsonNode.class));
			rootNode.put("paymentechEnabled", paymentechEnabled);
			rootNode.put("isDebitCardSwitch", isDebitCardSwitch);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADD_PAYMENT_METHOD),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("ErpPaymentMethodException".equals(response.getMessage())) {
					if (response.getError() != null && response.getError().get("ErpPaymentMethodException") != null) {
						throw new ErpPaymentMethodException(
								response.getError().get("ErpPaymentMethodException").toString());
					} else {
						throw new ErpPaymentMethodException();
					}
				}
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerPaymentService: ", e);
			throw new RemoteException(e.getMessage());
		}

	}

	/**
	 * update a payment method for the customer
	 *
	 * @param identity
	 *            the customer's identity reference
	 * @param paymentMethod
	 *            ErpPaymentMethodI to update
	 *
	 * @throws FDResourceException
	 *             if an error occurred using remote resources
	 */
	public void updatePaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod)
			throws FDResourceException, RemoteException, ErpPaymentMethodException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.set("paymentMethod", getMapper().convertValue(paymentMethod, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_PAYMENT_METHOD),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("ErpPaymentMethodException".equals(response.getMessage())) {
					if (response.getError() != null && response.getError().get("ErpPaymentMethodException") != null) {
						throw new ErpPaymentMethodException(
								response.getError().get("ErpPaymentMethodException").toString());
					} else {
						throw new ErpPaymentMethodException();
					}
				}
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerPaymentService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * remove a payment method for the customer
	 *
	 * @param identity
	 *            the customer's identity reference
	 * @param pk
	 *            PrimaryKey of the paymentMethod to remove
	 *
	 *            throws FDResourceException if an error occured using remote
	 *            resources
	 */
	public void removePaymentMethod(FDActionInfo info, ErpPaymentMethodI paymentMethod, boolean isDebitCardSwitch)
			throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.set("paymentMethod", getMapper().convertValue(paymentMethod, JsonNode.class));
			rootNode.put("isDebitCardSwitch", isDebitCardSwitch);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(REMOVE_PAYMENT_METHOD),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerPaymentService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String getDefaultPaymentMethodPK(String fdCustomerId) throws FDResourceException, RemoteException {
		Response<String> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_DEFAULT_PAYMENT_METHOD_PK + "/" + fdCustomerId),
				new TypeReference<Response<String>>() {
				});

		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}

	@Override
	public void setDefaultPaymentMethod(FDActionInfo info, String paymentMethodPK,
			EnumPaymentMethodDefaultType type, boolean isDebitCardSwitch) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.set("type", getMapper().convertValue(type, JsonNode.class));
			rootNode.put("paymentMethodPK", paymentMethodPK);
			rootNode.put("isDebitCardSwitch", isDebitCardSwitch);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SET_DEFAULT_PAYMENT_METHOD_PK),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerPaymentService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public int resetDefaultPaymentValueType(String fdCustomerId) throws FDResourceException, RemoteException {
		Response<Integer> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(RESET_DEFAULT_PAYMENT_METHOD_TYPE + "/" + fdCustomerId),
				new TypeReference<Response<Integer>>() {
				});

		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}
}
