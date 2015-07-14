package com.freshdirect.fdlogistics.dao;

import java.sql.SQLException;
import java.util.List;

import com.freshdirect.logistics.delivery.dto.Addresses;
import com.freshdirect.logistics.delivery.dto.Customers;

public interface ICustomerDAO {
	
	public Customers getCustomerDetails(List<String> ids)
			throws SQLException;

	public Customers getCustomerAddressesByBuildingLocation(
			String scrubbedAddress, String apartment, String zipcode)
			throws SQLException;

	public Addresses getAddressDetails(List<String> ids) throws SQLException;
}