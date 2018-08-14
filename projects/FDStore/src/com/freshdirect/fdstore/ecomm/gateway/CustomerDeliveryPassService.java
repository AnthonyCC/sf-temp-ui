package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerDeliveryPassService extends AbstractEcommService implements CustomerDeliveryPassServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerDeliveryPassService.class);

	private static final String GET_INFO = "customerDeliveryPass/getInfo";

	private static final String HAS_AUTO_RENEW_DP = "customerDeliveryPass/hasAutoRenewDP";
	private static final String UPDATE_FREE_TRIAL_OPT_IN = "customerDeliveryPass/freeTrialOptin/update";
	private static final String GET_FREE_TRIAL_OPT_IN_DATE = "customerDeliveryPass/freeTrialOptin/getDate/";
	private static final String UPDATE_DETAILS = "customerDeliveryPass/details/update";

	private static CustomerDeliveryPassServiceI INSTANCE;

	public static CustomerDeliveryPassServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerDeliveryPassService();

		return INSTANCE;
	}

	@Override
	public FDUserDlvPassInfo getDeliveryPassInfo(FDIdentity identity, EnumEStoreId estore)
			throws FDResourceException, RemoteException {
		Response<FDUserDlvPassInfo> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.set("estore", getMapper().convertValue(estore, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_INFO),
					new TypeReference<Response<FDUserDlvPassInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerDeliveryPassService.getDeliveryPassInfo: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerDeliveryPassService.getDeliveryPassInfo: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String hasAutoRenewDP(String customerPK) throws FDResourceException, RemoteException {
		Response<String> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(HAS_AUTO_RENEW_DP + "/" + customerPK),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}

	@Override
	public void setDpFreeTrialOptin(boolean dpFreeTrialOptin, String custId, FDActionInfo info)
			throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("custId", custId);
			rootNode.put("dpFreeTrialOptin", dpFreeTrialOptin);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_FREE_TRIAL_OPT_IN),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerDeliveryPassService.setDpFreeTrialOptin: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerDeliveryPassService.setDpFreeTrialOptin: ", e);
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public Date getDpFreeTrialOptinDate(String custId) throws FDResourceException, RemoteException {
		Response<Long> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_FREE_TRIAL_OPT_IN_DATE + custId),
				new TypeReference<Response<Long>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		Long optInDate = response.getData();
		return optInDate == null ? null : new Date(optInDate);
	}

	@Override
	public void updateDpOptinDetails(boolean isAutoRenewDp, String custId, String dpType, FDActionInfo info,
			EnumEStoreId eStore) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.set("eStore", getMapper().convertValue(eStore, JsonNode.class));
			rootNode.put("isAutoRenewDp", isAutoRenewDp);
			rootNode.put("custId", custId);
			rootNode.put("dpType", dpType);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_DETAILS),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerDeliveryPassService.updateDpOptinDetails: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerDeliveryPassService.updateDpOptinDetails: custId" + custId
					+ ", isAutoRenewDp=" + isAutoRenewDp + ", dpType=" + dpType, e);
			throw new RemoteException(e.getMessage());
		}
	}

}
