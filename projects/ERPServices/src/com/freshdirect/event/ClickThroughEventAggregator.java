package com.freshdirect.event;

import java.util.Date;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.event.FDRecommendationEvent;

public class ClickThroughEventAggregator extends RecommendationEventAggregator {
	
private static RecommendationEventAggregator aggregator;
	
	/**
	 * Get the impression aggregator instance.
	 * 
	 * If the aggregator instance does not exist, it will create one and will
	 * start its timed flushes.
	 * @return aggregator
	 */
	public static synchronized RecommendationEventAggregator getInstance() {
		if (aggregator == null) {
			aggregator = new ClickThroughEventAggregator(
					ErpServicesProperties.getClickThroughsEntryLimit(),
					ErpServicesProperties.getClickThroughsCountLimit());
			aggregator.startTimedFlushes(1000*ErpServicesProperties.getClickThroughsFlushSeconds());
		}
		return aggregator;
	}

	public ClickThroughEventAggregator(int maxEntries, int maxCount) {
		super(maxEntries, maxCount);
	}

	protected FDRecommendationEvent createInstance(String variantId, String contentId, Date date) {
		return new FDRecommendationEvent.ClickThrough(variantId,contentId,date);
	}

	protected Class getEventClass() {
		return FDRecommendationEvent.ClickThrough.class;
	}

}
