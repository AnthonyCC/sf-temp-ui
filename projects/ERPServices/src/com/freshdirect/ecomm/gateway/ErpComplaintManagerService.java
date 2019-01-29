package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.complaint.ErpComplaintReasonData;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.ModelConverter;

public class ErpComplaintManagerService extends AbstractEcommService implements ErpComplaintManagerServiceI {

	private static final Category LOGGER = LoggerFactory
			.getInstance(ErpComplaintManagerService.class);
	
	private static ErpComplaintManagerService INSTANCE;
	
	
	public static ErpComplaintManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ErpComplaintManagerService();

		return INSTANCE;
	}


	private static final String GET_COMPLAINT_REASONS = "complaint/reason/excludeCartonReq/";
	private static final String GET_COMPLAINT_CODES = "complaint/code";
	private static final String REJECT_MAKE_GOOD_COMPLAINT = "complaint/reject/makeGoodSaleId/";
	private static final String GET_PENDING_COMPLAINTS = "complaint/pending";

	@Override
	public Map<String, List<ErpComplaintReason>> getReasons(
			boolean excludeCartonReq) throws RemoteException {
		Response<Map<String, List<ErpComplaintReasonData>>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_COMPLAINT_REASONS
							+ excludeCartonReq),
					new TypeReference<Response<Map<String, List<ErpComplaintReasonData>>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			LOGGER.error("Error occured, excludeCartonReq=" + excludeCartonReq, e);
		}
		return ModelConverter.buildErpComplaintReason(response.getData());
	}

	@Override
	public Map<String, String> getComplaintCodes() throws RemoteException {
		Response<Map<String, String>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_COMPLAINT_CODES),
					new TypeReference<Response<Map<String, String>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			LOGGER.error("Error occured, ", e);
		}
		return response.getData();
	}

	@Override
	public Collection<String> getPendingComplaintSaleIds()
			throws RemoteException {
		Response<Collection<String>> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_PENDING_COMPLAINTS),
					new TypeReference<Response<Collection<String>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			LOGGER.error("Error occured, ", e);
		}
		return response.getData();
	}

	@Override
	public void rejectMakegoodComplaint(String makegood_sale_id)
			throws RemoteException {
		Response<Void> response = null;
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(REJECT_MAKE_GOOD_COMPLAINT
							+ makegood_sale_id),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				if (response.getError() != null && response.getError().get("FDResourceException") != null) {
					throw new FDResourceException(response.getError().get("FDResourceException").toString());
				} else {
					throw new FDResourceException(response.getMessage());
				}

		} catch (FDResourceException e) {
			LOGGER.error("Error occured, makegood_sale_id=" + makegood_sale_id, e);
			throw new RemoteException(e.getMessage());
		}
	}

}
