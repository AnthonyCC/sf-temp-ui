package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpCreateOrderModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.ecomm.converter.CustomerRatingConverter;
import com.freshdirect.ecomm.converter.ErpFraudPreventionConverter;
import com.freshdirect.ecomm.converter.FDActionInfoConverter;
import com.freshdirect.ecomm.converter.SapGatewayConverter;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.order.CreateOrderRequestData;
import com.freshdirect.ecommerce.data.order.OrderSearchCriteriaRequest;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;

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
	private static final String GET_PERISHABLE_BUFFER_API = 	"orders/getPerishableBufferAmount";
	private static final String UPDATE_WAVE_INFO_API = 	"orders/{id}/waveInfo";
	private static final String UPDATE_CARTON_INFO_API = 	"orders/{id}/cartonInfo";
	private static final String GET_DELIVERYINFO_API = 	"orders/{id}/getDeliveryInfo";
	private static final String RESUBMIT_GC_ORDERS_API = 	"orders/resubmitNsmGcOrders";
	private static final String CREATE_GC_ORDER_API = 	"orders/gc/create";
	private static final String CREATE_SUB_ORDER_API = 	"orders/sub/create";
	
	private static final String CREATE_DON_ORDER_API = 	"orders/don/create";
	private static final String CREATE_REG_ORDER_API = 	"orders/reg/create";
	private static final String MODIFY_REG_ORDER_API = 	"orders/reg/modify";
	
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
	public double getPerishableBufferAmount(ErpAbstractOrderModel order)
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
}
