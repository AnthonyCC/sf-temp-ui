package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class OrderServiceApiClient extends AbstractEcommService implements OrderServiceApiClientI {
	
	private static OrderServiceApiClient INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(CrmManagerService.class);

	private static final String ORDER_HISTORY_API = "customers/{customerId}/orders";
	private static final String LAST_ORDER_ID_API = "customers/{customerId}/orders/last";
	private static final String LAST_ORDER_ID_ESTORE_API = "customers/{customerId}/estore/{eStore}/orders/last";
	private static final String DELIVERY_ORDER_API = "orders/{orderId}";
	private static final String ORDERS_BYDLVPASS_API = "customers/{customerId}/orders/deliverypass/{dlvPassId}";
	private static final String ORDERS_BYDLVPASS_RECENT_API = "customers/{customerId}/orders/deliverypass/{dlvPassId}/{noOfDaysOld}";
	private static final String DLVPASS_USAGE_API = "customers/{customerId}/deliverypassUsage";
	private static final String VALID_ORDERCOUNT_API = "customers/{customerId}/orders/count";
	private static final String ORDER_BELONGS_TOUSER_API = "customers/{customerId}/orders/{orderId}/";
	private static final String WEB_ORDER_HISTORY_API = "customers/{customerId}/orders?web";
	
	public static OrderServiceApiClientI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OrderServiceApiClient();

		return INSTANCE;
	}

	@Override
	public List<ErpSaleInfo> getOrderHistory(String customerId)throws FDResourceException, RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(ORDER_HISTORY_API), String.class, new Object[]{customerId});
			List<ErpSaleInfo> info = new ArrayList<ErpSaleInfo>();
			try{
				info = getMapper().readValue(response, new TypeReference<Response<List<ErpSaleInfo>>>() { });
			}catch(Exception e){
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
			}
			return info;	
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public String getLastOrderId(String customerId) throws RemoteException {
		try{
			return httpGetData(getFdCommerceEndPoint(LAST_ORDER_ID_API), String.class, new Object[]{customerId});
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public String getLastOrderId(String customerId, EnumEStoreId eStoreId)
			throws RemoteException {
		try{
			return httpGetData(getFdCommerceEndPoint(LAST_ORDER_ID_ESTORE_API), String.class, new Object[]{customerId, (eStoreId!=null)? eStoreId.getContentId():EnumEStoreId.FD.getContentId() });
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public DlvSaleInfo getDlvSaleInfo(String orderNumber)
			throws RemoteException {
		try{
			return httpGetData(getFdCommerceEndPoint(DELIVERY_ORDER_API), DlvSaleInfo.class, new Object[]{orderNumber});
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public List<ErpSaleInfo> getOrdersByDlvPassId(String customerId,
			String dlvPassId) throws RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(ORDERS_BYDLVPASS_API), String.class, new Object[]{customerId, dlvPassId});
			List<ErpSaleInfo> info = new ArrayList<ErpSaleInfo>();
			try{
				info = getMapper().readValue(response, new TypeReference<Response<List<ErpSaleInfo>>>() { });
			}catch(Exception e){
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
			}
			return info;	
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public List<DlvPassUsageLine> getRecentOrdersByDlvPassId(
			String customerId, String dlvPassId, int noOfDaysOld)
			throws RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(ORDERS_BYDLVPASS_RECENT_API), String.class, new Object[]{customerId, dlvPassId, noOfDaysOld});
			List<DlvPassUsageLine> info = new ArrayList<DlvPassUsageLine>();
			try{
				info = getMapper().readValue(response, new TypeReference<Response<List<DlvPassUsageLine>>>() { });
			}catch(Exception e){
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
			}
			return info;	
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}
	
	@Override
	public Map<String, DlvPassUsageInfo>  getDlvPassesUsageInfo(String customerId)
			throws RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(DLVPASS_USAGE_API), String.class, new Object[]{customerId});
			Map<String, DlvPassUsageInfo>  info = new HashMap<String, DlvPassUsageInfo>();
			try{
				info = getMapper().readValue(response, new TypeReference<Response<Map<String, DlvPassUsageInfo> >>() { });
			}catch(Exception e){
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
			}
			return info;	
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public int getValidOrderCount(String customerId) throws RemoteException {
		try{
			return httpGetData(getFdCommerceEndPoint(VALID_ORDERCOUNT_API), Integer.class, new Object[]{customerId});
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public boolean isOrderBelongsToUser(String customerId, String orderId) throws RemoteException {
		try{
			return httpGetData(getFdCommerceEndPoint(ORDER_BELONGS_TOUSER_API), Boolean.class, new Object[]{customerId, orderId});
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}
	
	@Override
	public List<ErpSaleInfo> getWebOrderHistory(String customerId)throws FDResourceException, RemoteException {
		try{
			String response =  httpGetData(getFdCommerceEndPoint(WEB_ORDER_HISTORY_API), String.class, new Object[]{customerId});
			List<ErpSaleInfo> info = new ArrayList<ErpSaleInfo>();
			try{
				info = getMapper().readValue(response, new TypeReference<Response<List<ErpSaleInfo>>>() { });
			}catch(Exception e){
				LOGGER.info("Exception converting {} to ListOfObjects "+response);
			}
			return info;	
		
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}
	
	
}