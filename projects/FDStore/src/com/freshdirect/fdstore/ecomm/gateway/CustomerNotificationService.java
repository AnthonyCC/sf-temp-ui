package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerNotificationService extends AbstractEcommService implements CustomerNotificationServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerNotificationService.class);

	private static final String SEND_PASSWORD_EMAIL = "customerNotification/sendPasswordEmail";
	private static final String GET_REMINDER_LIST = "customerNotification/getReminderListForToday";
	private static final String SEND_REMINDER_EMAIL = "customerNotification/sendReminderEmail";
	private static final String CAPTURE_EMAIL = "customerNotification/iPhoneCaptureEmail";
	private static final String SEND_SETTLEMENT_FAILED_EMAIL = "customerNotification/sendSettlementFailedEmail";
	private static final String GET_ID_BY_EMAIL = "customerNotification/getIdByEmail";
	private static final String IS_ON_ALERT = "customerNotification/isOnAlert";
	private static final String DO_EMAIL = "customerNotification/doEmail";
	
	private static CustomerNotificationServiceI INSTANCE;

	public static CustomerNotificationServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerNotificationService();

		return INSTANCE;
	}

	@Override
	public boolean sendPasswordEmail(String emailAddress, boolean isAltEmail)
			throws RemoteException, FDResourceException, PasswordNotExpiredException {
		Response<Boolean> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("emailAddress", emailAddress);
			rootNode.put("isAltEmail", isAltEmail);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SEND_PASSWORD_EMAIL),
					new TypeReference<Response<Boolean>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				if ("PasswordNotExpiredException".equals(response.getMessage())) {
					throw new PasswordNotExpiredException();
				}
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<String> getReminderListForToday() throws FDResourceException, RemoteException {
		Response<List<String>> response = null;

		response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REMINDER_LIST),
				new TypeReference<Response<List<String>>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();

	}

	@Override
	public void sendReminderEmail(String custId) throws FDResourceException, RemoteException {
		Response<Void> response = null;

		response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SEND_REMINDER_EMAIL + "/" + custId),
				new TypeReference<Response<Void>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}

	}

	@Override
	public boolean iPhoneCaptureEmail(String email, String zipCode, String serviceType)
			throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("email", email);
			rootNode.put("zipCode", zipCode);
			rootNode.put("serviceType", serviceType);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CAPTURE_EMAIL),
					new TypeReference<Response<Boolean>>() {
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
	public void sendSettlementFailedEmail(String saleID) throws FDResourceException, RemoteException {
		Response<Void> response = null;

		response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SEND_SETTLEMENT_FAILED_EMAIL + "/" + saleID),
				new TypeReference<Response<Void>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
	}

	@Override
	public String getIdByEmail(String email) throws FDResourceException, RemoteException {
		Response<String> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("email", email);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_ID_BY_EMAIL),
					new TypeReference<Response<String>>() {
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
	public boolean isOnAlert(String pk, String alertType) throws RemoteException {
		Response<Boolean> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("pk", pk);
			rootNode.put("alertType", alertType);
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(IS_ON_ALERT),
					new TypeReference<Response<Boolean>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void doEmail(XMLEmailI email) throws RemoteException, FDResourceException {
		Response<Void> response = null;
		try {
			ObjectMapper mapper = new ObjectMapper()
					.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"))
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
					.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

			String inputJson = mapper.writeValueAsString(email);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(DO_EMAIL),
					new TypeReference<Response<Void>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		} catch (JsonProcessingException e) {
			throw new RemoteException(e.getMessage());
		}
	}

}
