package com.freshdirect.storeapi.content;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bodyText == null) ? 0 : bodyText.hashCode());
        result = prime * result + ((captionText == null) ? 0 : captionText.hashCode());
        result = prime * result + ((categoryList == null) ? 0 : categoryList.hashCode());
        result = prime * result + ((components == null) ? 0 : components.hashCode());
        result = prime * result + ((displayType == null) ? 0 : displayType.hashCode());
        result = prime * result + ((drawer == null) ? 0 : drawer.hashCode());
        result = prime * result + ((headlineText == null) ? 0 : headlineText.hashCode());
        result = prime * result + ((imageBanner == null) ? 0 : imageBanner.hashCode());
        result = prime * result + ((linkTarget == null) ? 0 : linkTarget.hashCode());
        result = prime * result + ((linkText == null) ? 0 : linkText.hashCode());
        result = prime * result + ((linkType == null) ? 0 : linkType.hashCode());
        result = prime * result + ((linkURL == null) ? 0 : linkURL.hashCode());
        result = prime * result + ((mustHaveProdList == null) ? 0 : mustHaveProdList.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((pickList == null) ? 0 : pickList.hashCode());
        result = prime * result + ((productList == null) ? 0 : productList.hashCode());
        result = prime * result + ((schedules == null) ? 0 : schedules.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        CMSSectionModel other = (CMSSectionModel) obj;
        if (bodyText == null) {
            if (other.bodyText != null)
                return false;
        } else if (!bodyText.equals(other.bodyText))
            return false;
        if (captionText == null) {
            if (other.captionText != null)
                return false;
        } else if (!captionText.equals(other.captionText))
            return false;
        if (categoryList == null) {
            if (other.categoryList != null)
                return false;
        } else if (!categoryList.equals(other.categoryList))
            return false;
        if (components == null) {
            if (other.components != null)
                return false;
        } else if (!components.equals(other.components))
            return false;
        if (displayType == null) {
            if (other.displayType != null)
                return false;
        } else if (!displayType.equals(other.displayType))
            return false;
        if (drawer == null) {
            if (other.drawer != null)
                return false;
        } else if (!drawer.equals(other.drawer))
            return false;
        if (headlineText == null) {
            if (other.headlineText != null)
                return false;
        } else if (!headlineText.equals(other.headlineText))
            return false;
        if (imageBanner == null) {
            if (other.imageBanner != null)
                return false;
        } else if (!imageBanner.equals(other.imageBanner))
            return false;
        if (linkTarget == null) {
            if (other.linkTarget != null)
                return false;
        } else if (!linkTarget.equals(other.linkTarget))
            return false;
        if (linkText == null) {
            if (other.linkText != null)
                return false;
        } else if (!linkText.equals(other.linkText))
            return false;
        if (linkType == null) {
            if (other.linkType != null)
                return false;
        } else if (!linkType.equals(other.linkType))
            return false;
        if (linkURL == null) {
            if (other.linkURL != null)
                return false;
        } else if (!linkURL.equals(other.linkURL))
            return false;
        if (mustHaveProdList == null) {
            if (other.mustHaveProdList != null)
                return false;
        } else if (!mustHaveProdList.equals(other.mustHaveProdList))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (pickList == null) {
            if (other.pickList != null)
                return false;
        } else if (!pickList.equals(other.pickList))
            return false;
        if (productList == null) {
            if (other.productList != null)
                return false;
        } else if (!productList.equals(other.productList))
            return false;
        if (schedules == null) {
            if (other.schedules != null)
                return false;
        } else if (!schedules.equals(other.schedules))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
	
}
