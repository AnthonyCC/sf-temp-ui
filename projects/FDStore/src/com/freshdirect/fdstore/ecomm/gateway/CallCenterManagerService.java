package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.ecomm.converter.CustomerRatingConverter;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecomm.gateway.ErpComplaintManagerService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.CustomerRatingAdapterData;
import com.freshdirect.ecommerce.data.customer.ErpRedeliveryData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.ecomm.converter.CallCenterConverter;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CallCenterManagerService extends AbstractEcommService implements CallCenterManagerServiceI{
	
private final static Category LOGGER = LoggerFactory.getInstance(CallCenterManagerService.class);

private static final String RESUBMIT_ORDER = "callcenter/resubmitOrder/saleId/";
private static final String RESUBMIT_CUSTOMER = "callcenter/resubmitCustomer/customerId/";
private static final String SCHEDULE_REDELIVERY = "callcenter/scheduleRedelivery/saleId/";
private static final String EMAIL_CUTTOFF_TIME_REPORT = "callcenter/cuttOffTime/report/email/date/";
private static final String SAVE_TOP_FAQs = "callcenter/topFaqs/save";
private static final String VOID_CAPTURE_ORDER = "callcenter/voidCaptureOrder/saleId/";

	private static CallCenterManagerService INSTANCE;
	
	
	public static CallCenterManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CallCenterManagerService();

		return INSTANCE;
	}


	@Override
	public Map<String, List<ErpComplaintReason>> getComplaintReasons(boolean excludeCartonReq) throws FDResourceException,
			RemoteException {
		return ErpComplaintManagerService.getInstance().getReasons(excludeCartonReq);
	}

	@Override
	public void rejectMakegoodComplaint(String makegood_sale_id)throws FDResourceException, RemoteException {
		ErpComplaintManagerService.getInstance().rejectMakegoodComplaint(makegood_sale_id);
	}

	@Override
	public void resubmitOrder(String saleId, CustomerRatingI cra,EnumSaleType saleType) throws RemoteException, FDResourceException,
			ErpTransactionException {
		Request<CustomerRatingAdapterData> request = new Request<CustomerRatingAdapterData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CustomerRatingConverter.buildCustomerRatingData(cra));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(RESUBMIT_ORDER+saleId+"/saleType/"+saleType.getSaleType()),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}


	@Override
	public void resubmitCustomer(String customerID) throws FDResourceException,
			RemoteException {
		Response<String> response = new Response<String>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(RESUBMIT_CUSTOMER+customerID),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} 
	}

	@Override
	public void scheduleRedelivery(String saleId,ErpRedeliveryModel redeliveryModel) throws FDResourceException,
			ErpTransactionException, RemoteException {
		Request<ErpRedeliveryData> request = new Request<ErpRedeliveryData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CallCenterConverter.buildErpRedeliveryData(redeliveryModel));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SCHEDULE_REDELIVERY+saleId),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void emailCutoffTimeReport(Date day) throws FDResourceException,
			RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(EMAIL_CUTTOFF_TIME_REPORT + day.getTime()),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void saveTopFaqs(List faqIds) throws FDResourceException,
			RemoteException {
		Request<List<String>> request = new Request<List<String>>();
		try{
			request.setData(faqIds);
			String inputJson = buildRequest(request);
			 postDataTypeMap(inputJson,getFdCommerceEndPoint(SAVE_TOP_FAQs),new TypeReference<Response<String>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void voidCaptureOrder(String saleId) throws RemoteException,
			FDResourceException, ErpTransactionException {
		try {
			 this.httpGetDataTypeMap(getFdCommerceEndPoint(VOID_CAPTURE_ORDER+saleId),  new TypeReference<Response<String>>(){});
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	

}
