package com.freshdirect.smartstore.fdstore;

import java.util.List;

public class CategoryAggregationTest extends SamplingTestsBase {

	
	public void testNoAggregationMarkers() {
		MockRecommendationService service = new MockRecommendationService("semmi");
		List items = service.getCandidates(false);
		
		assertTrue(items.size() == 7);
		assertEquals(getLabel(items.get(0)),getLabel(EGRES));
		assertEquals(getLabel(items.get(1)),getLabel(ZOLDALMA));
		assertEquals(getLabel(items.get(2)),getLabel(MEGGY));
		assertEquals(getLabel(items.get(3)),getLabel(CSERESZNYE));
		assertEquals(getLabel(items.get(4)),getLabel(EPER));
		assertEquals(getLabel(items.get(5)),getLabel(CITROM));
		assertEquals(getLabel(items.get(6)),getLabel(BANAN));
		
		items = service.getCandidates(true);
		
		assertTrue(items.size() == 7);
		assertEquals(getLabel(items.get(0)),getLabel(EGRES));
		assertEquals(getLabel(items.get(1)),getLabel(ZOLDALMA));
		assertEquals(getLabel(items.get(2)),getLabel(MEGGY));
		assertEquals(getLabel(items.get(3)),getLabel(CSERESZNYE));
		assertEquals(getLabel(items.get(4)),getLabel(EPER));
		assertEquals(getLabel(items.get(5)),getLabel(CITROM));
		assertEquals(getLabel(items.get(6)),getLabel(BANAN));
	}
	
	public void testFirtCategoryLevelAggregationMarkers() {
		MockRecommendationService service = new MockRecommendationService("semmi");
		
		service.setCategoryMarker(SARGA_GYUMOLCS);
		service.setCategoryMarker(PIROS_GYUMOLCS);
		service.setCategoryMarker(ZOLD_GYUMOLCS);
		
		List items = service.getCandidates(false);
		assertTrue(items.size() == 7);
		assertEquals(getLabel(items.get(0)),getLabel(EGRES));
		assertEquals(getLabel(items.get(1)),getLabel(ZOLDALMA));
		assertEquals(getLabel(items.get(2)),getLabel(MEGGY));
		assertEquals(getLabel(items.get(3)),getLabel(CSERESZNYE));
		assertEquals(getLabel(items.get(4)),getLabel(EPER));
		assertEquals(getLabel(items.get(5)),getLabel(CITROM));
		assertEquals(getLabel(items.get(6)),getLabel(BANAN));
		
		items = service.getCandidates(true);
		
		assertTrue(items.size() == 3);
		assertEquals(getLabel(items.get(0)), getLabel(PIROS_GYUMOLCS));
		assertEquals(getLabel(items.get(1)), getLabel(ZOLD_GYUMOLCS));
		assertEquals(getLabel(items.get(2)), getLabel(SARGA_GYUMOLCS));
	}
	
	public void testTopCategoryLevelAggregationMarkers() {
		MockRecommendationService service = new MockRecommendationService("semmi");
		
		service.setCategoryMarker(GYUMOLCS);
		
		List items = service.getCandidates(false);
		assertTrue(items.size() == 7);
		assertEquals(getLabel(items.get(0)),getLabel(EGRES));
		assertEquals(getLabel(items.get(1)),getLabel(ZOLDALMA));
		assertEquals(getLabel(items.get(2)),getLabel(MEGGY));
		assertEquals(getLabel(items.get(3)),getLabel(CSERESZNYE));
		assertEquals(getLabel(items.get(4)),getLabel(EPER));
		assertEquals(getLabel(items.get(5)),getLabel(CITROM));
		assertEquals(getLabel(items.get(6)),getLabel(BANAN));
		
		items = service.getCandidates(true);
		
		assertTrue(items.size() == 1);
		assertEquals(getLabel(items.get(0)), getLabel(GYUMOLCS));
		
	}
}
