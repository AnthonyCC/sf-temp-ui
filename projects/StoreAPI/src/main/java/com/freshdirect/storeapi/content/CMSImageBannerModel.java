package com.freshdirect.storeapi.content;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((anchors == null) ? 0 : anchors.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((flagColor == null) ? 0 : flagColor.hashCode());
        result = prime * result + ((flagText == null) ? 0 : flagText.hashCode());
        result = prime * result + ((image == null) ? 0 : image.hashCode());
        result = prime * result + ((linkOneTarget == null) ? 0 : linkOneTarget.hashCode());
        result = prime * result + ((linkOneText == null) ? 0 : linkOneText.hashCode());
        result = prime * result + ((linkOneType == null) ? 0 : linkOneType.hashCode());
        result = prime * result + ((linkOneURL == null) ? 0 : linkOneURL.hashCode());
        result = prime * result + ((linkTwoTarget == null) ? 0 : linkTwoTarget.hashCode());
        result = prime * result + ((linkTwoText == null) ? 0 : linkTwoText.hashCode());
        result = prime * result + ((linkTwoType == null) ? 0 : linkTwoType.hashCode());
        result = prime * result + ((linkTwoURL == null) ? 0 : linkTwoURL.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CMSImageBannerModel other = (CMSImageBannerModel) obj;
        if (anchors == null) {
            if (other.anchors != null)
                return false;
        } else if (!anchors.equals(other.anchors))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (flagColor == null) {
            if (other.flagColor != null)
                return false;
        } else if (!flagColor.equals(other.flagColor))
            return false;
        if (flagText == null) {
            if (other.flagText != null)
                return false;
        } else if (!flagText.equals(other.flagText))
            return false;
        if (image == null) {
            if (other.image != null)
                return false;
        } else if (!image.equals(other.image))
            return false;
        if (linkOneTarget == null) {
            if (other.linkOneTarget != null)
                return false;
        } else if (!linkOneTarget.equals(other.linkOneTarget))
            return false;
        if (linkOneText == null) {
            if (other.linkOneText != null)
                return false;
        } else if (!linkOneText.equals(other.linkOneText))
            return false;
        if (linkOneType == null) {
            if (other.linkOneType != null)
                return false;
        } else if (!linkOneType.equals(other.linkOneType))
            return false;
        if (linkOneURL == null) {
            if (other.linkOneURL != null)
                return false;
        } else if (!linkOneURL.equals(other.linkOneURL))
            return false;
        if (linkTwoTarget == null) {
            if (other.linkTwoTarget != null)
                return false;
        } else if (!linkTwoTarget.equals(other.linkTwoTarget))
            return false;
        if (linkTwoText == null) {
            if (other.linkTwoText != null)
                return false;
        } else if (!linkTwoText.equals(other.linkTwoText))
            return false;
        if (linkTwoType == null) {
            if (other.linkTwoType != null)
                return false;
        } else if (!linkTwoType.equals(other.linkTwoType))
            return false;
        if (linkTwoURL == null) {
            if (other.linkTwoURL != null)
                return false;
        } else if (!linkTwoURL.equals(other.linkTwoURL))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (target == null) {
            if (other.target != null)
                return false;
        } else if (!target.equals(other.target))
            return false;
        return true;
    }
	
}