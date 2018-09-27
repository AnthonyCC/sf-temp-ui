package com.freshdirect.backoffice.selfcredit.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditRequest;
import com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditResponse;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BackOfficeSelfCreditService {

    private static final Logger LOGGER = LoggerFactory.getInstance(BackOfficeSelfCreditService.class);
    private static final BackOfficeSelfCreditService INSTANCE = new BackOfficeSelfCreditService();

    private static final String SELF_CREDIT_CASE_MEDIA = "Self Credit";

    private BackOfficeSelfCreditService() {
    }

    public static BackOfficeSelfCreditService defaultService() {
        return INSTANCE;
    }

    public IssueSelfCreditResponse postSelfCreditRequest(IssueSelfCreditRequest issueSelfCreditRequest) {

        StringBuilder content = new StringBuilder();
        BufferedReader input = null;
        try {
            URL url = new URL(FDStoreProperties.getBkofficeSelfCreditUrl());
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
                    LOGGER.error("Exception while closing conenction with Backoffice Self-Credit service.", e);
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
        return issueSelfCreditResponse;
    }
}
