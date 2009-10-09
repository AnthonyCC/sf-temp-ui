package com.freshdirect.smartstore.fdstore;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.ejb.ScoreFactorHome;
import com.freshdirect.smartstore.ejb.ScoreFactorSB;

public class DatabaseScoreFactorProvider {

    private static Logger                      LOGGER         = LoggerFactory.getInstance(DatabaseScoreFactorProvider.class);

    private static DatabaseScoreFactorProvider instance       = null;

    private ServiceLocator                     serviceLocator = null;

    private ScoreFactorSB                      scoreFactorSB  = null;

    public synchronized static DatabaseScoreFactorProvider getInstance() {
        if (instance == null) {
            try {
                instance = new DatabaseScoreFactorProvider();
            } catch (RemoteException e) {
                LOGGER.warn("Could not create " + DatabaseScoreFactorProvider.class.getName() + " instance", e);
                throw new FDRuntimeException(e);
            } catch (CreateException e) {
                LOGGER.warn("Could not create " + DatabaseScoreFactorProvider.class.getName() + " instance", e);
                throw new FDRuntimeException(e);
            } catch (NamingException e) {
                LOGGER.warn("Could not create " + DatabaseScoreFactorProvider.class.getName() + " instance", e);
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public Map getPersonalizedFactors(String erpCustomerId, List factors) {
        try {
            if (erpCustomerId == null) {
                return Collections.EMPTY_MAP;
            }
            return getSessionBean().getPersonalizedFactors(erpCustomerId, factors);
        } catch (RemoteException e) {
            LOGGER.warn(e);
            throw new FDRuntimeException(e);
        }
    }

    public Map getGlobalFactors(List factors) {
        try {
            return getSessionBean().getGlobalFactors(factors);
        } catch (RemoteException e) {
            LOGGER.warn(e);
            throw new FDRuntimeException(e);
        }
    }

    public Set getGlobalFactorNames() {
        try {
            return getSessionBean().getGlobalFactorNames();
        } catch (RemoteException e) {
            LOGGER.warn(e);
            throw new FDRuntimeException(e);
        }
    }

    public Set getPersonalizedFactorNames() {
        try {
            return getSessionBean().getPersonalizedFactorNames();
        } catch (RemoteException e) {
            LOGGER.warn(e);
            throw new FDRuntimeException(e);
        }
    }

    public Set getPersonalizedProducts(String erpCustomerId) {
        try {
            return getSessionBean().getPersonalizedProducts(erpCustomerId);
        } catch (RemoteException e) {
            LOGGER.warn(e);
            throw new FDRuntimeException(e);
        }
    }

    public Set getGlobalProducts() {
        try {
            return getSessionBean().getGlobalProducts();
        } catch (RemoteException e) {
            LOGGER.warn(e);
            throw new FDRuntimeException(e);
        }
    }

    /**
     * Return a list of product recommendation for a given product by a
     * recommender vendor.
     * 
     * @param recommender
     * @param key
     * @return List<ContentKey>
     * @throws RemoteException
     */
    public List getProductRecommendations(String recommender, ContentKey key) {
        try {
            return getSessionBean().getProductRecommendations(recommender, key);
        } catch (RemoteException e) {
            LOGGER.warn(e);
            throw new FDRuntimeException(e);
        }
    }

    /**
     * Return a list of personal recommendation (ContentKey-s) for a user by a
     * recommender vendor.
     * 
     * @param recommender
     * @param erpCustomerId
     * @return List<ContentKey>
     * @throws RemoteException
     */
    public List getPersonalRecommendations(String recommender, String erpCustomerId) {
        try {
            return getSessionBean().getPersonalRecommendations(recommender, erpCustomerId);
        } catch (RemoteException e) {
            LOGGER.warn(e);
            throw new FDRuntimeException(e);
        }
    }

    private DatabaseScoreFactorProvider() throws NamingException, RemoteException, CreateException {
        serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
        scoreFactorSB = getScoreFactorHome().create();
    }

    // get service configuration home bean
    private ScoreFactorHome getScoreFactorHome() throws NamingException {
        return (ScoreFactorHome) serviceLocator.getRemoteHome("freshdirect.smartstore.ScoreFactorHome", ScoreFactorHome.class);

    }

    private final ScoreFactorSB getSessionBean() {
        return scoreFactorSB;
    }
}
