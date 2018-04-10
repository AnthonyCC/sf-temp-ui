package com.freshdirect.fdstore.survey.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.fdstore.survey.SurveyKey;

/**
 *@deprecated Please use the SurveyController and FDSurveyServiceI  in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface FDSurveySB extends EJBObject {
	@Deprecated
	public FDSurvey getSurvey(SurveyKey key) throws RemoteException;
	@Deprecated
	public FDSurveyResponse getCustomerProfile(FDIdentity identity, EnumServiceType serviceType) throws RemoteException;
	@Deprecated
	public FDSurveyResponse getSurveyResponse(FDIdentity identity, SurveyKey key) throws RemoteException;
	@Deprecated
	public void storeSurvey(FDSurveyResponse survey) throws FDResourceException,RemoteException;
}



