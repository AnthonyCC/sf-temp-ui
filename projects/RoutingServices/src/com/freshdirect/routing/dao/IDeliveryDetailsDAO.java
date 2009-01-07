package com.freshdirect.routing.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.IServiceTimeModel;

public interface IDeliveryDetailsDAO {
	
	IDeliveryModel getDeliveryInfo(String saleId) throws SQLException;
	
	IServiceTimeModel getServiceTime(String serviceTimeType, String zoneType) throws SQLException;
	
	String getDeliveryType(String zoneCode) throws SQLException;
	
	String getDeliveryZoneType(String zoneCode) throws SQLException;
	
	Map getDeliveryZoneDetails() throws SQLException;
	
	List getLateDeliveryOrders(String query) throws SQLException;
}
