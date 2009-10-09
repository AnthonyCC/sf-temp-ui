package com.freshdirect.smartstore.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.offline.OfflineBatchRecommendationsResult;

public class OfflineBatchRecommenderSessionBean extends SessionBeanSupport {
	private static final long serialVersionUID = 8606750945494173083L;

	private static final Logger LOGGER = LoggerFactory
			.getInstance(OfflineBatchRecommenderSessionBean.class);

	private static OfflineRecommenderHome recommenderHome = null;

	private static void lookupRecommenderHome() throws FDResourceException {
		if (recommenderHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			recommenderHome = (OfflineRecommenderHome) ctx
					.lookup(OfflineRecommenderHome.JNDI_HOME);
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}

	private static void invalidateRecommenderHome() {
		recommenderHome = null;
	}

	public OfflineBatchRecommendationsResult runBatch(String siteFeature)
			throws RemoteException, FDResourceException {
		lookupRecommenderHome();
		OfflineRecommenderSB sb;
		try {
			sb = recommenderHome.create();
			List<String> customerEmails = new ArrayList<String>();
			customerEmails.add("fob@fob.com");
			customerEmails.add("akwok@freshdirect.com");
			for (String customerEmail : customerEmails) {
				sb.recommend(EnumSiteFeature.getEnum(siteFeature),
						customerEmail, null);
			}
			return new OfflineBatchRecommendationsResult(customerEmails.size());
		} catch (CreateException ce) {
			invalidateRecommenderHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateRecommenderHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
}
