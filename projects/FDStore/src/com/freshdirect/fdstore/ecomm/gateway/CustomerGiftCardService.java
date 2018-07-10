package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.CardInUseException;
import com.freshdirect.giftcard.CardOnHoldException;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ServiceUnavailableException;

public class CustomerGiftCardService extends AbstractEcommService implements CustomerGiftCardServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerGiftCardService.class);

	private static final String GET_RECIPIENT_DLV_INFO = "customerGiftCard/getRecipientDlvInfo";
	private static final String RESEND_EMAIL = "customerGiftCard/resendEmail";
	private static final String APPLY_GIFT_CARD = "customerGiftCard/apply";
	private static final String GET_GIFT_CARDS = "customerGiftCard/getGiftCards";
	private static final String RESUBMIT_ORDERS = "customerGiftCard/resubmitOrders";
	private static final String GET_OUTSTANDING_BALANCE = "customerGiftCard/getOutstandingBalance";
	private static final String SAVE_DONATION_OPT_IN = "customerGiftCard/saveDonationOptIn";

	private static CustomerGiftCardServiceI INSTANCE;

	public static CustomerGiftCardServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerGiftCardService();

		return INSTANCE;
	}

	@Override
	public ErpGCDlvInformationHolder getRecipientDlvInfo(FDIdentity identity, String saleId, String certificationNum)
			throws FDResourceException, RemoteException {
		Response<ErpGCDlvInformationHolder> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.put("saleId", saleId);
			rootNode.put("certificationNum", certificationNum);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_RECIPIENT_DLV_INFO),
					new TypeReference<Response<ErpGCDlvInformationHolder>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerGiftCardService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean resendEmail(String saleId, String certificationNum, String resendEmailId, String recipName,
			String personMsg, EnumTransactionSource source) throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();

			rootNode.put("saleId", saleId);
			rootNode.put("certificationNum", certificationNum);
			rootNode.put("resendEmailId", resendEmailId);
			rootNode.put("recipName", recipName);
			rootNode.put("personMsg", personMsg);
			rootNode.set("source", getMapper().convertValue(source, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(RESEND_EMAIL),
					new TypeReference<Response<Boolean>>() {
					});

			if (!response.getResponseCode().equals("OK")) {

				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerGiftCardService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public ErpGiftCardModel applyGiftCard(FDIdentity identity, String givexNum, FDActionInfo info)
			throws ServiceUnavailableException, InvalidCardException, CardInUseException, CardOnHoldException,
			FDResourceException, RemoteException {
		Response<ErpGiftCardModel> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();

			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.put("givexNum", givexNum);
			rootNode.set("info", getMapper().convertValue(info, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(APPLY_GIFT_CARD),
					new TypeReference<Response<ErpGiftCardModel>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("InvalidCardException".equals(response.getMessage())) {
					throw new InvalidCardException();
				}
				if ("ServiceUnavailableException".equals(response.getMessage())) {
					throw new ServiceUnavailableException();
				}
				if ("CardOnHoldException".equals(response.getMessage())) {
					throw new CardOnHoldException();
				}
				if ("CardInUseException".equals(response.getMessage())) {
					CardInUseException cie = new CardInUseException();
					cie.setCardOwner(response.getError().get("cardOwner").toString());
					throw cie;
				}

				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerGiftCardService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<ErpGiftCardModel> getGiftCards(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<List<ErpGiftCardModel>> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_GIFT_CARDS),
					new TypeReference<Response<List<ErpGiftCardModel>>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerGiftCardService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void resubmitGCOrders() throws RemoteException, FDResourceException {
		Response<Void> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(RESUBMIT_ORDERS),
				new TypeReference<Response<Void>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
	}

	@Override
	public double getOutstandingBalance(ErpAbstractOrderModel order, boolean isModifyOrderModel) throws FDResourceException, RemoteException {
		Response<Double> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("order",  getMapper().convertValue(order, JsonNode.class));
			rootNode.put("isModifyOrderModel", isModifyOrderModel);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_OUTSTANDING_BALANCE),
					new TypeReference<Response<Double>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerGiftCardService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void saveDonationOptIn(String custId, String saleId, boolean optIn)
			throws RemoteException, FDResourceException {
		Response<Void> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("custId", custId);
			rootNode.put("saleId", saleId);
			rootNode.put("optIn", optIn);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SAVE_DONATION_OPT_IN),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerGiftCardService: ", e);
			throw new RemoteException(e.getMessage());
		}

	}

}
