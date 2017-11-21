package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.storeapi.content.CMSImageBannerModel;

public class PicksListResponse extends Message {
	private Image bannerImage;
	private List<Idea> picksList;
	private List<CMSImageBannerModel> iPhoneHomePageImageBanners;
	 
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
	
	public List<CMSImageBannerModel> getiPhoneHomePageImageBanners() {
        return iPhoneHomePageImageBanners;
    }

    public void setiPhoneHomePageImageBanners(List<CMSImageBannerModel> iPhoneHomePageImageBanners) {
        this.iPhoneHomePageImageBanners = iPhoneHomePageImageBanners;
    }
}