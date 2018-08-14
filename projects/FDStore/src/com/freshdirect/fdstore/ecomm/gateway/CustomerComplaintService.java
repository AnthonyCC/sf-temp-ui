package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerComplaintService extends AbstractEcommService implements CustomerComplaintServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerComplaintService.class);

	private static final String ADD_COMPLAINT = "customerComplaint/addComplaint";
	private static final String APPROVE_COMPLAINT = "customerComplaint/approve";
	private static final String AUTO_APPROVE_CREDIT = "customerComplaint/credit/autoApprove";
	
	private static CustomerComplaintServiceI INSTANCE;

	public static CustomerComplaintServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerComplaintService();

		return INSTANCE;
	}

	@Override
	public String addComplaint(ErpComplaintModel complaint, String saleId, String erpCustomerId, String fdCustomerId,
			boolean autoApproveAuthorized, Double limit)
			throws FDResourceException, ErpComplaintException, RemoteException {
		Response<String> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("complaint", getMapper().convertValue(complaint, JsonNode.class));
			rootNode.put("saleId", saleId);
			rootNode.put("erpCustomerId", erpCustomerId);
			rootNode.put("fdCustomerId", fdCustomerId);
			rootNode.put("autoApproveAuthorized", autoApproveAuthorized);
			rootNode.put("limit", limit);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADD_COMPLAINT),
					new TypeReference<Response<String>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerComplaintService.addComplaint: data=" + inputJson);
				if ("ErpComplaintException".equals(response.getMessage())) {
					throw new ErpComplaintException(response.getError() == null ? null
							: response.getError().get("ErpComplaintException").toString());
				}
				if ("FDResourceException".equals(response.getMessage())) {
					throw new ErpComplaintException(response.getError() == null ? null
							: response.getError().get("FDResourceException").toString());
				}
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerComplaintService.addComplaint: ", e);
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public List<String> autoApproveCredit() throws FDResourceException, ErpComplaintException, RemoteException {
		Response<List<String>> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(AUTO_APPROVE_CREDIT),
					new TypeReference<Response<List<String>>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerComplaintService.autoApproveCredit: data=" + inputJson);
				if ("ErpComplaintException".equals(response.getMessage())) {
					throw new ErpComplaintException(response.getError() == null ? null
							: response.getError().get("ErpComplaintException").toString());
				}
				if ("FDResourceException".equals(response.getMessage())) {
					throw new ErpComplaintException(response.getError() == null ? null
							: response.getError().get("FDResourceException").toString());
				}
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerComplaintService.autoApproveCredit: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void approveComplaint(String complaintId, boolean isApproved, String csrId, boolean sendMail, Double limit)
			throws FDResourceException, ErpComplaintException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("complaintId", complaintId);
			rootNode.put("isApproved", isApproved);
			rootNode.put("csrId", csrId);
			rootNode.put("sendMail", sendMail);
			rootNode.put("limit", limit);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(APPROVE_COMPLAINT),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerComplaintService.approveComplaint: data=" + inputJson);
				if ("ErpComplaintException".equals(response.getMessage())) {
					throw new ErpComplaintException(response.getError() == null ? null
							: response.getError().get("ErpComplaintException").toString());
				}
				if ("FDResourceException".equals(response.getMessage())) {
					throw new ErpComplaintException(response.getError() == null ? null
							: response.getError().get("FDResourceException").toString());
				}
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerComplaintService.approveComplaint: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

}
