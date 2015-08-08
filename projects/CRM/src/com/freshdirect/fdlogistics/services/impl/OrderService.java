package com.freshdirect.fdlogistics.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.fdlogistics.dao.IOrderDAO;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.IOrderService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.controller.data.request.OrderSearchCriteria;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.DiscountTimeslot;
import com.freshdirect.logistics.delivery.dto.OrderDTO;
import com.freshdirect.logistics.delivery.dto.OrdersDTO;
import com.freshdirect.logistics.delivery.dto.OrdersSummaryDTO;
import com.freshdirect.logistics.delivery.model.CartonInfo;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.DeliverySummary;

@Component
public class OrderService implements IOrderService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(OrderService.class);

	@Autowired
	private IOrderDAO orderDAO;
	
	@Override
	public OrderDTO getOrderById(String orderId) throws FDLogisticsServiceException  {
		try {
			return orderDAO.getOrderById(orderId);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public Map<String, Integer> getOrderStatsByCutoff(
			Date deliveryDate, Date cutOff) throws FDLogisticsServiceException{
		try {
		return orderDAO.getOrderStatsByCutoff(deliveryDate, cutOff);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public OrdersDTO getOrderByCutoff(Date deliveryDate, Date cutOff)
			throws FDLogisticsServiceException{
		try {
			return orderDAO.getOrderByCutoff(deliveryDate, cutOff);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public OrdersSummaryDTO getActiveOrderByArea(Date deliveryDate,
			boolean standingOrder) throws FDLogisticsServiceException{
		try {
			return orderDAO.getActiveOrderByArea(deliveryDate, standingOrder);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public OrdersDTO getOrderByCriteria(OrderSearchCriteria criteria) throws FDLogisticsServiceException{
		try {
			return orderDAO.getOrderByCriteria(criteria);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public OrdersDTO getOrderStatsByDate(Date deliveryDate)
			throws FDLogisticsServiceException{
		try {
			return orderDAO.getOrderStatsByDate(deliveryDate);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public OrdersDTO getStandingOrderByCriteria(OrderSearchCriteria criteria)
			throws FDLogisticsServiceException{
		
		try {
			return orderDAO.getStandingOrderByCriteria(criteria);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	public IOrderDAO getOrderDAO() {
		return orderDAO;
	}

	public void setOrderDAO(IOrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	@Override
	public Map<String, List<DiscountTimeslot>> getWindowSteeringDiscounts(Date deliveryDate)
			throws FDLogisticsServiceException{
		
		try {
			return orderDAO.getWindowSteeringDiscounts(deliveryDate);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public OrdersDTO getOrders(OrderSearchCriteria criteria) throws FDLogisticsServiceException{
		try {
			return orderDAO.getOrders(criteria);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public List<CallLogModel> getDeliveryReq(String orderId) throws FDLogisticsServiceException{
		try {
			return orderDAO.getDeliveryReq(orderId);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}
	
	@Override
	public DeliveryManifest getDeliveryManifest(String orderId) throws FDLogisticsServiceException{
		try {
			return orderDAO.getDeliveryManifest(orderId);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}
	
	@Override
	public List<CallLogModel> getOrderCallLog(String orderId)
			throws FDLogisticsServiceException {
		try {
			return orderDAO.getOrderCallLog(orderId);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}
	
	@Override
	public List<String> getCartonList(String orderId) throws FDLogisticsServiceException {
		try {
			return orderDAO.getCartonList(orderId);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}
	
	@Override
	public Map<String, DeliveryException> getIVRCallLog(
			final Map<String, DeliveryException> result, final Date fromTime,
			final Date toTime) throws FDLogisticsServiceException {
			try {
				return orderDAO.getIVRCallLog(result, fromTime, toTime);
			} catch (Exception e) {
				throw new FDLogisticsServiceException(e);
			}
	}

	@Override
	public void saveCartonInfo(List<CartonInfo> data) throws FDLogisticsServiceException{
		try {
			orderDAO.saveCartonInfo(data);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}
	
	@Override
	public List<CartonInfo> getCartonInfo(String orderId) throws FDLogisticsServiceException {
		try {
			return orderDAO.getCartonInfo(orderId);
		} catch (Exception e) {
			throw new FDLogisticsServiceException(e);
		}
	}
}
