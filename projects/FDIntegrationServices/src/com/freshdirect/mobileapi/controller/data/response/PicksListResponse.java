package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;

public class PicksListResponse extends Message {
	private Image bannerImage;
	private List<Idea> picksList;
	public Image getBannerImage() {
		return bannerImage;
	}
	public void setBannerImage(Image bannerImage) {
		this.bannerImage = bannerImage;
	}
	public List<Idea> getPicksList() {
		return picksList;
	}
	public void setPicksList(List<Idea> picksList) {
		this.picksList = picksList;
	}
}