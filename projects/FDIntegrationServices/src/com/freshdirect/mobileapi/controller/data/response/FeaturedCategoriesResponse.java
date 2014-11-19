package com.freshdirect.mobileapi.controller.data.response;

import java.util.Collections;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.Category;

public class FeaturedCategoriesResponse extends Message {

	private Image homescreenBanner;
	private List<Category> featuredCategories = Collections.emptyList();

	public List<Category> getFeaturedCategories() {
		return featuredCategories;
	}

	public void setFeaturedCategories(List<Category> featuredCategories) {
		this.featuredCategories = featuredCategories;
	}

	public Image getHomescreenBanner() {
		return homescreenBanner;
	}

	public void setHomescreenBanner(Image homescreenBanner) {
		this.homescreenBanner = homescreenBanner;
	}
}
