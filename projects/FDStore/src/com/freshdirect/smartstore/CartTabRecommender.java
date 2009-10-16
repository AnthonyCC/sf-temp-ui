package com.freshdirect.smartstore;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;

public class CartTabRecommender {

    private static Logger LOGGER = LoggerFactory.getInstance(CartTabRecommender.class);

    /**
     * 
     * @param user
     * @param input
     * @param maxRecommendations
     * @return up to 3 variant used to produce cart tab recommendations
     */
    public static TabRecommendation recommendTabs(FDUserI user, SessionInput input, String overriddenVariantId) {
        List<Variant> recs = new ArrayList<Variant>();
        RecommendationService rs = null;
        boolean smartSavingsFound = false;
        try {
            if (user != null) {
                rs = SmartStoreUtil.getRecommendationService(user, EnumSiteFeature.CART_N_TABS, overriddenVariantId);
                if (rs != null) {
                    SortedMap<Integer, SortedMap<Integer, CartTabStrategyPriority>> prios = rs.getVariant().getTabStrategyPriorities();

                      for (Integer p1 : prios.keySet()) { 
                        SortedMap<Integer,CartTabStrategyPriority> map = prios.get(p1);

                        for (Integer p2 : prios.keySet()) { 
                            CartTabStrategyPriority strat = map.get(p2);

                            if (strat == null) {
                                continue;
                            }

                            try {
                            	EnumSiteFeature sf = EnumSiteFeature.getEnum(strat.getSiteFeatureId());
                            	if(sf.isSmartSavings() && smartSavingsFound) {
                            	    continue;
                            	}
                                Recommendations rec = FDStoreRecommender.getInstance().getRecommendations(sf, user, input, overriddenVariantId,
                                        FDStoreRecommender.getShoppingCartContentKeys(user));
                                if (rec.getProducts().size() > 0) {
                                    recs.add(rec.getVariant());
                                    if(sf.isSmartSavings()) {
                                        smartSavingsFound = true;
                                    }
                                    break;
                                }
                            } catch (FDResourceException e) {

                            }

                        }

                        if (recs.size() >= 3) {
                            break;
                        }
                    }
                } else {
                    LOGGER.warn("CartTabRecommender.recommendTabs() failed. Returning empty array.");
                }
            }
        } catch (FDResourceException e) {
            LOGGER.error("unable to query Cart Tab Recommenders", e);
        }

        return new TabRecommendation(rs, recs);
    }
}
