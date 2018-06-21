package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.CustomerCreditModel;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerReportService extends AbstractEcommService implements CustomerReportServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerReportService.class);

	private static final String GENERATE_LATE_DELIVERY_CREDIT_REPORT = "customerReport/generateLateDeliveryCreditReport";

	private static CustomerReportServiceI INSTANCE;

	public static CustomerReportServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerReportService();

		return INSTANCE;
	}

	@Override
	public void generateLateDeliveryCreditReport()
	throws FDResourceException, RemoteException {
		Response<Void> response = null;

		response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GENERATE_LATE_DELIVERY_CREDIT_REPORT),
				new TypeReference<Response<Void>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}


	}

	
}
