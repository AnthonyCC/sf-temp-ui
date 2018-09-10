package com.freshdirect.fdstore.ecomm.gateway;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressVerificationException;
import com.freshdirect.customer.ErpAuthorizationException;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.ecomm.converter.CustomerRatingConverter;
import com.freshdirect.ecomm.converter.ErpFraudPreventionConverter;
import com.freshdirect.ecomm.converter.FDActionInfoConverter;
import com.freshdirect.ecomm.converter.SapGatewayConverter;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecomm.gateway.OrderServiceApiClient;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.dlv.FDReservationData;
import com.freshdirect.ecommerce.data.order.CancelOrderRequestData;
import com.freshdirect.ecommerce.data.order.CreateOrderRequestData;
import com.freshdirect.ecommerce.data.order.ModifyOrderRequestData;
import com.freshdirect.ecommerce.data.order.OrderSearchCriteriaRequest;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.payment.EnumPaymentMethodType;

public class OrderResourceApiClient extends AbstractEcommService implements OrderResourceApiClientI {

	private static OrderResourceApiClient INSTANCE;

	private final static Category LOGGER = LoggerFactory.getInstance(OrderServiceApiClient.class);

	private static final String GET_ORDERS_API = "orders/list";
	private static final String GET_ORDERS_BY_PYMTTYPE_API = "orders/noncos/last/byPaymentType";
	private static final String GET_ORDERS_BY_PYMTTYPES_API = "orders/noncos/last/byPaymentTypes";
	private static final String CUTOFF_ORDER_API = "orders/cutoff/{id}";
	private static final String GET_OUTSTANDING_BALANCE_API = "orders/getOutStandingBalance";
	private static final String UPDATE_WAVE_INFO_API = "orders/{id}/waveInfo";
	private static final String UPDATE_CARTON_INFO_API = "orders/{id}/cartonInfo";
	
	private static final String RESUBMIT_GC_ORDERS_API = "orders/resubmitNsmGcOrders";
	private static final String CREATE_GC_ORDER_API = "orders/gc/create";
	private static final String CREATE_SUB_ORDER_API = "orders/sub/create";

	private static final String CREATE_DON_ORDER_API = "orders/don/create";
	private static final String CREATE_REG_ORDER_API = "orders/reg/create";
	private static final String MODIFY_REG_ORDER_API = "orders/reg/modify";
	private static final String CANCEL_REG_ORDER_API = "orders/reg/cancel";
	private static final String MODIFY_AUTORENEW_ORDER_API = "orders/sub/modify";

	private static ObjectMapper TYPED_OBJECT_MAPPER = new ObjectMapper()
			.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"))
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);

	public static OrderResourceApiClientI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OrderResourceApiClient();

		return INSTANCE;
	}

	@Override
	public void chargeOrder(FDIdentity identity, String saleId, ErpPaymentMethodI paymentMethod, boolean sendEmail,
			CustomerRatingI cra, CrmAgentModel agent, double additionalCharge) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public List<ErpSaleModel> getOrders(List<String> ids) throws RemoteException {

		try {
			Request<List<String>> request = new Request<List<String>>();
			request.setData(ids);
			String inputJson = buildRequest(request);
			String response = postData(inputJson, getFdCommerceEndPoint(GET_ORDERS_API), String.class);
			Response<List<ErpSaleModel>> info = getMapper().readValue(response,
					new TypeReference<Response<List<ErpSaleModel>>>() {
					});
			return parseResponse(info);
		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects " + ids);
			throw new RemoteException(e.getMessage(), e);

		}

	}

	@Override
	public ErpSaleModel getLastNonCOSOrder(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus,
			EnumPaymentMethodType paymentType) throws ErpSaleNotFoundException, RemoteException {

		try {
			Request<OrderSearchCriteriaRequest> request = new Request<OrderSearchCriteriaRequest>();
			OrderSearchCriteriaRequest criteria = new OrderSearchCriteriaRequest();
			criteria.setCustomerId(customerID);
			criteria.setSaleType(saleType.getSaleType());
			criteria.setSaleStatus(saleStatus.getStatusCode());
			List<String> paymentTypes = new ArrayList<String>();
			paymentTypes.add(paymentType.getCode());
			criteria.setPaymentTypes(paymentTypes);
			request.setData(criteria);
			String inputJson = buildRequest(request);
			String response = postData(inputJson, getFdCommerceEndPoint(GET_ORDERS_BY_PYMTTYPE_API), String.class);
			Response<ErpSaleModel> info = getMapper().readValue(response, new TypeReference<Response<ErpSaleModel>>() {
			});
			return parseResponse(info);
		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);

		}

	}

	@Override
	public ErpSaleModel getLastNonCOSOrder(String customerID, EnumSaleType saleType, EnumSaleStatus saleStatus,
			List<EnumPaymentMethodType> pymtMethodTypes) throws ErpSaleNotFoundException, RemoteException {

		try {
			Request<OrderSearchCriteriaRequest> request = new Request<OrderSearchCriteriaRequest>();
			OrderSearchCriteriaRequest criteria = new OrderSearchCriteriaRequest();
			criteria.setCustomerId(customerID);
			criteria.setSaleType(saleType.getSaleType());
			criteria.setSaleStatus(saleStatus.getStatusCode());
			List<String> paymentTypes = new ArrayList<String>();
			for (EnumPaymentMethodType type : pymtMethodTypes) {
				paymentTypes.add(type.getCode());
			}
			criteria.setPaymentTypes(paymentTypes);
			request.setData(criteria);
			String inputJson = buildRequest(request);
			String response = postData(inputJson, getFdCommerceEndPoint(GET_ORDERS_BY_PYMTTYPES_API), String.class);
			Response<ErpSaleModel> info = getMapper().readValue(response, new TypeReference<Response<ErpSaleModel>>() {
			});
			return parseResponse(info);
		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);

		}

	}

	@Override
	public void cutOffSale(String saleId) throws ErpSaleNotFoundException, RemoteException, FDResourceException {

		String inputJson = null;
		Response<String> response = null;
		response = httpPostData(getFdCommerceEndPoint(CUTOFF_ORDER_API), inputJson, Response.class,
				new Object[] { saleId });
		if (!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());

	}

	@Override
	public void updateWaveInfo(String saleId, ErpShippingInfo shippingInfo) throws RemoteException, FinderException {

		try {
			Response<String> response = null;
			Request<ErpShippingInfo> request = new Request<ErpShippingInfo>();
			request.setData(new ErpShippingInfo(shippingInfo.getWaveNumber(), shippingInfo.getTruckNumber(),
					shippingInfo.getStopSequence(), shippingInfo.getRegularCartons(), shippingInfo.getFreezerCartons(),
					shippingInfo.getAlcoholCartons()));
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(UPDATE_WAVE_INFO_API), inputJson, Response.class,
					new Object[] { saleId });
			if (!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());
		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);

		}

	}

	@Override
	public void updateCartonInfo(String saleId, List<ErpCartonInfo> cartonList)
			throws RemoteException, FinderException, FDResourceException {

		try {
			Response<String> response = null;
			Request<List<ErpCartonInfo>> request = new Request<List<ErpCartonInfo>>();
			request.setData(cartonList);
			String inputJson = buildRequest(request);

			response = httpPostData(inputJson, getFdCommerceEndPoint(UPDATE_CARTON_INFO_API), Response.class,
					new Object[] { saleId });
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (Exception e) {
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);

		}

	}

	

	@Override
	public double getOutStandingBalance(ErpAbstractOrderModel order) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String placeGiftCardOrder(FDActionInfo info, ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra, CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isBulkOrder) throws RemoteException {

		Request<CreateOrderRequestData> request = new Request<CreateOrderRequestData>();

		try {
			request.setData(new CreateOrderRequestData(FDActionInfoConverter.buildActionInfoData(info),
					SapGatewayConverter.buildOrderData(createOrder), appliedPromos, id, sendEmail,
					CustomerRatingConverter.buildCustomerRatingData(cra),
					ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole),
					(status != null) ? status.getName() : null, isBulkOrder));
			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(CREATE_GC_ORDER_API), inputJson, Response.class,
					new Object[] {});
			return parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}

	}

	@Override
	public String placeSubscriptionOrder(FDActionInfo info, ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra, CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isRealTimeAuthNeeded) throws RemoteException {

		Request<CreateOrderRequestData> request = new Request<CreateOrderRequestData>();

		try {

			CreateOrderRequestData data = new CreateOrderRequestData();
			data.setInfo(FDActionInfoConverter.buildActionInfoData(info));
			data.setModel(SapGatewayConverter.buildOrderData(createOrder));
			data.setAppliedPromos(appliedPromos);
			data.setId(id);
			data.setSendMail(sendEmail);
			data.setCra(CustomerRatingConverter.buildCustomerRatingData(cra));
			data.setAgentRole(ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole));
			data.setDeliveryPassStatus((status != null) ? status.getName() : null);
			data.setRealTimeAuthNeeded(isRealTimeAuthNeeded);

			request.setData(data);

			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(CREATE_SUB_ORDER_API), inputJson, Response.class,
					new Object[] {});
			return parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}

	}

	@Override
	public String placeDonationOrder(FDActionInfo info, ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra, CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isOptIn) throws RemoteException {

		Request<CreateOrderRequestData> request = new Request<CreateOrderRequestData>();

		try {

			CreateOrderRequestData data = new CreateOrderRequestData();
			data.setInfo(FDActionInfoConverter.buildActionInfoData(info));
			data.setModel(SapGatewayConverter.buildOrderData(createOrder));
			data.setAppliedPromos(appliedPromos);
			data.setId(id);
			data.setSendMail(sendEmail);
			data.setCra(CustomerRatingConverter.buildCustomerRatingData(cra));
			data.setAgentRole(ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole));
			data.setDeliveryPassStatus((status != null) ? status.getName() : null);
			data.setOptIn(isOptIn);

			request.setData(data);

			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(CREATE_DON_ORDER_API), inputJson, Response.class,
					new Object[] {});
			return parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}

	}

	@Override
	public String placeOrder(FDActionInfo info, ErpCreateOrderModel createOrder, Set<String> appliedPromos, String id,
			boolean sendEmail, CustomerRatingI cra, CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isFriendReferred, int fdcOrderCount) throws FDResourceException, ErpFraudException,
			ErpAuthorizationException, ErpAddressVerificationException, ReservationException, DeliveryPassException,
			FDPaymentInadequateException, ErpTransactionException, InvalidCardException, RemoteException {
		String inputJson = null;
		try {

			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.set("createOrder", getMapper().convertValue(createOrder, JsonNode.class));
			rootNode.set("appliedPromos", getMapper().convertValue(appliedPromos, JsonNode.class));
			rootNode.put("id", id);
			rootNode.put("sendEmail", sendEmail);
			rootNode.set("cra", getMapper().convertValue(cra, JsonNode.class));
			rootNode.set("crmAgentRole", getMapper().convertValue(crmAgentRole, JsonNode.class));
			rootNode.put("status", status.getName());
			rootNode.put("isFriendReferred", isFriendReferred);
			rootNode.put("fdcOrderCount", fdcOrderCount);
			
			request.setData(rootNode);
			inputJson = buildRequest(request);
			
			Response<String> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CREATE_REG_ORDER_API),
					new TypeReference<Response<String>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in placeOrder: inputJson=" + inputJson + ",response=" + response);
				
				handleErrorOrderResponse(response);
			}
			return response.getData();
		} catch (Exception e) {
			LOGGER.error("Error in placeOrder: inputJson=" + inputJson, e);
			throw new RemoteException(e.getMessage(), e);
			
		}

	}

	@Override
	public void modifyOrder(FDActionInfo info, String saleId, ErpModifyOrderModel order, Set<String> appliedPromos,
			String originalReservationId, boolean sendEmail, CustomerRatingI cra, CrmAgentRole crmAgentRole,
			EnumDlvPassStatus status, boolean hasCouponDiscounts, int fdcOrderCount) throws FDResourceException,
			ErpFraudException, ErpAuthorizationException, ErpTransactionException, DeliveryPassException,
			FDPaymentInadequateException, ErpAddressVerificationException, InvalidCardException, RemoteException {
		String inputJson = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("saleId", saleId);
			rootNode.set("order", getMapper().convertValue(order, JsonNode.class));
			rootNode.set("appliedPromos", getMapper().convertValue(appliedPromos, JsonNode.class));
			rootNode.put("originalReservationId", originalReservationId);
			rootNode.put("sendEmail", sendEmail);
			rootNode.set("cra", getMapper().convertValue(cra, JsonNode.class));
			rootNode.set("crmAgentRole", getMapper().convertValue(crmAgentRole, JsonNode.class));
			rootNode.put("status", status.getName());
			rootNode.put("hasCouponDiscounts", hasCouponDiscounts);
			rootNode.put("fdcOrderCount", fdcOrderCount);

			request.setData(rootNode);
			inputJson = buildRequest(request);

			Response<String> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(MODIFY_REG_ORDER_API),
					new TypeReference<Response<String>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in modifyOrder: inputJson=" + inputJson + ",response=" + response);

				handleErrorOrderResponse(response);
			}
		} catch (Exception e) {
			LOGGER.error("Error in modifyOrder: inputJson=" + inputJson, e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	private void handleErrorOrderResponse(Response<String> response) throws FDResourceException, ErpFraudException,
			ErpAuthorizationException, ErpAddressVerificationException, ReservationException, DeliveryPassException,
			FDPaymentInadequateException, ErpTransactionException, InvalidCardException {
		if ("FDResourceException".equals(response.getMessage())) {
			throw new FDResourceException(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null);
		}
		if ("ErpFraudException".equals(response.getMessage())) {
			throw new ErpFraudException(EnumFraudReason.getEnum(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null));
		}
		if ("ErpAuthorizationException".equals(response.getMessage())) {
			throw new ErpAuthorizationException(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null);
		}
		if ("ErpAddressVerificationException".equals(response.getMessage())) {
			throw new ErpAddressVerificationException(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null);
		}
		if ("ReservationException".equals(response.getMessage())) {
			throw new ReservationException(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null);
		}
		if ("DeliveryPassException".equals(response.getMessage())) {
			throw new DeliveryPassException(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null);
		}
		if ("FDPaymentInadequateException".equals(response.getMessage())) {
			throw new FDPaymentInadequateException(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null);
		}
		if ("ErpTransactionException".equals(response.getMessage())) {
			throw new ErpTransactionException(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null);
		}
		if ("InvalidCardException".equals(response.getMessage())) {
			throw new InvalidCardException(
					response.getError() != null ? response.getError().get(response.getMessage()).toString()
							: null);
		}

		throw new FDResourceException(response.getMessage());
	}

	@Override
	public void modifyAutoRenewOrder(FDActionInfo info, String saleId, ErpModifyOrderModel order,
			Set<String> appliedPromos, String originalReservationId, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status) {

		Request<ModifyOrderRequestData> request = new Request<ModifyOrderRequestData>();

		try {

			ModifyOrderRequestData data = new ModifyOrderRequestData(FDActionInfoConverter.buildActionInfoData(info),
					saleId, SapGatewayConverter.buildOrderData(order), appliedPromos, originalReservationId, sendEmail,
					CustomerRatingConverter.buildCustomerRatingData(cra),
					ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole),
					(status != null) ? status.getName() : null);
			request.setData(data);

			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(MODIFY_AUTORENEW_ORDER_API), inputJson, Response.class,
					new Object[] {});
			parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public FDReservation cancelOrder(FDActionInfo info, String saleId, boolean sendEmail, int currentDPExtendDays,
			boolean restoreReservation)
			throws FDResourceException, ErpTransactionException, DeliveryPassException, RemoteException {
		String inputJson = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("saleId", saleId);
			rootNode.put("sendEmail", sendEmail);
			rootNode.put("currentDPExtendDays", currentDPExtendDays);
			rootNode.put("restoreReservation", restoreReservation);

			request.setData(rootNode);
			inputJson = buildRequest(request);

			Response<FDReservation> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CANCEL_REG_ORDER_API),
					new TypeReference<Response<FDReservation>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in cancelOrder: inputJson=" + inputJson + ",response=" + response);

				if ("FDResourceException".equals(response.getMessage())) {
					throw new FDResourceException(
							response.getError() != null ? response.getError().get(response.getMessage()).toString()
									: null);
				}
				if ("ErpTransactionException".equals(response.getMessage())) {
					throw new ErpTransactionException(
							response.getError() != null ? response.getError().get(response.getMessage()).toString()
									: null);
				}
				if ("DeliveryPassException".equals(response.getMessage())) {
					throw new DeliveryPassException(
							response.getError() != null ? response.getError().get(response.getMessage()).toString()
									: null);
				}
			}
			return response.getData();

		} catch (Exception e) {
			LOGGER.error("Error in cancelOrder: inputJson=" + inputJson, e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	private static final String ORDER_CHECK_AVAILABILITY = "orders/checkAvailability";
	private static final String ORDER_RESUBMIT = "orders/resubmit";

	@Override
	public Map<String, FDAvailabilityI> checkAvailability(FDIdentity identity, ErpCreateOrderModel createOrder,
			long timeout, String isFromLogin) throws FDResourceException {

		Request<ObjectNode> request = new Request<ObjectNode>();
		ObjectNode rootNode = getMapper().createObjectNode();
		rootNode.set("createOrder", getMapper().convertValue(createOrder, JsonNode.class));
		rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
		rootNode.put("timeout", timeout);
		rootNode.put("isFromLogin", isFromLogin);
		request.setData(rootNode);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<String> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ORDER_CHECK_AVAILABILITY),
					new TypeReference<Response<String>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error occurs in checkAvailability: inputJson=" + inputJson
						+ ", response=" + response);
				throw new FDResourceException(response.getMessage());
			}
			try {
				HashMap<String, FDAvailabilityI> map = TYPED_OBJECT_MAPPER.readValue(response.getData(),
						new TypeReference<HashMap<String, FDAvailabilityI>>() {
						});
				
				return map;
			} catch (JsonMappingException e) {
				LOGGER.error("Error occurs in checkAvailability: inputJson=" + inputJson
						+ ", responseData=" + response.getData(), e);
				throw new FDResourceException(e);
			} catch (JsonParseException e) {
				LOGGER.error("Error occurs in checkAvailability: inputJson=" + inputJson
						+ ", responseData=" + response.getData(), e);
				throw new FDResourceException(e);
			} catch (IOException e) {
				LOGGER.error("Error occurs in checkAvailability: inputJson=" + inputJson
						+ ", response=" + response, e);
				throw new FDResourceException(e);

			}

		} catch (FDEcommServiceException e) {
			LOGGER.error("Error occurs in checkAvailability: identity=" + identity, e);
			throw new FDResourceException(e);
		}
	}

	@Override
	public void resubmitOrder(String saleId, CustomerRatingI cra, EnumSaleType saleType, String deliveryRegionId)
			throws ErpTransactionException, RemoteException {

		Request<ObjectNode> request = new Request<ObjectNode>();
		ObjectNode rootNode = getMapper().createObjectNode();
		rootNode.set("customerRating",
				getMapper().convertValue(CustomerRatingConverter.buildCustomerRatingData(cra), JsonNode.class));
		rootNode.put("saleType", saleType.getSaleType());
		rootNode.put("saleId", saleId);
		rootNode.put("regionId", deliveryRegionId);
		request.setData(rootNode);
		Response<String> info = null;
		String inputJson;
		try {
			inputJson = buildRequest(request);
			String response = postData(inputJson, getFdCommerceEndPoint(ORDER_RESUBMIT), String.class);
			info = getMapper().readValue(response, new TypeReference<Response<String>>() {
			});

			if (!info.getResponseCode().equals("OK"))
				throw new ErpTransactionException(info.getMessage());

		} catch (FDEcommServiceException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		} catch (FDResourceException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
