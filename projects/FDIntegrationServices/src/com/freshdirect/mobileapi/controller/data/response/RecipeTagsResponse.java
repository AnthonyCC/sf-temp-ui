package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Message;

public class RecipeTagsResponse extends Message {
	private Image bannerImage; 
	private List<Idea> recipeTags;
	public Image getBannerImage() {
		return bannerImage;
	}
	public void setBannerImage(Image bannerImage) {
		this.bannerImage = bannerImage;
	}
	public List<Idea> getRecipeTags() {
		return recipeTags;
	}
	public void setRecipeTags(List<Idea> recipeTags) {
		this.recipeTags = recipeTags;
	} 
}
