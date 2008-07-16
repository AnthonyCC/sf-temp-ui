package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;

import junit.framework.TestCase;

public class VariantSelectorTest extends TestCase {
	
	
	private static class MockService implements RecommendationService {

		private Variant variant;
		
		private MockService(String id) {
			this.variant = new Variant(id, EnumSiteFeature.DYF, null);
		}
		
		public Variant getVariant() {
			return variant;
		}

		public List recommend(int max, SessionInput input) {
			return null;
		}
		
	};
	
	private List userIds;
	private RecommendationService v1 = new MockService("v1");
	private RecommendationService v2 = new MockService("v2");
	private RecommendationService v3 = new MockService("v3");
	
	private VariantSelector selector = new VariantSelector() {
		
		protected void init() {
			
			// v1 -> 3/10
			// v2 -> 2/10
			// v3 -> 5/10
			
			addCohort("C", v1, 5);
			addCohort("A", v1, 10);
			addCohort("C", v1, 5);
			addCohort("E", v2, 10);
			addCohort("F", v3, 10);
			addCohort("G", v3, 10);
			addCohort("B", v1, 10);
			addCohort("H", v3, 10);
			addCohort("D", v2, 10);
			addCohort("J", v3, 10);
			addCohort("I", v3, 10);
			
		}
		
		protected int randomize(String id) {
			return id.hashCode();
		}
	};
	
	public void setUp () {
		
		Random R = new Random();
		userIds = new ArrayList();
		for(int i=0; i< 10000; ++i) {
			String id = new StringBuffer(4).append((100*((Math.abs(R.nextInt()) % 900) + 100)) + Math.abs(R.nextInt()) % 30).toString();
			userIds.add(id);
		}
		
		selector.init();
	}
	
	private static boolean withinTenPercent(int v1, int v2) {
		return Math.abs(v1 - v2) * 10 <  v2;
	}
	
	public void testSelectionFrequencies() {
		
		Map counts = new HashMap();
		
		for(Iterator i = userIds.iterator(); i.hasNext(); ) {
			String userId = i.next().toString();
			Variant v = selector.select(userId).getVariant();
			Integer f = (Integer)counts.get(v);
			if (f == null) counts.put(v, new Integer(1));
			else counts.put(v, new Integer(f.intValue() + 1));
		}
		
		
		assertTrue(withinTenPercent(((Integer)counts.get(v1.getVariant())).intValue(), (userIds.size()*3)/10));
		assertTrue(withinTenPercent(((Integer)counts.get(v2.getVariant())).intValue(), (userIds.size()*2)/10));
		assertTrue(withinTenPercent(((Integer)counts.get(v3.getVariant())).intValue(), (userIds.size()*5)/10));
		
	}

}
