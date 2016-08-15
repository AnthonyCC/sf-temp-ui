package com.freshdirect.fdstore.content;

import java.util.List;

public class CMSSectionModel {
	private String name;
	private List<CMSComponentModel> components;
	private List<CMSScheduleModel> schedules;
	private String type;
	private String captionText;
	private String headlineText;
	private String bodyText;
	private String linkText;
	private String linkURL;
	private String linkType;
	private String linkTarget;
	private String displayType;
	private Boolean drawer;
	private CMSImageBannerModel imageBanner;
	private List<CMSPickListModel> pickList;
	private List<String> productList;
	private List<String> mustHaveProdList;
	private List<String> categoryList;
	
	public List<CMSComponentModel> getComponents() {
		return components;
	}

	public void setComponents(List<CMSComponentModel> components) {
		this.components = components;
	}

	public List<CMSScheduleModel> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<CMSScheduleModel> schedules) {
		this.schedules = schedules;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaptionText() {
		return captionText;
	}

	public void setCaptionText(String captionText) {
		this.captionText = captionText;
	}

	public String getHeadlineText() {
		return headlineText;
	}

	public void setHeadlineText(String headlineText) {
		this.headlineText = headlineText;
	}

	public String getBodyText() {
		return bodyText;
	}

	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public String getLinkURL() {
		return linkURL;
	}

	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public Boolean getDrawer() {
		return drawer;
	}

	public void setDrawer(Boolean drawer) {
		this.drawer = drawer;
	}

	public CMSImageBannerModel getImageBanner() {
		return imageBanner;
	}

	public void setImageBanner(CMSImageBannerModel imageBanner) {
		this.imageBanner = imageBanner;
	}
	
	public List<CMSPickListModel> getPickList() {
		return pickList;
	}

	public void setPickList(List<CMSPickListModel> pickList) {
		this.pickList = pickList;
	}

	public List<String> getProductList() {
		return productList;
	}

	public void setProductList(List<String> productList) {
		this.productList = productList;
	}
	
	public List<String> getMustHaveProdList() {
		return mustHaveProdList;
	}

	public void setMustHaveProdList(List<String> mustHaveProdList) {
		this.mustHaveProdList = mustHaveProdList;
	}

	public List<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}

	public String getLinkTarget() {
		return linkTarget;
	}

	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}
	
	
	
}
