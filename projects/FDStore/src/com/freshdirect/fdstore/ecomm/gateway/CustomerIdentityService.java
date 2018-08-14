package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.FDUserData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.ejb.FDUserDAO;
import com.freshdirect.fdstore.ecomm.model.RecognizedUserData;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerIdentityService extends AbstractEcommService implements CustomerIdentityServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(RegistrationService.class);

	private static final String LOGIN = "customerIdentity/login";

	private static final String RECOGNIZE = "customerIdentity/recognize";

	private static CustomerIdentityServiceI INSTANCE;

	public static CustomerIdentityServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerIdentityService();

		return INSTANCE;
	}

	@Override
	public FDIdentity login(String userId, String password)
			throws FDAuthenticationException, FDResourceException, RemoteException {
		Response<FDIdentity> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("userId", userId);
			rootNode.put("password", password);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOGIN),
					new TypeReference<Response<FDIdentity>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("FDAuthenticationException".equals(response.getMessage())) {
					throw new FDAuthenticationException(
							response.getError().get("FDAuthenticationException").toString());
				} else if ("FDResourceException".equals(response.getMessage())) {
					throw new FDResourceException(response.getError().get("FDResourceException").toString());
				}
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerIdentityService: userId=" + userId, e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in CustomerIdentityService: userId=" + userId, e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public FDUser recognize(String cookie, EnumEStoreId eStoreId)
			throws FDAuthenticationException, FDResourceException, RemoteException {
		Response<RecognizedUserData> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();

			rootNode.put("cookie", cookie);
			rootNode.set("eStoreId", getMapper().convertValue(eStoreId, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(RECOGNIZE),
					new TypeReference<Response<RecognizedUserData>>() {
					});
			RecognizedUserData recognizedUserData = response.getData();
			if (!response.getResponseCode().equals("OK") || recognizedUserData == null) {
				if ("FDAuthenticationException".equals(response.getMessage())) {
					throw new FDAuthenticationException("Unrecognized user");
				}
				throw new FDResourceException(response.getMessage());
			}
			FDUser user = loadFromRecognizedUserData(recognizedUserData, false, true);
			return user;
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerIdentityService: cookie=" + cookie, e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in CustomerIdentityService: cookie=" + cookie, e);
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public FDUser recognize(FDIdentity identity)
			throws FDAuthenticationException, FDResourceException, RemoteException {
		return recognize(identity, null, false, true);
	}
	
	@Override
	public FDUser recognize(FDIdentity identity, EnumEStoreId eStoreId, boolean lazy, boolean populateDeliveryPlantInfo)
			throws FDAuthenticationException, FDResourceException, RemoteException {
		Response<RecognizedUserData> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();

			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.set("eStoreId", getMapper().convertValue(eStoreId, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(RECOGNIZE),
					new TypeReference<Response<RecognizedUserData>>() {
					});
			RecognizedUserData recognizedUserData = response.getData();
			if (!response.getResponseCode().equals("OK") || recognizedUserData == null) {
				if ("FDAuthenticationException".equals(response.getMessage())) {
					throw new FDAuthenticationException("Unrecognized user");
				}
				throw new FDResourceException(response.getMessage());
			}
			FDUser user = loadFromRecognizedUserData(recognizedUserData, lazy, populateDeliveryPlantInfo);
			return user;
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerIdentityService: identity=" + identity, e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in CustomerIdentityService: identity=" + identity, e);
			throw new RemoteException(e.getMessage());
		}
	}

	private FDUser loadFromFDUserData(FDUserData data, boolean populateUserContext) {
		FDUser user = null;
		if (data != null) {
			PrimaryKey pk = new PrimaryKey(data.getPK());
			user = new FDUser(pk);
			user.setCookie(data.getCookie());
			user.setZipCode(data.getZipCode(), populateUserContext);
			// setAddressbyZipCode logic 
			if (user.getAddress() != null) {
	        	user.getAddress().setCity(data.getCity());
	        	user.getAddress().setState(data.getState());
	        	user.setEbtAccepted(data.isEbtAccepted());
	        }
			user.setDepotCode(data.getDepotCode());
			user.setSelectedServiceType(EnumServiceType.getEnum(data.getSelectedServiceType()));
			user.setLastRefTrackingCode(data.getLastRefTrackingCode());
			user.setLastRefProgramId(data.getLastRefProgramId());
			user.setLastRefProgInvtId(data.getLastRefProgInvtId());
			user.setLastRefTrkDtls(data.getLastRefTrkDtls());
			// for new COS customer
			user.setUserServiceType(EnumServiceType.getEnum(data.getUserServiceType()));
			// for zone pricing
			user.setZPServiceType(EnumServiceType.getEnum(data.getZPServiceType()));

			AddressModel addr = user.getAddress();
			if (addr != null) {
				addr.setAddress1(data.getAddress1());
				addr.setApartment(data.getApartment());
			}

			String fdcustId = data.getFdCustomerId();
			String erpcustId = data.getErpCustomerId();
			if ((fdcustId != null) && (erpcustId != null)) {
				user.setIdentity(new FDIdentity(erpcustId, fdcustId));
			}
			user.setCustSapId(data.getSapId());
			user.setUserId(data.getUserId());
			user.setActive(data.isActive());
			user.setReceiveFDEmails(data.isReceiveFDEmails());
			user.setHomePageLetterVisited(data.isHomePageLetterVisited());
			user.setCampaignMsgViewed(data.getCampaignMsgViewed());
			user.setGlobalNavTutorialSeen(data.isGlobalNavTutorialSeen());

			// Smart Store - Cohort ID
			user.setCohortName(data.getCohortName());

			// APPDEV-1888 referral info
			user.setReferralCustomerId(data.getReferralCustomerId());

			user.setDefaultListId(data.getDefaultListId());
			// user.setTcAcknowledge(NVL.apply(rs.getString("fd_tc_agree"),
			// "").equalsIgnoreCase("X")?true:false);
			user.setRafClickId(data.getRafClickId());
			user.setRafPromoCode(data.getRafPromoCode());
			user.setZipCheckPopupUsed(data.isZipCheckPopupUsed());
		} else {
			user = new FDUser();
		}

		return user;
	}

	private FDUser loadFromRecognizedUserData(RecognizedUserData recognizedUserData, boolean lazy,
			boolean populateDeliveryPlantInfo) throws FDAuthenticationException {
		FDUser user = loadFromFDUserData(recognizedUserData.getFdUserData(), populateDeliveryPlantInfo);
		if (!user.isAnonymous()) {
			FDUserDAO.loadCart(user, recognizedUserData.getOrderLines(), lazy);
			FDRecipientList repList = new FDRecipientList(recognizedUserData.getReceipts());
			user.setRecipientList(repList);
			user.setExternalPromoCampaigns(recognizedUserData.getExternalCampaign());

		} else {
			throw new FDAuthenticationException("Unrecognized user");
		}
		// Load Promo Audience Details for this customer.
		user.setAssignedCustomerParams(recognizedUserData.getAsssignedCustomerParam());

		user.setDlvPassInfo(recognizedUserData.getDlvPassInfo());
		return user;
	}
}
