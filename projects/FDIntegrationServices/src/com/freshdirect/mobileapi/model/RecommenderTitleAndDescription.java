package com.freshdirect.mobileapi.model;


public class RecommenderTitleAndDescription {

    private String title;

    private String description;

    public RecommenderTitleAndDescription() {
    }

    public RecommenderTitleAndDescription(String title, String description) {
        this.title = title;
        this.description = description;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

}
