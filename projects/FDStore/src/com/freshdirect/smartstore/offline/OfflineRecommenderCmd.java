package com.freshdirect.smartstore.offline;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.smartstore.ejb.OfflineRecommenderHome;
import com.freshdirect.smartstore.ejb.OfflineRecommenderSB;

public class OfflineRecommenderCmd {
	private static final Logger LOG = Logger
			.getLogger(OfflineRecommenderCmd.class);

	public static void main(String[] args) {
		if (args.length < 2)
			usage();
		int days = -1;
		try {
			days = Integer.parseInt(args[0]);
			if (days <= 0) {
				LOG.error("num_of_days parameter must be positive");
				usage();
			}
		} catch (NumberFormatException e) {
			LOG.error("num_of_days parameter: integer number expected");
		}
		Set<String> siteFeaturesSet = new HashSet<String>(args.length);
		for (int i = 1; i < args.length; i++)
			try {
				lookupOfflineRecommenderHome();
				OfflineRecommenderSB sb = offlineRecommenderHome.create();
				sb.checkSiteFeature(args[i]);
				siteFeaturesSet.add(args[i]);
				LOG.info("site feature valid: " + args[i]);
			} catch (Exception e) {
				invalidateOfflineRecommenderHome();
				LOG.error("error while validating site feature: " + args[i], e);
				LOG.fatal("exiting: failed to validate site feature");
				System.exit(-1);
			}
		String[] siteFeatures = siteFeaturesSet.toArray(new String[0]);
		Set<String> customerIds = null;
		try {
			lookupOfflineRecommenderHome();
			OfflineRecommenderSB sb = offlineRecommenderHome.create();
			customerIds = sb.getRecentCustomers(days);
			LOG.info("customers counted for the last " + days + " days: "
					+ customerIds.size());
		} catch (Exception e) {
			invalidateOfflineRecommenderHome();
			LOG.error("error while retrieving recent customers", e);
			LOG.fatal("exiting: failed to retrieve recent customers");
			System.exit(-1);
		}
		for (String customerId : customerIds)
			for (String siteFeature : siteFeatures)
				try {
					lookupOfflineRecommenderHome();
					OfflineRecommenderSB sb = offlineRecommenderHome.create();
					int n = sb.recommend(siteFeature, customerId, null);
					LOG.debug("recommendations (" + siteFeature
							+ ") for customer #" + customerId + ": " + n);
				} catch (Exception e) {
					invalidateOfflineRecommenderHome();
					LOG
							.error("exception while generating " + siteFeature
									+ " recommendations for customer #"
									+ customerId, e);
				}
	}

	private static void usage() {
		LOG.error("usage: java " + OfflineRecommenderCmd.class.getSimpleName()
				+ " num_of_days SITE_FEATURE [ SITE_FEATURE_2 ... ]");
		LOG.fatal("exiting: wrong parameters");
		System.exit(-1);
	}

	private static Context getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}

	private static OfflineRecommenderHome offlineRecommenderHome = null;

	private static void lookupOfflineRecommenderHome()
			throws FDResourceException {
		if (offlineRecommenderHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = getInitialContext();
			offlineRecommenderHome = (OfflineRecommenderHome) ctx
					.lookup(OfflineRecommenderHome.JNDI_HOME);
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOG.warn("cannot close Context while trying to cleanup", ne);
			}
		}
	}

	private static void invalidateOfflineRecommenderHome() {
		offlineRecommenderHome = null;
	}
}
