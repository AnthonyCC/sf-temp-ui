package com.freshdirect.smartstore.fdstore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.smartstore.sampling.RankedContent;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;

public class SamplingTestsBase extends TestCase {

	protected static ContentKey BANAN = new ContentKey(FDContentTypes.PRODUCT,"BANAN");
	protected static ContentKey CITROM = new ContentKey(FDContentTypes.PRODUCT,"CITROM");
	
	protected static ContentKey EPER = new ContentKey(FDContentTypes.PRODUCT,"EPER");
	protected static ContentKey CSERESZNYE = new ContentKey(FDContentTypes.PRODUCT,"CSERESZNYE");
	protected static ContentKey MEGGY = new ContentKey(FDContentTypes.PRODUCT,"MEGGY");
	
	protected static ContentKey ZOLDALMA = new ContentKey(FDContentTypes.PRODUCT,"ZOLDALMA");
	protected static ContentKey EGRES = new ContentKey(FDContentTypes.PRODUCT,"EGRES");
	
	protected static ContentKey SARGA_GYUMOLCS = new ContentKey(FDContentTypes.CATEGORY,"SARGA_GYUMOLCS");
	protected static ContentKey PIROS_GYUMOLCS = new ContentKey(FDContentTypes.CATEGORY,"PIROS_GYUMOLCS");
	protected static ContentKey ZOLD_GYUMOLCS = new ContentKey(FDContentTypes.CATEGORY,"ZOLD_GYUMOLCS");
	
	protected static ContentKey GYUMOLCS = new ContentKey(FDContentTypes.CATEGORY,"GYUMOLCS");
	
	protected static ContentKey CITROMIZUBANAN = new ContentKey(FDContentTypes.PRODUCT,"CITROM IZU BANAN");

	protected static Map categoryMap = new HashMap();
	
	static {
		categoryMap.put(BANAN, SARGA_GYUMOLCS);
		categoryMap.put(CITROM, SARGA_GYUMOLCS);
		
		categoryMap.put(EPER, PIROS_GYUMOLCS);
		categoryMap.put(CSERESZNYE, PIROS_GYUMOLCS);
		categoryMap.put(MEGGY, PIROS_GYUMOLCS);
		
		categoryMap.put(ZOLDALMA, ZOLD_GYUMOLCS);
		categoryMap.put(EGRES, ZOLD_GYUMOLCS);
		
		categoryMap.put(SARGA_GYUMOLCS, GYUMOLCS);
		categoryMap.put(PIROS_GYUMOLCS, GYUMOLCS);
		categoryMap.put(ZOLD_GYUMOLCS, GYUMOLCS);
	}
	
	protected static Map scoreMap = new HashMap();
	
	static {
		scoreMap.put(BANAN, new Double(130));
		scoreMap.put(CITROM, new Double(140));
		
		scoreMap.put(EPER, new Double(150));
		scoreMap.put(CSERESZNYE, new Double(160));
		scoreMap.put(MEGGY, new Double(170));
		
		scoreMap.put(ZOLDALMA, new Double(180));
		scoreMap.put(EGRES, new Double(190));
		
		scoreMap.put(CITROMIZUBANAN, new Double(-20));
	}

	protected static double exponent = 0.67;		
	
	protected class MockRecommendationService extends AbstractRecommendationService {
		
		public MockRecommendationService(final String sampling_strat) {
			super(new Variant("test",EnumSiteFeature.DYF,new RecommendationServiceConfig("SEMMI",RecommendationServiceType.NIL) {
				
				private static final long serialVersionUID = 1L;

				public String get(String key) {
					if ("sampling_strat".equals(key)) {
						return sampling_strat;
					} else if ("exponent".equals(key)) {
						return "" + exponent;
					} else {
						return super.get(key);			
					}
					
				}
			}), RecommendationServiceFactory.configureSampler(new RecommendationServiceConfig("SEMMI",RecommendationServiceType.NIL) {
				
				private static final long serialVersionUID = 1L;

				public String get(String key) {
					if ("sampling_strat".equals(key)) {
						return sampling_strat;
					} else if ("exponent".equals(key)) {
						return "" + exponent;
					} else {
						return super.get(key);			
					}
					
				}
			}, new java.util.HashMap()), false, false);		
		}
		
		protected Set markers = new HashSet();
		
		public void clearCategoryMarkers() {
			markers.clear();
		}
		
		public void setCategoryMarker(ContentKey key) {
			markers.add(key);
		}
		
		protected ContentKey getAggregationKey(ContentKey key) {
			ContentKey result = key;
			while(key != null) {
				if (markers.contains(key)) result = key;
				key = (ContentKey)categoryMap.get(key);
			}
			return result;
		}
		
		public List getCandidates(boolean aggregate) {
			return super.createSortedRankedContentList(scoreMap, aggregate);
		}
		
		public void setAggregation(boolean value) {
			aggregateAtCategoryLevel = value;
		}

		public List doRecommendNodes(SessionInput input) {
			return null;
		}

		SessionInput input = new SessionInput((String) null, null, null) {
		    public boolean isNoShuffle() { return false; }
		};

		public List recommend() {
			return RankedContent.getKeys(getSampler(input).sample(getCandidates(aggregateAtCategoryLevel), new HashSet(), Integer.MAX_VALUE));
		}
	}
	
	protected String getLabel(Object o) {
		if (o instanceof RankedContent) {
			return ((RankedContent)o).getId();
		} else if (o instanceof ContentKey) {
			return ((ContentKey)o).getId();
		} else {
			return o.toString();
		}
	}
}
