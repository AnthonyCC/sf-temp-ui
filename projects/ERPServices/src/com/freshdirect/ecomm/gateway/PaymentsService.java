package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PaymentsService extends AbstractEcommService implements PaymentServiceI{

	private final static Category LOGGER = LoggerFactory.getInstance(PaymentsService.class);


	private static final String CAPTURE_AUTHORIZATION = "payment/authorization/capture/saleId/";
	private static final String VOID_CAPTURES = "payment/void/capture/saleId/";
	private static final String DELIVERY_CONFIRM = "payment/delivery/confirm/saleId/";
	private static final String CAPTURE_AUTH_EBT_SALE = "payment/unconfirm/saleId/";
	private static final String UNCONFIRM = "payment/authEbtSale/capture/saleId/";
	
	
	private static PaymentsService INSTANCE;
	
	
	public static PaymentServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PaymentsService();

		return INSTANCE;
	}
	@Override
	public void captureAuthorization(String saleId)throws ErpTransactionException, RemoteException {
		Response<String> response = new Response<String>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(CAPTURE_AUTHORIZATION+saleId),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	public void voidCaptures(String saleId) throws ErpTransactionException,RemoteException {
		Response<String> response = new Response<String>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(VOID_CAPTURES+saleId),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void deliveryConfirm(String saleId) throws ErpTransactionException,RemoteException {
		Response<String> response = new Response<String>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(DELIVERY_CONFIRM +saleId),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void unconfirm(String saleId) throws ErpTransactionException,
			RemoteException {
		Response<String> response = new Response<String>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(UNCONFIRM+saleId),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void captureAuthEBTSale(String saleId)
			throws ErpTransactionException, RemoteException {
		Response<String> response = new Response<String>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(CAPTURE_AUTH_EBT_SALE+saleId),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

}
