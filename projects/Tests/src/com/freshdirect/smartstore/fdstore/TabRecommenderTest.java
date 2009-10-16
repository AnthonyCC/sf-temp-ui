package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.freshdirect.TestUtils;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.CartTabRecommender;
import com.freshdirect.smartstore.CartTabStrategyPriority;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.impl.NullRecommendationService;

public class TabRecommenderTest extends RecommendationServiceTestBase {
	private static class MockService implements RecommendationService {
		private Variant variant;
		
		private MockService(String id) {
			RecommendationServiceConfig rsc;
			if (id.startsWith("A")) {
				rsc = new RecommendationServiceConfig(id, RecommendationServiceType.RANDOM_DYF);
				this.variant = new Variant(id, EnumSiteFeature.DYF, rsc);
			} else if (id.startsWith("B")) {
				rsc = new RecommendationServiceConfig(id, RecommendationServiceType.FAVORITES);
				this.variant = new Variant(id, EnumSiteFeature.FAVORITES, rsc);				
			} else if (id.startsWith("C")) {
				rsc = new RecommendationServiceConfig(id, RecommendationServiceType.FEATURED_ITEMS);
				this.variant = new Variant(id, EnumSiteFeature.FEATURED_ITEMS, rsc);				
			} else {
				rsc = new RecommendationServiceConfig(id, RecommendationServiceType.SMART_YMAL);
				this.variant = new Variant(id, EnumSiteFeature.YMAL, rsc);
			}
		}
		
		public Variant getVariant() {
			return variant;
		}

		public List<ContentNodeModel> recommendNodes(SessionInput input) {
	            ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode("gro_7gen_diaperlg");
                    List<ContentNodeModel> x = new ArrayList<ContentNodeModel>();
                    x.add(contentNode);
                    return x;
		}

		public String getDescription() {
			return "";
		}

		public Map getConfiguration() {
			return Collections.EMPTY_MAP;
		}

		public boolean isIncludeCartItems() {
			return false;
		}
		
		public boolean isSmartSavings() {
			return false;
		}
		
		public boolean isRefreshable() {
			return false;
		}
	};
	
	private RecommendationService v1 = new MockService("A1");
	private RecommendationService v2 = new MockService("B2");
	private RecommendationService v3 = new MockService("C3");
	private RecommendationService v4 = new MockService("D4");
	private RecommendationService nullDyf = new NullRecommendationService(
			new Variant("ANULL", EnumSiteFeature.DYF, 
			new RecommendationServiceConfig("ANULL", RecommendationServiceType.NIL)));
	private RecommendationService nullFavorites = new NullRecommendationService(
			new Variant("BNULL", EnumSiteFeature.FAVORITES, 
			new RecommendationServiceConfig("BNULL", RecommendationServiceType.NIL)));
	
	
	public void setUp () throws Exception {
		super.setUp();
        Map cohorts = new HashMap();
        
        Integer FIFTY = new Integer(50);
        cohorts.put("A", FIFTY);
        cohorts.put("B", FIFTY);
        CohortSelector.setCohorts(cohorts);
        
        CohortSelector.setCohortNames(Arrays.asList(new String[] {"A", "B" } ));
		
    	VariantSelector selector;
		selector = new VariantSelector();

        selector.addCohort("A", v1);
        selector.addCohort("B", nullDyf);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.DYF, selector);

		selector = new VariantSelector();

        selector.addCohort("A", v2);
        selector.addCohort("B", nullFavorites);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FAVORITES, selector);

		selector = new VariantSelector();

        selector.addCohort("A", v3);
        selector.addCohort("B", v3);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, selector);

		selector = new VariantSelector();

        selector.addCohort("A", v4);
        selector.addCohort("B", v4);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.YMAL, selector);

        SortedMap<Integer, SortedMap<Integer, CartTabStrategyPriority>> ps = new TreeMap<Integer, SortedMap<Integer, CartTabStrategyPriority>>();
        SortedMap<Integer, CartTabStrategyPriority> ss = new TreeMap<Integer, CartTabStrategyPriority>();
        ss.put(new Integer(1), new CartTabStrategyPriority("strat-1", "DYF", 1, 1));
        ps.put(new Integer(1), ss);
        ss = new TreeMap<Integer, CartTabStrategyPriority>();
        ss.put(new Integer(1), new CartTabStrategyPriority("strat-1", "FAVORITES", 2, 1));
        ps.put(new Integer(2), ss);
        ss = new TreeMap<Integer, CartTabStrategyPriority>();
        ss.put(new Integer(2), new CartTabStrategyPriority("strat-1", "FEATURED_ITEMS", 3, 2));
        ss.put(new Integer(1), new CartTabStrategyPriority("strat-1", "YMAL", 3, 1));
        ps.put(new Integer(3), ss);
        
    	RecommendationService strat1 = new NullRecommendationService(new Variant("strat-1", EnumSiteFeature.CART_N_TABS, 
    			new RecommendationServiceConfig("strat-1", RecommendationServiceType.NIL), ps));

    	selector = new VariantSelector();

        selector.addCohort("A", strat1);
        selector.addCohort("B", strat1);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.CART_N_TABS, selector);       
	}
	
	public void testTabRecommenders() {
        FDUser user = TestUtils.createUser("125", "459", "790");
        user.setCohortName(CohortSelector.getInstance().getCohortName(user.getPrimaryKey()));
        
        SessionInput input = new SessionInput(user);
        input.setMaxRecommendations(5);
        TabRecommendation tr = CartTabRecommender.recommendTabs(user, input, SmartStoreUtil.SKIP_OVERRIDDEN_VARIANT);
        assertNotNull("tab recommender always returns an object", tr);
        assertEquals("tab recommender returns 3 tabs / variants", 3, tr.size());
        assertEquals("first Variant is A1", "A1", tr.get(0).getId());
        assertEquals("second Variant is B2", "B2", tr.get(1).getId());
        assertEquals("third Variant is D4", "D4", tr.get(2).getId());

        user = TestUtils.createUser("123", "456", "789");
        user.setCohortName(CohortSelector.getInstance().getCohortName(user.getPrimaryKey()));
        
        input = new SessionInput(user);
        input.setMaxRecommendations(5);
        tr = CartTabRecommender.recommendTabs(user, input, SmartStoreUtil.SKIP_OVERRIDDEN_VARIANT);
        assertNotNull("tab recommender always returns an object", tr);
        assertEquals("tab recommender returns 1 tab / variant", 1, tr.size());
        assertEquals("first Variant is D4", "D4", tr.get(0).getId());
	}
}
