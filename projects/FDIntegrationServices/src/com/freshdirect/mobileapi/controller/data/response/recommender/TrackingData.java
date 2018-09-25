package com.freshdirect.mobileapi.controller.data.response.recommender;

import java.io.Serializable;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.Cohort;
import com.freshdirect.smartstore.Variant;

public class TrackingData implements Serializable {

    private static final long serialVersionUID = 7743273378536504789L;

    /**
     * The primary identifier of the location
     * where product recommendation was requested
     *
     * @see EnumSiteFeature
     */
    private String siteFeature;

    /**
     * ID of variant that actually delivered
     * recommendation
     *
     * @see Variant
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
