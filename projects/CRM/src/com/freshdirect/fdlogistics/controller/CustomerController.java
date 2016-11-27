package com.freshdirect.fdlogistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.ICustomerService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.AddressSearchRequest;
import com.freshdirect.logistics.controller.data.request.CustomerSearchRequest;
import com.freshdirect.logistics.controller.data.request.ScrubbedAddressRequest;
import com.freshdirect.logistics.controller.data.response.ChefsTableResponse;
import com.freshdirect.logistics.delivery.dto.Addresses;
import com.freshdirect.logistics.delivery.dto.Customers;
import com.freshdirect.logistics.delivery.model.ActionError;
import com.freshdirect.logistics.delivery.model.SystemMessageList;

@Controller
@RequestMapping("/v/{version}/{companycode}/customer")
public class CustomerController extends BaseController {

	@Autowired
	private ICustomerService customerService;
	
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public @ResponseBody
	Customers getCustomerDetails(@RequestBody CustomerSearchRequest request) {
		Customers customers = new Customers();
		try {
			customers = customerService.getCustomerDetails(request.getCustomerIds());
			customers.setSuccessMessage("customers retrieved successfully");
			return customers;
		} catch (FDLogisticsServiceException e) {
			customers.setStatus(Result.STATUS_FAILED);
			customers.addErrorMessages(new ActionError("technical_difficulty",
					SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		return customers; 
	
	}
	
	@RequestMapping(value = "/addresses", method = RequestMethod.POST)
	public @ResponseBody
	Addresses getAddressDetails(@RequestBody AddressSearchRequest request) {
		Addresses addresses = new Addresses();
		try {
			addresses = customerService.getAddressDetails(request.getAddressIds());
			addresses.setSuccessMessage("address retrieved successfully");
			return addresses;
		} catch (FDLogisticsServiceException e) {
			addresses.setStatus(Result.STATUS_FAILED);
			addresses.addErrorMessages(new ActionError("technical_difficulty",
					SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		return addresses; 
	
	}

	
	
	@RequestMapping(value = "/get/address", method = RequestMethod.POST)
	public @ResponseBody
	Customers getCustomerAddressesByBuildingLocation(@RequestBody ScrubbedAddressRequest request) {
		Customers customers = new Customers();
		try {
			customers = customerService.getCustomerAddressesByBuildingLocation(request.getScrubbedAddress(), request.getApartment(), null);
			customers.setSuccessMessage("customer details retrieved successfully");
			return customers;
		} catch (FDLogisticsServiceException e) {
			customers.setStatus(Result.STATUS_FAILED);
			customers.addErrorMessages(new ActionError("technical_difficulty",
					SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		
		return customers;
	}
	
	@RequestMapping(value="/chefsTable/{customerId}", method = RequestMethod.POST)
	public @ResponseBody ChefsTableResponse isChefsTable(@PathVariable("customerId") String customerId) {
			
			ChefsTableResponse response = new ChefsTableResponse();
		
			try {
				FDCustomerInfo customerInfo = FDCustomerManager.getCustomerInfo(
						new FDIdentity(customerId, FDCustomerFactory.getFDCustomerIdFromErpId(customerId)));
				response.setStatus(Result.STATUS_SUCCESS);
				response.setChefsTable(customerInfo.isChefsTable());
			} catch (FDResourceException e) {
				response.setStatus(Result.STATUS_FAILED);
			}
		
		return response;
	}
}
