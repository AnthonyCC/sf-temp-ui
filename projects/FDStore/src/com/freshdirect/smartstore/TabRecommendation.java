package com.freshdirect.smartstore;

import java.util.List;

import com.freshdirect.fdstore.util.EnumSiteFeature;

public class TabRecommendation {
    public static final String PIP_DEFAULT_DESC = "These are some of the items we recommend you:";

    final List variants;
    final RecommendationService tabRecommender;
    String parentImpressionId;
    String[] featureImpId;
    int selected;

    public TabRecommendation(RecommendationService tabRecommender, List variants) {
        this.tabRecommender = tabRecommender;
        this.variants = variants;
        this.featureImpId = new String[variants.size()];
    }

    public int size() {
        return variants.size();
    }
    
    public void setFeatureImpressionId(int pos, String featureImpId) {
        this.featureImpId[pos] = featureImpId;
    }
    
    public String getFeatureImpressionId(int pos) {
        return featureImpId[pos];
    }

    public Variant get(int index) {
        return (Variant) variants.get(index);
    }

    public String getTabTitle(int index) {
        Variant variant = get(index);

        String varPrezTitle = variant.getServiceConfig().getPresentationTitle();
        if (varPrezTitle != null) {
            return varPrezTitle;
        }

        EnumSiteFeature siteFeature = variant.getSiteFeature();
        String sfPrezTitle = siteFeature.getPresentationTitle();
        if (sfPrezTitle != null) {
            return sfPrezTitle;
        }

        String sfTitle = siteFeature.getTitle();
        if (sfTitle != null) {
            return sfTitle;
        } else {
            return siteFeature.getName();
        }
    }

    public String getTabDescription(int index) {
        Variant variant = get(index);

        String varPrezDescription = variant.getServiceConfig().getPresentationDescription();
        if (varPrezDescription != null) {
            return varPrezDescription;
        }

        return PIP_DEFAULT_DESC;
    }

    public RecommendationService getTabRecommender() {
        return tabRecommender;
    }
    
    public String getParentImpressionId() {
        return parentImpressionId;
    }
    
    public void setParentImpressionId(String parentImpressionId) {
        this.parentImpressionId = parentImpressionId;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
    
    public int getSelected() {
        return selected;
    }
    
}
