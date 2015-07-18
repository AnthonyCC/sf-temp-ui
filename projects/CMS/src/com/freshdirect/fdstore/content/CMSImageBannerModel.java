package com.freshdirect.fdstore.content;

import java.util.List;

public class CMSImageBannerModel extends CMSComponentModel{
	private CMSImageModel image;
	private List<CMSAnchorModel> anchors;
	private String description;
	private String flagText;
	private String flagColor;
	private String price;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFlagText() {
		return flagText;
	}
	public void setFlagText(String flagText) {
		this.flagText = flagText;
	}
	public String getFlagColor() {
		return flagColor;
	}
	public void setFlagColor(String flagColor) {
		this.flagColor = flagColor;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public CMSImageModel getImage() {
		return image;
	}
	public void setImage(CMSImageModel image) {
		this.image = image;
	}
	public List<CMSAnchorModel> getAnchors() {
		return anchors;
	}
	public void setAnchors(List<CMSAnchorModel> anchors) {
		this.anchors = anchors;
	}
}