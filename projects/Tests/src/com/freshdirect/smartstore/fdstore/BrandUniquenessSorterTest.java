package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.core.MockProductModel;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.impl.BrandUniquenessSorter;

public class BrandUniquenessSorterTest extends TestCase {
	private static Category LOGGER = LoggerFactory.getInstance(BrandUniquenessSorterTest.class);

	
	List<BrandModel> brands;
	List<ProductModel> products;
	
	
    public void setUp() throws Exception {
        super.setUp();
        EnumSiteFeature.mock();
    }

    
    private SessionInput prepareSessionInput(int cnt) {
		SessionInput inp = new SessionInput("123", EnumServiceType.DEPOT, PricingContext.DEFAULT);
		inp.setPrioritizedCount(cnt);
		
		return inp;
    }
    
	
	public void testBasic() throws InvalidContentKeyException {
		// normal lists (no preceeding priority items)
		testBUS(
				new String[] {"A", "A", "A", "A", "A"},
				new String[] {"A", "A", "A", "A", "A"},
				0);
		testBUS(
				new String[] {"A", "A", "A", "B", "B"},
				new String[] {"A", "B", "A", "B", "A"},
				0);
		testBUS(
				new String[] {"A", "A", "B", "B", "C"},
				new String[] {"A", "B", "C", "A", "B"},
				0);
		testBUS(
				new String[] {"A", "A", "B", "C", "B"},
				new String[] {"A", "B", "C", "A", "B"},
				0);
		testBUS(
				new String[] {"A", "B", "C", "D", "E"},
				new String[] {"A", "B", "C", "D", "E"},
				0);
	}

	
	public void testPriorityItems() {
		// The three untouchable "A" priority items
		testBUS(
				new String[] {"A", "A", "A", "A", "A", "A", "A", "A"},
				new String[] {"A", "A", "A", "A", "A", "A", "A", "A"},
				3);
		testBUS(
				new String[] {"A", "A", "A", "A", "A", "A", "B", "B"},
				new String[] {"A", "A", "A", "B", "A", "A", "A", "B"},
				3);
		testBUS(
				new String[] {"A", "A", "A", "A", "A", "B", "B", "C"},
				new String[] {"A", "A", "A", "B", "C", "A", "A", "B"},
				3);
		testBUS(
				new String[] {"A", "A", "A", "A", "A", "B", "C", "B"},
				new String[] {"A", "A", "A", "B", "C", "A", "A", "B"},
				3);
		testBUS(
				new String[] {"A", "A", "A", "A", "B", "C", "D", "E"},
				new String[] {"A", "A", "A", "B", "C", "D", "E", "A"},
				3);
	}



	/**
	 * Test brand uniqueness sorter
	 * 
	 * @param initBrandNames Input list of brand names
	 * @param testBrandNames Expected result.
	 * @param cnt First N items are untouchables
	 */
	private void testBUS(String initBrandNames[], String testBrandNames[], int cnt) {
		Map<String,BrandModel> brandz = new HashMap<String,BrandModel>();
		
		assertTrue("Out of range", cnt >= 0);
		
		assertNotNull("Empty brand names", initBrandNames);
		assertNotNull("Empty test brand names", testBrandNames);
		
		assertEquals("Inequal lists", initBrandNames.length, testBrandNames.length);
		
		for (int i=0; i<initBrandNames.length; i++) {
			if (!brandz.keySet().contains(initBrandNames[i]) ) {
				try {
					brandz.put(initBrandNames[i], new BrandModel(ContentKey.create(FDContentTypes.BRAND, initBrandNames[i] )) );
				} catch (InvalidContentKeyException e) {
					LOGGER.error(e);
				}
			}
		}

		// check test keys - they must already exist in init list
		for (int i=0; i<testBrandNames.length; i++) {
			assertTrue("", brandz.keySet().contains(testBrandNames[i]));
		}
		
		
		// create test products
		final List<ContentNodeModel> prds = new ArrayList<ContentNodeModel>();
		for (int k=0; k<testBrandNames.length; k++) {
	        MockProductModel p = new MockProductModel("test", "prd"+k);
	        p.addBrand( brandz.get(initBrandNames[k]) );
	        prds.add(p);
		}
		
		BrandUniquenessSorter bus = new BrandUniquenessSorter(new X(prds));
		final List<ContentNodeModel> result = bus.recommendNodes(prepareSessionInput(cnt));
		
		assertTrue(result.size() <= initBrandNames.length);
		final int min = Math.min(result.size(), initBrandNames.length);
		
		debugArray("Init set ", initBrandNames);
		debugArray("Expected Test set ", testBrandNames);
		
		String resultList[] = new String[min];
		for (int i=0; i<min; i++) {
			resultList[i] = ((ProductModel) result.get(i)).getBrands().get(0).getContentName();
		}
		debugArray("Result set ", resultList);
		
		for (int i=0; i<min; i++) {
			ProductModel prd = (ProductModel) result.get(i);
			assertEquals("Brands do not match at position "+(i+1), brandz.get(testBrandNames[i]), prd.getBrands().get(0));
		}
	}
	
	private void debugArray(String msg, String x[]) {
		StringBuilder b = new StringBuilder(msg);
		b.append(": ");
		for (int k=0; k<x.length; k++) {
			b.append(x[k].toString());
			if (k<x.length-1)
				b.append(", ");
		}
		LOGGER.debug(b.toString());
	}
}






class X implements RecommendationService {
	private static Variant v = new Variant("valami", EnumSiteFeature.DYF, null);
	
	List<ContentNodeModel> nodes = null;
	
	public X() {
	}

	public X(List<ContentNodeModel> nodes) {
		this.nodes = nodes;
	}

	@Override
	public Variant getVariant() {
		return v;
	}

	@Override
	public boolean isIncludeCartItems() {
		return false;
	}

	@Override
	public boolean isRefreshable() {
		return false;
	}

	@Override
	public boolean isSmartSavings() {
		return false;
	}

	@Override
	public List<ContentNodeModel> recommendNodes(SessionInput input) {
		return nodes;
	}
	
	public void setNodes(List<ContentNodeModel> nodes) {
		this.nodes = nodes;
	}
}