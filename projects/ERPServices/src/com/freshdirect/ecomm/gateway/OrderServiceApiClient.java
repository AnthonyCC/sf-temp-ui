/*
 * @author tbalumuri
 */
package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.CustomMapper;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.dlvpass.DlvPassUsageLineData;
import com.freshdirect.ecommerce.data.order.DlvSaleInfoData;
import com.freshdirect.ecommerce.data.order.ErpSaleInfoData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class OrderServiceApiClient extends AbstractEcommService implements OrderServiceApiClientI {
	
	private static OrderServiceApiClient INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(OrderServiceApiClient.class);

	private static final String ORDER_HISTORY_API = "customers/{customerId}/orders";
	private static final String LAST_ORDER_ID_API = "customers/{customerId}/orders/last";
	private static final String LAST_ORDER_ID_ESTORE_API = "customers/{customerId}/estore/{eStore}/orders/last";
	private static final String DELIVERY_ORDER_API = "delivery/orders/{orderId}";
	private static final String ORDERS_BYDLVPASS_API = "customers/{customerId}/orders/deliverypass/{dlvPassId}";
	private static final String ORDERS_BYDLVPASS_RECENT_API = "customers/{customerId}/orders/deliverypass/{dlvPassId}/{noOfDaysOld}";
	private static final String DLVPASS_USAGE_API = "customers/{customerId}/deliverypassUsage";
	private static final String VALID_ORDERCOUNT_API = "customers/{customerId}/orders/count";
	private static final String ORDER_BELONGS_TOUSER_API = "customers/{customerId}/orders/{orderId}/";
	private static final String WEB_ORDER_HISTORY_API = "customers/{customerId}/orders?web";
	
	private static final String ORDERS_BYTRUCK = "orders/deliveryDate/{date}/truck/{truckId}";
	
	public static OrderServiceApiClientI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OrderServiceApiClient();

		return INSTANCE;
	}

	@Override
	public List<ErpSaleInfo> getOrderHistory(String customerId)throws FDResourceException, RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(ORDER_HISTORY_API), String.class, new Object[]{customerId});
			
			try{
				Response<List<ErpSaleInfoData>> info = getMapper().readValue(response, new TypeReference<Response<List<ErpSaleInfoData>>>() { });
				return CustomMapper.map(parseResponse(info));	
			}catch(Exception e){
				LOGGER.info(e);
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
				throw new FDResourceException(e);
				
			}
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
		
	}

	@Override
	public String getLastOrderId(String customerId) throws RemoteException {
		try{
			Response<String> info = httpGetData(getFdCommerceEndPoint(LAST_ORDER_ID_API), Response.class, new Object[]{customerId});
			return parseResponse(info);	
		}  catch (FDResourceException e) {
			LOGGER.info(e);
			throw new RemoteException(e.getMessage());
		}
		
		
	}
	
	@Override
	public String getLastOrderId(String customerId, EnumEStoreId eStoreId)
			throws RemoteException {
		try{
			Response<String> info = httpGetData(getFdCommerceEndPoint(LAST_ORDER_ID_ESTORE_API), Response.class, new Object[]{customerId, (eStoreId!=null)? eStoreId.getContentId():EnumEStoreId.FD.getContentId() });
			return parseResponse(info);	
		}  catch (FDResourceException e) {
			LOGGER.info(e);
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public DlvSaleInfo getDlvSaleInfo(String orderNumber)
			throws RemoteException {
		try{
			String response = httpGetData(getFdCommerceEndPoint(DELIVERY_ORDER_API), String.class, new Object[]{orderNumber});
			try{
				Response<DlvSaleInfoData> info  = getMapper().readValue(response, new TypeReference<Response<DlvSaleInfoData>>() { });
				return CustomMapper.map(parseResponse(info));
			}catch(Exception e){
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
				LOGGER.info(e);
				throw new FDResourceException(e);
			}
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<ErpSaleInfo> getOrdersByDlvPassId(String customerId,
			String dlvPassId) throws RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(ORDERS_BYDLVPASS_API), String.class, new Object[]{customerId, dlvPassId});
			try{
				Response<List<ErpSaleInfoData>> info  = getMapper().readValue(response, new TypeReference<Response<List<ErpSaleInfoData>>>() { });
				return CustomMapper.map(parseResponse(info));	
			}catch(Exception e){
				LOGGER.info(e);
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
				throw new FDResourceException(e);
			}
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.ecomm.gateway.OrderServiceApiClientI#getRecentOrdersByDlvPassId(java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<DlvPassUsageLine> getRecentOrdersByDlvPassId(
			String customerId, String dlvPassId, int noOfDaysOld)
			throws RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(ORDERS_BYDLVPASS_RECENT_API), String.class, new Object[]{customerId, dlvPassId, noOfDaysOld});
			try{
				Response<List<DlvPassUsageLineData>> info = getMapper().readValue(response, new TypeReference<Response<List<DlvPassUsageLineData>>>() { });
				return CustomMapper.mapDeliverypass(parseResponse(info));	
			}catch(Exception e){
				LOGGER.info(e);
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
				throw new FDResourceException(e);
			}
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}
	
	@Override
	public Map<String, DlvPassUsageInfo>  getDlvPassesUsageInfo(String customerId)
			throws RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(DLVPASS_USAGE_API), String.class, new Object[]{customerId});
			try{
				Response<Map<String, DlvPassUsageInfo>> info = getMapper().readValue(response, new TypeReference<Response<Map<String, DlvPassUsageInfo> >>() { });
				return parseResponse(info);	
			}catch(Exception e){
				LOGGER.info(e);
				LOGGER.info("Exception converting {} to getDlvPassesUsageInfo "+response);
				throw new FDResourceException(e);
			}
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public int getValidOrderCount(String customerId) throws RemoteException {
		try{
			String response = httpGetData(getFdCommerceEndPoint(VALID_ORDERCOUNT_API), String.class, new Object[]{customerId});
			try{
				Response<Integer> info = getMapper().readValue(response, new TypeReference<Response<Integer>>() { });
				return parseResponse(info);	
			}catch(Exception e){
				LOGGER.info(e);
				LOGGER.info("Exception converting {} to getValidOrderCount "+response);
				throw new FDResourceException(e);
			}
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public boolean isOrderBelongsToUser(String customerId, String orderId) throws RemoteException {
		try{
			String response = httpGetData(getFdCommerceEndPoint(ORDER_BELONGS_TOUSER_API), String.class, new Object[]{customerId, orderId});
			try{
				Response<Boolean> info = getMapper().readValue(response, new TypeReference<Response<Boolean>>() { });
				return parseResponse(info);	
			}catch(Exception e){
				LOGGER.info(e);
				LOGGER.info("Exception converting {} to isOrderBelongsToUser "+response);
				throw new FDResourceException(e);
			}
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public List<ErpSaleInfo> getWebOrderHistory(String customerId)throws FDResourceException, RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(WEB_ORDER_HISTORY_API), String.class, new Object[]{customerId});
			Response<List<ErpSaleInfoData>> info = null;
			try{
				info = getMapper().readValue(response, new TypeReference<Response<List<ErpSaleInfoData>>>() { });
				return CustomMapper.map(parseResponse(info));	
			}catch(Exception e){
				LOGGER.info(e);
				LOGGER.info("Exception converting {} to getWebOrderHistory "+response);
				throw new FDResourceException(e);
			}
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	private <T> T parseResponse(Response<T> info) throws FDResourceException {
		if(info!=null && info.getResponseCode().equalsIgnoreCase("OK")){
			return info.getData();
		}else{
			throw new FDResourceException("API error");
		}
	}

	@Override
	public List<DlvSaleInfo> getOrdersByTruck(String truckNumber, Date dlvDate) throws RemoteException {

		try{
			String response = httpGetData(getFdCommerceEndPoint(ORDERS_BYTRUCK), String.class, new Object[]{truckNumber, dlvDate});
			try{
				Response<List<DlvSaleInfoData>> info  = getMapper().readValue(response, new TypeReference<Response<List<DlvSaleInfoData> >>() { });
				return CustomMapper.mapDeliveryOrders(parseResponse(info));
			}catch(Exception e){
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
				LOGGER.info(e);
				throw new FDResourceException(e);
			}
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	
	}

	
	
}