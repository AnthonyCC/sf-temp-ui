package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.CustomMapper;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.ecomm.converter.GiftCardModelDataConverter;
import com.freshdirect.ecomm.converter.SapGatewayConverter;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.giftcard.ErpGCDlvInformationHolderData;
import com.freshdirect.ecommerce.data.giftcard.GiftCardData;
import com.freshdirect.ecommerce.data.order.ErpSaleInfoData;
import com.freshdirect.ecommerce.data.sap.ErpGiftCardData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;

public class GiftCardManagerService extends AbstractEcommService implements GiftCardManagerServiceI {

	private static GiftCardManagerService INSTANCE;

	private final static Category LOGGER = LoggerFactory
			.getInstance(GiftCardManagerService.class);

	private static final String VERIFY_STATUS_AND_BALANCE = "giftcard/verifyStatusBalance/reloadBalance/";
	private static final String GIFTCARD_RECEPIENTS_FOR_CUSTOMER = "giftcard/recipients/customer/";
	private static final String GIFTCARD_RECEPIENTS_FOR_ORDERS = "giftcard/recipients/orders";
	private static final String GIFTCARD_ORDERS_FOR_CUSTOMER = "giftcard/orders/customerId/";
	private static final String GIFTCARD_ORDERS_REDEEMED_ORDERS_BYCUSTOMER = "giftcard/orders/redeemed/customerId/";
	private static final String GIFTCARD_ORDERS_REDEEMED_ORDERS_BYCERTNUM = "giftcard/orders/redeemed/certNum/";
	private static final String GIFTCARD_DELETED_BY_CUSTOMERID = "giftcard/deleted/customerId/";
	private static final String GIFTCARD_ORDER_RECEPIENTS_BY_SALEID = "giftcard/order/recipients/saleId/";
	private static final String GIFTCARD_VALIDATE_AND_BALANCE = "giftcard/validateAndBalance";
	private static final String GIFTCARD_RECEPIENTS_BY_CERTNUM = "giftcard/recipient/load/certNum/";
	private static final String GIFTCARD_BALANCE_TRANSFER = "giftcard//balance/transfer/customerId/";

		
	public static GiftCardManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new GiftCardManagerService();

		return INSTANCE;
	}



	@Override
	public void registerGiftCard(String saleId, double amount)
			throws FDResourceException, ErpTransactionException,
			RemoteException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public List loadRecipentsForOrder(String saleId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void resendGiftCard(String saleId, List recepientList,
			EnumTransactionSource source) throws RemoteException,
			FDResourceException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public List getGiftCardRecepientsForCustomer(String customerId)
			throws RemoteException, FDResourceException {
		Response<List<ErpGCDlvInformationHolderData>> response = new Response<List<ErpGCDlvInformationHolderData>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(GIFTCARD_RECEPIENTS_FOR_CUSTOMER+customerId), new TypeReference<Response<List<ErpGCDlvInformationHolderData>>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		List recipientsList = GiftCardModelDataConverter.getErpGCDlvInformationHolder(response.getData());
		
		return response.getData();
	}



	@Override
	public ErpGiftCardModel validate(String givexNum)
			throws InvalidCardException, CardInUseException, RemoteException,
			CardOnHoldException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List verifyStatusAndBalance(List giftcards, boolean reloadBalance)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public ErpGiftCardModel verifyStatusAndBalance(ErpGiftCardModel giftcard,
			boolean reloadBalance) throws RemoteException {
		try{
		Request<ErpGiftCardData> request = new Request<ErpGiftCardData>();
		ErpGiftCardData erpGiftCardData = (ErpGiftCardData)SapGatewayConverter.buildPaymentMethodData(giftcard);
		request.setData(erpGiftCardData);
		String inputJson = buildRequest(request);
		Response<ErpGiftCardData> response = null;
		response =  this.postDataTypeMap(inputJson, getFdCommerceEndPoint(VERIFY_STATUS_AND_BALANCE+reloadBalance), new TypeReference<Response<ErpGiftCardData>>() {});
		if(!response.getResponseCode().equals("OK")){
			throw new FDResourceException(response.getMessage());
		}
		return (ErpGiftCardModel)SapGatewayConverter.buildPaymentMethodModel( response.getData());
	} catch (FDEcommServiceException e) {
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}catch (FDResourceException e){
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}
	}



	@Override
	public void initiatePreAuthorization(String saleId)
			throws ErpTransactionException, RemoteException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void initiateCancelAuthorizations(String saleId)
			throws ErpTransactionException, RemoteException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public List preAuthorizeSales(String saleId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List reversePreAuthForCancelOrders(String saleId)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void postAuthorizeSales(String saleId) throws RemoteException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public List getGiftCardModel(GenericSearchCriteria resvCriteria)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List getGiftCardOrdersForCustomer(String erpCustomerPK)
			throws RemoteException, FDResourceException {
		Response<List<ErpSaleInfoData>> response = new Response<List<ErpSaleInfoData>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(GIFTCARD_ORDERS_FOR_CUSTOMER+erpCustomerPK), new TypeReference<Response<List<ErpSaleInfoData>>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		return CustomMapper.map(parseResponse(response));
	}



	@Override
	public Object getGiftCardRedeemedOrders(String erpCustomerPK, String certNum)
			throws RemoteException, FDResourceException {
		Response<List<ErpSaleInfoData>> response = new Response<List<ErpSaleInfoData>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(GIFTCARD_ORDERS_REDEEMED_ORDERS_BYCUSTOMER+erpCustomerPK+"/certNum/"+certNum), new TypeReference<Response<List<ErpSaleInfoData>>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		return CustomMapper.map(parseResponse(response));
	}



	@Override
	public Object getGiftCardRedeemedOrders(String certNum)
			throws RemoteException, FDResourceException {
		Response<List<ErpSaleInfoData>> response = new Response<List<ErpSaleInfoData>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(GIFTCARD_ORDERS_REDEEMED_ORDERS_BYCERTNUM+certNum), new TypeReference<Response<List<ErpSaleInfoData>>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		return CustomMapper.map(parseResponse(response));
	}



	@Override
	public List getAllDeletedGiftCard(String erpCustomerPK)
			throws RemoteException, FDResourceException {
		Response<List<ErpGiftCardData>> response = new Response<List<ErpGiftCardData>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(GIFTCARD_DELETED_BY_CUSTOMERID+erpCustomerPK), new TypeReference<Response<List<ErpGiftCardData>>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		List<ErpGiftCardModel> modelList = new ArrayList();
		for(ErpGiftCardData item:response.getData()){
			modelList.add((ErpGiftCardModel)SapGatewayConverter.buildPaymentMethodModel(item));
		}
		return modelList;
	}



	@Override
	public List getGiftCardForOrder(String saleId) throws RemoteException,
			FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List getGiftCardRecepientsForOrder(String saleId)
			throws RemoteException, FDResourceException {
		Response<List<ErpGCDlvInformationHolderData>> response = new Response<List<ErpGCDlvInformationHolderData>>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(GIFTCARD_ORDER_RECEPIENTS_BY_SALEID+saleId), new TypeReference<Response<List<ErpGCDlvInformationHolderData>>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		List recipientsList = GiftCardModelDataConverter.getErpGCDlvInformationHolder(response.getData());
		return recipientsList;
	}



	@Override
	public ErpGiftCardModel validateAndGetGiftCardBalance(String givexNum)
			throws RemoteException {
		try{
		Request<String> request = new Request<String>();
		request.setData(givexNum);
		String inputJson = buildRequest(request);
		Response<ErpGiftCardData> response = null;
		response =  this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GIFTCARD_VALIDATE_AND_BALANCE), new TypeReference<Response<ErpGiftCardData>>() {});
		if(!response.getResponseCode().equals("OK")){
			throw new FDResourceException(response.getMessage());
		}
		return (ErpGiftCardModel)SapGatewayConverter.buildPaymentMethodModel( response.getData());
	} catch (FDEcommServiceException e) {
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}catch (FDResourceException e){
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}
	}



	@Override
	public void transferGiftCardBalance(String customerid,String fromGivexNum, String toGivexNum,
			double amount) throws RemoteException {
		try{
			System.out.println("asdf");
		Request<GiftCardData> request = new Request<GiftCardData>();
		GiftCardData giftCardData = new GiftCardData();
		giftCardData.setFromGivexNum(fromGivexNum);
		giftCardData.setToGivexNum(toGivexNum);
		giftCardData.setAmount(amount);
		request.setData(giftCardData);
		String inputJson = buildRequest(request);
		Response<ErpGiftCardData> response = null;
		response =  this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GIFTCARD_BALANCE_TRANSFER+customerid), new TypeReference<Response<GiftCardData>>() {});
		if(!response.getResponseCode().equals("OK")){
			throw new FDResourceException(response.getMessage());
		}
	} catch (FDEcommServiceException e) {
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}catch (FDResourceException e){
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}
	}



	@Override
	public ErpGCDlvInformationHolder loadGiftCardRecipentByGivexNum(
			String fromGivexNum) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Map getGiftCardRecepientsForOrders(List saleIds)
			throws RemoteException, FDResourceException {
		try{
		Request<List<String>> request = new Request<List<String>>();
		request.setData(saleIds);
		String inputJson = buildRequest(request);
		Response<Map<String,List<ErpGCDlvInformationHolderData>>> response = null;
		response =  this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GIFTCARD_RECEPIENTS_FOR_ORDERS), new TypeReference<Response<Map<String,List<ErpGCDlvInformationHolderData>>>>() {});
		if(!response.getResponseCode().equals("OK")){
			throw new FDResourceException(response.getMessage());
		}
		Map<String,List<ErpGCDlvInformationHolderData>> map = new HashMap();
		Map<String,List<ErpGCDlvInformationHolderData>> responsemap =(Map<String,List<ErpGCDlvInformationHolderData>>)  response.getData();
		 for(Object key:responsemap.keySet()){
			 String keyy = (String)key;
			 map.put(keyy, GiftCardModelDataConverter.getErpGCDlvInformationHolder(responsemap.get(keyy)));
    			
    		}
		
		return map;
	} catch (FDEcommServiceException e) {
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}catch (FDResourceException e){
		LOGGER.error(e.getMessage());
		throw new RemoteException(e.getMessage());
	}
	}



	@Override
	public ErpGCDlvInformationHolder loadGiftCardRecipentByCertNum(
			String certNum) throws RemoteException {
		Response<ErpGCDlvInformationHolderData> response = new Response<ErpGCDlvInformationHolderData>();
		try {
			response = httpGetDataTypeMap(getFdCommerceEndPoint(GIFTCARD_RECEPIENTS_BY_CERTNUM+certNum), new TypeReference<Response<ErpGCDlvInformationHolderData>>() {});
		} catch (FDResourceException e) {
			
			throw new RemoteException(response.getMessage());
		}
		if(!response.getResponseCode().equals("OK"))
			throw new RemoteException(response.getMessage());
		ErpGCDlvInformationHolder holder = GiftCardModelDataConverter.buildErpGCDlvInformationHolder(response.getData());
		return holder;
	}


}
