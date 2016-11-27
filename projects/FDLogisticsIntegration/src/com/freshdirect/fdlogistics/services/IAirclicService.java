package com.freshdirect.fdlogistics.services;

import java.util.List;
import java.util.Map;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.DeliverySignatureRequest;
import com.freshdirect.logistics.controller.data.request.TextMessageRequest;
import com.freshdirect.logistics.controller.data.response.AirclicCartonScanHistory;
import com.freshdirect.logistics.controller.data.response.AirclicTextMessages;
import com.freshdirect.logistics.controller.data.response.CartonStatusesResponse;
import com.freshdirect.logistics.controller.data.response.CartonTrackingResponse;
import com.freshdirect.logistics.controller.data.response.DeliveryETA;
import com.freshdirect.logistics.controller.data.response.DeliveryETAWindow;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.DeliverySignature;
import com.freshdirect.logistics.controller.data.response.DeliverySummary;
import com.freshdirect.logistics.controller.data.response.ListOfObjects;
import com.freshdirect.logistics.controller.data.response.RouteNextelResponse;
import com.freshdirect.logistics.delivery.model.CartonInfo;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.sms.model.CrmSmsDisplayInfo;

public interface IAirclicService {

	public Result sendMessage(TextMessageRequest textMessageRequest) throws FDLogisticsServiceException;

	public AirclicTextMessages getMessages(String orderId) throws FDLogisticsServiceException;

	public DeliverySignature getSignatureDetails(String orderId) throws FDLogisticsServiceException, FDResourceException;

	public AirclicCartonScanHistory getCartonScanHistory(List<String> cartonList) throws FDLogisticsServiceException;

	public DeliveryManifest getDeliveryManifest(String orderId,
			String deliveryDate,String companyCode) throws FDLogisticsServiceException;

	public DeliveryETA getDeliveryETA(String orderId) throws FDLogisticsServiceException;
	
	public RouteNextelResponse getRouteNextels(String orderId, String route,
			String date) throws FDLogisticsServiceException;

	public DeliverySummary getDeliverySummary(String orderId,
			String routeNo, String date,String erpOrderId,String companyCode) throws FDLogisticsServiceException;

	public Map<String, DeliveryException> getCartonScanInfo() throws FDLogisticsServiceException;

	public CartonTrackingResponse getCartonTrackingByOrderId(
			String companycode, DeliverySignatureRequest request) throws FDLogisticsServiceException;

	public CartonStatusesResponse getCartonStatusByOrderId(String companycode,
			DeliverySignatureRequest request) throws FDLogisticsServiceException;

	public byte[] getSignature(String orderId,String companyCode) throws FDLogisticsServiceException;

	public DeliveryETAWindow getDeliveryETAWindow(String orderId) throws FDLogisticsServiceException;

	public ListOfObjects<CartonInfo> getCartonList(String orderId) throws FDLogisticsServiceException;

	public ListOfObjects<CallLogModel> getOrderCallLog(String orderId) throws FDLogisticsServiceException;

	public ListOfObjects<String> getScanReportedLates() throws FDLogisticsServiceException;

	ListOfObjects<CrmSmsDisplayInfo> getSmsInfo(String orderId) throws FDLogisticsServiceException;

	
 }