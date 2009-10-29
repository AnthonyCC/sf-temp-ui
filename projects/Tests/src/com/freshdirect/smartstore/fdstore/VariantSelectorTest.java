package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;

public class VariantSelectorTest extends TestCase {
	
	
	private static class MockService implements RecommendationService {

		private Variant variant;
		
		private MockService(Variant variant) {
			this.variant = variant;
		}
		
		public Variant getVariant() {
			return variant;
		}

		public List<ContentNodeModel> recommendNodes(SessionInput input) {
			return null;
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
		Variant variant = new Variant(id, EnumSiteFeature.DYF, null);
		variant.setRecommender(new MockService(variant));
		return variant;
	}
	
	private List<FDUserI> userIds;
	private Variant v1 = createMockService("v1");
	private Variant v2 = createMockService("v2");
	private Variant v3 = createMockService("v3");
	
	private VariantSelector selector;
	
	public void setUp () {
	    
		Random R = new Random();
		userIds = new ArrayList();
		for(int i=0; i< 10000; ++i) {
			String id = new StringBuffer(4).append((100*((Math.abs(R.nextInt()) % 900) + 100)) + Math.abs(R.nextInt()) % 30).toString();
			userIds.add(new FDUser(new PrimaryKey(id)));
		}
		
                Map cohorts = new HashMap();
                
                final Integer TEN = new Integer(10);
                cohorts.put("A", TEN);
                cohorts.put("C", TEN);
                cohorts.put("E", TEN);
                cohorts.put("F", TEN);
                cohorts.put("G", TEN);
                cohorts.put("B", TEN);
                cohorts.put("H", TEN);
                cohorts.put("D", TEN);
                cohorts.put("J", TEN);
                cohorts.put("I", TEN);
                CohortSelector.setCohorts(cohorts);
                
                CohortSelector.setCohortNames(Arrays.asList(new String[] {"C", "A", "E", "F", "G", "B", "H", "D", "J", "I" } ));

		
		selector = new VariantSelector(EnumSiteFeature.DYF);

                selector.addCohort("C", v1);
                selector.addCohort("A", v1);
                selector.addCohort("C", v1); // DUMMY
                selector.addCohort("E", v2);
                selector.addCohort("F", v3);
                selector.addCohort("G", v3);
                selector.addCohort("B", v1);
                selector.addCohort("H", v3);
                selector.addCohort("D", v2);
                selector.addCohort("J", v3);
                selector.addCohort("I", v3);
	
	}
	
	private static void withinTenPercent(int v1, int v2) {
		System.out.println("actual " + v1 + " expected " + v2);
	    if (Math.abs(v1 - v2) * 10 <  v2) {
	        return ;
	    } else {
	        fail("not within ten percent, actual:"+v1+" expected:"+v2);
	    }
	}
	
	public void testSelectionFrequencies() {
		
		Map counts = new HashMap();
		
		for(Iterator<FDUserI> i = userIds.iterator(); i.hasNext(); ) {
			FDUserI user = i.next();
			Variant v = selector.select(user, true);
			Integer f = (Integer)counts.get(v);
			if (f == null) counts.put(v, new Integer(1));
			else counts.put(v, new Integer(f.intValue() + 1));
		}
		
		
		withinTenPercent(((Integer)counts.get(v1)).intValue(), (userIds.size()*3)/10);
		withinTenPercent(((Integer)counts.get(v2)).intValue(), (userIds.size()*2)/10);
		withinTenPercent(((Integer)counts.get(v3)).intValue(), (userIds.size()*5)/10);
		
	}

}
