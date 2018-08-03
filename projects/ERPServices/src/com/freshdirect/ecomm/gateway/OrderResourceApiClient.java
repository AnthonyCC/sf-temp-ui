package com.freshdirect.ecomm.gateway;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.ecomm.converter.CustomerRatingConverter;
import com.freshdirect.ecomm.converter.ErpFraudPreventionConverter;
import com.freshdirect.ecomm.converter.FDActionInfoConverter;
import com.freshdirect.ecomm.converter.SapGatewayConverter;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.dlv.FDReservationData;
import com.freshdirect.ecommerce.data.erp.inventory.FDAvailabilityData;
import com.freshdirect.ecommerce.data.order.CancelOrderRequestData;
import com.freshdirect.ecommerce.data.order.CreateOrderRequestData;
import com.freshdirect.ecommerce.data.order.ModifyOrderRequestData;
import com.freshdirect.ecommerce.data.order.OrderSearchCriteriaRequest;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.service.ModelConverter;

public class OrderResourceApiClient extends AbstractEcommService implements
		OrderResourceApiClientI {

private static OrderResourceApiClient INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(OrderServiceApiClient.class);

	private static final String GET_ORDER_API = "orders/{id}";
	private static final String GET_ORDERS_API = "orders/list";
	private static final String GET_ORDERS_BY_PYMTTYPE_API = "orders/noncos/last/byPaymentType";
	private static final String GET_ORDERS_BY_PYMTTYPES_API= "orders/noncos/last/byPaymentTypes";
	private static final String CUTOFF_ORDER_API = 	"orders/cutoff/{id}";
	private static final String GET_OUTSTANDING_BALANCE_API = 	"orders/getOutStandingBalance";
	private static final String UPDATE_WAVE_INFO_API = 	"orders/{id}/waveInfo";
	private static final String UPDATE_CARTON_INFO_API = 	"orders/{id}/cartonInfo";
	private static final String GET_DELIVERYINFO_API = 	"orders/{id}/getDeliveryInfo";
	private static final String RESUBMIT_GC_ORDERS_API = 	"orders/resubmitNsmGcOrders";
	private static final String CREATE_GC_ORDER_API = 	"orders/gc/create";
	private static final String CREATE_SUB_ORDER_API = 	"orders/sub/create";
	
	private static final String CREATE_DON_ORDER_API = 	"orders/don/create";
	private static final String CREATE_REG_ORDER_API = 	"orders/reg/create";
	private static final String MODIFY_REG_ORDER_API = 	"orders/reg/modify";
	private static final String CANCEL_REG_ORDER_API = 	"orders/reg/cancel";
	private static final String MODIFY_AUTORENEW_ORDER_API = 	"orders/sub/modify";
	
	public static OrderResourceApiClient getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OrderResourceApiClient();

		return INSTANCE;
	}

	@Override
	public void chargeOrder(FDIdentity identity, String saleId,
			ErpPaymentMethodI paymentMethod, boolean sendEmail,
			CustomerRatingI cra, CrmAgentModel agent, double additionalCharge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ErpSaleModel getOrder(String id) throws RemoteException {
		
			
			
			try{
				String response =  httpGetData(getFdCommerceEndPoint(GET_ORDER_API), String.class, new Object[]{id});
				Response<ErpSaleModel> info = getMapper().readValue(response, new TypeReference<Response<ErpSaleModel>>() { });
				return parseResponse(info);	
			}catch(Exception e){
				LOGGER.info(e);
				LOGGER.info("Exception converting {} to ListOfObjects "+ id);
				throw new RemoteException(e.getMessage(), e);
				
			}
		
		
	}

	@Override
	public List<ErpSaleModel> getOrders(List<String> ids)
			throws RemoteException {
		
		
		
		try{
			Request<List<String>> request = new Request<List<String>>();
			request.setData(ids);
			String inputJson = buildRequest(request);
			String response =  postData(inputJson, getFdCommerceEndPoint(GET_ORDERS_API), String.class);
			Response<List<ErpSaleModel>> info = getMapper().readValue(response, new TypeReference<Response<List<ErpSaleModel>>>() { });
			return parseResponse(info);	
		}catch(Exception e){
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects "+ ids);
			throw new RemoteException(e.getMessage(), e);
			
		}
	
	
}

	@Override
	public ErpSaleModel getLastNonCOSOrder(String customerID,
			EnumSaleType saleType, EnumSaleStatus saleStatus,
			EnumPaymentMethodType paymentType) throws ErpSaleNotFoundException,
			RemoteException {
		
		
		
		try{
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
			Response<ErpSaleModel> info = getMapper().readValue(response, new TypeReference<Response<ErpSaleModel>>() { });
			return parseResponse(info);	
		}catch(Exception e){
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);
			
		}
	
	
}

	@Override
	public ErpSaleModel getLastNonCOSOrder(String customerID,
			EnumSaleType saleType, EnumSaleStatus saleStatus,
			List<EnumPaymentMethodType> pymtMethodTypes)
			throws ErpSaleNotFoundException, RemoteException {

		
		
		
		try{
			Request<OrderSearchCriteriaRequest> request = new Request<OrderSearchCriteriaRequest>();
			OrderSearchCriteriaRequest criteria = new OrderSearchCriteriaRequest();
			criteria.setCustomerId(customerID);
			criteria.setSaleType(saleType.getSaleType());
			criteria.setSaleStatus(saleStatus.getStatusCode());
			List<String> paymentTypes = new ArrayList<String>();
			for(EnumPaymentMethodType type : pymtMethodTypes){
				paymentTypes.add(type.getCode());
			}
			criteria.setPaymentTypes(paymentTypes);
			request.setData(criteria);
			String inputJson = buildRequest(request);
			String response = postData(inputJson, getFdCommerceEndPoint(GET_ORDERS_BY_PYMTTYPES_API), String.class);
			Response<ErpSaleModel> info = getMapper().readValue(response, new TypeReference<Response<ErpSaleModel>>() { });
			return parseResponse(info);	
		}catch(Exception e){
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);
			
		}
	
	

	}

	@Override
	public void cutOffSale(String saleId) throws ErpSaleNotFoundException,
			RemoteException, FDResourceException {
		

		String inputJson=null;
		Response<String> response = null;
		response = httpPostData(getFdCommerceEndPoint(CUTOFF_ORDER_API), inputJson, Response.class, new Object[]{saleId});
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		
	
	}

	
	@Override
	public void updateWaveInfo(String saleId, ErpShippingInfo shippingInfo)
			throws RemoteException, FinderException {
		
		try{
		Response<String> response = null;
		Request<ErpShippingInfo> request = new Request<ErpShippingInfo>();
		request.setData(new ErpShippingInfo(shippingInfo.getWaveNumber(), shippingInfo.getTruckNumber(), 
				shippingInfo.getStopSequence(), shippingInfo.getRegularCartons(), 
				shippingInfo.getFreezerCartons(), shippingInfo.getAlcoholCartons()));
		String inputJson = buildRequest(request);
		response = httpPostData(getFdCommerceEndPoint(UPDATE_WAVE_INFO_API), inputJson, Response.class, new Object[]{saleId});
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		}catch(Exception e){
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);
			
		}
	
	}

	@Override
	public void updateCartonInfo(String saleId, List<ErpCartonInfo> cartonList)
			throws RemoteException, FinderException, FDResourceException {
		

		try{
			Response<String> response = null;
			Request<List<ErpCartonInfo>> request = new Request<List<ErpCartonInfo>>();
			request.setData(cartonList);
			String inputJson = buildRequest(request);
			
		response = httpPostData(inputJson, getFdCommerceEndPoint(UPDATE_CARTON_INFO_API), Response.class, new Object[]{saleId}); 
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		}catch(Exception e){
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);
			
		}
		
	
	}

	@Override
	public ErpDeliveryInfoModel getDeliveryInfo(String saleId)
			throws ErpSaleNotFoundException, RemoteException {

		
		
		
		try{
			String response =  httpGetData(getFdCommerceEndPoint(GET_DELIVERYINFO_API), String.class, new Object[]{saleId});
			Response<ErpDeliveryInfoModel> info = getMapper().readValue(response, new TypeReference<Response<ErpDeliveryInfoModel>>() { });
			return parseResponse(info);	
		}catch(Exception e){
			LOGGER.info(e);
			LOGGER.info("Exception converting {} to ListOfObjects ");
			throw new RemoteException(e.getMessage(), e);
			
		}
	
	

	}

	@Override
	public double getOutStandingBalance(ErpAbstractOrderModel order)
			throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public String placeGiftCardOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isBulkOrder) throws RemoteException {

		Request<CreateOrderRequestData> request = new Request<CreateOrderRequestData>();
		
		try{
			request.setData(
					new CreateOrderRequestData(
							FDActionInfoConverter.buildActionInfoData(info), 
							SapGatewayConverter.buildOrderData(createOrder), 
							appliedPromos, 
							id, 
							sendEmail, 
							CustomerRatingConverter.buildCustomerRatingData(cra), 
							ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole), 
							(status!=null)?status.getName():null, 
							isBulkOrder));
			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(CREATE_GC_ORDER_API), inputJson, Response.class, new Object[]{});
			return parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
		
	}

	@Override
	public String placeSubscriptionOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isRealTimeAuthNeeded) throws RemoteException {

		Request<CreateOrderRequestData> request = new Request<CreateOrderRequestData>();
		
		try{
			
			CreateOrderRequestData data = new CreateOrderRequestData();
			data.setInfo(FDActionInfoConverter.buildActionInfoData(info));
			data.setModel(SapGatewayConverter.buildOrderData(createOrder));
			data.setAppliedPromos(appliedPromos);
			data.setId(id);
			data.setSendMail(sendEmail);
			data.setCra(CustomerRatingConverter.buildCustomerRatingData(cra));
			data.setAgentRole(ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole));
			data.setDeliveryPassStatus((status!=null)?status.getName():null);
			data.setRealTimeAuthNeeded(isRealTimeAuthNeeded);
			
			
			request.setData(data);
			
			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(CREATE_SUB_ORDER_API), inputJson, Response.class, new Object[]{});
			return parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
		
	}

	@Override
	public String placeDonationOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status, boolean isOptIn) throws RemoteException {


		Request<CreateOrderRequestData> request = new Request<CreateOrderRequestData>();
		
		try{
			
			CreateOrderRequestData data = new CreateOrderRequestData();
			data.setInfo(FDActionInfoConverter.buildActionInfoData(info));
			data.setModel(SapGatewayConverter.buildOrderData(createOrder));
			data.setAppliedPromos(appliedPromos);
			data.setId(id);
			data.setSendMail(sendEmail);
			data.setCra(CustomerRatingConverter.buildCustomerRatingData(cra));
			data.setAgentRole(ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole));
			data.setDeliveryPassStatus((status!=null)?status.getName():null);
			data.setOptIn(isOptIn);
			
			
			request.setData(data);
			
			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(CREATE_DON_ORDER_API), inputJson, Response.class, new Object[]{});
			return parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
		
	
		
	}

	@Override
	public String placeOrder(FDActionInfo info,
			ErpCreateOrderModel createOrder, Set<String> appliedPromos,
			String id, boolean sendEmail, CustomerRatingI cra,
			CrmAgentRole crmAgentRole, EnumDlvPassStatus status,
			boolean isFriendReferred, int fdcOrderCount) throws RemoteException {


		Request<CreateOrderRequestData> request = new Request<CreateOrderRequestData>();
		
		try{
			
			CreateOrderRequestData data = new CreateOrderRequestData();
			data.setInfo(FDActionInfoConverter.buildActionInfoData(info));
			data.setModel(SapGatewayConverter.buildOrderData(createOrder));
			data.setAppliedPromos(appliedPromos);
			data.setId(id);
			data.setSendMail(sendEmail);
			data.setCra(CustomerRatingConverter.buildCustomerRatingData(cra));
			data.setAgentRole(ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole));
			data.setDeliveryPassStatus((status!=null)?status.getName():null);
			data.setFriendReferred(isFriendReferred);
			data.setFdcOrderCount(fdcOrderCount);
			
			
			request.setData(data);
			
			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(CREATE_REG_ORDER_API), inputJson, Response.class, new Object[]{});
			return parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
		
	
		
	}

	@Override
	public void modifyOrder(FDActionInfo info, String saleId,
			ErpModifyOrderModel order, Set<String> appliedPromos,
			String originalReservationId, boolean sendEmail,
			CustomerRatingI cra, CrmAgentRole crmAgentRole,
			EnumDlvPassStatus status, boolean hasCouponDiscounts,
			int fdcOrderCount) {
		



		Request<ModifyOrderRequestData> request = new Request<ModifyOrderRequestData>();
		
		try{
			
			ModifyOrderRequestData data = new ModifyOrderRequestData(FDActionInfoConverter.buildActionInfoData(info), saleId, SapGatewayConverter.buildOrderData(order), 
					appliedPromos, originalReservationId, sendEmail, CustomerRatingConverter.buildCustomerRatingData(cra), ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole), (status!=null)?status.getName():null);
			data.setHasCouponDiscounts(hasCouponDiscounts);
			data.setFdcOrderCount(fdcOrderCount);
			request.setData(data);
			
			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(MODIFY_REG_ORDER_API), inputJson, Response.class, new Object[]{});
			parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
	
	}

	@Override
	public void modifyAutoRenewOrder(FDActionInfo info, String saleId,
			ErpModifyOrderModel order, Set<String> appliedPromos,
			String originalReservationId, boolean sendEmail,
			CustomerRatingI cra, CrmAgentRole crmAgentRole,
			EnumDlvPassStatus status) {
		

		



		Request<ModifyOrderRequestData> request = new Request<ModifyOrderRequestData>();
		
		try{
			
			ModifyOrderRequestData data = new ModifyOrderRequestData(FDActionInfoConverter.buildActionInfoData(info), saleId, SapGatewayConverter.buildOrderData(order), 
					appliedPromos, originalReservationId, sendEmail, CustomerRatingConverter.buildCustomerRatingData(cra), ErpFraudPreventionConverter.buildCrmAgentRoleData(crmAgentRole), (status!=null)?status.getName():null);
			request.setData(data);
			
			Response<String> response = null;
			String inputJson = buildRequest(request);
			response = httpPostData(getFdCommerceEndPoint(MODIFY_AUTORENEW_ORDER_API), inputJson, Response.class, new Object[]{});
			parseResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
	
	
		
	}

	@Override
	public FDReservationData cancelOrder(FDActionInfo info, String saleId,
			boolean sendEmail, int currentDPExtendDays,
			boolean restoreReservation) {

		Request<CancelOrderRequestData> request = new Request<CancelOrderRequestData>();

		try {

			CancelOrderRequestData data = new CancelOrderRequestData(
					FDActionInfoConverter.buildActionInfoData(info), saleId,
					sendEmail, currentDPExtendDays, restoreReservation);
			request.setData(data);

			String inputJson = buildRequest(request);
			String response = postData(inputJson, getFdCommerceEndPoint(CANCEL_REG_ORDER_API), String.class);
			Response<FDReservationData> responseWrapper = getMapper().readValue(response, new TypeReference<Response<FDReservationData>>() { });
			
			return parseResponse(responseWrapper);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	

	private static final String ORDER_CHECK_AVAILABILITY = 	"orders/checkAvailability";
	private static final String ORDER_RESUBMIT = 	"orders/resubmit";
	
	@Override
	public Map<String, FDAvailabilityI> checkAvailability(FDIdentity identity,
			ErpCreateOrderModel createOrder, long timeout, String isFromLogin) {

		Request<ObjectNode> request = new Request<ObjectNode>();
		ObjectNode rootNode = getMapper().createObjectNode();
		rootNode.set("info", getMapper().convertValue(SapGatewayConverter.buildOrderData(createOrder), JsonNode.class));
		rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
		rootNode.put("timeout", timeout);
		rootNode.put("isFromLogin", isFromLogin);
		request.setData(rootNode);
		Response<Map<String, FDAvailabilityData>> info=null;
		Map<String, FDAvailabilityI> data = null;
		String inputJson;
		try {
			inputJson = buildRequest(request);
			String response = postData(inputJson, getFdCommerceEndPoint(ORDER_CHECK_AVAILABILITY), String.class);
			info = getMapper().readValue(response, new TypeReference<Response<Map<String, FDAvailabilityData>>>() { });
			data = new HashMap();
			for(String key:info.getData().keySet()){
				data.put(key, ModelConverter.buildAvailableModelFromData(info.getData().get(key)));
			}
			
		} catch (FDEcommServiceException e) {
			e.printStackTrace();
		}catch (FDResourceException e) {
			e.printStackTrace();
		}catch(JsonMappingException e){
			e.printStackTrace();
		}catch(JsonParseException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();

		}
		return data;
	}

	@Override
	public void resubmitOrder(String saleId, CustomerRatingI cra,
			EnumSaleType saleType, String deliveryRegionId)
			throws ErpTransactionException,RemoteException {

		Request<ObjectNode> request = new Request<ObjectNode>();
		ObjectNode rootNode = getMapper().createObjectNode();
		rootNode.set("customerRating", getMapper().convertValue(CustomerRatingConverter.buildCustomerRatingData(cra), JsonNode.class));
		rootNode.put("saleType", saleType.getSaleType());
		rootNode.put("saleId", saleId);
		rootNode.put("regionId", deliveryRegionId);
		request.setData(rootNode);
		Response<String> info=null;
		String inputJson;
		try {
			inputJson = buildRequest(request);
			String response = postData(inputJson, getFdCommerceEndPoint(ORDER_RESUBMIT), String.class);
			info = getMapper().readValue(response, new TypeReference<Response<String>>() { });
			
			if(!info.getResponseCode().equals("OK"))
				throw new ErpTransactionException(info.getMessage());
					
		} catch (FDEcommServiceException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}catch (FDResourceException e) {
			e.printStackTrace();
		}catch(JsonMappingException e){
			e.printStackTrace();
		}catch(JsonParseException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();

		}
	}
	
}
