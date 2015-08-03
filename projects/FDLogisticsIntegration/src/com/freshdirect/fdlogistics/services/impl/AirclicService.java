package com.freshdirect.fdlogistics.services.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.crm.CallLogModel;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.IAirclicService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.CartonRequest;
import com.freshdirect.logistics.controller.data.request.DeliveryRequest;
import com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest;
import com.freshdirect.logistics.controller.data.request.TextMessageRequest;
import com.freshdirect.logistics.controller.data.response.AirclicCartonScanHistory;
import com.freshdirect.logistics.controller.data.response.AirclicTextMessages;
import com.freshdirect.logistics.controller.data.response.CartonStatusesResponse;
import com.freshdirect.logistics.controller.data.response.CartonTrackingResponse;
import com.freshdirect.logistics.controller.data.response.DeliveryETA;
import com.freshdirect.logistics.controller.data.response.DeliveryETAWindow;
import com.freshdirect.logistics.controller.data.response.DeliveryExceptions;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.DeliverySignature;
import com.freshdirect.logistics.controller.data.response.DeliverySummary;
import com.freshdirect.logistics.controller.data.response.ListOfObjects;
import com.freshdirect.logistics.controller.data.response.RouteNextelResponse;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.EnumDeliveryMenuOption;
import com.freshdirect.logistics.delivery.sms.model.CrmSmsDisplayInfo;

public class AirclicService extends AbstractLogisticsService implements IAirclicService  {

	private final static Category LOGGER = LoggerFactory
			.getInstance(AirclicService.class);

	private static final String SIGNATURE_DETAIL_API = "/delivery/signature";
	private static final String SIGNATURE_API = "/delivery/resource/signature/";
	private static final String SEND_MESSAGE_API = "/delivery/message/send";
	private static final String GET_CARTON_API = "/delivery/cartoninfo";
	private static final String GET_MESSAGES_API = "/delivery/message/get";
	private static final String GET_MANIFEST_API = "/delivery/manifest";
	private static final String GET_ROUTENEXTTEL_API = "/delivery/resource/nexttel";
	private static final String GET_DELIVERYETA_WINDOW_API = "/delivery/etawindow";
	private static final String GET_DELIVERYETA_API = "/delivery/eta";
	private static final String GET_DELIVERYSUMMARY_API = "/delivery/summary";
	private static final String GET_DELIVERYREQ_API = "/orders/delivery/req/";
	private static final String GET_DELIVERYEXCEPTION_API = "/delivery/exceptions";
	private static final String SCAN_LATE_API = "/delivery/scanlates";
	private static final String SMS_API = "/delivery/sms";
	
	private static final String IVR_CALLLOG_API = "/orders/ivrlog/";
	private static final String CARTON_LIST_API = "/orders/cartons/";
	private static final String GET_ORDER_MANIFEST_API = "/orders/manifest/";
	
	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#sendMessage(java.lang.String, com.freshdirect.logistics.controller.data.request.TextMessageRequest)
	 */
	
	
	
	@Override
	public Result sendMessage(String[] data, List<String> nextTelList) throws FDLogisticsServiceException {

		int stop = 0;
		if(data[2]!=null) stop = Integer.parseInt(data[2]);
							
		TextMessageRequest textMessageRequest = new TextMessageRequest(data[0], data[1], stop ,
				data[3], data[4], data[5], data[6], data[7]);
		textMessageRequest.setNextTelList(nextTelList);
		String inputJson = buildRequest(textMessageRequest);
		Result result =  getData(inputJson, getEndPoint(SEND_MESSAGE_API), Result.class);
		return result;
		
	
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getMessages(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest)
	 */
	
	
	@Override
	public AirclicTextMessages getMessages(String orderId) throws FDLogisticsServiceException {
		
		DeliverySignatureRequest request = new DeliverySignatureRequest();
		request.setOrderId(orderId);
		String inputJson = buildRequest(request);
		AirclicTextMessages result =  getData(inputJson, getEndPoint(GET_MESSAGES_API), AirclicTextMessages.class);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getSignatureDetails(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest)
	 */
	
	
	@Override
	public DeliverySignature getSignatureDetails(String orderId) throws FDLogisticsServiceException {
		DeliverySignatureRequest signatureRequest = new DeliverySignatureRequest();
		signatureRequest.setOrderId(orderId);
		String inputJson = buildRequest(signatureRequest);
		DeliverySignature signature =  getData(inputJson, getEndPoint(SIGNATURE_DETAIL_API), DeliverySignature.class);
		return signature;
		
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getCartonScanHistory(java.lang.String, com.freshdirect.logistics.controller.data.request.CartonRequest)
	 */
	
	
	@Override
	public AirclicCartonScanHistory getCartonScanHistory(List<String> cartonList) throws FDLogisticsServiceException {
		
		CartonRequest cartonRequest = new CartonRequest();
		cartonRequest.setCartonList(cartonList);
		String inputJson = buildRequest(cartonRequest);
		AirclicCartonScanHistory cartonScanHistory =  getData(inputJson, getEndPoint(GET_CARTON_API), AirclicCartonScanHistory.class);
		return cartonScanHistory;

	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getDeliveryManifest(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliveryRequest)
	 */
	
	
	@Override
	public DeliveryManifest getDeliveryManifest(String orderId,
			String deliveryDate) throws FDLogisticsServiceException {
		DeliveryRequest request =  new DeliveryRequest();
		request.setDeliveryDate(deliveryDate);
		request.setOrderId(orderId);
		String inputJson = buildRequest(request);
		DeliveryManifest response =  getData(inputJson, getEndPoint(GET_MANIFEST_API), DeliveryManifest.class);
		
		DeliveryManifest deliveryInfo =  getData(null, getOMSEndPoint(GET_ORDER_MANIFEST_API)+orderId, DeliveryManifest.class);
		
		response.setFirstName(deliveryInfo.getFirstName());
		response.setLastName(deliveryInfo.getLastName());
		response.setDeliveryInstructions(deliveryInfo.getDeliveryInstructions());
		response.setCartonCnt(deliveryInfo.getCartonCnt());
		return response;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getDeliveryETA(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest)
	 */
	
	
	@Override
	public DeliveryETA getDeliveryETA(String orderId) throws FDLogisticsServiceException {

		DeliverySignatureRequest request =  new DeliverySignatureRequest();
		request.setOrderId(orderId);
		String inputJson = buildRequest(request);
		DeliveryETA response =  getData(inputJson, getEndPoint(GET_DELIVERYETA_API), DeliveryETA.class);
		return response;

	
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getRouteNextels(java.lang.String, com.freshdirect.logistics.controller.data.request.TextMessageRequest)
	 */
	
	
	@Override
	public RouteNextelResponse getRouteNextels(String orderId, String route,
			String deliveryDate) throws FDLogisticsServiceException {
		
		TextMessageRequest request = new TextMessageRequest(deliveryDate, route, 0, null, null, null, orderId);
		String inputJson = buildRequest(request);
		RouteNextelResponse response =  getData(inputJson, getEndPoint(GET_ROUTENEXTTEL_API), RouteNextelResponse.class);
		return response;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getDeliverySummary(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliveryRequest)
	 */
	
	
	@SuppressWarnings("unchecked")
	@Override
	public DeliverySummary getDeliverySummary(String orderId,
			String routeNo, String date) throws FDLogisticsServiceException {

		DeliveryRequest request = new DeliveryRequest();
		request.setOrderId(orderId);
		request.setRouteNo(routeNo);
		request.setDeliveryDate(date);
		String inputJson = buildRequest(request);
		DeliverySummary response =  getData(inputJson, getEndPoint(GET_DELIVERYSUMMARY_API), DeliverySummary.class);
		
		ListOfObjects<CallLogModel> modelList  =  getData(null, getOMSEndPoint(GET_DELIVERYREQ_API+orderId), ListOfObjects.class);
		
		
		Date maxDlvAccessCallTime = null;
        Date maxEarlyDlvCallTime = null;
		
        for(CallLogModel model : modelList.getData()) {
			if(EnumDeliveryMenuOption.DELIVERY_ACCESS.getName().equals(model.getCallOutcome()) && (maxDlvAccessCallTime == null || (model.getStartTime() != null && model.getStartTime().after(maxDlvAccessCallTime))) ) {
				response.setDeliveryAccessReq(true);
				response.setDlvAccessStatus((null != model.getCallOutcome() && !"".equals(model.getCallOutcome()) && !"NoAnswer".equals(model.getCallOutcome()) && !"ReceiverRejected".equals(model.getCallOutcome())) ? "Y" : "N");
			}
			
			if(EnumDeliveryMenuOption.EARLY_DELIVERY.getName().equals(model.getCallOutcome()) && (maxEarlyDlvCallTime == null || (model.getStartTime() != null && model.getStartTime().after(maxEarlyDlvCallTime))) ) {
				response.setEarlyDeliveryReq(true);
				response.setEarlyDlvStatus((null != model.getCallOutcome() && !"".equals(model.getCallOutcome()) && !"NoAnswer".equals(model.getCallOutcome()) && !"ReceiverRejected".equals(model.getCallOutcome())) ? "Y" : "N");
			}
        }
		
		StringBuffer strBuf = new StringBuffer();
		
		if (response.isEarlyDeliveryReq()) {
			strBuf.append("Early ").append("(")
					.append(response.getEarlyDlvStatus()).append(")")
					.append(" / ");
		}
		if (response.isDeliveryAccessReq()) {
			strBuf.append("Access ").append("(")
					.append(response.isDeliveryAccessReq()).append(")");
		}
		response.setCustomerContactStatus(strBuf.toString());
		return response;
	
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getDeliveryExceptions(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest)
	 */
	
	
	@Override
	public Map<String, DeliveryException> getCartonScanInfo() throws FDLogisticsServiceException {
		
		DeliveryExceptions response =  getData(null, getEndPoint(GET_DELIVERYEXCEPTION_API), DeliveryExceptions.class);
		Map<String, DeliveryException> result = response.getData();
		return result;
	}

	
	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getCartonTrackingByOrderId(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest)
	 */
	
	
	@Override
	public CartonTrackingResponse getCartonTrackingByOrderId(
			String companycode, DeliverySignatureRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getCartonStatusByOrderId(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest)
	 */
	
	
	@Override
	public CartonStatusesResponse getCartonStatusByOrderId(String companycode,
			DeliverySignatureRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getSignature(java.lang.String, java.lang.String)
	 */
	
	
	@Override
	public byte[] getSignature(String orderId) throws FDLogisticsServiceException {

		return getData(null, SIGNATURE_API+orderId, byte[].class);
			
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdlogistics.services.impl.IAirclicService#getDeliveryETAWindow(java.lang.String, com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest)
	 */
	
	
	@Override
	public DeliveryETAWindow getDeliveryETAWindow(String orderId) throws FDLogisticsServiceException {
		
		DeliverySignatureRequest request = new DeliverySignatureRequest();
		request.setOrderId(orderId);
		String inputJson = buildRequest(request);
		DeliveryETAWindow response =  getData(inputJson, getEndPoint(GET_DELIVERYETA_WINDOW_API), DeliveryETAWindow.class);
		return response;
	}

	@Override
	public ListOfObjects getCartonList(String orderId) throws FDLogisticsServiceException {
		ListOfObjects<String> response =  getData(null, getOMSEndPoint(CARTON_LIST_API)+orderId, ListOfObjects.class);
		return response;
	}

	@Override
	public ListOfObjects<CallLogModel> getOrderCallLog(String orderId)
			throws FDLogisticsServiceException {
		String response =  getData(null, getOMSEndPoint(IVR_CALLLOG_API)+orderId, String.class);
		ListOfObjects<CallLogModel> info = new ListOfObjects<CallLogModel>();
		try{
			info = getMapper().readValue(response, new TypeReference<ListOfObjects<CallLogModel>>() { });
		}catch(Exception e){
			LOGGER.info("Exception converting {} to ListOfObjects"+response);
		}
		return info;
	}

	@Override
	public ListOfObjects getScanReportedLates()
			throws FDLogisticsServiceException {
		ListOfObjects result =  getData(null, getEndPoint(SCAN_LATE_API), ListOfObjects.class);
		return result;
	}	
	
	@Override
	public ListOfObjects<CrmSmsDisplayInfo> getSmsInfo(String orderId)
			throws FDLogisticsServiceException {
		DeliverySignatureRequest request = new DeliverySignatureRequest();
		request.setOrderId(orderId);
		String inputJson = buildRequest(request);
		String result = getData(inputJson, getEndPoint(SMS_API), String.class);
		ListOfObjects<CrmSmsDisplayInfo> info = new ListOfObjects<CrmSmsDisplayInfo>();
		try{
			info = getMapper().readValue(result , new TypeReference<ListOfObjects<CrmSmsDisplayInfo>>() { });
		}catch(Exception e){
			LOGGER.info("Exception converting {} to ListOfObjects"+result);
		}
		return info;
	}	
	
	/*public static void main(String s[]){
		try{
			ListOfObjects<CrmSmsDisplayInfo> info = getMapper().readValue(new File("file.json"), new TypeReference<ListOfObjects<CrmSmsDisplayInfo>>() { });
			System.err.println(info.getData());
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.info("Exception converting {} to ListOfObjects");
		}
	}*/
}
