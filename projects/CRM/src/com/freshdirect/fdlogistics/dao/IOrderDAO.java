package com.freshdirect.fdlogistics.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.logistics.controller.data.request.OrderSearchCriteria;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.DiscountTimeslot;
import com.freshdirect.logistics.delivery.dto.OrderDTO;
import com.freshdirect.logistics.delivery.dto.OrdersDTO;
import com.freshdirect.logistics.delivery.dto.OrdersSummaryDTO;
import com.freshdirect.logistics.delivery.model.CartonInfo;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.DeliverySummary;
import com.freshdirect.logistics.delivery.model.RouteStop;

public interface IOrderDAO {

	public Map<String, Integer> getOrderStatsByCutoff(
			Date deliveryDate, Date cutOff);

	public OrdersDTO getOrderByCutoff(Date deliveryDate, Date cutOff);

	public OrdersSummaryDTO getActiveOrderByArea(Date deliveryDate,
			boolean standingOrder);

	public OrdersDTO getOrderByCriteria(OrderSearchCriteria criteria);

	public OrdersDTO getOrderStatsByDate(Date deliveryDate);

	public OrdersDTO getStandingOrderByCriteria(OrderSearchCriteria criteria);

	public OrderDTO getOrderById(String orderId);

	public List<String> getCartonList(String orderId);

	public List<CallLogModel> getOrderCallLog(String orderId);

	Map<String, DeliveryException> getIVRCallLog(Map<String, DeliveryException> result, Date fromTime, Date toTime);

	DeliveryManifest getDeliveryManifest(String orderId);

	Map<String, List<DiscountTimeslot>> getWindowSteeringDiscounts(Date deliveryDate);

	public OrdersDTO getOrders(OrderSearchCriteria criteria);
	
	public List<CallLogModel> getDeliveryReq(final String orderId);

	public void saveCartonInfo(List<CartonInfo> data);

	public List<CartonInfo> getCartonInfo(String orderId);

	public void saveRouteStopInfo(List<RouteStop> data);

}