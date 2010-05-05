package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.smartstore.ConfigurationStatus;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.filter.FilterFactory;
import com.freshdirect.smartstore.sampling.AbstractImpressionSampler;
import com.freshdirect.smartstore.sampling.ConsiderationLimit;
import com.freshdirect.smartstore.sampling.ImpressionSampler;
import com.freshdirect.smartstore.sampling.RankedContent;
import com.freshdirect.smartstore.sampling.RankedContent.Single;
import com.freshdirect.smartstore.scoring.MockFilterFactory;
import com.freshdirect.smartstore.service.RecommendationServiceFactory;

public class SamplingTestsBase extends TestCase {
	protected static ContentKey BANAN = new ContentKey(FDContentTypes.PRODUCT, "BANAN");
	protected static ContentKey CITROM = new ContentKey(FDContentTypes.PRODUCT, "CITROM");

	protected static ContentKey EPER = new ContentKey(FDContentTypes.PRODUCT, "EPER");
	protected static ContentKey CSERESZNYE = new ContentKey(FDContentTypes.PRODUCT, "CSERESZNYE");
	protected static ContentKey MEGGY = new ContentKey(FDContentTypes.PRODUCT, "MEGGY");

	protected static ContentKey ZOLDALMA = new ContentKey(FDContentTypes.PRODUCT, "ZOLDALMA");
	protected static ContentKey EGRES = new ContentKey(FDContentTypes.PRODUCT, "EGRES");

	protected static ContentKey SARGA_GYUMOLCS = new ContentKey(FDContentTypes.CATEGORY, "SARGA_GYUMOLCS");
	protected static ContentKey PIROS_GYUMOLCS = new ContentKey(FDContentTypes.CATEGORY, "PIROS_GYUMOLCS");
	protected static ContentKey ZOLD_GYUMOLCS = new ContentKey(FDContentTypes.CATEGORY, "ZOLD_GYUMOLCS");

	protected static ContentKey GYUMOLCS = new ContentKey(FDContentTypes.CATEGORY, "GYUMOLCS");

	protected static ContentKey CITROMIZUBANAN = new ContentKey(FDContentTypes.PRODUCT, "CITROM_IZU_BANAN");

	protected static Map<ContentKey, ContentKey> categoryMap = new HashMap<ContentKey, ContentKey>();

	protected static Map<ContentKey, Double> scoreMap = new HashMap<ContentKey, Double>();

	static {
		scoreMap.put(ZOLDALMA, new Double(170));
		scoreMap.put(EGRES, new Double(180));

		scoreMap.put(BANAN, new Double(-20));
		scoreMap.put(CITROM, new Double(130));

		scoreMap.put(EPER, new Double(140));
		scoreMap.put(MEGGY, new Double(160));
		scoreMap.put(CSERESZNYE, new Double(150));

		scoreMap.put(CITROMIZUBANAN, new Double(190));
	}

	private final static class MockRecommendationServiceConfig extends RecommendationServiceConfig {
		private final String sampling_strat;
		private static final long serialVersionUID = 1L;

		private MockRecommendationServiceConfig(String sampling_strat) {
			super("SEMMI", RecommendationServiceType.NIL);
			this.sampling_strat = sampling_strat;
		}

		public String get(String key) {
			if ("sampling_strat".equals(key)) {
				return sampling_strat;
			} else if ("exponent".equals(key)) {
				return "" + exponent;
			} else if ("cat_aggr".equals(key)) {
				return "true";
			} else {
				return super.get(key);
			}

		}
	}

	protected static double exponent = 0.67;
	protected static String sampling_strat = "exponent";
	protected static List<RankedContent.Single> candidates = new ArrayList<RankedContent.Single>();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
    	FilterFactory.mockInstance(new MockFilterFactory());
    	
        List<ContentTypeServiceI> list = new ArrayList<ContentTypeServiceI>();
        list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

        CompositeTypeService typeService = new CompositeTypeService(list);

        XmlContentService service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/smartstore/SamplingTest.xml");

        CmsManager.setInstance(new CmsManager(service, null));

		candidates.clear();
		for (ContentKey key : scoreMap.keySet()) {
			candidates.add(new RankedContent.Single(key, scoreMap.get(key)));
		}
		Collections.sort(candidates);
	}

	protected static class MockedImpressionSampler implements ImpressionSampler {
		ImpressionSampler mockedSampler;

		private MockedImpressionSampler(ImpressionSampler mockedSampler) {
			this.mockedSampler = mockedSampler;
		}
		
		public static MockedImpressionSampler create(String sampling_strat) {
			ImpressionSampler sampler = RecommendationServiceFactory.configureSampler(new MockRecommendationServiceConfig(sampling_strat), new HashMap<String, ConfigurationStatus>());
			return new MockedImpressionSampler(sampler);
		}
		
		public List<RankedContent.Single> getCandidates() {
			return candidates;
		}
		
		@Override
		public boolean isDeterministic() {
			return mockedSampler.isDeterministic();
		}

		@Override
		public ConsiderationLimit getConsiderationLimit() {
			return mockedSampler.getConsiderationLimit();
		}

		@Override
		public boolean isCategoryAggregationEnabled() {
			return mockedSampler.isCategoryAggregationEnabled();
		}
		
		@Override
		public boolean isUseAlternatives() {
			return mockedSampler.isUseAlternatives();
		}

		@Override
		public List<ContentKey> sample(List<Single> rankedContent, boolean aggregatable, Set<ContentKey> exclusions, boolean showTempUnavailable) {
			return mockedSampler.sample(rankedContent, aggregatable, exclusions, showTempUnavailable);
		}

		public void setAggregationMarker(ContentKey key) {
			ContentNodeI node = CmsManager.getInstance().getContentNode(key);
			node.setAttributeValue("SS_LEVEL_AGGREGATION", Boolean.TRUE);
		}
		SessionInput input = new SessionInput((String) null, null, null) {
		    public boolean isNoShuffle() { return false; }
		};

		public void resetAggregationMarker(ContentKey key) {
			ContentNodeI node = CmsManager.getInstance().getContentNode(key);
			node.setAttributeValue("SS_LEVEL_AGGREGATION", Boolean.FALSE);
		}
		
		public List<RankedContent> getAggregatedContentList(List<RankedContent.Single> rankedContentNodes) {
			return ((AbstractImpressionSampler) mockedSampler).testAggregateContentList(rankedContentNodes);
		}
}

	protected String getLabel(Object o) {
		if (o instanceof RankedContent) {
			return ((RankedContent) o).getId();
		} else if (o instanceof ContentKey) {
			return ((ContentKey) o).getId();
		} else {
			return o.toString();
		}
	}
}
