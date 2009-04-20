package com.freshdirect.fdstore.survey.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyResponse;

public interface FDSurveySB extends EJBObject {
	public FDSurvey getSurvey(String surveyName) throws RemoteException;
	public FDSurveyResponse getCustomerProfile(FDIdentity identity) throws RemoteException;
	public FDSurveyResponse getSurveyResponse(FDIdentity identity, String survey) throws RemoteException;
}


