package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.FDUserData;
import com.freshdirect.ecommerce.data.product.ProductRequestData;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.ecomm.model.RecognizedUserData;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumReservationType;

public class CustomerInfoService extends AbstractEcommService implements CustomerInfoServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerInfoService.class);

	private static final String GET_CUSTOMER_ID = "customerInfo/getCustomerId";
	private static final String GET_CUSTOMER_INFO = "customerInfo/getCustomerInfo";
	private static final String GET_SO_CUSTOMER_INFO = "customerInfo/getSOCustomerInfo";
	private static final String UPDATE_USER_ID = "customerInfo/updateUserId";
	private static final String STORE_USER = "customerInfo/storeUser";
	private static final String STORE_COHORT_NAME = "customerInfo/storeCohortName";
	private static final String IS_PASSWORD_REQUEST_EXPIRED = "customerInfo/isPasswordRequestExpired";
	private static final String CHANGE_PASSWORD = "customerInfo/changePassword";
	private static final String SET_PROFILE_ATTRIBUTE = "customerInfo/setProfileAttribute";
	private static final String CREATE_CASE = "customerInfo/createCase";
	private static final String GET_CREDIT_HISTORY = "customerInfo/getCreditHistory";
	private static final String MAKE_RESERVATION = "customerInfo/makeReservation";
	private static final String CANCEL_RESERVATION = "customerInfo/cancelReservation";
	private static final String STORE_REQUEST = "customerInfo/storeProductRequest";
	private static final String GET_PRODUCT_REQUEST_DATA = "customerInfo/productRequestFetchAll";
	private static final String GET_NEXT_ID = "customerInfo/getNextId";
	private static final String IS_ECHECK_RESTRICTED = "customerInfo/isECheckRestricted";
	private static final String GET_CUSTOMER_PAYMENT_AND_CREDIT = "customerInfo/getCustomerPaymentAndCredit";
	
	
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
			LOGGER.error("Error in CustomerInfoService: ", e);
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
			LOGGER.error("Error in CustomerInfoService: ", e);
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
			LOGGER.error("Error in CustomerInfoService: ", e);
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
			LOGGER.error("Error in CustomerInfoService: ", e);
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
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void storeCohortName(String userId, String cohortName) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("userId", userId);
			rootNode.put("cohortName", cohortName);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_COHORT_NAME),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean isPasswordRequestExpired(String emailAddress, String passReq)
			throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("emailAddress", emailAddress);
			rootNode.put("passReq", passReq);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(IS_PASSWORD_REQUEST_EXPIRED),
					new TypeReference<Response<Boolean>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void changePassword(FDActionInfo info, String emailAddress, String password)
			throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.put("emailAddress", emailAddress);
			rootNode.put("password", password);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CHANGE_PASSWORD),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void setProfileAttribute(FDIdentity identity, String key, String value, FDActionInfo info)
			throws RemoteException, FDResourceException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.put("key", key);
			rootNode.put("value", value);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SET_PROFILE_ATTRIBUTE),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void createCase(CrmSystemCaseInfo caseInfo) throws RemoteException, FDResourceException {
		Response<Void> response = null;
		try {
			Request<CrmSystemCaseInfo> request = new Request<CrmSystemCaseInfo>();
			request.setData(caseInfo);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CREATE_CASE),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public FDCustomerCreditHistoryModel getCreditHistory(FDIdentity identity)
			throws FDResourceException, RemoteException {
		Response<List<FDCustomerCreditModel>> response = null;

		response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_CREDIT_HISTORY + "/" + identity.getErpCustomerPK()),
				new TypeReference<Response<List<FDCustomerCreditModel>>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		FDCustomerCreditHistoryModel creditHistory = new FDCustomerCreditHistoryModel(identity, response.getData());
		return creditHistory;
	}
	
	@Override
	public FDReservation makeReservation(FDUserI user, String timeslotId, EnumReservationType rsvType, String addressId,
			FDActionInfo aInfo, boolean chefsTable, TimeslotEvent event, boolean isForced)
			throws FDResourceException, ReservationException, RemoteException {
		Response<FDReservation> response = null;
		try {
			int settledOrderCount = (user != null && user.getOrderHistory() != null) ? user.getOrderHistory().getSettledOrderCount()
					: 0;
			ProfileModel profile = user!=null && user.getIdentity()!=null && user.getFDCustomer()!=null && 
					user.getFDCustomer().getProfile()!=null? user.getFDCustomer().getProfile(): null;
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("info", getMapper().convertValue(aInfo, JsonNode.class));
			rootNode.set("identity", getMapper().convertValue(user.getIdentity(), JsonNode.class));
			rootNode.set("event", getMapper().convertValue(event, JsonNode.class));
			rootNode.set("profile", getMapper().convertValue(profile, JsonNode.class));
			
			rootNode.put("addressId", addressId);
			rootNode.put("timeslotId", timeslotId);
			rootNode.put("reservationTypeCode", rsvType.getName());
			
			rootNode.put("chefsTable", chefsTable);
			rootNode.put("isForced", isForced);
			
			rootNode.put("settledOrderCount", settledOrderCount);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(MAKE_RESERVATION),
					new TypeReference<Response<FDReservation>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("ReservationException".equals(response.getMessage())) {
					throw new ReservationException(
							response.getError().get("ReservationException").toString());
				}
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void cancelReservation(FDIdentity identity, FDReservation reservation,
			FDActionInfo actionInfo, TimeslotEvent event) throws FDResourceException, RemoteException {
		Response<Response<Void>> response = null;
		try {
			
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("reservation", getMapper().convertValue(reservation, JsonNode.class));
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.set("actionInfo", getMapper().convertValue(actionInfo, JsonNode.class));
			rootNode.set("event", getMapper().convertValue(event, JsonNode.class));
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CANCEL_RESERVATION),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void storeProductRequest(List<FDProductRequest> productRequest) throws FDResourceException, RemoteException {
		Response<Void> response = null;
		try {
			Request<List<FDProductRequest>> request = new Request<List<FDProductRequest>>();
			
			request.setData(productRequest);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_REQUEST),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerInfoService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}
	

	@Override
	public ProductRequestData productRequestFetchAll() throws FDResourceException, RemoteException {
		Response<ProductRequestData> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_PRODUCT_REQUEST_DATA),
				new TypeReference<Response<ProductRequestData>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean isECheckRestricted(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<Boolean> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(IS_ECHECK_RESTRICTED + "/" + identity.getErpCustomerPK()),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public String getNextId(String schema, String sequence) throws FDResourceException, RemoteException {
		Response<String> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_NEXT_ID + "/" + schema +"/" + sequence),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public ErpCustomerModel getCustomerPaymentAndCredit(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<ErpCustomerModel> response = this.httpGetDataTypeMap(
				getFdCommerceEndPoint(GET_CUSTOMER_PAYMENT_AND_CREDIT + "/" + identity.getErpCustomerPK()),
				new TypeReference<Response<ErpCustomerModel>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}
	
	private RecognizedUserData fdUserToRecognizedUserData(FDUser user) {
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
		if (user.getZPServiceType() != null) {
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

	private static List<ErpOrderLineModel> convertToErpOrderlines(List<FDCartLineI> cartlines)
			throws FDResourceException {

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
