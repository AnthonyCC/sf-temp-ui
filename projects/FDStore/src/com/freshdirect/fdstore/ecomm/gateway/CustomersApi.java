package com.freshdirect.fdstore.ecomm.gateway;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.ObjectNotFoundException;


import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.ErpSaleData;
import com.freshdirect.ecommerce.data.customer.ProfileData;
import com.freshdirect.ecommerce.data.order.OrderSearchCriteriaRequest;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.sms.shortsubstitute.ShortSubstituteResponse;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.ModelConverter;

public class CustomersApi extends AbstractEcommService implements CustomersApiClientI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomersApi.class);
	
	private static CustomersApiClientI INSTANCE=new CustomersApi();
	
	public static CustomersApiClientI getInstance() {
		
		return INSTANCE;
	}
	
	public Collection<ErpPaymentMethodI> getPaymentMethods(String customerId) throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isActive(String customerId) throws FDResourceException {
		
			String data= httpGetData(getFdCommerceEndPoint(EndPoints.STATUS.getValue()), String.class, new Object[]{customerId});
			String isActive="";
			try {
				
				Response<String> response= getMapper().readValue(data, new TypeReference<Response<String>>() { });
				isActive=response.getData();
			} catch (JsonParseException e) {
				throw new FDResourceException(e);
			} catch (JsonMappingException e) {
				throw new FDResourceException(e);
			} catch (IOException e) {
				throw new FDResourceException(e);
			}
			return Boolean.getBoolean(isActive);
		
	}
	

	@Override
	public String setActive(String customerId) throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String setInActive(String customerId) throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getCustomerIdForUserId(String userId)throws FDResourceException {
		
		String data="";
		String customerId="";
		data = httpGetData(getFdCommerceEndPoint(EndPoints.CUSTOMER_ID_FOR_USER.getValue() +userId), String.class);
		try {
			Response<String> response= getMapper().readValue(data, new TypeReference<Response<String>>() { });
			customerId=response.getData();
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
		return customerId;
	}


	public static void main (String[] a) throws FDResourceException, RemoteException {
			
			CustomersApi customer =new CustomersApi();
			/*
			System.out.println(customer.isActive("12345"));
			System.out.println(customer.isActive("10236962595"));
			*/
			//System.out.println(customer.getCustomerIdForUserId("jayatest@gmail.com"));
			 // System.out.println(customer.getDefaultPaymentMethodId("10236962695"));
			  //System.out.println(customer.getDefaultShippingAddressId("10236962695"));//10236962695
			//System.out.println(customer.getProfile("10236962695"));//10236962695
			System.out.println(customer.getPromotionHistory("10236962595"));
			//System.out.println(customer.getProfileForCustomerId("10236962595"));//10236962695 
			
		}
	
	@Override
    public ProfileModel getProfile(String fdCustomerId) throws FDResourceException {
		
		String data="";
		ProfileData profileData=null;
		data = httpGetData(getFdCommerceEndPoint(EndPoints.PROFILES.getValue() ), String.class, new Object[]{fdCustomerId});
		try {
			Response<ProfileData> response= getMapper().readValue(data, new TypeReference<Response<ProfileData>>() { });
			profileData=response.getData();
			ProfileModel model=new ProfileModel();
			model.setPK(new PrimaryKey(fdCustomerId));
			model.setAttributes(profileData.getAttributes());
			return model;
			
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
			

	}

	@Override
    public ProfileModel getProfileForCustomerId(String customerId) throws FDResourceException {
		
		String data="";
		ProfileModel profileData=null;
		data = httpGetData(getFdCommerceEndPoint(EndPoints.PROFILES_FOR_CUSTOMER.getValue() ), String.class, new Object[]{customerId});
		try {
			Response<ProfileModel> response= getMapper().readValue(data, new TypeReference<Response<ProfileModel>>() { });
			profileData=response.getData();
			
			return profileData;
			
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
			

	}

	@Override
	public ErpPromotionHistory getPromotionHistory(String customerId) throws FDResourceException {
		
		String data="";
		data = httpGetData(getFdCommerceEndPoint(EndPoints.PROMOTION_HISTORY.getValue() ), String.class, new Object[]{customerId});
		try {
			Response<ErpPromotionHistory> response= getMapper().readValue(data, new TypeReference<Response<ErpPromotionHistory>>() { });
			return response.getData();
			
			
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
			
	}
@Override
public FDOrderI getLastNonCOSOrder(String customerID,	EnumSaleType saleType, EnumSaleStatus saleStatus, EnumEStoreId storeId)
		throws ErpSaleNotFoundException, RemoteException {
		Request<OrderSearchCriteriaRequest> request = new Request<OrderSearchCriteriaRequest>();
		OrderSearchCriteriaRequest criteria = new OrderSearchCriteriaRequest();
		criteria.setCustomerId(customerID);
		criteria.setSaleType(saleType.getSaleType());
		criteria.setSaleStatus(saleStatus.getStatusCode());
		criteria.setStoreId(storeId.getContentId());
		request.setData(criteria);
		ErpSaleModel saleModel=null;
		try{
		String inputJson = buildRequest(request);
		String response = postData(inputJson, getFdCommerceEndPoint(EndPoints.GET_ORDERS_BY_SALESTATUS_STORE_API.getValue()), String.class);
		if(response!=null&&response.equals("JsonProcessingException")){
			LOGGER.info("JsonProcessingException in ecom Node");
			throw new JsonMappingException("JsonProcessingException in eCom Node");
		}
		if(response==null||response.trim().equals("")){
			LOGGER.info("Response is null or Empty");
			throw new FDResourceException("Response is null or Empty");
		}
		
		Response<ErpSaleModel> info = getMapper().readValue(response, new TypeReference<Response<ErpSaleModel>>() { });
		if(info.getResponseCode().equals("500")){
			if ("ErpSaleNotFoundException".equals(info.getMessage())) {
				throw new ErpSaleNotFoundException(info.getMessage());
			}
			if ("JsonProcessingException".equals(info.getMessage())) {
				throw new JsonMappingException(info.getMessage());
			}
		}
		saleModel =getMapper().readValue(info.getData(), new TypeReference<ErpSaleModel>() { });//ModelConverter.buildErpSaleData(info.getData());
		cleanUpSaleModel(saleModel);
		}catch(FDEcommServiceException e){
			LOGGER.info(e);
			throw new RemoteException(e.getMessage());
		}catch(FDResourceException e){
			LOGGER.info(e);
			throw new RemoteException(e.getMessage());
		}catch(JsonMappingException e){
			LOGGER.info(e);
			throw new RemoteException(e.getMessage());
		}catch(JsonParseException e){
			LOGGER.info(e);
			throw new RemoteException(e.getMessage());
		}catch(IOException e){
			
		}

		return new FDOrderAdapter(saleModel);	

	}

private void cleanUpSaleModel(ErpSaleModel saleModel) {
	if (saleModel.getCartonMetrics() != null && saleModel.getCartonMetrics().containsKey("")) {
		saleModel.getCartonMetrics().put(null, saleModel.getCartonMetrics().get(""));
		saleModel.getCartonMetrics().remove("");
	}
	
}

@Override
public FDOrderI getLastNonCOSOrder(String customerID, EnumSaleType saleType,	EnumEStoreId eStore) throws FDResourceException,
		ErpSaleNotFoundException, RemoteException {
	ErpSaleModel saleModel = null;
	try{
		Request<OrderSearchCriteriaRequest> request = new Request<OrderSearchCriteriaRequest>();
		OrderSearchCriteriaRequest criteria = new OrderSearchCriteriaRequest();
		criteria.setCustomerId(customerID);
		criteria.setSaleType(saleType.getSaleType());
		criteria.setStoreId(eStore.getContentId());
		request.setData(criteria);
		String inputJson = buildRequest(request);
		String response = postData(inputJson, getFdCommerceEndPoint(EndPoints.GET_ORDERS_BY_SALETYPE_STORE_API.getValue()), String.class);
		Response<ErpSaleModel> info = getMapper().readValue(response, new TypeReference<Response<ErpSaleModel>>() { });
		if(info.getResponseCode().equals("500")){
			if ("ErpSaleNotFoundException".equals(info.getMessage())) {
				throw new ErpSaleNotFoundException(info.getMessage());
			}
		}
	saleModel =info.getData();//ModelConverter.buildErpSaleData(info.getData());
		
	}catch(FDEcommServiceException e){
		LOGGER.info(e);
		throw new RemoteException(e.getMessage());
	}catch(FDResourceException e){
		LOGGER.info(e);
		throw new RemoteException(e.getMessage());
	}catch(JsonMappingException e){
		LOGGER.info(e);
		throw new RemoteException(e.getMessage());
	}catch(JsonParseException e){
		LOGGER.info(e);
		throw new RemoteException(e.getMessage());
	}catch(IOException e){
		
	}
	return  new FDOrderAdapter(saleModel);	
	}

@Override
public ShortSubstituteResponse getShortSubstituteOrders(List<String> orderList)
		throws FDResourceException, RemoteException {

	try{
		Request<List<String>> request = new Request<List<String>>();

		request.setData(orderList);
		String inputJson = buildRequest(request);
		String response = postData(inputJson, getFdCommerceEndPoint(EndPoints.GET_SS_ORDERS.getValue()), String.class);
		Response<ShortSubstituteResponse> info = getMapper().readValue(response, new TypeReference<Response<ShortSubstituteResponse>>() { });
		return parseResponse(info);	
	}catch(Exception e){
		LOGGER.info(e);
		LOGGER.info("Exception converting {} to ListOfObjects ");
		throw new RemoteException(e.getMessage(), e);
		
	}
	}

@Override
public boolean isReadyForPick(String saleId) throws FDResourceException,
		RemoteException {

	try{
		String data= httpGetData(getFdCommerceEndPoint(EndPoints.GET_ORDER_IS_READY_TO_PICK.getValue()), String.class, new Object[]{saleId});

		Response<Boolean> info = getMapper().readValue(data, new TypeReference<Response<Boolean>>() { });
		return parseResponse(info);	
	}catch(Exception e){
		LOGGER.info(e);
		LOGGER.info("Exception converting {} to ListOfObjects ");
		throw new RemoteException(e.getMessage(), e);
		
	}
	}

@Override
public void releaseModificationLock(String saleId) throws FDResourceException,
		RemoteException {

	try{
		String data= httpGetData(getFdCommerceEndPoint(EndPoints.GET_ORDER_RELEASE_MODIFICATION_LOCK.getValue()), String.class, new Object[]{saleId});

		Response<Boolean> info = getMapper().readValue(data, new TypeReference<Response<Boolean>>() { });
		if(!info.getResponseCode().equals("OK")){
			throw new FDResourceException(info.getMessage());
		}
	}catch(Exception e){
		LOGGER.info(e);
		LOGGER.info("Exception converting {} to ListOfObjects ");
		throw new RemoteException(e.getMessage(), e);
		
	}
	}


}
