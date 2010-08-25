package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IPackagingModel;
import com.freshdirect.routing.model.IServiceTimeModel;
import com.freshdirect.routing.model.IUnassignedModel;

public interface IDeliveryDetailsDAO {
	
	IDeliveryModel getDeliveryInfo(String saleId) throws SQLException;
		
	String getDeliveryType(String zoneCode) throws SQLException;
	
	String getDeliveryZoneType(String zoneCode) throws SQLException;
	
	Map getDeliveryZoneDetails() throws SQLException;
	
	List getLateDeliveryOrders(String query) throws SQLException;
	
	Map<String, List<IDeliverySlot>> getTimeslotsByDate(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException;
	
	Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException;
	
	List<IUnassignedModel> getUnassigned(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException;
	
	IPackagingModel getHistoricOrderSize(final String customerId, final int range) throws SQLException;
	
	IOrderModel getRoutingOrderByReservation(final String reservationId) throws SQLException;
	
	int updateRoutingOrderByReservation(final String reservationId, final double orderSize, final double serviceTime) throws SQLException;
	
	int updateTimeslotForStatus(final String timeslotId, final boolean isClosed) throws SQLException;
	
	int updateTimeslotForStatusByZone(final Date baseDate, final String zoneCode,final String cutOff, final boolean isClosed) throws SQLException;
	
	int updateTimeslotForStatusByRegion(final Date baseDate, final String regionCode,final String cutOff, final boolean isClosed) throws SQLException;
	
	int updateTimeslotForDynamicStatus(final String timeslotId, final boolean isDynamic) throws SQLException;
	
	int updateTimeslotForDynamicStatusByZone(final Date baseDate, final String zoneCode,final String cutOff, final boolean isDynamic) throws SQLException;
	
	int updateTimeslotForDynamicStatusByRegion(final Date baseDate, final String regionCode,final String cutOff, final boolean isDynamic) throws SQLException;
	
	List<IOrderModel> getRoutingOrderByDate(final Date deliveryDate, final String zoneCode) throws SQLException;
}
