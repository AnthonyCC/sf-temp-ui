package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.CustomMapper;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDResourceException;
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
			String response =  httpGetData(getFdCommerceEndPoint(GET_ORDERS_API), String.class, new Object[]{ids});
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
			String response =  httpGetData(getFdCommerceEndPoint(GET_ORDERS_BY_PYMTTYPE_API), String.class, new Object[]{customerID,
				 saleType, saleStatus,
				 paymentType});
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
			String response =  httpGetData(getFdCommerceEndPoint(GET_ORDERS_BY_PYMTTYPES_API), String.class, new Object[]{customerID,
				 saleType, saleStatus,
				 pymtMethodTypes});
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
		response = postData(inputJson, getFdCommerceEndPoint(CUTOFF_ORDER_API), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		
	
	}

	
	@Override
	public void updateWaveInfo(String saleId, ErpShippingInfo shippingInfo)
			throws RemoteException, FinderException, FDResourceException {
		

		String inputJson=null;
		Response<String> response = null;
		response = postData(inputJson, getFdCommerceEndPoint(UPDATE_WAVE_INFO_API), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		
	
	}

	@Override
	public void updateCartonInfo(String saleId, List<ErpCartonInfo> cartonList)
			throws RemoteException, FinderException, FDResourceException {
		

		String inputJson=null;
		Response<String> response = null;
		response = postData(inputJson, getFdCommerceEndPoint(UPDATE_CARTON_INFO_API), Response.class);
		if(!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());
		
	
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
}
