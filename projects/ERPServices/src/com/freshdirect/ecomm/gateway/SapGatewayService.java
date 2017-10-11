package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.ecomm.converter.SapGatewayConverter;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.sap.ChangeSalesOrderData;
import com.freshdirect.ecommerce.data.sap.SapCustomerData;
import com.freshdirect.ecommerce.data.sap.SapOrderData;
import com.freshdirect.ecommerce.data.sap.SapPostReturnCommandData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.command.SapPostReturnCommand;

public class SapGatewayService extends AbstractEcommService implements SapGatewayServiceI{
	
	private static SapGatewayService INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(SapGatewayService.class);

	private static final String CANCEL_SALES_ORDER = "sapgateway/cancelSalesOrder/webOrderNumber/";
	private static final String SEND_CREATE_CUSTOMER = "sapgateway/createCustomer/erpCustomerNumber/";
	private static final String SEND_RETURN_INVOICE = "sapgateway/returnInvoice";
	private static final String CHANGE_SALES_ORDER = "sapgateway/changeSaleOrder";
	private static final String SEND_CREATE_SALES_ORDER = "sapgateway/createSaleOrder/saleType/";
	private static final String CHECK_AVAILABILITY = "sapgateway/available/timeout/";
	
	public static SapGatewayServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SapGatewayService();

		return INSTANCE;
	}

	@Override
	public SapOrderI checkAvailability(SapOrderI order, long timeout)throws RemoteException {
		Request<SapOrderData> request = new Request<SapOrderData>();
		String inputJson;
		Response<SapOrderData> response = null;
		try{
			request.setData(SapGatewayConverter.buildSapOrderData(order));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECK_AVAILABILITY+timeout), new TypeReference<Response<SapOrderData>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return SapGatewayConverter.buildSapOrderI(response.getData());
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public void sendCreateSalesOrder(SapOrderI order, EnumSaleType saleType)throws RemoteException {
		Request<SapOrderData> request = new Request<SapOrderData>();
		String inputJson;
		try{
			request.setData(SapGatewayConverter.buildSapOrderData(order));
			inputJson = buildRequest(request);
			postData(inputJson,getFdCommerceEndPoint(SEND_CREATE_SALES_ORDER+saleType.getSaleType()), Response.class);
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void sendCreateCustomer(String erpCustomerNumber,SapCustomerI customer) throws RemoteException {
		Request<SapCustomerData> request = new Request<SapCustomerData>();
		String inputJson;
		try{
			request.setData(SapGatewayConverter.buildSapCustomerData(customer));
			inputJson = buildRequest(request);
			postData(inputJson,getFdCommerceEndPoint(SEND_CREATE_CUSTOMER+erpCustomerNumber), Response.class);
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void sendCancelSalesOrder(String webOrderNumber,String sapOrderNumber) throws RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CANCEL_SALES_ORDER + webOrderNumber+"/sapOrderNumber/"+sapOrderNumber),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void sendChangeSalesOrder(String webOrderNumber,String sapOrderNumber, SapOrderI order, boolean isPlantChanged)
			throws RemoteException {
		Request<ChangeSalesOrderData> request = new Request<ChangeSalesOrderData>();
		String inputJson;
		try{
			request.setData(SapGatewayConverter.buildChangeSalesOrderData(webOrderNumber,sapOrderNumber,order,isPlantChanged));
			inputJson = buildRequest(request);
			postData(inputJson,getFdCommerceEndPoint(CHANGE_SALES_ORDER), Response.class);
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void sendReturnInvoice(ErpAbstractOrderModel order, ErpInvoiceModel invoice, boolean cancelCoupons) throws RemoteException {
		Request<SapPostReturnCommandData> request = new Request<SapPostReturnCommandData>();
		String inputJson;
		try{
			request.setData(SapGatewayConverter.buildSapPostReturnCommandData(order,invoice,cancelCoupons));
			inputJson = buildRequest(request);
			postData(inputJson,getFdCommerceEndPoint(SEND_RETURN_INVOICE), Response.class);
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

}
