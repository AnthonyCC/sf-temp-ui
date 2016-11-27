package com.freshdirect.webapp.ajax.viewcart.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.webapp.ajax.browse.data.CarouselData;

public class ViewCartCarouselData {
	
	private List<RecommendationTab> recommendationTabs = new ArrayList<RecommendationTab>();
	
	private ProductSamplesCarousel productSamplesTab = null;

	public List<RecommendationTab> getRecommendationTabs() {
		return recommendationTabs;
	}

	public void setRecommendationTabs(List<RecommendationTab> recommendationTabs) {
		this.recommendationTabs = recommendationTabs;
	}

	public ProductSamplesCarousel getproductSamplesTab() {
		return productSamplesTab;
	}

	public void setProductSamplesTab(ProductSamplesCarousel productSamplesTab) {
		this.productSamplesTab = productSamplesTab;
	}

	
}
