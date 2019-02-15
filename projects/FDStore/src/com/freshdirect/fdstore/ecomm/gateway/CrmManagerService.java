package com.freshdirect.fdstore.ecomm.gateway;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CrmManagerService extends AbstractEcommService implements CrmManagerServiceI {

	private static final Category LOGGER = LoggerFactory.getInstance(CrmManagerService.class);

	
	private static final String LOGIN_AGENT = "crmManager/getAgent";

	private static CrmManagerServiceI INSTANCE;
	
	public static CrmManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrmManagerService();

		return INSTANCE;
	}
	@Override
	public CrmAgentModel loginAgent(String username, String password)
			throws CrmAuthenticationException, FDResourceException {
		Response<CrmAgentModel> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("userId", username);
			rootNode.put("password", password);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOGIN_AGENT),
					new TypeReference<Response<CrmAgentModel>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				LOGGER.error("Error in loginAgent: inputJson=" + inputJson
						+ ", response=" + response);
				if ("CrmAuthenticationException".equals(response.getMessage())) {
					throw new CrmAuthenticationException(response.getError() != null
							? response.getError().get("CrmAuthenticationException").toString()
							: null);
				}
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in loginAgent: username=" + username + ", password=" + password);
			throw new FDResourceException(e.getMessage());
		}
	}
}
