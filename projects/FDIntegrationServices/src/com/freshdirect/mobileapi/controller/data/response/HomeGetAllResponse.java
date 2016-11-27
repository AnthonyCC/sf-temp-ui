package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class HomeGetAllResponse extends Message {
	private List<Idea> carouselItems;
	private List<Idea> shops;
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
}
