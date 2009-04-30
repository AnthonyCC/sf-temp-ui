package com.freshdirect.smartstore;

import java.util.ArrayList;
import java.util.Iterator;
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
        List recs = new ArrayList();
        RecommendationService rs = null;
        try {
            if (user != null) {
                rs = SmartStoreUtil.getRecommendationService(user, EnumSiteFeature.CART_N_TABS, overriddenVariantId);
                if (rs != null) {
                    SortedMap prios = rs.getVariant().getTabStrategyPriorities();

                    Iterator it = prios.keySet().iterator();
                    while (it.hasNext()) {
                        Integer p1 = (Integer) it.next();
                        SortedMap map = (SortedMap) prios.get(p1);

                        Iterator it2 = map.keySet().iterator();
                        while (it2.hasNext()) {
                            Integer p2 = (Integer) it2.next();
                            CartTabStrategyPriority strat = (CartTabStrategyPriority) map.get(p2);

                            if (strat == null) {
                                continue;
                            }

                            try {
                                Recommendations rec = FDStoreRecommender.getInstance().getRecommendations(EnumSiteFeature.getEnum(strat.getSiteFeatureId()), user, input, overriddenVariantId,
                                        SmartStoreUtil.toContentKeySetFromModels(FDStoreRecommender.getShoppingCartContents(user)));
                                if (rec.getProducts().size() > 0) {
                                    recs.add(rec.getVariant());
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
