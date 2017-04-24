package com.freshdirect.webapp.ajax.modulehandling.data;

import java.io.Serializable;

public class IconData implements Serializable {

    private static final long serialVersionUID = 613186788376682464L;

    private String iconImage;
    private String iconLink;
    private String iconLinkText;
    private String iconId;

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getIconLinkText() {
        return iconLinkText;
    }

    public void setIconLinkText(String iconLinkText) {
        this.iconLinkText = iconLinkText;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

}
