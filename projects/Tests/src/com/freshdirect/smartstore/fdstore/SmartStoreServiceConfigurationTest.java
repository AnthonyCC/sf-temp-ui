package com.freshdirect.smartstore.fdstore;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;

import junit.framework.TestCase;

import org.mockejb.MockContainer;
import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.fdstore.aspects.SmartStoreServiceConfigurationBeanAspect;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.Variant;

public class SmartStoreServiceConfigurationTest extends TestCase {

    final static String[] STRATS    = { "linear", "uniform", "harmonic", "cubic", "deterministic", "quadratic" };

    final static int[]    TOP_N     = { 20, 20, 30, 40, 20, 20 };
    final static String[] STRAT_RES = { "linear", "uniform", "harmonic(100.0)", "cubic", "deterministic", "quadratic" };

    protected void setUp() throws Exception {
        Context context = TestUtils.createContext();

        TestUtils.createTransaction(context);

        MockContainer mockContainer = TestUtils.createMockContainer(context);

        AspectSystem aspectSystem = TestUtils.createAspectSystem();

        aspectSystem.add(new SmartStoreServiceConfigurationBeanAspect() {
            public Collection getVariants(EnumSiteFeature feature) {
                if (feature == EnumSiteFeature.FEATURED_ITEMS) {
                    Set result = new HashSet();
                    List all = RecommendationServiceType.all();
                    for (int i = 0; i < all.size(); i++) {
                        RecommendationServiceType type = (RecommendationServiceType) all.get(i);
                        result.add(new Variant("test_" + type.getName(), EnumSiteFeature.FEATURED_ITEMS, new RecommendationServiceConfig("test"
                                + type.getName() + "_config", type)));
                    }

                    return result;
                }
                if (feature == EnumSiteFeature.DYF) {
                    Set result = new HashSet();
                    for (int i = 0; i < STRATS.length; i++) {
                        result.add(new Variant("dyf_test_" + i, EnumSiteFeature.DYF, new RecommendationServiceConfig("dyf_conf_" + i,
                                RecommendationServiceType.FREQUENTLY_BOUGHT_DYF).set("sampling_strat", STRATS[i]).set("top_n", String.valueOf(TOP_N[i]))));
                    }
                    return result;
                }
                return Collections.EMPTY_SET;
            }
        });

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFeaturedItems() {
        Map services = SmartStoreServiceConfiguration.getInstance().getServices(EnumSiteFeature.FEATURED_ITEMS);
        RecommendationService service = (RecommendationService) services.get("test_manual_override");
        assertNotNull("test_manual_override", service);
        assertNotNull("test_manual_override variant", service.getVariant());
        assertEquals("test_manual_override variant", "test_manual_override", service.getVariant().getId());

        for (Iterator iter = services.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            RecommendationService s = (RecommendationService) services.get(key);
            if (key.startsWith("test_")) {
                String name = key.substring(5);
                assertNotNull("s variant[" + key + ']', s.getVariant());
                assertEquals("s variant[" + key + "] id", key, s.getVariant().getId());
                assertEquals("s variant[" + key + "] type", name, s.getVariant().getServiceConfig().getType().getName());
            }
        }
    }

    public void testDyf() {
        Map services = SmartStoreServiceConfiguration.getInstance().getServices(EnumSiteFeature.DYF);

        for (int i = 0; i < STRATS.length; i++) {
            RecommendationService s = (RecommendationService) services.get("dyf_test_" + i);
            assertNotNull("dyf_test_" + i, s);
            assertEquals("description", "Service(feature:DYF,variant:dyf_test_" + i
                    + ",class:MostFrequentlyBoughtDyfVariant,cat_aggr:false,include_cart_items:false,sampler:(limit:topN:" + TOP_N[i] + ",percent:20.0,list:"
                    + STRAT_RES[i] + "),)", s.toString());
        }
    }
}
