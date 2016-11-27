package com.freshdirect.fdlogistics.services;

import java.util.List;

import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.logistics.delivery.dto.Addresses;
import com.freshdirect.logistics.delivery.dto.Customers;

public interface ICustomerService {

	public Customers getCustomerDetails(List<String> ids) throws FDLogisticsServiceException;
	
	public Customers getCustomerAddressesByBuildingLocation(String scrubbedAddress, String apartment, String zipcode) throws FDLogisticsServiceException;

	public Addresses getAddressDetails(List<String> ids) throws FDLogisticsServiceException;

}