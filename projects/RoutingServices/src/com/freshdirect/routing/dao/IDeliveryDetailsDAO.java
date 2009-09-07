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

public interface IDeliveryDetailsDAO {
	
	IDeliveryModel getDeliveryInfo(String saleId) throws SQLException;
	
	IServiceTimeModel getServiceTime(String serviceTimeType, String zoneType) throws SQLException;
	
	String getDeliveryType(String zoneCode) throws SQLException;
	
	String getDeliveryZoneType(String zoneCode) throws SQLException;
	
	Map getDeliveryZoneDetails() throws SQLException;
	
	List getLateDeliveryOrders(String query) throws SQLException;
	
	Map<String, List<IDeliverySlot>> getTimeslotsByDate(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException;
	
	Map<String, List<IDeliveryWindowMetrics>> getTimeslotsByDateEx(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException;
	
	List<IOrderModel> getUnassigned(final Date deliveryDate, final Date cutOffTime, final String zoneCode) throws SQLException;
	
	IPackagingModel getHistoricOrderSize(final String customerId, final int range) throws SQLException;
}
