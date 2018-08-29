package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.ecommerce.data.survey.SurveyData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.SurveyKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDSurveyService extends AbstractEcommService implements FDSurveyServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(FDSurveyService.class);

	private static final String SURVEY ="survey";
	private static final String STORE_SURVEY = "survey/store";
	private static final String GET_CUSTOMER_PROFILE = "survey/customerprofile";

	private static FDSurveyServiceI INSTANCE;

	public static FDSurveyServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDSurveyService();

		return INSTANCE;
	}

	@Override
	public void storeSurvey(FDSurveyResponseData survey) throws FDResourceException {
		try {
			Request<FDSurveyResponseData> request = new Request<FDSurveyResponseData>();
			request.setData(survey);
			String inputJson = buildRequest(request);
			Response<FDSurveyResponseData> response = this.postDataTypeMap(inputJson,
					getFdCommerceEndPoint(STORE_SURVEY), new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in FDSurveyService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in FDSurveyService: survey=" + survey, e);
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public FDSurveyResponseData getCustomerProfile(FDIdentity identity, EnumServiceType serviceType)
			throws RemoteException {
		Response<FDSurveyResponseData> response = null;
		try {
			Request<SurveyData> request = buildSurveyData(identity, serviceType);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_CUSTOMER_PROFILE),
					new TypeReference<Response<FDSurveyResponseData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in FDSurveyService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}

		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in FDSurveyService: identity=" + identity + ", serviceType=" + serviceType, e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in FDSurveyService: identity=" + identity + ", serviceType=" + serviceType, e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	
	@Override
	public FDSurvey getSurvey(SurveyKey key) throws RemoteException {
		Response<FDSurvey> response = new Response<FDSurvey>();
		try {
			Request<SurveyKey> request = new Request<SurveyKey>();
			request.setData(key);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SURVEY), new TypeReference<Response<FDSurvey>>() {});
			if (!response.getResponseCode().equals("OK")){
				LOGGER.error("Error in FDSurveyService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDEcommServiceException e) {
			LOGGER.error("Error in FDSurveyService: key=" + key, e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in FDSurveyService: key=" + key, e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	private Request<SurveyData> buildSurveyData(FDIdentity identity, EnumServiceType serviceType) {
		Request<SurveyData> request = new Request<SurveyData>();
		SurveyData surveyData = new SurveyData();
		surveyData.setErpCustomerid(identity.getErpCustomerPK());
		surveyData.setFdCustomerId(identity.getFDCustomerPK());
		surveyData.setServiceType(serviceType.toString());
		request.setData(surveyData);
		return request;
	}



}
