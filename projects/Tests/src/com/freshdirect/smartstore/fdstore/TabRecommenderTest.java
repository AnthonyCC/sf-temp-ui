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
import com.freshdirect.smartstore.service.VariantRegistry;

public class TabRecommenderTest extends RecommendationServiceTestBase {
	private static class MockService implements RecommendationService {
		private Variant variant;
		
		private MockService(Variant variant) {
			this.variant = variant;
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
	
	private Variant createMockService(String id) {
		RecommendationServiceConfig rsc;
		Variant variant;
		if (id.startsWith("A")) {
			rsc = new RecommendationServiceConfig(id, RecommendationServiceType.RANDOM_DYF);
			variant = new Variant(id, EnumSiteFeature.DYF, rsc);
		} else if (id.startsWith("B")) {
			rsc = new RecommendationServiceConfig(id, RecommendationServiceType.FAVORITES);
			variant = new Variant(id, EnumSiteFeature.FAVORITES, rsc);				
		} else if (id.startsWith("C")) {
			rsc = new RecommendationServiceConfig(id, RecommendationServiceType.FEATURED_ITEMS);
			variant = new Variant(id, EnumSiteFeature.FEATURED_ITEMS, rsc);				
		} else {
			rsc = new RecommendationServiceConfig(id, RecommendationServiceType.SMART_YMAL);
			variant = new Variant(id, EnumSiteFeature.YMAL, rsc);
		}
		variant.setRecommender(new MockService(variant));
		VariantRegistry.getInstance().addService(variant);
		return variant;
	}
	
	private Variant createNullService(Variant variant) {
		variant.setRecommender(new NullRecommendationService(variant));
		VariantRegistry.getInstance().addService(variant);
		return variant;
	}
	
	private Variant v1 = createMockService("A1");
	private Variant v2 = createMockService("B2");
	private Variant v3 = createMockService("C3");
	private Variant v4 = createMockService("D4");
	private Variant nullDyf = createNullService(new Variant("ANULL",
			EnumSiteFeature.DYF, new RecommendationServiceConfig("ANULL",
					RecommendationServiceType.NIL)));
	private Variant nullFavorites = createNullService(new Variant("BNULL",
			EnumSiteFeature.FAVORITES, new RecommendationServiceConfig("BNULL",
					RecommendationServiceType.NIL)));
	
	
	public void setUp () throws Exception {
		super.setUp();
        Map cohorts = new HashMap();
        
        Integer FIFTY = new Integer(50);
        cohorts.put("A", FIFTY);
        cohorts.put("B", FIFTY);
        CohortSelector.setCohorts(cohorts);
        
        CohortSelector.setCohortNames(Arrays.asList(new String[] {"A", "B" } ));
		
    	VariantSelector selector;
		selector = new VariantSelector(EnumSiteFeature.DYF);

        selector.addCohort("A", v1);
        selector.addCohort("B", nullDyf);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.DYF, selector);

		selector = new VariantSelector(EnumSiteFeature.FAVORITES);

        selector.addCohort("A", v2);
        selector.addCohort("B", nullFavorites);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FAVORITES, selector);

		selector = new VariantSelector(EnumSiteFeature.FEATURED_ITEMS);

        selector.addCohort("A", v3);
        selector.addCohort("B", v3);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.FEATURED_ITEMS, selector);

		selector = new VariantSelector(EnumSiteFeature.YMAL);

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
        
        Variant tabVariant = new Variant("strat-1", EnumSiteFeature.CART_N_TABS, 
    			new RecommendationServiceConfig("strat-1", RecommendationServiceType.NIL), ps);
    	RecommendationService strat1 = new NullRecommendationService(tabVariant);
    	tabVariant.setRecommender(strat1);

    	selector = new VariantSelector(EnumSiteFeature.CART_N_TABS);

        selector.addCohort("A", tabVariant);
        selector.addCohort("B", tabVariant);
        
        VariantSelectorFactory.setVariantSelector(EnumSiteFeature.CART_N_TABS, selector);       
	}
	
	public void testTabRecommenders() {
        FDUser user = TestUtils.createUser("125", "459", "790");
        user.setCohortName(CohortSelector.getInstance().getCohortName(user.getPrimaryKey()));
        
        SessionInput input = new SessionInput(user);
        input.setMaxRecommendations(5);
        TabRecommendation tr = CartTabRecommender.recommendTabs(user, input);
        assertNotNull("tab recommender always returns an object", tr);
        assertEquals("tab recommender returns 3 tabs / variants", 3, tr.size());
        assertEquals("first Variant is A1", "A1", tr.get(0).getId());
        assertEquals("second Variant is B2", "B2", tr.get(1).getId());
        assertEquals("third Variant is D4", "D4", tr.get(2).getId());

        user = TestUtils.createUser("123", "456", "789");
        user.setCohortName(CohortSelector.getInstance().getCohortName(user.getPrimaryKey()));
        
        input = new SessionInput(user);
        input.setMaxRecommendations(5);
        tr = CartTabRecommender.recommendTabs(user, input);
        assertNotNull("tab recommender always returns an object", tr);
        assertEquals("tab recommender returns 1 tab / variant", 1, tr.size());
        assertEquals("first Variant is D4", "D4", tr.get(0).getId());
	}
}
