package com.freshdirect.webapp.ajax.viewcart.data;

import java.util.ArrayList;
import java.util.List;

public class ViewCartCarouselData {

	private List<RecommendationTab> recommendationTabs = new ArrayList<RecommendationTab>();

    public void addRecommendationTab(RecommendationTab tab) {
	    recommendationTabs.add(tab);
	}

	public List<RecommendationTab> getRecommendationTabs() {
		return recommendationTabs;
	}

	public void setRecommendationTabs(List<RecommendationTab> recommendationTabs) {
		this.recommendationTabs = recommendationTabs;
	}
}
