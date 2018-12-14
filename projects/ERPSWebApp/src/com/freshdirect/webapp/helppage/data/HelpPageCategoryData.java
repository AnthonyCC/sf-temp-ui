package com.freshdirect.webapp.helppage.data;

import java.io.Serializable;

public class HelpPageCategoryData implements Serializable{

    private static final long serialVersionUID = -4126647535314776468L;
    private final String contentName;
    private final String fullName;

    public HelpPageCategoryData(String contentName, String fullName) {
        this.contentName = contentName;
        this.fullName = fullName;
    }

    public String getContentName() {
        return contentName;
    }

    public String getFullName() {
        return fullName;
    }

}
