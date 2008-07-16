package com.freshdirect.event;

import java.util.Date;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.event.FDRecommendationEvent;

public class ImpressionEventAggregator extends RecommendationEventAggregator {
	
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
			aggregator = new ImpressionEventAggregator(
					ErpServicesProperties.getImpressionsEntryLimit(),
					ErpServicesProperties.getImpressionsCountLimit());
			aggregator.startTimedFlushes(1000*ErpServicesProperties.getImpressionsFlushSeconds());
		}
		return aggregator;
	}

	public ImpressionEventAggregator(int maxEntries, int maxCount) {
		super(maxEntries, maxCount);
	}

	protected FDRecommendationEvent createInstance(String variantId, String contentId, Date date) {
		
		return new FDRecommendationEvent.Impression(variantId,contentId,date);
	}

	protected Class getEventClass() {
		return FDRecommendationEvent.Impression.class;
	}

}
