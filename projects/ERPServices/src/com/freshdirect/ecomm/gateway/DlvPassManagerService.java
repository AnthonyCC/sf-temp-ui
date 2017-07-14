package com.freshdirect.ecomm.gateway;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.dlvpass.DeliveryPassData;
import com.freshdirect.ecommerce.data.dlvpass.DeliveryPassDataWrapper;
import com.freshdirect.ecommerce.data.dlvpass.DlvPassAutoRenewData;
import com.freshdirect.ecommerce.data.dlvpass.DlvPassStatusMapData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDPayPalServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.AbstractService;
import com.freshdirect.payment.service.ModelConverter;

public class DlvPassManagerService extends AbstractEcommService{
	
	private static DlvPassManagerService INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(DlvPassManagerService.class);
	
	private static final String CREATE_DLV_PASS = "dlvpass/create";
	private static final String APPLY_DLV_PASS = "dlvpass/apply";
	private static final String REVOKE_DLV_PASS = "dlvpass/revoke";
	private static final String MODIFY_DLV_PASS = "dlvpass/modify/";
	private static final String CANCEL_DLV_PASS = "dlvpass/cancel";
	private static final String REACTIVATE_DLV_PASS = "dlvpass/reactivate";
	private static final String CREDITDELIVERY_DLV_PASS = "dlvpass/creditdelivery/";
	private static final String EXTEND_EXPIRATION_PERIOD_DLV_PASS = "dlvpass/extendexpirationperiod/";
	private static final String GET_DLV_PASS_BY_CUST_ID = "dlvpass/getbycustomerid/";
	private static final String GET_DLV_PASS_BY_CUST_ID_AND_STATUS = "dlvpass/getbycustidandstatus/";
	private static final String GET_DLV_PASS_BY_ID = "dlvpass/getbyid/";
	private static final String GET_DLV_PASS_BY_ORDER_ID = "dlvpass/getbyorderid/";
	private static final String APPLY_NEW_DLV_PASS = "dlvpass/applynew";
	private static final String REMOVE_DLV_PASS = "dlvpass/remove";
	private static final String GET_ALL_DLV_PASS_STATUS = "dlvpass/getallstatusmap/";
	private static final String UPDATE_DLV_PASS = "dlvpass/updateprice/";
	private static final String ACTIVATE_READY_TO_USEPASS_DLV_PASS = "dlvpass/activatereadytousepass";
	private static final String REVOKE_DLV_PASS_USING_ACTIVEPASS = "dlvpass/revokeusingactivepass";
	private static final String DLV_PASS_HAS_PURCHASED = "dlvpass/haspurchasedpass/";
	private static final String GET_USABLE__AUTORENEW_DLV_PASS = "dlvpass/getusableautorenewpasses/";
	private static final String GET_AUTO_RENEWAL_INFO = "dlvpass/getautorenewalinfo";
	private static final String GET_DAYS_SINCE_DLV_PASS_EXPIRY = "dlvpass/getdayssincedpexpiry/";
	private static final String GET_DAYS_TO_EXPIRY_DLV_PASS = "dlvpass/getdaystodpexpiry/";
	private static final String GET_PENDING_PASSES= "dlvpass/getpendingpasses";
	

	
	public static DlvPassManagerService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DlvPassManagerService();

		return INSTANCE;
	}

	public String create(DeliveryPassModel model) throws RemoteException, DeliveryPassException {
		Response<String> response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(CREATE_DLV_PASS), Response.class);
			if(response.getResponseCode().equals(HttpStatus.BAD_REQUEST.toString()))
				throw new DeliveryPassException(response.getMessage());
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
		
	
	public void apply(DeliveryPassModel model) throws RemoteException, DeliveryPassException {
		Response response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(APPLY_DLV_PASS), Response.class);
			if(response.getResponseCode().equals(HttpStatus.BAD_REQUEST.toString()))
				throw new DeliveryPassException(response.getMessage());
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	public void revoke(DeliveryPassModel model) throws RemoteException {
		Response response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(REVOKE_DLV_PASS), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	
	public String modify(String purchaseOrderId, DeliveryPassModel model) throws RemoteException, DeliveryPassException {
		Response response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(MODIFY_DLV_PASS)+purchaseOrderId, Response.class);
			if(response.getResponseCode().equals(HttpStatus.BAD_REQUEST.toString()))
				throw new DeliveryPassException(response.getMessage());
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return (String) response.getData();
	}
	
	
	public void cancel(DeliveryPassModel model) throws RemoteException {
		Response response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(CANCEL_DLV_PASS), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	
	public void reactivate(DeliveryPassModel model) throws RemoteException {
		Response response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(REACTIVATE_DLV_PASS), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	
	public void creditDelivery(DeliveryPassModel model,int delta) throws RemoteException {
		Response response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(CREDITDELIVERY_DLV_PASS)+delta, Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	
	public void extendExpirationPeriod(DeliveryPassModel model,int noOfdays) throws RemoteException {
		Response response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(EXTEND_EXPIRATION_PERIOD_DLV_PASS)+noOfdays, Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	public List<DeliveryPassModel> getDeliveryPasses(String customerPk)throws RemoteException {
		Response<List<DeliveryPassData>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_DLV_PASS_BY_CUST_ID+customerPk),new TypeReference<Response<List<DeliveryPassData>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildDeliveryPassModelList(response.getData());
	}
	
	
	
	public List<DeliveryPassModel> getDlvPassesByStatus(String customerPk, EnumDlvPassStatus status)throws RemoteException {
		Response<List<DeliveryPassData>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_DLV_PASS_BY_CUST_ID_AND_STATUS+customerPk+"/"+status.getName()),new TypeReference<Response<List<DeliveryPassData>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildDeliveryPassModelList(response.getData());
	}
	
	
	public DeliveryPassModel getDeliveryPassInfo(String deliveryPassId)throws RemoteException {
		Response<DeliveryPassData> response = null;
		String inputJson; 
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_DLV_PASS_BY_ID+deliveryPassId),new TypeReference<Response<DeliveryPassData>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildDeliveryPassModel(response.getData());
	}
	
	
	public List<DeliveryPassModel> getDlvPassesByOrderId(String orderId)throws RemoteException {
		Response<List<DeliveryPassData>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_DLV_PASS_BY_ORDER_ID+orderId),new TypeReference<Response<List<DeliveryPassData>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		} 
		return ModelConverter.buildDeliveryPassModelList(response.getData());
	}
	
	
	
	
	
	
	public void applyNew(DeliveryPassModel model) throws RemoteException {
		Response response = null;
		
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(APPLY_NEW_DLV_PASS), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	public void remove(DeliveryPassModel model) throws RemoteException {
		Response response = null;
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(REMOVE_DLV_PASS), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	public Map<Comparable, Serializable> getAllStatusMap(String customerPk)
			throws RemoteException {
		Response<DlvPassStatusMapData> response = null;
		DlvPassStatusMapData data = null;
		 Map<Comparable, Serializable> map = new  HashMap<Comparable, Serializable>();
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_ALL_DLV_PASS_STATUS+customerPk),new TypeReference<Response<DlvPassStatusMapData>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
			data = response.getData();
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return data.getStatusMap();
	}
	
	
	public void updatePrice(DeliveryPassModel model,double newPrice) throws RemoteException {
		Response response = null;
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(UPDATE_DLV_PASS)+newPrice, Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	public void activateReadyToUsePass(DeliveryPassModel model) throws RemoteException {
		Response response = null;
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		try{
			DeliveryPassData data = ModelConverter.buildDeliveryPassData(model);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(ACTIVATE_READY_TO_USEPASS_DLV_PASS), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	public void revoke(DeliveryPassModel appliedPass,
			DeliveryPassModel activePass) throws RemoteException {
		Response response = null;
		Request<DeliveryPassDataWrapper> request = new Request<DeliveryPassDataWrapper>();
		String inputJson;
		try{
			DeliveryPassData appliedPassData = ModelConverter.buildDeliveryPassData(appliedPass);
			DeliveryPassData activePassData = ModelConverter.buildDeliveryPassData(activePass);
			ModelConverter.buildDeliveryPassData(activePass);
			DeliveryPassDataWrapper data = new DeliveryPassDataWrapper();
			data.setActivePass(activePassData);
			data.setAppliedPass(appliedPassData);
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(REVOKE_DLV_PASS_USING_ACTIVEPASS), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	public boolean hasPurchasedPass(String customerPK)throws RemoteException {
		Response<Boolean> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(DLV_PASS_HAS_PURCHASED+customerPK),new TypeReference<Response<Boolean>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	public List<DeliveryPassModel> getUsableAutoRenewPasses(String customerPK)throws RemoteException {
		Response<List<DeliveryPassData>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_USABLE__AUTORENEW_DLV_PASS+customerPK),new TypeReference<Response<List<DeliveryPassData>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildDeliveryPassModelList(response.getData());
	}
	
	
	public Object[] getAutoRenewalInfo() throws RemoteException {
		Response<DlvPassAutoRenewData> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_AUTO_RENEWAL_INFO),new TypeReference<Response<DlvPassAutoRenewData>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData().getObj();
	}
	
	
	public int getDaysSinceDPExpiry(String customerID)throws RemoteException {
		Response<Integer> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_DAYS_SINCE_DLV_PASS_EXPIRY+customerID),new TypeReference<Response<Integer>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	public int getDaysToDPExpiry(String customerID,String activeDPID)throws RemoteException {
		Response<Integer> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_DAYS_TO_EXPIRY_DLV_PASS+customerID+"/"+activeDPID),new TypeReference<Response<Integer>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	public List<List<String>> getPendingPasses() throws RemoteException {
		Response<List<List<String>>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_PENDING_PASSES),new TypeReference<Response<List<List<String>>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
}