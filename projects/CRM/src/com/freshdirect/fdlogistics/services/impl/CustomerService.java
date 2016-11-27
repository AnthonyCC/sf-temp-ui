package com.freshdirect.fdlogistics.services.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freshdirect.fdlogistics.dao.ICustomerDAO;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.ICustomerService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.dto.Addresses;
import com.freshdirect.logistics.delivery.dto.Customers;

@Component
public class CustomerService implements ICustomerService {

	private final static Category LOGGER = LoggerFactory
			.getInstance(CustomerService.class);

	@Autowired
	private ICustomerDAO customerDAO;
	
	public ICustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	@Override
	public Customers getCustomerDetails(List<String> ids)
			throws FDLogisticsServiceException {
		try {
			return customerDAO.getCustomerDetails(ids);
		} catch (SQLException e) {
			throw new FDLogisticsServiceException(e);
		}
	}
	
	@Override
	public Addresses getAddressDetails(List<String> ids)
			throws FDLogisticsServiceException {
		try {
			return customerDAO.getAddressDetails(ids);
		} catch (SQLException e) {
			throw new FDLogisticsServiceException(e);
		}
	}

	@Override
	public Customers getCustomerAddressesByBuildingLocation(
			String scrubbedAddress, String apartment, String zipcode)
			throws FDLogisticsServiceException {

		try {
			return customerDAO.getCustomerAddressesByBuildingLocation(scrubbedAddress, apartment, zipcode);
		} catch (SQLException e) {
			throw new FDLogisticsServiceException(e);
		}
	
	}

	
}
