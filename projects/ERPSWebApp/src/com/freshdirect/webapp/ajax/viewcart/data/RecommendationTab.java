package com.freshdirect.webapp.ajax.viewcart.data;

import com.freshdirect.webapp.ajax.browse.data.CarouselData;

public class RecommendationTab {

	private String title;
	private String siteFeature;
	private CarouselData carouselData;
	private String parentImpressionId;
	private String impressionId;
	private String parentVariantId;
	
	public RecommendationTab(String title, String siteFeature, String parentImpressionId, String impressionId, String parentVariantId) {
		this.title = title;
		this.siteFeature = siteFeature;
		this.parentImpressionId = parentImpressionId;
		this.impressionId = impressionId;
		this.parentVariantId = parentVariantId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSiteFeature() {
		return siteFeature;
	}

	public void setSiteFeature(String siteFeature) {
		this.siteFeature = siteFeature;
	}

	public CarouselData getCarouselData() {
		return carouselData;
	}

	public void setCarouselData(CarouselData carouselData) {
		this.carouselData = carouselData;
	}

	public String getParentImpressionId() {
		return parentImpressionId;
	}

	public void setParentImpressionId(String parentImpressionId) {
		this.parentImpressionId = parentImpressionId;
	}

	public String getImpressionId() {
		return impressionId;
	}

	public void setImpressionId(String impressionId) {
		this.impressionId = impressionId;
	}

	public String getParentVariantId() {
		return parentVariantId;
	}

	public void setParentVariantId(String parentVariantId) {
		this.parentVariantId = parentVariantId;
	}
}
