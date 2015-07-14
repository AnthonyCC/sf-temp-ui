package com.freshdirect.fdlogistics.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.logistics.controller.data.request.OrderSearchCriteria;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.DiscountTimeslot;
import com.freshdirect.logistics.delivery.dto.OrderDTO;
import com.freshdirect.logistics.delivery.dto.OrdersDTO;
import com.freshdirect.logistics.delivery.dto.OrdersSummaryDTO;
import com.freshdirect.logistics.delivery.model.CartonInfo;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.DeliverySummary;

public interface IOrderService {

	public OrderDTO getOrderById(String orderId) throws FDLogisticsServiceException;

	public Map<String, Integer> getOrderStatsByCutoff(
			Date deliveryDate, Date cutOff) throws FDLogisticsServiceException;

	public OrdersDTO getOrderByCutoff(Date deliveryDate, Date cutOff)
			throws FDLogisticsServiceException;

	public OrdersSummaryDTO getActiveOrderByArea(Date deliveryDate,
			boolean standingOrder) throws FDLogisticsServiceException;

	public OrdersDTO getOrderByCriteria(OrderSearchCriteria criteria) throws FDLogisticsServiceException;

	public OrdersDTO getOrderStatsByDate(Date deliveryDate)
			throws FDLogisticsServiceException;

	public OrdersDTO getStandingOrderByCriteria(OrderSearchCriteria criteria)
			throws FDLogisticsServiceException;

	Map<String, List<DiscountTimeslot>> getWindowSteeringDiscounts(Date deliveryDate)
			throws FDLogisticsServiceException;

	public OrdersDTO getOrders(OrderSearchCriteria criteria) throws FDLogisticsServiceException;

	public List<CallLogModel> getDeliveryReq(String orderId) throws FDLogisticsServiceException;

	List<CallLogModel> getOrderCallLog(String orderId)
			throws FDLogisticsServiceException;

	List<String> getCartonList(String orderId)
			throws FDLogisticsServiceException;

	public DeliveryManifest getDeliveryManifest(String orderId)
			throws FDLogisticsServiceException;

	Map<String, DeliveryException> getIVRCallLog(
			Map<String, DeliveryException> result, Date fromTime, Date toTime)
			throws FDLogisticsServiceException;

	public void saveCartonInfo(List<CartonInfo> data) throws FDLogisticsServiceException;

}