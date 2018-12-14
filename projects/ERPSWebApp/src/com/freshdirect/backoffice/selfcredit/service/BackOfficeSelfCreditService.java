package com.freshdirect.backoffice.selfcredit.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.python.parser.ast.boolopType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditRequest;
import com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditResponse;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderItemRequestData;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BackOfficeSelfCreditService {

    private static final Logger LOGGER = LoggerFactory.getInstance(BackOfficeSelfCreditService.class);
    private static final BackOfficeSelfCreditService INSTANCE = new BackOfficeSelfCreditService();

    private BackOfficeSelfCreditService() {
    }

    public static BackOfficeSelfCreditService defaultService() {
        return INSTANCE;
    }

    public IssueSelfCreditResponse postSelfCreditRequest(IssueSelfCreditRequest issueSelfCreditRequest, FDUserI user) {

    	final String orderId = issueSelfCreditRequest.getOrderId();
    	final boolean isSelfCreditRequestValid = validateSelfCreditRequest(issueSelfCreditRequest, user);
		
        if (!isSelfCreditRequestValid) {
			IssueSelfCreditResponse issueSelfCreditResponse = new IssueSelfCreditResponse();
			issueSelfCreditResponse.setMessage("ERROR");
			issueSelfCreditResponse.setSuccess(false);
			logActivityError(user, orderId, issueSelfCreditResponse);
			return issueSelfCreditResponse;
		}
    	
    	StringBuilder content = new StringBuilder();
        BufferedReader input = null;
        try {
        	String backofficeUrl = new StringBuilder().append(FDStoreProperties.getBackOfficeApiUrl()).append(FDStoreProperties.getBkofficeSelfCreditUrl()).toString();
            URL url = new URL(backofficeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            OutputStream output = connection.getOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            String issueCreditRequestAsString = objectMapper.writeValueAsString(issueSelfCreditRequest);
            output.write(issueCreditRequestAsString.getBytes("UTF-8"));

            output.close();

            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine = null;

            while ((inputLine = input.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while making call to Backoffice Self-Credit service.", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("Exception while closing connection with Backoffice Self-Credit service.", e);
                }
            }
        }

        IssueSelfCreditResponse issueSelfCreditResponse = new IssueSelfCreditResponse();
        ObjectMapper objectMapper = new ObjectMapper();
		try {
			issueSelfCreditResponse = objectMapper.readValue(content.toString(), IssueSelfCreditResponse.class);
		} catch (JsonParseException e) {
			LOGGER.error("Exception while mapping issue self-complaint response.", e);
		} catch (JsonMappingException e) {
			LOGGER.error("Exception while mapping issue self-complaint response.", e);
		} catch (IOException e) {
			LOGGER.error("Exception while mapping issue self-complaint response.", e);
		}
		
		if (!issueSelfCreditResponse.isSuccess()) {
			logActivityError(user, orderId, issueSelfCreditResponse);
		} else {
			LOGGER.debug(issueSelfCreditResponse.getMessage());
		}
        return issueSelfCreditResponse;
    }

	private boolean validateSelfCreditRequest(IssueSelfCreditRequest issueSelfCreditRequest, FDUserI user) {
		final String orderId = issueSelfCreditRequest.getOrderId();
		final boolean isOrderIdValid = validateOrder(orderId, user);
		final boolean hasCartonNumbers = validateCartonNumbers(issueSelfCreditRequest.getOrderLineParams(), orderId);
		return isOrderIdValid && hasCartonNumbers;
	}

	private boolean validateOrder(String orderId, FDUserI user) {
		boolean isOrderIdValid = false;
		try {
			isOrderIdValid = FDCustomerManager.orderBelongsToUser(user.getIdentity(), orderId);
		} catch (FDResourceException e1) {
			LOGGER.error("Error while checking if user " + user.getUserId() + " has Self-credit order id: " + orderId);
		}
		if (!isOrderIdValid) {
			LOGGER.error("Self-credit order id: " + orderId + " does not belong to user: " + user.getUserId());
		}
		return isOrderIdValid;
	}
	
	private boolean validateCartonNumbers(List<SelfCreditOrderItemRequestData> selfCreditItems, String orderId) {
		for (SelfCreditOrderItemRequestData selfCreditItem : selfCreditItems) {
			if (null == selfCreditItem.getCartonNumbers() || selfCreditItem.getCartonNumbers().isEmpty() ) {
				LOGGER.error("Unable to issue self-complaint for order " + orderId + " due to missing carton numbers.");
				return false;
			}
		}
		return true;
	}

	private void logActivityError(FDUserI user, String orderId, IssueSelfCreditResponse issueSelfCreditResponse) {
		LOGGER.error("Unable to issue self-complaint for order " + orderId + ".");
    	LOGGER.error(issueSelfCreditResponse.getMessage());

		String customerId = user.getIdentity().getErpCustomerPK();
		EnumTransactionSource transactionSource = user.getApplication();
		String note = new StringBuilder(EnumAccountActivityType.SELF_CREDIT_ERROR.getName()).append(", orderId: ").append(orderId).toString();
		
		ErpActivityRecord erpActivityRecord = new ErpActivityRecord();
		erpActivityRecord.setCustomerId(customerId);
		erpActivityRecord.setActivityType(EnumAccountActivityType.SELF_CREDIT_ERROR);
		erpActivityRecord.setDate(new Date());
		erpActivityRecord.setSource(transactionSource);
		erpActivityRecord.setNote(note);
		new ErpLogActivityCommand(erpActivityRecord).execute();
	}
	
}
