package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.FDUserData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class RegistrationService extends AbstractEcommService implements RegistrationServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(RegistrationService.class);

	private static final String REGISTER = "registration/register";

	private static final String CREATENEWUSER = "registration/createNewUser";
	
	private static RegistrationService INSTANCE;

	public static RegistrationServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new RegistrationService();

		return INSTANCE;
	}

	@Override
	public RegistrationResult register(FDActionInfo info, ErpCustomerModel erpCustomer, FDCustomerModel fdCustomer,
			String cookie, boolean pickupOnly, boolean eligibleForPromotion, FDSurveyResponse survey,
			EnumServiceType serviceType, boolean isGiftCardBuyer)
			throws FDResourceException, ErpDuplicateUserIdException, ErpFraudException, RemoteException {
		Response<RegistrationResult> response = null;
		String inputJson = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.set("erpCustomer", getMapper().convertValue(erpCustomer, JsonNode.class));
			rootNode.set("fdCustomer", getMapper().convertValue(fdCustomer, JsonNode.class));
			rootNode.set("cookie", getMapper().convertValue(cookie, JsonNode.class));
			rootNode.set("pickupOnly", getMapper().convertValue(pickupOnly, JsonNode.class));
			rootNode.set("eligibleForPromotion", getMapper().convertValue(eligibleForPromotion, JsonNode.class));
			rootNode.set("survey", getMapper().convertValue(survey, JsonNode.class));
			rootNode.set("serviceType", getMapper().convertValue(serviceType, JsonNode.class));
			rootNode.set("isGiftCardBuyer", getMapper().convertValue(isGiftCardBuyer, JsonNode.class));

			request.setData(rootNode);
			inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(REGISTER),
					new TypeReference<Response<RegistrationResult>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("ErpDuplicateUserIdException".equals(response.getMessage())) {
					throw new ErpDuplicateUserIdException(erpCustomer!=null?erpCustomer.getUserId():"");
				}
				if ("ErpFraudException".equals(response.getMessage())) {
					throw new ErpFraudException(EnumFraudReason.getEnum(
							response.getError() != null ? response.getError().get("ErpFraudException").toString()
									: null));
				}
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in RegistrationService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public FDUser createNewUser(String zipCode, EnumServiceType serviceType, EnumEStoreId eStoreId)
			throws FDResourceException, RemoteException {
		Response<FDUserData> response = null;
		String inputJson = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("zipCode", zipCode);
			rootNode.set("serviceType", getMapper().convertValue(serviceType, JsonNode.class));
			rootNode.set("eStoreId", getMapper().convertValue(eStoreId, JsonNode.class));;

			request.setData(rootNode);
			inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CREATENEWUSER),
					new TypeReference<Response<FDUserData>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			FDUserData userData = response.getData();
			FDUser user = new FDUser(new PrimaryKey(userData.getPK()));
	        user.setCookie(userData.getCookie());
	        user.setZipCode(userData.getZipCode());
	        
	        if (user.getAddress() != null) {
	        	user.getAddress().setCity(userData.getCity());
	        	user.getAddress().setState(userData.getState());
	        	user.setEbtAccepted(userData.isEbtAccepted());
	        }
	        
			return user;
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in RegistrationService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in RegistrationService: data=" + (inputJson == null ? "" : inputJson), e);
			throw new RemoteException(e.getMessage());
		}
	}
}
