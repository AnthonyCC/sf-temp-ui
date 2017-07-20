package com.freshdirect.webapp.ajax.viewcart.data;

import com.freshdirect.smartstore.CarouselItemType;
import com.freshdirect.webapp.ajax.AbstractCarouselService;
import com.freshdirect.webapp.ajax.browse.data.CarouselData;

public class RecommendationTab {

	private String title;
    private String description;
	private String siteFeature;
	private CarouselData carouselData;
	private String parentImpressionId;
	private String impressionId;
	private String parentVariantId;
    private String itemType;
    private boolean productSamplesReacedMaximumItemQuantity;
    private boolean selected;
    private boolean updateContent = true;

    public RecommendationTab(String title, String siteFeature) {
		this.title = title;
		this.siteFeature = siteFeature;
        this.itemType = getItemType(siteFeature);
    }

    private String getItemType(String siteFeature) {
        String itemType = CarouselItemType.GRID.getType();
        if (AbstractCarouselService.isSample(siteFeature)) {
            itemType = CarouselItemType.PRODUCT_SAMPLE_GRID.getType();
        }
        return itemType;
    }

	public String getTitle() {
		return title;
	}

    public RecommendationTab setTitle(String title) {
		this.title = title;
        return this;
	}

	public String getSiteFeature() {
		return siteFeature;
	}

    public RecommendationTab setSiteFeature(String siteFeature) {
		this.siteFeature = siteFeature;
        return this;
	}

	public CarouselData getCarouselData() {
		return carouselData;
	}

    public RecommendationTab setCarouselData(CarouselData carouselData) {
		this.carouselData = carouselData;
        return this;
	}

	public String getParentImpressionId() {
		return parentImpressionId;
	}

    public RecommendationTab setParentImpressionId(String parentImpressionId) {
		this.parentImpressionId = parentImpressionId;
        return this;
	}

	public String getImpressionId() {
		return impressionId;
	}

    public RecommendationTab setImpressionId(String impressionId) {
		this.impressionId = impressionId;
        return this;
	}

	public String getParentVariantId() {
		return parentVariantId;
	}

    public RecommendationTab setParentVariantId(String parentVariantId) {
		this.parentVariantId = parentVariantId;
        return this;
	}
	
    public boolean isProductSamplesReacedMaximumItemQuantity() {
        return productSamplesReacedMaximumItemQuantity;
    }

    public RecommendationTab setProductSamplesReacedMaximumItemQuantity(boolean productSamplesReacedMaximumItemQuantity) {
        this.productSamplesReacedMaximumItemQuantity = productSamplesReacedMaximumItemQuantity;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public RecommendationTab setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public RecommendationTab setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RecommendationTab setDescription(String description) {
        if (AbstractCarouselService.isSample(siteFeature)) {
            this.description = description;
        }
        return this;
    }

    public boolean isUpdateContent() {
        return updateContent;
    }

    public RecommendationTab setUpdateContent(boolean updateContent) {
        this.updateContent = updateContent;
        return this;
    }

}
