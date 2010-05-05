package com.freshdirect.smartstore.fdstore;

import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.smartstore.sampling.RankedContent;

public class CategoryAggregationTest extends SamplingTestsBase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
		
	public void testNoAggregationMarkers() {
		MockedImpressionSampler sampler = MockedImpressionSampler.create("deterministic");

		List<ContentKey> items = sampler.sample(sampler.getCandidates(), false, Collections.EMPTY_SET, false);
		
		assertTrue(items.size() == 8);
		assertEqualsReverse(getLabel(items.get(0)),getLabel(CITROMIZUBANAN));
		assertEqualsReverse(getLabel(items.get(1)),getLabel(EGRES));
		assertEqualsReverse(getLabel(items.get(2)),getLabel(ZOLDALMA));
		assertEqualsReverse(getLabel(items.get(3)),getLabel(MEGGY));
		assertEqualsReverse(getLabel(items.get(4)),getLabel(CSERESZNYE));
		assertEqualsReverse(getLabel(items.get(5)),getLabel(EPER));
		assertEqualsReverse(getLabel(items.get(6)),getLabel(CITROM));
		assertEqualsReverse(getLabel(items.get(7)),getLabel(BANAN));
		
		sampler.resetAggregationMarker(GYUMOLCS);
		sampler.resetAggregationMarker(SARGA_GYUMOLCS);
		sampler.resetAggregationMarker(PIROS_GYUMOLCS);
		sampler.resetAggregationMarker(ZOLD_GYUMOLCS);

		List<RankedContent> items2 = sampler.getAggregatedContentList(sampler.getCandidates());
		
		assertTrue(items2.size() == 8);
		assertEqualsReverse(getLabel(items2.get(0)),getLabel(CITROMIZUBANAN));
		assertEqualsReverse(getLabel(items2.get(1)),getLabel(EGRES));
		assertEqualsReverse(getLabel(items2.get(2)),getLabel(ZOLDALMA));
		assertEqualsReverse(getLabel(items2.get(3)),getLabel(MEGGY));
		assertEqualsReverse(getLabel(items2.get(4)),getLabel(CSERESZNYE));
		assertEqualsReverse(getLabel(items2.get(5)),getLabel(EPER));
		assertEqualsReverse(getLabel(items2.get(6)),getLabel(CITROM));
		assertEqualsReverse(getLabel(items2.get(7)),getLabel(BANAN));
	}
	
	public void testFirtCategoryLevelAggregationMarkers() {
		MockedImpressionSampler sampler = MockedImpressionSampler.create("deterministic");
		
		sampler.resetAggregationMarker(GYUMOLCS);
		sampler.setAggregationMarker(SARGA_GYUMOLCS);
		sampler.setAggregationMarker(PIROS_GYUMOLCS);
		sampler.setAggregationMarker(ZOLD_GYUMOLCS);
		
		List<ContentKey> items = sampler.sample(sampler.getCandidates(), false, Collections.EMPTY_SET, false);

		assertTrue(items.size() == 8);
		assertEqualsReverse(getLabel(items.get(0)),getLabel(CITROMIZUBANAN));
		assertEqualsReverse(getLabel(items.get(1)),getLabel(EGRES));
		assertEqualsReverse(getLabel(items.get(2)),getLabel(ZOLDALMA));
		assertEqualsReverse(getLabel(items.get(3)),getLabel(MEGGY));
		assertEqualsReverse(getLabel(items.get(4)),getLabel(CSERESZNYE));
		assertEqualsReverse(getLabel(items.get(5)),getLabel(EPER));
		assertEqualsReverse(getLabel(items.get(6)),getLabel(CITROM));
		assertEqualsReverse(getLabel(items.get(7)),getLabel(BANAN));
		
		List<RankedContent> items2 = sampler.getAggregatedContentList(sampler.getCandidates());
		
		assertTrue(items2.size() == 4);
		assertEqualsReverse(getLabel(items2.get(0)),getLabel(PIROS_GYUMOLCS));
		assertEqualsReverse(getLabel(items2.get(1)),getLabel(ZOLD_GYUMOLCS));
		assertEqualsReverse(getLabel(items2.get(2)),getLabel(CITROMIZUBANAN));
		assertEqualsReverse(getLabel(items2.get(3)),getLabel(SARGA_GYUMOLCS));

		items = sampler.sample(sampler.getCandidates(), true, Collections.EMPTY_SET, false);

		assertTrue(items.size() == 8);
		assertEqualsReverse(getLabel(items.get(0)),getLabel(MEGGY));
		assertEqualsReverse(getLabel(items.get(1)),getLabel(EGRES));
		assertEqualsReverse(getLabel(items.get(2)),getLabel(CSERESZNYE));
		assertEqualsReverse(getLabel(items.get(3)),getLabel(CITROMIZUBANAN));
		assertEqualsReverse(getLabel(items.get(4)),getLabel(ZOLDALMA));
		assertEqualsReverse(getLabel(items.get(5)),getLabel(EPER));
		assertEqualsReverse(getLabel(items.get(6)),getLabel(CITROM));
		assertEqualsReverse(getLabel(items.get(7)),getLabel(BANAN));
	}
	
	public void testTopCategoryLevelAggregationMarkers() {
		MockedImpressionSampler sampler = MockedImpressionSampler.create("deterministic");
		
		sampler.resetAggregationMarker(SARGA_GYUMOLCS);
		sampler.resetAggregationMarker(PIROS_GYUMOLCS);
		sampler.resetAggregationMarker(ZOLD_GYUMOLCS);
		sampler.setAggregationMarker(GYUMOLCS);
		
		List<ContentKey> items = sampler.sample(sampler.getCandidates(), false, Collections.EMPTY_SET, false);

		assertTrue(items.size() == 8);
		assertEqualsReverse(getLabel(items.get(0)),getLabel(CITROMIZUBANAN));
		assertEqualsReverse(getLabel(items.get(1)),getLabel(EGRES));
		assertEqualsReverse(getLabel(items.get(2)),getLabel(ZOLDALMA));
		assertEqualsReverse(getLabel(items.get(3)),getLabel(MEGGY));
		assertEqualsReverse(getLabel(items.get(4)),getLabel(CSERESZNYE));
		assertEqualsReverse(getLabel(items.get(5)),getLabel(EPER));
		assertEqualsReverse(getLabel(items.get(6)),getLabel(CITROM));
		assertEqualsReverse(getLabel(items.get(7)),getLabel(BANAN));
		
		List<RankedContent> items2 = sampler.getAggregatedContentList(sampler.getCandidates());
		
		assertTrue(items2.size() == 2);
		assertEqualsReverse(getLabel(items2.get(0)),getLabel(GYUMOLCS));
		assertEqualsReverse(getLabel(items2.get(1)),getLabel(CITROMIZUBANAN));

		items = sampler.sample(sampler.getCandidates(), true, Collections.EMPTY_SET, false);

		assertEqualsReverse(getLabel(items.get(0)),getLabel(EGRES));
		assertEqualsReverse(getLabel(items.get(1)),getLabel(ZOLDALMA));
		assertEqualsReverse(getLabel(items.get(2)),getLabel(MEGGY));
		assertEqualsReverse(getLabel(items.get(3)),getLabel(CSERESZNYE));
		assertEqualsReverse(getLabel(items.get(4)),getLabel(EPER));
		assertEqualsReverse(getLabel(items.get(5)),getLabel(CITROMIZUBANAN));
		assertEqualsReverse(getLabel(items.get(6)),getLabel(CITROM));
		assertEqualsReverse(getLabel(items.get(7)),getLabel(BANAN));
	}

	public static void assertEqualsReverse(String actual, String expected) {
		assertEquals(expected, actual);
	}
}
