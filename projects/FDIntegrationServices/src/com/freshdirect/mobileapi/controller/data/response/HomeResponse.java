package com.freshdirect.mobileapi.controller.data.response;

import java.util.LinkedHashMap;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.BrowseResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.FeaturedCategory;

public class HomeResponse extends Message {
	
	//Fields from getAll response
	private List<Idea> carouselItems;
	private List<Idea> shops;
	

	//Featured Categories contain the categories and the products.
	private List<FeaturedCategory> featuredCategories;
	

	public List<Idea> getCarouselItems() {
		return carouselItems;
	}

	public void setCarouselItems(List<Idea> carouselItems) {
		this.carouselItems = carouselItems;
	}

	public List<Idea> getShops() {
		return shops;
	}

	public void setShops(List<Idea> shops) {
		this.shops = shops;
	}

	public List<FeaturedCategory> getFeaturedCategories() {
		return featuredCategories;
	}

	public void setFeaturedCategories(List<FeaturedCategory> featuredCategories) {
		this.featuredCategories = featuredCategories;
	}

	

	

	

	
	
	
	

}
