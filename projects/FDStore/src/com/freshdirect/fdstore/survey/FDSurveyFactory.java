package com.freshdirect.fdstore.survey;

import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.ecommerce.data.survey.FDSurveyResponseData;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecomm.gateway.FDSurveyService;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.LazyTimedCache;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDSurveyFactory {

    private static Logger LOGGER = LoggerFactory.getInstance(FDSurveyFactory.class);

    private final static FDSurveyFactory INSTANCE = new FDSurveyFactory();
    
    //private final static FDServiceLocator LOCATOR = new FDServiceLocator();


    private final BuiltinSurveys builtinSurveys = new BuiltinSurveys();
    
    private final static LazyTimedCache<SurveyKey, FDSurvey> surveyDefCache = new LazyTimedCache<SurveyKey, FDSurvey>("SurveyDefCache",
            FDStoreProperties.getSurveyDefCacheSize(), FDStoreProperties.getRefreshSecsSurveyDef() * 1000);

    private final static Thread piRefreshThread = new LazyTimedCache.RefreshThread<SurveyKey, FDSurvey>(surveyDefCache, 3 * 60 * 1000) {
        protected void refresh(List<SurveyKey> expiredKeys) {
            try {
                LOGGER.debug("FDSurvey Refresh reloading " + expiredKeys.size() + " survey definitions");

                for (SurveyKey s : expiredKeys) {
                    FDSurvey fs = FDSurveyFactory.getInstance().getSurveyFromDatabase(s);
                    if (fs != null) {
                        // cache these
                        this.cache.put(s, fs);
                    }
                }

            } catch (Exception ex) {
                LOGGER.warn("Error occured in FDSurvey Refresh", ex);
            }
        }
    };

    static {
        piRefreshThread.start();
    }

    private FDSurveyFactory() {
    }

    /**
     * Get current survey information object for surveyId.
     * 
     * @param surveyId
     *            surveyId
     * 
     * @return FDSurvey object
     * @throws FDResourceException
     * 
     */
    public FDSurvey getSurvey(EnumSurveyType surveyType, EnumServiceType userType) throws FDResourceException {
        userType = correctServiceType(userType);
        SurveyKey key = new SurveyKey(surveyType, userType);
        FDSurvey survey= getSurvey(key);
        if(survey!=null && survey.getKey()!=null  && survey.getKey().getUserType()==null) survey.getKey().setUserType(userType); 
        return getSurvey(key);
    }

    public FDSurvey getSurvey(SurveyKey key) throws FDResourceException {
        // just for testing ...
        FDSurvey survey = builtinSurveys.getOverrideSurvey(key);
        if (survey != null) {
            return survey;
        }
        survey = surveyDefCache.get(key);
        if (survey == null) {
        	synchronized(surveyDefCache) {
        		//check from cache again
        		survey = surveyDefCache.get(key);
        		if (survey != null) {
        			return survey;
        		}
        		survey = getSurveyFromDatabase(key);
	            if (survey == null) {
	                survey = builtinSurveys.getDefaultSurvey(key);
	            }
	            if (survey != null) {
	                surveyDefCache.put(key, survey);
	            }
        	}
            return survey;
        }
        return survey;
    }

    /**
     * Return CORPORATE if the userType is CORPORATE, HOME otherwise.
     * @param userType
     * @return
     */
    private static EnumServiceType correctServiceType(EnumServiceType userType) {
        return (userType != EnumServiceType.CORPORATE) ? EnumServiceType.HOME : userType;
    }

    public FDSurvey getSurvey(EnumSurveyType surveyType, FDUserI user) throws FDResourceException {
        return getSurvey(surveyType, extractServiceType(user));
    }

    public static EnumServiceType getServiceType(FDUserI user, EnumServiceType override) {
        return override != null ? override : (user != null ? user.getSelectedServiceType() : EnumServiceType.HOME);
    }
    
    public static EnumServiceType getServiceType(FDUserI user, HttpServletRequest request) {
        EnumServiceType serviceType = null;
        String serviceTypeParam = request.getParameter("serviceType");
        if (serviceTypeParam != null) {
            serviceType = EnumServiceType.getEnum(serviceTypeParam);
        }
        if (!user.hasServiceBasedOnUserAddress(serviceType)) {
            serviceType = null;
        }
        serviceType = FDSurveyFactory.getServiceType(user, serviceType);
        return serviceType;
    }
    
    private static EnumServiceType extractServiceType(FDUserI user) {
        //return EnumServiceType.CORPORATE; 
        return user != null ? user.getSelectedServiceType() : EnumServiceType.HOME;
    }

    public static FDSurveyFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Return an FDSurvey from the database
     */
    FDSurvey getSurveyFromDatabase(SurveyKey key) throws FDResourceException {
        try {
        	return FDSurveyService.getInstance().getSurvey(key);
        	
        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
    }

    /**
     * User can't be null !
     *  
     * @param user
     * @return
     * @throws FDResourceException
     */
    public static FDSurveyResponse getCustomerProfileSurveyInfo(FDUserI user) throws FDResourceException {
        return getCustomerProfileSurveyInfo(user.getIdentity(), extractServiceType(user));
    }
    
    /**
     * user can be null!
     * @param identity
     * @param user
     * @return
     * @throws FDResourceException
     */
    public static FDSurveyResponse getCustomerProfileSurveyInfo(FDIdentity identity, FDUserI user) throws FDResourceException {
        return getCustomerProfileSurveyInfo(identity, extractServiceType(user));
    }

    public static FDSurveyResponse getCustomerProfileSurveyInfo(FDIdentity identity, EnumServiceType serviceType) throws FDResourceException {
        try {
			FDSurveyResponseData surveyResponseData = FDSurveyService.getInstance().getCustomerProfile(identity,
					serviceType);
			FDSurveyResponse fdSurveyResponse = buildFdSurveyResponse(surveyResponseData);
			return fdSurveyResponse;

        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
    }  
    
    private static FDSurveyResponse buildFdSurveyResponse(FDSurveyResponseData responseData) {
    	if(responseData != null && responseData.getIdentity() != null){
		FDIdentity fdIdentity = new FDIdentity(responseData.getIdentity().getErpCustomerPK(), responseData.getIdentity().getFdCustomerPK());
		SurveyKey surveykey = new SurveyKey(EnumSurveyType.getEnum(responseData.getKey().getSurveyType()), EnumServiceType.getEnum(responseData.getKey().getUserType()));
		FDSurveyResponse fdSurveyResponse = null;
		if(!StringUtil.isEmpty(responseData.getSalePk())){
			 PrimaryKey saleId = new PrimaryKey(responseData.getSalePk());
			 fdSurveyResponse = new FDSurveyResponse(fdIdentity, surveykey,saleId);
		}
		else{
			 fdSurveyResponse = new FDSurveyResponse(fdIdentity, surveykey);
		}
		fdSurveyResponse.setAnswers(responseData.getAnswers());
		return fdSurveyResponse;
    	}
    	return null;
	}

}
