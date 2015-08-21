package com.freshdirect.fdstore.content;

import java.util.List;

public class CMSImageBannerModel extends CMSComponentModel{
	private CMSImageModel image;
	private List<CMSAnchorModel> anchors;
	private String description;
	private String flagText;
	private String flagColor;
	private String price;
	private String target;
	private String linkOneText;
	private String linkOneTarget;
	private String linkOneURL;
	private String linkOneType;
	private String linkTwoText;
	private String linkTwoTarget;
	private String linkTwoURL;
	private String linkTwoType;
	
	
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
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getLinkOneText() {
		return linkOneText;
	}
	public void setLinkOneText(String linkOneText) {
		this.linkOneText = linkOneText;
	}
	public String getLinkOneTarget() {
		return linkOneTarget;
	}
	public void setLinkOneTarget(String linkOneTarget) {
		this.linkOneTarget = linkOneTarget;
	}
	public String getLinkOneURL() {
		return linkOneURL;
	}
	public void setLinkOneURL(String linkOneURL) {
		this.linkOneURL = linkOneURL;
	}
	public String getLinkOneType() {
		return linkOneType;
	}
	public void setLinkOneType(String linkOneType) {
		this.linkOneType = linkOneType;
	}
	public String getLinkTwoText() {
		return linkTwoText;
	}
	public void setLinkTwoText(String linkTwoText) {
		this.linkTwoText = linkTwoText;
	}
	public String getLinkTwoTarget() {
		return linkTwoTarget;
	}
	public void setLinkTwoTarget(String linkTwoTarget) {
		this.linkTwoTarget = linkTwoTarget;
	}
	public String getLinkTwoURL() {
		return linkTwoURL;
	}
	public void setLinkTwoURL(String linkTwoURL) {
		this.linkTwoURL = linkTwoURL;
	}
	public String getLinkTwoType() {
		return linkTwoType;
	}
	public void setLinkTwoType(String linkTwoType) {
		this.linkTwoType = linkTwoType;
	}
	
	
}