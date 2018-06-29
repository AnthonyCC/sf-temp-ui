package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodDeserializer;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.FDUserData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.fdstore.ecomm.model.RecognizedUserData;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerInfoService extends AbstractEcommService implements CustomerInfoServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerInfoService.class);

	private static final String GET_CUSTOMER_ID = "customerInfo/getCustomerId";
	private static final String GET_CUSTOMER_INFO = "customerInfo/getCustomerInfo";
	private static final String GET_SO_CUSTOMER_INFO = "customerInfo/getSOCustomerInfo";
	private static final String UPDATE_USER_ID = "customerInfo/updateUserId";
	private static final String STORE_USER = "customerInfo/storeUser";

	private static CustomerInfoServiceI INSTANCE;

	public static CustomerInfoServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerInfoService();

		return INSTANCE;
	}

	@Override
	public PrimaryKey getCustomerId(String userId) throws FDResourceException, RemoteException {
		Response<String> response = null;
		try {
			Request<String> request = new Request<String>();
			request.setData(userId);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_CUSTOMER_ID),
					new TypeReference<Response<ErpAddressModel>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
			return new PrimaryKey(response.getData());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public FDCustomerInfo getCustomerInfo(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<FDCustomerInfo> response = null;
		try {
			Request<FDIdentity> request = new Request<FDIdentity>();
			request.setData(identity);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_CUSTOMER_INFO),
					new TypeReference<Response<FDCustomerInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public FDCustomerInfo getSOCustomerInfo(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<FDCustomerInfo> response = null;
		try {
			Request<FDIdentity> request = new Request<FDIdentity>();
			request.setData(identity);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_SO_CUSTOMER_INFO),
					new TypeReference<Response<FDCustomerInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void updateUserId(FDActionInfo info, String userId)
			throws FDResourceException, ErpDuplicateUserIdException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("userId", userId);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_USER_ID),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("ErpDuplicateUserIdException".equals(response.getMessage())) {
					throw new ErpDuplicateUserIdException(
							response.getError().get("ErpDuplicateUserIdException").toString());
				}
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void storeUser(FDUser user) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("user", getMapper().convertValue(fdUserToRecognizedUserData(user), JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_USER),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	public RecognizedUserData fdUserToRecognizedUserData(FDUser user) {
		RecognizedUserData data = new RecognizedUserData();
		FDUserData fdUserData = new FDUserData();
		data.setFdUserData(fdUserData);
		fdUserData.setPK(user.getPK().getId());
		fdUserData.setUserId(user.getId());
		fdUserData.setCookie(user.getCookie());
		fdUserData.setZipCode(user.getZipCode());
		fdUserData.setDepotCode(user.getDepotCode());
		fdUserData.setCampaignMsgViewed(user.getCampaignMsgViewed());
		fdUserData.setCohortName(user.getCohortName());
		fdUserData.setDefaultListId(user.getDefaultListId());
		fdUserData.setEStoreContentId(user.getUserContext().getStoreContext().getEStoreId().getContentId());
		
		fdUserData.setLastRefProgramId(user.getLastRefProgId());
		fdUserData.setLastRefProgInvtId(user.getLastRefProgInvtId());
		fdUserData.setLastRefTrackingCode(user.getLastRefTrackingCode());
		fdUserData.setLastRefTrkDtls(user.getLastRefTrkDtls());
		
		fdUserData.setGlobalNavTutorialSeen(user.isGlobalNavTutorialSeen());
		fdUserData.setHomePageLetterVisited(user.isHomePageLetterVisited());
		fdUserData.setZipCheckPopupUsed(user.isZipCheckPopupUsed());
		
		if (user.getSelectedServiceType() != null)
			fdUserData.setSelectedServiceType(user.getSelectedServiceType().getName());
		if (user.getIdentity() != null) {
			fdUserData.setFdCustomerId(user.getIdentity().getFDCustomerPK());
			fdUserData.setErpCustomerId(user.getIdentity().getErpCustomerPK());
		}
		if (user.getZPServiceType() != null ) {
			fdUserData.setZPServiceType(user.getZPServiceType().getName());
		}
		data.setAddress(user.getAddress());
		data.setRecipientList(user.getRecipientList());
		try {
			data.setOrderLines(convertToErpOrderlines(user.getShoppingCart().getOrderLines()));
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
		return data;
	}
	
	private static List<ErpOrderLineModel> convertToErpOrderlines(List<FDCartLineI> cartlines) throws FDResourceException {

        int num = 0;
        List<ErpOrderLineModel> erpOrderlines = new ArrayList<ErpOrderLineModel>();
        for (FDCartLineI cartline : cartlines) {
            ErpOrderLineModel erpLines;
            try {
                erpLines = cartline.buildErpOrderLines(num);
            } catch (FDInvalidConfigurationException e) {
                LOGGER.warn("Skipping invalid cartline", e);
                continue;
            }
            erpOrderlines.add(erpLines);
            num += 1;
        }

        return erpOrderlines;
    }
}
