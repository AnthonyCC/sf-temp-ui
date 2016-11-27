package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Information specific for wine products.
 * @author fgarcia
 *
 */
public class WineProductInfo {
    /**
     * Wine review
     */
    private String review;

    /**
     * List of wine type icons. e.g. Red/White, Kosher, etc
     */
    private List<Image> typeIcons = new ArrayList<Image>();

    /**
     * Region label
     */
    private String regionLabel;

    /**
     * Region description
     */
    private String regionDescription;

    /**
     * Grape used on wine production
     */
    private String grape;

    /**
     * Wine importer
     */
    private String importer;

    /**
     * Wine alcohol grade;
     */
    private String alcoholGrade;

    /**
     * Wine classification
     */
    private String classification;

    /**
     * Wine aging
     */
    private String aging;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public List<Image> getTypeIcons() {
        return typeIcons;
    }

    public void setTypeIcons(List<Image> typeIcons) {
        this.typeIcons = typeIcons;
    }

    public String getRegionLabel() {
        return regionLabel;
    }

    public void setRegionLabel(String regionLabel) {
        this.regionLabel = regionLabel;
    }

    public String getRegionDescription() {
        return regionDescription;
    }

    public void setRegionDescription(String regionDescription) {
        this.regionDescription = regionDescription;
    }

    public String getGrape() {
        return grape;
    }

    public void setGrape(String grape) {
        this.grape = grape;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

    public String getAlcoholGrade() {
        return alcoholGrade;
    }

    public void setAlcoholGrade(String alcoholGrade) {
        this.alcoholGrade = alcoholGrade;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getAging() {
        return aging;
    }

    public void setAging(String aging) {
        this.aging = aging;
    }

}
