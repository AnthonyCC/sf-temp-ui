package com.freshdirect.mobileapi.controller.data.response.recommender;

import java.io.Serializable;

import com.freshdirect.smartstore.Cohort;

public class RecommendationTrackingData implements Serializable {

    private static final long serialVersionUID = 7743273378536504789L;

    /**
     * The primary identifier of the location
     * where product recommendation was requested
     *
     * @see com.freshdirect.fdstore.util.EnumSiteFeature#getName()
     */
    private String siteFeature;

    /**
     * ID of variant that actually delivered
     * recommendation
     *
     * @see com.freshdirect.smartstore.Variant#getId()
     */
    private String variant;

    /**
     * Customer Cohort identifier
     *
     * @see Cohort
     */
    private String cohortName;


    public String getSiteFeature() {
        return siteFeature;
    }


    public void setSiteFeature(String siteFeature) {
        this.siteFeature = siteFeature;
    }


    public String getVariant() {
        return variant;
    }


    public void setVariant(String variant) {
        this.variant = variant;
    }


    public String getCohortName() {
        return cohortName;
    }


    public void setCohortName(String cohortName) {
        this.cohortName = cohortName;
    }


}
