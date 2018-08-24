package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.SurveyKey;

public interface FDSurveyServiceI {
	public FDSurvey getSurvey(SurveyKey key) throws RemoteException;

	public void storeSurvey(FDSurveyResponseData survey) throws FDResourceException;

	public FDSurveyResponseData getCustomerProfile(FDIdentity identity, EnumServiceType serviceType)
			throws RemoteException;
}
