package com.freshdirect.fdstore.ecomm.gateway;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.DefaultPaymentMethodData;
import com.freshdirect.ecommerce.data.customer.ProfileData;
import com.freshdirect.ecommerce.data.ecoupon.CrmAgentModelData;
import com.freshdirect.ecommerce.data.list.FDActionInfoData;
import com.freshdirect.ecommerce.data.list.RenameCustomerListData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.ecomm.converter.ListConverter;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;

public class RegistrationService extends AbstractEcommService implements RegistrationServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(RegistrationService.class);

	private static final String REGISTER = "/registration/register";
	

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
			throws FDResourceException, ErpDuplicateUserIdException, RemoteException {
		Response<RegistrationResult> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("erpCustomer", getMapper().convertValue(erpCustomer, JsonNode.class));
			rootNode.put("fdCustomer", getMapper().convertValue(fdCustomer, JsonNode.class));
			rootNode.put("cookie", getMapper().convertValue(cookie, JsonNode.class));
			rootNode.put("pickupOnly", getMapper().convertValue(pickupOnly, JsonNode.class));
			rootNode.put("eligibleForPromotion", getMapper().convertValue(eligibleForPromotion, JsonNode.class));
			rootNode.put("survey", getMapper().convertValue(survey, JsonNode.class));
			rootNode.put("serviceType", getMapper().convertValue(serviceType, JsonNode.class));
			rootNode.put("isGiftCardBuyer", getMapper().convertValue(isGiftCardBuyer, JsonNode.class));
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(REGISTER), new TypeReference<Response<RegistrationResult>>() {});
			if (response.getError().containsKey("ErpDuplicateUserIdException")) {
				throw new ErpDuplicateUserIdException(response.getError().get("ErpDuplicateUserIdException").toString());
			}
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

}
