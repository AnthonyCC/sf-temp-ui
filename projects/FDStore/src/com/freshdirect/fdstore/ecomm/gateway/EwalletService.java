package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.fdstore.EwalletData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecomm.converter.EwalletConverter;
import com.freshdirect.fdstore.ewallet.EwalletRequestData;
import com.freshdirect.fdstore.ewallet.EwalletResponseData;

public class EwalletService  extends AbstractEcommService implements EwalletServiceI {
	
	private static final String GET_TOKEN = "/ewallet/getToken";
	private static final String CHECKOUT = "/ewallet/checkout";
	private static final String EXP_CHECKOUT = "/ewallet/expressCheckout";
	private static final String ALL_PAYMETHOD = "/ewallet/allPayMethod";
	private static final String CONNECT_COMPLETE = "/ewallet/connectComplete";
	private static final String DISCONNECT = "/ewallet/disconnect";
	private static final String POST_BACK_TRANX = "/ewallet/postbackTrxns";
	private static final String STD_CHECKOUT = "/ewallet/standardCheckout";
	private static final String PRE_STD_CHECKOUT = "/ewallet/preStandardCheckout";
	private static final String ADD_PAYPAL_WALLET = "/ewallet/addPayPalWallet";
	
	private static EwalletServiceI INSTANCE;
	
	
	public static EwalletServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new EwalletService();

		return INSTANCE;
	}
	
	
	@Override
	public EwalletResponseData getToken(EwalletRequestData ewalletRequestData)
			throws RemoteException {
		
		Response<com.freshdirect.ecommerce.data.fdstore.EwalletResponseData> response = null;
		Request<EwalletData> request = new Request<EwalletData>();
			try {
				String inputJson;
				request.setData(EwalletConverter.buildEwalletData(ewalletRequestData));
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_TOKEN),new TypeReference<Response<com.freshdirect.ecommerce.data.fdstore.EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return EwalletConverter.buildEwalletResponseData(response.getData());
	}

	@Override
	public EwalletResponseData checkout(EwalletRequestData ewalletRequestData)
			throws RemoteException {
		
		Response<EwalletResponseData> response = null;
		Request<EwalletRequestData> request = new Request<EwalletRequestData>();
			try {
				String inputJson;
				request.setData(ewalletRequestData);
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECKOUT),new TypeReference<Response<EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return response.getData();
	}

	@Override
	public EwalletResponseData expressCheckout(
			EwalletRequestData ewalletRequestData) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EwalletResponseData connect(EwalletRequestData ewalletRequestData)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EwalletResponseData getAllPayMethodInEwallet(
			EwalletRequestData ewalletRequestData) throws RemoteException {
		
		Response<EwalletResponseData> response = null;
		EwalletResponseData result = null;
		Request<EwalletRequestData> request = new Request<EwalletRequestData>();
			try {
				String inputJson;
				request.setData(ewalletRequestData);
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(ALL_PAYMETHOD),new TypeReference<Response<EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return response.getData();
	}

	@Override
	public EwalletResponseData connectComplete(
			EwalletRequestData ewalletRequestData) throws RemoteException {
		
		Response<EwalletResponseData> response = null;
		EwalletResponseData result = null;
		Request<EwalletRequestData> request = new Request<EwalletRequestData>();
			try {
				request.setData(ewalletRequestData);
				String inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CONNECT_COMPLETE),new TypeReference<Response<EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return response.getData();
	}

	@Override
	public EwalletResponseData disconnect(EwalletRequestData ewalletRequestData)
			throws RemoteException {
		
		Response<EwalletResponseData> response = null;
		EwalletResponseData result = null;
		Request<EwalletData> request = new Request<EwalletData>();
		try {
				request.setData(EwalletConverter.buildEwalletData(ewalletRequestData));
				String inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(DISCONNECT),new TypeReference<Response<EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return response.getData();
	}

	@Override
	public EwalletResponseData postbackTrxns(EwalletRequestData req)
			throws RemoteException {
		
		Response<EwalletResponseData> response = null;
		Request<EwalletRequestData> request = new Request<EwalletRequestData>();
			try {
				request.setData(req);
				String inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(POST_BACK_TRANX),new TypeReference<Response<EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return response.getData();
	}

	@Override
	public EwalletResponseData standardCheckout(
			EwalletRequestData ewalletRequestData) throws RemoteException {
		
		Response<EwalletResponseData> response = null;
		Request<EwalletRequestData> request = new Request<EwalletRequestData>();
			try {
				request.setData(ewalletRequestData);
				String inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(STD_CHECKOUT),new TypeReference<Response<EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return response.getData();
	}

	@Override
	public EwalletResponseData preStandardCheckout(
			EwalletRequestData ewalletRequestData) throws RemoteException {
		
		Response<EwalletResponseData> response = null;
		Request<EwalletRequestData> request = new Request<EwalletRequestData>();
			try {
				request.setData(ewalletRequestData);
				String inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(PRE_STD_CHECKOUT),new TypeReference<Response<EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			} catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return response.getData();
	}

	@Override
	public EwalletResponseData addPayPalWallet(
			EwalletRequestData ewalletRequestData) throws RemoteException {
		
		Response<EwalletResponseData> response = null;
		EwalletResponseData result = null;
		Request<EwalletData> request = new Request<EwalletData>();
			try {
				String inputJson;
				request.setData(EwalletConverter.buildEwalletData(ewalletRequestData));
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(ADD_PAYPAL_WALLET),new TypeReference<Response<EwalletResponseData>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
			} catch (FDResourceException e) {
				throw new RemoteException(e.getMessage());
			}catch (FDEcommServiceException e) {
				throw new RemoteException(e.getMessage());
			}
			
			return response.getData();
	}



}
