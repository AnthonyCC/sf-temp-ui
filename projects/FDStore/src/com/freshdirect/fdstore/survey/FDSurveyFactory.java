package com.freshdirect.fdstore.survey;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.survey.ejb.FDSurveyHome;
import com.freshdirect.fdstore.survey.ejb.FDSurveySB;
import com.freshdirect.framework.util.LazyTimedCache;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDSurveyFactory {

    private static Logger LOGGER = LoggerFactory.getInstance(FDSurveyFactory.class);

    private final static FDSurveyFactory INSTANCE = new FDSurveyFactory();

    private static FDSurveyHome surveyHome = null;


    private final BuiltinSurveys builtinSurveys = new BuiltinSurveys();
    
    private final static LazyTimedCache<SurveyKey, FDSurvey> surveyDefCache = new LazyTimedCache<SurveyKey, FDSurvey>(
            FDStoreProperties.getSurveyDefCacheSize(), FDStoreProperties.getRefreshSecsSurveyDef() * 1000);

    private final static Thread piRefreshThread = new LazyTimedCache.RefreshThread<SurveyKey, FDSurvey>(surveyDefCache, "FDSurvey Refresh", 3 * 60 * 1000) {
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
    public synchronized FDSurvey getSurvey(EnumSurveyType surveyType, EnumServiceType userType) throws FDResourceException {
        SurveyKey key = new SurveyKey(surveyType, userType);
        FDSurvey cached = surveyDefCache.get(key);
        if (cached == null) {
            cached = getSurveyFromDatabase(key);
            if (cached == null) {
                cached = builtinSurveys.getDefaultSurvey(key);
            }
            if (cached != null) {
                surveyDefCache.put(key, cached);
            }
            return cached;
        }
        return cached;
    }

    public FDSurvey getSurvey(EnumSurveyType surveyType, FDUserI user) throws FDResourceException {
        return getSurvey(surveyType, extractServiceType(user));
    }

    private static EnumServiceType extractServiceType(FDUserI user) {
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
            FDSurveySB sb = lookupSurveyHome().create();
            return sb.getSurvey(key);
        } catch (CreateException ce) {
            throw new FDResourceException(ce, "Error creating session bean");
        } catch (RemoteException re) {
            throw new FDResourceException(re, "Error talking to session bean");
        }
    }



    private synchronized static FDSurveyHome lookupSurveyHome() throws FDResourceException {
        if (surveyHome != null) {
            return surveyHome;
        }
        Context ctx = null;
        try {
            ctx = FDStoreProperties.getInitialContext();
            surveyHome = (FDSurveyHome) ctx.lookup(FDStoreProperties.getFDSurveyHome());
            return surveyHome;
        } catch (NamingException ne) {
            throw new FDResourceException(ne);
        } finally {
            try {
                if (ctx != null) {
                    ctx.close();
                }
            } catch (NamingException e) {
            }
        }
    }
    
    private static synchronized void invalidateSurveyHome() {
        surveyHome = null;
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
            FDSurveySB sb = lookupSurveyHome().create();
            return sb.getCustomerProfile(identity, serviceType);
        } catch (CreateException ce) {
            invalidateSurveyHome();
            throw new FDResourceException(ce, "Error creating session bean");
        } catch (RemoteException re) {
            invalidateSurveyHome();
            throw new FDResourceException(re, "Error talking to session bean");
        }
    }       

    public static FDSurveyResponse getSurveyResponse(FDIdentity identity, SurveyKey survey) throws FDResourceException {
        try {
            FDSurveySB sb = lookupSurveyHome().create();
            return sb.getSurveyResponse(identity, survey);
        } catch (CreateException ce) {
            invalidateSurveyHome();
            throw new FDResourceException(ce, "Error creating session bean");
        } catch (RemoteException re) {
            invalidateSurveyHome();
            throw new FDResourceException(re, "Error talking to session bean");
        }
    }

}
